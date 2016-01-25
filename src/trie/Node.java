package trie;

/**
 * Node is base interface for all node realizations in trie package.
 * Node contains methods, each of which defines basic behaviour of its realizations.
 */
public interface Node {

    /**
     * Sets node as end-of-word node
     */
    void setAsEOW();

    /**
     * Returns letter, which node contains
     *
     * @return  the letter, which node contains
     */
    char getLetter();

    /**
     * Returns true if node is end-of-word node
     *
     * @return  <code>true</code> if node is end-of-word node
     */
    boolean getEOW();

}
