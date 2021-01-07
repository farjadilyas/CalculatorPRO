package com.farjad.calculator;

public class ThemeVar {
    static int s = -1;
    private static final ThemeVar ourInstance = new ThemeVar();
    public static ThemeVar getInstance() {
        return ourInstance;
    }
    private ThemeVar() {
    }
    public static void setData(int data) {
        s = data;
    }
    public static int getData() {
        return s;
    }
}
