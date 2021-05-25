package com.tcl.dias.wfe.validations;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Range;

public class Student {
	
	@NotNull
	private int id;
	
	@Range(min=0, max=100)
	private int marks;
	
	 @NotNull(message = "First name is compulsory")
	 @NotBlank(message = "First name is compulsory")
	 @Pattern(regexp = "[a-z-A-Z]*", message = "First name has invalid characters")
	private String name;
	
	@Range(min=5, max=8)
	private int age;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMarks() {
		return marks;
	}

	public void setMarks(int marks) {
		this.marks = marks;
	}

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


	

}
