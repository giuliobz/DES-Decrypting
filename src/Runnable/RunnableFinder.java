package Runnable;

import Decryptor.DES;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class RunnableFinder {

    // define variable to password search
    private DES des;
    private int numberThreads;
    private AtomicBoolean findPasswordDict;

    // define the collection of searcher
    private List<DictSearcher> ds;

    public RunnableFinder(DES des, int numberThreads) {
        this.des = des;
        this.numberThreads = numberThreads;
        this.ds = new ArrayList<>();
        this.findPasswordDict = new AtomicBoolean(false);
    }

    public void setThreads(int numberThreads) {
        this.numberThreads = numberThreads;
        ds = new ArrayList<DictSearcher>();
        findPasswordDict = new AtomicBoolean(false);
    }


    public long dictionaryFinder(ArrayList<String> dictionary) throws FileNotFoundException, InterruptedException {

        // create dict chunk
        int chunkSize = dictionary.size() / numberThreads;
        int startIndex, endIndex;

        // Init the num threads searcher
        for (int thredId = 0; thredId < numberThreads; ++thredId){
            startIndex = chunkSize * thredId;
            endIndex = (startIndex + chunkSize);

            // the last thread take the dictionary left
            if ((thredId + 1 == numberThreads) & (dictionary.size() % numberThreads != 0)){
                endIndex = dictionary.size();
            }

            ds.add(new DictSearcher(des, findPasswordDict, new ArrayList<String>(dictionary.subList(startIndex, endIndex)), thredId));
        }

        ExecutorService executorService = Executors.newFixedThreadPool(numberThreads);;
        long startTime = System.nanoTime();

        try {
            System.out.println("Starting Runnable Thread Dict Search");
            for (Runnable r : ds) {
                executorService.submit(r);
            }

        }catch (Exception e){
            System.out.println("Error in threads execution : ");
            System.err.println(e);
        }
        executorService.shutdownNow();
        executorService.awaitTermination(10000, TimeUnit.MINUTES);
        long endTime = System.nanoTime();
        long duration = endTime - startTime;

        return duration / 1000000;
    }
}
