package kr.hhplus.be.server.common.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.hhplus.be.server.application.queue.QueueFacade;
import kr.hhplus.be.server.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class QueueInterceptor implements HandlerInterceptor {
    private final QueueFacade queueFacade;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        log.info("==================== QueueInterceptor START ====================");
        log.info(" Request URI \t: " + request.getRequestURI());

        String token = request.getHeader("Queue-Token");

        //헤더에 토큰이 비어있거나 검증에 통과하지 못하면 대기열 진입 불가
        if (token == null || token.isEmpty() || !queueFacade.isQueueValidToken(token)) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/plain; charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 상태 반환
            // ApiResponse 객체 생성
            ApiResponse<Object> apiResponse = ApiResponse.failure("사용자 대기열 토큰 검증에 실패하였습니다.", HttpServletResponse.SC_UNAUTHORIZED);

            // JSON 변환 및 응답 쓰기
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(apiResponse);
            response.getWriter().write(jsonResponse);
            return false; // 요청 중단

        }

        return true;
    }
}
