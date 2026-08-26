package xbb.ai.erp.module.approval.application.condition;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import xbb.ai.erp.module.approval.contract.ApprovalConditionBranch;
import xbb.ai.erp.module.approval.contract.ApprovalConditionMatcher;
import xbb.ai.erp.module.approval.contract.ApprovalConditionOperator;
import xbb.ai.erp.module.approval.contract.ApprovalConditionRule;
import xbb.ai.erp.module.approval.contract.ApprovalFieldDefinition;
import xbb.ai.erp.module.approval.contract.ApprovalFieldType;
import xbb.ai.erp.module.approval.contract.ApprovalSubjectSchema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

/**
 * 基于冻结 JSON 快照执行白名单字段条件匹配。
 */
@Component
@RequiredArgsConstructor
public class JsonApprovalConditionMatcher implements ApprovalConditionMatcher {

    private final ObjectMapper objectMapper;

    @Override
    public boolean matches(ApprovalSubjectSchema schema, ApprovalConditionBranch branch, String snapshotJson) {
        try {
            JsonNode snapshot = objectMapper.readTree(snapshotJson);
            return branch.rules().stream().allMatch(rule -> matchesRule(schema, rule, snapshot));
        } catch (Exception exception) {
            throw new IllegalArgumentException("审批快照不是合法 JSON", exception);
        }
    }

    private boolean matchesRule(ApprovalSubjectSchema schema, ApprovalConditionRule rule, JsonNode snapshot) {
        ApprovalFieldDefinition field = schema.fields().stream()
            .filter(item -> item.attr().equals(rule.fieldAttr()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("未定义审批字段: " + rule.fieldAttr()));
        if (!field.operators().contains(rule.operator())) {
            throw new IllegalArgumentException("审批字段不支持该操作符: " + rule.fieldAttr() + "/" + rule.operator());
        }
        JsonNode actual = snapshot.at(field.path());
        if (actual.isMissingNode() || actual.isNull()) {
            return false;
        }
        return evaluate(field.fieldType(), actual, rule.operator(), rule.value());
    }

    private boolean evaluate(ApprovalFieldType fieldType, JsonNode actual, ApprovalConditionOperator operator, String expected) {
        return switch (fieldType) {
            case DECIMAL -> compareDecimal(actual, operator, expected);
            case INTEGER -> compareDecimal(actual, operator, expected);
            case DATE -> compareDate(actual, operator, expected);
            case BOOLEAN -> compareText(actual, operator, expected);
            case STRING, ENUM -> compareText(actual, operator, expected);
        };
    }

    private boolean compareDecimal(JsonNode actual, ApprovalConditionOperator operator, String expected) {
        BigDecimal actualValue = actual.decimalValue();
        return switch (operator) {
            case EQ -> actualValue.compareTo(new BigDecimal(expected)) == 0;
            case GT -> actualValue.compareTo(new BigDecimal(expected)) > 0;
            case GE -> actualValue.compareTo(new BigDecimal(expected)) >= 0;
            case LT -> actualValue.compareTo(new BigDecimal(expected)) < 0;
            case LE -> actualValue.compareTo(new BigDecimal(expected)) <= 0;
            case BETWEEN -> between(actualValue, expected, BigDecimal::new);
            case IN -> Arrays.stream(parseArray(expected)).map(BigDecimal::new).anyMatch(value -> actualValue.compareTo(value) == 0);
            case CONTAINS -> false;
        };
    }

    private boolean compareDate(JsonNode actual, ApprovalConditionOperator operator, String expected) {
        LocalDate actualValue = LocalDate.parse(actual.asText());
        return switch (operator) {
            case EQ -> actualValue.equals(LocalDate.parse(expected));
            case GT -> actualValue.isAfter(LocalDate.parse(expected));
            case GE -> !actualValue.isBefore(LocalDate.parse(expected));
            case LT -> actualValue.isBefore(LocalDate.parse(expected));
            case LE -> !actualValue.isAfter(LocalDate.parse(expected));
            case BETWEEN -> between(actualValue, expected, LocalDate::parse);
            case IN -> Arrays.stream(parseArray(expected)).map(LocalDate::parse).anyMatch(actualValue::equals);
            case CONTAINS -> false;
        };
    }

    private boolean compareText(JsonNode actual, ApprovalConditionOperator operator, String expected) {
        String actualValue = actual.asText();
        return switch (operator) {
            case EQ -> Objects.equals(actualValue, expected);
            case CONTAINS -> actualValue.contains(expected);
            case IN -> Arrays.asList(parseArray(expected)).contains(actualValue);
            case GT, GE, LT, LE, BETWEEN -> false;
        };
    }

    private <T extends Comparable<T>> boolean between(T actual, String expected, java.util.function.Function<String, T> parser) {
        String[] values = parseArray(expected);
        if (values.length != 2) {
            throw new IllegalArgumentException("BETWEEN 条件必须包含两个值");
        }
        return actual.compareTo(parser.apply(values[0])) >= 0 && actual.compareTo(parser.apply(values[1])) <= 0;
    }

    private String[] parseArray(String value) {
        try {
            JsonNode array = objectMapper.readTree(value);
            if (!array.isArray()) {
                throw new IllegalArgumentException("条件值必须是 JSON 数组");
            }
            String[] values = new String[array.size()];
            for (int index = 0; index < array.size(); index++) {
                values[index] = array.get(index).asText();
            }
            return values;
        } catch (Exception exception) {
            throw new IllegalArgumentException("条件值不是合法 JSON 数组", exception);
        }
    }
}
