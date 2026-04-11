import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import com.github.javafaker.Faker;
/**
 * Entry point: rounds, city codes, simulation, then grid cell selection.
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Faker faker = new Faker(Locale.ENGLISH);

        System.out.print("Number of rounds: ");
        int rounds = Integer.parseInt(scanner.nextLine().trim());

        System.out.println("Enter city codes (space-separated), e.g. 18 25 79 37 62 86 17 50");
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
        System.out.println("Grid: " + Game.citiesPerRow() + " cities per row, " + game.rowCount() + " row(s).");
        System.out.print("Row (1-based): ");
        int row = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Column (1-based): ");
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
