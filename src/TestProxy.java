import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;

/**
 * Created by IntelliJ IDEA.
 * User: pzagiel
 * Date: Mar 6, 2019
 * Time: 12:09:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestProxy {
    public static void main(String[] args) throws Exception {
        System.setProperty("https.protocols", "TLSv1.1,TLSv1.2");
        //System.out.println("Start");
        TestProxy test = new TestProxy();
        test.run();
    }

    public void run() throws Exception {
        //URL url = new URL("https://www.bloomberg.com/markets/api/bulk-time-series/price/INGA:NA?timeFrame=1_MONTH");
        URL url = new URL("https://www.google.com/");
        //URL url = new URL("http://www.bloomberg.com");
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("194.88.106.86", 3128)); // or whatever your proxy is
        HttpURLConnection uc = (HttpURLConnection) url.openConnection(proxy);
        //HttpURLConnection uc = (HttpURLConnection) url.openConnection();

        uc.connect();

        String line = null;
        StringBuffer tmp = new StringBuffer();
        BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
        while ((line = in.readLine()) != null) {
            tmp.append(line);
        }
        System.out.println(tmp);
    }
}
