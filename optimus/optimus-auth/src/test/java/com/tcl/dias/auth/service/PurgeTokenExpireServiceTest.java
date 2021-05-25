package com.tcl.dias.auth.service;

import static org.mockito.Mockito.doNothing;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.auth.utils.ObjectCreator;
import com.tcl.dias.common.redis.service.AuthTokenService;
import com.tcl.dias.common.redis.service.TokenExpireService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ContextConfiguration
public class PurgeTokenExpireServiceTest {

	@Autowired
	PurgeTokenExpireService purgeTokenExpireService;
	
	@Autowired
	ObjectCreator objectCreator;
	
	@MockBean
	TokenExpireService tokenExpireService;
	
	@MockBean
	AuthTokenService authTokenService;
	
	
	@Test
	public void processExpiredTokenPurging() throws TclCommonException{
		 
		Mockito.when(tokenExpireService.findAllKeys())
			.thenReturn(objectCreator.token());
		Mockito.when(tokenExpireService.find(Mockito.any()))
			.thenReturn(objectCreator.tokenExpire());
		doNothing().when(authTokenService).delete(Mockito.any());
		doNothing().when(tokenExpireService).delete(Mockito.anyString());
				
		purgeTokenExpireService.processExpiredTokenPurging();		
	}
	
	@Test
	public void processExpiredTokenPurging2() throws TclCommonException{
		
		Mockito.when(tokenExpireService.findAllKeys())
		.thenReturn(objectCreator.token());
	Mockito.when(tokenExpireService.find(Mockito.any()))
		.thenReturn(objectCreator.tokenExpireChildNull());
	doNothing().when(authTokenService).delete(Mockito.any());
	doNothing().when(tokenExpireService).delete(Mockito.anyString());
			
	purgeTokenExpireService.processExpiredTokenPurging();		
		
		
	}
	
	
	
}
