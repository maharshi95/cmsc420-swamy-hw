import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;

class OperationType {
    public static final int ADD = 1;
    public static final int UPDATE = 2;
    public static final int RESOLVE = 3;
}

class Operation {
    int type;
    Integer urgency;
    String taskId;
    String[] dependencies;

    Operation(int type, String taskId, Integer urgency, String[] dependencies) {
        this.type = type;
        this.urgency = urgency;
        this.taskId = taskId;
        this.dependencies = dependencies;
    }

    public static String makeDependenciesString(String[] dependencies) {
        String str = "[";
        for (int i = 0; i < dependencies.length; i++) {
            str += dependencies[i];
            if (i < dependencies.length - 1) {
                str += ", ";
            }
        }
        str += "]";

        return str;
    }

    public String toString() {
        switch (type) {
            case 1:
                return "Op:[add " + taskId + " " + urgency + " " + makeDependenciesString(dependencies) + "]";
            case 2:
                return "Op:[update " + taskId + " " + urgency + "]";
            case 3:
                return "Op:[resolve]";
            default:
                return "Op:[Invalid]";
        }
    }
}

class TestCase {
    Operation[] operations;
    String[] expected;

    Operation readAdd(Scanner scanner) {
        String[] lineStrings = scanner.nextLine().split(" ");
        String taskId = lineStrings[1];
        Integer urgency = Integer.parseInt(lineStrings[2]);

        String[] dependencies = new String[lineStrings.length - 3];

        for (int i = 3; i < lineStrings.length; i++) {
            dependencies[i - 3] = lineStrings[i];
        }

        return new Operation(1, taskId, urgency, dependencies);
    }

    Operation readUpdate(Scanner scanner) {
        String[] lineStrings = scanner.nextLine().split(" ");
        String taskId = lineStrings[1];
        Integer urgency = Integer.parseInt(lineStrings[2]);
        return new Operation(2, taskId, urgency, null);
    }

    Operation readOperation(Scanner scanner) {
        int type = scanner.nextInt();
        switch (type) {
            case 1:
                return readAdd(scanner);
            case 2:
                return readUpdate(scanner);
            case 3:
                return new Operation(type, null, null, null);
            default:
                return null;
        }
    }

    TestCase(String filepath) {
        try (Scanner scanner = new Scanner(new File(filepath))) {
            int numOps = scanner.nextInt();
            operations = new Operation[numOps];
            for (int i = 0; i < numOps; i++) {
                operations[i] = readOperation(scanner);
            }
            int numOutputs = scanner.nextInt();
            expected = new String[numOutputs];
            int k = 0;
            while (k < numOutputs) {
                String line = scanner.nextLine();
                String[] lineStrings = scanner.nextLine().split(" ");
                for (int i = 0; i < lineStrings.length; i++) {
                    expected[k++] = lineStrings[i];
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Testcase file not found: " + filepath);
            throw new RuntimeException(e);
        }
    }

    public String toString() {
        String result = "";
        result += "Operations[" + operations.length + "]:{\n";
        for (Operation op : operations) {
            result += "  " + op.toString() + "\n";
        }
        result += "}\n";
        result += "Expected[" + expected.length + "]:{\n";
        for (String output : expected) {
            result += "  " + output + "\n";
        }
        result += "}\n";
        return result;
    }
}

public class Evaluator {

    private TaskPrioritizer taskPrioritizer;

    public boolean runOperations(Operation[] operations, String[] expected) {
        ArrayList<String> results = new ArrayList<String>();
        int i = 0;
        for (Operation op : operations) {
            switch (op.type) {
                case 1:
                    taskPrioritizer.add(op.taskId, op.urgency, op.dependencies);
                    break;
                case 2:
                    taskPrioritizer.update(op.taskId, op.urgency);
                    break;
                case 3:
                    results.add(taskPrioritizer.resolve());

                    if (!results.get(results.size() - 1).equals(expected[results.size() - 1])) {
                        String message = "Test failed at operation " + i + ": expected " + expected[results.size() - 1]
                                + " but got " + results.get(results.size() - 1);
                        System.out.println(message);
                        return false;
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Invalid operation type: " + op.type);
            }
            i++;
        }
        return true;
    }

    public boolean runTestCase(TestCase testCase) {
        taskPrioritizer = new TaskPrioritizer();
        return runOperations(testCase.operations, testCase.expected);
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
                    runSingleTest(testFile.getPath(), false);
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
