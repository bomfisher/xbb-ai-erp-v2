package xbb.ai.erp.module.common.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xbb.ai.erp.module.common.infrastructure.persistence.po.MenuPO;

@Mapper
public interface MenuMapper extends BaseMapper<MenuPO> {
}
