import java.io.IOException;

/**
 * Implementation of the generic HashTableInterface using String and HTEntry as
 * key, value pair
 * 
 * @author Justin Mathias
 * @version 2020.12.04
 *
 */
public class HashTable implements HashTableInterface<String, HTEntry> {
    private HTEntry[] table;
    private int numEntries;

    /**
     * Constructor for the HashTable
     * 
     * @param size
     *            Size of the HashTable, must be a multiple of 32
     */
    public HashTable(int size) {
        table = new HTEntry[size];
        numEntries = 0;
    }


    @Override
    public int insert(String key, HTEntry value) {
        int hash = sfold(key, table.length);
        int count = 0;
        int indexToInsert = -1;
        while (table[hash] != null && count <= 32) {
            if (table[hash].getStatus() == EntryStatus.TOMBSTONE
                && indexToInsert == -1) {
                indexToInsert = hash;
            }
            else {
                try {
                    if (table[hash].getStatus() != EntryStatus.TOMBSTONE && key
                        .equals(MemoryManager.getSequence(table[hash]
                            .getSeqID()))) {
                        return 1;
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            hash = nextSlot(hash);
            count++;
        }
        if (indexToInsert != -1) {
            table[indexToInsert].fill(value.getSeqID(), value.getSequence());
            numEntries++;
            return 0;
        }
        if (count >= 32) {
            return -1;
        }
        table[hash] = value;
        numEntries++;
        return 0;
    }


    @Override
    public boolean remove(String key) {
        int index = getIndex(key);
        if (index == -1) {
            return false;
        }
        table[index].toTombstone();
        numEntries--;
        return true;
    }


    @Override
    public int getIndex(String key) {
        int hash = sfold(key, table.length);
        int count = 0;
        while (table[hash] != null && count <= 32) {
            try {
                if (table[hash].getStatus() != EntryStatus.TOMBSTONE && key
                    .equals(MemoryManager.getSequence(table[hash]
                        .getSeqID()))) {
                    return hash;
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            hash = nextSlot(hash);
            count++;
        }
        return -1;
    }


    @Override
    public HTEntry get(String key) {
        int index = getIndex(key);
        if (index == -1) {
            return null;
        }
        return table[index];
    }


    @Override
    public boolean isEmpty() {
        return numEntries == 0;
    }


    /**
     * Implements circular functionality for buckets to get the next slot in the
     * bucket
     * 
     * @param index
     *            The current index
     * @return The next index
     */
    private int nextSlot(int index) {
        if ((index + 1) % 32 != 0) {
            return index + 1;
        }
        return index + 1 - 32; // +1 to go to next index, -32 to circle around
                               // to the beginning of the bucket
    }


    /**
     * Hashing function
     * 
     * @param s
     *            Key to be converted to an index
     * @param m
     *            Size of the HashTable
     * @return Index to insert the key, value pair
     */
    private int sfold(String s, int m) {
        int intLength = s.length() / 4;
        long sum = 0;
        for (int j = 0; j < intLength; j++) {
            char[] c = s.substring(j * 4, (j * 4) + 4).toCharArray();
            long mult = 1;
            for (int k = 0; k < c.length; k++) {
                sum += c[k] * mult;
                mult *= 256;
            }
        }

        char[] c = s.substring(intLength * 4).toCharArray();
        long mult = 1;
        for (int k = 0; k < c.length; k++) {
            sum += c[k] * mult;
            mult *= 256;
        }

        sum = (sum * sum) >> 8;
        return (int)(Math.abs(sum) % m);
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        boolean firstEntry = true;
        for (int i = 0; i < table.length; i++) {
            HTEntry entry = table[i];
            if (entry != null && entry.getStatus() != EntryStatus.TOMBSTONE) {
                if (!firstEntry) {
                    str.append("\n");
                }
                str.append(entry.toString() + i + "]");
                firstEntry = false;
            }
        }
        return str.toString();
    }
}
