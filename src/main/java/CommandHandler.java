
import java.util.HashMap;
import java.util.Map;

/**
 * Handles registration and execution of game commands.
 * Maps string inputs to corresponding Command objects.
 */

public class CommandHandler {
   private final Map<String,Command> commandMap = new HashMap<>();

   // Registers command with given name.
   public void registerCommand(String name, Command command) {
       commandMap.put(name.toLowerCase(), command);
   }

   // Handle user input by executing the corresponding command.
   public void handleCommand(String userInput) {
       Command command = commandMap.get(userInput.toLowerCase());
       if(command != null) {
           command.execute();
       } else {
           System.out.println("Invalid command! Please try again");
       }
   }


}
