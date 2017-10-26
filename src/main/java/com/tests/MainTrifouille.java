package com.tests;

import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;

import com.stuff.DBUtils;
import com.stuff.PoloAPI;
import com.stuff.WalletUtils;

import static com.stuff.PoloAPI.CURRENCIES.ETH;

public class MainTrifouille {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) throws FileNotFoundException {
        DBUtils.addSession(1);
        System.out.println(WalletUtils.postOrder(1,true, ETH,1.23,0.0565));
    }
}
