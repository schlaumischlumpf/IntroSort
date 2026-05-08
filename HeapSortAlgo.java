import java.util.Arrays;

public class HeapSortAlgo {

    //  heapify-Methode 
    static void heapify(int[] arr, int n, int i) {
        // Größter Wert als Startpunkt wählen
        int largest = i;

        // linker Index = 2 * i + 1
        int l = 2 * i + 1;

        // rechter Index = 2 * i + 2
        int r = 2 * i + 2;

        if (l < n && arr[l] > arr[largest])
            largest = l;

        if (r < n && arr[r] > arr[largest])
            largest = r;

        if (largest != i){
            int temp = arr[i];
            arr[i] = arr[largest];
            arr[largest] = temp;

            heapify(arr, n, largest);
        }
    }

    static void heapsort(int[] arr) {
        int n = arr.length;

        for (int i = n / 2 - 1; i >= 0; i--)
            heapify(arr, n, i);

        for (int i = n - 1; i > 0; i--) {
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;

            heapify(arr, i, 0);
        } 
    }

    public static void main(String[] args) {
        int[] arr = { 18, -27, 0, 14, 42, 33, -7 };

        heapsort(arr);

        for (int i = 0; i < arr.length; ++i)
            System.out.print(arr[i] + " ");
    }
}