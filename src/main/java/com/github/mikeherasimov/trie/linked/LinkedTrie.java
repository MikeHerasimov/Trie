package com.github.mikeherasimov.trie.linked;

import com.github.mikeherasimov.trie.Optimizer;
import com.github.mikeherasimov.trie.Trie;
import gnu.trove.list.linked.TCharLinkedList;
import gnu.trove.list.linked.TIntLinkedList;

import java.io.*;


/**
 * LinkedTrie is one of realization of Trie interface.
 * Where each node holds references to first child and brother-node.
 * LinkedTrie uses less memory then ArrayTrie, but it algorithms has some overhead.
 * Also it can not be fully optimized to DAWG.
 */
public final class LinkedTrie implements Trie, Externalizable{

    private int size;
    private LinkedNode root;
    private String alphabet = "abcdefghijklmnopqrstuvwxyz";

    /**
     * Returns new LinkedTrie object, supported by english lowercase alphabet.
     */
    public LinkedTrie(){
        root = new LinkedNode();
    }

    /**
     * Returns new LinkedTrie object, supported by specified alphabet.
     *
     * @param alphabet  specified alphabet
     */
    public LinkedTrie(String alphabet) {
        this.alphabet = alphabet;
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
        alphabet = trie.alphabet;
    }

    /**
     * Appends specified word to this LinkedTrie.
     *
     * @param word                       word to be added to this Trie
     * @throws IllegalArgumentException  if at least one letter of word isn't supported by
     *                                   alphabet of this <code>ArrayTrie</code>
     */
    @Override
    public void add(String word) throws IllegalArgumentException{
        inspectWord(word);

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
        return toDAWG(copy);
    }

    static LinkedDAWG toDAWG(LinkedTrie trie){
        int nodeCount = LinkedOptimizerBehaviour.INSTANCE.countNodes(trie.root);
        LinkedNode[] nodes = new LinkedNode[nodeCount];
        int[] ancestors = new int[nodeCount];
        TIntLinkedList leafs = new TIntLinkedList();

        trie.getInfo(nodes, ancestors, leafs, trie.root, 0);
        recursiveCallsCount = -1;

        Optimizer<LinkedNode> optimizer =
                new Optimizer<>(LinkedOptimizerBehaviour.INSTANCE, nodes, ancestors, leafs.toArray());
        optimizer.eliminateDuplicates();
        return new LinkedDAWG(trie);
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
        LinkedNode current = root;
        for (int i = 0; i < word.length(); i++) {
            if(current.getChild() == null) {
                return false;
            }
            LinkedNode temp = listScan(current.getChild(), word.charAt(i));
            if(temp == null) {
                return false;
            }
            current = temp;
        }
        return current.getEOW();
    }

    @Override
    public int size() {
        return size;
    }

    private void inspectWord(String word) throws IllegalArgumentException{
        for (int i = 0; i < word.length(); i++){
            if(alphabet.indexOf(word.charAt(i)) < 0)
                throw new IllegalArgumentException("At least one letter of word is not supported by current alphabet");
        }
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
        return size == trie.size &&
                alphabet.equals(trie.alphabet) && root.equals(trie.root);
    }

    private static int recursiveCallsCount = -1;
    private void getInfo(LinkedNode[] nodes,
                         int[] ancestors,
                         TIntLinkedList leafs,
                         LinkedNode current, int anc) {
        recursiveCallsCount++;
        nodes[recursiveCallsCount] = current;
        ancestors[recursiveCallsCount] = anc;

        if(current.getChild() != null){
            getInfo(nodes, ancestors, leafs, current.getChild(), recursiveCallsCount);
        } else {
            leafs.add(recursiveCallsCount);
        }

        if(current.getBrother() != null){
            getInfo(nodes, ancestors, leafs, current.getBrother(), anc);
        }
    }

    private void preorderSerialize(TCharLinkedList list, LinkedNode current){
        if(current == null){
            list.add(')');
        } else {
            list.add(current.getLetter());
            if(current.getEOW()){
                list.add('*');
            }
            preorderSerialize(list, current.getChild());
            preorderSerialize(list, current.getBrother());
        }
    }

    private LinkedNode preorderDeserialize(char[] sequence){
        char letter = sequence[++recursiveCallsCount];
        if(letter == ')'){
            return null;
        }
        boolean EOW = false;
        if(sequence[recursiveCallsCount + 1] == '*'){
            recursiveCallsCount++;
            EOW = true;
        }

        LinkedNode root = new LinkedNode(letter, EOW);
        root.setChild(preorderDeserialize(sequence));
        root.setBrother(preorderDeserialize(sequence));
        return root;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        TCharLinkedList list = new TCharLinkedList();
        preorderSerialize(list, root);

        out.writeInt(size);
        out.writeUTF(alphabet);
        out.writeObject(list.toArray());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.size = in.readInt();
        this.alphabet = in.readUTF();

        char[] sequence = (char[]) in.readObject();
        this.root = preorderDeserialize(sequence);
        recursiveCallsCount = -1;
    }
}
