package xbb.ai.erp.module.demo.sub.application.port;

import java.util.List;
import xbb.ai.erp.module.demo.sub.application.pojo.DemoSubSaveDraftPojo;

public interface DemoSubDraftRepository {
  String saveDraft(DemoSubSaveDraftPojo draft);

  List<DemoSubSaveDraftPojo> listDrafts(String corpid, int limit);

  DemoSubSaveDraftPojo loadDraft(String corpid, String draftCode);

  void removeDraft(String corpid, String draftCode);
}
