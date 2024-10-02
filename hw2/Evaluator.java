import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Arrays;

class OperationType {
    public static final int INSERT_AT_MOST_VALUABLE_VALLEY = 1;
    public static final int INSERT_AT_LEAST_VALUABLE_VALLEY = 2;
    public static final int REMOVE_MOST_VALUABLE_VALLEY = 3;
    public static final int REMOVE_LEAST_VALUABLE_VALLEY = 4;
    public static final int GET_MOST_VALUABLE_VALLEY = 5;
    public static final int GET_LEAST_VALUABLE_VALLEY = 6;
    public static final int GET_VALLEY_COUNT = 7;
    public static final int IS_EMPTY = 8;
}

class Operation {
    int type;
    Integer height;
    Integer value;
    Integer depth;

    Operation(int type, Integer height, Integer value, Integer depth) {
        this.type = type;
        this.height = height;
        this.value = value;
        this.depth = depth;
    }

    public String toString() {
        switch (type) {
            case 1:
                return "Op:[insertAtMostValuableValley " + height + " " + value + " " + depth + "]";
            case 2:
                return "Op:[insertAtLeastValuableValley " + height + " " + value + " " + depth + "]";
            case 3:
                return "Op:[removeMostValuableValley " + depth + "]";
            case 4:
                return "Op:[removeLeastValuableValley " + depth + "]";
            case 5:
                return "Op:[getMostValuableValley " + depth + "]";
            case 6:
                return "Op:[getLeastValuableValley " + depth + "]";
            case 7:
                return "Op:[getValleyCount " + depth + "]";
            case 8:
                return "Op:[isEmpty]";
            default:
                return "Op:[Invalid]";
        }
    }
}

class TestCase {
    int[] heights;
    int[] values;
    Operation[] operations;
    Output[] expected;

    private Output readOutput(Scanner scanner) {
        String token = scanner.next();
        if (token.equals("n")) {
            return new Output();
        } else if (token.equals("t")) {
            return new Output(true);
        } else if (token.equals("f")) {
            return new Output(false);
        } else if (token.startsWith("(")) {
            if (!token.endsWith(")")) {
                throw new IllegalArgumentException("Invalid output format: " + token);
            }
            String[] parts = token.substring(1, token.length() - 1).split(",");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid output format: " + token);
            }
            return new Output(new IntPair(Integer.parseInt(parts[0].trim()), Integer.parseInt(parts[1].trim())));
        } else {
            return new Output(Integer.parseInt(token));
        }
    }

    int[] readIntArray(Scanner scanner, int N) {
        int[] arr = new int[N];
        for (int i = 0; i < N; i++) {
            arr[i] = scanner.nextInt();
        }
        return arr;
    }

    Operation readOperation(Scanner scanner) {
        int type = scanner.nextInt();
        Integer height = null, value = null, depth = null;
        switch (type) {
            case 1:
            case 2:
                height = scanner.nextInt();
                value = scanner.nextInt();
                depth = scanner.nextInt();
                break;
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                depth = scanner.nextInt();
                break;
        }
        return new Operation(type, height, value, depth);
    }

    TestCase(String filepath) {
        try (Scanner scanner = new Scanner(new File(filepath))) {
            int N = scanner.nextInt();
            heights = readIntArray(scanner, N);
            values = readIntArray(scanner, N);
            int M = scanner.nextInt();
            operations = new Operation[M];
            for (int i = 0; i < M; i++) {
                operations[i] = readOperation(scanner);
            }
            int K = scanner.nextInt();
            expected = new Output[K];
            for (int i = 0; i < K; i++) {
                expected[i] = readOutput(scanner);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Testcase file not found: " + filepath);
            throw new RuntimeException(e);
        }
    }

    public String toString() {
        String result = "Heights[" + heights.length + "]:{" + Arrays.toString(heights) + "}\n";
        result += "Values[" + values.length + "]:{" + Arrays.toString(values) + "}\n";
        result += "Operations[" + operations.length + "]:{\n";
        for (Operation op : operations) {
            result += "  " + op.toString() + "\n";
        }
        result += "}\n";
        result += "Expected[" + expected.length + "]:{\n";
        for (Output output : expected) {
            result += "  " + output.toString() + "\n";
        }
        result += "}\n";
        return result;
    }
}

