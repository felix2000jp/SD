import java.io.*;
import java.net.Socket;

public class Client
{

    public static User parseLine (String userInput)
    {
        String[] tokens = userInput.split(" ");

            if (tokens[2].equals("null")) tokens[2] = null;
        return new User(tokens[0], tokens[1], Integer.parseInt(tokens[2]));
    }


    public static void main (String[] args) throws IOException
    {
        Socket socket = new Socket("localhost", 12345);
        DataInputStream in = new DataInputStream( new BufferedInputStream(socket.getInputStream()) );
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        BufferedReader line = new BufferedReader(new InputStreamReader(System.in));
        String input;
        String eueueu = "";

        int userInput;

        int login = -1;
        while (login == -1) {
            userInput = Integer.parseInt(line.readLine());

            if(userInput == 1 || userInput == 2) {
                out.writeInt(userInput);
                out.flush();

                switch (userInput) {
                    case 1:
                        System.out.println(in.readUTF());
                        if ((input = line.readLine()) != null) {
                            User newUser = parseLine(input);
                            newUser.serialize(out);
                            out.flush();
                            System.out.println(in.readUTF());
                        }
                        break;

                    case 2:
                        System.out.println(in.readUTF());
                        if ((input = line.readLine()) != null) {
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
                    default:
                }
            }
        }




        while ( (userInput = Integer.parseInt(line.readLine()) ) != 0)
        {
            out.writeInt(userInput);
            out.flush();

            switch (userInput)
            {
                case 3:
                    System.out.println(in.readUTF());
                    if( (input = line.readLine()) != null )
                    {
                        int local = Integer.parseInt(input);
                        out.writeInt(local);
                        out.flush();
                        System.out.println("Numero de Utilizadores: " + in.readInt());
                    }
                    break;
                case 4:
                    System.out.println(in.readUTF());
                    if( (input = line.readLine()) != null )
                    {
                        out.writeUTF(eueueu);
                        out.writeInt(Integer.parseInt(input));
                        out.flush();
                        System.out.println("Alterou");
                    }
                    break;
                case 5:
                    System.out.println(in.readUTF());
                    if( (input = line.readLine()) != null )
                    {
                        int local = Integer.parseInt(input);
                        out.writeInt(local);
                        out.flush();
                        System.out.println(in.readUTF());
                    }
                    break;

                default:
            }
        }

        socket.close();
    }
}
