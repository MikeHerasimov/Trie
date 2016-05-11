package com.github.mikeherasimov.trie.linked;

import com.github.mikeherasimov.trie.OptimizerBehaviour;

class LinkedOptimizerBehaviour implements OptimizerBehaviour<LinkedNode> {
    public static final LinkedOptimizerBehaviour INSTANCE = new LinkedOptimizerBehaviour();

    private LinkedOptimizerBehaviour(){

    }

    @Override
    public void changeRefs(LinkedNode ancestor, LinkedNode internal, LinkedNode dest) {
        if(ancestor.getChild() == internal){
            ancestor.setChild(dest);
        } else {
            LinkedNode brother = ancestor.getChild();

            while(brother.getBrother() != internal)
                brother = brother.getBrother();
            brother.setBrother(dest);
        }
    }

    @Override
    public int countNodes(LinkedNode current) {
        return current.numberOfNodesInSubtrie();
    }
}
