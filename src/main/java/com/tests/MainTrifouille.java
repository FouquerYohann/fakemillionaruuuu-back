package com.tests;

import org.json.JSONObject;

import com.stuff.DBUtils;

public class MainTrifouille {

    public static void main(String[] args) {
        JSONObject inscription = DBUtils.inscription("arthur", "pass");
        System.out.println(inscription.toString());

    }
}
