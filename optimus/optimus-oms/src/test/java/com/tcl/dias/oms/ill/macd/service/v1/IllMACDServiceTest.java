package com.tcl.dias.oms.ill.macd.service.v1;

import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Optional;

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

import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.macd.beans.MacdQuoteResponse;
import com.tcl.dias.oms.utils.ObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IllMACDServiceTest {
	
	@Autowired
	IllMACDService illMacdService;
	
	@Autowired
	ObjectCreator objectCreator;
	
	
	
	@MockBean
	QuoteToLeRepository quoteToLeRepository;
	
	@MockBean
	UserRepository userRepository;

	@MockBean
	OrderToLeRepository orderToLeRepository;

	@MockBean
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;
	
	@MockBean
	ProductSolutionRepository productSolutionRepository;
	
	@MockBean
	IllSiteRepository illSiteRepository;
	
	@MockBean
	QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;
	
	@MockBean 
	QuoteRepository quoteRepository;
	
	
	//positive case
	@Test
	public void handleMacdQuoteTest1() throws TclCommonException {
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt())).thenReturn((objectCreator.craeteUser()));
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLeAndMstProductFamily(Mockito.any(), Mockito.any()))
		.thenReturn(objectCreator.getQuoteToLeFamily());
		Mockito.when(productSolutionRepository.findByQuoteToLeProductFamily(Mockito.any()))
		.thenReturn(objectCreator.createProductSolutions());

		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
		.thenReturn(objectCreator.getQuote());
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLeAndMstProductFamily(Mockito.any(), Mockito.any()))
		.thenReturn(objectCreator.getQuoteToLeFamily());
		Mockito.when(quoteToLeProductFamilyRepository.save(Mockito.any()))
		.thenReturn(objectCreator.getQuoteToLeFamily());
		Mockito.when( quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(objectCreator.getOptionalQuoteToLe());
		Mockito.when(productSolutionRepository.findByQuoteToLeProductFamilyAndMstProductOffering(Mockito.any(),
		Mockito.any())).thenReturn(null);
		Mockito.when(productSolutionRepository.save(Mockito.any())).thenReturn(objectCreator.getSolution());
		Mockito.when(illSiteRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getIllsites()));
		Mockito.when(illSiteRepository.save(Mockito.any())).thenReturn(objectCreator.getQuoteIllSite());
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute_Name(Mockito.any(),
		Mockito.anyString())).thenReturn(new ArrayList<>());
		Mockito.when(illSiteRepository.findByProductSolutionAndStatus(Mockito.any(),
				Mockito.anyByte())).thenReturn(objectCreator.getListOfQouteIllSites());


		MacdQuoteResponse response=illMacdService.handleMacdRequestToCreateQuote(objectCreator.getMacdQuoteRequest(),false);
		assertTrue(response != null&&response.getQuoteResponse().getQuoteId()!=null);
	}
	
	
	//Negative case
	@Test(expected=Exception.class)
	public void handleMacdQuoteTest2() throws TclCommonException {
		MacdQuoteResponse response=illMacdService.handleMacdRequestToCreateQuote(objectCreator.getMacdQuoteRequestQuoteRequestNull(),false);
	}
	
	@Test(expected=Exception.class)
	public void handleMacdQuoteTest3() throws TclCommonException {
		MacdQuoteResponse response=illMacdService.handleMacdRequestToCreateQuote(objectCreator.getMacdQuoteRequestRequestTypeNull(),false);
	}
	
	@Test(expected=Exception.class)
	public void handleMacdQuoteTest4() throws TclCommonException {
		MacdQuoteResponse response=illMacdService.handleMacdRequestToCreateQuote(null,false);
	}

	
	//positive case
	@Test
	public void createMacdQuoteTest1() throws TclCommonException, ParseException {
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt())).thenReturn((objectCreator.craeteUser()));
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLeAndMstProductFamily(Mockito.any(), Mockito.any()))
		.thenReturn(objectCreator.getQuoteToLeFamily());
		Mockito.when(productSolutionRepository.findByQuoteToLeProductFamily(Mockito.any()))
		.thenReturn(objectCreator.createProductSolutions());

		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
		.thenReturn(objectCreator.getQuote());
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLeAndMstProductFamily(Mockito.any(), Mockito.any()))
		.thenReturn(objectCreator.getQuoteToLeFamily());
		Mockito.when(quoteToLeProductFamilyRepository.save(Mockito.any()))
		.thenReturn(objectCreator.getQuoteToLeFamily());
		Mockito.when( quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(objectCreator.getOptionalQuoteToLe());
		Mockito.when(productSolutionRepository.findByQuoteToLeProductFamilyAndMstProductOffering(Mockito.any(),
		Mockito.any())).thenReturn(null);
		Mockito.when(productSolutionRepository.save(Mockito.any())).thenReturn(objectCreator.getSolution());
		Mockito.when(illSiteRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getIllsites()));
		Mockito.when(illSiteRepository.save(Mockito.any())).thenReturn(objectCreator.getQuoteIllSite());
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute_Name(Mockito.any(),
		Mockito.anyString())).thenReturn(new ArrayList<>());
		Mockito.when(illSiteRepository.findByProductSolutionAndStatus(Mockito.any(),
				Mockito.anyByte())).thenReturn(objectCreator.getListOfQouteIllSites());

		MacdQuoteResponse response=illMacdService.createMacdQuote(objectCreator.getMacdQuoteRequest(),false);
		assertTrue(response != null&&response.getQuoteResponse().getQuoteId()!=null);
	}
	
	//Negative case
	@Test
	public void createMacdQuoteTest2() throws TclCommonException,ParseException {

		Mockito.when(userRepository.findByUsernameAndStatus(null, 1)).thenReturn((null));
		MacdQuoteResponse response=illMacdService.createMacdQuote(objectCreator.getMacdQuoteRequestQuoteRequestNull(),false);
		assertTrue(response==null);
	}
		
		
		
		
	
}
