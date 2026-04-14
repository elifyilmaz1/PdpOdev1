import java.util.concurrent.atomic.AtomicLong;


public class Person {

    private static final AtomicLong NEXT_ID = new AtomicLong(1);

    private final long id;
    private final String name;
    private int age;

    public Person(String name, int age) {
        this.id = NEXT_ID.getAndIncrement();
        this.name = name;
        this.age = age;
    }

    public long getId() {
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
