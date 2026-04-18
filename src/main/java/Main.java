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

		System.out.println("Şehir Kodlarını Giriniz:");
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
		game.printCityDetails(row, col);
		System.out.println("\nÇıkmak için herhangi bir tuşa basınız...");
		scanner.nextLine(); 
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
