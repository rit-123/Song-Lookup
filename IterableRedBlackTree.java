import java.util.Iterator;
import java.util.Stack;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
 * This class implements the iterable version of a RedBlackTree
 */
public class IterableRedBlackTree<T extends Comparable<T>>
    extends RedBlackTree<T> implements IterableSortedCollection<T> {

    // Default value of iteration starting point using anonymous class
    private Comparable<T> iterationStartingPoint = new Comparable<T>() {
        @Override
        public int compareTo(T other) {
            return -1; // return -1 to indicate that it's less than ALL values in the tree
        }
    }; 

    /**
     * This method is used to set the lowest value the iterator should start at
     */
    public void setIterationStartPoint(Comparable<T> startPoint) {
        if (startPoint == null) { // if null entered as the new start point
            this.iterationStartingPoint = new Comparable<T>() {
                @Override
                public int compareTo(T other) {
                    return -1;
                }
            };
        } else {
            this.iterationStartingPoint = startPoint;
        }
    }

    /**
     * This method creates and returns an RBTIterator object
     * 
     * @return iterator
     */
    public Iterator<T> iterator() { 
        Iterator<T> iterator = new RBTIterator<T>(this.root, this.iterationStartingPoint);
        return iterator;
    }

    /**
     * This class implements the iterator for the IterableRedBlackTree class 
     */
    private static class RBTIterator<R> implements Iterator<R> {
        private Comparable<R> startPoint;
        private Stack<Node<R>> stack = new Stack<Node<R>>();

        /**
         * Constructs and RBTIterator
         */
        public RBTIterator(Node<R> root, Comparable<R> startPoint) {
            this.startPoint = startPoint;
            this.buildStackHelper(root);
        }

        /**
         * This method adds the next nodes to iterate through
         */
        private void buildStackHelper(Node<R> node) {
            // base case
            if (node == null) {
                return;
            } // if node is GREATER than the start point, add to the stack and go to the left
            if (startPoint.compareTo(node.data) <= 0) { //
                this.stack.push(node);
                this.buildStackHelper(node.down[0]);
            } else { // if node is LESS than the start point, go right
                this.buildStackHelper(node.down[1]);
            }
        }

        /**
         * Check to see if there are any remaining values to iterate through
         * 
         * @return true if there are any more values to iterate through
         */
        public boolean hasNext() { return !stack.isEmpty(); }

        /**
         * returns the next value while iterating
         * 
         * @return the next value in the tree to iterate
         */
        public R next() {
            if (stack.isEmpty()) {
                throw new NoSuchElementException();
            }

            Node<R> returnValue = this.stack.pop(); // save the returnvalue

            this.buildStackHelper(returnValue.down[1]); // add any values in it's right subtree to the stack
            return returnValue.data;
        }
    }

    /**
     * Performs a naive insertion into a binary search tree: adding the new node
     * in a leaf position within the tree. After this insertion, no attempt is made
     * to restructure or balance the tree. This overrides the original method to allow
     * duplicate values
     * 
     * @param node the new node to be inserted
     * @return true if the value was inserted, false if is was in the tree already
     * @throws NullPointerException when the provided node is null
     */
    @Override
    protected boolean insertHelper(Node<T> newNode) throws NullPointerException {
        if(newNode == null) throw new NullPointerException("new node cannot be null");

        if (this.root == null) {
            // add first node to an empty tree
            root = newNode;
            size++;
            return true;
        } else {
            // insert into subtree
            Node<T> current = this.root;
            while (true) {
                int compare = newNode.data.compareTo(current.data);
                // if (compare == 0) {
                //     return false;
                if (compare <= 0) {
                    // insert in left subtree
                    if (current.down[0] == null) {
                        // empty space to insert into
                        current.down[0] = newNode;
                        newNode.up = current;
                        this.size++;
                        return true;
                    } else {
                        // no empty space, keep moving down the tree
                        current = current.down[0];
                    }
                } else {
                    // insert in right subtree
                    if (current.down[1] == null) {
                        // empty space to insert into
                        current.down[1] = newNode;
                        newNode.up = current;
                        this.size++;
                        return true;
                    } else {
                        // no empty space, keep moving down the tree
                        current = current.down[1]; 
                    }
                }
            }
        }
    }

    /** 
     * JUnit Test for a tree with Integer objects and another tree with String objects both
     * WITHOUT duplicate values and without a specified start point
     */
    @Test
    public void iteratorTest1() {
        {
            // Creating a tree with Integer objects
            IterableRedBlackTree<Integer> tree = new IterableRedBlackTree<Integer>();
            tree.insert(10);
            tree.insert(5);
            tree.insert(15);
            tree.insert(13);
            tree.insert(20);
            tree.insert(3);
            tree.insert(7);
            tree.insert(1);
            Integer[] expected = {1,3,5,7,10,13,15,20};
            int counterIndex = 0;
            for (Integer i: tree) {
                System.out.print("INDEX: ");
                System.out.println(counterIndex);
                //System.out.println("EXPECTED: " + expected[counterIndex]);
                System.out.println("ACTUAL: " + i);
                Assertions.assertEquals(expected[counterIndex], i);
                counterIndex = counterIndex + 1;
            }
            //Assertions.assertEquals(expected, counterIndex);
        }
        {
            // Creating a tree with String Objects
            IterableRedBlackTree<String> tree = new IterableRedBlackTree<String>();
            tree.insert("D");
            tree.insert("C");
            tree.insert("E");
            tree.insert("F");
            tree.insert("G");
            tree.insert("A");
            tree.insert("B");
            String[] expected = {"A", "B", "C", "D", "E", "F", "G"};
            int counterIndex = 0;
            for (String i: tree) {
                boolean comparison = expected[counterIndex].equals(i);
                Assertions.assertEquals(true, comparison);
                counterIndex = counterIndex + 1;
            }
        }
    }

    /**
     * JUnit Test for tree with Integers and Strings with duplicate values and a specified start point
     */
    @Test
    public void iteratorTest2() {
        {
            // Creating a tree with Integer objects with duplicate values and a start point
            IterableRedBlackTree<Integer> tree = new IterableRedBlackTree<Integer>();
            tree.insert(10);
            tree.insert(5);
            tree.insert(15);
            tree.insert(13);
            tree.insert(20);
            tree.insert(3);
            tree.insert(7);
            tree.insert(10);
            Integer[] expected = {7,10,10,13,15,20}; // Only values AFTER the specified starting point
            int counterIndex = 0;

            tree.setIterationStartPoint(7);
            for (Integer i: tree) {
                Assertions.assertEquals(expected[counterIndex], i);
                counterIndex = counterIndex + 1;
            }
        }
        {
            // Creating a tree with String Objects and with a start point
            IterableRedBlackTree<String> tree = new IterableRedBlackTree<String>();
            tree.insert("D");
            tree.insert("D");
            tree.insert("E");
            tree.insert("F");
            tree.insert("G");
            tree.insert("A");
            tree.insert("A");
            tree.insert("G");
            int counterIndex = 0;
            String[] expected = {"F", "G", "G"};
            tree.setIterationStartPoint("F");
            for (String i: tree) {
                boolean comparison = expected[counterIndex].equals(i);
                Assertions.assertEquals(true, comparison);
                counterIndex = counterIndex + 1;
            }
        }
        
    }

    /**
     * JUnit tester method for tree with specified starting point without duplicate values
     */
    @Test
    public void iteratorTest3() {
        {
            // Creating a tree with Integer objects with a specified start point
            IterableRedBlackTree<Integer> tree = new IterableRedBlackTree<Integer>();
            tree.insert(10);
            tree.insert(5);
            tree.insert(15);
            tree.insert(13);
            tree.insert(20);
            tree.insert(3);
            tree.insert(7);
            tree.insert(1);
            Integer[] expected = {10,13,15,20};
            tree.setIterationStartPoint(10);
            int counterIndex = 0;
            for (Integer i: tree) {
                Assertions.assertEquals(expected[counterIndex], i);
                counterIndex = counterIndex + 1;
            }
        }
        {
            // Creating a tree with String Objects with a specified start point
            IterableRedBlackTree<String> tree = new IterableRedBlackTree<String>();
            tree.insert("D");
            tree.insert("C");
            tree.insert("E");
            tree.insert("F");
            tree.insert("G");
            tree.insert("A");
            tree.insert("B");
            String[] expected = {"B", "C", "D", "E", "F", "G"};
            int counterIndex = 0;
            tree.setIterationStartPoint("B");
            for (String i: tree) {
                boolean comparison = expected[counterIndex].equals(i);
                Assertions.assertEquals(true, comparison);
                counterIndex = counterIndex + 1;
            }
        }
    }

}
