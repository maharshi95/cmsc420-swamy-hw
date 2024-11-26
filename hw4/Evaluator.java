import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Arrays;

class OperationType {
    public static final int ADD = 1;
    public static final int REMOVE = 2;
    public static final int GET_DEF = 3;
    public static final int COUNT = 4;
    public static final int COMPRESS = 5;
    public static final int GET_SEQ = 6;
}

class Operation {
    int type;
    String word;
    String definition;
    String expected;

    Operation(int type, String word, String definition, String expected) {
        this.type = type;
        this.word = word;
        this.definition = definition;
        this.expected = expected;
    }

    public String toString() {
        switch (type) {
            case OperationType.ADD:
                return "Op:[add " + word + " \"" + definition + "\"]";
            case OperationType.REMOVE:
                return "Op:[remove " + word + "]";
            case OperationType.GET_DEF:
                return "Op:[getDefinition " + word + "]";
            case OperationType.GET_SEQ:
                return "Op:[getSequence " + word + "]";
            case OperationType.COUNT:
                return "Op:[countPrefix " + word + "]";
            case OperationType.COMPRESS:
                return "Op:[compress]";
            default:
                return "Op:[Invalid]";
        }
    }
}

class TestCase {
    Operation[] operations;

    Operation readAdd(Scanner scanner) {
        try {
            String word = scanner.next();
            String definition = scanner.nextLine().trim();
            return new Operation(OperationType.ADD, word, definition, null);
        } catch (Exception e) {
            throw new RuntimeException("Error reading ADD operation: " + e.getMessage());
        }
    }

    Operation readRemove(Scanner scanner) {
        try {
            String word = scanner.next();
            scanner.nextLine(); // consume the rest of the line
            return new Operation(OperationType.REMOVE, word, null, null);
        } catch (Exception e) {
            throw new RuntimeException("Error reading REMOVE operation: " + e.getMessage());
        }
    }

    Operation readGetDefinition(Scanner scanner) {
        try {
            String word = scanner.next();
            String expected = scanner.nextLine().trim();
            return new Operation(OperationType.GET_DEF, word, null, expected);
        } catch (Exception e) {
            throw new RuntimeException("Error reading GET_DEF operation: " + e.getMessage());
        }
    }

    Operation readGetSequence(Scanner scanner) {
        try {
            String word = scanner.next();
            String expected = scanner.nextLine().trim();
            return new Operation(OperationType.GET_SEQ, word, null, expected);
        } catch (Exception e) {
            throw new RuntimeException("Error reading GET_SEQ operation: " + e.getMessage());
        }
    }

    Operation readCountPrefix(Scanner scanner) {
        try {
            String prefix = scanner.next();
            String expected = scanner.nextLine().trim();
            return new Operation(OperationType.COUNT, prefix, null, expected);
        } catch (Exception e) {
            throw new RuntimeException("Error reading COUNT operation: " + e.getMessage());
        }
    }

    Operation readCompress(Scanner scanner) {
        try {
            scanner.nextLine(); // consume the rest of the line
            return new Operation(OperationType.COMPRESS, null, null, null);
        } catch (Exception e) {
            throw new RuntimeException("Error reading COMPRESS operation: " + e.getMessage());
        }
    }

