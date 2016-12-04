package com.kirkbyo;

import java.util.ArrayList;

/**
 * Queue of letters.
 * Ex:
 WordQueue queue = new WordQueue();
 queue.insert("H");
 queue.insert("E");
 queue.insert("L");
 queue.insert("L");
 queue.insert("O");
 queue.word(); -> "Hello"
 */
public class WordQueue {
    /**
     * Array of characters that contains the queue of letters.
     */
    private ArrayList<Character> letterList = new ArrayList<Character>();

    /**
     * Inserts another letter into the queue.
     * @param character to be added into the queue.
     */
    public void insert(char character) { letterList.add(character); }

    /**
     * Empties the queue. (Removes all elements from the array)
     */
    public void empty() { letterList.clear(); }

    /**
     * Removes the last character from queue.
     */
    public void removeLast() { letterList.remove(letterList.size() - 1); }

    /**
     * Constructs a string from the letters in the queue.
     * @return String containing the letters from queue.
     */
    public String word() {
        StringBuilder builder = new StringBuilder(letterList.size());
        for(Character letter: letterList) {
            builder.append(letter);
        }
        return builder.toString();
    }

}
