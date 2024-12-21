# Backgammon Project

## Overview
This is a **Backgammon game implementation** written in Java. The project uses **Maven** for build management and dependency resolution, and **JUnit** for unit testing. It provides core game functionality, including board setup, checker movement, dice rolling, legal move validation, and pip calculation.

## Features
- Full implementation of Backgammon game mechanics.
- Automated dice rolls and validation of moves.
- Command-based interface for game control.
- Comprehensive unit tests using JUnit.
- Maven-based project structure for easy dependency management and builds.

---

## Prerequisites
### For Running the Project
- **Java 23 or higher**  
  Install the required Java version from [OpenJDK Downloads](https://jdk.java.net/).  
  Verify installation with:
  java -version

### For Building and Running Tests
Maven

### Project Structure
backgammon-project/
│
├── pom.xml               # Maven configuration file
├── README.md             # Project documentation
├── src/
│   ├── main/
│   │   └── java/
│   │       ├── Board.java
│   │       ├── Checker.java
│   │       ├── Constants.java
│   │       ├── Dice.java
│   │       ├── Game.java
│   │       ├── Move.java
│   │       ├── PipCalculator.java
│   │       ├── Player.java
│   │       └── Validation.java
│   └── test/
│       └── java/
│           └── Main.java    # Unit tests
├── .gitignore            # Files ignored by Git

### Compile the Project
To compile the project, run:

mvn compile

### How the Game Works
1. Start the Game:
Players are prompted to roll the dice to determine who goes first.

2. Gameplay:
Each player takes turns rolling the dice and making moves.
Legal moves are validated, and invalid moves are rejected.
Checkers can be moved onto the board, to the bar, or borne off.

3. Winning:
The first player to bear off all their checkers wins the game.

### **7. Troubleshooting for Teammates**
1. **If Maven Dependencies Fail to Resolve**:
    - Run:
      ```bash
      mvn clean install
      ```
    - Or reload Maven projects in IntelliJ.

2. **If Java Version Mismatch Occurs**:
    - Ensure teammates install the correct Java version (Java 23 in your case).

3. **If IDE Issues Persist**:
    - Encourage them to delete `.idea/` and `.iml` files, then reopen the project.

---

### **8. Summary**
For your teammates to pull and run your project:
- They pull the `main` branch from GitHub.
- They ensure their environment (Java version) matches yours.
- Maven handles all dependencies and builds the project for them.
- A clear `README.md` helps guide them through the setup.

If you follow these steps, your project will be easy for teammates and your lecturer to use. Let me know if you’d like help refining your `README.md` or automating any steps further!
