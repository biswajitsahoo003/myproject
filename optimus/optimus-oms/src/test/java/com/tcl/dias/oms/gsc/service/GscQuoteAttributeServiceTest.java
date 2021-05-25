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

import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.gsc.beans.GscContactAttributeInfo;
import com.tcl.dias.oms.gsc.service.v1.GscQuoteAttributeService;
import com.tcl.dias.oms.utils.GscObjectCreator;
import com.tcl.dias.oms.utils.ObjectCreator;

/**
 * THis class contains all the test cases related to
 * {@link GscQuoteAttributeService}
 * 
 * @author VISHESH AWASTHI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GscQuoteAttributeServiceTest {

	@Autowired
	GscQuoteAttributeService gscQuoteAttributeService;

	@Autowired
	ObjectCreator omsObjectCreator;

	@Autowired
	GscObjectCreator gscObjectCreator;

	@MockBean
	QuoteRepository quoteRepository;

	@MockBean
	QuoteToLeRepository quoteToLeRepository;

	@MockBean
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	@Before
	public void init() {
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(gscObjectCreator.createOptionalQuote().get());
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(gscObjectCreator.getQuoteToLe()));
	}

	/**
	 * Positive test case for
	 * {@link GscQuoteAttributeService#getContractInfo(Integer, Integer)}
	 */
	@Test
	public void getContractInfoTest() {
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLe(Mockito.any()))
				.thenReturn(gscObjectCreator.getQuoteLeAttributeValueListForContactInfo());
		GscContactAttributeInfo response = gscQuoteAttributeService.getContractInfo(1, 1);
		assertNotNull(response);
	}

	/**
	 * Positive test case for
	 * {@link GscQuoteAttributeService#getContractInfo(Integer, Integer)}
	 */
	@Test(expected = NullPointerException.class)
	public void getContractInfoTestForNull() {
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLe(Mockito.any())).thenReturn(null);
		GscContactAttributeInfo response = gscQuoteAttributeService.getContractInfo(1, 1);
	}
}
