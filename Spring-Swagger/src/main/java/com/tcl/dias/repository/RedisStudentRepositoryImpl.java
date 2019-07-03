package com.tcl.dias.repository;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.tcl.dias.entity.Student;
@Repository
public class RedisStudentRepositoryImpl implements RedisStudentRepository {

	private static final String KEY = "Student";
	 
	private RedisTemplate<String, Object> redisTemplate;
	private HashOperations<String, Long, Student> hashOperations;
 
	@Autowired
	public RedisStudentRepositoryImpl(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
 
	@PostConstruct
	private void init() {
		hashOperations = redisTemplate.opsForHash();
	}
 
	@Override
	public void save(Student student) {
		hashOperations.put(KEY, student.getId(), student);
	}
 
	@Override
	public Student find(Long id) {
		return hashOperations.get(KEY, id);
	}
 

}
