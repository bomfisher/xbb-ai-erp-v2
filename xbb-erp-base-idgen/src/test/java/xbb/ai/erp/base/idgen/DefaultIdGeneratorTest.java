package xbb.ai.erp.base.idgen;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultIdGeneratorTest {

    @Test
    void should_generate_positive_id() {
        IdGenerator generator = new DefaultIdGenerator();
        Long id = generator.nextId();

        assertNotNull(id);
        assertTrue(id > 0);
    }
}
