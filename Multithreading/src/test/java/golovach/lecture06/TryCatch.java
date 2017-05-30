package golovach.lecture06;

/**
 * Created by kgr on 5/30/2017.
 */
public class TryCatch {
    public static void main(String[] args) {
        try{
            main(args);
        } catch (StackOverflowError e){
            main(args);
        }
    }
}
