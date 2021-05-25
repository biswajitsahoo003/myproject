package com.tcl.dias.oms.gsc.service;

import static org.junit.Assert.assertNotNull;

import java.util.Optional;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.gsc.beans.GscQuoteToLeBean;
import com.tcl.dias.oms.gsc.service.v1.GscQuoteService;
import com.tcl.dias.oms.utils.GscObjectCreator;
import com.tcl.dias.oms.utils.ObjectCreator;

/**
 * THis class contains all the test cases related to {@link GscQuoteService}
 * 
 * @author VISHESH AWASTHI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GscQuoteServiceTest {

	@Autowired
	GscQuoteService gscQuoteService;

	@Autowired
	ObjectCreator omsObjectCreator;

	@Autowired
	GscObjectCreator gscObjectCreator;

	@MockBean
	QuoteToLeRepository quoteToLeRepository;

	@Before
	public void init() {
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.ofNullable(gscObjectCreator.getQuoteToLe()));
		Mockito.when(quoteToLeRepository.save(Mockito.any())).thenReturn(new QuoteToLe());
	}

	/**
	 * Positive test case for
	 * {@link GscQuoteService#updateStageStatus(Integer, String)}}
	 */
	@Test
	public void updateStageStatusTest() {
		GscQuoteToLeBean response = gscQuoteService.updateStageStatus(1, "Get_Quote");
		assertNotNull(response);
	}

	/**
	 * Negative test case for
	 * {@link GscQuoteService#updateStageStatus(Integer, String)}} Check for null
	 * input
	 */
	@Test(expected = NullPointerException.class)
	public void updateStageStatusForNullTest() {
		gscQuoteService.updateStageStatus(null, null);
	}

	/**
	 * Positive test case for
	 * {@link GscQuoteService#updateStageStatus(Integer, String)}} checks for null
	 * QuoteToLe
	 */
	@Test(expected = NullPointerException.class)
	public void updateStageStatusForNullQuoteToLeTest() {
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(null);
		gscQuoteService.updateStageStatus(123123, null);
	}
}
