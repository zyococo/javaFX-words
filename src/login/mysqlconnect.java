package login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class mysqlconnect {

    Connection conn = null;

    public static Connection ConnectDb() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1/javaFX_words",
                    "root",
                    "Araiharuka0207@"
            );
            //?useSSL=false
//                JOptionPane.showMessageDialog(null, "JDBC load successed");
            return conn;
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Load failed");
//                JOptionPane.showMessageDialog(null, "JDBC Load failed");

            return null;
        }
    }

}
