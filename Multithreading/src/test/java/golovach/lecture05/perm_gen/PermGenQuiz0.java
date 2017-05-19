package golovach.lecture05.perm_gen;

/**
 * Created by kgr on 5/19/2017.
 */
public class PermGenQuiz0 {

    public static void main(String[] args) throws Exception {
        Class<?> clazz = PermGenQuiz0.class;
        byte[] buffer = ClassLoaderUtil.loadByteCode(clazz, clazz.getName());

        MyClassLoader loader = new MyClassLoader();
        for (long index = 0; index < Long.MAX_VALUE; index++) {
            String newClassName =
                    "_" + String.format("%0"
                            + (clazz.getSimpleName().length() - 1) + "d", index);
            byte[] newClassData = new String(buffer, "latin1")
                    .replaceAll(clazz.getSimpleName(), newClassName)
                    .getBytes("latin1");
//                loader = new MyClassLoader();
            loader._defineClass(
                    clazz.getName()
                            .replace(clazz.getSimpleName(), newClassName),
                    newClassData);
            System.out.println("Load class\t" + newClassName);
        }
    }
}
