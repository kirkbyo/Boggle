package com.kirkbyo;

/**
 * Created by ozziekirkby on 2016-11-21.
 */
public class LetterNode {
    char character = '\0';

    // Straight Connections
    LetterNode left = null;
    LetterNode right = null;
    LetterNode top = null;
    LetterNode bottom = null;
    // Diagonal Connections
    LetterNode topLeftDiagonal = null;
    LetterNode topRightDiagonal = null;
    LetterNode bottomLeftDiagonal = null;
    LetterNode bottomRightDiagonal = null;

    public boolean visited = false;

    private int connections() {
        int connectionCount = 0;
        if (left != null) { connectionCount += 1; }
        if (right != null) { connectionCount += 1; }
        if (top != null) { connectionCount += 1; }
        if (bottom != null) { connectionCount += 1; }
        if (topLeftDiagonal != null) { connectionCount += 1; }
        if (topRightDiagonal != null) { connectionCount += 1; }
        if (bottomLeftDiagonal != null) { connectionCount += 1; }
        if (bottomRightDiagonal != null) { connectionCount += 1; }
        return  connectionCount;
    }

    public LetterNode(char character) {
        this.character = character;
    }

    @Override
    public String toString(){
        return character + ": " + connections();
    }
}
