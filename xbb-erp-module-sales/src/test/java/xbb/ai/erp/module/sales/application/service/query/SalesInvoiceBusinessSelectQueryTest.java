package xbb.ai.erp.module.sales.application.service.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.enums.AuditStatusEnum;
import xbb.ai.erp.module.sales.admin.dto.SalesInvoiceBusinessSelectQueryDTO;
import xbb.ai.erp.module.sales.admin.vo.SalesInvoiceBusinessSelectOptionVO;
import xbb.ai.erp.module.sales.domain.model.SalesInvoice;
import xbb.ai.erp.module.sales.domain.model.SalesInvoiceLine;
import xbb.ai.erp.module.sales.domain.repository.SalesInvoiceLineRepository;
import xbb.ai.erp.module.sales.domain.repository.SalesInvoiceRepository;

class SalesInvoiceBusinessSelectQueryTest {
    private final SalesInvoiceQueryAppServiceImpl queryService = new SalesInvoiceQueryAppServiceImpl(
        new StubSalesInvoiceRepository(), new StubSalesInvoiceLineRepository(), null, null, null, null);

    @Test
    void shouldLimitBusinessSelectResultsToCurrentTenantAndKeyword() {
        SalesInvoiceBusinessSelectQueryDTO dto = query("corp-a");
        dto.setKeyword("SI-002");

        List<SalesInvoiceBusinessSelectOptionVO> options = queryService.businessSelectQuickSearch(dto);

        assertEquals(1, options.size());
        assertEquals(2L, options.getFirst().getId());
        assertEquals("SI-002", options.getFirst().getLabel());
    }

    @Test
    void shouldPageBusinessSelectResultsAndHideOtherTenantsById() {
        SalesInvoiceBusinessSelectQueryDTO dialogDto = query("corp-a");
        dialogDto.setPageNum(2);
        dialogDto.setPageSize(1);

        ListBaseVO<SalesInvoiceBusinessSelectOptionVO> page = queryService.businessSelectDialogSearch(dialogDto);
        assertEquals(1, page.getList().size());
        assertEquals(2L, page.getList().getFirst().getId());

        SalesInvoiceBusinessSelectQueryDTO idDto = query("corp-a");
        idDto.setId(3L);
        assertNull(queryService.businessSelectGetById(idDto));
    }

    private SalesInvoiceBusinessSelectQueryDTO query(String corpid) {
        SalesInvoiceBusinessSelectQueryDTO dto = new SalesInvoiceBusinessSelectQueryDTO();
        dto.setCorpid(corpid);
        return dto;
    }

    private static class StubSalesInvoiceRepository implements SalesInvoiceRepository {
        private final List<SalesInvoice> invoices = List.of(
            invoice(1L, "corp-a", "SI-001", AuditStatusEnum.APPROVED.getCode()),
            invoice(2L, "corp-a", "SI-002", AuditStatusEnum.NO_NEED_APPROVED.getCode()),
            invoice(3L, "corp-b", "SI-003", AuditStatusEnum.APPROVED.getCode()));

        @Override
        public SalesInvoice findById(String corpid, Long id) {
            return invoices.stream()
                .filter(invoice -> invoice.getCorpid().equals(corpid) && invoice.getId().equals(id))
                .findFirst()
                .orElse(null);
        }

        @Override
        public List<SalesInvoice> findByIds(String corpid, java.util.Collection<Long> ids) {
            return invoices.stream()
                .filter(invoice -> invoice.getCorpid().equals(corpid) && ids.contains(invoice.getId()))
                .toList();
        }

        @Override
        public List<SalesInvoice> findByCondition(Map<String, Object> conditionMap) {
            return invoices.stream()
                .filter(invoice -> invoice.getCorpid().equals(conditionMap.get("corpid")))
                .toList();
        }

        @Override
        public Long insert(SalesInvoice salesInvoice) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void insertBatch(List<SalesInvoice> salesInvoiceList) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void removeById(String corpid, Long id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void removeBatchByIds(String corpid, List<Long> ids) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void update(SalesInvoice salesInvoice) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Long count(Map<String, Object> conditionMap) {
            return 0L;
        }

        private static SalesInvoice invoice(Long id, String corpid, String invoiceNo, Integer auditStatus) {
            SalesInvoice invoice = new SalesInvoice();
            invoice.setId(id);
            invoice.setCorpid(corpid);
            invoice.setInvoiceNo(invoiceNo);
            invoice.setAuditStatus(auditStatus);
            return invoice;
        }
    }

    private static class StubSalesInvoiceLineRepository implements SalesInvoiceLineRepository {
        @Override
        public Long insert(SalesInvoiceLine salesInvoiceLine) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void insertBatch(List<SalesInvoiceLine> salesInvoiceLineList) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void removeById(String corpid, Long id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void removeBatchByIds(String corpid, List<Long> ids) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void update(SalesInvoiceLine salesInvoiceLine) {
            throw new UnsupportedOperationException();
        }

        @Override
        public SalesInvoiceLine findById(String corpid, Long id) {
            return null;
        }

        @Override
        public List<SalesInvoiceLine> findByCondition(Map<String, Object> conditionMap) {
            return List.of();
        }

        @Override
        public Long count(Map<String, Object> conditionMap) {
            return 0L;
        }
    }
}
