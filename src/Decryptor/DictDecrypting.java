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
import java.util.Scanner;

public class DictDecrypting {

    // arg[0] the number of max threads (24)
    // arg[1] the dictionary for dictSearch
    // args[2] max iteretion per number of thread
    public static void main(String[] args) throws Exception {

        // Dictionary used
        ArrayList<String> dict = readDict(args[1]);
        System.out.println(dict.size());

        // Password used for experiment
        String password = dict.get((dict.size() / 2) - 1);
        System.out.println("The password to find " + password);

        // Define DES class and methods classes
        DES des = new DES(password);
        SequentialFinder sequentialFinder = new SequentialFinder(des);
        CallableFinder callableFinder = new CallableFinder(des, 2);
        RunnableFinder runnableFinder = new RunnableFinder(des, 2);
        ReadWriteLockFinder readWriteLockFinder = new ReadWriteLockFinder(des, 2);
        SynchronizedFinder synchronizedFinder = new SynchronizedFinder(des, 2);

        // *************************************************************************************************************
        // Dictionary search
        // *************************************************************************************************************

        ArrayList<List> speedupC = new ArrayList<>();
        ArrayList<List> speedupR = new ArrayList<>();
        ArrayList<List> speedupL = new ArrayList<>();
        ArrayList<List> speedupS = new ArrayList<>();

        for (int numThread = 2; numThread <= Integer.parseInt(args[0]); ++numThread) {

            callableFinder.setThreads(numThread);
            runnableFinder.setThreads(numThread);
            readWriteLockFinder.setThreads(numThread);
            synchronizedFinder.setThreads(numThread);

            System.out.println("Starting iteration " + (numThread));

            ArrayList<Double> C = new ArrayList<>();
            ArrayList<Double> R = new ArrayList<>();
            ArrayList<Double> L = new ArrayList<>();
            ArrayList<Double> S = new ArrayList<>();

            for (int i = 0; i < Integer.parseInt(args[2]); ++ i) {

                double sequentialElapsedTime = sequentialFinder.dictionaryFinder(dict);
                double durationCallable = callableFinder.dictionaryFinder(dict);
                double durationRunnable = runnableFinder.dictionaryFinder(dict);
                double durationLock = readWriteLockFinder.dictionaryFinder(dict);
                double durationSync = synchronizedFinder.dictionaryFinder(dict);

                C.add(sequentialElapsedTime / durationCallable);
                R.add(sequentialElapsedTime / durationRunnable);
                L.add(sequentialElapsedTime / durationLock);
                S.add(sequentialElapsedTime / durationSync);

                System.out.println(" ********************************************************************************* ");
                System.out.println("");

                callableFinder.setThreads(numThread);
                runnableFinder.setThreads(numThread);
                readWriteLockFinder.setThreads(numThread);
                synchronizedFinder.setThreads(numThread);

            }

            speedupC.add(firstOrderStatistics(C, numThread));
            speedupR.add(firstOrderStatistics(R, numThread));
            speedupL.add(firstOrderStatistics(L, numThread));
            speedupS.add(firstOrderStatistics(S, numThread));
        }

        writeCSV(speedupC, "CallableSpeedup_" + args[2]);
        writeCSV(speedupR, "RunnableSpeedup_" + args[2]);
        writeCSV(speedupL, "LockSpeedup_" + args[2]);
        writeCSV(speedupS, "SyncSpeedup_" + args[2]);
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

    public static List<Double> firstOrderStatistics(ArrayList<Double> m, int numberThread) {

        List<Double> data = new ArrayList<>();

        double sum = 0;

        for (int i = 0; i < m.size(); i++) {
            sum += m.get(i);
        }

        double mean = sum / m.size();
        data.add(mean);

        double std = 0;
        for (double num : m) {
            std += Math.pow(num - mean, 2);
        }

        std = Math.sqrt(std / m.size());
        data.add(std);
        data.add((double) numberThread);

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

}
