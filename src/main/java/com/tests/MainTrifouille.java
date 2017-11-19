package com.tests;

import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;

import com.stuff.WalletUtils;

public class MainTrifouille {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println(WalletUtils.postOrder(1,true,"ETH",6,0.4));
    }
}
