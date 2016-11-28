package com.kirkbyo;

import java.util.ArrayList;

/**
 * Created by ozziekirkby on 2016-11-27.
 */
public class WordQueue {
    private ArrayList<Character> letterList = new ArrayList<Character>();

    public void insert(char character) { letterList.add(character); }

    public void empty() { letterList.clear(); }

    public String word() {
        StringBuilder builder = new StringBuilder(letterList.size());
        for(Character letter: letterList) {
            builder.append(letter);
        }
        return builder.toString();
    }

}
