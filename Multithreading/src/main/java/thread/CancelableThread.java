package thread;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

@Slf4j
public class CancelableThread extends Thread {
    private AtomicBoolean stopped = new AtomicBoolean(false);

    public CancelableThread(String name) {
        super(name);
    }

    private List<CancelableThread> childThreads = new ArrayList<>();

    public void setChildThreads(List<CancelableThread> childThreads) {
        this.childThreads = childThreads;
    }

    public void cancel() {
//        this.stopped.set(true);
        childThreads.forEach(CancelableThread::cancel);
        Thread.currentThread().interrupt();
        childThreads.forEach(thread -> log.info("Child thread {} state: {}", thread.getName(), thread.getState()));
        log.info("Current thread {} state: {}", this.getName(), this.getState());
    }

    @Override
    public void run() {
        try {
            log.info("Start {} thread", this.getName());
//                Thread.sleep(2000L);
            while (!stopped.get()) {
                IntStream.range(0, 1_000_000).boxed().filter(i -> i % 2 == 0).forEach(i -> {
                    log.info("{}: i = {}", this.getState(), i);
                    try {
                        Thread.sleep(10000L);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        this.stopped.set(true);
                    }
                });
            }
            log.info("Stop {} thread", this.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService timeouter = Executors.newSingleThreadExecutor();
        CancelableThread child1 = new CancelableThread("Child1");
        CancelableThread child2 = new CancelableThread("Child2");
        CancelableThread parent = new CancelableThread("Parent");

        try {
            parent.setChildThreads(List.of(child1, child2));
            parent.start();
            child1.start();
            child2.start();
            Thread.sleep(1000L);
            parent.cancel();
        } catch (Exception e) {
            log.info("Parent state = {}", parent.getState());
            log.info("Child1 state = {}", child1.getState());
            log.info("Child2 state = {}", child2.getState());
        }

    }
}
