ALTER TABLE receivable
    DROP INDEX uk_receivable_invoice,
    ADD INDEX idx_receivable_source_invoice (corpid, source_invoice_id);

ALTER TABLE payable
    DROP INDEX uk_payable_invoice,
    ADD INDEX idx_payable_source_invoice (corpid, source_invoice_id);

ALTER TABLE sales_invoice
    ADD COLUMN receivable_opened_amount DECIMAL(18, 2) NOT NULL DEFAULT 0.00 COMMENT '已开应收金额' AFTER amount,
    ADD COLUMN receivable_available_amount DECIMAL(18, 2) NOT NULL DEFAULT 0.00 COMMENT '可开应收金额' AFTER receivable_opened_amount;

ALTER TABLE purchase_invoice
    ADD COLUMN payable_opened_amount DECIMAL(18, 2) NOT NULL DEFAULT 0.00 COMMENT '已开应付金额' AFTER amount,
    ADD COLUMN payable_available_amount DECIMAL(18, 2) NOT NULL DEFAULT 0.00 COMMENT '可开应付金额' AFTER payable_opened_amount;

UPDATE sales_invoice invoice
LEFT JOIN (
    SELECT corpid, source_invoice_id, SUM(amount) AS opened_amount
    FROM receivable
    WHERE del = 0 AND source_type = 'SALES_INVOICE' AND source_invoice_id IS NOT NULL
    GROUP BY corpid, source_invoice_id
) opened ON opened.corpid = invoice.corpid AND opened.source_invoice_id = invoice.id
SET invoice.receivable_opened_amount = COALESCE(opened.opened_amount, 0.00),
    invoice.receivable_available_amount = invoice.amount - COALESCE(opened.opened_amount, 0.00);

UPDATE purchase_invoice invoice
LEFT JOIN (
    SELECT corpid, source_invoice_id, SUM(amount) AS opened_amount
    FROM payable
    WHERE del = 0 AND source_type = 'PURCHASE_INVOICE' AND source_invoice_id IS NOT NULL
    GROUP BY corpid, source_invoice_id
) opened ON opened.corpid = invoice.corpid AND opened.source_invoice_id = invoice.id
SET invoice.payable_opened_amount = COALESCE(opened.opened_amount, 0.00),
    invoice.payable_available_amount = invoice.amount - COALESCE(opened.opened_amount, 0.00);
