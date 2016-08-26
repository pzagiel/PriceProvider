import org.sqlite.SQLiteConfig;

import java.sql.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * This program demonstrates making JDBC connection to a SQLite database.
 *
 * @author www.codejava.net
 */
public class JdbcSQLiteConnection {
    static Connection conn = null;

    public static Connection getConnection() {
        Connection conn = null;

        if (conn == null) try {
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            Class.forName("org.sqlite.JDBC");
            String dbURL = "jdbc:sqlite:"+getDatabaseFileNameFromConfig();
            // Error checking is not good if didn't find database. Should be handle rapidely
            conn = DriverManager.getConnection(dbURL, config.toProperties());
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return conn;
    }

    public void finalize() {
        try {
            super.finalize();
            if (conn != null)
                conn.close();
        } catch (Throwable throwable) {
            throwable.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public static String getDatabaseFileNameFromConfig() {
        Properties prop = new Properties();
        InputStream input = null;

        try {

            input = new FileInputStream("classes/config.properties");

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            // System.out.println(prop.getProperty("database"));
            //System.out.println(prop.getProperty("dbuser"));
            //System.out.println(prop.getProperty("dbpassword"));

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        return prop.getProperty("database");
    }

    public static void main(String[] args) {

        getDatabaseFileNameFromConfig();
        try {
            Class.forName("org.sqlite.JDBC");
            String dbURL = "jdbc:sqlite:" + getDatabaseFileNameFromConfig();
            Connection conn = DriverManager.getConnection(dbURL);
            if (conn != null) {
                System.out.println("Connected to the database");
                /*              DatabaseMetaData dm = (DatabaseMetaData) conn.getMetaData();
            System.out.println("Driver name: " + dm.getDriverName());
            System.out.println("Driver version: " + dm.getDriverVersion());
            System.out.println("Product name: " + dm.getDatabaseProductName());
            System.out.println("Product version: " + dm.getDatabaseProductVersion());*/

                Statement stmt = null;
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM INSTRUMENT;");
                while (rs.next()) {
                    System.out.println(rs.getString("name") + "|" + rs.getString("code"));
                }
                rs.close();
                stmt.close();
                conn.close();
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}