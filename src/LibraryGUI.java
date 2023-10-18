import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import GUI.Book;

public class LibraryGUI {
    private JPanel rootPanel;
    private List<Book> books; // Define the 'books' List

    public LibraryGUI() {
        String[] columnNames = {"Name", "Author", "Publication Year", "Read Item", "Popularity Count"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);

        table.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer(table));

        JButton addItemButton = new JButton("Add Item");
        JButton editItemButton = new JButton("Edit Item");
        JButton deleteItemButton = new JButton("Delete Item");

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());

                if (row >= 0 && col == 3) {
                    // If the mouse is clicked on the "Read Item" button column
                    openBookForReading(row);
                } else {
                    // Handle row selection logic here
                    table.setSelectionBackground(Color.YELLOW);
                    table.setSelectionForeground(Color.BLACK);
                    table.setRowSelectionInterval(row, row);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    table.setSelectionBackground(Color.YELLOW);
                    table.setSelectionForeground(Color.BLACK);
                    table.setRowSelectionInterval(row, row);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                table.setSelectionBackground(table.getBackground());
            }
        });

// ... (your existing code)

        JButton showPopularityButton = new JButton("Show Popularity");

        showPopularityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Define the action when the button is clicked
                // For example, you can open a dialog to display popularity information.
                // Replace this with your logic.
                JOptionPane.showMessageDialog(rootPanel, "Showing Popularity Information.");
            }
        });

// ... (the rest of your code)

        addItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add a new book using a dialog box
                addBookDialog(table);
            }
        });

        editItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Edit the selected book using a dialog box
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    editBookDialog(selectedRow, table);
                } else {
                    JOptionPane.showMessageDialog(rootPanel, "Please select a book to edit.");
                }
            }
        });

        deleteItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Delete the selected book
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    tableModel.removeRow(selectedRow);
                } else {
                    JOptionPane.showMessageDialog(rootPanel, "Please select a book to delete.");
                }
            }
        });


        rootPanel = new JPanel(); // Initialize the rootPanel

        rootPanel.setLayout(new BorderLayout());
        rootPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addItemButton);
        buttonPanel.add(editItemButton);
        buttonPanel.add(deleteItemButton);
        buttonPanel.add(showPopularityButton);

        rootPanel.add(buttonPanel, BorderLayout.SOUTH);

        books = loadBooksFromFile("books.txt");
        displayBooksInTable(table); // Pass the 'table' to the method
    }

    private List<Book> loadBooksFromFile(String fileName) {
        List<Book> loadedBooks = new ArrayList<>();
        String fileContent = readFile(fileName);

        if (fileContent != null) {
            String[] bookLines = fileContent.split("\n");

            for (String bookLine : bookLines) {
                String[] elements = bookLine.split(",");
                if (elements.length == 7) {
                    loadedBooks.add(new Book(
                            Integer.parseInt(elements[0]),
                            Integer.parseInt(elements[1]),
                            elements[2],
                            elements[3],
                            Integer.parseInt(elements[4]),
                            Integer.parseInt(elements[5]),
                            Integer.parseInt(elements[6])
                    ));
                }
            }
        }

        return loadedBooks;
    }

    private String readFile(String fileName) {
        StringBuilder content = new StringBuilder();

        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }

            bufferedReader.close();
        } catch (IOException ignored) {
            return null;
        }

        return content.toString();
    }

    private void displayBooksInTable(JTable table) {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        for (Book book : books) {
            Object[] rowData = {
                    book.getName(),
                    book.getAuthor(),
                    book.getPublicationYear(),
                    book.getReadItem(),
                    book.getPopularityCount() // Add popularity count
            };
            tableModel.addRow(rowData);
        }
    }



    private void addBookDialog(JTable table) {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();

        JTextField titleField = new JTextField(20);
        JTextField authorField = new JTextField(20);
        JTextField yearField = new JTextField(4);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Author:"));
        panel.add(authorField);
        panel.add(new JLabel("Publication Year:"));
        panel.add(yearField);

        int result = JOptionPane.showConfirmDialog(rootPanel, panel, "Add a New Book",
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String title = titleField.getText();
            String author = authorField.getText();
            String year = yearField.getText();

            if (!title.isEmpty() && !author.isEmpty() && !year.isEmpty()) {
                Object[] rowData = {title, author, year, "Not Read"};
                tableModel.addRow(rowData);
            } else {
                JOptionPane.showMessageDialog(rootPanel, "Please fill in all fields.");
            }
        }
    }

    private void editBookDialog(int selectedRow, JTable table) {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        JTextField titleField = new JTextField((String) tableModel.getValueAt(selectedRow, 0));
        JTextField authorField = new JTextField((String) tableModel.getValueAt(selectedRow, 1));
        JTextField yearField = new JTextField(tableModel.getValueAt(selectedRow, 2).toString());

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Author:"));
        panel.add(authorField);
        panel.add(new JLabel("Publication Year:"));
        panel.add(yearField);

        int result = JOptionPane.showConfirmDialog(rootPanel, panel, "Edit Book Details",
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String title = titleField.getText();
            String author = authorField.getText();
            String year = yearField.getText();

            if (!title.isEmpty() && !author.isEmpty() && !year.isEmpty()) {
                // Ensure you're updating the table with appropriate data types
                tableModel.setValueAt(title, selectedRow, 0);
                tableModel.setValueAt(author, selectedRow, 1);
                tableModel.setValueAt(year, selectedRow, 2);
            } else {
                JOptionPane.showMessageDialog(rootPanel, "Please fill in all fields.");
            }
        }
    }


    class ButtonRenderer implements TableCellRenderer {
        private JButton button;
        private JTable table;

        public ButtonRenderer(JTable table) {
            this.table = table;
            button = new JButton("Read Book");
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow >= 0) {
                        // Get the current popularity count
                        int currentCount = (int) table.getValueAt(selectedRow, 4);
                        // Increment the popularity count by 1
                        table.setValueAt(currentCount + 1, selectedRow, 4);
                        ((DefaultTableModel) table.getModel()).fireTableCellUpdated(selectedRow, 4); // Notify the model
                        openBookForReading(selectedRow);
                    }
                }
            });
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(UIManager.getColor("Button.background"));
            }

            return button;
        }
    }



    private void openBookForReading(int row) {
        String bookContent = books.get(row).getReadItem();

        JFrame readingFrame = new JFrame("Read Book");
        readingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea textArea = new JTextArea(bookContent);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setCaretPosition(0);

        JScrollPane scrollPane = new JScrollPane(textArea);

        readingFrame.getContentPane().add(scrollPane);
        readingFrame.pack();
        readingFrame.setSize(600, 400);
        readingFrame.setVisible(true);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("LibraryGUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        LibraryGUI libraryGUI = new LibraryGUI();
        frame.setContentPane(libraryGUI.rootPanel);
        frame.pack();
        frame.setSize(600, 400); // Adjust the size as needed
        frame.setVisible(true);

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(frame, "Do you want to close the window?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    frame.dispose();
                }
            }
        });
    }
}