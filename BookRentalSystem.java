// Book Rental System..

import java.sql.*;
import java.util.Scanner;
import java.util.InputMismatchException;


public class BookRentalSystem {
    static Scanner sc = new Scanner(System.in);
    static Connection con;
    static Statement st;
    static int count1;
    static boolean userLoginStatus = false;
    static boolean adminLoginStatus = false;
    static String loggedInUser = null;

    static int loggedInAdminId;




    // Main Method
    public static void main(String[] args) throws Exception {
        BookRental.connectDB();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ JDBC Driver Loaded Successfully!");
        } catch (ClassNotFoundException e) {
            System.out.println("❌ Driver not found: " + e.getMessage());
        }

        Class.forName("com.mysql.cj.jdbc.Driver");

        // connect to DataBase

        String dburl = "jdbc:mysql://localhost:3306/book_rental";

        String dbuser = "root";
        String dbpass = "";
        con = DriverManager.getConnection(dburl, dbuser, dbpass);
        StackBook sbook = new StackBook(con);
        StackBook sbook2 = new StackBook();

        if (con != null) {
            //System.out.println("~~~~~~~~~ Welcome To Shree Krishna Book Rental System ~~~~~~~~~");
            System.out.println("+===========================================================+");
            System.out.println("|               Shree Krishna Book Rental System            |");
            System.out.println("|-----------------------------------------------------------|");
            System.out.println("|               Rent | Read | Return | Explore              |");
            System.out.println("+===========================================================+");

            int choiceLoginAs = -1 ;
            do{
                System.out.println(
                        "+----------------------------+\n" +
                                "|        WELCOME MENU        |\n" +
                                "|----------------------------|\n" +
                                "| Case 1 : Enter As Admin    |\n" +
                                "| Case 2 : Enter As User     |\n" +
                                "| Case 3 : Exit              |\n" +
                                "+----------------------------+");
                System.out.println("👉 Please select an option: ");

                try {
                    choiceLoginAs = sc.nextInt();
                    sc.nextLine();

                    switch (choiceLoginAs) {
                        case 1:

                            adminLoginStatus = administrator_login();
                            if (adminLoginStatus) {

                                int choice = -1;
                                do {
                                    try {
                                        System.out.println(
                                                "+-------------------------------------------+\n" +
                                                        "|                 ADMIN MENU                |\n" +
                                                        "+-------------------------------------------+\n" +
                                                        "| Case 1  | Add A Book                      |\n" +
                                                        "| Case 2  | Delete A Book                   |\n" +
                                                        "| Case 3  | Show All Book Details           |\n" +
                                                        "| Case 4  | Show All Bookings               |\n" +
                                                        "| Case 5  | Add An Admin                    |\n" +
                                                        "| Case 6  | Remove A User                   |\n" +
                                                        "| Case 7  | Remove An Admin                 |\n" +
                                                        "| Case 8  | Show All Users Info             |\n" +
                                                        "| Case 9  | Update Admin Information        |\n" +
                                                        "| Case 10 | Admin Information               |\n" +
                                                        "| Case 11 | View User/Admin Delete Logs     |\n" +
                                                        "| Case 0  | Exit                            |\n" +
                                                        "+-------------------------------------------+"
                                        );

                                        System.out.print("Enter your choice: ");
                                        choice = sc.nextInt();
                                        sc.nextLine(); // buffer clear
                                        sbook.in();

                                        switch (choice) {
                                            case 1:
                                                System.out.println(
                                                        "+-----------------------------------+\n" +
                                                                "|         ENTER BOOK DETAILS        |\n" +
                                                                "+-----------------------------------+"
                                                );

                                                System.out.print("| Book Name          : ");
                                                String book_name = sc.nextLine();

                                                System.out.print("| Author Name        : ");
                                                String author_name = sc.nextLine();

                                                int book_no = 0;
                                                int boxWidth = 35; // Box width

                                                while (true) {
                                                    // Top border
                                                    System.out.println("+" + "-".repeat(boxWidth) + "+");
                                                    System.out.print("| Book No. [6 Digits] : ");
                                                    try {
                                                        book_no = sc.nextInt();
                                                        sc.nextLine(); // consume newline

                                                        // Check if exactly 6 digits
                                                        if (book_no >= 100000 && book_no <= 999999) {
                                                            System.out.println("+" + "-".repeat(boxWidth) + "+");
                                                            System.out.printf("| %-33s |\n", "✅ Book No. accepted: " + book_no);
                                                            System.out.println("+" + "-".repeat(boxWidth) + "+");
                                                            break;
                                                        } else {
                                                            System.out.println("+" + "-".repeat(boxWidth) + "+");
                                                            System.out.printf("| %-33s |\n", "❌ Error: Enter exactly 6 digits!");
                                                            System.out.println("+" + "-".repeat(boxWidth) + "+");
                                                        }
                                                    } catch (InputMismatchException e) {
                                                        System.out.println("+" + "-".repeat(boxWidth) + "+");
                                                        System.out.printf("| %-33s |\n", "❌ Error: Enter digits only!");
                                                        System.out.println("+" + "-".repeat(boxWidth) + "+");
                                                        sc.nextLine(); // clear invalid input
                                                    }
                                                }

                                                double rent_price = 0;

                                                while (true) {
                                                    System.out.println("+" + "-".repeat(boxWidth) + "+");
                                                    System.out.print("| Rent Price Per Day : ");
                                                    try {
                                                        rent_price = sc.nextInt();
                                                        sc.nextLine(); // consume newline

                                                        if (rent_price > 0) { // positive rent price
                                                            System.out.println("+" + "-".repeat(boxWidth) + "+");
                                                            System.out.printf("| %-33s |\n", "✅ Rent Price accepted: " + rent_price);
                                                            System.out.println("+" + "-".repeat(boxWidth) + "+");
                                                            break;
                                                        } else {
                                                            System.out.println("+" + "-".repeat(boxWidth) + "+");
                                                            System.out.printf("| %-33s |\n", "❌ Error: Enter positive number!");
                                                            System.out.println("+" + "-".repeat(boxWidth) + "+");
                                                        }
                                                    } catch (InputMismatchException e) {
                                                        System.out.println("+" + "-".repeat(boxWidth) + "+");
                                                        System.out.printf("| %-33s |\n", "❌ Error: Enter digits only!");
                                                        System.out.println("+" + "-".repeat(boxWidth) + "+");
                                                        sc.nextLine(); // clear invalid input
                                                    }
                                                }

                                                double rating = 0.0;

                                                while (true) {
                                                    System.out.println("+" + "-".repeat(boxWidth) + "+");
                                                    System.out.print("| Book Rating [0.0 - 10.0] : ");
                                                    try {
                                                        rating = sc.nextDouble();
                                                        sc.nextLine(); // consume newline

                                                        if (rating >= 0.0 && rating <= 10.0) {
                                                            System.out.println("+" + "-".repeat(boxWidth) + "+");
                                                            System.out.printf("| %-33s |\n", "✅ Rating accepted: " + rating);
                                                            System.out.println("+" + "-".repeat(boxWidth) + "+");
                                                            break;
                                                        } else {
                                                            System.out.println("+" + "-".repeat(boxWidth) + "+");
                                                            System.out.printf("| %-33s |\n", "❌ Error: Enter rating 0.0 - 10.0!");
                                                            System.out.println("+" + "-".repeat(boxWidth) + "+");
                                                        }
                                                    } catch (InputMismatchException e) {
                                                        System.out.println("+" + "-".repeat(boxWidth) + "+");
                                                        System.out.printf("| %-33s |\n", "❌ Error: Enter numeric value!");
                                                        System.out.println("+" + "-".repeat(boxWidth) + "+");
                                                        sc.nextLine(); // clear invalid input
                                                    }
                                                }

                                                int copies = 0;

                                                while (true) {
                                                    System.out.println("+" + "-".repeat(boxWidth) + "+");
                                                    System.out.print("| Enter number of copies [1 - 50] : ");
                                                    try {
                                                        copies = sc.nextInt();
                                                        sc.nextLine(); // consume newline

                                                        if (copies >= 1 && copies <= 50) {
                                                            System.out.println("+" + "-".repeat(boxWidth) + "+");
                                                            System.out.printf("| %-33s |\n", "✅ Copies accepted: " + copies);
                                                            System.out.println("+" + "-".repeat(boxWidth) + "+");
                                                            break;
                                                        } else {
                                                            System.out.println("+" + "-".repeat(boxWidth) + "+");
                                                            System.out.printf("| %-33s |\n", "❌ Error: Enter between 1 and 50!");
                                                            System.out.println("+" + "-".repeat(boxWidth) + "+");
                                                        }
                                                    } catch (InputMismatchException e) {
                                                        System.out.println("+" + "-".repeat(boxWidth) + "+");
                                                        System.out.printf("| %-33s |\n", "❌ Error: Enter numeric value!");
                                                        System.out.println("+" + "-".repeat(boxWidth) + "+");
                                                        sc.nextLine(); // clear invalid input
                                                    }
                                                }

                                                System.out.println("+-----------------------------------+");

                                                Book book = new Book(book_name, book_no, rating, rent_price, author_name, copies);
                                                sbook.push(book);

                                                break;

                                            case 2:
                                                int del_book_no = 0;
                                                int boxWidth2 = 35;

                                                System.out.println(
                                                        "+-----------------------------------+\n" +
                                                                "|        DELETE A BOOK              |\n" +
                                                                "+-----------------------------------+"
                                                );

                                                while (true) {
                                                    System.out.print("| Enter Book No to Delete : ");
                                                    try {
                                                        del_book_no = sc.nextInt();
                                                        sc.nextLine(); // consume newline

                                                        if (del_book_no >= 100000 && del_book_no <= 999999) {
                                                            System.out.println("+" + "-".repeat(boxWidth2) + "+");
                                                            System.out.printf("| %-33s |\n", "✅ Book No accepted: " + del_book_no);
                                                            System.out.println("+" + "-".repeat(boxWidth2) + "+");
                                                            break;
                                                        } else {
                                                            System.out.println("+" + "-".repeat(boxWidth2) + "+");
                                                            System.out.printf("| %-33s |\n", "❌ Error: Enter exactly 6 digits!");
                                                            System.out.println("+" + "-".repeat(boxWidth2) + "+");
                                                        }
                                                    } catch (InputMismatchException e) {
                                                        System.out.println("+" + "-".repeat(boxWidth2) + "+");
                                                        System.out.printf("| %-33s |\n", "❌ Error: Enter digits only!");
                                                        System.out.println("+" + "-".repeat(boxWidth2) + "+");
                                                        sc.nextLine(); // clear invalid input
                                                    }
                                                }
                                                deleteByBookNo(del_book_no);
                                                break;

                                            case 3:
                                                System.out.println(
                                                        "+-----------------------------------+\n" +
                                                                "|        ALL BOOK DETAILS           |\n" +
                                                                "+-----------------------------------+"
                                                );
                                                viewBooks();
                                                break;

                                            case 4:
                                                System.out.println(
                                                        "+-----------------------------------+\n" +
                                                                "|        ALL BOOKINGS               |\n" +
                                                                "+-----------------------------------+"
                                                );
                                                displayAllBookings(con);
                                                break;

                                            case 5:
                                                showAdminInfo();
                                                System.out.println(
                                                        "+-----------------------------------+\n" +
                                                                "|          ADD AN ADMIN             |\n" +
                                                                "+-----------------------------------+"
                                                );
                                                addAdmin();
                                                break;

                                            case 6:
                                                int boxWidth4 = 50;

                                                System.out.println(
                                                        "+--------------------------------------+\n" +
                                                                "|            REMOVE A USER             |\n" +
                                                                "+--------------------------------------+"
                                                );

                                                int del_user_id = -1;
                                                while (true) {
                                                    System.out.print("| Enter User ID to Delete : ");
                                                    String input = sc.nextLine().trim();

                                                    try {
                                                        del_user_id = Integer.parseInt(input);
                                                        if (del_user_id > 0) break;  // ✅ valid ID
                                                    } catch (NumberFormatException e) {
                                                        // ignore, show warning
                                                    }

                                                    System.out.println("+" + "-".repeat(boxWidth4) + "+");
                                                    System.out.printf("| %-48s |\n", "⚠️ Invalid input! Please enter a valid User ID.");
                                                    System.out.println("+" + "-".repeat(boxWidth4) + "+");
                                                }

                                                removeUser(del_user_id);

                                                break;

                                            case 7:
                                                showAdminInfo();
                                                int boxWidth5 = 65;

                                                System.out.println(
                                                        "+-----------------------------------+\n" +
                                                                "|         DELETE AN ADMIN           |\n" +
                                                                "+-----------------------------------+"
                                                );

                                                int del_administrator;
                                                while (true) {
                                                    System.out.print("| Enter 4-digit Admin ID to Delete : ");
                                                    try {
                                                        del_administrator = sc.nextInt();
                                                        sc.nextLine(); // consume newline

                                                        // Validate 4-digit, positive, cannot start with 0
                                                        if (del_administrator >= 1000 && del_administrator <= 9999) break;

                                                        System.out.println("+" + "-".repeat(boxWidth5) + "+");
                                                        System.out.printf("| %-63s |\n", "⚠️ Admin ID must be 4 digits and cannot start with 0!");
                                                        System.out.println("+" + "-".repeat(boxWidth5) + "+");

                                                    } catch (InputMismatchException ime) {
                                                        System.out.println("+" + "-".repeat(boxWidth5) + "+");
                                                        System.out.printf("| %-63s |\n", "⚠️ Invalid input! Please enter numeric digits only.");
                                                        System.out.println("+" + "-".repeat(boxWidth5) + "+");
                                                        sc.nextLine(); // clear invalid input
                                                    }
                                                }

                                                removeAdmin(del_administrator);
                                                break;

                                            case 8:
                                                System.out.println(
                                                        "+-----------------------------------+\n" +
                                                                "|         ALL USERS INFO            |\n" +
                                                                "+-----------------------------------+"
                                                );
                                                displayUsers();
                                                break;

                                            case 9:
                                                System.out.println(
                                                        "+-----------------------------------+\n" +
                                                                "|   UPDATE ADMIN INFORMATION        |\n" +
                                                                "+-----------------------------------+"
                                                );
                                                changeInformationOfAdmin();
                                                break;

                                            case 10:
                                                System.out.println(
                                                        "+-----------------------------------+\n" +
                                                                "|   ADMIN INFORMATION               |\n" +
                                                                "+-----------------------------------+"
                                                );
                                                showAdminInfo();

                                                break;

                                            case 11:
                                                System.out.println(
                                                        "+-----------------------------------+\n" +
                                                                "|   VIEW USER/ADMIN DELETE LOGS     |\n" +
                                                                "+-----------------------------------+"
                                                );
                                                showUserLogs();
                                                break;

                                            case 0:
                                                System.out.println(
                                                        "+-----------------------------+\n" +
                                                                "|         EXITING...          |\n" +
                                                                "+-----------------------------+"
                                                );
                                                break;

                                            default:
                                                System.out.println(
                                                        "+-----------------------------+\n" +
                                                                "|       INVALID CHOICE        |\n" +
                                                                "+-----------------------------+"
                                                );
                                                break;
                                        }

                                    } catch (InputMismatchException e) {
                                        System.out.println(
                                                "+-----------------------------------+\n" +
                                                        "| ❌ Invalid input! Enter a number. |\n" +
                                                        "+-----------------------------------+"
                                        );
                                        sc.nextLine(); // buffer clear
                                        choice = -1;   // reset so loop continues
                                    }
                                } while (choice != 0);

                            }
                            break;

                        case 2:
                            int ch = -1;
                            do {
                                try {
                                    System.out.println(
                                            "+----------------------------+\n" +
                                                    "|          USER MENU         |\n" +
                                                    "+----------------------------+\n" +
                                                    "| Case 1 | Login             |\n" +
                                                    "| Case 2 | Sign Up           |\n" +
                                                    "| Case 3 | Exit              |\n" +
                                                    "+----------------------------+"
                                    );
                                    System.out.print("Enter your choice: ");
                                    ch = sc.nextInt();
                                    sc.nextLine();

                                    switch (ch) {
                                        case 1:
                                            boolean temp = user_login();
                                            if (temp) {
                                                int temp1 = -1;
                                                do {
                                                    try {
                                                        System.out.println(
                                                                "+--------------------------------------------+\n" +
                                                                        "|               USER BOOK MENU               |\n" +
                                                                        "+--------------------------------------------+\n" +
                                                                        "| Case 1 | Book List                         |\n" +
                                                                        "| Case 2 | Rent A Book Now                   |\n" +
                                                                        "| Case 3 | Top 3 Books                       |\n" +
                                                                        "| Case 4 | Search Book                       |\n" +
                                                                        "| Case 5 | Update Personal Information       |\n" +
                                                                        "| Case 6 | Return A Book                     |\n" +
                                                                        "| Case 0 | Exit                              |\n" +
                                                                        "+--------------------------------------------+"
                                                        );
                                                        System.out.print("Enter your choice: ");
                                                        temp1 = sc.nextInt();
                                                        sc.nextLine();

                                                        switch (temp1) {
                                                            case 1:
                                                                viewBooks();
                                                                break;

                                                            case 2:
                                                                viewBooks();
                                                                BookRental.bookForRent(loggedInUser);
                                                                break;

                                                            case 3:
                                                                System.out.println(
                                                                        "+-----------------------------------+\n" +
                                                                                "|            TOP 3 BOOKS            |\n" +
                                                                                "+-----------------------------------+"
                                                                );
                                                                try {
                                                                    sbook2.peekTop3(); // top 3 books
                                                                } catch (Exception e) {
                                                                    System.out.println("| ❌ No books available!             |");
                                                                }
                                                                System.out.println("+-----------------------------------+");
                                                                break;

                                                            case 4:
                                                                System.out.println(
                                                                        "+-----------------------------------+\n" +
                                                                                "|        SEARCH A BOOK BY ISBN      |\n" +
                                                                                "+-----------------------------------+"
                                                                );

                                                                int bookNo;

                                                                // Input validation loop
                                                                while (true) {
                                                                    System.out.print("| Enter Book ISBN No to Search : ");
                                                                    String input = sc.nextLine().trim();

                                                                    if (!input.matches("\\d{6}")) {  // must be 6 digits
                                                                        System.out.println("+-----------------------------------+");
                                                                        System.out.println("| ❌ Invalid ISBN! Must be 6 digits.|");
                                                                        System.out.println("+-----------------------------------+");
                                                                        continue;
                                                                    }

                                                                    bookNo = Integer.parseInt(input);
                                                                    if (bookNo <= 0) {
                                                                        System.out.println("+-----------------------------------+");
                                                                        System.out.println("| ❌ ISBN cannot be negative or 0.  |");
                                                                        System.out.println("+-----------------------------------+");
                                                                        continue;
                                                                    }
                                                                    break;
                                                                }

                                                                try {
                                                                    SearchBook searchBook = new SearchBook();
                                                                    boolean found = searchBook.search(bookNo); // existing search method

                                                                    if (!found) {
                                                                        System.out.println("+-----------------------------------+");
                                                                        System.out.println("| ❌ Book not found.                |");
                                                                    }
                                                                } catch (Exception e) {
                                                                    System.out.println("+-----------------------------------+");
                                                                    System.out.println("| ❌ Error occurred: " + e.getMessage());
                                                                }

                                                                System.out.println("+-----------------------------------+");
                                                                break;

                                                            case 5:
                                                                changeInformationOfUser();
                                                                break;

                                                            case 6:
                                                                BookRental.returnBook(loggedInUser);
                                                                break;

                                                            case 0:
                                                                System.out.println(
                                                                        "+-------------------------------------------+\n" +
                                                                                "| ✅ Exiting Book Menu...                   |\n" +
                                                                                "+-------------------------------------------+"
                                                                );
                                                                break;

                                                            default:
                                                                System.out.println(
                                                                        "+-----------------------------+\n" +
                                                                                "|       INVALID CHOICE        |\n" +
                                                                                "+-----------------------------+"
                                                                );
                                                                break;
                                                        }
                                                    } catch (InputMismatchException e) {
                                                        System.out.println(
                                                                "+-----------------------------------+\n" +
                                                                        "| ❌ Invalid input! Enter a number. |\n" +
                                                                        "+-----------------------------------+"
                                                        );
                                                        sc.nextLine();
                                                        temp1 = -1;
                                                    }
                                                } while (temp1 != 0);
                                            }
                                            break;

                                        case 2:
                                            user_signUp();
                                            break;

                                        case 3:
                                            System.out.println(
                                                    "+----------------------------+\n" +
                                                            "| ✅ Exiting User Menu...    |\n" +
                                                            "+----------------------------+"
                                            );
                                            break;

                                        default:
                                            System.out.println(
                                                    "+-----------------------------+\n" +
                                                            "|       INVALID CHOICE        |\n" +
                                                            "+-----------------------------+"
                                            );
                                            break;
                                    }
                                } catch (InputMismatchException e) {
                                    System.out.println(
                                            "+-----------------------------------+\n" +
                                                    "| ❌ Invalid input! Enter a number. |\n" +
                                                    "+-----------------------------------+"
                                    );
                                    sc.nextLine();
                                    ch = -1;
                                }
                            } while (ch != 3);
                            break;


                        case 3:
                            System.out.println(
                                    "+----------------------------------------------------+\n" +
                                            "| Exiting..                                          |\n" +
                                            "| Thanks For Using Shree Krishna Book Store..        |\n" +
                                            "+----------------------------------------------------+");

                            break;

                        default:
                            System.out.println(
                                    "+-----------------------------+\n" +
                                            "|     ENTER A VALID CHOICE!   |\n" +
                                            "+-----------------------------+");

                            break;
                    }
                }
                catch (InputMismatchException e) {
                    System.out.println(
                            "+-----------------------------------+\n" +
                                    "| ❌ Invalid input! Enter a number. |\n" +
                                    "+-----------------------------------+");
                    sc.nextLine(); // clear wrong input
                }



            }while(choiceLoginAs != 3);

        }
    }

    // ✅ Safe User Login Method with Full Exception Handling
    static int loggedInUserId = -1;  // 👈 Global variable

    static boolean user_login() {
        System.out.println(
                "+-----------------------------+\n" +
                        "|         USER LOGIN          |\n" +
                        "+-----------------------------+");

        String userName = "";
        String password = "";

        try {
            // Input User Name
            while (true) {
                System.out.print("| Enter User Name : ");
                userName = sc.nextLine().trim();
                if (!userName.isEmpty()) break;
                System.out.println("| ❌ User name cannot be empty!");
            }

            // Input Password
            while (true) {
                System.out.print("| Enter Password  : ");
                password = sc.nextLine().trim();
                if (!password.isEmpty()) break;
                System.out.println("| ❌ Password cannot be empty!");
            }

            System.out.println("+-----------------------------+");

            String sql = "SELECT user_id, user_name FROM users WHERE user_name=? AND password=?";
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                pstmt.setString(1, userName);
                pstmt.setString(2, password);

                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    // ✅ Save user_id
                    loggedInUserId = rs.getInt("user_id");
                    loggedInUser = rs.getString("user_name");
                    userLoginStatus = true;

                    int boxWidth = 48;
                    System.out.println("+" + "-".repeat(boxWidth - 2) + "+");
                    System.out.printf("| %-44s |\n", "WELCOME BACK " + loggedInUser);
                    System.out.printf("| %-46s |\n", "Your User ID: " + loggedInUserId);
                    System.out.println("+" + "-".repeat(boxWidth - 2) + "+");

                    return true;
                } else {
                    System.out.println(
                            "+---------------------------------------------+\n" +
                                    "| ❌ Invalid user name or password!           |\n" +
                                    "|    Please try again.                        |\n" +
                                    "+---------------------------------------------+");
                    return false;
                }
            }

        } catch (SQLException sqle) {
            System.out.println("⚠ DB Error: " + sqle.getMessage());
            return false;
        }
    }




    // ✅ Safe User Sign-Up Method with Full Exception Handling
    static void user_signUp() {
        int boxWidth = 60;

        System.out.println("+" + "-".repeat(boxWidth) + "+");
        System.out.printf("| %-58s |\n", "REGISTRATION FORM");
        System.out.println("+" + "-".repeat(boxWidth) + "+");

        try {
            // ✅ Input User Name
            String user_name;
            while (true) {
                System.out.print("| Enter User Name : ");
                user_name = sc.nextLine().trim();
                if (!user_name.isEmpty()) break;
                System.out.println("| ❌ User name cannot be empty!");
            }

            // ✅ Input Password
            String password;
            while (true) {
                System.out.print("| Enter Password  : ");
                password = sc.nextLine().trim();
                if (!password.isEmpty()) break;
                System.out.println("| ❌ Password cannot be empty!");
            }

            // ✅ Input Email
            String email;
            while (true) {
                System.out.print("| Enter Email (must end with @gmail.com) : ");
                email = sc.nextLine().trim().toLowerCase(); // automatically lowercase
                if (email.matches("^[\\w._%+-]+@gmail\\.com$")) break;
                System.out.println("| ❌ Invalid email! Must be a Gmail address.");
            }

            // ✅ Input Phone Number
            String mobileNo;
            while (true) {
                System.out.print("| Enter Phone Number (10 digits, 6-9 start) : ");
                mobileNo = sc.nextLine().trim();
                if (mobileNo.matches("^[6-9]\\d{9}$")) break;
                System.out.println("| ❌ Invalid phone number! Must start with 6-9 and be 10 digits.");
            }

            System.out.println("+" + "-".repeat(boxWidth) + "+");

            // ✅ Insert into Database
            String sql = "INSERT INTO users (user_name, password, email, mobileNo) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                pstmt.setString(1, user_name);
                pstmt.setString(2, password);
                pstmt.setString(3, email);
                pstmt.setString(4, mobileNo);

                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("+" + "-".repeat(boxWidth) + "+");
                    System.out.printf("| %-58s |\n", "✅ Registration Successful!");
                    System.out.println("+" + "-".repeat(boxWidth) + "+");
                } else {
                    System.out.println("+" + "-".repeat(boxWidth) + "+");
                    System.out.printf("| %-58s |\n", "❌ Registration Failed!");
                    System.out.println("+" + "-".repeat(boxWidth) + "+");
                }
            }

        } catch (SQLIntegrityConstraintViolationException dup) {
            // ✅ Error ka proper handling for duplicate entries
            String msg = dup.getMessage().toLowerCase();

            System.out.println("+" + "-".repeat(boxWidth) + "+");
            if (msg.contains("email")) {
                System.out.printf("| %-58s |\n", "❌ Email already exists! Use another.");
            } else if (msg.contains("mobileno")) {
                System.out.printf("| %-58s |\n", "❌ Mobile number already exists! Use another.");
            } else if (msg.contains("user_name")) {
                System.out.printf("| %-58s |\n", "❌ Username already exists! Choose another.");
            } else {
                System.out.printf("| %-58s |\n", "❌ Duplicate entry! Try different values.");
            }
            System.out.println("+" + "-".repeat(boxWidth) + "+");

        } catch (SQLException sqle) {
            System.out.println("+" + "-".repeat(boxWidth) + "+");
            System.out.printf("| %-58s |\n", "❌ Database Error: " + sqle.getMessage());
            System.out.println("+" + "-".repeat(boxWidth) + "+");

        } catch (Exception e) {
            System.out.println("+" + "-".repeat(boxWidth) + "+");
            System.out.printf("| %-58s |\n", "❌ Unexpected Error: " + e.getMessage());
            System.out.println("+" + "-".repeat(boxWidth) + "+");
        }
    }




    // ✅ Safe Administrator Login with Full Exception Handling
    static boolean administrator_login() {
        int boxWidth = 60;
        System.out.println("+" + "-".repeat(boxWidth) + "+");
        System.out.printf("| %-58s |\n", "ADMIN LOGIN");
        System.out.println("+" + "-".repeat(boxWidth) + "+");

        try {
            // ✅ Safe Admin ID input
            int adminId;
            while (true) {
                System.out.print("| Enter Administrator ID : ");
                try {
                    String input = sc.nextLine().trim();
                    if (!input.matches("\\d{4}")) { // 4-digit ID validation
                        System.out.println("| ❌ Invalid ID! Must be 4 digits.");
                        continue;
                    }
                    adminId = Integer.parseInt(input);
                    loggedInAdminId = Integer.parseInt(input);
                    break;
                } catch (NumberFormatException nfe) {
                    System.out.println("| ❌ Invalid input! Please enter numbers only.");
                }
            }

            // ✅ Password input
            String password;
            while (true) {
                System.out.print("| Enter Password         : ");
                password = sc.nextLine().trim();
                if (!password.isEmpty()) break;
                System.out.println("| ❌ Password cannot be empty.");
            }

            System.out.println("+" + "-".repeat(boxWidth) + "+");

            // ✅ Database check
            String sql = "SELECT * FROM administrator WHERE administrator_id = ? AND password = ?";
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                pstmt.setInt(1, adminId);
                pstmt.setString(2, password);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String adminName = rs.getString("administrator_name");
                        System.out.println("+" + "-".repeat(boxWidth) + "+");
                        System.out.printf("| %-57s |\n", "✅ Welcome Back, " + adminName + "!");
                        System.out.println("+" + "-".repeat(boxWidth) + "+");
                        adminLoginStatus = true;
                        return true;
                    } else {
                        System.out.println("+" + "-".repeat(boxWidth) + "+");
                        System.out.printf("| %-58s |\n", "❌ Invalid Administrator ID or Password!");
                        System.out.println("+" + "-".repeat(boxWidth) + "+");
                        return false;
                    }
                }
            }

        } catch (SQLException sqle) {
            System.out.println("+" + "-".repeat(boxWidth) + "+");
            System.out.printf("| %-58s |\n", "❌ Database error: " + sqle.getMessage());
            System.out.println("+" + "-".repeat(boxWidth) + "+");
        } catch (Exception e) {
            System.out.println("+" + "-".repeat(boxWidth) + "+");
            System.out.printf("| %-58s |\n", "❌ Unexpected error: " + e.getMessage());
            System.out.println("+" + "-".repeat(boxWidth) + "+");
        }

        return false; // default fail
    }



    // ✅ Safe Add Admin Method with Full Handling
    public static void addAdmin() {
        Scanner sc = new Scanner(System.in);
        int boxWidth = 60;

        try {
            // 1️⃣ Admin ID: 4 digits, positive, cannot start with 0
            int administrator_id;
            while (true) {
                System.out.print("| Enter 4-digit Admin ID (cannot start with 0) : ");
                try {
                    administrator_id = sc.nextInt();
                    sc.nextLine(); // consume newline
                    if (administrator_id >= 1000 && administrator_id <= 9999) break;
                    System.out.println("+" + "-".repeat(boxWidth) + "+");
                    System.out.printf("| %-58s |\n", "❌ Admin ID must be 4 digits and positive!");
                    System.out.println("+" + "-".repeat(boxWidth) + "+");
                } catch (InputMismatchException ime) {
                    System.out.println("+" + "-".repeat(boxWidth) + "+");
                    System.out.printf("| %-58s |\n", "❌ Invalid input! Enter numeric digits only.");
                    System.out.println("+" + "-".repeat(boxWidth) + "+");
                    sc.nextLine();
                }
            }

            // 2️⃣ Admin Name
            String administrator_name;
            while (true) {
                System.out.print("| Enter Admin Name : ");
                administrator_name = sc.nextLine().trim();
                if (!administrator_name.isEmpty()) break;
                System.out.println("+" + "-".repeat(boxWidth) + "+");
                System.out.printf("| %-58s |\n", "❌ Name cannot be empty!");
                System.out.println("+" + "-".repeat(boxWidth) + "+");
            }

            // 3️⃣ Password
            String password;
            while (true) {
                System.out.print("| Enter Password : ");
                password = sc.nextLine().trim();
                if (!password.isEmpty()) break;
                System.out.println("+" + "-".repeat(boxWidth) + "+");
                System.out.printf("| %-58s |\n", "❌ Password cannot be empty!");
                System.out.println("+" + "-".repeat(boxWidth) + "+");
            }

            // 4️⃣ Mobile Number: 10 digits, starts with 6-9, unique
            String mobileNo;
            while (true) {
                System.out.print("| Enter Mobile Number (10 digits, starts 6-9) : ");
                mobileNo = sc.nextLine().trim();
                if (mobileNo.matches("[6-9][0-9]{9}")) break;
                System.out.println("+" + "-".repeat(boxWidth) + "+");
                System.out.printf("| %-58s |\n", "❌ Invalid mobile number!");
                System.out.println("+" + "-".repeat(boxWidth) + "+");
            }

            // 5️⃣ Email: Gmail validation
            String administrator_email;
            while (true) {
                System.out.print("| Enter Email (must end with @gmail.com) : ");
                administrator_email = sc.nextLine().trim().toLowerCase();
                if (administrator_email.matches("[a-zA-Z0-9._%+-]+@gmail\\.com")) break;
                System.out.println("+" + "-".repeat(boxWidth) + "+");
                System.out.printf("| %-58s |\n", "❌ Invalid email. Must be Gmail.");
                System.out.println("+" + "-".repeat(boxWidth) + "+");
            }

            // 6️⃣ Insert into DB
            String sql = "INSERT INTO administrator (administrator_id, administrator_name, password, mobileNo, administrator_email) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                pstmt.setInt(1, administrator_id);
                pstmt.setString(2, administrator_name);
                pstmt.setString(3, password);
                pstmt.setString(4, mobileNo); // unique
                pstmt.setString(5, administrator_email);

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("+" + "-".repeat(boxWidth) + "+");
                    System.out.printf("| %-58s |\n", "✅ Administrator added successfully!");
                    System.out.println("+" + "-".repeat(boxWidth) + "+");
                } else {
                    System.out.println("+" + "-".repeat(boxWidth) + "+");
                    System.out.printf("| %-58s |\n", "❌ Failed to add administrator.");
                    System.out.println("+" + "-".repeat(boxWidth) + "+");
                }
            }

        } catch (SQLIntegrityConstraintViolationException dup) {
            System.out.println("+" + "-".repeat(boxWidth) + "+");
            System.out.printf("| %-58s |\n", "❌ Admin ID or Mobile or Email already exists!");
            System.out.println("+" + "-".repeat(boxWidth) + "+");
        } catch (SQLException sqle) {
            System.out.println("+" + "-".repeat(boxWidth) + "+");
            System.out.printf("| %-58s |\n", "❌ Database error: " + sqle.getMessage());
            System.out.println("+" + "-".repeat(boxWidth) + "+");
        } catch (Exception e) {
            System.out.println("+" + "-".repeat(boxWidth) + "+");
            System.out.printf("| %-58s |\n", "❌ Unexpected error: " + e.getMessage());
            System.out.println("+" + "-".repeat(boxWidth) + "+");
        }
    }


    // ✅ Method: Show Admin Info in Box Format
    public static void showAdminInfo() {
        int boxWidth = 100;

        try {
            String query = "SELECT administrator_id, administrator_name, mobileNo, administrator_email FROM administrator";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // 🔲 Top Border
            System.out.println("+" + "-".repeat(boxWidth) + "+");
            System.out.printf("| %-98s |\n", "👨‍💼 ADMINISTRATOR INFORMATION");
            System.out.println("+" + "-".repeat(12) + "+" + "-".repeat(25) + "+" + "-".repeat(15) + "+" + "-".repeat(40) + "+");
            System.out.printf("| %-10s | %-23s | %-13s | %-38s |\n",
                    "Admin ID", "Admin Name", "Mobile No", "Email");
            System.out.println("+" + "-".repeat(12) + "+" + "-".repeat(25) + "+" + "-".repeat(15) + "+" + "-".repeat(40) + "+");

            boolean found = false;
            while (rs.next()) {
                found = true;
                int id = rs.getInt("administrator_id");
                String name = rs.getString("administrator_name");
                String mobile = rs.getString("mobileNo");
                String email = rs.getString("administrator_email");

                System.out.printf("| %-10d | %-23s | %-13s | %-38s |\n", id, name, mobile, email);
            }

            if (!found) {
                System.out.printf("| %-98s |\n", "❌ No Admin Records Found.");
            }

            // 🔲 Bottom Border
            System.out.println("+" + "-".repeat(12) + "+" + "-".repeat(25) + "+" + "-".repeat(15) + "+" + "-".repeat(40) + "+");

        } catch (Exception e) {
            System.out.println("+" + "-".repeat(boxWidth) + "+");
            System.out.printf("| %-98s |\n", "❌ Error while fetching Admin Info: " + e.getMessage());
            System.out.println("+" + "-".repeat(boxWidth) + "+");
        }
    }

    // ✅ Method To Remove User {only admin can use this method}
    public static void removeUser(int userId) {
        int boxWidth = 50;

        String sql = "DELETE FROM users WHERE user_id = ?";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, userId);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("+" + "-".repeat(boxWidth) + "+");
                System.out.printf("| %-48s |\n", "✅ User with ID " + userId + " removed successfully.");
                System.out.println("+" + "-".repeat(boxWidth) + "+");
            } else {
                System.out.println("+" + "-".repeat(boxWidth) + "+");
                System.out.printf("| %-48s |\n", "❌ User ID " + userId + " not found in database.");
                System.out.println("+" + "-".repeat(boxWidth) + "+");
            }

        } catch (SQLException sqle) {
            System.out.println("+" + "-".repeat(boxWidth) + "+");
            System.out.printf("| %-48s |\n", "❌ Database error while removing user!");
            System.out.printf("| %-48s |\n", "   " + sqle.getMessage());
            System.out.println("+" + "-".repeat(boxWidth) + "+");
        } catch (Exception e) {
            System.out.println("+" + "-".repeat(boxWidth) + "+");
            System.out.printf("| %-48s |\n", "❌ Unexpected error occurred!");
            System.out.printf("| %-48s |\n", "   " + e.getMessage());
            System.out.println("+" + "-".repeat(boxWidth) + "+");
        }
    }




    // ✅ Method to Remove Admin {only admin can use this method}
    public static void removeAdmin(int administrator_id) {
        int boxWidth = 65;

        String sql = "DELETE FROM administrator WHERE administrator_id = ?";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, administrator_id);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("+" + "-".repeat(boxWidth) + "+");
                System.out.printf("| %-63s |\n", "✅ Administrator with ID " + administrator_id + " removed successfully.");
                System.out.println("+" + "-".repeat(boxWidth) + "+");
            } else {
                System.out.println("+" + "-".repeat(boxWidth) + "+");
                System.out.printf("| %-63s |\n", "❌ Administrator with ID " + administrator_id + " not found in database.");
                System.out.println("+" + "-".repeat(boxWidth) + "+");
            }

        } catch (SQLException sqle) {
            System.out.println("+" + "-".repeat(boxWidth) + "+");
            System.out.printf("| %-63s |\n", "❌ Database error while removing administrator!");
            System.out.printf("| %-63s |\n", "   " + sqle.getMessage());
            System.out.println("+" + "-".repeat(boxWidth) + "+");
        } catch (Exception e) {
            System.out.println("+" + "-".repeat(boxWidth) + "+");
            System.out.printf("| %-63s |\n", "❌ Unexpected error occurred!");
            System.out.printf("| %-63s |\n", "   " + e.getMessage());
            System.out.println("+" + "-".repeat(boxWidth) + "+");
        }
    }


    // Method to Update User Information

    static void changeInformationOfUser() {
        int choiceInfo = -1;

        do {
            System.out.println(
                    "+-------------------------------------------+\n" +
                            "|         UPDATE USER INFORMATION           |\n" +
                            "+-------------------------------------------+");
            System.out.println("| Case 1 : Change User Email                |");
            System.out.println("| Case 2 : Change User Password             |");
            System.out.println("| Case 3 : Change User Phone Number         |");
            System.out.println("| Case 0 : Exit                             |");
            System.out.print("| Enter your choice : ");

            try {
                choiceInfo = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("+-------------------------------------------+");
                System.out.println("| ❌ Invalid input! Enter a number.        |");
                System.out.println("+-------------------------------------------+");
                continue;
            }

            System.out.println("+-------------------------------------------+");

            switch (choiceInfo) {
                case 1: updateUserField("email", "User Email"); break;
                case 2: updateUserField("password", "User Password"); break;
                case 3: updatePhoneNumber(); break;
                case 0:
                    System.out.println(
                            "+-------------------------------------------+\n" +
                                    "| ✅ Exiting Update Menu...                 |\n" +
                                    "+-------------------------------------------+");
                    break;
                default:
                    System.out.println(
                            "+-------------------------------------------+\n" +
                                    "| ❌ Invalid Option                         |\n" +
                                    "+-------------------------------------------+");
            }

        } while (choiceInfo != 0);
    }


    // ✅ Generic function for Name, Email, Password
    static void updateUserField(String column, String fieldLabel) {
        System.out.println(
                "+-------------------------------------------+\n" +
                        "|       CHANGE " + fieldLabel.toUpperCase() + "          |\n" +
                        "+-------------------------------------------+");

        System.out.print("| Enter New " + fieldLabel + " : ");
        String newValue = sc.nextLine().trim();
        if (newValue.isEmpty()) {
            System.out.println("| ❌ New value cannot be empty.             |");
            System.out.println("+-------------------------------------------+");
            return;
        }

        String sql = "UPDATE users SET " + column + " = ? WHERE user_id = ?";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, newValue);
            pstmt.setInt(2, loggedInUserId); // 👈 Login se milne wala user id

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("| ✅ " + fieldLabel + " Changed Successfully       |");
            } else {
                System.out.println("| ❌ Update failed. Please try again.        |");
            }
            System.out.println("+-------------------------------------------+");

        } catch (SQLException e) {
            System.out.println("| ❌ Error: " + e.getMessage());
            System.out.println("+-------------------------------------------+");
        }
    }


    // ✅ Phone number update with validation (first digit 6-9)
    static void updatePhoneNumber() {
        String newPhone;

        while (true) {
            System.out.print("| Enter New Phone Number (10 digits, 6-9 start): ");
            newPhone = sc.nextLine().trim();
            if (newPhone.matches("[6-9]\\d{9}")) break;
            System.out.println("| ❌ Invalid Phone Number! Must start with 6-9 and 10 digits.");
        }

        String sql = "UPDATE users SET mobileNo = ? WHERE user_id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, newPhone);
            pstmt.setInt(2, loggedInUserId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("| ✅ Phone Number Changed Successfully       |");
            } else {
                System.out.println("| ❌ Update failed. Please try again.        |");
            }
            System.out.println("+-------------------------------------------+");

        } catch (SQLException e) {
            System.out.println("| ❌ Error: " + e.getMessage());
            System.out.println("+-------------------------------------------+");
        }
    }




    // Method to Change Information Of Admin
    // Assume: yeh variable tum login ke baad set karte ho


    public static void changeInformationOfAdmin() {
        Scanner sc = new Scanner(System.in);
        int boxWidth = 55;
        int choiceInfo = -1;

        do {
            System.out.println("+" + "-".repeat(boxWidth) + "+");
            System.out.printf("| %-53s |\n", "UPDATE ADMIN INFORMATION");
            System.out.println("+" + "-".repeat(boxWidth) + "+");
            System.out.printf("| %-53s |\n", "Case 1 : Change Admin Name");
            System.out.printf("| %-53s |\n", "Case 2 : Change Admin Email");
            System.out.printf("| %-53s |\n", "Case 3 : Change Admin Password");
            System.out.printf("| %-53s |\n", "Case 4 : Change Admin Mobile No");
            System.out.printf("| %-53s |\n", "Case 0 : Exit");
            System.out.println("+" + "-".repeat(boxWidth) + "+");

            System.out.print("| Enter your choice : ");
            String choiceInput = sc.nextLine();

            try {
                choiceInfo = Integer.parseInt(choiceInput);
            } catch (NumberFormatException e) {
                System.out.println("+" + "-".repeat(boxWidth) + "+");
                System.out.printf("| %-53s |\n", "❌ Invalid input! Please enter a number.");
                System.out.println("+" + "-".repeat(boxWidth) + "+");
                continue;
            }

            if (choiceInfo == 0) {
                System.out.println("+" + "-".repeat(boxWidth) + "+");
                System.out.printf("| %-53s |\n", "✅ Exiting Update Menu...");
                System.out.println("+" + "-".repeat(boxWidth) + "+");
                break;
            }

            try {
                String sql = "";
                String newValue = "";
                switch (choiceInfo) {
                    case 1: // Name
                        System.out.print("| Enter New Admin Name : ");
                        newValue = sc.nextLine().trim();
                        if (newValue.isEmpty()) {
                            System.out.println("+" + "-".repeat(boxWidth) + "+");
                            System.out.printf("| %-53s |\n", "❌ Admin name cannot be empty.");
                            System.out.println("+" + "-".repeat(boxWidth) + "+");
                            break;
                        }
                        sql = "UPDATE administrator SET administrator_name=? WHERE administrator_id=?";
                        break;

                    case 2: // Email
                        System.out.print("| Enter New Admin Email : ");
                        newValue = sc.nextLine().trim();
                        if (!newValue.matches("[a-zA-Z0-9._%+-]+@gmail\\.com")) {
                            System.out.println("+" + "-".repeat(boxWidth) + "+");
                            System.out.printf("| %-53s |\n", "❌ Invalid Gmail address.");
                            System.out.println("+" + "-".repeat(boxWidth) + "+");
                            break;
                        }
                        sql = "UPDATE administrator SET administrator_email=? WHERE administrator_id=?";
                        break;

                    case 3: // Password
                        System.out.print("| Enter New Admin Password : ");
                        newValue = sc.nextLine().trim();
                        if (newValue.isEmpty()) {
                            System.out.println("+" + "-".repeat(boxWidth) + "+");
                            System.out.printf("| %-53s |\n", "❌ Password cannot be empty.");
                            System.out.println("+" + "-".repeat(boxWidth) + "+");
                            break;
                        }
                        sql = "UPDATE administrator SET password=? WHERE administrator_id=?";
                        break;

                    case 4: // Mobile No
                        System.out.print("| Enter New Admin Mobile No : ");
                        newValue = sc.nextLine().trim();
                        if (!newValue.matches("^[6-9][0-9]{9}$")) {
                            System.out.println("+" + "-".repeat(boxWidth) + "+");
                            System.out.printf("| %-53s |\n", "❌ Invalid Mobile No! Must start with 6/7/8/9 and be 10 digits.");
                            System.out.println("+" + "-".repeat(boxWidth) + "+");
                            break;
                        }
                        sql = "UPDATE administrator SET mobileNo=? WHERE administrator_id=?";
                        break;

                    default:
                        System.out.println("+" + "-".repeat(boxWidth) + "+");
                        System.out.printf("| %-53s |\n", "❌ Invalid choice! Please try again.");
                        System.out.println("+" + "-".repeat(boxWidth) + "+");
                        break;
                }

                if (!sql.isEmpty()) {
                    try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                        pstmt.setString(1, newValue);
                        pstmt.setInt(2, loggedInAdminId); // 👈 Login ki ID

                        int rowsAffected = pstmt.executeUpdate();
                        System.out.println("+" + "-".repeat(boxWidth) + "+");
                        if (rowsAffected > 0) {
                            System.out.printf("| %-53s |\n", "✅ Admin information updated successfully!");
                            System.out.println("+" + "-".repeat(boxWidth) + "+");

                            // 🔥 Ab updated admin info table format me print karo
                            showUpdatedAdmin(loggedInAdminId);

                        } else {
                            System.out.printf("| %-53s |\n", "❌ Admin not found. No changes made.");
                            System.out.println("+" + "-".repeat(boxWidth) + "+");
                        }
                    }
                }

            } catch (SQLException sqle) {
                System.out.println("+" + "-".repeat(boxWidth) + "+");
                System.out.printf("| %-53s |\n", "❌ Database error: " + sqle.getMessage());
                System.out.println("+" + "-".repeat(boxWidth) + "+");
            } catch (Exception e) {
                System.out.println("+" + "-".repeat(boxWidth) + "+");
                System.out.printf("| %-53s |\n", "❌ Unexpected error: " + e.getMessage());
                System.out.println("+" + "-".repeat(boxWidth) + "+");
            }

        } while (true);

    }

    // ✅ Helper method to print updated Admin info
    public static void showUpdatedAdmin(int adminId) {
        int boxWidth = 95;
        try {
            String query = "SELECT administrator_id, administrator_name, mobileNo, administrator_email FROM administrator WHERE administrator_id=?";
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setInt(1, adminId);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    System.out.println("+" + "-".repeat(boxWidth) + "+");
                    System.out.printf("| %-95s |\n", " UPDATED ADMIN INFORMATION");
                    System.out.println("+" + "-".repeat(12) + "+" + "-".repeat(25) + "+" + "-".repeat(15) + "+" + "-".repeat(40) + "+");
                    System.out.printf("| %-10s | %-23s | %-13s | %-38s |\n",
                            "Admin ID", "Admin Name", "Mobile No", "Email");
                    System.out.println("+" + "-".repeat(12) + "+" + "-".repeat(25) + "+" + "-".repeat(15) + "+" + "-".repeat(40) + "+");

                    System.out.printf("| %-10d | %-23s | %-13s | %-38s |\n",
                            rs.getInt("administrator_id"),
                            rs.getString("administrator_name"),
                            rs.getString("mobileNo"),
                            rs.getString("administrator_email"));

                    System.out.println("+" + "-".repeat(12) + "+" + "-".repeat(25) + "+" + "-".repeat(15) + "+" + "-".repeat(40) + "+");
                }
            }
        } catch (SQLException e) {
            System.out.println("+" + "-".repeat(boxWidth) + "+");
            System.out.printf("| %-93s |\n", "❌ Error fetching updated info: " + e.getMessage());
            System.out.println("+" + "-".repeat(boxWidth) + "+");
        }
    }






    // Method to Display All Users {only admin can use this method}
    public static void displayUsers() {
        int boxWidth = 80;
        String sql = "SELECT user_id, user_name, email, mobileNo FROM users";

        try (PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            // Header
            System.out.println("+" + "-".repeat(boxWidth) + "+");
            System.out.printf("| %-78s |\n", "📋 USER LIST");
            System.out.println("+" + "-".repeat(8) + "+" + "-".repeat(17) + "+" + "-".repeat(25) + "+" + "-".repeat(13) + "+");

            System.out.printf("| %-6s | %-15s | %-23s | %-11s |\n",
                    "ID", "User Name", "Email", "Phone No");
            System.out.println("+" + "-".repeat(8) + "+" + "-".repeat(17) + "+" + "-".repeat(25) + "+" + "-".repeat(13) + "+");

            boolean found = false;
            while (rs.next()) {
                int userId = rs.getInt("user_id");
                String userName = rs.getString("user_name");
                String email = rs.getString("email");
                String mobileNo = rs.getString("mobileNo");

                // Null-safe
                if (userName == null || userName.isEmpty()) userName = "N/A";
                if (email == null || email.isEmpty()) email = "N/A";
                if (mobileNo == null || mobileNo.isEmpty()) mobileNo = "N/A";

                System.out.printf("| %-6d | %-15s | %-23s | %-11s |\n",
                        userId, userName, email, mobileNo);
                found = true;
            }

            if (!found) {
                System.out.printf("| %-78s |\n", "❌ No users found in database.");
            }

            // Footer
            System.out.println("+" + "-".repeat(8) + "+" + "-".repeat(17) + "+" + "-".repeat(25) + "+" + "-".repeat(13) + "+");

        } catch (SQLException e) {
            System.out.println("+" + "-".repeat(boxWidth) + "+");
            System.out.printf("| %-78s |\n", "⚠️ Error fetching user list: " + e.getMessage());
            System.out.println("+" + "-".repeat(boxWidth) + "+");
        } catch (Exception e) {
            System.out.println("+" + "-".repeat(boxWidth) + "+");
            System.out.printf("| %-78s |\n", "❌ Unexpected error occurred: " + e.getMessage());
            System.out.println("+" + "-".repeat(boxWidth) + "+");
        }
    }






    //Method For Book Details
    static void viewBooks() {
        String sql = "SELECT * FROM book_list";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println(
                    "+-------------------------------------------------------------------------------------------------------------+\n" +
                            "|                                           📚 AVAILABLE BOOKS                                                |\n" +
                            "+------------+---------------------------+---------------+------------+--------------------+------------+-----+"
            );
            System.out.printf("| %-10s | %-25s | %-13s | %-10s | %-18s | %-10s |\n",
                    "ISBN", "Name", "Rent Price", "Rating", "Author", "Copies");
            System.out.println(
                    "+------------+---------------------------+---------------+------------+--------------------+------------+");

            boolean found = false;
            while (rs.next()) {
                // Null-safe values
                String isbn   = String.valueOf(rs.getInt("book_isbn_no"));
                String name   = rs.getString("book_name");
                String price  = String.valueOf(rs.getInt("rent_price"));
                String rating = String.valueOf(rs.getDouble("rating"));
                String author = rs.getString("author_name");
                String copies = String.valueOf(rs.getInt("copies"));

                if (name == null) name = "N/A";
                if (author == null) author = "N/A";

                System.out.printf("| %-10s | %-25s | %-13s | %-10s | %-18s | %-10s |\n",
                        isbn, name, price, rating, author, copies);
                found = true;
            }

            if (!found) {
                System.out.println("|                                ❌ No books found in database.                                               |");
            }

            System.out.println("+------------+---------------------------+---------------+------------+--------------------+------------+");

        } catch (Exception e) {
            System.out.println(
                    "+-------------------------------------------------------------------------------------------------------------+\n" +
                            "| ⚠️ Error fetching books: " + e.getMessage() + "\n" +
                            "+-------------------------------------------------------------------------------------------------------------+"
            );
        }
    }


    // Method To Delete Book..
    public static void deleteByBookNo(int book_no) {
        String sql = "DELETE FROM book_list WHERE book_isbn_no = ?";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, book_no);

            int rowsAffected = pstmt.executeUpdate();

            System.out.println("+---------------------------------------------------+");
            if (rowsAffected > 0) {
                System.out.println("|  ✅ Book with ISBN " + book_no + " deleted successfully.   |");
            } else {
                System.out.println("|  ❌ No book found with ISBN: " + book_no + "              |");
            }
            System.out.println("+---------------------------------------------------+");

        } catch (SQLException e) {
            System.out.println(
                    "+--------------------------------------------------+\n" +
                            "| ⚠️ Error deleting book: " + e.getMessage() + "\n" +
                            "+--------------------------------------------------+"
            );
        }
    }

    public static void showUserLogs() {
        int boxWidth = 100;
        try {
            String query = "SELECT log_id, user_id, user_name, action, " +
                    "COALESCE(DATE_FORMAT(deleted_at, '%Y-%m-%d %H:%i:%s'), 'N/A') AS deleted_at " +
                    "FROM user_logs ORDER BY log_id DESC";

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            System.out.println("+" + "-".repeat(boxWidth) + "+");
            System.out.printf("| %-98s |\n", "📜 USER/ADMIN DELETE LOGS");
            System.out.println("+" + "-".repeat(6) + "+" + "-".repeat(10) + "+" + "-".repeat(20) + "+" + "-".repeat(25) + "+" + "-".repeat(30) + "+");
            System.out.printf("| %-4s | %-8s | %-18s | %-23s | %-28s |\n",
                    "ID", "UserID", "User/Admin Name", "Action", "Deleted At");
            System.out.println("+" + "-".repeat(6) + "+" + "-".repeat(10) + "+" + "-".repeat(20) + "+" + "-".repeat(25) + "+" + "-".repeat(30) + "+");

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("| %-4d | %-8d | %-18s | %-23s | %-28s |\n",
                        rs.getInt("log_id"),
                        rs.getInt("user_id"),
                        rs.getString("user_name"),
                        rs.getString("action"),
                        rs.getString("deleted_at"));
            }

            if (!found) {
                System.out.printf("| %-98s |\n", "⚠️ No logs found.");
            }

            System.out.println("+" + "-".repeat(6) + "+" + "-".repeat(10) + "+" + "-".repeat(20) + "+" + "-".repeat(25) + "+" + "-".repeat(30) + "+");

        } catch (Exception e) {
            System.out.println("+" + "-".repeat(boxWidth) + "+");
            System.out.printf("| %-98s |\n", "❌ Error fetching logs: " + e.getMessage());
            System.out.println("+" + "-".repeat(boxWidth) + "+");
        }
    }


    // Method To Display All Bookings
    public static void displayAllBookings(Connection con) {
        // Sirf NOT_RETURNED bookings
        String sql = "SELECT * FROM book_rentals WHERE return_status = 'NOT_RETURNED'";

        int boxWidth3 = 83; // total table width

        try (PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            // Table Header
            System.out.println("+" + "-".repeat(boxWidth3) + "+");
            System.out.printf("| %-81s |\n", "📖 ACTIVE BOOK RENTALS LIST");
            System.out.println("+" + "-".repeat(17) + "+" + "-".repeat(15) + "+" + "-".repeat(15) + "+" + "-".repeat(15) + "+-----------------+");

            System.out.printf("| %-15s | %-13s | %-13s | %-13s |\n",
                    "User Name", "Book ISBN", "Rent Date", "Return Date");
            System.out.println("+" + "-".repeat(17) + "+" + "-".repeat(15) + "+" + "-".repeat(15) + "+" + "-".repeat(15) + "+");

            boolean found = false;
            while (rs.next()) {
                String userName = rs.getString("user_name");
                int bookNo = rs.getInt("book_isbn_no");
                Date rentDate = rs.getDate("rent_date");
                Date returnDate = rs.getDate("return_date");

                if (userName == null) userName = "N/A";

                System.out.printf("| %-15s | %-13s | %-13s | %-13s |\n",
                        userName,
                        String.format("%06d", bookNo), // 6-digit ISBN
                        rentDate != null ? rentDate.toString() : "N/A",
                        returnDate != null ? returnDate.toString() : "N/A"
                );
                found = true;
            }

            if (!found) {
                System.out.printf("| %-81s |\n", "❌ No active bookings found in database.");
            }

            // Bottom border
            System.out.println("+" + "-".repeat(17) + "+" + "-".repeat(15) + "+" + "-".repeat(15) + "+" + "-".repeat(15) + "+");

        } catch (SQLException e) {
            System.out.println("+" + "-".repeat(boxWidth3) + "+");
            System.out.printf("| %-81s |\n", "⚠️ Error fetching bookings: " + e.getMessage());
            System.out.println("+" + "-".repeat(boxWidth3) + "+");
        }
    }


}

