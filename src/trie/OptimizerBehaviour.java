package trie;

/**
 * OptimizerBehaviour is strategy interface that defines behaviour of Optimizer class algorithms
 *
 * @param <T>  class that implements <code>Node</code> interface.
 *             In other words one of <code>Node</code> realization.
 */
public interface OptimizerBehaviour <T extends Node>{

    /**
     * Makes replacement of reference that pointing to duplicate node
     * to another duplicate with purpose of elimination all but one duplicate nodes.
     *
     * @param ancestor  node that is ancestor to node, reference to which will be replaced
     * @param internal  duplicate node, reference to which has to be replaced
     * @param dest      another duplicate, to which reference that pointed to internal node has to point
     */
    void changeRefs(T ancestor, T internal, T dest);

    /**
     * Returns number of nodes in subtrie, specified by its root node
     *
     * @param current  root node of subtrie
     * @return         number of nodes in subtrie
     */
    int countNodes(T current);

}
