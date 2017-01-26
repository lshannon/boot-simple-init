package com.lukeshannon.boot;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.lukeshannon.boot.model.Customer;

@SpringBootApplication
public class CommandLineRunnerApplication {
	
	private static final Logger log = LoggerFactory
			.getLogger(CommandLineRunnerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(CommandLineRunnerApplication.class, args);
	}
	
	public static Map<String,Customer> customers = new HashMap<String,Customer>();
	
	@Bean
	public CommandLineRunner demo() {
		return (args) -> {
			log.info("Inserting two customers. Current Map Size: " +  customers.size());
			Customer one = new Customer("gopika", 23, new Date());
			customers.put(one.getName(), one);
			Customer two = new Customer("george", 23, new Date());
			customers.put(two.getName(), two);
			log.info("Done inserting customers. Current Map Size: " +  customers.size());
		};
	}

}
