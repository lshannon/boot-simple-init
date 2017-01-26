package com.lukeshannon.boot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lukeshannon.boot.CommandLineRunnerApplication;
import com.lukeshannon.boot.model.Customer;

@RestController
@RequestMapping("/v1")
public class CustomerController {
	
	private static final Logger log = LoggerFactory.getLogger(CustomerController.class);
	
	@RequestMapping("/customer/{name}")
	public ResponseEntity<Customer> customer(@PathVariable String name) {
		log.info("Searching for : " + name);
		Customer returnValue = CommandLineRunnerApplication.customers.get(name);
		if (returnValue  != null) {
			log.info("Found a reference for name: " + name + " = " + returnValue.toString());
			return ResponseEntity.ok(returnValue);
		}
		log.info("Nothing found for name: " + name);
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

}
