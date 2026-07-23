package xbb.ai.erp.module.customer.admin.dto;

import lombok.Data;

@Data
public class CustomerAddressItemDTO {
    private Long id;
    private String addressType;
    private String receiverName;
    private String receiverMobile;
    private String provinceCode;
    private String cityCode;
    private String districtCode;
    private String detailAddress;
    private String postalCode;
    private Integer defaultFlag;
    private String bizStatus;
    private Integer version;
}
