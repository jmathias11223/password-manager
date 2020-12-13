import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Comparator;
import java.util.LinkedList;

/**
 * This class takes care of storing all the Passwords in a memory file
 * 
 * @author Justin Mathias
 * @version 2020.12.02
 *
 */
public class MemoryManager {
    private static LinkedList<Pair> freeSpots = new LinkedList<Pair>();
    private static RandomAccessFile memory = PasswordManager.getMemoryFile();

    /**
     * This method inserts a given String of characters into the memory file
     * 
     * @param data
     *            The DNA sequence or ID to be inserted
     * @return pair
     *         The Pair object that represents the location and length of the
     *         data being inserted
     * @throws IOException
     */
    public static Pair insert(String data) throws IOException {
        int fileLoc = 0;
        byte[] toInsert = Converter.toASCII(data);
        if (freeSpots.size() == 0) {
            fileLoc = (int)memory.length();
            memory.seek(fileLoc);
            memory.write(toInsert);
        }
        else {
            // Find the best spot for the bytes to be inserted
            Pair bestSpot = null;
            int index = -1;
            for (int i = 0; i < freeSpots.size(); i++) {
                if (freeSpots.get(i).getSize() >= toInsert.length) {
                    if (bestSpot == null) {
                        bestSpot = freeSpots.get(i);
                        index = i;
                    }
                    else if (bestSpot.getSize() > freeSpots.get(i).getSize()) {
                        bestSpot = freeSpots.get(i);
                        index = i;
                    }
                }
            }

            // Insert the byte array in the best spot for it
            if (bestSpot == null) {
                fileLoc = (int)memory.length();
                memory.seek(fileLoc);
                memory.write(toInsert);
            }
            else {
                fileLoc = bestSpot.getLocation();
                memory.seek(fileLoc);
                memory.write(toInsert);
                if (bestSpot.getSize() == toInsert.length) {
                    freeSpots.remove(bestSpot);
                }
                else {
                    freeSpots.remove(bestSpot);
                    Pair update = new Pair(bestSpot.getLocation()
                        + toInsert.length, bestSpot.getSize()
                            - toInsert.length);
                    freeSpots.add(index, update);
                }
            }

        }
        Pair pair = new Pair(fileLoc, data.length());
        return pair;
    }


    /**
     * Gets the sequence of characters associated with a Pair object
     * 
     * @param pair
     *            The Pair to be looked up in the memory file
     * @return The sequence associated with the Pair
     * @throws IOException
     */
    public static String getSequence(Pair pair) throws IOException {
        memory.seek(pair.getLocation());
        int addSize = pair.getSize() * 2 % 8;
        byte[] arr = new byte[pair.getSize() * 2 + (8 - addSize)];
        memory.read(arr);
        return Converter.toSequence(arr, pair.getSize());
    }


    /**
     * Removes a certain set of bytes from the memory file
     * 
     * @param pair
     *            The Object that represents the bytes to remove
     * @return null if object not found, the sequence if object found
     * @throws IOException
     */
    public static String remove(Pair pair) throws IOException {
        if (pair == null || pair.getLocation() > memory.length()) {
            return null;
        }
        int padding = (pair.getSize() * 2) % 8;
        String ret = getSequence(pair);
        if (ret != null) {
            updateFreeSpots(new Pair(pair.getLocation(), (pair.getSize() * 2
                + ((8 - padding) % 8)) / 8));
        }
        return ret;
    }


    /**
     * Updates the freeSpots list by adding the pair passed as parameter and
     * checking for various conditions
     * 
     * @param pair
     *            The pair to be added
     * @throws IOException
     */
    private static void updateFreeSpots(Pair pair) throws IOException {
        if (freeSpots.size() == 0) {
            freeSpots.add(pair);
            return;
        }
        // Insert pair into proper spot
        boolean isInserted = false;
        for (int i = 0; i < freeSpots.size(); i++) {
            if (pair.getLocation() <= freeSpots.get(i).getLocation()) {
                freeSpots.add(i, pair);
                i = freeSpots.size();
                isInserted = true;
            }
        }
        // Handles case where pair has greatest location value
        if (!isInserted) {
            freeSpots.add(pair);
        }
        checkForCombinations();
    }


    /**
     * Getter method for the free spots list
     * 
     * @return The free spots list
     */
    public static LinkedList<Pair> getFreeSpots() {
        return freeSpots;
    }


    /**
     * String representation of the free block list
     * 
     * @return The String associated with the free block list
     */
    public static String listToString() {
        StringBuilder str = new StringBuilder();
        int count = 1;
        boolean firstEntry = true;
        freeSpots.sort(new Comparator<Pair>() {
            @Override
            public int compare(Pair o1, Pair o2) {
                return o1.getLocation() - o2.getLocation();
            }
        });
        for (Pair p : freeSpots) {
            if (!firstEntry) {
                str.append("\n");
            }
            str.append("[Block " + count + "] Starting Byte Location: " + p
                .getLocation() + ", Size " + p.getSize() + " bytes");
            count++;
            firstEntry = false;
        }
        return str.toString();
    }


    /**
     * Checks for any possible combinations of free blocks if there are any
     * adjacent to each other
     * 
     * @throws IOException
     */
    public static void checkForCombinations() throws IOException {
        for (int i = freeSpots.size() - 2; i >= 0; i--) {
            Pair p1 = freeSpots.get(i);
            Pair p2 = freeSpots.get(i + 1);
            // Check if two free spots are adjacent
            if (p1.getLocation() + p1.getSize() == p2.getLocation()) {
                freeSpots.remove(p1);
                freeSpots.remove(p2);
                freeSpots.add(i, new Pair(p1.getLocation(), p1.getSize() + p2
                    .getSize()));
            }
        }
        // Checks if the last entry in the free blocks list is at EOF
        Pair lastPair = freeSpots.get(freeSpots.size() - 1);
        if (lastPair.getLocation() + lastPair.getSize() >= memory.length()) {
            freeSpots.remove(lastPair);
            memory.setLength(lastPair.getLocation());
        }
    }


    /**
     * Sets the memory file to a new file name, used only for testing
     * 
     * @param fileName
     *            New file name
     * @throws IOException
     *             Thrown when file has issues reading or writing
     */
    public static void setMemoryFile(String fileName) throws IOException {
        memory = new RandomAccessFile(fileName, "rw");
        memory.setLength(0);
    }
}
