package com.ewolff.compiletimeweaving;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import java.util.concurrent.*;

/**
 * Created by konstantin on 25.11.2018.
 */
//@Aspect
public class TimeOutAspect {
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
    public void beforeMethod(JoinPoint joinPoint) {
        System.out.println("Before proceeding method:" + joinPoint.toString());
        state = State.STARTED;
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        final Timeouter timeouter = new Timeouter(joinPoint.toLongString(), 1000L);
        final Future future = executor.submit(timeouter);
      /*  try {
            future.get(TIMEOUT, TimeUnit.MILLISECONDS);//
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException("Stop method " + joinPoint);
        }*/
    }

    @Before("execution(void advicedMethod())")
    public void afterMethod(JoinPoint joinPoint) {
        System.out.println("After proceeding method:" + joinPoint.toString());
        state = State.FINISHED;
    }

    private final class Timeouter implements Runnable {
        private final String methodSignature;
        private final long timeout;

        public Timeouter(String methodSignature, long timeout) {
            this.methodSignature = methodSignature;
            this.timeout = timeout;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(timeout);
                if (state != State.FINISHED) {
                    throw new RuntimeException("Method: " + methodSignature + " not finished in " + timeout + " ms");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
