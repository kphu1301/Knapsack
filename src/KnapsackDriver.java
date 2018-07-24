import java.util.*;

public class KnapsackDriver
{
    public static void main( String[] args)
    {
        int n = 16;
        int[] weights = {-1, 2, 5, 3, 8, 10, 20, 8, 25, 20, 22, 15, 3, 9, 5, 20, 5, 7, 5 , 4, 2, 5, 7, 12, 25, 3, 2, 6, 4, 15, 20, 30, 5,14, 25, 10, 24, 18, 12, 7, 5, 7, 18, 15, 20, 2, 20, 15, 18, 6, 12};
        int W = 300;
        int[] benefits = {-1, 4, 7, 9, 12, 15, 22, 30, 35, 29, 40, 33, 5, 13, 8, 27, 12, 8, 19 , 40, 5, 15, 17, 27, 29, 6, 8, 10, 6, 17, 22, 35, 40, 35, 40, 55, 48, 18, 36, 42, 38, 12, 55, 42, 55, 30, 43, 55, 55, 30, 72};

        // Print input values as required in Project 2

        System.out.println("\nBrute Force Solution");
        Knapsack kp1 = new Knapsack(W, weights, benefits);
        kp1.BruteForceSolution();

        System.out.println("\nDynamic Programming Solution");
        Knapsack kp3 = new Knapsack(W,weights, benefits);
        kp3.DynamicProgrammingSolution(false);

        System.out.println("\nGreedy Approximate Solution");
        Knapsack kp4 = new Knapsack(W, weights, benefits);
        kp4.GreedyApproximateSolution();

    }
}
