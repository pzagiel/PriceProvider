import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: pzagiel
 * Date: Sep 19, 2015
 * Time: 1:09:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class StorePrice {

    public void storeDebug(String name, String message) {
        File priceFile = new File(name + ".txt");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(priceFile));
            writer.write(message);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    // Store in individual Text file
    public void storeInXLS(String fileName, ArrayList<Price> prices) throws IOException {
        File priceFile = new File(fileName);
        BufferedWriter writer = new BufferedWriter(new FileWriter(priceFile));
        for (Price price : prices) {
            if (price != null) {
                writer.write(price.date);
                writer.write("\t");
                writer.write(new String(Double.toString(price.priceValue)));
                writer.write("\t");
                writer.write(new String(Double.toString(price.priceValueEvol)));
                writer.write("\n");
            }
        }
        writer.close();
    }

    // To test carrefully
    public void storeInSqlite(ArrayList<Price> prices) {
        for (Price price : prices) {
            storeInSqlite(price);
        }
    }

    // Store in sqliteDB
    public void storeInSqlite(Price price) {
        // Getting Sqlite JDBC connection
        Connection conn = JdbcSQLiteConnection.getConnection();

        // Execute sql insert-update statement
        Statement stmt = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = sdf.parse(price.date);
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        // Insert Statement
        String sql = "INSERT INTO INSTR_PRICE (provider_id,instr_id,currency,value_d,value,evol) " +
                "VALUES (" +
                "(select id from provider where code='" + price.provider + "')," +
                "(select id from INSTRUMENT where code='" + price.instrumentCode + "')," +
                "'EUR'," +
                date.getTime() + "," +
                price.priceValue + "," +
                price.priceValueEvol + ")";


        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            // Create an update statement if duplicate key for insert price
            if (e.getErrorCode() == 0) {
                sql = "UPDATE INSTR_PRICE " +
                        "SET value=" + price.priceValue + " , evol      =" + price.priceValueEvol +
                        " WHERE instr_id=(select id from INSTRUMENT where code='" + price.instrumentCode + "') AND " +
                        "value_d=" + date.getTime() + " AND provider_id=(select id FROM PROVIDER where code='" + price.provider + "')";
                //System.out.println(sql);

                try {
                    stmt = conn.createStatement();
                    stmt.executeUpdate(sql);
                    //System.out.println("Update no Insert for Instrument " + price.instrumentCode);
                } catch (SQLException e1) {
                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

            }
            if (e.getErrorCode() != 0)
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }


}
