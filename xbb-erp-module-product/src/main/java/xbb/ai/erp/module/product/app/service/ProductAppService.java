package xbb.ai.erp.module.product.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xbb.ai.erp.module.product.admin.dto.ProductCreateDTO;
import xbb.ai.erp.module.product.admin.dto.ProductDeleteDTO;
import xbb.ai.erp.module.product.admin.dto.ProductDetailDTO;
import xbb.ai.erp.module.product.admin.dto.ProductSkuListDTO;
import xbb.ai.erp.module.product.admin.dto.ProductSpuListDTO;
import xbb.ai.erp.module.product.admin.dto.ProductSpuSkuListDTO;
import xbb.ai.erp.module.product.admin.dto.ProductUpdateDTO;
import xbb.ai.erp.module.product.admin.vo.ProductSkuListVO;
import xbb.ai.erp.module.product.admin.vo.ProductSpuListVO;
import xbb.ai.erp.module.product.admin.vo.ProductSpuSkuListVO;
import xbb.ai.erp.module.product.admin.vo.ProductVO;
import xbb.ai.erp.module.product.domain.model.ProductSku;
import xbb.ai.erp.module.product.domain.model.ProductSpu;
import xbb.ai.erp.module.product.domain.repository.ProductSkuRepository;
import xbb.ai.erp.module.product.domain.repository.ProductSpuRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductAppService {

    private final ProductSpuRepository productSpuRepository;
    private final ProductSkuRepository productSkuRepository;

    public Long create(ProductCreateDTO dto) {
        ProductSpu productSpu = new ProductSpu();
        productSpu.setCorpid(dto.getCorpid());
        productSpu.setSpuCode(dto.getSpuCode());
        productSpu.setSpuName(dto.getSpuName());
        productSpu.setCategoryId(dto.getCategoryId());
        productSpu.setBrandId(dto.getBrandId());
        productSpu.setProductType(dto.getProductType());
        productSpu.setEnableSpec(dto.getEnableSpec());
        productSpu.setDescription(dto.getDescription());
        productSpu.setImageUrl(dto.getImageUrl());
        productSpu.setEnableStatus(dto.getSpuEnableStatus());
        Long spuId = productSpuRepository.save(productSpu, dto.getUserId());

        ProductSku productSku = new ProductSku();
        productSku.setCorpid(dto.getCorpid());
        productSku.setSpuId(spuId);
        productSku.setSkuCode(dto.getSkuCode());
        productSku.setSkuName(dto.getSkuName());
        productSku.setMnemonicCode(dto.getMnemonicCode());
        productSku.setMainBarcode(dto.getMainBarcode());
        productSku.setCanPurchase(dto.getCanPurchase());
        productSku.setCanSale(dto.getCanSale());
        productSku.setCanInventory(dto.getCanInventory());
        productSku.setCanProduce(dto.getCanProduce());
        productSku.setEnableStatus(dto.getSkuEnableStatus());
        productSku.setListingStatus(dto.getListingStatus());
        productSkuRepository.save(productSku, dto.getUserId());
        return spuId;
    }

    public void update(ProductUpdateDTO dto) {
        ProductSpu productSpu = new ProductSpu();
        productSpu.setId(dto.getSpuId());
        productSpu.setCorpid(dto.getCorpid());
        productSpu.setSpuCode(dto.getSpuCode());
        productSpu.setSpuName(dto.getSpuName());
        productSpu.setCategoryId(dto.getCategoryId());
        productSpu.setBrandId(dto.getBrandId());
        productSpu.setProductType(dto.getProductType());
        productSpu.setEnableSpec(dto.getEnableSpec());
        productSpu.setDescription(dto.getDescription());
        productSpu.setImageUrl(dto.getImageUrl());
        productSpu.setEnableStatus(dto.getSpuEnableStatus());
        productSpuRepository.update(productSpu, dto.getUserId());

        ProductSku productSku = new ProductSku();
        productSku.setId(dto.getSkuId());
        productSku.setCorpid(dto.getCorpid());
        productSku.setSpuId(dto.getSpuId());
        productSku.setSkuCode(dto.getSkuCode());
        productSku.setSkuName(dto.getSkuName());
        productSku.setMnemonicCode(dto.getMnemonicCode());
        productSku.setMainBarcode(dto.getMainBarcode());
        productSku.setCanPurchase(dto.getCanPurchase());
        productSku.setCanSale(dto.getCanSale());
        productSku.setCanInventory(dto.getCanInventory());
        productSku.setCanProduce(dto.getCanProduce());
        productSku.setEnableStatus(dto.getSkuEnableStatus());
        productSku.setListingStatus(dto.getListingStatus());
        productSkuRepository.update(productSku, dto.getUserId());
    }

    public void remove(ProductDeleteDTO dto) {
        ProductSku productSku = productSkuRepository.findById(dto.getCorpid(), dto.getSkuId());
        Long spuId = dto.getSpuId() != null ? dto.getSpuId() : productSku == null ? null : productSku.getSpuId();
        if (dto.getSkuId() != null) {
            productSkuRepository.removeById(dto.getCorpid(), dto.getSkuId(), dto.getUserId());
        }
        if (spuId != null) {
            productSpuRepository.removeById(dto.getCorpid(), spuId, dto.getUserId());
        }
    }

    public ProductVO detail(ProductDetailDTO dto) {
        ProductSpu productSpu = productSpuRepository.findById(dto.getCorpid(), dto.getSpuId());
        ProductSku productSku = dto.getSkuId() == null
            ? productSkuRepository.findBySpuId(dto.getCorpid(), dto.getSpuId())
            : productSkuRepository.findById(dto.getCorpid(), dto.getSkuId());
        return toVO(productSpu, productSku);
    }

    public List<ProductSpuListVO> listSpu(ProductSpuListDTO dto) {
        return productSpuRepository.findByCondition(toSpuCondition(dto)).stream().map(this::toSpuListVO).toList();
    }

    public List<ProductSkuListVO> listSku(ProductSkuListDTO dto) {
        return productSkuRepository.findByCondition(toSkuCondition(dto)).stream().map(this::toSkuListVO).toList();
    }

    public List<ProductSpuSkuListVO> listSpuSku(ProductSpuSkuListDTO dto) {
        return productSkuRepository.findSpuSkuList(toSpuSkuCondition(dto));
    }

    private Map<String, Object> toSpuCondition(ProductSpuListDTO dto) {
        Map<String, Object> condition = new HashMap<>();
        condition.put("corpid", dto.getCorpid());
        condition.put("spuCode", dto.getSpuCode());
        condition.put("spuName", dto.getSpuName());
        condition.put("offset", dto.getOffset());
        condition.put("pageSize", dto.getPageSize());
        return condition;
    }

    private Map<String, Object> toSkuCondition(ProductSkuListDTO dto) {
        Map<String, Object> condition = new HashMap<>();
        condition.put("corpid", dto.getCorpid());
        condition.put("skuCode", dto.getSkuCode());
        condition.put("skuName", dto.getSkuName());
        condition.put("offset", dto.getOffset());
        condition.put("pageSize", dto.getPageSize());
        return condition;
    }

    private Map<String, Object> toSpuSkuCondition(ProductSpuSkuListDTO dto) {
        Map<String, Object> condition = new HashMap<>();
        condition.put("corpid", dto.getCorpid());
        condition.put("spuCode", dto.getSpuCode());
        condition.put("spuName", dto.getSpuName());
        condition.put("skuCode", dto.getSkuCode());
        condition.put("skuName", dto.getSkuName());
        condition.put("offset", dto.getOffset());
        condition.put("pageSize", dto.getPageSize());
        return condition;
    }

    private ProductVO toVO(ProductSpu productSpu, ProductSku productSku) {
        ProductVO vo = new ProductVO();
        if (productSpu != null) {
            vo.setSpuId(productSpu.getId());
            vo.setSpuCode(productSpu.getSpuCode());
            vo.setSpuName(productSpu.getSpuName());
            vo.setCategoryId(productSpu.getCategoryId());
            vo.setBrandId(productSpu.getBrandId());
            vo.setProductType(productSpu.getProductType());
            vo.setEnableSpec(productSpu.getEnableSpec());
            vo.setDescription(productSpu.getDescription());
            vo.setImageUrl(productSpu.getImageUrl());
            vo.setSpuEnableStatus(productSpu.getEnableStatus());
        }
        if (productSku != null) {
            vo.setSkuId(productSku.getId());
            vo.setSkuCode(productSku.getSkuCode());
            vo.setSkuName(productSku.getSkuName());
            vo.setMnemonicCode(productSku.getMnemonicCode());
            vo.setMainBarcode(productSku.getMainBarcode());
            vo.setCanPurchase(productSku.getCanPurchase());
            vo.setCanSale(productSku.getCanSale());
            vo.setCanInventory(productSku.getCanInventory());
            vo.setCanProduce(productSku.getCanProduce());
            vo.setSkuEnableStatus(productSku.getEnableStatus());
            vo.setListingStatus(productSku.getListingStatus());
        }
        return vo;
    }

    private ProductSpuListVO toSpuListVO(ProductSpu productSpu) {
        ProductSpuListVO vo = new ProductSpuListVO();
        vo.setSpuId(productSpu.getId());
        vo.setSpuCode(productSpu.getSpuCode());
        vo.setSpuName(productSpu.getSpuName());
        vo.setProductType(productSpu.getProductType());
        vo.setEnableStatus(productSpu.getEnableStatus());
        return vo;
    }

    private ProductSkuListVO toSkuListVO(ProductSku productSku) {
        ProductSkuListVO vo = new ProductSkuListVO();
        vo.setSkuId(productSku.getId());
        vo.setSkuCode(productSku.getSkuCode());
        vo.setSkuName(productSku.getSkuName());
        vo.setMainBarcode(productSku.getMainBarcode());
        vo.setEnableStatus(productSku.getEnableStatus());
        vo.setListingStatus(productSku.getListingStatus());
        return vo;
    }
}
