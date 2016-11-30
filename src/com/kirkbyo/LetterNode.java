package com.kirkbyo;

/**
 * Created by ozziekirkby on 2016-11-21.
 */
public class LetterNode {
    char character = '\0';
    boolean visited = false;

    public LetterNode(char character) {
        this.character = character;
    }

    @Override
    public String toString(){
        return character + "";
    }
}
