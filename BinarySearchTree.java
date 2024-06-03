// == CS400 Spring 2024 File Header Information ==
// Name: Ritesh Neela
// Email: rneela@wisc.edu
// Lecturer: Gary Dahl
// Notes to Grader:

import java.util.LinkedList;
import java.util.Stack;

import org.w3c.dom.Node;


/**
 * Binary Search Tree implementation with a Node inner class for representing
 * the nodes of the tree. We will turn this Binary Search Tree into a self-balancing
 * tree as part of project 1 by modifying its insert functionality.
 * In week 0 of project 1, we will start this process by implementing tree rotations.
 */
public class BinarySearchTree<T extends Comparable<T>> implements SortedCollectionInterface<T> {

    /**
     * This class represents a node holding a single value within a binary tree.
     */
    protected static class Node<T> {
        public T data;
        // up stores a reference to the node's parent
        public Node<T> up;
        // The down array stores references to the node's children:
        // - down[0] is the left child reference of the node,
        // - down[1] is the right child reference of the node.
        // The @SupressWarning("unchecked") annotation is use to supress an unchecked
        // cast warning. Java only allows us to instantiate arrays without generic
        // type parameters, so we use this cast here to avoid future casts of the
        // node type's data field.
        @SuppressWarnings("unchecked")
        public Node<T>[] down = (Node<T>[])new Node[2];
        public Node(T data) { this.data = data; }
        
        /**
         * @return true when this node has a parent and is the right child of
         * that parent, otherwise return false
         */
        public boolean isRightChild() {
            return this.up != null && this.up.down[1] == this;
        }

    }

    protected Node<T> root; // reference to root node of tree, null when empty
    protected int size = 0; // the number of values in the tree

    /**
     * Inserts a new data value into the tree.
     * This tree will not hold null references, nor duplicate data values.
     * @param data to be added into this binary search tree
     * @return true if the value was inserted, false if is was in the tree already
     * @throws NullPointerException when the provided data argument is null
     */
    public boolean insert(T data) throws NullPointerException {
        if (data == null)
            throw new NullPointerException("Cannot insert data value null into the tree.");
        return this.insertHelper(new Node<>(data));
    }

