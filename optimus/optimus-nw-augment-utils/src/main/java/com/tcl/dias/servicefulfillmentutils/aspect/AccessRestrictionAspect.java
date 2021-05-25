/**
 * @author vivek
 *
 * 
 */
package com.tcl.dias.servicefulfillmentutils.aspect;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.networkaugment.entity.entities.Task;
import com.tcl.dias.networkaugment.entity.entities.TaskAssignment;
import com.tcl.dias.networkaugment.entity.repository.TaskRepository;
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

				String userName = userInformation.getUserId();
				if (!userName.equalsIgnoreCase("root")) {

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
}
