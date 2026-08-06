package xbb.ai.erp.codegen.generator;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DddGenerationReport {

    private final List<Path> createdDirectories = new ArrayList<>();
    private final List<Path> generatedFiles = new ArrayList<>();
    private final List<Path> skippedFiles = new ArrayList<>();
    private final List<Path> failedFiles = new ArrayList<>();

    public void addCreatedDirectory(Path path) {
        createdDirectories.add(path);
    }

    public void addGeneratedFile(Path path) {
        generatedFiles.add(path);
    }

    public void addSkippedFile(Path path) {
        skippedFiles.add(path);
    }

    public void addFailedFile(Path path) {
        failedFiles.add(path);
    }

    public List<Path> createdDirectories() {
        return createdDirectories;
    }

    public List<Path> generatedFiles() {
        return generatedFiles;
    }

    public List<Path> skippedFiles() {
        return skippedFiles;
    }

    public List<Path> failedFiles() {
        return failedFiles;
    }
}
