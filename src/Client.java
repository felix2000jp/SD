import java.io.*;
import java.net.Socket;

public class Client
{
    public static void main (String[] args) throws IOException
    {
        Socket socket = new Socket("localhost", 12345);
        DataInputStream in = new DataInputStream( new BufferedInputStream(socket.getInputStream()) );
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        String input;           // Argumentos da Opção escolhida
        String eueueu = "";     // Utilizador Logado
        int userInput = -1;     // Opção a efetuar
        int cont  = 1;          // Para o client continuar a correr
        int login = -1;         // Para o utilizador estar logado

        while (cont == 1)
        {
            while (login == -1 && cont == 1)
            {
                userInput = View.menu1();
                switch (userInput)
                {
                    case 1: // Regista
                        input = View.registoClienteCliente();
                        if (input != null) Options.sendRegistar(input, in, out);
                        break;
                    case 2: // Login
                        input = View.loginClienteCliente();
                        if (input != null)
                        {
                            String resposta = Options.sendLogin(input,in,out);
                            login = View.loginClienteServer(resposta);
                            eueueu = View.loginClienteServer(resposta,input);
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
                        Options.sendOption1(in,out);
                        break;
                    case 2: // Atualizar localizacao
                        Options.sendOption2(eueueu,in,out);
                        break;
                    case 3: // Existem pessoa num local?
                        Options.sendOption3(in,out);
                        break;
                    case 4: // Está Infetado
                        Options.sendOption4(eueueu,in,out);
                        userInput = 0;
                        break;
                    case 5: // Au revoir
                        Options.sendOption5(eueueu,in,out);
                        userInput = 0;
                        break;
                    default:
                }
            }
            login = -1;
        }
        socket.shutdownInput();
        socket.close();
    }
}
