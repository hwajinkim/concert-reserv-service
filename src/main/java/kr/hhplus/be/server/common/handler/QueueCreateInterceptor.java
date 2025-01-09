package kr.hhplus.be.server.common.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.hhplus.be.server.application.queue.QueueFacade;
import kr.hhplus.be.server.common.response.ApiResponse;
import kr.hhplus.be.server.common.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class QueueCreateInterceptor implements HandlerInterceptor {
    private final QueueFacade queueFacade;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        log.info("==================== QueueCreateInterceptor START ====================");
        log.info(" Request URI \t: " + request.getRequestURI());

        String token = request.getHeader("Queue-Token");

        //헤더에 토큰이 비어있지 않고 검증도 통과하면 토큰 발급 중지
        if (token != null && !token.isEmpty() && queueFacade.isQueueCreatedValidToken(token)) {

            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/plain; charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_CONFLICT); // 401 상태 반환
            /*response.getWriter().write("헤더에 기존 사용자 대기열 토큰이 있습니다.");*/
            // ApiResponse 객체 생성
            ApiResponse<Object> apiResponse = ApiResponse.failure("헤더에 기존 사용자의 대기열 토큰이 포함되어 있습니다.", HttpServletResponse.SC_CONFLICT);

            // JSON 변환 및 응답 쓰기
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(apiResponse);
            response.getWriter().write(jsonResponse);
            return false; // 요청 중단
        }

        return true;
    }
}
