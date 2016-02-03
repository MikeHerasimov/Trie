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

    /**
     * Builder is inner class, which can be used to create ArrayDAWG object from supplied words.
     */
    public static class Builder{
        private ArrayTrie trie;

        /**
         * Returns Builder object
         */
        public Builder(){
            trie = new ArrayTrie();
        }

        /**
         * Returns Builder object, supported by specified alphabet
         *
         * @param alphabet  specified alphabet
         */
        public Builder(String alphabet){
            trie = new ArrayTrie(alphabet);
        }

        /**
         * Adds specified words to builder and returns it
         *
         * @param word  specified word
         * @return      <code>Builder</code> object
         */
        public Builder put(String word){
            trie.add(word);
            return this;
        }

        /**
         * Generates new ArrayDAWG object from supplied words and returns it.
         * Note that this method doesn't creates copy of ArrayTrie that it holds
         * it just makes optimization of it to DAWG and returns respective ArrayDAWG object.
         *
         * @return  respective <code>ArrayDAWG</code> object
         */
        public ArrayDAWG build(){
            ArrayDAWG arrayDAWG = (ArrayDAWG) ArrayTrie.toDAWG(trie);
            trie = null;
            return arrayDAWG;
        }
    }

    @Override
    public boolean contains(String word) {
        return trie.contains(word);
    }

    @Override
    public int size() {
        return trie.size();
    }
}
