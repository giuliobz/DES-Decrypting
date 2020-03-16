package Callable;

import Decryptor.DES;
import Decryptor.FindingClass;

import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class CallableFinder implements FindingClass {

    // define variable to password search
    private DES des;
    private int numberThreads;
    private AtomicBoolean findPassword;

    // define the collection of searcher
    private List<DictSearcher> ds;
    private List<Future<ArrayList<String>>> futures;

    public CallableFinder(DES des, int numberThreads) {
        this.des = des;
        this.numberThreads = numberThreads;
        this.ds = new ArrayList<>();
        this.futures = new ArrayList<>();
        this.findPassword = new AtomicBoolean(false);
    }

    @Override
    public void setThreads(int numberThreads) {
        this.numberThreads = numberThreads;
        this.ds = new ArrayList<DictSearcher>();
        this.findPassword = new AtomicBoolean(false);
    }


    @Override
    public double dictionaryFinder(ArrayList<String> dictionary) {


        // create dict chunk
        int chunkSize = dictionary.size() / numberThreads;
        int startIndex, endIndex;

        // Init the num threads searcher
        for (int threadId = 0; threadId < numberThreads; ++threadId){
            startIndex = chunkSize * threadId;
            endIndex = (startIndex + chunkSize);

            // the last thread take the dictionary left
            if ((threadId + 1 == numberThreads) & (dictionary.size() % numberThreads != 0)){
                endIndex = dictionary.size();
            }

            ds.add(new DictSearcher(des, findPassword, new ArrayList<String>(dictionary.subList(startIndex, endIndex)), threadId));
        }

        ExecutorService executorService = Executors.newFixedThreadPool(numberThreads);
        //System.out.println("Starting Callable Thread Dict Search");
        double startTime = System.nanoTime();

        try {

            // invoke all thread and wait to take results
            futures  = executorService.invokeAll(ds);

        }catch (Exception e){
            System.out.println("Error in threads execution : ");
            System.err.println(e);
        }
        executorService.shutdownNow();
        double endTime = System.nanoTime();
        double duration = endTime - startTime;

        /*
        for (Future<String> future : futures) {
            if (future.get() != ""){
                System.out.println("The password is " + future.get());
            }
        }
        */

        return duration / 1000000;
    }
}
