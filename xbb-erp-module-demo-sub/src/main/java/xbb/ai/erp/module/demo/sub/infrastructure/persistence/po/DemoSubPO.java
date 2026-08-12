package xbb.ai.erp.module.demo.sub.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("demo_sub")
public class DemoSubPO extends BaseEntity {
  private String corpid;
  private Long dataId;
  private String name;
  private String parentName;
  private String userId;
  private Long departmentId;
  private String creatorId;
  private String modifyId;
}
