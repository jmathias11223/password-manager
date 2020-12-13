/**
 * Enum to track the status of each entry in the HashTable
 * 
 * @author Justin Mathias
 * @version 2020.12.03
 *
 */
public enum EntryStatus {
    /**
     * Flag for HTEntry when it is open, tombstones, or filled
     */
    OPEN, TOMBSTONE, FILLED;
}
