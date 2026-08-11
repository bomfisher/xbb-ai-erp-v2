package xbb.ai.erp.module.demo.admin.vo;

import lombok.Data;
import xbb.ai.erp.module.demo.admin.dto.DemoMainDTO;
import xbb.ai.erp.module.demo.admin.dto.DemoItemDTO;
import java.util.List;

@Data
public class DemoSaveItemVO {
    private DemoMainDTO main;
    private List<DemoItemDTO> items;
    private List<DemoItemDTO> items2;
}
