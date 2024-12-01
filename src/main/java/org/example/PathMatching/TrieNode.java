package org.example.PathMatching;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class TrieNode implements Serializable {
    private static final long serialVersionUID = 1L;
    public int pathCount = 0;
    private final TrieNode[] children = new TrieNode[4];
    private Map<String, TrieNode> wildCardChildren;
    private byte endPosition = -1;

    public TrieNode getChild(int index) {
        return children[index];
    }

    public TrieNode addChild(int index, TrieNode child) {
        children[index] = child;
        return child;
    }

    // New methods to support wildcard functionality
    public synchronized TrieNode getOrCreateWildCardChild(String path) {
        if (wildCardChildren == null) {
            wildCardChildren = new HashMap<>();
        }
        return wildCardChildren.computeIfAbsent(path, k -> new TrieNode());
    }

    public Map<String, TrieNode> getWildCardChildren() {
        return wildCardChildren;
    }

    public byte getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(byte position) {
        this.endPosition = position;
    }

    // Helper method to check if this node has any wildcard children
    public boolean hasWildCardChildren() {
        return wildCardChildren != null && !wildCardChildren.isEmpty();
    }
}