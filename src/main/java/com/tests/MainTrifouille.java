package com.tests;

import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;

import com.stuff.DBUtils;

public class MainTrifouille {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println(DBUtils.changeValue(2, "btc", -9));
        System.out.println(DBUtils.changeValue(1, "btc", 9));

    }
}
