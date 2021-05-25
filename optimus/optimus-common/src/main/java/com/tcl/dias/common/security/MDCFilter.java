package com.tcl.dias.common.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import org.keycloak.KeycloakPrincipal;
import org.slf4j.MDC;
import org.springframework.security.core.context.SecurityContextHolder;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.utils.Utils;

/**
 * Mapped Diagnostic Context (MDC) Filter
 * 
 * @author Manojkumar R
 *
 */
@WebFilter(filterName = "mdcFilter", urlPatterns = { "/*" })
public class MDCFilter implements Filter {

	@Override
	public void destroy() {
		// DO Nothing
	}

	/**
	 * doFilter
	 */
	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException {
		String principal = CommonConstants.ANONYMOUS_USER;
		if (SecurityContextHolder.getContext().getAuthentication() != null) {
			if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof KeycloakPrincipal) {
				@SuppressWarnings("rawtypes")
				KeycloakPrincipal customUserData = (KeycloakPrincipal) SecurityContextHolder.getContext()
						.getAuthentication().getPrincipal();
				if (customUserData != null)
					principal = customUserData.getName();
			} else if (SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal() instanceof UserInformation) {
				UserInformation userInformation = (UserInformation) SecurityContextHolder.getContext()
						.getAuthentication().getPrincipal();
				if (userInformation != null)
					principal = userInformation.getUserId();
			} else {
				principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			}
		}
		MDC.put(CommonConstants.MDC_TOKEN_KEY, Utils.generateUid(22));
		MDC.put(CommonConstants.MDC_UID_KEY, principal);
		try {
			chain.doFilter(request, response);
		} finally {
			MDC.remove(CommonConstants.MDC_TOKEN_KEY);
			MDC.remove(CommonConstants.MDC_UID_KEY);
		}
	}

	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
		// DO Nothing
	}
}
