package xbb.ai.erp.module.product.application.service.support;

public class FakeProductCodeCheckRepository {

    private boolean duplicatedSpuCode;
    private boolean duplicatedSkuCode;

    public boolean existsDuplicatedSpuCode() {
        return duplicatedSpuCode;
    }

    public boolean existsDuplicatedSkuCode() {
        return duplicatedSkuCode;
    }

    public void setDuplicatedSpuCode(boolean duplicatedSpuCode) {
        this.duplicatedSpuCode = duplicatedSpuCode;
    }

    public void setDuplicatedSkuCode(boolean duplicatedSkuCode) {
        this.duplicatedSkuCode = duplicatedSkuCode;
    }
}
