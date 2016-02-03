package com.github.mikeherasimov.trie.test;

import com.github.mikeherasimov.trie.array.ArrayTrie;
import com.github.mikeherasimov.trie.DAWG;
import com.github.mikeherasimov.trie.Trie;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ArrayTrieTest {
    Trie trie;

    @Before
    public void setUp() throws Exception {
        trie = new ArrayTrie();
        trie.add("war");
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test (expected = IllegalArgumentException.class)
    public void testAdd() throws Exception {
        trie.add("landscape");
        trie.add("land");
        trie.add("war");
        assertTrue(trie.size() == 3);
        trie.add("wa*");
    }

    @Test
    public void testIsEmpty() throws Exception {
        assertFalse(trie.isEmpty());
        trie.clear();
        assertTrue(trie.isEmpty());
    }

    @Test
    public void testClear() throws Exception {
        trie.add("warrior");
        trie.add("wall");
        trie.clear();
        assertTrue(trie.isEmpty());
        assertTrue(trie.size() == 0);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testContains() throws Exception {
        trie.add("warrior");
        trie.add("wall");
        assertTrue(trie.contains("wall"));
        assertTrue(trie.contains("warrior"));
        assertFalse(trie.contains("word"));
        assertFalse(trie.contains("wa"));
        trie.contains("wa*");
    }

    @Test
    public void testSize() throws Exception {
        trie.add("war");
        assertTrue(trie.size() == 1);
        trie.add("warrior");
        trie.add("wall");
        assertTrue(trie.size() == 3);
        trie.clear();
        assertTrue(trie.size() == 0);
    }

    @Test
    public void testToDAWG() {
        Trie trie = new ArrayTrie("абвгґдеєжзиіїйклмнопрстуфхцчшщьюя");
        String[] words = {"бар", "барабан", "баран", "балон", "бал", "балка", "батон"};
        for (String item : words){
            trie.add(item);
        }

        DAWG dawg = trie.toDAWG();
        boolean containsAllWords = true;
        for (String item : words){
            containsAllWords &= dawg.contains(item);
        }
        assertTrue(containsAllWords);
    }

    @Test
    public void testSerializationAndDeserialization() throws Exception {
        ArrayTrie trie = new ArrayTrie("абвгґдеєжзиіїйклмнопрстуфхцчшщьюя");
        String[] words = {"бар", "барабан", "баран", "балон", "бал", "балка", "батон"};
        for (String item : words){
            trie.add(item);
        }

        FileOutputStream fos = new FileOutputStream("testArray.txt");
        ObjectOutputStream out = new ObjectOutputStream(fos);
        trie.writeExternal(out);
        out.flush();
        out.close();
        fos.close();

        FileInputStream fis = new FileInputStream("testArray.txt");
        ObjectInputStream in = new ObjectInputStream(fis);
        ArrayTrie copy = new ArrayTrie();
        copy.readExternal(in);
        in.close();
        fis.close();

        boolean containsAllWords = true;
        for (String item : words){
            containsAllWords &= copy.contains(item);
        }
        assertTrue(containsAllWords);
        assertTrue(copy.equals(trie));
    }
}