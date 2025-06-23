import java.sql.*;

public class DBHelper {

    // Load MySQL JDBC Driver
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Database connection details
    private static final String URL = "jdbc:mysql://localhost:3306/library_db?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "root";      // Change to your username if needed
    private static final String PASSWORD = "siddu@2005";      // Change to your MySQL password

    // Method to get DB connection
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Add a new book to the database
    public boolean addBook(String id, String title, String author, String publisher, String year, String isbn, String copies) {
        String query = "INSERT INTO books (book_id, title, author, publisher, year, isbn, copies) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, id);
            stmt.setString(2, title);
            stmt.setString(3, author);
            stmt.setString(4, publisher);
            stmt.setString(5, year);
            stmt.setString(6, isbn);
            stmt.setInt(7, Integer.parseInt(copies));
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Retrieve all books from the database
    public ResultSet getAllBooks() throws SQLException {
        String query = "SELECT * FROM books";
        Connection conn = getConnection();
        Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        return stmt.executeQuery(query);
    }

    // Update an existing book
    public boolean updateBook(String id, String title, String author, String publisher, String year, String isbn, String copies) {
        String query = "UPDATE books SET title=?, author=?, publisher=?, year=?, isbn=?, copies=? WHERE book_id=?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.setString(3, publisher);
            stmt.setString(4, year);
            stmt.setString(5, isbn);
            stmt.setInt(6, Integer.parseInt(copies));
            stmt.setString(7, id);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete a book by ID
    public boolean deleteBook(String id) {
        String query = "DELETE FROM books WHERE book_id=?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
