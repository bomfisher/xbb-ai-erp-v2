package xbb.ai.erp.module.masterdata.application.validator;

import org.springframework.stereotype.Component;
import java.util.List;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.masterdata.admin.dto.CustomerContactDTO;
import xbb.ai.erp.module.masterdata.admin.dto.CustomerSaveDTO;

@Component
public class CustomerSaveCommonValidator {
    public void validateForDraft(CustomerSaveDTO dto) {}
    public void validateForSubmit(CustomerSaveDTO dto) {
        List<CustomerContactDTO> contacts = dto.getContacts();
        long defaultCount = contacts == null ? 0 : contacts.stream().filter(contact -> Integer.valueOf(1).equals(contact.getDefaultFlag())).count();
        if (defaultCount > 1) throw new BizException("只能设置一位默认联系人");
    }
}
