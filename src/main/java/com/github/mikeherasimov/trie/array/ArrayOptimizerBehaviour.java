package com.github.mikeherasimov.trie.array;

import com.github.mikeherasimov.trie.OptimizerBehaviour;

final class ArrayOptimizerBehaviour implements OptimizerBehaviour<ArrayNode> {
    public static final ArrayOptimizerBehaviour INSTANCE = new ArrayOptimizerBehaviour();

    private ArrayOptimizerBehaviour(){

    }

    @Override
    public void changeRefs(ArrayNode ancestor, ArrayNode internal, ArrayNode dest) {
        for (int i = 0; i < ancestor.numberOfDescendants(); i++){
            ArrayNode child = ancestor.getChild(i);
            if(child == internal){
                ancestor.addChild(dest, i);
                return;
            }
        }
    }

    @Override
    public int countNodes(ArrayNode current) {
        return current.numberOfNodesInSubtrie();
    }
}
