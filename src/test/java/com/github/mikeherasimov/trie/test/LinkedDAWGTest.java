package com.github.mikeherasimov.trie.test;

import com.github.mikeherasimov.trie.linked.LinkedDAWG;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class LinkedDAWGTest {

    @Test
    public void testBuilder(){
        LinkedDAWG linkedDAWG = new LinkedDAWG.Builder()
                .put("troll")
                .put("wall")
                .build();
        assertTrue(linkedDAWG.size() == 2);
        assertTrue(linkedDAWG.contains("wall") && linkedDAWG.contains("troll"));
    }

}