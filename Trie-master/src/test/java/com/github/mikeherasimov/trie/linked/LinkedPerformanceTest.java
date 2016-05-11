package com.github.mikeherasimov.trie.linked;


import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LinkedPerformanceTest {

    private static LinkedDAWG dawg;
    private static LinkedTrie trie;
    private static List<String> wordList;

    @BeforeClass
    public static void setUp() throws Exception {
        wordList = new ArrayList<>();
        fillListWithLines(wordList, "src\\test\\dict\\Dictionary_en.txt");
        trie = new LinkedTrie();
        for (String item : wordList) {
            trie.add(item);
        }
        dawg = trie.toDAWG();
    }

    private static void fillListWithLines(List<String> wordList, String filename) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(filename);
        InputStreamReader streamReader = new InputStreamReader(fileInputStream, "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(streamReader);

        String line;
        while ((line = bufferedReader.readLine())!= null){
            wordList.add(line);
        }

        fileInputStream.close();
        streamReader.close();
        bufferedReader.close();
    }

    @Test
    public void writeTrie() throws Exception{
        long begin, end;
        begin = System.nanoTime();

        FileOutputStream fos = new FileOutputStream("testLinkedTrie.txt");
        ObjectOutputStream out = new ObjectOutputStream(fos);
        trie.writeExternal(out);
        out.flush();
        out.close();
        fos.close();

        end = System.nanoTime();
        System.out.println("WriteTrie " + (float) (end - begin) / 1000000);
    }

    @Test
    public void readTrie() throws Exception{
        long begin, end;
        begin = System.nanoTime();

        FileInputStream fis = new FileInputStream("testLinkedTrie.txt");
        ObjectInputStream in = new ObjectInputStream(fis);
        LinkedTrie linkedTrie = new LinkedTrie();
        linkedTrie.readExternal(in);
        in.close();
        fis.close();

        end = System.nanoTime();
        System.out.println("ReadTrie " +(float) (end - begin) / 1000000);
    }

    @Test
    public void writeDAWG() throws Exception{
        long begin, end;
        begin = System.nanoTime();

        FileOutputStream fos = new FileOutputStream("testLinkedDAWG.txt");
        ObjectOutputStream out = new ObjectOutputStream(fos);
        dawg.writeExternal(out);
        out.flush();
        out.close();
        fos.close();

        end = System.nanoTime();
        System.out.println("WriteDAWG " + (float) (end - begin) / 1000000);
    }

    @Test
    public void readDAWG() throws Exception{
        long begin, end;
        begin = System.nanoTime();

        FileInputStream fis = new FileInputStream("testLinkedDAWG.txt");
        ObjectInputStream in = new ObjectInputStream(fis);
        LinkedDAWG linkedDAWG = new LinkedDAWG();
        linkedDAWG.readExternal(in);
        in.close();
        fis.close();

        end = System.nanoTime();
        System.out.println("ReadDAWG " + (float) (end - begin) / 1000000);
    }
}
