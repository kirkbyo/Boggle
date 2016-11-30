package com.kirkbyo;

import com.sun.xml.internal.ws.util.StringUtils;

import java.lang.*;
import java.util.*;

/**
 * Created by ozziekirkby on 2016-11-09.
 */
public class Boggle {
    int maxAmountLetters = 8;
    int minimumAmountLetters = 3;
    int gridWidth = 4;
    int gridHeight = 4;

    private WordSearch wordSeacher = new WordSearch();
    private ArrayList<ArrayList<LetterNode>> nodes = null;
    public Set<String> foundWords = new HashSet<String>();

    public void generateNodeArrayFrom(ArrayList<ArrayList<Character>> charMatrix) {
        ArrayList<ArrayList<LetterNode>> nodes = new ArrayList<ArrayList<LetterNode>>();

        for (ArrayList<Character> row: charMatrix) {
            ArrayList<LetterNode> rowCharacters = new ArrayList<LetterNode>();
            for (char character: row) {
                LetterNode node = new LetterNode(character);
                rowCharacters.add(node);
            }
            nodes.add(rowCharacters);
        }

        this.nodes = nodes;
    }

    public Set<String> findWords() {
        for (int i=0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                recursivelyFindWords(nodes, i, j, new WordQueue());
            }
        }

        sanitizeFoundWords();

        return foundWords;
    }

    private void sanitizeFoundWords() {
        Set<String> cleanArray = ((Set) ((HashSet) foundWords).clone());
        for (String word: foundWords) {
            if (word.length() <= 2) {
                cleanArray.remove(word);
            }
        }
        foundWords = cleanArray;
    }

    private void recursivelyFindWords(ArrayList<ArrayList<LetterNode>> nodes2D, int row, int column, WordQueue queue) {
        LetterNode activeNode = nodes2D.get(row).get(column);
        activeNode.visited = true;

        queue.insert(activeNode.character);

        if (wordSeacher.fileContainsWord(queue.word())) {
            foundWords.add(StringUtils.capitalize(queue.word().toLowerCase()));
        }

        for (int r=row-1; r <= row+1 && r < gridWidth; r++) {
            for (int col=column-1; col <= column+1 && col < gridHeight; col++) {
                if (r >= 0 && col>=0 && !nodes2D.get(r).get(col).visited && queue.word().length() < maxAmountLetters) {
                    recursivelyFindWords(nodes2D, r, col, queue);
                }
            }
        }

        queue.removeLast();
        activeNode.visited = false;
    }
}