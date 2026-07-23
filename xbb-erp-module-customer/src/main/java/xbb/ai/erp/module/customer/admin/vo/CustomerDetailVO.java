package xbb.ai.erp.module.customer.admin.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CustomerDetailVO {
    private CustomerSaveItemVO mainData;
    private List<String> referenceTodoSections = new ArrayList<>();
    private List<String> operateLogTodoSections = new ArrayList<>();
}
