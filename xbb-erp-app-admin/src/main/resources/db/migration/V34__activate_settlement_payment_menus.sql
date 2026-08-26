INSERT INTO sys_menu (
    id, corpid, menu_code, menu_name, surface, parent_menu_id, menu_type, route_path, component_path,
    icon, sort_no, pinned_home, enable_status, del, add_time, update_time, creator_id, modify_id
)
SELECT 910000001, 'demo-corp', 'PURCHASE_CENTER', '采购业务', 'PC_ADMIN', NULL, 'GROUP', NULL, NULL,
    NULL, 20, 0, 1, 0, UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000, 'system', 'system'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE corpid = 'demo-corp' AND menu_code = 'PURCHASE_CENTER' AND del = 0
);

INSERT INTO sys_menu (
    id, corpid, menu_code, menu_name, surface, parent_menu_id, menu_type, route_path, component_path,
    icon, sort_no, pinned_home, enable_status, del, add_time, update_time, creator_id, modify_id
)
SELECT 910000002, 'demo-corp', 'SETTLEMENT_CENTER', '资金往来', 'PC_ADMIN', NULL, 'GROUP', NULL, NULL,
    NULL, 50, 0, 1, 0, UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000, 'system', 'system'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE corpid = 'demo-corp' AND menu_code = 'SETTLEMENT_CENTER' AND del = 0
);

INSERT INTO sys_menu (
    id, corpid, menu_code, menu_name, surface, parent_menu_id, menu_type, route_path, component_path,
    icon, sort_no, pinned_home, enable_status, del, add_time, update_time, creator_id, modify_id
)
SELECT 910000010, 'demo-corp', 'PURCHASE_INVOICE', '采购发票', 'PC_ADMIN', parent.id, 'PAGE',
    '/purchase/purchaseInvoice', '/purchase/purchaseInvoice', NULL, 10, 0, 1, 0,
    UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000, 'system', 'system'
FROM (SELECT id FROM sys_menu WHERE corpid = 'demo-corp' AND menu_code = 'PURCHASE_CENTER' AND del = 0) parent
WHERE 1 = 1
  AND NOT EXISTS (
      SELECT 1 FROM sys_menu existing
      WHERE existing.corpid = 'demo-corp' AND existing.menu_code = 'PURCHASE_INVOICE' AND existing.del = 0
  );

INSERT INTO sys_menu (
    id, corpid, menu_code, menu_name, surface, parent_menu_id, menu_type, route_path, component_path,
    icon, sort_no, pinned_home, enable_status, del, add_time, update_time, creator_id, modify_id
)
SELECT 910000020, 'demo-corp', 'PAYMENT', '付款单', 'PC_ADMIN', parent.id, 'PAGE',
    '/settlement/payment', 'settlement/payment', NULL, 10, 0, 1, 0,
    UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000, 'system', 'system'
FROM (SELECT id FROM sys_menu WHERE corpid = 'demo-corp' AND menu_code = 'SETTLEMENT_CENTER' AND del = 0) parent
WHERE 1 = 1
  AND NOT EXISTS (
      SELECT 1 FROM sys_menu existing
      WHERE existing.corpid = 'demo-corp' AND existing.menu_code = 'PAYMENT' AND existing.del = 0
  );

INSERT INTO sys_menu (
    id, corpid, menu_code, menu_name, surface, parent_menu_id, menu_type, route_path, component_path,
    icon, sort_no, pinned_home, enable_status, del, add_time, update_time, creator_id, modify_id
)
SELECT 910000021, 'demo-corp', 'ADVANCE_PAYMENT', '预付款', 'PC_ADMIN', parent.id, 'PAGE',
    '/settlement/advancePayment', 'settlement/advancePayment', NULL, 20, 0, 1, 0,
    UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000, 'system', 'system'
FROM (SELECT id FROM sys_menu WHERE corpid = 'demo-corp' AND menu_code = 'SETTLEMENT_CENTER' AND del = 0) parent
WHERE 1 = 1
  AND NOT EXISTS (
      SELECT 1 FROM sys_menu existing
      WHERE existing.corpid = 'demo-corp' AND existing.menu_code = 'ADVANCE_PAYMENT' AND existing.del = 0
  );

INSERT INTO sys_menu (
    id, corpid, menu_code, menu_name, surface, parent_menu_id, menu_type, route_path, component_path,
    icon, sort_no, pinned_home, enable_status, del, add_time, update_time, creator_id, modify_id
)
SELECT 910000022, 'demo-corp', 'PAYABLE', '应付款', 'PC_ADMIN', parent.id, 'PAGE',
    '/settlement/payable', 'settlement/payable', NULL, 30, 0, 1, 0,
    UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000, 'system', 'system'
FROM (SELECT id FROM sys_menu WHERE corpid = 'demo-corp' AND menu_code = 'SETTLEMENT_CENTER' AND del = 0) parent
WHERE 1 = 1
  AND NOT EXISTS (
      SELECT 1 FROM sys_menu existing
      WHERE existing.corpid = 'demo-corp' AND existing.menu_code = 'PAYABLE' AND existing.del = 0
  );

INSERT INTO sys_menu (
    id, corpid, menu_code, menu_name, surface, parent_menu_id, menu_type, route_path, component_path,
    icon, sort_no, pinned_home, enable_status, del, add_time, update_time, creator_id, modify_id
)
SELECT 910000023, 'demo-corp', 'PAYMENT_WRITEOFF', '付款核销', 'PC_ADMIN', parent.id, 'PAGE',
    '/settlement/paymentWriteoff', 'settlement/paymentWriteoff', NULL, 40, 0, 1, 0,
    UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000, 'system', 'system'
FROM (SELECT id FROM sys_menu WHERE corpid = 'demo-corp' AND menu_code = 'SETTLEMENT_CENTER' AND del = 0) parent
WHERE 1 = 1
  AND NOT EXISTS (
      SELECT 1 FROM sys_menu existing
      WHERE existing.corpid = 'demo-corp' AND existing.menu_code = 'PAYMENT_WRITEOFF' AND existing.del = 0
  );
