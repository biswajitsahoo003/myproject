package com.tcl.dias.repository;

import java.util.Optional;

import com.tcl.dias.entity.Student;

public interface RedisStudentRepository {
	public void save(Student student);
	public Student find(Long id);
}
