package xbb.ai.erp.module.customer.application.service.support;

import xbb.ai.erp.module.customer.application.pojo.CustomerSaveDraftPojo;
import xbb.ai.erp.module.customer.application.port.CustomerDraftRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class InMemoryCustomerDraftRepository implements CustomerDraftRepository {

    private final List<CustomerSaveDraftPojo> data = new ArrayList<>();

    public List<CustomerSaveDraftPojo> all() {
        return data;
    }

    public void seed(CustomerSaveDraftPojo draft) {
        data.add(copy(draft));
    }

    @Override
    public String saveDraft(CustomerSaveDraftPojo draft) {
        String draftCode = draft.getDraftCode();
        if (draftCode == null || draftCode.isBlank()) {
            draftCode = UUID.randomUUID().toString();
            draft.setDraftCode(draftCode);
        }
        removeDraft(draft.getCorpid(), draftCode);
        data.add(copy(draft));
        return draftCode;
    }

    @Override
    public List<CustomerSaveDraftPojo> listDrafts(String corpid, int limit) {
        return data.stream()
            .filter(item -> corpid.equals(item.getCorpid()))
            .sorted(Comparator.comparing(CustomerSaveDraftPojo::getUpdatedTime, Comparator.nullsLast(Comparator.reverseOrder())))
            .limit(Math.max(limit, 0))
            .map(this::copy)
            .toList();
    }

    @Override
    public CustomerSaveDraftPojo loadDraft(String corpid, String draftCode) {
        return data.stream()
            .filter(item -> corpid.equals(item.getCorpid()) && draftCode.equals(item.getDraftCode()))
            .findFirst()
            .map(this::copy)
            .orElse(null);
    }

    @Override
    public void removeDraft(String corpid, String draftCode) {
        data.removeIf(item -> corpid.equals(item.getCorpid()) && draftCode.equals(item.getDraftCode()));
    }

    private CustomerSaveDraftPojo copy(CustomerSaveDraftPojo source) {
        if (source == null) {
            return null;
        }
        CustomerSaveDraftPojo target = new CustomerSaveDraftPojo();
        target.setCorpid(source.getCorpid());
        target.setDraftCode(source.getDraftCode());
        target.setDraftTitle(source.getDraftTitle());
        target.setUpdatedTime(source.getUpdatedTime());
        if (source.getMain() != null) {
            target.setMain(source.getMain());
        }
        if (source.getExt() != null) {
            target.setExt(source.getExt());
        }
        if (source.getSectionState() != null) {
            target.setSectionState(source.getSectionState());
        }
        return target;
    }
}
