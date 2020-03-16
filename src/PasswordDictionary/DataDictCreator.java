package PasswordDictionary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class DataDictCreator {

    private ArrayList<String> dataDictionary;
    private int startingYear;
    private int currentYear;

    public DataDictCreator(int startingYear, int currentYear){

        this.dataDictionary = new ArrayList<>();
        this.startingYear = startingYear;
        this.currentYear = currentYear;
    }

    public ArrayList<String> createDict() {

        for (int year = startingYear; year <= currentYear; ++year) {

            for (int month = 1 ; month <= 12; ++month ){

                for (int day = 1; day <= endDay(month, year); ++day ){
                    dataDictionary.add(String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year));
                    dataDictionary.add(String.valueOf(month) + "/" + String.valueOf(day) + "/" + String.valueOf(year));
                    dataDictionary.add(String.valueOf(year) + "/" + String.valueOf(month) + "/" + String.valueOf(day));

                }

            }

        }

        Collections.shuffle(dataDictionary);

        return dataDictionary;
    }

    private int endDay(int month, int years) {
        int end = 0;

        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12 ) {
            end = 31;

        } else if ( month == 4 || month == 6 || month == 9 || month == 11){

            end = 30;

        } else {

            end = 28;

            if (leapYearChecking(years)) {
                end = 29;
            }

        }

        return end;
    }

    private boolean leapYearChecking(int year){

        boolean leap = false;

        // passo 1
        if ( ( year % 4 == 0 || ( year % 100 == 0 & year % 400 == 0) ) ) {
            leap = true;
        }

        return leap;
    }


}
