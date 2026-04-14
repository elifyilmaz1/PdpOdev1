import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.github.javafaker.Faker;

public class Game {

    private static final int CITIES_PER_ROW = 5;

    private final Faker faker;
    private final List<City> cities;
    private final int rounds;

    public Game(List<City> cities, int rounds) {
        this.cities = new ArrayList<>(cities);
        this.rounds = rounds;
        this.faker = new Faker(Locale.ENGLISH);
    }

    public void play() {
        System.out.println("--- BAŞLANGIÇ DURUMU ---");
        printGrid();
        System.out.println();

        for (int r = 1; r <= rounds; r++) {
            runSingleRound(r);
            clearConsole();
            System.out.println( + r + ". TURUN SONU");
            printGrid();
        }
    }

    private void runSingleRound(int round) {
        for (City city : cities) {
            int before = city.getPopulation();
            city.applyPopulationGrowth(faker);
            int after = city.getPopulation();
            int added = after - before;
        }

        for (City city : cities) {
            city.incrementAllAges();
        }

        List<City> spawned = new ArrayList<>();
        for (City city : new ArrayList<>(cities)) {
            int popBefore = city.getPopulation();
            City child = city.splitIfNeeded(faker);
            
        }
        cities.addAll(spawned);
    }

    private void clearConsole() {
        try {
            String os = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH);
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[2J\033[H");
                System.out.flush();
            }
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) System.out.println();
        }
    }

    private void printGrid() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cities.size(); i++) {
            if (i > 0) {
                sb.append(i % CITIES_PER_ROW == 0 ? System.lineSeparator() : "-");
            }
            sb.append('[').append(cities.get(i).getPopulation()).append(']');
        }
        System.out.println(sb);
    }

    public void printCityDetails(int row, int col) {
        int idx = row * CITIES_PER_ROW + col;
        if (idx < 0 || idx >= cities.size()) {
            System.out.println("Geçersiz seçim: satır " + (row + 1) + ", sütun " + (col + 1) + ".");
            return;
        }
        City city = cities.get(idx);
        System.out.println("Şehir: " + city.getName() + " - Nüfus: " + city.getPopulation());
        System.out.println();
        for (District district : city.getDistricts()) {
            System.out.println("İlçe: " + district.getName() + " - Nüfus: " + district.getPopulation());
            for (Neighborhood hood : district.getNeighborhoods()) {
                System.out.println("  Mahalle: " + hood.getName() + " - Nüfus: " + hood.getPopulation());
                System.out.println("  Kişiler:");
                for (Person p : hood.getPeople()) {
                    System.out.println("    " + p.getId() + " - " + p.getName() + " - " + p.getAge());
                }
            }
            System.out.println();
        }
    }

    public int getCityCount() {
        return cities.size();
    }

    public int rowCount() {
        return (cities.size() + CITIES_PER_ROW - 1) / CITIES_PER_ROW;
    }

    public static int citiesPerRow() {
        return CITIES_PER_ROW;
    }
}