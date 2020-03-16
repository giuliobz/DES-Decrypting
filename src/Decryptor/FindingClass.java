package Decryptor;

import java.util.ArrayList;

public interface FindingClass {

    public void setThreads(int numberThreads);

    public abstract double dictionaryFinder(ArrayList<String> dictionary) throws Exception;
}
