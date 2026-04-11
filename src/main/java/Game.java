import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.github.javafaker.Faker;
/**
 * Runs rounds, prints the population grid, and resolves city selection for detailed view.
 */
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
        // Print initial structure derivation for each city
        for (City city : cities) {
            int code = city.getSeedCode();
            int tens = code / 10;
            int units = code % 10;
            System.out.println("City '" + city.getName() + "' seed=" + code
                + " → districts=" + city.getDistrictCount()
                + " (tens=" + tens + ")"
                + " neighborhoods-per-district=" + (units == 0 ? 1 : units)
                + " growthRate=" + city.growthRate()
                + " initialPop=" + city.getPopulation());
        }
        System.out.println();

        for (int r = 1; r <= rounds; r++) {
            runSingleRound(r);
            clearConsole();
            System.out.println("Round " + r + " of " + rounds);
            printGrid();
        }
    }
    private void runSingleRound(int round) {
        for (City city : cities) {
            int before = city.getPopulation();
            city.applyPopulationGrowth(faker);
            int after = city.getPopulation();
            int added = after - before;
            System.out.println("  [Growth] " + city.getName()
                + " seed=" + city.getSeedCode()
                + " rate=" + city.growthRate()
                + " pop: " + before + " → " + after
                + " (+" + added + " people across "
                + city.getDistrictCount() + " districts)");
        }

        for (City city : cities) {
            city.incrementAllAges();
        }
        System.out.println("  [Age] All people aged +1 in round " + round);

        List<City> spawned = new ArrayList<>();
        for (City city : new ArrayList<>(cities)) {
            int popBefore = city.getPopulation();
            City child = city.splitIfNeeded(faker);
            if (child != null) {
                System.out.println("  [Split] '" + city.getName()
                    + "' pop=" + popBefore
                    + " → split into '" + city.getName()
                    + "' (" + city.getDistrictCount() + " districts)"
                    + " + '" + child.getName()
                    + "' (" + child.getDistrictCount() + " districts)");
                spawned.add(child);
            }
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

    private void runSingleRound() {
        for (City city : cities) {
            int before = city.getPopulation();
            city.applyPopulationGrowth(faker);
            // Shows: CityName: seed=18 rate=9 before=72 after=81
            System.out.println(city.getName() + ": seed=" + city.getSeedCode() 
                + " rate=" + city.growthRate() 
                + " before=" + before 
                + " after=" + city.getPopulation());
        }
        for (City city : cities) {
            city.incrementAllAges();
        }
        List<City> spawned = new ArrayList<>();
        for (City city : new ArrayList<>(cities)) {
            City child = city.splitIfNeeded(faker);
            if (child != null) {
                spawned.add(child);
            }
        }
        cities.addAll(spawned);
    }


    public void printCityDetails(int row, int col) {
        int idx = row * CITIES_PER_ROW + col;
        if (idx < 0 || idx >= cities.size()) {
            System.out.println("No city at row " + (row + 1) + ", column " + (col + 1) + " (1-based).");
            return;
        }
        City city = cities.get(idx);
        System.out.println("City: " + city.getName() + " (seed " + city.getSeedCode() + ")");
        int dNum = 1;
        for (District district : city.getDistricts()) {
            System.out.println("  District " + dNum + ": " + district.getName());
            int nNum = 1;
            for (Neighborhood hood : district.getNeighborhoods()) {
                System.out.println("    Neighborhood " + nNum + ": " + hood.getName());
                for (Person p : hood.getPeople()) {
                    System.out.println("      " + p.getId() + ", " + p.getName() + ", " + p.getAge());
                }
                nNum++;
            }
            dNum++;
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
