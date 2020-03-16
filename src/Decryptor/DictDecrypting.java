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

    // args[0] the number of max threads (24)
    // args[1] the dictionary for dictSearch
    // args[2] max iteretion per number of thread
    // args[3] the number of password to find
    // args[4] the method used
    public static void main(String[] args) throws Exception {

        // Dictionary used
        ArrayList<String> dict = readDict(args[1]);

        // Password used for experiment
        ArrayList<String> passwords = takeRandomPassword(dict, Integer.parseInt(args[3]));
        System.out.println("The passwords to find are ");
        for (String p : passwords) {
            System.out.println(p);
        }

        // Define DES class and methods classes
        DES des = new DES(passwords);
        SequentialFinder sequentialFinder = new SequentialFinder(des);
        ArrayList<List> speedup = new ArrayList<>();

        // ********************************************************************************************************** \\
        //                                              Dictionary search                                             \\
        // ********************************************************************************************************** \\

        switch (args[4]) {
            case "Callable":

                CallableFinder callableFinder = new CallableFinder(des, 2);
                speedup = startSearch(sequentialFinder, callableFinder, dict, Integer.parseInt(args[2]), Integer.parseInt(args[0]));

                break;

            case "Runnable":
                RunnableFinder runnableFinder = new RunnableFinder(des, 2);
                speedup = startSearch(sequentialFinder, runnableFinder, dict, Integer.parseInt(args[2]), Integer.parseInt(args[0]));
                break;

            case "Lock":
                ReadWriteLockFinder readWriteLockFinder = new ReadWriteLockFinder(des, 2);
                speedup = startSearch(sequentialFinder, readWriteLockFinder, dict, Integer.parseInt(args[2]), Integer.parseInt(args[0]));
                break;

            case "Sync":
                SynchronizedFinder synchronizedFinder = new SynchronizedFinder(des, 2);
                speedup = startSearch(sequentialFinder, synchronizedFinder, dict, Integer.parseInt(args[2]), Integer.parseInt(args[0]));
                break;

        }

        // ********************************************************************************************************** \\
        //                                          Save Result in CSV                                                \\
        // ********************************************************************************************************** \\


        writeCSV(speedup, args[4] + "Speedup_" + args[2]);

        /*

        int numTreads = 8;
        int chunkSize = dict.size() / numTreads;
        System.out.println(chunkSize);
        int startIndex, endIndex;

        for (int threadId = 0; threadId < numTreads; ++threadId) {
            startIndex = chunkSize * threadId;
            endIndex = (startIndex + chunkSize);

            // the last thread take the dictionary left
            if ((threadId + 1 == numTreads) & (dict.size() % numTreads != 0)) {
                endIndex = dict.size();
            }

            ArrayList<String> l = new ArrayList<String>(dict.subList(startIndex, endIndex));
            System.out.println("Thread " + threadId + " controll from [ " + startIndex + " - " + endIndex + " ]");
            System.out.println("Starting word " + l.get(0) + " and end wod " + l.get(l.size() - 1));
        }
         */
    }

    private static ArrayList<List> startSearch(FindingClass dictSequentialFinder, FindingClass dictParallelFinder,ArrayList<String> dict, int maxIteration, int maxThread) throws Exception {
        ArrayList<List> speedup = new ArrayList<>();

        for (int numThread = 2; numThread <= maxThread; numThread += 2) {

            dictParallelFinder.setThreads(numThread);

            System.out.println("/***********************************************************************************");
            System.out.println("Starting test with " + numThread + " num threads");
            System.out.println("/***********************************************************************************");

            ArrayList<Double> dF = new ArrayList<>();


            for (int i = 0; i < maxIteration; ++i) {

                double sequentialElapsedTime = dictSequentialFinder.dictionaryFinder(dict);

                double durationCallable = dictParallelFinder.dictionaryFinder(dict);


                dF.add(sequentialElapsedTime / durationCallable);


                System.out.println("/*******************************************************************************");
                System.out.println("");

                dictParallelFinder.setThreads(numThread);

            }

            speedup.add(firstOrderStatistics(dF, numThread));
        }

        return speedup;
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

    public static List<String> firstOrderStatistics(ArrayList<Double> m, int numberThread) {

        List<String> data = new ArrayList<>();

        double sum = 0;

        for (int i = 0; i < m.size(); i++) {
            sum += m.get(i);
        }

        double mean = sum / m.size();
        data.add(String.valueOf(mean));

        double std = 0;
        for (double num : m) {
            std += Math.pow(num - mean, 2);
        }

        std = Math.sqrt(std / m.size());
        data.add(String.valueOf(std));
        data.add(String.valueOf(numberThread));

        return data;
    }

    public static void writeCSV(ArrayList<List> source, String name) throws IOException {
        FileWriter csvWriter = new FileWriter(name + ".csv");

        for (List data : source) {
            csvWriter.append(String.join(",", data));
            csvWriter.append("\n");
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
