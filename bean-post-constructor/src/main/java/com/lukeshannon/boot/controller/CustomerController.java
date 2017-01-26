package com.lukeshannon.boot.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lukeshannon.boot.model.Customer;

@RestController
@RequestMapping("/v1")
public class CustomerController {
	
	private static final Logger log = LoggerFactory.getLogger(CustomerController.class);
	
	private Map<String,Customer> customers = new HashMap<String,Customer>();
	
	@PostConstruct
	public void initData() {
		log.info("Inserting two customers. Current Map Size: " +  customers.size());
		Customer one = new Customer("gopika2", 23, new Date());
		customers.put(one.getName(), one);
		Customer two = new Customer("george2", 23, new Date());
		customers.put(two.getName(), two);
		log.info("Done inserting customers. Current Map Size: " +  customers.size());
	}
	
	
	@RequestMapping("/customer/{name}")
	public ResponseEntity<Customer> customer(@PathVariable String name) {
		log.info("Searching for : " + name);
		Customer returnValue = customers.get(name);
		if (returnValue  != null) {
			log.info("Found a reference for name: " + name + " = " + returnValue.toString());
			return ResponseEntity.ok(returnValue);
		}
		log.info("Nothing found for name: " + name);
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

}
