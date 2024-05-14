# CS3642 Artificial Intelligence
This repository is to track the development process for my classwork.
Some projects implement JavaFX via Maven package management in IntelliJ. These projects can be ran using the .iml files 
which are configured to run each project. These projects will also be available in a self-packaged .jar file in the 
releases when time permits. 

### :memo: JavaDoc Commenting Structure
All of my finished code and any public class should contain a JavaDoc comment block. This comment block will contain the
what parameters the function accepts, what are the preconditions, what are the post conditions and what values it returns
*(if not void)*. This commenting structure along with designing with Self-Documenting code it should allow for easier 
understanding and code tracing when troubleshooting, revising, or utilizing the functions. 
## Assignment 1 - 8 Puzzle Solver
This application was focused around pathing with regards to path generation and graph traversal. The goal of the 
assignment was to work with the different graph traversal algorithms to understand their strengths, weaknesses, and 
use-cases.
### :fountain_pen: Initial Design Model
This application was modeled closely with MVVM (Model-View-ViewModel) Structure. This allows for a seperation of 
concerns and to ensure no user interacts directly with a model, no model, interacts directly with the user, and that 
there is a intermediatary, [MainViewModel](src/main/java/Assignments/A1_8Puzzles/view/MainViewModel.java), that will bind with the View, [BoardView](src/main/resources/A1/view/BoardView.fxml), via the Property file, 
[BoardViewCodeBehind](src/main/java/Assignments/A1_8Puzzles/view/BoardViewCodeBehind.java).

From this design method I began the module classes. The structure for these are as follows: The main 3 are [Board](src/main/java/A1_8Puzzles/model/Board.java),
[BoardNode](src/main/java/A1_8Puzzles/model/BoardNode.java) and [BoardGenerator](src/main/java/A1_8Puzzles/model/BoardGenerator.java). The board focused on holding the positions and functionality a board could 
perform at a given moment. This included the available [Moves](src/main/java/A1_8Puzzles/model/helper/Move.java) and attributes associated with each algorithm. Each 
Board calculates a heuristic *(estimated future cost)*, an actual cost *(cost to get to this node)*, and a total cost 
*(actual cost + estimated cost)*. These attributes were then utilized by the different traversal algorithms to change 
the priority of each node. 
### :arrows_counterclockwise: Traversal Algorithms
These algorithms were [A* (A Star)](src/main/java/Assignments/A1_8Puzzles/solving_algorithms/comparators/AStar.java), [BFS (Best-First Search)](src/main/java/Assignments/A1_8Puzzles/solving_algorithms/comparators/BFS.java), [UCS (Uniform-Cost Search)](src/main/java/Assignments/A1_8Puzzles/solving_algorithms/comparators/UCS.java), and lastly 
[DFS (Depth-First Search)](src/main/java/Assignments/A1_8Puzzles/solving_algorithms/DFS.java). The first 3 algorithms were implemented using a Priority Queue and a comparator that 
will change how the queue will prioritize the next moves. These comparators were then passed into 
[PriorityTraversal](src/main/java/Assignments/A1_8Puzzles/solving_algorithms/PriorityTraversal.java). The last algorithm DFS was implemented using a Stack for LIFO (Last-In -> First-Out) Priority. 
### :computer: JavaFX Implementation 
The GUI is rudimentary and is mainly focused on displaying the current board, *does not display the traversal algorithm*,
and will show a directory representation of the tree generated from the graph traversal. The GUI will also allow you to 
select which algorithm you would like to solve for and run performance analysis of that algorithm with 100 sequential 
runs. From these runs it will check the time taken, outliers and average, and will display the average number of nodes. 
These results are saved during runtime so after running once, you can go back to that algorithm and view the previous 
test without needing to run again. 

## Assignment 2 - Genetic Algorithm
This application was focused around Genetic Algorithms and how they can be used to solve problems. The goal of the
assignment was to create a genetic algorithm to solve the problem of finding the minimum value of a function via
evolutionary processes.

### :page_facing_up: Initial Requirements
The requirements for this assignment were to create a genetic algorithm that would solve the problem of finding the
minimum value of a function. This function was defined as: `f(x)=x^2, where 0 <= x <= 63`. This was done by defining 
the population, which was represented a
[6-Bit Binary Vector](/src/main/java/Assignments/A2_Genetic_Algorithm/models/BinaryVector.java). 

