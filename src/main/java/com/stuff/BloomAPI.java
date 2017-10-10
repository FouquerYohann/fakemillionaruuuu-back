package com.stuff;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

import org.json.*;

public class BloomAPI {

    public static final String URL_NEWS = "https://newsapi" +
                    ".org/v1/articles?source=bloomberg&sortBy=top&apiKey=8f62fa4b0b064270929346bf0677c51e";

    private static final List<String> CURRENCIES = Arrays
                    .asList("BTC", "Bitcoin", "ETH", "Ethereum", "BCH", "Bitcoin Cash", "XRP", "Ripple", "DASH",
                                    "LTC", "LiteCoin");

    private static JSONArray getNews() {

        try {
            URL request = new URL(URL_NEWS);
            URLConnection connection = request.openConnection();
            Scanner in = new Scanner(new InputStreamReader(connection.getInputStream()));
            String received = "";

            while (in.hasNext()) {
                received += in.nextLine();
            }
            in.close();

            JSONObject jsonObject = new JSONObject(received);
            JSONArray jsonArray = (JSONArray) jsonObject.get("articles");
            JSONArray jsonArrayToReturn = new JSONArray();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject cur = (JSONObject) jsonArray.get(i);
                String title = cur.getString("title");
//                if (CURRENCIES.stream().anyMatch(currency -> title.contains(currency))) {
                    jsonArrayToReturn.put(cur);
//                }
            }

            return jsonArrayToReturn;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [
     *    {
     *        "urlToImage": "https://assets.bwbx.io/images/users/iqjWHBFdfxIU/i4SQ_f93gYJg/v0/1200x800.jpg",
     *        "title": "Spain Readies Forces Able to Seize Catalan Leader Today",
     *        "url": "https://www.bloomberg.com/news/articles/2017-10-10/spain-is-said-to-ready-forces-able-to-seize-catalan-leader-today"
     *    },
     *    {
     *        "urlToImage": "https://assets.bwbx.io/images/users/iqjWHBFdfxIU/iYHoTXo9Jqno/v0/1200x802.jpg",
     *        "title": "Trump Floats IQ Contest With Tillerson After ‘Moron’ Report",
     *        "url": "https://www.bloomberg.com/news/articles/2017-10-10/trump-floats-iq-test-contest-with-tillerson-after-moron-report"
     *    }
     * ]
     */
    public static JSONArray requestNews(int nbNews) {
        nbNews = (nbNews > 10) ? 10 : nbNews;
        JSONArray news = getNews();
        JSONArray retour = new JSONArray();
        for (int i = 0; i < nbNews; i++) {
            JSONObject toAdd = new JSONObject();
            JSONObject o = (JSONObject) news.get(i);
            toAdd.put("urlToImage", o.get("urlToImage"));
            toAdd.put("title", o.get("title"));
            toAdd.put("url", o.get("url"));
            retour.put(toAdd);
        }
        return retour;
    }

}
