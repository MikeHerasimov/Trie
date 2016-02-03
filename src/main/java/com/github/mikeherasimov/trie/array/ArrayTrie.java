package com.github.mikeherasimov.trie.array;

import com.github.mikeherasimov.trie.Optimizer;
import com.github.mikeherasimov.trie.Trie;
import gnu.trove.list.linked.TCharLinkedList;
import gnu.trove.list.linked.TIntLinkedList;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * ArrayTrie is one of realization of Trie interface.
 * Where each node holds its descendants in array, which size is equal to length of alphabet
 * regardless of current number of descendants.
 * ArrayTrie is fastest Trie implementation.
 */
public final class ArrayTrie implements Trie, Externalizable{

    private int size;
    private ArrayNode root;
    private String alphabet = "abcdefghijklmnopqrstuvwxyz";

    /**
     * Returns new ArrayTrie object, supported by english lowercase alphabet.
     */
    public ArrayTrie() {
        root = new ArrayNode(alphabet.length());
    }

    /**
     * Returns new ArrayTrie object, supported by specified alphabet.
     *
     * @param alphabet  specified alphabet
     */
    public ArrayTrie(String alphabet) {
        this.alphabet = alphabet;
        root = new ArrayNode(alphabet.length());
    }

    /**
     * Returns copy of supplied ArrayTrie object.
     * More formally if <code>copy = new ArrayTrie(trie)</code> then <p>
     * <code>trie != copy</code> and <p>
     * <code>trie.equals(copy) == true</code> and <p>
     * <code>trie.getClass() == copy.getClass()</code>
     *
     * @param trie  supplied <code>ArrayTrie</code> object
     */
    public ArrayTrie(ArrayTrie trie){
        size = trie.size;
        root = ArrayNode.newInstance(trie.root);
        alphabet = trie.alphabet;
    }

    /**
     * Appends specified word to this ArrayTrie.
     *
     * @param  word                      word to be added to this ArrayTrie
     * @throws IllegalArgumentException  if at least one letter of word isn't supported by
     *                                   alphabet of this <code>ArrayTrie</code>
     */
    @Override
    public void add(String word) throws IllegalArgumentException{
        int[] pos = inspectWord(word);

        ArrayNode current = root;
        for (int i = 0, dest = word.length()-1; i < word.length(); i++){
            current = createNodeIfNeeds(current, pos[i], word.charAt(i),
                    i == dest);
        }
    }

    /**
     * Makes optimization of this ArrayTrie to DAWG and returns respective DAWG object.
     * More formally makes copy of this ArrayTrie object and then makes optimization of copy to DAWG
     * and returns respective DAWG object.
     *
     * @return  <code>DAWG</code> object
     */
    @Override
    public ArrayDAWG toDAWG() {
        ArrayTrie copy = new ArrayTrie(this);
        return toDAWG(copy);
    }

    static ArrayDAWG toDAWG(ArrayTrie trie){
        int nodeCount = ArrayOptimizerBehaviour.INSTANCE.countNodes(trie.root);
        ArrayNode[] nodes = new ArrayNode[nodeCount];
        int[] ancestors = new int[nodeCount];
        TIntLinkedList leafs = new TIntLinkedList();

        trie.getInfo(nodes, ancestors, leafs, trie.root, 0);
        recursiveCallsCount = -1;

        Optimizer<ArrayNode> optimizer =
                new Optimizer<>(ArrayOptimizerBehaviour.INSTANCE, nodes, ancestors, leafs.toArray());
        optimizer.eliminateDuplicates();
        return new ArrayDAWG(trie);
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        size = 0;
        root = new ArrayNode(alphabet.length());
    }

    /**
     * Returns true if ArrayTrie contains specified word
     *
     * @param word                       word whose presence in this <code>ArrayTrie</code> is to be tested
     * @return                           <code>true</code> if <code>ArrayTrie</code> contains specified word
     * @throws IllegalArgumentException  if at least one letter of word isn't supported by
     *                                   alphabet of this <code>ArrayTrie</code>
     */
    @Override
    public boolean contains(String word) throws IllegalArgumentException{
        int[] pos = inspectWord(word);

        ArrayNode current = root;
        for (int i = 0, dest = word.length()-1; i < word.length(); i++){
            ArrayNode child = current.getChild(pos[i]);
            if (child == null) {
                return false;
            }
            if (i == dest && !child.getEOW()) {
                return false;
            }
            current = child;
        }
        return true;
    }

