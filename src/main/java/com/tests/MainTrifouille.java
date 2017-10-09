package com.tests;

import java.time.format.DateTimeFormatter;

import com.stuff.DBUtils;

public class MainTrifouille {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
//        JSONObject inscription = DBUtils.inscription("arthur", "pass");
//        System.out.println(inscription.toString());

        System.out.println(DBUtils.checkSession(1));
//
//        LocalDateTime last = LocalDateTime.parse("2017-10-09 16:21:33",DATE_TIME_FORMATTER);


    }
}
