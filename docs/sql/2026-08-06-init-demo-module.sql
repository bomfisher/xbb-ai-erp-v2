CREATE TABLE `demo` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `corpid` varchar(50) NOT NULL COMMENT '公司',
  `name` varchar(50) DEFAULT NULL COMMENT '名称',
  `user_id` varchar(50) DEFAULT NULL COMMENT '员工',
  `dep_id` varchar(50) DEFAULT NULL COMMENT '部门',
  `comb` varchar(50) DEFAULT NULL COMMENT '下拉',
  `comb_multi` json DEFAULT NULL COMMENT '下拉多选',
  `num_int` int DEFAULT NULL COMMENT '整数',
  `num_double` decimal(10,2) DEFAULT NULL COMMENT '浮点',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '金额',
  `date` bigint DEFAULT NULL COMMENT '日期',
  `time` bigint DEFAULT NULL COMMENT '时间',
  `file` varchar(255) DEFAULT NULL COMMENT '附件',
  `image` varchar(255) DEFAULT NULL COMMENT '图片',
  `address` json DEFAULT NULL COMMENT '地址',
  `del` tinyint NOT NULL DEFAULT '0' COMMENT '删除',
  `add_time` bigint NOT NULL DEFAULT '0' COMMENT '添加时间',
  `update_time` bigint NOT NULL DEFAULT '0' COMMENT '修改时间',
  `creator_id` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  `modify_id` varchar(50) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`),
  KEY `idx_demo_corpid_name` (`corpid`,`name`,`del`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='DEMO主表';

CREATE TABLE `demo_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `corpid` varchar(50) NOT NULL COMMENT '公司',
  `data_id` bigint NOT NULL COMMENT 'DEMO主表ID',
  `name` varchar(50) DEFAULT NULL COMMENT '名称',
  `del` tinyint NOT NULL DEFAULT '0' COMMENT '删除',
  `add_time` bigint NOT NULL DEFAULT '0' COMMENT '添加时间',
  `update_time` bigint NOT NULL DEFAULT '0' COMMENT '修改时间',
  `creator_id` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  `modify_id` varchar(50) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`),
  KEY `idx_demo_item_data` (`corpid`,`data_id`,`del`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='DEMO明细表';

CREATE TABLE `demo_sub` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `corpid` varchar(50) NOT NULL COMMENT '公司',
  `data_id` bigint NOT NULL COMMENT 'DEMO主表ID',
  `name` varchar(50) DEFAULT NULL COMMENT '名称',
  `del` tinyint NOT NULL DEFAULT '0' COMMENT '删除',
  `add_time` bigint NOT NULL DEFAULT '0' COMMENT '添加时间',
  `update_time` bigint NOT NULL DEFAULT '0' COMMENT '修改时间',
  `creator_id` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  `modify_id` varchar(50) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`),
  KEY `idx_demo_sub_data` (`corpid`,`data_id`,`del`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='DEMO下游表';
