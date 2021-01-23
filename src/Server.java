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

            int query                  = -1;
            int regista                = 1;
            int login                  = 2;
            int nrDePessoasPorLocal    = 3;
            int atualizaLocalizacao    = 4;
            int haAlguem               = 5;
            int touInfetado            = 6;
            int terminaSessao          = 7;

            boolean b;

            while (isOpen)
            {
                try
                {
                    query = in.readInt();

                    if(query == regista)
                    {
                        String msgAdiciona = "Indique neste formato: \nnome password localizacao";
                        out.writeUTF(msgAdiciona);
                        out.flush();
                        b = this.users.addUser(in);
                        if (b)
                            msgAdiciona = "Adicionado com Sucesso";
                        else
                            msgAdiciona = "Nome utilizador indisponivel";
                        out.writeUTF(msgAdiciona);
                        out.flush();

                        int n = 0;
                        for(User user : users.getUsers().values())
                        {
                            n++;
                            System.out.println(user.toString());
                            System.out.println(n);
                            System.out.println("--------------");
                            System.out.println("\n");
                        }
                    }

                    if(query == login)
                    {
                        String msgAdiciona = "Indique neste formato: \nnome password";
                        out.writeUTF(msgAdiciona);
                        out.flush();
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
                    }

                    if(query == nrDePessoasPorLocal)
                    {
                        String msgLista = "Que Localizacao pretende consultar?";
                        out.writeUTF(msgLista);
                        out.flush();
                        int n = this.users.numeroLocal(in);
                        out.writeInt(n);
                        out.flush();
                        System.out.println("Numero de Utilizadores: " + n);
                    }

                    if(query == atualizaLocalizacao)
                    {
                        String msgLista = "Qual a sua localizacao?";
                        out.writeUTF(msgLista);
                        out.flush();
                        String nome = in.readUTF();
                        int localizacao = in.readInt();
                        users.atualizaLocalizacao(nome, localizacao);
                    }

                    if(query == haAlguem)
                    {
                        String msgLista = "Que Localizacao pretende consultar?";
                        out.writeUTF(msgLista);
                        out.flush();
                        int n = this.users.numeroLocal(in);
                        String resposta;
                        if(n > 0)
                            resposta = "Tem pessoas no local";
                        else
                            resposta = "É seguro ir para o lugar";
                        out.writeUTF(resposta);
                        out.flush();
                    }

                    if(query == terminaSessao)
                    {
                        this.users.terminaSessao(in.readUTF());
                        String resposta = "Goodbye, Adeus, Au revoir, Auf Wiedersehen";
                        out.writeUTF(resposta);
                        out.flush();
                    }

                    if(query == touInfetado)
                    {
                        this.users.infetado(in.readUTF());
                        String resposta = "R.I.P. \nPress f to pay respects";
                        out.writeUTF(resposta);
                        out.flush();
                    }
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

        while (true)
        {
            Socket socket = serverSocket.accept();
            Thread worker = new Thread(new ServerWorker(socket, users));
            worker.start();
        }
    }

}