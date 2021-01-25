import java.io.DataInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class UserList
{
    private Map<String, User> users;
    private Set<String> usersLogin;
    private Set<String> doentes;
    private Map<Integer,List<Integer>> pedidos;
    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public UserList()
    {
        this.users = new HashMap<>();
        this.doentes = new HashSet<>();
        this.usersLogin = new HashSet<>();
        this.pedidos = new HashMap<>();
    }

    public boolean addUser(DataInputStream in) throws IOException
    {
        try
        {
            User user = User.deserialize(in);

            lock.lock();
            boolean b = !this.users.containsKey(user.getNome());
            if (b) this.users.put(user.getNome(), user);

            return b;
        }
        finally
        {
            lock.unlock();
        }
    }

    public Integer numeroLocal(DataInputStream in) throws IOException
    {
        int n = 0;
        String local = in.readUTF();
        String[] tokens = local.split(" ");
        int localizacaoX = Integer.parseInt(tokens[0]);
        int localizacaoY = Integer.parseInt(tokens[1]);

        //lock.lock();

        /*
        for(User user : this.users.values())
        {
            user.rw.readLock().lock();
        }
        */

        //lock.unlock();
        lock.lock();
        for(User user : this.users.values())
        {
            if(user.getLocalizacaoX() == localizacaoX && user.getLocalizacaoY() == localizacaoY) n++;
           // user.rw.readLock().unlock();
        }
        lock.unlock();
        return n;
    }

    public Integer login(DataInputStream in) throws IOException
    {
        try
        {
            lock.lock();

            String login = in.readUTF();
            String[] tokens = login.split(" ");
            String nome = tokens[0];
            String pass = tokens[1];
            Integer msg = 0;
            User u;

            if((u = this.users.get(nome)) == null || !u.getPassword().equals(pass)) msg = 1;
            else if( this.doentes.contains(nome) ) msg = 2;
            else if( !this.usersLogin.add(nome) ) msg = 3;
            return msg;
        }
        finally
        {
            lock.unlock();
        }
    }

    public Integer terminaSessao(DataInputStream in) throws IOException
    {
        try
        {
            String nome = in.readUTF();

            lock.lock();
            this.usersLogin.remove(nome);
            return 0;
        }
        finally
        {
            lock.unlock();
        }
    }

    public Integer infetado(DataInputStream in) throws IOException
    {
        try
        {
            String nome = in.readUTF();

            lock.lock();
            this.doentes.add(nome);
            return 0;
        }
        finally
        {
            lock.unlock();
        }
    }

    public void atualizaLocalizacao(DataInputStream in) throws IOException, InterruptedException
    {
        User u;

        //lock.lock();
        String nome = in.readUTF();
        String local = in.readUTF();
        String[] tokens = local.split(" ");
        Integer localizacaoX = Integer.parseInt(tokens[0]);
        Integer localizacaoY = Integer.parseInt(tokens[1]);

        lock.lock();
        u = this.users.get(nome);

        // u.rw.writeLock().lock();

        //lock.unlock();
        //Thread.sleep(10000);

        u.setLocalizacaoX(localizacaoX);
        u.setLocalizacaoY(localizacaoY);
        lock.unlock();

        lock.lock();
        if( this.pedidos.containsKey(localizacaoX) )
        {
            if( !this.pedidos.get(localizacaoX).contains(localizacaoY) ) condition.signal();
        }
        else condition.signal();
        lock.unlock();


        //u.rw.writeLock().unlock();

    }

    public Boolean possoIr(DataInputStream in) throws IOException, InterruptedException
    {
        Boolean b = false;
        String local = in.readUTF();
        String[] tokens = local.split(" ");
        Integer localizacaoX = Integer.parseInt(tokens[0]);
        Integer localizacaoY = Integer.parseInt(tokens[1]);

        lock.lock();
        for(User user : this.users.values())
        {
            if (user.getLocalizacaoX() == localizacaoX && user.getLocalizacaoY() == localizacaoY) {
                b = true;
                break;
            }
        }
        lock.unlock();

        lock.lock();
        while(b)
        {
            List<Integer> ls = new ArrayList<>();
            ls.add(localizacaoY);
            if(this.pedidos.containsKey(localizacaoX))
            {
                if (!this.pedidos.get(localizacaoX).isEmpty())
                    ls.addAll(this.pedidos.get(localizacaoX));
            }

            this.pedidos.put(localizacaoX,ls);

            this.condition.await();
            b = false;

            ls.remove(localizacaoY);
            if(!ls.isEmpty()) this.pedidos.put(localizacaoX, ls);
        }
        lock.unlock();
        return b;
    }


}
