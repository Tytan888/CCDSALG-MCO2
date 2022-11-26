import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.IOException;
import java.io.FileWriter;

/**
 * This Driver class utilizes both the HashSlot class and BST class to calculate
 * for the k-mer distribution of a given DNA sequence using HashTables and
 * Binary Search Algortithms respectively. The two hashing functions used in the
 * implementation below are Java's in-built .hashCode method and the FNV-1a hash
 * function. This program takes in an input txt file, utilizes the two
 * aforementioned methods to solve the program, then produces an output txt file
 * accordingly.
 */
public class Driver {

    private static final long FNV1_64_INIT = 0xcbf29ce484222325L;
    private static final long FNV1_PRIME_64 = 1099511628211L;
    private static int elementsInMap;

    /**
     * This method is essentially the FNV-1a hash function in its entirety.
     * 
     * @param data   the data to the hashed
     * @param length the length of the data to be hashed
     * @return the hash code produced by the input data
     */
    public static long fnv1aHash(byte[] data, int length) {
        long hash = FNV1_64_INIT;
        for (int i = 0; i < length; i++) {
            hash ^= (data[i] & 0xff);
            hash *= FNV1_PRIME_64;
        }

        return hash;
    }

    /**
     * This method facilities the creation of new HashSlots as well as altering the
     * existing HashSlots if the need arises.
     * 
     * @param map   the hashtable containing all the HashSlots
     * @param index the index of the slot to be inserted into
     * @param key   the key to be inserted or altered into the slot
     * @param count the count to be inserted or incremented into the slot
     */
    public static void insertHT(HashSlot[] map, int index, String key, int count) {
        // If the table at index contains no HashSlot, create that new HashSlot.
        if (map[index] == null) {
            map[index] = new HashSlot(key, count);
            elementsInMap++;
        }
        // If the table at index contains a HashSlot...
        else {
            int arrayListIndex = map[index].searchKey(key);

            // If the key is not within the HashSlot, then add that key.
            if (arrayListIndex == -1) {
                map[index].addKey(key, count);
                elementsInMap++;
            }
            // If the key is within the HashSlot, simply increment.
            else {
                map[index].incrementCount(arrayListIndex, count);
            }
        }
    }

    /**
     * This method utilizes a hashtable approach to solving the specification
     * problem, using Java's in-built .hashCode function as the hashing function and
     * our own custom HashSlot class.
     * 
     * @param input    the input DNA sequence String to be analyzed
     * @param k        the length of all substrings to be analyzed
     * @param doPrint  whether or not to print the results
     * @param myWriter the FileWriter object to be used to print to the output file
     * @throws IOException
     */
    public static void searchHT1(String input, int k, boolean doPrint, FileWriter myWriter) throws IOException {

        elementsInMap = 0;

        // Initial hashtable size of 101 as it is a nice prime number.
        int arrSize = 101;
        HashSlot[] map = new HashSlot[arrSize];

        // For each substring of the input of length k...
        for (int i = 0; i < input.length() - k + 1; i++) {
            String thisKey = input.substring(i, i + k);

            int index = thisKey.hashCode() % (arrSize);
            if (index < 0) {
                index += arrSize;
            }

            // Insert thisKey into the hashtable appropriately.
            insertHT(map, index, thisKey, 1);

            // Resize and rehash array if load factor exceeds 0.75.
            if ((float) elementsInMap / (float) arrSize > 0.75) {
                // Multiplies the array size by 2 times.
                arrSize *= 2;

                HashSlot[] tempMap = new HashSlot[arrSize];

                elementsInMap = 0;
                // Rehashes the elements of the array and places them into tempMap.
                for (int j = 0; j < arrSize / 2; j++) {
                    if (map[j] != null) {
                        for (int l = 0; l < map[j].getKeys().size(); l++) {
                            String entry = map[j].getKeys().get(l);
                            index = entry.hashCode() % (arrSize);
                            if (index < 0) {
                                index += arrSize;
                            }
                            insertHT(tempMap, index, entry, map[j].getCounts().get(l));
                        }
                    }
                }
                // Clones tempMap to main map to finish the rehashing process.
                map = tempMap;
            }

        }

        // Count the number of collisinos within the hashtable.
        int collisions = 0;
        for (HashSlot hashSlot : map) {
            if (hashSlot != null && !hashSlot.getKeys().isEmpty()) {
                collisions = collisions + hashSlot.getKeys().size() - 1;
            }
        }

        // Print all results from the hashtable.
        if (doPrint) {
            myWriter.write("Collisions: " + collisions + "\n");
            myWriter.write("Array Size: " + arrSize + "\n");
            myWriter.write("Load Factor: " + String.format("%.02f", (float) elementsInMap / (float) arrSize) + "\n");
            myWriter.write("Number of Elements: " + elementsInMap + "\n");

            for (HashSlot entry : map) {
                if (entry != null) {
                    for (int i = 0; i < entry.getKeys().size(); i++) {
                        myWriter.write("[" + entry.getKeys().get(i) + "] " + entry.getCounts().get(i) + "\n");
                    }
                }
            }
        }
    }

