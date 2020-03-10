package Callable;

import Decryptor.DES;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

public class DictSearcher implements Callable<String> {

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
    public String call(){

        try {

            int i = 0;
            while ( !findPassword.get() & i < dictionary.size()){

                String passwords = dictionary.get(i);

                byte[] epss = des.encryt(passwords.getBytes());

                if (des.checkEqual(epss)) {
                    System.out.println("Password find by thread " + threadID + " is " + passwords + " after " + i + " iteration");
                    findPassword.set(true);
                    return passwords;
                }

                ++i;
            }

        } catch (Exception e) {
            System.out.println("Error in thred " + threadID);
            System.err.println(e);
        }


        return "";
    }

}
