package com.kirkbyo;

import java.io.File;
import java.util.Scanner;

/**
 * Created by ozziekirkby on 2016-11-27.
 */
public class WordSearch {
    private Scanner scanner;

    public WordSearch() {
        setFile(new File("words-100k.txt"));
    }

    public boolean setFile(File file) {
        try {
            scanner = new Scanner(file);
            return true;
        } catch (java.io.FileNotFoundException error) {
            System.out.println(error);
            return false;
        }
    }

    public boolean fileContainsWord(String word) {
        while (scanner.hasNextLine()) {
            String nextWord = scanner.nextLine();
            if (nextWord.toLowerCase().equals(word)) {
                return true;
            }
        }
        return false;
    }
}
