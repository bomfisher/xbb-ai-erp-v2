package xbb.ai.erp.module.customer.application.validator;

import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.filed.FieldValidateModeEnum;
import xbb.ai.erp.base.common.filed.FieldValueValidator;
import xbb.ai.erp.module.customer.application.pojo.CustomerSaveContextPojo;
import xbb.ai.erp.module.customer.application.pojo.CustomerSaveExtPojo;
import xbb.ai.erp.module.customer.application.pojo.CustomerSectionStatePojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class CustomerSaveCommonValidator {

    private final FieldValueValidator fieldValueValidator = new FieldValueValidator();

    public void validateForDraft(CustomerSaveContextPojo context) {
        fieldValueValidator.validate(CustomerSaveFieldRules.allFieldRules(), context, this::buildValidateTarget, FieldValidateModeEnum.DRAFT);
        validateDefaultUniqueness(context.getExt());
    }

    public void validateForSubmit(CustomerSaveContextPojo context) {
        fieldValueValidator.validate(CustomerSaveFieldRules.allFieldRules(), context, this::buildValidateTarget, FieldValidateModeEnum.SUBMIT);
        validateDefaultUniqueness(context.getExt());
    }

    private Map<String, Object> buildValidateTarget(CustomerSaveContextPojo context) {
        Map<String, Object> target = new HashMap<>();
        if (context == null) {
            return target;
        }
        target.put("main", context.getMain());
        CustomerSaveExtPojo ext = filterClosedSections(context.getExt(), context.getSectionState());
        target.put("contacts", ext.getContacts());
        target.put("addresses", ext.getAddresses());
        target.put("bankAccounts", ext.getBankAccounts());
        target.put("invoiceProfiles", ext.getInvoiceProfiles());
        return target;
    }

    private void validateDefaultUniqueness(CustomerSaveExtPojo ext) {
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

    private CustomerSaveExtPojo filterClosedSections(CustomerSaveExtPojo ext, CustomerSectionStatePojo sectionState) {
        CustomerSaveExtPojo filtered = new CustomerSaveExtPojo();
        if (ext == null) {
            return filtered;
        }
        filtered.setContacts(isOpen(sectionState == null ? null : sectionState.getContacts(), ext.getContacts()) ? ext.getContacts() : List.of());
        filtered.setAddresses(isOpen(sectionState == null ? null : sectionState.getAddresses(), ext.getAddresses()) ? ext.getAddresses() : List.of());
        filtered.setBankAccounts(isOpen(sectionState == null ? null : sectionState.getBankAccounts(), ext.getBankAccounts()) ? ext.getBankAccounts() : List.of());
        filtered.setInvoiceProfiles(isOpen(sectionState == null ? null : sectionState.getInvoiceProfiles(), ext.getInvoiceProfiles()) ? ext.getInvoiceProfiles() : List.of());
        return filtered;
    }

    private boolean isOpen(Integer value, List<?> rows) {
        if (value != null) {
            return Integer.valueOf(1).equals(value);
        }
        return rows != null && !rows.isEmpty();
    }
}
