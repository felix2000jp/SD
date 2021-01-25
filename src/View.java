import java.io.*;
public class View {

    public static void responde(String resposta)
    {
        System.out.println(resposta);
    }

    public static int menu1 ()
    {
        BufferedReader line = new BufferedReader(new InputStreamReader(System.in));
        int n = -1;
        do {
            System.out.println("1 - Registar");
            System.out.println("2 - Login");
            System.out.println("3 - Sair");
            try {
                n = Integer.parseInt(line.readLine());
            } catch (IOException e) {
                System.out.println("Opcao nao valida");
                n = -1;
            }

            if(n < 1 || n > 3)
                System.out.println("Opcao nao valida");

        } while (n < 1 || n > 3);

        return n;
    }

    public static int menu2 ()
    {
        BufferedReader line = new BufferedReader(new InputStreamReader(System.in));
        int n = -1;
        do {
            System.out.println("1 - Saber o numero de pessoas numa localização");
            System.out.println("2 - Informar servidor da sua localização");
            System.out.println("3 - Saber o quando não há pessoas numa localização");
            System.out.println("4 - Comunicar ao servidor que está doente");
            System.out.println("5 - Sair");
            try {
                n = Integer.parseInt(line.readLine());
            } catch (IOException e) {
                System.out.println("Opcao nao valida");
                n = -1;
            }

            if(n < 1 || n > 5)
                System.out.println("Opcao nao valida");

        } while (n < 1 || n > 5);

        return n;
    }

    private static String geralMenu1(String formato) throws IOException
    {
        BufferedReader line = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Indique neste formato: \n" + formato);
        return line.readLine();
    }

    public static String registoCliente() throws IOException
    {
        String resposta = "";
        int cond = 0;

        while(cond == 0)
        {
            resposta = geralMenu1("nome password localizacaoX localizacaoY");
            String[] tokens = resposta.split(" ");
            Integer localX = Integer.parseInt(tokens[2]);
            Integer localY = Integer.parseInt(tokens[3]);

            if( (localX <= 10 && localX >= 1) && (localY <= 10 && localY >= 1) ) cond = 1;
            else System.out.println("Erro ao introduzir localizacao");
        }
        return resposta;
    }

    public static String loginCliente() throws IOException
    {
        return geralMenu1("nome password");
    }

    public static int loginClienteOut(String resposta)
    {
        Integer login = -1;
        System.out.println(resposta);
        if (resposta.equals("Login efetuado com sucesso")) login = 1;

        return login;
    }

    public static String loginClienteOut(String resposta ,String input)
    {
        String eueueu = "";
        if (resposta.equals("Login efetuado com sucesso")) eueueu = input.split(" ")[0];

        return eueueu;
    }


    private static String geralMenu2(String pergunta) throws IOException
    {
        BufferedReader line = new BufferedReader(new InputStreamReader(System.in));
        int cond = 0;
        String n = "";

        while(cond == 0)
        {
            System.out.println(pergunta);

            n = line.readLine();
            String[] tokens = n.split(" ");
            Integer localX = Integer.parseInt(tokens[0]);
            Integer localY = Integer.parseInt(tokens[1]);

            if( (localX <= 10 && localX >= 1) && (localY <= 10 && localY >= 1) ) cond = 1;
            else System.out.println("Erro ao introduzir localizacao");
        }

        return n;
    }

    public static String nrPessoasPorLocal() throws IOException
    {
        return geralMenu2("Que Localizacao pretende consultar?");
    }

    public static void nrPessoasPorLocalResponde(int resposta)
    {
        System.out.println("Numero de Utilizadores: " + resposta);
    }

    public static String atualizaLocalizacao() throws IOException
    {
        return geralMenu2("Qual a sua localizacao?");
    }


    // ServerWorker
    public static String registoServidor(Boolean bool)
    {
        if (bool)
            return "Adicionado com Sucesso";
        else
            return "Nome utilizador indisponivel";
    }

    public static String loginServidor(Integer cond)
    {
        if( cond == 0 ) return "Login efetuado com sucesso";
        if( cond == 1 ) return "Nome e/ou Password Incorreta";
        if( cond == 2 ) return "Utilizador infetado";
        else return "Já existe alguém logado nesta conta";

    }

    public static String haPessoasServidor(Boolean cond)
    {
        if(cond) return "Tem pessoas no local";
        else return "É seguro ir para o lugar";

    }

    public static String infetadoServidor()
    {
        return "R.I.P. \nPress f to pay respects";
    }

    public static String terminarSessaoServidor()
    {
        return "Goodbye, Adeus, Au revoir, Auf Wiedersehen";
    }

}
