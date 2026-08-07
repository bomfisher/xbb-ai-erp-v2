package xbb.ai.erp.module.demo.admin.vo;

import lombok.Data;
import xbb.ai.erp.module.demo.admin.dto.DemoMainDTO;

@Data
public class DemoDraftDetailVO {
    private String draftCode;
    private DemoMainDTO main;
}
