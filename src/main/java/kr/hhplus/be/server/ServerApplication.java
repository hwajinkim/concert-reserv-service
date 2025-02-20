package kr.hhplus.be.server;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDateTime;
import java.util.TimeZone;


@SpringBootApplication
@EnableScheduling
public class ServerApplication {

	public static void main(String[] args) {

		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
		SpringApplication.run(ServerApplication.class, args);
	}

}
