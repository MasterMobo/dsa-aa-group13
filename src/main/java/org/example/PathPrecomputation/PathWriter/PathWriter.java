package org.example.PathPrecomputation;

public interface PathWriter {
    String getFileName();
    void onPathFound(String path);
    void onSearchComplete();
}
