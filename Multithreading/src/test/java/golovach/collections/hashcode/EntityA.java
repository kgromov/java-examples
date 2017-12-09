package golovach.collections.hashcode;

/**
 * Created by konstantin on 08.12.2017.
 */
public class EntityA {
    private int age;
    private int height;
    private String name;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return 31 * age + 37 * height + (name != null ? name.hashCode() : 0);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof EntityA)) return false;
        EntityA that = (EntityA) obj;
        return this.age == that.age && this.height == that.height &&
                (this.name == null ? that.name == null : this.name.equals(that.name));
    }

    @Override
    public String toString() {
        return "EntityA{" +
                "age=" + age +
                ", height=" + height +
                ", name='" + name + '\'' +
                '}';
    }
}
