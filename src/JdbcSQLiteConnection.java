import org.sqlite.SQLiteConfig;

import java.sql.*;

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
            String dbURL = "jdbc:sqlite:Prices/FinancialWorld.db";
            conn = DriverManager.getConnection(dbURL,config.toProperties());
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

    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");
            String dbURL = "jdbc:sqlite:Prices/FinancialWorld.db";
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