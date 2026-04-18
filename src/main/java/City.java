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

	    int totalNeighbourhoods;
	    boolean overflowed = false; 

	    if (units == 0) {
	        totalNeighbourhoods = districtCount;
	        overflowed = false; 
	    } else {
	        int rem = units % districtCount;
	        if (rem == 0) {
	            totalNeighbourhoods = units;
	            overflowed = false;
	        } else {
	            int roundedUp = units + (districtCount - rem);
	            if (roundedUp <= 9) {
	                totalNeighbourhoods = roundedUp; 
	                overflowed = false;
	            } else {
	                totalNeighbourhoods = districtCount; 
	                overflowed = true;
	            }
	        }
	    }

	    int perHood;
	    if (overflowed) {
	        perHood = Math.max(1, code / totalNeighbourhoods); 
	    } else {
	        perHood = (code + totalNeighbourhoods) / totalNeighbourhoods; 
	    }

	    String cityName = faker.address().city();
	    List<District> built = new ArrayList<>();
	    int neighbourhoodsPerDistrict = totalNeighbourhoods / districtCount;

	    for (int d = 0; d < districtCount; d++) {
	        District district = District.create(faker);
	        for (int n = 0; n < neighbourhoodsPerDistrict; n++) {
	        	Neighbourhood hood = Neighbourhood.create(faker);
	            for (int p = 0; p < perHood; p++) {
	                hood.addPerson(new Person(
	                    faker.name().fullName(),
	                    (int) faker.number().numberBetween(0, 51)
	                ));
	            }
	            district.addNeighbourhood(hood);
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
	    List<Neighbourhood> flat = allNeighbourhoods();
	    if (flat.isEmpty()) return;

	    int rate = growthRate();
	    if (rate == 0) rate = 1;  

	    for (Neighbourhood hood : flat) {
	        int current = hood.getPeople().size();
	        int toAdd = current * rate - current;  
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
			for (Neighbourhood hood : district.getNeighbourhoods()) {
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

	private List<Neighbourhood> allNeighbourhoods() {
		List<Neighbourhood> list = new ArrayList<>();
		for (District d : districts) {
			list.addAll(d.getNeighbourhoods());
		}
		return list;
	}
}