package com.tcl.dias.controller;

import java.util.List;
import java.util.Optional;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.entity.Student;
import com.tcl.dias.rabbitmq.MessageSender;
import com.tcl.dias.repository.RedisStudentRepository;
import com.tcl.dias.repository.StudentRepository;

import io.swagger.annotations.ApiOperation;

@RestController
public class StudentOperationController {
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private RedisStudentRepository redisStudentRepository;
	
	@Autowired
	MessageSender messageSender;

	@GetMapping("/students")
	public List<Student> retrieveAllStudents() {

		return studentRepository.findAll();
	}

	@GetMapping("/students/{id}")
	@ApiOperation(value = "Find student by id", notes = "Also returns a link to retrieve all students with rel - all-students")
	public Optional<Student> retrieveStudent(@RequestHeader HttpHeaders headers,@PathVariable long id) {
		 String keyPrincipal=(String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		 
		//System.out.println(keyPrincipal.getName());
		Optional<Student> student = studentRepository.findById(id);
		messageSender.sendMessage(student);
		redisStudentRepository.save(student.get());
		Student redisStudent= redisStudentRepository.find(id);
		System.out.println(redisStudent.getName()+"--"+redisStudent.getPassportNumber());
		return student;
	}

	@DeleteMapping("/students/{id}")
	public void deleteStudent(@PathVariable long id) {
		studentRepository.deleteById(id);
	}

	@PutMapping("/students/")
	public ResponseEntity<Object> updateStudent(@RequestBody Student student) {

			studentRepository.save(student);

		return ResponseEntity.ok(student);
	}
}
