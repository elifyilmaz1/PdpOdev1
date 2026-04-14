import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import com.github.javafaker.Faker;

public class Main {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		Faker faker = new Faker(Locale.ENGLISH);

		System.out.print("Tur Sayısını Giriniz: ");
		int rounds = Integer.parseInt(scanner.nextLine().trim());

		System.out.println("Şehir Kodlarını Giriniz: (örn. 18 25 79 37 62 86 17 50)");
		String line = scanner.nextLine().trim();
		String[] parts = line.split("\\s+");
		List<City> cities = new ArrayList<>();
		for (String part : parts) {
			int code = Integer.parseInt(part);
			cities.add(City.fromSeed(code, faker));
		}

		clearConsole();

		Game game = new Game(cities, rounds);
		game.play();
		System.out.print("Satır Giriniz: ");
		int row = Integer.parseInt(scanner.nextLine().trim());
		System.out.print("Sütun Giriniz: ");
		int col = Integer.parseInt(scanner.nextLine().trim());

		clearConsole();
		game.printCityDetails(row - 1, col - 1);
		scanner.close();
	}

	private static void clearConsole() {
		try {
			String os = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
			if (os.contains("win")) {
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			} else {
				System.out.print("\033[2J\033[H");
				System.out.flush();
			}
		} catch (Exception e) {
			for (int i = 0; i < 50; i++) {
				System.out.println();
			}
		}
	}
}
