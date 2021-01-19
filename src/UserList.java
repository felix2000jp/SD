import java.io.DataInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;


public class UserList
{
    private Map<String, User> users;
    private List<String> usersLogin;
    private ReentrantLock lock = new ReentrantLock();

    public UserList()
    {
        this.users = new HashMap<>();
        this.usersLogin = new ArrayList<>();
    }

    public Map<String, User> getUsers()
    {
        return this.users;
    }

    public boolean addUser(DataInputStream in) throws IOException
    {
        User user = User.deserialize(in);
        lock.lock();
        try
        {
            boolean b = !this.users.containsKey(user.getNome());
            if(b)
                this.users.put(user.getNome(), user);

            return b;
        }
        finally
        {
            lock.unlock();
        }
    }

    public Integer numeroLocal(DataInputStream in) throws IOException {
        int n = 0;
        int tatau = in.readInt();
        try {
            lock.lock();

            for(User user : this.users.values())
            {
                if(user.getLocalizacao() ==  tatau) n++;
            }

            return n;
        }finally {
            lock.unlock();
        }
    }

    public void printUser()
    {
        lock.lock();
        for(User user : this.users.values())
        {
            System.out.println(user);
        }
        lock.unlock();
    }

    public boolean login(String nome, String pass){
        boolean b = true;
        User u;
        try {
            lock.lock();
            if((u = this.users.get(nome)) == null || !u.getPassword().equals(pass)) {
                b = false;
            }
            else {
                this.usersLogin.add(nome);
            }
            return b;
        }finally {
            lock.unlock();
        }
    }

    public void atualizaLocalizacao(String nome, int localizacao){
        lock.lock();
        this.users.get(nome).setLocalizacao(localizacao);
        lock.unlock();
    }
}
