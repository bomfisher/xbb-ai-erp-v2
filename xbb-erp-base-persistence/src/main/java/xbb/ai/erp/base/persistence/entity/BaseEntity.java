package xbb.ai.erp.base.persistence.entity;

import lombok.Data;


@Data
public class BaseEntity {

    private Long id;
    private Long addTime;
    private Long updateTime;
    private Integer del;
}