class Output {
    final IntPair pair;
    final Boolean success;
    final Integer count;

    Output() {
        this.pair = null;
        this.success = null;
        this.count = null;
    }

    Output(IntPair pair) {
        this.pair = pair;
        this.success = null;
        this.count = null;
    }

    Output(Boolean success) {
        this.pair = null;
        this.success = success;
        this.count = null;
    }

    Output(Integer count) {
        this.pair = null;
        this.success = null;
        this.count = count;
    }

    boolean isNull() {
        return pair == null && success == null && count == null;
    }

    boolean isPair() {
        return pair != null;
    }

    boolean isBoolean() {
        return success != null;
    }

    boolean isInteger() {
        return count != null;
    }

    public boolean equals(Output other) {
        if (this.isNull() && other.isNull())
            return true;
        if (this.isNull() || other.isNull())
            return false;
        if (this.isPair() && other.isPair())
            return this.pair.equals(other.pair);
        if (this.isBoolean() && other.isBoolean())
            return this.success.equals(other.success);
        if (this.isInteger() && other.isInteger())
            return this.count.equals(other.count);
        return false;
    }

    public int hashCode() {
        if (isNull())
            return 0;
        if (isPair())
            return pair.hashCode();
        if (isBoolean())
            return success.hashCode();
        return count.hashCode();
    }

    public String toString() {
        if (isNull()) {
            return "null";
        } else if (success != null) {
            return success.toString();
        } else if (count != null) {
            return count.toString();
        }
        return pair.toString();
    }
}

public class Evaluator {

    private TreasureValleyExplorer explorer;

    public Output operate(Operation op) {
        switch (op.type) {
            case 1:
                return new Output(explorer.insertAtMostValuableValley(op.height, op.value, op.depth));
            case 2:
                return new Output(explorer.insertAtLeastValuableValley(op.height, op.value, op.depth));
            case 3:
                return new Output(explorer.removeMostValuableValley(op.depth));
            case 4:
                return new Output(explorer.removeLeastValuableValley(op.depth));
            case 5:
                return new Output(explorer.getMostValuableValley(op.depth));
            case 6:
                return new Output(explorer.getLeastValuableValley(op.depth));
            case 7:
                return new Output(explorer.getValleyCount(op.depth));
            case 8:
                return new Output(explorer.isEmpty() ? true : false);
            default:
                throw new IllegalArgumentException("Invalid operation type: " + op.type);
        }
    }

    public Output[] runOperations(Operation[] operations) {

        Output[] results = new Output[operations.length];
        int i = 0;
        for (Operation op : operations) {
            results[i++] = operate(op);
        }
        return results;
    }

    public boolean runTestCase(TestCase testCase) {
        explorer = new TreasureValleyExplorer(testCase.heights, testCase.values);
        Output[] results = runOperations(testCase.operations);
        boolean passed = true;
        if (results.length != testCase.expected.length) {
            System.out
                    .println("Test failed: results array length does not match expected array length. Expected length: "
                            + testCase.expected.length + ", but got " + results.length);
            return false;
        }
        for (int i = 0; i < testCase.expected.length; i++) {
            if (results[i].isNull() && testCase.expected[i].isNull())
                continue;
            if (!results[i].equals(testCase.expected[i])) {
                String message = "Test failed at operation " + i + ": expected " + testCase.expected[i].toString()
                        + " but got " + results[i].toString();
                System.out.println(message);
                passed = false;
            }
        }
        return passed;
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("No testcase file provided");
            return;
        } else {
            for (String filepath : args) {
                TestCase testCase = new TestCase(filepath);
                System.out.println(testCase.toString());
                boolean passed = new Evaluator().runTestCase(testCase);
                if (passed)
                    System.out.println("Test passed!");
            }
        }
    }
}
