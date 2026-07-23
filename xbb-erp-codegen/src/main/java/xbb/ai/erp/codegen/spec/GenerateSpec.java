package xbb.ai.erp.codegen.spec;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GenerateSpec {
    private boolean admin = true;
    private boolean application = true;
    private boolean domain = true;
    private boolean persistence = true;
    private boolean xml = true;
    private List<String> children = new ArrayList<>();
}
