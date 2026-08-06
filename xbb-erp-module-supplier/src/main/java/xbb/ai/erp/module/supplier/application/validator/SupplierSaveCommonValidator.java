package xbb.ai.erp.module.supplier.application.validator;

import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.supplier.application.pojo.SupplierSaveContextPojo;
import xbb.ai.erp.module.supplier.application.pojo.SupplierSaveExtPojo;

import java.util.List;
import java.util.function.Function;

public class SupplierSaveCommonValidator {

    public void validateForDraft(SupplierSaveContextPojo context) {
        validateDefaultUniqueness(context == null ? null : context.getExt());
    }

    public void validateForSubmit(SupplierSaveContextPojo context) {
        validateRequired(context);
        validateDefaultUniqueness(context == null ? null : context.getExt());
    }

    private void validateRequired(SupplierSaveContextPojo context) {
        if (context == null || context.getMain() == null) {
            throw new BizException("供应商主档不能为空");
        }
        if (context.getMain().getSupplierCode() == null || context.getMain().getSupplierCode().isBlank()) {
            throw new BizException("供应商编码不能为空");
        }
        if (context.getMain().getSupplierName() == null || context.getMain().getSupplierName().isBlank()) {
            throw new BizException("供应商名称不能为空");
        }
    }

    private void validateDefaultUniqueness(SupplierSaveExtPojo ext) {
        if (ext == null) {
            return;
        }
        validateList(ext.getContacts(), item -> item.getDefaultFlag(), "联系人默认项只能有一个");
        validateList(ext.getAddresses(), item -> item.getDefaultFlag(), "地址默认项只能有一个");
        validateList(ext.getBankAccounts(), item -> item.getDefaultFlag(), "银行账户默认项只能有一个");
        validateList(ext.getInvoiceProfiles(), item -> item.getDefaultFlag(), "开票信息默认项只能有一个");
    }

    private <T> void validateList(List<T> list, Function<T, Integer> getter, String message) {
        long count = list == null ? 0 : list.stream().filter(item -> Integer.valueOf(1).equals(getter.apply(item))).count();
        if (count > 1) {
            throw new BizException(message);
        }
    }
}
