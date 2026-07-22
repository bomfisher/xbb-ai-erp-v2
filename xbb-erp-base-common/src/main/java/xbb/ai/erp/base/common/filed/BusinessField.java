package xbb.ai.erp.base.common.filed;

import java.util.ArrayList;
import java.util.List;

public interface BusinessField {

    default List<String> getRequiredList(){ return new ArrayList<>();}

    default List<String> getEditableList(){ return new ArrayList<>();}

}
