import java.io.DataInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;


public class UserList
{
    private Map<String, User> users;
    private Set<String> usersLogin;
    private Set<String> doenteInfetadoComOVirusCoronaVirusDezanoveTambemConhecidoComoSARSCOV2;
    private ReentrantLock lock = new ReentrantLock();

    public UserList()
    {
        this.users = new HashMap<>();
        this.doenteInfetadoComOVirusCoronaVirusDezanoveTambemConhecidoComoSARSCOV2 = new HashSet<>();
        this.usersLogin = new HashSet<>();
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
        int local = in.readInt();
        try {
            lock.lock();

            for(User user : this.users.values())
            {
                if(user.getLocalizacao() ==  local) n++;
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

    public Integer login(String nome, String pass){
        Integer msg = 0;
        User u;
        try
        {
            lock.lock();
            if((u = this.users.get(nome)) == null || !u.getPassword().equals(pass)) msg = 1;
            else if( this.doenteInfetadoComOVirusCoronaVirusDezanoveTambemConhecidoComoSARSCOV2.contains(nome) ) msg = 2;
            else if( !this.usersLogin.add(nome) ) msg = 3;
            return msg;
        }
        finally
        {
            lock.unlock();
        }
    }


    public Integer terminaSessao(String nome){
        try
        {
            lock.lock();
            this.usersLogin.remove(nome);
            return 0;
        }
        finally
        {
            lock.unlock();
        }
    }

    public Integer infetado(String nome){
        try
        {
            lock.lock();
            this.doenteInfetadoComOVirusCoronaVirusDezanoveTambemConhecidoComoSARSCOV2.add(nome);
            return 0;
        }
        finally
        {
            lock.unlock();
        }
    }

    public void atualizaLocalizacao(String nome, int localizacao){
        try
        {
            lock.lock();
            this.users.get(nome).setLocalizacao(localizacao);
        }
        finally
        {
            lock.unlock();
        }



    }


}