    Operation readOperation(Scanner scanner) {
        try {
            int type = scanner.nextInt();
            switch (type) {
                case OperationType.ADD:
                    return readAdd(scanner);
                case OperationType.REMOVE:
                    return readRemove(scanner);
                case OperationType.GET_DEF:
                    return readGetDefinition(scanner);
                case OperationType.GET_SEQ:
                    return readGetSequence(scanner);
                case OperationType.COUNT:
                    return readCountPrefix(scanner);
                case OperationType.COMPRESS:
                    return readCompress(scanner);
                default:
                    throw new IllegalArgumentException("Invalid operation type: " + type);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error reading operation: " + e.getMessage());
        }
    }

    TestCase(String filepath) {
        try (Scanner scanner = new Scanner(new File(filepath))) {
            int numOps = scanner.nextInt();
            scanner.nextLine(); // consume newline
            operations = new Operation[numOps];
            for (int i = 0; i < numOps; i++) {
                operations[i] = readOperation(scanner);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Testcase file not found: " + filepath);
            throw new RuntimeException(e);
        } catch (Exception e) {
            System.out.println("Error reading testcase file: " + filepath);
            throw new RuntimeException(e);
        }
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Operations[").append(operations.length).append("]:{\n");
        for (Operation op : operations) {
            result.append("  ").append(op.toString()).append("\n");
        }
        result.append("}\n");
        return result.toString();
    }
}

public class Evaluator {
    private Dictionary dictionary;

    public boolean runOperations(Operation[] operations) {
        int i = 0;
        for (Operation op : operations) {
            try {
                String result;
                switch (op.type) {
                    case OperationType.ADD:
                        dictionary.add(op.word, op.definition);
                        break;
                    case OperationType.REMOVE:
                        dictionary.remove(op.word);
                        break;
                    case OperationType.GET_DEF:
                        result = dictionary.getDefinition(op.word);
                        if (!String.valueOf(result).equals(op.expected)) {
                            System.out.println("Test failed at operation " + i + "[" + op.toString() + "]: expected "
                                    + op.expected + " but got " + result);
                            return false;
                        }
                        break;
                    case OperationType.GET_SEQ:
                        result = dictionary.getSequence(op.word);
                        if (!String.valueOf(result).equals(op.expected)) {
                            System.out.println("Test failed at operation " + i + "[" + op.toString() + "]: expected "
                                    + op.expected + " but got " + result);
                            return false;
                        }
                        break;
                    case OperationType.COUNT:
                        int count = dictionary.countPrefix(op.word);
                        if (!String.valueOf(count).equals(op.expected)) {
                            System.out.println("Test failed at operation " + i + "[" + op.toString() + "]: expected "
                                    + op.expected + " but got " + count);
                            return false;
                        }
                        break;
                    case OperationType.COMPRESS:
                        dictionary.compress();
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid operation type: " + op.type);
                }
                i++;
            } catch (Exception e) {
                System.out.println("Test failed at operation " + i + ": " + e.getMessage());
                return false;
            }
        }
        return true;
    }

    public boolean runTestCase(TestCase testCase) {
        dictionary = new Dictionary();
        return runOperations(testCase.operations);
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("No testcase file provided");
            return;
        }

        for (String path : args) {
            File file = new File(path);
            processTestFile(file);
        }
    }

    private static void processTestFile(File file) {
        System.out.println("Processing file: " + file.getPath());
        if (!file.isDirectory()) {
            runSingleTest(file.getPath(), true);
        } else {
            File[] files = file.listFiles();
            if (files != null) {
                Arrays.sort(files);
                for (File testFile : files) {
                    if (testFile.isFile() && testFile.getName().endsWith(".txt")) {
                        runSingleTest(testFile.getPath(), false);
                    }
                }
            } else {
                System.out.println("No files found in directory: " + file.getPath());
            }
        }
    }

    private static void runSingleTest(String path, boolean verbose) {
        TestCase testCase = new TestCase(path);
        if (verbose) {
            System.out.println(testCase.toString());
        }
        long startTime = System.currentTimeMillis();
        boolean passed = new Evaluator().runTestCase(testCase);
        long endTime = System.currentTimeMillis();
        long runtime = endTime - startTime;
        String fileName = new File(path).getName();
        String status = passed ? "PASS" : "FAIL";
        System.out.println("+" + "-".repeat(62) + "+" + "-".repeat(12) + "+" + "-".repeat(12) + "+");
        System.out.println(String.format("| %-60s | %-10s | %-8dms |", fileName, status, runtime));
        System.out.println("+" + "-".repeat(62) + "+" + "-".repeat(12) + "+" + "-".repeat(12) + "+");
    }
}