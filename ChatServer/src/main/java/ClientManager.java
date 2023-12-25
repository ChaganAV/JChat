import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientManager implements Runnable {
    private final Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private String name;

    public final static ArrayList<ClientManager> clients = new ArrayList<>();

    public ClientManager(Socket socket) {
        this.socket = socket;
        try{
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            name = bufferedReader.readLine();
            clients.add(this);
            String message = " подключился к чату.";
            System.out.println(name + message);
            broadcastMessage("Server: " + name + message);

        }catch (IOException e){

        }
    }

    @Override
    public void run() {
        String message;
        while (socket.isConnected()){
            try{
                message = bufferedReader.readLine();
                if(message == null){
                    closeSocket(socket,bufferedReader,bufferedWriter);
                    break;
                }
                broadcastMessage(message);
            }catch (IOException e){
                closeSocket(socket,bufferedReader,bufferedWriter);
                break;
            }
        }
    }

    public void closeSocket(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        removeClient();
        try{
            if(bufferedWriter != null){
                bufferedWriter.close();
            }
            if(bufferedReader != null){
                bufferedReader.close();
            }
            if(socket != null){
                socket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void removeClient() {
        clients.remove(this);
        System.out.println(name + " покинул чат.");
        broadcastMessage("Server: " + name + " покинул чат.");
    }

    private void broadcastMessage(String message) {
        for (ClientManager client: clients) {
            try{
                if(!client.name.equals(name)){
                    client.bufferedWriter.write(message);
                    client.bufferedWriter.newLine();
                    client.bufferedWriter.flush();
                }
            }catch (IOException e){
                closeSocket(socket,bufferedReader,bufferedWriter);
            }
        }
    }
}
