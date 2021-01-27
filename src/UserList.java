import java.io.DataInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class UserList
{
    private Map<String,User> users;                        // Utilizadores
    private Set<String> usersLogin;                        // Utilizadores Logados
    private Set<String> doentes;                           // Utilizadores Infetados
    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public UserList()
    {
        this.users = new HashMap<>();
        this.doentes = new HashSet<>();
        this.usersLogin = new HashSet<>();
    }

    public boolean addUser(DataInputStream in) throws IOException
    {
        try
        {
            User user = User.deserialize(in);

            lock.lock();
            boolean b = !this.users.containsKey(user.getNome()); // Se um utilizador com o mesmo nome não
            if (b) this.users.put(user.getNome(), user);         // existir então adiconamos

            return b;
        }
        finally
        {
            lock.unlock();
        }
    }

    public Integer login(DataInputStream in) throws IOException
    {
        String login = in.readUTF();
        String[] tokens = login.split(" ");
        String nome = tokens[0];
        String pass = tokens[1];
        User u;
        Integer msg = 0;

        lock.lock();
        if((u = this.users.get(nome)) == null || !u.getPassword().equals(pass)) msg = 1;  // Erro 1 (Nome ou pass incorreto)
        else if( this.doentes.contains(nome) ) msg = 2;                                   // Erro 2 (Está doente)
        else if( !this.usersLogin.add(nome) ) msg = 3;                                    // Erro 3 (Já está logado noutro client)
        lock.unlock();

        return msg;
    }

    public Integer numeroLocal(DataInputStream in) throws IOException
    {
        int n = 0;
        String local = in.readUTF();
        String[] tokens = local.split(" ");
        int localizacaoX = Integer.parseInt(tokens[0]);
        int localizacaoY = Integer.parseInt(tokens[1]);

        lock.lock();                                                       // Lock no sistema
        for(User user : this.users.values()) user.rw.readLock().lock();    // Percorrer os utilizadores e dar lock em cada
        lock.unlock();                                                     // unlock no sistema
        for(User user : this.users.values())
        {
            // Cada Utilizador na localizacao dada incrementa
            if(user.getLocalizacaoX() == localizacaoX && user.getLocalizacaoY() == localizacaoY) n++;
            user.rw.readLock().unlock();       // Á medida que percorremos os utilizadores damos unlock
        }

        return n;
    }

    public void atualizaLocalizacao(DataInputStream in) throws IOException
    {
        User u;

        String nome = in.readUTF();
        String local = in.readUTF();
        String[] tokens = local.split(" ");
        Integer localizacaoX = Integer.parseInt(tokens[0]);
        Integer localizacaoY = Integer.parseInt(tokens[1]);

        lock.lock();               // Lock no Sistema
        u = this.users.get(nome);
        u.rw.writeLock().lock();  // Lock no utilizador que estamos a alterar
        condition.signalAll();    // Signal para quem está à espera
        lock.unlock();            // Unlock no Sistema

        u.setLocalizacaoX(localizacaoX);  // Alterar Localizacao X
        u.setLocalizacaoY(localizacaoY);  // Alterar Localizacao Y

        u.rw.writeLock().unlock();        // Unlock no utilizador que estamos a alterar
    }

    public String possoIr(DataInputStream in) throws IOException, InterruptedException
    {
        boolean b = false;
        String local = in.readUTF();
        String[] tokens = local.split(" ");
        Integer localizacaoX = Integer.parseInt(tokens[0]);
        Integer localizacaoY = Integer.parseInt(tokens[1]);

        lock.lock();
        while (!b)
        {
            b = true;

            for (User user : this.users.values())  // Verifica se há algum utilizador no local para onde se quer ir
            {
                if (user.getLocalizacaoX() == localizacaoX && user.getLocalizacaoY() == localizacaoY)
                {
                    b = false;                     // Caso haja alguém no local
                    break;
                }
            }
            if(!b) condition.await();              // Enquanto b == false o processo espera
        }
        lock.unlock();

        return local;
    }

    public void infetado(DataInputStream in) throws IOException
    {
        String nome = in.readUTF();

        lock.lock();
        this.doentes.add(nome);  // Adiciona aos utilizadores doentes
        lock.unlock();
    }

    public void terminaSessao(DataInputStream in) throws IOException
    {
        String nome = in.readUTF();
        lock.lock();
        this.usersLogin.remove(nome);  // Removemos da Lista de Users Logados
        lock.unlock();
    }


}
