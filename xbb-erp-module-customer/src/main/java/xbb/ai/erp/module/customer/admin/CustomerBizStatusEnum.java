package xbb.ai.erp.module.customer.admin;

import lombok.Getter;
import xbb.ai.erp.base.common.filed.FieldItem;

import java.util.List;

@Getter
public enum CustomerBizStatusEnum {
    ENABLED("1", "启用"),
    DISABLED("0", "停用");

    private final String value;
    private final String text;

    CustomerBizStatusEnum(String value, String text) {
        this.value = value;
        this.text = text;
    }

    public static List<FieldItem> toFieldItems() {
        return java.util.Arrays.stream(values())
            .map(item -> {
                FieldItem fieldItem = new FieldItem();
                fieldItem.setValue(item.getValue());
                fieldItem.setText(item.getText());
                return fieldItem;
            })
            .toList();
    }
}
