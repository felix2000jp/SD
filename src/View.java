import java.io.*;
public class View {

    View(){}

    public int menu1 () {
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

    public int menu2 () {
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

    private String geralMenu1(String formato) throws IOException {
        BufferedReader line = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Indique neste formato: \n" + formato);
        return line.readLine();
    }

    public String registo() throws IOException {
        return geralMenu1("nome password localizacao");
    }

    public String login() throws IOException {
        return geralMenu1("nome password");
    }

    private int geralMenu2(String pergunta){
        BufferedReader line = new BufferedReader(new InputStreamReader(System.in));
        int n;
        do{
            try {
                System.out.println(pergunta);
                n = Integer.parseInt(line.readLine());
            }catch (IOException e){
                System.out.println("Erro ao introduzir localizacao");
                n = -1;
            }
        } while (n == -1);

        return n;
    }

    public int nrPessoasPorLocal() {
        return geralMenu2("Que Localizacao pretende consultar?");
    }

    public int atualizaLocalizacao() {
        return geralMenu2("Qual a sua localizacao?");
    }

}
