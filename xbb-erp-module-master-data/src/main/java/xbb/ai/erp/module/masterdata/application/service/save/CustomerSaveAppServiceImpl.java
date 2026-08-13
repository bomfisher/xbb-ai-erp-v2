package xbb.ai.erp.module.masterdata.application.service.save;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.module.masterdata.admin.dto.CustomerSaveDTO;
import xbb.ai.erp.module.masterdata.admin.dto.CustomerSubmitSaveDTO;
import xbb.ai.erp.module.masterdata.application.assembler.CustomerAdminAssembler;
import xbb.ai.erp.module.masterdata.application.validator.CustomerValidator;
import xbb.ai.erp.module.masterdata.application.port.CustomerDraftRepository;
import xbb.ai.erp.module.masterdata.application.validator.CustomerSaveProtocolValidator;
import xbb.ai.erp.module.masterdata.application.validator.CustomerSaveCommonValidator;
import xbb.ai.erp.module.masterdata.application.validator.CustomerSaveBusinessValidator;
import xbb.ai.erp.module.masterdata.domain.model.Customer;
import xbb.ai.erp.module.masterdata.domain.repository.CustomerRepository;
import xbb.ai.erp.module.masterdata.domain.model.CustomerContact;
import xbb.ai.erp.module.masterdata.domain.repository.CustomerContactRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerSaveAppServiceImpl {

    private final CustomerRepository customerRepository;
    private final CustomerContactRepository customerContactRepository;

    private final CustomerDraftRepository draftRepository;
    private final CustomerSaveProtocolValidator protocolValidator;
    private final CustomerSaveCommonValidator commonValidator;
    private final CustomerSaveBusinessValidator businessValidator;

    @Transactional
    public BaseVO saveAndSubmit(CustomerSubmitSaveDTO dto) {
        protocolValidator.validate(dto);
        commonValidator.validateForSubmit(dto);
        businessValidator.validateForSubmit(dto);
        save(dto);
        if (dto.getDraftMeta() != null && dto.getDraftMeta().getDraftCode() != null) draftRepository.removeDraft(dto.getCorpid(), dto.getDraftMeta().getDraftCode());
        return new BaseVO();
    }

    @Transactional
    public Long save(CustomerSaveDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        CustomerValidator.validateSave(dto);
        Customer entity = CustomerAdminAssembler.toCustomer(dto);
        Long customerId;
        if (entity.getId() == null) {
            customerId = customerRepository.insert(entity);
        } else {
            customerRepository.update(entity);
            customerId = entity.getId();
        }
        List<CustomerContact> contacts = CustomerAdminAssembler.toContacts(dto, customerId);
        customerContactRepository.sync(dto.getCorpid(), customerId, contacts);
        Long defaultContactId = contacts.stream().filter(contact -> Integer.valueOf(1).equals(contact.getDefaultFlag())).map(CustomerContact::getId).findFirst().orElse(null);
        customerRepository.updateDefaultContactId(dto.getCorpid(), customerId, defaultContactId);
        return customerId;
    }

    public void delete(BatchBaseDTO dto) {
        if (dto.getIdList() != null && !dto.getIdList().isEmpty()) {
            customerRepository.removeBatchByIds(dto.getCorpid(), dto.getIdList());
        }
    }
}
