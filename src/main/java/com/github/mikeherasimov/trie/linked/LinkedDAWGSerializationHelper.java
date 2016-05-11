package com.github.mikeherasimov.trie.linked;

import gnu.trove.map.custom_hash.TObjectIntCustomHashMap;
import gnu.trove.strategy.IdentityHashingStrategy;

import java.util.Arrays;

class LinkedDAWGSerializationHelper {
    private static int recursiveCallsCount = -1;

    private final LinkedNode root;
    private final int numberOfNodes;
    private int nuberOfDistinctNodes;
    private char[] letters;
    private boolean[] eows;
    private int[] childIndexes;
    private int[] brotherIndexes;

    public LinkedDAWGSerializationHelper(LinkedNode root) {
        this.root = root;
        this.numberOfNodes = root.numberOfNodesInSubtrie();
        letters = new char[numberOfNodes];
        eows = new boolean[numberOfNodes];
        childIndexes = new int[numberOfNodes];
        brotherIndexes = new int[numberOfNodes];
    }

    public void calculateValues(){
        TObjectIntCustomHashMap<LinkedNode> identityMap =
                new TObjectIntCustomHashMap<>(IdentityHashingStrategy.INSTANCE, numberOfNodes);
        calculateValues(identityMap, root);
        nuberOfDistinctNodes = identityMap.size();
        recursiveCallsCount = -1;
    }

    private int calculateValues(TObjectIntCustomHashMap<LinkedNode> identityMap, LinkedNode current){
        if (identityMap.contains(current)){
            return identityMap.get(current);
        }
        int numberOfCurrentCall = ++recursiveCallsCount;
        identityMap.put(current, numberOfCurrentCall);
        collectNode(current, numberOfCurrentCall);
        if (current.getChild() != null){
            childIndexes[numberOfCurrentCall] = calculateValues(identityMap, current.getChild());
        }
        if (current.getBrother() != null){
            brotherIndexes[numberOfCurrentCall] = calculateValues(identityMap, current.getBrother());
        }
        return numberOfCurrentCall;
    }

    private void collectNode(LinkedNode node, int i){
        letters[i] = node.getLetter();
        eows[i] = node.getEOW();
    }

    public char[] getLetters() {
        return Arrays.copyOfRange(letters, 0, nuberOfDistinctNodes);
    }

    public boolean[] getEOWs() {
        return Arrays.copyOfRange(eows, 0, nuberOfDistinctNodes);
    }

    public int[] getChildIndexes() {
        return Arrays.copyOfRange(childIndexes, 0, nuberOfDistinctNodes);
    }

    public int[] getBrotherIndexes() {
        return Arrays.copyOfRange(brotherIndexes, 0, nuberOfDistinctNodes);
    }
}
