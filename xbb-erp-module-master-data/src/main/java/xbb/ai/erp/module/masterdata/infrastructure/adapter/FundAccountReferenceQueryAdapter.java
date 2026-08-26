package xbb.ai.erp.module.masterdata.infrastructure.adapter;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xbb.ai.erp.module.masterdata.contract.FundAccountReferenceQueryApi;
import xbb.ai.erp.module.masterdata.domain.model.FundAccount;
import xbb.ai.erp.module.masterdata.domain.repository.FundAccountRepository;

@Service
@RequiredArgsConstructor
public class FundAccountReferenceQueryAdapter implements FundAccountReferenceQueryApi {

    private static final int ENABLED = 1;
    private static final int DEFAULT_ACCOUNT = 1;

    private final FundAccountRepository fundAccountRepository;

    @Override
    public FundAccountReference findDefaultEnabledAccount(String corpid) {
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("corpid", corpid);
        conditionMap.put("enabled", ENABLED);
        conditionMap.put("defaultFlag", DEFAULT_ACCOUNT);
        return fundAccountRepository.findByCondition(conditionMap).stream()
            .findFirst()
            .map(this::toReference)
            .orElse(null);
    }

    private FundAccountReference toReference(FundAccount fundAccount) {
        return new FundAccountReference(
            fundAccount.getId(),
            fundAccount.getAccountCode(),
            fundAccount.getAccountName(),
            fundAccount.getAccountType()
        );
    }
}
