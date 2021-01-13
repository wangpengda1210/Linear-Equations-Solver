import java.util.Scanner;
import java.util.function.BiFunction;
import java.util.function.IntBinaryOperator;
import java.util.function.ToIntBiFunction;
import java.util.stream.*;

class CustomReducer {

    // Don't change the code below
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String[] values = scanner.nextLine().split(" ");

        int l = Integer.parseInt(values[0]);
        int r = Integer.parseInt(values[1]);

        int sumReducer = 0;
        for (int i = l; i <= r; i++) {
            sumReducer += i;
        }
        int sum = sumReducer;

        int prodReducer = 1;
        for (int i = l; i <= r; i++) {
            prodReducer *= i;
        }
        int prod = prodReducer;

        System.out.println(String.format("%d %d %d %d", sumReducer, sum, prodReducer, prod));
    }
}