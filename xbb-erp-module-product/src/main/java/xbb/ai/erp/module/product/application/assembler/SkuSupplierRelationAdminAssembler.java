package xbb.ai.erp.module.product.application.assembler;

import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.filed.FieldItem;
import xbb.ai.erp.base.common.filed.FieldTypeEnum;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.product.admin.dto.SkuSupplierRelationMainDTO;
import xbb.ai.erp.module.product.admin.dto.SkuSupplierRelationSaveDTO;
import xbb.ai.erp.module.product.admin.vo.SkuSupplierRelationHistoryItemVO;
import xbb.ai.erp.module.product.admin.vo.SkuSupplierRelationListItemVO;
import xbb.ai.erp.module.product.admin.vo.SkuSupplierRelationSkuOptionVO;
import xbb.ai.erp.module.product.admin.vo.SkuSupplierRelationSupplierOptionVO;
import xbb.ai.erp.module.product.domain.model.ProductSkuSupplierRelation;
import xbb.ai.erp.module.product.domain.model.ProductSkuSupplierRelationHistory;
import xbb.ai.erp.module.product.domain.model.ProductSkuSupplierRelationListItem;
import xbb.ai.erp.module.product.domain.model.ProductSkuSupplierRelationSkuOption;
import xbb.ai.erp.module.product.domain.model.ProductSkuSupplierRelationSupplierOption;

import java.util.List;
import java.util.Map;

public final class SkuSupplierRelationAdminAssembler {

    private SkuSupplierRelationAdminAssembler() {
    }

    public static ProductSkuSupplierRelation toRelation(SkuSupplierRelationSaveDTO dto) {
        ProductSkuSupplierRelation relation = new ProductSkuSupplierRelation();
        relation.setId(dto.getId());
        relation.setCorpid(dto.getCorpid());
        relation.setSkuId(dto.getSkuId());
        relation.setSupplierId(dto.getSupplierId());
        relation.setPurchasePrice(dto.getPurchasePrice());
        relation.setDeliveryCycleDay(dto.getDeliveryCycleDay());
        relation.setMinOrderQty(dto.getMinOrderQty());
        relation.setSupplierSkuCode(dto.getSupplierSkuCode());
        relation.setDefaultFlag(dto.getDefaultFlag());
        relation.setEnableStatus(dto.getEnableStatus());
        relation.setRemark(dto.getRemark());
        return relation;
    }

    public static SaveItemVO<SkuSupplierRelationMainDTO> buildSaveItemVO(SkuSupplierRelationMainDTO main, List<ProductSkuSupplierRelationSkuOption> skuOptions, String corpid) {
        SaveItemVO<SkuSupplierRelationMainDTO> vo = new SaveItemVO<>();
        vo.setHeadList(List.of(
            buildSkuField(skuOptions),
            buildSupplierField(corpid)
        ));
        vo.setData(main == null ? buildEmptyMain() : main);
        return vo;
    }

    public static FieldEntity.BusinessSelectConfig buildSupplierSelectConfig(String corpid) {
        FieldEntity.BusinessSelectConfig config = new FieldEntity.BusinessSelectConfig();
        config.setBusinessType("supplier");
        config.setRequestPayload(Map.of("corpid", corpid));
        config.setPlaceholder("请输入供应商名称或编码");
        config.setDialogTitle("选择供应商");
        return config;
    }

    public static SkuSupplierRelationMainDTO toMainDTO(ProductSkuSupplierRelation relation) {
        SkuSupplierRelationMainDTO main = buildEmptyMain();
        if (relation == null) {
            return main;
        }
        main.setId(relation.getId());
        main.setSkuId(relation.getSkuId());
        main.setSupplierId(relation.getSupplierId());
        main.setPurchasePrice(relation.getPurchasePrice());
        main.setDeliveryCycleDay(relation.getDeliveryCycleDay());
        main.setMinOrderQty(relation.getMinOrderQty());
        main.setSupplierSkuCode(relation.getSupplierSkuCode());
        main.setDefaultFlag(relation.getDefaultFlag());
        main.setEnableStatus(relation.getEnableStatus());
        main.setRemark(relation.getRemark());
        return main;
    }

