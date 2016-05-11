package com.github.mikeherasimov.trie.linked;

import com.github.mikeherasimov.trie.SubtrieConverter;
import gnu.trove.list.linked.TIntLinkedList;

class LinkedSubtrieConverter implements SubtrieConverter<LinkedNode>{
    private static int recursiveCallsCount = -1;

    private final LinkedNode root;
    private final int numberOfNodes;

    public LinkedSubtrieConverter(LinkedNode root){
        this.root = root;
        numberOfNodes = root.numberOfNodesInSubtrie();
    }

    @Override
    public LinkedNode[] getNodes() {
        LinkedNode[] nodes = new LinkedNode[numberOfNodes];
        getNodes(nodes, root);
        recursiveCallsCount = -1;
        return nodes;
    }

    private void getNodes(LinkedNode[] nodes, LinkedNode current){
        nodes[++recursiveCallsCount] = current;
        if(current.getChild() != null){
            getNodes(nodes, current.getChild());
        }
        if(current.getBrother() != null){
            getNodes(nodes, current.getBrother());
        }
    }

    @Override
    public int[] getAncestorIndexes() {
        int[] ancestorIndexes = new int[numberOfNodes];
        getAncestorIndexes(ancestorIndexes, root, 0);
        recursiveCallsCount = -1;
        return ancestorIndexes;
    }

    private void getAncestorIndexes(int[] ancestorIndexes, LinkedNode current, int curAncestorIndex){
        ancestorIndexes[++recursiveCallsCount] = curAncestorIndex;
        if(current.getChild() != null){
            getAncestorIndexes(ancestorIndexes, current.getChild(), recursiveCallsCount);
        }
        if(current.getBrother() != null){
            getAncestorIndexes(ancestorIndexes, current.getBrother(), curAncestorIndex);
        }
    }

    @Override
    public int[] getLeafIndexes() {
        TIntLinkedList leafIndexes = new TIntLinkedList();
        getLeafIndexes(leafIndexes, root);
        recursiveCallsCount = -1;
        return leafIndexes.toArray();
    }

    private void getLeafIndexes(TIntLinkedList leafIndexes, LinkedNode current){
        recursiveCallsCount++;
        if(current.getChild() != null){
            getLeafIndexes(leafIndexes, current.getChild());
        } else {
            leafIndexes.add(recursiveCallsCount);
        }
        if(current.getBrother() != null){
            getLeafIndexes(leafIndexes, current.getBrother());
        }
    }
}
