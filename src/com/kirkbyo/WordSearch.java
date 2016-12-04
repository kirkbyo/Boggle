package com.kirkbyo;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Searches for words against a text file to see if letter combinations is a word or not.
 */
public class WordSearch {
    // File Scanner. Used to get all the words from the text file.
    private Scanner scanner;
    // Array containing all the words. I know there must be a better solution out there, but I couldn't find it...
    // Another way to increase efficiency would be to split the .txt file into groups, based on the amount of letters in
    // the word. This would speed on the process of verifying if the word is contained because you could just check against
    // the words that have the same amount of characters.
    static ArrayList<String> wordList = null;

    /* --- Methods --- */
    public WordSearch() {
        // Sets the .txt file to the scanner
        setFile(new File("words-10k.txt"));
        if (wordList == null) { // If words have not been found, go and find them
            wordList = findWords();
        }
    }

    /**
     * Sets the file to the scanner
     * @param file .txt file for the scanner
     * @return Boolean depending on if the process was successful or not
     */
    public boolean setFile(File file) {
        try {
            scanner = new Scanner(file);
            return true;
        } catch (java.io.FileNotFoundException error) {
            System.out.println(error);
            return false;
        }
    }

    /**
     * Goes through the .txt file and adds each word into the array
     * @return ArrayList contaning all the words in the file
     */
    public ArrayList<String> findWords() {
        ArrayList<String> words = new ArrayList<String>();
        while (scanner.hasNextLine()) {
            String nextWord = scanner.nextLine();
            words.add(nextWord);
        }
        return words;
    }

    /**
     * Checks to see if the array contains the word.
     * @param word to verify
     * @return Boolean on if the letter combination is a word or not.
     */
    public boolean fileContainsWord(String word) {
        return wordList.contains(word.toLowerCase());
    }
}
