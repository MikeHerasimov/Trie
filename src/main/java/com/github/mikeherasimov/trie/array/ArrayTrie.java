package com.github.mikeherasimov.trie.array;

import com.github.mikeherasimov.trie.Alphabet;
import com.github.mikeherasimov.trie.Optimizer;
import com.github.mikeherasimov.trie.Trie;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;

/**
 * ArrayTrie is one of realization of Trie interface.
 * Where each node holds its descendants in array, which size is equal to length of alphabet
 * regardless of current number of descendants.
 * ArrayTrie is fastest Trie implementation.
 */
public final class ArrayTrie implements Trie, Externalizable{

    private int size;
    private ArrayNode root;
    private char[] alphabet;

    /**
     * Returns new ArrayTrie object, supported by specified alphabet.
     *
     * @param alphabet  specified alphabet
     */
    public ArrayTrie(String alphabet) {
        this.alphabet = alphabet.toCharArray();
        Arrays.sort(this.alphabet);
        root = new ArrayNode(alphabet.length());
    }

    /**
     * Returns new ArrayTrie object, supported by specified Alphabet`s instance.
     *
     * @param alphabet  specified <code>Alphabet</code>`s instance
     */
    public ArrayTrie(Alphabet alphabet) {
        this(alphabet.characters());
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
        ArraySubtrieConverter subtrieConverter = new ArraySubtrieConverter(copy.root);

        Optimizer<ArrayNode> optimizer =
                new Optimizer<>(ArrayOptimizerBehaviour.INSTANCE, subtrieConverter);
        optimizer.eliminateDuplicates();
        return new ArrayDAWG(copy);
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        size = 0;
        root = new ArrayNode(alphabet.length);
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
        ArrayNode lastNode = searchNodeBySequence(word);
        return lastNode != null && lastNode.getEOW();
    }

    /**
     * Returns true if ArrayTrie contains specified prefix
     *
     * @param prefix                     prefix whose presence in this <code>ArrayTrie</code> is to be tested
     * @return                           <code>true</code> if <code>ArrayTrie</code> contains specified prefix
     * @throws IllegalArgumentException  if at least one letter of word isn't supported by
     *                                   alphabet of this <code>ArrayTrie</code>
     */
    @Override
    public boolean isPrefix(String prefix) {
        return searchNodeBySequence(prefix) != null;
    }

    private ArrayNode searchNodeBySequence(String sequence){
        int[] pos = inspectWord(sequence);

        ArrayNode current = root;
        for (int i = 0; i < sequence.length(); i++){
            ArrayNode child = current.getChild(pos[i]);
            if (child == null) {
                return null;
            }
            current = child;
        }
        return current;
    }

    @Override
    public int size() {
        return size;
    }

    private int[] inspectWord(String word) {
        int[] array = new int[word.length()];
        for (int i = 0; i < word.length(); i++){
            int index = Arrays.binarySearch(alphabet, word.charAt(i));
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
        ArrayNode node = new ArrayNode(letter, EOW, alphabet.length);
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
                Arrays.equals(alphabet, trie.alphabet) && root.equals(trie.root);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(size);
        out.writeObject(alphabet);
        out.writeObject(root.serializeSubtrie());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.size = in.readInt();
        this.alphabet = (char[]) in.readObject();
        char[] sequence = (char[]) in.readObject();
        this.root = ArrayNode.deserializeSubtrie(sequence, alphabet.length);
    }
}
