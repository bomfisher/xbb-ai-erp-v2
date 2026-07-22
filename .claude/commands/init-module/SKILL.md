---
description: 用户提供了完整的module初始化资料时执行
---


## 适用场景
当用户提供了完整的module初始化资料，并且要求就行module初始化时执行


## 执行流程
1、module项目包的前缀是xbb-erp-module，和用户确认当前要创建的module包的业务名，例如销售管理，用户提供了sales，那项目包名是xbb-erp-module-sales
2、包名查重，重复返回第一步
3、完成项目初始化创建，仅生成package结构，不创建任何文件，详见`
3、根据提供的数据库设计资料，先生成持久化对象,PO和Mapper(当前项目采用mybatis管理)
4、生成领域对象，实现新建、删除(逻辑删除)、修改、按id查询和按条件查询(有corpid的字段的表都需要传corpid)
5、
## 边界规则
修改的接口查询接口文档时，根据接口所在module的名称后缀，例如xbb-erp-module-sales，当前接口领域就是sales