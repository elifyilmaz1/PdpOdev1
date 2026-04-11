import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.javafaker.Faker;

/**
 * A neighborhood contains people and is named via Faker.
 */
public class Neighborhood {

	private final String name;
	private final List<Person> people;

	public Neighborhood(String name) {
		this.name = name;
		this.people = new ArrayList<>();
	}

	public static Neighborhood create(Faker faker) {
		return new Neighborhood(faker.address().streetName() + " " + faker.address().secondaryAddress());
	}

	public String getName() {
		return name;
	}

	public List<Person> getPeople() {
		return Collections.unmodifiableList(people);
	}

	void addPerson(Person person) {
		people.add(person);
	}

	void addPeople(List<Person> toAdd) {
		people.addAll(toAdd);
	}

	public int getPopulation() {
		return people.size();
	}
}
