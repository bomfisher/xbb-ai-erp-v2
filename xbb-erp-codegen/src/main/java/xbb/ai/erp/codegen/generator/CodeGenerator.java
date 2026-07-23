package xbb.ai.erp.codegen.generator;

import xbb.ai.erp.codegen.spec.FileSlotEnum;
import xbb.ai.erp.codegen.spec.GenerateSpec;
import xbb.ai.erp.codegen.spec.ModuleSpec;
import xbb.ai.erp.codegen.spec.PathStrategySpec;
import xbb.ai.erp.codegen.strategy.PathResolver;
import xbb.ai.erp.codegen.strategy.ResolvedPath;
import xbb.ai.erp.codegen.template.TemplateRenderer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public class CodeGenerator {

    private final PathResolver pathResolver = new PathResolver();
    private final TemplateRenderer templateRenderer = new TemplateRenderer();

    public Map<String, String> dryRun(ModuleSpec moduleSpec, PathStrategySpec pathStrategySpec) {
        String aggregateName = moduleSpec.getAggregate().getAggregateName();
        GenerateSpec generateSpec = moduleSpec.getGenerate();
        Map<String, String> pathMap = new LinkedHashMap<>();
        if (generateSpec.isAdmin()) {
            pathMap.put(FileSlotEnum.ADMIN_CONTROLLER.name(), pathResolver.resolve(moduleSpec, pathStrategySpec, FileSlotEnum.ADMIN_CONTROLLER, aggregateName + "AdminController.java").getRelativePath());
            pathMap.put(FileSlotEnum.ADMIN_DTO.name(), pathResolver.resolve(moduleSpec, pathStrategySpec, FileSlotEnum.ADMIN_DTO, aggregateName + "SaveDTO.java").getRelativePath());
            pathMap.put(FileSlotEnum.ADMIN_VO.name(), pathResolver.resolve(moduleSpec, pathStrategySpec, FileSlotEnum.ADMIN_VO, aggregateName + "DetailVO.java").getRelativePath());
        }
        if (generateSpec.isApplication()) {
            pathMap.put(FileSlotEnum.APP_SERVICE.name(), pathResolver.resolve(moduleSpec, pathStrategySpec, FileSlotEnum.APP_SERVICE, aggregateName + "AdminAppService.java").getRelativePath());
            pathMap.put(FileSlotEnum.APP_SERVICE_IMPL.name(), pathResolver.resolve(moduleSpec, pathStrategySpec, FileSlotEnum.APP_SERVICE_IMPL, aggregateName + "AdminAppServiceImpl.java").getRelativePath());
            pathMap.put(FileSlotEnum.APP_ASSEMBLER.name(), pathResolver.resolve(moduleSpec, pathStrategySpec, FileSlotEnum.APP_ASSEMBLER, aggregateName + "AdminAssembler.java").getRelativePath());
        }
        if (generateSpec.isDomain()) {
            pathMap.put(FileSlotEnum.DOMAIN_MODEL.name(), pathResolver.resolve(moduleSpec, pathStrategySpec, FileSlotEnum.DOMAIN_MODEL, aggregateName + ".java").getRelativePath());
            pathMap.put(FileSlotEnum.DOMAIN_REPOSITORY.name(), pathResolver.resolve(moduleSpec, pathStrategySpec, FileSlotEnum.DOMAIN_REPOSITORY, aggregateName + "Repository.java").getRelativePath());
        }
        if (generateSpec.isPersistence()) {
            pathMap.put(FileSlotEnum.PERSISTENCE_PO.name(), pathResolver.resolve(moduleSpec, pathStrategySpec, FileSlotEnum.PERSISTENCE_PO, aggregateName + "PO.java").getRelativePath());
            pathMap.put(FileSlotEnum.PERSISTENCE_MAPPER.name(), pathResolver.resolve(moduleSpec, pathStrategySpec, FileSlotEnum.PERSISTENCE_MAPPER, aggregateName + "Mapper.java").getRelativePath());
            pathMap.put(FileSlotEnum.PERSISTENCE_CONVERTOR.name(), pathResolver.resolve(moduleSpec, pathStrategySpec, FileSlotEnum.PERSISTENCE_CONVERTOR, aggregateName + "Convertor.java").getRelativePath());
            pathMap.put(FileSlotEnum.PERSISTENCE_REPOSITORY_IMPL.name(), pathResolver.resolve(moduleSpec, pathStrategySpec, FileSlotEnum.PERSISTENCE_REPOSITORY_IMPL, aggregateName + "RepositoryImpl.java").getRelativePath());
        }
        if (generateSpec.isXml()) {
            pathMap.put(FileSlotEnum.MAPPER_XML.name(), pathResolver.resolveMapperXml(moduleSpec, pathStrategySpec, aggregateName + "Mapper.xml").getRelativePath());
        }
        return pathMap;
    }

    public void generate(Path rootPath, ModuleSpec moduleSpec, PathStrategySpec pathStrategySpec) throws IOException {
        String aggregateName = moduleSpec.getAggregate().getAggregateName();
        GenerateSpec generateSpec = moduleSpec.getGenerate();
        if (generateSpec.isAdmin()) {
            write(rootPath, pathResolver.resolve(moduleSpec, pathStrategySpec, FileSlotEnum.ADMIN_CONTROLLER, aggregateName + "AdminController.java"), templateRenderer.renderAdminController(moduleSpec));
            write(rootPath, pathResolver.resolve(moduleSpec, pathStrategySpec, FileSlotEnum.ADMIN_DTO, aggregateName + "ListDTO.java"), templateRenderer.renderListDTO(moduleSpec));
            write(rootPath, pathResolver.resolve(moduleSpec, pathStrategySpec, FileSlotEnum.ADMIN_DTO, aggregateName + "MainDTO.java"), templateRenderer.renderMainDTO(moduleSpec));
            write(rootPath, pathResolver.resolve(moduleSpec, pathStrategySpec, FileSlotEnum.ADMIN_DTO, aggregateName + "SaveDTO.java"), templateRenderer.renderSaveDTO(moduleSpec));
            write(rootPath, pathResolver.resolve(moduleSpec, pathStrategySpec, FileSlotEnum.ADMIN_VO, aggregateName + "ListItemVO.java"), templateRenderer.renderListItemVO(moduleSpec));
            write(rootPath, pathResolver.resolve(moduleSpec, pathStrategySpec, FileSlotEnum.ADMIN_VO, aggregateName + "SaveItemVO.java"), templateRenderer.renderSaveItemVO(moduleSpec));
            write(rootPath, pathResolver.resolve(moduleSpec, pathStrategySpec, FileSlotEnum.ADMIN_VO, aggregateName + "DetailVO.java"), templateRenderer.renderDetailVO(moduleSpec));
        }
        if (generateSpec.isApplication()) {
            write(rootPath, pathResolver.resolve(moduleSpec, pathStrategySpec, FileSlotEnum.APP_SERVICE, aggregateName + "AdminAppService.java"), templateRenderer.renderAppService(moduleSpec));
            write(rootPath, pathResolver.resolve(moduleSpec, pathStrategySpec, FileSlotEnum.APP_SERVICE_IMPL, aggregateName + "AdminAppServiceImpl.java"), templateRenderer.renderAppServiceImpl(moduleSpec));
            write(rootPath, pathResolver.resolve(moduleSpec, pathStrategySpec, FileSlotEnum.APP_ASSEMBLER, aggregateName + "AdminAssembler.java"), templateRenderer.renderAdminAssembler(moduleSpec));
        }
        if (generateSpec.isDomain()) {
            write(rootPath, pathResolver.resolve(moduleSpec, pathStrategySpec, FileSlotEnum.DOMAIN_MODEL, aggregateName + ".java"), templateRenderer.renderDomainModel(moduleSpec));
            write(rootPath, pathResolver.resolve(moduleSpec, pathStrategySpec, FileSlotEnum.DOMAIN_REPOSITORY, aggregateName + "Repository.java"), templateRenderer.renderRepository(moduleSpec));
        }
        if (generateSpec.isPersistence()) {
            write(rootPath, pathResolver.resolve(moduleSpec, pathStrategySpec, FileSlotEnum.PERSISTENCE_PO, aggregateName + "PO.java"), templateRenderer.renderPO(moduleSpec));
            write(rootPath, pathResolver.resolve(moduleSpec, pathStrategySpec, FileSlotEnum.PERSISTENCE_MAPPER, aggregateName + "Mapper.java"), templateRenderer.renderMapper(moduleSpec));
            write(rootPath, pathResolver.resolve(moduleSpec, pathStrategySpec, FileSlotEnum.PERSISTENCE_CONVERTOR, aggregateName + "Convertor.java"), templateRenderer.renderConvertor(moduleSpec));
            write(rootPath, pathResolver.resolve(moduleSpec, pathStrategySpec, FileSlotEnum.PERSISTENCE_REPOSITORY_IMPL, aggregateName + "RepositoryImpl.java"), templateRenderer.renderRepositoryImpl(moduleSpec));
        }
        if (generateSpec.isXml()) {
            write(rootPath, pathResolver.resolveMapperXml(moduleSpec, pathStrategySpec, aggregateName + "Mapper.xml"), templateRenderer.renderMapperXml(moduleSpec));
        }
    }

    private void write(Path rootPath, ResolvedPath resolvedPath, String content) throws IOException {
        Path targetPath = rootPath.resolve(resolvedPath.getRelativePath());
        Files.createDirectories(targetPath.getParent());
        Files.writeString(targetPath, content);
    }
}
