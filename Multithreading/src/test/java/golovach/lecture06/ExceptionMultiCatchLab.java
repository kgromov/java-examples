package golovach.lecture06;

/**
 * Created by kgr on 5/30/2017.
 */
public class ExceptionMultiCatchLab {
    /*
     * 1) insert throw new NullPointerException();
     * 2) insert throw new RuntimeException();
     * 3) insert throw new IOException();
     * 4) insert throw new Error();
     */
    public static void main(String[] args) {
        System.out.println(0);
        try{
            System.out.println(1);
            if(true) {/*...*/}
            System.out.println(2);
        } catch(NullPointerException e){
            System.out.println(3);
            if(true) {/*...*/}
            System.out.println(4);
        } catch(RuntimeException e){
            System.out.println(5);
            if(true) {/*...*/}
            System.out.println(6);
        } catch(Exception e){
            System.out.println(7);
            if(true) {/*...*/}
            System.out.println(8);
        } finally {
            System.out.println(9);
            if(true) {/*...*/}
            System.out.println(10);
        }
        System.out.println(11);
    }
}
