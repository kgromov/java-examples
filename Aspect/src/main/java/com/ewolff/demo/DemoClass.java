package com.ewolff.demo;


import org.apache.commons.lang3.time.StopWatch;

import java.time.Instant;

public class DemoClass {

	public void advicedMethod() {
		StopWatch watch = new StopWatch();
		watch.start();
		System.out.println("Start: " + watch);
		for (int i = 0; i < 1_0000_0000; i++)
		{
			String res = " "+ i;
		}
		/*try {
			Thread.sleep(1100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		watch.stop();
		System.out.println("Finish in: " + watch.toString());
	}

	public void callsTheAdvicedMethohd() {
		advicedMethod();
	}

}
