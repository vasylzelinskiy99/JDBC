package com.pavelchak.jdbctest;
import java.sql.*;
import java.util.Scanner;

public class Application {
    private static final String url = "jdbc:mysql://localhost:3306/vasia_zel_5?serverTimezone=UTC&useSSL=false";
    private static final String user = "root";
    private static final String password = "12345";

    private static Connection connection=null;
    private static Statement statement=null;
    private static ResultSet rs=null;

    public static void menu()throws SQLException{
        System.out.println("1.readData();\n" +
                "2.updateDataLibrary();\n" +
                "3.insertDataLibrary();\n" +
                "4.DeleteDataLibrary();\n" +
                "5.CallProcedureForInsertToReadersBook(); ");
        Scanner input = new Scanner(System.in);
        int choice = input.nextInt();

        if(choice == 1){
            readData();

        }

        if(choice == 2){
            updateDataLibrary();
        }
        if(choice == 3){
            insertDataLibrary();
        }
        if(choice == 4){
            DeleteDataLibrary();

        }

        if(choice == 5){
            CallProcedureForInsertToReadersBook();

        }
        else{
            rec();
        }
    }
    public static void rec() throws SQLException {
        menu();
    }
    public static void main(String args[]){
        try {
//region    0. This will load the MySQL driver, each DB has its own driver //
            Class.forName("com.mysql.cj.jdbc.Driver");
            //endregion


//region    1. Get a connection to database //
            connection = DriverManager.getConnection(url, user, password);
            //endregion

//region  2. Create a statement
            // Statements allow to issue SQL queries to the database
            statement=connection.createStatement();
            //endregion

            menu();


            //updateDataLibrary();
//            readData();

            //insertDataLibrary();

            //DeleteDataLibrary();

            // CallProcedureForInsertToReadersBook();


        } catch (ClassNotFoundException e) {
            System.out.println("MySQL Driver is not loaded");

        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

        } finally {
            //close connection ,statement and resultset
            if (rs != null) try { rs.close(); } catch (SQLException e) { } // ignore
            if (statement != null) try { statement.close(); } catch (SQLException e) { }
            if (connection != null) try { connection.close(); } catch (SQLException e) { }
        }
    }

    private static void readData() throws SQLException {
//region    SELECT COUNT(*) FROM Reader //
        // 3. executing SELECT query
        rs=statement.executeQuery("SELECT COUNT(*) FROM vasia_zel_5.Reader");

        // 4. Process the result set
        while (rs.next()) {
            int count = rs.getInt(1);
            // Simply Print the results
            System.out.format("\ncount: %d\n", count);
        }
        //endregion

//region    SELECT * FROM Reader //
        // 3. executing SELECT query
        rs=statement.executeQuery("SELECT * FROM vasia_zel_5.Reader");
// 4. Process the result set
        System.out.format("\nTable Reader —------------------\n");
        System.out.format("%3s %-12s %-12s %-10s %s\n", "ID", "Surname", "Name", "Library", "Email");
        while (rs.next())
        {
            int id=rs.getInt("ID_Reader");
            String surname = rs.getString("Surname");
            String name = rs.getString("Name");
            String library=rs.getString("Library");
            String email=rs.getString("Email");
            // Simply Print the results
            System.out.format("%3d %-12s %-12s %-10s %s\n", id, surname, name, library, email);
        }
        //endregion

//region    SELECT * FROM Book //
        // 3. executing SELECT query
        rs=statement.executeQuery("SELECT * FROM vasia_zel_5.Book");

        // 4. Process the result set
        System.out.format("\nTable Book —------------------\n");
        System.out.format("%3s %-18s %-18s %s\n", "ID", "BookName", "Author", "Amount");
        while (rs.next())
        {
            int id=rs.getInt("ID_Book");
            String bookName = rs.getString("BookName");
            String author = rs.getString("Author");
            String amount=rs.getString("Amount");
            // Simply Print the results
            System.out.format("%3d %-18s %-18s %s\n", id, bookName, author, amount);
        }
        //endregion

//region    SELECT * FROM Library//
        // 3. executing SELECT query
        rs=statement.executeQuery("SELECT * FROM vasia_zel_5.Library");

        // 4. Process the result set
        System.out.format("\nTable Library —------------------\n");
        System.out.format("%s\n", "Library");
        while (rs.next())
        {
            String library = rs.getString("Library");
            // Simply Print the results
            System.out.format("%s\n", library);
        }
        //endregion

//region    SELECT * FROM ReadersBook //
        // 3. executing SELECT query
        String query="Select " +
                "(SELECT Surname FROM vasia_zel_5.Reader WHERE ID_Reader=P.ID_Reader) AS Surname, " +
                "(SELECT BookName FROM vasia_zel_5.book WHERE ID_Book=P.ID_Book) AS BookName "+
                "FROM vasia_zel_5.readersbook AS P";
        rs=statement.executeQuery(query);

        // 4. Process the result set
        System.out.format("\nJoining Table ReadersBook —------------------\n");
        System.out.format("%-15s %s\n", "Surname", "BookName");
        while (rs.next())
        {
            String surname = rs.getString("Surname");
            String bookName = rs.getString("BookName");
            // Simply Print the results
            System.out.format("%-15s %s\n", surname, bookName);
        }
        //endregion

    }

    private static void updateDataLibrary() throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("Input name library what you want to update: ");
        String library = input.nextLine();
        System.out.println("Input new name library for %s: "+ library);
        String librarynew = input.nextLine();


        statement.execute("UPDATE vasia_zel_5.library SET Library='"+librarynew+"' WHERE Library='"+library+"';");

    }

    private static void insertDataLibrary() throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("Input a new library name: ");
        String newlibrary = input.nextLine();

        PreparedStatement preparedStatement;
        preparedStatement=connection.prepareStatement("INSERT vasia_zel_5.library VALUES (?)");
        preparedStatement.setString(1, newlibrary);
        int n=preparedStatement.executeUpdate();
        System.out.println("Count rows that inserted: "+n);

    }


    private static void DeleteDataLibrary() throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("Input a name library for delete: ");
        String library = input.nextLine();

        // 3. executing SELECT query
        //   PreparedStatements can use variables and are more efficient
        PreparedStatement preparedStatement;
        preparedStatement=connection.prepareStatement("DELETE FROM vasia_zel_5.library WHERE Library=?");
        preparedStatement.setString(1, library);
        int n=preparedStatement.executeUpdate();
        System.out.println("Count rows that deleted: "+n);
    }

    private static void CallProcedureForInsertToReadersBook() throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("\nInput Surname for Reader: ");
        String surname = input.next();
        System.out.println("Input NameBook for Book: ");
        String book = input.next();

        CallableStatement callableStatement;
        callableStatement= connection.prepareCall("{call InsertReadersBook(?, ?)}");
        callableStatement.setString("SurnameReaderIn",surname);
        callableStatement.setString("BookNameIN",book);
        ResultSet rs = callableStatement.executeQuery();

        while (rs.next())
        {
            String msg = rs.getString(1);
            // Simply Print the results
            System.out.format("\nResult: "+msg);
        }
    }

}