### :muscle: Fitness Function
The fitness function was defined as the value of the function `f(x)=x^2` where `0` is more fit than `63`. This was done 
by accepting each individual, which was a `BinaryVector`, and converting it to a decimal value. This decimal value was 
then squared and the result was the fitness value. 

### :pushpin: Selection
The selection process was done by selecting the top 50% of the population based on their fitness value. This was done by
sorting the population by their fitness value and then selecting the top 50% of the population. The selection process
was done by placing the population in a `PriorityQueue` and then popping the top 50% of the population. 

### :books: Crossover
The crossover process was done by taking the selected population and then crossing over the individuals. This was
done by taking randomly selecting a crossover point, then from that index, leading up to that index was the first
individual and after that index was the second individual. This was done for each pair of individuals.

(i.e., Individual 1: `101010`, Individual 2: `110011`, Crossover Point: `3`, Children: `101011`, `110010`)

### :scissors: Mutation
The mutation process was first provided a mutation rate, which was user defined. This mutation rate was then used to
determine if a mutation would occur. If the mutation rate was greater than a random number between 0 and 1, then a 
mutation would occur. This mutation was done by selecting a random index and then flipping the bit at that index.

### :balance_scale: Evolution
The evolution process was done by taking the selected population, crossing over the individuals, and then mutating the
individuals. This was done for a set number of generations, and/or until the minimum value was found. 

## Assignment 3 - Perceptron
This application was focused around perceptrons and how you can train something to classify and recognize patterns. The
goal of the assignment was two parts, the first was to create a perceptron that could classify the OR gates, and the
second was to create a perceptron that could if a 'image' (2x2 pixel grid) was bright or dark. 

### :door: OR Gates Requirements
The requirements for the OR gates were to create a perceptron that could classify the OR gates. This was done by
creating a perceptron that would take in the inputs of the OR gate and then classify it as a 1 or 0. This perceptron 
would be trained on a input of 4 different values, (0, 0), (0, 1), (1, 0), and (1, 1), and then classify them as a 0 
or 1. This was done by training the perceptron with the inputs and then adjusting the weights and bias until the 
perceptron could classify the OR gate correctly.

The Truth Table for the OR gate is as follows:

| A | B | Output |
|---|---|--------|
| 0 | 0 |   0    |
| 0 | 1 |   1    |
| 1 | 0 |   1    |
| 1 | 1 |   1    |

### :low_brightness: Bright or Dark Requirements
The requirements for the Bright or Dark was to create a perceptron that could classify if an image was bright or dark.
This was done by creating a perceptron that would take in the inputs of the image and then classify it as a 1 or 0. 
This perceptron would be trained on a input of 16 different values due to the 2x2 pixel grid, and then classify them as
a 0 (bright) or 1 (dark). This was done by training the perceptron with the inputs and then adjusting the weights and 
bias until the perceptron could classify the image correctly.

The Truth Table for the Bright or Dark is as follows:

| a | b | c | d | Output |
|---|---|---|---|--------|
| 1 | 1 | 1 | 1 | 0      |
| 1 | 1 | 1 | 0 | 0      |
| 1 | 1 | 0 | 1 | 0      |
| 1 | 1 | 0 | 0 | 0      |
| 1 | 0 | 1 | 1 | 0      |
| 1 | 0 | 1 | 0 | 0      |
| 1 | 0 | 0 | 1 | 0      |
| 1 | 0 | 0 | 0 | 1      |
| 0 | 1 | 1 | 1 | 0      |
| 0 | 1 | 1 | 0 | 0      |
| 0 | 1 | 0 | 1 | 0      |
| 0 | 1 | 0 | 0 | 1      |
| 0 | 0 | 1 | 1 | 0      |
| 0 | 0 | 1 | 0 | 1      |
| 0 | 0 | 0 | 1 | 1      |
| 0 | 0 | 0 | 0 | 1      |

### :hammer_and_wrench: Epochs
The epochs were defined as the number of times the perceptron would train on the data. This was done by taking the
inputs and then training the perceptron on the inputs. This was done for a set number of epochs or until the perceptron
could classify the OR gate or the brightness correctly.