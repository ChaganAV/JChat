import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private final Socket socket;
    private final String name;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public Client(Socket socket, String name) {
        this.socket = socket;
        this.name = name;
        try{
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }catch (IOException e){
            closeClient(socket,bufferedReader,bufferedWriter);
        }
    }

    public void sendMessage(){
        try{
            bufferedWriter.write(name); // наше имя отправим
            bufferedWriter.newLine();
            bufferedWriter.flush(); // отправим

            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()){
                String message = scanner.nextLine();
                bufferedWriter.write(name +": " + message);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }catch (IOException e){
            closeClient(socket,bufferedReader,bufferedWriter);
        }
    }
    /**
     * Слушатель входящих сообщений
     */
    public void listenMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String message;
                while (socket.isConnected()){
                   try{
                       message = bufferedReader.readLine();
                       System.out.println(message);
                   }catch (IOException e){
                       closeClient(socket,bufferedReader,bufferedWriter);
                   }
                }
            }
        }).start();
    }

    /**
     * Закроем клиента и освободим ресурсы reader/writer
     * @param socket
     * @param bufferedReader
     * @param bufferedWriter
     */
    private void closeClient(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try{
            if(bufferedReader != null){
                bufferedReader.close();
            }
            if(bufferedWriter != null){
                bufferedWriter.close();
            }
            if(socket != null){
                socket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("Закрыли socket и освободили reader/writer");
    }
}
