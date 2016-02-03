package com.github.mikeherasimov.trie.linked;

import com.github.mikeherasimov.trie.Node;

import java.util.Objects;

final class LinkedNode implements Node {
	
	private boolean EOW;
	private char letter;
	
	private LinkedNode brother;
	private LinkedNode child;

	public LinkedNode() {

	}
	
	public LinkedNode(char letter, boolean EOW) {
		this.letter = letter;
		this.EOW = EOW;
	}

	public static LinkedNode newInstance(LinkedNode node){
		LinkedNode root = weakCopy(node);
		copy(node, root);
		return root;
	}
	
	public void setBrother(LinkedNode brother) {
		this.brother = brother;
	}
	
	public void setChild(LinkedNode child) {
		this.child = child;
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
	
	public LinkedNode getChild() {
		return child;
	}
	
	public LinkedNode getBrother() {
		return brother;
	}
	
	public LinkedNode getLastBrother(){
		LinkedNode node = this;
		while(node.getBrother()!=null){
			node = node.getBrother();
		}
		return node;
	}
	
	@Override
	public String toString() {
		return "[" + letter + " " + EOW + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == this) return true;
		if(!(obj instanceof LinkedNode)) return false;
		LinkedNode node = (LinkedNode) obj;
		return node.getLetter() == letter && node.getEOW() == EOW &&
				Objects.equals(child, node.getChild()) &&
				Objects.equals(brother, node.getBrother());
	}
	
	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + (EOW ? 1:0);
		result = 31 * result + letter;
		result = 31 * result + (brother == null ? 0:brother.hashCode());
		result = 31 * result + (child == null ? 0:child.hashCode());
		return result;
	}

	private static void copy(LinkedNode node, LinkedNode copy){
		if(node.child != null) {
			LinkedNode childCopy = weakCopy(node.child);
			copy.setChild(childCopy);
			copy(node.child, copy.child);
		}
		if(node.brother != null) {
			LinkedNode brotherCopy = weakCopy(node.brother);
			copy.setBrother(brotherCopy);
			copy(node.brother, copy.brother);
		}
	}

	private static LinkedNode weakCopy(LinkedNode node){
		return new LinkedNode(node.letter, node.EOW);
	}


}
