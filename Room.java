import java.util.HashMap;
import java.util.Set;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  The exits are labelled north, 
 * east, south, west.  For each direction, the room stores a reference
 * to the neighboring room, or null if there is no exit in that direction.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */
public class Room 
{
    private String description;
    private HashMap<String,Room> exits;  // stores exits of this room
    private ArrayList<Item> itemList;

    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * @param description The room's description.
     */
    public Room(String description) 
    {
        this.description = description;
        exits = new HashMap<>();
        itemList = new ArrayList<>();
    }

    /**
     * Define the exits of this room.  Every direction either leads
     * to another room or is null (no exit there).
     * @param north The north exit.
     * @param east The east east.
     * @param south The south exit.
     * @param west The west exit.
     */
    // Room north, Room east, Room south, Room west
    public void setExit(String direction, Room neighbor)
    {
        exits.put(direction, neighbor);
    }

    /**
     * @return The description of the room.
     */
    public String getDescription()
    {
        return description;
    }
    
    public Room getExit(String direction){
        return exits.get(direction);
    }
    
    public String getExitString(){
        String returnString = "Exits:";
        Set<String> keys = exits.keySet();
        for(String string : keys){
            returnString += " " + string;
        }
        return returnString;
    }
    
    public String getLongDescription(){
        if(itemList.size() != 0){
            return "You are " + description + ".\n" + 
            "Item(s) in the room: " + getListOfItems() + ".\n" + getExitString();
        }
        else
            return "You are " + description + ".\n" + 
            "There's no item in this room.\n" + getExitString();
    }
    
    public void addItem(Item item){
        itemList.add(item);
    }
    
    public void removeItem(Item item) {
        itemList.remove(item);
    }
    
    public String getListOfItems(){
        String list = "";
        for(Item item : itemList)
            list += item.getDescription() + " + ";
        return list;
    }
    
    public Item itemInRoom(String item) {
        Iterator<Item> itr = itemList.iterator();
        Item aux = itr.next();
        while(!aux.getDescription().contains(item))
            aux = itr.next();
        return aux;
    }
}
