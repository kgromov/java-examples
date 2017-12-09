package golovach.collections.hashcode;

import java.util.Arrays;

/**
 * Created by konstantin on 08.12.2017.
 */
public class EntityB {
    private final String[][] stringArr;
    private final double[] doubleArr;

    public EntityB(String[][] stringArr, double[] doubleArr) {
        this.stringArr = stringArr;
        this.doubleArr = doubleArr;
    }

    public String[][] getStringArr() {
        return stringArr;
    }

    public double[] getDoubleArr() {
        return doubleArr;
    }

    @Override
    public int hashCode() {
        int hashCode = 1;
        // String
        for (String[] array : stringArr) {
            for (String str : array) {
                hashCode = 31 * hashCode + (str == null ? 0 : str.hashCode());
            }
        }
        // double
        for (double d : doubleArr) {
            hashCode += 37 * d;
        }
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof EntityB)) return false;
        EntityB that = (EntityB) obj;
        return Arrays.equals(this.doubleArr, that.doubleArr) &&
                Arrays.equals(this.stringArr, that.stringArr);
    }

    @Override
    public String toString() {
        return "EntityB{" +
                "stringArr=" + Arrays.toString(stringArr) +
                ", doubleArr=" + Arrays.toString(doubleArr) +
                '}';
    }
}
