package lt.rieske.payments.functional;


import com.fasterxml.jackson.databind.ObjectMapper;
import lt.rieske.payments.domain.Payment;
import lt.rieske.payments.domain.PaymentsRepository;
import org.apache.commons.codec.Charsets;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.ManualRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public abstract class SpringBootSteps {

    private RestDocumentationResultHandler documentationHandler;
    private ManualRestDocumentation restDocumentation;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    protected PaymentsRepository paymentsRepository;

    private MockMvc mockMvc;
    private ResultActions resultActions;

    public void setUp() {
        documentationHandler = document("{method-name}",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()));

        restDocumentation = new ManualRestDocumentation("build/generated-snippets");

        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(this.documentationHandler)
                .build();

        restDocumentation.beforeTest(FunctionalTest.class, "setUp");
    }

    public void tearDown() {
        restDocumentation.afterTest();
    }

    void paymentWithIdExists(String fixturePath, String paymentId) throws IOException {
        Payment payment = readFixture(fixturePath);
        payment.setId(paymentId);
        paymentsRepository.save(payment);
    }

    void get(String path, String contentType) throws Exception {
        resultActions = mockMvc.perform(MockMvcRequestBuilders.get(path).accept(contentType));
    }

    void post(String resourcePath, String resourceFixturePath) throws Exception {
        String content = IOUtils.resourceToString(resourceFixturePath, Charsets.UTF_8);
        resultActions = mockMvc.perform(MockMvcRequestBuilders.post(resourcePath).content(content));
    }

    void postAccepting(String resourcePath, String fixturePath, String contentType) throws Exception {
        String content = IOUtils.resourceToString(fixturePath, Charsets.UTF_8);
        resultActions = mockMvc.perform(MockMvcRequestBuilders.post(resourcePath).content(content).accept(contentType));
    }

    void assertHttpStatus(int statusCode) throws Exception {
        resultActions.andExpect(status().is(statusCode));
    }

    void assertHeader(String header, String value) throws Exception {
        resultActions.andExpect(MockMvcResultMatchers.header().string(header, value));
    }

    void assertLocationHeaderPointsToNewResource() throws Exception {
        resultActions.andExpect(header().exists("Location"))
                .andDo(result -> {
                    String location = result.getResponse().getHeader("Location");
                    String resourceId = location.substring(location.lastIndexOf('/') + 1);
                    mockMvc.perform(MockMvcRequestBuilders.get(location)).andExpect(status().is(200))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.id", equalTo(resourceId)));
                });
    }

    void assertResponseBodyIsEmpty() throws Exception {
        resultActions.andExpect(MockMvcResultMatchers.content().string(""));
    }

    void assertResponseBodyMatchesFixture(String fixturePath) throws Exception {
        String expectedContent = IOUtils.resourceToString(fixturePath, Charsets.UTF_8);
        resultActions.andExpect(MockMvcResultMatchers.content().json(expectedContent));
    }

    private Payment readFixture(String fixturePath) throws IOException {
        return mapper.readValue(getClass().getResourceAsStream(fixturePath), Payment.class);
    }
}
