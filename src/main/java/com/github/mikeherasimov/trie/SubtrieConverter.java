package com.github.mikeherasimov.trie;

/**
 * SubtrieConverter is a interface, that contains algorithms of converting subtrie to array of nodes
 * or array of ancestor`s indexes or array of leaf`s indexes.
 * Instance of this interface can be used to instantiate <code>Optimizer</code> instead of supplying <p>
 * array of nodes, array of ancestor`s indexes and array of leaf`s indexes as params explicitly.
 *
 * @param <T>  class that implements <code>Node</code> interface.
 *             In other words one of <code>Node</code> realization.
 */
public interface SubtrieConverter <T extends Node> {

    /**
     * Returns all nodes of subtrie, stored in array
     *
     * @return  all nodes of subtrie, stored in array
     */
    T[] getNodes();

    /**
     * Returns array of ancestor`s indexes
     *
     * @return  array of ancestor`s indexes
     */
    int[] getAncestorIndexes();

    /**
     * Returns array of leaf`s indexes
     *
     * @return  array of leaf`s indexes
     */
    int[] getLeafIndexes();

}
