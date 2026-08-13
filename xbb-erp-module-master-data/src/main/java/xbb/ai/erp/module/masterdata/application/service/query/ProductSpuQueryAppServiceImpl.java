package xbb.ai.erp.module.masterdata.application.service.query;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.common.application.render.ListValueRenderer;
import xbb.ai.erp.module.common.application.util.ListQueryMapUtil;
import xbb.ai.erp.module.masterdata.admin.vo.CustomerListItemVO;
import xbb.ai.erp.module.masterdata.application.assembler.CustomerAdminAssembler;
import xbb.ai.erp.scene.meta.SceneFieldAssembler;
import xbb.ai.erp.scene.meta.SceneTypeEnum;
import xbb.ai.erp.module.masterdata.admin.vo.ProductSpuDetailVO;
import xbb.ai.erp.module.masterdata.admin.vo.ProductSpuListItemVO;
import xbb.ai.erp.module.masterdata.application.assembler.ProductSpuAdminAssembler;
import xbb.ai.erp.module.masterdata.application.field.ProductSpuFieldFactory;
import xbb.ai.erp.module.masterdata.application.schema.ProductSpuListSchemaProvider;
import xbb.ai.erp.module.masterdata.domain.model.ProductSpu;
import xbb.ai.erp.module.masterdata.domain.repository.ProductSpuRepository;

@Service
public class ProductSpuQueryAppServiceImpl {
    private final ProductSpuRepository productSpuRepository;
    private final ProductSpuFieldFactory fieldFactory;
    private final ProductSpuListSchemaProvider schemaProvider;
    private final ListValueRenderer listValueRenderer;
    private final ListQueryMapUtil listQueryMapUtil = new ListQueryMapUtil();

    public ProductSpuQueryAppServiceImpl(ProductSpuRepository productSpuRepository, ProductSpuFieldFactory fieldFactory, ProductSpuListSchemaProvider schemaProvider, ListValueRenderer listValueRenderer) {
        this.productSpuRepository = productSpuRepository;
        this.fieldFactory = fieldFactory;
        this.schemaProvider = schemaProvider;
        this.listValueRenderer = listValueRenderer;
    }

    public ListBaseVO<ProductSpuListItemVO> list(ListBaseDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        Map<String, Object> conditionMap = listQueryMapUtil.gen(dto, schemaProvider.conditionMetaMap());
        List<ProductSpu> list = productSpuRepository.findByCondition(conditionMap);
        Long total = productSpuRepository.count(conditionMap);
        ListBaseVO<ProductSpuListItemVO> vo = new ListBaseVO<>();
        List<ProductSpuListItemVO> items = list.stream().map(ProductSpuAdminAssembler::toListItemVO).toList();
        vo.setList(listValueRenderer.render(dto.getCorpid(), BusinessCodeEnum.PRODUCT_SPU.getCode(), items));
        vo.setPageHelper(new ListBaseVO.PageHelper(dto.getPageNum(), total == null ? 0 : total.intValue()));
        return vo;
    }

    public SaveItemVO<xbb.ai.erp.module.masterdata.admin.vo.ProductSpuSaveItemVO> addItem(BaseDTO dto) {
        SaveItemVO<xbb.ai.erp.module.masterdata.admin.vo.ProductSpuSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(SceneFieldAssembler.buildHeadList(fieldFactory.getFields(SceneTypeEnum.CREATE)));
        vo.setData(ProductSpuAdminAssembler.buildEmptySaveItemVO());
        return vo;
    }

    public SaveItemVO<xbb.ai.erp.module.masterdata.admin.vo.ProductSpuSaveItemVO> updateItem(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        ProductSpu entity = productSpuRepository.findById(dto.getCorpid(), dto.getId());
        SaveItemVO<xbb.ai.erp.module.masterdata.admin.vo.ProductSpuSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(SceneFieldAssembler.buildHeadList(fieldFactory.getFields(SceneTypeEnum.UPDATE)));
        vo.setData(ProductSpuAdminAssembler.toSaveItemVO(entity));
        return vo;
    }

    public ProductSpuDetailVO detail(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        ProductSpu entity = productSpuRepository.findById(dto.getCorpid(), dto.getId());
        return ProductSpuAdminAssembler.toDetailVO(ProductSpuAdminAssembler.toSaveItemVO(entity));
    }
}
