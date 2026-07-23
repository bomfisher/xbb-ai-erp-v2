package xbb.ai.erp.codegen.spec;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class ModuleSpecLoader {

    private final ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

    public ModuleSpec load(Path path) throws IOException {
        try (InputStream inputStream = Files.newInputStream(path)) {
            return objectMapper.readValue(inputStream, ModuleSpec.class);
        }
    }
}
