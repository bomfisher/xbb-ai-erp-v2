package xbb.ai.erp.base.common.filed;

import xbb.ai.erp.base.common.exception.BizException;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Pattern;

public class FieldValueValidator {

    private static final Pattern NUM_INT_PATTERN = Pattern.compile("^-?\\d+$");
    private static final Pattern NUM_DOUBLE_PATTERN = Pattern.compile("^-?\\d+(\\.\\d+)?$");
    private static final Pattern RADIO_BTN_PATTERN = Pattern.compile("^[01]$");

    public void validate(List<FieldRule> fieldRules, Object target) {
        validate(fieldRules, target, FieldValidateModeEnum.DRAFT);
    }

    public void validate(List<FieldRule> fieldRules, Object target, FieldValidateModeEnum mode) {
        if (fieldRules == null || fieldRules.isEmpty() || target == null) {
            return;
        }
        for (FieldRule fieldRule : fieldRules) {
            validatePath(fieldRule, target, splitAttr(fieldRule.getAttr()), 0, mode);
        }
    }

    public <T> void validate(List<FieldRule> fieldRules, T target, Function<T, Object> accessor) {
        validate(fieldRules, target, accessor, FieldValidateModeEnum.DRAFT);
    }

    public <T> void validate(List<FieldRule> fieldRules, T target, Function<T, Object> accessor, FieldValidateModeEnum mode) {
        if (fieldRules == null || fieldRules.isEmpty() || target == null) {
            return;
        }
        Object value = accessor.apply(target);
        validate(fieldRules, value, mode);
    }

    private void validatePath(FieldRule fieldRule, Object current, String[] segments, int index, FieldValidateModeEnum mode) {
        if (segments.length == 0) {
            validateValue(fieldRule, current, mode);
            return;
        }
        if (current == null) {
            validateValue(fieldRule, null, mode);
            return;
        }
        if (index >= segments.length) {
            validateValue(fieldRule, current, mode);
            return;
        }
        Object next = readSegmentValue(current, segments[index]);
        if (next instanceof List<?> list) {
            if (list.isEmpty()) {
                return;
            }
            for (Object item : list) {
                if (isBlankContainer(item)) {
                    continue;
                }
                validatePath(fieldRule, item, segments, index + 1, mode);
            }
            return;
        }
        validatePath(fieldRule, next, segments, index + 1, mode);
    }

    private void validateValue(FieldRule fieldRule, Object value, FieldValidateModeEnum mode) {
        if (mode == FieldValidateModeEnum.SUBMIT && isRequired(fieldRule) && isBlankValue(value)) {
            throw new BizException(fieldRule.getAttrName() + "不能为空");
        }
        if (value == null) {
            return;
        }
        if (value instanceof String text) {
            validateTextLength(fieldRule, text);
            validateTextType(fieldRule, text);
            return;
        }
        Integer fieldType = fieldRule.getFieldType();
        if (Objects.equals(FieldTypeEnum.RADIO_BTN.getType(), fieldType)) {
            if (!(value instanceof Number number) || !(number.intValue() == 0 || number.intValue() == 1)) {
                throw new BizException(fieldRule.getAttrName() + "格式不合法");
            }
            return;
        }
        if (Objects.equals(FieldTypeEnum.CHECKBOX.getType(), fieldType)) {
            if (!(value instanceof Number number) || !(number.intValue() == 0 || number.intValue() == 1)) {
                throw new BizException(fieldRule.getAttrName() + "格式不合法");
            }
        }
    }

    private void validateTextLength(FieldRule fieldRule, String text) {
        Integer maxLength = resolveMaxLength(fieldRule);
        if (maxLength != null && text.length() > maxLength) {
            throw new BizException(fieldRule.getAttrName() + "长度不能超过" + maxLength);
        }
    }

    private void validateTextType(FieldRule fieldRule, String text) {
        if (text.isBlank()) {
            return;
        }
        Integer fieldType = fieldRule.getFieldType();
        if (Objects.equals(FieldTypeEnum.TEXT.getType(), fieldType)
            || Objects.equals(FieldTypeEnum.COMB.getType(), fieldType)
            || Objects.equals(FieldTypeEnum.COMB_MULTI.getType(), fieldType)
            || Objects.equals(FieldTypeEnum.USER.getType(), fieldType)
            || Objects.equals(FieldTypeEnum.USER_MULTI.getType(), fieldType)
            || Objects.equals(FieldTypeEnum.DEPT.getType(), fieldType)
            || Objects.equals(FieldTypeEnum.DEPT_MULTI.getType(), fieldType)
            || Objects.equals(FieldTypeEnum.BUSINESS.getType(), fieldType)
            || Objects.equals(FieldTypeEnum.BUSINESS_MULTI.getType(), fieldType)
            || Objects.equals(FieldTypeEnum.FILE.getType(), fieldType)
            || Objects.equals(FieldTypeEnum.IMAGE.getType(), fieldType)
            || Objects.equals(FieldTypeEnum.DATE.getType(), fieldType)
            || Objects.equals(FieldTypeEnum.TIME.getType(), fieldType)) {
            return;
        }
        if (Objects.equals(FieldTypeEnum.NUM_INT.getType(), fieldType)
            || Objects.equals(FieldTypeEnum.STOCK.getType(), fieldType)) {
            if (!NUM_INT_PATTERN.matcher(text).matches()) {
                throw new BizException(fieldRule.getAttrName() + "格式不合法");
            }
            return;
        }
        if (Objects.equals(FieldTypeEnum.NUM_DOUBLE.getType(), fieldType)
            || Objects.equals(FieldTypeEnum.AMOUNT.getType(), fieldType)) {
            if (!NUM_DOUBLE_PATTERN.matcher(text).matches()) {
                throw new BizException(fieldRule.getAttrName() + "格式不合法");
            }
            return;
        }
        if (Objects.equals(FieldTypeEnum.RADIO_BTN.getType(), fieldType)
            || Objects.equals(FieldTypeEnum.CHECKBOX.getType(), fieldType)) {
            if (!RADIO_BTN_PATTERN.matcher(text).matches()) {
                throw new BizException(fieldRule.getAttrName() + "格式不合法");
            }
        }
    }

