package PasswordDictionary;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class DictionaryModifier {

    /*
        Modify the starting dict to create two new dictionary type:

            - Dictionary with capital letter words.
            - Dictionary with number instead of the vocal.
            - Dictionary where words are join with random number.

        This different dictionary type can be merged together.
     */

    // Starting Dict
    private ArrayList<String> startingDict;

    // Possible dictionary
    private ArrayList<String> capitolLetterDict;
    private ArrayList<String> leetWordDict;
    private ArrayList<String> numberWordDict;

    // Possible Dict permutate
    private boolean perm;
    private ArrayList<String> PstartingDict;
    private ArrayList<String> PcapitolLetterDict;
    private ArrayList<String> PleetWordDict;
    private ArrayList<String> PnumberWordDict;

    // Mapping the vocal to a number
    private HashMap<String, String> letter2number = new HashMap<>(){{
        put("a", "4");
        put("e", "3");
        put("i", "1");
        put("o", "0");
        put("u", "7");
    }};

    public DictionaryModifier(String startindDictPath) throws FileNotFoundException {
        this.startingDict = readDict(startindDictPath);
        this.perm = false;

    }

    private ArrayList<String> readDict(String startindDictPath) throws FileNotFoundException {
        /*
            Load the starting Dictionary
        */
        ArrayList<String> dict = new ArrayList<>();
        Scanner in = new Scanner(new FileReader(startindDictPath));
        while(in.hasNext()){
            //System.out.println(in.next());
            dict.add(in.next());
        }
        return dict;
    }

    public void setCapitolLetterDict(){
        /*
            Create first capital letter word
        */

        this.capitolLetterDict = new ArrayList<>();
        for (String words : this.startingDict){
            words = words.substring(0, 1).toUpperCase() + words.substring(1);
            //System.out.println(words);
            this.capitolLetterDict.add(words);
        }
    }

    public void setLeetWordDict(){
        /*
            Change the vocal with number
        */
        this.leetWordDict = new ArrayList<>();
        for (String words : this.startingDict){
            words = words.replaceAll("a", this.letter2number.get("a"));
            words = words.replaceAll("e", this.letter2number.get("e"));
            words = words.replaceAll("i", this.letter2number.get("i"));
            words = words.replaceAll("o", this.letter2number.get("o"));
            words = words.replaceAll("u", this.letter2number.get("u"));

            //System.out.println(words);
            this.leetWordDict.add(words);
        }
    }

    public void setNumberWordDict(){
        /*
            Replace the last 4 caracters with random number
        */
        this.numberWordDict = new ArrayList<>();
        for (String words: this.startingDict){
            String word =  words.substring(0, words.length() - 4) + (new Random().nextInt(999) + 1000);

            //System.out.println(words);
            this.numberWordDict.add(word);
        }
    }

    public void permuteDict(){
        PcapitolLetterDict = new ArrayList<>();
        PleetWordDict = new ArrayList<>();
        PnumberWordDict = new ArrayList<>();
        PstartingDict = new ArrayList<>();

        System.out.println("Permute starting dict");
        permute(startingDict, PstartingDict);

        System.out.println("Permute capital letter dict");
        permute(capitolLetterDict, PcapitolLetterDict);

        System.out.println("Permute leet dict dict");
        permute(leetWordDict, PleetWordDict);

        System.out.println("Permute num word dict");
        permute(numberWordDict, PnumberWordDict);

        perm = true;
    }

    private void permute(ArrayList<String> s, ArrayList<String> d) {

        for (String w : s) {
            d.addAll(computeAllPossiblePermutations(w));
        }

        Collections.shuffle(d);

    }


    private ArrayList<String> computeAllPossiblePermutations(String str) {
        ArrayList<String> perms = new ArrayList<>();
        if (str.length() == 5) {
            perms.add(str);
        } else {
            String chr = str.substring(0,1);
            String rest = str.substring(1);
            ArrayList<String> subPerms = computeAllPossiblePermutations(rest);
            for (String s : subPerms) {
                for (int j = 0; j <= s.length(); j++) {
                    String newPerm = s.substring(0,j) + chr + s.substring(j);
                    perms.add(newPerm);
                }
            }
        }
        return perms;
    }

    public ArrayList<String> mergingFolders(){
        /*
            Merge the dictionaries create.
        */
        ArrayList<String> mergedDict = new ArrayList<>();
        mergedDict.addAll(this.startingDict);

        if (this.capitolLetterDict.size() > 0){
            mergedDict.addAll(this.capitolLetterDict);
        }

        if (this.leetWordDict.size() > 0){
            mergedDict.addAll(this.leetWordDict);
        }

        if (this.numberWordDict.size() > 0){
            mergedDict.addAll(this.numberWordDict);
        }

        Collections.shuffle(mergedDict);

        return mergedDict;
    }

    public ArrayList<String> mergingPermutation(){
        ArrayList<String> mergedDict = new ArrayList<>();

        if (perm) {
            mergedDict.addAll(PstartingDict);
            mergedDict.addAll(PcapitolLetterDict);
            mergedDict.addAll(PleetWordDict);
            mergedDict.addAll(PnumberWordDict);
        }

        Collections.shuffle(mergedDict);

        return mergedDict;
    }



}