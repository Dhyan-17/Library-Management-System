import java.sql.*;
import java.util.*;

public class SearchBook {
    static Connection con;
    static Statement st;
    static Scanner sc = new Scanner(System.in);

    // Default constructor
    public SearchBook() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");

        // ✅ Change DB name here if needed
        String dburl = "jdbc:mysql://localhost:3306/book_rental";
        String dbuser = "root";
        String dbpass = "";
        con = DriverManager.getConnection(dburl, dbuser, dbpass);

        if (con != null) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM book_list ORDER BY rating");
            while (rs.next()) {
                String book_name = rs.getString("book_name");
                int book_isbn_no = rs.getInt("book_isbn_no");
                double rent_price = rs.getInt("rent_price");
                double rating = rs.getDouble("rating");
                int copies = rs.getInt("copies");

                SrcBook book = new SrcBook(book_isbn_no, book_name, rent_price, rating, copies);
                insert(book);
            }
        } else {
            System.out.println("Connection Failed...!!!");
        }
    }

    // BST Node
    class Node {
        SrcBook book;
        Node left, right;

        Node(SrcBook book) {
            this.book = book;
            left = right = null;
        }
    }

    static Node root = null;

    void insert(SrcBook book) {
        root = insertInto(root, book);
    }

    Node insertInto(Node root, SrcBook book) {
        if (root == null) return new Node(book);

        if (book.book_isbn_no < root.book.book_isbn_no) {
            root.left = insertInto(root.left, book);
        } else if (book.book_isbn_no > root.book.book_isbn_no) {
            root.right = insertInto(root.right, book);
        }
        return root;
    }

    public static boolean search(int bookNo) {
        int boxWidth = 85;

        try {
            Node result = searchbook(root, bookNo); // existing BST search

            System.out.println("+" + "-".repeat(boxWidth) + "+");
            System.out.printf("| %-83s |\n", "📖 BOOK SEARCH RESULT");
            System.out.println("+" + "-".repeat(12) + "+" + "-".repeat(30) + "+" + "-".repeat(12) + "+" + "-".repeat(10) + "+" + "-".repeat(12) + "+");
            System.out.printf("| %-10s | %-28s | %-10s | %-8s | %-10s |\n",
                    "ISBN", "Book Name", "Rent Price", "Rating", "Copies");
            System.out.println("+" + "-".repeat(12) + "+" + "-".repeat(30) + "+" + "-".repeat(12) + "+" + "-".repeat(10) + "+" + "-".repeat(12) + "+");
            if (result != null) {
                String name = result.book.book_name != null ? result.book.book_name : "N/A";

                System.out.printf("| %-10d | %-28s | %-10.2f | %-8.1f | %-10d |\n",
                        result.book.book_isbn_no,   // ISBN (int)
                        name,                       // Book Name (String)
                        result.book.rent_price,     // Rent Price (float/double → %.2f)
                        result.book.rating,         // Rating (float → %.1f)
                        result.book.copies);        // Copies (int)

                System.out.println("+" + "-".repeat(12) + "+" + "-".repeat(30) + "+" + "-".repeat(12) + "+" + "-".repeat(10) + "+" + "-".repeat(12) + "+");                return true;
            } else {
                System.out.printf("| %-83s |\n", "❌ No book found with ISBN: " + bookNo);
                System.out.println("+" + "-".repeat(boxWidth) + "+");
                return false;
            }

        } catch (Exception e) {
            System.out.printf("| %-83s |\n", "❌ Error while searching book: " + e.getMessage());
            System.out.println("+" + "-".repeat(boxWidth) + "+");
            return false;
        }
    }




    public static Node searchbook(Node root, int bookNO) {
        if (root == null || root.book.book_isbn_no == bookNO) {
            return root;
        }
        if (bookNO < root.book.book_isbn_no) {
            return searchbook(root.left, bookNO);
        } else {
            return searchbook(root.right, bookNO);
        }
    }
}

class SrcBook {
    int book_isbn_no;
    String book_name;
    double rent_price;
    double rating;
    int copies;

    public SrcBook(int book_isbn_no, String book_name, double rent_price, double rating, int copies) {
        this.book_isbn_no = book_isbn_no;
        this.book_name = book_name;
        this.rent_price = rent_price;
        this.rating = rating;
        this.copies = copies;
    }
}
