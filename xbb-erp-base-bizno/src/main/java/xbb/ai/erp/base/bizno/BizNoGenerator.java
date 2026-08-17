package xbb.ai.erp.base.bizno;

/** 生成租户隔离的业务编号。 */
public interface BizNoGenerator {

    String next(String corpid, String businessCode);
}
