package com.github.mikeherasimov.trie;

/**
 * Alphabet is interface intended for defining alphabets used to create new Trie instances.
 * Main reason to use this interface is making such alphabets reusable by
 * creating enum and pasting couple alphabets as his instances, or by creating singleton of custom alphabet.
 */
public interface Alphabet {

    /**
     * returns String object that contains alphabet's letters
     *
     * @return  String object that contains alphabet's letters
     */
    String characters();

}
