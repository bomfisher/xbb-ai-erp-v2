package xbb.ai.erp.module.demo.infrastructure.persistence.convertor;

import xbb.ai.erp.module.demo.domain.model.DemoItem;
import xbb.ai.erp.module.demo.infrastructure.persistence.po.DemoItemPO;

public final class DemoItemConvertor {

    private DemoItemConvertor() {
    }

    public static DemoItemPO toPO(DemoItem demoItem) {
        if (demoItem == null) {
            return null;
        }
        DemoItemPO po = new DemoItemPO();
        po.setId(demoItem.getId());
        po.setCorpid(demoItem.getCorpid());
        po.setDataId(demoItem.getDataId());
        po.setName(demoItem.getName());
        po.setDeleted(demoItem.getDeleted());
        po.setAddTime(demoItem.getAddTime());
        po.setUpdateTime(demoItem.getUpdateTime());
        po.setCreatorId(demoItem.getCreatorId());
        po.setModifyId(demoItem.getModifyId());
        return po;
    }

    public static DemoItem toDomain(DemoItemPO po) {
        if (po == null) {
            return null;
        }
        DemoItem demoItem = new DemoItem();
        demoItem.setId(po.getId());
        demoItem.setCorpid(po.getCorpid());
        demoItem.setDataId(po.getDataId());
        demoItem.setName(po.getName());
        demoItem.setDeleted(po.getDeleted());
        demoItem.setAddTime(po.getAddTime());
        demoItem.setUpdateTime(po.getUpdateTime());
        demoItem.setCreatorId(po.getCreatorId());
        demoItem.setModifyId(po.getModifyId());
        return demoItem;
    }
}
