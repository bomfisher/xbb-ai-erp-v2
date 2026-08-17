DROP TABLE IF EXISTS purchase_inbound_item;
DROP TABLE IF EXISTS purchase_inbound;
DROP TABLE IF EXISTS purchase_order_item;
DROP TABLE IF EXISTS purchase_order;

CREATE TABLE purchase_order (
    id BIGINT NOT NULL AUTO_INCREMENT,
    corpid VARCHAR(64) NOT NULL,
    order_no VARCHAR(64) NOT NULL,
    supplier_id BIGINT NOT NULL,
    supplier_name VARCHAR(128) NOT NULL,
    order_date BIGINT,
    expected_date BIGINT,
    total_amount DECIMAL(18, 2) NOT NULL,
    status VARCHAR(32),
    audit_status INT,
    inbound_status INT,
    payment_status INT,
    remark VARCHAR(255),
    del TINYINT NOT NULL DEFAULT 0,
    add_time BIGINT NOT NULL,
    update_time BIGINT NOT NULL,
    creator_id VARCHAR(64) NOT NULL,
    modify_id VARCHAR(64) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE purchase_order_item (
    id BIGINT NOT NULL AUTO_INCREMENT,
    corpid VARCHAR(64) NOT NULL,
    purchase_order_id BIGINT NOT NULL,
    line_no INT NOT NULL,
    sku_id BIGINT NOT NULL,
    sku_code VARCHAR(64) NOT NULL,
    sku_name VARCHAR(128) NOT NULL,
    specification VARCHAR(128),
    unit_name VARCHAR(32) NOT NULL,
    qty DECIMAL(18, 6) NOT NULL,
    inbound_qty DECIMAL(18, 6) NOT NULL DEFAULT 0,
    unit_price DECIMAL(18, 6) NOT NULL,
    tax_rate DECIMAL(8, 4) NOT NULL DEFAULT 0,
    amount DECIMAL(18, 2) NOT NULL,
    inbound_status INT,
    del TINYINT NOT NULL DEFAULT 0,
    add_time BIGINT NOT NULL,
    update_time BIGINT NOT NULL,
    creator_id VARCHAR(64) NOT NULL,
    modify_id VARCHAR(64) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE purchase_inbound (
    id BIGINT NOT NULL AUTO_INCREMENT,
    corpid VARCHAR(64) NOT NULL,
    inbound_no VARCHAR(64),
    purchase_order_id BIGINT NOT NULL,
    supplier_id BIGINT NOT NULL,
    supplier_name VARCHAR(128),
    warehouse_id BIGINT,
    inbound_date BIGINT,
    total_amount DECIMAL(18, 2) NOT NULL,
    status VARCHAR(32) NOT NULL,
    audit_status INT NOT NULL,
    remark VARCHAR(255),
    del TINYINT NOT NULL DEFAULT 0,
    add_time BIGINT NOT NULL,
    update_time BIGINT NOT NULL,
    creator_id VARCHAR(64) NOT NULL,
    modify_id VARCHAR(64) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE purchase_inbound_item (
    id BIGINT NOT NULL AUTO_INCREMENT,
    corpid VARCHAR(64) NOT NULL,
    purchase_inbound_id BIGINT NOT NULL,
    purchase_order_item_id BIGINT NOT NULL,
    sku_id BIGINT NOT NULL,
    sku_name VARCHAR(128) NOT NULL,
    unit_name VARCHAR(32) NOT NULL,
    warehouse_id BIGINT NOT NULL,
    qty DECIMAL(18, 6) NOT NULL,
    unit_price DECIMAL(18, 6) NOT NULL,
    amount DECIMAL(18, 2) NOT NULL,
    cost_unit DECIMAL(18, 6) NOT NULL,
    cost_amount DECIMAL(18, 2) NOT NULL,
    del TINYINT NOT NULL DEFAULT 0,
    add_time BIGINT NOT NULL,
    update_time BIGINT NOT NULL,
    creator_id VARCHAR(64) NOT NULL,
    modify_id VARCHAR(64) NOT NULL,
    PRIMARY KEY (id)
);
