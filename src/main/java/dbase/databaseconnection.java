package dbase;
import java.sql.Connection;
import java.sql.DriverManager;

public class databaseconnection {
    
    //database parameters
    private static final String URL = "jdbc:mysql://localhost:3308/db_dentist";
    private static final String USER = "root";
    private static final String PASSWORD = "";
       
    public static Connection getConnection(){
        Connection conn = null;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            
            System.out.println("Connected Successfully");
        } 
        catch (Exception e) {
            System.err.println("Error");
            e.printStackTrace();
        }
        
        return conn;
    }
}
