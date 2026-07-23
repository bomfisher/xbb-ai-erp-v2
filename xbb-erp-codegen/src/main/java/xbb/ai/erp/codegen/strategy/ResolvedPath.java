package xbb.ai.erp.codegen.strategy;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResolvedPath {
    private String packageName;
    private String relativePath;
}
