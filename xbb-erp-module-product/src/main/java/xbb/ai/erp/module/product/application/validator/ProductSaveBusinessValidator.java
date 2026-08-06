package xbb.ai.erp.module.product.application.validator;

import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.product.admin.dto.ProductSkuItemDTO;
import xbb.ai.erp.module.product.application.pojo.ProductSaveContextPojo;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProductSaveBusinessValidator {

    public void validateForSubmit(ProductSaveContextPojo context) {
        if (context == null || context.getSkus() == null) {
            return;
        }
        validateDuplicate(context.getSkus(), ProductSkuItemDTO::getSkuCode, "同一商品下SKU编码不允许重复");
        validateDuplicate(context.getSkus(), ProductSkuItemDTO::getSpecSignature, "同一商品下规格签名不允许重复");
    }

    private void validateDuplicate(List<ProductSkuItemDTO> skus, Function<ProductSkuItemDTO, String> getter, String message) {
        Map<String, Long> countMap = skus.stream()
            .map(getter)
            .filter(Objects::nonNull)
            .filter(value -> !value.isBlank())
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        boolean duplicated = countMap.values().stream().anyMatch(count -> count > 1);
        if (duplicated) {
            throw new BizException(message);
        }
    }
}
