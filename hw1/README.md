# Homework 1: Valley Traveler (25 points)

## :sparkles: Lore: The First Valley Explorers

In the land of Numerica, travelers seek the legendary First Valley - the initial dip in the terrain that holds untold treasures :moneybag::moneybag::moneybag:. As a master cartographer, you've been tasked with creating a magical map called `ValleyTraveler` that can identify these prized valleys in the ever-changing landscape, and also modify them whenever there is a sudden change in the terrain. The sudden change in terrain could be due to the Data Lords of Numerica altering the landscape when they find out that the travelers have discovered the first valley.

## :scroll: Problem Description

Implement a Java class called `ValleyTraveler` that operates on an array of distinct integers (representing the landscape of Numerica). The class should perform the following operations:

1. `ValleyTraveler(int[] landscape)`: Constructor that initializes the magical map with the given landscape of Numerica.
2. `getFirst()`: Locates the first valley point in the landscape of Numerica.
3. `remove()`: Excavates the first valley point, removing it from the landscape of Numerica.
4. `insert(int height)`: Creates a new landform at the first valley point with the given height.
5. `isEmpty()`: Returns true if the entire landscape is excavated (i.e. there are no landforms left), false otherwise.

A valley point is a landform that is lower than its neighboring landforms. For example, in the landscape [5, 2, 8, 6, 3, 9, 4], the valley points are [2, 3, 4].    

More formally, let $A$ be the $0$-indexed array of $n$ distinct integers, and $A_{i}$ be the element at index $i$.
An element $A_{i}$ is a valley point if and only if:
- For $n = 1$: $A_{0}$ is a valley point
- For $i = 0$: $A_{i} < A_{i+1}$
- For $i = n-1$: $A_{i} < A_{i-1}$
- For $0 < i < n-1$: $A_{i} < A_{i-1}$ and $A_{i} < A_{i+1}$

**Important Note:** The `ValleyTraveler` class should maintain its own state to reflect changes to the landscape. This means that the provided array may not be modified directly. Instead, the class should modify its internal state to reflect the changes made by `remove()` and `insert(int height)` operations. This ensures that subsequent calls to `getFirst()`, `remove()`, and `insert(int height)` will operate on the modified landscape.

## :briefcase: Requirements

1. Implement the `ValleyTraveler` class in `ValleyTraveler.java`.
2. The class should have a constructor that takes an array of distinct integers (the initial landscape).
3. Implement the three methods: `getFirst()`, `remove()`, and `insert(int height)`.
4. Handle edge cases and maintain the landscape's integrity after operations.

**Important Note:** Java standard collections libraries (e.g., `ArrayList`, `HashMap`, `HashSet`, etc.) are not to be used. Only primitive data types and their arrays (e.g., `int[] values`) can be used. However, you are encouraged to implement your own data structure using classes. You can either create a new class in the same file or create a new file and submit it alongside `ValleyTraveler.java`.

## :footprints: Example Expedition

Consider the following initial landscape: `[3, 2, 1, 4, 5]`

**Initial terrain:** `[3, 2, 1, 4, 5]`

1. `getFirst()` returns `1` (First valley located)

   **Terrain**: `[3, 2, 1, 4, 5]`

2. `remove()` returns `1` (Valley excavated)

   **Terrain**: `[3, 2, 4, 5]`

3. `insert(6)` (New landform created at the current first valley)

   **Terrain**: `[3, 6, 2, 4, 5]`

4. `remove()` returns `3` (Valley excavated)

   **Terrain**: `[6, 2, 4, 5]`

5. `remove()` returns `2` (Valley excavated)

   **Terrain**: `[6, 4, 5]`

6. `remove()` returns `4` (Valley excavated)

   **Terrain**: `[6, 5]`

## :envelope: Submission Details

For this assignment, you will submit your `ValleyTraveler.java` file and any additional files you create through Gradescope. 

__Submission link:__ [https://www.gradescope.com/courses/544834](https://www.gradescope.com/courses/844378)

__Deadline:__ Sept 18th, 2024 at 11:59 PM EST.

__Late Submission Policy:__ Late submissions will not be accepted.

## :bar_chart: Evaluation:

### Criteria

- __Correctness:__ Does the implementation correctly identify and modify valley points in the landscape?
- __Efficiency:__ Does the implementation have a reasonable time and space complexity?

### Procedure

- __Initialization:__ The `ValleyTraveler` object is initialized with an arbitrary array of distinct integers, representing the initial landscape.
- __Operations:__ The three methods `getFirst()`, `remove()`, and `insert(int height)` can be invoked in any arbitrary order.
- __Assumptions:__ It is guaranteed that invalid invocations will not occur. Specifically, `remove()` or `getFirst()` will not be called on an empty landscape. Also, at any points, the heights of all the landforms in the landscape are guaranteed to be distinct.   
- __Time Complexity:__ Given an initial landscape with $N$ landforms and $Q$ queries (operations), the entire procedure should be completed in $O(N+Q)$ time complexity.
- __Testing:__ We will test your implementation with various test cases, each containing different landscapes and operations, and based on how many and which test cases your implementation passes, we will assign you a score between 0 and 25 points. 

### Partial Credits
You can get partial credits for passing a subset of the test cases if you meet at least one of the below conditions:
- `remove()` method is correct.
- Both `getFirst()` and `insert()` method is correct.


## :rocket: Starter Code

Begin your expedition with this [`ValleyTraveler.java`](ValleyTraveler.java) file.

__Note:__ This starter code is provided to help you get started. You are not required to use it, but it may be helpful.

### Evaluation Script

We will use a script very similar to [`Evaluator.java`](Evaluator.java) to evaluate the correctness and efficiency of your implementation. You may run the script locally to evaluate your implementation.

Steps to run the evaluation script:

```bash
# Make sure you have Java installed on your computer
java -version

# Navigate to the problems/hw1 directory
cd problems/hw1

# Compile the Evaluator.java file
javac Evaluator.java ValleyTraveler.java

# Run the Evaluator file with the provided sample test case
java Evaluator sample_tc.txt

# You can create your own test cases to test your implementation
```

## Frequently Asked Questions

Q: What version of Java with what packages will the autograder compile submitted code?

A: Your solution will be compiled using JDK 17 with no additional libraries or packages.

Q: How do we submit our local files to Gradescope for project submissions?

A: Submit your solution "ValleyTraveler.java" and any additional java files you produce directly to the Gradescope submission page.

Q: Is there a specific IDE that we have to use or any recommended IDEs?

A: No specific IDE is required, use whichever one you prefer.