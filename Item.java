
/**
 * Items are in the rooms and the user can pick them up.
 *
 * @author Martin, Estella, Kim
 * @version 1 - 30.06.2022
 */
public class Item
{
    String description;
    int weight;

    /**
     * Constructor for objects of class Item
     */
    public Item(String description, int weight)
    {
        this.description = description;
        this.weight = weight; //in grams
    }

    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    public String getDescription()
    {
       return description;
    }
    
    public int getWeight()
    {
        return weight;
    }
}
