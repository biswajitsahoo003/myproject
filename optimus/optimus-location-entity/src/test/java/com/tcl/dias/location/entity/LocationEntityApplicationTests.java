package com.tcl.dias.location.entity;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.location.entity.repository.MstPincodeRespository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LocationEntityApplicationTests {
	
	@Autowired
	MstPincodeRespository mstPincodeRespository;

	@Test
	@Transactional
	public void contextLoads() {
		List<Map<String, Object>> response=mstPincodeRespository.findByPincode("60002","INDIA");
		System.out.println(response);
	}

}
