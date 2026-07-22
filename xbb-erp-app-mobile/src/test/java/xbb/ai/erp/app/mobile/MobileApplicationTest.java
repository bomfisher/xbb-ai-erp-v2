package xbb.ai.erp.app.mobile;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MobileApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void should_expose_mobile_user_info_endpoint() throws Exception {
        mockMvc.perform(get("/erp/v1/user/info"))
            .andExpect(status().isOk());
    }
}
