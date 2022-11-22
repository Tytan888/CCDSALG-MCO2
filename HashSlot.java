import java.util.ArrayList;

public class HashSlot {
    
    private ArrayList<String> keys;
    private ArrayList<Integer> counts;

    public HashSlot(String thisKey) {
        keys = new ArrayList<String>();
        counts = new ArrayList<Integer>();
        this.keys.add(thisKey);
        this.counts.add(1);
    }

    public ArrayList<String> getKeys() {
        return keys;
    }

    public ArrayList<Integer> getCounts() {
        return counts;
    }

    public int searchKey(String thisKey) {
        return keys.indexOf(thisKey);
    }

    public void incrementCount(int index) {
        counts.set(index, counts.get(index) + 1);
    }

    public void addKey(String thisKey) {
        this.keys.add(thisKey);
        this.counts.add(1);
    }

}
