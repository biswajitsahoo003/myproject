/**
 * @author vivek
 *
 * 
 */
package com.tcl.dias.servicefulfillmentutils.aspect;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.entities.TaskAssignment;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;
import com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * @author vivek
 * aspect for validation of use access based on roles
 *
 */
@Aspect
@Component
@Order(0)
public class AccessRestrictionAspect {
	
	@Autowired
	UserInfoUtils userInfoUtils;
	
	@Autowired
	TaskRepository taskRepository;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AccessRestrictionAspect.class);

	
	@Pointcut("@annotation(com.tcl.dias.servicefulfillmentutils.custom.annotations.RestrictUserAccess)")
	public void accessRestriction() {
		// Point cut definition
	}
	
	@Pointcut("@annotation(com.tcl.dias.servicefulfillmentutils.custom.annotations.RestrictNetworkAccess)")
	public void networkAccessRestriction() {
		// Point cut definition
	}

	@Around("accessRestriction()")
	public void restrictUserAccessAround(ProceedingJoinPoint joinPoint) throws Throwable {
		LOGGER.info("Entering aspect for restrictUserAccessAround");
		long start = System.currentTimeMillis();
		String methodName = joinPoint.getSignature().getName();
		String className = joinPoint.getSignature().getDeclaringTypeName();
		LOGGER.info("Entering aspect for restrictUserAccessAround methodName:{} and className:{} and start time:{} ",
				methodName, className, start);
		try {
			UserInformation userInformation = userInfoUtils.getUserInformation();
			if (userInformation != null && userInformation.getUserId() != null) {

				LOGGER.info("userInformation user id :{} and user type:{} and user name:{}",
						userInformation.getUserId(), userInformation.getUserType(), userInformation.getFirstName());
				
				if(userInformation.getPmi()!=null && userInformation.getPmiro()!=null && userInformation.getPmi() && userInformation.getPmiro()) {
					LOGGER.info("Unauthorized access for impersonated user:{} with only read access",userInformation.getUserId());
					throw new TclCommonException(ExceptionConstants.COMMON_ERROR,ResponseResource.R_CODE_FORBIDDEN_ERROR);
				}

				String userName = userInformation.getUserId();
				
				List<String> userGroups = userInformation.getUserToUserGroupName();
				
				LOGGER.info("userInformation user id :{} and user type:{} and user name:{} userName:{} userGroups:{}",
						userInformation.getUserId(), userInformation.getUserType(), userInformation.getFirstName(),userName,userGroups);
				
				boolean supportUser = false;
				try {
					if(userGroups!=null && !userGroups.isEmpty()) {
						for(Object groupNameObject : userGroups) {							
							Map<String, Object> groupNameMap= (Map<String, Object>)groupNameObject;
							if(groupNameMap!=null && groupNameMap.get("groupName")!=null && String.valueOf(groupNameMap.get("groupName")).equalsIgnoreCase("IT_SUPPORT")){
								supportUser =true;
								break;
							}
						}
					}
				}catch(Exception ee) {
					LOGGER.error("eXception {}",ee);
				}
				
				
				if (!userName.equalsIgnoreCase("root") && !supportUser) {
									

					Object methodsSingnature[] = joinPoint.getArgs();

					for (Object methodParam : Arrays.asList(methodsSingnature)) {
						if (methodParam!=null && methodParam instanceof Task) {
							Task task = (Task) methodParam;
							if ("Y".equalsIgnoreCase(task.getMstTaskDef().getIsManualTask())) {
								LOGGER.info("access validation started form manual task with name:{} and task id:{}",
										task.getMstTaskDef().getKey(), task.getId());

								TaskAssignment taskAssignment = task.getTaskAssignments().stream().findFirst()
										.orElse(null);
								if (taskAssignment.getUserName() != null
										&& !userName.equalsIgnoreCase(taskAssignment.getUserName())) {
									LOGGER.info("Unauthorized access for user {} and task nam:{} and taskid:{}",
											userName, task.getId());
									throw new TclCommonException(ExceptionConstants.COMMON_ERROR,
											ResponseResource.R_CODE_FORBIDDEN_ERROR);

								}
							}
							else {
							LOGGER.info("skip validation for  system task with name:{} and task id:{}",
									task.getMstTaskDef().getKey(), task.getId());
							}

						}

					}
				} else {
					LOGGER.info("skip validation as user is root");

				}
			} else {
				LOGGER.info("skip validation as user info is not there");
			}

			joinPoint.proceed();
		} catch (TclCommonException | TclCommonRuntimeException e) {
			LOGGER.error("failed to validate user access:{}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR,
					ResponseResource.R_CODE_FORBIDDEN_ERROR);
		}

	}
	
	@Around("networkAccessRestriction()")
	public void restrictNetworkAccessAround(ProceedingJoinPoint joinPoint) throws Throwable {
		LOGGER.info("Entering aspect for restrictNetworkAccessAround");
		long start = System.currentTimeMillis();
		String methodName = joinPoint.getSignature().getName();
		String className = joinPoint.getSignature().getDeclaringTypeName();
		LOGGER.info("Entering aspect for restrictNetworkAccessAround methodName:{} and className:{} and start time:{} ",
				methodName, className, start);
		boolean internalUser = false;
		javax.servlet.http.HttpServletRequest httpServletRequest = ((org.springframework.web.context.request.ServletRequestAttributes) RequestContextHolder
				.currentRequestAttributes()).getRequest();
		try {
			UserInformation userInformation = userInfoUtils.getUserInformation();
			if (userInformation != null && userInformation.getUserId() != null) {

				LOGGER.info("restrictNetworkAccessAround userInformation user id :{} and user type:{} and user name:{}",
						userInformation.getUserId(), userInformation.getUserType(), userInformation.getFirstName());

				String userName = userInformation.getUserId();

				Object methodsSingnature[] = joinPoint.getArgs();

				String xRefValue = httpServletRequest.getHeader("x-f-in-int-ext");
				LOGGER.info("xRefValue value={}", xRefValue);

				if (xRefValue != null && xRefValue.equalsIgnoreCase("338D4077-B862-4DD6-B41C-DE41B9B036C0")) {
					internalUser = true;
					LOGGER.info("xRefValue value={}", xRefValue);

				}

				for (Object methodParam : Arrays.asList(methodsSingnature)) {
					if (methodParam != null && methodParam instanceof BaseRequest) {
						if (!internalUser) {

							LOGGER.info("access is restricted for this user:{} and time", userName, new Date());

							throw new TclCommonRuntimeException(
									com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants.NOT_CONNECTED_TO_VPN,
									ResponseResource.R_CODE_ERROR);

						}

					}

				}
			} else {
				LOGGER.info("restrictNetworkAccessAround failed validation as user info is not there");

				throw new TclCommonException(
						com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants.NOT_CONNECTED_TO_VPN,
						ResponseResource.R_CODE_ERROR);
			}

			joinPoint.proceed();
		} catch (TclCommonException | TclCommonRuntimeException e) {
			LOGGER.error("restrictNetworkAccessAround failed to validate user access:{}", e);
			throw new TclCommonException(
					com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants.NOT_CONNECTED_TO_VPN,
					ResponseResource.R_CODE_ERROR);
		}

	}
}
