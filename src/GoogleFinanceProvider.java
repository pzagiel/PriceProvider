import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.text.DecimalFormatSymbols;
import java.text.DecimalFormat;

/**
 * Created by IntelliJ IDEA.
 * User: pzagiel
 * Date: Nov 3, 2015
 * Time: 10:16:54 PM
 */
public class GoogleFinanceProvider {

    public static void main(String[] args) throws IOException {
        GoogleFinanceProvider provider = new GoogleFinanceProvider();
        // Get Price of VALE S.A on NYSE
        provider.getPrice("US91912E1055", "https://www.google.com/finance/historical?q=NYSE%3AVALE&ei=oCM5VonTG8PGUbrvrNAF");
        provider.getPrice("CA3518581051", "https://www.google.com/finance/historical?q=NYSE%3AFNV&ei=9JfmVuPNL9jjsgHNi6zABQ");
        provider.getPrice("US0378331005", "https://www.google.com/finance/historical?q=NASDAQ:AAPL");
        provider.getPrice("US5007541064", "https://www.google.com/finance/historical?q=NASDAQ:KHC");
        provider.getPrice("JE00B2QKY057", "http://www.google.com/finance/historical?q=LON%3ASHP&ei=WPUDV_ndJ4qUUtrqjYAB");
        provider.getPrice("US19122T1097", "http://www.google.com/finance/historical?q=NYSE%3ACCE&ei=H4Y0V8HnHNLBU_76nsAN");
        provider.getPrice("NL0000009538", "https://www.google.com/finance/historical?q=AMS:PHIA");  //Philips Amsterdam Price


    }

    public Price getPrice(String isin, String url) {
        String ua = "Mozilla/5.0 (Macintosh)";
        Document doc = null;
        try {
            doc = Jsoup.connect(url).userAgent(ua).get();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        // Get row 1 of Historical Price
        Element lastPriceRowElt = doc.select("div[id=prices]").select("tr").get(1);
        Price priceValue = new Price();
        priceValue.provider = "GoogleNyse";
        priceValue.instrumentCode = isin;
        // Get content of first column of table
        priceValue.date = convertDate(lastPriceRowElt.getElementsByIndexEquals(0).text());
        // Get content of fifth column of table
        priceValue.priceValue = convertPrice(lastPriceRowElt.getElementsByIndexEquals(4).text());
        if (priceValue != null)
            System.out.println("Store Price From GoogleNyse " + priceValue.instrumentCode + " " + priceValue.date + " " + priceValue.priceValue);
        new StorePrice().storeInSqlite(priceValue);
        return priceValue;
    }

    private String convertDate(String strDate) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
        try {
            date = sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return sdf1.format(date);
    }

    private double convertPrice(String price) {
        double priceValue = -1;
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(',');
        //DecimalFormat format = new DecimalFormat("#.####");
        DecimalFormat format = new DecimalFormat("#,##0.###");
        format.setDecimalFormatSymbols(symbols);
        try {
            priceValue = format.parse(price).doubleValue();
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return priceValue;
    }
}
