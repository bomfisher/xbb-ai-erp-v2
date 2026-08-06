package xbb.ai.erp.module.product.application.service.support;

import xbb.ai.erp.module.product.admin.dto.ProductMainDTO;
import xbb.ai.erp.module.product.admin.dto.ProductSkuItemDTO;
import xbb.ai.erp.module.product.application.pojo.ProductSaveDraftPojo;
import xbb.ai.erp.module.product.application.port.ProductDraftRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class InMemoryProductDraftRepository implements ProductDraftRepository {

    private final List<ProductSaveDraftPojo> data = new ArrayList<>();

    public List<ProductSaveDraftPojo> all() {
        return data;
    }

    public void seed(ProductSaveDraftPojo draft) {
        data.add(copy(draft));
    }

    @Override
    public String saveDraft(ProductSaveDraftPojo draft) {
        if (draft.getDraftCode() == null || draft.getDraftCode().isBlank()) {
            draft.setDraftCode(UUID.randomUUID().toString());
        }
        if (draft.getDraftId() == null) {
            draft.setDraftId(Math.abs(UUID.randomUUID().getMostSignificantBits()));
            if (draft.getDraftId() == 0L) {
                draft.setDraftId(1L);
            }
        }
        if (draft.getUpdatedTime() == null) {
            draft.setUpdatedTime(System.currentTimeMillis());
        }
        data.removeIf(item -> draft.getCorpid().equals(item.getCorpid()) && draft.getDraftId().equals(item.getDraftId()));
        data.add(copy(draft));
        data.sort(Comparator.comparing(ProductSaveDraftPojo::getUpdatedTime, Comparator.nullsLast(Comparator.reverseOrder())));
        while (data.stream().filter(item -> draft.getCorpid().equals(item.getCorpid())).count() > 10) {
            ProductSaveDraftPojo oldest = data.stream()
                .filter(item -> draft.getCorpid().equals(item.getCorpid()))
                .min(Comparator.comparing(ProductSaveDraftPojo::getUpdatedTime, Comparator.nullsLast(Comparator.naturalOrder())))
                .orElse(null);
            if (oldest == null) {
                break;
            }
            data.remove(oldest);
        }
        return draft.getDraftCode();
    }

    @Override
    public List<ProductSaveDraftPojo> listDrafts(String corpid, int limit) {
        return data.stream()
            .filter(item -> corpid.equals(item.getCorpid()))
            .sorted(Comparator.comparing(ProductSaveDraftPojo::getUpdatedTime, Comparator.nullsLast(Comparator.reverseOrder())))
            .limit(Math.max(limit, 0))
            .map(this::copy)
            .toList();
    }

    @Override
    public ProductSaveDraftPojo loadDraft(String corpid, Long draftId) {
        return data.stream()
            .filter(item -> corpid.equals(item.getCorpid()) && draftId.equals(item.getDraftId()))
            .findFirst()
            .map(this::copy)
            .orElse(null);
    }

    @Override
    public void removeDraft(String corpid, String draftCode) {
        data.removeIf(item -> corpid.equals(item.getCorpid()) && draftCode.equals(item.getDraftCode()));
    }

    private ProductSaveDraftPojo copy(ProductSaveDraftPojo source) {
        if (source == null) {
            return null;
        }
        ProductSaveDraftPojo target = new ProductSaveDraftPojo();
        target.setCorpid(source.getCorpid());
        target.setDraftId(source.getDraftId());
        target.setDraftCode(source.getDraftCode());
        target.setDraftName(source.getDraftName());
        target.setUpdatedTime(source.getUpdatedTime());
        if (source.getMain() != null) {
            ProductMainDTO main = new ProductMainDTO();
            main.setSpuId(source.getMain().getSpuId());
            main.setSpuCode(source.getMain().getSpuCode());
            main.setSpuName(source.getMain().getSpuName());
            main.setCategoryId(source.getMain().getCategoryId());
            main.setBrandId(source.getMain().getBrandId());
            main.setProductType(source.getMain().getProductType());
            main.setEnableSpec(source.getMain().getEnableSpec());
            main.setDescription(source.getMain().getDescription());
            main.setImageUrl(source.getMain().getImageUrl());
            main.setSpuEnableStatus(source.getMain().getSpuEnableStatus());
            target.setMain(main);
        }
        if (source.getSkus() != null) {
            target.setSkus(source.getSkus().stream().map(this::copySku).toList());
        }
        return target;
    }

    private ProductSkuItemDTO copySku(ProductSkuItemDTO source) {
        ProductSkuItemDTO target = new ProductSkuItemDTO();
        target.setSkuId(source.getSkuId());
        target.setSkuCode(source.getSkuCode());
        target.setSkuName(source.getSkuName());
        target.setSpecSignature(source.getSpecSignature());
        target.setSpecSnapshot(source.getSpecSnapshot());
        target.setMnemonicCode(source.getMnemonicCode());
        target.setMainBarcode(source.getMainBarcode());
        target.setCanPurchase(source.getCanPurchase());
        target.setCanSale(source.getCanSale());
        target.setCanInventory(source.getCanInventory());
        target.setCanProduce(source.getCanProduce());
        target.setSkuEnableStatus(source.getSkuEnableStatus());
        target.setListingStatus(source.getListingStatus());
        return target;
    }
}
