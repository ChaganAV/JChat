import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        try{
            Scanner scanner = new Scanner(System.in);
            System.out.println("Введите имя");
            String name = scanner.nextLine();
            InetAddress address = InetAddress.getLocalHost();
            Socket socket = new Socket(address,1400);
            Client client = new Client(socket,name);
            System.out.println("InetAddress: " + address);
            System.out.println("Remote IP: " + address.getHostAddress());
            System.out.println("LocalPort: " + socket.getLocalPort());

            client.listenMessage();
            client.sendMessage();
        }catch (UnknownHostException ehost){
            ehost.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
