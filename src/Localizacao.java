public class Localizacao
{
    private Integer x;
    private Integer y;

    Localizacao(Localizacao local)
    {
        this.x = local.getX();
        this.y = local.getY();
    }

    public Localizacao()
    {
        this.x = -1;
        this.y = -1;
    }

    public Localizacao(Integer x, Integer y)
    {
        this.x = x;
        this.y = y;
    }

    public Integer getX()
    {
        return this.x;
    }

    public void setX(Integer x)
    {
        this.x = x;
    }

    public Integer getY()
    {
        return this.y;
    }

    public void setY(Integer y)
    {
        this.y = y;
    }

    @Override
    protected Object clone()
    {
        return new Localizacao(this);
    }

    @Override
    public String toString()
    {
        return  "Coordenada X: " + this.x + "\n" +
                "Coordenada Y: " + this.y ;
    }
}
