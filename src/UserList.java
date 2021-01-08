import java.io.DataInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class UserList
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
            users.add(user);
        }
        finally
        {
            lock.unlock();
        }
    }

    public void printUser()
    {
        for(User user : users)
        {
            System.out.println(user);
        }
    }

}