    public static SkuSupplierRelationMainDTO buildEmptyMain() {
        SkuSupplierRelationMainDTO main = new SkuSupplierRelationMainDTO();
        main.setDefaultFlag(0);
        main.setEnableStatus(1);
        return main;
    }

    public static SkuSupplierRelationListItemVO toListItemVO(ProductSkuSupplierRelationListItem item) {
        SkuSupplierRelationListItemVO vo = new SkuSupplierRelationListItemVO();
        if (item == null) {
            return vo;
        }
        vo.setId(item.getId());
        vo.setSkuId(item.getSkuId());
        vo.setSkuCode(item.getSkuCode());
        vo.setSkuName(item.getSkuName());
        vo.setSpecSnapshot(item.getSpecSnapshot());
        vo.setSupplierId(item.getSupplierId());
        vo.setSupplierCode(item.getSupplierCode());
        vo.setSupplierName(item.getSupplierName());
        vo.setPurchasePrice(item.getPurchasePrice());
        vo.setDeliveryCycleDay(item.getDeliveryCycleDay());
        vo.setMinOrderQty(item.getMinOrderQty());
        vo.setSupplierSkuCode(item.getSupplierSkuCode());
        vo.setDefaultFlag(item.getDefaultFlag());
        vo.setEnableStatus(item.getEnableStatus());
        vo.setRemark(item.getRemark());
        vo.setUpdateTime(item.getUpdateTime());
        return vo;
    }

    public static SkuSupplierRelationSkuOptionVO toSkuOptionVO(ProductSkuSupplierRelationSkuOption option) {
        SkuSupplierRelationSkuOptionVO vo = new SkuSupplierRelationSkuOptionVO();
        if (option == null) {
            return vo;
        }
        vo.setSkuId(option.getSkuId());
        vo.setSkuCode(option.getSkuCode());
        vo.setSkuName(option.getSkuName());
        return vo;
    }

    public static SkuSupplierRelationSupplierOptionVO toSupplierOptionVO(ProductSkuSupplierRelationSupplierOption option) {
        SkuSupplierRelationSupplierOptionVO vo = new SkuSupplierRelationSupplierOptionVO();
        if (option == null) {
            return vo;
        }
        vo.setSupplierId(option.getSupplierId());
        vo.setSupplierCode(option.getSupplierCode());
        vo.setSupplierName(option.getSupplierName());
        return vo;
    }

    private static FieldEntity buildSkuField(List<ProductSkuSupplierRelationSkuOption> skuOptions) {
        FieldEntity field = new FieldEntity();
        field.setAttr("main.skuId");
        field.setAttrName("SKU");
        field.setFieldType(String.valueOf(FieldTypeEnum.COMB.getType()));
        field.setRequired(1);
        field.setEditable(1);
        field.setItemList(skuOptions == null ? List.of() : skuOptions.stream().map(SkuSupplierRelationAdminAssembler::toSkuFieldItem).toList());
        return field;
    }

    private static FieldEntity buildSupplierField(String corpid) {
        FieldEntity field = new FieldEntity();
        field.setAttr("main.supplierId");
        field.setAttrName("供应商");
        field.setFieldType(String.valueOf(FieldTypeEnum.BUSINESS.getType()));
        field.setRequired(1);
        field.setEditable(1);
        field.setItemList(List.of());
        field.setBusinessSelectConfig(buildSupplierSelectConfig(corpid));
        return field;
    }

    private static FieldItem toSkuFieldItem(ProductSkuSupplierRelationSkuOption option) {
        FieldItem item = new FieldItem();
        if (option == null) {
            return item;
        }
        item.setValue(option.getSkuId());
        item.setText((option.getSkuCode() == null ? String.valueOf(option.getSkuId()) : option.getSkuCode()) + (option.getSkuName() == null ? "" : " " + option.getSkuName()));
        return item;
    }

    public static SkuSupplierRelationHistoryItemVO toHistoryItemVO(ProductSkuSupplierRelationHistory history) {
        SkuSupplierRelationHistoryItemVO vo = new SkuSupplierRelationHistoryItemVO();
        if (history == null) {
            return vo;
        }
        vo.setId(history.getId());
        vo.setOperateType(history.getOperateType());
        vo.setOperatorId(history.getOperatorId());
        vo.setOperateTime(history.getAddTime());
        vo.setRemark(history.getRemark());
        return vo;
    }
}