    /**
     * This method utilizes a hashtable approach to solving the specification
     * problem, using the FNV-1a algorithm as the hashing function and our own
     * custom HashSlot class.
     * 
     * @param input    the input DNA sequence String to be analyzed
     * @param k        the length of all substrings to be analyzed
     * @param doPrint  whether or not to print the results
     * @param myWriter the FileWriter object to be used to print to the output file
     * @throws IOException
     */
    public static void searchHT2(String input, int k, boolean doPrint, FileWriter myWriter) throws IOException {
        elementsInMap = 0;

        // Initial hashtable size of 101 as it is a nice prime number.
        int arrSize = 101;
        HashSlot[] map = new HashSlot[arrSize];

        // For each substring of the input of length k...
        for (int i = 0; i < input.length() - k + 1; i++) {
            String thisKey = input.substring(i, i + k);

            int index = (int) fnv1aHash(thisKey.getBytes(), k) % (arrSize);
            if (index < 0) {
                index += arrSize;
            }

            // Insert thisKey into the hashtable appropriately.
            insertHT(map, index, thisKey, 1);

            // Resize and rehash array if the load factor exceeds 0.75.
            if ((float) elementsInMap / (float) arrSize > 0.75) {
                // Multiplies the array size by 2 times.
                arrSize *= 2;

                HashSlot[] tempMap = new HashSlot[arrSize];

                elementsInMap = 0;
                // Rehashes the elements of the array and places them into tempMap.
                for (int j = 0; j < arrSize / 2; j++) {
                    if (map[j] != null) {
                        for (int l = 0; l < map[j].getKeys().size(); l++) {
                            String entry = map[j].getKeys().get(l);
                            index = (int) fnv1aHash(entry.getBytes(), k) % (arrSize);
                            if (index < 0) {
                                index += arrSize;
                            }
                            insertHT(tempMap, index, entry, map[j].getCounts().get(l));
                        }
                    }
                }
                // Clones tempMap to main map to finish the rehashing process.
                map = tempMap;
            }
        }

        // Count the number of collisinos within the hashtable.
        int collisions = 0;
        for (HashSlot hashSlot : map) {
            if (hashSlot != null && !hashSlot.getKeys().isEmpty()) {
                collisions = collisions + hashSlot.getKeys().size() - 1;
            }
        }

        // Print all results from the hashtable.
        if (doPrint) {
            myWriter.write("Collisions: " + collisions + "\n");
            myWriter.write("Array Size: " + arrSize + "\n");
            myWriter.write("Load Factor: " + String.format("%.02f", (float) elementsInMap / (float) arrSize) + "\n");
            myWriter.write("Number of Elements: " + elementsInMap + "\n");

            for (HashSlot entry : map) {
                if (entry != null) {
                    for (int i = 0; i < entry.getKeys().size(); i++) {
                        myWriter.write("[" + entry.getKeys().get(i) + "] " + entry.getCounts().get(i) + "\n");
                    }
                }
            }
        }
    }

