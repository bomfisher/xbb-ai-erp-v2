package xbb.ai.erp.codegen.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import xbb.ai.erp.codegen.generator.CodeGenerator;
import xbb.ai.erp.codegen.generator.DddFilePlan;
import xbb.ai.erp.codegen.generator.DddGenerationContext;
import xbb.ai.erp.codegen.generator.DddGenerationReport;
import xbb.ai.erp.codegen.generator.DddModuleLayoutPlanner;
import xbb.ai.erp.codegen.spec.AggregateSpec;
import xbb.ai.erp.codegen.spec.FieldSpec;
import xbb.ai.erp.codegen.spec.GenerateSpec;
import xbb.ai.erp.codegen.spec.ModuleSpec;
import xbb.ai.erp.codegen.spec.SpecValidator;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class DbTableCodegenCli {

    private static final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/your_db?useUnicode=true&characterEncoding=UTF-8";
    private static final String JDBC_USERNAME = "root";
    private static final String JDBC_PASSWORD = "root";

    private static final Path YAML_OUTPUT_DIR = Path.of("src/main/resources/examples");

    private static final String MODULE_CODE = "demo";
    private static final String MODULE_NAME = "演示模块";
    private static final String PACKAGE_BASE = "xbb.ai.erp.module.demo";
    private static final Path CODE_OUTPUT_ROOT = Path.of(".");
    private static final List<String> TABLE_NAMES = List.of(
        "demo_table"
    );
//    private static final Set<String> NON_EDITABLE_FIELD_NAMES = Set.of(
//        "id", "corpid", "deleted", "addTime", "updateTime", "creatorId", "modifyId"
//    );

    public static void main(String[] args) throws Exception {
        System.out.println("jdbcUrl=" + JDBC_URL);
        System.out.println("tableNames=" + TABLE_NAMES);
        System.out.println("yamlOutputDir=" + YAML_OUTPUT_DIR.toAbsolutePath());
        System.out.println("codeOutputRoot=" + CODE_OUTPUT_ROOT.toAbsolutePath());
        System.out.println("moduleCode=" + MODULE_CODE);
        System.out.println("packageBase=" + PACKAGE_BASE);

        CodeGenerator codeGenerator = new CodeGenerator();
        int yamlSuccess = 0;
        int codegenSuccess = 0;
        int codegenSkipped = 0;
        int failed = 0;

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD)) {
            for (String tableName : TABLE_NAMES) {
                try {
                    TableMeta tableMeta = loadTableMeta(connection, tableName);
                    Path yamlPath = writeYaml(tableMeta, MODULE_CODE, MODULE_NAME, PACKAGE_BASE, YAML_OUTPUT_DIR);
                    yamlSuccess++;
                    System.out.println("generated yaml: " + yamlPath.toAbsolutePath());

                    ModuleSpec moduleSpec = new SpecValidatorAwareModuleSpecLoader().load(yamlPath);
                    boolean generated = generateToModuleRoot(CODE_OUTPUT_ROOT, moduleSpec, codeGenerator);
                    if (generated) {
                        codegenSuccess++;
                    } else {
                        codegenSkipped++;
                    }
                } catch (Exception exception) {
                    failed++;
                    System.out.println("failed table: " + tableName + ", reason=" + exception.getMessage());
                }
            }
        }

        System.out.println("success yaml: " + yamlSuccess);
        System.out.println("skip codegen: " + codegenSkipped);
        System.out.println("success codegen: " + codegenSuccess);
        System.out.println("failed: " + failed);
    }

    public static Path writeYaml(TableMeta tableMeta, String moduleCode, String moduleName, String packageBase, Path yamlOutputDir) throws IOException {
        ModuleSpec moduleSpec = toModuleSpec(tableMeta, moduleCode, moduleName, packageBase);
        Files.createDirectories(yamlOutputDir);
        Path yamlPath = yamlOutputDir.resolve(tableMeta.tableName() + ".yaml");
        Files.deleteIfExists(yamlPath);
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        objectMapper.writeValue(yamlPath.toFile(), moduleSpec);
        return yamlPath;
    }

    public static boolean generateToModuleRoot(Path moduleRootDir, ModuleSpec moduleSpec, CodeGenerator codeGenerator) throws Exception {
        DddGenerationContext context = DddGenerationContext.create(moduleRootDir, moduleSpec, "full");
        List<DddFilePlan> plans = new DddModuleLayoutPlanner().plan(context);
        DddGenerationReport report = codeGenerator.generate(context, plans);
        printReport(context, report);
        return !report.generatedFiles().isEmpty();
    }

    static ModuleSpec toModuleSpec(TableMeta tableMeta, String moduleCode, String moduleName, String packageBase) {
        ModuleSpec moduleSpec = new ModuleSpec();
        moduleSpec.setModuleCode(moduleCode);
        moduleSpec.setModuleName(moduleName);
        moduleSpec.setPackageBase(packageBase);
        moduleSpec.setPathStrategy("ddd-mybatis-plus");

        AggregateSpec aggregateSpec = new AggregateSpec();
        aggregateSpec.setAggregateName(toUpperCamel(tableMeta.tableName()));
        aggregateSpec.setTableName(tableMeta.tableName());
        aggregateSpec.setFields(tableMeta.columns().stream().map(DbTableCodegenCli::toFieldSpec).toList());
        moduleSpec.setAggregate(aggregateSpec);

        GenerateSpec generateSpec = new GenerateSpec();
        generateSpec.setAdmin(true);
        generateSpec.setApplication(true);
        generateSpec.setDomain(true);
        generateSpec.setPersistence(true);
        generateSpec.setXml(true);
        moduleSpec.setGenerate(generateSpec);
        return moduleSpec;
    }

    static FieldSpec toFieldSpec(ColumnMeta columnMeta) {
        FieldSpec fieldSpec = new FieldSpec();
        String fieldName = toLowerCamel(columnMeta.columnName());
        fieldSpec.setName(fieldName);
        fieldSpec.setColumn("deleted".equals(fieldName) ? "del" : columnMeta.columnName());
        fieldSpec.setJavaType(resolveJavaType(columnMeta));
        fieldSpec.setPrimaryKey(columnMeta.primaryKey());
        fieldSpec.setComment(columnMeta.comment());
        fieldSpec.setQueryable(isQueryable(fieldName, fieldSpec.getJavaType()));
        fieldSpec.setVisibleInList(isVisibleInList(fieldName));
        fieldSpec.setVisibleInDetail(isVisibleInDetail(fieldName));
        return fieldSpec;
    }

    static TableMeta loadTableMeta(Connection connection, String tableName) throws Exception {
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        Set<String> primaryKeys = new LinkedHashSet<>();
        try (ResultSet primaryKeyResultSet = databaseMetaData.getPrimaryKeys(connection.getCatalog(), null, tableName)) {
            while (primaryKeyResultSet.next()) {
                primaryKeys.add(primaryKeyResultSet.getString("COLUMN_NAME"));
            }
        }
        List<ColumnMeta> columns = new ArrayList<>();
        try (ResultSet columnsResultSet = databaseMetaData.getColumns(connection.getCatalog(), null, tableName, null)) {
            while (columnsResultSet.next()) {
                String columnName = columnsResultSet.getString("COLUMN_NAME");
                String typeName = columnsResultSet.getString("TYPE_NAME");
                String remarks = columnsResultSet.getString("REMARKS");
                int nullable = columnsResultSet.getInt("NULLABLE");
                int jdbcType = columnsResultSet.getInt("DATA_TYPE");
                columns.add(new ColumnMeta(
                    columnName,
                    normalizeTypeName(typeName),
                    remarks == null ? "" : remarks,
                    nullable == DatabaseMetaData.columnNullable,
                    primaryKeys.contains(columnName),
                    jdbcType
                ));
            }
        }
        columns.sort(Comparator.comparing(ColumnMeta::columnName));
        return new TableMeta(tableName, columns);
    }

    static String toUpperCamel(String tableName) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String part : tableName.split("_")) {
            if (part.isBlank()) {
                continue;
            }
            String normalized = part.toLowerCase(Locale.ROOT);
            stringBuilder.append(Character.toUpperCase(normalized.charAt(0))).append(normalized.substring(1));
        }
        return stringBuilder.toString();
    }

    static String toLowerCamel(String columnName) {
        String upperCamel = toUpperCamel(columnName);
        if (upperCamel.isBlank()) {
            return upperCamel;
        }
        return Character.toLowerCase(upperCamel.charAt(0)) + upperCamel.substring(1);
    }

    private static String resolveJavaType(ColumnMeta columnMeta) {
        String typeName = columnMeta.typeName().toLowerCase(Locale.ROOT);
        if (typeName.contains("tinyint") || typeName.contains("smallint") || typeName.contains("int")) {
            return "Integer";
        }
        if (typeName.contains("bigint")) {
            return "Long";
        }
        if (typeName.contains("decimal") || typeName.contains("numeric")) {
            return BigDecimal.class.getName();
        }
        if (typeName.contains("datetime") || typeName.contains("timestamp")) {
            return LocalDateTime.class.getSimpleName();
        }
        if (typeName.equals("date")) {
            return LocalDate.class.getSimpleName();
        }
        if (typeName.contains("bit") || typeName.contains("bool")) {
            return "Integer";
        }
        return "String";
    }

    private static boolean isQueryable(String fieldName, String javaType) {
        if (List.of("addTime", "updateTime").contains(fieldName)) {
            return false;
        }
        return "String".equals(javaType) || "Long".equals(javaType) || "Integer".equals(javaType);
    }

    private static boolean isVisibleInList(String fieldName) {
        return !List.of("creatorId", "modifyId", "deleted").contains(fieldName);
    }

    private static boolean isVisibleInDetail(String fieldName) {
        return !List.of("creatorId", "modifyId", "deleted").contains(fieldName);
    }

    private static String normalizeTypeName(String typeName) {
        if (typeName == null) {
            return "varchar";
        }
        return typeName.toLowerCase(Locale.ROOT);
    }

    private static void printReport(DddGenerationContext context, DddGenerationReport report) {
        System.out.println("moduleRootDir=" + context.moduleRootDir().toAbsolutePath());
        System.out.println("basePackage=" + context.basePackage());
        System.out.println("aggregateName=" + context.aggregateName());
        System.out.println("Created directories");
        report.createdDirectories().forEach(path -> System.out.println("  + " + path.toAbsolutePath()));
        System.out.println("Generated files");
        report.generatedFiles().forEach(path -> System.out.println("  + " + path.toAbsolutePath()));
        System.out.println("Skipped existing files");
        report.skippedFiles().forEach(path -> System.out.println("  - " + path.toAbsolutePath()));
        System.out.println("Failed files");
        report.failedFiles().forEach(path -> System.out.println("  ! " + path.toAbsolutePath()));
    }

    private static final class SpecValidatorAwareModuleSpecLoader {

        ModuleSpec load(Path yamlPath) throws IOException {
            ModuleSpec moduleSpec = new xbb.ai.erp.codegen.spec.ModuleSpecLoader().load(yamlPath);
            new SpecValidator().validate(moduleSpec);
            return moduleSpec;
        }
    }

    public record TableMeta(String tableName, List<ColumnMeta> columns) {
    }

    public record ColumnMeta(String columnName, String typeName, String comment, boolean nullable, boolean primaryKey) {
        public ColumnMeta(String columnName, String typeName, String comment, boolean nullable, boolean primaryKey, int jdbcType) {
            this(columnName, typeName, comment, nullable, primaryKey);
        }
    }
}
