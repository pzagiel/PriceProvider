import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.Jsoup;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.DecimalFormatSymbols;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 * Created by IntelliJ IDEA.
 * User: pzagiel
 * Date: Mar 2, 2016
 * Time: 7:52:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class BloombergProvider {

    public static void main(String[] args) {
        BloombergProvider bloombergProvider = new BloombergProvider();
        bloombergProvider.getLastPrice("US0378331005", "AAPL:US");  //Apple Inc
        bloombergProvider.getLastPrice("GB00B0CTWC01", "OLBP:LN");  // ETFS WTI Crude Oil USD
        bloombergProvider.getLastPrice("GB00B15KXV33", "CRUD:LN");  // Brent ETFS Tracker in GBX
        bloombergProvider.getLastPrice("NL0011821202", "INGA:NA");  // ING
        bloombergProvider.getLastPrice("FR0010923375", "NH2MBRC:FP");  // H20 Multibonds
        bloombergProvider.getLastPrice("LU0252633754", "DAX:FP");  // Lyxor ETF DAX
        bloombergProvider.getLastPrice("FR0007054358", "MSE:FP");  //LYXOR UC ESTX50-D-EUR-ETF/DIS
        bloombergProvider.getLastPrice("IE00B0M62T89", "IQQV:GR");  //iShares EURO Total Market Value Large UCITS ETF 
        bloombergProvider.getLastPrice("CA3518581051", "FNV:US");  //FRANCO-NEVADA CORP (USD)
        bloombergProvider.getLastPrice("JE00B2QKY057", "SHP:LN");  //SHirpe PLC

        bloombergProvider.getLastPrice("FR0000051732", "ATO:FP");  //ATOS
        bloombergProvider.getLastPrice("FR0011253624", "SOGVALR:FP");  //R Valor
        bloombergProvider.getLastPrice("US5007541064", "KHC:US");  //Kraft
        bloombergProvider.getLastPrice("US19122T1097", "CCE:US");  //Coca Cola Entreprise
        bloombergProvider.getLastPrice("FR0000125338", "CAP:FP");  //Cap Gemini
        bloombergProvider.getLastPrice("FR0000121667", "EI:FP");  //Essilor
        bloombergProvider.getLastPrice("GB00B00FHZ82", "GBS:LN");  //Gold Bullion
        bloombergProvider.getLastPrice("NL0000009538", "PHIA:NA");  //Philips Amsterdam
        bloombergProvider.getLastPrice("CL1COM", "CL1:COM");  //WTI Crude
    }


    /*
    Send query to get realTime prices from Bloomberg
    for all financial instruments passed in parameter instruments
    */
    private void queryBloombergForInstruments(List instruments) {

    }

    /*
    Method that get all instrument for which wee need to query Bloomberg
    */
    private List getInstruments() {
        return null;
    }

    public void getPrices(String isin, String ticker) {
        // Possible to use 1_WEEK, 1_MONTH, 1_YEAR,5_YEAR,MAX default is 1_WEEK
        String baseUrl = "http://www.bloomberg.com/markets/api/bulk-time-series/price/" + ticker + "?timeFrame=1_YEAR";
        Document doc = null;
        Elements priceElt = null;
        try {
            String ua = "Mozilla/5.0 (Macintosh)";
            doc = Jsoup.connect(baseUrl).ignoreContentType(true).userAgent(ua).get();
            // System.out.println(doc.text());
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        Price priceValue = new Price();
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(doc.body().text());
            ArrayList prices = (ArrayList) obj;
            JSONObject jsonPrice = (JSONObject) prices.get(0);

            ArrayList prices1 = (ArrayList) jsonPrice.get("price");
            // Loop on prices

            for (int i = 0; i < prices1.size(); i++) {
                priceValue.instrumentCode = isin;
                priceValue.provider = "Bloomberg";
                JSONObject jsonPrice1 = (JSONObject) prices1.get(i);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                try {
                    date = sdf.parse((String) jsonPrice1.get("date"));
                } catch (ParseException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
                // Very good page on type class testing

                priceValue.date = sdf1.format(date);
                //priceValue.date = (String) jsonPrice1.get("date)");
                try {
                    Double priceObj = (Double) jsonPrice1.get("value");
                    priceValue.priceValue = priceObj.doubleValue();
                }

                catch (ClassCastException e) {
                    // Could be the cast if number without decimal than JSON create e Long object in place of Double
                    Long priceObj = (Long) jsonPrice1.get("value");
                    priceValue.priceValue = priceObj.doubleValue();
                }
                if (priceValue != null)
                    System.out.println("Store Price From Bloomberg " + priceValue.instrumentCode + " " + priceValue.date + " " + priceValue.priceValue);
                new StorePrice().storeInSqlite(priceValue);

            }

        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }


    }


    public void getLastPrice(String isin, String ticker) {
        // Possible to use 1_WEEK, 1_MONTH, 1_YEAR,5_YEAR,MAX default is 1_WEEK
        String baseUrl = "http://www.bloomberg.com/markets/api/bulk-time-series/price/" + ticker + "?timeFrame=1_MONTH";
        Document doc = null;
        Elements priceElt = null;
        try {
            String ua = "Mozilla/5.0 (Macintosh)";
            doc = Jsoup.connect(baseUrl).validateTLSCertificates(false).ignoreContentType(true).userAgent(ua).get();
            // System.out.println(doc.text());
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        Price priceValue = new Price();
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(doc.body().text());
            ArrayList prices = (ArrayList) obj;
            JSONObject jsonPrice = (JSONObject) prices.get(0);
            priceValue.instrumentCode = isin;
            priceValue.provider = "Bloomberg";

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = sdf.parse((String) jsonPrice.get("lastUpdateDate"));
            } catch (ParseException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
            priceValue.date = sdf1.format(date);
            Number priceObj = (Number) jsonPrice.get("lastPrice");
            priceValue.priceValue = priceObj.doubleValue();

        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }

        if (priceValue != null)
            System.out.println("Store Price From Bloomberg " + priceValue.instrumentCode + " " + priceValue.date + " " + priceValue.priceValue);
        new StorePrice().storeInSqlite(priceValue);


    }
}
