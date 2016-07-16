package com.example.priansyah.demo1;

/**
 * Created by ester gyo fanny on 03/06/2016.
 */
public class Constant {
    public static final String INT_TYPE = "INT";
    public static class Transaction {
        public static final String ID = "_id";

        public static String[] getColumns() {
            return new String[]{ID, "something", "something", "something"};
        }
    }
}
