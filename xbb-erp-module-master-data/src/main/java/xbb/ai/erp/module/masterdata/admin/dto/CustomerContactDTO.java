package xbb.ai.erp.module.masterdata.admin.dto;

import lombok.Data;

@Data
public class CustomerContactDTO {
    private Long id;
    private String name;
    private String mobile;
    private Integer defaultFlag;
}
