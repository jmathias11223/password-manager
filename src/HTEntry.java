import java.io.IOException;

/**
 * Class that represents each entry in the HashTable
 * 
 * @author Justin Mathias
 * @version 2020.12.01
 *
 */
public class HTEntry {
    private Pair seqID;
    private Pair sequence;
    private EntryStatus status;

    /**
     * Default constructor
     */
    public HTEntry() {
        status = EntryStatus.OPEN;
    }


    /**
     * Constructor that allows for prefilling of fields
     * 
     * @param seqID
     *            Sequence ID Pair object
     * @param sequence
     *            Sequence Pair object
     */
    public HTEntry(Pair seqID, Pair sequence) {
        this.seqID = seqID;
        this.sequence = sequence;
        status = EntryStatus.FILLED;
    }


    /**
     * Gets the Pair object for the sequence ID
     * 
     * @return seqID Pair object for sequence ID
     */
    public Pair getSeqID() {
        return seqID;
    }


    /**
     * Gets the Pair object for the sequence itself
     * 
     * @return sequence Pair object for sequence
     */
    public Pair getSequence() {
        return sequence;
    }


    /**
     * Gets the status of the entry
     * 
     * @return status The status of the entry
     */
    public EntryStatus getStatus() {
        return status;
    }


    /**
     * Sets the sequence ID to param newID
     * 
     * @param newID
     *            The new sequence ID Pair
     */
    public void setSeqID(Pair newID) {
        seqID = newID;
    }


    /**
     * Sets the sequence to param newSequence
     * 
     * @param newSequence
     *            The new sequence Pair
     */
    public void setSequence(Pair newSequence) {
        sequence = newSequence;
    }


    /**
     * Tombstones the entry
     */
    public void toTombstone() {
        status = EntryStatus.TOMBSTONE;
    }


    /**
     * Used for refilling a tombstoned entry easily
     * 
     * @param seqID2
     *            New sequence ID Pair
     * @param sequence2
     *            New sequence Pair
     */
    public void fill(Pair seqID2, Pair sequence2) {
        this.seqID = seqID2;
        this.sequence = sequence2;
        status = EntryStatus.FILLED;
    }


    @Override
    public String toString() {
        String sequenceID = "";
        try {
            sequenceID = MemoryManager.getSequence(seqID);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sequenceID + ": hash slot [";
    }
}
