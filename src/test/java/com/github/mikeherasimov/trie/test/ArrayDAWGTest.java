package com.github.mikeherasimov.trie.test;

import com.github.mikeherasimov.trie.array.ArrayDAWG;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ArrayDAWGTest {

    @Test
    public void testBuilder(){
        ArrayDAWG arrayDAWG = new ArrayDAWG.Builder()
                .put("troll")
                .put("wall")
                .build();
        assertTrue(arrayDAWG.size() == 2);
        assertTrue(arrayDAWG.contains("wall") && arrayDAWG.contains("troll"));
    }

}