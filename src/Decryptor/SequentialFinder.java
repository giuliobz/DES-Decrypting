package Decryptor;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class SequentialFinder implements FindingClass {


    private DES des;

    public SequentialFinder(DES des){
        this.des = des;
    }

    @Override
    public void setThreads(int numberThreads) {
        System.out.println("Is not for sequential function");
    }

    @Override
    public double dictionaryFinder(ArrayList<String> dictionary) throws Exception {
        /*
            Dictionary hacking function. It use the password in the
            dictionary to hack the password that the user want to find.
        */

        System.out.println("Starting searching password in dictionary");
        double startTime = System.nanoTime();
        // Start finding password
        for (String passwords : dictionary){
            byte [] epss = des.encryt(passwords.getBytes());
            des.checkPss(epss);

            if (des.checkEqual()){
                System.out.println("Sequential method find all passwords");
                break;
            }
        }

        double sequentialElapsedTime = (System.nanoTime() - startTime) / 1000000;

        return sequentialElapsedTime;

    }
}
