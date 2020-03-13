package Decryptor;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class SequentialFinder {


    private DES des;

    public SequentialFinder(DES des){
        this.des = des;
    }

    public void dictionaryFinder(ArrayList<String> dictionary) throws Exception {
        /*
            Dictionary hacking function. It use the password in the
            dictionary to hack the password that the user want to find.
        */

        // Open dict folder
        //long startTime = System.nanoTime();

        int i = 0;
        System.out.println("Starting searching password in dictionary");
        // Start finding password
        for (String passwords : dictionary){
            byte [] epss = des.encryt(passwords.getBytes());

            if (des.checkEqual(epss)){
                System.out.println("Sequential method find password after " + i + " iteration and it is " + passwords);
                break;
            }

            ++i;
        }

        //long endTime = System.nanoTime();
        //long duration = endTime - startTime;

        //return duration / 1000000;
    }
}
