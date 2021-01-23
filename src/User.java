import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class User
{
    private String nome;
    private String password;
    private Integer localizacaoX;
    private Integer localizacaoY;
    ReentrantReadWriteLock rw = new ReentrantReadWriteLock();

    User(User user)
    {
        this.nome = user.getNome();
        this.password = user.getPassword();
        this.localizacaoX = user.getLocalizacaoX();
        this.localizacaoY = user.getLocalizacaoY();
    }

    public User()
    {
        this.nome = "";
        this.password = "";
        this.localizacaoX = -1;
        this.localizacaoY = -1;
    }

    public User(String nome, String password, Integer localizacaoX, Integer localizacaoY)
    {
        this.nome = nome;
        this.password = password;
        this.localizacaoX = localizacaoX;
        this.localizacaoY = localizacaoY;
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

    public Integer getLocalizacaoX()
    {
        return this.localizacaoX;
    }

    public Integer getLocalizacaoY()
    {
        return this.localizacaoY;
    }

    public void setLocalizacaoX(Integer localizacao)
    {
        this.localizacaoX = localizacao;
    }

    public void setLocalizacaoY(Integer localizacao)
    {
        this.localizacaoY = localizacao;
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
                "Localizacao: " + this.localizacaoX + ":" + this.localizacaoY ;
    }

    public static User deserialize(DataInputStream in) throws IOException
    {
        String nome = in.readUTF();
        String password = in.readUTF();
        Integer localizacaoX = in.readInt();
        Integer localizacaoY = in.readInt();

        return new User(nome, password, localizacaoX, localizacaoY);
    }

    public void serialize(DataOutputStream out) throws IOException
    {
        out.writeUTF(this.nome);
        out.writeUTF(this.password);
        out.writeInt(this.localizacaoX);
        out.writeInt(this.localizacaoY);

        out.flush();
    }
}
