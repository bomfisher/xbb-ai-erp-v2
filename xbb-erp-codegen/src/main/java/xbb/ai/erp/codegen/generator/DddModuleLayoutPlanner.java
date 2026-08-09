package xbb.ai.erp.codegen.generator;

import xbb.ai.erp.codegen.template.TemplateType;
import xbb.ai.erp.codegen.spec.AggregateRoleEnum;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DddModuleLayoutPlanner {

    public List<DddFilePlan> plan(DddGenerationContext context) {
        String basePackagePath = context.basePackage().replace('.', '/');
        String aggregateName = context.aggregateName();
        List<DddFilePlan> plans = new ArrayList<>();

        if (context.moduleSpec().getAggregateRole() == AggregateRoleEnum.ROOT) {
            plans.add(planJava(context, basePackagePath, "admin", aggregateName + "AdminController.java", TemplateType.ADMIN_CONTROLLER));
            plans.add(planJava(context, basePackagePath, "admin/dto", aggregateName + "ListDTO.java", TemplateType.ADMIN_LIST_DTO));
            plans.add(planJava(context, basePackagePath, "admin/dto", aggregateName + "MainDTO.java", TemplateType.ADMIN_MAIN_DTO));
            plans.add(planJava(context, basePackagePath, "admin/dto", aggregateName + "SaveDTO.java", TemplateType.ADMIN_SAVE_DTO));
            plans.add(planJava(context, basePackagePath, "admin/dto", aggregateName + "SubmitSaveDTO.java", TemplateType.ADMIN_SUBMIT_SAVE_DTO));
            plans.add(planJava(context, basePackagePath, "admin/dto", aggregateName + "DraftSaveDTO.java", TemplateType.ADMIN_DRAFT_SAVE_DTO));
            plans.add(planJava(context, basePackagePath, "admin/dto", aggregateName + "DraftMetaDTO.java", TemplateType.ADMIN_DRAFT_META_DTO));
            plans.add(planJava(context, basePackagePath, "admin/dto", aggregateName + "DraftListDTO.java", TemplateType.ADMIN_DRAFT_LIST_DTO));
            plans.add(planJava(context, basePackagePath, "admin/dto", aggregateName + "DraftLoadDTO.java", TemplateType.ADMIN_DRAFT_LOAD_DTO));
            plans.add(planJava(context, basePackagePath, "admin/vo", aggregateName + "ListItemVO.java", TemplateType.ADMIN_LIST_ITEM_VO));
            plans.add(planJava(context, basePackagePath, "admin/vo", aggregateName + "SaveItemVO.java", TemplateType.ADMIN_SAVE_ITEM_VO));
            plans.add(planJava(context, basePackagePath, "admin/vo", aggregateName + "DetailVO.java", TemplateType.ADMIN_DETAIL_VO));
            plans.add(planJava(context, basePackagePath, "admin/vo", aggregateName + "DraftListItemVO.java", TemplateType.ADMIN_DRAFT_LIST_ITEM_VO));
            plans.add(planJava(context, basePackagePath, "admin/vo", aggregateName + "DraftDetailVO.java", TemplateType.ADMIN_DRAFT_DETAIL_VO));
            plans.add(planJava(context, basePackagePath, "application/service", aggregateName + "AdminAppService.java", TemplateType.APP_SERVICE));
            plans.add(planJava(context, basePackagePath, "application/service/impl", aggregateName + "AdminAppServiceImpl.java", TemplateType.APP_SERVICE_IMPL));
            plans.add(planJava(context, basePackagePath, "application/service/query", aggregateName + "QueryAppServiceImpl.java", TemplateType.APP_QUERY_SERVICE_IMPL));
            plans.add(planJava(context, basePackagePath, "application/service/save", aggregateName + "SaveAppServiceImpl.java", TemplateType.APP_SAVE_SERVICE_IMPL));
            plans.add(planJava(context, basePackagePath, "application/service/draft", aggregateName + "DraftAppService.java", TemplateType.APP_DRAFT_SERVICE));
            plans.add(planJava(context, basePackagePath, "application/service/draft", aggregateName + "DraftAppServiceImpl.java", TemplateType.APP_DRAFT_SERVICE_IMPL));
            plans.add(planJava(context, basePackagePath, "application/port", aggregateName + "DraftRepository.java", TemplateType.APP_DRAFT_REPOSITORY));
            plans.add(planJava(context, basePackagePath, "application/pojo", aggregateName + "SaveDraftPojo.java", TemplateType.APP_DRAFT_POJO));
            plans.add(planJava(context, basePackagePath, "application/validator", aggregateName + "SaveProtocolValidator.java", TemplateType.APP_SAVE_PROTOCOL_VALIDATOR));
            plans.add(planJava(context, basePackagePath, "application/validator", aggregateName + "SaveCommonValidator.java", TemplateType.APP_SAVE_COMMON_VALIDATOR));
            plans.add(planJava(context, basePackagePath, "application/validator", aggregateName + "SaveBusinessValidator.java", TemplateType.APP_SAVE_BUSINESS_VALIDATOR));
            plans.add(planJava(context, basePackagePath, "application/assembler", aggregateName + "AdminAssembler.java", TemplateType.APP_ASSEMBLER));
            plans.add(planJava(context, basePackagePath, "application/validator", aggregateName + "Validator.java", TemplateType.APP_VALIDATOR));
            plans.add(planJava(context, basePackagePath, "application/pojo", aggregateName + "QueryPojo.java", TemplateType.APP_QUERY_POJO));
            plans.add(planJava(context, basePackagePath, "application/provider", aggregateName + "ListMetaProvider.java", TemplateType.APP_LIST_META_PROVIDER));
        }
        plans.add(planJava(context, basePackagePath, "domain/model", aggregateName + ".java", TemplateType.DOMAIN_MODEL));
        plans.add(planJava(context, basePackagePath, "domain/repository", aggregateName + "Repository.java", TemplateType.DOMAIN_REPOSITORY));
        plans.add(planJava(context, basePackagePath, "domain/pojo", aggregateName + "QueryPojo.java", TemplateType.DOMAIN_QUERY_POJO));
        plans.add(planJava(context, basePackagePath, "infrastructure/persistence/po", aggregateName + "PO.java", TemplateType.PERSISTENCE_PO));
        plans.add(planJava(context, basePackagePath, "infrastructure/persistence/mapper", aggregateName + "Mapper.java", TemplateType.PERSISTENCE_MAPPER));
        plans.add(planJava(context, basePackagePath, "infrastructure/persistence/convertor", aggregateName + "Convertor.java", TemplateType.PERSISTENCE_CONVERTOR));
            plans.add(planJava(context, basePackagePath, "infrastructure/persistence/repository", aggregateName + "RepositoryImpl.java", TemplateType.PERSISTENCE_REPOSITORY_IMPL));
            plans.add(planJava(context, basePackagePath, "infrastructure/persistence/repository", aggregateName + "DraftRepositoryImpl.java", TemplateType.PERSISTENCE_DRAFT_REPOSITORY_IMPL));
        plans.add(planJava(context, basePackagePath, "infrastructure/persistence/repository", "ConditionMapHelper.java", TemplateType.PERSISTENCE_CONDITION_MAP_HELPER));
        plans.add(new DddFilePlan(context.mapperXmlDir().resolve(aggregateName + "Mapper.xml"), "", aggregateName + "Mapper.xml", TemplateType.MAPPER_XML, true));
        return plans;
    }

    private DddFilePlan planJava(DddGenerationContext context, String basePackagePath, String relativeDir, String fileName, TemplateType templateType) {
        Path targetPath = context.javaSourceRoot().resolve(basePackagePath).resolve(relativeDir).resolve(fileName);
        String packageName = context.basePackage() + "." + relativeDir.replace('/', '.');
        String className = fileName.endsWith(".java") ? fileName.substring(0, fileName.length() - 5) : fileName;
        return new DddFilePlan(targetPath, packageName, className, templateType, true);
    }
}
