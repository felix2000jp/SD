import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;


public class UserList implements Iterable<User>
{
    private List<User> users;
    private ReentrantLock lock = new ReentrantLock();

    public UserList()
    {
        this.users = new ArrayList<>();
    }

    public List<User> getUsers()
    {
        return this.users;
    }

    public void addUser(DataInputStream in) throws IOException
    {
        User user = User.deserialize(in);
        lock.lock();
        try
        {
            this.users.add(user);
        }
        finally
        {
            lock.unlock();
        }
    }

    public Integer numeroLocal(DataInputStream in) throws IOException {
        int n = 0;
        int tatau = in.readInt();

        for(User user : this.users)
        {
            if(user.getLocalizacao() ==  tatau) n++;
        }

        return n;
    }

    public void printUser()
    {
        for(User user : users)
        {
            System.out.println(user);
        }
    }

    @Override
    public Iterator<User> iterator()
    {
        return this.users.iterator();
    }
}
