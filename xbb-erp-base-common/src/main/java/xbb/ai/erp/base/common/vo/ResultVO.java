package xbb.ai.erp.base.common.vo;

import lombok.Data;

@Data
public class ResultVO<T> {

    private Integer code;
    private String message;
    private Boolean success;
    private T data;

    public static <T> ResultVO<T> success(T data) {
        ResultVO<T> result = new ResultVO<>();
        result.setCode(0);
        result.setMessage("success");
        result.setSuccess(true);
        result.setData(data);
        return result;
    }

    public static <T> ResultVO<T> failure(Integer code, String message) {
        ResultVO<T> result = new ResultVO<>();
        result.setCode(code);
        result.setMessage(message);
        result.setSuccess(false);
        return result;
    }
}
