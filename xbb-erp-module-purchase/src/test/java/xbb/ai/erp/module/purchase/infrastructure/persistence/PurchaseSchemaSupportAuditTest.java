package xbb.ai.erp.module.purchase.infrastructure.persistence;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PurchaseSchemaSupportAuditTest {

    private static final Path SQL_PATH = Path.of("/Users/bomfish/xbb-ai-erp-v2/docs/sql/2026-07-23-init-purchase-module.sql");
    private static final Path REQUEST_MAPPER_PATH = Path.of("/Users/bomfish/xbb-ai-erp-v2/xbb-erp-module-purchase/src/main/resources/mapper/purchase/PurchaseRequestMapper.xml");
    private static final Path REQUEST_ITEM_MAPPER_PATH = Path.of("/Users/bomfish/xbb-ai-erp-v2/xbb-erp-module-purchase/src/main/resources/mapper/purchase/PurchaseRequestItemMapper.xml");
    private static final Path ORDER_MAPPER_PATH = Path.of("/Users/bomfish/xbb-ai-erp-v2/xbb-erp-module-purchase/src/main/resources/mapper/purchase/PurchaseOrderMapper.xml");
    private static final Path ORDER_ITEM_MAPPER_PATH = Path.of("/Users/bomfish/xbb-ai-erp-v2/xbb-erp-module-purchase/src/main/resources/mapper/purchase/PurchaseOrderItemMapper.xml");

    @Test
    void should_keep_current_purchase_sql_when_request_and_order_schema_already_cover_current_fields() throws Exception {
        String sql = Files.readString(SQL_PATH);
        String requestMapper = Files.readString(REQUEST_MAPPER_PATH);
        String requestItemMapper = Files.readString(REQUEST_ITEM_MAPPER_PATH);
        String orderMapper = Files.readString(ORDER_MAPPER_PATH);
        String orderItemMapper = Files.readString(ORDER_ITEM_MAPPER_PATH);

        assertContains(sql, "CREATE TABLE IF NOT EXISTS `purchase_request`");
        assertContains(sql, "`request_no` varchar(64) NOT NULL");
        assertContains(sql, "`biz_status` varchar(32) NOT NULL");
        assertContains(sql, "`approval_status` varchar(32) NOT NULL");
        assertContains(sql, "CREATE TABLE IF NOT EXISTS `purchase_request_item`");
        assertContains(sql, "`request_qty` decimal(18,6) NOT NULL DEFAULT '0.000000'");
        assertContains(sql, "`reserved_qty` decimal(18,6) NOT NULL DEFAULT '0.000000'");
        assertContains(sql, "`executed_qty` decimal(18,6) NOT NULL DEFAULT '0.000000'");
        assertContains(sql, "`closed_qty` decimal(18,6) NOT NULL DEFAULT '0.000000'");

        assertContains(sql, "CREATE TABLE IF NOT EXISTS `purchase_order`");
        assertContains(sql, "`order_no` varchar(64) NOT NULL");
        assertContains(sql, "`sales_linked_flag` tinyint(4) NOT NULL DEFAULT '0'");
        assertContains(sql, "`biz_status` varchar(32) NOT NULL");
        assertContains(sql, "`approval_status` varchar(32) NOT NULL");
        assertContains(sql, "`execution_status` varchar(32) NOT NULL");
        assertContains(sql, "`receipt_status` varchar(32) NOT NULL");
        assertContains(sql, "`inbound_status` varchar(32) NOT NULL");
        assertContains(sql, "`payable_status` varchar(32) NOT NULL");
        assertContains(sql, "`invoice_status` varchar(32) NOT NULL");
        assertContains(sql, "`payment_status` varchar(32) NOT NULL");
        assertContains(sql, "CREATE TABLE IF NOT EXISTS `purchase_order_item`");
        assertContains(sql, "`order_qty` decimal(18,6) NOT NULL DEFAULT '0.000000'");
        assertContains(sql, "`received_qty` decimal(18,6) NOT NULL DEFAULT '0.000000'");
        assertContains(sql, "`inbounded_qty` decimal(18,6) NOT NULL DEFAULT '0.000000'");
        assertContains(sql, "`closed_qty` decimal(18,6) NOT NULL DEFAULT '0.000000'");
        assertContains(sql, "`is_gift` tinyint(4) NOT NULL DEFAULT '0'");

        assertContains(requestMapper, "request_no");
        assertContains(requestMapper, "biz_status");
        assertContains(requestMapper, "approval_status");
        assertContains(requestItemMapper, "request_qty");
        assertContains(requestItemMapper, "reserved_qty");
        assertContains(requestItemMapper, "executed_qty");
        assertContains(requestItemMapper, "closed_qty");

        assertContains(orderMapper, "order_no");
        assertContains(orderMapper, "sales_linked_flag");
        assertContains(orderMapper, "biz_status");
        assertContains(orderMapper, "approval_status");
        assertContains(orderMapper, "execution_status");
        assertContains(orderMapper, "receipt_status");
        assertContains(orderMapper, "inbound_status");
        assertContains(orderMapper, "payable_status");
        assertContains(orderMapper, "invoice_status");
        assertContains(orderMapper, "payment_status");
        assertContains(orderItemMapper, "order_qty");
        assertContains(orderItemMapper, "received_qty");
        assertContains(orderItemMapper, "inbounded_qty");
        assertContains(orderItemMapper, "closed_qty");
        assertContains(orderItemMapper, "is_gift");
    }

    private void assertContains(String source, String expected) {
        assertTrue(source.contains(expected), () -> "缺少关键字段或映射: " + expected);
    }
}
