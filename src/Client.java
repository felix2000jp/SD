import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
/*
public class Client {

    public static void main(String[] args)
    {
        try {
            Socket socket = new Socket("localhost", 12345);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            BufferedReader systemIn = new BufferedReader(new InputStreamReader(System.in));

            String userInput;
            while ((userInput = systemIn.readLine()) != null) {
                out.println(userInput);
                out.flush();

                String response = in.readLine();
                System.out.println("Server response: " + response);
            }

            socket.shutdownOutput();
            socket.shutdownInput();
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
 */

public class Client {

    public static User parseLine (String userInput) {
        String[] tokens = userInput.split(" ");

        if (tokens[2].equals("null")) tokens[2] = null;
        if (tokens[3].equals("null")) tokens[3] = null;

        return new User(tokens[0], tokens[1], Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]));
    }


    public static void main (String[] args) throws IOException
    {
        Socket socket = new Socket("localhost", 12345);
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        String userInput;
        while ((userInput = in.readLine()) != null) {
            User newUser = parseLine(userInput);
            newUser.serialize(out);
            System.out.println(newUser.toString());
        }

        socket.close();
    }
}