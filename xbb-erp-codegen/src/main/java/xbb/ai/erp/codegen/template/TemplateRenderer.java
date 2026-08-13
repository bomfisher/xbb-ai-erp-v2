package xbb.ai.erp.codegen.template;

import xbb.ai.erp.codegen.generator.DddGenerationContext;
import xbb.ai.erp.codegen.spec.FieldSpec;
import xbb.ai.erp.codegen.spec.ModuleSpec;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TemplateRenderer {

    public String render(TemplateType templateType, DddGenerationContext context) {
        return switch (templateType) {
            case ADMIN_CONTROLLER -> renderAdminController(context.moduleSpec());
            case ADMIN_LIST_DTO -> renderListDTO(context.moduleSpec());
            case ADMIN_MAIN_DTO -> renderMainDTO(context.moduleSpec());
            case ADMIN_SAVE_DTO -> renderSaveDTO(context.moduleSpec());
            case ADMIN_SUBMIT_SAVE_DTO -> renderSubmitSaveDTO(context.moduleSpec());
            case ADMIN_DRAFT_SAVE_DTO -> renderDraftSaveDTO(context.moduleSpec());
            case ADMIN_DRAFT_META_DTO -> renderDraftMetaDTO(context.moduleSpec());
            case ADMIN_DRAFT_LIST_DTO -> renderDraftListDTO(context.moduleSpec());
            case ADMIN_DRAFT_LOAD_DTO -> renderDraftLoadDTO(context.moduleSpec());
            case ADMIN_LIST_ITEM_VO -> renderListItemVO(context.moduleSpec());
            case ADMIN_SAVE_ITEM_VO -> renderSaveItemVO(context.moduleSpec());
            case ADMIN_DETAIL_VO -> renderDetailVO(context.moduleSpec());
            case ADMIN_DRAFT_SAVE_VO -> renderDraftSaveVO(context.moduleSpec());
            case ADMIN_DRAFT_LIST_ITEM_VO -> renderDraftListItemVO(context.moduleSpec());
            case ADMIN_DRAFT_DETAIL_VO -> renderDraftDetailVO(context.moduleSpec());
            case APP_SERVICE -> renderAppService(context.moduleSpec());
            case APP_SERVICE_IMPL -> renderAppServiceImpl(context.moduleSpec());
            case APP_QUERY_SERVICE_IMPL -> renderQueryAppServiceImpl(context.moduleSpec());
            case APP_SAVE_SERVICE_IMPL -> renderSaveAppServiceImpl(context.moduleSpec());
            case APP_DRAFT_SERVICE -> renderDraftAppService(context.moduleSpec());
            case APP_DRAFT_SERVICE_IMPL -> renderDraftAppServiceImpl(context.moduleSpec());
            case APP_DRAFT_REPOSITORY -> renderDraftRepository(context.moduleSpec());
            case APP_DRAFT_POJO -> renderDraftPojo(context.moduleSpec());
            case APP_SAVE_PROTOCOL_VALIDATOR -> renderSaveProtocolValidator(context.moduleSpec());
            case APP_SAVE_COMMON_VALIDATOR -> renderSaveCommonValidator(context.moduleSpec());
            case APP_SAVE_BUSINESS_VALIDATOR -> renderSaveBusinessValidator(context.moduleSpec());
            case APP_ASSEMBLER -> renderAdminAssembler(context.moduleSpec());
            case APP_VALIDATOR -> renderValidator(context.moduleSpec());
            case APP_QUERY_POJO -> renderApplicationQueryPojo(context.moduleSpec());
            case APP_LIST_META_PROVIDER -> renderListMetaProvider(context.moduleSpec());
            case DOMAIN_MODEL -> renderDomainModel(context.moduleSpec());
            case DOMAIN_REPOSITORY -> renderRepository(context.moduleSpec());
            case DOMAIN_QUERY_POJO -> renderDomainQueryPojo(context.moduleSpec());
            case PERSISTENCE_PO -> renderPO(context.moduleSpec());
            case PERSISTENCE_MAPPER -> renderMapper(context.moduleSpec());
            case PERSISTENCE_CONVERTOR -> renderConvertor(context.moduleSpec());
            case PERSISTENCE_REPOSITORY_IMPL -> renderRepositoryImpl(context.moduleSpec());
            case PERSISTENCE_DRAFT_REPOSITORY_IMPL -> renderDraftRepositoryImpl(context.moduleSpec());
            case PERSISTENCE_CONDITION_MAP_HELPER -> renderConditionMapHelper(context.moduleSpec());
            case MAPPER_XML -> renderMapperXml(context.moduleSpec());
        };
    }

    public String renderDomainModel(ModuleSpec moduleSpec) {
        String packageName = moduleSpec.getPackageBase() + ".domain.model";
        String aggregateName = aggregateName(moduleSpec);
        return "package " + packageName + ";\n\n"
            + "import lombok.Data;\n\n"
            + "@Data\n"
            + "public class " + aggregateName + " {\n"
            + renderFields(moduleSpec.getAggregate().getFields(), false)
            + "}\n";
    }

    public String renderRepository(ModuleSpec moduleSpec) {
        String packageName = moduleSpec.getPackageBase() + ".domain.repository";
        String aggregateName = aggregateName(moduleSpec);
        String variableName = lowerCamel(aggregateName);
        return "package " + packageName + ";\n\n"
            + "import " + moduleSpec.getPackageBase() + ".domain.model." + aggregateName + ";\n\n"
            + "import java.util.List;\n"
            + "import java.util.Map;\n\n"
            + "public interface " + aggregateName + "Repository {\n"
            + "    Long insert(" + aggregateName + " " + variableName + ");\n\n"
            + "    void insertBatch(List<" + aggregateName + "> " + variableName + "List);\n\n"
            + "    void removeById(String corpid, Long id);\n\n"
            + "    void removeBatchByIds(String corpid, List<Long> ids);\n\n"
            + "    void update(" + aggregateName + " " + variableName + ");\n\n"
            + "    " + aggregateName + " findById(String corpid, Long id);\n\n"
            + "    List<" + aggregateName + "> findByCondition(Map<String, Object> conditionMap);\n\n"
            + "    Long count(Map<String, Object> conditionMap);\n"
            + "}\n";
    }

    public String renderPO(ModuleSpec moduleSpec) {
        String packageName = moduleSpec.getPackageBase() + ".infrastructure.persistence.po";
        String aggregateName = aggregateName(moduleSpec);
        return "package " + packageName + ";\n\n"
            + "import com.baomidou.mybatisplus.annotation.TableName;\n"
            + "import lombok.Data;\n"
            + "import lombok.EqualsAndHashCode;\n"
            + "import xbb.ai.erp.base.persistence.entity.BaseEntity;\n\n"
            + "@Data\n"
            + "@EqualsAndHashCode(callSuper = true)\n"
            + "@TableName(\"" + moduleSpec.getAggregate().getTableName() + "\")\n"
            + "public class " + aggregateName + "PO extends BaseEntity {\n"
            + renderFields(filterBaseEntityFields(moduleSpec.getAggregate().getFields()), true)
            + "}\n";
    }

    public String renderMapper(ModuleSpec moduleSpec) {
        String packageName = moduleSpec.getPackageBase() + ".infrastructure.persistence.mapper";
        String aggregateName = aggregateName(moduleSpec);
        return "package " + packageName + ";\n\n"
            + "import org.apache.ibatis.annotations.Mapper;\n"
            + "import org.apache.ibatis.annotations.Param;\n"
            + "import " + moduleSpec.getPackageBase() + ".infrastructure.persistence.po." + aggregateName + "PO;\n\n"
            + "import java.util.List;\n"
            + "import java.util.Map;\n\n"
            + "@Mapper\n"
            + "public interface " + aggregateName + "Mapper {\n"
            + "    int insert(" + aggregateName + "PO po);\n\n"
            + "    int insertBatch(@Param(\"list\") List<" + aggregateName + "PO> poList);\n\n"
            + "    int removeById(@Param(\"corpid\") String corpid, @Param(\"id\") Long id);\n\n"
            + "    int removeBatchByIds(@Param(\"corpid\") String corpid, @Param(\"ids\") List<Long> ids);\n\n"
            + "    int update(" + aggregateName + "PO po);\n\n"
            + "    " + aggregateName + "PO findById(@Param(\"corpid\") String corpid, @Param(\"id\") Long id);\n\n"
            + "    List<" + aggregateName + "PO> findByCondition(@Param(\"conditionMap\") Map<String, Object> conditionMap);\n\n"
            + "    Long count(@Param(\"conditionMap\") Map<String, Object> conditionMap);\n"
            + "}\n";
    }

    public String renderConvertor(ModuleSpec moduleSpec) {
        String aggregateName = aggregateName(moduleSpec);
        String packageName = moduleSpec.getPackageBase() + ".infrastructure.persistence.convertor";
        String poType = aggregateName + "PO";
        String variableName = lowerCamel(aggregateName);
        return "package " + packageName + ";\n\n"
            + "import " + moduleSpec.getPackageBase() + ".domain.model." + aggregateName + ";\n"
            + "import " + moduleSpec.getPackageBase() + ".infrastructure.persistence.po." + poType + ";\n\n"
            + "public final class " + aggregateName + "Convertor {\n\n"
            + "    private " + aggregateName + "Convertor() {\n"
            + "    }\n\n"
            + "    public static " + poType + " toPO(" + aggregateName + " " + variableName + ") {\n"
            + "        if (" + variableName + " == null) {\n"
            + "            return null;\n"
            + "        }\n"
            + "        " + poType + " po = new " + poType + "();\n"
            + copyFieldAssignments(moduleSpec.getAggregate().getFields(), variableName, "po")
            + "        return po;\n"
            + "    }\n\n"
            + "    public static " + aggregateName + " toDomain(" + poType + " po) {\n"
            + "        if (po == null) {\n"
            + "            return null;\n"
            + "        }\n"
            + "        " + aggregateName + " " + variableName + " = new " + aggregateName + "();\n"
            + copyFieldAssignments(moduleSpec.getAggregate().getFields(), "po", variableName)
            + "        return " + variableName + ";\n"
            + "    }\n"
            + "}\n";
    }

    public String renderRepositoryImpl(ModuleSpec moduleSpec) {
        String aggregateName = aggregateName(moduleSpec);
        String variableName = lowerCamel(aggregateName);
        String packageName = moduleSpec.getPackageBase() + ".infrastructure.persistence.repository";
        return "package " + packageName + ";\n\n"
            + "import lombok.RequiredArgsConstructor;\n"
            + "import org.springframework.stereotype.Repository;\n"
            + "import xbb.ai.erp.base.persistence.entity.BaseEntity;\n"
            + "import " + moduleSpec.getPackageBase() + ".domain.model." + aggregateName + ";\n"
            + "import " + moduleSpec.getPackageBase() + ".domain.repository." + aggregateName + "Repository;\n"
            + "import " + moduleSpec.getPackageBase() + ".infrastructure.persistence.convertor." + aggregateName + "Convertor;\n"
            + "import " + moduleSpec.getPackageBase() + ".infrastructure.persistence.mapper." + aggregateName + "Mapper;\n"
            + "import " + moduleSpec.getPackageBase() + ".infrastructure.persistence.po." + aggregateName + "PO;\n\n"
            + "import java.util.List;\n"
            + "import java.util.Map;\n\n"
            + "@Repository(\"" + repositoryBeanName(moduleSpec, aggregateName) + "\")\n"
            + "@RequiredArgsConstructor\n"
            + "public class " + aggregateName + "RepositoryImpl implements " + aggregateName + "Repository {\n\n"
            + "    private final " + aggregateName + "Mapper " + variableName + "Mapper;\n\n"
            + "    @Override\n"
            + "    public Long insert(" + aggregateName + " " + variableName + ") {\n"
            + "        " + aggregateName + "PO po = " + aggregateName + "Convertor.toPO(" + variableName + ");\n"
            + "        initializeForInsert(po);\n"
            + "        " + variableName + "Mapper.insert(po);\n"
            + "        " + variableName + ".setId(po.getId());\n"
            + "        return po.getId();\n"
            + "    }\n\n"
            + "    @Override\n"
            + "    public void insertBatch(List<" + aggregateName + "> " + variableName + "List) {\n"
            + "        List<" + aggregateName + "PO> poList = " + variableName + "List.stream().map(" + aggregateName + "Convertor::toPO).toList();\n"
            + "        poList.forEach(this::initializeForInsert);\n"
            + "        " + variableName + "Mapper.insertBatch(poList);\n"
            + "        for (int index = 0; index < " + variableName + "List.size(); index++) {\n"
            + "            " + variableName + "List.get(index).setId(poList.get(index).getId());\n"
            + "        }\n"
            + "    }\n\n"
            + "    @Override\n"
            + "    public void removeById(String corpid, Long id) {\n"
            + "        " + variableName + "Mapper.removeById(corpid, id);\n"
            + "    }\n\n"
            + "    @Override\n"
            + "    public void removeBatchByIds(String corpid, List<Long> ids) {\n"
            + "        " + variableName + "Mapper.removeBatchByIds(corpid, ids);\n"
            + "    }\n\n"
            + "    @Override\n"
            + "    public void update(" + aggregateName + " " + variableName + ") {\n"
            + "        " + aggregateName + "PO po = " + aggregateName + "Convertor.toPO(" + variableName + ");\n"
            + "        " + variableName + "Mapper.update(po);\n"
            + "    }\n\n"
            + "    @Override\n"
            + "    public " + aggregateName + " findById(String corpid, Long id) {\n"
            + "        return " + aggregateName + "Convertor.toDomain(" + variableName + "Mapper.findById(corpid, id));\n"
            + "    }\n\n"
            + "    @Override\n"
            + "    public List<" + aggregateName + "> findByCondition(Map<String, Object> conditionMap) {\n"
            + "        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);\n"
            + "        return " + variableName + "Mapper.findByCondition(preparedConditionMap).stream().map(" + aggregateName + "Convertor::toDomain).toList();\n"
            + "    }\n\n"
            + "    @Override\n"
            + "    public Long count(Map<String, Object> conditionMap) {\n"
            + "        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);\n"
            + "        return " + variableName + "Mapper.count(preparedConditionMap);\n"
            + "    }\n\n"
            + "    private void initializeForInsert(BaseEntity po) {\n"
            + "        long now = System.currentTimeMillis();\n"
            + "        po.setId(null);\n"
            + "        po.setDel(0);\n"
            + "        po.setAddTime(now);\n"
            + "        po.setUpdateTime(now);\n"
            + "    }\n"
            + "}\n";
    }

    public String renderListDTO(ModuleSpec moduleSpec) {
        String aggregateName = aggregateName(moduleSpec);
        String packageName = moduleSpec.getPackageBase() + ".admin.dto";
        return "package " + packageName + ";\n\n"
            + "import lombok.Data;\n"
            + "import lombok.EqualsAndHashCode;\n"
            + "import xbb.ai.erp.base.common.dto.BaseDTO;\n"
            + "import xbb.ai.erp.base.common.pojo.ListFilterCondition;\n\n"
            + "import java.util.List;\n\n"
            + "@Data\n"
            + "@EqualsAndHashCode(callSuper = true)\n"
            + "public class " + aggregateName + "ListDTO extends BaseDTO {\n"
            + renderQueryableFields(moduleSpec.getAggregate().getFields())
            + "    private Integer pageNum;\n"
            + "    private Integer pageSize;\n"
            + "    private Integer offset;\n"
            + "    private String groupByStr;\n"
            + "    private String orderByStr;\n"
            + "    private List<ListFilterCondition> conditions;\n"
            + "}\n";
    }

    public String renderMainDTO(ModuleSpec moduleSpec) {
        String aggregateName = aggregateName(moduleSpec);
        String packageName = moduleSpec.getPackageBase() + ".admin.dto";
        return "package " + packageName + ";\n\n"
            + "import lombok.Data;\n\n"
            + "@Data\n"
            + "public class " + aggregateName + "MainDTO {\n"
            + renderFields(moduleSpec.getAggregate().getFields(), false)
            + "}\n";
    }

    public String renderSaveDTO(ModuleSpec moduleSpec) {
        String aggregateName = aggregateName(moduleSpec);
        String packageName = moduleSpec.getPackageBase() + ".admin.dto";
        return "package " + packageName + ";\n\n"
            + "import lombok.Data;\n"
            + "import lombok.EqualsAndHashCode;\n"
            + "import xbb.ai.erp.base.common.dto.BaseDTO;\n\n"
            + "@Data\n"
            + "@EqualsAndHashCode(callSuper = true)\n"
            + "public class " + aggregateName + "SaveDTO extends BaseDTO {\n"
            + "    private " + aggregateName + "MainDTO main;\n"
            + "}\n";
    }

    public String renderSubmitSaveDTO(ModuleSpec moduleSpec) {
        String aggregateName = aggregateName(moduleSpec);
        String packageName = moduleSpec.getPackageBase() + ".admin.dto";
        return "package " + packageName + ";\n\n"
            + "import lombok.Data;\n"
            + "import lombok.EqualsAndHashCode;\n\n"
            + "@Data\n"
            + "@EqualsAndHashCode(callSuper = true)\n"
            + "public class " + aggregateName + "SubmitSaveDTO extends " + aggregateName + "SaveDTO {\n"
            + "    private " + aggregateName + "DraftMetaDTO draftMeta = new " + aggregateName + "DraftMetaDTO();\n"
            + "}\n";
    }

    public String renderDraftSaveDTO(ModuleSpec moduleSpec) {
        String aggregateName = aggregateName(moduleSpec);
        String packageName = moduleSpec.getPackageBase() + ".admin.dto";
        return "package " + packageName + ";\n\n"
            + "import lombok.Data;\n"
            + "import lombok.EqualsAndHashCode;\n\n"
            + "@Data\n"
            + "@EqualsAndHashCode(callSuper = true)\n"
            + "public class " + aggregateName + "DraftSaveDTO extends " + aggregateName + "SaveDTO {\n"
            + "    private " + aggregateName + "DraftMetaDTO draftMeta = new " + aggregateName + "DraftMetaDTO();\n"
            + "}\n";
    }

    public String renderDraftMetaDTO(ModuleSpec moduleSpec) {
        String aggregateName = aggregateName(moduleSpec);
        String packageName = moduleSpec.getPackageBase() + ".admin.dto";
        return "package " + packageName + ";\n\n"
            + "import lombok.Data;\n\n"
            + "@Data\n"
            + "public class " + aggregateName + "DraftMetaDTO {\n"
            + "    private String draftCode;\n"
            + "    private String draftTitle;\n"
            + "}\n";
    }

    public String renderDraftListDTO(ModuleSpec moduleSpec) {
        return renderBaseDTO(moduleSpec, "DraftListDTO", "");
    }

    public String renderDraftLoadDTO(ModuleSpec moduleSpec) {
        return renderBaseDTO(moduleSpec, "DraftLoadDTO", "    private String draftCode;\n");
    }

    private String renderBaseDTO(ModuleSpec moduleSpec, String suffix, String fields) {
        String aggregateName = aggregateName(moduleSpec);
        String packageName = moduleSpec.getPackageBase() + ".admin.dto";
        return "package " + packageName + ";\n\n"
            + "import lombok.Data;\n"
            + "import lombok.EqualsAndHashCode;\n"
            + "import xbb.ai.erp.base.common.dto.BaseDTO;\n\n"
            + "@Data\n"
            + "@EqualsAndHashCode(callSuper = true)\n"
            + "public class " + aggregateName + suffix + " extends BaseDTO {\n"
            + fields
            + "}\n";
    }

    public String renderListItemVO(ModuleSpec moduleSpec) {
        String aggregateName = aggregateName(moduleSpec);
        String packageName = moduleSpec.getPackageBase() + ".admin.vo";
        return "package " + packageName + ";\n\n"
            + "import lombok.Data;\n\n"
            + "@Data\n"
            + "public class " + aggregateName + "ListItemVO {\n"
            + renderVisibleListFields(moduleSpec.getAggregate().getFields())
            + "}\n";
    }

    public String renderSaveItemVO(ModuleSpec moduleSpec) {
        String aggregateName = aggregateName(moduleSpec);
        String packageName = moduleSpec.getPackageBase() + ".admin.vo";
        return "package " + packageName + ";\n\n"
            + "import lombok.Data;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.dto." + aggregateName + "MainDTO;\n\n"
            + "@Data\n"
            + "public class " + aggregateName + "SaveItemVO {\n"
            + "    private " + aggregateName + "MainDTO main;\n"
            + "}\n";
    }

    public String renderDetailVO(ModuleSpec moduleSpec) {
        String aggregateName = aggregateName(moduleSpec);
        String packageName = moduleSpec.getPackageBase() + ".admin.vo";
        return "package " + packageName + ";\n\n"
            + "import lombok.Data;\n\n"
            + "@Data\n"
            + "public class " + aggregateName + "DetailVO {\n"
            + "    private " + aggregateName + "SaveItemVO mainData;\n"
            + "}\n";
    }

    public String renderDraftSaveVO(ModuleSpec moduleSpec) {
        return renderDraftFieldVO(moduleSpec, "DraftSaveVO", "    private String draftCode;\n");
    }

    public String renderDraftListItemVO(ModuleSpec moduleSpec) {
        return renderDraftFieldVO(moduleSpec, "DraftListItemVO", "    private String draftCode;\n    private String draftTitle;\n");
    }

    public String renderDraftDetailVO(ModuleSpec moduleSpec) {
        String aggregateName = aggregateName(moduleSpec);
        String packageName = moduleSpec.getPackageBase() + ".admin.vo";
        return "package " + packageName + ";\n\n"
            + "import lombok.Data;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.dto." + aggregateName + "MainDTO;\n\n"
            + "@Data\n"
            + "public class " + aggregateName + "DraftDetailVO {\n"
            + "    private String draftCode;\n"
            + "    private " + aggregateName + "MainDTO main;\n"
            + "}\n";
    }

    private String renderDraftFieldVO(ModuleSpec moduleSpec, String suffix, String fields) {
        String aggregateName = aggregateName(moduleSpec);
        String packageName = moduleSpec.getPackageBase() + ".admin.vo";
        return "package " + packageName + ";\n\n"
            + "import lombok.Data;\n\n"
            + "@Data\n"
            + "public class " + aggregateName + suffix + " {\n"
            + fields
            + "}\n";
    }

    public String renderQueryAppServiceImpl(ModuleSpec moduleSpec) {
        String aggregateName = aggregateName(moduleSpec);
        String variableName = lowerCamel(aggregateName);
        String packageName = moduleSpec.getPackageBase() + ".application.service.query";
        String businessCode = businessCode(moduleSpec);
        return "package " + packageName + ";\n\n"
            + "import lombok.RequiredArgsConstructor;\n"
            + "import org.springframework.stereotype.Service;\n"
            + "import org.springframework.transaction.annotation.Transactional;\n"
            + "import xbb.ai.erp.base.common.vo.BaseVO;\n"
            + "import xbb.ai.erp.base.common.dto.BaseDTO;\n"
            + "import xbb.ai.erp.base.common.dto.IdBaseDTO;\n"
            + "import xbb.ai.erp.base.common.module.BusinessCodeEnum;\n"
            + "import xbb.ai.erp.base.common.support.AdminParamValidator;\n"
            + "import xbb.ai.erp.base.common.vo.ListBaseVO;\n"
            + "import xbb.ai.erp.base.common.vo.SaveItemVO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.dto." + aggregateName + "ListDTO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.vo." + aggregateName + "DetailVO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.vo." + aggregateName + "ListItemVO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.vo." + aggregateName + "SaveItemVO;\n"
            + "import " + moduleSpec.getPackageBase() + ".application.assembler." + aggregateName + "AdminAssembler;\n"
            + "import xbb.ai.erp.module.common.application.render.ListValueRenderer;\n"
            + "import " + moduleSpec.getPackageBase() + ".domain.model." + aggregateName + ";\n"
            + "import " + moduleSpec.getPackageBase() + ".domain.repository." + aggregateName + "Repository;\n\n"
            + "import java.util.HashMap;\n"
            + "import java.util.List;\n"
            + "import java.util.Map;\n\n"
            + "@Service\n"
            + "public class " + aggregateName + "QueryAppServiceImpl {\n\n"
            + "    private final " + aggregateName + "Repository " + variableName + "Repository;\n"
            + "    private final ListValueRenderer listValueRenderer;\n\n"
            + "    public " + aggregateName + "QueryAppServiceImpl(" + aggregateName + "Repository " + variableName + "Repository, ListValueRenderer listValueRenderer) {\n"
            + "        this." + variableName + "Repository = " + variableName + "Repository;\n"
            + "        this.listValueRenderer = listValueRenderer;\n"
            + "    }\n\n"
            + "    public ListBaseVO<" + aggregateName + "ListItemVO> list(" + aggregateName + "ListDTO dto) {\n"
            + "        AdminParamValidator.requireCorpid(dto);\n"
            + "        Map<String, Object> conditionMap = new HashMap<>();\n"
            + "        conditionMap.put(\"corpid\", dto.getCorpid());\n"
            + renderConditionMapLines(moduleSpec.getAggregate().getFields())
            + "        conditionMap.put(\"pageNum\", dto.getPageNum());\n"
            + "        conditionMap.put(\"pageSize\", dto.getPageSize());\n"
            + "        conditionMap.put(\"offset\", dto.getOffset());\n"
            + "        conditionMap.put(\"conditions\", dto.getConditions());\n"
            + "        List<" + aggregateName + "> list = " + variableName + "Repository.findByCondition(conditionMap);\n"
            + "        Long total = " + variableName + "Repository.count(conditionMap);\n"
            + "        ListBaseVO<" + aggregateName + "ListItemVO> vo = new ListBaseVO<>();\n"
            + "        List<" + aggregateName + "ListItemVO> items = list.stream().map(" + aggregateName + "AdminAssembler::toListItemVO).toList();\n"
            + "        vo.setList(listValueRenderer.render(dto.getCorpid(), BusinessCodeEnum." + businessCode + ".getCode(), items));\n"
            + "        vo.setPageHelper(new ListBaseVO.PageHelper(dto.getPageNum() == null ? 1 : dto.getPageNum(), total == null ? 0 : total.intValue()));\n"
            + "        return vo;\n"
            + "    }\n\n"
            + "    public SaveItemVO<" + aggregateName + "SaveItemVO> addItem(BaseDTO dto) {\n"
            + "        SaveItemVO<" + aggregateName + "SaveItemVO> vo = new SaveItemVO<>();\n"
            + "        vo.setData(" + aggregateName + "AdminAssembler.buildEmptySaveItemVO());\n"
            + "        return vo;\n"
            + "    }\n\n"
            + "    public SaveItemVO<" + aggregateName + "SaveItemVO> updateItem(IdBaseDTO dto) {\n"
            + "        AdminParamValidator.validateIdQuery(dto);\n"
            + "        " + aggregateName + " entity = " + variableName + "Repository.findById(dto.getCorpid(), dto.getId());\n"
            + "        SaveItemVO<" + aggregateName + "SaveItemVO> vo = new SaveItemVO<>();\n"
            + "        vo.setData(" + aggregateName + "AdminAssembler.toSaveItemVO(entity));\n"
            + "        return vo;\n"
            + "    }\n\n"
            + "    public " + aggregateName + "DetailVO detail(IdBaseDTO dto) {\n"
            + "        AdminParamValidator.validateIdQuery(dto);\n"
            + "        " + aggregateName + " entity = " + variableName + "Repository.findById(dto.getCorpid(), dto.getId());\n"
            + "        return " + aggregateName + "AdminAssembler.toDetailVO(" + aggregateName + "AdminAssembler.toSaveItemVO(entity));\n"
            + "    }\n"
            + "}\n";
    }

    public String renderSaveAppServiceImpl(ModuleSpec moduleSpec) {
        String aggregateName = aggregateName(moduleSpec);
        String variableName = lowerCamel(aggregateName);
        String packageName = moduleSpec.getPackageBase() + ".application.service.save";
        return "package " + packageName + ";\n\n"
            + "import lombok.RequiredArgsConstructor;\n"
            + "import org.springframework.stereotype.Service;\n"
            + "import xbb.ai.erp.base.common.dto.BatchBaseDTO;\n"
            + "import org.springframework.transaction.annotation.Transactional;\n"
            + "import xbb.ai.erp.base.common.support.AdminParamValidator;\n"
            + "import xbb.ai.erp.base.common.vo.BaseVO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.dto." + aggregateName + "SaveDTO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.dto." + aggregateName + "SubmitSaveDTO;\n"
            + "import " + moduleSpec.getPackageBase() + ".application.assembler." + aggregateName + "AdminAssembler;\n"
            + "import " + moduleSpec.getPackageBase() + ".application.validator." + aggregateName + "Validator;\n"
            + "import " + moduleSpec.getPackageBase() + ".application.port." + aggregateName + "DraftRepository;\n"
            + "import " + moduleSpec.getPackageBase() + ".application.validator." + aggregateName + "SaveProtocolValidator;\n"
            + "import " + moduleSpec.getPackageBase() + ".application.validator." + aggregateName + "SaveCommonValidator;\n"
            + "import " + moduleSpec.getPackageBase() + ".application.validator." + aggregateName + "SaveBusinessValidator;\n"
            + "import " + moduleSpec.getPackageBase() + ".domain.model." + aggregateName + ";\n"
            + "import " + moduleSpec.getPackageBase() + ".domain.repository." + aggregateName + "Repository;\n\n"
            + "@Service\n"
            + "@RequiredArgsConstructor\n"
            + "public class " + aggregateName + "SaveAppServiceImpl {\n\n"
            + "    private final " + aggregateName + "Repository " + variableName + "Repository;\n\n"
            + "    private final " + aggregateName + "DraftRepository draftRepository;\n"
            + "    private final " + aggregateName + "SaveProtocolValidator protocolValidator;\n"
            + "    private final " + aggregateName + "SaveCommonValidator commonValidator;\n"
            + "    private final " + aggregateName + "SaveBusinessValidator businessValidator;\n\n"
            + "    @Transactional\n"
            + "    public BaseVO saveAndSubmit(" + aggregateName + "SubmitSaveDTO dto) {\n"
            + "        protocolValidator.validate(dto);\n"
            + "        commonValidator.validateForSubmit(dto);\n"
            + "        businessValidator.validateForSubmit(dto);\n"
            + "        save(dto);\n"
            + "        if (dto.getDraftMeta() != null && dto.getDraftMeta().getDraftCode() != null) draftRepository.removeDraft(dto.getCorpid(), dto.getDraftMeta().getDraftCode());\n"
            + "        return new BaseVO();\n"
            + "    }\n\n"
            + "    public Long save(" + aggregateName + "SaveDTO dto) {\n"
            + "        AdminParamValidator.requireCorpid(dto);\n"
            + "        " + aggregateName + "Validator.validateSave(dto);\n"
            + "        " + aggregateName + " entity = " + aggregateName + "AdminAssembler.to" + aggregateName + "(dto);\n"
            + "        if (entity.getId() == null) {\n"
            + "            return " + variableName + "Repository.insert(entity);\n"
            + "        }\n"
            + "        " + variableName + "Repository.update(entity);\n"
            + "        return entity.getId();\n"
            + "    }\n\n"
            + "    public void delete(BatchBaseDTO dto) {\n"
            + "        if (dto.getIdList() != null && !dto.getIdList().isEmpty()) {\n"
            + "            " + variableName + "Repository.removeBatchByIds(dto.getCorpid(), dto.getIdList());\n"
            + "        }\n"
            + "    }\n"
            + "}\n";
    }

    public String renderValidator(ModuleSpec moduleSpec) {
        String aggregateName = aggregateName(moduleSpec);
        String packageName = moduleSpec.getPackageBase() + ".application.validator";
        return "package " + packageName + ";\n\n"
            + "import xbb.ai.erp.base.common.exception.BizException;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.dto." + aggregateName + "SaveDTO;\n\n"
            + "public final class " + aggregateName + "Validator {\n\n"
            + "    private " + aggregateName + "Validator() {\n"
            + "    }\n\n"
            + "    public static void validateSave(" + aggregateName + "SaveDTO dto) {\n"
            + "        if (dto == null) {\n"
            + "            throw new BizException(\"save dto不能为空\");\n"
            + "        }\n"
            + "        if (dto.getMain() == null) {\n"
            + "            throw new BizException(\"main不能为空\");\n"
            + "        }\n"
            + "    }\n"
            + "}\n";
    }

    public String renderApplicationQueryPojo(ModuleSpec moduleSpec) {
        return renderQueryPojo(moduleSpec, moduleSpec.getPackageBase() + ".application.pojo");
    }

    public String renderDomainQueryPojo(ModuleSpec moduleSpec) {
        return renderQueryPojo(moduleSpec, moduleSpec.getPackageBase() + ".domain.pojo");
    }

    private String renderQueryPojo(ModuleSpec moduleSpec, String packageName) {
        String aggregateName = aggregateName(moduleSpec);
        return "package " + packageName + ";\n\n"
            + "import lombok.Data;\n\n"
            + "@Data\n"
            + "public class " + aggregateName + "QueryPojo {\n"
            + "    private String corpid;\n"
            + "    private String keyword;\n"
            + "    private Integer pageNum;\n"
            + "    private Integer pageSize;\n"
            + "    private Integer offset;\n"
            + renderQueryableFields(moduleSpec.getAggregate().getFields())
            + "}\n";
    }

    public String renderAppService(ModuleSpec moduleSpec) {
        String aggregateName = aggregateName(moduleSpec);
        String packageName = moduleSpec.getPackageBase() + ".application.service";
        return "package " + packageName + ";\n\n"
            + "import xbb.ai.erp.base.common.dto.BaseDTO;\n"
            + "import xbb.ai.erp.base.common.dto.BatchBaseDTO;\n"
            + "import xbb.ai.erp.base.common.dto.IdBaseDTO;\n"
            + "import xbb.ai.erp.base.common.exception.BizException;\n"
            + "import xbb.ai.erp.base.common.vo.BaseVO;\n"
            + "import xbb.ai.erp.base.common.vo.ListBaseVO;\n"
            + "import xbb.ai.erp.base.common.vo.SaveItemVO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.dto." + aggregateName + "ListDTO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.dto." + aggregateName + "SaveDTO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.dto." + aggregateName + "SubmitSaveDTO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.dto." + aggregateName + "DraftSaveDTO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.dto." + aggregateName + "DraftListDTO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.dto." + aggregateName + "DraftLoadDTO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.vo." + aggregateName + "DetailVO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.vo." + aggregateName + "ListItemVO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.vo." + aggregateName + "SaveItemVO;\n\n"
            + "import xbb.ai.erp.base.common.vo.DraftSaveVO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.vo." + aggregateName + "DraftListItemVO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.vo." + aggregateName + "DraftDetailVO;\n\n"
            + "import java.util.List;\n\n"
            + "public interface " + aggregateName + "AdminAppService {\n"
            + "    ListBaseVO<" + aggregateName + "ListItemVO> list(" + aggregateName + "ListDTO dto);\n\n"
            + "    SaveItemVO<" + aggregateName + "SaveItemVO> addItem(BaseDTO dto);\n\n"
            + "    SaveItemVO<" + aggregateName + "SaveItemVO> updateItem(IdBaseDTO dto);\n\n"
            + "    DraftSaveVO saveDraft(" + aggregateName + "DraftSaveDTO dto);\n\n"
            + "    BaseVO saveAndSubmit(" + aggregateName + "SubmitSaveDTO dto);\n\n"
            + "    List<" + aggregateName + "DraftListItemVO> draftList(" + aggregateName + "DraftListDTO dto);\n\n"
            + "    " + aggregateName + "DraftDetailVO loadDraft(" + aggregateName + "DraftLoadDTO dto);\n\n"
            + "    Long save(" + aggregateName + "SaveDTO dto);\n\n"
            + "    " + aggregateName + "DetailVO detail(IdBaseDTO dto);\n\n"
            + "    void delete(BatchBaseDTO dto);\n"
            + "}\n";
    }

    public String renderAppServiceImpl(ModuleSpec moduleSpec) {
        String aggregateName = aggregateName(moduleSpec);
        String packageName = moduleSpec.getPackageBase() + ".application.service.impl";
        String variableName = lowerCamel(aggregateName);
        return "package " + packageName + ";\n\n"
            + "import lombok.RequiredArgsConstructor;\n"
            + "import org.springframework.stereotype.Service;\n"
            + "import xbb.ai.erp.base.common.exception.BizException;\n"
            + "import xbb.ai.erp.base.common.dto.BaseDTO;\n"
            + "import xbb.ai.erp.base.common.dto.BatchBaseDTO;\n"
            + "import xbb.ai.erp.base.common.dto.IdBaseDTO;\n"
            + "import xbb.ai.erp.base.common.vo.BaseVO;\n"
            + "import xbb.ai.erp.base.common.vo.ListBaseVO;\n"
            + "import xbb.ai.erp.base.common.vo.SaveItemVO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.dto." + aggregateName + "ListDTO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.dto." + aggregateName + "SaveDTO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.dto." + aggregateName + "SubmitSaveDTO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.dto." + aggregateName + "DraftSaveDTO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.dto." + aggregateName + "DraftListDTO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.dto." + aggregateName + "DraftLoadDTO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.vo." + aggregateName + "DetailVO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.vo." + aggregateName + "ListItemVO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.vo." + aggregateName + "SaveItemVO;\n"
            + "import xbb.ai.erp.base.common.vo.DraftSaveVO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.vo." + aggregateName + "DraftListItemVO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.vo." + aggregateName + "DraftDetailVO;\n"
            + "import " + moduleSpec.getPackageBase() + ".application.service." + aggregateName + "AdminAppService;\n"
            + "import " + moduleSpec.getPackageBase() + ".application.service.draft." + aggregateName + "DraftAppService;\n"
            + "import " + moduleSpec.getPackageBase() + ".application.service.query." + aggregateName + "QueryAppServiceImpl;\n"
            + "import " + moduleSpec.getPackageBase() + ".application.service.save." + aggregateName + "SaveAppServiceImpl;\n\n"
            + "import java.util.List;\n"
            + "@Service\n"
            + "@RequiredArgsConstructor\n"
            + "public class " + aggregateName + "AdminAppServiceImpl implements " + aggregateName + "AdminAppService {\n\n"
            + "    private final " + aggregateName + "QueryAppServiceImpl queryService;\n"
            + "    private final " + aggregateName + "SaveAppServiceImpl saveService;\n"
            + "    private final " + aggregateName + "DraftAppService draftService;\n\n"
            + "    @Override\n"
            + "    public ListBaseVO<" + aggregateName + "ListItemVO> list(" + aggregateName + "ListDTO dto) {\n"
            + "        return queryService.list(dto);\n"
            + "    }\n\n"
            + "    @Override\n"
            + "    public SaveItemVO<" + aggregateName + "SaveItemVO> addItem(BaseDTO dto) {\n"
            + "        return queryService.addItem(dto);\n"
            + "    }\n\n"
            + "    @Override\n"
            + "    public SaveItemVO<" + aggregateName + "SaveItemVO> updateItem(IdBaseDTO dto) {\n"
            + "        return queryService.updateItem(dto);\n"
            + "    }\n\n"
            + "    @Override\n"
            + "    public DraftSaveVO saveDraft(" + aggregateName + "DraftSaveDTO dto) {\n"
            + "        return draftService.saveDraft(dto);\n"
            + "    }\n\n"
            + "    @Override\n"
            + "    public BaseVO saveAndSubmit(" + aggregateName + "SubmitSaveDTO dto) {\n"
            + "        return saveService.saveAndSubmit(dto);\n"
            + "    }\n\n"
            + "    @Override\n"
            + "    public List<" + aggregateName + "DraftListItemVO> draftList(" + aggregateName + "DraftListDTO dto) {\n"
            + "        return draftService.draftList(dto);\n"
            + "    }\n\n"
            + "    @Override\n"
            + "    public " + aggregateName + "DraftDetailVO loadDraft(" + aggregateName + "DraftLoadDTO dto) {\n"
            + "        return draftService.loadDraft(dto);\n"
            + "    }\n\n"
            + "    @Override\n"
            + "    public Long save(" + aggregateName + "SaveDTO dto) {\n"
            + "        return saveService.save(dto);\n"
            + "    }\n\n"
            + "    @Override\n"
            + "    public " + aggregateName + "DetailVO detail(IdBaseDTO dto) {\n"
            + "        return queryService.detail(dto);\n"
            + "    }\n\n"
            + "    @Override\n"
            + "    public void delete(BatchBaseDTO dto) {\n"
            + "        saveService.delete(dto);\n"
            + "    }\n"
            + "}\n";
    }

    public String renderDraftAppService(ModuleSpec moduleSpec) {
        String aggregateName = aggregateName(moduleSpec);
        String packageName = moduleSpec.getPackageBase() + ".application.service.draft";
        return "package " + packageName + ";\n\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.dto." + aggregateName + "DraftListDTO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.dto." + aggregateName + "DraftLoadDTO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.dto." + aggregateName + "DraftSaveDTO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.vo." + aggregateName + "DraftDetailVO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.vo." + aggregateName + "DraftListItemVO;\n"
            + "import xbb.ai.erp.base.common.vo.DraftSaveVO;\n\n"
            + "import java.util.List;\n\n"
            + "public interface " + aggregateName + "DraftAppService {\n"
            + "    DraftSaveVO saveDraft(" + aggregateName + "DraftSaveDTO dto);\n\n"
            + "    List<" + aggregateName + "DraftListItemVO> draftList(" + aggregateName + "DraftListDTO dto);\n\n"
            + "    " + aggregateName + "DraftDetailVO loadDraft(" + aggregateName + "DraftLoadDTO dto);\n"
            + "}\n";
    }

    public String renderDraftAppServiceImpl(ModuleSpec moduleSpec) {
        String aggregateName = aggregateName(moduleSpec);
        String packageName = moduleSpec.getPackageBase() + ".application.service.draft";
        return "package " + packageName + ";\n\n"
            + "import org.springframework.stereotype.Service;\n"
            + "import lombok.RequiredArgsConstructor;\n"
            + "import xbb.ai.erp.base.common.vo.DraftSaveVO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.dto." + aggregateName + "DraftListDTO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.dto." + aggregateName + "DraftLoadDTO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.dto." + aggregateName + "DraftSaveDTO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.vo." + aggregateName + "DraftDetailVO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.vo." + aggregateName + "DraftListItemVO;\n"
            + "import " + moduleSpec.getPackageBase() + ".application.pojo." + aggregateName + "SaveDraftPojo;\n"
            + "import " + moduleSpec.getPackageBase() + ".application.port." + aggregateName + "DraftRepository;\n"
            + "import " + moduleSpec.getPackageBase() + ".application.validator." + aggregateName + "SaveCommonValidator;\n"
            + "import " + moduleSpec.getPackageBase() + ".application.validator." + aggregateName + "SaveProtocolValidator;\n\n"
            + "import java.util.List;\n\n"
            + "@Service\n"
            + "@RequiredArgsConstructor\n"
            + "public class " + aggregateName + "DraftAppServiceImpl implements " + aggregateName + "DraftAppService {\n\n"
            + "    private final " + aggregateName + "DraftRepository repository;\n"
            + "    private final " + aggregateName + "SaveProtocolValidator protocolValidator;\n"
            + "    private final " + aggregateName + "SaveCommonValidator commonValidator;\n\n"
            + "    @Override\n"
            + "    public DraftSaveVO saveDraft(" + aggregateName + "DraftSaveDTO dto) {\n"
            + "        protocolValidator.validate(dto);\n"
            + "        commonValidator.validateForDraft(dto);\n"
            + "        " + aggregateName + "SaveDraftPojo draft = new " + aggregateName + "SaveDraftPojo();\n"
            + "        draft.setCorpid(dto.getCorpid());\n"
            + "        draft.setMain(dto.getMain());\n"
            + "        draft.setDraftCode(dto.getDraftMeta().getDraftCode());\n"
            + "        draft.setDraftTitle(dto.getDraftMeta().getDraftTitle());\n"
            + "        String code = repository.saveDraft(draft);\n"
            + "        dto.getDraftMeta().setDraftCode(code);\n"
            + "        DraftSaveVO vo = new DraftSaveVO();\n"
            + "        vo.setDraftCode(code);\n"
            + "        return vo;\n"
            + "    }\n\n"
            + "    @Override\n"
            + "    public List<" + aggregateName + "DraftListItemVO> draftList(" + aggregateName + "DraftListDTO dto) {\n"
            + "        return repository.listDrafts(dto.getCorpid(), 10).stream().map(draft -> {\n"
            + "            " + aggregateName + "DraftListItemVO vo = new " + aggregateName + "DraftListItemVO();\n"
            + "            vo.setDraftCode(draft.getDraftCode());\n"
            + "            vo.setDraftTitle(draft.getDraftTitle());\n"
            + "            return vo;\n"
            + "        }).toList();\n"
            + "    }\n\n"
            + "    @Override\n"
            + "    public " + aggregateName + "DraftDetailVO loadDraft(" + aggregateName + "DraftLoadDTO dto) {\n"
            + "        " + aggregateName + "SaveDraftPojo draft = repository.loadDraft(dto.getCorpid(), dto.getDraftCode());\n"
            + "        " + aggregateName + "DraftDetailVO vo = new " + aggregateName + "DraftDetailVO();\n"
            + "        if (draft != null) { vo.setDraftCode(draft.getDraftCode()); vo.setMain(draft.getMain()); }\n"
            + "        return vo;\n"
            + "    }\n"
            + "}\n";
    }

    public String renderAdminAssembler(ModuleSpec moduleSpec) {
        String aggregateName = aggregateName(moduleSpec);
        String variableName = lowerCamel(aggregateName);
        String packageName = moduleSpec.getPackageBase() + ".application.assembler";
        return "package " + packageName + ";\n\n"
            + "import java.util.Objects;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.dto." + aggregateName + "MainDTO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.dto." + aggregateName + "SaveDTO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.vo." + aggregateName + "DetailVO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.vo." + aggregateName + "ListItemVO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.vo." + aggregateName + "SaveItemVO;\n"
            + "import " + moduleSpec.getPackageBase() + ".domain.model." + aggregateName + ";\n\n"
            + "public final class " + aggregateName + "AdminAssembler {\n\n"
            + "    private " + aggregateName + "AdminAssembler() {\n"
            + "    }\n\n"
            + "    public static " + aggregateName + "SaveItemVO buildEmptySaveItemVO() {\n"
            + "        return new " + aggregateName + "SaveItemVO();\n"
            + "    }\n\n"
            + "    public static " + aggregateName + " to" + aggregateName + "(" + aggregateName + "SaveDTO dto) {\n"
            + "        " + aggregateName + " " + variableName + " = new " + aggregateName + "();\n"
            + "        " + aggregateName + "MainDTO main = dto.getMain();\n"
            + "        if (main != null) {\n"
            + copyFieldAssignmentsFromMainDto(moduleSpec.getAggregate().getFields(), "main", variableName)
            + "            if (Objects.isNull(main.getId())) {\n"
            + "                " + variableName + ".setCreatorId(dto.getUserId());\n"
            + "            }\n"
            + "            " + variableName + ".setModifyId(dto.getUserId());\n"
            + "        }\n"
            + "        " + variableName + ".setCorpid(dto.getCorpid());\n"
            + "        return " + variableName + ";\n"
            + "    }\n\n"
            + "    public static " + aggregateName + "ListItemVO toListItemVO(" + aggregateName + " " + variableName + ") {\n"
            + "        " + aggregateName + "ListItemVO vo = new " + aggregateName + "ListItemVO();\n"
            + copyFieldAssignments(moduleSpec.getAggregate().getFields().stream().filter(field -> Boolean.TRUE.equals(field.getVisibleInList())).toList(), variableName, "vo")
            + "        return vo;\n"
            + "    }\n\n"
            + "    public static " + aggregateName + "SaveItemVO toSaveItemVO(" + aggregateName + " " + variableName + ") {\n"
            + "        " + aggregateName + "SaveItemVO vo = new " + aggregateName + "SaveItemVO();\n"
            + "        if (" + variableName + " == null) {\n"
            + "            return vo;\n"
            + "        }\n"
            + "        " + aggregateName + "MainDTO main = new " + aggregateName + "MainDTO();\n"
            + copyFieldAssignments(moduleSpec.getAggregate().getFields(), variableName, "main")
            + "        vo.setMain(main);\n"
            + "        return vo;\n"
            + "    }\n\n"
            + "    public static " + aggregateName + "DetailVO toDetailVO(" + aggregateName + "SaveItemVO saveItemVO) {\n"
            + "        " + aggregateName + "DetailVO detailVO = new " + aggregateName + "DetailVO();\n"
            + "        detailVO.setMainData(saveItemVO);\n"
            + "        return detailVO;\n"
            + "    }\n"
            + "}\n";
    }

    public String renderDraftRepository(ModuleSpec moduleSpec) {
        String aggregateName = aggregateName(moduleSpec);
        String packageName = moduleSpec.getPackageBase() + ".application.port";
        return "package " + packageName + ";\n\nimport java.util.List;\nimport " + moduleSpec.getPackageBase() + ".application.pojo." + aggregateName + "SaveDraftPojo;\n\npublic interface " + aggregateName + "DraftRepository {\n    String saveDraft(" + aggregateName + "SaveDraftPojo draft);\n    List<" + aggregateName + "SaveDraftPojo> listDrafts(String corpid, int limit);\n    " + aggregateName + "SaveDraftPojo loadDraft(String corpid, String draftCode);\n    void removeDraft(String corpid, String draftCode);\n}\n";
    }

    public String renderDraftPojo(ModuleSpec moduleSpec) {
        String aggregateName = aggregateName(moduleSpec);
        String packageName = moduleSpec.getPackageBase() + ".application.pojo";
        return "package " + packageName + ";\n\nimport lombok.Data;\nimport " + moduleSpec.getPackageBase() + ".admin.dto." + aggregateName + "MainDTO;\n\n@Data\npublic class " + aggregateName + "SaveDraftPojo {\n    private String corpid;\n    private String draftCode;\n    private String draftTitle;\n    private " + aggregateName + "MainDTO main;\n    private Long updatedTime;\n}\n";
    }

    public String renderSaveProtocolValidator(ModuleSpec moduleSpec) { return renderSaveValidator(moduleSpec, "SaveProtocolValidator", "validate", "if (dto == null || dto.getMain() == null || dto.getCorpid() == null || dto.getCorpid().isBlank()) throw new BizException(\"保存协议不完整\");"); }

    public String renderSaveCommonValidator(ModuleSpec moduleSpec) {
        String aggregateName = aggregateName(moduleSpec);
        String packageName = moduleSpec.getPackageBase() + ".application.validator";
        return "package " + packageName + ";\n\nimport org.springframework.stereotype.Component;\nimport " + moduleSpec.getPackageBase() + ".admin.dto." + aggregateName + "SaveDTO;\n\n@Component\npublic class " + aggregateName + "SaveCommonValidator {\n    public void validateForDraft(" + aggregateName + "SaveDTO dto) {}\n    public void validateForSubmit(" + aggregateName + "SaveDTO dto) {}\n}\n";
    }

    public String renderSaveBusinessValidator(ModuleSpec moduleSpec) { return renderSaveValidator(moduleSpec, "SaveBusinessValidator", "validateForSubmit", ""); }

    private String renderSaveValidator(ModuleSpec moduleSpec, String suffix, String method, String body) {
        String aggregateName = aggregateName(moduleSpec);
        String packageName = moduleSpec.getPackageBase() + ".application.validator";
        return "package " + packageName + ";\n\nimport org.springframework.stereotype.Component;\nimport xbb.ai.erp.base.common.exception.BizException;\nimport " + moduleSpec.getPackageBase() + ".admin.dto." + aggregateName + "SaveDTO;\n\n@Component\npublic class " + aggregateName + suffix + " {\n    public void " + method + "(" + aggregateName + "SaveDTO dto) { " + body + " }\n}\n";
    }

    public String renderDraftRepositoryImpl(ModuleSpec moduleSpec) {
        String aggregateName = aggregateName(moduleSpec);
        String packageName = moduleSpec.getPackageBase() + ".infrastructure.persistence.repository";
        return "package " + packageName + ";\n\nimport java.util.List;\nimport org.springframework.stereotype.Repository;\nimport " + moduleSpec.getPackageBase() + ".application.pojo." + aggregateName + "SaveDraftPojo;\nimport " + moduleSpec.getPackageBase() + ".application.port." + aggregateName + "DraftRepository;\n\n@Repository\npublic class " + aggregateName + "DraftRepositoryImpl implements " + aggregateName + "DraftRepository {\n    public String saveDraft(" + aggregateName + "SaveDraftPojo draft) { throw new UnsupportedOperationException(\"请配置草稿缓存实现\"); }\n    public List<" + aggregateName + "SaveDraftPojo> listDrafts(String corpid, int limit) { throw new UnsupportedOperationException(\"请配置草稿缓存实现\"); }\n    public " + aggregateName + "SaveDraftPojo loadDraft(String corpid, String draftCode) { throw new UnsupportedOperationException(\"请配置草稿缓存实现\"); }\n    public void removeDraft(String corpid, String draftCode) { throw new UnsupportedOperationException(\"请配置草稿缓存实现\"); }\n}\n";
    }

    public String renderListMetaProvider(ModuleSpec moduleSpec) {
        String aggregateName = aggregateName(moduleSpec);
        String businessCode = businessCode(moduleSpec);
        String packageName = moduleSpec.getPackageBase() + ".application.provider";
        return "package " + packageName + ";\n\n"
            + "import org.springframework.stereotype.Component;\n"
            + "import xbb.ai.erp.base.common.filed.FieldEntity;\n"
            + "import xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO;\n"
            + "import xbb.ai.erp.base.common.pojo.FilterField;\n"
            + "import xbb.ai.erp.module.common.application.filter.ListFilterMetaPojo;\n"
            + "import xbb.ai.erp.module.common.application.pojo.ListMetaBundlePojo;\n"
            + "import xbb.ai.erp.module.common.application.provider.ListMetaProvider;\n\n"
            + "import java.util.List;\n"
            + "import java.util.Map;\n\n"
            + "@Component\n"
            + "public class " + aggregateName + "ListMetaProvider implements ListMetaProvider {\n\n"
            + "    @Override\n"
            + "    public String businessCode() {\n"
            + "        return \"" + businessCode + "\";\n"
            + "    }\n\n"
            + "    @Override\n"
            + "    public List<FilterField> buildFilterMeta(ListCommonQueryDTO dto) {\n"
            + "        return List.of();\n"
            + "    }\n\n"
            + "    @Override\n"
            + "    public Map<String, ListFilterMetaPojo> buildFilterConditionMeta(ListCommonQueryDTO dto) {\n"
            + "        return Map.of();\n"
            + "    }\n\n"
            + "    @Override\n"
            + "    public List<FieldEntity> buildHeaderMeta(ListCommonQueryDTO dto) {\n"
            + "        return List.of();\n"
            + "    }\n\n"
            + "    @Override\n"
            + "    public ListMetaBundlePojo buildTopButtonMeta(ListCommonQueryDTO dto) {\n"
            + "        return new ListMetaBundlePojo();\n"
            + "    }\n\n"
            + "    @Override\n"
            + "    public ListMetaBundlePojo buildBottomButtonMeta(ListCommonQueryDTO dto) {\n"
            + "        return new ListMetaBundlePojo();\n"
            + "    }\n\n"
            + "    @Override\n"
            + "    public ListMetaBundlePojo buildRowActionMeta(ListCommonQueryDTO dto) {\n"
            + "        return new ListMetaBundlePojo();\n"
            + "    }\n"
            + "}\n";
    }

    public String renderAdminController(ModuleSpec moduleSpec) {
        String aggregateName = aggregateName(moduleSpec);
        String packageName = moduleSpec.getPackageBase() + ".admin";
        String variableName = lowerCamel(aggregateName);
        String apiPath = apiPath(moduleSpec);
        return "package " + packageName + ";\n\n"
            + "import lombok.RequiredArgsConstructor;\n"
            + "import org.springframework.web.bind.annotation.PostMapping;\n"
            + "import org.springframework.web.bind.annotation.RequestBody;\n"
            + "import org.springframework.web.bind.annotation.RequestMapping;\n"
            + "import org.springframework.web.bind.annotation.RestController;\n"
            + "import xbb.ai.erp.base.common.dto.BaseDTO;\n"
            + "import xbb.ai.erp.base.common.dto.IdBaseDTO;\n"
            + "import xbb.ai.erp.base.common.vo.BaseVO;\n"
            + "import xbb.ai.erp.base.common.vo.ListBaseVO;\n"
            + "import xbb.ai.erp.base.common.vo.ResultVO;\n"
            + "import xbb.ai.erp.base.common.vo.SaveItemVO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.dto." + aggregateName + "DraftListDTO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.dto." + aggregateName + "DraftLoadDTO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.dto." + aggregateName + "DraftSaveDTO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.dto." + aggregateName + "ListDTO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.dto." + aggregateName + "SubmitSaveDTO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.vo." + aggregateName + "DraftDetailVO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.vo." + aggregateName + "DraftListItemVO;\n"
            + "import xbb.ai.erp.base.common.vo.DraftSaveVO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.vo." + aggregateName + "ListItemVO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.vo." + aggregateName + "SaveItemVO;\n"
            + "import " + moduleSpec.getPackageBase() + ".application.service." + aggregateName + "AdminAppService;\n\n"
            + "import java.util.List;\n\n"
            + "@RestController\n"
            + "@RequestMapping(\"/erp/v1/" + apiPath + "\")\n"
            + "@RequiredArgsConstructor\n"
            + "public class " + aggregateName + "AdminController {\n\n"
            + "    private final " + aggregateName + "AdminAppService " + variableName + "AdminAppService;\n\n"
            + "    @PostMapping(\"/list\")\n"
            + "    public ResultVO<ListBaseVO<" + aggregateName + "ListItemVO>> list(@RequestBody " + aggregateName + "ListDTO dto) {\n"
            + "        return ResultVO.success(" + variableName + "AdminAppService.list(dto));\n"
            + "    }\n\n"
            + "    @PostMapping(\"/addItem\")\n"
            + "    public ResultVO<SaveItemVO<" + aggregateName + "SaveItemVO>> addItem(@RequestBody BaseDTO dto) {\n"
            + "        return ResultVO.success(" + variableName + "AdminAppService.addItem(dto));\n"
            + "    }\n\n"
            + "    @PostMapping(\"/updateItem\")\n"
            + "    public ResultVO<SaveItemVO<" + aggregateName + "SaveItemVO>> updateItem(@RequestBody IdBaseDTO dto) {\n"
            + "        return ResultVO.success(" + variableName + "AdminAppService.updateItem(dto));\n"
            + "    }\n\n"
            + "    @PostMapping(\"/saveDraft\")\n"
            + "    public ResultVO<DraftSaveVO> saveDraft(@RequestBody " + aggregateName + "DraftSaveDTO dto) {\n"
            + "        return ResultVO.success(" + variableName + "AdminAppService.saveDraft(dto));\n"
            + "    }\n\n"
            + "    @PostMapping(\"/saveAndSubmit\")\n"
            + "    public ResultVO<BaseVO> saveAndSubmit(@RequestBody " + aggregateName + "SubmitSaveDTO dto) {\n"
            + "        return ResultVO.success(" + variableName + "AdminAppService.saveAndSubmit(dto));\n"
            + "    }\n\n"
            + "    @PostMapping(\"/draftList\")\n"
            + "    public ResultVO<List<" + aggregateName + "DraftListItemVO>> draftList(@RequestBody " + aggregateName + "DraftListDTO dto) {\n"
            + "        return ResultVO.success(" + variableName + "AdminAppService.draftList(dto));\n"
            + "    }\n\n"
            + "    @PostMapping(\"/loadDraft\")\n"
            + "    public ResultVO<" + aggregateName + "DraftDetailVO> loadDraft(@RequestBody " + aggregateName + "DraftLoadDTO dto) {\n"
            + "        return ResultVO.success(" + variableName + "AdminAppService.loadDraft(dto));\n"
            + "    }\n"
            + "}\n";
    }

    private String apiPath(ModuleSpec moduleSpec) {
        if (moduleSpec.getModuleApiName() == null || moduleSpec.getModuleApiName().isBlank()
            || moduleSpec.getBusinessName() == null || moduleSpec.getBusinessName().isBlank()) {
            return moduleSpec.getModuleCode();
        }
        return moduleSpec.getModuleApiName() + "/" + moduleSpec.getBusinessName();
    }

    private String businessCode(ModuleSpec moduleSpec) {
        return moduleSpec.getBusinessCode() == null || moduleSpec.getBusinessCode().isBlank()
            ? moduleSpec.getModuleCode()
            : moduleSpec.getBusinessCode();
    }

    public String renderMapperXml(ModuleSpec moduleSpec) {
        String aggregateName = aggregateName(moduleSpec);
        String namespace = moduleSpec.getPackageBase() + ".infrastructure.persistence.mapper." + aggregateName + "Mapper";
        String resultType = moduleSpec.getPackageBase() + ".infrastructure.persistence.po." + aggregateName + "PO";
        String tableName = moduleSpec.getAggregate().getTableName();
        List<FieldSpec> fields = moduleSpec.getAggregate().getFields();
        String baseColumns = fields.stream().map(FieldSpec::getColumn).collect(Collectors.joining(", "));
        String insertColumns = fields.stream().map(FieldSpec::getColumn).collect(Collectors.joining(", "));
        String insertValues = fields.stream().map(field -> "#{item." + field.getName() + "}").collect(Collectors.joining(", "));
        String updateSet = fields.stream()
            .filter(field -> !Boolean.TRUE.equals(field.getPrimaryKey()))
            .map(field -> "            <if test=\"" + field.getName() + " != null\">" + field.getColumn() + " = #{" + field.getName() + "},</if>")
            .collect(Collectors.joining("\n"));
        String queryCondition = renderQueryConditions(fields);
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n"
            + "<!DOCTYPE mapper\n"
            + "        PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"\n"
            + "        \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n"
            + "<mapper namespace=\"" + namespace + "\">\n\n"
            + "    <sql id=\"BaseColumns\">\n        " + baseColumns + "\n    </sql>\n\n"
            + "    <sql id=\"BaseCondition\">\n        corpid = #{conditionMap.corpid}\n        and del = 0\n"
            + queryCondition + "\n    </sql>\n\n"
            + "    <sql id=\"QueryTail\">\n"
            + "        <if test=\"conditionMap.groupByStr != null and conditionMap.groupByStr != ''\">\n"
            + "            group by ${conditionMap.groupByStr}\n"
            + "        </if>\n"
            + "        <if test=\"conditionMap.orderByStr != null and conditionMap.orderByStr != ''\">\n"
            + "            order by ${conditionMap.orderByStr}\n"
            + "        </if>\n"
            + "        <if test=\"conditionMap.pageSize != null and conditionMap.pageSize > 0 and conditionMap.offset != null\">\n"
            + "            limit #{conditionMap.offset}, #{conditionMap.pageSize}\n"
            + "        </if>\n"
            + "        <if test=\"conditionMap.pageSize != null and conditionMap.pageSize > 0 and conditionMap.offset == null\">\n"
            + "            limit #{conditionMap.pageSize}\n"
            + "        </if>\n"
            + "    </sql>\n\n"
            + "    <insert id=\"insert\" useGeneratedKeys=\"true\" keyProperty=\"id\">\n        insert into " + tableName + " (\n            " + insertColumnsWithoutId(moduleSpec.getAggregate().getFields()) + "\n        )\n        values (" + insertValuesWithoutId(moduleSpec.getAggregate().getFields()).replace("#{item.", "#{") + ")\n    </insert>\n\n"
            + "    <insert id=\"insertBatch\" useGeneratedKeys=\"true\" keyProperty=\"id\">\n        insert into " + tableName + " (\n            " + insertColumnsWithoutId(moduleSpec.getAggregate().getFields()) + "\n        )\n        values\n        <foreach collection=\"list\" item=\"item\" separator=\",\">\n            (" + insertValuesWithoutId(moduleSpec.getAggregate().getFields()) + ")\n        </foreach>\n    </insert>\n\n"
            + "    <update id=\"removeById\">\n        update " + tableName + "\n        set del = 1\n        where corpid = #{corpid}\n          and id = #{id}\n          and del = 0\n    </update>\n\n"
            + "    <update id=\"removeBatchByIds\">\n        update " + tableName + "\n        set del = 1\n        where corpid = #{corpid}\n          and del = 0\n          and id in\n        <foreach collection=\"ids\" item=\"id\" open=\"(\" separator=\",\" close=\")\">\n            #{id}\n        </foreach>\n    </update>\n\n"
            + "    <update id=\"update\" parameterType=\"" + resultType + "\">\n        update " + tableName + "\n        <set>\n"
            + updateSet + "\n        </set>\n        where corpid = #{corpid}\n          and id = #{id}\n          and del = 0\n    </update>\n\n"
            + "    <select id=\"findById\" resultType=\"" + resultType + "\">\n        select <include refid=\"BaseColumns\"/>\n        from " + tableName + "\n        where corpid = #{corpid}\n          and id = #{id}\n          and del = 0\n    </select>\n\n"
            + "    <select id=\"findByCondition\" resultType=\"" + resultType + "\">\n        select <include refid=\"BaseColumns\"/>\n        from " + tableName + "\n        where <include refid=\"BaseCondition\"/>\n"
            + "        <if test=\"conditionMap.conditions != null and conditionMap.conditions.size() > 0\">\n"
            + "            <foreach collection=\"conditionMap.conditions\" item=\"c\">\n"
            + "                <include refid=\"xbb.ai.erp.module.common.application.filter.CommonListFilterMapper.dynamicCondition\"/>\n"
            + "            </foreach>\n"
            + "        </if>\n"
            + "        <include refid=\"QueryTail\"/>\n    </select>\n\n"
            + "    <select id=\"count\" resultType=\"java.lang.Long\">\n        select count(1)\n        from " + tableName + "\n        where <include refid=\"BaseCondition\"/>\n    </select>\n"
            + "</mapper>\n";
    }

    public String renderConditionMapHelper(ModuleSpec moduleSpec) {
        String packageName = moduleSpec.getPackageBase() + ".infrastructure.persistence.repository";
        return "package " + packageName + ";\n\n"
            + "import xbb.ai.erp.base.common.exception.BizException;\n"
            + "import java.util.HashMap;\n"
            + "import java.util.Map;\n\n"
            + "final class ConditionMapHelper {\n\n"
            + "    private ConditionMapHelper() {\n"
            + "    }\n\n"
            + "    static Map<String, Object> prepare(Map<String, Object> source) {\n"
            + "        Map<String, Object> conditionMap = source == null ? new HashMap<>() : new HashMap<>(source);\n"
            + "        normalizePage(conditionMap);\n"
            + "        normalizeClause(conditionMap, \"groupByStr\");\n"
            + "        normalizeClause(conditionMap, \"orderByStr\");\n"
            + "        return conditionMap;\n"
            + "    }\n\n"
            + "    private static void normalizePage(Map<String, Object> conditionMap) {\n"
            + "        Integer pageSize = toInteger(conditionMap.get(\"pageSize\"));\n"
            + "        Integer pageNum = toInteger(conditionMap.get(\"pageNum\"));\n"
            + "        if (pageSize == null || pageSize <= 0) {\n"
            + "            conditionMap.remove(\"pageSize\");\n"
            + "            conditionMap.remove(\"pageNum\");\n"
            + "            conditionMap.remove(\"offset\");\n"
            + "            return;\n"
            + "        }\n"
            + "        conditionMap.put(\"pageSize\", pageSize);\n"
            + "        if (pageNum != null && pageNum > 0) {\n"
            + "            conditionMap.put(\"pageNum\", pageNum);\n"
            + "            conditionMap.put(\"offset\", (pageNum - 1) * pageSize);\n"
            + "            return;\n"
            + "        }\n"
            + "        conditionMap.remove(\"pageNum\");\n"
            + "        conditionMap.remove(\"offset\");\n"
            + "    }\n\n"
            + "    private static void normalizeClause(Map<String, Object> conditionMap, String key) {\n"
            + "        Object value = conditionMap.get(key);\n"
            + "        if (!(value instanceof String clause)) {\n"
            + "            conditionMap.remove(key);\n"
            + "            return;\n"
            + "        }\n"
            + "        String trimmed = clause.trim();\n"
            + "        if (trimmed.isEmpty()) {\n"
            + "            conditionMap.remove(key);\n"
            + "            return;\n"
            + "        }\n"
            + "        if (!trimmed.matches(\"[a-zA-Z0-9_,\\\\s]+\")) {\n"
            + "            throw new BizException(key + \" contains invalid characters\");\n"
            + "        }\n"
            + "        conditionMap.put(key, trimmed.replaceAll(\"\\\\s+\", \" \"));\n"
            + "    }\n\n"
            + "    private static Integer toInteger(Object value) {\n"
            + "        if (value instanceof Integer integerValue) {\n"
            + "            return integerValue;\n"
            + "        }\n"
            + "        if (value instanceof Long longValue) {\n"
            + "            return longValue.intValue();\n"
            + "        }\n"
            + "        if (value instanceof String stringValue && !stringValue.isBlank()) {\n"
            + "            return Integer.parseInt(stringValue.trim());\n"
            + "        }\n"
            + "        return null;\n"
            + "    }\n"
            + "}\n";
    }

    public String renderDryRun(Map<String, String> slotToPath) {
        return slotToPath.entrySet().stream().map(entry -> entry.getKey() + " -> " + entry.getValue()).collect(Collectors.joining("\n"));
    }

    private String renderFields(List<FieldSpec> fields, boolean persistenceMode) {
        return fields.stream().map(field -> renderField(field, persistenceMode)).collect(Collectors.joining());
    }

    private String insertColumnsWithoutId(List<FieldSpec> fields) {
        return fields.stream()
            .filter(field -> !Boolean.TRUE.equals(field.getPrimaryKey()))
            .map(FieldSpec::getColumn)
            .collect(Collectors.joining(", "));
    }

    private String insertValuesWithoutId(List<FieldSpec> fields) {
        return fields.stream()
            .filter(field -> !Boolean.TRUE.equals(field.getPrimaryKey()))
            .map(field -> "#{item." + field.getName() + "}")
            .collect(Collectors.joining(", "));
    }

    private String renderField(FieldSpec field, boolean persistenceMode) {
        String tableId = persistenceMode && Boolean.TRUE.equals(field.getPrimaryKey())
            ? "    @TableId(value = \"" + field.getColumn() + "\", type = IdType.AUTO)\n"
            : "";
        return tableId + "    private " + normalizeType(field.getJavaType(), persistenceMode) + " " + field.getName() + ";\n";
    }

    private String renderQueryableFields(List<FieldSpec> fields) {
        return fields.stream()
            .filter(field -> !"corpid".equals(field.getName()))
            .filter(field -> Boolean.TRUE.equals(field.getQueryable()))
            .map(field -> "    private " + field.getJavaType() + " " + field.getName() + ";\n")
            .collect(Collectors.joining());
    }

    private String renderVisibleListFields(List<FieldSpec> fields) {
        return fields.stream().filter(field -> Boolean.TRUE.equals(field.getVisibleInList())).map(field -> "    private " + field.getJavaType() + " " + field.getName() + ";\n").collect(Collectors.joining());
    }

    private String copyFieldAssignments(List<FieldSpec> fields, String source, String target) {
        return fields.stream().map(field -> "        " + target + ".set" + upperCamel(field.getName()) + "(" + source + ".get" + upperCamel(field.getName()) + "());\n").collect(Collectors.joining());
    }

    private String copyFieldAssignmentsFromMainDto(List<FieldSpec> fields, String source, String target) {
        return fields.stream().map(field -> "            " + target + ".set" + upperCamel(field.getName()) + "(" + source + ".get" + upperCamel(field.getName()) + "());\n").collect(Collectors.joining());
    }

    private String renderConditionMapLines(List<FieldSpec> fields) {
        return fields.stream()
            .filter(field -> Boolean.TRUE.equals(field.getQueryable()))
            .map(field -> "        conditionMap.put(\"" + field.getName() + "\", dto.get" + upperCamel(field.getName()) + "());\n")
            .collect(Collectors.joining());
    }

    private String renderQueryConditions(List<FieldSpec> fields) {
        return fields.stream()
            .filter(field -> !"corpid".equals(field.getName()) && Boolean.TRUE.equals(field.getQueryable()))
            .map(field -> {
                if ("String".equals(field.getJavaType())) {
                    return "        <if test=\"conditionMap." + field.getName() + " != null and conditionMap." + field.getName() + " != ''\">\n"
                        + "            and " + field.getColumn() + " = #{conditionMap." + field.getName() + "}\n"
                        + "        </if>";
                }
                return "        <if test=\"conditionMap." + field.getName() + " != null\">\n"
                    + "            and " + field.getColumn() + " = #{conditionMap." + field.getName() + "}\n"
                    + "        </if>";
            })
            .collect(Collectors.joining("\n"));
    }

    private List<FieldSpec> filterBaseEntityFields(List<FieldSpec> fields) {
        return fields.stream()
            .filter(field -> !isBaseEntityField(field.getName()))
            .collect(Collectors.toList());
    }

    private boolean isBaseEntityField(String fieldName) {
        return "id".equals(fieldName)
            || "del".equals(fieldName)
            || "deleted".equals(fieldName)
            || "addTime".equals(fieldName)
            || "updateTime".equals(fieldName)
            || "createTime".equals(fieldName);
    }

    private boolean hasField(List<FieldSpec> fields, String fieldName) {
        return fields.stream().anyMatch(field -> fieldName.equals(field.getName()));
    }

    private String normalizeType(String javaType, boolean persistenceMode) {
        if (persistenceMode && ("boolean".equalsIgnoreCase(javaType) || "Boolean".equalsIgnoreCase(javaType))) {
            return "Integer";
        }
        return javaType;
    }

    private String aggregateName(ModuleSpec moduleSpec) {
        return moduleSpec.getAggregate().getAggregateName();
    }

    private String repositoryBeanName(ModuleSpec moduleSpec, String aggregateName) {
        String[] packageParts = moduleSpec.getPackageBase().split("\\.");
        StringBuilder beanName = new StringBuilder();
        for (String packagePart : packageParts) {
            beanName.append(upperCamel(packagePart));
        }
        return lowerCamel(beanName.toString()) + aggregateName + "RepositoryImpl";
    }

    private String lowerCamel(String value) {
        if (value == null || value.isBlank()) {
            return value;
        }
        return Character.toLowerCase(value.charAt(0)) + value.substring(1);
    }

    private String upperCamel(String value) {
        if (value == null || value.isBlank()) {
            return value;
        }
        return Character.toUpperCase(value.charAt(0)) + value.substring(1);
    }
}
