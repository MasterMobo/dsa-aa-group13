package org.example.PathPrecomputation.PathWriter;

public interface PathWriter {
    String getFileName();
    void onPathFound(String path);
    void onSearchComplete();
}
