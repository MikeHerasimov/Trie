package com.github.mikeherasimov.trie.linked;

import com.github.mikeherasimov.trie.Optimizer;
import com.github.mikeherasimov.trie.Trie;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * LinkedTrie is one of realization of Trie interface.
 * Where each node holds references to first child and brother-node.
 * LinkedTrie uses less memory then ArrayTrie, but it algorithms has some overhead.
 * Also it can not be fully optimized to DAWG.
 */
public final class LinkedTrie implements Trie, Externalizable{

    private int size;
    private LinkedNode root;

    /**
     * Returns new LinkedTrie object, that can hold any <code>String</code>`s.
     * More formally its alphabet contains all UTF characters.
     */
    public LinkedTrie(){
        root = new LinkedNode();
    }

    /**
     * Returns copy of supplied LinkedTrie object.
     * More formally if <code>copy = new LinkedTrie(trie)</code> then <p>
     * <code>trie != copy</code> and <p>
     * <code>trie.equals(copy) == true</code> and <p>
     * <code>trie.getClass() == copy.getClass()</code>
     *
     * @param trie  supplied <code>LinkedTrie</code> object
     */
    public LinkedTrie(LinkedTrie trie){
        size = trie.size;
        root = LinkedNode.newInstance(trie.root);
    }

    LinkedTrie(int size, LinkedNode root) {
        this.size = size;
        this.root = root;
    }

    /**
     * Appends specified word to this LinkedTrie.
     *
     * @param word                       word to be added to this Trie
     * @throws IllegalArgumentException  if at least one letter of word isn't supported by
     *                                   alphabet of this <code>ArrayTrie</code>
     */
    @Override
    public void add(String word) throws IllegalArgumentException {
        LinkedNode current = root;
        for (int i = 0, dest = word.length()-1; i < word.length(); i++){
            current = createNodeIfNeeds(current, word.charAt(i),
                    i == dest);
        }
    }

    /**
     * Makes optimization of this LinkedTrie to DAWG and returns respective DAWG object.
     * More formally makes copy of this LinkedTrie object and then makes optimization of copy to DAWG
     * and returns respective DAWG object.
     * Note that LinkedTrie can't make full optimization to DAWG that is it can't eliminate all duplicate nodes.
     * Because of its node structure, where each node contains reference to its brother and child.
     * So when checking nodes for equality LinkedTrie has to check not only all descendants of node,
     * it has to check brother-nodes too.
     *
     * @return  <code>DAWG</code> object
     */
    @Override
    public LinkedDAWG toDAWG() {
        LinkedTrie copy = new LinkedTrie(this);
        LinkedSubtrieConverter subtrieConverter = new LinkedSubtrieConverter(copy.root);

        Optimizer<LinkedNode> optimizer =
                new Optimizer<>(LinkedOptimizerBehaviour.INSTANCE, subtrieConverter);
        optimizer.eliminateDuplicates();
        return new LinkedDAWG(copy);
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        size = 0;
        root.setChild(null);
    }

    @Override
    public boolean contains(String word) {
        LinkedNode lastNode = searchNodeBySequence(word);
        return lastNode != null && lastNode.getEOW();
    }

    @Override
    public boolean isPrefix(String prefix) {
        return searchNodeBySequence(prefix) != null;
    }

    private LinkedNode searchNodeBySequence(String sequence){
        LinkedNode current = root;
        for (int i = 0; i < sequence.length(); i++) {
            if(current.getChild() == null) {
                return null;
            }
            LinkedNode temp = listScan(current.getChild(), sequence.charAt(i));
            if(temp == null) {
                return null;
            }
            current = temp;
        }
        return current;
    }

    @Override
    public int size() {
        return size;
    }

    private LinkedNode createNodeIfNeeds(LinkedNode ancestor, char letter, boolean EOW){
        LinkedNode child = listScan(ancestor.getChild(), letter);
        if (child == null){
            if (EOW){
                size++;
            }
            return addNewChild(ancestor, letter, EOW);
        } else {
            return checkNodeIfExists(child, EOW);
        }
    }

    private LinkedNode listScan(LinkedNode child, char letter){
        while(child != null) {
            if(child.getLetter() == letter) {
                return child;
            }
            child = child.getBrother();
        }
        return null;
    }

    private LinkedNode addNewChild(LinkedNode ancestor, char letter, boolean EOW){
        LinkedNode newChild = new LinkedNode(letter, EOW);
        if(ancestor.getChild() == null) {
            ancestor.setChild(newChild);
        } else {
            ancestor.getChild().getLastBrother().setBrother(newChild);
        }
        return newChild;
    }

    private LinkedNode checkNodeIfExists(LinkedNode node, boolean EOW) {
        if(EOW && !node.getEOW()) {
            size++;
            node.setAsEOW();
        }
        return node;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof LinkedTrie)) return false;
        LinkedTrie trie = (LinkedTrie) obj;
        return size == trie.size && root.equals(trie.root);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(size);
        out.writeObject(root.serializeSubtrie());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.size = in.readInt();
        char[] sequence = (char[]) in.readObject();
        this.root = LinkedNode.deserializeSubtrie(sequence);
    }

    LinkedDAWGSerializationHelper getDAWGSerializationHelper(){
        return new LinkedDAWGSerializationHelper(root);
    }
}
