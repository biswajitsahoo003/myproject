package com.tcl.dias.customer.service.v1;

import com.tcl.dias.customer.entity.entities.MstDopMatrix;
import com.tcl.dias.customer.entity.repository.MstDopMatrixRepository;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;

/**
 * This file contains Test Case for CustomerService.java class
 *
 * @author Harini Sri Reka J
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

@RunWith(MockitoJUnitRunner.Silent.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class CustomerServiceTest {

	@Mock
	MstDopMatrixRepository mstDopMatrixRepository;

	@Test
	public void testDopExcelMapping()
	{
		MstDopMatrix mstDopMatrix = new MstDopMatrix();
		Mockito.doReturn(mstDopMatrix).when(mstDopMatrixRepository).saveAndFlush(any(mstDopMatrix.getClass()));
		MstDopMatrix mstDopMatrix1 = mstDopMatrixRepository.saveAndFlush(mstDopMatrix);
		assertEquals(mstDopMatrix1,mstDopMatrix);
	}

	@Test
	public void testDopExcelMappingNull()
	{
		MstDopMatrix mstDopMatrix = new MstDopMatrix();
		Mockito.doReturn(mstDopMatrix).when(mstDopMatrixRepository).saveAndFlush(any(mstDopMatrix.getClass()));
		MstDopMatrix mstDopMatrix1 = mstDopMatrixRepository.saveAndFlush(any(mstDopMatrix.getClass()));
		assertNotEquals(mstDopMatrix1,mstDopMatrix);
	}
}