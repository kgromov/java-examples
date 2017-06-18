package golovach.lecture08;

/**
 * Created by konstantin on 18.06.2017.
 */
public class PrintRunnable implements Runnable {
    private String message;
    private long sleep;

    public PrintRunnable(String message, long sleep) {
        this.message = message;
        this.sleep = sleep;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try{
                Thread.sleep(sleep);
            } catch (InterruptedException e){
                throw  new RuntimeException(e);
            }
            System.out.println(message);
        }
    }
}
