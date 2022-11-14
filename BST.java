import java.io.FileWriter;
import java.io.IOException;

public class BST {

    private String key;
    private int count;
    private BST leftChild;
    private BST rightChild;

    public BST(String key) {
        this.key = key;
        this.count = 1;
        this.leftChild = null;
        this.rightChild = null;
    }

    public void insert(String newKey) {
        // If newKey is lexicographically less than this.key...
        if (this.key.compareTo(newKey) > 0) {
            if (this.leftChild == null) {
                this.leftChild = new BST(newKey);
            } else {
                this.leftChild.insert(newKey);
            }
        }
        // If newKey is lexicographically greater than this.key...
        else {
            if (this.rightChild == null) {
                this.rightChild = new BST(newKey);
            } else {
                this.rightChild.insert(newKey);
            }
        }
    }

    public BST search(String searchKey) {
        // If searchKey is exactly the same as this.key...
        if (this.key.compareTo(searchKey) == 0) {
            return this;
        } 
        // If searchKey is lexicographically less than this.key...
        else if (this.key.compareTo(searchKey) > 0) {
            if (this.leftChild == null) {
                return null;
            } else {
                return this.leftChild.search(searchKey);
            }
        }
        // If searchKey is lexicographically greater than this.key...
        else {
            if (this.rightChild == null) {
                return null;
            } else {
                return this.rightChild.search(searchKey);
            }
        }
    }


    public void printAll(FileWriter fileWriter) throws IOException {
        if (this.leftChild != null) {
            this.leftChild.printAll(fileWriter);
        }
        fileWriter.write("[" + this.key + "] " + count + "\n");
        if (this.rightChild != null) {
            this.rightChild.printAll(fileWriter);
        }
    }

    public String getKey() {
        return this.key;
    }

    public int getCount() {
        return this.count;
    }

    public void incrementCount() {
        this.count++;
    }
}
