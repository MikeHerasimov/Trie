package com.github.mikeherasimov.trie;


/**
 * CommonAlphabets is enum, which defines couple commonly used alphabets
 * in purpose of making this alphabets reusable
 */
public enum CommonAlphabets implements Alphabet{
    ENG("abcdefghijklmnopqrstuvwxyz"),
    UA("абвгґдеєжзиіїйклмнопрстуфхцчшщьюя"),
    RU("абвгдеёжзийклмнопрстуфхцчшщъыьэюя");

    private final String characters;

    CommonAlphabets(String characters){
        this.characters = characters;
    }

    @Override
    public String characters() {
        return characters;
    }

}
