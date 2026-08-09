package xbb.ai.erp.module.common.application.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListFilterMetaPojo {
    private String attr;
    private String column;
    private String fieldType;
    private List<String> supportedSymbols;
}
