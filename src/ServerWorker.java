import java.io.*;
import java.net.Socket;

class ServerWorker implements Runnable {
    private Socket socket;
    private UserList users;

    public ServerWorker(Socket socket, UserList users)
    {
        this.socket = socket;
        this.users = users;
    }

    @Override
    public void run()
    {
        try
        {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(new BufferedInputStream(this.socket.getInputStream()));
            boolean isOpen = true;
            int query;

            while (isOpen)
            {
                try
                {
                    query = in.readInt();
                    switch (query)
                    {
                        case 11: // regista
                            Options.receiveRegista(in,out,this.users);
                            break;
                        case 12: // login
                            Options.receiveLogin(in,out,this.users);
                            break;
                        case 21:  // nrDePessoasPorLocal
                            Options.receiveOption1(in,out,this.users);
                            break;
                        case 22: // atualizaLocalizacao
                            Options.receiveOption2(in,out,this.users);
                            break;
                        case 23: // haAlguem
                            Options.receiveOption3(in,out,this.users);
                            break;
                        case 24: // touInfetado
                            Options.receiveOption4(in,out,this.users);
                            break;
                        case 25: // terminaSessao
                            Options.receiveOption5(in,out,this.users);
                            break;
                        default:
                    }
                }
                catch (EOFException e)
                {
                    isOpen = false;
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
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
