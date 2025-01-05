package kr.hhplus.be.server.interfaces.api.queue;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.common.response.ApiResponse;
import kr.hhplus.be.server.common.response.ResponseCode;
import kr.hhplus.be.server.domain.queue.QueueStatus;
import kr.hhplus.be.server.interfaces.api.dto.QueueResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
@Tag(name = "유저 토큰 발급 API", description = "유저의 토큰을 발급하는 api 입니다.")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class QueueController {

    //1. 유저 대기열 토큰 발급 API
    @Operation(summary = "유저 대기열 토큰 발급")
    @PostMapping("/queue/token")
    public ApiResponse<QueueResponse> createQueueToken(){
        QueueResponse queueResponse = new QueueResponse(
                12345L, 12345L, QueueStatus.WAIT, LocalDateTime.of(2025, 1, 1, 12, 0, 0),
                LocalDateTime.of(2025, 1, 1, 12, 30, 0));
        return ApiResponse.success(ResponseCode.TOKEN_CREATE_SUCCESS.getMessage(), queueResponse);
    }
}
