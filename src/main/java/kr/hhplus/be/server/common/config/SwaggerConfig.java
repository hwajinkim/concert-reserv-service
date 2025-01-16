package kr.hhplus.be.server.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("콘서트 예약 서비스 API 명세서")
                .version("v1.0.0")
                .description("- 유저 토큰 발급 API\n" +
                        "- 예약 가능 날짜 / 좌석 API\n" +
                        "- 좌석 예약 요청 API\n" +
                        "- 잔액 충전 / 조회 API\n" +
                        "- 결제 API");

        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}
