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

        lock.lock();

        for(User user : this.users.values())
        {
            user.rw.readLock().lock();
        }


        lock.unlock();
        for(User user : this.users.values())
        {
            if(user.getLocalizacaoX() == localizacaoX && user.getLocalizacaoY() == localizacaoY) n++;
            user.rw.readLock().unlock();
        }

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

        String nome = in.readUTF();
        String local = in.readUTF();
        String[] tokens = local.split(" ");
        Integer localizacaoX = Integer.parseInt(tokens[0]);
        Integer localizacaoY = Integer.parseInt(tokens[1]);

        lock.lock();
        u = this.users.get(nome);

        u.rw.writeLock().lock();
        System.out.println("oy");
        condition.signalAll();
        System.out.println("oyy");
        lock.unlock();

        u.setLocalizacaoX(localizacaoX);
        u.setLocalizacaoY(localizacaoY);

        u.rw.writeLock().unlock();
    }

    public boolean possoIr(DataInputStream in) throws IOException, InterruptedException
    {
        boolean b = false;
        String local = in.readUTF();
        String[] tokens = local.split(" ");
        Integer localizacaoX = Integer.parseInt(tokens[0]);
        Integer localizacaoY = Integer.parseInt(tokens[1]);


        lock.lock();
        while (!b) {
            b = true;

            System.out.println("yo");

            for (User user : this.users.values()) {
                if (user.getLocalizacaoX() == localizacaoX && user.getLocalizacaoY() == localizacaoY) {
                    b = false;
                }
            }

            System.out.println("yoo");
            if(!b) {
                condition.await();
            }

            System.out.println("yooo");
        }
        lock.unlock();

        System.out.println("yoooo");


        return b;
    }


}
