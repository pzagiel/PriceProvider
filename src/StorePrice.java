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

/**
 * Created by IntelliJ IDEA.
 * User: pzagiel
 * Date: Sep 19, 2015
 * Time: 1:09:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class StorePrice {
    // Store in individual Text file
    public void store(String fileName, Price price) throws IOException {
        File priceFile = new File(fileName);
        BufferedWriter writer = new BufferedWriter(new FileWriter(priceFile));

        if (price != null) {
            writer.write(price.date);
            writer.write(",");
            writer.write(new String(Double.toString(price.priceValue)));
            writer.write(",");
            writer.write(new String(Double.toString(price.priceValueEvol)));
            writer.write("\n");
        }

        //Close writer
        writer.close();
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
                "(select id from provider where code='"+price.provider+"')," +
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
                        "value_d=" + date.getTime()+ " AND provider_id=(select id FROM PROVIDER where code='"+price.provider+"')";
                //System.out.println(sql);

                try {
                    stmt = conn.createStatement();
                    stmt.executeUpdate(sql);
                    //System.out.println("Update no Insert for Instrument " + price.instrumentCode);
                } catch (SQLException e1) {
                    //e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

            }
            if (e.getErrorCode() != 0)
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }


}
