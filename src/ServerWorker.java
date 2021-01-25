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

            int cond;
            boolean b;
            String resposta;

            while (isOpen)
            {
                try
                {
                    query = in.readInt();
                    switch (query)
                    {
                        case 11: // regista
                            b = this.users.addUser(in);
                            resposta = View.registoServidor(b);
                            out.writeUTF(resposta);
                            out.flush();
                            break;
                        case 12: // login
                            cond = users.login(in);
                            resposta = View.loginServidor(cond);
                            out.writeUTF(resposta);
                            out.flush();
                            break;
                        case 21:  // nrDePessoasPorLocal
                            cond = this.users.numeroLocal(in);
                            out.writeInt(cond);
                            out.flush();
                            break;
                        case 22: // atualizaLocalizacao
                            users.atualizaLocalizacao(in);
                            break;
                        case 23: // haAlguem
                            b = this.users.possoIr(in);
                            resposta = View.haPessoasServidor(b);
                            out.writeUTF(resposta);
                            out.flush();
                            break;
                        case 24: // touInfetado
                            this.users.infetado(in);
                            resposta = View.infetadoServidor();
                            out.writeUTF(resposta);
                            out.flush();
                            break;
                        case 25: // terminaSessao
                            this.users.terminaSessao(in);
                            resposta = View.terminarSessaoServidor();
                            out.writeUTF(resposta);
                            out.flush();
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
