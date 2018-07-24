import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Knapsack {
    //set private fields here
    private int[] weights;
    private int[] benefits;
    private int number_of_items;
    private int weight_capacity;

    public Knapsack(int W, int[] w, int[] b) {
        //constructor
        this.weight_capacity = W;
        this.number_of_items = w.length - 1;
        this.weights = w;
        this.benefits = b;
        printInputValues();
    }

    public static int[] generateSubset(int k, int n) {
        //  0 <= k <= 2n - 1
        //  Generates the kth subset of { 0,1,..., n-1 }
        //  in the form of the binary representation of k
        if (0 <= k && k <= ((int) Math.pow(2.0, (double) n)) - 1) {
            int subset[] = new int[n];
            int j = 0;

            for (int i = n - 1; i >= 0; i--) {
                j = k % 2;
                k = k / 2;
                subset[i] = j;
            }
            return subset;
        } else {
            return null;
        }
    }

    public void BruteForceSolution() {
        //Prints all optimal solutions to the 0-1 knapsack problem
        //using brute force algorithm described in Project 2
        //Print solution in format specified in Project 2
        int[] result;
        int length = weights.length;
        int weightLimit = weight_capacity;
        int max_benefit_curr = -1;
        int set_weight = 0;
        int set_benefit = 0;
        int k = 0;
        ArrayList<int[]> optimal_sets = new ArrayList<int[]>();

        while ((result = generateSubset(k++, length)) != null) {
            set_weight = 0;
            set_benefit = 0;

            for (int i = 0; i < length; i++) {
                if (result[i] != 0) {
                    set_weight += weights[i];
                    set_benefit += benefits[i];
                }
            }

            if (set_weight <= weightLimit) {
                if (set_benefit > max_benefit_curr) {
                    max_benefit_curr = set_benefit;
                    optimal_sets.clear();
                    optimal_sets.add(result);
                }
                else if (set_benefit == max_benefit_curr) {
                    optimal_sets.add(result);
                }
            }
        }
        optimal_sets.forEach((int[] feasible_set) -> printSubset(feasible_set));
    }

    public void DynamicProgrammingSolution(boolean printBmatrix) {
        //Prints one optimal solutions to the 0-1 knapsack problem
        // using dynamic programming algorithm described in Project 2
        // Print solution in format specified in Project 2
        // If printmatrix is true, print the OPT matrix.
        int weightLimit = weight_capacity;
        int items = weights.length;
        int[][] bMatrix = new int[items][weightLimit + 1];
        int weight_curr = 0;
        int benefit_curr = 0;
        int prev = 0;
        int temp = 0;

        for(int i = 1; i < items; i++){
            weight_curr = weights[i];
            benefit_curr = benefits[i];
            for(int j = weightLimit; j >= 0; j--) {
                if (weight_curr > j) {
                    bMatrix[i][j] = bMatrix[i-1][j];
                }
                else {
                    prev = bMatrix[i-1][j];
                    temp = bMatrix[i-1][j - weight_curr] + benefit_curr;
                    bMatrix[i][j] = temp > prev ? temp : prev;
                }
            }
        }
        printDynamic(bMatrix);
        if (printBmatrix) {
            printBMatrix(bMatrix);
        }
    }

    public void GreedyApproximateSolution() {
        //Prints one approximate solution to the 0-1 knapsack problem using a variation of
        // used to solve the Fractional Knapsack Problem.
        int weight_Capacity = weight_capacity;
        int capacity_curr = 0;
        int benefit_curr = 0;
        int items = weights.length;
        WBRate comparator = new WBRate();
        PriorityQueue<Items> q = new PriorityQueue<Items>(items, comparator);
        PriorityQueue<Integer> set_q = new PriorityQueue<Integer>(items);

        for(int i = 1; i < items; i++){
            Items wb = new Items(weights[i], benefits[i], i);
            q.add(wb);
        }
        Items wb;
        while ((wb = q.poll()) != null ) {
            if (capacity_curr + wb.weight <= weight_Capacity ) {
                capacity_curr += wb.weight;
                benefit_curr += wb.benefit;
                set_q.offer(wb.key);
            }
        }
        System.out.print("Optimal set = { ");
        while(set_q.size() > 0){
            System.out.print(set_q.poll() + ", ");
        }
        System.out.println("} weight sum = " + capacity_curr + " benefit sum = " + benefit_curr);
    }

    private class Items {
        private int key;
        private int weight;
        private int benefit;
        private double wb_rate;

        public Items (int w, int b, int k) {
            this.key = k;
            this.weight = w;
            this.benefit = b;
            this.wb_rate = benefit/(double)weight;
        }
    }
    private class WBRate implements Comparator<Items> {
        public int compare(Items wb1, Items wb2){
            if(wb2.wb_rate < wb1.wb_rate){
                return -1;
            }
            else if(wb2.wb_rate == wb1.wb_rate){
                return 0;
            }
            else {
                return 1;
            }
        }
    }
    private void printDynamic(int[][] bMatrix) {
        int i = bMatrix.length - 1;
        int j = bMatrix[0].length - 1;
        int sum = 0, benefit = bMatrix[i][j];
        PriorityQueue<Integer> q = new PriorityQueue<Integer>();

        while (i > 0 && j > 0) {
            if (bMatrix[i][j] != bMatrix[i-1][j]) {
                q.add(i);
                j = j - weights[i];
                sum += weights[i];
                i--;
            }
            else {
                i--;
            }
        }
        System.out.print("Optimal sets= { ");
        while (q.size() > 0) {
            System.out.print(q.poll() + " ");
        }
        System.out.println("} weight sum = " + sum + " benefit sum = " + benefit);
    }

    private void printInputValues() {
        System.out.println("Number of Items = " + number_of_items + " Knapsack Capacity = "
                            + weight_capacity);
        System.out.println("Input weights: " + Arrays.toString(weights));
        System.out.println("Input benefits: " + Arrays.toString(benefits));
    }

    private void printBMatrix(int[][] bMatrix) {
        int rows = bMatrix.length;
        int columns = bMatrix[0].length;

        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                System.out.print(bMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    private void printSubset(int[] set) {
        int length = weights.length;
        int total = 0;
        int weight = 0;

        System.out.print("Optimal Set= { ");
        for (int i = 1; i < length; i++) {
            if (set[i] == 1) {
                total += benefits[i];
                weight += weights[i];
                System.out.print(i + " ");
            }
        }
        System.out.println("} Weight sum = " + weight + " benefit sum = " + total);
    }
}
