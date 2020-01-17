import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.Jsoup;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: pzagiel
 * Date: Nov 14, 2018
 * Time: 1:02:36 PM
 * To change this template use File | Settings | File Templates.
 */

// New API
//https://query2.finance.yahoo.com/v8/finance/chart/UMI.BR?formatted=true&crumb=oC6qFyVRUTg&lang=en-US&region=US&interval=1d&period1=1529419126&period2=1560955126&events=div%7Csplit&corsDomain=finance.yahoo.com
public class YahooFinanceProvider {

// Yahoo Quote Query

//https://query1.finance.yahoo.com/v8/finance/chart/EL.PA?symbol=EL.PA&period1=1487286000&period2=1542196209&interval=1d&includePrePost=true&events=div|split|earn&lang=fr-FR&region=FR&crumb=oC6qFyVRUTg&corsDomain=fr.finance.yahoo.com

    public static void main(String[] args) {

        YahooFinanceProvider yahooFinanceProvider = new YahooFinanceProvider();
        yahooFinanceProvider.getLastPrice("","EL.PA");
    }

    public void getLastPrice(String isin, String symbol) {
        String baseUrl = "https://query1.finance.yahoo.com/v8/finance/chart/"+symbol+"?symbol="+symbol+"&period1=1487286000&period2=1542196209&interval=1d&includePrePost=true&events=div|split|earn&lang=fr-FR&region=FR&crumb=oC6qFyVRUTg&corsDomain=fr.finance.yahoo.com";
        Document doc = null;
        Elements priceElt = null;
        try {
            //String ua = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.12; rv:57.0) Gecko/20100101 Firefox/57.0";
            String ua = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.12; rv:62.0) Gecko/20100101 Firefox/62.0";
            doc = Jsoup.connect(baseUrl).validateTLSCertificates(true).ignoreContentType(true).userAgent(ua).timeout(50 * 1000).get();
            // System.out.println(doc.text());
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        JSONParser parser = new JSONParser();
        Object obj = null;;
        try {
            obj = parser.parse(doc.body().text());
        } catch (ParseException e) {
            e.printStackTrace();
        }


        System.out.println("OK");
    }


}
