package com.ewolff.test;

import com.ewolff.compiletimeweaving.DemoAspect;
import com.ewolff.demo.DemoChild;
import com.ewolff.demo.DemoClass;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AspectTest {

    @Before
    public void bm()
    {

    }

    @After
    public void am()
    {
        System.out.println(DemoAspect.internalInvocation);
        System.out.println(DemoAspect.externalInvocation);
    }

    @Test
    public void adviceIsCalled() {
        DemoClass demoClass = new DemoClass();
        demoClass.advicedMethod();
    }

    @Test
    public void adviceIsCalled2() {
        DemoClass demoClass = new DemoChild();
        demoClass.advicedMethod();
    }
}
