package com.kirkbyo;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ozziekirkby on 2016-12-03.
 * Utility methods for the grid.
 */
public class GridUtilities {
    /**
     * Determines the amount of possibilities of word combinations depending on the grid size. Note: this only includes
     * the combinations of 8 letters or less. Also, this function only supports grid types of 3x3 to a 6x6.
     * TODO: Create a math function to determine the amount of possibilities mathematically over the current solution of
     * using the pre calculated constant numbers.
     * @param rows: amount of rows in grid
     * @param columns // amount of columns in grid
     * @return Amount of possibilities
     */
     public int amountOfPossibilities(int rows, int columns) {
         if (rows == columns) {
             switch (rows) {
                 case 3: return 9521; // 3x3
                 case 4: return 283400; // 4x4
                 case 5: return 1502417; // 5x5
                 case 6: return 4113192; // 6x6
                 default: return 1;
             }
         } else if ((rows == 3 || rows == 4) && (columns == 3 || columns == 4)) { // 4x3
             return 60984;
         } else if ((rows == 5 || rows == 4) && (columns == 5 || columns == 4)) { // 5x4
             return 678600;
         } else if ((rows == 5 || rows == 6) && (columns == 5 || columns == 6)) { // 6x5
             return 2510808;
         } else if ((rows == 6 || rows == 3) && (columns == 6 || columns == 3)) { // 6x3
             return 320228;
         } else if ((rows == 5 || rows == 3) && (columns == 5 || columns == 3)) { // 5x3
             return 170357;
         } else if ((rows == 4 || rows == 6) && (columns == 4 || columns == 6)) { // 6x4
             return 1174676;
         }
         return 1;
     }

    /**
     * Creates an array with empty strings inside.
     * Example:
     arrayWithEmptyStrings(1) -> [""]
     arrayWithEmptyStrings(3) -> ["", "", ""]
     * @param amount: Amount of empty strings inside the array
     * @return String[] containing the empty strings.
     */
    public String[] arrayWithEmptyStrings(int amount) {
        ArrayList<String> array = new ArrayList<String>();
        for (int i=0; i < amount; i++) { array.add(""); }
        return array.toArray(new String[array.size()]);
    }

    /**
     * Aligns table cells content
     * @param table for cells to be adjusted within
     * @param alignment of the cell
     */
    public void setCellsAlignment(JTable table, int alignment) {
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer(); // Creates a new cell render
        rightRenderer.setHorizontalAlignment(alignment); // Adjust the text alignment of the cells
        TableModel tableModel = table.getModel(); // Gets the current table model
        // Loops over each column and applies the new cell renderer
        for (int columnIndex = 0; columnIndex < tableModel.getColumnCount(); columnIndex++) {
            table.getColumnModel().getColumn(columnIndex).setCellRenderer(rightRenderer);
        }
    }

    /**
     * Sets the editor for when adjusting the contents of a table cell
     * @param table to set new editor upon
     * @param font of the new  text editor
     */
    public void setTableTextEditorFontSize(JTable table, Font font) {
        JTextField exampleTextField = new JTextField(); // Creates a text field to be applied to each cell
        exampleTextField.setFont(font); // Sets the font for the new text field
        for (int i=0; i < table.getColumnModel().getColumnCount(); i++) {
            DefaultCellEditor cellEditor = new DefaultCellEditor(exampleTextField);
            table.getColumnModel().getColumn(i).setCellEditor(cellEditor);
        }
    }

    /**
     * Generates a random 2D array containing random letters
     * @param rowAmount Amount of rows in the array
     * @param columnAmount Amount of columns in the array
     * @return Object[][] containing the random letters
     */
    public Object[][] generateRandomLetterGrid(int rowAmount, int columnAmount) {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; // Alphabet
        ArrayList<ArrayList<Object>> grid = new ArrayList<ArrayList<Object>>(); // Generated Grid

        for (int row=0; row < rowAmount; row++) { // Creates new rows
            ArrayList<Object> rowArray = new ArrayList<Object>(); // New row array
            for (int column=0; column < columnAmount; column++) { // Creates new column
                rowArray.add(alphabet.charAt(randomInt(0, alphabet.length()))); // Adds a new random letter to the array
            }
            grid.add(rowArray);
        }

        // Converts ArrayList<ArrayList<Object>> to Object[][]
        Object[][] objectArray = new Object[grid.size()][]; // Creates the new array with the same size
        for (int i = 0; i < grid.size(); i++) { // Adds row to the new array
            ArrayList<Object> row = grid.get(i);
            objectArray[i] = row.toArray(new Object[row.size()]);
        }
        return objectArray;
    }

    /**
     * Generates a random number between the min and the max
     * @param min: the smallest possible number that can be returned
     * @param max: the biggest possible number that can be returned
     * @return random number
     */
    public int randomInt(int min, int max) { return new Random().nextInt(max) + min; }
}
