import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: pzagiel
 * Date: Mar 2, 2016
 * Time: 7:52:16 PM
 * To change this template use File | Settings | File Templates.
 */

// New API to test
// https://www.bloomberg.com/markets2/api/history/IFX:GR/PX_LAST?timeframe=1_MONTH&period=daily&volumePeriod=daily
public class BloombergProvider {
    HashMap<String, String> isinLookup = new HashMap<String, String>() {
        {


            put("MSE:FP", "FR0007054358");  // Lyxor Eurostox
            put("WLN:FP", "FR0011981968");  // Atos Worldline
            put("ALGN:US", "US0162551016");  // Align Technology
            put("UMI:BB", "BE0974320526");  // Umicore
            put("USDTRY:CUR", "USDTRY");  // USD TRY
            put("INGA:NA", "NL0011821202");  // ING
            put("XAUUSD:CUR", "XAUUSD");  // Gold Spot
            put("PHIA:NA", "NL0000009538");  //Philips Amsterdam
            put("CAP:FP", "FR0000125338");  //Cap Gemini
            put("EL:FP", "FR0000121667");  //Essilor

            put("GB00B00FHZ82", "GBS:LN");  //Gold Bullion
            put("ATO:FP", "FR0000051732");  //ATOS
            put("NAH2VIR:FP", "FR0011015478");  // H20 VIVACE EUR FR0011015478
            put("NHRCUSD:FP", "FR0012497980");  // H20 vivace HUSD
            put("NH2MBRC:FP", "FR0010923375");  // H20 Multibonds FR0010923375
            put("H2OMERC:FP", "FR0011008762");  // H20  Multiequities FR0011008762
            put("NAH2ALR:FP", "FR0011015460");  // H20 Allegro EUR FR0011015460
            



            put("IFX:GR", "DE0006231004");  // Infineon
            put("GBTPGR10:IND", "GBTPGR10:IND");  // Italy 10Y Govt Bonds
            put("MU:US", "US5951121038");  // Micron Technology
            put("POAGX:US", "US74160Q2021");  // Amazon
            put("AMZN:US", "US0231351067");  // Amazon
            put("SPX:IND", "SPX");  // S&P 500 Index
            put("JD:US", "US47215P1066");  // JD.COM
            put("STM:FP", "NL0000226223");  // STMicroelectronics NV
            
            put("SODA:US", "IL0011213001");  //Sodastream
            put("BABA:US", "US01609W1027");  //Alibaba
            put("9988:HK", "9988.HK");  //Alibaba
            put("ZTS:US", "US98978V1035");  //Zoetis Inc
            put("CL1:COM", "CL1COM");  //WTI Crude
            put("SX5E:IND", "SX5E");  //EURO STOXX 50
            //put("NL0011821202", "INGA:NA");  // ING
            put("RORIHUS:LX", "LU0955120620");
            put("IE00B1FZSC47", "IDTP:LN");  //iShares $ TIPS UCITS ETF (USD) | ITPS

            put("AAPL:US", "US0378331005");  //Apple Inc
            put("OLBP:LN", "GB00B0CTWC01");  // ETFS WTI Crude Oil USD
            put("CRUD:LN", "GB00B15KXV33");  // Brent ETFS Tracker in GBX

            // put("FR0010923375", "NH2MBRC:FP");  // H20 Multibonds
            put("DAX:FP", "LU0252633754");  // Lyxor ETF DAX
            put("MSE:FP", "FR0007054358");  //LYXOR UC ESTX50-D-EUR-ETF/DIS
            put("IQQV:GR", "IE00B0M62T89");  //iShares EURO Total Market Value Large UCITS ETF
            put("FNV:US", "CA3518581051");  //FRANCO-NEVADA CORP (USD)
            put("SHP:LN", "JE00B2QKY057");  //SHirpe PLC

            //put("FR0000051732", "ATO:FP");  //ATOS
            put("SOGVALR:FP", "FR0011253624");  //R Valor
            put("KHC:US", "US5007541064");  //Kraft
            put("CCE:US", "US19122T1097");  //Coca Cola Entreprise
            put("GBS:LN", "GB00B00FHZ82");  //Gold Bullion

            put("CL1COM", "CL1:COM");  //WTI Crude
            put("GB00B15KXV33", "CRUD:LN");  //Brent
        }
    };

