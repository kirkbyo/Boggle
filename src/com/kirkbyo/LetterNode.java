package com.kirkbyo;

/**
 * Letter Node containing the character and if the node has been visited or not yet
 */
public class LetterNode {
    // Character of the node
    char character = '\0';
    // Boolean value determining if the node has been visited or not
    boolean visited = false;

    public LetterNode(char character) {
        this.character = character;
    }

    @Override
    public String toString(){
        return character + "";
    }
}
