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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.javafaker.Faker;

public class District {

    private final String name;
    private final List<Neighbourhood> neighbourhoods;

    public District(String name) {
        this.name = name;
        this.neighbourhoods = new ArrayList<>();
    }

    public static District create(Faker faker) {
        return new District(faker.address().cityName() + " " + faker.address().streetSuffix());
    }

    public String getName() {
        return name;
    }

    public List<Neighbourhood> getNeighbourhoods() {
        return Collections.unmodifiableList(neighbourhoods);
    }

    void addNeighbourhood(Neighbourhood n) {
        neighbourhoods.add(n);
    }

    public int getNeighbourhoodCount() {
        return neighbourhoods.size();
    }

    public int getPopulation() {
        int sum = 0;
        for (Neighbourhood n : neighbourhoods) {
            sum += n.getPopulation();
        }
        return sum;
    }

    List<Neighbourhood> detachNeighbourhoods() {
        List<Neighbourhood> copy = new ArrayList<>(neighbourhoods);
        neighbourhoods.clear();
        return copy;
    }
}
