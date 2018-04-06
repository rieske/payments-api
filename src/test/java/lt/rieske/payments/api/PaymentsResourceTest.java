package lt.rieske.payments.api;

import lt.rieske.payments.domain.PaymentsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PaymentsResource.class)
public class PaymentsResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentsRepository paymentsRepository;

    @Test
    public void returnsEmptyListGivenNoPaymentsPresent() throws Exception {
        mockMvc.perform(get("/api/v1/payments")).andDo(print())
          .andExpect(status().isOk())
          .andExpect(content().json("[]"));
    }
}
