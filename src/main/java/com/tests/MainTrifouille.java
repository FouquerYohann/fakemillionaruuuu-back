package com.tests;

import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;

import com.stuff.WalletUtils;

public class MainTrifouille {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println(WalletUtils.checkForSales(8,true,"ETH",2.8,0.4));
    }
}
