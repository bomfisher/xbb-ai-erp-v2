package xbb.ai.erp.module.common.application.render;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.filed.FieldItem;
import xbb.ai.erp.base.common.filed.FieldTypeEnum;
import xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO;
import xbb.ai.erp.module.common.application.provider.ListMetaProvider;
import xbb.ai.erp.module.common.application.provider.ListMetaRegistry;

@Service
public class ListValueRenderer {
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private static final ZoneId DEFAULT_ZONE_ID = ZoneId.of("Asia/Shanghai");
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private static final DateTimeFormatter TIME_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  private final ListMetaRegistry listMetaRegistry;
  private final ListReferenceValueProviderRegistry providerRegistry;
  private final Map<Class<?>, Map<String, PropertyDescriptor>> propertyDescriptors =
      new ConcurrentHashMap<>();

  public ListValueRenderer(
      ListMetaRegistry listMetaRegistry, ListReferenceValueProviderRegistry providerRegistry) {
    this.listMetaRegistry = listMetaRegistry;
    this.providerRegistry = providerRegistry;
  }

  public <T> List<T> render(String corpid, String businessCode, List<T> rows) {
    if (rows == null || rows.isEmpty()) {
      return rows;
    }
    ListMetaProvider metaProvider = listMetaRegistry.getRequiredProvider(businessCode);
    ListCommonQueryDTO metaDto = new ListCommonQueryDTO();
    metaDto.setCorpid(corpid);
    metaDto.setBusinessCode(businessCode);
    List<RenderField> fields = compileFields(metaProvider.buildHeaderMeta(metaDto), rows.getFirst().getClass());
    renderStaticValues(rows, fields);
    renderReferenceValues(corpid, rows, fields);
    return rows;
  }

  private <T> void renderStaticValues(List<T> rows, List<RenderField> fields) {
    for (RenderField field : fields) {
      if (isReferenceField(field) || (!isDateField(field) && field.itemList().isEmpty())) {
        continue;
      }
      for (T row : rows) {
        Object value = field.source().read(row);
        String displayValue = isDateField(field) ? formatTime(value, field.fieldType()) : formatItem(value, field.itemList());
        if (displayValue != null) {
          field.target().write(row, displayValue);
        }
      }
    }
  }

  private <T> void renderReferenceValues(String corpid, List<T> rows, List<RenderField> fields) {
    Map<ListReferenceKey, Set<String>> valuesByKey = new LinkedHashMap<>();
    for (RenderField field : fields) {
      ListReferenceKey key = referenceKey(field);
      if (key == null || providerRegistry.find(key) == null) {
        continue;
      }
      Set<String> values = valuesByKey.computeIfAbsent(key, ignored -> new LinkedHashSet<>());
      for (T row : rows) {
        values.addAll(toValues(field.source().read(row)));
      }
    }
    Map<ListReferenceKey, Map<String, String>> displayMapByKey = new HashMap<>();
    valuesByKey.forEach(
        (key, values) -> {
          if (!values.isEmpty()) {
            displayMapByKey.put(key, providerRegistry.find(key).findDisplayMap(corpid, values));
          }
        });
    for (RenderField field : fields) {
      ListReferenceKey key = referenceKey(field);
      Map<String, String> displayMap = key == null ? null : displayMapByKey.get(key);
      if (displayMap == null) {
        continue;
      }
      for (T row : rows) {
        Object value = field.source().read(row);
        String displayValue = formatReference(value, displayMap);
        if (displayValue != null) {
          field.target().write(row, displayValue);
        }
      }
    }
  }

  private List<RenderField> compileFields(List<FieldEntity> fields, Class<?> rowType) {
    if (fields == null || fields.isEmpty()) {
      return List.of();
    }
    List<RenderField> renderFields = new ArrayList<>();
    for (FieldEntity field : fields) {
      String targetAttr = propertyName(field.getAttr());
      String sourceAttr =
          field instanceof ListRenderFieldEntity listField && listField.getRenderValueAttr() != null
              ? propertyName(listField.getRenderValueAttr())
              : targetAttr;
      PropertyAccessor source = propertyAccessor(rowType, sourceAttr);
      PropertyAccessor target = propertyAccessor(rowType, targetAttr);
      if (source != null && target != null) {
        renderFields.add(
            new RenderField(
                field.getFieldType(),
                field.getItemList() == null ? List.of() : field.getItemList(),
                field.getBusinessSelectConfig() == null
                    ? null
                    : field.getBusinessSelectConfig().getBusinessCode(),
                source,
                target));
      }
    }
    return renderFields;
  }

  private boolean isReferenceField(RenderField field) {
    return referenceKey(field) != null;
  }

  private ListReferenceKey referenceKey(RenderField field) {
    if (field.businessCode() == null || field.businessCode().isBlank()) {
      return null;
    }
    return new ListReferenceKey(normalizeReferenceFieldType(field.fieldType()), field.businessCode());
  }

