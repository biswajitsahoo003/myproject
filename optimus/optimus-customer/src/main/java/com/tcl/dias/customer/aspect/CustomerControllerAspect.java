package com.tcl.dias.customer.aspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.UserType;
import com.tcl.dias.customer.constants.ExceptionConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * This file contains the CustomerControllerAspect.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Aspect
@Component
@Order(1)
public class CustomerControllerAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerControllerAspect.class);

	@Autowired
	UserInfoUtils userInfoUtils;

	@Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
	public void controller() {
		// Point cut definition
	}

	@Pointcut("execution(* com.tcl.dias.customer.controller.v1.CustomerController.*(..))")
	protected void allCustomerMethod() {
		// Point cut definition
	}

	@Around("controller() && allCustomerMethod()")
	public Object validateCustomerAround(ProceedingJoinPoint joinPoint) throws Throwable {
		Object[] args = joinPoint.getArgs();
		Integer customerLeId = null;
		Integer customerId = null;
		MethodSignature methodSignature = (MethodSignature) joinPoint.getStaticPart().getSignature();
		Method method = methodSignature.getMethod();
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		assert args.length == parameterAnnotations.length;
		for (int argIndex = 0; argIndex < args.length; argIndex++) {
			for (Annotation annotation : parameterAnnotations[argIndex]) {
				if (!(annotation instanceof PathVariable))
					continue;
				PathVariable pathParam = (PathVariable) annotation;
				if ("customerLeId".equals(pathParam.value())) {
					customerLeId = (Integer) args[argIndex];
				}

				if ("customerId".equals(pathParam.value())) {
					customerId = (Integer) args[argIndex];
				}
			}
		}

		//TODO :  Since customer id will be null until opportunity is created for partner,
		// API breaks with null pointer exception, so Temporary fix, need to be updated.
		if(!userInfoUtils.getUserType().equalsIgnoreCase(CommonConstants.PARTNER)) {
			List<CustomerDetail> customerDetails = userInfoUtils.getCustomerDetails();
			Set<Integer> customersSet = new HashSet<>();
			Set<Integer> customerLeIds = new HashSet<>();
			getMapperCustomerDetails(customerDetails, customersSet, customerLeIds);
			if (customerId != null && !customersSet.contains(customerId)
					&& !userInfoUtils.getUserType().equalsIgnoreCase(UserType.INTERNAL_USERS.toString())
					&& !userInfoUtils.getUserType().equalsIgnoreCase(UserType.SUPER_ADMIN.toString())) {
				LOGGER.error("Unauthorized access for customer {}", customerId);
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_FORBIDDEN_ERROR);
			}
			if (customerLeId != null && !customerLeIds.contains(customerLeId)
					&& !userInfoUtils.getUserType().equalsIgnoreCase(UserType.INTERNAL_USERS.toString())
					&& !userInfoUtils.getUserType().equalsIgnoreCase(UserType.SUPER_ADMIN.toString())) {
				LOGGER.error("Unauthorized access for customerLeId {}", customerLeId);
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_FORBIDDEN_ERROR);
			}
		}
		return joinPoint.proceed();
	}

	private void getMapperCustomerDetails(List<CustomerDetail> customerDetails, Set<Integer> customersSet,
			Set<Integer> customerLeIds) {
		for (CustomerDetail customerDetail : customerDetails) {
			customersSet.add(customerDetail.getErfCustomerId());
			customerLeIds.add(customerDetail.getCustomerLeId());
		}
	}
}
