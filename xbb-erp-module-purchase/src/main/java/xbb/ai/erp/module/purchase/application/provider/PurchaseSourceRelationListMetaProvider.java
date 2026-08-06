package xbb.ai.erp.module.purchase.application.provider;

import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO;
import xbb.ai.erp.module.common.admin.pojo.FilterField;
import xbb.ai.erp.module.common.application.filter.ListFilterMetaPojo;
import xbb.ai.erp.module.common.application.pojo.ListMetaBundlePojo;
import xbb.ai.erp.module.common.application.provider.ListMetaProvider;

import java.util.List;
import java.util.Map;

@Component
public class PurchaseSourceRelationListMetaProvider implements ListMetaProvider {

    private static final List<PurchaseListMetaSupport.FilterDefinition> FILTER_DEFINITIONS = List.of(
        new PurchaseListMetaSupport.FilterDefinition("sourceDocType", "来源单据类型", "TEXT", "source_doc_type", PurchaseListMetaSupport.TEXT_SYMBOLS, List.of()),
        new PurchaseListMetaSupport.FilterDefinition("sourceDocId", "来源单据ID", "ID", "source_doc_id", PurchaseListMetaSupport.ID_SYMBOLS, List.of()),
        new PurchaseListMetaSupport.FilterDefinition("sourceLineId", "来源单据行ID", "ID", "source_line_id", PurchaseListMetaSupport.ID_SYMBOLS, List.of()),
        new PurchaseListMetaSupport.FilterDefinition("targetDocType", "目标单据类型", "TEXT", "target_doc_type", PurchaseListMetaSupport.TEXT_SYMBOLS, List.of()),
        new PurchaseListMetaSupport.FilterDefinition("targetDocId", "目标单据ID", "ID", "target_doc_id", PurchaseListMetaSupport.ID_SYMBOLS, List.of()),
        new PurchaseListMetaSupport.FilterDefinition("targetLineId", "目标单据行ID", "ID", "target_line_id", PurchaseListMetaSupport.ID_SYMBOLS, List.of()),
        new PurchaseListMetaSupport.FilterDefinition("relationStatus", "关系状态", "TEXT", "relation_status", PurchaseListMetaSupport.TEXT_SYMBOLS, List.of())
    );
    private static final Map<String, ListFilterMetaPojo> CONDITION_META_MAP = PurchaseListMetaSupport.buildConditionMetaMap(FILTER_DEFINITIONS);

    @Override
    public String businessCode() {
        return BusinessCodeEnum.PURCHASE_SOURCE_RELATION.getCode();
    }

    @Override
    public List<FilterField> buildFilterMeta(ListCommonQueryDTO dto) {
        return FILTER_DEFINITIONS.stream().map(PurchaseListMetaSupport::buildFilterField).toList();
    }

    @Override
    public Map<String, ListFilterMetaPojo> buildFilterConditionMeta(ListCommonQueryDTO dto) {
        return CONDITION_META_MAP;
    }

    @Override
    public List<FieldEntity> buildHeaderMeta(ListCommonQueryDTO dto) {
        return List.of(
            PurchaseListMetaSupport.buildHeader("main.sourceDocType", "来源单据类型"),
            PurchaseListMetaSupport.buildHeader("main.sourceDocId", "来源单据ID"),
            PurchaseListMetaSupport.buildHeader("main.targetDocType", "目标单据类型"),
            PurchaseListMetaSupport.buildHeader("main.targetDocId", "目标单据ID"),
            PurchaseListMetaSupport.buildHeader("main.sourceQty", "来源数量"),
            PurchaseListMetaSupport.buildHeader("main.reservedQty", "已占用数量"),
            PurchaseListMetaSupport.buildHeader("main.executedQty", "已执行数量"),
            PurchaseListMetaSupport.buildHeader("main.closedQty", "已关闭数量"),
            PurchaseListMetaSupport.buildHeader("main.reversedQty", "已回退数量"),
            PurchaseListMetaSupport.buildHeader("main.relationStatus", "关系状态"),
            PurchaseListMetaSupport.buildHeader("main.addTime", "创建时间"),
            PurchaseListMetaSupport.buildHeader("main.updateTime", "更新时间")
        );
    }

    @Override
    public ListMetaBundlePojo buildTopButtonMeta(ListCommonQueryDTO dto) {
        ListMetaBundlePojo bundle = new ListMetaBundlePojo();
        bundle.setTopButtonList(List.of(PurchaseListMetaSupport.buildButton("ADD", "新增", 10, "ADD")));
        return bundle;
    }

    @Override
    public ListMetaBundlePojo buildBottomButtonMeta(ListCommonQueryDTO dto) {
        ListMetaBundlePojo bundle = new ListMetaBundlePojo();
        bundle.setBottomButtonList(List.of(PurchaseListMetaSupport.buildButton("EXPORT", "导出", 20, "EXPORT")));
        return bundle;
    }

    @Override
    public ListMetaBundlePojo buildRowActionMeta(ListCommonQueryDTO dto) {
        ListMetaBundlePojo bundle = new ListMetaBundlePojo();
        bundle.setRowActionList(List.of(PurchaseListMetaSupport.buildRowAction("EDIT", "编辑", 10, "PRIMARY", "NONE")));
        return bundle;
    }
}