  private String normalizeReferenceFieldType(String fieldType) {
    if (Objects.equals(fieldType, String.valueOf(FieldTypeEnum.USER_MULTI.getType()))) {
      return String.valueOf(FieldTypeEnum.USER.getType());
    }
    if (Objects.equals(fieldType, String.valueOf(FieldTypeEnum.DEPT_MULTI.getType()))) {
      return String.valueOf(FieldTypeEnum.DEPT.getType());
    }
    return fieldType;
  }

  private boolean isDateField(RenderField field) {
    return Objects.equals(field.fieldType(), String.valueOf(FieldTypeEnum.DATE.getType()))
        || Objects.equals(field.fieldType(), String.valueOf(FieldTypeEnum.TIME.getType()));
  }

  private String formatTime(Object value, String fieldType) {
    if (value == null) {
      return null;
    }
    try {
      long timestamp = Long.parseLong(String.valueOf(value));
      return (Objects.equals(fieldType, String.valueOf(FieldTypeEnum.DATE.getType()))
              ? DATE_FORMATTER
              : TIME_FORMATTER)
          .format(Instant.ofEpochMilli(timestamp).atZone(DEFAULT_ZONE_ID));
    } catch (NumberFormatException exception) {
      return String.valueOf(value);
    }
  }

  private String formatItem(Object value, List<FieldItem> itemList) {
    if (value == null) {
      return null;
    }
    Map<String, String> itemMap = new HashMap<>();
    itemList.forEach(item -> itemMap.put(String.valueOf(item.getValue()), item.getText()));
    return toValues(value).stream().map(item -> itemMap.getOrDefault(item, item)).reduce((left, right) -> left + "," + right).orElse(null);
  }

  private String formatReference(Object value, Map<String, String> displayMap) {
    if (value == null) {
      return null;
    }
    return toValues(value).stream()
        .map(item -> displayMap.getOrDefault(item, item))
        .reduce((left, right) -> left + "," + right)
        .orElse(null);
  }

  private Set<String> toValues(Object value) {
    if (value == null) {
      return Set.of();
    }
    if (value instanceof Collection<?> collection) {
      return collectValues(collection);
    }
    String text = String.valueOf(value);
    if (text.startsWith("[") && text.endsWith("]")) {
      try {
        return collectValues(OBJECT_MAPPER.readValue(text, new TypeReference<List<Object>>() {}));
      } catch (Exception exception) {
        throw new BizException("列表多选字段值不是合法JSON数组");
      }
    }
    return text.isBlank() ? Set.of() : Set.of(text);
  }

  private Set<String> collectValues(Collection<?> values) {
    Set<String> normalized = new LinkedHashSet<>();
    values.forEach(
        value -> {
          if (value != null && !String.valueOf(value).isBlank()) {
            normalized.add(String.valueOf(value));
          }
        });
    return normalized;
  }

  private PropertyAccessor propertyAccessor(Class<?> rowType, String propertyName) {
    if (propertyName == null || propertyName.isBlank()) {
      return null;
    }
    PropertyDescriptor descriptor =
        propertyDescriptors
            .computeIfAbsent(rowType, this::describeProperties)
            .get(propertyName);
    return descriptor == null || descriptor.getReadMethod() == null || descriptor.getWriteMethod() == null
        ? null
        : new PropertyAccessor(descriptor.getReadMethod(), descriptor.getWriteMethod());
  }

  private Map<String, PropertyDescriptor> describeProperties(Class<?> type) {
    try {
      Map<String, PropertyDescriptor> descriptors = new HashMap<>();
      for (PropertyDescriptor descriptor : Introspector.getBeanInfo(type).getPropertyDescriptors()) {
        descriptors.put(descriptor.getName(), descriptor);
      }
      return descriptors;
    } catch (IntrospectionException exception) {
      throw new BizException("无法读取列表行属性: " + type.getName());
    }
  }

  private String propertyName(String attr) {
    if (attr == null) {
      return null;
    }
    int separator = attr.lastIndexOf('.');
    return separator < 0 ? attr : attr.substring(separator + 1);
  }

  private record RenderField(
      String fieldType,
      List<FieldItem> itemList,
      String businessCode,
      PropertyAccessor source,
      PropertyAccessor target) {}

  private record PropertyAccessor(Method reader, Method writer) {
    Object read(Object row) {
      try {
        return reader.invoke(row);
      } catch (ReflectiveOperationException exception) {
        throw new BizException("读取列表行属性失败");
      }
    }

    void write(Object row, String value) {
      try {
        writer.invoke(row, value);
      } catch (ReflectiveOperationException | IllegalArgumentException exception) {
        throw new BizException("写入列表展示值失败");
      }
    }
  }
}
