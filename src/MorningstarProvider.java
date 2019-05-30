import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.text.DecimalFormatSymbols;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: pzagiel
 * Date: Jan 1, 2015
 * Time: 2:30:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class MorningstarProvider {
    static int numberOfRetry = 0;

    public static void main(String[] args) throws IOException {
        // Date format is 2015-06-16
        MorningstarProvider morningstarProvider = new MorningstarProvider();
        if (args.length == 3) {
            //ISIN
            // MSID
            // Date format is 2015-06-16
            //FR0010923375 F00000JQYQ 2015-06-16
            morningstarProvider.getHistoricalPrices(args[0], args[1], args[2]);
        }
        //System.out.println("Invalid arguments");
        else if (args.length == 0) {
            morningstarProvider.getDailyNav();

        }

        //morningstarProvider.getHistoricalPrices("FR0010923375", "F00000JQYQ");
        //morningstarProvider.getHistoricalPrices("FR0011008762", "F00000MIEA");


    }

    public void getDailyNav() {

        MorningstarProvider morningstarProvider = new MorningstarProvider();
        FundsList myfunds = new FundsList();
        myfunds.initWithFile("Excel/FinancialInstruments.csv");
        Instrument fund;
        Price price;
        for (int i = 0; i < myfunds.getFunds().size(); i++) {
            price = null;
            fund = (Instrument) myfunds.getFunds().get(i);
            if (fund.Isin.equals("FR0007054358")) {
                // ETF autre structure page Web
                // cours de cloture ˆ la place de VL
                System.out.println("Stop");
            }

            int numberOfRetry = 0;
            while ((numberOfRetry < 10) && (price == null)) {
                price = morningstarProvider.getPriceMorningstar("www." + fund.provider, fund.dataSourceId);
                if (numberOfRetry > 0)
                    System.out.println("Retry to get price for " + fund.name);
                numberOfRetry++;
            }
            numberOfRetry = 0;

            if (price != null) {
                price.instrumentCode = fund.Isin;
                System.out.println(fund.name + " " + price.date + " " + " Price:" + price.priceValue + " Evol:" + price.priceValueEvol * 100 + "%");
                try {
                    //new StorePrice().storeInXLS("Prices/" + fund.Isin + ".xls", price);
                    new StorePrice().storeInSqlite(price);
                } catch (Exception e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }


        }
        // It eems they have change class name for Umicore and ING to check
        /*System.out.println("Prix Umicore=" + morningstarProvider.getPriceEcho("Umicore.60115409"));
        Price priceMorningstar = morningstarProvider.getPriceMorningstar("F00000JQYQ");
        System.out.println("H20 Multibonds Date:" + priceMorningstar.date + " VL:" + priceMorningstar.priceValue);
        System.out.println("Prix EURO STOXX 50=" + morningstarProvider.getPriceEcho("190031976"));

        System.out.println("Prix Philips=" + morningstarProvider.getPriceEcho("11783"));
        System.out.println("Prix ING=" + morningstarProvider.getPriceEcho("11773"));    */

    }

    public String getPriceEcho(String codeTitre) {
        codeTitre = "http://www.lecho.be/bourses/" + codeTitre;
        Document doc = null;
        String price = null;
        Elements priceElt = null;
        try {
            String ua = "Mozilla/5.0 (Macintosh)";
            doc = Jsoup.connect(codeTitre).userAgent(ua).get();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        priceElt = doc.select(".koersenfiche-main-price.js-stock-detail-main-price");
        if (priceElt.size() == 0)
            priceElt = doc.select(".reactionbox--header-currentPrice.jsCurrentPrice");

        return priceElt.text();
    }

    public Price getPriceMorningstar(String hostname, String codeTitre) {
        /*if (codeTitre.equals("0P00002D7X")) {
            System.out.println("To Check");
        } */
        codeTitre = "http://" + hostname + "/snapshot/snapshot.aspx?id=" + codeTitre;
        Document doc = null;
        String price = null;
        Elements priceElt = null;
        Elements priceEltValue = null;
        try {
            doc = Jsoup.connect(codeTitre).timeout(20 * 1000).get();
        } catch (IOException e) {
            //e.printStackTrace();
            //System.out.println("Cannot find url: " + codeTitre);
            // Retry 5 times
            /*numberOfRetry++;
            if(numberOfRetry<11)
             this.getPriceMorningstar(hostname,codeTitre);  */

        }
        numberOfRetry = 0;
        if (doc == null) {
            System.out.println("No Price for Instrument" + codeTitre);
            return null;
        }

        priceElt = doc.select(".line.heading"); // Get Date of VL
        if (priceElt.size() == 0)
            // There is no price in the page
            return null;
        // Replace Cours de cl™ture par VL si nŽcessaire
        String priceEltnew = priceElt.first().text().replace("Cours de cl™ture", "VL");
        //priceEltnew = priceEltnew.replace("VL hors frais", "VL");
        String[] array = priceEltnew.split(" +");

        if (array.length != 2)
            return null;
        priceEltValue = doc.select(".line.text");
        String[] array1 = priceEltValue.first().text().split(String.valueOf((char) 160));
        // Get Day Evolution
        priceEltValue.get(1).text().split("%");
        //Object[] all=priceElt.toArray();
        //Element test;
        //test=(Element)all[3];
        Price priceValue = new Price();
        priceValue.provider = "MS";
        // Change date format from 12-05-2015 to 12/05/2015 if the case on NL for instance
        String priceDate = array[1].replace('-', '/');
        // Change . to - for CH
        priceValue.date = priceDate.replace('.', '/');
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        DecimalFormat format = new DecimalFormat("#.####");
        format.setDecimalFormatSymbols(symbols);
        try {
            // Repalce . with , if necessary for uk
            String pricevalue = array1[1].replace('.', ',');
            priceValue.priceValue = format.parse(pricevalue).doubleValue();
            // Get Day Evolution
            if (priceEltValue.get(1) != null) {
                String[] evol = priceEltValue.get(1).text().split("%");
                String evolStr = evol[0];
                // replace . by , for uk values
                String evolStr1 = evolStr.replace('.', ',');
                priceValue.priceValueEvol = format.parse(evolStr1).doubleValue();
                priceValue.priceValueEvol = priceValue.priceValueEvol / 100;
            }

        } catch (ParseException e) {
            e.printStackTrace();

        }
        return priceValue;
    }


    public Collection getHistoricalPrices(String isin, String MSId, String fromDate) throws IOException {
        // Url H20
        // http://tools.morningstarpro.fr/api/rest.svc/timeseries_price/i6wq0tnd7a?currencyId=EUR&idtype=Morningstar&frequency=daily&startDate=2007-06-01&priceType=&outputType=COMPACTJSON&id=F00000JQYQ]2]0]FOEUR$$ALL

        //String url = "http://tools.morningstarpro.fr/api/rest.svc/timeseries_price/i6wq0tnd7a?currencyId=EUR&idtype=Morningstar&frequency=daily&startDate=2010-06-16&priceType=&outputType=COMPACTJSON&id=F00000JQYQ]2]0]FOEUR$$ALL";
        String url = "http://tools.morningstarpro.fr/api/rest.svc/timeseries_price/i6wq0tnd7a?currencyId=EUR&idtype=Morningstar&frequency=daily&startDate=" + fromDate + "&priceType=&outputType=COMPACTJSON&id=" + MSId + "]2]0]FOEUR$$ALL";

        //String url = "http://tools.morningstarpro.fr/api/rest.svc/timeseries_price/i6wq0tnd7a?currencyId=EUR&idtype=Morningstar&frequency=daily&startDate=2016-02-09&priceType=&outputType=COMPACTJSON&id=" + MSId + "]2]0]UNIVE$$ALL";
        Document doc = null;
        Elements priceElt = null;
        try {
            String ua = "Mozilla/5.0 (Macintosh)";
            doc = Jsoup.connect(url).ignoreContentType(true).userAgent(ua).get();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(doc.body().text());
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        // Loop on Price
        ArrayList prices = (ArrayList) obj;
        ArrayList priceValues = new ArrayList();
        for (int i = 0; i < prices.size(); i++) {
            ArrayList priceData = (ArrayList) prices.get(i);
            Price priceValue = new Price();
            priceValue.instrumentCode = isin;
            Long seconds = (Long) priceData.get(0);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date priceDate = new Date(seconds);
            priceValue.date = sdf.format(priceDate);
            priceValue.priceValue = (Double) priceData.get(1);
            priceValue.provider = "MS";
            if (priceValue != null) {
                System.out.println("Store Price From Morningstar " + priceValue.instrumentCode + " " + priceValue.date + " " + priceValue.priceValue);
                priceValues.add(priceValue);
            }
            //new StorePrice().storeInSqlite(priceValue);
        }
        StorePrice storePrice = new StorePrice();
        Price.computeEvol(priceValues);
        storePrice.storeInSqlite(priceValues);
        storePrice.storeInXLS(isin + ".xls", priceValues);
        return priceValues;
    }

}




