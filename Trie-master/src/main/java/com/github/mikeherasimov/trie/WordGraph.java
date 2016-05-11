package com.github.mikeherasimov.trie;

/**
 * WordGraph is superinterface for all types of word graphs in trie package.
 * It contains basic methods, each of which defines basic behaviour of its subinerfaces.
 */
public interface WordGraph {

    /**
     * Returns true if WordGraph contains specified word
     *
     * @param word  word whose presence in this <code>WordGraph</code> is to be tested
     * @return      <code>true</code> if <code>WordGraph</code> contains specified word
     */
    boolean contains(String word);

    /**
     * Returns true if WordGraph contains specified prefix
     *
     * @param prefix  word whose presence in this <code>WordGraph</code> is to be tested
     * @return      <code>true</code> if <code>WordGraph</code> contains specified prefix
     */
    boolean isPrefix(String prefix);

    /**
     * Returns the number of words in this WordGraph
     *
     * @return  the number of words in this WordGraph
     */
    int size();

}
