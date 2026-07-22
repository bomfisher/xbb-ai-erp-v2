# ERP 初始化 Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 初始化 `xbb-erp-base-*` 与 `xbb-erp-app-*` 共 12 个模块，形成可构建、可测试、可独立启动的 ERP 后端最小可运行骨架。

**Architecture:** 根工程作为聚合父工程统一管理依赖、插件和模块列表；9 个 `xbb-erp-base-*` 提供公共技术底座，3 个 `xbb-erp-app-*` 作为独立 Spring Boot 装配入口。数据库与 Redis 采用真实接线，Flyway 本期不接入，日志由 `xbb-erp-base-log` 统一提供本地 `DEBUG`、其他环境 `INFO` 的 `logback` 配置。

**Tech Stack:** JDK 21、Spring Boot 3.3.2、Maven、多模块工程、Spring Web、MyBatis-Plus、MySQL、Redis、Logback、JUnit 5、Testcontainers、Spring Boot Actuator。

## Global Constraints

- 对话永远在中文语境下。
- 项目架构为 DDD 领域驱动设计，遵循 `docs/base/后端项目框架设计原则.md`。
- 本期只初始化 `xbb-erp-base-*` 与 `xbb-erp-app-*`，不初始化 `xbb-erp-module-*`、`xbb-erp-process-*`、`xbb-erp-ext-*`。
- 包根名统一为 `xbb.ai.erp`。
- 运行时固定为 `JDK 21`。
- 应用框架固定为 `Spring Boot 3.3.2`。
- 构建工具固定为 `Maven`。
- Web 固定为 `Spring Web`。
- ORM 固定为 `MyBatis-Plus`。
- 数据库为本地 `MySQL 5.6`，其余环境 `MySQL 8.0`。
- 缓存固定为 `Redis 7`。
- 日志固定为 `Logback`。
- 测试固定为 `JUnit 5 + Testcontainers`。
- 本版本不对接 Flyway。
- 直接对接数据库的对象实体需要添加 `Entity` 后缀。
- 直接对接数据库的字段不允许使用布尔值对接，改用 `Integer`。
- 枚举类需要 `Enum` 结尾。
- 系统内 pojo 尾缀规范：前端入参对象 `DTO`、接口出参对象 `VO`、其余中转参数对象 `Pojo`。
- 所有接口 DTO 作为参数，而不是散列参数。
- 所有非脚本类接口入参 DTO 都需要继承 `BaseDTO`。
- `BaseDTO` 必须包含 `corpid` 和 `userId`。
- 接口 URL 统一采用 `/erp/v1/{domain}/{feature-path}`；第一级固定 `erp`，第二级固定 `v1`，第三级为领域，第三级之后为功能路径。
- `xbb-erp-base-log` 统一提供日志配置；本地环境打印 `DEBUG`，其余环境打印 `INFO`。
- `xbb-erp-app-admin`、`xbb-erp-app-mobile`、`xbb-erp-app-job` 三个应用都必须可独立启动成功。
- 每个应用优先提供 `/actuator/health` 探活。

---

## File Structure

### Root / Parent
- Modify: `pom.xml` — 改造成聚合父工程 `xbb-erp-parent`，集中管理版本、插件、模块列表。
- Create: `.mvn/wrapper/maven-wrapper.properties`（仅当仓库已有 wrapper 缺失必要配置时）— 保持 Maven wrapper 可用。

