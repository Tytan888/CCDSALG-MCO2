import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;

public class Generator {

    /**
     * This method takes in n in the form of an int,
     * then proceeds to generate a random String with
     * length n with only the characters 'a', 'c',
     * 't', and 'g'.
     * 
     * @param n length for the String to be generated
     */
    public static String generateRandomString(int n) {
        // Choose a character randomly from this String.
        String alphaString = "acgt";

        // Create StringBuffer size of AlphaNumericString.
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // Generate a random number between
            // 0 to AlphaNumericString variable length.
            int index = (int) (alphaString.length() * Math.random());

            // Add characters one by one at end of sb.
            sb.append(alphaString.charAt(index));
        }

        return sb.toString();
    }

    public static void main(String args[]) {

        // This represents the input size of string
        // and the number of test cases to generate.
        int n, cases;

        // This stores the name of the file to be created.
        String fileName;

        // Get all appropriate information for generation.
        Scanner sc = new Scanner(System.in);
        System.out.print("Input length of Strings to be generated: ");
        n = sc.nextInt();
        System.out.print("Input number of test cases: ");
        cases = sc.nextInt();
        System.out.print("Input name of file to be created: ");
        fileName = sc.nextLine();
        fileName = sc.nextLine();
        fileName = fileName + ".txt";
        sc.close();

        // First, try to create the file with the specified name.
        try {
            File myObj = new File(fileName);
            if (myObj.createNewFile()) {
                // Then, generate and write the randomized strings.
                FileWriter myWriter = new FileWriter(fileName);
                for(int i = 0; i < cases; i++){
                    myWriter.write(generateRandomString(n) + "\n");
                }
                myWriter.close();
            } else {
                // If file already exists, abort.
                System.out.println("File already exists. Please try again.");
            }
        } catch (IOException e) {
            // If IO Exception occurs, abort.
            System.out.println("An error occurred. Please try again.");
            e.printStackTrace();
        }
    }
}