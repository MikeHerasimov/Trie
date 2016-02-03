package com.github.mikeherasimov.trie.array;

import com.github.mikeherasimov.trie.Node;

import java.util.Arrays;

final class ArrayNode implements Node {

    private boolean EOW;
    private char letter;
    private ArrayNode[] children;

    public ArrayNode(int size){
        children = new ArrayNode[size];
    }

    public ArrayNode(char letter, boolean EOW, int size){
        this.letter = letter;
        this.EOW = EOW;
        children = new ArrayNode[size];
    }

    public static ArrayNode newInstance(ArrayNode node){
        ArrayNode root = weakCopy(node);
        copy(node, root);
        return root;
    }

    public void addChild(ArrayNode node, int pos) {
        children[pos] = node;
    }

    public ArrayNode getChild(int pos) {
        return children[pos];
    }

    @Override
    public void setAsEOW(){
        EOW = true;
    }

    @Override
    public char getLetter() {
        return letter;
    }

    @Override
    public boolean getEOW() {
        return EOW;
    }

    @Override
    public String toString() {
        return "[" + letter + " " + EOW + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof ArrayNode)) return false;
        ArrayNode node = (ArrayNode) obj;
        return node.letter == letter && node.EOW == EOW &&
                Arrays.equals(children, node.children);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (EOW ? 1:0);
        result = 31 * result + letter;
        result = 31 * result + Arrays.hashCode(children);
        return result;
    }

    int numberOfDescendants(){
        return children.length;
    }

    private static void copy(ArrayNode ancestor, ArrayNode copyAncestor){
        for (int i = 0; i < ancestor.children.length; i++){
            ArrayNode child = ancestor.getChild(i);
            if(child != null){
                ArrayNode copyChild = weakCopy(child);
                copyAncestor.addChild(copyChild, i);
                copy(child, copyChild);
            }
        }
    }

    private static ArrayNode weakCopy(ArrayNode node){
        return new ArrayNode(node.letter, node.EOW,
                node.children.length);
    }


}
