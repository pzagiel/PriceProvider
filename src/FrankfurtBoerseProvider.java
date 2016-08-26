import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.text.DecimalFormatSymbols;
import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * Created by IntelliJ IDEA.
 * User: pzagiel
 * Date: Oct 6, 2015
 * Time: 12:44:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class FrankfurtBoerseProvider {


    public static void main(String[] args) {
        FrankfurtBoerseProvider provider = new FrankfurtBoerseProvider();
        provider.getPrice("DE0005933931");
        //provider.getPrice("US91912E1055");  VAle is EUR take on NYSE
        provider.getPrice("DE000A1N49P6");
        provider.getPrice("FR0007054358");
        provider.getPrice("US71647NAF69");
        provider.getPrice("BE0003884047");
        provider.getPrice("LU0252633754");
        //provider.getPrice("IE00B0M62T89");
    }

    public Price getPrice(String isinCode) {
        Price priceValue = new Price();
        priceValue.provider = "frankfurtBoerse";
        priceValue.instrumentCode = isinCode;
        String codeTitre = "http://www.boerse-frankfurt.de/en/etc_etn/etc+commodities/etfs+brent+crude+" + isinCode +
                "/price+turnover+history";
        Document doc = null;
        String price = null;
        Elements priceElt = null;
        try {
            String ua = "Mozilla/5.0 (Macintosh)";
            doc = Jsoup.connect(codeTitre).userAgent(ua).get();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        Elements dateElt = doc.select(".column-date.first");
        if (dateElt.size() != 0)
            // Print date of Last Price
            priceValue.date = dateElt.text();
        priceElt = doc.select(".column-price.first");
        if (priceElt.size() != 0) {
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator('.');
            DecimalFormat format = new DecimalFormat("#.####");
            format.setDecimalFormatSymbols(symbols);
            try {
                priceValue.priceValue = format.parse(priceElt.get(1).text()).doubleValue();
            } catch (ParseException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        // Print Last Price
        //System.out.println(priceElt.get(1).text());
        // if (priceElt.size() == 0)
        //   priceElt = doc.select(".reactionbox--header-currentPrice.jsCurrentPrice");
        if (priceValue != null)
            System.out.println("Store Price From frankfurtBoerse " + priceValue.instrumentCode + " " + priceValue.date + " " + priceValue.priceValue);
        new StorePrice().storeInSqlite(priceValue);
        return priceValue;
    }
}