    @Override
    public int size() {
        return size;
    }

    private int[] inspectWord(String word) throws IllegalArgumentException{
        int[] array = new int[word.length()];
        for (int i = 0; i < word.length(); i++){
            int index = alphabet.indexOf(word.charAt(i));
            if(index < 0){
                throw new IllegalArgumentException("At least one letter of word is not supported by current alphabet");
            } else {
                array[i] = index;
            }
        }
        return array;
    }

    private ArrayNode createNodeIfNeeds(ArrayNode ancestor, int pos, char letter, boolean EOW) {
        ArrayNode child = ancestor.getChild(pos);
        if(child == null) {
            if(EOW) {
                size++;
            }
            return createAndAttachNode(ancestor, pos, letter, EOW);
        } else {
            return checkNodeIfExists(child, EOW);
        }
    }

    private ArrayNode createAndAttachNode(ArrayNode ancestor, int pos, char letter, boolean EOW) {
        ArrayNode node = new ArrayNode(letter, EOW, alphabet.length());
        ancestor.addChild(node, pos);
        return node;
    }

    private ArrayNode checkNodeIfExists(ArrayNode node, boolean EOW) {
        if(EOW && !node.getEOW()) {
            size++;
            node.setAsEOW();
        }
        return node;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof ArrayTrie)) return false;
        ArrayTrie trie = (ArrayTrie) obj;
        return size == trie.size &&
                alphabet.equals(trie.alphabet) && root.equals(trie.root);
    }

    private static int recursiveCallsCount = -1;
    private void getInfo(ArrayNode[] nodes,
                         int[] ancestors,
                         TIntLinkedList leafs,
                         ArrayNode current, int anc) {
        recursiveCallsCount++;
        nodes[recursiveCallsCount] = current;
        ancestors[recursiveCallsCount] = anc;
        int numberOfCurrentCall = recursiveCallsCount;

        int nullCount = 0;
        for (int i = 0; i < current.numberOfDescendants(); i++){
            ArrayNode child = current.getChild(i);
            if(child != null){
                getInfo(nodes, ancestors, leafs, child, numberOfCurrentCall);
            } else {
                nullCount++;
            }
        }
        if(nullCount == current.numberOfDescendants()){
            leafs.add(recursiveCallsCount);
        }
    }

    private void preorderSerialize(TCharLinkedList list, ArrayNode current){
        list.add(current.getLetter());
        if(current.getEOW()){
            list.add('*');
        }

        int i = 0;
        int lengthOfAlphabet = alphabet.length();
        while (i < lengthOfAlphabet && current.getChild(i) == null){
            i++;
        }
        if(i != lengthOfAlphabet){
            list.add('{');
            for ( ; i < lengthOfAlphabet; i++){
                ArrayNode child = current.getChild(i);
                if(child != null){
                    list.add((char)i);
                    preorderSerialize(list, child);
                }
            }
            list.add('}');
        }
    }

    private ArrayNode preorderDeserialize(char[] sequence, int lengthOfAlphabet){
        char letter = sequence[++recursiveCallsCount];
        boolean EOW = false;
        if(sequence[recursiveCallsCount + 1] == '*'){
            recursiveCallsCount++;
            EOW = true;
        }
        ArrayNode root = new ArrayNode(letter, EOW, lengthOfAlphabet);

        if(sequence[recursiveCallsCount + 1] == '{'){
            recursiveCallsCount++;
            do {
                int pos = sequence[++recursiveCallsCount];
                root.addChild(preorderDeserialize(sequence, lengthOfAlphabet), pos);
            } while (sequence[recursiveCallsCount + 1] != '}');
            recursiveCallsCount++;
        }
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
        this.root = preorderDeserialize(sequence, alphabet.length());
        recursiveCallsCount = -1;
    }
}
