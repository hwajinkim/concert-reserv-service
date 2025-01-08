package kr.hhplus.be.server.interfaces.api.queue;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.application.dto.queue.QueueTokenParam;
import kr.hhplus.be.server.application.dto.queue.QueueTokenResult;
import kr.hhplus.be.server.application.queue.QueueFacade;
import kr.hhplus.be.server.common.response.ApiResponse;
import kr.hhplus.be.server.common.response.ResponseCode;
import kr.hhplus.be.server.interfaces.api.dto.queue.QueueTokenRequest;
import kr.hhplus.be.server.interfaces.api.dto.queue.QueueTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "유저 토큰 발급 API", description = "유저의 토큰을 발급하는 api 입니다.")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class QueueController {

    private final QueueFacade queueFacade;

    //1. 유저 대기열 토큰 발급 API
    @Operation(summary = "유저 대기열 토큰 발급")
    @PostMapping("/queues")
    public ApiResponse<QueueTokenResponse> createQueueToken(@RequestBody QueueTokenRequest queueTokenRequest){

        QueueTokenParam queueTokenParam = QueueTokenParam.from(queueTokenRequest);
        return ApiResponse.success(ResponseCode.TOKEN_CREATE_SUCCESS.getMessage(), QueueTokenResponse.from(queueFacade.createQueueToken(queueTokenParam)));
    }
}
