import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.IOException;
import java.io.FileWriter;

public class Driver {

    public static void searchHT1(String input, int k, boolean doPrint, FileWriter myWriter) throws IOException {
        HashMap<Integer, HT> map = new HashMap<Integer, HT>();
        int collisions = 0;
        for (int i = 0; i < input.length() - k + 1; i++) {
            String thisKey = input.substring(i, i + k);

            if (map.containsKey(thisKey.hashCode()) && map.get(thisKey.hashCode()).getKey().equals(thisKey)) {
                map.get(thisKey.hashCode()).incrementCount();
            } else if (map.containsKey(thisKey.hashCode()) && !map.get(thisKey.hashCode()).getKey().equals(thisKey)) {
                collisions++;
            } else {
                map.put(thisKey.hashCode(), new HT(thisKey));
            }
        }

        if(doPrint){
            myWriter.write("Collisions: " + collisions + "\n");
            for (Map.Entry<Integer, HT> entry : map.entrySet()) {
                myWriter.write("[" + entry.getValue().getKey() + "] " + entry.getValue().getCount() + "\n");
            }
        }
    }

    // public static void searchHT2(String input, int k, boolean doPrint, FileWriter myWriter) throws IOException {
    //     int arrSize = 101;
    //     HT[] map = new HT[arrSize];
    //     int itemsInArray = 0;

    //     for (int i = 0; i < input.length() - k + 1; i++) {
    //         String thisKey = input.substring(i, i + k);

    //         int index = (int)HT.fnv1aHash(thisKey.getBytes(), k) % arrSize;

    //         if (map[index] == null) {
    //             map[index] = new HT(thisKey);
    //             itemsInArray++;
    //         }

    //         // Resize and rehash array if load factor exceeds 0.75
    //         if ((float)itemsInArray/(float)arrSize > 0.75) {
                
    //         }
    //     }

    //     if(doPrint){
    //     }
    // }

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

                    
                    myWriter.write("\nHash Table..." + "\n");
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
