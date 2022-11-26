import java.util.ArrayList;

/**
 * This HashSlot class replicates the functions of a single slot within a hash
 * table that utilizes chaining as its collision resolution method. In
 * accordance with this, this class utilizes ArrayLists to store multiple keys
 * (and their corresponding satelite data) simultaneously.
 */
public class HashSlot {

    private ArrayList<String> keys;
    private ArrayList<Integer> counts;

    /**
     * This constructor initializes a HashSlot with the given key and count
     * provided within the parameters.
     * 
     * @param key   the first key of this slot within the hash table
     * @param count the first key of this slot within the hash table
     */
    public HashSlot(String key, int count) {
        keys = new ArrayList<String>();
        counts = new ArrayList<Integer>();
        this.keys.add(key);
        this.counts.add(count);
    }

    /**
     * This method simply returns the ArrayList of keys stored within this slot.
     * 
     * @return the ArrayList of keys stored within this slot
     */
    public ArrayList<String> getKeys() {
        return keys;
    }

    /**
     * This method simply returns the ArrayList of counts stored within this slot.
     * 
     * @return the ArrayList of counts stored within this slot
     */
    public ArrayList<Integer> getCounts() {
        return counts;
    }

    /**
     * This method receives a certain key from the parameter and returns
     * the index at which said key is found within the ArrayList of keys, returning
     * -1 if it is not found.
     * 
     * @param key the key to be searched within the ArrayList of keys
     * 
     * @return the index at which key is found, -1 if not found
     */
    public int searchKey(String key) {
        return keys.indexOf(key);
    }

    /**
     * This method increments a given count in the ArrayList of counts specified
     * by the parameter index. The amount to increment is indicated by the parameter
     * add.
     * 
     * @param index the index of the count to be incremented
     * @param add   the amount to be incremented to the specified count
     */
    public void incrementCount(int index, int add) {
        counts.set(index, counts.get(index) + add);
    }

    /**
     * This methods adds a given key and a corresponding count to their
     * corresponding ArrayLists.
     * 
     * @param key   the key to be added to the ArrayList of keys
     * @param count the count to be added to the ArrayList of counts
     */
    public void addKey(String key, int count) {
        this.keys.add(key);
        this.counts.add(count);
    }

}
