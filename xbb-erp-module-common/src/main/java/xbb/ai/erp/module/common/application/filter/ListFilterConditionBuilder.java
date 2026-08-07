package xbb.ai.erp.module.common.application.filter;

import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.pojo.ListFilterCondition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListFilterConditionBuilder {

    public List<ListFilterCondition> build(List<ListFilterCondition> rawConditions, Map<String, ListFilterMetaPojo> metaMap) {
        if (rawConditions == null || rawConditions.isEmpty()) {
            return List.of();
        }
        List<ListFilterCondition> result = new ArrayList<>();
        for (ListFilterCondition rawCondition : rawConditions) {
            ListFilterMetaPojo meta = metaMap.get(rawCondition.getAttr());
            if (meta == null) {
                throw new BizException("筛选字段不合法");
            }
            if (!meta.getFieldType().equals(rawCondition.getFieldType())) {
                throw new BizException("筛选字段类型不匹配");
            }
            if (!meta.getSupportedSymbols().contains(rawCondition.getSymbol())) {
                throw new BizException("筛选操作符不支持");
            }
            validateValue(rawCondition);

            ListFilterCondition safeCondition = new ListFilterCondition();
            safeCondition.setAttr(meta.getColumn());
            safeCondition.setFieldType(rawCondition.getFieldType());
            safeCondition.setSymbol(rawCondition.getSymbol());
            safeCondition.setValue(rawCondition.getValue());
            result.add(safeCondition);
        }
        return result;
    }

    private void validateValue(ListFilterCondition condition) {
        List<String> values = condition.getValue();
        ListFilterSymbolEnum symbol = ListFilterSymbolEnum.valueOf(condition.getSymbol());
        switch (symbol) {
            case IS_EMPTY, IS_NOT_EMPTY -> {
                if (values != null && !values.isEmpty()) {
                    throw new BizException("筛选值长度不合法");
                }
            }
            case BETWEEN -> {
                if (values == null || values.size() != 2 || isBlank(values.get(0)) || isBlank(values.get(1))) {
                    throw new BizException("筛选值长度不合法");
                }
            }
            case IN -> {
                if (values == null || values.isEmpty()) {
                    throw new BizException("筛选值长度不合法");
                }
                for (String value : values) {
                    if (isBlank(value)) {
                        throw new BizException("筛选值不能为空");
                    }
                }
            }
            default -> {
                if (values == null || values.size() != 1 || isBlank(values.get(0))) {
                    throw new BizException("筛选值长度不合法");
                }
            }
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
