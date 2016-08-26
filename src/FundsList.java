import java.util.ArrayList;
import java.util.List;
import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: pzagiel
 * Date: Sep 19, 2015
 * Time: 12:32:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class FundsList {
    List funds;


    {
        funds = new ArrayList();
    }

    public void addInstrument(String name, String Isin, String dataSourceId, String provider) {
        funds.add(new Instrument(name, Isin, dataSourceId, provider));
    }

    public void init() {

        /* addInstrument("H20 Multibonds","F00000JQYQ");
    addInstrument("H20 Multibonds","F00000JQYQ");
    addInstrument("H20 Multiequities","F00000MIEA");
    addInstrument("Kaldemorgen","F00000MEJK");
    addInstrument("DNCA Eurorose","f000001ai5");
    addInstrument("R Valor","F0GBR06756");
    addInstrument("BlackRock Strategic Fund European Absolute Return A2","f000002m3n");
    addInstrument("Carmignac Patrimoine","f0gbr04f90");   */

        addInstrument("UBS Height Yield", "LU0086177085", "F0GBR04AKY", "FrankfurtBoerse");
        addInstrument("Blue Bay Emerging Market Bond", "LU0356218064", "F000002NG4", "FrankfurtBoerse");
        addInstrument("Henderson Global Technology", "LU0070992663", "F0GBR04E8V", "FrankfurtBoerse");
        addInstrument("Henderson Horizon Fund - Global Property", "LU0828244219", "F00000OS39", "FrankfurtBoerse");
        addInstrument("BlackRock Global Funds - World Mining A2", "LU0075056555", "F0GBR04ARJ", "FrankfurtBoerse");


    }

    public void initWithFile(String filename) {
        FileInputStream fstream = null;
        try {
            fstream = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        String strLine;
        int lineNumber = 0;
//Read File Line By Line
        try {
            while ((strLine = br.readLine()) != null) {
                // Print the content on the console
                lineNumber = lineNumber + 1;
                if (lineNumber > 1) {
                    String[] data = strLine.split(",");
                    if (data.length == 4) {
                        funds.add(new Instrument(data[0], data[1], data[2], data[3]));
                        //System.out.println(strLine);
                    }

                }

            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

//Close the input stream
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    public List getFunds() {
        return funds;
    }

}
