package SynchronizedThread;

public class State {

    private boolean findPassword;
    public State(){
        findPassword = false;
    }

    public synchronized boolean getState(){
        return findPassword;
    }

    public synchronized void setState(){
        findPassword = true;
    }

    public void reset(){
        findPassword = false;
    }

}
