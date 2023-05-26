import java.util.ArrayList;
import java.util.Iterator;

/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael KÃ¶lling and David J. Barnes
 * @version 2016.02.29
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    private ArrayList<Item> items;
    private CommandWords commandWords;
    private ArrayList<Command> movements;
    private int maxWeight = 50000;
    private int weight = 0;

    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
        commandWords = new CommandWords();
        movements = new ArrayList<>();
        items = new ArrayList<>();
    }
    
    /**
     * Create all the rooms, link their exits together and maybe asign an item to them.
     */
    private void createRooms()
    {
        Room trainingGround, kitchen, hallway, diningRoom, garden, infirmary, weaponry, lab, chamber1, 
             chamber2, chamber3, office, start, exit;
        Item stone, bearTrap, ration, waterBottle, hedge, medKit, bullet, locker, flashLight, handPistol;
        
        // create the items
        stone = new Item("a very heavy stone",5);
        bearTrap = new Item("a sharp bear trap",24000);
        ration = new Item("a delicious canned ration",250);
        waterBottle = new Item("a fresh water bottle",250);
        hedge = new Item("a big hedge",10000);
        medKit = new Item("a medKit in case you get hurt",250);
        bullet = new Item("a bullet for your weapon - don't kill anyone!",10);
        locker = new Item("an locker to put your stuff in",44000);
        flashLight = new Item("a flash light - at night it gets dark!",1000);
        handPistol = new Item("a hand pistol",1100);
   
        // create the rooms
        trainingGround = new Room("in the training room");
        kitchen = new Room("in the kitchen");
        hallway = new Room("in the hallway");
        diningRoom = new Room("in the dining room");
        garden = new Room("in the garden");
        infirmary = new Room("in the infirmary");
        weaponry = new Room("in the weaponry");
        lab = new Room("in a computing lab");
        chamber1 = new Room("in a small chamber");
        chamber2 = new Room("in a big chamber");
        chamber3 = new Room("in a dirty chamber");
        office = new Room("in the computing admin office");
        start = new Room("in front of the base");
        exit = new Room("in the exit. You have managed to escape! Congratulations");
        
        // asign the items to the rooms
        trainingGround.addItem(stone);
        trainingGround.addItem(bearTrap);
        kitchen.addItem(ration);
        hallway.addItem(stone);
        diningRoom.addItem(waterBottle);
        garden.addItem(hedge);
        garden.addItem(stone);
        garden.addItem(bearTrap);
        infirmary.addItem(medKit);
        weaponry.addItem(bullet);
        lab.addItem(locker);
        chamber1.addItem(locker);
        chamber2.addItem(flashLight);
        chamber3.addItem(locker);
        office.addItem(handPistol);

        // initialise room exits
        trainingGround.setExit("north",hallway);
        trainingGround.setExit("south",start);
        kitchen.setExit("west",hallway);
        hallway.setExit("west",diningRoom);
        hallway.setExit("east",kitchen);
        hallway.setExit("south",trainingGround);
        hallway.setExit("north",garden);
        diningRoom.setExit("east",hallway);
        garden.setExit("east",infirmary);
        garden.setExit("west",weaponry);
        infirmary.setExit("west",garden);
        weaponry.setExit("east",garden);
        lab.setExit("north",chamber3);
        lab.setExit("south",infirmary);
        chamber1.setExit("east",office);
        chamber1.setExit("south",chamber2);
        chamber2.setExit("north",chamber1);
        chamber2.setExit("south",weaponry);
        chamber3.setExit("west",office);
        chamber3.setExit("south",lab);
        office.setExit("west",chamber1);
        office.setExit("east",chamber3);
        office.setExit("north",exit);
        start.setExit("north",trainingGround);
        exit.setExit("south",office);

        currentRoom = start;  // start game at the start
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.

        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            String output = processCommand(command);
            finished = (null == output);
            if (!finished)
            {
                System.out.println(output);
            }
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * This is a further method added by BK to
     * provide a clearer interface that can be tested:
     * Game processes a commandLine and returns output.
     * @param commandLine - the line entered as String
     * @return output of the command
     */
    public String processCommand(String commandLine){
        Command command = parser.getCommand(commandLine);
        return processCommand(command);
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        //printLocationInfo();
        currentRoom.getLongDescription();
    }
    
    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private String processCommand(Command command) 
    {
        String commandWord = command.getCommandWord();
        CommandWord commandText = commandWords.getCommandWord(commandWord);
        switch (commandText) {
            case UNKNOWN:
                return "I don’t know what you mean. . .";
            case HELP:
                return printHelp();
            case GO:
                addMovementToArray(command);
                return goRoom(command);
            case TAKE:
                return takeItem(command);
            case DROP:
                return dropItem(command);
            case BACK:
                Command newCommand;
                if(movements.size() == 0)
                    return "You just started! You can't go back.";
                else {
                    newCommand = movements.get(movements.size()-1);
                    movements.remove(movements.size()-1);
                    return goRoom(newCommand);
                }
            case ITEMS:
                return getInformationAboutItems();
            case QUIT:
                return quit(command);
        }

        return null;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private String printHelp() 
    {
        return "You are lost. You are alone. You wander"
        +"\n"
        + "around at the university."
        +"\n"
        +"\n"
        +"Your command words are:"
        +"\n"
        + parser.showCommands();
    }
    
    /**
     * Drop an item or all of them if the users type "drop all".
     */
    private String dropItem(Command command)
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know what to drop...
            return "What would you like to drop?";
        }
        
        String itemToDrop = command.getSecondWord();
        
        if(itemToDrop.equals("all")) {
            if(!items.isEmpty()) {
                for(Item item : items) {
                    currentRoom.addItem(item);
                }
                items = new ArrayList<>();
                weight = 0;
                return "You dropped all your items in the room! \n" + currentRoom.getLongDescription();
            }
            else 
                return "You don't have any items! \n" + currentRoom.getLongDescription();
        }   
        else {
            Item item = itemInInventory(itemToDrop);
            if(item == null)
                return "You don't have a " + itemToDrop + " in your bag! \n" + currentRoom.getLongDescription();
            else {
                weight -= item.getWeight();
                items.remove(item);
                currentRoom.addItem(item);
                return "You dropped the " + itemToDrop + " in the room! \n" + currentRoom.getLongDescription();
            }
        }
    }
    
    /**
     *  Check out if the item is in the inventory.
     */
    public Item itemInInventory(String item) {
        if(!items.isEmpty()) {
            Iterator<Item> itr = items.iterator();
            Item aux = itr.next();
            while(itr.hasNext() && !aux.getDescription().contains(item))
                aux = itr.next();
            if(aux.getDescription().contains(item))
                return aux;
            else 
                return null;
        }
        return null;
    }
    
    /**
     * @return information about the items the user have in the inventory, how much they weigh and how much free space
     * the user has.
     */
    private String getInformationAboutItems() {
        String result = "";
        if(items.isEmpty()) 
            result = "You don't have any items in your bag. Go and take some of them! \n" +
            "Be carfeul! You can only take 50.000 grams in total.";
        else{
            result = "You have ";
            for(Item item : items) {
                result += item.getDescription() + ", ";
            }
            result += "in your bag. \n" + "That's in total " + weight + " grams. Be careful! You can still take "
            + (50000-weight) + " more grams.";
        }
        return result;
    }
    
    /**
     * Try to take an item. If the item is in the room, take it, otherwise
     * print an error message.
     */
    private String takeItem(Command command)
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know what to take...
            return "What would you like to take?";
        }
        
        String itemToTake = command.getSecondWord();
        
        String listOfItemsInCurrentRoom = currentRoom.getListOfItems();
        if (listOfItemsInCurrentRoom.contains(itemToTake)) {
            Item item = currentRoom.itemInRoom(itemToTake);
            if(isThereSpace(item)) {
                items.add(item);
                weight += item.getWeight();
                currentRoom.removeItem(item);
                return "Nice! You have now a " + itemToTake + 
                " in your bag :D \n" + currentRoom.getLongDescription();
            }
            else
                return "You don't have more space in your bag! :/";
        }
        else
            return "Mmmh, there's no " + itemToTake + 
            " in this room. Keep searching! \n" + currentRoom.getLongDescription();
    }
    
    /**
     * Function that evaluates whether there's is enough space for an item or not.
     */
    private boolean isThereSpace(Item item) {
        if(item.getWeight() + weight <= maxWeight)
            return true;
        else
            return false;
    }

    /** 
     * Try to go in one direction. If there is an exit, enter
     * the new room, otherwise print an error message.
     */
    private String goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            return "Go where?";
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);
        String result = "";
        if (nextRoom == null) {
            result += "There is no door!";
        }
        else {
            currentRoom = nextRoom;
            result = currentRoom.getLongDescription();
        }
        return result + "\n";
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private String quit(Command command) 
    {
        if(command.hasSecondWord()) {
            return "Quit what?";
        }
        else {
            return null;  // signal that we want to quit
        }
    }
    
    private void addMovementToArray(Command command){
        if(command.hasSecondWord()){
            String direction = command.getSecondWord();
            Room nextRoom = currentRoom.getExit(direction);
            if (nextRoom != null) {
                Command newCommand = null;
                if(command.getSecondWord().equals("east")) 
                    newCommand = new Command("go","west");
                else if (command.getSecondWord().equals("west")) 
                    newCommand = new Command("go","east");
                else if (command.getSecondWord().equals("south")) 
                    newCommand = new Command("go","north");
                else 
                    newCommand = new Command("go","south");
                movements.add(newCommand);
            }
        }
    }
}
