package com.ewolff.demo;

/**
 * Created by konstantin on 18.11.2018.
 */
public class DemoChild extends DemoClass {

    @Override
    public void advicedMethod() {
        super.advicedMethod();
        try {
            Thread.sleep(110);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "DemoChild{}";
    }
}
