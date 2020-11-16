package edu.utep.cs.cs4330.mypricewatchernew;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
    Authors: Luis Gutierrez Antonio Zavala
    Class: CS4330
 */

public class PriceFinder{
    private double price;
    private String store;
    private Pattern pattern;

    public double findPrice(String url) {
        try {
            if (url.substring(12, 21).equals("homedepot")) {
                pattern = Pattern.compile("content=\"\\d+[\\.]\\d+\\d");
                price = readSite();
                return price;
            } else if (url.substring(12, 16).equals("ebay")) {
                pattern = Pattern.compile("content=\"\\d+[\\.]\\d+\\d");
                price = readSite();
                return price;
            } else if (url.substring(12, 19).equals("walmart")) {
                pattern = Pattern.compile("content=\"\\d+[\\.]\\d+\\d");
                price = readSite();
                return price;
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return price;
    }

    public double readSite() throws Exception {
        URL url = new URL(store);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String line;
        while ((line = in.readLine()) != null) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                price = Double.parseDouble((line.substring(matcher.start(0), matcher.end(0))).substring(9));
                return price;
            }
        }
        in.close();
        return price;
    }
}
