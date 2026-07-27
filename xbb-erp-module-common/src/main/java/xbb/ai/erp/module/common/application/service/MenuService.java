package xbb.ai.erp.module.common.application.service;

import xbb.ai.erp.module.common.admin.dto.MenuListDTO;
import xbb.ai.erp.module.common.admin.vo.MenuListVO;

public interface MenuService {
    MenuListVO list(MenuListDTO dto);
}
