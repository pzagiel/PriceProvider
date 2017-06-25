import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: pzagiel
 * Date: Sep 15, 2015
 * Time: 7:03:00 PM
 */
public class Price {
    public String instrumentCode;
    public String provider;
    public String date;
    public double priceValue;
    public double priceValueEvol;

    public static ArrayList<Price> computeEvol(ArrayList<Price> prices) {
        for (int i = prices.size() - 1; i > 0; i--) {
            Price currentPrice = prices.get(i);
            Price prevPrice = prices.get(i - 1);
            currentPrice.priceValueEvol = (currentPrice.priceValue - prevPrice.priceValue) / prevPrice.priceValue;
        }
        return prices;
    }

}
