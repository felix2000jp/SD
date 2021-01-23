import java.io.*;
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

            int query;
            int n;
            boolean b;
            String msgAdiciona;

            while (isOpen)
            {
                try
                {
                    query = in.readInt();
                    switch (query){
                        // regista
                        case 11:
                            b = this.users.addUser(in);
                            if (b)
                                msgAdiciona = "Adicionado com Sucesso";
                            else
                                msgAdiciona = "Nome utilizador indisponivel";
                            out.writeUTF(msgAdiciona);
                            out.flush();
                            break;
                        // login
                        case 12:
                            int cond = users.login(in.readUTF(), in.readUTF());

                            if( cond == 0 )
                            {
                                msgAdiciona = "Login efetuado com sucesso";
                            }
                            else
                            {
                                if( cond == 1 ) msgAdiciona = "Nome e/ou Password Incorreta";
                                else if( cond == 2 ) msgAdiciona = "Utilizador infetado";
                                else msgAdiciona = "Já existe alguém logado nesta conta";
                            }
                            out.writeUTF(msgAdiciona);
                            out.flush();
                            break;

                        // nrDePessoasPorLocal
                        case 21:
                            n = this.users.numeroLocal(in);
                            out.writeInt(n);
                            out.flush();
                            System.out.println("Numero de Utilizadores: " + n);
                            break;
                        // atualizaLocalizacao
                        case 22:
                            String nome = in.readUTF();
                            int localizacao = in.readInt();
                            users.atualizaLocalizacao(nome, localizacao);
                            break;
                        // haAlguem
                        case 23:
                            n = this.users.numeroLocal(in);
                            String resposta;
                            if(n > 0)
                                resposta = "Tem pessoas no local";
                            else
                                resposta = "É seguro ir para o lugar";
                            out.writeUTF(resposta);
                            out.flush();
                            break;
                        // touInfetado
                        case 24:
                            this.users.infetado(in.readUTF());
                            resposta = "R.I.P. \nPress f to pay respects";
                            out.writeUTF(resposta);
                            out.flush();
                            break;
                        // terminaSessao
                        case 25:
                            this.users.terminaSessao(in.readUTF());
                            resposta = "Goodbye, Adeus, Au revoir, Auf Wiedersehen";
                            out.writeUTF(resposta);
                            out.flush();
                            break;
                        default:
                    }
                }
                catch (EOFException e)
                {
                    isOpen = false;
                } catch (InterruptedException e) {
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
