import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Teste {

    public static void teste2(String eueueu, String local, DataOutputStream out) throws IOException {
        out.writeInt(22);
        out.writeUTF(eueueu);
        out.writeUTF(local);
        out.flush();
        View.responde("Alterou");
    }

    public static void teste3(String local, DataInputStream in, DataOutputStream out) throws IOException {

        out.writeInt(23);
        out.writeUTF(local);
        out.flush();
        View.responde(in.readUTF());
    }
}
