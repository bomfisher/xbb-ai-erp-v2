package xbb.ai.erp.module.system.application.catalog;

import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.module.system.contract.ApprovalConfigKeyEnum;
import xbb.ai.erp.module.system.contract.BooleanConfigKeyEnum;
import xbb.ai.erp.module.system.contract.BusinessConfigKey;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class BusinessConfigCatalog {

    private final Map<String, Entry> entries = new LinkedHashMap<>();

    public BusinessConfigCatalog() {
        register("SALES", "销售业务", ApprovalConfigKeyEnum.SALES_ORDER_APPROVAL_MODE, "销售订单", 10);
        register("SALES", "销售业务", ApprovalConfigKeyEnum.SALES_OUTBOUND_APPROVAL_MODE, "销售出库", 20);
        register("SALES", "销售业务", ApprovalConfigKeyEnum.SALES_INVOICE_APPROVAL_MODE, "销售发票", 30);
        register("SALES", "销售业务", BooleanConfigKeyEnum.SALES_INVOICE_AUTO_CREATE_RECEIVABLE, "销售发票", 30);
        register("PURCHASE", "采购业务", ApprovalConfigKeyEnum.PURCHASE_ORDER_APPROVAL_MODE, "采购订单", 10);
        register("PURCHASE", "采购业务", ApprovalConfigKeyEnum.PURCHASE_INBOUND_APPROVAL_MODE, "采购入库", 20);
        register("PURCHASE", "采购业务", ApprovalConfigKeyEnum.PURCHASE_INVOICE_APPROVAL_MODE, "采购发票", 30);
        register("PURCHASE", "采购业务", BooleanConfigKeyEnum.PURCHASE_INVOICE_AUTO_CREATE_PAYABLE, "采购发票", 30);
        register("PAYMENT", "付款业务", ApprovalConfigKeyEnum.ADVANCE_PAYMENT_APPROVAL_MODE, "预付款", 10);
        register("PAYMENT", "付款业务", ApprovalConfigKeyEnum.PAYABLE_APPROVAL_MODE, "应付款", 20);
        register("PAYMENT", "付款业务", ApprovalConfigKeyEnum.PAYMENT_APPROVAL_MODE, "付款单", 30);
        register("PAYMENT", "付款业务", ApprovalConfigKeyEnum.PAYMENT_WRITEOFF_APPROVAL_MODE, "付款核销", 40);
        register("RECEIPT", "收款业务", ApprovalConfigKeyEnum.ADVANCE_RECEIPT_APPROVAL_MODE, "预收款", 10);
        register("RECEIPT", "收款业务", ApprovalConfigKeyEnum.RECEIVABLE_APPROVAL_MODE, "应收款", 20);
        register("RECEIPT", "收款业务", ApprovalConfigKeyEnum.RECEIPT_APPROVAL_MODE, "收款单", 30);
        register("RECEIPT", "收款业务", ApprovalConfigKeyEnum.RECEIPT_WRITEOFF_APPROVAL_MODE, "收款核销", 40);
    }

    public List<Category> categories() {
        return List.of(new Category("GLOBAL", "全局配置"), new Category("SALES", "销售业务"),
            new Category("PURCHASE", "采购业务"));
    }

    public List<Entry> entries(String categoryCode) {
        if ("GLOBAL".equals(categoryCode)) {
            return entries.values().stream().filter(entry -> entry.key() instanceof ApprovalConfigKeyEnum).toList();
        }
        return entries.values().stream().filter(entry -> entry.categoryCode().equals(categoryCode))
            .filter(entry -> !(entry.key() instanceof ApprovalConfigKeyEnum)).toList();
    }

    public List<Entry> entriesForBusiness(String businessCode) {
        return entries.values().stream().filter(entry -> entry.businessCode().getCode().equals(businessCode)).toList();
    }

    public List<Entry> approvalEntries() {
        return entries.values().stream().filter(entry -> entry.key() instanceof ApprovalConfigKeyEnum).toList();
    }

    public Entry require(String businessCode, String configCode) {
        Entry entry = entries.get(businessCode + ":" + configCode);
        if (entry == null) {
            throw new BizException("不支持的系统配置项");
        }
        return entry;
    }

    private void register(String categoryCode, String categoryName, BusinessConfigKey<?> key, String businessName, int order) {
        entries.put(key.businessCode().getCode() + ":" + key.code(), new Entry(categoryCode, categoryName,
            key.businessCode(), businessName, order, key));
    }

    public record Category(String code, String name) {
    }

    public record Entry(String categoryCode, String categoryName, BusinessCodeEnum businessCode, String businessName,
                        int order, BusinessConfigKey<?> key) {
    }
}
