public class User
{
    private String nome;
    private String password;
    private Localizacao localizacao;

    User(User user)
    {
        this.nome = user.getNome();
        this.password = user.getPassword();
        this.localizacao = user.localizacao;
    }

    public User()
    {
        this.nome = "";
        this.password = "";
        this.localizacao = new Localizacao();
    }

    public User(String nome, String password, Localizacao localizacao)
    {
        this.nome = nome;
        this.password = password;
        this.localizacao = localizacao;
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

    public Localizacao getLocalizacao()
    {
        return this.localizacao;
    }

    public void setLocalizacao(Localizacao localizacao)
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
        return  "Nome: " + this.nome + "\n" +
                "Password: " + this.password + "\n" +
                this.localizacao;
    }
}
