import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import GUI.Book;

public class LibraryGUI {
    private JPanel rootPanel;
    private List<Book> books; // Define the 'books' List

    public LibraryGUI() {
        String[] columnNames = {"Name", "Author", "Publication Year", "Read Item"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);

        JButton addItemButton = new JButton("Add Item");
        JButton editItemButton = new JButton("Edit Item");
        JButton deleteItemButton = new JButton("Delete Item");

        addItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add your code to add a new row to the table here
            }
        });

        editItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add your code to edit the selected item here
            }
        });

        deleteItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add your code to delete the selected item here
            }
        });

        rootPanel = new JPanel(); // Initialize the rootPanel

        rootPanel.setLayout(new BorderLayout());
        rootPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addItemButton);
        buttonPanel.add(editItemButton);
        buttonPanel.add(deleteItemButton);

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

    private void displayBooksInTable(JTable table) { // Pass 'table' as a parameter
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        for (Book book : books) {
            Object[] rowData = {
                    book.getName(),
                    book.getAuthor(),
                    book.getPublicationYear(),
                    book.getReadItem()
            };
            tableModel.addRow(rowData);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("LibraryGUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        LibraryGUI libraryGUI = new LibraryGUI();
        frame.setContentPane(libraryGUI.rootPanel);
        frame.pack();
        frame.setSize(600, 400); // Adjust the size as needed
        frame.setVisible(true);
    }
}
