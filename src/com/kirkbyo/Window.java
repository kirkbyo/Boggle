package com.kirkbyo;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.ProgressBarUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.Timer;
import java.util.concurrent.Callable;

/**
 * Created by ozziekirkby on 2016-11-09.
 */
public class Window extends JFrame {
    // Properties
    public int tableWidth = 500;
    public int windowHeight = 500;
    private int wordPanelWidth = 200;
    public int rowAmount = 4;
    public int columnAmount = 4;
    Boggle boggle = new Boggle();

    private JProgressBar progressBar;
    private DefaultTableModel letterTableModel;
    private DefaultTableModel wordTableModel;
    private JTable letterTable = new JTable();
    private LetterGridUtilities gridUtilities = new LetterGridUtilities();

    /* --- Window Methods --- */
    public void create() {
        // Letter Panel
        JScrollPane letterPane = new JScrollPane();

        // Found Boggle Words Panel
        JPanel wordPane = new JPanel();
        wordPane.setPreferredSize(new Dimension(wordPanelWidth, windowHeight));

        // Solve Button®
        JButton solveButton = new JButton("Solve");
        solveButton.addActionListener(findWordsAction);
        solveButton.setPreferredSize(new Dimension(wordPanelWidth, 30));
        wordPane.add(solveButton, BorderLayout.NORTH);

        // Table contaning all the words
        JTable wordTable = new JTable();
        wordTable.setDefaultEditor(Object.class, null);
        wordTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        wordTable.setFont(new Font("Sans-Serif", Font.PLAIN, 13));
        wordTable.setRowHeight(25);

        JScrollPane wordScrollPanel = new JScrollPane();
        wordScrollPanel.setPreferredSize(new Dimension(wordPanelWidth, windowHeight - 102));

        wordTableModel = new DefaultTableModel(0, 1);
        wordTableModel.setColumnIdentifiers(new String[]{"Words"});
        wordTable.setModel(wordTableModel);
        wordScrollPanel.setViewportView(wordTable);
        wordPane.add(wordScrollPanel);

        // Table set up for the table for the users to enter what letters the boggle contains
        Object[][] data = {
                {"K", "J", "S", "R"},
                {"O", "N", "O", "N"},
                {"L", "U", "Y", "D"},
                {"O", "D", "E", "R"},
        };


        letterTableModel = new DefaultTableModel(data, new String[]{"", "", "", ""});
        letterTable.setRowHeight(estimatedRowHeight());
        letterTable.setModel(letterTableModel);
        letterTable.setFont(new Font("Sans-Serif", Font.PLAIN, 25));
        letterTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setCellsAlignment(letterTable, SwingConstants.CENTER);
        letterPane.setViewportView(letterTable);
        letterPane.setPreferredSize(new Dimension(tableWidth, windowHeight));

        add(wordPane, BorderLayout.WEST);
        add(letterPane, BorderLayout.EAST);
        add(createLoaderHDD(), BorderLayout.SOUTH);

        // Window setup
        setSize(windowWidth(), windowHeight);
        setTitle("Boggle Solver");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JPanel createLoaderHDD() {
        JPanel HDDPanel = new JPanel();
        HDDPanel.setLayout(new BorderLayout());
        HDDPanel.setPreferredSize(new Dimension(windowWidth(), 40));

        JPanel properties = new JPanel();
        properties.setPreferredSize(new Dimension(windowWidth(), 35));

        JLabel rowLabel = new JLabel();
        rowLabel.setText("Rows: ");
        properties.add(rowLabel);

        final String[] comboxBoxGridOptions = {"3", "4", "5", "6"};
        final JComboBox rowComboBox = new JComboBox(comboxBoxGridOptions);
        rowComboBox.setSelectedIndex(1);
        rowComboBox.addActionListener (new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rowAmount = Integer.valueOf(comboxBoxGridOptions[rowComboBox.getSelectedIndex()]);
                refreshGrid();
                boggle.gridHeight = rowAmount;
            }
        });
        properties.add(rowComboBox);

        JLabel columnLabel = new JLabel();
        columnLabel.setText("Column: ");
        properties.add(columnLabel);

