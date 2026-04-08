package com.dpu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.dpu")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}