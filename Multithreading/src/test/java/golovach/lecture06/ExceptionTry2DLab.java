package golovach.lecture06;

/**
 * Created by kgr on 5/30/2017.
 */
public class ExceptionTry2DLab {
    /*
     * 1) insert throw new NullPointerException();
     * 2) insert throw new Exception();
     * 3) insert throw new OutOfMemoryError();
     */
    public static void main(String[] args) {
        System.out.println(0);
        try {
            System.out.println(1);
            if (true) {/*...*/}
            System.out.println(10);
            try {
                System.out.println(2);
                if (true) {/*...*/}
                System.out.println(20);
            } catch (RuntimeException e) {
                System.out.println(3);
                if (true) {/*...*/}
                System.out.println(30);
            } catch (Error e){
                System.out.println(4);
                if (true) {/*...*/}
                System.out.println(40);
            } finally {
                System.out.println(5);
                if (true) {/*...*/}
                System.out.println(50);
            }
            System.out.println(6);
        } catch (RuntimeException e2) {
            System.out.println(1);
            if (true) {/*...*/}
            System.out.println(10);
            try {
                System.out.println(2);
                if (true) {/*...*/}
                System.out.println(20);
            } catch (RuntimeException e) {
                System.out.println(3);
                if (true) {/*...*/}
                System.out.println(30);
            } catch (Error e){
                System.out.println(4);
                if (true) {/*...*/}
                System.out.println(40);
            } finally {
                System.out.println(5);
                if (true) {/*...*/}
                System.out.println(50);
            }
            System.out.println(6);
        } catch (Error e3) {
            System.out.println(1);
            if (true) {/*...*/}
            System.out.println(10);
            try {
                System.out.println(2);
                if (true) {/*...*/}
                System.out.println(20);
            } catch (RuntimeException e) {
                System.out.println(3);
                if (true) {/*...*/}
                System.out.println(30);
            } catch (Error e){
                System.out.println(4);
                if (true) {/*...*/}
                System.out.println(40);
            } finally {
                System.out.println(5);
                if (true) {/*...*/}
                System.out.println(50);
            }
            System.out.println(6);
        }  finally {
            System.out.println(1);
            if (true) {/*...*/}
            System.out.println(10);
            try {
                System.out.println(2);
                if (true) {/*...*/}
                System.out.println(20);
            } catch (RuntimeException e) {
                System.out.println(3);
                if (true) {/*...*/}
                System.out.println(30);
            } catch (Error e){
                System.out.println(4);
                if (true) {/*...*/}
                System.out.println(40);
            } finally {
                System.out.println(5);
                if (true) {/*...*/}
                System.out.println(50);
            }
            System.out.println(6);
        }
        System.out.println(11);
    }
}
