package main.com.stuff;

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

    public static JSONArray requestNews() {

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

}
