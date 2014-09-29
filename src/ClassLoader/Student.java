package classloader;

/**
 * Created by i.vartanian on 25.09.2014.
 */
public class Student {

    private String name = "Anna";
    private int age = 25;

    public Student() {
    }

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return new StringBuilder("Student[")
                .append("name = ")
                .append(name)
                .append("; age = ")
                .append(age)
                .append("]")
                .append(" -> ")
                .append("version #2")
                .toString();
    }

}
