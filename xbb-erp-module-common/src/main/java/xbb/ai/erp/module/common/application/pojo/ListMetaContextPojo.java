package xbb.ai.erp.module.common.application.pojo;

import lombok.Data;

import java.util.List;

@Data
public class ListMetaContextPojo {
    private String corpid;
    private String userId;
    private String businessCode;
    private List<String> packageCodeList;
}
