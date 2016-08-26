import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.Jsoup;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Date;
import java.text.DecimalFormatSymbols;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * Created by IntelliJ IDEA.
 * User: pzagiel
 * Date: Nov 30, 2015
 * Time: 11:47:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class FrankfurtBoerseProviderNew {


    public static void main(String[] args) {

        FrankfurtBoerseProviderNew frankfurtBoerseProviderNew = new FrankfurtBoerseProviderNew();
        frankfurtBoerseProviderNew.getLastPrice("FR0007054358", "00000012,1189343,13,814");        // Lyxor Eurostox
        frankfurtBoerseProviderNew.getLastPrice("FR0007054358", "00000012,1189343,13,814");        //
        // Search preLoadedSeries in page source
        frankfurtBoerseProviderNew.getLastPrice("DE0005933931", "00000012,1174954,13,814"); // IShare Dax

        // Bizare le cours de cloture sur le graph et dans le tableau ne sont pas les mêmes BUG ?
        frankfurtBoerseProviderNew.getLastPrice("LU0252633754", "00000012,2532201,13,814");        // Lyxor DAX for the moment we take price from Morningstar

        // http://www.boerse-frankfurt.de/Ajax/DerivativesController_HistoricPriceList/A1N49P/FSE/1.11.2015_1.12.2015_Derivative
        // Pas Historique pour le moment
        frankfurtBoerseProviderNew.getLastPrice("DE000A1N49P6", "1613,18171660,13,814");        // ETFS Brent Crude
        frankfurtBoerseProviderNew.getLastPrice("US71647NAF69", "1,21412786,13,333");        // Petrobras
        // Petrobras example from and till not necessary but maybe can be use
        // Ou je confond avec euronext
        // Pas oublier de rendre la période dynamique, sinon pas de prix après le 30 nov 2015

        //http://proxy.boerse-frankfurt.de/cst/BoerseFrankfurt/Share/chart.json?instruments=1,21412786,13,333&period=OneYear
    }


    public void getLastPrice(String isinCode, String valorCode) {
        String baseUrl = "http://proxy.boerse-frankfurt.de/cst/BoerseFrankfurt/Share/chart.json?instruments=" + valorCode + "&period=OneWeek";

        //System.out.println(baseUrl);
        Document doc = null;
        Elements priceElt = null;
        try {
            String ua = "Mozilla/5.0 (Macintosh)";
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
        ArrayList instruments = (ArrayList) jsonObject.get("instruments");
        // Get collection of data of instrument
        JSONObject jsonObjectPrices = (JSONObject) instruments.get(0);
        ArrayList prices = (ArrayList) jsonObjectPrices.get("data");
        ArrayList priceData = (ArrayList) prices.get(prices.size() - 1);
        // Get Last Price
        Price priceValue = buildPriceFromJson(priceData, isinCode);
        if (priceValue != null)
            System.out.println("Store Price From frankfurtBoerse " + priceValue.instrumentCode + " " + priceValue.date + " " + priceValue.priceValue);
        new StorePrice().storeInSqlite(priceValue);

        // Get Last Price corresponding to till date
        //JSONObject jsonPrice = (JSONObject) prices.get(prices.size() - 1);
    }


    private Price buildPriceFromJson(ArrayList priceData, String isin) {
        Price priceValue = new Price();
        priceValue.instrumentCode = isin;
        Double doubleDeconds = (Double) priceData.get(0);
        long seconds = doubleDeconds.longValue();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date priceDate = new Date(seconds);
        priceValue.date = sdf.format(priceDate);
        priceValue.priceValue = (Double) priceData.get(4);
        priceValue.provider = "frankfurtBoerse";
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

    // JSON Data Url example
    //http://proxy.boerse-frankfurt.de/cst/BoerseFrankfurt/Share/chart.json?instruments=00000012,2532201,13,814&period=OneWeek
    //http://proxy.boerse-frankfurt.de/cst/BoerseFrankfurt/Share/chart.json?instruments=00000012,2532201,830,814&period=OneWeek
    // Instrument ETF Lyxor DAX =00000012,2532201,13,814 13 pour Boerse Frankfurt et 830 Extra je crois
    //http://www.boerse-frankfurt.de/etp/historisch/Multi-Units-Lux----Lyxor-ETF-DAX-LU0252633754#Kurshistorie
    //http://proxy.boerse-frankfurt.de/cst/BoerseFrankfurt/Share/chart.json?instruments=00000012,2532201,13,814&period=OneYear

    // URL Lyxor EURO/Stocks 50
    //http://proxy.boerse-frankfurt.de/cst/BoerseFrankfurt/Share/chart.json?instruments=00000012,1189343,13,814&period=OneYear

    // URL PETROBARS Oblig

    //http://proxy.boerse-frankfurt.de/cst/BoerseFrankfurt/Share/chart.json?instruments=1,21412786,13,333&period=OneYear
    // 333 pour Obli et 814 pour ETF je crois

    /*
     Period correspondant je crois au 3 ème paramètres instrument


         case 'Intraday':		prefix = '10'; break;
                     case 'CurrentWeek': prefix = '11'; break;
                     case 'OneWeek':			prefix = '12'; break;
                     case 'OneMonth':		prefix = '13'; break;
                     case 'ThreeMonths': prefix = '14'; break;
                     case 'SixMonths':		prefix = '15'; break;
                     case 'OneYear':			prefix = '16'; break;
                     case 'ThreeYears':	prefix = '17'; break;
                     case 'FiveYears':		prefix = '18'; break;
                     case 'TenYears':		prefix = '19'; break;
                     case 'Maximum':			prefix = '99'; break;
                     default:						prefix = '10'; break;

    */

    /*
    Url Construction
    'http://proxy.boerse-frankfurt.de/cst/BoerseFrankfurt/Share/chart.json?' + "instruments=" + prefix + identifier + "&period=" + period;

    identifier= code instrument puis virgule puis code bourse
    identifier défini comme suit dans le preload des données lors du chargement de la page
    identifier: '00000012,2825395,13,814
    */

    /* Apparement identifiant instrument est la réference suivant la codification Valor*/
    // En effet valor est un identifiant utilisé en Suisse come le CUSIP au US

    // LYXOR DAX code valor=2532201  ISIN=LU0252633754 valor code

    // Globally a VALOR number is allocated for any type of financial instrument which meets the allocation rules.[1] It can be used in conjunction with the MIC and the Currency Code to uniquely identify a traded instrument. It can be used in transaction reporting and for position keeping.
    // MIC ISO 10383 - Market Identifier Codes
    // XBRU EURONEXT - EURONEXT BRUSSELS
    //XPAR	O	EURONEXT - EURONEXT PARIS
    //XBER	O	BOERSE BERLIN
    //XFRA	S	BOERSE FRANKFURT - REGULIERTER MARKT
    //XFRA	S	XETRA
    //XNAS	O	NASDAQ - ALL MARKETS
    //XNYS	O	NEW YORK STOCK EXCHANGE, INC.
}
