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

    public boolean addUser(DataInputStream in) throws IOException{
        try {
            lock.lock();
            User user = User.deserialize(in);
            boolean b = !this.users.containsKey(user.getNome());
            if (b)
                this.users.put(user.getNome(), user);

            return b;
        }
        finally {
            lock.unlock();
        }
    }

    public Integer numeroLocal(DataInputStream in) throws IOException, InterruptedException {
        int n = 0;
        int local = in.readInt();
        int controlo = 0;
        try {
            lock.lock();

            for(User user : this.users.values())
            {
                user.rw.readLock().lock();
            }

            Thread.sleep(10000);
            controlo = 1;

            lock.unlock();

            controlo = 2;

            for(User user : this.users.values())
            {
                if(user.getLocalizacao() ==  local) n++;
                user.rw.readLock().unlock();
            }

            controlo = 3;

            return n;
        }finally {
            if(controlo == 1) {
                lock.unlock();
            }
            if(controlo <= 2){
                for(User user : this.users.values())
                {
                    user.rw.readLock().unlock();
                }
            }
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

    public synchronized Integer login(String nome, String pass){
        Integer msg = 0;
        User u;
        if((u = this.users.get(nome)) == null || !u.getPassword().equals(pass)) msg = 1;
        else if( this.doenteInfetadoComOVirusCoronaVirusDezanoveTambemConhecidoComoSARSCOV2.contains(nome) ) msg = 2;
        else if( !this.usersLogin.add(nome) ) msg = 3;
        return msg;
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
        int controlo = 0;
        User u = new User();
        try
        {
            lock.lock();
            u = this.users.get(nome);
            u.rw.writeLock().lock();

            controlo = 1;
            lock.unlock();
            controlo = 2;

            u.setLocalizacao(localizacao);
            u.rw.writeLock().unlock();
            controlo = 3;
        }
        finally
        {
            if(controlo == 1) {
                lock.unlock();
            }
            if(controlo <= 2)
                u.rw.writeLock().unlock();

        }



    }


}
