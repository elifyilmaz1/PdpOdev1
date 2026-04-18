import java.util.concurrent.atomic.AtomicInteger;


public class Person {

    private static final AtomicInteger NEXT_ID = new AtomicInteger(1);

    private final int id;
    private final String name;
    private int age;

    public Person(String name, int age) {
        this.id = NEXT_ID.getAndIncrement();
        this.name = name;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void incrementAge() {
        age++;
    }
}