        final JComboBox columnComboBox = new JComboBox(comboxBoxGridOptions);
        columnComboBox.setSelectedIndex(1);
        columnComboBox.addActionListener (new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                columnAmount = Integer.valueOf(comboxBoxGridOptions[columnComboBox.getSelectedIndex()]);
                refreshGrid();
                boggle.gridWidth = columnAmount;
            }
        });
        properties.add(columnComboBox);

        HDDPanel.add(properties, BorderLayout.SOUTH);

        progressBar = new JProgressBar(0, 100);
        progressBar.setBorder(BorderFactory.createEmptyBorder());
        progressBar.setValue(0);
        HDDPanel.add(progressBar);

        return HDDPanel;
    }

    /* setCellsAlignment: Aligns table cells content
    JTable table: table for cells to be adjusted within
    int alignment: of the cell
     */
    public void setCellsAlignment(JTable table, int alignment) {
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(alignment);

        TableModel tableModel = table.getModel();

        for (int columnIndex = 0; columnIndex < tableModel.getColumnCount(); columnIndex++) {
            table.getColumnModel().getColumn(columnIndex).setCellRenderer(rightRenderer);
        }
    }

    private Object[][] generateRandomLetterGrid() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        ArrayList<ArrayList<Object>> grid = new ArrayList<ArrayList<Object>>();

        for (int row=0; row < rowAmount; row++) {
            ArrayList<Object> rowArray = new ArrayList<Object>();
            for (int column=0; column < columnAmount; column++) {
                rowArray.add(alphabet.charAt(randomInt(0, alphabet.length())));
            }
            grid.add(rowArray);
        }

        Object[][] objectArray = new Object[grid.size()][];
        for (int i = 0; i < grid.size(); i++) {
            ArrayList<Object> row = grid.get(i);
            objectArray[i] = row.toArray(new Object[row.size()]);
        }
        return objectArray;
    }

    private void refreshGrid() {
        letterTableModel.setDataVector(generateRandomLetterGrid(), arrayWithEmptyStrings(columnAmount));
        letterTable.setRowHeight(estimatedRowHeight());
        setCellsAlignment(letterTable, SwingConstants.CENTER);
    }

    private String[] arrayWithEmptyStrings(int amount) {
        ArrayList<String> array = new ArrayList<String>();
        for (int i=0; i < amount; i++) { array.add(""); }
        return array.toArray(new String[array.size()]);
    }

    private Action findWordsAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ArrayList<ArrayList<Character>> charactersTable = new ArrayList<ArrayList<Character>>();
            for (int row=0; row < letterTable.getRowCount(); row++) {
                ArrayList<Character> rowCharacters = new ArrayList<Character>();
                for (int column=0; column < letterTable.getColumnCount(); column++) {
                    try {
                        String character = (String) letterTable.getModel().getValueAt(row, column);
                        rowCharacters.add(character.charAt(0));
                    } catch (java.lang.ClassCastException error) {
                        Character character = (Character) letterTable.getModel().getValueAt(row, column);
                        rowCharacters.add(character);
                    }
                }
                charactersTable.add(rowCharacters);
            }

            progressBar.setValue(0);
            progressBar.setMaximum(gridUtilities.amountOfPossibilities(rowAmount, columnAmount));
            boggle.generateNodeArrayFrom(charactersTable);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Set<String> foundWords = boggle.findWords();
                    Object[] foundWordsArray = foundWords.toArray(new String[foundWords.size()]);

                    for (int i=0; i < foundWords.size(); i++) {
                        Vector<Object> data = new Vector<Object>();
                        data.add(foundWordsArray[i]);
                        wordTableModel.addRow(data);
                    }
                }
            }, "Boggle solver thread").start();

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

    // Generates a random number between the min and the max
    public int randomInt(int min, int max) { return new Random().nextInt(max) + min; }

    private int windowWidth() { return (tableWidth + wordPanelWidth); }
    // Estimates the row height in order to fill the entire window.
    private int estimatedRowHeight() { return (windowHeight - 67) / rowAmount; }
}