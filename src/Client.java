import java.io.*;
import java.net.Socket;

public class Client
{

    private static User parseLine (String userInput)
    {
        String[] tokens = userInput.split(" ");

            if (tokens[2].equals("null")) tokens[2] = null;
        return new User(tokens[0], tokens[1], Integer.parseInt(tokens[2]));
    }


    public static void main (String[] args) throws IOException
    {
        View view = new View();
        Socket socket = new Socket("localhost", 12345);
        DataInputStream in = new DataInputStream( new BufferedInputStream(socket.getInputStream()) );
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        BufferedReader line = new BufferedReader(new InputStreamReader(System.in));
        String input;
        String eueueu = "";

        int userInput = -1;
        int cont  = 1;
        int login = -1;

        while (cont == 1)
        {
            while (login == -1 && cont == 1)
            {
                userInput = view.menu1();

                switch (userInput) {
                    case 1:
                        input = view.registo();
                        if (input != null) {
                            out.writeInt(userInput + 10);
                            User newUser = parseLine(input);
                            newUser.serialize(out);
                            out.flush();
                            System.out.println(in.readUTF());
                        }
                        break;

                    case 2:
                        input = view.login();
                        if (input != null) {
                            out.writeInt(userInput + 10);

                            String[] tokens = input.split(" ");
                            out.writeUTF(tokens[0]);
                            out.writeUTF(tokens[1]);
                            out.flush();
                            String resposta = in.readUTF();
                            System.out.println(resposta);
                            if (resposta.equals("Login efetuado com sucesso")) {
                                login = 1;
                                eueueu = tokens[0];
                            }
                        }
                        break;
                    case 3:
                        cont = 0;
                    default:
                }
            }

            while (userInput != 0 && cont == 1)
            {
                userInput = view.menu2();

                switch (userInput) {
                    case 1:
                        int local = view.nrPessoasPorLocal();
                        out.writeInt(userInput + 20);

                        out.writeInt(local);
                        out.flush();
                        System.out.println("Numero de Utilizadores: " + in.readInt());
                        break;
                    case 2:
                        local = view.atualizaLocalizacao();
                        out.writeInt(userInput + 20);

                        out.writeUTF(eueueu);
                        out.writeInt(local);
                        out.flush();
                        System.out.println("Alterou");
                        break;
                    case 3:
                        local = view.nrPessoasPorLocal();
                        out.writeInt(userInput + 20);

                        out.writeInt(local);
                        out.flush();
                        System.out.println(in.readUTF());
                        break;
                    case 4:
                        out.writeInt(userInput + 20);

                        out.writeUTF(eueueu);
                        System.out.println(in.readUTF());
                        cont = -1;
                        break;
                    case 5:
                        out.writeInt(userInput + 20);

                        out.writeUTF(eueueu);
                        out.flush();
                        userInput = 0;
                        System.out.println(in.readUTF());
                        break;

                    default:
                }
            }
            login = -1;
        }

        socket.close();
    }
}
