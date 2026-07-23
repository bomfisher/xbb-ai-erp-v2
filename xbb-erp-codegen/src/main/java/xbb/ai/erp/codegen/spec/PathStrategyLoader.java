package xbb.ai.erp.codegen.spec;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.io.InputStream;

public class PathStrategyLoader {

    private final ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

    public PathStrategySpec loadPreset(String presetName) throws IOException {
        String resourcePath = "/presets/" + presetName + ".yaml";
        try (InputStream inputStream = PathStrategyLoader.class.getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("找不到路径策略预设: " + presetName);
            }
            CodegenPreset preset = objectMapper.readValue(inputStream, CodegenPreset.class);
            return preset.getPathStrategy();
        }
    }
}
