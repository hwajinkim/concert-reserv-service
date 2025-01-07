package kr.hhplus.be.server.integration_test.inter;

import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.interfaces.api.dto.user.UserBalanceRequest;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserIntegrationControllerTest extends BaseIntegrationTest{
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
    void 사용자_잔액_조회_시_포인트_반환() throws Exception {
        //given
        User user = userSetUp.saveUser("김화진", BigDecimal.valueOf(50000.00));

        //when
        ResultActions resultActions = mvc.perform(get("/api/v1/users/"+user.getId()+"/balance")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("success", Matchers.is("true")))
                .andExpect(jsonPath("message", Matchers.is("잔액 조회에 성공했습니다.")))
                .andExpect(jsonPath("data", Matchers.is(Matchers.notNullValue())));
    }

    @Test
    void 사용자_잔액_충전_시_포인트_내역_저장_후_충전된_포인트_반환() throws Exception {
        //given
        User user = userSetUp.saveUser("김화진", BigDecimal.valueOf(50000.00));
        BigDecimal amount = BigDecimal.valueOf(10000.00);

        UserBalanceRequest userBalanceRequest = new UserBalanceRequest(
                user.getId(), amount);

        //when
        ResultActions resultActions = mvc.perform(put("/api/v1/users/charge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userBalanceRequest))
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print());
        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("success", Matchers.is("true")))
                .andExpect(jsonPath("message", Matchers.is("잔액 충전에 성공했습니다.")))
                .andExpect(jsonPath("data", Matchers.is(notNullValue())));
    }
}
