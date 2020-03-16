package Runnable;

import Decryptor.DES;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class DictSearcher implements Runnable {

    // define variable used to password search
    private DES des;
    private AtomicBoolean findPassword;

    // define thread id
    private int threadID;

    // peace of dictionary used by the thread
    private ArrayList<String> dictionary;

    public DictSearcher(DES des, AtomicBoolean findPassword, ArrayList<String> dict, int threadID) {
        this.des = des;
        this.findPassword = findPassword;
        this.dictionary = dict;
        this.threadID = threadID;
    }

    @Override
    public void run() {

        try {

            int i = 0;
            while (!findPassword.get() & i < dictionary.size()) {

                String passwords = dictionary.get(i);

                byte[] epss = des.encryt(passwords.getBytes());
                des.checkPss(epss);

                if (des.checkEqual()) {
                    findPassword.set(true);
                }
                ++i;
            }

        } catch (Exception e) {
            System.out.println("Error in thred " + threadID);
            System.err.println(e);
        }
    }
}
