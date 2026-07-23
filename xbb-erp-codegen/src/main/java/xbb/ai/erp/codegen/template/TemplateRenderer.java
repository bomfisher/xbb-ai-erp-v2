package xbb.ai.erp.codegen.template;

import xbb.ai.erp.codegen.spec.FieldSpec;
import xbb.ai.erp.codegen.spec.ModuleSpec;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TemplateRenderer {

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
            + "    void insert(" + aggregateName + " " + variableName + ");\n\n"
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
        if (shouldExtendBaseEntity(moduleSpec.getAggregate().getFields())) {
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
        return "package " + packageName + ";\n\n"
            + "import com.baomidou.mybatisplus.annotation.TableName;\n"
            + "import lombok.Data;\n\n"
            + "@Data\n"
            + "@TableName(\"" + moduleSpec.getAggregate().getTableName() + "\")\n"
            + "public class " + aggregateName + "PO {\n"
            + renderFields(moduleSpec.getAggregate().getFields(), true)
            + "}\n";
    }

    public String renderMapper(ModuleSpec moduleSpec) {
        String packageName = moduleSpec.getPackageBase() + ".infrastructure.persistence.mapper";
        String aggregateName = aggregateName(moduleSpec);
        return "package " + packageName + ";\n\n"
            + "import com.baomidou.mybatisplus.core.mapper.BaseMapper;\n"
            + "import org.apache.ibatis.annotations.Param;\n"
            + "import " + moduleSpec.getPackageBase() + ".infrastructure.persistence.po." + aggregateName + "PO;\n\n"
            + "import java.util.List;\n"
            + "import java.util.Map;\n\n"
            + "public interface " + aggregateName + "Mapper extends BaseMapper<" + aggregateName + "PO> {\n"
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
            + "import " + moduleSpec.getPackageBase() + ".domain.model." + aggregateName + ";\n"
            + "import " + moduleSpec.getPackageBase() + ".domain.repository." + aggregateName + "Repository;\n"
            + "import " + moduleSpec.getPackageBase() + ".infrastructure.persistence.convertor." + aggregateName + "Convertor;\n"
            + "import " + moduleSpec.getPackageBase() + ".infrastructure.persistence.mapper." + aggregateName + "Mapper;\n"
            + "import " + moduleSpec.getPackageBase() + ".infrastructure.persistence.po." + aggregateName + "PO;\n\n"
            + "import java.util.List;\n"
            + "import java.util.Map;\n\n"
            + "@Repository\n"
            + "@RequiredArgsConstructor\n"
            + "public class " + aggregateName + "RepositoryImpl implements " + aggregateName + "Repository {\n\n"
            + "    private final " + aggregateName + "Mapper " + variableName + "Mapper;\n\n"
            + "    @Override\n"
            + "    public void insert(" + aggregateName + " " + variableName + ") {\n"
            + "        " + variableName + "Mapper.insert(" + aggregateName + "Convertor.toPO(" + variableName + "));\n"
            + "    }\n\n"
            + "    @Override\n"
            + "    public void insertBatch(List<" + aggregateName + "> " + variableName + "List) {\n"
            + "        " + variableName + "Mapper.insertBatch(" + variableName + "List.stream().map(" + aggregateName + "Convertor::toPO).toList());\n"
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
            + "    }\n"
            + "}\n";
    }

    public String renderListDTO(ModuleSpec moduleSpec) {
        String aggregateName = aggregateName(moduleSpec);
        String packageName = moduleSpec.getPackageBase() + ".admin.dto";
        return "package " + packageName + ";\n\n"
            + "import lombok.Data;\n"
            + "import lombok.EqualsAndHashCode;\n"
            + "import xbb.ai.erp.base.common.dto.BaseDTO;\n\n"
            + "@Data\n"
            + "@EqualsAndHashCode(callSuper = true)\n"
            + "public class " + aggregateName + "ListDTO extends BaseDTO {\n"
            + renderQueryableFields(moduleSpec.getAggregate().getFields())
            + "    private Integer pageNum;\n"
            + "    private Integer pageSize;\n"
            + "    private Integer offset;\n"
            + "    private String groupByStr;\n"
            + "    private String orderByStr;\n"
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

    public String renderAppService(ModuleSpec moduleSpec) {
        String aggregateName = aggregateName(moduleSpec);
        String packageName = moduleSpec.getPackageBase() + ".application.service";
        return "package " + packageName + ";\n\n"
            + "import xbb.ai.erp.base.common.dto.BaseDTO;\n"
            + "import xbb.ai.erp.base.common.dto.BatchBaseDTO;\n"
            + "import xbb.ai.erp.base.common.dto.IdBaseDTO;\n"
            + "import xbb.ai.erp.base.common.vo.ListBaseVO;\n"
            + "import xbb.ai.erp.base.common.vo.SaveItemVO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.dto." + aggregateName + "ListDTO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.dto." + aggregateName + "SaveDTO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.vo." + aggregateName + "DetailVO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.vo." + aggregateName + "ListItemVO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.vo." + aggregateName + "SaveItemVO;\n\n"
            + "public interface " + aggregateName + "AdminAppService {\n"
            + "    ListBaseVO<" + aggregateName + "ListItemVO> list(" + aggregateName + "ListDTO dto);\n\n"
            + "    SaveItemVO<" + aggregateName + "SaveItemVO> addItem(BaseDTO dto);\n\n"
            + "    SaveItemVO<" + aggregateName + "SaveItemVO> updateItem(IdBaseDTO dto);\n\n"
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
            + "import xbb.ai.erp.base.common.dto.BaseDTO;\n"
            + "import xbb.ai.erp.base.common.dto.BatchBaseDTO;\n"
            + "import xbb.ai.erp.base.common.dto.IdBaseDTO;\n"
            + "import xbb.ai.erp.base.common.vo.ListBaseVO;\n"
            + "import xbb.ai.erp.base.common.vo.SaveItemVO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.dto." + aggregateName + "ListDTO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.dto." + aggregateName + "SaveDTO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.vo." + aggregateName + "DetailVO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.vo." + aggregateName + "ListItemVO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.vo." + aggregateName + "SaveItemVO;\n"
            + "import " + moduleSpec.getPackageBase() + ".application.assembler." + aggregateName + "AdminAssembler;\n"
            + "import " + moduleSpec.getPackageBase() + ".application.service." + aggregateName + "AdminAppService;\n"
            + "import " + moduleSpec.getPackageBase() + ".domain.model." + aggregateName + ";\n"
            + "import " + moduleSpec.getPackageBase() + ".domain.repository." + aggregateName + "Repository;\n\n"
            + "import java.util.HashMap;\n"
            + "import java.util.List;\n"
            + "import java.util.Map;\n\n"
            + "@Service\n"
            + "@RequiredArgsConstructor\n"
            + "public class " + aggregateName + "AdminAppServiceImpl implements " + aggregateName + "AdminAppService {\n\n"
            + "    private final " + aggregateName + "Repository " + variableName + "Repository;\n\n"
            + "    @Override\n"
            + "    public ListBaseVO<" + aggregateName + "ListItemVO> list(" + aggregateName + "ListDTO dto) {\n"
            + "        Map<String, Object> conditionMap = new HashMap<>();\n"
            + renderConditionMapLines(moduleSpec.getAggregate().getFields())
            + "        conditionMap.put(\"pageNum\", dto.getPageNum());\n"
            + "        conditionMap.put(\"offset\", dto.getOffset());\n"
            + "        conditionMap.put(\"pageSize\", dto.getPageSize());\n"
            + "        conditionMap.put(\"groupByStr\", dto.getGroupByStr());\n"
            + "        conditionMap.put(\"orderByStr\", dto.getOrderByStr());\n"
            + "        List<" + aggregateName + "> list = " + variableName + "Repository.findByCondition(conditionMap);\n"
            + "        Long total = " + variableName + "Repository.count(conditionMap);\n"
            + "        ListBaseVO<" + aggregateName + "ListItemVO> vo = new ListBaseVO<>();\n"
            + "        vo.setList(list.stream().map(" + aggregateName + "AdminAssembler::toListItemVO).toList());\n"
            + "        vo.setPageHelper(new ListBaseVO.PageHelper(dto.getPageNum() == null ? 1 : dto.getPageNum(), total == null ? 0 : total.intValue()));\n"
            + "        return vo;\n"
            + "    }\n\n"
            + "    @Override\n"
            + "    public SaveItemVO<" + aggregateName + "SaveItemVO> addItem(BaseDTO dto) {\n"
            + "        SaveItemVO<" + aggregateName + "SaveItemVO> vo = new SaveItemVO<>();\n"
            + "        vo.setData(" + aggregateName + "AdminAssembler.buildEmptySaveItemVO());\n"
            + "        return vo;\n"
            + "    }\n\n"
            + "    @Override\n"
            + "    public SaveItemVO<" + aggregateName + "SaveItemVO> updateItem(IdBaseDTO dto) {\n"
            + "        SaveItemVO<" + aggregateName + "SaveItemVO> vo = new SaveItemVO<>();\n"
            + "        vo.setData(toSaveItem(dto));\n"
            + "        return vo;\n"
            + "    }\n\n"
            + "    @Override\n"
            + "    public Long save(" + aggregateName + "SaveDTO dto) {\n"
            + "        " + aggregateName + " " + variableName + " = " + aggregateName + "AdminAssembler.to" + aggregateName + "(dto);\n"
            + "        if (" + variableName + ".getId() == null) {\n"
            + "            " + variableName + "Repository.insert(" + variableName + ");\n"
            + "        } else {\n"
            + "            " + variableName + "Repository.update(" + variableName + ");\n"
            + "        }\n"
            + "        return " + variableName + ".getId();\n"
            + "    }\n\n"
            + "    @Override\n"
            + "    public " + aggregateName + "DetailVO detail(IdBaseDTO dto) {\n"
            + "        return " + aggregateName + "AdminAssembler.toDetailVO(toSaveItem(dto));\n"
            + "    }\n\n"
            + "    @Override\n"
            + "    public void delete(BatchBaseDTO dto) {\n"
            + "        if (dto.getIdList() == null || dto.getIdList().isEmpty()) {\n"
            + "            return;\n"
            + "        }\n"
            + "        " + variableName + "Repository.removeBatchByIds(dto.getCorpid(), dto.getIdList());\n"
            + "    }\n\n"
            + "    private " + aggregateName + "SaveItemVO toSaveItem(IdBaseDTO dto) {\n"
            + "        return " + aggregateName + "AdminAssembler.toSaveItemVO(" + variableName + "Repository.findById(dto.getCorpid(), dto.getId()));\n"
            + "    }\n"
            + "}\n";
    }

    public String renderAdminAssembler(ModuleSpec moduleSpec) {
        String aggregateName = aggregateName(moduleSpec);
        String variableName = lowerCamel(aggregateName);
        String packageName = moduleSpec.getPackageBase() + ".application.assembler";
        return "package " + packageName + ";\n\n"
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

    public String renderAdminController(ModuleSpec moduleSpec) {
        String aggregateName = aggregateName(moduleSpec);
        String packageName = moduleSpec.getPackageBase() + ".admin";
        String variableName = lowerCamel(aggregateName);
        return "package " + packageName + ";\n\n"
            + "import lombok.RequiredArgsConstructor;\n"
            + "import org.springframework.web.bind.annotation.PostMapping;\n"
            + "import org.springframework.web.bind.annotation.RequestBody;\n"
            + "import org.springframework.web.bind.annotation.RequestMapping;\n"
            + "import org.springframework.web.bind.annotation.RestController;\n"
            + "import xbb.ai.erp.base.common.dto.BaseDTO;\n"
            + "import xbb.ai.erp.base.common.dto.BatchBaseDTO;\n"
            + "import xbb.ai.erp.base.common.dto.IdBaseDTO;\n"
            + "import xbb.ai.erp.base.common.vo.ListBaseVO;\n"
            + "import xbb.ai.erp.base.common.vo.SaveItemVO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.dto." + aggregateName + "ListDTO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.dto." + aggregateName + "SaveDTO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.vo." + aggregateName + "DetailVO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.vo." + aggregateName + "ListItemVO;\n"
            + "import " + moduleSpec.getPackageBase() + ".admin.vo." + aggregateName + "SaveItemVO;\n"
            + "import " + moduleSpec.getPackageBase() + ".application.service." + aggregateName + "AdminAppService;\n\n"
            + "@RestController\n"
            + "@RequestMapping(\"/erp/v1/" + moduleSpec.getModuleCode() + "\")\n"
            + "@RequiredArgsConstructor\n"
            + "public class " + aggregateName + "AdminController {\n\n"
            + "    private final " + aggregateName + "AdminAppService " + variableName + "AdminAppService;\n\n"
            + "    @PostMapping(\"/list\")\n"
            + "    public ListBaseVO<" + aggregateName + "ListItemVO> list(@RequestBody " + aggregateName + "ListDTO dto) {\n"
            + "        return " + variableName + "AdminAppService.list(dto);\n"
            + "    }\n\n"
            + "    @PostMapping(\"/addItem\")\n"
            + "    public SaveItemVO<" + aggregateName + "SaveItemVO> addItem(@RequestBody BaseDTO dto) {\n"
            + "        return " + variableName + "AdminAppService.addItem(dto);\n"
            + "    }\n\n"
            + "    @PostMapping(\"/updateItem\")\n"
            + "    public SaveItemVO<" + aggregateName + "SaveItemVO> updateItem(@RequestBody IdBaseDTO dto) {\n"
            + "        return " + variableName + "AdminAppService.updateItem(dto);\n"
            + "    }\n\n"
            + "    @PostMapping(\"/save\")\n"
            + "    public Long save(@RequestBody " + aggregateName + "SaveDTO dto) {\n"
            + "        return " + variableName + "AdminAppService.save(dto);\n"
            + "    }\n\n"
            + "    @PostMapping(\"/detail\")\n"
            + "    public " + aggregateName + "DetailVO detail(@RequestBody IdBaseDTO dto) {\n"
            + "        return " + variableName + "AdminAppService.detail(dto);\n"
            + "    }\n\n"
            + "    @PostMapping(\"/delete\")\n"
            + "    public void delete(@RequestBody BatchBaseDTO dto) {\n"
            + "        " + variableName + "AdminAppService.delete(dto);\n"
            + "    }\n"
            + "}\n";
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
            + "    <insert id=\"insertBatch\">\n        insert into " + tableName + " (\n            " + insertColumns + "\n        )\n        values\n        <foreach collection=\"list\" item=\"item\" separator=\",\">\n            (" + insertValues + ")\n        </foreach>\n    </insert>\n\n"
            + "    <update id=\"removeById\">\n        update " + tableName + "\n        set del = 1\n        where corpid = #{corpid}\n          and id = #{id}\n          and del = 0\n    </update>\n\n"
            + "    <update id=\"removeBatchByIds\">\n        update " + tableName + "\n        set del = 1\n        where corpid = #{corpid}\n          and del = 0\n          and id in\n        <foreach collection=\"ids\" item=\"id\" open=\"(\" separator=\",\" close=\")\">\n            #{id}\n        </foreach>\n    </update>\n\n"
            + "    <update id=\"update\" parameterType=\"" + resultType + "\">\n        update " + tableName + "\n        <set>\n"
            + updateSet + "\n        </set>\n        where corpid = #{corpid}\n          and id = #{id}\n          and del = 0\n    </update>\n\n"
            + "    <select id=\"findById\" resultType=\"" + resultType + "\">\n        select <include refid=\"BaseColumns\"/>\n        from " + tableName + "\n        where corpid = #{corpid}\n          and id = #{id}\n          and del = 0\n    </select>\n\n"
            + "    <select id=\"findByCondition\" resultType=\"" + resultType + "\">\n        select <include refid=\"BaseColumns\"/>\n        from " + tableName + "\n        where <include refid=\"BaseCondition\"/>\n        <include refid=\"QueryTail\"/>\n    </select>\n\n"
            + "    <select id=\"count\" resultType=\"java.lang.Long\">\n        select count(1)\n        from " + tableName + "\n        where <include refid=\"BaseCondition\"/>\n    </select>\n"
            + "</mapper>\n";
    }

    public String renderConditionMapHelper(ModuleSpec moduleSpec) {
        String packageName = moduleSpec.getPackageBase() + ".infrastructure.persistence.repository";
        return "package " + packageName + ";\n\n"
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
            + "            throw new IllegalArgumentException(key + \" contains invalid characters\");\n"
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
        return fields.stream().map(field -> "    private " + normalizeType(field.getJavaType(), persistenceMode) + " " + field.getName() + ";\n").collect(Collectors.joining());
    }

    private String renderQueryableFields(List<FieldSpec> fields) {
        return fields.stream().filter(field -> Boolean.TRUE.equals(field.getQueryable())).map(field -> "    private " + field.getJavaType() + " " + field.getName() + ";\n").collect(Collectors.joining());
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

    private boolean shouldExtendBaseEntity(List<FieldSpec> fields) {
        return hasField(fields, "id", "Long")
            && hasField(fields, "deleted", "Integer")
            && hasField(fields, "createTime", "LocalDateTime")
            && hasField(fields, "updateTime", "LocalDateTime")
            && !hasField(fields, "addTime");
    }

    private List<FieldSpec> filterBaseEntityFields(List<FieldSpec> fields) {
        return fields.stream()
            .filter(field -> !isBaseEntityField(field.getName()))
            .collect(Collectors.toList());
    }

    private boolean isBaseEntityField(String fieldName) {
        return "id".equals(fieldName)
            || "deleted".equals(fieldName)
            || "updateTime".equals(fieldName)
            || "createTime".equals(fieldName);
    }

    private boolean hasField(List<FieldSpec> fields, String fieldName) {
        return fields.stream().anyMatch(field -> fieldName.equals(field.getName()));
    }

    private boolean hasField(List<FieldSpec> fields, String fieldName, String javaType) {
        return fields.stream().anyMatch(field -> fieldName.equals(field.getName()) && javaType.equals(field.getJavaType()));
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
