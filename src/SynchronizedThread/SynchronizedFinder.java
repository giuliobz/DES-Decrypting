package SynchronizedThread;

import Decryptor.DES;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SynchronizedFinder {

    // define variable to password search
    private DES des;
    private int numberThreads;

    // define the collection of searcher
    private List<DictSearcher> ds;
    private State findPasswordDict;

    public SynchronizedFinder(DES des, int numberThreads) {
        this.des = des;
        this.numberThreads = numberThreads;
        this.ds = new ArrayList<>();
        this.findPasswordDict = new State();
    }

    public void setThreads(int numThread) {
        this.numberThreads = numThread;
        ds = new ArrayList<DictSearcher>();
        this.findPasswordDict.reset();
    }


    public double dictionaryFinder(ArrayList<String> dictionary) throws  InterruptedException {


        // create dict chunk
        int chunkSize = dictionary.size() / numberThreads;
        int startIndex, endIndex;

        // Init the num threads searcher
        for (int thredId = 0; thredId < numberThreads; ++thredId) {
            startIndex = chunkSize * thredId;
            endIndex = (startIndex + chunkSize);

            // the last thread take the dictionary left
            if ((thredId + 1 == numberThreads) & (dictionary.size() % numberThreads != 0)) {
                endIndex = dictionary.size();
            }

            ds.add(new DictSearcher(des, findPasswordDict, new ArrayList<String>(dictionary.subList(startIndex, endIndex)), thredId));
        }

        ExecutorService executorService = Executors.newFixedThreadPool(numberThreads);
        double startTime = System.nanoTime();

        try {

            // invoke all thread
            System.out.println("Starting Sync Thread Dict Search");
            for (Runnable r : ds) {
                executorService.submit(r);
            }

        } catch (Exception e) {
            System.out.println("Error in threads execution : ");
            System.err.println(e);
        }
        executorService.shutdownNow();
        executorService.awaitTermination(10000, TimeUnit.MINUTES);
        double endTime = System.nanoTime();
        double duration = endTime - startTime;

        return duration / 1000000;
    }
}
