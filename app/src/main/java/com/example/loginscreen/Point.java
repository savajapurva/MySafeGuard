package com.example.loginscreen;

public class Point {
    private double data;
    private long time;

    public Point(double d, long t) {
        this.time = t;
        this.data = d;
    }

    public long getTime() {
        return this.time;
    }

    public Double getData() {
        return this.data;
    }

    public void setTime(long t) {
        this.time = t;
    }

    public void setData(double d) {
        this.data = d;
    }
}
