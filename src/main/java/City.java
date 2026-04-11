import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.javafaker.Faker;

/**
 * A city is built from a numeric code: tens = districts, units = neighborhoods (target),
 * whole number = population. Names use Faker.
 */
public class City {

    private final int seedCode;
    private String name;
    private final List<District> districts;

    private City(int seedCode, String name, List<District> districts) {
        this.seedCode = seedCode;
        this.name = name;
        this.districts = districts;
    }

    public static City fromSeed(int code, Faker faker) {
        int tens = code / 10;
        int units = code % 10;
        int districtCount = Math.max(1, tens);
        int targetNeighborhoods = Math.max(1, units);
        int totalNeighborhoods = nextMultipleAtLeast(targetNeighborhoods, districtCount);
        int population = nextMultipleAtLeast(code, totalNeighborhoods);

        String cityName = faker.address().city();
        List<District> built = new ArrayList<>();
        int neighborhoodsPerDistrict = totalNeighborhoods / districtCount;
        int peoplePerNeighborhood = population / totalNeighborhoods;

        for (int d = 0; d < districtCount; d++) {
            District district = District.create(faker);
            for (int n = 0; n < neighborhoodsPerDistrict; n++) {
                Neighborhood hood = Neighborhood.create(faker);
                for (int p = 0; p < peoplePerNeighborhood; p++) {
                    hood.addPerson(new Person(faker.name().fullName(), (int) faker.number().numberBetween(0, 50)));
                }
                district.addNeighborhood(hood);
            }
            built.add(district);
        }

        return new City(code, cityName, built);
    }

    private City(int seedCode, String name) {
        this(seedCode, name, new ArrayList<>());
    }

    /**
     * @return the new city created from the split, or {@code null} if no split occurred
     */
    public City splitIfNeeded(Faker faker) {
        if (getPopulation() < 1000) {
            return null;
        }
        hasSplit = true;
        int d = districts.size();
        int newCityDistricts = d / 2;
        int oldCityDistricts = d - newCityDistricts;
        if (newCityDistricts <= 0) {
            return null;
        }
        List<District> moving = new ArrayList<>();
        for (int i = oldCityDistricts; i < districts.size(); i++) {
            moving.add(districts.get(i));
        }
        while (districts.size() > oldCityDistricts) {
            districts.remove(districts.size() - 1);
        }
        City spawned = new City(seedCode, faker.address().city());
        spawned.districts.addAll(moving);
        return spawned;
    }

    public void applyPopulationGrowth(Faker faker) {
        List<Neighborhood> flat = allNeighborhoods();
        if (flat.isEmpty()) {
            return;
        }
        int rate = growthRate();
        // Add exactly growthRate new people to EACH neighborhood (bottom-up, never multiply city total)
        for (Neighborhood hood : flat) {
            for (int j = 0; j < rate; j++) {
                hood.addPerson(new Person(faker.name().fullName(), (int) faker.number().numberBetween(0, 50)));
            }
        }
    }

    public void incrementAllAges() {
        for (District district : districts) {
            for (Neighborhood hood : district.getNeighborhoods()) {
                for (Person person : hood.getPeople()) {
                    person.incrementAge();
                }
            }
        }
    }

    public int growthRate() {
        int tens = seedCode / 10;
        int units = seedCode % 10;
        int sum = tens + units;
        return sum == 0 ? 1 : sum;
    }

    public int getSeedCode() {
        return seedCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<District> getDistricts() {
        return Collections.unmodifiableList(districts);
    }

    public int getPopulation() {
        int sum = 0;
        for (District d : districts) {
            sum += d.getPopulation();
        }
        return sum;
    }

    public int getDistrictCount() {
        return districts.size();
    }

    private int countNeighborhoods() {
        int c = 0;
        for (District d : districts) {
            c += d.getNeighborhoodCount();
        }
        return c;
    }

    private List<Neighborhood> allNeighborhoods() {
        List<Neighborhood> list = new ArrayList<>();
        for (District d : districts) {
            list.addAll(d.getNeighborhoods());
        }
        return list;
    }

    private static int nextMultipleAtLeast(int value, int divisor) {
        if (divisor <= 0) {
            return value;
        }
        int rem = value % divisor;
        if (rem == 0) {
            return value;
        }
        return value + (divisor - rem);
    }
    private boolean hasSplit = false;

    /** Smallest multiple of divisor that is at least value, capped at Integer.MAX_VALUE (then floored to a multiple). */
    private static int nextMultipleAtLeastCapped(long value, int divisor) {
        if (divisor <= 0 || value <= 0) {
            return 0;
        }
        long v = value;
        long rem = v % divisor;
        if (rem != 0) {
            v += divisor - rem;
        }
        if (v > Integer.MAX_VALUE) {
            v = Integer.MAX_VALUE;
            rem = v % divisor;
            if (rem != 0) {
                v -= rem;
            }
        }
        return (int) v;
    }
}
