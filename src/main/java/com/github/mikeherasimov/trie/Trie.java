package com.github.mikeherasimov.trie;

/**
 * Trie is base interface for all trie data-structure realizations in trie package.
 * Trie contains methods, each of which defines basic behaviour of its realizations.
 */
public interface Trie extends WordGraph {

    /**
     * Appends specified word to this Trie.
     *
     * @param word  word to be added to this Trie
     */
    void add(String word);

    /**
     * Makes optimization of this Trie to DAWG and returns respective DAWG object.
     *
     * @return  <code>DAWG</code> object
     */
    DAWG toDAWG();

    /**
     * Returns true if this Trie doesn't contains any words
     *
     * @return  <code>true</code> if this Trie doesn't contains any words
     */
    boolean isEmpty();

    /**
     * Removes all nodes from this Trie.
     * After invocation of this method Trie will be empty.
     */
    void clear();

}
