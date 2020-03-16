package Decryptor;

import java.util.ArrayList;


public class SequentialFinder implements FindingClass {


    private DES des;
    private ArrayList<Double> time;

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

        //System.out.println("Starting searching password in dictionary");
        double startTime = System.nanoTime();
        // Start finding password
        for (String passwords : dictionary){
            byte [] epss = des.encryt(passwords.getBytes());
            des.checkPss(epss);

            if (des.checkEqual()){
                //System.out.println("Sequential method find all passwords");
                break;
            }
        }

        double sequentialElapsedTime = (System.nanoTime() - startTime) / 1000000;

        return sequentialElapsedTime;

    }
}
