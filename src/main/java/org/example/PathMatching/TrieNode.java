package org.example.PathMatching;

public class TrieNode {
    public int pathCount = 0;
    private final TrieNode[] children = new TrieNode[4];

    public TrieNode getChild(int index) {
        return children[index];
    }

    public TrieNode addChild(int index, TrieNode child) {
        children[index] = child;
        return child;
    }

}
