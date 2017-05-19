package golovach.lecture05.perm_gen;

/**
 * Created by kgr on 5/19/2017.
 */
public class MyClassLoader extends ClassLoader {
    public MyClassLoader() {
        super();
    }

    public Class<?> _defineClass(String name, byte[] byteCodes) {
        return super.defineClass(name, byteCodes, 0, byteCodes.length);
    }
}
