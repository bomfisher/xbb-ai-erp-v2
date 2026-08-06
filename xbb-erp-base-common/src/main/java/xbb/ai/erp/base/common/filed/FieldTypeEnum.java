package xbb.ai.erp.base.common.filed;

import lombok.Getter;

@Getter
public enum FieldTypeEnum {
    //文本
    TEXT(1),
    //整数数字
    NUM_INT(2),
    //浮点数字
    NUM_DOUBLE(3),
    //金额
    AMOUNT(4),
    //库存
    STOCK(5),
    //日期 年月日
    DATE(6),
    //时间 年月日 时分秒
    TIME(7),
    //下拉选项
    COMB(8),
    //下拉多选
    COMB_MULTI(9),
    //复选框
    CHECKBOX(10),
    //单选框
    RADIO_BTN(11),
    //开关
    SWITCH(19),
    //员工单选
    USER(12),
    //员工多选
    USER_MULTI(13),
    //部门单选
    DEPT(14),
    //部门多选
    DEPT_MULTI(15),
    //业务单选
    BUSINESS(16),
    //业务多选
    BUSINESS_MULTI(16),
    //附件
    FILE(17),
    //图片
    IMAGE(18),

    PRODUCT(50),
    ;

    private Integer type;

    FieldTypeEnum(Integer type) {
        this.type = type;
    }
}
