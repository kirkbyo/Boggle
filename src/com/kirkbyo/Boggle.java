package com.kirkbyo;

import com.sun.xml.internal.ws.util.StringUtils;
import java.lang.*;
import java.util.*;

/**
 * Solver for Boggle Grids of varying length
 */
public class Boggle {
    /* --- Properties --- */
    public int maxAmountLetters = 8; // Max amount of letters to be found from grid
    public int minimumAmountLetters = 3; // Minimum amount of letter to be found from grid
    public int gridWidth = 4; // Width of the grid
    public int gridHeight = 4; // Height of the grid
    public int currentWordCount = 0; // Amount of words already processed by the solver. (Used to update the progress bar)
    public Set<String> foundWords = new HashSet<String>(); // Set containing all the found words in the boggle

    /* --- Utilities --- */
    // Search letter combinations against a text file containing a large amount of popular words
    private WordSearch wordSeacher = new WordSearch();
    // 2D Array list of nodes
    private ArrayList<ArrayList<LetterNode>> nodes = null;

    /* --- Methods --- */
    /**
     * Creates a 2D array of Letter nodes in order to traverse it
     * @param charMatrix 2D array of character that will be converted to letter nodes
     */
    public void generateNodeArrayFrom(ArrayList<ArrayList<Character>> charMatrix) {
        ArrayList<ArrayList<LetterNode>> nodes = new ArrayList<ArrayList<LetterNode>>(); // 2D array of nodes

        // Converts all the characters to letter nodes
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

    /**
     * Find the words within the Boggle. Warning: the amount of words found is dependent on the text file, if the letter
     * combination is not contained within the text file it will not count as a word. So this function might not find all the words
     * because the text file does not contain every word in the english language.
     * This solution was inspired by http://www.geeksforgeeks.org/boggle-find-possible-words-board-characters/
     * @return Set contaning all the found words
     */
    public Set<String> findWords() {
        currentWordCount=0; // Resets the word count
        foundWords.clear(); // Clears the found words
        // Loops through each node and tries every possible combination
        for (int i=0; i < gridHeight; i++) {
            for (int j = 0; j < gridWidth; j++) {
                recursivelyFindWords(nodes, i, j, new WordQueue());
            }
        }
        // Sanitizes the found words (removes all the words that contain two letters or less)
        sanitizeFoundWords();

        return foundWords;
    }

    /**
     * Sanitizes the found words. ie: removes all the words that contain two letters or less
     */
    private void sanitizeFoundWords() {
        Set<String> cleanArray = ((Set) ((HashSet) foundWords).clone()); // Clones the set
        for (String word: foundWords) { // Loops over each word
            // If the word is smaller then two letters it gets removed from the set
            if (word.length() <= minimumAmountLetters - 1) {
                cleanArray.remove(word);
            }
        }
        foundWords = cleanArray; // Sets the clean array to the found words
    }

    /**
     * Recursively finds all the possible letter combinations and checks to see if it's a word.
     * @param nodes2D: 2D array of nodes
     * @param row location of the next node
     * @param column location of the next node
     * @param queue: Queue of letter (current letter combination)
     */
    private void recursivelyFindWords(ArrayList<ArrayList<LetterNode>> nodes2D, int row, int column, WordQueue queue) {
        currentWordCount++; // Increases the word count
        LetterNode activeNode = nodes2D.get(row).get(column); // Gets the current node
        activeNode.visited = true; // Sets it to visited
        queue.insert(activeNode.character); // Inserts letter into letter combination queue

        if (wordSeacher.fileContainsWord(queue.word())) { // Checks to see if the queue forms a words
            foundWords.add(StringUtils.capitalize(queue.word().toLowerCase()));
        }

        // Uses a traversing algorithm to try every possible letter combination
        for (int r=row-1; r <= row+1 && r < gridHeight; r++) {
            for (int col=column-1; col <= column+1 && col < gridWidth; col++) {
                // Ensures the next cell is within range, has not been visited and is less then the max amount of letters
                if (r >= 0 && col>=0 && !nodes2D.get(r).get(col).visited && queue.word().length() < maxAmountLetters) {
                    recursivelyFindWords(nodes2D, r, col, queue);
                }
            }
        }
        queue.removeLast(); // Removes the last letter from the queue
        activeNode.visited = false; // Resets state
    }
}