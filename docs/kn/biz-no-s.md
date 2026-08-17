# 业务编号生成

业务模块使用 `BizNoGenerator#next(corpid, businessCode)` 获取并持久化编号。编号规则维护与 HTTP 发号入口见 `docs/kn/system-biz-no-m.md`；稳定实现约束见 `docs/guide/common/biz-no/README.md`。
