package com.github.mikeherasimov.trie.linked;

import com.github.mikeherasimov.trie.Alphabet;
import com.github.mikeherasimov.trie.DAWG;

/**
 * LinkedDAWG represents optimized LinkedTrie data-structure.
 */
public final class LinkedDAWG implements DAWG {
    private LinkedTrie trie;

    LinkedDAWG(LinkedTrie trie){
        this.trie = trie;
    }

    /**
     * Builder is inner class, which can be used to create LinkedDAWG object from supplied words.
     */
    public static class Builder{
        private LinkedTrie trie;

        /**
         * Returns Builder object
         */
        public Builder(){
            trie = new LinkedTrie();
        }

        /**
         * Returns Builder object, supported by specified alphabet
         *
         * @param alphabet  specified alphabet
         */
        public Builder(String alphabet){
            trie = new LinkedTrie(alphabet);
        }

        /**
         * Returns new Builder object, supported by specified Alphabet`s instance.
         *
         * @param alphabet  specified <code>Alphabet</code>`s instance
         */
        public Builder(Alphabet alphabet){
            trie = new LinkedTrie(alphabet);
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
         * Generates new LinkedDAWG object from supplied words and returns it.
         * Note that this method doesn't creates copy of LinkedTrie that it holds
         * it just makes optimization of it to DAWG and returns respective LinkedDAWG object.
         *
         * @return  respective <code>LinkedDAWG</code> object
         */
        public LinkedDAWG build(){
            return trie.toDAWG();
        }
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
