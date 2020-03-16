package Decryptor;

import Callable.CallableFinder;
import Runnable.RunnableFinder;
import ReadWriteLock.ReadWriteLockFinder;
import SynchronizedThread.SynchronizedFinder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class DictDecrypting {

    // args[0] the number of max threads (12)
    // args[1] the dictionary for dictSearch
    // args[2] max iteretion per number of thread
    // args[3] the number of password to find
    // args[4] the method used
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
                    speedup.add(startSearch(sequentialFinder, parallelFinder, dict, Integer.parseInt(args[0]), passwords.size()));

                    break;

                case "Runnable":

                    parallelFinder = new RunnableFinder(des, 2);
                    speedup.add(startSearch(sequentialFinder, parallelFinder, dict, Integer.parseInt(args[0]), passwords.size()));

                    break;

                case "Lock":

                    parallelFinder = new ReadWriteLockFinder(des, 2);
                    speedup.add(startSearch(sequentialFinder, parallelFinder, dict, Integer.parseInt(args[0]), passwords.size()));

                    break;

                case "Sync":

                    parallelFinder = new SynchronizedFinder(des, 2);
                    speedup.add(startSearch(sequentialFinder, parallelFinder, dict, Integer.parseInt(args[0]), passwords.size()));

                    break;

            }

            passwords = takeRandomPassword(dict, Integer.parseInt(args[3]));

        }

        // ********************************************************************************************************** \\
        //                                          Save Result in CSV                                                \\
        // ********************************************************************************************************** \\


        String [] name = args[1].split("/");
        writeCSV(firstOrderStatistics(speedup, Integer.parseInt(args[0])), args[4] + "Speedup_" + args[3] + "_" + name[1]);

    }

    private static ArrayList<Double> startSearch(FindingClass dictSequentialFinder, FindingClass dictParallelFinder, ArrayList<String> dict, int maxThread, int numPasswords) throws Exception {

        ArrayList<Double> dF = new ArrayList<>();

        for (int numThread = 2; numThread <= maxThread; ++numThread) {

            dictParallelFinder.setThreads(numThread);

            System.out.println("/***********************************************************************************");
            System.out.println("Starting test with " + numThread + " num threads");
            System.out.println("/***********************************************************************************");

            double sequentialElapsedTime = (dictSequentialFinder.dictionaryFinder(dict)) / numPasswords;

            double durationCallable = dictParallelFinder.dictionaryFinder(dict) / numPasswords;

            dF.add(sequentialElapsedTime / durationCallable);

            System.out.println("/*******************************************************************************");
            System.out.println("");

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

    public static ArrayList<List> firstOrderStatistics(ArrayList<List> data, int numberThread) {

        ArrayList<List> speedup = new ArrayList<>();

        List<Double> mean = new ArrayList<>();
        List<Double> std = new ArrayList<>();

        for (int i = 0; i < (numberThread - 1); ++i){
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

    public static void writeCSV(ArrayList<List> source, String name) throws IOException {
        FileWriter csvWriter = new FileWriter("DictResults/" + name + ".csv");

        List<Double> mean = source.get(0);
        List<Double> std = source.get(1);

        int NumberThread = 2;
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
        Random r = new Random();
        int start = 0;
        int end = chunk - 1;

        for (int i = 0; i < numPassword; ++ i) {
            passwords.add(dictionary.get( r.nextInt(end - start) + start ));
            start += chunk;
            end += chunk - 1;

            if ((i + 1) == numPassword ){
                end = dictionary.size();
            }
        }

        return passwords;
    }

}
