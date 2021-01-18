import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

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

        int userInput;
        while ( (userInput = Integer.parseInt(line.readLine()) ) != 0)
        {
            out.writeInt(userInput);
            out.flush();

            switch (userInput)
            {
                case 1:
                    System.out.println(in.readUTF());
                    if( (input = line.readLine()) != null )
                    {
                        User newUser = parseLine(input);
                        newUser.serialize(out);
                        out.flush();
                        System.out.println(in.readUTF());
                    }
                    break;

                case 3:
                    System.out.println(in.readUTF());
                    if( (input = line.readLine()) != null )
                    {
                        Integer local = Integer.parseInt(input);
                        out.writeInt(local);
                        out.flush();
                        System.out.println("Numero de Utilizadores: " + in.readInt());
                    }
                    break;

                default:
            }
        }

        socket.close();
    }
}
