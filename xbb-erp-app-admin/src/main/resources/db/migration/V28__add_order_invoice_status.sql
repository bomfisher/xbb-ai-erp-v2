ALTER TABLE sales_order
    ADD COLUMN invoice_status tinyint NOT NULL DEFAULT 0 COMMENT '开票状态：0未开票，1部分开票，2全部开票' AFTER receipt_status;

ALTER TABLE purchase_order
    ADD COLUMN invoice_status tinyint NOT NULL DEFAULT 0 COMMENT '开票状态：0未开票，1部分开票，2全部开票' AFTER payment_status;
