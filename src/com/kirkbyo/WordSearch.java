package com.kirkbyo;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by ozziekirkby on 2016-11-27.
 */
public class WordSearch {
    private Scanner scanner;
    static ArrayList<String> wordList = null;

    public WordSearch() {
        setFile(new File("words-10k.txt"));
        if (wordList == null) {
            wordList = findWords();
        }
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


    public ArrayList<String> findWords() {
        ArrayList<String> words = new ArrayList<String>();
        while (scanner.hasNextLine()) {
            String nextWord = scanner.nextLine();
            words.add(nextWord);
        }
        return words;
    }

    public boolean fileContainsWord(String word) {
        return wordList.contains(word.toLowerCase());
    }
}
