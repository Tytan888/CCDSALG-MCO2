import java.io.FileWriter;
import java.io.IOException;

/**
 * This BST class facilitates a number of operations associated with Binary
 * Search Trees, including inserting new nodes to the tree given a key,
 * searching if a given key exists within the tree, and printing all of its
 * nodes (in the form of their associated keys and counts).
 */
public class BST {

    private String key;
    private int count;
    private BST leftChild;
    private BST rightChild;

    /**
     * This constructor initializes a BST with key as its root.
     * 
     * @param key the key of the root node of this newly created BST
     */
    public BST(String key) {
        this.key = key;
        this.count = 1;
        this.leftChild = null;
        this.rightChild = null;
    }

    /**
     * This method accepts a given newKey and inserts its node appropriately into
     * the tree, following the rules that any Binary Search Tree must adhere to.
     * 
     * @param newKey the key of the new node to be inserted
     */
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

    /**
     * This method accepts a given searchKey and searches for the node corresponding
     * to said key within this Binary Search Tree.
     * 
     * @param the key of the node to be searched
     * 
     * @return a BST object containing the node being searched as its root
     */
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

    /**
     * This method prints all nodes within the tree in alphabetical order (in-order
     * tree traversal), specifically in the format: "[key]: count".
     * 
     * @param fileWriter the FileWriter class pointing to the output file
     * @throws IOException
     */
    public void printAll(FileWriter fileWriter) throws IOException {
        if (this.leftChild != null) {
            this.leftChild.printAll(fileWriter);
        }
        fileWriter.write("[" + this.key + "] " + count + "\n");
        if (this.rightChild != null) {
            this.rightChild.printAll(fileWriter);
        }
    }

    /**
     * This method simply returns the key of this BST's root node.
     * 
     * @return the key of this BST's root node
     */
    public String getKey() {
        return this.key;
    }

    /**
     * This method simply returns the count of this BST's root node
     * 
     * @return the count of this BST's root node
     */
    public int getCount() {
        return this.count;
    }

    /**
     * This method simply increments the count of this BST's root by 1
     */
    public void incrementCount() {
        this.count++;
    }
}
