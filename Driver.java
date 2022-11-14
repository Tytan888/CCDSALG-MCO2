import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.IOException;
import java.io.FileWriter;

public class Driver {

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
            //myWriter.write("i: " + i + "\n");
            //mainBST.printAll(myWriter);
            //myWriter.write("\n");
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
        String inputFile = "input.txt";
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

                    /*
                     * myWriter.write("Hash Table..." + "\n");
                     * 
                     * // Starts the timer.
                     * long startSelectionTime = System.nanoTime();
                     * 
                     * selectionSort(input, doPrint, myWriter);
                     * 
                     * // Ends timer and subtracts it with the start time to get the total time.
                     * long totalSelectionTime = System.nanoTime() - startSelectionTime;
                     * // Prints out time in nanoseconds.
                     * myWriter.write("\n" + totalSelectionTime + " Nanoseconds" + "\n");
                     * 
                     */

                    myWriter.write("\nBinary Search Table...\n");
                    for (int k = 5; k < 8; k++) {
                        myWriter.write("\nk = " + k + "\n");
                        // Starts the timer.
                        long startMergeTime = System.nanoTime();

                        searchBST(input, k, doPrint, myWriter);

                        // Ends timer and subtracts it with the start time to get the total time.
                        long totalMergeTime = System.nanoTime() - startMergeTime;
                        // Prints out time in nanoseconds.
                        myWriter.write("\n" + totalMergeTime + " Nanoseconds" + "\n");
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
