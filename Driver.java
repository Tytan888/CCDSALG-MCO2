import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.IOException;
import java.io.FileWriter;

public class Driver {

    private static final long FNV1_64_INIT = 0xcbf29ce484222325L;
    private static final long FNV1_PRIME_64 = 1099511628211L;
    private static int elementsInMap;

    public static long fnv1aHash(byte[] data, int length) {
        long hash = FNV1_64_INIT;
        for (int i = 0; i < length; i++) {
            hash ^= (data[i] & 0xff);
            hash *= FNV1_PRIME_64;
        }
        
        return hash;
    }

    public static void insertHT(HashSlot[] map, int index, String thisKey) {
        if (map[index] != null) {
            int arrayListIndex = map[index].searchKey(thisKey);
            if (arrayListIndex != -1) {
                map[index].incrementCount(arrayListIndex);
            } else {
                map[index].addKey(thisKey);
                elementsInMap++;
            }
        } else {
            map[index] = new HashSlot(thisKey);
            elementsInMap++;
        }
    }

    public static void searchHT1(String input, int k, boolean doPrint, FileWriter myWriter) throws IOException {
        elementsInMap = 0;
        int arrSize = 101;
        HashSlot[] map = new HashSlot[arrSize];

        for (int i = 0; i < input.length() - k + 1; i++) {
            String thisKey = input.substring(i, i + k);

            int index = thisKey.hashCode() % (arrSize);
            if (index < 0) {
                index += arrSize;
            }

            insertHT(map, index, thisKey);

            // Resize and rehash array if load factor exceeds 0.75
            if ((float)elementsInMap/(float)arrSize > 0.75) {
                // Increases array size by 2 times
                arrSize *= 2;
                
                HashSlot[] tempMap = new HashSlot[arrSize];

                // Rehashes the elements of the array and places them into tempMap
                for (int j = 0; j < arrSize; j++) {
                    if (map[j] != null) {
                        for (String entry : map[j].getKeys()) {
                            index = thisKey.hashCode() % (arrSize);
                            if (index < 0) {
                                index += arrSize;
                            }
                            insertHT(tempMap, index, entry);
                        }
                    }
                    // Clones tempMap to main map
                    map = tempMap;
                }
            }
        }

        int collisions = 0;

        for (HashSlot hashSlot : map) {
            if(hashSlot != null && !hashSlot.getKeys().isEmpty()) {
                collisions = collisions + hashSlot.getKeys().size() - 1;
            }
        }

        if(doPrint){
            myWriter.write("Collisions: " + collisions + "\n");
            myWriter.write("Array Size: " + arrSize + "\n");
            myWriter.write("Load Factor: " + String.format("%.02f", (float)elementsInMap/(float)arrSize) + "\n");
            for (HashSlot entry : map) {
                if (entry != null) {
                    for (int i = 0; i < entry.getKeys().size(); i++) {
                        myWriter.write("[" + entry.getKeys().get(i) + "] " + entry.getCounts().get(i) + "\n");
                    }
                }
            }
        }
    }

    public static void searchHT2(String input, int k, boolean doPrint, FileWriter myWriter) throws IOException {
        elementsInMap = 0;
        int arrSize = 101;
        HashSlot[] map = new HashSlot[arrSize];

        for (int i = 0; i < input.length() - k + 1; i++) {
            String thisKey = input.substring(i, i + k);

            int index = (int)fnv1aHash(thisKey.getBytes(), k) % (arrSize);
            if (index < 0) {
                index += arrSize;
            }

            insertHT(map, index, thisKey);

            // Resize and rehash array if load factor exceeds 0.75
            if ((float)elementsInMap/(float)arrSize > 0.75) {
                // Increases array size by 2 times
                arrSize *= 2;
                
                HashSlot[] tempMap = new HashSlot[arrSize];

                // Rehashes the elements of the array and places them into tempMap
                for (int j = 0; j < arrSize; j++) {
                    if (map[j] != null) {
                        for (String entry : map[j].getKeys()) {
                            index = (int)fnv1aHash(entry.getBytes(), k) % (arrSize);
                            if (index < 0) {
                                index += arrSize;
                            }
                            insertHT(tempMap, index, entry);
                        }
                    }
                    // Clones tempMap to main map
                    map = tempMap;
                }
            }
        }

        int collisions = 0;

        for (HashSlot hashSlot : map) {
            if(hashSlot != null && !hashSlot.getKeys().isEmpty()) {
                collisions = collisions + hashSlot.getKeys().size() - 1;
            }
        }

        if(doPrint){
            myWriter.write("Collisions: " + collisions + "\n");
            myWriter.write("Array Size: " + arrSize + "\n");
            myWriter.write("Load Factor: " + String.format("%.02f", (float)elementsInMap/(float)arrSize) + "\n");
            for (HashSlot entry : map) {
                if (entry != null) {
                    for (int i = 0; i < entry.getKeys().size(); i++) {
                        myWriter.write("[" + entry.getKeys().get(i) + "] " + entry.getCounts().get(i) + "\n");
                    }
                }
            }
        }
    }

    public static void searchBST(String input, int k, boolean doPrint, FileWriter myWriter) throws IOException {
        BST mainBST = null;
        for (int i = 0; i < input.length() - k + 1; i++) {
            String thisKey = input.substring(i, i + k);
            if (mainBST == null) {
                mainBST = create(thisKey);
            } else {
                BST targetBST = mainBST.search(thisKey);
                if(targetBST == null){
                    mainBST.insert(thisKey);
                } else {
                    targetBST.incrementCount();
                }
            }
        }
        if(doPrint){
            mainBST.printAll(myWriter);
        }
    }

    public static BST create(String root) {
        return new BST(root);
    }

    public static void main(String args[]) {

        // This indicates whether the result should be printed or not.
        boolean doPrint = true;
        // This stores the name of the file containing the inputs and outputs.
        String inputFile = "Testing.txt";
        String outputFile = "output.txt";

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
