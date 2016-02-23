package com.github.mikeherasimov.trie.array;

import com.github.mikeherasimov.trie.DAWG;

/**
 * ArrayDAWG represents optimized ArrayTrie data-structure
 */
public final class ArrayDAWG implements DAWG {
    private ArrayTrie trie;

    ArrayDAWG(ArrayTrie trie){
        this.trie = trie;
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
}