    public static void main(String[] args) throws Exception {
        System.setProperty("https.protocols", "TLSv1.1,TLSv1.2");
        /* Proxy proxy = new Proxy(                                      //
              Proxy.Type.HTTP,                                      //
              InetSocketAddress.createUnresolved("91.183.190.8", 8080) //
      );  */

        if (args.length == 0) {
            BloombergProvider bloombergProvider = new BloombergProvider();
            // bloombergProvider.getPrices("DE0006231004", "IFX:GR");
            //bloombergProvider.getLastPrice("FR0010923383", "NH2PATI:FP");  // H20 Multistartegy EUR Instit
            //bloombergProvider.getPrices("USDTRY", "USDTRY:CUR");
            //bloombergProvider.getPrices("BE0974320526", "UMI:BB");  // Umicore

            bloombergProvider.getLastPrice("US0162551016", "ALGN:US");  // Align Technology
            bloombergProvider.getLastPrice("BE0974320526", "UMI:BB");  // Umicore
            bloombergProvider.getLastPrice("USDTRY", "USDTRY:CUR");  // USD TRY
            bloombergProvider.getLastPrice("NL0011821202", "INGA:NA");  // ING
            bloombergProvider.getLastPrice("XAUUSD", "XAUUSD:CUR");  // Gold Spot
            bloombergProvider.getLastPrice("NL0000009538", "PHIA:NA");  //Philips Amsterdam
            bloombergProvider.getLastPrice("FR0000125338", "CAP:FP");  //Cap Gemini
            bloombergProvider.getLastPrice("FR0000121667", "EL:FP");  //Essilor

            bloombergProvider.getLastPrice("GB00B00FHZ82", "GBS:LN");  //Gold Bullion
            bloombergProvider.getLastPrice("FR0000051732", "ATO:FP");  //ATOS
            bloombergProvider.getLastPrice("FR0011015478", "NAH2VIR:FP");  // H20 VIVACE EUR
            bloombergProvider.getLastPrice("DE0006231004", "IFX:GR");  // Infineon
            bloombergProvider.getLastPrice("GBTPGR10:IND", "GBTPGR10:IND");  // Italy 10Y Govt Bonds
            bloombergProvider.getLastPrice("US5951121038", "MU:US");  // Micron Technology
            bloombergProvider.getLastPrice("US74160Q2021", "POAGX:US");  // Amazon
            bloombergProvider.getLastPrice("US0231351067", "AMZN:US");  // Amazon
            bloombergProvider.getLastPrice("SPX", "SPX:IND");  // S&P 500 Index
            bloombergProvider.getLastPrice("US47215P1066", "JD:US");  // JD.COM
            bloombergProvider.getLastPrice("NL0000226223", "STM:FP");  // STMicroelectronics NV
            bloombergProvider.getLastPrice("FR0010923375", "NH2MBRC:FP");  // H20 Multibonds
            bloombergProvider.getLastPrice("IL0011213001", "SODA:US");  //Sodastream
            bloombergProvider.getLastPrice("US01609W1027", "BABA:US");  //Alibaba
            bloombergProvider.getLastPrice("US98978V1035", "ZTS:US");  //Zoetis Inc
            bloombergProvider.getLastPrice("CL1COM", "CL1:COM");  //WTI Crude
            bloombergProvider.getLastPrice("SX5E", "SX5E:IND");  //EURO STOXX 50
            bloombergProvider.getLastPrice("NL0011821202", "INGA:NA");  // ING
            bloombergProvider.getLastPrice("LU0955120620", "RORIHUS:LX");
            bloombergProvider.getLastPrice("IE00B1FZSC47", "IDTP:LN");  //iShares $ TIPS UCITS ETF (USD) | ITPS
            // bloombergProvider.getLastPrice("LU0234577681", "PIWEQUT:LX");  //Piguet International Fund World Equities D (USD)
            //bloombergProvider.getLastPrice("LU0334117230", "PIWEDED:LX");  //Piguet International Fund World Equities D (EUR)
            bloombergProvider.getLastPrice("US0378331005", "AAPL:US");  //Apple Inc
            bloombergProvider.getLastPrice("GB00B0CTWC01", "OLBP:LN");  // ETFS WTI Crude Oil USD
            bloombergProvider.getLastPrice("GB00B15KXV33", "CRUD:LN");  // Brent ETFS Tracker in GBX

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
            bloombergProvider.getLastPrice("GB00B00FHZ82", "GBS:LN");  //Gold Bullion

            bloombergProvider.getLastPrice("CL1COM", "CL1:COM");  //WTI Crude
        }
        // Store data from JSON file
        else {
            String filename;
            filename = args[0];
            BloombergProvider bloombergProvider = new BloombergProvider();
            String jsonData = bloombergProvider.loadJsonData(filename);
            bloombergProvider.storePricesFromData(jsonData);
        }

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

    public String loadJsonData(String filename) throws Exception {
        Scanner in = new Scanner(new FileReader(filename));
        StringBuilder sb = new StringBuilder();
        while (in.hasNext()) {
            sb.append(in.next());
        }
        in.close();
        return sb.toString();
    }

    private String getIsin(String ticker) {
        String isin = isinLookup.get(ticker);
        return isin;
    }

    private String getTicker(JSONObject jsonPrice) {
        String ticker = (String) jsonPrice.get("id");
        return ticker;
    }

    public void storePricesFromData(String jsonData) {
        // Other URL to try
        // https://www.bloomberg.com/markets/chart/data/1M/AAPL:US
        // Possible to use 1_WEEK, 1_MONTH, 1_YEAR,5_YEAR,MAX default is 1_WEEK

        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(jsonData);
            ArrayList prices = (ArrayList) obj;
            JSONObject jsonPrice = (JSONObject) prices.get(0);

            ArrayList prices1 = (ArrayList) jsonPrice.get("price");
            // Loop on prices
            ArrayList priceValues = new ArrayList();
            for (int i = 0; i < prices1.size(); i++) {
                Price priceValue = new Price();

                priceValue.provider = "Bloomberg";
                JSONObject jsonPrice1 = (JSONObject) prices1.get(i);
                priceValue.instrumentCode = getIsin(getTicker(jsonPrice));
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
                priceValue.priceValueEvol = comuteEvolution((JSONObject) prices.get(0));
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
                priceValues.add(priceValue);
            }

            Price.computeEvol(priceValues);
            new StorePrice().storeInSqlite(priceValues);

        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }

    }

