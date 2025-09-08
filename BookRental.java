import java.util.InputMismatchException;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class BookRental {
    static java.sql.Connection con;
    static Statement st;
    static int comparation;
    private static Scanner sc = new Scanner(System.in);
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public static void connectDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/book_rental", "root", "");
            System.out.println("✅ Database Connected Successfully!");
        } catch (Exception e) {
            System.out.println("❌ DB Connection Failed: " + e.getMessage());
        }
    }

    public static void bookForRent(String username) {
        connectDB();
        int boxWidth = 90;

        try {
            System.out.println("+" + "-".repeat(boxWidth) + "+");
            System.out.printf("| %-86s |\n", "📖 RENT A BOOK");
            System.out.println("+" + "-".repeat(boxWidth) + "+");

            // Step 1: Enter ISBN
            System.out.print("Enter Book ISBN No (6 digits): ");
            String input = sc.nextLine().trim();
            if (!input.matches("\\d{6}")) {
                System.out.println("❌ Invalid ISBN! Must be 6 digits.");
                return;
            }
            int bookNo = Integer.parseInt(input);

            // Step 2: Check book availability
            String checkSql = "SELECT * FROM book_list WHERE book_isbn_no = ?";
            try (PreparedStatement pstmt = con.prepareStatement(checkSql)) {
                pstmt.setInt(1, bookNo);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println("+" + "-".repeat(boxWidth) + "+");
                        System.out.printf("| %-86s |\n", "❌ Book not found!");
                        System.out.println("+" + "-".repeat(boxWidth) + "+");
                        return;
                    }

                    String bookName = rs.getString("book_name");
                    double rentPrice = rs.getDouble("rent_price");
                    int copies = rs.getInt("copies");

                    if (copies <= 0) {
                        System.out.println("+" + "-".repeat(boxWidth) + "+");
                        System.out.printf("| %-86s |\n", "❌ No copies available for rent.");
                        System.out.println("+" + "-".repeat(boxWidth) + "+");
                        return;
                    }

                    // Step 3: Ask for copies
                    int reqCopies = 1;
                    System.out.print("How many copies you want (Available: " + copies + "): ");

                    try {
                        reqCopies = Integer.parseInt(sc.nextLine().trim());
                    } catch (Exception e) {
                        System.out.println("❌ Invalid number of copies!");
                        return;
                    }

                    if (reqCopies <= 0 || reqCopies > copies) {
                        System.out.println("⚠ Copy not available.");
                        return;
                    }

                    // Step 4: Ask for rental days
                    System.out.print("Enter rental days (1 - 30): ");
                    int rentalDays;
                    try {
                        rentalDays = Integer.parseInt(sc.nextLine().trim());
                    } catch (Exception e) {
                        System.out.println("❌ Invalid rental days!");
                        return;
                    }
                    if (rentalDays <= 0 || rentalDays > 30) {
                        System.out.println("⚠ Rental days must be between 1 and 30.");
                        return;
                    }

                    LocalDate rentDate = LocalDate.now();
                    LocalDate returnDate = rentDate.plusDays(rentalDays);

                    // Step 5: Payment process (✅ Correct Formula)
                    double totalRent = rentPrice * reqCopies * rentalDays;
                    processPayment(totalRent, rentalDays, rentPrice, reqCopies);

                    // Step 6: Insert rental record
                    String insertSql =
                            "INSERT INTO book_rentals (book_isbn_no, user_name, rent_date, return_date, return_status, copies, total_amount) " +
                                    "VALUES (?, ?, ?, ?, 'NOT_RETURNED', ?, ?)";

                    con.setAutoCommit(false);
                    try (PreparedStatement ins = con.prepareStatement(insertSql)) {
                        ins.setInt(1, bookNo);
                        ins.setString(2, username);
                        ins.setDate(3, java.sql.Date.valueOf(rentDate));
                        ins.setDate(4, java.sql.Date.valueOf(returnDate));
                        ins.setInt(5, reqCopies);
                        ins.setDouble(6, totalRent);

                        int rows = ins.executeUpdate();
                        if (rows > 0) {
                            // Update book copies
                            String updateCopies = "UPDATE book_list SET copies = copies - ? WHERE book_isbn_no = ?";
                            try (PreparedStatement upd = con.prepareStatement(updateCopies)) {
                                upd.setInt(1, reqCopies);
                                upd.setInt(2, bookNo);
                                upd.executeUpdate();
                            }

                            con.commit();
                            System.out.println("+" + "-".repeat(boxWidth) + "+");
                            System.out.printf("| %-86s |\n", "✅ BOOK RENTED SUCCESSFULLY!");
                            System.out.println("+" + "-".repeat(boxWidth) + "+");

                            // Rental Summary
//                            System.out.println("+----------+----------------------------+-----------+------------+------------+--------+");
//                            System.out.printf("| %-8s | %-26s | %-9s | %-10s | %-10s | %-6s |\n",
//                                    "ISBN", "Book Name", "Rent/Day", "Rent Date", "Return Date", "Copies");
//                            System.out.println("+----------+----------------------------+-----------+------------+------------+--------+");
//
//                            System.out.printf("| %-8d | %-26s | %-9.2f | %-10s | %-10s | %-6d |\n",
//                                    231245, "sw", 20.0, "2025-08-24", "2025-09-05", 3);
//
//                            System.out.println("+----------+----------------------------+-----------+------------+------------+--------+");

                        } else {
                            con.rollback();
                            System.out.println("❌ Could not rent book.");
                        }
                    } catch (SQLException sqle) {
                        con.rollback();
                        throw sqle;
                    } finally {
                        con.setAutoCommit(true);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("+" + "-".repeat(boxWidth) + "+");
            System.out.printf("| %-86s |\n", "❌ Error: " + e.getMessage());
            System.out.println("+" + "-".repeat(boxWidth) + "+");
        }
    }


    public static void returnBook(String username) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            if (con == null || con.isClosed()) {
                System.out.println("❌ Database connection not available.");
                return;
            }

            System.out.println("+-----------------------------------+");
            System.out.println("|   Enter Book No/ISBN No to Return |");
            System.out.println("+-----------------------------------+");

            int book_no = -1;
            while (true) {
                try {
                    System.out.print("👉 Your Input: ");
                    book_no = sc.nextInt();
                    sc.nextLine(); // buffer clear
                    break;
                } catch (InputMismatchException ime) {
                    System.out.println("❌ Invalid input! Please enter a valid ISBN (number).");
                    sc.nextLine(); // clear wrong input
                }
            }

            // Get rental record
            String checkRentalSql =
                    "SELECT * FROM book_rentals br " +
                            "JOIN book_list bl ON br.book_isbn_no = bl.book_isbn_no " +
                            "WHERE br.book_isbn_no = ? AND br.user_name = ? AND br.return_status = 'NOT_RETURNED' " +
                            "ORDER BY br.rent_date DESC LIMIT 1";
            pstmt = con.prepareStatement(checkRentalSql);
            pstmt.setInt(1, book_no);
            pstmt.setString(2, username);
            rs = pstmt.executeQuery();

            if (!rs.next()) {
                System.out.println("+------------------------------------------------------+");
                System.out.println("| ❌  No active rental found for this book             |");
                System.out.println("|     under your account.                              |");
                System.out.println("+------------------------------------------------------+");
                return;
            }

            int rentalId = rs.getInt("rental_id");
            String bookName = rs.getString("book_name");
            String author = rs.getString("author_name");
            LocalDate rentDate = rs.getDate("rent_date").toLocalDate();
            LocalDate dueDate = rs.getDate("return_date").toLocalDate();
            int rentedCopies = rs.getInt("copies");

            LocalDate actualReturnDate = LocalDate.now();

            int fine = 0;
            String status = "RETURNED";

            if (actualReturnDate.isBefore(dueDate)) {
                status = "RETURNED (Early)";
            } else if (actualReturnDate.isAfter(dueDate)) {
                long lateDays = java.time.temporal.ChronoUnit.DAYS.between(dueDate, actualReturnDate);
                fine = (int) (lateDays * 20);
                status = "RETURNED (Late)";
            }

            con.setAutoCommit(false);
            try {
                // Update rental
                String updateRentalSql =
                        "UPDATE book_rentals " +
                                "SET return_status = ?, actual_return_date = ?, fine = ? " +
                                "WHERE rental_id = ?";
                try (PreparedStatement u1 = con.prepareStatement(updateRentalSql)) {
                    u1.setString(1, "RETURNED");
                    u1.setDate(2, java.sql.Date.valueOf(actualReturnDate));
                    u1.setInt(3, fine);
                    u1.setInt(4, rentalId);
                    u1.executeUpdate();
                }

                // Update book copies
                String updateCopiesSql = "UPDATE book_list SET copies = copies + ? WHERE book_isbn_no = ?";
                try (PreparedStatement u2 = con.prepareStatement(updateCopiesSql)) {
                    u2.setInt(1, rentedCopies);
                    u2.setInt(2, book_no);
                    u2.executeUpdate();
                }

                con.commit();

                // ✅ Final Output Table
                System.out.println("+---------------------------------------------------------------------------------+");
                System.out.printf("| %-10s | %-8s | %-25s | %-15s |\n", "Rental ID", "ISBN", "Book Name", "Author");
                System.out.println("+---------------------------------------------------------------------------------+");
                System.out.printf("| %-10d | %-8d | %-25s | %-15s |\n", rentalId, book_no, bookName, author);
                System.out.println("+---------------------------------------------------------------------------------+");
                System.out.printf("| %-12s | %-12s | %-12s | %-12s |\n", "Rent Date", "Due Date", "Returned", "Status");
                System.out.println("+---------------------------------------------------------------------------------+");
                System.out.printf("| %-12s | %-12s | %-12s | %-12s |\n",
                        rentDate, dueDate, actualReturnDate, status);
                System.out.println("+---------------------------------------------------------------------------------+");

                if (fine > 0) {
                    System.out.println("⚠ Late return fine: ₹" + fine);
                }

            } catch (Exception ex) {
                con.rollback();
                System.out.println("❌ Error during book return: " + ex.getMessage());
            } finally {
                con.setAutoCommit(true);
            }

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException ignored) {}
            try { if (pstmt != null) pstmt.close(); } catch (SQLException ignored) {}
        }
    }






    // ---------------- HELPERS ----------------
    private static boolean isBookAvailable(int book_no, LocalDate rentDateFrom, LocalDate rentDateTo) {
        String sql = "SELECT * FROM book_rentals WHERE book_isbn_no = ? AND (rent_date <= ? AND return_date >= ?)";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, book_no);
            pstmt.setDate(2, Date.valueOf(rentDateTo));
            pstmt.setDate(3, Date.valueOf(rentDateFrom));

            rs = pstmt.executeQuery();
            return !rs.next(); // true = available, false = already rented

        } catch (SQLException e) {
            System.out.println("❌ Error checking book availability: " + e.getMessage());
            return false; // error पर default false मतलब "not available"
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException ignored) {
            }
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException ignored) {
            }
        }
    }


    private static Date getNextAvailableFromDate(int book_no, Date rentDateTo) {
        String sql = "SELECT MAX(return_date) AS last_rent_date FROM book_rentals WHERE book_isbn_no = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, book_no);
            rs = pstmt.executeQuery();

            if (rs.next() && rs.getDate("last_rent_date") != null) {
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.setTime(rs.getDate("last_rent_date"));
                cal.add(java.util.Calendar.DAY_OF_MONTH, 1); // next available day
                return new Date(cal.getTimeInMillis());
            } else {
                return new Date(System.currentTimeMillis()); // available today
            }

        } catch (SQLException e) {
            System.out.println("❌ Error getting next available date: " + e.getMessage());
            return new Date(System.currentTimeMillis()); // fallback: today
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException ignored) {
            }
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException ignored) {
            }
        }
    }

    private static double processPayment(double totalRent, int rentalDays, double rentPrice, int reqCopies) {
        // Rent Summary Table
        System.out.println("\n+------------------------------------------+");
        System.out.printf("| %-20s : %-10d |\n", "Rental Days", rentalDays);
        System.out.printf("| %-20s : %-10.2f |\n", "Rent per Day", rentPrice);
        System.out.printf("| %-20s : %-10d |\n", "Copies", reqCopies);
        System.out.printf("| %-20s : ₹%-9.2f |\n", "Total Rent", totalRent);
        System.out.println("+------------------------------------------+");

        boolean isPaymentDone = false;
        String paymentMode = "";

        System.out.printf("Total Rent is :"+totalRent);

        do {
            try {
                // Payment Options Table
                System.out.println("\n+---------------- Payment Options ----------------+");
                System.out.println("| 1. Debit Card Payment                          |");
                System.out.println("| 2. UPI Payment                                 |");
                System.out.println("+------------------------------------------------+");
                System.out.print("👉 Enter your Payment Type: ");

                if (!sc.hasNextInt()) {
                    System.out.println("❌ Invalid input! Please enter a number.");
                    sc.next(); // wrong input consume
                    continue;
                }

                int ch = sc.nextInt();
                double payedAmount;

                switch (ch) {
                    case 1: // Debit Card
                        while (true) {
                            // System.out.print("\n💳 Enter Amount To Pay : ₹");

//                            if (!sc.hasNextDouble()) {
//                                System.out.println("❌ Invalid amount! Must be a number.");
//                                sc.next();
//                                continue;
//                            }

//                            payedAmount = sc.nextDouble();
//                            if (payedAmount != totalRent) {
//                                System.out.println("❌ Incorrect amount! Please pay exact: ₹" + totalRent);
//                                continue;
//                            }

                            payedAmount = totalRent;

                            System.out.print("Enter 12-Digit Debit Card Number: ");
                            String card = sc.next();

                            if (!(card.length() == 12 && card.matches("\\d+"))) {
                                System.out.println("❌ Invalid Card Number! Must be 12 digits.");
                                continue;
                            }

                            System.out.print("Enter 3-digit CVV: ");
                            String cvv = sc.next();

                            if (!(cvv.length() == 3 && cvv.matches("\\d+"))) {
                                System.out.println("❌ Invalid CVV! Must be 3 digits.");
                                continue;
                            }

                            System.out.print("Enter OTP: ");
                            String otp_card = sc.next();

                            if (otp_card.length() == 4 && otp_card.matches("\\d+")) {
                                paymentMode = "Debit Card";
                                isPaymentDone = true;
                                break;
                            } else {
                                System.out.println("❌ Payment Failed! Wrong OTP.");
                            }
                        }
                        break;

                    case 2: // UPI
                        while (true) {
                            //System.out.print("\n📱 Enter Amount To Pay : ₹");
//
//                            if (!sc.hasNextDouble()) {
//                                System.out.println("❌ Invalid amount! Must be a number.");
//                                sc.next();
//                                continue;
//                            }

                           // payedAmount = sc.nextDouble();
                            payedAmount = totalRent;
//                            if (payedAmount != totalRent) {
//                                System.out.println("❌ Incorrect amount! Please pay exact: ₹" + totalRent);
//                                continue;
//                            }

                            System.out.print("Enter 4-digit UPI Pin: ");
                            String pass = sc.next();

                            if (pass.length() == 4 && pass.matches("\\d+")) {
                                paymentMode = "UPI";
                                isPaymentDone = true;
                                break;
                            } else {
                                System.out.println("❌ Invalid UPI Pin!");
                            }
                        }
                        break;

                    default:
                        System.out.println("❌ Invalid option! Please try again.");
                        break;
                }

            } catch (Exception e) {
                System.out.println("❌ Payment process error: " + e.getMessage());
                sc.nextLine(); // scanner reset
            }

        } while (!isPaymentDone);

        // Final Payment Receipt Table
        System.out.println("\n+---------------- Payment Receipt ----------------+");
        System.out.printf("| %-20s : %-15s |\n", "Payment Mode", paymentMode);
        System.out.printf("| %-20s : %-15d |\n", "Rental Days", rentalDays);
        System.out.printf("| %-20s : %-15.2f |\n", "Rent per Day", rentPrice);
        System.out.printf("| %-20s : %-15d |\n", "Copies", reqCopies);
        System.out.printf("| %-20s : ₹%-14.2f |\n", "Total Paid", totalRent);
        System.out.println("+-------------------------------------------------+");
        System.out.println("✅ Payment Successful! Thank you for booking.");

        return totalRent;
    }
}