// Book Class..
class Book {
    String book_name;
    int book_no;
    double rating;
    double rent_price;
    String author_name;
    int copies;


    public Book(String book_name, int book_no, double rating, double rent_price, String author_name, int copies) {
        this.book_name = book_name;
        this.book_no = book_no;
        this.rating = rating;
        this.rent_price = rent_price;
        this.author_name = author_name;
        this.copies = copies;

    }


    // Getters And Setters..
    public String getBookName() {
        return book_name;
    }

    public int getBookNo() {
        return book_no;
    }

    public double getRating() {
        return rating;
    }

    public double getRentPrice() {
        return rent_price;
    }

    public String getAuthorName() { return author_name; }

    public void setBookName(String book_name) {
        this.book_name = book_name;
    }

    public void setBookNo(int book_no) {
        this.book_no = book_no;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setRentPrice(double rent_price) {
        this.rent_price = rent_price;
    }

    public void setAuthorName(String author_name) { this.author_name = author_name; }

    public int getCopies() { return copies; }
}

// StackBook Class..
class StackBook {

    static Connection con;
    int top;
    Book[] book1 = new Book[100];

    public StackBook(Connection con) {
        StackBook.con = con;
        this.top = -1;
    }

    public StackBook() {
        this.top = -1;
    }

    // Load all books from DB into stack (sorted by rating)
    public void in() {
        if (con == null) {
            System.out.println("| ❌ Database connection not established! |");
            return;
        }

        top = -1; // reset stack before reloading
        String sql = "SELECT * FROM book_list ORDER BY rating DESC";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String book_name = rs.getString("book_name");
                int isbn_no = rs.getInt("book_isbn_no");
                int rent_price = rs.getInt("rent_price");
                double rating = rs.getDouble("rating");
                String author_name = rs.getString("author_name");
                int copies = rs.getInt("copies");

                Book book = new Book(book_name, isbn_no, rating, rent_price, author_name, copies);
                if (top < book1.length - 1) {
                    top++;
                    book1[top] = book;
                } else {
                    System.out.println("| ⚠ Stack full, cannot load more books |");
                    break;
                }
            }

