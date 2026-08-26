package xbb.ai.erp.module.sales.application.approval;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.approval.contract.ApprovalPlatformApi;
import xbb.ai.erp.module.approval.contract.ApprovalStatus;
import xbb.ai.erp.module.approval.contract.ApprovalSubmission;
import xbb.ai.erp.module.sales.admin.dto.SalesOrderItemDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesOrderMainDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesOrderSubmitSaveDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SalesOrderApprovalSubmitServiceTest {

    private final ApprovalPlatformApi approvalPlatformApi = mock(ApprovalPlatformApi.class);
    private final SalesOrderApprovalSubmitService service =
        new SalesOrderApprovalSubmitService(approvalPlatformApi, new ObjectMapper());

    @Test
    void 提交销售订单审批冻结快照并绑定订单标识() {
        when(approvalPlatformApi.submit(argThat(command -> command.businessCode().equals("SALES_ORDER"))))
            .thenReturn(new ApprovalSubmission("instance-001", ApprovalStatus.IN_APPROVAL, 1));

        SalesOrderSubmitSaveDTO dto = request();
        service.submitCreate(100L, dto);

        verify(approvalPlatformApi).submit(argThat(command -> command.businessCode().equals("SALES_ORDER")
            && command.scene().name().equals("CREATE")
            && command.subjectId().equals("100")
            && command.requestId().equals("sales-order-create:SO-001")
            && command.subjectSnapshotJson().contains("\"totalAmount\":120.00")
            && command.subjectSnapshotJson().contains("\"orderDate\":\"2026-08-25\"")));
    }

    private SalesOrderSubmitSaveDTO request() {
        SalesOrderSubmitSaveDTO dto = new SalesOrderSubmitSaveDTO();
        dto.setCorpid("demo-corp");
        dto.setUserId("user-001");
        SalesOrderMainDTO main = new SalesOrderMainDTO();
        main.setOrderNo("SO-001");
        main.setCustomerId(10L);
        main.setOrderDate(date("2026-08-25"));
        main.setTotalAmount(new BigDecimal("120.00"));
        dto.setMain(main);
        SalesOrderItemDTO item = new SalesOrderItemDTO();
        item.setSkuId(20L);
        item.setQty(new BigDecimal("2"));
        item.setUnitPrice(new BigDecimal("60.00"));
        dto.setItems(List.of(item));
        return dto;
    }

    private long date(String date) {
        return LocalDate.parse(date).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
