package com.tcl.dias.products.consumer;

import static org.junit.Assert.assertNotNull;

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

import com.tcl.dias.productcatelog.entity.repository.GscCountrySpecificDocumentListRepository;
import com.tcl.dias.products.gsc.consumer.v1.GscCountrySpecificDocumentListListener;
import com.tcl.dias.products.util.ObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This class contains test cases for CountrySpecificDocumentListener.
 * 
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestGscCountrySpecificDocumentListListener {

	@MockBean
	GscCountrySpecificDocumentListRepository gscCountrySpecificDocumentListRepository;

	@Autowired
	GscCountrySpecificDocumentListListener gscCountrySpecificDocumentListListener;

	@Autowired
	ObjectCreator objectCreator;

	@Before
	public void init() throws TclCommonException {
		Mockito.when(gscCountrySpecificDocumentListRepository.findByDocumentNameAndProductNameAndCountryName(
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
				.thenReturn(objectCreator.getDocumentUID());
	}

	/**
	 * Test country specific document listener.
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testGscCountryDocumentListener() throws TclCommonException {
		String request = "{\"gscCountrySpecificDocumentBean\":{\"uID\":null,\"productName\":\"Domestic Voice\",\"countryCode\":null,\"category\":null,\"countryName\":\"United States of America\",\"documentName\":\"USA E_DomesticVoice_porting_form\"}}";
		String documentID = gscCountrySpecificDocumentListListener.processCountrySpecificDocument(request);
		assertNotNull(documentID);
	}
}