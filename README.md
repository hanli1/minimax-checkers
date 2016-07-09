# minimax-checkers

This project includes the implementation of a standard 12 vs 12 checkers AI in Java that uses the minimax algorithm along with fail-soft alpha-beta pruning to increase tree traversal speed. 

### Requirements
In order to run the project, IntelliJ must be installed. 

### Installation
* To open and run the project, either clone or download into a folder of your choice.
* If prompted, set the Java SDK path
* Click on `Edit Run Configurations`, then set the main class to be `Main.java`.
* Set the module output path as `Inherit from Project`.
* Set the project output path as the path to your folder appended by `/out`. This is to ensure IntelliJ knows where to create the `.class` files for execution. 
* Change settings in `Main.java` for bots/human, and then run. 

### Features
* Support for 2 players including human, minimax bot, or random bot.
* Command line GUI for testing with basic commands such as `board`, `rand`, and movement commands.

### Todo
* Implement quiescence search so that the AI can be more intelligent at combating the horizon effect.
* Possibly a GUI in JavaFX to enhance testing. 
* Option for turning on/off force jumping.



