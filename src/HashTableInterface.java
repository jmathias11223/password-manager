
/**
 * Generic Interface for the HashTable class
 * 
 * @author Justin Mathias
 * @version 2020.12.03
 *
 * @param <K>
 *            Key parameter
 * @param <V>
 *            Value parameter
 */
public interface HashTableInterface<K extends Comparable<K>, V> {

    /**
     * Inserts the key, value pair into the HashTable
     * 
     * @param key
     *            The key to be inserted
     * @param value
     *            The value to be inserted
     * @return 0 if insertion successful
     *         -1 if bucket is full
     *         1 if value already exists in table
     */
    int insert(K key, V value);


    /**
     * Removes the key, value pair associated with the given key
     * 
     * @param key
     *            The key to be removed
     * @return True if removal successful, false if key not found
     */
    boolean remove(K key);


    /**
     * Returns the index of the given key, if it exists in the HashTable
     * 
     * @param key
     *            The key to look for
     * @return The index of the key if exists, if not, returns -1
     */
    int getIndex(K key);


    /**
     * Returns the value associated with the key in the table
     * 
     * @param key
     *            The key to search with
     * @return The value associated with the key
     */
    V get(K key);


    /**
     * Checks if the table is empty
     * 
     * @return True if empty, false if not
     */
    boolean isEmpty();
}
