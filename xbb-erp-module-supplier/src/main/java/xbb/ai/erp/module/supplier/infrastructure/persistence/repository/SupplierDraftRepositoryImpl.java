package xbb.ai.erp.module.supplier.infrastructure.persistence.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.supplier.application.pojo.SupplierSaveDraftPojo;
import xbb.ai.erp.module.supplier.application.port.SupplierDraftRepository;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class SupplierDraftRepositoryImpl implements SupplierDraftRepository {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final List<SupplierSaveDraftPojo> data = new CopyOnWriteArrayList<>();

    @Override
    public String saveDraft(SupplierSaveDraftPojo draft) {
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
    public List<SupplierSaveDraftPojo> listDrafts(String corpid, int limit) {
        return data.stream()
            .filter(item -> corpid.equals(item.getCorpid()))
            .sorted(Comparator.comparing(SupplierSaveDraftPojo::getUpdatedTime, Comparator.nullsLast(Comparator.reverseOrder())))
            .limit(Math.max(limit, 0))
            .map(this::copy)
            .toList();
    }

    @Override
    public SupplierSaveDraftPojo loadDraft(String corpid, String draftCode) {
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

    private SupplierSaveDraftPojo copy(SupplierSaveDraftPojo source) {
        if (source == null) {
            return null;
        }
        return OBJECT_MAPPER.convertValue(source, SupplierSaveDraftPojo.class);
    }
}
