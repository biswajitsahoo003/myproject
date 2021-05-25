package com.tcl.dias.oms.gsc.common;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.utils.GscObjectCreator;
import com.tcl.dias.oms.utils.ObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ContextConfiguration
public class GscPdfHelperTest {

	@MockBean
	QuoteRepository quoteRepository;

	@MockBean
	QuoteToLeRepository quoteToLeRepository;

	@MockBean
	ObjectCreator objectCreator;

	@MockBean
	GscObjectCreator gscObjectCreator;

	@Autowired
	GscPdfHelper gscPdfHelper;

	@Before
	public void init() {

		mock(UserInformation.class);
		Authentication authentication = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.thenReturn(objectCreator.getUserInformation());
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getQuote());
		Mockito.when(quoteToLeRepository.findByQuote(Mockito.any())).thenReturn(gscObjectCreator.getQuoteToLeList());
		Mockito.when(quoteToLeRepository.findByQuote(Mockito.any())).thenReturn(gscObjectCreator.getQuoteToLeList());

	}

	@Test
	public void testGenerateGscCof() throws TclCommonException {
		gscPdfHelper.generateGscCof(1, new MockHttpServletResponse());
	}

}
