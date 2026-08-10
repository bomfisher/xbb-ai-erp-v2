package xbb.ai.erp.module.demo.infrastructure.persistence.convertor;

import xbb.ai.erp.module.demo.domain.model.Demo;
import xbb.ai.erp.module.demo.infrastructure.persistence.po.DemoPO;

public final class DemoConvertor {

    private DemoConvertor() {
    }

    public static DemoPO toPO(Demo demo) {
        if (demo == null) {
            return null;
        }
        DemoPO po = new DemoPO();
        po.setId(demo.getId());
        po.setCorpid(demo.getCorpid());
        po.setName(demo.getName());
        po.setUserId(demo.getUserId());
        po.setDepId(demo.getDepId());
        po.setComb(demo.getComb());
        po.setCombMulti(demo.getCombMulti());
        po.setNumInt(demo.getNumInt());
        po.setNumDouble(demo.getNumDouble());
        po.setAmount(demo.getAmount());
        po.setDate(demo.getDate());
        po.setTime(demo.getTime());
        po.setFile(demo.getFile());
        po.setImage(demo.getImage());
        po.setAddress(demo.getAddress());
        po.setCreatorId(demo.getCreatorId());
        po.setModifyId(demo.getModifyId());
        po.setDel(demo.getDel());
        po.setAddTime(demo.getAddTime());
        po.setUpdateTime(demo.getUpdateTime());
        return po;
    }

    public static Demo toDomain(DemoPO po) {
        if (po == null) {
            return null;
        }
        Demo demo = new Demo();
        demo.setId(po.getId());
        demo.setCorpid(po.getCorpid());
        demo.setName(po.getName());
        demo.setUserId(po.getUserId());
        demo.setDepId(po.getDepId());
        demo.setComb(po.getComb());
        demo.setCombMulti(po.getCombMulti());
        demo.setNumInt(po.getNumInt());
        demo.setNumDouble(po.getNumDouble());
        demo.setAmount(po.getAmount());
        demo.setDate(po.getDate());
        demo.setTime(po.getTime());
        demo.setFile(po.getFile());
        demo.setImage(po.getImage());
        demo.setAddress(po.getAddress());
        demo.setCreatorId(po.getCreatorId());
        demo.setModifyId(po.getModifyId());
        demo.setDel(po.getDel());
        demo.setAddTime(po.getAddTime());
        demo.setUpdateTime(po.getUpdateTime());
        return demo;
    }
}
