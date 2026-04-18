import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.javafaker.Faker;

public class City {

	private final int seedCode;
	private String name;
	private final List<District> districts;

	private City(int seedCode, String name, List<District> districts) {
		this.seedCode = seedCode;
		this.name = name;
		this.districts = districts;
	}

	private City(int seedCode, String name) {
		this(seedCode, name, new ArrayList<>());
	}

	
	public static City fromSeed(int code, Faker faker) {
	    int tens = code / 10;
	    int units = code % 10;
	    int districtCount = Math.max(1, tens);

	    int totalNeighborhoods;
	    boolean overflowed = false; // ← overflow flag'i

	    if (units == 0) {
	        totalNeighborhoods = districtCount;
	        overflowed = false; // units=0 durumu, overflow değil
	    } else {
	        int rem = units % districtCount;
	        if (rem == 0) {
	            totalNeighborhoods = units; // 62→6, 86→8 gibi zaten tam bölünüyor
	            overflowed = false;
	        } else {
	            int roundedUp = units + (districtCount - rem);
	            if (roundedUp <= 9) {
	                totalNeighborhoods = roundedUp; // 25→6, 37→9 gibi normal yuvarlama
	                overflowed = false;
	            } else {
	                totalNeighborhoods = districtCount; // 79→7 gibi taşma
	                overflowed = true;
	            }
	        }
	    }

	    // Nüfus: sadece gerçekten taştıysa floor, diğer tüm durumlarda ceiling formülü
	    int perHood;
	    if (overflowed) {
	        perHood = Math.max(1, code / totalNeighborhoods); // 79/7=11 → pop=77
	    } else {
	        perHood = (code + totalNeighborhoods) / totalNeighborhoods; // ceiling
	    }
	    int population = perHood * totalNeighborhoods;

	    String cityName = faker.address().city();
	    List<District> built = new ArrayList<>();
	    int neighborhoodsPerDistrict = totalNeighborhoods / districtCount;

	    for (int d = 0; d < districtCount; d++) {
	        District district = District.create(faker);
	        for (int n = 0; n < neighborhoodsPerDistrict; n++) {
	            Neighborhood hood = Neighborhood.create(faker);
	            for (int p = 0; p < perHood; p++) {
	                hood.addPerson(new Person(
	                    faker.name().fullName(),
	                    (int) faker.number().numberBetween(0, 51)
	                ));
	            }
	            district.addNeighborhood(hood);
	        }
	        built.add(district);
	    }
	    return new City(code, cityName, built);
	}
	public City splitIfNeeded(Faker faker) {
		if (getPopulation() < 1000) {
			return null;
		}

		int d = districts.size();
		int newCityDistrictCount = d / 2;
		int oldCityDistrictCount = d - newCityDistrictCount;

		if (newCityDistrictCount <= 0) {
			return null;
		}

		List<District> moving = new ArrayList<>();
		for (int i = oldCityDistrictCount; i < d; i++) {
			moving.add(districts.get(i));
		}

		districts.subList(oldCityDistrictCount, d).clear();

		City spawned = new City(seedCode, faker.address().city());
		spawned.districts.addAll(moving);
		return spawned;
	}

	public void applyPopulationGrowth(Faker faker) {
	    List<Neighborhood> flat = allNeighborhoods();
	    if (flat.isEmpty()) return;

	    int rate = growthRate();
	    if (rate == 0) rate = 1;   // sıfırsa her mahallede 1 kişi artar

	    for (Neighborhood hood : flat) {
	        int current = hood.getPeople().size();
	        int toAdd = current * rate - current;  // (rate-1) × current kadar ekle
	        for (int i = 0; i < toAdd; i++) {
	            hood.addPerson(new Person(
	                faker.name().fullName(),
	                (int) faker.number().numberBetween(0, 51)
	            ));
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
		int pop = getPopulation();
		int lastTwo = pop % 100;
		int tens = lastTwo / 10;
		int units = lastTwo % 10;
		return tens + units;
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
}