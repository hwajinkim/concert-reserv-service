package kr.hhplus.be.server.integration_test.inter;

import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.interfaces.api.dto.queue.QueueTokenRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class QueueIntegrationControllerTest extends BaseIntegrationTest{

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private UserSetUp userSetUp;

    @BeforeEach
    public void setup() {
        // MockMvc 초기화
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void 사용자_대기열_토큰_생성() throws Exception {
        //given
        User user = userSetUp.saveUser("김화진", BigDecimal.valueOf(50000.00));

        QueueTokenRequest queueTokenRequest = new QueueTokenRequest(
                user.getId());

        //when
        ResultActions resultActions = mvc.perform(post("/api/v1/queues")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(queueTokenRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());


        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("success", Matchers.is("true")))
                .andExpect(jsonPath("message", Matchers.is("유저 대기열 토큰 발급에 성공했습니다.")))
                .andExpect(jsonPath("data", Matchers.is(notNullValue())));
    }
}
