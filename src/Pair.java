
/**
 * Class that represents the location and size of either a sequence or sequence
 * ID, or a free block
 * 
 * @author Justin Mathias
 * @version 2020.12.01
 *
 */
public class Pair {
    private int location;
    private int size;

    /**
     * Constructor that allows for filling location and size
     * 
     * @param location
     *            Location of the block or sequence
     * @param size
     *            Size of block or sequence
     */
    public Pair(int location, int size) {
        this.location = location;
        this.size = size;
    }


    /**
     * Getter for location field
     * 
     * @return location The location of the block or sequence
     */
    public int getLocation() {
        return location;
    }


    /**
     * Getter for size field
     * 
     * @return size The size of the block or sequence
     */
    public int getSize() {
        return size;
    }


    /**
     * Setter for location field
     * 
     * @param newLocation
     *            The new location of the block or sequence
     */
    public void setLocation(int newLocation) {
        location = newLocation;
    }


    /**
     * Setter for the size field
     * 
     * @param newSize
     *            The new size of the block or sequence
     */
    public void setSize(int newSize) {
        size = newSize;
    }


    @Override
    public String toString() {
        return location + " " + size;
    }

}
