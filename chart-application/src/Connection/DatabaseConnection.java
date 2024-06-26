package Connection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static DatabaseConnection instance;
    private static Connection connection;

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    private DatabaseConnection() {

    }

    public static void connectToDatabase()  {
        String server = "localhost";
        String port = "1433";
        String database = "ChatApp";
        String username = "sa";
        String password = "123";
        
        if (connection==null){
            try {
                /*Nạp driver */
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                 /*Kết nối đến cơ sở dữ liệu */
                String url = "jdbc:sqlserver://HUY:1433;databaseName=ChatApp;encrypt=true;trustServerCertificate=true";

                connection = DriverManager.getConnection(url, username, password);
            } catch (Exception e) {
                System.out.println("BUG: "+e);
            }
            
        } 
//        connection = java.sql.DriverManager.getConnection("jdbc:mysql://" + server + ":" + port + "/" + database, userName, password);
        
    }

    public static Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}