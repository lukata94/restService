package com.rest.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({ "com.rest.app" })
public class RestServiceApplication {

	public static void main(final String[] args) {
		SpringApplication.run(RestServiceApplication.class, args);
	}

}
