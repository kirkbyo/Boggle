package com.kirkbyo;

import java.lang.*;
import java.util.*;

/**
 * Created by ozziekirkby on 2016-11-09.
 */
public class Boggle {
    int maxAmountLetters = 8;
    int minimumAmountLetters = 3;

    Set<LetterNode> nodes = null;

    LetterNode getValueIfExists(ArrayList<ArrayList<LetterNode>> array2D, int row, int column) {
        try {
            LetterNode value = array2D.get(row).get(column);
            return value;
        } catch (IndexOutOfBoundsException error) {
            return null;
        }
    }

    Set<LetterNode> generateNodeSetFrom(Character[][] charMatrix) {
        ArrayList<ArrayList<LetterNode>> unlinkedNodes = new ArrayList<ArrayList<LetterNode>>();
        Set<LetterNode> nodeSet = new HashSet<LetterNode>();

        for (Character[] row: charMatrix) {
            ArrayList<LetterNode> rowCharacters = new ArrayList<LetterNode>();
            for (char character: row) {
                LetterNode node = new LetterNode(character);
                rowCharacters.add(node);
            }
            unlinkedNodes.add(rowCharacters);
        }

        for (int row=0; row < unlinkedNodes.size(); row++) {
            ArrayList<LetterNode> rowNodes = unlinkedNodes.get(row);

            for (int column=0; column < rowNodes.size(); column++) {
                LetterNode node = rowNodes.get(column);
                node.left = getValueIfExists(unlinkedNodes, row, column - 1);
                node.right = getValueIfExists(unlinkedNodes, row, column + 1);
                node.top = getValueIfExists(unlinkedNodes, row + 1, column);
                node.bottom = getValueIfExists(unlinkedNodes, row - 1, column);
                node.topLeftDiagonal= getValueIfExists(unlinkedNodes, row + 1, column - 1);
                node.topRightDiagonal= getValueIfExists(unlinkedNodes, row + 1, column + 1);
                node.bottomLeftDiagonal = getValueIfExists(unlinkedNodes, row - 1, column - 1);
                node.bottomRightDiagonal = getValueIfExists(unlinkedNodes, row - 1, column + 1);
                nodeSet.add(node);
            }
        }

        return nodeSet;
    }

    String[] findWords() {

        for (LetterNode node: nodes) {
            WordQueue queue = new WordQueue();
            queue.insert(node.character);
            if (node.left != null) {

            }
        }


        return new String[]{ "Hello Word" };
    }
}