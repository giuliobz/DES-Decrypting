package PasswordDictionary;

import java.io.*;
import java.util.ArrayList;

public class PasswordBuilder {

    // args[0] deifine the starting dict to modify
    // args[1] name of the file to save the passwrd dictionary
    public static void main(String [] args) throws IOException {
        DictionaryModifier dm = new DictionaryModifier(args[0]);

        // Create different dictionary type
        System.out.println("Create Capital Letter Dict");
        dm.setCapitolLetterDict();
        System.out.println("Create leet word Dict");
        dm.setLeetWordDict();
        System.out.println("Create number word Dict");
        dm.setNumberWordDict();

        System.out.println("Create permute dictionary");
        dm.permuteDict();

        // Write the final folder
        BufferedWriter writer = new BufferedWriter(new FileWriter(args[1] + args[2] + ".txt", true));
        ArrayList<String> m = dm.mergingFolders();
        ArrayList<String> p = dm.mergingFolders();

        for (int i = 0; i < p.size(); ++i){
            if (args[2] == "even"){
                if ( (m.size() % 2 == 0) & (m.size() % 4 == 0) & (m.size() % 6 == 0) & (m.size() % 8 == 0) & (m.size() % 10 == 0) & (m.size() % 12 == 0) ) {
                    break;
                }
            }else {

                if ( (m.size() % 3 == 0) & (m.size() % 5 == 0) & (m.size() % 7 == 0) & (m.size() % 9 == 0) & (m.size() % 11 == 0)) {
                    break;
                }
            }

            m.add(p.get(i));
        }

        System.out.println(" Write all passwords ");


        // 19 958 400
        for (int i = 1; i <= m.size(); ++i){
            writer.write(m.get(i) + "\n");
        }
        writer.close();
    }
}