    /**
     * Performs a naive insertion into a binary search tree: adding the new node
     * in a leaf position within the tree. After this insertion, no attempt is made
     * to restructure or balance the tree.
     * @param node the new node to be inserted
     * @return true if the value was inserted, false if is was in the tree already
     * @throws NullPointerException when the provided node is null
     */
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
                if (compare == 0) {
                    return false;
                } else if (compare < 0) {
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
     * Performs the rotation operation on the provided nodes within this tree.
     * When the provided child is a left child of the provided parent, this
     * method will perform a right rotation. When the provided child is a
     * right child of the provided parent, this method will perform a left rotation.
     * When the provided nodes are not related in one of these ways, this method
     * will throw an IllegalArgumentException.
     * @param child is the node being rotated from child to parent position
     *      (between these two node arguments)
     * @param parent is the node being rotated from parent to child position
     *      (between these two node arguments)
     * @throws IllegalArgumentException when the provided child and parent
     *      node references are not initially (pre-rotation) related that way
     */
    protected void rotate(Node<T> child, Node<T> parent) throws IllegalArgumentException {
        // checking for null
        if (child == null || parent == null) {
            throw new IllegalArgumentException("cannot be null");
        }
        if (parent.down[0] == child) { // right rotation
            int inx = 0;
            // storing the right child of the child node and the parent of the parent node
            Node<T> childRightChild = child.down[1];
            Node<T> parentParent = parent.up;

            if ( parentParent != null && parentParent.down[1] == parent) {
                inx = 1;
            }

            // setting the child in place of the current parent
            child.up = parentParent;
            child.down[1] = parent;

            // setting the parent as the left child of the child node
            parent.up = child;
            parent.down[0] = childRightChild;
            if (childRightChild != null){
                childRightChild.up = parent;
            }

            // changing root if necessary
            if (parentParent == null) {
                this.root = child;
            }
            else {
                parentParent.down[inx] = child;
            }
        }
        else if (parent.down[1] == child) { // left rotation
            int inx = 0;
            // storing the left child of the child node and the parent of the parent node
            Node<T> childLeftChild = child.down[0];
            Node<T> parentParent = parent.up;
            
            if ( parentParent != null && parentParent.down[1] == parent) {
                inx = 1;
            }

            // setting the child in place of the current parent
            child.up = parentParent;
            child.down[0] = parent;

            // setting the parent as the right child of the child node
            parent.up = child;
            parent.down[1] = childLeftChild;
            if (childLeftChild != null){
                childLeftChild.up = parent;
            }

            // changing root if necessary
            if (parentParent == null) {
                this.root = child;
            }
            else {
                parentParent.down[inx] = child;
            }
        }
        else {
            throw new IllegalArgumentException("child and parent are not correctly related");
        }
    }

    /**
     * Get the size of the tree (its number of nodes).
     * @return the number of nodes in the tree
     */
    public int size() {
        return size;
    }

    /**
     * Method to check if the tree is empty (does not contain any node).
     * @return true of this.size() returns 0, false if this.size() != 0
     */
    public boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * Checks whether the tree contains the value *data*.
     * @param data a comparable for the data value to check for
     * @return true if *data* is in the tree, false if it is not in the tree
     */
    public boolean contains(Comparable<T> data) {
        // null references will not be stored within this tree
        if (data == null) {
            throw new NullPointerException("This tree cannot store null references.");
        } else {
            Node<T> nodeWithData = this.findNode(data);
            // return false if the node is null, true otherwise
            return (nodeWithData != null);
        }
    }

    /**
     * Removes all keys from the tree.
     */
    public void clear() {
        this.root = null;
        this.size = 0;
    }

    /**
     * Helper method that will return the node in the tree that contains a specific
     * key. Returns null if there is no node that contains the key.
     * @param data the data value for which we want to find the node that contains it
     * @return the node that contains the data value or null if there is no such node
     */
    protected Node<T> findNode(Comparable<T> data) {
        Node<T> current = this.root;
        while (current != null) {
            int compare = data.compareTo(current.data);
            if (compare == 0) {
                // we found our value
                return current;
            } else if (compare < 0) {
                if (current.down[0] == null) {
                    // we have hit a null node and did not find our node
                    return null;
                }
                // keep looking in the left subtree
                current = current.down[0];
            } else {
                if (current.down[1] == null) {
                    // we have hit a null node and did not find our node
                    return null;
                }
                // keep looking in the right subtree
                current = current.down[1];
            }
        }
        return null;
    }

    /**
     * This method performs an inorder traversal of the tree. The string 
     * representations of each data value within this tree are assembled into a
     * comma separated string within brackets (similar to many implementations 
     * of java.util.Collection, like java.util.ArrayList, LinkedList, etc).
     * @return string containing the ordered values of this tree (in-order traversal)
     */
    public String toInOrderString() {
        // generate a string of all values of the tree in (ordered) in-order
        // traversal sequence
        StringBuffer sb = new StringBuffer();
        sb.append("[ ");
        int nodesVisited = 0;
        if (this.root != null) {
            Stack<Node<T>> nodeStack = new Stack<>();
            Node<T> current = this.root;
            while (!nodeStack.isEmpty() || current != null) {
                if (current == null) {
                    Node<T> popped = nodeStack.pop();
                    if (++nodesVisited > this.size()) {
                        throw new RuntimeException("visited more nodes during traversal than there are keys in the tree; make sure there is no loop in the tree structure");
                    }
                    sb.append(popped.data.toString());
                    if(!nodeStack.isEmpty() || popped.down[1] != null) sb.append(", ");
                    current = popped.down[1];
                } else {
                    nodeStack.add(current);
                    current = current.down[0];
                }
            }
        }
        sb.append(" ]");
        return sb.toString();
    }

    /**
     * This method performs a level order traversal of the tree. The string
     * representations of each data value
     * within this tree are assembled into a comma separated string within
     * brackets (similar to many implementations of java.util.Collection).
     * This method will be helpful as a helper for the debugging and testing
     * of your rotation implementation.
     * @return string containing the values of this tree in level order
     */
    public String toLevelOrderString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[ ");
        int nodesVisited = 0;
        if (this.root != null) {
            LinkedList<Node<T>> q = new LinkedList<>();
            q.add(this.root);
            while(!q.isEmpty()) {
                Node<T> next = q.removeFirst();
                if (++nodesVisited > this.size()) {
                    throw new RuntimeException("visited more nodes during traversal than there are keys in the tree; make sure there is no loop in the tree structure");
                }
                if(next.down[0] != null) q.add(next.down[0]);
                if(next.down[1] != null) q.add(next.down[1]);
                sb.append(next.data.toString());
                if(!q.isEmpty()) sb.append(", ");
            }
        }
        sb.append(" ]");
        return sb.toString();
    }

    public String toString() {
        return "level order: " + this.toLevelOrderString() +
                "\nin order: " + this.toInOrderString();
    }

    /**
     * This method tests a left rotation at the root of the tree.
     * 
     * @return true if the rotate() methods works correctly, false otherwise
     */
    public static boolean test1() {
        // left rotation at the root of the tree

        // original tree
        BinarySearchTree<Integer> originalTree = new BinarySearchTree<>();
        originalTree.insert(10);
        originalTree.insert(5);
        originalTree.insert(15);
        originalTree.insert(17);
        originalTree.insert(12);
        
        // expected tree after rotation
        BinarySearchTree<Integer> expectedTree = new BinarySearchTree<>();
        expectedTree.insert(15);
        expectedTree.insert(10);
        expectedTree.insert(17);
        expectedTree.insert(5);
        expectedTree.insert(12);

        originalTree.rotate(originalTree.root.down[1], originalTree.root);
        if (!originalTree.toString().equals(expectedTree.toString())) {
          return false;
        }
        return true;
    }
    
    /**
     * This method tests a right rotation at the root of the tree.
     * 
     * @return true if the rotate() methods works correctly, false otherwise
     */
    public static boolean test2() {
        // right rotation at the root of the tree

        // original tree
        BinarySearchTree<Integer> originalTree = new BinarySearchTree<>(); 
        originalTree.insert(7);
        originalTree.insert(5);
        originalTree.insert(9);
        originalTree.insert(6);
        originalTree.insert(4);
        originalTree.insert(8);
        originalTree.insert(10);
        originalTree.rotate(originalTree.root.down[0], originalTree.root);

        // expected tree after rotation
        BinarySearchTree<Integer> expectedTree = new BinarySearchTree<>();
        expectedTree.insert(5);
        expectedTree.insert(4);
        expectedTree.insert(7);
        expectedTree.insert(6);
        expectedTree.insert(9);
        expectedTree.insert(8);
        expectedTree.insert(10);

        if (!originalTree.toString().equals(expectedTree.toString())) {
            return false;
        }
        return true;
    }
    
    /**
     * This method tests a rotation in the middle of the tree (not at the root).
     * 
     * @return true if the rotate() methods works correctly, false otherwise
     */
    public static boolean test3() {
        // rotation in the middle of the tree (not at the root)

        // original tree
        BinarySearchTree<Integer> originalTree = new BinarySearchTree<>();
        originalTree.insert(42);
        originalTree.insert(27);
        originalTree.insert(61);
        originalTree.insert(19);
        originalTree.insert(33);
        originalTree.insert(54);
        originalTree.insert(76);

        originalTree.rotate(originalTree.root.down[1].down[0], originalTree.root.down[1]);

        // expected tree after rotation
        BinarySearchTree<Integer> expectedTree = new BinarySearchTree<>();
        expectedTree.insert(42);
        expectedTree.insert(27);
        expectedTree.insert(54);
        expectedTree.insert(19);
        expectedTree.insert(33);
        expectedTree.insert(61);
        expectedTree.insert(76);
        
        if (!originalTree.toString().equals(expectedTree.toString())) {
            return false;
        }
        // making sure the root is unchanged
        if (originalTree.root.data != expectedTree.root.data) {
            return false;
        }
        return true;
    }

    public static boolean test4() {
        BinarySearchTree<Integer> originalTree = new BinarySearchTree<>();
        originalTree.insert(4);
        originalTree.insert(2);
        originalTree.insert(1);
        originalTree.insert(3);
        originalTree.insert(6);
        originalTree.insert(5);
        originalTree.insert(7);

        originalTree.rotate(originalTree.root.down[1].down[1], originalTree.root.down[1]);

        BinarySearchTree<Integer> expectedTree = new BinarySearchTree<>();
        expectedTree.insert(4);
        expectedTree.insert(2);
        expectedTree.insert(1);
        expectedTree.insert(3);
        expectedTree.insert(7);
        expectedTree.insert(6);
        expectedTree.insert(5);

        if (!(originalTree.toString().equals(expectedTree.toString()))) {
            return false;
        }

        return true;
    }
    
    /**
     * Main method to run tests. If you'd like to add additional test methods, add a line for each
     * of them.
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Test 1 passed: " + test1());
        System.out.println("Test 2 passed: " + test2());
        System.out.println("Test 3 passed: " + test3());
        System.out.println("Test 4 passed: " + test4());
    }

}
