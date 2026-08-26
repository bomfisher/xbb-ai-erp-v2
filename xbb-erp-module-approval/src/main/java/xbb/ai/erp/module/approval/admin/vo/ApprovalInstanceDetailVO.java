package xbb.ai.erp.module.approval.admin.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 审批实例详情。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ApprovalInstanceDetailVO extends ApprovalInstanceListItemVO {

    private String flowName;

    private Integer flowVersion;

    private String scene;

    private String subjectSnapshotJson;

    private List<TimelineItemVO> timeline;

    private List<FlowNodeVO> flowNodes;

    @Data
    public static class TimelineItemVO {

        private String nodeName;

        private String operatorName;

        private String action;

        private String comment;

        private String operatedAt;
    }

    @Data
    public static class FlowNodeVO {

        private Integer nodeNo;

        private String nodeName;

        private String approverSummary;

        private String status;
    }
}
