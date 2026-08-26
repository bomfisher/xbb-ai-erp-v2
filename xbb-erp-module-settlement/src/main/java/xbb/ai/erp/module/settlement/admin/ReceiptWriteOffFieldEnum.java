package xbb.ai.erp.module.settlement.admin;

import java.util.List;
import lombok.Getter;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.filed.FieldTypeEnum;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.scene.meta.SceneFieldMeta;

@Getter
public enum ReceiptWriteOffFieldEnum {
    WRITEOFF_NO("main.writeoffNo", "核销批次号", FieldTypeEnum.SERIAL_NO, true, false, null),
    CUSTOMER_ID("main.customerId", "客户", FieldTypeEnum.BUSINESS, false, false, BusinessCodeEnum.CUSTOMER.getCode()),
    RECEIPT_ID("main.receiptId", "预收款", FieldTypeEnum.BUSINESS, true, true, BusinessCodeEnum.RECEIPT.getCode()),
    RECEIVABLE_ID("main.receivableId", "应收开放项", FieldTypeEnum.BUSINESS, true, true, BusinessCodeEnum.RECEIVABLE.getCode()),
    WRITEOFF_DATE("main.writeoffDate", "核销日期", FieldTypeEnum.DATE, true, true, null),
    AMOUNT("main.amount", "本次核销金额", FieldTypeEnum.AMOUNT, true, true, null),
    REMARK("main.remark", "备注", FieldTypeEnum.TEXT, false, true, null);

    private final String attr;
    private final String attrName;
    private final FieldTypeEnum fieldType;
    private final boolean required;
    private final boolean editable;
    private final String businessCode;

    ReceiptWriteOffFieldEnum(String attr, String attrName, FieldTypeEnum fieldType, boolean required, boolean editable,
            String businessCode) {
        this.attr = attr;
        this.attrName = attrName;
        this.fieldType = fieldType;
        this.required = required;
        this.editable = editable;
        this.businessCode = businessCode;
    }

    public SceneFieldMeta toSceneFieldMeta() {
        return new SceneFieldMeta(attr, attrName, fieldType.getType(), required ? 1 : 0, editable ? 1 : 0,
            List.of(), businessCode, List.of());
    }

    public FieldEntity.BusinessSelectConfig businessSelectConfig() {
        if (businessCode == null) {
            return null;
        }
        FieldEntity.BusinessSelectConfig config = new FieldEntity.BusinessSelectConfig();
        config.setBusinessCode(businessCode);
        return config;
    }
}
