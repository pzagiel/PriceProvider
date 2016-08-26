/**
 * Created by IntelliJ IDEA.
 * User: pzagiel
 * Date: Sep 19, 2015
 * Time: 12:38:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class Instrument {
    public String name;
    public String Isin;
    public String ticker;
    public String dataSourceId;
    public String provider;

    Instrument(String name, String Isin, String dataSourceId, String provider) {
        this.name = name;
        this.Isin = Isin;
        this.dataSourceId = dataSourceId;
        this.provider = provider;

    }
}
