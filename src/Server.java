import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

class ServerWorker implements Runnable {
    private Socket socket;
    private UserList users;

    public ServerWorker(Socket socket, UserList users) {
        this.socket = socket;
        this.users = users;
    }

    @Override
    public void run() {

        try {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(new BufferedInputStream(this.socket.getInputStream()));
            boolean isOpen = true;

            int adiciona = 1;

            while (isOpen)
            {
                try
                {
                    if(in.readInt() == adiciona)
                    {
                        String msgAdiciona = "Indique neste formato: \n nome password localizacao";
                        out.writeUTF(msgAdiciona);
                        out.flush();

                        this.users.addUser(in);
                        int n = 0;
                        for(User user : users.getUsers())
                        {
                            n++;
                            System.out.println(user.toString());
                            System.out.println(n);
                            System.out.println("--------------");
                            System.out.println("\n");
                        }
                    }
                    else ;
                }
                catch (EOFException e)
                {
                    isOpen = false;
                }
            }

            socket.shutdownInput();
            socket.close();

        }
        catch (IOException e)
        {
            System.out.println("IO Exception");
        }

    }
}

public class Server {

    public static void main (String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(12345);
        UserList users = new UserList();

        Mapa mapa = new Mapa();
        mapa.printa();

        while (true)
        {
            Socket socket = serverSocket.accept();
            Thread worker = new Thread(new ServerWorker(socket, users));
            worker.start();
        }
    }

}