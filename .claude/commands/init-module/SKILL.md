---
description: 用户提供了完整的module初始化资料时执行
---


## 适用场景
当用户提供了完整的module初始化资料，并且要求就行module初始化时执行


## 执行流程
- module项目包的前缀是xbb-erp-module，和用户确认当前要创建的module包的业务名，例如销售管理，用户提供了sales，那项目包名是xbb-erp-module-sales
- 包名查重，重复返回第一步
- 完成项目初始化创建，仅生成package结构，不创建任何文件，详见`.claude/commands/init-module/module-demo.md`
- 根据提供的数据库设计资料，先生成持久化对象,PO和Mapper(当前项目采用mybatis管理)
- 生成建表语句，生成到 `docs/sql`
- 生成领域对象，实现单个insert(新建)、insertBatch(批量新建,sql批量插入，禁止for循环)、removeById(删除(id条件逻辑删除))、removeBatchByIds(批量删除(id条件逻辑删除))、update(单个修改)、findById(按id查询)、findByCondition(条件查询，条件用map)，count(计数，条件用map)在mapper.xml 条件查询和计数共用一个筛选条件 。以上有corpid的字段的表都需要传corpid
- findByCondition 需要对接分页参 `offset` 起始行 `pageSize` 是一页几条，如果`offset`不存在，仅有`pageSize` 那就只取`pageSize`条数据
- findByCondition 需要对接 groupByStr参数 对应group by功能 orderByStr参数 对应order by 功能 
- 创建完新module后，回写到 `docs/base/项目业务module导航.md`
## 边界规则

[//]: # (修改的接口查询接口文档时，根据接口所在module的名称后缀，例如xbb-erp-module-sales，当前接口领域就是sales)