package ReadWriteLock;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class State {

    private boolean findPassword;
    private ReadWriteLock lock;

    public State(){
        findPassword = false;
        lock = new ReentrantReadWriteLock();
    }

    public boolean getState(){
        lock.readLock().lock();
        boolean state = findPassword;
        lock.readLock().unlock();
        return state;
    }

    public void setState(){
        lock.writeLock().lock();
        findPassword = true;
        lock.writeLock().unlock();
    }

    public void reset(){
        findPassword = false;
    }

}
