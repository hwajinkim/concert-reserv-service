package kr.hhplus.be.server.interfaces.api.queue;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import kr.hhplus.be.server.application.dto.queue.QueueTokenParam;
import kr.hhplus.be.server.application.dto.queue.QueueTokenResult;
import kr.hhplus.be.server.application.queue.QueueFacade;
import kr.hhplus.be.server.common.response.ApiResponse;
import kr.hhplus.be.server.common.response.ResponseCode;
import kr.hhplus.be.server.interfaces.api.dto.queue.QueueTokenRequest;
import kr.hhplus.be.server.interfaces.api.dto.queue.QueueTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "유저 토큰 발급 API", description = "유저의 토큰을 발급하는 api 입니다.")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class QueueController {

    private final QueueFacade queueFacade;

    //1. 유저 대기열 토큰 발급 API
    @Operation(summary = "유저 대기열 토큰 발급")
    @PostMapping("/queues")
    public ApiResponse<QueueTokenResponse> createQueueToken(@RequestBody QueueTokenRequest queueTokenRequest, HttpServletResponse response){

        QueueTokenParam queueTokenParam = QueueTokenParam.from(queueTokenRequest);
        QueueTokenResponse queueTokenResponse = QueueTokenResponse.from(queueFacade.createQueueToken(queueTokenParam));

        response.setHeader("Queue-Token", String.valueOf(queueTokenResponse.userId()));
        response.setHeader("Queue-Token-Queue-Id", String.valueOf(queueTokenResponse.queueId()));

        return ApiResponse.success(ResponseCode.TOKEN_CREATE_SUCCESS.getMessage(), queueTokenResponse);
    }
}
