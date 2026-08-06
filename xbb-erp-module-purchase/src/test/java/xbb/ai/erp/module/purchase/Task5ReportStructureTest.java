package xbb.ai.erp.module.purchase;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class Task5ReportStructureTest {

    private static final Path REPORT_PATH = Path.of("/Users/bomfish/xbb-ai-erp-v2/.git/sdd/task-5-report.md");

    @Test
    void should_write_task_5_report_with_required_sections() throws Exception {
        assertTrue(Files.exists(REPORT_PATH), "Task 5 报告文件必须存在");
        String report = Files.readString(REPORT_PATH);
        assertContains(report, "## Status");
        assertContains(report, "## 新增/修改文件");
        assertContains(report, "## 先写了哪些失败测试，失败原因是什么");
        assertContains(report, "## 做了哪些实现/为什么没有改生产或 SQL");
        assertContains(report, "## 跑了哪些测试/命令，结果如何");
        assertContains(report, "## 阻塞与修复");
        assertContains(report, "## Concerns");
    }

    private void assertContains(String report, String expected) {
        assertTrue(report.contains(expected), () -> "报告缺少章节: " + expected);
    }
}
