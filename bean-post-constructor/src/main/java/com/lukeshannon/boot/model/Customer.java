package com.lukeshannon.boot.model;

import java.util.Date;

public class Customer {
	
	private String name;
	private int age;
	private Date dob;
	

	public Customer(String name, int age, Date dob) {
		super();
		this.name = name;
		this.age = age;
		this.dob = dob;
	}
	
	public Customer() {}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}
	
	

}
