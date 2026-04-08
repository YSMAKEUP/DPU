package com.dpu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication(scanBasePackages = "com.dpu")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}