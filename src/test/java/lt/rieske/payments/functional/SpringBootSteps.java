package lt.rieske.payments.functional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lt.rieske.payments.domain.Payment;
import lt.rieske.payments.domain.PaymentsRepository;
import org.apache.commons.codec.Charsets;
import org.apache.commons.io.IOUtils;
import org.skyscreamer.jsonassert.JSONAssert;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public abstract class SpringBootSteps {

    private static final String FIXTURES_PATH = "/fixtures/functional/";

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
        paymentsRepository.deleteAll();
    }

    void paymentExists(String fixture) throws IOException {
        paymentsRepository.save(readPaymentFromFixture(fixture));
    }

    void paymentsListExists(String fixture) throws IOException {
        List<Payment> payments = readPaymentsListFromFixture(fixture);
        paymentsRepository.saveAll(payments);
    }

    void paymentWithIdExists(String fixture, String paymentId) throws IOException {
        Payment payment = readPaymentFromFixture(fixture);
        payment.setId(paymentId);
        paymentsRepository.save(payment);
    }

    void assertNoPaymentsExist() {
        assertThat(paymentsRepository.findAll()).isEmpty();
    }

    void assertPaymentDoesNotExist(String paymentId) {
        assertThat(paymentsRepository.findById(paymentId)).isEmpty();
    }

    void get(String path, String contentType) throws Exception {
        resultActions = mockMvc.perform(MockMvcRequestBuilders.get(path).accept(contentType));
    }

    void post(String resourcePath, String fixture) throws Exception {
        String content = readFixtureAsString(fixture);
        resultActions = mockMvc.perform(MockMvcRequestBuilders.post(resourcePath).content(content));
    }

    void postAccepting(String resourcePath, String fixture, String contentType) throws Exception {
        String content = readFixtureAsString(fixture);
        resultActions = mockMvc.perform(MockMvcRequestBuilders.post(resourcePath).content(content).accept(contentType));
    }

    void delete(String resourcePath) throws Exception {
        resultActions = mockMvc.perform(MockMvcRequestBuilders.delete(resourcePath));
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(resourceId));
          });
    }

    void assertResponseBodyIsEmpty() throws Exception {
        resultActions.andExpect(MockMvcResultMatchers.content().string(""));
    }

    void assertResponseBodyContainsAnEmptyListOfPayments() throws Exception {
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$._embedded.payments").isArray())
          .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.payments").isEmpty());
    }

    void assertResponseBodyContainsPayments(String fixture) throws Exception {
        String expectedContent = readFixtureAsString(fixture);
        String returnedContent = resultActions
          .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.payments").isArray())
          .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.payments").isNotEmpty())
          .andReturn().getResponse().getContentAsString();

        String returnedPayments = JsonPath.read(returnedContent, "$._embedded.payments").toString();
        JSONAssert.assertEquals(expectedContent, returnedPayments, false);
    }

    void assertResponseBodyMatchesFixture(String fixture) throws Exception {
        String expectedContent = readFixtureAsString(fixture);
        resultActions.andExpect(MockMvcResultMatchers.content().json(expectedContent));
    }

    private Payment readPaymentFromFixture(String fixture) throws IOException {
        return mapper.readValue(getClass().getResourceAsStream(fixturePath(fixture)), Payment.class);
    }

    private List<Payment> readPaymentsListFromFixture(String fixture) throws IOException {
        return mapper.readValue(getClass().getResourceAsStream(fixturePath(fixture)), new TypeReference<List<Payment>>() {
        });
    }

    private String readFixtureAsString(String fixture) throws IOException {
        return IOUtils.resourceToString(fixturePath(fixture), Charsets.UTF_8);
    }

    private String fixturePath(String fixture) {
        return FIXTURES_PATH + fixture;
    }

}
