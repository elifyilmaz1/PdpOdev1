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


public class Neighbourhood {

	private final String name;
	private final List<Person> people;

	public Neighbourhood(String name) {
		this.name = name;
		this.people = new ArrayList<>();
	}

	public static Neighbourhood create(Faker faker) {
		return new Neighbourhood(faker.address().streetName() + " " + faker.address().secondaryAddress());
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
