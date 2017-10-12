package com.tests;

import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;

import org.json.JSONArray;

import com.stuff.PoloAPI;

public class MainTrifouille {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) throws FileNotFoundException {
//        long start = Instant.now().minusSeconds(3600).getEpochSecond();
//        long end = Instant.now().getEpochSecond();
//        System.out.println(start);
//        System.out.println(end);
//        System.out.println(requestCandleChart(BTC_BCH, FIVE_MINUTES, start, end));
//
        PoloAPI.CandlePeriod candlePeriod = PoloAPI.CandlePeriod.findCandlePeriod("FIVE_MINUTES");
        PoloAPI.CurrencyPair currencyPair = PoloAPI.CurrencyPair.valueOf("BTC_BCH");

        System.out.println(candlePeriod);
        System.out.println(currencyPair);
        System.out.println();


        JSONArray retour = PoloAPI.requestCandleChart(currencyPair,Integer.parseInt(candlePeriod.toString()), 1507811223,1507814823);

        System.out.print(retour.toString());
    }
}
