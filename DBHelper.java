import java.sql.*;
import java.util.ArrayList;

public class DBHelper {
    private static final String URL = "jdbc:mysql://localhost:3306/library_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "password@2001";  // your MySQL password

    private Connection conn;

    public DBHelper() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Connected to MySQL!");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found.", e);
        }
    }

    public boolean addBook(int id, String title, String author, String publisher, int year, String isbn, int copies) throws SQLException {
        String sql = "INSERT INTO book VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setString(2, title);
            stmt.setString(3, author);
            stmt.setString(4, publisher);
            stmt.setInt(5, year);
            stmt.setString(6, isbn);
            stmt.setInt(7, copies);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean updateBook(int id, String title, String author, String publisher, int year, String isbn, int copies) throws SQLException {
        String sql = "UPDATE book SET title=?, author=?, publisher=?, year=?, isbn=?, copies=? WHERE book_id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.setString(3, publisher);
            stmt.setInt(4, year);
            stmt.setString(5, isbn);
            stmt.setInt(6, copies);
            stmt.setInt(7, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteBook(int id) throws SQLException {
        String sql = "DELETE FROM book WHERE book_id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public ArrayList<String[]> getAllBooks() throws SQLException {
        ArrayList<String[]> list = new ArrayList<>();
        String sql = "SELECT * FROM book";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String[] book = {
                    String.valueOf(rs.getInt("book_id")),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("publisher"),
                    String.valueOf(rs.getInt("year")),
                    rs.getString("isbn"),
                    String.valueOf(rs.getInt("copies"))
                };
                list.add(book);
            }
        }
        return list;
    }
}
