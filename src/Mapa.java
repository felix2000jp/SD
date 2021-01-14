import java.util.HashMap;
import java.util.Map;

public class Mapa
{
    Map< Integer, UserList> mapa;

    Mapa(Mapa mapa)
    {
        this.mapa = new HashMap<>( mapa.getMapa() );
    }

    public Mapa()
    {
        this.mapa = new HashMap<>();

        for(int i = 1; i <= 400; i++)
        {
            this.mapa.put(i,new UserList());
        }
    }

    public Mapa(Map<Integer, UserList> mapa)
    {
        this.mapa = mapa;
    }

    public Map<Integer, UserList> getMapa()
    {
        return this.mapa;
    }

    public void setMapa(Map<Integer, UserList> mapa)
    {
        this.mapa = mapa;
    }

    @Override
    public Mapa clone()
    {
        return new Mapa(this);
    }

    public void printa() {
        int n = 1;
        System.out.println("-------------------------------------------------------------------------------------------------------------------------");
        for(int i = 1; i <= 20; i++)
        {
            System.out.print("| ");

            for(int j = 1; j <= 20; j++)
            {
                String N = String.valueOf(n);
                if(N.length() == 1) N = "00" + N;
                if(N.length() == 2) N = "0" + N;

                System.out.print(N + " | ");
                n++;
            }
            System.out.println();
            System.out.println("-------------------------------------------------------------------------------------------------------------------------");
        }
    }
}
