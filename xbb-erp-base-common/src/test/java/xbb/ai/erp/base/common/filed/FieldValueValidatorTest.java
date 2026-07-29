package xbb.ai.erp.base.common.filed;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.exception.BizException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FieldValueValidatorTest {

    private final FieldValueValidator validator = new FieldValueValidator();

    @Test
    void should_reject_text_value_when_length_exceeds_rule() {
        DemoPojo pojo = new DemoPojo();
        pojo.setName("123456");

        BizException ex = assertThrows(
                BizException.class,
            () -> validator.validate(java.util.List.of(new FieldRule("name", "名称", FieldTypeEnum.TEXT.getType(), 5)), pojo)
        );

        assertEquals("名称长度不能超过5", ex.getMessage());
    }

    @Test
    void should_reject_integer_text_when_format_is_invalid() {
        DemoPojo pojo = new DemoPojo();
        pojo.setCount("12a");

        BizException ex = assertThrows(
            BizException.class,
            () -> validator.validate(java.util.List.of(new FieldRule("count", "数量", FieldTypeEnum.NUM_INT.getType(), null)), pojo)
        );

        assertEquals("数量格式不合法", ex.getMessage());
    }

    @Test
    void should_apply_default_text_length_rule_from_field_type() {
        DemoPojo pojo = new DemoPojo();
        pojo.setName("x".repeat(201));

        BizException ex = assertThrows(
            BizException.class,
            () -> validator.validate(java.util.List.of(new FieldRule("name", "名称", FieldTypeEnum.TEXT.getType(), null)), pojo)
        );

        assertEquals("名称长度不能超过200", ex.getMessage());
    }

    @Test
    void should_allow_blank_text_value() {
        DemoPojo pojo = new DemoPojo();
        pojo.setCount("   ");

        assertDoesNotThrow(() -> validator.validate(List.of(new FieldRule("count", "数量", FieldTypeEnum.NUM_INT.getType(), null)), pojo));
    }

    @Test
    void should_reject_required_blank_text_on_submit_mode() {
        DemoPojo pojo = new DemoPojo();
        pojo.setName("   ");

        BizException ex = assertThrows(
            BizException.class,
            () -> validator.validate(List.of(new FieldRule("name", "名称", FieldTypeEnum.TEXT.getType(), 5, 1)), pojo, FieldValidateModeEnum.SUBMIT)
        );

        assertEquals("名称不能为空", ex.getMessage());
    }

    @Test
    void should_allow_required_blank_text_on_draft_mode() {
        DemoPojo pojo = new DemoPojo();
        pojo.setName("   ");

        assertDoesNotThrow(() -> validator.validate(List.of(new FieldRule("name", "名称", FieldTypeEnum.TEXT.getType(), 5, 1)), pojo, FieldValidateModeEnum.DRAFT));
    }

    @Test
    void should_reject_required_blank_text_in_started_nested_list_on_submit_mode() {
        DemoContext context = new DemoContext();
        DemoItem item = new DemoItem();
        item.setContactName("   ");
        item.setMobile("13800000000");
        context.setContacts(List.of(item));

        BizException ex = assertThrows(
            BizException.class,
            () -> validator.validate(List.of(new FieldRule("contacts.contactName", "联系人姓名", FieldTypeEnum.TEXT.getType(), 64, 1)), context, FieldValidateModeEnum.SUBMIT)
        );

        assertEquals("联系人姓名不能为空", ex.getMessage());
    }

    @Test
    void should_allow_empty_required_nested_list_on_submit_mode() {
        DemoContext context = new DemoContext();
        context.setContacts(List.of());

        assertDoesNotThrow(
            () -> validator.validate(List.of(new FieldRule("contacts.contactName", "联系人姓名", FieldTypeEnum.TEXT.getType(), 64, 1)), context, FieldValidateModeEnum.SUBMIT)
        );
    }

    static class DemoPojo {
        private String name;
        private String count;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }
    }

    static class DemoContext {
        private List<DemoItem> contacts;

        public List<DemoItem> getContacts() {
            return contacts;
        }

        public void setContacts(List<DemoItem> contacts) {
            this.contacts = contacts;
        }
    }

    static class DemoItem {
        private String contactName;
        private String mobile;

        public String getContactName() {
            return contactName;
        }

        public void setContactName(String contactName) {
            this.contactName = contactName;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }
    }
}
