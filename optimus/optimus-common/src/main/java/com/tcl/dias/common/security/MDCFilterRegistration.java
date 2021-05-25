package com.tcl.dias.common.security;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.stereotype.Component;
/**
 * 
 * @author Manojkumar R
 *
 */
@Component
public class MDCFilterRegistration extends FilterRegistrationBean<MDCFilter> {

	public MDCFilterRegistration() {
		super(new MDCFilter());
		addUrlPatterns("/*");
		setOrder(Integer.MAX_VALUE);
	}
}
