package com.ewolff.compiletimeweaving;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Aspect
public class DemoAspect {
    public static Map<String, AtomicInteger> internalInvocation = new HashMap<>();
    public static Map<String, AtomicInteger> externalInvocation = new HashMap<>();
    public static Map<String, AtomicInteger> annotatedInvocation = new HashMap<>();

    //    @Before("execution(void advicedMethod())")
    public void logInternal(JoinPoint pointcut) {
//        System.out.println("Internal Aspect called:" + pointcut.toString());
        internalInvocation.computeIfAbsent(pointcut.toString(), count -> new AtomicInteger()).incrementAndGet();
    }

    //    @Before("within(org.apache.commons.lang3..*) && execution(* translate(..))")
//    @Before("within(org.junit..*) && execution(* *(..))")
    public void logExternal(JoinPoint pointcut) {
//        System.out.println("Before execution method:" + pointcut.toString());
        externalInvocation.computeIfAbsent(pointcut.toString(), count -> new AtomicInteger()).incrementAndGet();
    }

    private static final long TIMEOUT = 1_000L;
    private static volatile State state;

    private enum State {
        STARTED, FINISHED
    }

    //    @Around("execution(void run())")
    public void logExternal2(ProceedingJoinPoint joinPoint) {
        System.out.println("Before proceeding method:" + joinPoint.toString());
        try {
//            final Runnable stuffToDo = () -> {};
            final ExecutorService executor = Executors.newSingleThreadExecutor();
            final Future future = executor.submit(Thread.currentThread());
            future.get(TIMEOUT, TimeUnit.MILLISECONDS);
//            future.cancel(true);
            joinPoint.proceed();
        } catch (Throwable t) {
            if (t instanceof TimeoutException || t instanceof InterruptedException) {
                throw new RuntimeException("Stop method " + joinPoint);
            }
        }
    }

    @Before("execution(void advicedMethod())")
    public void beforeMethod(JoinPoint joinPoint) throws InterruptedException {
        state = State.STARTED;
        System.out.println("Before proceeding method:" + joinPoint.toString() + " state = " + state);
        System.out.println("Inside Aspect = " + Thread.currentThread().getName());
        Timer timer = new Timer(true);
        InterruptTimerTask interruptTimerTask = new InterruptTimerTask(Thread.currentThread());
        timer.schedule(interruptTimerTask, 1100);

        try {
            // put here the portion of code that may take more than "waitTimeout"
        } finally {
            timer.cancel();
        }
        /*// fails but not stop thread with method
        Timeouter timeouter = new Timeouter(Thread.currentThread(), joinPoint.toString(), 10);
        Thread thread = new Thread(timeouter);
        try {
            thread.start();
        }
        catch (RuntimeException e)
        {
            throw new RuntimeException(e);
        }*/
    }

    @After("execution(void advicedMethod())")
    public void afterMethod(JoinPoint joinPoint) {
        state = State.FINISHED;
        System.out.println("After proceeding method:" + joinPoint.toString() + " state = " + state);
    }

    private final class Timeouter implements Runnable {
        private Thread theTread;
        private final String methodSignature;
        private final long timeout;

        public Timeouter(String methodSignature, long timeout) {
            this.methodSignature = methodSignature;
            this.timeout = timeout;
        }

        public Timeouter(Thread theTread, String methodSignature, long timeout) {
            this.theTread = theTread;
            this.methodSignature = methodSignature;
            this.timeout = timeout;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(timeout);
                if (state != State.FINISHED) {
                    System.out.println("Method: " + methodSignature + " not finished in " + timeout + " ms");
                    theTread.interrupt();
//                    theTread.stop();
                    throw new RuntimeException("Method: " + methodSignature + " not finished in " + timeout + " ms");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class InterruptTimerTask extends TimerTask {
        private Thread theTread;

        public InterruptTimerTask(Thread theTread) {
            this.theTread = theTread;
        }

        @Override
        public void run() {
            System.out.println("timeout exeeded");
            theTread.interrupt();
        }
    }

   /* //    @Before("execution(void bm()) && @annotation(Before)")
    @Before("execution(@annotation(Before)")
    public void logAnnotatedMethods(JoinPoint pointcut)
    {
        System.out.println("Before annotated method:" + pointcut.toString());
        annotatedInvocation.computeIfAbsent(pointcut.toString(), count -> new AtomicInteger()).incrementAndGet();
    }*/
}
