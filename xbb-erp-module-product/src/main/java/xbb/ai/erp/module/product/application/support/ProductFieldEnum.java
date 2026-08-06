package xbb.ai.erp.module.product.application.support;

import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.filed.FieldItem;
import xbb.ai.erp.base.common.filed.FieldRule;
import xbb.ai.erp.base.common.filed.FieldTypeEnum;

import java.util.Arrays;
import java.util.List;

public enum ProductFieldEnum {
    SPU_CODE("main.spuCode", "商品编码", "input", FieldTypeEnum.TEXT.getType(), 1, 1),
    SPU_NAME("main.spuName", "商品名称", "input", FieldTypeEnum.TEXT.getType(), 1, 1),
    CATEGORY_ID("main.categoryId", "分类", "input-number", FieldTypeEnum.NUM_INT.getType(), 0, 1),
    BRAND_ID("main.brandId", "品牌", "input-number", FieldTypeEnum.NUM_INT.getType(), 0, 1),
    PRODUCT_TYPE("main.productType", "商品类型", "input", FieldTypeEnum.TEXT.getType(), 1, 1),
    ENABLE_SPEC("main.enableSpec", "启用规格", String.valueOf(FieldTypeEnum.SWITCH.getType()), FieldTypeEnum.SWITCH.getType(), 1, 1),
    DESCRIPTION("main.description", "商品描述", "textarea", FieldTypeEnum.TEXT.getType(), 0, 1),
    IMAGE_URL("main.imageUrl", "图片地址", "input", FieldTypeEnum.TEXT.getType(), 0, 1),
    SPU_ENABLE_STATUS("main.spuEnableStatus", "主档启用状态", "select", FieldTypeEnum.COMB.getType(), 1, 1),
    SKU_CODE("skus.skuCode", "SKU编码", "input", FieldTypeEnum.TEXT.getType(), 1, 1),
    SKU_NAME("skus.skuName", "SKU名称", "input", FieldTypeEnum.TEXT.getType(), 1, 1),
    SPEC_SIGNATURE("skus.specSignature", "规格签名", "input", FieldTypeEnum.TEXT.getType(), 0, 1),
    SPEC_SNAPSHOT("skus.specSnapshot", "规格快照", "textarea", FieldTypeEnum.TEXT.getType(), 0, 1),
    MNEMONIC_CODE("skus.mnemonicCode", "助记码", "input", FieldTypeEnum.TEXT.getType(), 0, 1),
    MAIN_BARCODE("skus.mainBarcode", "主条码", "input", FieldTypeEnum.TEXT.getType(), 0, 1),
    CAN_PURCHASE("skus.canPurchase", "可采购", "select", FieldTypeEnum.COMB.getType(), 0, 1),
    CAN_SALE("skus.canSale", "可销售", "select", FieldTypeEnum.COMB.getType(), 0, 1),
    CAN_INVENTORY("skus.canInventory", "可库存", "select", FieldTypeEnum.COMB.getType(), 0, 1),
    CAN_PRODUCE("skus.canProduce", "可生产", "select", FieldTypeEnum.COMB.getType(), 0, 1),
    SKU_ENABLE_STATUS("skus.skuEnableStatus", "SKU启用状态", "select", FieldTypeEnum.COMB.getType(), 0, 1),
    LISTING_STATUS("skus.listingStatus", "上下架状态", "select", FieldTypeEnum.COMB.getType(), 0, 1);

    private final String attr;
    private final String attrName;
    private final String fieldType;
    private final Integer validateFieldType;
    private final Integer required;
    private final Integer editable;

    ProductFieldEnum(String attr, String attrName, String fieldType, Integer validateFieldType, Integer required, Integer editable) {
        this.attr = attr;
        this.attrName = attrName;
        this.fieldType = fieldType;
        this.validateFieldType = validateFieldType;
        this.required = required;
        this.editable = editable;
    }

    public FieldEntity toFieldEntity() {
        FieldEntity fieldEntity = new FieldEntity();
        fieldEntity.setAttr(attr);
        fieldEntity.setAttrName(attrName);
        fieldEntity.setFieldType(fieldType);
        fieldEntity.setRequired(required);
        fieldEntity.setEditable(editable);
        fieldEntity.setItemList(resolveItemList());
        return fieldEntity;
    }

    public FieldRule toFieldRule() {
        return new FieldRule(attr, attrName, validateFieldType, null, required);
    }

    public String getAttr() {
        return attr;
    }

    public static List<FieldEntity> formHead() {
        return Arrays.stream(values())
            .map(ProductFieldEnum::toFieldEntity)
            .toList();
    }

    public static List<FieldRule> allFieldRules() {
        return Arrays.stream(values())
            .map(ProductFieldEnum::toFieldRule)
            .toList();
    }

    public static List<String> formRequiredFields() {
        return Arrays.stream(values())
            .filter(field -> Integer.valueOf(1).equals(field.required))
            .map(ProductFieldEnum::getAttr)
            .toList();
    }

    private List<FieldItem> resolveItemList() {
        return switch (this) {
            case ENABLE_SPEC -> binaryItems("1", "启用", "0", "不启用");
            case SPU_ENABLE_STATUS, SKU_ENABLE_STATUS -> binaryItems("1", "启用", "0", "停用");
            case CAN_PURCHASE, CAN_SALE, CAN_INVENTORY, CAN_PRODUCE -> binaryItems("1", "是", "0", "否");
            case LISTING_STATUS -> binaryItems("1", "上架", "0", "下架");
            default -> List.of();
        };
    }

    private static List<FieldItem> binaryItems(String trueValue, String trueText, String falseValue, String falseText) {
        return List.of(item(trueValue, trueText), item(falseValue, falseText));
    }

    private static FieldItem item(String value, String text) {
        FieldItem fieldItem = new FieldItem();
        fieldItem.setValue(value);
        fieldItem.setText(text);
        return fieldItem;
    }
}