    private Integer resolveMaxLength(FieldRule fieldRule) {
        if (fieldRule.getMaxLength() != null) {
            return fieldRule.getMaxLength();
        }
        Integer fieldType = fieldRule.getFieldType();
        if (Objects.equals(FieldTypeEnum.TEXT.getType(), fieldType)) {
            return 200;
        }
        if (Objects.equals(FieldTypeEnum.COMB.getType(), fieldType)
            || Objects.equals(FieldTypeEnum.USER.getType(), fieldType)
            || Objects.equals(FieldTypeEnum.DEPT.getType(), fieldType)
            || Objects.equals(FieldTypeEnum.BUSINESS.getType(), fieldType)
            || Objects.equals(FieldTypeEnum.DATE.getType(), fieldType)
            || Objects.equals(FieldTypeEnum.TIME.getType(), fieldType)) {
            return 64;
        }
        if (Objects.equals(FieldTypeEnum.COMB_MULTI.getType(), fieldType)
            || Objects.equals(FieldTypeEnum.USER_MULTI.getType(), fieldType)
            || Objects.equals(FieldTypeEnum.DEPT_MULTI.getType(), fieldType)
            || Objects.equals(FieldTypeEnum.BUSINESS_MULTI.getType(), fieldType)
            || Objects.equals(FieldTypeEnum.FILE.getType(), fieldType)
            || Objects.equals(FieldTypeEnum.IMAGE.getType(), fieldType)) {
            return 500;
        }
        if (Objects.equals(FieldTypeEnum.NUM_INT.getType(), fieldType)
            || Objects.equals(FieldTypeEnum.STOCK.getType(), fieldType)
            || Objects.equals(FieldTypeEnum.RADIO_BTN.getType(), fieldType)
            || Objects.equals(FieldTypeEnum.CHECKBOX.getType(), fieldType)) {
            return 32;
        }
        if (Objects.equals(FieldTypeEnum.NUM_DOUBLE.getType(), fieldType)
            || Objects.equals(FieldTypeEnum.AMOUNT.getType(), fieldType)) {
            return 64;
        }
        return null;
    }

    private boolean isBlankContainer(Object value) {
        if (value == null) {
            return true;
        }
        if (value instanceof String text) {
            return text.isBlank();
        }
        if (value instanceof Map<?, ?> map) {
            if (map.isEmpty()) {
                return true;
            }
            for (Object item : map.values()) {
                if (!isBlankContainer(item)) {
                    return false;
                }
            }
            return true;
        }
        if (value instanceof List<?> list) {
            if (list.isEmpty()) {
                return true;
            }
            for (Object item : list) {
                if (!isBlankContainer(item)) {
                    return false;
                }
            }
            return true;
        }
        if (value instanceof Number || value instanceof Boolean) {
            return false;
        }
        return isBlankBean(value);
    }

    private boolean isBlankBean(Object value) {
        for (java.lang.reflect.Method method : value.getClass().getMethods()) {
            if (method.getParameterCount() != 0) {
                continue;
            }
            if (method.getDeclaringClass() == Object.class) {
                continue;
            }
            String methodName = method.getName();
            if ("getClass".equals(methodName)) {
                continue;
            }
            if (!methodName.startsWith("get") && !methodName.startsWith("is")) {
                continue;
            }
            try {
                Object propertyValue = method.invoke(value);
                if (!isBlankContainer(propertyValue)) {
                    return false;
                }
            } catch (ReflectiveOperationException ignored) {
            }
        }
        return true;
    }

    private boolean isBlankPathValue(Object current, String[] segments, int index) {
        if (current == null) {
            return true;
        }
        if (index >= segments.length) {
            return isBlankValue(current);
        }
        Object next = readSegmentValue(current, segments[index]);
        if (next instanceof List<?> list) {
            if (list.isEmpty()) {
                return true;
            }
            for (Object item : list) {
                if (!isBlankPathValue(item, segments, index + 1)) {
                    return false;
                }
            }
            return true;
        }
        return isBlankPathValue(next, segments, index + 1);
    }

    private boolean isRequired(FieldRule fieldRule) {
        return Integer.valueOf(1).equals(fieldRule.getRequired());
    }

    private boolean isBlankValue(Object value) {
        if (value == null) {
            return true;
        }
        if (value instanceof String text) {
            return text.isBlank();
        }
        return false;
    }

    private String[] splitAttr(String attr) {
        if (attr == null || attr.isBlank()) {
            return new String[0];
        }
        return attr.split("\\.");
    }

    private Object readSegmentValue(Object current, String segment) {
        if (current == null || segment == null || segment.isBlank()) {
            return null;
        }
        if (current instanceof Map<?, ?> map) {
            return map.get(segment);
        }
        return readProperty(current, segment);
    }

    private Object readProperty(Object target, String propertyName) {
        Class<?> currentType = target.getClass();
        String suffix = Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
        String[] candidates = new String[]{"get" + suffix, "is" + suffix};
        for (String candidate : candidates) {
            try {
                return currentType.getMethod(candidate).invoke(target);
            } catch (ReflectiveOperationException ignored) {
            }
        }
        return null;
    }
}
