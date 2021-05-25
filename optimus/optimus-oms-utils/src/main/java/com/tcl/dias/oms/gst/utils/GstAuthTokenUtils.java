package com.tcl.dias.oms.gst.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

public class GstAuthTokenUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(GstAuthTokenUtils.class);

	public static String getGstAuthToken(ApplicationContext appCtx) throws TclCommonException {
		LOGGER.info("Inside getGstAuthToken for getting the token");
		String token = null;
		try {
			//GstTokenRepository gstTokenRepository = appCtx.getBean(GstTokenRepository.class);
		//	Set<Object> gstObj = gstTokenRepository.find();
		//	if (gstObj == null || gstObj.isEmpty()) {
				LOGGER.info("Gst is not there is Redis so getting the token");
				GstTokenService gstTokenService = appCtx.getBean(GstTokenService.class);
				token = gstTokenService.getGstToken();
				//GstToken gstToken = new GstToken();
			//	gstToken.setCreatedTime(new Date());
			//	gstToken.setToken(token);
			//	gstTokenRepository.save(gstToken, 18000000);
			//} else {
			//	for (Object tokenBean : gstObj) {
			//		GstToken gstToken = (GstToken) tokenBean;
			//		token = gstToken.getToken();
			//		LOGGER.info("Token retrived {}", token);
			//	}
			//}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return token;
	}

}
