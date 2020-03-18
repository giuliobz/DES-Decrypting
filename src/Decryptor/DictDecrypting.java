package Decryptor;

import Callable.CallableFinder;
import Runnable.RunnableFinder;
import ReadWriteLock.ReadWriteLockFinder;
import SynchronizedThread.SynchronizedFinder;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class DictDecrypting {

    // args[0] the number of starting threads
    // args[1] the dictionary for dictSearch
    // args[2] max iteretion per number of thread
    // args[3] the number of password to find
    // args[4] the parallel method used
    // args[5] the test type (pss or thread : pss change the number of password using 12 thread, thead changes the number of thread with default password (100))
    public static void main(String[] args) throws Exception {

        // Dictionary used
        ArrayList<String> dict = readDict(args[1]);

        // Password used for experiment
        ArrayList<String> passwords = takeRandomPassword(dict, Integer.parseInt(args[3]));

        // Define DES class and methods classes
        DES des = new DES();
        SequentialFinder sequentialFinder = new SequentialFinder(des);
        FindingClass parallelFinder = null;
        ArrayList<List> speedup = new ArrayList<>();
        int maxThread = 12;



        // ********************************************************************************************************** \\
        //                                              Dictionary search                                             \\
        // ********************************************************************************************************** \\

        for (int i = 0; i < Integer.parseInt(args[2]); ++i) {

            System.out.println("/***********************************************************************************");
            System.out.println("Starting test " + (i + 1));
            System.out.println("/***********************************************************************************");

            des.setPasswords(passwords);

            switch (args[4]) {

                case "Callable":

                    parallelFinder = new CallableFinder(des, 2);
                    speedup.add(startSearch(sequentialFinder, parallelFinder, des, dict, Integer.parseInt(args[0]), maxThread, passwords.size()));

                    break;

                case "Runnable":

                    parallelFinder = new RunnableFinder(des, 2);
                    speedup.add(startSearch(sequentialFinder, parallelFinder, des, dict, Integer.parseInt(args[0]), maxThread, passwords.size()));

                    break;

                case "Lock":

                    parallelFinder = new ReadWriteLockFinder(des, 2);
                    speedup.add(startSearch(sequentialFinder, parallelFinder, des, dict, Integer.parseInt(args[0]), maxThread, passwords.size()));

                    break;

                case "Sync":

                    parallelFinder = new SynchronizedFinder(des, 2);
                    speedup.add(startSearch(sequentialFinder, parallelFinder, des, dict, Integer.parseInt(args[0]), maxThread, passwords.size()));

                    break;

            }

            passwords = takeRandomPassword(dict, Integer.parseInt(args[3]));

        }

        // ********************************************************************************************************** \\
        //                                          Save Result in CSV                                                \\
        // ********************************************************************************************************** \\

        String [] name = args[1].split("/");
        String path = System.getProperty("user.dir") + "/DictResults/" + args[4] + "_" + name[1].replace(".txt", "") + "_" + args[5];

        //Creating a File object
        System.out.println(path);
        File file = new File(path);

        //Creating the directory
        if (!file.exists()){
            file.mkdir();
        }

        int value = Integer.parseInt(args[0]);
        if (args[5].equals("pss")){
            value = Integer.parseInt(args[3]);
        }

        System.out.println(value);

        writeCSV(firstOrderStatistics(speedup), path + "/" + args[4] + "Speedup_" + args[3], value);


    }

    private static ArrayList<Double> startSearch(FindingClass dictSequentialFinder, FindingClass dictParallelFinder, DES des, ArrayList<String> dict, int startThread, int maxThread, int numPasswords) throws Exception {

        ArrayList<Double> dF = new ArrayList<>();

        for (int numThread = startThread; numThread <= maxThread; ++numThread) {

            dictParallelFinder.setThreads(numThread);
            

            System.out.println("/***********************************************************************************");
            System.out.println("Starting test with " + numThread + " num threads");
            System.out.println("/***********************************************************************************");

	    des.resetBool();
            double sequentialElapsedTime = (dictSequentialFinder.dictionaryFinder(dict)) / numPasswords;
            des.resetBool();
            double durationCallable = dictParallelFinder.dictionaryFinder(dict) / numPasswords;

            dF.add(sequentialElapsedTime / durationCallable);

        }

        return dF;
    }

    private static ArrayList<String> readDict(String dictPath) throws FileNotFoundException {
        /*
            Load the Dictionary
        */
        ArrayList<String> dict = new ArrayList<>();
        Scanner in = new Scanner(new FileReader(dictPath));

        while (in.hasNext()) {
            dict.add(in.next());
        }
        return dict;
    }

    public static ArrayList<List> firstOrderStatistics(ArrayList<List> data) {

        ArrayList<List> speedup = new ArrayList<>();

        List<Double> mean = new ArrayList<>();
        List<Double> std = new ArrayList<>();

        for (int i = 0; i < data.get(0).size(); ++i){
            mean.add(0.0);
            std.add(0.0);
        }


        for (List a : data) {
            for (int i = 0; i < a.size(); ++i) {
                mean.set(i, mean.get(i) + (double) a.get(i));
            }
        }

        for (int i = 0; i < mean.size(); ++i){
            mean.set(i, mean.get(i) / data.size());
        }

        for (List a : data) {
            for (int i = 0; i < a.size(); ++i) {
                std.set(i, std.get(i) + Math.pow( (double) a.get(i) - mean.get(i) , 2) );
            }
        }

        for (int i = 0; i < std.size(); ++i){
            std.set(i, Math.sqrt( std.get(i) / data.size() ) );
        }

        speedup.add(mean);
        speedup.add(std);

        return speedup;
    }

    public static void writeCSV(ArrayList<List> source, String name, int startinThread) throws IOException {
        FileWriter csvWriter = new FileWriter(name + ".csv");

        List<Double> mean = source.get(0);
        List<Double> std = source.get(1);

        int NumberThread = startinThread;
        for (int i = 0; i < mean.size(); ++ i) {
            String [] data = {String.valueOf(mean.get(i)), String.valueOf(std.get(i)), String.valueOf(NumberThread) };
            csvWriter.append(String.join(",", data));
            csvWriter.append("\n");
            NumberThread ++;
        }

        csvWriter.flush();
        csvWriter.close();
    }

    public static ArrayList<String> takeRandomPassword(ArrayList<String> dictionary, int numPassword) {
        ArrayList<String> passwords = new ArrayList<>();

        int chunk = dictionary.size() / numPassword;
        int start = 0;
        int end = chunk;

        for (int i = 0; i < numPassword; ++ i) {

            passwords.add(dictionary.get( ThreadLocalRandom.current().nextInt(start, end)) );
            start += chunk;
            end += chunk;

            if ((i + 1) == numPassword ){
                end = dictionary.size();
            }
        }

        return passwords;
    }

}
