package com.tests;

import static com.stuff.PoloAPI.CandlePeriod.FIVE_MINUTES;
import static com.stuff.PoloAPI.CurrencyPair.BTC_BCH;
import static com.stuff.PoloAPI.requestCandleChart;

import java.io.FileNotFoundException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class MainTrifouille {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) throws FileNotFoundException {
        long start = Instant.now().minusSeconds(3600).getEpochSecond();
        long end = Instant.now().getEpochSecond();
        System.out.println(start);
        System.out.println(end);
        System.out.println(requestCandleChart(BTC_BCH, FIVE_MINUTES, start, end));
    }
}
