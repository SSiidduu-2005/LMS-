import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Library_management extends JFrame implements ActionListener {
    private JLabel label1, label2, label3, label4, label5, label6, label7;
    private JTextField textField1, textField2, textField3, textField4, textField5, textField6, textField7;
    private JButton addButton, viewButton, editButton, deleteButton, clearButton, exitButton;
    private JPanel panel;

    private DBHelper dbHelper;

    public Library_management() {
        dbHelper = new DBHelper();

        setTitle("Library Management System");
        setSize(600, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        label1 = new JLabel("Book ID");
        label2 = new JLabel("Book Title");
        label3 = new JLabel("Author");
        label4 = new JLabel("Publisher");
        label5 = new JLabel("Year of Publication");
        label6 = new JLabel("ISBN");
        label7 = new JLabel("Number of Copies");

        textField1 = new JTextField(10);
        textField2 = new JTextField(20);
        textField3 = new JTextField(20);
        textField4 = new JTextField(20);
        textField5 = new JTextField(10);
        textField6 = new JTextField(20);
        textField7 = new JTextField(10);

        addButton = new JButton("Add");
        viewButton = new JButton("View");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        clearButton = new JButton("Clear");
        exitButton = new JButton("Exit");

        addButton.addActionListener(this);
        viewButton.addActionListener(this);
        editButton.addActionListener(this);
        deleteButton.addActionListener(this);
        clearButton.addActionListener(this);
        exitButton.addActionListener(this);

        panel = new JPanel(new GridLayout(10, 2));
        panel.add(label1); panel.add(textField1);
        panel.add(label2); panel.add(textField2);
        panel.add(label3); panel.add(textField3);
        panel.add(label4); panel.add(textField4);
        panel.add(label5); panel.add(textField5);
        panel.add(label6); panel.add(textField6);
        panel.add(label7); panel.add(textField7);
        panel.add(addButton); panel.add(viewButton);
        panel.add(editButton); panel.add(deleteButton);
        panel.add(clearButton); panel.add(exitButton);

        add(panel);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            boolean success = dbHelper.addBook(
                textField1.getText(), textField2.getText(), textField3.getText(),
                textField4.getText(), textField5.getText(), textField6.getText(),
                textField7.getText()
            );
            if (success) {
                JOptionPane.showMessageDialog(this, "Book added successfully");
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add book");
            }
        } else if (e.getSource() == viewButton) {
            try {
                ResultSet rs = dbHelper.getAllBooks();
                JTable table = new JTable(buildTableModel(rs));
                JScrollPane scrollPane = new JScrollPane(table);
                JFrame frame = new JFrame("View Books");
                frame.add(scrollPane);
                frame.setSize(800, 400);
                frame.setVisible(true);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error displaying books");
            }
        } else if (e.getSource() == editButton) {
            String id = JOptionPane.showInputDialog(this, "Enter Book ID to edit:");
            boolean success = dbHelper.updateBook(
                id, textField2.getText(), textField3.getText(),
                textField4.getText(), textField5.getText(),
                textField6.getText(), textField7.getText()
            );
            JOptionPane.showMessageDialog(this, success ? "Book updated" : "Update failed");
            clearFields();
        } else if (e.getSource() == deleteButton) {
            String id = JOptionPane.showInputDialog(this, "Enter Book ID to delete:");
            boolean success = dbHelper.deleteBook(id);
            JOptionPane.showMessageDialog(this, success ? "Book deleted" : "Delete failed");
            clearFields();
        } else if (e.getSource() == clearButton) {
            clearFields();
        } else if (e.getSource() == exitButton) {
            System.exit(0);
        }
    }

    private void clearFields() {
        textField1.setText(""); textField2.setText(""); textField3.setText("");
        textField4.setText(""); textField5.setText(""); textField6.setText(""); textField7.setText("");
    }

    // Utility: Convert ResultSet to TableModel with friendly column headers
    public static javax.swing.table.TableModel buildTableModel(ResultSet rs) throws Exception {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Map SQL column names to user-friendly column headers
        String[] friendlyColumnNames = new String[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            String colName = metaData.getColumnName(i);
            switch (colName.toLowerCase()) {
                case "book_id": friendlyColumnNames[i - 1] = "Book ID"; break;
                case "title": friendlyColumnNames[i - 1] = "Book Title"; break;
                case "author": friendlyColumnNames[i - 1] = "Author"; break;
                case "publisher": friendlyColumnNames[i - 1] = "Publisher"; break;
                case "year": friendlyColumnNames[i - 1] = "Year of Publication"; break;
                case "isbn": friendlyColumnNames[i - 1] = "ISBN"; break;
                case "copies": friendlyColumnNames[i - 1] = "Number of Copies"; break;
                default: friendlyColumnNames[i - 1] = colName;
            }
        }

        java.util.Vector<String[]> data = new java.util.Vector<>();
        while (rs.next()) {
            String[] row = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                row[i - 1] = rs.getString(i);
            }
            data.add(row);
        }

        Object[][] array = new Object[data.size()][columnCount];
        for (int i = 0; i < data.size(); i++) {
            array[i] = data.get(i);
        }

        return new javax.swing.table.DefaultTableModel(array, friendlyColumnNames);
    }

    public static void main(String[] args) {
        new Library_management();
    }
}
