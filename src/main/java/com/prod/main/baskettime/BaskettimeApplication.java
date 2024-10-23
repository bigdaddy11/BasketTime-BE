package com.prod.main.baskettime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling  // 스케줄링 기능 활성화
@EnableJpaAuditing  // JPA Auditing 활성화
public class BaskettimeApplication {
	public static void main(String[] args) {
		SpringApplication.run(BaskettimeApplication.class, args);
	}

}
