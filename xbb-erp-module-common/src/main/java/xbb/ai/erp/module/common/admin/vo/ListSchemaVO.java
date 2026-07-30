package xbb.ai.erp.module.common.admin.vo;

import lombok.Data;

@Data
public class ListSchemaVO {
    private ListFilterVO filter;
    private ListHeaderVO header;
    private ListTopButtonVO topButton;
    private ListBottomButtonVO bottomButton;
    private ListRowActionVO rowAction;
}
