package xbb.ai.erp.module.product.application.assembler;

import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.product.admin.dto.ProductDraftMetaDTO;
import xbb.ai.erp.module.product.admin.dto.ProductDraftSaveDTO;
import xbb.ai.erp.module.product.admin.dto.ProductMainDTO;
import xbb.ai.erp.module.product.admin.dto.ProductSaveDTO;
import xbb.ai.erp.module.product.admin.dto.ProductSkuItemDTO;
import xbb.ai.erp.module.product.admin.vo.ProductBusinessSelectOptionVO;
import xbb.ai.erp.module.product.admin.vo.ProductDetailVO;
import xbb.ai.erp.module.product.admin.vo.ProductDraftDetailVO;
import xbb.ai.erp.module.product.admin.vo.ProductDraftListItemVO;
import xbb.ai.erp.module.product.admin.vo.ProductListItemVO;
import xbb.ai.erp.module.product.admin.vo.ProductSaveItemVO;
import xbb.ai.erp.module.product.admin.vo.ProductSkuItemVO;
import xbb.ai.erp.module.product.application.pojo.ProductSaveContextPojo;
import xbb.ai.erp.module.product.application.pojo.ProductSaveDraftPojo;
import xbb.ai.erp.module.product.domain.model.ProductSku;
import xbb.ai.erp.module.product.domain.model.ProductSpu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ProductAdminAssembler {

    private ProductAdminAssembler() {
    }

    public static ProductListItemVO toListItemVO(ProductSpu productSpu) {
        ProductListItemVO vo = new ProductListItemVO();
        if (productSpu == null) {
            return vo;
        }
        vo.setSpuId(productSpu.getId());
        vo.setSpuCode(productSpu.getSpuCode());
        vo.setSpuName(productSpu.getSpuName());
        vo.setProductType(productSpu.getProductType());
        vo.setSpuEnableStatus(productSpu.getEnableStatus());
        return vo;
    }

    public static ProductDetailVO toDetailVO(ProductSpu productSpu, List<ProductSku> productSkuList) {
        ProductDetailVO vo = new ProductDetailVO();
        vo.setMainData(toProductSaveItemVO(productSpu, productSkuList));
        return vo;
    }

    public static SaveItemVO<ProductSaveItemVO> buildEmptySaveItemVO() {
        SaveItemVO<ProductSaveItemVO> vo = new SaveItemVO<>();
        vo.setData(buildEmptyProductSaveItemVO());
        return vo;
    }

    public static SaveItemVO<ProductSaveItemVO> toSaveItemVO(ProductSpu productSpu, List<ProductSku> productSkuList) {
        SaveItemVO<ProductSaveItemVO> vo = buildEmptySaveItemVO();
        vo.setData(toProductSaveItemVO(productSpu, productSkuList));
        return vo;
    }

    public static ProductSaveItemVO buildEmptyProductSaveItemVO() {
        ProductSaveItemVO saveItemVO = new ProductSaveItemVO();
        ProductMainDTO main = new ProductMainDTO();
        main.setEnableSpec(0);
        main.setSpuEnableStatus(1);
        saveItemVO.setMain(main);
        saveItemVO.setSkus(List.of());
        return saveItemVO;
    }

    public static ProductSaveItemVO toProductSaveItemVO(ProductSpu productSpu, List<ProductSku> productSkuList) {
        ProductSaveItemVO saveItemVO = buildEmptyProductSaveItemVO();
        if (productSpu != null) {
            ProductMainDTO main = new ProductMainDTO();
            main.setSpuId(productSpu.getId());
            main.setSpuCode(productSpu.getSpuCode());
            main.setSpuName(productSpu.getSpuName());
            main.setCategoryId(productSpu.getCategoryId());
            main.setBrandId(productSpu.getBrandId());
            main.setProductType(productSpu.getProductType());
            main.setEnableSpec(productSpu.getEnableSpec());
            main.setDescription(productSpu.getDescription());
            main.setImageUrl(productSpu.getImageUrl());
            main.setSpuEnableStatus(productSpu.getEnableStatus());
            saveItemVO.setMain(main);
        }
        saveItemVO.setSkus(productSkuList == null ? List.<ProductSkuItemDTO>of() : productSkuList.stream().map(ProductAdminAssembler::toSkuItemDTO).toList());
        return saveItemVO;
    }

    public static ProductSkuItemDTO toSkuItemDTO(ProductSku productSku) {
        ProductSkuItemDTO dto = new ProductSkuItemDTO();
        if (productSku == null) {
            return dto;
        }
        dto.setSkuId(productSku.getId());
        dto.setSkuCode(productSku.getSkuCode());
        dto.setSkuName(productSku.getSkuName());
        dto.setSpecSignature(productSku.getSpecSignature());
        dto.setSpecSnapshot(productSku.getSpecSnapshot());
        return dto;
    }

    public static ProductSkuItemDTO toSkuItemDTO(ProductSkuItemDTO skuItem) {
        ProductSkuItemDTO dto = new ProductSkuItemDTO();
        if (skuItem == null) {
            return dto;
        }
        dto.setSkuId(skuItem.getSkuId());
        dto.setSkuCode(skuItem.getSkuCode());
        dto.setSkuName(skuItem.getSkuName());
        dto.setSpecSignature(skuItem.getSpecSignature());
        dto.setSpecSnapshot(skuItem.getSpecSnapshot());
        dto.setMnemonicCode(skuItem.getMnemonicCode());
        dto.setMainBarcode(skuItem.getMainBarcode());
        dto.setCanPurchase(skuItem.getCanPurchase());
        dto.setCanSale(skuItem.getCanSale());
        dto.setCanInventory(skuItem.getCanInventory());
        dto.setCanProduce(skuItem.getCanProduce());
        dto.setSkuEnableStatus(skuItem.getSkuEnableStatus());
        dto.setListingStatus(skuItem.getListingStatus());
        return dto;
    }

    public static ProductBusinessSelectOptionVO toBusinessSelectOptionVO(ProductSku productSku) {
        ProductBusinessSelectOptionVO option = new ProductBusinessSelectOptionVO();
        if (productSku == null) {
            return option;
        }
        option.setId(productSku.getId());
        option.setCode(productSku.getSkuCode());
        option.setName(productSku.getSkuName());
        option.setLabel(buildBusinessSelectLabel(productSku));
        Map<String, Object> linePatch = new HashMap<>();
        linePatch.put("skuId", productSku.getId());
        linePatch.put("skuCodeSnapshot", productSku.getSkuCode());
        linePatch.put("skuNameSnapshot", productSku.getSkuName());
        linePatch.put("specSnapshot", productSku.getSpecSnapshot());
        option.setLinePatch(linePatch);
        return option;
    }

    public static FieldEntity.ProductSelectConfig buildProductSelectConfig(String corpid, String businessCode) {
        FieldEntity.ProductSelectConfig config = new FieldEntity.ProductSelectConfig();
        config.setProductType("product-sku");
        config.setBusinessCode(businessCode);
        config.setRequestPayload(Map.of("corpid", corpid, "businessCode", businessCode));
        config.setPlaceholder("请选择产品");
        config.setDialogTitle("选择产品");
        config.setMultiple(Boolean.TRUE);
        return config;
    }

    public static FieldEntity.BusinessSelectConfig buildProductBusinessSelectConfig(String corpid, String businessCode) {
        FieldEntity.BusinessSelectConfig config = new FieldEntity.BusinessSelectConfig();
        config.setBusinessType("product-sku");
        config.setProductType("product-sku");
        config.setBusinessCode(businessCode);
        config.setRequestPayload(Map.of("corpid", corpid, "businessCode", businessCode));
        config.setPlaceholder("请选择产品");
        config.setDialogTitle("选择产品");
        config.setMultiple(Boolean.TRUE);
        return config;
    }

    public static ProductSaveContextPojo toSaveContext(ProductSaveDTO dto) {
        ProductSaveContextPojo context = new ProductSaveContextPojo();
        if (dto == null) {
            return context;
        }
        context.setCorpid(dto.getCorpid());
        context.setUserId(dto.getUserId());
        context.setMain(dto.getMain());
        context.setSkus(dto.getSkus() == null ? List.of() : dto.getSkus());
        if (dto instanceof xbb.ai.erp.module.product.admin.dto.ProductSubmitSaveDTO submitDTO) {
            context.setDraft(toDraftPojo(submitDTO.getDraftMeta()));
        }
        if (dto instanceof xbb.ai.erp.module.product.admin.dto.ProductDraftSaveDTO draftDTO) {
            context.setDraft(toDraftPojo(draftDTO.getDraftMeta()));
        }
        return context;
    }

    public static ProductSpu toProductSpu(ProductSaveDTO dto) {
        if (dto == null) {
            return null;
        }
        return toProductSpu(dto.getCorpid(), dto.getMain());
    }

    public static ProductSpu toProductSpu(String corpid, ProductMainDTO main) {
        ProductSpu productSpu = new ProductSpu();
        if (main == null) {
            return productSpu;
        }
        productSpu.setId(main.getSpuId());
        productSpu.setCorpid(corpid);
        productSpu.setSpuCode(main.getSpuCode());
        productSpu.setSpuName(main.getSpuName());
        productSpu.setCategoryId(main.getCategoryId());
        productSpu.setBrandId(main.getBrandId());
        productSpu.setProductType(main.getProductType());
        productSpu.setEnableSpec(main.getEnableSpec());
        productSpu.setDescription(main.getDescription());
        productSpu.setImageUrl(main.getImageUrl());
        productSpu.setEnableStatus(main.getSpuEnableStatus());
        return productSpu;
    }

    public static ProductSku toProductSku(String corpid, Long spuId, ProductSkuItemDTO skuItem) {
        ProductSku productSku = new ProductSku();
        if (skuItem == null) {
            return productSku;
        }
        productSku.setId(skuItem.getSkuId());
        productSku.setCorpid(corpid);
        productSku.setSpuId(spuId);
        productSku.setSkuCode(skuItem.getSkuCode());
        productSku.setSkuName(skuItem.getSkuName());
        productSku.setMnemonicCode(skuItem.getMnemonicCode());
        productSku.setMainBarcode(skuItem.getMainBarcode());
        productSku.setSpecSignature(skuItem.getSpecSignature());
        productSku.setSpecSnapshot(skuItem.getSpecSnapshot());
        productSku.setCanPurchase(skuItem.getCanPurchase());
        productSku.setCanSale(skuItem.getCanSale());
        productSku.setCanInventory(skuItem.getCanInventory());
        productSku.setCanProduce(skuItem.getCanProduce());
        productSku.setEnableStatus(skuItem.getSkuEnableStatus());
        productSku.setListingStatus(skuItem.getListingStatus());
        return productSku;
    }

    public static ProductSaveDraftPojo toDraftPojo(ProductDraftSaveDTO dto) {
        ProductSaveDraftPojo draft = toDraftPojo(dto == null ? null : dto.getDraftMeta());
        if (dto == null) {
            return draft;
        }
        draft.setCorpid(dto.getCorpid());
        draft.setMain(dto.getMain() == null ? new ProductMainDTO() : dto.getMain());
        draft.setSkus(dto.getSkus() == null ? List.of() : dto.getSkus());
        return draft;
    }

    public static ProductDraftListItemVO toDraftListItemVO(ProductSaveDraftPojo draft) {
        ProductDraftListItemVO vo = new ProductDraftListItemVO();
        if (draft == null) {
            return vo;
        }
        vo.setDraftId(draft.getDraftId());
        vo.setDraftCode(draft.getDraftCode());
        vo.setDraftName(draft.getDraftName());
        return vo;
    }

    public static ProductDraftDetailVO toDraftDetailVO(ProductSaveDraftPojo draft) {
        ProductDraftDetailVO vo = new ProductDraftDetailVO();
        if (draft == null) {
            return vo;
        }
        ProductDraftDetailVO.ProductDraftMetaVO metaVO = new ProductDraftDetailVO.ProductDraftMetaVO();
        metaVO.setDraftId(draft.getDraftId());
        metaVO.setDraftCode(draft.getDraftCode());
        metaVO.setDraftName(draft.getDraftName());
        vo.setDraftMeta(metaVO);

        ProductDetailVO detailVO = new ProductDetailVO();
        ProductMainDTO mainVO = new ProductMainDTO();
        if (draft.getMain() != null) {
            mainVO.setSpuId(draft.getMain().getSpuId());
            mainVO.setSpuCode(draft.getMain().getSpuCode());
            mainVO.setSpuName(draft.getMain().getSpuName());
            mainVO.setCategoryId(draft.getMain().getCategoryId());
            mainVO.setBrandId(draft.getMain().getBrandId());
            mainVO.setProductType(draft.getMain().getProductType());
            mainVO.setEnableSpec(draft.getMain().getEnableSpec());
            mainVO.setDescription(draft.getMain().getDescription());
            mainVO.setImageUrl(draft.getMain().getImageUrl());
            mainVO.setSpuEnableStatus(draft.getMain().getSpuEnableStatus());
        }
        ProductSaveItemVO saveItemVO = new ProductSaveItemVO();
        saveItemVO.setMain(mainVO);
        saveItemVO.setSkus(draft.getSkus() == null ? List.of() : draft.getSkus());
        detailVO.setMainData(saveItemVO);
        vo.setDetail(detailVO);
        return vo;
    }

    public static boolean matchKeyword(ProductSaveDraftPojo draft, String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return true;
        }
        if (draft == null) {
            return false;
        }
        return contains(draft.getDraftCode(), keyword)
            || contains(draft.getDraftName(), keyword)
            || draft.getMain() != null && (contains(draft.getMain().getSpuCode(), keyword) || contains(draft.getMain().getSpuName(), keyword));
    }

    private static boolean contains(String source, String keyword) {
        return source != null && source.contains(keyword);
    }

    private static String buildBusinessSelectLabel(ProductSku productSku) {
        if (productSku == null) {
            return null;
        }
        if (productSku.getSkuCode() == null || productSku.getSkuCode().isBlank()) {
            return productSku.getSkuName();
        }
        if (productSku.getSkuName() == null || productSku.getSkuName().isBlank()) {
            return productSku.getSkuCode();
        }
        return productSku.getSkuCode() + " " + productSku.getSkuName();
    }

    private static ProductSaveDraftPojo toDraftPojo(ProductDraftMetaDTO draftMeta) {
        ProductSaveDraftPojo draft = new ProductSaveDraftPojo();
        if (draftMeta == null) {
            return draft;
        }
        draft.setDraftId(draftMeta.getDraftId());
        draft.setDraftCode(draftMeta.getDraftCode());
        draft.setDraftName(draftMeta.getDraftName());
        draft.setUpdatedTime(draftMeta.getUpdatedTime());
        return draft;
    }
}