### Base modules
- Create: `xbb-erp-base-common/pom.xml`
- Create: `xbb-erp-base-common/src/main/java/xbb/ai/erp/base/common/dto/BaseDTO.java`
- Create: `xbb-erp-base-common/src/main/java/xbb/ai/erp/base/common/vo/ResultVO.java`
- Create: `xbb-erp-base-common/src/main/java/xbb/ai/erp/base/common/exception/BizException.java`
- Create: `xbb-erp-base-common/src/main/java/xbb/ai/erp/base/common/enums/CommonErrorCodeEnum.java`
- Create: `xbb-erp-base-common/src/test/java/xbb/ai/erp/base/common/dto/BaseDTOTest.java`
- Create: `xbb-erp-base-web/pom.xml`
- Create: `xbb-erp-base-web/src/main/java/xbb/ai/erp/base/web/config/WebAutoConfiguration.java`
- Create: `xbb-erp-base-web/src/main/java/xbb/ai/erp/base/web/handler/GlobalExceptionHandler.java`
- Create: `xbb-erp-base-web/src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
- Create: `xbb-erp-base-security/pom.xml`
- Create: `xbb-erp-base-security/src/main/java/xbb/ai/erp/base/security/config/SecurityAutoConfiguration.java`
- Create: `xbb-erp-base-security/src/main/java/xbb/ai/erp/base/security/context/UserContext.java`
- Create: `xbb-erp-base-security/src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
- Create: `xbb-erp-base-tenant/pom.xml`
- Create: `xbb-erp-base-tenant/src/main/java/xbb/ai/erp/base/tenant/context/TenantContext.java`
- Create: `xbb-erp-base-tenant/src/main/java/xbb/ai/erp/base/tenant/config/TenantAutoConfiguration.java`
- Create: `xbb-erp-base-tenant/src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
- Create: `xbb-erp-base-persistence/pom.xml`
- Create: `xbb-erp-base-persistence/src/main/java/xbb/ai/erp/base/persistence/config/MybatisPlusAutoConfiguration.java`
- Create: `xbb-erp-base-persistence/src/main/java/xbb/ai/erp/base/persistence/entity/BaseEntity.java`
- Create: `xbb-erp-base-persistence/src/main/java/xbb/ai/erp/base/persistence/handler/AuditMetaObjectHandler.java`
- Create: `xbb-erp-base-persistence/src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
- Create: `xbb-erp-base-cache/pom.xml`
- Create: `xbb-erp-base-cache/src/main/java/xbb/ai/erp/base/cache/config/RedisAutoConfiguration.java`
- Create: `xbb-erp-base-cache/src/main/java/xbb/ai/erp/base/cache/key/CacheKeyConstants.java`
- Create: `xbb-erp-base-cache/src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
- Create: `xbb-erp-base-idgen/pom.xml`
- Create: `xbb-erp-base-idgen/src/main/java/xbb/ai/erp/base/idgen/IdGenerator.java`
- Create: `xbb-erp-base-idgen/src/main/java/xbb/ai/erp/base/idgen/DefaultIdGenerator.java`
- Create: `xbb-erp-base-idgen/src/main/java/xbb/ai/erp/base/idgen/config/IdGenAutoConfiguration.java`
- Create: `xbb-erp-base-idgen/src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
- Create: `xbb-erp-base-log/pom.xml`
- Create: `xbb-erp-base-log/src/main/resources/logback-spring.xml`
- Create: `xbb-erp-base-log/src/main/java/xbb/ai/erp/base/log/config/LogContextFilter.java`
- Create: `xbb-erp-base-log/src/main/java/xbb/ai/erp/base/log/config/LogAutoConfiguration.java`
- Create: `xbb-erp-base-log/src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
- Create: `xbb-erp-base-test/pom.xml`
- Create: `xbb-erp-base-test/src/main/java/xbb/ai/erp/base/test/container/RedisContainerSupport.java`
- Create: `xbb-erp-base-test/src/main/java/xbb/ai/erp/base/test/container/MySqlContainerSupport.java`

### App modules
- Create: `xbb-erp-app-admin/pom.xml`
- Create: `xbb-erp-app-admin/src/main/java/xbb/ai/erp/app/admin/AdminApplication.java`
- Create: `xbb-erp-app-admin/src/main/java/xbb/ai/erp/app/admin/controller/UserInfoAdminController.java`
- Create: `xbb-erp-app-admin/src/main/java/xbb/ai/erp/app/admin/dto/UserInfoQueryDTO.java`
- Create: `xbb-erp-app-admin/src/main/resources/application.yml`
- Create: `xbb-erp-app-admin/src/main/resources/application-local.yml`
- Create: `xbb-erp-app-admin/src/main/resources/application-dev.yml`
- Create: `xbb-erp-app-admin/src/main/resources/application-test.yml`
- Create: `xbb-erp-app-admin/src/main/resources/application-prod.yml`
- Create: `xbb-erp-app-admin/src/test/java/xbb/ai/erp/app/admin/AdminApplicationTest.java`
- Create: `xbb-erp-app-mobile/pom.xml`
- Create: `xbb-erp-app-mobile/src/main/java/xbb/ai/erp/app/mobile/MobileApplication.java`
- Create: `xbb-erp-app-mobile/src/main/java/xbb/ai/erp/app/mobile/controller/UserInfoMobileController.java`
- Create: `xbb-erp-app-mobile/src/main/java/xbb/ai/erp/app/mobile/dto/UserInfoQueryDTO.java`
- Create: `xbb-erp-app-mobile/src/main/resources/application.yml`
- Create: `xbb-erp-app-mobile/src/main/resources/application-local.yml`
- Create: `xbb-erp-app-mobile/src/main/resources/application-dev.yml`
- Create: `xbb-erp-app-mobile/src/main/resources/application-test.yml`
- Create: `xbb-erp-app-mobile/src/main/resources/application-prod.yml`
- Create: `xbb-erp-app-mobile/src/test/java/xbb/ai/erp/app/mobile/MobileApplicationTest.java`
- Create: `xbb-erp-app-job/pom.xml`
- Create: `xbb-erp-app-job/src/main/java/xbb/ai/erp/app/job/JobApplication.java`
- Create: `xbb-erp-app-job/src/main/java/xbb/ai/erp/app/job/controller/JobHealthController.java`
- Create: `xbb-erp-app-job/src/main/resources/application.yml`
- Create: `xbb-erp-app-job/src/main/resources/application-local.yml`
- Create: `xbb-erp-app-job/src/main/resources/application-dev.yml`
- Create: `xbb-erp-app-job/src/main/resources/application-test.yml`
- Create: `xbb-erp-app-job/src/main/resources/application-prod.yml`
- Create: `xbb-erp-app-job/src/test/java/xbb/ai/erp/app/job/JobApplicationTest.java`

### Docs
- Modify: `docs/base/项目module导航.md` — 增加 12 个模块的功能定位、责任范围、边界和依赖说明。

## Task 1: 重构父工程为多模块聚合根

**Files:**
- Modify: `pom.xml`
- Create: `xbb-erp-base-common/pom.xml`
- Create: `xbb-erp-base-web/pom.xml`
- Create: `xbb-erp-base-security/pom.xml`
- Create: `xbb-erp-base-tenant/pom.xml`
- Create: `xbb-erp-base-persistence/pom.xml`
- Create: `xbb-erp-base-cache/pom.xml`
- Create: `xbb-erp-base-idgen/pom.xml`
- Create: `xbb-erp-base-log/pom.xml`
- Create: `xbb-erp-base-test/pom.xml`
- Create: `xbb-erp-app-admin/pom.xml`
- Create: `xbb-erp-app-mobile/pom.xml`
- Create: `xbb-erp-app-job/pom.xml`
- Test: `mvn -q -pl xbb-erp-base-common -am test`

**Interfaces:**
- Consumes: 根工程现有 `pom.xml` 中的 `groupId=org.bomfish`、`artifactId=xbb-ai-erp-v2`、`version=1.0-SNAPSHOT`
- Produces:
  - 父工程坐标：`groupId=org.bomfish`、`artifactId=xbb-erp-parent`、`version=1.0-SNAPSHOT`
  - 所有子模块均继承 `org.bomfish:xbb-erp-parent:1.0-SNAPSHOT`
  - 统一属性：`java.version=21`、`spring-boot.version=3.3.2`、`mybatis-plus.version=3.5.7`

- [ ] **Step 1: 写父工程的失败测试（先让模块列表成为编译约束）**

```xml
<!-- pom.xml -->
<modules>
    <module>xbb-erp-base-common</module>
    <module>xbb-erp-base-web</module>
    <module>xbb-erp-base-security</module>
    <module>xbb-erp-base-tenant</module>
    <module>xbb-erp-base-persistence</module>
    <module>xbb-erp-base-cache</module>
    <module>xbb-erp-base-idgen</module>
    <module>xbb-erp-base-log</module>
    <module>xbb-erp-base-test</module>
    <module>xbb-erp-app-admin</module>
    <module>xbb-erp-app-mobile</module>
    <module>xbb-erp-app-job</module>
