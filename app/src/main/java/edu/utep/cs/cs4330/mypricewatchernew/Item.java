package edu.utep.cs.cs4330.mypricewatchernew;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
    Authors: Luis Gutierrez Antonio Zavala
    Class: CS4330
 */

public class Item {
    private String name;
    private double initPrice;
    private double currentPrice;
    private String change;
    private String url;
    private String dateAdded;
    private String sourceName;
    private PriceFinder priceFinder;
    private int id;

    //region Constructors

    public Item(int id, String name, double initPrice, double currentPrice, String url, String sourceName, String dateAdded) {
        this.id = id;
        this.name = name;
        this.initPrice = initPrice;
        this.currentPrice = currentPrice;
        this.url = url;
        this.sourceName = sourceName;
        this.change = calculateChange();
        this.dateAdded = dateAdded;
    }

    public Item(String name, String url, String sourceName) {
        this.name = name;
        this.url = url;
        new PriceFinderAsync().execute();
        this.initPrice = currentPrice;
        this.sourceName = sourceName;
        this.change = calculateChange();
        this.dateAdded = setDateAdded();
    }

    //endregion

    //region Getters

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getInitPrice() {
        return initPrice;
    }

    public double getCurrentPrice() {
        new PriceFinderAsync().execute();
        return currentPrice;
    }

    public String getChange() {
        return calculateChange();
    }

    public String getUrl() {
        return url;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public String getSourceName() {
        return sourceName;
    }

    //endregion

    //region Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String setDateAdded() {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

    //endregion

    private String calculateChange() {
        String perDifStr;
        if (initPrice == 0) {
            perDifStr = "Price hasn't changed";
            return perDifStr;
        } else {
            double percDif = ((currentPrice - initPrice) / initPrice) * 100;
            percDif = Math.round(percDif * 100d) / 100d;

            if (percDif == 0) {
                perDifStr = "Price hasn't changed";
                return perDifStr;
            } else if (percDif < 0) {
                percDif = percDif * -1;
                perDifStr = "It is " + percDif + "% more expensive.";
                return perDifStr;
            } else {
                perDifStr = "It is " + percDif + "% cheaper.";
                return perDifStr;
            }
        }
    }

    public void refresh() {
        new PriceFinderAsync().execute();
        change = calculateChange();
    }

    private class PriceFinderAsync extends AsyncTask<Void, Void, Void> {
        private double price;
        private String store;
        private Pattern pattern;

        @Override
        protected Void doInBackground(Void... voids) {
            findPrice();
            return null;
        }

        public void findPrice() {
            try {
                if (url.substring(12, 21).equals("homedepot")) {
                    pattern = Pattern.compile("content=\"\\d+[.]\\d+\\d");
                    price = readSite();
                    currentPrice = price;
                } else if (url.substring(12, 16).equals("ebay")) {
                    pattern = Pattern.compile("content=\"\\d+[.]\\d+\\d");
                    price = readSite();
                    currentPrice = price;
                } else if (url.substring(12, 19).equals("walmart")) {
                    pattern = Pattern.compile("content=\"\\d+[.]\\d+\\d");
                    price = readSite();
                    currentPrice = price;
                }

            } catch (Exception e) {
                System.out.println(e);
            }
            currentPrice = price;
        }

        public double readSite() throws Exception {
            URL url = new URL(getUrl());
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
}
