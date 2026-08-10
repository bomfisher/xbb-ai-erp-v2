package xbb.ai.erp.module.demo.application.assembler;

import xbb.ai.erp.module.demo.admin.dto.DemoMainDTO;
import xbb.ai.erp.module.demo.admin.dto.DemoSaveDTO;
import xbb.ai.erp.module.demo.admin.vo.DemoDetailVO;
import xbb.ai.erp.module.demo.admin.vo.DemoListItemVO;
import xbb.ai.erp.module.demo.admin.vo.DemoSaveItemVO;
import xbb.ai.erp.module.demo.domain.model.Demo;

public final class DemoAdminAssembler {

    private DemoAdminAssembler() {
    }

    public static DemoSaveItemVO buildEmptySaveItemVO() {
        return new DemoSaveItemVO();
    }

    public static Demo toDemo(DemoSaveDTO dto) {
        Demo demo = new Demo();
        DemoMainDTO main = dto.getMain();
        if (main != null) {
            demo.setId(main.getId());
            demo.setCorpid(main.getCorpid());
            demo.setName(main.getName());
            demo.setUserId(main.getUserId());
            demo.setDepId(main.getDepId());
            demo.setComb(main.getComb());
            demo.setCombMulti(main.getCombMulti());
            demo.setNumInt(main.getNumInt());
            demo.setNumDouble(main.getNumDouble());
            demo.setAmount(main.getAmount());
            demo.setDate(main.getDate());
            demo.setTime(main.getTime());
            demo.setFile(main.getFile());
            demo.setImage(main.getImage());
            demo.setAddress(main.getAddress());
            demo.setCreatorId(main.getCreatorId());
            demo.setModifyId(main.getModifyId());
            demo.setDel(main.getDel());
            demo.setAddTime(main.getAddTime());
            demo.setUpdateTime(main.getUpdateTime());
        }
        demo.setCorpid(dto.getCorpid());
        return demo;
    }

    public static DemoListItemVO toListItemVO(Demo demo) {
        DemoListItemVO vo = new DemoListItemVO();
        vo.setId(demo.getId());
        vo.setName(demo.getName());
        vo.setUserId(demo.getUserId());
        vo.setDepId(demo.getDepId());
        vo.setComb(demo.getComb());
        vo.setCombMulti(demo.getCombMulti());
        vo.setNumInt(demo.getNumInt());
        vo.setNumDouble(demo.getNumDouble());
        vo.setAmount(demo.getAmount());
        vo.setDate(demo.getDate());
        vo.setTime(demo.getTime());
        vo.setFile(demo.getFile());
        vo.setImage(demo.getImage());
        vo.setAddress(demo.getAddress());
        return vo;
    }

    public static DemoSaveItemVO toSaveItemVO(Demo demo) {
        DemoSaveItemVO vo = new DemoSaveItemVO();
        if (demo == null) {
            return vo;
        }
        DemoMainDTO main = new DemoMainDTO();
        main.setId(demo.getId());
        main.setCorpid(demo.getCorpid());
        main.setName(demo.getName());
        main.setUserId(demo.getUserId());
        main.setDepId(demo.getDepId());
        main.setComb(demo.getComb());
        main.setCombMulti(demo.getCombMulti());
        main.setNumInt(demo.getNumInt());
        main.setNumDouble(demo.getNumDouble());
        main.setAmount(demo.getAmount());
        main.setDate(demo.getDate());
        main.setTime(demo.getTime());
        main.setFile(demo.getFile());
        main.setImage(demo.getImage());
        main.setAddress(demo.getAddress());
        main.setCreatorId(demo.getCreatorId());
        main.setModifyId(demo.getModifyId());
        main.setDel(demo.getDel());
        main.setAddTime(demo.getAddTime());
        main.setUpdateTime(demo.getUpdateTime());
        vo.setMain(main);
        return vo;
    }

    public static DemoDetailVO toDetailVO(DemoSaveItemVO saveItemVO) {
        DemoDetailVO detailVO = new DemoDetailVO();
        detailVO.setMainData(saveItemVO);
        return detailVO;
    }
}
