package PasswordDictionary;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class PasswordBuilder {

    // args[0] define the starting dict to modify
    // args[1] name of the file to save the password dictionary
    // args[2] method used
    public static void main(String [] args) throws IOException {

        switch (args[2]) {
            case "data":
                dataDictionary(args[1]);
                break;
            case "words":
                wordDictionary(args[0], args[1]);
        }

    }

    private static void dataDictionary(String savePath) throws IOException {

        DataDictCreator dataDictCreator = new DataDictCreator(1600, 2020);

        System.out.println("Create Data Dict");
        ArrayList<String> dataDict = dataDictCreator.createDict();

        BufferedWriter writer = new BufferedWriter(new FileWriter(savePath + ".txt", true));

        System.out.println("Write all passwords " + dataDict.size());

        for (String data : dataDict){
            writer.write(data + "\n");
        }
        writer.close();
    }

    private static void wordDictionary(String startingDict, String savePath) throws IOException {

        DictionaryModifier dm = new DictionaryModifier(startingDict);
        int maxDictSize = 19958400;

        // Create different dictionary type
        System.out.println("Create Capital Letter Dict");
        dm.setCapitolLetterDict();
        System.out.println("Create leet word Dict");
        dm.setLeetWordDict();
        System.out.println("Create number word Dict");
        dm.setNumberWordDict();

        System.out.println("Create permute dictionary");
        dm.permuteDict();

        BufferedWriter writer = new BufferedWriter(new FileWriter(savePath + ".txt", true));
        ArrayList<String> m = dm.mergingFolders();
        ArrayList<String> p = dm.mergingPermutation();


        int i = 0;
        while (m.size() <= maxDictSize - 1) {
            m.add(p.get(i));

            ++i;
        }


        //Collections.shuffle(m);
        System.out.println(" Write all passwords " + m.size());

        for (String word : m){
            writer.write(word + "\n");
        }
        writer.close();

    }

}
