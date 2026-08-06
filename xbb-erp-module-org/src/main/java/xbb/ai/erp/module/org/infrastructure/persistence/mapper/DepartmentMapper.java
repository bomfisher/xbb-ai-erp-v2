package xbb.ai.erp.module.org.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xbb.ai.erp.module.org.infrastructure.persistence.po.DepartmentPO;

@Mapper
public interface DepartmentMapper extends BaseMapper<DepartmentPO> {
}
