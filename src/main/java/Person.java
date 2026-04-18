 /**
  *
  * @author Elif Yılmaz - elif.yilmaz41@ogr.sakarya.edu.tr
  * @since 11 Nisan 2026
  * <p>
  * Şehir Nüfus Simülasyonu projesi kapsamında geliştirilen sınıftır.
  * Nesne yönelimli programlama prensipleri kullanılarak oluşturulmuştur.
  * </p>
  * @group 1B
  */
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
        if (age < 50) {
            age++;
        }
    }
}  