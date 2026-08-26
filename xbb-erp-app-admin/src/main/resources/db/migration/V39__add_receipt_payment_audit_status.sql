alter table receipt add column audit_status tinyint(2) not null default 0 comment '审核状态：0待审核，1审核中，2已审核，3已拒绝';
alter table payment add column audit_status tinyint(2) not null default 0 comment '审核状态：0待审核，1审核中，2已审核，3已拒绝';
