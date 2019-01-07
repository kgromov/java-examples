package com.ewolff.demo;

import com.ewolff.compiletimeweaving.DemoAspect;
import org.apache.commons.lang3.StringEscapeUtils;

import java.util.Timer;

public class CallAdvicedMethod {
	
	public static void main(String[] args) throws InterruptedException {
		/*Thread [] threads = new Thread[5];
		for (int i = 0; i < 5; i++)
		{
			threads[i] = new Thread(new LongRunner());
		}
		for (int i = 0; i < 5; i++)
		{
			threads[i].start();
			threads[i].join();
		}*/
		/*Timer timer = new Timer(true);
		DemoAspect.InterruptTimerTask interruptTimerTask = new DemoAspect.InterruptTimerTask(Thread.currentThread());
		timer.schedule(interruptTimerTask, 10);
		System.out.println("Inside main = " + Thread.currentThread().getName());*/
		try {
			// put here the portion of code that may take more than "waitTimeout"
			DemoClass demoClass = new DemoClass();
			demoClass.advicedMethod();
			System.out.println("After aspect");
		} finally {
//			timer.cancel();
		}
//		demoClass.callsTheAdvicedMethohd();
		/*demoClass.toString();
		StringEscapeUtils.escapeJava("vcx");
        System.out.println(DemoAspect.internalInvocation);
        System.out.println(DemoAspect.externalInvocation);*/
//		LongRunner longRunner = new LongRunner();
//		longRunner.run();
//		System.out.println("After running...");
	}

}