            if (top == -1) {
                System.out.println("| ⚠ No books found in database. |");
            }

        } catch (SQLException e) {
            System.out.println("| ❌ Error loading books: " + e.getMessage() + " |");
        }
    }


    // Method To Push (Add Book in Stack + DB)
    public void push(Book book) {
        int boxWidth = 45; // Box width for messages
        try {
            if (top >= book1.length - 1) {
                System.out.println("+" + "-".repeat(boxWidth) + "+");
                System.out.printf("| %-42s |\n", "⚠️ Stack Overflow: Cannot add more books.");
                System.out.println("+" + "-".repeat(boxWidth) + "+");
                return;
            }
            top++;
            book1[top] = book;
            addBook(); // database insert
            System.out.println("+" + "-".repeat(boxWidth) + "+");
            System.out.printf("| %-43s |\n", "✅ Book added successfully: " + book.getBookName());
            System.out.println("+" + "-".repeat(boxWidth) + "+");
        } catch (Exception e) {
            System.out.println("+" + "-".repeat(boxWidth) + "+");
            System.out.printf("| %-43s |\n", "❌ Error: " + e.getMessage());
            System.out.println("+" + "-".repeat(boxWidth) + "+");
        }
    }


    // Method To Insert Book into DB
    private void addBook() {
        int boxWidth = 85;

        // Validate book object exists
        if (book1[top] == null) {
            System.out.println("+" + "-".repeat(boxWidth) + "+");
            System.out.printf("| %-83s |\n", "❌ No book data available to add.");
            System.out.println("+" + "-".repeat(boxWidth) + "+");
            return;
        }

        // Input validations
        if (book1[top].book_no <= 0) {
            System.out.println("+" + "-".repeat(boxWidth) + "+");
            System.out.printf("| %-83s |\n", "❌ Invalid ISBN number.");
            System.out.println("+" + "-".repeat(boxWidth) + "+");
            return;
        }
        if (book1[top].book_name == null || book1[top].book_name.isEmpty()) {
            System.out.println("+" + "-".repeat(boxWidth) + "+");
            System.out.printf("| %-83s |\n", "❌ Book name cannot be empty.");
            System.out.println("+" + "-".repeat(boxWidth) + "+");
            return;
        }
        if (book1[top].rent_price < 0) {
            System.out.println("+" + "-".repeat(boxWidth) + "+");
            System.out.printf("| %-83s |\n", "❌ Rent price cannot be negative.");
            System.out.println("+" + "-".repeat(boxWidth) + "+");
            return;
        }
        if (book1[top].rating < 0 || book1[top].rating > 10) { // ✅ updated 0–10
            System.out.println("+" + "-".repeat(boxWidth) + "+");
            System.out.printf("| %-83s |\n", "❌ Rating must be between 0 and 10.");
            System.out.println("+" + "-".repeat(boxWidth) + "+");
            return;
        }
        if (book1[top].author_name == null || book1[top].author_name.isEmpty()) {
            System.out.println("+" + "-".repeat(boxWidth) + "+");
            System.out.printf("| %-83s |\n", "❌ Author name cannot be empty.");
            System.out.println("+" + "-".repeat(boxWidth) + "+");
            return;
        }
        if (book1[top].copies <= 0 || book1[top].copies > 50) { // ✅ copies check
            System.out.println("+" + "-".repeat(boxWidth) + "+");
            System.out.printf("| %-83s |\n", "❌ Copies must be between 1 and 50.");
            System.out.println("+" + "-".repeat(boxWidth) + "+");
            return;
        }

        // DB insert
        String sql = "INSERT INTO book_list (book_isbn_no, book_name, rent_price, rating, author_name, copies) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, book1[top].book_no);
            pstmt.setString(2, book1[top].book_name);
            pstmt.setDouble(3, book1[top].rent_price);
            pstmt.setDouble(4, book1[top].rating);
            pstmt.setString(5, book1[top].author_name);
            pstmt.setInt(6, book1[top].copies); // ✅ user input copies

            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                System.out.println("+" + "-".repeat(boxWidth) + "+");
                System.out.printf("| %-83s |\n", "✅ BOOK ADDED SUCCESSFULLY");
                System.out.println("+" + "-".repeat(boxWidth) + "+");

                // Table Header
                System.out.println("+----------+----------------------------+------------+--------+----------------------+--------+");
                System.out.printf("| %-8s | %-26s | %-10s | %-6s | %-20s | %-6s |\n",
                        "ISBN", "Book Name", "Rent Price", "Rating", "Author", "Copies");
                System.out.println("+----------+----------------------------+------------+--------+----------------------+--------+");


                // Table Row
                System.out.printf("| %-8d | %-26s | %-9.2f | %-6.1f | %-14s | %-5d |\n",
                        book1[top].book_no,
                        book1[top].book_name,
                        book1[top].rent_price,
                        book1[top].rating,
                        book1[top].author_name,
                        book1[top].copies);

                System.out.println("+----------+----------------------------+-----------+--------+----------------+-------+");
            } else {
                System.out.println("+" + "-".repeat(boxWidth) + "+");
                System.out.printf("| %-83s |\n", "⚠️ Book could not be added. Please try again.");
                System.out.println("+" + "-".repeat(boxWidth) + "+");
            }

        } catch (SQLIntegrityConstraintViolationException dup) {
            System.out.println("+" + "-".repeat(boxWidth) + "+");
            System.out.printf("| %-83s |\n", "❌ Duplicate ISBN! Book already exists.");
            System.out.println("+" + "-".repeat(boxWidth) + "+");

        } catch (SQLException e) {
            System.out.println("+" + "-".repeat(boxWidth) + "+");
            System.out.printf("| %-83s |\n", "❌ Database error: " + e.getMessage());
            System.out.println("+" + "-".repeat(boxWidth) + "+");
        } catch (Exception e) {
            System.out.println("+" + "-".repeat(boxWidth) + "+");
            System.out.printf("| %-83s |\n", "❌ Unexpected error: " + e.getMessage());
            System.out.println("+" + "-".repeat(boxWidth) + "+");
        }
    }


    // Method To Peek Top N Books (default: top 3 rated)
    public void peekTop3() {
        int boxWidth = 100;
        int n = 3; // top 3 books

        try {
            // ✅ Fetch top 3 by rating directly from DB
            String query = "SELECT book_isbn_no, book_name, rent_price, rating, author_name, copies " +
                    "FROM book_list ORDER BY rating DESC LIMIT " + n;
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // Agar koi book hi nahi hai
            if (!rs.isBeforeFirst()) {
                System.out.println("+" + "-".repeat(boxWidth) + "+");
                System.out.printf("| %-98s |\n", "⚠️ No books available in database.");
                System.out.println("+" + "-".repeat(boxWidth) + "+");
                return;
            }

            // Header
            System.out.println("+" + "-".repeat(boxWidth) + "+");
            System.out.printf("| %-98s |\n", "📚 TOP 3 BOOKS BY RATING");
            System.out.println("+" + "-".repeat(10) + "+" + "-".repeat(28) + "+" + "-".repeat(13) + "+" + "-".repeat(10) + "+" + "-".repeat(20) + "+" + "-".repeat(8) + "+");

            System.out.printf("| %-8s | %-26s | %-11s | %-8s | %-18s | %-6s |\n",
                    "ISBN", "Book Name", "Rent Price", "Rating", "Author", "Copies");

            System.out.println("+" + "-".repeat(10) + "+" + "-".repeat(28) + "+" + "-".repeat(13) + "+" + "-".repeat(10) + "+" + "-".repeat(20) + "+" + "-".repeat(8) + "+");

            // Data rows
            while (rs.next()) {
                System.out.printf("| %-8d | %-26s | %-11.2f | %-8.1f | %-18s | %-6d |\n",
                        rs.getInt("book_isbn_no"),      // ISBN
                        rs.getString("book_name"),     // Book Name
                        rs.getDouble("rent_price"),    // Rent Price
                        rs.getDouble("rating"),        // Rating
                        rs.getString("author_name"),   // Author
                        rs.getInt("copies"));          // Copies
            }

            System.out.println("+" + "-".repeat(10) + "+" + "-".repeat(28) + "+" + "-".repeat(13) + "+" + "-".repeat(10) + "+" + "-".repeat(20) + "+" + "-".repeat(8) + "+");

        } catch (Exception e) {
            System.out.println("+" + "-".repeat(boxWidth) + "+");
            System.out.printf("| %-98s |\n", "❌ Unexpected error: " + e.getMessage());
            System.out.println("+" + "-".repeat(boxWidth) + "+");
        }
    }





    // Method To Display All Books from DB
    public void displayBookList() {
        String sql = "SELECT * FROM book_list";
        int boxWidth = 100;

        try (PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            // Header
            System.out.println("+" + "-".repeat(boxWidth) + "+");
            System.out.printf("| %-98s |\n", "📚 BOOK LIST");
            System.out.println("+" + "-".repeat(12) + "+" + "-".repeat(30) + "+" + "-".repeat(22) + "+" + "-".repeat(9) + "+" + "-".repeat(10) + "+" + "-".repeat(12) + "+");

            System.out.printf("| %-10s | %-28s | %-20s | %-7s | %-8s | %-10s |\n",
                    "ISBN", "Book Name", "Author", "Rating", "Price", "Copies");
            System.out.println("+" + "-".repeat(12) + "+" + "-".repeat(30) + "+" + "-".repeat(22) + "+" + "-".repeat(9) + "+" + "-".repeat(10) + "+" + "-".repeat(12) + "+");

            boolean found = false;
            while (rs.next()) {
                int bookNo = rs.getInt("book_isbn_no");
                String bookName = rs.getString("book_name");
                String authorName = rs.getString("author_name");
                double rating = rs.getDouble("rating");
                double rentPrice = rs.getDouble("rent_price");
                int copies = rs.getInt("copies");

                // Null-safe
                if (bookName == null || bookName.isEmpty()) bookName = "N/A";
                if (authorName == null || authorName.isEmpty()) authorName = "N/A";

                System.out.printf("| %-10d | %-28s | %-20s | %-7.1f | %-8d | %-10d |\n",
                        bookNo, bookName, authorName, rating, rentPrice, copies);
                found = true;
            }

            if (!found) {
                System.out.println("|" + String.format("%-98s", "❌ No books found in database.") + "|");
            }

            // Footer
            System.out.println("+" + "-".repeat(12) + "+" + "-".repeat(30) + "+" + "-".repeat(22) + "+" + "-".repeat(9) + "+" + "-".repeat(10) + "+" + "-".repeat(12) + "+");

        } catch (SQLException sqle) {
            System.out.println("+" + "-".repeat(boxWidth) + "+");
            System.out.printf("| %-98s |\n", "⚠️ Database error: " + sqle.getMessage());
            System.out.println("+" + "-".repeat(boxWidth) + "+");
        } catch (Exception e) {
            System.out.println("+" + "-".repeat(boxWidth) + "+");
            System.out.printf("| %-98s |\n", "❌ Unexpected error: " + e.getMessage());
            System.out.println("+" + "-".repeat(boxWidth) + "+");
        }
    }
}
