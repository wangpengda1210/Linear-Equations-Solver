package solver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        if (args.length != 4 || !"-in".equals(args[0]) || !"-out".equals(args[2])) {
            System.out.println("Argument error");
            return;
        }

        Complex[][] matrix = readMatrix(args[1]);

        System.out.println("Start solving the equation.\nRows manipulation:");
        Complex[] result = solveMatrix(matrix);
        if (result.length == 0) {
            System.out.println("No solutions");
        } else if (result[0].getReal() == Double.POSITIVE_INFINITY) {
            System.out.println("Infinitely many solutions");
        } else {
            System.out.println("The solution is: " +
                    Arrays.toString(result).replace("[", "(")
                            .replace("]", ")"));
        }

        writeResultToFile(result, args[3]);
        System.out.println("Saved to file " + args[3]);
    }

    private static Complex[] solveMatrix(Complex[][] matrix) {
        int numRows = matrix.length;
        int numCols = matrix[0].length;

        for (int i = 0; i < numRows; i++) {
            if (matrix[i][i].isZero()) {
                for (int k = i + 1; k < numRows; k++) {
                    if (!matrix[k][i].isZero()) {
                        exchangeRows(matrix, i, k);
                        break;
                    }
                }
            }

            if (matrix[i][i].isZero()) {
                for (int k = i + 1; k < numCols - 1; k++) {
                    if (!matrix[i][k].isZero()) {
                        exchangeColumns(matrix, i, k);
                        break;
                    }
                }
            }

            if (matrix[i][i].isZero()) {
                theLoop:for (int k = i + 1; k < numRows; k++) {
                    for (int l = i + 1; l < numCols - 1; l++) {
                        if (!matrix[k][l].isZero()) {
                            exchangeRows(matrix, i, k);
                            exchangeColumns(matrix, i, l);
                            break theLoop;
                        }
                    }
                }
            }

            if (matrix[i][i].isZero()) {
                break;
            }

            scaleRow(matrix, i);
            for (int j = i + 1; j < numRows; j++) {
                addRow(matrix, i, j, matrix[j][i].negate());
            }
        }

        if (hasNoSolution(matrix)) {
            return new Complex[0];
        }

        int significantRows = 0;
        for (int i = 0; i < numRows; i++) {
            if (matrix[i][i].getReal() == 1 && matrix[i][i].getImagery() == 0) {
                significantRows++;
            }
        }

        if (significantRows < numCols - 1) {
            return new Complex[] {new Complex(Double.POSITIVE_INFINITY, 0)};
        }

        for (int i = 1; i < numRows; i++) {
            for (int j = i - 1; j >= 0; j--) {
                addRow(matrix, i, j, matrix[j][i].negate());
            }
        }

        Complex[] result = new Complex[significantRows];
        for (int i = 0; i < result.length; i++) {
            result[i] = matrix[i][numCols - 1];
        }

        return result;
    }

    private static boolean hasNoSolution(Complex[][] matrix) {
        rowLoop:for (Complex[] row : matrix) {
            for (int i = 0; i < row.length - 1; i++) {
                if (!row[i].isZero()) {
                    continue rowLoop;
                }
            }
            if (!row[row.length - 1].isZero()) {
                return true;
            }
        }
        return false;
    }

    private static void scaleRow(Complex[][] matrix, int rowNumber) {
        if (rowNumber >= matrix.length) {
            return;
        }

        Complex scale = matrix[rowNumber][rowNumber];

        System.out.println("R" + rowNumber + "/" + scale + " -> R" + rowNumber);
        for (int i = 0; i < matrix[0].length; i++) {
            matrix[rowNumber][i] = matrix[rowNumber][i].divide(scale);
            if (matrix[rowNumber][i].isZero()) {
                matrix[rowNumber][i] = new Complex(Math
                        .abs(matrix[rowNumber][i].getReal()),
                        Math.abs(matrix[rowNumber][i].getImagery()));
            }
        }
    }

    private static void addRow(Complex[][] matrix, int from, int to, Complex scale) {
        if (from >= matrix.length || to >= matrix.length) {
            return;
        }

        System.out.println(scale + " * R" + from + " + R" + to + " -> R" + to);
        for (int i = 0; i < matrix[0].length; i++) {
            matrix[to][i] = matrix[to][i].add(matrix[from][i].multiply(scale));
        }
    }

    private static void exchangeRows(Complex[][] matrix, int first, int second) {
        if (first > matrix.length || second > matrix.length) {
            return;
        }

        System.out.println("R" + first + " <-> R" + second);
        for (int i = 0; i < matrix[0].length; i++) {
            Complex temp = matrix[first][i];
            matrix[first][i] = matrix[second][i];
            matrix[second][i] = temp;
        }
    }

    private static void exchangeColumns(Complex[][] matrix, int first, int second) {
        if (first > matrix[0].length || second > matrix[0].length) {
            return;
        }

        System.out.println("C" + first + " <-> C" + second);
        for (int i = 0; i < matrix.length; i++) {
            Complex temp = matrix[i][first];
            matrix[i][first] = matrix[i][second];
            matrix[i][second] = temp;
        }
    }

    private static Complex[][] readMatrix(String path) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(path));

        int cols = scanner.nextInt();
        int rows = scanner.nextInt();

        Complex[][] matrix = new Complex[rows][cols + 1];

        for (int i = 0; i < rows; i++) {
            Complex[] row = new Complex[cols + 1];
            for (int j = 0; j < cols + 1; j++) {
                String entry = scanner.next();

                if (entry.equals("i")) {
                    row[j] = new Complex(0, 1);
                    continue;
                } else if (entry.equals("-i")) {
                    row[j] = new Complex(0, -1);
                    continue;
                }

                if (!entry.contains("i")) {
                    row[j] = new Complex(Double.parseDouble(entry), 0);
                } else {
                    int operatorPosition = 0;
                    boolean isMinus = false;
                    if (entry.contains("+")) {
                        operatorPosition = entry.indexOf("+");
                    } else if (entry.contains("-")) {
                        operatorPosition = entry.indexOf("-");
                        if (operatorPosition == 0) {
                            operatorPosition = entry.indexOf("-", 1);
                        }
                        if (operatorPosition != -1) {
                            isMinus = true;
                        }
                    } else {
                        operatorPosition = -1;
                    }

                    if (operatorPosition == -1) {
                        row[j] = new Complex(0, Double.parseDouble(
                                entry.substring(0, entry.length() - 1)));
                    } else {
                        String substring = entry.substring(operatorPosition + 1,
                                entry.length() - 1);
                        double aDouble;
                        if (substring.isEmpty()) {
                            aDouble = 1;
                        } else {
                            aDouble = Double.parseDouble(substring);
                        }

                        if (isMinus) {
                            row[j] = new Complex(Double.parseDouble(entry
                                    .substring(0, operatorPosition)), -aDouble);
                        } else {
                            row[j] = new Complex(Double.parseDouble(entry
                                    .substring(0, operatorPosition)), aDouble);
                        }
                    }
                }
            }
            matrix[i] = row;
        }

        scanner.reset();
        scanner.close();
        return matrix;
    }

    private static void writeResultToFile(Complex[] result, String path) throws FileNotFoundException {
        PrintWriter printWriter = new PrintWriter(path);

        if (result.length == 0) {
            printWriter.println("No solutions");
        } else if (result[0].getReal() == Double.POSITIVE_INFINITY) {
            printWriter.println("Infinitely many solutions");
        } else {
            for (Complex solution : result) {
                printWriter.println(solution);
            }
        }

        printWriter.flush();
        printWriter.close();
    }

}
