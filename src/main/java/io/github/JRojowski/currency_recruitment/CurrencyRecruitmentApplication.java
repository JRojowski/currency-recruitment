package io.github.JRojowski.currency_recruitment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CurrencyRecruitmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(CurrencyRecruitmentApplication.class, args);
	}
}
