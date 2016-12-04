package com.kirkbyo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.Timer;

/**
 * Created by ozziekirkby on 2016-11-09.
 * Main Window UI
 */
public class Window extends JFrame {
    /* --- Properties --- */
    private int tableWidth = 500; // Width of the table containing all the letters
    private int windowHeight = 500; // Height of the window
    private int wordPanelWidth = 200; // Width of the panel containing all the words in the boggle
    private int rowAmount = 4; // Amount of rows in Boggle's
    private int columnAmount = 4; // Amount of columns in Boggle's
    private Boggle boggle = new Boggle(); // Logic code for the boggle
    private GridUtilities utilities = new GridUtilities(); // All the utilities for the table
    /* --- UI Elements --- */
    private JProgressBar progressBar; // Bar showing the progress of finding the words
    private DefaultTableModel letterTableModel; // Model for the table containing the letters
    private DefaultTableModel wordTableModel; // Model for the table containing the words
    private JTable letterTable = new JTable(); // Table of all the letters

    /* --- Window Methods --- */
    /**
     * Creates, prepares and lays out alll the UI elements in the view.
     */
    public void create() {
        this.setBackground(Color.white);
        // Found Boggle Words Panel
        JPanel wordPane = new JPanel();
        wordPane.setBackground(Color.decode("#f7f7f7"));
        wordPane.setPreferredSize(new Dimension(wordPanelWidth, windowHeight));

        // Solve Button
        JButton solveButton = new JButton("Solve");
        solveButton.setFocusPainted(false); // Removes Blue outline
        solveButton.addActionListener(findWordsAction); // Listen for button click
        solveButton.setPreferredSize(new Dimension(wordPanelWidth, 30));
        wordPane.add(solveButton, BorderLayout.NORTH);

        // Table contaning all the words
        JTable wordTable = new JTable();
        wordTable.setEnabled(false); // Disables selectability on the table
        wordTable.setFont(new Font("Sans-Serif", Font.PLAIN, 13));
        wordTable.setRowHeight(25);

        // Panel allowing the word table to scroll
        JScrollPane wordScrollPanel = new JScrollPane();
        wordScrollPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.lightGray)); // Sets border onlt to top
        wordScrollPanel.setPreferredSize(new Dimension(wordPanelWidth, windowHeight - 102));
        wordScrollPanel.setViewportView(wordTable);
        wordPane.add(wordScrollPanel);

        // Sets the table model
        wordTableModel = new DefaultTableModel(0, 1);
        wordTableModel.setColumnIdentifiers(new String[]{""});
        wordTable.setModel(wordTableModel);

        // Default Table set up for the table for the users to enter what letters the boggle contains
        Object[][] data = {
                {"K", "J", "S", "R"},
                {"O", "N", "O", "N"},
                {"L", "U", "Y", "D"},
                {"O", "D", "E", "R"},
        };

        // Letter Panel allowing the letters to scroll
        JScrollPane letterPane = new JScrollPane();
        letterPane.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.lightGray));
        letterPane.setViewportView(letterTable);
        letterPane.setPreferredSize(new Dimension(tableWidth, windowHeight));

        // Letter Table Model
        letterTableModel = new DefaultTableModel(data, new String[]{"", "", "", ""});
        // Sets the row to the estimated row height in order to fit each cell on the screen without scrolling
        letterTable.setRowHeight(estimatedRowHeight());
        letterTable.setModel(letterTableModel);
        letterTable.setFont(new Font("Sans-Serif", Font.PLAIN, 25));
        letterTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Sets selection to only one row
        letterTable.setSelectionBackground(Color.decode("#55acee"));
        utilities.setCellsAlignment(letterTable, SwingConstants.CENTER); // Aligns the contents of the cell to the centre
        // Adjusts the table text editor for when the user editing the cells content
        utilities.setTableTextEditorFontSize(letterTable, new Font("Sans-Serif", Font.PLAIN, 25));

        // Adds the views to the screen
        add(wordPane, BorderLayout.WEST);
        add(letterPane, BorderLayout.EAST);
        add(createLoaderHDD(), BorderLayout.SOUTH);

        // Window setup
        setSize(windowWidth(), windowHeight);
        setTitle("Boggle Solver");
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Creates panel giving the user customization options over the boggle board and displays the progress of the
     find the words.
     * @return Panel containing all the properties UI elements and the progress bar
     */
    private JPanel createLoaderHDD() {
        JPanel HDDPanel = new JPanel(); // Main Panel
        HDDPanel.setBackground(Color.decode("#e3e3e3"));
        HDDPanel.setLayout(new BorderLayout());
        HDDPanel.setBorder(BorderFactory.createMatteBorder(1,0,0,0, Color.lightGray));
        HDDPanel.setPreferredSize(new Dimension(windowWidth(), 40));

        // Panel contaning all the adjustable properties of the boggle
        JPanel properties = new JPanel();
        properties.setBackground(Color.decode("#e3e3e3"));
        properties.setPreferredSize(new Dimension(windowWidth(), 35));

        // Row Label
        properties.add(new JLabel("Rows: "));

        // Array of all possible grid options
        final String[] comboxBoxGridOptions = {"3", "4", "5", "6"};

        // Combo Box for the different row options
        final JComboBox rowComboBox = new JComboBox(comboxBoxGridOptions);
        rowComboBox.setSelectedIndex(1); // Sets it to the 4 row option as a default
        rowComboBox.addActionListener (new ActionListener() {
            public void actionPerformed(ActionEvent e) { // Event listener for when the combo box value changes
                // Sets the row amount from the combo box value
                rowAmount = Integer.valueOf(comboxBoxGridOptions[rowComboBox.getSelectedIndex()]);
                refreshGrid(); // Refreshes the grid to update the amount of rows in the table
                boggle.gridHeight = rowAmount; // Adjusts the height of boggle game also
            }
        });
        properties.add(rowComboBox);

        // Column Label
        properties.add(new JLabel("Column: "));

        // Combo box for the different column options
        final JComboBox columnComboBox = new JComboBox(comboxBoxGridOptions);
        columnComboBox.setSelectedIndex(1); // Sets it to the 4 column option as a default
        columnComboBox.addActionListener (new ActionListener() {
            public void actionPerformed(ActionEvent e) { // Event listener for when the combo box value changes
                columnAmount = Integer.valueOf(comboxBoxGridOptions[columnComboBox.getSelectedIndex()]);
                refreshGrid(); // Refreshes the grid to update the amount of rows in the table
                boggle.gridWidth = columnAmount; // Adjusts the height of boggle game also
            }
        });
        properties.add(columnComboBox);

        HDDPanel.add(properties, BorderLayout.NORTH);

        // Progress bar showing how much more word combinations need to be ran until the process is completed
        progressBar = new JProgressBar(0, 100);
        progressBar.setBorder(BorderFactory.createEmptyBorder());
        progressBar.setValue(0);
        HDDPanel.add(progressBar);

        return HDDPanel;
    }

    /**
     * Refreshes the table with the new amount of rows and columns.
     */
    private void refreshGrid() {
        // Updates the table data, by generating a 2D array containing random letters.
        // Reasoning for generating an empty array of strings: JTables need to have column identifiers, although if
        // you don't want any you just give it an empty string and the cell is not even rendered.
        letterTableModel.setDataVector(
                utilities.generateRandomLetterGrid(rowAmount, columnAmount),
                utilities.arrayWithEmptyStrings(columnAmount)
        );
        letterTable.setRowHeight(estimatedRowHeight()); // Adjusts the row height for the new changes
        utilities.setCellsAlignment(letterTable, SwingConstants.CENTER); // Centers the cell once again
        // Adjusts the font for when editing the cell
        utilities.setTableTextEditorFontSize(letterTable, new Font("Sans-Serif", Font.PLAIN, 25));
    }

    /**
     * Converts table to 2D array and then uses the boggle solver to find all the word possibilities.
     */
    private Action findWordsAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Grabs all the values from the table and creates a 2D array from it
            ArrayList<ArrayList<Character>> charactersTable = getTableCharacters();
            letterTable.setEnabled(false); // Turns off editing while finding words
            progressBar.setValue(0); // Resets the progress bar
            // Sets the max for the progress bar
            progressBar.setMaximum(utilities.amountOfPossibilities(rowAmount, columnAmount));
            // Resets the found words table
            wordTableModel.setRowCount(0);
            // Converts 2D Array List of characters to a 2D array of LetterNodes
            boggle.generateNodeArrayFrom(charactersTable);

            // Creates a new thread to find all the words.
            // Reasoning: Without opening another thread the program would stop responding when doing the bigger grids
            // (ie: 6x6). Also, the process of finding all the words would block the main thread in order to update the
            // progress bar.
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Set<String> foundWords = boggle.findWords(); // Begins finding all the words
                    Object[] foundWordsArray = foundWords.toArray(new String[foundWords.size()]); // Converts set to array
                    // Adds the words to the table
                    for (int i=0; i < foundWords.size(); i++) {
                        // For some reason the table would not add all the elements in the Object[], only the first item.
                        // So I now append each element into it's own array and then add that array to the table.
                        Vector<Object> data = new Vector<Object>();
                        data.add(foundWordsArray[i]);
                        wordTableModel.addRow(data);
                    }
                    letterTable.setEnabled(true); // Allows the letter table to be edited
                }
            }, "Boggle solver thread").start();
            // Reasoning for opening another thread to update the progress bar: Originally implemented the updating of the
            // progress bar as a callback that was called every time another word combinations was tried. This slowed down
            // the program significantly (2x to 3x slower). In order to maintain the efficiency of the program and show the
            // user that progress is still happening, the progress bar is updated every second with the amount of words that
            // have been processed.
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Timer timer = new Timer();
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            progressBar.setValue(boggle.currentWordCount);
                        }
                    }, 1000, 1000);
                }
            }, "UI Thread").start();
        }
    };

    /**
     * Creates a 2D array list of Characters from the letters table.
     * @return ArrayList<ArrayList<Character>>
     */
    private ArrayList<ArrayList<Character>> getTableCharacters() {
        // 2D array of characters
        ArrayList<ArrayList<Character>> charactersTable = new ArrayList<ArrayList<Character>>();

        for (int row=0; row < letterTable.getRowCount(); row++) { // Loops through all the rows in the table
            ArrayList<Character> rowCharacters = new ArrayList<Character>(); // Characters in a row
            for (int column=0; column < letterTable.getColumnCount(); column++) { // Loops through all the columns in the table
                try { // Tries to handle the input as a string, if type cast fails will use Character type as a backup
                    String character = (String) letterTable.getModel().getValueAt(row, column); // Converts input to string
                    // Adds the first letter of the string to the array (should only contain one letter in the string
                    // regardless)
                    rowCharacters.add(character.charAt(0));
                } catch (java.lang.ClassCastException error) { // Treats input as a character
                    Character character = (Character) letterTable.getModel().getValueAt(row, column);
                    rowCharacters.add(character);
                }
            }
            charactersTable.add(rowCharacters);
        }
        return charactersTable;
    }


    /**
     * Determines the panel width based on the width of the letter table and the word panel.
     * @return window height
     */
    private int windowWidth() { return (tableWidth + wordPanelWidth); }

    /**
     * Determines the row height based on the letter table height divided by the amount of rows in table.
     * @return the estimated row height
     */
    private int estimatedRowHeight() { return (windowHeight - 67) / rowAmount; }
}