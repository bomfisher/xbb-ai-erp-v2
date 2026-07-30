package xbb.ai.erp.module.customer.application.service.draft;

import xbb.ai.erp.module.customer.admin.dto.CustomerDraftListDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerDraftLoadDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerDraftSaveDTO;
import xbb.ai.erp.module.customer.admin.vo.CustomerDraftDetailVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerDraftListItemVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerDraftSaveVO;
import xbb.ai.erp.module.customer.application.assembler.CustomerAdminAssembler;
import xbb.ai.erp.module.customer.application.pojo.CustomerSaveContextPojo;
import xbb.ai.erp.module.customer.application.pojo.CustomerSaveDraftPojo;
import xbb.ai.erp.module.customer.application.port.CustomerDraftRepository;
import xbb.ai.erp.module.customer.application.validator.CustomerSaveCommonValidator;
import xbb.ai.erp.module.customer.application.validator.CustomerSaveProtocolValidator;

import java.util.List;

public class CustomerDraftAppServiceImpl implements CustomerDraftAppService {

    private final CustomerDraftRepository customerDraftRepository;
    private final CustomerSaveProtocolValidator protocolValidator;
    private final CustomerSaveCommonValidator commonValidator;

    public CustomerDraftAppServiceImpl(CustomerDraftRepository customerDraftRepository) {
        this.customerDraftRepository = customerDraftRepository;
        this.protocolValidator = new CustomerSaveProtocolValidator();
        this.commonValidator = new CustomerSaveCommonValidator();
    }

    @Override
    public CustomerDraftSaveVO saveDraft(CustomerDraftSaveDTO dto) {
        CustomerSaveContextPojo context = CustomerAdminAssembler.toDraftContext(dto);
        protocolValidator.validate(context);
        commonValidator.validateForDraft(context);
        CustomerSaveDraftPojo draft = CustomerAdminAssembler.toDraftPojo(dto);
        String draftCode = customerDraftRepository.saveDraft(draft);
        if (dto.getDraftMeta() != null) {
            dto.getDraftMeta().setDraftCode(draftCode);
        }
        CustomerDraftSaveVO vo = new CustomerDraftSaveVO();
        vo.setDraftCode(draftCode);
        return vo;
    }

    @Override
    public List<CustomerDraftListItemVO> draftList(CustomerDraftListDTO dto) {
        if (customerDraftRepository == null) {
            return List.of();
        }
        return customerDraftRepository.listDrafts(dto.getCorpid(), 10).stream()
            .map(CustomerAdminAssembler::toDraftListItemVO)
            .toList();
    }

    @Override
    public CustomerDraftDetailVO loadDraft(CustomerDraftLoadDTO dto) {
        if (customerDraftRepository == null) {
            return new CustomerDraftDetailVO();
        }
        return CustomerAdminAssembler.toDraftDetailVO(
            customerDraftRepository.loadDraft(dto.getCorpid(), dto.getDraftCode())
        );
    }
}
