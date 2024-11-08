# Homework 3: Numerica's Task Prioritizer (30 points)

## :sparkles: Lore: The Urgent Quests of Numerica

Welcome back to the mystical land of Numerica! As the realm's premier problem-solver, you've been summoned by the Council of Data to tackle a new challenge. The kingdom is facing an unprecedented influx of quests and tasks, each with varying levels of urgency and complex dependencies.

The Council has entrusted you with creating a magical artifact called the `TaskPrioritizer`. This artifact must efficiently manage and resolve the kingdom's numerous tasks, considering their urgency levels and intricate dependencies. Your creation will be crucial in maintaining order and productivity throughout Numerica.

## :scroll: Problem Description

Implement a Java class called `TaskPrioritizer` that manages a collection of tasks, each with an associated urgency level and potential dependencies. The class should efficiently handle task additions, updates, and resolutions while respecting the urgency levels and dependencies between tasks.

The class should have a constructor as follows:

`TaskPrioritizer()`: Initializes the TaskPrioritizer with no tasks.

The class should implement the following methods:

1. `void add(String taskId, int urgencyLevel, String[] dependencies)`: Adds a new task with the given taskId, urgencyLevel, and optional dependencies. If the taskId already exists, this operation should be ignored.

2. `void update(String taskId, int newUrgencyLevel)`: Updates the urgency level of an existing task. If the taskId doesn't exist, this operation should be ignored. Note that update may be called on a resolved task, however the task will remain resolved.

3. `String resolve()`: Resolves and returns the taskId of the next task with the highest urgency level. If multiple tasks have the same highest urgency level, the one added earlier should be resolved first. If a task has unresolved dependencies, it cannot be resolved until all its dependencies are resolved. If no tasks can be resolved, return null.

You may make the following assumptions:

- No taskIds will be added more than once
- There will be no cyclic dependencies

## :briefcase: Requirements

1. Implement the `TaskPrioritizer` class in `TaskPrioritizer.java`.
2. The class should efficiently handle a large number of tasks (up to 500,000).
3. Implement all the methods described in the problem description.
4. Ensure that task dependencies are correctly managed and respected during resolution.
5. Optimize for efficient addition, update, and resolution operations.

**Important Note:** As with previous homework assignments, most Java standard collections libraries (e.g., `HashMap`, `PriorityQueue`, etc.) are not to be used. However, you may import java.util.List, java.util.ArrayList, java.util.LinkedList, and java.util.Random which may assist you in this assignment. Primitive data types and their arrays (e.g., `String[] dependencies`) can be used. However, you are encouraged to implement your own data structures using classes. You can either create new classes in the same file or create new files and submit them alongside `TaskPrioritizer.java`.

## :footprints: Example Quest Log

Consider the following sequence of operations:

1. `add("T1", 3, [])`
2. `add("T2", 5, ["T1"])`
3. `add("T3", 7, [])`
4. `resolve()` returns "T3"
5. `resolve()` returns "T1"
6. `resolve()` returns "T2"
7. `add("T5", 2, [])`
8. `add("T4", 6, ["T5"])`
9. `resolve()` returns "T5"
10. `resolve()` returns "T4"

In cases where multiple tasks have the same priority and all of their dependencies are met, the task which was added to the task prioritizer first should resolve first. Consider this example:

1. `add("T1", 3, [])`
2. `add("T2", 5, ["T1"])`
3. `add("T3", 5, ["T1"])`
4. `resolve()` returns "T1"
5. `resolve()` returns "T2"
6. `resolve()` returns "T3"
7. `add("T4", 2, [])`
8. `add("T5", 6, [])`
9. `update("T4", 6)`
10. `resolve()` returns "T4"
11. `resolve()` returns "T5"

You may find it helpful to keep track of the order in which `add()` was called for each task for this purpose.

## :envelope: Submission Details

For this assignment, you will submit your `TaskPrioritizer.java` file and any additional files you create through Gradescope.

__Submission link:__ [https://www.gradescope.com/courses/544834](https://www.gradescope.com/courses/844378)

__Deadline:__ Nov 18th, 2024 at 11:59 PM EST (2 weeks from the release of the assignment).

__Late Submission Policy:__ Late submissions will not be accepted.

## :bar_chart: Evaluation:

### Procedure

- __Initialization:__ The `TaskPrioritizer` object is initialized with no tasks.
- __Operations:__ The methods can be invoked in any arbitrary order, as long as they are valid.
- __Testing:__ We will test your implementation with various test cases, each containing different sequences of operations. Based on how many and which test cases your implementation passes, we will assign you a score between 0 and 30 points.

### Criteria

- __Correctness:__ Does the implementation correctly handle all operations on the task collection?
- __Efficiency:__ Does the implementation have a reasonable time and space complexity?
- __Time Complexity:__ For full credit, your implementation should achieve time complexity:
  - O((N + E)log(N)) 
  - where N is the number of tasks in the system and E is the number of dependencies

### Partial Credits
You can get partial credits for passing a subset of the test cases, some tests will not evaluate `update()`

## :rocket: Starter Code

Begin your quest with this [`TaskPrioritizer.java`](TaskPrioritizer.java) file. Make sure to adhere to the requirements and constraints provided in the problem description, and do not use any Java standard collection libraries.

### Evaluation Script

We will use a script similar to [`Evaluator.java`](Evaluator.java) to evaluate the correctness and efficiency of your implementation. You may run the script locally to evaluate your implementation.

Steps to run the evaluation script:
```bash
# Make sure you have Java installed on your computer
java -version

# Navigate to the problems/hw2 directory
cd problems/hw3

# Compile the Evaluator.java file
javac Evaluator.java TaskPrioritizer.java

# Run the Evaluator file with a single test case
java Evaluator tests/tc_00_manual.txt

# Run the Evaluator file with all public test cases
java Evaluator tests/

# You can create your own test cases to test your implementation
```