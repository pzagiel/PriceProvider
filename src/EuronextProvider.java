import org.jsoup.select.Elements;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONObject;

import java.util.Calendar;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.net.URL;
import java.text.DecimalFormatSymbols;
import java.text.DecimalFormat;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * Created by IntelliJ IDEA.
 * User: pzagiel
 * Date: Nov 30, 2015
 * Time: 5:57:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class EuronextProvider {


    public static void main(String[] args) {
        EuronextProvider euronextProvider = new EuronextProvider();
        //euronextProvider.getLastPriceForOption("", "", "", "");

        euronextProvider.getLastPrice("BE0003884047", "XBRU"); // Umicore
        euronextProvider.getLastPrice("FR0007054358", "XPAR"); // Lyxor EURO STOXX
        euronextProvider.getLastPrice("LU0252633754", "XPAR"); // Lyxor DAX
        euronextProvider.getLastPrice("FR0000051732", "XPAR"); // ATOS
        euronextProvider.getLastPrice("FR0000125338", "XPAR"); // Cap Gemini
        euronextProvider.getLastPrice("FR0000121667", "XPAR"); // Essilor
        euronextProvider.getLastPrice("NL0011821202 ", "XAMS"); // ING Groep New ISIN
        euronextProvider.getLastPrice("NL0000009538 ", "XAMS");  // Philips Amsterdam

        //euronextProvider.getLastPrice("NL0000303600 ", "XAMS"); // ING Groep ISIN A changé je crois

        //euronextProvider.getLastPrice("BE0003562700","XBRU"); // Delhaize
        //euronextProvider.getLastPrice("BE0003822393","XBRU"); // Elia

        //System.out.println(euronextProvider.getCurrentDate().getTimeInMillis());
        //System.out.println(euronextProvider.subtractDay(euronextProvider.getCurrentDate(), 10).getTimeInMillis());
    }


    public void getLastPrice(String isinCode, String market) {
        //Security.addProvider(new BouncyCastleProvider());
        String baseUrl = "https://www.euronext.com/sites/www.euronext.com/modules/common/common_listings/custom/nyx_eu_listings/nyx_eu_listings_price_chart/pricechart/pricechart.php?q=historical_data&adjusted=1&from=";
        baseUrl = baseUrl + subtractDay(getCurrentDate(), 30).getTimeInMillis() + "&to=" + getCurrentDate().getTimeInMillis() + "&isin=" + isinCode + "&mic=" + market + "&dateFormat=d/m/Y";
        ;
        //System.out.println(baseUrl);
        Document doc = null;
        Elements priceElt = null;
        try {
            String ua = "Mozilla/5.0 (Macintosh)";
            //doc = Jsoup.connect(baseUrl).userAgent(ua).get();
            doc = Jsoup.connect(baseUrl).validateTLSCertificates(false).userAgent(ua).get();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(doc.body().text());
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        JSONObject jsonObject = (JSONObject) obj;
        ArrayList prices = (ArrayList) jsonObject.get("data");
        // Get Last Price corresponding to till date
        JSONObject jsonPrice = (JSONObject) prices.get(prices.size() - 1);
        Price priceValue = buildPriceFromJson(jsonPrice);
        if (priceValue != null)
            System.out.println("Store Price From Euronext " + priceValue.instrumentCode + " " + priceValue.date + " " + priceValue.priceValue);
        new StorePrice().storeInSqlite(priceValue);

    }


    public void getLastPriceForOption(String isinCode, String callPut, String maturity, String strike) {
        Document doc = null;
        Elements priceTableElt = null;
        String url = "http://www.eurexchange.com/exchange-en/products/idx/stx/blc/19068!quotesSingleViewOption?callPut=Put&maturityDate=201603";
        try {
            String ua = "Mozilla/5.0 (Macintosh)";
            doc = Jsoup.connect(url).validateTLSCertificates(false).userAgent(ua).get();
            Elements priceElt = null;
            priceTableElt = priceElt = doc.select("td[id=notation128402585]");
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        System.out.println("Debug");
    }


    private Price buildPriceFromJson(JSONObject jsonPrice) {
        Price priceValue = new Price();
        priceValue.instrumentCode = (String) jsonPrice.get("ISIN");
        priceValue.date = (String) jsonPrice.get("date");
        priceValue.provider = "Euronext";

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        DecimalFormat format = new DecimalFormat("#.####");
        format.setDecimalFormatSymbols(symbols);

        try {
            priceValue.priceValue = format.parse((String) jsonPrice.get("close")).doubleValue();
        } catch (java.text.ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return priceValue;
    }

    public Calendar getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        //System.out.println(cal.getTime());
        return cal;
    }

    public Calendar subtractDay(Calendar cal, int days) {
        cal.add(Calendar.DAY_OF_MONTH, -days);
        //System.out.println(cal.getTime());
        return cal;

    }


    public void getPage(String dataUrl) throws IOException {
        URL url = new URL(dataUrl);
        BufferedReader reader = new BufferedReader
                (new InputStreamReader(url.openStream()));
        BufferedWriter writer = new BufferedWriter
                (new FileWriter("umicore.txt"));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            writer.write(line);
            writer.newLine();
        }
        reader.close();
        writer.close();
    }
}
