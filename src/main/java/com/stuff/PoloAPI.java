package com.stuff;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.*;

import org.json.*;

public class PoloAPI extends Socket {

    private static final String HTTPS_POLONIEX_COM_PUBLIC_COMMAND_RETURN_TICKER = "https://poloniex" +
                    ".com/public?command=returnTicker";

    private static final String PRIMARY_CURRENCY = "BTC";
    private static final List<String> CURRENCIES = Arrays.asList("ETH", "BCH", "XRP", "DASH", "LTC");
    private static final List<String> KEY_MAP = Arrays.asList("BTC_ETH", "BTC_BCH", "BTC_XRP", "BTC_DASH", "BTC_LTC");

    /**
     * "BTC_XRP":{
     * "percentChange":"0.00339702",
     * "high24hr":"0.00004809",
     * "last":"0.00004726",
     * "highestBid":"0.00004723",
     * "id":117,
     * "quoteVolume":"32437091.28079614",
     * "baseVolume":"1533.30540299",
     * "isFrozen":"0",
     * "lowestAsk":"0.00004726",
     * "low24hr":"0.00004600"
     *
     * @return JSonObject
     */
    public static JSONObject requestPrices() {
        try {
            URL request = new URL(HTTPS_POLONIEX_COM_PUBLIC_COMMAND_RETURN_TICKER);
            URLConnection connection = request.openConnection();
            Scanner in = new Scanner(new InputStreamReader(connection.getInputStream()));
            JSONObject json = null;
            String received = "";

            while (in.hasNext()) {
                received += in.nextLine();
            }
            in.close();

            json = new JSONObject(received);

            List<String> keysToRemove = new ArrayList<>();
            Iterator it = json.keys();
            while (it.hasNext()) {
                String key = (String) it.next();
                if (!KEY_MAP.contains(key)) {
                    keysToRemove.add(key);
                }
            }

            for (String key : keysToRemove) {
                json.remove(key);
            }

            return json;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONArray requestCandleChart(CurrencyPair pair, CandlePeriod period, long start, long end) {
        try {
            String req = "https://poloniex" +
                            ".com/public?command=returnChartData&currencyPair=" + pair + "&start=" + start + "&end="
                            + end + "&period=" + period.seconds;
            URL request = new URL(req);
            URLConnection connection = request.openConnection();
            Scanner in = new Scanner(new InputStreamReader(connection.getInputStream()));
            JSONObject json = null;
            String received = "";

            while (in.hasNext()) {
                received += in.nextLine();
            }
            in.close();
            JSONArray jsonArray = new JSONArray(received);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject tmp = (JSONObject) jsonArray.get(i);
                tmp.remove("volume");
                tmp.remove("weightedAverage");
                tmp.remove("quoteVolume");
            }

            return jsonArray;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public enum CandlePeriod {
        FIVE_MINUTES(300),
        FIFTEEN_MINUTES(900),
        THIRTY_MINUTES(1800),
        TWO_HOURS(7200),
        FOUR_HOURS(14400),
        ONE_DAY(86400);

        private int seconds;

        CandlePeriod(int i) {this.seconds = i;}

        public static CandlePeriod findCandlePeriod(String period) {
            if (period.equals("FIVE_MINUTES"))
                return FIVE_MINUTES;
            if (period.equals("FIFTEEN_MINUTES"))
                return FIFTEEN_MINUTES;
            if (period.equals("THIRTY_MINUTES"))
                return THIRTY_MINUTES;
            if (period.equals("TWO_HOURS"))
                return TWO_HOURS;
            if (period.equals("FOUR_HOURS"))
                return FOUR_HOURS;
            if (period.equals("ONE_DAY"))
                return ONE_DAY;
        }

        @Override
        public String toString() {
            return "" + this.seconds;
        }
    }

    public enum CurrencyPair {BTC_ETH, BTC_BCH, BTC_XRP, BTC_DASH, BTC_LTC;}

}
