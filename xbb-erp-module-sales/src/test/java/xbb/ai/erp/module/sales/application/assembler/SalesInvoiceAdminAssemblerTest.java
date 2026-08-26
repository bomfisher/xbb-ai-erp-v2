package xbb.ai.erp.module.sales.application.assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.sales.admin.vo.SalesInvoiceSaveItemVO;
import xbb.ai.erp.module.sales.domain.model.SalesInvoice;
import xbb.ai.erp.module.sales.domain.model.SalesInvoiceLine;
import xbb.ai.erp.module.sales.domain.model.SalesInvoiceLineSource;

class SalesInvoiceAdminAssemblerTest {

    @Test
    void shouldRestoreSourceReferenceToInvoiceLine() {
        SalesInvoice invoice = new SalesInvoice();
        invoice.setId(10L);

        SalesInvoiceLine line = new SalesInvoiceLine();
        line.setId(20L);
        line.setProductId(30L);
        line.setProductName("源单产品");

        SalesInvoiceLineSource source = new SalesInvoiceLineSource();
        source.setSalesInvoiceLineId(20L);
        source.setSourceType("SALES_ORDER");
        source.setSourceId(40L);
        source.setSourceLineId(50L);

        SalesInvoiceSaveItemVO result = SalesInvoiceAdminAssembler.toSaveItemVO(
            invoice, List.of(line), List.of(source));

        assertEquals("SALES_ORDER", result.getLines().getFirst().getSourceType());
        assertEquals(40L, result.getLines().getFirst().getSourceId());
        assertEquals(50L, result.getLines().getFirst().getSourceLineId());
        assertEquals(30L, result.getLines().getFirst().getProductId());
        assertEquals("源单产品", result.getLines().getFirst().getProductName());
    }
}
