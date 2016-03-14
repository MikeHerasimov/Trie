package com.github.mikeherasimov.trie.array;

import com.github.mikeherasimov.trie.SubtrieConverter;
import gnu.trove.list.linked.TIntLinkedList;

final class ArraySubtrieConverter implements SubtrieConverter<ArrayNode>{
    private static int recursiveCallsCount = -1;

    private final ArrayNode root;
    private final int numberOfNodes;

    public ArraySubtrieConverter(ArrayNode root){
        this.root = root;
        numberOfNodes = root.numberOfNodesInSubtrie();
    }

    @Override
    public ArrayNode[] getNodes() {
        ArrayNode[] nodes = new ArrayNode[numberOfNodes];
        getNodes(nodes, root);
        recursiveCallsCount = -1;
        return nodes;
    }

    public void getNodes(ArrayNode[] nodes, ArrayNode current){
        nodes[++recursiveCallsCount] = current;
        for (int i = 0; i < current.numberOfDescendants(); i++){
            ArrayNode child = current.getChild(i);
            if(child != null){
                getNodes(nodes, child);
            }
        }
    }

    @Override
    public int[] getAncestorIndexes() {
        int[] ancestorIndexes = new int[numberOfNodes];
        getAncestorIndexes(ancestorIndexes, root, 0);
        recursiveCallsCount = -1;
        return ancestorIndexes;
    }

    public void getAncestorIndexes(int[] ancestorIndexes, ArrayNode current, int curAncestorIndex){
        ancestorIndexes[++recursiveCallsCount] = curAncestorIndex;
        int numberOfCurrentCall = recursiveCallsCount;
        for (int i = 0; i < current.numberOfDescendants(); i++) {
            ArrayNode child = current.getChild(i);
            if (child != null) {
                getAncestorIndexes(ancestorIndexes, child, numberOfCurrentCall);
            }
        }
    }

    @Override
    public int[] getLeafIndexes() {
        TIntLinkedList leafIndexes = new TIntLinkedList();
        getLeafIndexes(leafIndexes, root);
        recursiveCallsCount = -1;
        return leafIndexes.toArray();
    }

    public void getLeafIndexes(TIntLinkedList leafIndexes, ArrayNode current){
        int numberOfCurrentCall = ++recursiveCallsCount;
        boolean isLeaf = true;
        for (int i = 0; i < current.numberOfDescendants(); i++) {
            ArrayNode child = current.getChild(i);
            if(child != null){
                getLeafIndexes(leafIndexes, child);
                isLeaf = false;
            }
        }
        if(isLeaf){
            leafIndexes.add(numberOfCurrentCall);
        }
    }
}
