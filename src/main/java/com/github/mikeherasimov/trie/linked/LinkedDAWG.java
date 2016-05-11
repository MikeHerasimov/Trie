package com.github.mikeherasimov.trie.linked;

import com.github.mikeherasimov.trie.DAWG;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * LinkedDAWG represents optimized LinkedTrie data-structure.
 */
public final class LinkedDAWG implements DAWG, Externalizable {
    private LinkedTrie trie;

    LinkedDAWG(LinkedTrie trie){
        this.trie = trie;
    }

    public LinkedDAWG() {
        this(new LinkedTrie());
    }

    @Override
    public boolean contains(String word) {
        return trie.contains(word);
    }

    @Override
    public boolean isPrefix(String prefix) {
        return trie.isPrefix(prefix);
    }

    @Override
    public int size() {
        return trie.size();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        LinkedDAWGSerializationHelper helper = trie.getDAWGSerializationHelper();
        helper.calculateValues();
        out.writeInt(trie.size());
        out.writeObject(helper.getLetters());
        out.writeObject(helper.getEOWs());
        out.writeObject(helper.getChildIndexes());
        out.writeObject(helper.getBrotherIndexes());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        int size = in.readInt();
        char[] letters = (char[]) in.readObject();
        boolean[] eows = (boolean[]) in.readObject();
        int[] childIndexes = (int[]) in.readObject();
        int[] brotherIndexes = (int[]) in.readObject();
        this.trie = new LinkedTrie(size, generateRootNode(letters, eows, childIndexes, brotherIndexes));
    }

    private LinkedNode generateRootNode(char[] letters, boolean[] eows, int[] childIndexes, int[] brotherIndexes){
        LinkedNode[] nodes = new LinkedNode[letters.length];
        fillNodes(nodes, letters, eows);
        restoreReferences(nodes, childIndexes, brotherIndexes);
        return nodes[0];
    }


    private void fillNodes(LinkedNode[] nodes, char[] letters, boolean[] eows){
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = new LinkedNode(letters[i], eows[i]);
        }
    }

    private void restoreReferences(LinkedNode[] nodes, int[] childIndexes, int[] brotherIndexes){
        for (int i = 0; i < nodes.length; i++) {
            if (childIndexes[i] != 0) {
                nodes[i].setChild(nodes[childIndexes[i]]);
            }
            if (brotherIndexes[i] != 0) {
                nodes[i].setBrother(nodes[brotherIndexes[i]]);
            }
        }
    }
}
