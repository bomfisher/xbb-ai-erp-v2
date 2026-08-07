package xbb.ai.erp.module.common.application.filter;

import xbb.ai.erp.base.common.filed.FieldTypeEnum;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public record ListFilterFieldTypeRule(String protocolFieldType, List<String> supportedSymbols) {
    private static final List<String> TEXT_SYMBOLS = List.of("EQ", "NE", "CONTAINS", "NOT_CONTAINS", "IS_EMPTY", "IS_NOT_EMPTY");
    private static final List<String> ID_SYMBOLS = List.of("EQ", "NE", "IN", "IS_EMPTY", "IS_NOT_EMPTY");
    private static final List<String> NUMBER_SYMBOLS = List.of("EQ", "GE", "LE", "BETWEEN");
    private static final List<String> DATE_SYMBOLS = List.of("EQ", "GE", "LE", "BETWEEN");
    private static final Map<Integer, ListFilterFieldTypeRule> RULES = Map.ofEntries(
        Map.entry(FieldTypeEnum.TEXT.getType(), new ListFilterFieldTypeRule("TEXT", TEXT_SYMBOLS)),
        Map.entry(FieldTypeEnum.USER.getType(), new ListFilterFieldTypeRule("ID", ID_SYMBOLS)),
        Map.entry(FieldTypeEnum.DEPT.getType(), new ListFilterFieldTypeRule("ID", ID_SYMBOLS)),
        Map.entry(FieldTypeEnum.COMB.getType(), new ListFilterFieldTypeRule("ENUM", ID_SYMBOLS)),
        Map.entry(FieldTypeEnum.NUM_INT.getType(), new ListFilterFieldTypeRule("NUMBER", NUMBER_SYMBOLS)),
        Map.entry(FieldTypeEnum.NUM_DOUBLE.getType(), new ListFilterFieldTypeRule("NUMBER", NUMBER_SYMBOLS)),
        Map.entry(FieldTypeEnum.AMOUNT.getType(), new ListFilterFieldTypeRule("NUMBER", NUMBER_SYMBOLS)),
        Map.entry(FieldTypeEnum.DATE.getType(), new ListFilterFieldTypeRule("DATE", DATE_SYMBOLS)),
        Map.entry(FieldTypeEnum.TIME.getType(), new ListFilterFieldTypeRule("DATE", DATE_SYMBOLS))
    );

    public static Optional<ListFilterFieldTypeRule> find(Integer fieldType) {
        return Optional.ofNullable(RULES.get(fieldType));
    }
}
