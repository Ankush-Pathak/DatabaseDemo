package com.ankushvpathakgmail.sqlfirebasedemo;

/**
 * Created by ankush on 23/2/18.
 */

public class Count {
    String name;
    int count;

    public Count(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public Count() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
