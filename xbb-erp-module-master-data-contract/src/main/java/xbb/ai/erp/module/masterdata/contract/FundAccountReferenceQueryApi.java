package xbb.ai.erp.module.masterdata.contract;

public interface FundAccountReferenceQueryApi {

    FundAccountReference findDefaultEnabledAccount(String corpid);

    record FundAccountReference(Long id, String accountCode, String accountName, String accountType) {
    }
}
