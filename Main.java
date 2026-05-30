import java.util.Arrays;
import java.util.Random;

public class Main {

    // Anzahl der Wiederholungen pro Konfiguration zur Mittelwertbildung
    private static final int RUNS = 15; 

    public static void main(String[] args) {
        // Die geforderten Array-Größen laut Zettel
        int[] sizes = {10, 100, 1000, 10000, 100000, 1000000};
        Random random = new Random();

        System.out.println("==========================================================================");
        System.out.println("EVALUATION: INTROSORT PERFORMANCE BENCHMARK");
        System.out.println("==========================================================================\n");

        // Kurzes Warm-up, damit die JVM die Methoden optimiert
        System.out.print("Initialisiere Testumgebung (JIT-Warm-up)... ");
        for (int i = 0; i < 10; i++) {
            int[] dummy = new int[10000];
            IntroSort.introSort(dummy);
        }
        System.out.println("Bereit.\n");

        // Durchführung der drei geforderten Szenarien
        runBenchmarkScenario("ZUFÄLLIGE DATEN (RANDOM CASE)", "Random", sizes, random);
        runBenchmarkScenario("BEREITS SORTIERTE DATEN (BEST CASE)", "Sorted", sizes, random);
        runBenchmarkScenario("UMGEKEHRT SORTIERTE DATEN (WORST CASE)", "Reversed", sizes, random);
        
        runManualTest();
    }

    private static void runBenchmarkScenario(String title, String type, int[] sizes, Random random) {
        System.out.println("### " + title);
        System.out.println("--------------------------------------------------------------------------");
        System.out.printf("%-12s | %-15s | %-15s | %-15s\n", "Größe (N)", "Zeit (Ø ms)", "Swaps (Ø)", "Calls (Ø)");
        System.out.println("--------------------------------------------------------------------------");

        for (int size : sizes) {
            long totalTime = 0;
            long totalSwaps = 0;
            long totalCalls = 0;

            for (int r = 0; r < RUNS; r++) {
                // Basis-Daten generieren
                int[] baseArray = new int[size];
                if (type.equals("Random")) {
                    for (int i = 0; i < size; i++) baseArray[i] = random.nextInt();
                } else if (type.equals("Sorted")) {
                    for (int i = 0; i < size; i++) baseArray[i] = i;
                } else if (type.equals("Reversed")) {
                    for (int i = 0; i < size; i++) baseArray[i] = size - i;
                }

                // Für jeden Durchlauf die Zähler zurücksetzen
                IntroSort.resetCounters();

                // Messung starten
                long start = System.nanoTime();
                IntroSort.introSort(baseArray);
                long end = System.nanoTime();

                // Validierung: Hat der Algorithmus seinen Job richtig gemacht?
                if (!isSorted(baseArray)) {
                    System.err.println("CRITICAL ERROR: Array wurde nicht korrekt sortiert!");
                    return;
                }

                totalTime += (end - start);
                totalSwaps += IntroSort.getSwapCount();
                totalCalls += IntroSort.getCallCount();
            }

            // Durchschnittsberechnung (Mittelwert) für die statistische Absicherung
            double avgTimeMs = (totalTime / (double) RUNS) / 1_000_000.0;
            long avgSwaps = totalSwaps / RUNS;
            long avgCalls = totalCalls / RUNS;

            System.out.printf("%-12d | %-15.4f | %-15d | %-15d\n", size, avgTimeMs, avgSwaps, avgCalls);
        }
        System.out.println("--------------------------------------------------------------------------\n");
    }

    private static void runManualTest() {
        System.out.println("### MANUELLER FUNKTIONSTEST");
        System.out.println("--------------------------------------------------------------------------");
        int[] manuell = {1, 2, 3, 4, 5, 6, 8, 7, 10, 9};
        System.out.println("Eingabe:  " + Arrays.toString(manuell));
        IntroSort.introSort(manuell);
        System.out.println("Ergebnis: " + Arrays.toString(manuell) + " -> Korrekt? " + (isSorted(manuell) ? "JA" : "NEIN"));
        System.out.println("--------------------------------------------------------------------------\n");
    }

    private static boolean isSorted(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] > arr[i + 1]) return false;
        }
        return true;
    }
}