package com.tcl.dias.location.aspect;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.UserType;
import com.tcl.dias.location.constants.ExceptionConstants;
import com.tcl.dias.location.entity.entities.CustomerLocation;
import com.tcl.dias.location.entity.entities.LocationLeCustomer;
import com.tcl.dias.location.entity.repository.CustomerLocationRepository;
import com.tcl.dias.location.entity.repository.LocationLeCustomerRepository;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
public class LocationControllerAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(LocationControllerAspect.class);

	@Autowired
	CustomerLocationRepository customerLocationRepository;
	
	@Autowired
	UserInfoUtils userInfoUtils;

	@Autowired
	LocationLeCustomerRepository locationLeCustomerRepository;

	@Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
	public void controller() {
		// Point cut definition
	}

	@Pointcut("execution(* com.tcl.dias.location.controller.v1.LocationController.*(..))")
	protected void allCustomerMethod() {
		// Point cut definition
	}

	@Around("controller() && allCustomerMethod()")
	public Object validateLocationAround(ProceedingJoinPoint joinPoint) throws Throwable {
		Object[] args = joinPoint.getArgs();
		Integer locationId = null;
		MethodSignature methodSignature = (MethodSignature) joinPoint.getStaticPart().getSignature();
		Method method = methodSignature.getMethod();
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		assert args.length == parameterAnnotations.length;
		for (int argIndex = 0; argIndex < args.length; argIndex++) {
			for (Annotation annotation : parameterAnnotations[argIndex]) {
				if (!(annotation instanceof PathVariable))
					continue;
				PathVariable pathParam = (PathVariable) annotation;
				if ("locationId".equals(pathParam.value())) {
					locationId = (Integer) args[argIndex];
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
			if (locationId != null) {
				List<CustomerLocation> customerLocationEntity = customerLocationRepository
						.findByLocation_IdAndErfCusCustomerIdIn(locationId, customersSet);
				validateCustomerLocationEntity(customerLocationEntity, customersSet, customerLeIds, locationId);
			}
		}
		return joinPoint.proceed();
	}
	
	public void validateCustomerLocationEntity(List<CustomerLocation> customerLocationEntity,Set<Integer> customersSet,Set<Integer> customerLeIds,Integer locationId) throws TclCommonException {
		boolean isValid = true;
		if ((customerLocationEntity==null || customerLocationEntity.isEmpty()) && !userInfoUtils.getUserType().equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
			isValid = false;
		}
		if (!isValid) {
			List<LocationLeCustomer> locationEntities = locationLeCustomerRepository.findByLocation_Id(locationId);
			for (LocationLeCustomer locationLeCustomer : locationEntities) {
				if (customerLeIds.contains(locationLeCustomer.getErfCusCustomerLeId())) {
					isValid = true;
					break;
				}
			}
		}
		if (!isValid && !userInfoUtils.getUserType().equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
			LOGGER.error("Unauthorized access for customer ");
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_FORBIDDEN_ERROR);
		}
	}

	private void getMapperCustomerDetails(List<CustomerDetail> customerDetails, Set<Integer> customersSet,
			Set<Integer> customerLeIds) {
		customerDetails.stream().forEach(customerDetail->{
			customersSet.add(customerDetail.getErfCustomerId());
			customerLeIds.add(customerDetail.getCustomerLeId());
		});
	}
}