</modules>
```

- [ ] **Step 2: 运行聚合构建，确认因为子模块缺失而失败**

Run: `mvn -q test`
Expected: FAIL，错误包含 `Child module ... does not exist`

- [ ] **Step 3: 写父工程最小实现**

```xml
<!-- pom.xml -->
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.bomfish</groupId>
    <artifactId>xbb-erp-parent</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>pom</packaging>

    <modules>
        <module>xbb-erp-base-common</module>
        <module>xbb-erp-base-web</module>
        <module>xbb-erp-base-security</module>
        <module>xbb-erp-base-tenant</module>
        <module>xbb-erp-base-persistence</module>
        <module>xbb-erp-base-cache</module>
        <module>xbb-erp-base-idgen</module>
        <module>xbb-erp-base-log</module>
        <module>xbb-erp-base-test</module>
        <module>xbb-erp-app-admin</module>
        <module>xbb-erp-app-mobile</module>
        <module>xbb-erp-app-job</module>
    </modules>

    <properties>
        <java.version>21</java.version>
        <maven.compiler.source>${java.version}</maven.compiler.source>
        <maven.compiler.target>${java.version}</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <spring-boot.version>3.3.2</spring-boot.version>
        <mybatis-plus.version>3.5.7</mybatis-plus.version>
        <testcontainers.version>1.20.1</testcontainers.version>
    </properties>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>${spring-boot.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <pluginManagement>
            <plugins>
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-compiler-plugin</artifactId>
                    <version>3.13.0</version>
                </plugin>
                <plugin>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-maven-plugin</artifactId>
                    <version>${spring-boot.version}</version>
                </plugin>
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-surefire-plugin</artifactId>
                    <version>3.2.5</version>
                </plugin>
            </plugins>
        </pluginManagement>
    </build>
</project>
```

```xml
<!-- example child pom: xbb-erp-base-common/pom.xml -->
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.bomfish</groupId>
        <artifactId>xbb-erp-parent</artifactId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <artifactId>xbb-erp-base-common</artifactId>
</project>
```

- [ ] **Step 4: 运行聚合构建，确认模块骨架可被 Maven 识别**

Run: `mvn -q -pl xbb-erp-base-common -am test`
Expected: PASS，输出包含 `BUILD SUCCESS`

- [ ] **Step 5: Commit**

```bash
git add pom.xml xbb-erp-*/pom.xml
git commit -m "build: initialize multi-module parent structure"
```

## Task 2: 实现 `base-common` 与 `base-web` 的公共入参与异常骨架

**Files:**
- Create: `xbb-erp-base-common/src/main/java/xbb/ai/erp/base/common/dto/BaseDTO.java`
- Create: `xbb-erp-base-common/src/main/java/xbb/ai/erp/base/common/vo/ResultVO.java`
- Create: `xbb-erp-base-common/src/main/java/xbb/ai/erp/base/common/exception/BizException.java`
- Create: `xbb-erp-base-common/src/main/java/xbb/ai/erp/base/common/enums/CommonErrorCodeEnum.java`
- Create: `xbb-erp-base-common/src/test/java/xbb/ai/erp/base/common/dto/BaseDTOTest.java`
- Modify: `xbb-erp-base-web/pom.xml`
- Create: `xbb-erp-base-web/src/main/java/xbb/ai/erp/base/web/config/WebAutoConfiguration.java`
- Create: `xbb-erp-base-web/src/main/java/xbb/ai/erp/base/web/handler/GlobalExceptionHandler.java`
- Create: `xbb-erp-base-web/src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
- Test: `mvn -q -pl xbb-erp-base-common,xbb-erp-base-web -am test`

**Interfaces:**
- Consumes: 父工程依赖管理；`spring-boot-starter-web`
- Produces:
  - `BaseDTO { String corpid; Long userId; }`
  - `ResultVO<T> { Integer code; String message; T data; }`
  - `BizException(CommonErrorCodeEnum errorCodeEnum)`
  - `GlobalExceptionHandler#handleBizException(BizException ex): ResponseEntity<ResultVO<Void>>`

- [ ] **Step 1: 写 `BaseDTO` 的失败测试**

```java
package xbb.ai.erp.base.common.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BaseDTOTest {

    @Test
    void should_store_corpid_and_user_id() {
        BaseDTO dto = new BaseDTO();
        dto.setCorpid("corp-001");
        dto.setUserId(1001L);

        assertEquals("corp-001", dto.getCorpid());
        assertEquals(1001L, dto.getUserId());
    }
}
```

- [ ] **Step 2: 运行测试，确认 `BaseDTO` 缺失导致失败**

Run: `mvn -q -pl xbb-erp-base-common -am -Dtest=BaseDTOTest test`
Expected: FAIL，错误包含 `cannot find symbol BaseDTO`

- [ ] **Step 3: 写 `base-common` 最小实现**

```java
package xbb.ai.erp.base.common.dto;

public class BaseDTO {

    private String corpid;
    private Long userId;

    public String getCorpid() {
        return corpid;
    }

    public void setCorpid(String corpid) {
        this.corpid = corpid;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
```

```java
package xbb.ai.erp.base.common.vo;

public class ResultVO<T> {

    private Integer code;
    private String message;
    private T data;

    public static <T> ResultVO<T> success(T data) {
        ResultVO<T> result = new ResultVO<>();
        result.setCode(0);
        result.setMessage("success");
        result.setData(data);
        return result;
    }

    public static <T> ResultVO<T> failure(Integer code, String message) {
        ResultVO<T> result = new ResultVO<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public Integer getCode() { return code; }
    public void setCode(Integer code) { this.code = code; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}
```

```java
package xbb.ai.erp.base.common.enums;

public enum CommonErrorCodeEnum {
    SYSTEM_ERROR(500, "系统异常"),
    BIZ_ERROR(400, "业务异常");

    private final Integer code;
    private final String message;

    CommonErrorCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() { return code; }
    public String getMessage() { return message; }
}
```

```java
package xbb.ai.erp.base.common.exception;

import xbb.ai.erp.base.common.enums.CommonErrorCodeEnum;

public class BizException extends RuntimeException {

    private final Integer code;

    public BizException(CommonErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum.getMessage());
        this.code = errorCodeEnum.getCode();
    }

    public Integer getCode() {
        return code;
    }
}
```

```java
package xbb.ai.erp.base.web.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xbb.ai.erp.base.common.enums.CommonErrorCodeEnum;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.vo.ResultVO;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BizException.class)
    public ResponseEntity<ResultVO<Void>> handleBizException(BizException exception) {
        return ResponseEntity.badRequest().body(ResultVO.failure(exception.getCode(), exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResultVO<Void>> handleException(Exception exception) {
        return ResponseEntity.internalServerError().body(
            ResultVO.failure(CommonErrorCodeEnum.SYSTEM_ERROR.getCode(), CommonErrorCodeEnum.SYSTEM_ERROR.getMessage())
        );
    }
}
```

```java
package xbb.ai.erp.base.web.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class WebAutoConfiguration {
}
```

```text
xbb.ai.erp.base.web.config.WebAutoConfiguration
```

- [ ] **Step 4: 运行测试，确认公共入参与异常骨架通过**

Run: `mvn -q -pl xbb-erp-base-common,xbb-erp-base-web -am test`
Expected: PASS，输出包含 `BUILD SUCCESS`

- [ ] **Step 5: Commit**

```bash
git add xbb-erp-base-common xbb-erp-base-web
git commit -m "feat: add common dto and web exception skeleton"
```

## Task 3: 实现持久化、缓存、日志与测试底座

**Files:**
- Modify: `xbb-erp-base-persistence/pom.xml`
- Create: `xbb-erp-base-persistence/src/main/java/xbb/ai/erp/base/persistence/entity/BaseEntity.java`
- Create: `xbb-erp-base-persistence/src/main/java/xbb/ai/erp/base/persistence/handler/AuditMetaObjectHandler.java`
- Create: `xbb-erp-base-persistence/src/main/java/xbb/ai/erp/base/persistence/config/MybatisPlusAutoConfiguration.java`
- Create: `xbb-erp-base-persistence/src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
- Modify: `xbb-erp-base-cache/pom.xml`
- Create: `xbb-erp-base-cache/src/main/java/xbb/ai/erp/base/cache/config/RedisAutoConfiguration.java`
- Create: `xbb-erp-base-cache/src/main/java/xbb/ai/erp/base/cache/key/CacheKeyConstants.java`
- Create: `xbb-erp-base-cache/src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
- Modify: `xbb-erp-base-log/pom.xml`
- Create: `xbb-erp-base-log/src/main/resources/logback-spring.xml`
- Create: `xbb-erp-base-log/src/main/java/xbb/ai/erp/base/log/config/LogContextFilter.java`
- Create: `xbb-erp-base-log/src/main/java/xbb/ai/erp/base/log/config/LogAutoConfiguration.java`
- Create: `xbb-erp-base-log/src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
- Modify: `xbb-erp-base-test/pom.xml`
- Create: `xbb-erp-base-test/src/main/java/xbb/ai/erp/base/test/container/RedisContainerSupport.java`
- Create: `xbb-erp-base-test/src/main/java/xbb/ai/erp/base/test/container/MySqlContainerSupport.java`
- Test: `mvn -q -pl xbb-erp-base-persistence,xbb-erp-base-cache,xbb-erp-base-log,xbb-erp-base-test -am test`

**Interfaces:**
- Consumes: `ResultVO` from Task 2；Spring Data Redis；MyBatis-Plus Starter
- Produces:
  - `BaseEntity { Long id; LocalDateTime createTime; LocalDateTime updateTime; Integer deleted; }`
  - `AuditMetaObjectHandler implements MetaObjectHandler`
  - `RedisAutoConfiguration#redisTemplate(RedisConnectionFactory): RedisTemplate<String,Object>`
  - `CacheKeyConstants.USER_INFO = "erp:user:info:"`
  - `logback-spring.xml` 支持 `local=DEBUG`、其他环境 `INFO`
  - `RedisContainerSupport#redisContainer(): RedisContainer`
  - `MySqlContainerSupport#mysqlContainer(): MySQLContainer<?>`

- [ ] **Step 1: 写持久化实体和缓存 key 的失败测试**

```java
package xbb.ai.erp.base.persistence.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BaseEntityTest {

    @Test
    void should_use_integer_deleted_flag() {
        BaseEntity entity = new BaseEntity();
        entity.setDeleted(0);

        assertEquals(0, entity.getDeleted());
    }
}
```

```java
package xbb.ai.erp.base.cache.key;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CacheKeyConstantsTest {

    @Test
    void should_expose_user_info_prefix() {
        assertEquals("erp:user:info:", CacheKeyConstants.USER_INFO);
    }
}
```

- [ ] **Step 2: 运行测试，确认基础设施类缺失导致失败**

Run: `mvn -q -pl xbb-erp-base-persistence,xbb-erp-base-cache -am -Dtest=BaseEntityTest,CacheKeyConstantsTest test`
Expected: FAIL，错误包含 `cannot find symbol BaseEntity` 或 `CacheKeyConstants`

- [ ] **Step 3: 写最小基础设施实现**

```java
package xbb.ai.erp.base.persistence.entity;

import java.time.LocalDateTime;

public class BaseEntity {

    private Long id;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer deleted;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public Integer getDeleted() { return deleted; }
    public void setDeleted(Integer deleted) { this.deleted = deleted; }
}
```

```java
package xbb.ai.erp.base.persistence.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

public class AuditMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        strictInsertFill(metaObject, "deleted", Integer.class, 0);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }
}
```

```java
package xbb.ai.erp.base.persistence.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xbb.ai.erp.base.persistence.handler.AuditMetaObjectHandler;

@Configuration
public class MybatisPlusAutoConfiguration {

    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new AuditMetaObjectHandler();
    }
}
```

```java
package xbb.ai.erp.base.cache.key;

public final class CacheKeyConstants {

    public static final String USER_INFO = "erp:user:info:";

    private CacheKeyConstants() {
    }
}
```

```java
package xbb.ai.erp.base.cache.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisAutoConfiguration {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
```

```xml
<!-- xbb-erp-base-log/src/main/resources/logback-spring.xml -->
<configuration>
    <springProperty scope="context" name="activeProfile" source="spring.profiles.active" defaultValue="local"/>

    <property name="CONSOLE_PATTERN" value="%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"/>

    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>${CONSOLE_PATTERN}</pattern>
        </encoder>
    </appender>

    <if condition='property("activeProfile").contains("local")'>
        <then>
            <root level="DEBUG">
                <appender-ref ref="CONSOLE"/>
            </root>
        </then>
        <else>
            <root level="INFO">
                <appender-ref ref="CONSOLE"/>
            </root>
        </else>
    </if>
</configuration>
```

```java
package xbb.ai.erp.base.test.container;

import org.testcontainers.containers.MySQLContainer;

public abstract class MySqlContainerSupport {

    protected static MySQLContainer<?> mysqlContainer() {
        return new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("xbb_erp")
            .withUsername("test")
            .withPassword("test");
    }
}
```

```java
package xbb.ai.erp.base.test.container;

import com.redis.testcontainers.RedisContainer;
import org.testcontainers.utility.DockerImageName;

public abstract class RedisContainerSupport {

    protected static RedisContainer redisContainer() {
        return new RedisContainer(DockerImageName.parse("redis:7"));
    }
}
```

- [ ] **Step 4: 运行测试，确认基础设施骨架通过**

Run: `mvn -q -pl xbb-erp-base-persistence,xbb-erp-base-cache,xbb-erp-base-log,xbb-erp-base-test -am test`
Expected: PASS，输出包含 `BUILD SUCCESS`

- [ ] **Step 5: Commit**

```bash
git add xbb-erp-base-persistence xbb-erp-base-cache xbb-erp-base-log xbb-erp-base-test
git commit -m "feat: add persistence cache log and test base modules"
```

## Task 4: 实现租户、安全与 ID 生成基础模块

**Files:**
- Modify: `xbb-erp-base-security/pom.xml`
- Create: `xbb-erp-base-security/src/main/java/xbb/ai/erp/base/security/context/UserContext.java`
- Create: `xbb-erp-base-security/src/main/java/xbb/ai/erp/base/security/config/SecurityAutoConfiguration.java`
- Create: `xbb-erp-base-security/src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
- Modify: `xbb-erp-base-tenant/pom.xml`
- Create: `xbb-erp-base-tenant/src/main/java/xbb/ai/erp/base/tenant/context/TenantContext.java`
- Create: `xbb-erp-base-tenant/src/main/java/xbb/ai/erp/base/tenant/config/TenantAutoConfiguration.java`
- Create: `xbb-erp-base-tenant/src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
- Modify: `xbb-erp-base-idgen/pom.xml`
- Create: `xbb-erp-base-idgen/src/main/java/xbb/ai/erp/base/idgen/IdGenerator.java`
- Create: `xbb-erp-base-idgen/src/main/java/xbb/ai/erp/base/idgen/DefaultIdGenerator.java`
- Create: `xbb-erp-base-idgen/src/main/java/xbb/ai/erp/base/idgen/config/IdGenAutoConfiguration.java`
- Create: `xbb-erp-base-idgen/src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
- Test: `mvn -q -pl xbb-erp-base-security,xbb-erp-base-tenant,xbb-erp-base-idgen -am test`

**Interfaces:**
- Consumes: Spring Boot 自动配置机制
- Produces:
  - `UserContext { Long userId; String username; }`
  - `TenantContext { String corpid; }`
  - `IdGenerator#nextId(): Long`
  - `DefaultIdGenerator#nextId(): Long`

- [ ] **Step 1: 写 ID 生成器的失败测试**

```java
package xbb.ai.erp.base.idgen;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultIdGeneratorTest {

    @Test
    void should_generate_positive_id() {
        IdGenerator generator = new DefaultIdGenerator();
        Long id = generator.nextId();

        assertNotNull(id);
        assertTrue(id > 0);
    }
}
```

- [ ] **Step 2: 运行测试，确认 ID 模块缺失导致失败**

Run: `mvn -q -pl xbb-erp-base-idgen -am -Dtest=DefaultIdGeneratorTest test`
Expected: FAIL，错误包含 `cannot find symbol IdGenerator`

- [ ] **Step 3: 写租户、安全、ID 最小实现**

```java
package xbb.ai.erp.base.security.context;

public class UserContext {

    private Long userId;
    private String username;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
```

```java
package xbb.ai.erp.base.tenant.context;

public class TenantContext {

    private String corpid;

    public String getCorpid() { return corpid; }
    public void setCorpid(String corpid) { this.corpid = corpid; }
}
```

```java
package xbb.ai.erp.base.idgen;

public interface IdGenerator {

    Long nextId();
}
```

```java
package xbb.ai.erp.base.idgen;

import java.util.concurrent.atomic.AtomicLong;

public class DefaultIdGenerator implements IdGenerator {

    private static final AtomicLong COUNTER = new AtomicLong(System.currentTimeMillis());

    @Override
    public Long nextId() {
        return COUNTER.incrementAndGet();
    }
}
```

```java
package xbb.ai.erp.base.idgen.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xbb.ai.erp.base.idgen.DefaultIdGenerator;
import xbb.ai.erp.base.idgen.IdGenerator;

@Configuration
public class IdGenAutoConfiguration {

    @Bean
    public IdGenerator idGenerator() {
        return new DefaultIdGenerator();
    }
}
```

```java
package xbb.ai.erp.base.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Bean;

@Configuration
public class SecurityAutoConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/actuator/**", "/erp/v1/user/info", "/erp/v1/job/health").permitAll()
                .anyRequest().authenticated()
            )
            .httpBasic(httpBasic -> {});
        return httpSecurity.build();
    }
}
```

```java
package xbb.ai.erp.base.tenant.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class TenantAutoConfiguration {
}
```

- [ ] **Step 4: 运行测试，确认三个基础模块通过**

Run: `mvn -q -pl xbb-erp-base-security,xbb-erp-base-tenant,xbb-erp-base-idgen -am test`
Expected: PASS，输出包含 `BUILD SUCCESS`

- [ ] **Step 5: Commit**

```bash
git add xbb-erp-base-security xbb-erp-base-tenant xbb-erp-base-idgen
git commit -m "feat: add security tenant and id generator skeleton"
```

## Task 5: 初始化 `app-admin` 为可启动后台入口

**Files:**
- Modify: `xbb-erp-app-admin/pom.xml`
- Create: `xbb-erp-app-admin/src/main/java/xbb/ai/erp/app/admin/AdminApplication.java`
- Create: `xbb-erp-app-admin/src/main/java/xbb/ai/erp/app/admin/dto/UserInfoQueryDTO.java`
- Create: `xbb-erp-app-admin/src/main/java/xbb/ai/erp/app/admin/controller/UserInfoAdminController.java`
- Create: `xbb-erp-app-admin/src/main/resources/application.yml`
- Create: `xbb-erp-app-admin/src/main/resources/application-local.yml`
- Create: `xbb-erp-app-admin/src/main/resources/application-dev.yml`
- Create: `xbb-erp-app-admin/src/main/resources/application-test.yml`
- Create: `xbb-erp-app-admin/src/main/resources/application-prod.yml`
- Create: `xbb-erp-app-admin/src/test/java/xbb/ai/erp/app/admin/AdminApplicationTest.java`
- Test: `mvn -q -pl xbb-erp-app-admin -am test`

**Interfaces:**
- Consumes:
  - `BaseDTO` from Task 2
  - `ResultVO` from Task 2
  - `IdGenerator` from Task 4
- Produces:
  - `AdminApplication`
  - `UserInfoQueryDTO extends BaseDTO`
  - `GET /erp/v1/user/info`
  - `AdminApplicationTest#contextLoads()`

- [ ] **Step 1: 写应用启动测试和 URL/DTO 约束测试**

```java
package xbb.ai.erp.app.admin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AdminApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
    }

    @Test
    void should_expose_user_info_endpoint_under_erp_v1_domain_path() throws Exception {
        mockMvc.perform(get("/erp/v1/user/info"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(0));
    }
}
```

- [ ] **Step 2: 运行测试，确认 `AdminApplication` 缺失导致失败**

Run: `mvn -q -pl xbb-erp-app-admin -am -Dtest=AdminApplicationTest test`
Expected: FAIL，错误包含 `Unable to find a @SpringBootConfiguration`

- [ ] **Step 3: 写 `app-admin` 最小实现**

```java
package xbb.ai.erp.app.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "xbb.ai.erp")
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }
}
```

```java
package xbb.ai.erp.app.admin.dto;

import xbb.ai.erp.base.common.dto.BaseDTO;

public class UserInfoQueryDTO extends BaseDTO {
}
```

```java
package xbb.ai.erp.app.admin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xbb.ai.erp.base.common.vo.ResultVO;

import java.util.Map;

@RestController
public class UserInfoAdminController {

    @GetMapping("/erp/v1/user/info")
    public ResultVO<Map<String, Object>> userInfo() {
        return ResultVO.success(Map.of("app", "admin", "status", "ok"));
    }
}
```

```yaml
# application.yml
spring:
  application:
    name: xbb-erp-app-admin
  profiles:
    active: local
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/xbb_erp?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver
  data:
    redis:
      host: 127.0.0.1
      port: 6379
management:
  endpoints:
    web:
      exposure:
        include: health
```

```yaml
# application-local.yml
logging:
  level:
    root: DEBUG
```

```yaml
# application-dev.yml
logging:
  level:
    root: INFO
```

```yaml
# application-test.yml
logging:
  level:
    root: INFO
```

```yaml
# application-prod.yml
logging:
  level:
    root: INFO
```

- [ ] **Step 4: 运行测试，确认 `app-admin` 可启动且 URL 合规**

Run: `mvn -q -pl xbb-erp-app-admin -am test`
Expected: PASS，输出包含 `BUILD SUCCESS`

- [ ] **Step 5: Commit**

```bash
git add xbb-erp-app-admin
git commit -m "feat: initialize admin application skeleton"
```

## Task 6: 初始化 `app-mobile` 与 `app-job` 为可启动入口

**Files:**
- Modify: `xbb-erp-app-mobile/pom.xml`
- Create: `xbb-erp-app-mobile/src/main/java/xbb/ai/erp/app/mobile/MobileApplication.java`
- Create: `xbb-erp-app-mobile/src/main/java/xbb/ai/erp/app/mobile/dto/UserInfoQueryDTO.java`
- Create: `xbb-erp-app-mobile/src/main/java/xbb/ai/erp/app/mobile/controller/UserInfoMobileController.java`
- Create: `xbb-erp-app-mobile/src/main/resources/application.yml`
- Create: `xbb-erp-app-mobile/src/main/resources/application-local.yml`
- Create: `xbb-erp-app-mobile/src/main/resources/application-dev.yml`
- Create: `xbb-erp-app-mobile/src/main/resources/application-test.yml`
- Create: `xbb-erp-app-mobile/src/main/resources/application-prod.yml`
- Create: `xbb-erp-app-mobile/src/test/java/xbb/ai/erp/app/mobile/MobileApplicationTest.java`
- Modify: `xbb-erp-app-job/pom.xml`
- Create: `xbb-erp-app-job/src/main/java/xbb/ai/erp/app/job/JobApplication.java`
- Create: `xbb-erp-app-job/src/main/java/xbb/ai/erp/app/job/controller/JobHealthController.java`
- Create: `xbb-erp-app-job/src/main/resources/application.yml`
- Create: `xbb-erp-app-job/src/main/resources/application-local.yml`
- Create: `xbb-erp-app-job/src/main/resources/application-dev.yml`
- Create: `xbb-erp-app-job/src/main/resources/application-test.yml`
- Create: `xbb-erp-app-job/src/main/resources/application-prod.yml`
- Create: `xbb-erp-app-job/src/test/java/xbb/ai/erp/app/job/JobApplicationTest.java`
- Test: `mvn -q -pl xbb-erp-app-mobile,xbb-erp-app-job -am test`

**Interfaces:**
- Consumes:
  - `BaseDTO` from Task 2
  - `ResultVO` from Task 2
- Produces:
  - `MobileApplication`
  - `JobApplication`
  - `GET /erp/v1/user/info` in mobile app
  - `GET /erp/v1/job/health` in job app

- [ ] **Step 1: 写 `mobile` 与 `job` 的失败测试**

```java
package xbb.ai.erp.app.mobile;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MobileApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void should_expose_mobile_user_info_endpoint() throws Exception {
        mockMvc.perform(get("/erp/v1/user/info"))
            .andExpect(status().isOk());
    }
}
```

```java
package xbb.ai.erp.app.job;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class JobApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void should_expose_job_health_endpoint() throws Exception {
        mockMvc.perform(get("/erp/v1/job/health"))
            .andExpect(status().isOk());
    }
}
```

- [ ] **Step 2: 运行测试，确认两个应用类缺失导致失败**

Run: `mvn -q -pl xbb-erp-app-mobile,xbb-erp-app-job -am -Dtest=MobileApplicationTest,JobApplicationTest test`
Expected: FAIL，错误包含 `Unable to find a @SpringBootConfiguration`

- [ ] **Step 3: 写 `mobile` 与 `job` 最小实现**

```java
package xbb.ai.erp.app.mobile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "xbb.ai.erp")
public class MobileApplication {

    public static void main(String[] args) {
        SpringApplication.run(MobileApplication.class, args);
    }
}
```

```java
package xbb.ai.erp.app.mobile.dto;

import xbb.ai.erp.base.common.dto.BaseDTO;

public class UserInfoQueryDTO extends BaseDTO {
}
```

```java
package xbb.ai.erp.app.mobile.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xbb.ai.erp.base.common.vo.ResultVO;

import java.util.Map;

@RestController
public class UserInfoMobileController {

    @GetMapping("/erp/v1/user/info")
    public ResultVO<Map<String, Object>> userInfo() {
        return ResultVO.success(Map.of("app", "mobile", "status", "ok"));
    }
}
```

```java
package xbb.ai.erp.app.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "xbb.ai.erp")
public class JobApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobApplication.class, args);
    }
}
```

```java
package xbb.ai.erp.app.job.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xbb.ai.erp.base.common.vo.ResultVO;

import java.util.Map;

@RestController
public class JobHealthController {

    @GetMapping("/erp/v1/job/health")
    public ResultVO<Map<String, Object>> health() {
        return ResultVO.success(Map.of("app", "job", "status", "ok"));
    }
}
```

```yaml
# mobile/application.yml and job/application.yml use the same structure as admin/application.yml,
# with spring.application.name set to xbb-erp-app-mobile / xbb-erp-app-job respectively.
spring:
  profiles:
    active: local
management:
  endpoints:
    web:
      exposure:
        include: health
```

- [ ] **Step 4: 运行测试，确认两个应用都能独立启动**

Run: `mvn -q -pl xbb-erp-app-mobile,xbb-erp-app-job -am test`
Expected: PASS，输出包含 `BUILD SUCCESS`

- [ ] **Step 5: Commit**

```bash
git add xbb-erp-app-mobile xbb-erp-app-job
git commit -m "feat: initialize mobile and job application skeletons"
```

## Task 7: 补全文档导航并完成全量验证

**Files:**
- Modify: `docs/base/项目module导航.md`
- Test: `mvn -q test`
- Test: `mvn -q -pl xbb-erp-app-admin spring-boot:run`
- Test: `mvn -q -pl xbb-erp-app-mobile spring-boot:run`
- Test: `mvn -q -pl xbb-erp-app-job spring-boot:run`

**Interfaces:**
- Consumes: 前 6 个任务产生的模块、启动类、URL 规范、DTO 规范
- Produces:
  - `docs/base/项目module导航.md` 中 12 个模块的导航说明
  - 全量构建通过
  - 三个应用均可独立启动的验证记录

- [ ] **Step 1: 写文档变更的失败检查（确认导航内容尚未覆盖 12 个模块）**

```markdown
## 导航
- xbb-erp-base-common
- xbb-erp-base-web
- xbb-erp-base-security
- xbb-erp-base-tenant
- xbb-erp-base-persistence
- xbb-erp-base-cache
- xbb-erp-base-idgen
- xbb-erp-base-log
- xbb-erp-base-test
- xbb-erp-app-admin
- xbb-erp-app-mobile
- xbb-erp-app-job
```

- [ ] **Step 2: 运行检查，确认文档尚未包含完整导航**

Run: `rg -n "xbb-erp-app-admin|xbb-erp-base-common" docs/base/项目module导航.md`
Expected: FAIL 或仅命中极少内容，说明导航仍未补全

- [ ] **Step 3: 写导航文档与最终验证命令**

```markdown
## 导航

### xbb-erp-base-*

#### xbb-erp-base-common
- 功能定位：最底层公共基础能力模块
- 责任范围：统一返回对象、基础异常、错误码基类、BaseDTO、通用常量
- 不负责什么：不承载业务规则、不承载 Web/缓存/持久化实现
- 当前依赖与被谁依赖：作为其他 base/app 模块的通用依赖

#### xbb-erp-base-web
- 功能定位：Web 协议层公共能力模块
- 责任范围：统一异常处理、统一返回包装、基础 MVC 配置
- 不负责什么：不承载业务 Controller 与业务规则
- 当前依赖与被谁依赖：依赖 xbb-erp-base-common，被三个 app 入口复用

#### xbb-erp-base-security
- 功能定位：安全基础能力模块
- 责任范围：最小安全配置、放行策略、用户上下文占位
- 不负责什么：不承载复杂鉴权流程与业务权限模型
- 当前依赖与被谁依赖：依赖 xbb-erp-base-common，被三个 app 入口复用

#### xbb-erp-base-tenant
- 功能定位：租户上下文基础模块
- 责任范围：TenantContext、基础租户解析透传骨架
- 不负责什么：不承载业务租户隔离规则
- 当前依赖与被谁依赖：依赖 xbb-erp-base-common，被三个 app 入口复用

#### xbb-erp-base-persistence
- 功能定位：持久化基础模块
- 责任范围：数据源、MyBatis-Plus、审计字段填充骨架
- 不负责什么：不承载业务仓储与业务 Mapper，不接 Flyway
- 当前依赖与被谁依赖：依赖 xbb-erp-base-common，被三个 app 入口复用

#### xbb-erp-base-cache
- 功能定位：缓存基础模块
- 责任范围：Redis 装配、RedisTemplate、缓存 Key 规范骨架
- 不负责什么：不承载业务缓存策略
- 当前依赖与被谁依赖：依赖 xbb-erp-base-common，被三个 app 入口复用

#### xbb-erp-base-idgen
- 功能定位：基础 ID 生成模块
- 责任范围：ID 生成接口与默认实现
- 不负责什么：不承载业务单号规则
- 当前依赖与被谁依赖：依赖 xbb-erp-base-common，被三个 app 入口复用

#### xbb-erp-base-log
- 功能定位：统一日志基础模块
- 责任范围：logback 配置、日志上下文基础能力
- 不负责什么：不承载业务审计规则
- 当前依赖与被谁依赖：依赖 xbb-erp-base-common，被三个 app 入口复用

#### xbb-erp-base-test
- 功能定位：公共测试底座模块
- 责任范围：JUnit 5 + Testcontainers 公共基座
- 不负责什么：不承载业务测试用例
- 当前依赖与被谁依赖：供后续 base/module/app 测试复用

### xbb-erp-app-*

#### xbb-erp-app-admin
- 功能定位：PC 管理后台启动装配入口
- 责任范围：AdminApplication、配置装配、健康检查、示例接口
- 不负责什么：不沉淀业务规则
- 当前依赖与被谁依赖：依赖所需 base 模块，不被其他 app 依赖

#### xbb-erp-app-mobile
- 功能定位：移动端/BFF 启动装配入口
- 责任范围：MobileApplication、配置装配、健康检查、示例接口
- 不负责什么：不复制独立业务模型
- 当前依赖与被谁依赖：依赖所需 base 模块，不被其他 app 依赖

#### xbb-erp-app-job
- 功能定位：任务与后台执行启动装配入口
- 责任范围：JobApplication、配置装配、健康检查
- 不负责什么：本期不接入具体定时任务
- 当前依赖与被谁依赖：依赖所需 base 模块，不被其他 app 依赖
```

- [ ] **Step 4: 运行全量验证，确认构建与启动都通过**

Run: `mvn -q test`
Expected: PASS，输出包含 `BUILD SUCCESS`

Run: `mvn -q -pl xbb-erp-app-admin spring-boot:run -Dspring-boot.run.profiles=local`
Expected: 应用启动成功，日志包含 `Started AdminApplication`

Run: `mvn -q -pl xbb-erp-app-mobile spring-boot:run -Dspring-boot.run.profiles=local`
Expected: 应用启动成功，日志包含 `Started MobileApplication`

Run: `mvn -q -pl xbb-erp-app-job spring-boot:run -Dspring-boot.run.profiles=local`
Expected: 应用启动成功，日志包含 `Started JobApplication`

- [ ] **Step 5: Commit**

```bash
git add docs/base/项目module导航.md
git commit -m "docs: record initialized base and app module boundaries"
```
