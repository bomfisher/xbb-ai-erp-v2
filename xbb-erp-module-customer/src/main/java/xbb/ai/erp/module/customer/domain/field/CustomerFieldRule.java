package xbb.ai.erp.module.customer.domain.field;

import java.util.List;

@FunctionalInterface
public interface CustomerFieldRule {

    List<CustomerFieldMeta> apply(List<CustomerFieldMeta> fields);
}
