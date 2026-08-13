CREATE TABLE `stock_balance` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键', `corpid` varchar(64) NOT NULL COMMENT '租户ID', `warehouse_id` bigint(20) NOT NULL, `sku_id` bigint(20) NOT NULL,
  `qty` decimal(18,6) NOT NULL DEFAULT '0.000000', `locked_qty` decimal(18,6) NOT NULL DEFAULT '0.000000', `available_qty` decimal(18,6) NOT NULL DEFAULT '0.000000',
  `total_cost` decimal(18,2) NOT NULL DEFAULT '0.00', `unit_cost` decimal(18,6) NOT NULL DEFAULT '0.000000', `version` int(11) NOT NULL DEFAULT '0', `del` tinyint(2) NOT NULL DEFAULT '0',
  `add_time` bigint(20) NOT NULL, `update_time` bigint(20) NOT NULL, `creator_id` varchar(64) NOT NULL, `modify_id` varchar(64) NOT NULL,
  PRIMARY KEY (`id`), UNIQUE KEY `uk_stock_balance` (`corpid`,`warehouse_id`,`sku_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='仓库SKU库存及成本台账';

CREATE TABLE `stock_cost_transaction` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键', `corpid` varchar(64) NOT NULL COMMENT '租户ID', `warehouse_id` bigint(20) NOT NULL, `sku_id` bigint(20) NOT NULL,
  `action_type` varchar(20) NOT NULL, `business_code` varchar(32) NOT NULL, `source_id` bigint(20) NOT NULL,
  `qty_before` decimal(18,6) NOT NULL, `qty_change` decimal(18,6) NOT NULL, `qty_after` decimal(18,6) NOT NULL,
  `total_cost_before` decimal(18,2) NOT NULL, `total_cost_change` decimal(18,2) NOT NULL, `total_cost_after` decimal(18,2) NOT NULL,
  `unit_cost_before` decimal(18,6) NOT NULL, `unit_cost` decimal(18,6) NOT NULL, `unit_cost_after` decimal(18,6) NOT NULL, `tail_difference` decimal(18,2) NOT NULL DEFAULT '0.00',
  `reason` varchar(255) DEFAULT NULL, `idempotency_key` varchar(64) NOT NULL, `operator_id` varchar(64) DEFAULT NULL, `occurred_at` datetime NOT NULL,
  `del` tinyint(2) NOT NULL DEFAULT '0', `add_time` bigint(20) NOT NULL, `update_time` bigint(20) NOT NULL, `creator_id` varchar(64) NOT NULL, `modify_id` varchar(64) NOT NULL,
  PRIMARY KEY (`id`), UNIQUE KEY `uk_stock_cost_idempotency` (`corpid`,`idempotency_key`), KEY `idx_stock_cost_transaction_query` (`corpid`,`warehouse_id`,`sku_id`,`occurred_at`), KEY `idx_stock_cost_transaction_source` (`corpid`,`business_code`,`source_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存成本流水';

CREATE TABLE `stock_reservation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键', `corpid` varchar(64) NOT NULL COMMENT '租户ID', `warehouse_id` bigint(20) NOT NULL, `sku_id` bigint(20) NOT NULL,
  `source_type` varchar(32) NOT NULL, `source_id` bigint(20) NOT NULL, `source_line_id` bigint(20) NOT NULL,
  `reserved_qty` decimal(18,6) NOT NULL, `outbound_qty` decimal(18,6) NOT NULL DEFAULT '0.000000', `released_qty` decimal(18,6) NOT NULL DEFAULT '0.000000', `remaining_qty` decimal(18,6) NOT NULL,
  `status` varchar(20) NOT NULL DEFAULT 'RESERVED', `reserved_at` datetime NOT NULL, `released_at` datetime DEFAULT NULL, `idempotency_key` varchar(64) NOT NULL, `version` int(11) NOT NULL DEFAULT '0',
  `del` tinyint(2) NOT NULL DEFAULT '0', `add_time` bigint(20) NOT NULL, `update_time` bigint(20) NOT NULL, `creator_id` varchar(64) NOT NULL, `modify_id` varchar(64) NOT NULL,
  PRIMARY KEY (`id`), UNIQUE KEY `uk_reservation_source` (`corpid`,`source_type`,`source_id`,`source_line_id`), UNIQUE KEY `uk_reservation_idempotency` (`corpid`,`idempotency_key`), KEY `idx_reservation_stock` (`corpid`,`warehouse_id`,`sku_id`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='销售订单锁库记录';

CREATE TABLE `stock_transaction` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键', `corpid` varchar(64) NOT NULL COMMENT '租户ID', `warehouse_id` bigint(20) NOT NULL, `sku_id` bigint(20) NOT NULL,
  `action_type` varchar(20) NOT NULL, `qty_before` decimal(18,6) NOT NULL, `qty_change` decimal(18,6) NOT NULL, `qty_after` decimal(18,6) NOT NULL,
  `source_type` varchar(32) NOT NULL, `source_id` bigint(20) NOT NULL, `idempotency_key` varchar(64) NOT NULL, `operator_id` varchar(64) DEFAULT NULL, `occurred_at` datetime NOT NULL,
  `del` tinyint(2) NOT NULL DEFAULT '0', `add_time` bigint(20) NOT NULL, `update_time` bigint(20) NOT NULL, `creator_id` varchar(64) NOT NULL, `modify_id` varchar(64) NOT NULL,
  PRIMARY KEY (`id`), UNIQUE KEY `uk_stock_transaction_idempotency` (`corpid`,`idempotency_key`), KEY `idx_stock_transaction_query` (`corpid`,`warehouse_id`,`sku_id`,`occurred_at`), KEY `idx_stock_transaction_source` (`corpid`,`source_type`,`source_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存数量流水';
