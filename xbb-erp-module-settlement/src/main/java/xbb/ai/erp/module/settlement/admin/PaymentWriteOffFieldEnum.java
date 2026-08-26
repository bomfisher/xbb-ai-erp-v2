package xbb.ai.erp.module.settlement.admin;

import java.util.List;
import lombok.Getter;
import xbb.ai.erp.base.common.filed.FieldTypeEnum;
import xbb.ai.erp.scene.meta.SceneFieldMeta;

@Getter
public enum PaymentWriteOffFieldEnum {
    WRITEOFF_NO("writeoffNo", "核销批次号", FieldTypeEnum.SERIAL_NO),
    PAYMENT_ID("paymentId", "预付款", FieldTypeEnum.NUM_INT),
    PAYABLE_ID("payableId", "应付款", FieldTypeEnum.NUM_INT),
    WRITEOFF_DATE("writeoffDate", "核销日期", FieldTypeEnum.DATE),
    AMOUNT("amount", "核销金额", FieldTypeEnum.AMOUNT),
    STATUS("status", "核销状态", FieldTypeEnum.NUM_INT),
    REMARK("remark", "备注", FieldTypeEnum.TEXT);

    private final String attr;
    private final String attrName;
    private final FieldTypeEnum fieldType;

    PaymentWriteOffFieldEnum(String attr, String attrName, FieldTypeEnum fieldType) {
        this.attr = attr;
        this.attrName = attrName;
        this.fieldType = fieldType;
    }

    public SceneFieldMeta toSceneFieldMeta() {
        return new SceneFieldMeta(attr, attrName, fieldType.getType(), 0, 0, List.of(), null, List.of());
    }
}
