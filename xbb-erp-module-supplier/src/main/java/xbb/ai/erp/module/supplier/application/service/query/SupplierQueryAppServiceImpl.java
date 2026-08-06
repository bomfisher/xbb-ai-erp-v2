package xbb.ai.erp.module.supplier.application.service.query;

import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.filed.FieldTypeEnum;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierBusinessSelectQueryDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierListDTO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierBusinessSelectOptionVO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierDetailVO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierListItemVO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierSaveItemVO;
import xbb.ai.erp.module.supplier.application.assembler.SupplierAdminAssembler;
import xbb.ai.erp.module.supplier.application.schema.SupplierListQueryAdapter;
import xbb.ai.erp.module.supplier.application.support.SupplierFieldMetadataSupport;
import xbb.ai.erp.module.supplier.domain.model.Supplier;
import xbb.ai.erp.module.supplier.domain.model.SupplierAddress;
import xbb.ai.erp.module.supplier.domain.model.SupplierBankAccount;
import xbb.ai.erp.module.supplier.domain.model.SupplierContact;
import xbb.ai.erp.module.supplier.domain.model.SupplierInvoiceProfile;
import xbb.ai.erp.module.supplier.domain.repository.SupplierAddressRepository;
import xbb.ai.erp.module.supplier.domain.repository.SupplierBankAccountRepository;
import xbb.ai.erp.module.supplier.domain.repository.SupplierContactRepository;
import xbb.ai.erp.module.supplier.domain.repository.SupplierInvoiceProfileRepository;
import xbb.ai.erp.module.supplier.domain.repository.SupplierRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SupplierQueryAppServiceImpl implements SupplierQueryAppService {

    private final SupplierRepository supplierRepository;
    private final SupplierContactRepository supplierContactRepository;
    private final SupplierAddressRepository supplierAddressRepository;
    private final SupplierBankAccountRepository supplierBankAccountRepository;
    private final SupplierInvoiceProfileRepository supplierInvoiceProfileRepository;
    private final SupplierListQueryAdapter supplierListQueryAdapter;

    public SupplierQueryAppServiceImpl(
        SupplierRepository supplierRepository,
        SupplierContactRepository supplierContactRepository,
        SupplierAddressRepository supplierAddressRepository,
        SupplierBankAccountRepository supplierBankAccountRepository,
        SupplierInvoiceProfileRepository supplierInvoiceProfileRepository
    ) {
        this.supplierRepository = supplierRepository;
        this.supplierContactRepository = supplierContactRepository;
        this.supplierAddressRepository = supplierAddressRepository;
        this.supplierBankAccountRepository = supplierBankAccountRepository;
        this.supplierInvoiceProfileRepository = supplierInvoiceProfileRepository;
        this.supplierListQueryAdapter = new SupplierListQueryAdapter();
    }

    @Override
    public ListBaseVO<SupplierListItemVO> list(SupplierListDTO dto) {
        Map<String, Object> conditionMap = supplierListQueryAdapter.toConditionMap(dto);
        List<Supplier> list = supplierRepository.findByCondition(conditionMap);
        Long total = supplierRepository.count(conditionMap);
        int pageNum = dto.getPageNum() == null || dto.getPageNum() < 1 ? 1 : dto.getPageNum();
        int pageSize = dto.getPageSize() == null || dto.getPageSize() < 1 ? Math.max((total == null ? 0 : total.intValue()), 1) : dto.getPageSize();
        int pageCount = Math.max(((total == null ? 0 : total.intValue()) + pageSize - 1) / pageSize, 1);
        ListBaseVO<SupplierListItemVO> vo = new ListBaseVO<>();
        vo.setList(list.stream().map(SupplierAdminAssembler::toListItemVO).toList());
        vo.setPageHelper(new ListBaseVO.PageHelper(pageNum, pageCount));
        return vo;
    }

    @Override
    public SaveItemVO<SupplierSaveItemVO> addItem(BaseDTO dto) {
        SaveItemVO<SupplierSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(buildSaveHeadList(dto.getCorpid()));
        vo.setData(SupplierAdminAssembler.buildEmptySaveItemVO());
        return vo;
    }

    @Override
    public SaveItemVO<SupplierSaveItemVO> updateItem(IdBaseDTO dto) {
        SaveItemVO<SupplierSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(buildSaveHeadList(dto.getCorpid()));
        vo.setData(loadSaveItem(dto));
        return vo;
    }

    @Override
    public SupplierDetailVO detail(IdBaseDTO dto) {
        return SupplierAdminAssembler.toDetailVO(loadSaveItem(dto));
    }

    @Override
    public List<SupplierBusinessSelectOptionVO> businessSelectQuickSearch(SupplierBusinessSelectQueryDTO dto) {
        return findSupplierOptions(dto);
    }

    @Override
    public ListBaseVO<SupplierBusinessSelectOptionVO> businessSelectDialogSearch(SupplierBusinessSelectQueryDTO dto) {
        int pageNum = dto.getPageNum() == null || dto.getPageNum() < 1 ? 1 : dto.getPageNum();
        int pageSize = dto.getPageSize() == null || dto.getPageSize() < 1 ? 20 : dto.getPageSize();
        List<SupplierBusinessSelectOptionVO> all = findSupplierOptions(dto);
        int fromIndex = Math.min((pageNum - 1) * pageSize, all.size());
        int toIndex = Math.min(fromIndex + pageSize, all.size());
        ListBaseVO<SupplierBusinessSelectOptionVO> vo = new ListBaseVO<>();
        vo.setList(all.subList(fromIndex, toIndex));
        vo.setPageHelper(new ListBaseVO.PageHelper(pageNum, Math.max((all.size() + pageSize - 1) / pageSize, 1)));
        return vo;
    }

    @Override
    public SupplierBusinessSelectOptionVO businessSelectGetById(SupplierBusinessSelectQueryDTO dto) {
        if (dto.getId() == null) {
            throw new BizException("id不能为空");
        }
        Supplier supplier = supplierRepository.findById(dto.getCorpid(), dto.getId());
        if (supplier == null) {
            return null;
        }
        return toBusinessSelectOption(supplier);
    }

    private SupplierSaveItemVO loadSaveItem(IdBaseDTO dto) {
        Map<String, Object> childConditionMap = new HashMap<>();
        childConditionMap.put("corpid", dto.getCorpid());
        childConditionMap.put("supplierId", dto.getId());
        Supplier supplier = supplierRepository.findById(dto.getCorpid(), dto.getId());
        List<SupplierContact> contacts = supplierContactRepository.findByCondition(childConditionMap);
        List<SupplierAddress> addresses = supplierAddressRepository.findByCondition(childConditionMap);
        List<SupplierBankAccount> bankAccounts = supplierBankAccountRepository.findByCondition(childConditionMap);
        List<SupplierInvoiceProfile> invoiceProfiles = supplierInvoiceProfileRepository.findByCondition(childConditionMap);
        return SupplierAdminAssembler.toSaveItemVO(supplier, contacts, addresses, bankAccounts, invoiceProfiles);
    }

    private List<FieldEntity> buildSaveHeadList(String corpid) {
        return List.of(
            buildTextHead("main.supplierCode", "供应商编码", 1),
            buildTextHead("main.supplierName", "供应商名称", 1),
            buildTextHead("main.supplierShortName", "供应商简称", 0),
            buildCombHead("main.supplierCategory", "供应商分类", 0, SupplierFieldMetadataSupport.supplierCategoryOptions()),
            buildCombHead("main.mainBusinessCategory", "主营业务分类", 0, SupplierFieldMetadataSupport.mainBusinessCategoryOptions()),
            buildUserHead("main.ownerPurchaserId", "归属采购", 0, corpid),
            buildCombHead("main.bizStatus", "业务状态", 0, SupplierFieldMetadataSupport.bizStatusOptions()),
            buildCombHead("main.refStatus", "引用状态", 0, SupplierFieldMetadataSupport.refStatusOptions()),
            buildTextHead("contacts.contactName", "联系人", 0),
            buildTextHead("contacts.mobile", "手机号", 0),
            buildTextHead("addresses.detailAddress", "详细地址", 0),
            buildTextHead("bankAccounts.accountName", "账户名称", 0),
            buildTextHead("invoiceProfiles.invoiceTitle", "发票抬头", 0)
        );
    }

    private FieldEntity buildTextHead(String attr, String attrName, Integer required) {
        FieldEntity field = createBaseHead(attr, attrName, required);
        field.setFieldType(String.valueOf(FieldTypeEnum.TEXT.getType()));
        return field;
    }

    private FieldEntity buildCombHead(String attr, String attrName, Integer required, List<xbb.ai.erp.base.common.filed.FieldItem> itemList) {
        FieldEntity field = createBaseHead(attr, attrName, required);
        field.setFieldType(String.valueOf(FieldTypeEnum.COMB.getType()));
        field.setItemList(itemList);
        return field;
    }

    private FieldEntity buildUserHead(String attr, String attrName, Integer required, String corpid) {
        FieldEntity field = createBaseHead(attr, attrName, required);
        field.setFieldType(String.valueOf(FieldTypeEnum.USER.getType()));
        field.setBusinessSelectConfig(SupplierFieldMetadataSupport.memberSingleSelectConfig(corpid));
        return field;
    }

    private FieldEntity createBaseHead(String attr, String attrName, Integer required) {
        FieldEntity field = new FieldEntity();
        field.setAttr(attr);
        field.setAttrName(attrName);
        field.setRequired(required);
        field.setEditable(1);
        field.setItemList(List.of());
        return field;
    }

    private List<SupplierBusinessSelectOptionVO> findSupplierOptions(SupplierBusinessSelectQueryDTO dto) {
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("corpid", dto.getCorpid());
        if (dto.getKeyword() != null && !dto.getKeyword().isBlank()) {
            conditionMap.put("keyword", dto.getKeyword().trim());
        }
        if (dto.getId() != null) {
            conditionMap.put("id", dto.getId());
        }
        return supplierRepository.findByCondition(conditionMap).stream()
            .filter(item -> matchesKeyword(item, dto.getKeyword()))
            .map(this::toBusinessSelectOption)
            .toList();
    }

    private boolean matchesKeyword(Supplier supplier, String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return true;
        }
        String trimmedKeyword = keyword.trim();
        return contains(supplier.getSupplierCode(), trimmedKeyword) || contains(supplier.getSupplierName(), trimmedKeyword);
    }

    private boolean contains(String source, String keyword) {
        return source != null && source.contains(keyword);
    }

    private SupplierBusinessSelectOptionVO toBusinessSelectOption(Supplier supplier) {
        SupplierBusinessSelectOptionVO option = new SupplierBusinessSelectOptionVO();
        option.setId(supplier.getId());
        option.setCode(supplier.getSupplierCode());
        option.setName(supplier.getSupplierName());
        option.setLabel(buildLabel(supplier));
        return option;
    }

    private String buildLabel(Supplier supplier) {
        if (supplier == null) {
            return null;
        }
        if (supplier.getSupplierCode() == null || supplier.getSupplierCode().isBlank()) {
            return supplier.getSupplierName();
        }
        if (supplier.getSupplierName() == null || supplier.getSupplierName().isBlank()) {
            return supplier.getSupplierCode();
        }
        return supplier.getSupplierCode() + " " + supplier.getSupplierName();
    }
}
