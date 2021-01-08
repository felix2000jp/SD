import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

class ServerWorker implements Runnable {
    private Socket socket;
    private UserList users;

    public ServerWorker(Socket socket, UserList users) {
        this.socket = socket;
        this.users = new UserList();
    }

    @Override
    public void run() {

        try {
            DataInputStream in = new DataInputStream(new BufferedInputStream(this.socket.getInputStream()));
            boolean isOpen = true;

            while (isOpen) {
                try {
                    users.addUser(in);
                    for(User us : users.getUsers()) {System.out.println(us);}
                } catch (EOFException e) {
                    isOpen = false;
                }
            }
            // print do contactos
            socket.shutdownInput();
            socket.close();

        } catch (IOException e) {
            System.out.println("IO Exception");
        }

    }
}


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