    /**
     * This method utilizes a Binary Search Tree approach to solving the
     * specification problem, using our own custom BST class to build the full
     * Binary Search Tree.
     * 
     * @param input    the input DNA sequence String to be analyzed
     * @param k        the length of all substrings to be analyzed
     * @param doPrint  whether or not to print the results
     * @param myWriter the FileWriter object to be used to print to the output file
     * @throws IOException
     */
    public static void searchBST(String input, int k, boolean doPrint, FileWriter myWriter) throws IOException {

        // Create a main Binary Search Tree.
        BST mainBST = null;

        // For each substring of the input of length k...
        for (int i = 0; i < input.length() - k + 1; i++) {
            String thisKey = input.substring(i, i + k);

            // If mainBST has yet to be initialized, initialize it.
            if (mainBST == null) {
                mainBST = create(thisKey);
            } else {
                // Search if thisKey is within the BST already.
                BST targetBST = mainBST.search(thisKey);

                // If not in the BST, then insert a new node.
                if (targetBST == null) {
                    mainBST.insert(thisKey);
                }
                // If already in the BST, then simply increment.
                else {
                    targetBST.incrementCount();
                }
            }
        }

        // Print all results from the BST.
        if (doPrint) {
            mainBST.printAll(myWriter);
        }
    }

    /**
     * This method simply creates a Binary Search Tree with the parameter root as
     * the key to its root.
     * 
     * @param root the key of the root of the Binary Search Tree to be created.
     * @return a BST with a root having a key specified by the parameter root
     */
    public static BST create(String root) {
        return new BST(root);
    }

    public static void main(String args[]) {

        // This indicates whether the result should be printed or not.
        boolean doPrint = true;
        // This stores the name of the file containing the inputs and outputs.
        String inputFile = "in.txt";
        String outputFile = "out1.txt";

        // First, try creating the output file from the name specified.
        try {
            File outFile = new File(outputFile);
            if (outFile.createNewFile()) {
                FileWriter myWriter = new FileWriter(outFile);

                // If successful, then try opening the input file.
                File inFile = new File(inputFile);
                Scanner sc = new Scanner(inFile);
                int i = 0;

                // Perform both sorts on each test case.
                while (sc.hasNextLine()) {
                    String input = sc.nextLine();
                    i++;
                    myWriter.write("\n___________________________\n\nTest Case #" + i + "\n");

                    myWriter.write("\nHash Table 1..." + "\n");
                    for (int k = 5; k < 8; k++) {
                        myWriter.write("\nk = " + k + "\n");
                        // Starts the timer.
                        long startHT1SearchTime = System.nanoTime();

                        searchHT1(input, k, doPrint, myWriter);

                        // Ends timer and subtracts it with the start time to get the total time.
                        long totalHT1SearchTime = System.nanoTime() - startHT1SearchTime;
                        // Prints out time in nanoseconds.
                        myWriter.write("\n" + totalHT1SearchTime + " Nanoseconds" + "\n");
                    }

                    myWriter.write("\nHash Table 2..." + "\n");
                    for (int k = 5; k < 8; k++) {
                        myWriter.write("\nk = " + k + "\n");
                        // Starts the timer.
                        long startHT2SearchTime = System.nanoTime();

                        searchHT2(input, k, doPrint, myWriter);

                        // Ends timer and subtracts it with the start time to get the total time.
                        long totalHT2SearchTime = System.nanoTime() - startHT2SearchTime;
                        // Prints out time in nanoseconds.
                        myWriter.write("\n" + totalHT2SearchTime + " Nanoseconds" + "\n");
                    }

                    myWriter.write("\nBinary Search Table...\n");
                    for (int k = 5; k < 8; k++) {
                        myWriter.write("\nk = " + k + "\n");
                        // Starts the timer.
                        long startBSTSearchTime = System.nanoTime();

                        searchBST(input, k, doPrint, myWriter);

                        // Ends timer and subtracts it with the start time to get the total time.
                        long totalBSTSearchTime = System.nanoTime() - startBSTSearchTime;
                        // Prints out time in nanoseconds.
                        myWriter.write("\n" + totalBSTSearchTime + " Nanoseconds" + "\n");
                    }
                }
                sc.close();
                myWriter.close();
            } else {
                System.out.println("File already exists. Please try again.");
            }

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred. Please try again.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("An error occurred. Please try again.");
            e.printStackTrace();
        }

    }
}
