package com.lukeshannon.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BeanPostConstructorApplication {

	public static void main(String[] args) {
		SpringApplication.run(BeanPostConstructorApplication.class, args);
	}
}
