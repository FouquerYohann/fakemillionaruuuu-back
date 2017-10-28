package com.tests;

import static com.stuff.PoloAPI.CURRENCIES.BCH;
import static com.stuff.PoloAPI.CURRENCIES.ETH;

import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;

import com.stuff.DBUtils;
import com.stuff.WalletUtils;

public class MainTrifouille {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println(WalletUtils.checkForSales(2,true, ETH,15,0.35));
    }
}
