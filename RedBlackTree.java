// == CS400 Spring 2024 File Header Information ==
// Name: Ritesh Neela
// Email: rneela@wisc.edu
// Lecturer: Gary Dahl
// Notes to Grader:

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class RedBlackTree<T extends Comparable<T>> extends BinarySearchTree<T> {

    protected static class RBTNode<T> extends Node<T> {
        public boolean isBlack = false;
        public RBTNode(T data) { super(data); }
        public RBTNode<T> getUp() { return (RBTNode<T>)this.up; }
        public RBTNode<T> getDownLeft() { return (RBTNode<T>)this.down[0]; }
        public RBTNode<T> getDownRight() { return (RBTNode<T>)this.down[1]; }
    }

    protected void enforceRBTreePropertiesAfterInsert(RBTNode<T> newlyAdded) {

        // Parent is black OR if the newlyAdded node is the root
        if (newlyAdded.getUp() == null || newlyAdded.getUp().isBlack == true) {
            return;
        }
        else { // Parent is red

            // Getting the parent and aunt nodes
            RBTNode<T> parent = newlyAdded.getUp();
            RBTNode<T> aunt = null;
            boolean rightChild = true;
            if (parent.getUp().getDownLeft() == parent) {
                aunt = parent.getUp().getDownRight();
            }
            else if (parent.getUp().getDownRight() == parent){
                aunt = parent.getUp().getDownLeft();
            }
            
            // Checking if the newlyAdded node is the right child of the parent
            if (parent.getDownRight() == newlyAdded) {
                rightChild = true;
            }
            else {
                rightChild = false;
            }

            // case 1: Aunt is black OR aunt is null
            if (aunt == null || aunt.isBlack == true) {
                // case 1.1 newNode is right child of right parent
                if ((rightChild && parent.getUp().getDownRight() == parent) || (!rightChild && parent.getUp().getDownLeft() == parent) ) {
                    flipColor(parent);
                    flipColor(parent.getUp());
                    this.rotate(parent, parent.getUp());
                    return;
                } else { // case 1.2 Zig Case
                    this.rotate(newlyAdded, parent);
                    flipColor(newlyAdded);
                    flipColor(newlyAdded.getUp());
                    this.rotate(newlyAdded, newlyAdded.getUp());
                    return;
                }

            }
            // case 2: Aunt is red
            else if (aunt.isBlack == false) {
                flipColor(aunt);
                flipColor(parent);
                flipColor(parent.getUp());

                // Calling method recursively as this may cause a violation higher up in the tree
                enforceRBTreePropertiesAfterInsert(parent.getUp());
                return;
            }
            
            

        }
        
    }

    /**
     * Private helper method that swaps the color of both the nodes given as input
     * 
     * @param node1 - first node to swap color
     * @param node2 - second node to swap color
     */
    private void swapColors(RBTNode<T> node1, RBTNode<T> node2) {
        boolean colorOfOne = ((RBTNode<T>) node1).isBlack;
        ((RBTNode<T>) node1).isBlack = ((RBTNode<T>) node2).isBlack;
        ((RBTNode<T>) node2).isBlack = colorOfOne;
        return;
    }

    /**
     * Private helper method that flips the color of the node given as input. i.e red becomes black and vice versa
     * 
     * @param node - node to flip color of
     */
    private void flipColor(RBTNode<T> node) {
        if (node.isBlack == false) {
            node.isBlack = true;
        }
        else {
            node.isBlack = false;
        }
        return;
    }

    /**
     * Overrides the insert method of the BinarySearchTree class to insert a node into a Red Black Tree
     * 
     * @param data - data that the newNode to be inserted should carry
     * @return true if successfully inserted and false if otherwise
     */
    @Override
    public boolean insert(T data) throws NullPointerException {
        RBTNode<T> newNode = new RBTNode<T>(data);

        this.insertHelper(newNode);
        enforceRBTreePropertiesAfterInsert(newNode);
        ((RBTNode<T>) this.root).isBlack = true;
        return true;
    }

    /**
     * junit test for RBTInsert algorithm for the case where parent is red and aunt is black
     * 
     */
    @Test
    public void RBTtest1() {
        RedBlackTree<Integer> tree = new RedBlackTree<Integer>();
        tree.insert(10);

        // Left side of the tree
        tree.insert(5);
        ((RBTNode<Integer>) tree.root).getDownLeft().isBlack = true;
        tree.insert(3);
        tree.insert(7);

        // Right side of the tree
        tree.insert(15);
        ((RBTNode<Integer>) tree.root).getDownRight().isBlack = true;
        tree.insert(17);
        tree.insert(19);

        // Making sure the rotation occurs correctly
        String expected = "level order: [ 10, 5, 17, 3, 7, 15, 19 ]\nin order: [ 3, 5, 7, 10, 15, 17, 19 ]";
        Assertions.assertEquals(expected, tree.toString(), "Tree does not rotate with red parent, black aunt");

        // Checking to see if recoloring was done correctly
        Assertions.assertEquals(true, ((RBTNode<Integer>) tree.root).getDownRight().isBlack, "Incorrect recoloring");
        Assertions.assertEquals(false, ((RBTNode<Integer>) tree.root).getDownRight().getDownRight().isBlack, "Incorrect recoloring");
        Assertions.assertEquals(false, ((RBTNode<Integer>) tree.root).getDownRight().getDownLeft().isBlack, "Incorrect recoloring");
    }

    /**
     * junit test for RBTInsert algorithm for the case where parent is black
     * 
     */
    @Test
    public void RBTtest2() {
        RedBlackTree<Integer> tree = new RedBlackTree<Integer>();
        tree.insert(10);
        tree.insert(15);
        tree.insert(5);
        ((RBTNode<Integer>) tree.root).getDownLeft().isBlack = true;
        ((RBTNode<Integer>) tree.root).getDownRight().isBlack = true;
        tree.insert(3);
        tree.insert(7);
        tree.insert(17);

        // No RBT properties should be enforced i.e tree should be unchanged
        String expected = "level order: [ 10, 5, 15, 3, 7, 17 ]\nin order: [ 3, 5, 7, 10, 15, 17 ]";
        Assertions.assertEquals(expected, tree.toString(), "Tree does not correctly insert red node with black parent");

        // Making sure no recoloring is done
        Assertions.assertEquals(false, ((RBTNode<Integer>) tree.root).getDownRight().getDownRight().isBlack, "No recoloring should take place");
        Assertions.assertEquals(true, ((RBTNode<Integer>) tree.root).getDownRight().isBlack, "No recoloring should take place");
    }

    /**
     * junit test for RBTInsert algorithm for the case where parent is red and aunt is also red
     * 
     */
    @Test
    public void RBTtest3() {
        RedBlackTree<Integer> tree = new RedBlackTree<Integer>();
        tree.insert(10);
        tree.insert(5);
        tree.insert(15);
        tree.insert(17);

        // Making sure no rotation takes place
        String expected = "level order: [ 10, 5, 15, 17 ]\nin order: [ 5, 10, 15, 17 ]";
        Assertions.assertEquals(expected, tree.toString(), "Tree does not correctly insert node with red parent and red aunt");

        // Making sure nodes are recoloured properly
        Assertions.assertEquals(true, ((RBTNode<Integer>) tree.root).isBlack, "Root should be black");
        Assertions.assertEquals(true, ((RBTNode<Integer>) tree.root).getDownRight().isBlack, "15 should be black");
        Assertions.assertEquals(true, ((RBTNode<Integer>) tree.root).getDownLeft().isBlack, "5 should be black");
        Assertions.assertEquals(false, ((RBTNode<Integer>) tree.root).getDownRight().getDownRight() .isBlack, "17 should be red");
    }
}