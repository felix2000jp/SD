import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Options {

    public static String sendLogin(String input, DataInputStream in, DataOutputStream out) throws IOException
    {
        out.writeInt(12);
        out.writeUTF(input);
        out.flush();
        return in.readUTF();
    }

    public static void sendRegistar(String input, DataInputStream in, DataOutputStream out) throws IOException
    {
        out.writeInt(11);
        User newUser = User.parseLine(input);
        newUser.serialize(out);
        out.flush();
        View.responde(in.readUTF());
    }

    public static void sendOption1(DataInputStream in, DataOutputStream out) throws IOException
    {
        String local = View.nrPessoasPorLocalCliente();
        out.writeInt(21);
        out.writeUTF(local);
        out.flush();
        View.nrPessoasPorLocalCliente(in.readInt());
    }

    public static void sendOption2(String eueueu, DataInputStream in, DataOutputStream out) throws IOException
    {
        String local = View.atualizaLocalizacaoCliente();
        out.writeInt(22);
        out.writeUTF(eueueu);
        out.writeUTF(local);
        out.flush();
        View.responde(in.readUTF());
    }

    public static void sendOption3(DataInputStream in, DataOutputStream out) throws IOException
    {
        String local = View.nrPessoasPorLocalCliente();
        out.writeInt(23);
        out.writeUTF(local);
        out.flush();
        View.responde(in.readUTF());
    }

    public static void sendOption4(String eueueu, DataInputStream in, DataOutputStream out) throws IOException
    {
        out.writeInt(24);
        out.writeUTF(eueueu);
        out.flush();
        View.responde(in.readUTF());
    }

    public static void sendOption5(String eueueu, DataInputStream in, DataOutputStream out) throws IOException
    {
        out.writeInt(25);
        out.writeUTF(eueueu);
        out.flush();
        View.responde(in.readUTF());
    }


    public static void receiveRegista(DataInputStream in, DataOutputStream out, UserList users) throws IOException
    {
        boolean b = users.addUser(in);
        String resposta = View.registoServidor(b);
        out.writeUTF(resposta);
        out.flush();
    }

    public static void receiveLogin(DataInputStream in, DataOutputStream out, UserList users) throws IOException
    {
        Integer cond = users.login(in);
        String resposta = View.loginServidor(cond);
        out.writeUTF(resposta);
        out.flush();
    }

    public static void receiveOption1(DataInputStream in, DataOutputStream out, UserList users) throws IOException
    {
        Integer cond = users.numeroLocal(in);
        out.writeInt(cond);
        out.flush();
    }

    public static void receiveOption2(DataInputStream in, DataOutputStream out, UserList users) throws IOException
    {
        users.atualizaLocalizacao(in);
        String resposta = View.atualizaServidor();
        out.writeUTF(resposta);
        out.flush();
    }

    public static void receiveOption3(DataInputStream in, DataOutputStream out, UserList users) throws IOException, InterruptedException
    {
        String local = users.possoIr(in);
        String resposta = View.haPessoasServidor(local);
        out.writeUTF(resposta);
        out.flush();
    }

    public static void receiveOption4(DataInputStream in, DataOutputStream out, UserList users) throws IOException
    {
        users.infetado(in);
        String resposta = View.infetadoServidor();
        out.writeUTF(resposta);
        out.flush();
    }

    public static void receiveOption5(DataInputStream in, DataOutputStream out, UserList users) throws IOException
    {
        users.terminaSessao(in);
        String resposta = View.terminarSessaoServidor();
        out.writeUTF(resposta);
        out.flush();
    }
}
