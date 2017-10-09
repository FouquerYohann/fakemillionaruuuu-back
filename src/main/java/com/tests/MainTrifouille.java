package com.tests;

import com.stuff.DBUtils;

public class MainTrifouille {

    public static void main(String[] args) {
        if (!DBUtils.checkLogin("yohann", "pass")) {
            System.out.println("pas trouver");
        }
        else{
            System.out.println("trouver");
        }
    }
}
