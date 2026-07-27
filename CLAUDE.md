# Claude Rules

## 项目约束
- 项目架构DDD领域驱动设计
- 项目结构参考`docs/base/项目业务module导航.md` `docs/base/项目顶部和底部module导航.md`
- 前端仓库位置/Users/bomfish/xbb-ai-erp-v2-front
- 技术栈
  - 运行时：`JDK 21`
  - 应用框架：`Spring Boot 3.3.2`
  - 构建工具：`Maven`
  - Web：`Spring Web`
  - ORM：`MyBatis-Plus`
  - 数据库：本地`MySQL 5.6` 其余环境`MySQL 8.0`
  - 数据库迁移：`Flyway`
  - 缓存：`Redis 7`
  - 日志：`Logback`
  - 测试：`JUnit 5 + Testcontainers`

## agent约束
- 对话永远在中文语境下，注释使用中文
- 主动询问我，代码修改是否要创建worktree
- 每个claude任务确认完成后，都执行技能`.claude/commands/gen-api-md/SKILL.md`


## 业务约束
- 直接对接数据库的对象实体需要添加PO后缀，并且对象内字段不允许使用布尔值对接，改用Integer;
- 枚举类需要Enum结尾
- 系统内pojo尾缀规范：对接前端入参DTO、对接接口出参VO。其余中转参数的对象Pojo
- 所有接口接口DTO作为参数，而不是散列的参数。非脚本接口，入参DTO都需要继承BaseDTO 
- userId 员工Id是字符串id
- getter setter用Lombok管理
- 如果接口业务代码没有需要返回的，用BaseVO返回
- 所有接口的参数返回，都使用ResultVO.success()包装返回