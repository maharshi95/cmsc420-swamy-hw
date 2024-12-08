import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.io.File;

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

    Operation readAdd(String buffer) {
        return createOperation(buffer, OperationType.ADD, true);
    }

    Operation readRemove(String buffer) {
        return createOperation(buffer, OperationType.REMOVE, false);
    }

    Operation readGetDefinition(String buffer) {
        return createOperation(buffer, OperationType.GET_DEF, true);
    }

    Operation readGetSequence(String buffer) {
        return createOperation(buffer, OperationType.GET_SEQ, true);
    }

    Operation readCountPrefix(String buffer) {
        return createOperation(buffer, OperationType.COUNT, true);
    }

    Operation readCompress(String buffer) {
        return new Operation(OperationType.COMPRESS, null, null, null);
    }

    private Operation createOperation(String buffer, int type, boolean hasExpected) {
        String[] parts = buffer.split(" ", 2);
        String wordOrPrefix = parts[0].trim();
        String secondPart = hasExpected ? parts[1].trim() : null;
        return new Operation(type, wordOrPrefix, hasExpected && type == OperationType.ADD ? secondPart : null,
                hasExpected && type != OperationType.ADD ? secondPart : null);
    }

    Operation readOperation(BufferedReader reader) throws IOException {
        String line = reader.readLine().trim();
        int type = Integer.parseInt(line.split(" ")[0]);
        String buffer = line.substring(line.indexOf(' ') + 1).trim();
        switch (type) {
            case OperationType.ADD:
                return readAdd(buffer);
            case OperationType.REMOVE:
                return readRemove(buffer);
            case OperationType.GET_DEF:
                return readGetDefinition(buffer);
            case OperationType.GET_SEQ:
                return readGetSequence(buffer);
            case OperationType.COUNT:
                return readCountPrefix(buffer);
            case OperationType.COMPRESS:
                return readCompress(buffer);
            default:
                throw new IllegalArgumentException("Invalid operation type: " + type);
        }
    }

    TestCase(String filepath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            int numOps = Integer.parseInt(reader.readLine().trim());
            operations = new Operation[numOps];
            for (int i = 0; i < numOps; i++) {
                operations[i] = readOperation(reader);
            }
        } catch (IOException e) {
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

class Evaluator {
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