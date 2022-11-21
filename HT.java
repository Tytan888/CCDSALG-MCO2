import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class HT {
    
    private static final long FNV1_64_INIT = 0xcbf29ce484222325L;
    private static final long FNV1_PRIME_64 = 1099511628211L;

    private String key;
    private int count;

    public HT(String key) {
        this.key = key;
        this.count = 1;
    }

    /**
     * FNV1a 64 bit variant.
     *
     * @param data   - input byte array
     * @param length - length of array
     * @param size - size of HashMap
     * @return - hashcode
     */
    public static long fnv1aHash(byte[] data, int length) {
        long hash = FNV1_64_INIT;
        for (int i = 0; i < length; i++) {
            hash ^= (data[i] & 0xff);
            hash *= FNV1_PRIME_64;
        }
        
        return hash;
    }

    public String getKey() {
        return key;
    }

    public int getCount() {
        return count;
    }

    public void incrementCount() {
        this.count++;
    }
}
