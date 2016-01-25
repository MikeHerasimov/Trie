import org.junit.Test;
import trie.LinkedDAWG;

import static org.junit.Assert.*;

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