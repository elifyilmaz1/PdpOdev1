import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.javafaker.Faker;

public class District {

    private final String name;
    private final List<Neighborhood> neighborhoods;

    public District(String name) {
        this.name = name;
        this.neighborhoods = new ArrayList<>();
    }

    public static District create(Faker faker) {
        return new District(faker.address().cityName() + " " + faker.address().streetSuffix());
    }

    public String getName() {
        return name;
    }

    public List<Neighborhood> getNeighborhoods() {
        return Collections.unmodifiableList(neighborhoods);
    }

    void addNeighborhood(Neighborhood n) {
        neighborhoods.add(n);
    }

    public int getNeighborhoodCount() {
        return neighborhoods.size();
    }

    public int getPopulation() {
        int sum = 0;
        for (Neighborhood n : neighborhoods) {
            sum += n.getPopulation();
        }
        return sum;
    }

    List<Neighborhood> detachNeighborhoods() {
        List<Neighborhood> copy = new ArrayList<>(neighborhoods);
        neighborhoods.clear();
        return copy;
    }
}
