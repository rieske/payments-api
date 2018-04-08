package lt.rieske.payments.api;

import lt.rieske.payments.domain.PaymentsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.emptyArray;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PaymentsResourceTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Autowired
    private PaymentsRepository paymentsRepository;

    @Test
    public void returnsEmptyListGivenNoPaymentsPresent() throws Exception {
        mockMvc.perform(get("/api/v1/payments")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.payments", empty()))
                .andExpect(jsonPath("$._links.self.href", equalTo("http://localhost/api/v1/payments")))
                .andExpect(jsonPath("$._links.profile.href", equalTo("http://localhost/api/v1/profile/payments")));
    }
}
