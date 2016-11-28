package com.kirkbyo;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Set;

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
    private DefaultTableModel letterTableModel;
    private DefaultTableModel wordTableModel;
    private JTable letterTable = new JTable();

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
        JScrollPane wordScrollPanel = new JScrollPane();
        wordScrollPanel.setPreferredSize(new Dimension(wordPanelWidth, windowHeight - 30));

        wordTableModel = new DefaultTableModel(0, 1);
        wordTableModel.setColumnIdentifiers(new String[]{"Words"});
        wordTable.setModel(wordTableModel);
        wordScrollPanel.setViewportView(wordTable);
        wordPane.add(wordScrollPanel);

        // Table set up for the table for the users to enter what letters the boggle contains
        Object[][] data = {
                {"A", "B", "C", "D"},
                {"E", "F", "G", "H"},
                {"I", "J", "K", "L"},
                {"M", "N", "O", "P"},
        };


        letterTableModel = new DefaultTableModel(data, new String[]{"", "", "", ""});
        letterTable.setRowHeight(estimatedRowHeight());
        letterTable.setModel(letterTableModel);
        letterTable.setFont(new Font("Sans-Serif", Font.PLAIN, 25));
        setCellsAlignment(letterTable, SwingConstants.CENTER);
        letterPane.setViewportView(letterTable);
        letterPane.setPreferredSize(new Dimension(tableWidth, windowHeight));

        add(wordPane, BorderLayout.WEST);
        add(letterPane, BorderLayout.EAST);

        // Window setup
        setSize((tableWidth + wordPanelWidth), windowHeight);
        setTitle("Boggle Solver");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
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

    private Action findWordsAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ArrayList<ArrayList<Character>> charactersTable = new ArrayList<ArrayList<Character>>();
            for (int row=0; row < letterTable.getRowCount(); row++) {
                ArrayList<Character> rowCharacters = new ArrayList<Character>();
                for (int column=0; column < letterTable.getColumnCount(); column++) {
                    String character = (String) letterTable.getModel().getValueAt(row, column);
                    rowCharacters.add(character.charAt(0));
                }
                charactersTable.add(rowCharacters);
            }

            Character[][] array = new Character[charactersTable.size()][];
            for (int i = 0; i < charactersTable.size(); i++) {
                ArrayList<Character> row = charactersTable.get(i);
                array[i] = row.toArray(new Character[row.size()]);
            }

            Set<LetterNode> nodes = new Boggle().generateNodeSetFrom(array);
            System.out.println(nodes.toString());
            System.out.println(nodes.size());
        }
    };

    // Estimates the row height in order to fill the entire window.
    private int estimatedRowHeight() {
        return (windowHeight - 27) / rowAmount;
    }
}