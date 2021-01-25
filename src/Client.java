import java.io.*;
import java.net.Socket;

public class Client
{

    private static User parseLine (String userInput)
    {
        String[] tokens = userInput.split(" ");
        if (tokens[2].equals("null")) tokens[2] = null;
        if (tokens[3].equals("null")) tokens[3] = null;

        return new User(tokens[0], tokens[1], Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]));
    }

    public static void main (String[] args) throws IOException
    {
        Socket socket = new Socket("localhost", 12345);
        DataInputStream in = new DataInputStream( new BufferedInputStream(socket.getInputStream()) );
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        Thread [] threads = new Thread[4];
        String input;
        String eueueu = "";
        String local;
        int userInput = -1;
        int cont  = 1;
        int login = -1;

        while (cont == 1)
        {
            while (login == -1 && cont == 1)
            {
                userInput = View.menu1();

                switch (userInput)
                {
                    case 1:
                        input = View.registoCliente();
                        if (input != null) {
                            out.writeInt(userInput + 10);
                            User newUser = parseLine(input);
                            newUser.serialize(out);
                            out.flush();

                            View.responde(in.readUTF());
                        }
                        break;

                    case 2:
                        input = View.loginCliente();
                        if (input != null)
                        {
                            out.writeInt(userInput + 10);
                            out.writeUTF(input);
                            out.flush();
                            String resposta = in.readUTF();
                            login = View.loginClienteOut(resposta);
                            eueueu = View.loginClienteOut(resposta,input);
                        }
                        break;
                    case 3:
                        cont = 0;
                    default:
                }
            }

            while (userInput != 0 && cont == 1)
            {
                userInput = View.menu2();

                switch (userInput) {
                    case 1: // Numero de Pessoas numa localizacao
                        local = View.nrPessoasPorLocal();

                        out.writeInt(userInput + 20);
                        out.writeUTF(local);
                        out.flush();
                        View.nrPessoasPorLocalResponde(in.readInt());
                        break;
                    case 2: // Atualizar localizacao
                        local = View.atualizaLocalizacao();

                        out.writeInt(userInput + 20);
                        out.writeUTF(eueueu);
                        out.writeUTF(local);
                        out.flush();
                        View.responde("Alterou");
                        break;
                    case 3: // Existem pessoa num local?
                        String locals = View.nrPessoasPorLocal();

                        threads[2] = new Thread(() -> {
                            try {
                                Teste.teste3(locals, in, out);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        threads[2].start();
                        break;
                    case 4: // Está Infetado
                        out.writeInt(userInput + 20);
                        out.writeUTF(eueueu);
                        View.responde(in.readUTF());

                        cont = -1;
                        break;
                    case 5: // Au revoir
                        out.writeInt(userInput + 20);
                        out.writeUTF(eueueu);
                        out.flush();
                        userInput = 0;
                        View.responde(in.readUTF());
                        break;

                    default:
                }
            }
            login = -1;
        }

        socket.close();
    }
}
