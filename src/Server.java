import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main (String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(12345);
        UserList users = new UserList();

        while (true)
        {
            Socket socket = serverSocket.accept();
            Thread worker = new Thread(new ServerWorker(socket, users));
            worker.start();
        }
    }

}