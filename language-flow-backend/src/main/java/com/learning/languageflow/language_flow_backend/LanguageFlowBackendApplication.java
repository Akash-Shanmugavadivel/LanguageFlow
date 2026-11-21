package com.learning.languageflow.language_flow_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.learning.languageflow", "com.ilp"})
public class LanguageFlowBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(LanguageFlowBackendApplication.class, args);
	}

}
