package xbb.ai.erp.module.product.admin.vo;

import lombok.Data;
import xbb.ai.erp.module.product.admin.dto.SkuSupplierRelationMainDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class SkuSupplierRelationSaveItemVO {

    private SkuSupplierRelationMainDTO main = new SkuSupplierRelationMainDTO();
    private List<SkuSupplierRelationSkuOptionVO> skuOptions = new ArrayList<>();
    private List<SkuSupplierRelationSupplierOptionVO> supplierOptions = new ArrayList<>();
    private SupplierSelectConfigVO supplierSelectConfig;

    @Data
    public static class SupplierSelectConfigVO {
        private String businessType;
        private String quickSearchUrl;
        private String dialogSearchUrl;
        private String getByIdUrl;
        private Map<String, Object> requestPayload;
        private String placeholder;
        private String dialogTitle;
    }
}
