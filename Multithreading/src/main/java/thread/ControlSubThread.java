package thread;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class ControlSubThread implements Runnable {
    private Thread worker;
    private String threadName;
    private int interval;
    private AtomicBoolean running = new AtomicBoolean(false);
    private AtomicBoolean stopped = new AtomicBoolean(true);
    private List<ControlSubThread> childThreads = new ArrayList<>();

    public ControlSubThread(String threadName, int interval) {
        this.threadName = threadName;
        this.interval = interval;
    }

    public void start() {
        worker = new Thread(this, threadName);
        worker.start();
//        childThreads.forEach(ControlSubThread::start);
    }

    public void stop() {
        running.set(false);
        childThreads.forEach(ControlSubThread::stop);
    }

    public void interrupt() {
        running.set(false);
        worker.interrupt();
        childThreads.forEach(ControlSubThread::interrupt);
    }

    public void setChildThreads(List<ControlSubThread> childThreads) {
        this.childThreads = childThreads;
    }

    boolean isRunning() {
        return running.get();
    }

    boolean isStopped() {
        return stopped.get();
    }

    public void run() {
        log.info("Thread {} started", worker.getName());
        running.set(true);
        stopped.set(false);
        while (running.get()) {
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.info("Thread {} was interrupted, Failed to complete operation", worker.getName());
                this.stop();
            }
            // do something
        }
        stopped.set(true);
    }

    public static void main(String[] args) throws InterruptedException {
        ControlSubThread child1 = new ControlSubThread("Child1", 1000);
        ControlSubThread child2 = new ControlSubThread("Child2", 8000);
        ControlSubThread parent = new ControlSubThread("Parent", 10000);
        parent.setChildThreads(List.of(child1, child2));
        parent.start();
        child1.start();
        child2.start();
        parent.interrupt();
    }
}
