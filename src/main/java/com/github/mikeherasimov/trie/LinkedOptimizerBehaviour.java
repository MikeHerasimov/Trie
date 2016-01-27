package com.github.mikeherasimov.trie;

final class LinkedOptimizerBehaviour implements OptimizerBehaviour<LinkedNode>{
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
        int child = current.getChild() == null ? 0 : countNodes(current.getChild());
        int brother = current.getBrother() == null ? 0 : countNodes(current.getBrother());
        return 1 + child + brother;
    }
}