    public void storeLastPriceFromData(String jsonData) {

        Price priceValue = new Price();
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(jsonData);
            ArrayList prices = (ArrayList) obj;
            JSONObject jsonPrice = (JSONObject) prices.get(0);
            priceValue.instrumentCode = getIsin(getTicker(jsonPrice));
            priceValue.provider = "Bloomberg";
            priceValue.priceValueEvol = comuteEvolution((JSONObject) prices.get(0));
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
            if (priceValue != null)
                System.out.println("Store Price From Bloomberg " + priceValue.instrumentCode + " " + priceValue.date + " " + priceValue.priceValue + " " + priceValue.priceValueEvol * 100 + "%");
            new StorePrice().storeInSqlite(priceValue);

        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
            // Store Message in fie system for debug
        }

        catch (Exception e) {
            //new StorePrice().storeDebug(ticker, baseUrl + "\n" + doc.toString());
            return;
        }


    }

    public void getPrices(String isin, String ticker) {
        // Other URL to try
        // https://www.bloomberg.com/markets/chart/data/1M/AAPL:US
        // Possible to use 1_WEEK, 1_MONTH, 1_YEAR,5_YEAR,MAX default is 1_WEEK
        String baseUrl = "http://www.bloomberg.com/markets/api/bulk-time-series/price/" + ticker + "?timeFrame=1_MONTH";
        //String baseUrl="https://www.bloomberg.com/markets/chart/data/1M/AAPL:US";
        Document doc = null;
        Elements priceElt = null;
        try {
            String ua = "Mozilla/5.0 (Macintosh)";
            doc = Jsoup.connect(baseUrl).validateTLSCertificates(false).ignoreContentType(true).userAgent(ua).get();
            // System.out.println(doc.text());
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(doc.body().text());
            ArrayList prices = (ArrayList) obj;
            JSONObject jsonPrice = (JSONObject) prices.get(0);

            ArrayList prices1 = (ArrayList) jsonPrice.get("price");
            // Loop on prices
            ArrayList priceValues = new ArrayList();
            for (int i = 0; i < prices1.size(); i++) {
                Price priceValue = new Price();
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
                priceValue.priceValueEvol = comuteEvolution((JSONObject) prices.get(0));
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
                priceValues.add(priceValue);
            }

            Price.computeEvol(priceValues);
            new StorePrice().storeInSqlite(priceValues);

        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }


    }


    public void getLastPrice(String isin, String ticker) {
        // wait 1 second between request to avoid robot detection by bloomberg
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        // Possible to use 1_WEEK, 1_MONTH, 1_YEAR,5_YEAR,MAX default is 1_WEEK
        String baseUrl = "https://www.bloomberg.com/markets/api/bulk-time-series/price/" + ticker + "?timeFrame=1_MONTH";
        //String baseUrl="https://www.bloomberg.com/markets/chart/data/1M/AAPL:US";
        Document doc = null;
        Elements priceElt = null;
        try {
            //String ua = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.12; rv:57.0) Gecko/20100101 Firefox/57.0";
            String ua = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.12; rv:62.0) Gecko/20100101 Firefox/62.0";
            doc = Jsoup.connect(baseUrl).validateTLSCertificates(true).ignoreContentType(true).userAgent(ua).timeout(50 * 1000).get();
            // System.out.println(doc.text());
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            new StorePrice().storeDebug(ticker, baseUrl + "\n" + doc.toString());
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
            priceValue.priceValueEvol = comuteEvolution((JSONObject) prices.get(0));
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
            if (priceValue != null)
                System.out.println("Store Price From Bloomberg " + priceValue.instrumentCode + " " + priceValue.date + " " + priceValue.priceValue + " " + priceValue.priceValueEvol * 100 + "%");
            new StorePrice().storeInSqlite(priceValue);

        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
            // Store Message in fie system for debug 
            new StorePrice().storeDebug(ticker, baseUrl + "\n" + doc.toString());
            return;
        }

        catch (Exception e) {
            new StorePrice().storeDebug(ticker, baseUrl + "\n" + doc.toString());
            return;
        }


    }


    double comuteEvolution(JSONObject price) {
        double evol = 0;
        Number priceObj = (Number) price.get("lastPrice");
        String lastUpdateDate = (String) price.get("lastUpdateDate");
        Number prevprice;
        JSONArray prices = (JSONArray) price.get("price");
        JSONObject jsonPrevPrice = (JSONObject) prices.get(prices.size() - 1);
        // Test if not same date
        if (!((String) jsonPrevPrice.get("date")).equals(lastUpdateDate)) {
            prevprice = (Number) jsonPrevPrice.get("value");
        } else {
            jsonPrevPrice = (JSONObject) prices.get(prices.size() - 2);
            prevprice = (Number) jsonPrevPrice.get("value");
        }

        if (prevprice != null) {
            evol = (priceObj.doubleValue() - prevprice.doubleValue()) / prevprice.doubleValue();
        }
        return evol;
    }

}
