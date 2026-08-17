package xbb.ai.erp.module.masterdata.application.service.query;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.common.application.render.ListValueRenderer;
import xbb.ai.erp.module.common.application.util.ListQueryMapUtil;
import xbb.ai.erp.scene.meta.SceneFieldAssembler;
import xbb.ai.erp.scene.meta.SceneTypeEnum;
import xbb.ai.erp.module.masterdata.admin.vo.CustomerDetailVO;
import xbb.ai.erp.module.masterdata.admin.vo.CustomerListItemVO;
import xbb.ai.erp.module.masterdata.admin.dto.CustomerBusinessSelectQueryDTO;
import xbb.ai.erp.module.masterdata.admin.vo.CustomerBusinessSelectOptionVO;
import xbb.ai.erp.module.masterdata.application.assembler.CustomerAdminAssembler;
import xbb.ai.erp.module.masterdata.application.field.CustomerFieldFactory;
import xbb.ai.erp.module.masterdata.application.schema.CustomerListSchemaProvider;
import xbb.ai.erp.module.masterdata.domain.model.Customer;
import xbb.ai.erp.module.masterdata.domain.repository.CustomerContactRepository;
import xbb.ai.erp.module.masterdata.domain.repository.CustomerRepository;

@Service
public class CustomerQueryAppServiceImpl {
    private final CustomerRepository customerRepository;
    private final CustomerContactRepository customerContactRepository;
    private final CustomerFieldFactory fieldFactory;
    private final CustomerListSchemaProvider schemaProvider;
    private final ListValueRenderer listValueRenderer;
    private final ListQueryMapUtil listQueryMapUtil = new ListQueryMapUtil();


    public CustomerQueryAppServiceImpl(CustomerRepository customerRepository, CustomerContactRepository customerContactRepository, CustomerFieldFactory fieldFactory, CustomerListSchemaProvider schemaProvider, ListValueRenderer listValueRenderer) {
        this.customerRepository = customerRepository;
        this.customerContactRepository = customerContactRepository;
        this.fieldFactory = fieldFactory;
        this.schemaProvider = schemaProvider;
        this.listValueRenderer = listValueRenderer;
    }

    public ListBaseVO<CustomerListItemVO> list(ListBaseDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        Map<String, Object> conditionMap = listQueryMapUtil.gen(dto, schemaProvider.conditionMetaMap());
        List<Customer> list = customerRepository.findByCondition(conditionMap);
        Long total = customerRepository.count(conditionMap);
        ListBaseVO<CustomerListItemVO> vo = new ListBaseVO<>();
        List<CustomerListItemVO> items = list.stream().map(CustomerAdminAssembler::toListItemVO).toList();
        vo.setList(listValueRenderer.render(dto.getCorpid(), BusinessCodeEnum.CUSTOMER.getCode(), items));
        vo.setList(list.stream().map(CustomerAdminAssembler::toListItemVO).toList());
        vo.setPageHelper(new ListBaseVO.PageHelper(dto.getPageNum(), total == null ? 0 : total.intValue()));
        return vo;
    }

    public SaveItemVO<xbb.ai.erp.module.masterdata.admin.vo.CustomerSaveItemVO> addItem(BaseDTO dto) {
        SaveItemVO<xbb.ai.erp.module.masterdata.admin.vo.CustomerSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(SceneFieldAssembler.buildHeadList(fieldFactory.getFields(SceneTypeEnum.CREATE)));
        vo.setData(CustomerAdminAssembler.buildEmptySaveItemVO());
        return vo;
    }

    public SaveItemVO<xbb.ai.erp.module.masterdata.admin.vo.CustomerSaveItemVO> updateItem(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        Customer entity = customerRepository.findById(dto.getCorpid(), dto.getId());
        SaveItemVO<xbb.ai.erp.module.masterdata.admin.vo.CustomerSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(SceneFieldAssembler.buildHeadList(fieldFactory.getFields(SceneTypeEnum.UPDATE)));
        vo.setData(CustomerAdminAssembler.toSaveItemVO(entity, customerContactRepository.findByCustomerId(dto.getCorpid(), dto.getId())));
        return vo;
    }

    public CustomerDetailVO detail(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        Customer entity = customerRepository.findById(dto.getCorpid(), dto.getId());
        return CustomerAdminAssembler.toDetailVO(CustomerAdminAssembler.toSaveItemVO(entity, customerContactRepository.findByCustomerId(dto.getCorpid(), dto.getId())));
    }

    public List<CustomerBusinessSelectOptionVO> businessSelectQuickSearch(CustomerBusinessSelectQueryDTO dto) {
        return findBusinessSelectOptions(dto);
    }

    public ListBaseVO<CustomerBusinessSelectOptionVO> businessSelectDialogSearch(CustomerBusinessSelectQueryDTO dto) {
        int pageNum = dto.getPageNum() == null || dto.getPageNum() < 1 ? 1 : dto.getPageNum();
        int pageSize = dto.getPageSize() == null || dto.getPageSize() < 1 ? 20 : dto.getPageSize();
        List<CustomerBusinessSelectOptionVO> all = findBusinessSelectOptions(dto);
        int fromIndex = Math.min((pageNum - 1) * pageSize, all.size());
        int toIndex = Math.min(fromIndex + pageSize, all.size());
        ListBaseVO<CustomerBusinessSelectOptionVO> vo = new ListBaseVO<>();
        vo.setList(all.subList(fromIndex, toIndex));
        vo.setPageHelper(new ListBaseVO.PageHelper(pageNum, Math.max((all.size() + pageSize - 1) / pageSize, 1)));
        return vo;
    }

    public CustomerBusinessSelectOptionVO businessSelectGetById(CustomerBusinessSelectQueryDTO dto) {
        if (dto.getId() == null) {
            return null;
        }
        AdminParamValidator.requireCorpid(dto);
        Customer customer = customerRepository.findById(dto.getCorpid(), dto.getId());
        return customer == null ? null : toBusinessSelectOption(customer);
    }

    private List<CustomerBusinessSelectOptionVO> findBusinessSelectOptions(CustomerBusinessSelectQueryDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        String keyword = dto.getKeyword() == null ? "" : dto.getKeyword().trim();
        return customerRepository.findByCondition(Map.of("corpid", dto.getCorpid())).stream()
            .filter(customer -> keyword.isEmpty()
                || (customer.getCustomerCode() != null && customer.getCustomerCode().contains(keyword))
                || (customer.getCustomerName() != null && customer.getCustomerName().contains(keyword)))
            .map(this::toBusinessSelectOption)
            .toList();
    }

    private CustomerBusinessSelectOptionVO toBusinessSelectOption(Customer customer) {
        CustomerBusinessSelectOptionVO option = new CustomerBusinessSelectOptionVO();
        option.setId(customer.getId());
        option.setCode(customer.getCustomerCode());
        option.setName(customer.getCustomerName());
        option.setLabel(customer.getCustomerCode() == null || customer.getCustomerCode().isBlank()
            ? customer.getCustomerName()
            : customer.getCustomerName() == null || customer.getCustomerName().isBlank()
                ? customer.getCustomerCode()
                : customer.getCustomerCode() + " " + customer.getCustomerName());
        return option;
    }
}
