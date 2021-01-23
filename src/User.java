import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class User
{
    private String nome;
    private String password;
    private Integer localizacao;
    ReentrantReadWriteLock rw = new ReentrantReadWriteLock();

    User(User user)
    {
        this.nome = user.getNome();
        this.password = user.getPassword();
        this.localizacao = user.getLocalizacao();
    }

    public User()
    {
        this.nome = "";
        this.password = "";
        this.localizacao = null;
    }

    public User(String nome, String password, Integer x)
    {
        this.nome = nome;
        this.password = password;
        this.localizacao = x;
    }

    public String getNome()
    {
        return this.nome;
    }

    public void setNome(String nome)
    {
        this.nome = nome;
    }

    public String getPassword()
    {
        return this.password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public Integer getLocalizacao()
    {
        return this.localizacao;
    }

    public void setLocalizacao(Integer localizacao)
    {
        this.localizacao = localizacao;
    }

    @Override
    public User clone()
    {
        return new User(this);
    }

    @Override
    public String toString()
    {
        return  "Nome:        " + this.nome + "\n" +
                "Password:    " + this.password + "\n" +
                "Localizacao: " + this.localizacao;
    }

    public static User deserialize(DataInputStream in) throws IOException
    {
        String nome = in.readUTF();
        String password = in.readUTF();
        Integer localizacao = in.readInt();

        return new User(nome, password, localizacao);
    }

    public void serialize(DataOutputStream out) throws IOException
    {
        out.writeUTF(this.nome);
        out.writeUTF(this.password);
        out.writeInt(this.localizacao);

        out.flush();
    }
}
