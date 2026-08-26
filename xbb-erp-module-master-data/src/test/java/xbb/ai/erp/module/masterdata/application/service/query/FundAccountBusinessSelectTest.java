package xbb.ai.erp.module.masterdata.application.service.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import xbb.ai.erp.module.masterdata.admin.dto.FundAccountBusinessSelectQueryDTO;
import xbb.ai.erp.module.masterdata.domain.model.FundAccount;
import xbb.ai.erp.module.masterdata.domain.repository.FundAccountRepository;

class FundAccountBusinessSelectTest {

    @Test
    void shouldQuickSearchEnabledFundAccountsByKeyword() {
        FundAccountRepository repository = mock(FundAccountRepository.class);
        when(repository.findByCondition(any())).thenReturn(List.of(fundAccount(10L, 1)));
        FundAccountQueryAppServiceImpl service = new FundAccountQueryAppServiceImpl(repository, null, null, null);

        List<?> options = service.businessSelectQuickSearch(queryDTO());

        ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
        verify(repository).findByCondition(captor.capture());
        assertEquals("corp-001", captor.getValue().get("corpid"));
        assertEquals(1, captor.getValue().get("enabled"));
        assertEquals(1, options.size());
    }

    @Test
    void shouldNotResolveDisabledFundAccountById() {
        FundAccountRepository repository = mock(FundAccountRepository.class);
        when(repository.findById("corp-001", 10L)).thenReturn(fundAccount(10L, 0));
        FundAccountQueryAppServiceImpl service = new FundAccountQueryAppServiceImpl(repository, null, null, null);
        FundAccountBusinessSelectQueryDTO dto = queryDTO();
        dto.setId(10L);

        assertNull(service.businessSelectGetById(dto));
    }

    private FundAccountBusinessSelectQueryDTO queryDTO() {
        FundAccountBusinessSelectQueryDTO dto = new FundAccountBusinessSelectQueryDTO();
        dto.setCorpid("corp-001");
        dto.setKeyword("现金");
        return dto;
    }

    private FundAccount fundAccount(Long id, Integer enabled) {
        FundAccount fundAccount = new FundAccount();
        fundAccount.setId(id);
        fundAccount.setAccountCode("FA-001");
        fundAccount.setAccountName("现金账户");
        fundAccount.setEnabled(enabled);
        return fundAccount;
    }
}
