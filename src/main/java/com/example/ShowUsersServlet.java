package com.example;

import static com.stuff.PoloAPI.CandlePeriod.FIVE_MINUTES;
import static com.stuff.PoloAPI.CurrencyPair.BTC_BCH;
import static com.stuff.PoloAPI.requestCandleChart;

import java.io.IOException;
import java.time.Instant;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.json.JSONArray;

public class ShowUsersServlet extends HttpServlet {

    public ShowUsersServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("html/text");

//        Scanner in = new Scanner(new File("src/main/resources/netflix-stock-price.csv"));
//        String received ="";
//
//        while (in.hasNext()) {
//            received += in.nextLine();
//            received+="\n";
//        }

        long start = Instant.now().minusSeconds(3600).getEpochSecond();
        long end = Instant.now().getEpochSecond();

        JSONArray jsonArray = requestCandleChart(BTC_BCH, FIVE_MINUTES, start, end);
        System.out.println(jsonArray.toString());
        resp.getOutputStream().print(jsonArray.toString());
    }


}
