import java.util.Random;

public class Quicksort {

    public static void main(String[] args) {
        Random rand = new Random();
        int[] zahlen = new int[10];

        for (int i = 0; i < zahlen.length; i++) {
            zahlen[i] = rand.nextInt(100);
        }

        System.out.println ("unsortiert");
        printArray(zahlen);

        quicksort(zahlen, 0, zahlen.length - 1);

        System.out.println ("\nsortiert:");
        printArray(zahlen);
    }

    public static void quicksort(int[] array, int lowIndex, int highIndex) {

        if (lowIndex >= highIndex) {
            return;
        }

        int pivot = array[highIndex];

        int lp = lowIndex;
        int rp = highIndex;

        while (lp < rp) {

            while (array[lp] <= pivot && lp < rp) {
                lp++;
            }

            while (array[rp] >= pivot && lp < rp) {
                rp--;
            }

            swap(array, lp, rp);
        }

        swap(array, lp, highIndex);

        quicksort(array, lowIndex, lp - 1);
        quicksort(array, lp + 1, highIndex);
    }

    private static void swap(int[] array, int index1, int index2) {

        int temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }


    private static void printArray(int[] zahlen) {
    for (int i = 0; i < zahlen.length; i++) {
        System.out.println(zahlen[i]);
    }
}
}

