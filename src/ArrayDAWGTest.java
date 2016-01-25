import org.junit.Test;
import trie.ArrayDAWG;

import static org.junit.Assert.*;

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