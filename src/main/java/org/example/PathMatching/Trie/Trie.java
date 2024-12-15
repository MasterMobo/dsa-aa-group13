package org.example.PathMatching.Trie;

import java.util.Stack;

// IMPORTANT: This is a failed solution to path matching. This is not used in the final program.

public class Trie {
    private final int requiredLength;
    TrieNode root;

    public Trie(int requiredLength) {
        this.requiredLength = requiredLength;
        this.root = new TrieNode();
    }

    // Convert direction character to children index of Trie Node
    private int dirToIndex(char direction) {
        return switch (direction) {
            case 'U':
                yield 0;
            case 'R':
                yield 1;
            case 'D':
                yield 2;
            case 'L':
                yield 3;
            default:
                yield -1;
        };
    }

    // Insert a new string into the trie
    public void insert(String s) {
        if (s.length() != requiredLength) {
            System.out.println("ERROR: Need string of length " + requiredLength);
            return;
        }

        TrieNode current = root;

        for (int i = 0; i < s.length(); i++) {
            int childIndex = dirToIndex(s.charAt(i));

            TrieNode child = current.getChild(childIndex);

            // If child exists, traverse child
            if (child != null) {
                current.pathCount++;
                current = child;
                continue;
            }

            // If child does not exist, push new node and traverse there
            current.pathCount++;
            current = current.addChild(childIndex, new TrieNode());
        }
    }

    // Find the number of matches given a string.
    // '*' in the string means this could be any character.
    public int countMatches(String s) {
        if (s.length() != requiredLength) {
            System.out.println("ERROR: Need string of length " + requiredLength);
            return -1;
        }

        // Keep track of current node
        TrieNode current;

        // (Old) Stack of current level, will be popped out and push onto nextStack at start of new level
        Stack<TrieNode> stack = new Stack<>();
        stack.push(root);

        // Stack of next level, receives new nodes from stack. Is swapped with stack after each level
        Stack<TrieNode> nextStack = new Stack<>();

        // Breakpoint for early traversal escape
        int breakPoint = indexOfLastStarSequence(s);

        // Go through each level
        for (int i = 0; i < s.length(); i++) {
            // Early check so we don't have to go through the whole string
            if (stack.isEmpty()) return 0;

            // Early breakpoint so we don't have to traverse the whole trie
            if (i == breakPoint) {
                int result = 0;
                while (!stack.isEmpty()) {
                    result += stack.pop().pathCount;
                }
                return result;
            }

            // Go through the old stack
            while (!stack.isEmpty()) {
                current = stack.pop();

                char c = s.charAt(i);

                // Wildcard direction
                if (c == '*') {
                    // Explore all non-null children
                    for (int j = 0; j < 4; j++) {
                        TrieNode child = current.getChild(j);
                        if (child != null)
                            nextStack.push(child);
                    }

                    continue;
                }

                // Normal direction
                int childIndex = dirToIndex(c);
                TrieNode child = current.getChild(childIndex);

                // Explore only if child is the wanted direction
                if (child != null)
                    nextStack.push(child);
            }

            // Swap the stacks for next level
            Stack<TrieNode> temp = stack;
            stack = nextStack;
            nextStack = temp;
        }

        // Remaining nodes in stack should have successfully gone through the filtering process
        // Therefore, the stack size is the number of matches
        return stack.size();
    }


    // Find the first index of the last continuous sequence of *
    private int indexOfLastStarSequence(String s) {
        for (int i = s.length() - 1; i >= 0; i--) {
            if (s.charAt(i) != '*') {
                return i + 1;
            }
        }

        // If there is no * in the string
        return 0;
    }
}
