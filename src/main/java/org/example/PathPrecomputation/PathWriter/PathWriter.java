package org.example.PathPrecomputation.PathWriter;

// Interface to write paths to a file
public interface PathWriter {

    // Retrieves name of file that is written to
    String getFileName();

    // Called when a path is found
    // Implementations should store the path in the file
    void onPathFound(String path);

    // Called when a path searching is complete
    // Implementations should use this for clean-up purposes
    void onSearchComplete();
}
