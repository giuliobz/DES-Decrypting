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
        ArrayList<String> m =dm.mergingFolders();

        System.out.println(" Write all passwords ");

        for (int i = 1; i <= m.size(); ++i){
            writer.write(m.get(i) + "\n");
            if (i > m.size() / 2) {
                if (args[2] == "even"){
                    if ( (i % 2 == 0) & (i % 4 == 0) & (i % 6 == 0) & (i % 8 == 0) & (i % 10 == 0) & (i % 12 == 0) ) {
                        break;
                    }
                }else {

                    if ( (i % 3 == 0) & (i % 5 == 0) & (i % 7 == 0) & (i % 9 == 0) & (i % 11 == 0) & (i % 12 == 0) ) {
                        break;
                    }
                }

            }
        }
        writer.close();
    }
}
