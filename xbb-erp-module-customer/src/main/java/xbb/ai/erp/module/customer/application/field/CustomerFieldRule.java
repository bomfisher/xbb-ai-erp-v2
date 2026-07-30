package xbb.ai.erp.module.customer.application.field;

import java.util.List;

@FunctionalInterface
public interface CustomerFieldRule {

    List<CustomerFieldMeta> apply(List<CustomerFieldMeta> fields);
}
