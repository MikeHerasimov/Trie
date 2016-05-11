package com.github.mikeherasimov.trie.linked;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static org.junit.Assert.assertTrue;


public class LinkedDAWGTest {

    private static LinkedDAWG dawg;
    private static String[] words = {"бар", "барабан", "баран", "балон", "бал", "балка", "батон"};

    @BeforeClass
    public static void setUp() throws Exception {
        LinkedTrie trie = new LinkedTrie();
        for (String item : words){
            trie.add(item);
        }

        dawg = trie.toDAWG();
    }

    @Test
    public void contains() throws Exception {
        boolean containsAllWords = true;
        for (String item : words){
            containsAllWords &= dawg.contains(item);
        }
        assertTrue(containsAllWords);
    }

    @Test
    public void writeExternal() throws Exception {
        FileOutputStream fos = new FileOutputStream("testLinkedDAWG.txt");
        ObjectOutputStream out = new ObjectOutputStream(fos);
        dawg.writeExternal(out);
        out.flush();
        out.close();
        fos.close();
    }

    @Test
    public void readExternal() throws Exception {
        FileInputStream fis = new FileInputStream("testLinkedDAWG.txt");
        ObjectInputStream in = new ObjectInputStream(fis);
        LinkedDAWG linkedDAWG = new LinkedDAWG();
        linkedDAWG.readExternal(in);
        in.close();
        fis.close();

        boolean containsAllWords = true;
        for (String item : words){
            containsAllWords &= linkedDAWG.contains(item);
        }
        assertTrue(containsAllWords);
    }

}