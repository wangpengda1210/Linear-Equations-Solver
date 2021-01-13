import java.util.Arrays;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // put your code here
        scanner.nextLine();
        int[] numbers = Arrays.stream(scanner.nextLine().split(" "))
                .mapToInt(Integer::parseInt)
                .toArray();

        int[] result = new int[numbers.length];
        result[0] = numbers[numbers.length - 1];
        System.arraycopy(numbers, 0, result, 1, numbers.length - 1);

        System.out.println(Arrays.toString(result)
                .replaceAll("[\\[\\],]", ""));
    }
}