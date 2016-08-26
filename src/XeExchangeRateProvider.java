import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: pzagiel
 * Date: Oct 12, 2015
 * Time: 4:37:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class XeExchangeRateProvider {

    public static void main(String[] args) throws IOException {
        XeExchangeRateProvider provider = new XeExchangeRateProvider();
        provider.getEurExchangeRate("2014-12-31");
        provider.getEurExchangeRate("2015-03-01");
        provider.getEurExchangeRate("2015-04-01");
        provider.getEurExchangeRate("2015-01-01");
        provider.getEurExchangeRate("2015-10-09");
        provider.getEurExchangeRate("2015-10-12");

    }

    public void getEurExchangeRate(String dateOfRate) throws IOException {
        /* Necessary to specify a user agent to avoid page
        with message 'Automated extraction of our content is prohibited' */
        String ua = "Mozilla/5.0 (Macintosh)";
        Document doc = Jsoup.connect("http://www.xe.com/currencytables/?from=EUR&date=" + dateOfRate).userAgent(ua).get();
        // Get row 2 of exchange rate table
        Element rateElt = doc.select("table[id=historicalRateTbl]").select("tr").get(1);
        // Get Column 3
        System.out.println("EUR/USD Exchange Rate at " + dateOfRate + "=" + rateElt.getElementsByIndexEquals(2).text());

    }
}
