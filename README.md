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

### How the Game Works
1. Start the Game:
Players are prompted to roll the dice to determine who goes first.

2. Gameplay:
Each player takes turns rolling the dice and making moves.
Legal moves are validated, and invalid moves are rejected.
Checkers can be moved onto the board, to the bar, or borne off.

3. Winning:
The first player to bear off all their checkers wins the game.

4. Extra Features:
Inputting hint as a command gives full list of available commands to execute
