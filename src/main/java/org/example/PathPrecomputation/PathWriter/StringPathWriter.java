package org.example.PathPrecomputation.PathWriter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

// Class to write paths as strings to a file
public class StringPathWriter implements PathWriter{
    protected final String fileName;
    private BufferedWriter writer = null;

    public StringPathWriter(String fileName) {
        this.fileName = fileName;
    }

    protected BufferedWriter getWriter() {
        if (writer == null) initializeWriter();
        return writer;
    }

    @Override
    public void onPathFound(String path) {
        try {
            getWriter().write(path);
            getWriter().newLine();
        } catch (IOException e) {
            throw new RuntimeException("Failed to write path to file: " + fileName, e);
        }
    }

    @Override
    public void onSearchComplete() {
        try {
            getWriter().flush();
            getWriter().close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to close writer for file: " + fileName, e);
        }
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    private void initializeWriter() {
        try {
            writer = Files.newBufferedWriter(Paths.get(fileName), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize writer for file: " + fileName, e);
        }
    }
}
