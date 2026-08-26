package xbb.ai.erp.module.system.application.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.system.admin.dto.BusinessConfigCategoryQueryDTO;
import xbb.ai.erp.module.system.admin.dto.BusinessConfigGlobalSaveDTO;
import xbb.ai.erp.module.system.admin.dto.BusinessConfigSaveDTO;
import xbb.ai.erp.module.system.admin.vo.BusinessConfigCategoryVO;
import xbb.ai.erp.module.system.admin.vo.BusinessConfigDetailVO;
import xbb.ai.erp.module.system.admin.vo.BusinessConfigDocumentVO;
import xbb.ai.erp.module.system.admin.vo.BusinessConfigItemVO;
import xbb.ai.erp.module.system.admin.vo.BusinessConfigOptionVO;
import xbb.ai.erp.module.system.application.catalog.BusinessConfigCatalog;
import xbb.ai.erp.module.system.application.service.BusinessConfigService;
import xbb.ai.erp.module.system.contract.ApprovalMode;
import xbb.ai.erp.module.system.contract.BooleanConfigKeyEnum;
import xbb.ai.erp.module.system.contract.BusinessConfigKey;
import xbb.ai.erp.module.system.contract.BusinessConfigQueryApi;
import xbb.ai.erp.module.system.domain.model.BusinessConfig;
import xbb.ai.erp.module.system.domain.repository.BusinessConfigRepository;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BusinessConfigServiceImpl implements BusinessConfigService, BusinessConfigQueryApi {

    private static final String CACHE_PREFIX = "erp:system:business-config:";

    private final BusinessConfigCatalog catalog;
    private final BusinessConfigRepository repository;
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public List<BusinessConfigCategoryVO> catalog() {
        return catalog.categories().stream().map(category -> {
            BusinessConfigCategoryVO vo = new BusinessConfigCategoryVO();
            vo.setCode(category.code());
            vo.setName(category.name());
            return vo;
        }).toList();
    }

    @Override
    public BusinessConfigDetailVO detail(BusinessConfigCategoryQueryDTO dto) {
        BusinessConfigCatalog.Category category = catalog.categories().stream()
            .filter(item -> item.code().equals(dto.getCategoryCode())).findFirst()
            .orElseThrow(() -> new BizException("不支持的系统配置目录"));
        BusinessConfigDetailVO result = new BusinessConfigDetailVO();
        result.setCategoryCode(category.code());
        result.setCategoryName(category.name());
        result.setDocuments(catalog.entries(category.code()).stream()
            .collect(Collectors.groupingBy(entry -> entry.businessCode().getCode(), LinkedHashMap::new, Collectors.toList()))
            .values().stream().map(entries -> toDocument(dto.getCorpid(), entries)).toList());
        return result;
    }

    @Override
    @Transactional
    public BusinessConfigDocumentVO save(BusinessConfigSaveDTO dto) {
        List<BusinessConfigCatalog.Entry> entries = catalog.entriesForBusiness(dto.getBusinessCode());
        if (entries.isEmpty()) {
            throw new BizException("不支持的业务单据配置");
        }
        Map<String, Object> values = dto.getValues() == null ? Map.of() : dto.getValues();
        saveDocument(dto.getCorpid(), dto.getBusinessCode(), entries, values);
        return toDocument(dto.getCorpid(), entries);
    }

    @Override
    @Transactional
    public BusinessConfigDetailVO saveGlobal(BusinessConfigGlobalSaveDTO dto) {
        Set<String> autoApprovalBusinessCodes = dto.getAutoApprovalBusinessCodes() == null ? Set.of()
            : Set.copyOf(dto.getAutoApprovalBusinessCodes());
        Set<String> supportedBusinessCodes = catalog.approvalEntries().stream()
            .map(entry -> entry.businessCode().getCode()).collect(Collectors.toSet());
        if (!supportedBusinessCodes.containsAll(autoApprovalBusinessCodes)) {
            throw new BizException("包含不支持自动审核的业务单据");
        }
        Map<String, List<BusinessConfigCatalog.Entry>> entriesByBusinessCode = catalog.approvalEntries().stream()
            .collect(Collectors.groupingBy(entry -> entry.businessCode().getCode(), LinkedHashMap::new, Collectors.toList()));
        for (Map.Entry<String, List<BusinessConfigCatalog.Entry>> entry : entriesByBusinessCode.entrySet()) {
            Map<String, Object> values = entry.getValue().stream().collect(Collectors.toMap(
                item -> item.key().code(),
                item -> autoApprovalBusinessCodes.contains(item.businessCode().getCode()) ? ApprovalMode.AUTO.name() : ApprovalMode.REQUIRED.name()
            ));
            saveDocument(dto.getCorpid(), entry.getKey(), catalog.entriesForBusiness(entry.getKey()), values);
        }
        BusinessConfigCategoryQueryDTO detailDTO = new BusinessConfigCategoryQueryDTO();
        detailDTO.setCorpid(dto.getCorpid());
        detailDTO.setCategoryCode("GLOBAL");
        return detail(detailDTO);
    }

    private void saveDocument(String corpid, String businessCode, List<BusinessConfigCatalog.Entry> entries,
                              Map<String, Object> values) {
        for (BusinessConfigCatalog.Entry entry : entries) {
            Object value = values.get(entry.key().code());
            if (value != null) {
                parseValue(entry.key(), value);
            }
        }
        Map<String, Object> effectiveValues = new HashMap<>(load(corpid, businessCode));
        effectiveValues.putAll(values);
        Map<String, Object> overrides = entries.stream().filter(entry -> effectiveValues.containsKey(entry.key().code()))
            .filter(entry -> !parseValue(entry.key(), effectiveValues.get(entry.key().code())).equals(entry.key().defaultValue()))
            .collect(Collectors.toMap(entry -> entry.key().code(), entry -> effectiveValues.get(entry.key().code())));
        if (overrides.isEmpty()) {
            repository.remove(corpid, businessCode);
        } else {
            repository.save(new BusinessConfig(corpid, businessCode, write(overrides)));
        }
        evict(corpid, businessCode);
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                evict(corpid, businessCode);
            }
        });
    }

    @Override
    public <T> T get(String corpid, BusinessConfigKey<T> configKey) {
        Object value = load(corpid, configKey.businessCode().getCode()).get(configKey.code());
        return value == null ? configKey.defaultValue() : parseValue(configKey, value);
    }

    private BusinessConfigDocumentVO toDocument(String corpid, List<BusinessConfigCatalog.Entry> entries) {
        BusinessConfigDocumentVO document = new BusinessConfigDocumentVO();
        BusinessConfigCatalog.Entry first = entries.getFirst();
        document.setCategoryCode(first.categoryCode());
        document.setCategoryName(first.categoryName());
        document.setBusinessCode(first.businessCode().getCode());
        document.setBusinessName(first.businessName());
        document.setItems(entries.stream().map(entry -> toItem(corpid, entry)).toList());
        return document;
    }

    private BusinessConfigItemVO toItem(String corpid, BusinessConfigCatalog.Entry entry) {
        BusinessConfigItemVO item = new BusinessConfigItemVO();
        item.setCode(entry.key().code());
        if (entry.key() instanceof BooleanConfigKeyEnum) {
            boolean purchaseInvoicePayable = BooleanConfigKeyEnum.PURCHASE_INVOICE_AUTO_CREATE_PAYABLE == entry.key();
            item.setTitle(purchaseInvoicePayable ? "过账后自动生成应付" : "过账后自动生成应收");
            item.setHelpText(purchaseInvoicePayable
                ? "开启后，采购发票过账成功时自动创建对应的应付开放项。"
                : "开启后，销售发票过账成功时自动创建对应的应收开放项。");
            item.setControlType("RADIO");
            item.setDefaultValue(entry.key().defaultValue().toString());
            item.setValue(get(corpid, entry.key()).toString());
            item.setOptions(List.of(option("true", "开启", purchaseInvoicePayable ? "过账成功后自动生成应付开放项。" : "过账成功后自动生成应收开放项。"),
                option("false", "关闭", purchaseInvoicePayable ? "过账成功后不自动生成应付开放项。" : "过账成功后不自动生成应收开放项。")));
            return item;
        }
        item.setTitle("审核方式");
        item.setHelpText("决定单据提交后是否必须经过人工审核。");
        item.setControlType("RADIO");
        item.setDefaultValue(entry.key().defaultValue().toString());
        item.setValue(get(corpid, entry.key()).toString());
        item.setOptions(List.of(option("REQUIRED", "必须审核", "提交后进入待审核状态。"), option("AUTO", "自动审核", "提交后直接视为已审核。")));
        return item;
    }

    private BusinessConfigOptionVO option(String value, String label, String helpText) {
        BusinessConfigOptionVO option = new BusinessConfigOptionVO();
        option.setValue(value);
        option.setLabel(label);
        option.setHelpText(helpText);
        return option;
    }

    private Map<String, Object> load(String corpid, String businessCode) {
        String cacheKey = CACHE_PREFIX + corpid + ":" + businessCode;
        try {
            String json = stringRedisTemplate.opsForValue().get(cacheKey);
            if (json != null) {
                return read(json);
            }
            BusinessConfig config = repository.findByKey(corpid, businessCode);
            Map<String, Object> values = config == null ? Map.of() : read(config.configJson());
            stringRedisTemplate.opsForValue().set(cacheKey, write(values), 24, TimeUnit.HOURS);
            return values;
        } catch (Exception exception) {
            BusinessConfig config = repository.findByKey(corpid, businessCode);
            return config == null ? Map.of() : read(config.configJson());
        }
    }

    private void evict(String corpid, String businessCode) {
        try {
            stringRedisTemplate.delete(CACHE_PREFIX + corpid + ":" + businessCode);
        } catch (Exception ignored) {
        }
    }

    private Map<String, Object> read(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<>() { });
        } catch (Exception exception) {
            throw new BizException("系统配置数据格式错误");
        }
    }

    private String write(Map<String, Object> values) {
        try {
            return objectMapper.writeValueAsString(values);
        } catch (Exception exception) {
            throw new BizException("系统配置数据序列化失败");
        }
    }

    private <T> T parseValue(BusinessConfigKey<T> key, Object value) {
        if (key.valueType() == ApprovalMode.class && value instanceof String stringValue) {
            try {
                return key.valueType().cast(ApprovalMode.valueOf(stringValue));
            } catch (IllegalArgumentException exception) {
                throw new BizException("系统配置值不合法");
            }
        }
        if (key.valueType() == Boolean.class && value instanceof String stringValue
            && ("true".equals(stringValue) || "false".equals(stringValue))) {
            return key.valueType().cast(Boolean.valueOf(stringValue));
        }
        if (key.valueType() == Boolean.class && value instanceof Boolean booleanValue) {
            return key.valueType().cast(booleanValue);
        }
        throw new BizException("系统配置值类型不合法");
    }
}
