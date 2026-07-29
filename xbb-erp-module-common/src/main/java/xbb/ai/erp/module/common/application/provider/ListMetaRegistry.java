package xbb.ai.erp.module.common.application.provider;

import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.exception.BizException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ListMetaRegistry {

    private final Map<String, ListMetaProvider> providerMap;

    public ListMetaRegistry(List<ListMetaProvider> providers) {
        this.providerMap = providers == null ? Map.of() : providers.stream().collect(
            Collectors.toMap(
                ListMetaProvider::businessCode,
                Function.identity(),
                (left, right) -> right,
                LinkedHashMap::new
            )
        );
    }

    public ListMetaProvider getRequiredProvider(String businessCode) {
        ListMetaProvider provider = providerMap.get(businessCode);
        if (provider == null) {
            throw new BizException("未找到业务编码对应的列表元数据提供者: " + businessCode);
        }
        return provider;
    }
}
