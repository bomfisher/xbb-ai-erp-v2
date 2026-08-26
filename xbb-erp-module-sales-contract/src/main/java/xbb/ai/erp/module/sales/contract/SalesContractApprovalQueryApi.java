package xbb.ai.erp.module.sales.contract;

/**
 * 向审批接入方提供销售合同的审批查询快照。
 */
public interface SalesContractApprovalQueryApi {

    SalesContractApprovalSnapshot requireApprovalSnapshot(String corpid, Long contractId);
}
