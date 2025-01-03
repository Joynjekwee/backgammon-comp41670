
import java.util.HashMap;
import java.util.Map;

public class CommandHandler {
   private final Map<String,Command> commandMap = new HashMap<>();

   public void registerCommand(String name, Command command) {
       commandMap.put(name.toLowerCase(), command);
   }
   public void handleCommand(String userInput) {
       Command command = commandMap.get(userInput.toLowerCase());
       if(command != null) {
           command.execute();
       } else {
           System.out.println("Invalid command! Please try again");
       }
   }

    public Map<String, Command> getCommandMap() {
        return commandMap;
    }
}
