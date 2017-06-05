package golovach.lecture06;

/**
 * Created by kgr on 5/30/2017.
 */
public class ExceptionSimpleLab {
    /*
     * 1) insert throw new NullPointerException();
     * 2) insert throw new Error();
     */
    public static void main(String[] args) {
        System.out.println(0);
        try{
            System.out.println(1);
            if(true) {/*...*/}
            System.out.println(2);
        } catch(RuntimeException e){
            System.out.println(3);
            if(true) {/*...*/}
            System.out.println(4);
        } finally {
            System.out.println(5);
            if(true) {/*...*/}
            System.out.println(6);
        }
        System.out.println(7);
    }
}

/*
 * 1) NPE, ..., ... - 0134567
 * 2) ..., NPE, ... - 012356
 * 3) ..., ..., NPE - 0125
 *
 * 1) NPE, NPE, ... - 01356
 * 2) ..., NPE, NPE - 0125
 * 3) NPE, NPE, NPE - 0135
 *
 * 5) E, ..., ... - 0156
 * 6) ..., E, ... - 01256
 * 7) ..., ..., E - 012345
 *
 * 1) E, E, ... - 0156
 * 2) ..., E, E - 0125
 * 3) E, E, E - 015
 *
 * 1) NPE, E, ... - 01356
 * 2) ..., NPE, E - 0125
 * 3) E, NPE, ... - 0156
 * 3) ..., E, NPE - 0125
 */
