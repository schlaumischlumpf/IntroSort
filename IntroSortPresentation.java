public class IntroSort {
    // Statistik-Zähler für Benchmark-Auswertung
    private static long swapCount = 0;      // Zählt die Anzahl der Vertauschungen
    private static long callCount = 0;      // Zählt die Anzahl der Funktionsaufrufe
    
    // Schwellwert: Bei Segmenten kleiner/gleich 16 wird zu Insertionsort gewechselt
    // (Insertionsort ist bei kleinen Arrays schneller als Quicksort)
    private static final int SIZE_THRESHOLD = 16;

    // Einstiegspunkt: Introsort starten und am Ende kleine Segmente glätten
    public static void introSort(int[] arr) {
        if (arr.length <= 1) return;  // Trivial-Fall: leeres oder 1-Element-Array ist bereits sortiert
        
        callCount++;
        
        // Berechne maximale Rekursionstiefe basierend auf Array-Länge
        // Formel: 2 * log2(n) => verhindert O(n²) Verhalten von Quicksort
        // Bei zu tiefer Rekursion wird auf Heapsort umgestellt (O(n log n) garantiert)
        int maxDepth = 2 * (31 - Integer.numberOfLeadingZeros(arr.length));
        
        // Führe Quicksort/Heapsort-Hybrid aus
        introSortLoop(arr, 0, arr.length - 1, maxDepth);
        
        // Finale Fein-Sortierung: Insertionsort für alle Segmente <= SIZE_THRESHOLD
        // Dies ist effizienter als würde Quicksort bis zum Ende durchlaufen
        insertionSort(arr, 0, arr.length - 1);
    }

    private static void introSortLoop(int[] arr, int left, int right, int depthLimit) {
        // Hauptschleife: Solange Segment größer als Schwellwert (16), wende Quicksort/Heapsort an
        // Kleinere Segmente werden später durch abschließenden Insertionsort sortiert
        while (right - left + 1 > SIZE_THRESHOLD) {
            
            // === INTROSPECTION: Fallback auf Heapsort bei zu vielen Rekursionen ===
            if (depthLimit == 0) {
                // IntroSort-Kern: Rekursionstiefe überschritten => Worst-Case erkannt
                // Fallback auf Heapsort, um O(n²)-Verhalten zu vermeiden (garantiert O(n log n))
                heapSort(arr, left, right);
                return;  // Segment ist nun sortiert
            }
            depthLimit--;

            // === QUICKSORT-PHASE: Partition durchführen ===
            int pivotIdx = partition(arr, left, right);
            
            // === TAIL-CALL-OPTIMIERUNG: Rekursion minimieren ===
            // Sortiere zuerst die kleinere Seite rekursiv, dann die größere iterativ
            // Dies spart Stack-Speicher und verbessert die Performance
            if (pivotIdx - left < right - pivotIdx) {
                // Linke Seite ist kleiner => sortiere rekursiv
                introSortLoop(arr, left, pivotIdx - 1, depthLimit);
                // Rechte Seite wird in nächster Schleifeniterration verarbeitet (iterativ)
                left = pivotIdx + 1;
            } else {
                // Rechte Seite ist kleiner oder gleich => sortiere rekursiv
                introSortLoop(arr, pivotIdx + 1, right, depthLimit);
                // Linke Seite wird in nächster Schleifeniterration verarbeitet (iterativ)
                right = pivotIdx - 1;
            }
        }
        // Schleife endet, wenn Segment <= SIZE_THRESHOLD; wird dann durch Insertionsort behandelt
    }

    private static int partition(int[] arr, int left, int right) {
        callCount++;
        
        // === PIVOT-AUSWAHL: Median-of-three ===
        // Wähle Median aus (left, mid, right) als Pivot
        // Verhindert Worst-Case bei bereits sortierten oder umgekehrt sortierten Arrays
        int mid = left + ((right - left) >> 1);  // Mittlerer Index (sichere Berechnung)
        
        // Sorge dafür: arr[left] <= arr[mid] <= arr[right]
        if (arr[left] > arr[mid]) { int t = arr[left]; arr[left] = arr[mid]; arr[mid] = t; swapCount++; }
        if (arr[left] > arr[right]) { int t = arr[left]; arr[left] = arr[right]; arr[right] = t; swapCount++; }
        if (arr[mid] > arr[right]) { int t = arr[mid]; arr[mid] = arr[right]; arr[right] = t; swapCount++; }
        
        // Der Median (arr[mid]) ist unser Pivot-Element
        int pivot = arr[mid];
        // Verschiebe Pivot ans Ende (Platz right-1) zur leichteren Verarbeitung
        arr[mid] = arr[right - 1];
        arr[right - 1] = pivot;
        swapCount++;
        
        // === PARTITIONIERUNGSPHASE: Hoare-Partition ===
        // Zwei Zeiger i und j laufen aufeinander zu
        int i = left;          // Zeiger von links: sucht Element >= Pivot
        int j = right - 1;     // Zeiger von rechts: sucht Element <= Pivot
        
        while (true) {
            // Finde erstes Element von links, das >= Pivot
            while (arr[++i] < pivot);
            // Finde erstes Element von rechts, das <= Pivot
            while (arr[--j] > pivot);
            
            // Wenn Zeiger sich kreuzen, ist Partition fertig
            if (i >= j) break;
            
            // Tausche die beiden Elemente (falsch platziert)
            int t = arr[i];
            arr[i] = arr[j];
            arr[j] = t;
            swapCount++;
        }
        
        // === PIVOT-PLATZIERUNG: Bringe Pivot in endgültige Position ===
        // Pivot sitzt noch an right-1, muss aber an Position i
        arr[right - 1] = arr[i];
        arr[i] = pivot;
        swapCount++;
        
        return i;  // Rückgabe: Position des Pivots (alles links ist kleiner, alles rechts ist größer)
    }

    private static void insertionSort(int[] arr, int left, int right) {
        callCount++;
        
        // === INSERTIONSORT: Optimal für kleine Arrays ===
        // Bei kleinen Bereichen (<= SIZE_THRESHOLD) ist Insertionsort schneller als Quicksort
        // Grund: Geringerer Overhead, Cache-freundlich, einfache Implementierung
        
        for (int i = left + 1; i <= right; i++) {
            int key = arr[i];  // Aktuelles Element zum Einsortieren
            int j = i - 1;
            
            // Verschiebe alle größeren Elemente ein Feld nach rechts
            // bis zum Platz gefunden ist
            while (j >= left && arr[j] > key) {
                arr[j + 1] = arr[j];  // Nach rechts verschieben
                j--;
                swapCount++;  // Zähle als Swap
            }
            
            // Einfügen des Keys an die richtige Position
            arr[j + 1] = key;
        }
    }

    private static void heapSort(int[] arr, int left, int right) {
        callCount++;
        
        // === HEAPSORT: Fallback-Algorithmus bei zu vielen Quicksort-Rekursionen ===
        // Garantiert O(n log n) Laufzeit in allen Fällen (auch Worst-Case)
        // Wird nur aufgerufen, wenn IntroSort Worst-Case-Verhalten erkennt
        
        int n = right - left + 1;  // Größe des Teilbereichs
        
        // Phase 1: Heap aufbauen (Bottom-Up)
        // Mache alle Teilbäume zu Maxheaps
        for (int i = (n >> 1) - 1; i >= 0; i--) {
            heapify(arr, n, i, left);
        }
        
        // Phase 2: Heap abbauen und sortieren
        // Extrahiere nacheinander das Maximum (Wurzel) und platziere es am Ende
        for (int i = n - 1; i > 0; i--) {
            // Tausche größtes Element (Wurzel) nach hinten
            int t = arr[left];
            arr[left] = arr[left + i];
            arr[left + i] = t;
            swapCount++;
            
            // Stelle Heap-Eigenschaft wieder her (reduzierter Heap)
            heapify(arr, i, 0, left);
        }
    }

    private static void heapify(int[] arr, int n, int i, int start) {
        callCount++;
        
        // === HEAPIFY: Stelle Maxheap-Eigenschaft wieder her ===
        // Sortiert einen Teilbaum mit Wurzel an Index i
        // Maxheap-Eigenschaft: Eltern >= Kinder
        
        int largest = i;  // Annahme: i ist das größte Element
        while (true) {
            // Berechne Indizes der Kinder (Bitshift: << 1 = *2)
            int l = (largest << 1) + 1;      // Linkes Kind:  2*i + 1
            int r = (largest << 1) + 2;      // Rechtes Kind: 2*i + 2
            int current = largest;

            // Vergleiche mit linkem Kind: ist es größer?
            if (l < n && arr[start + l] > arr[start + largest]) largest = l;
            
            // Vergleiche mit rechtem Kind: ist es größer?
            if (r < n && arr[start + r] > arr[start + largest]) largest = r;

            // Wenn ein Kind größer war, tausche und rekursive Heapify für das Kind
            if (largest != current) {
                int t = arr[start + current];
                arr[start + current] = arr[start + largest];
                arr[start + largest] = t;
                swapCount++;
            } else {
                // Heap-Eigenschaft erfüllt
                break;
            }
        }
    }

    public static void resetCounters() { swapCount = 0; callCount = 0; }
    public static long getSwapCount() { return swapCount; }
    public static long getCallCount() { return callCount; }
}
