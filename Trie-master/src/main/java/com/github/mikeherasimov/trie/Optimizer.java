package com.github.mikeherasimov.trie;

import java.util.BitSet;

/**
 * Optimizer class makes optimization of all realizations of Trie interface.
 *
 * @param <T>  class that implements <code>Node</code> interface.
 *             In other words one of <code>Node</code> realization.
 */
public final class Optimizer <T extends Node>{
    private OptimizerBehaviour<T> optimizerBehaviour;

    private T[] nodes;
    private int[] ancestors;
    private int[] leafs;

    /**
     * Creates and returns new instance of Optimizer class.
     * Supplied by: <p>
     * One of optimizerBehaviour strategy interface realization, <p>
     * All nodes of Trie, stored in array, where descendants of particular node has to have bigger indexes than their ancestor <p>
     * Array of ancestor-indexes, where index to ancestor from current node-index is present. <p>
     * Array of leaf-indexes, where indexes of leafs are stored.
     *
     * @param optimizerBehaviour  strategy interface realization
     * @param nodes               all nodes of <code>Trie</code>, stored in array
     * @param ancestors           array of ancestor-indexes
     * @param leafs               array of leaf-indexes
     */
    public Optimizer(OptimizerBehaviour<T> optimizerBehaviour, T[] nodes, int[] ancestors, int[] leafs){
        this.optimizerBehaviour = optimizerBehaviour;
        this.nodes = nodes;
        this.ancestors = ancestors;
        this.leafs = leafs;
    }


    /**
     * Creates and returns new instance of Optimizer class.
     * Supplied by: <p>
     * One of optimizerBehaviour strategy interface realization, <p>
     * Instance of SubtrieConverter interface.
     *
     * @param optimizerBehaviour  strategy interface realization
     * @param subtrieConverter    instance of <code>SubtrieConverter</code> interface
     */
    public Optimizer(OptimizerBehaviour<T> optimizerBehaviour, SubtrieConverter<T> subtrieConverter){
        this.optimizerBehaviour = optimizerBehaviour;
        nodes = subtrieConverter.getNodes();
        ancestors = subtrieConverter.getAncestorIndexes();
        leafs = subtrieConverter.getLeafIndexes();
    }

    /**
     * Makes search of duplicates and eliminates all but one,
     * using algorithms of supplied optimizerBehaviour implementation.
     */
    public void eliminateDuplicates(){
        BitSet bitSet = new BitSet(nodes.length);
        for (int i = 0; i < leafs.length-1; i++) {
            int curLeaf = leafs[i];
            T current = nodes[curLeaf];
            if (bitSet.get(curLeaf)){
                continue;
            }

            for (int j = i+1; j < leafs.length; j++){
                int repLeaf = leafs[j];
                if(bitSet.get(repLeaf) || curLeaf == repLeaf){
                    continue;
                }
                T replace = nodes[repLeaf];

                compareAndEliminate(current, curLeaf, replace, repLeaf, j, bitSet);
            }
        }
    }

    private void compareAndEliminate(T current, int curLeaf, T replace, int repLeaf,
                                     int index, BitSet bitSet){
        boolean matchFound = false;
        int curDescendant = 0;
        int repDescendant = 0;

        while(current.equals(replace)){
            matchFound = true;
            curDescendant = curLeaf;
            repDescendant = repLeaf;
            curLeaf = ancestors[curLeaf];
            repLeaf = ancestors[repLeaf];
            current = nodes[curLeaf];
            replace = nodes[repLeaf];
        }
        if(matchFound){
            leafs[index] = repLeaf;
            optimizerBehaviour.changeRefs(replace, nodes[repDescendant], nodes[curDescendant]);
            setBits(bitSet, repDescendant);
        }
    }

    private void setBits(BitSet bitSet, int rootIndex){
        int subtrieNodeCount = optimizerBehaviour.countNodes(nodes[rootIndex]);
        bitSet.set(rootIndex, rootIndex + subtrieNodeCount);
    }

}
