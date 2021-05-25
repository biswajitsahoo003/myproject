package com.tcl.dias.common.aspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.tcl.dias.common.beans.AuditRequestBean;
import com.tcl.dias.common.beans.QuoteDataBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.mq.beans.MqRequestResource;
import com.tcl.dias.common.mq.constants.MQIgnore;
import com.tcl.dias.common.service.CommonService;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.SourceAdapter;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * @author Manoj Controller Aspect to perform logging and exception handling
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 * 
 */
@Aspect
@Component
@Order(0)
public class CommonControllerAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(CommonControllerAspect.class);

	@Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
	public void controller() {
		// Point cut definition
	}

	@Pointcut("execution(@org.springframework.amqp.rabbit.annotation.RabbitListener void *.*(..))")
	void annotatedMethod() {
		// Point cut definition
	}

	@Pointcut("execution(@org.springframework.amqp.rabbit.annotation.RabbitListener java.lang.* *.*(..))")
	void methodOfAnnotatedClass() {
		// Point cut definition
	}

	@Pointcut("execution(* *.*(..))")
	protected void allMethod() {
		// Point cut definition
	}

	@Pointcut("execution(public * *(..))")
	protected void loggingPublicOperation() {
		// Point cut definition
	}

	@Pointcut("execution(* *.*(..))")
	protected void loggingAllOperation() {
		// Point cut definition
	}

	@Pointcut("within(com.tcl.dias..*)")
	private void logAnyFunctionWithinResource() {
		// Point cut definition
	}

	@Value("${rabbitmq.userinfo.name}")
	private String userInfoQueue;

	@Autowired
	MQUtils mqUtils;

	@Autowired
	UserInfoUtils userInfoUtils;

	@Autowired
	CommonService commonService;

	@Value("${optimus.audit.save.queue}")
	String auditSaveQueue;

	/**
	 * RabbitMQ logger- This method is used to log all the messages which is invoked
	 * by the RabbitMQ
	 * 
	 * @param joinPoint
	 * @throws Throwable
	 */
	@SuppressWarnings("unchecked")
	@Around("annotatedMethod()")
	public void adviseAnnotatedMethods(ProceedingJoinPoint joinPoint) throws Throwable {
		LOGGER.info("Entering aspect for queue");
		long start = System.currentTimeMillis();
		try {
			String methodName = joinPoint.getSignature().getName();
			String className = joinPoint.getSignature().getDeclaringTypeName();
			MethodSignature signature = (MethodSignature) joinPoint.getSignature();
			String queueName = getQueueName(signature);
			LOGGER.info("Entering MQ aspect => {} : {}", className, methodName);
			boolean ignoreFlag = false;
			if (Arrays.asList(MQIgnore.IGNORE_QUEUES).contains(queueName)) {
				ignoreFlag = true;
			}
			Object[] args = joinPoint.getArgs();
			MqRequestResource<String> mqRequestResource = null;
			Object[] newArgObjs = args;
			if (!ignoreFlag) {
				for (Object arg : args) {
					if (arg instanceof String) {
						mqRequestResource = Utils.convertJsonToObject((String) arg, MqRequestResource.class);
						newArgObjs = new Object[] { mqRequestResource.getRequest() };
					} else if (arg instanceof Message) {
						mqRequestResource = Utils.convertJsonToObject(((Message<String>) arg).getPayload(),
								MqRequestResource.class);
						Message<String> message = constructMessage(mqRequestResource, arg);
						newArgObjs = new Object[] { message };
					}
				}
			}
			if (ignoreFlag) {
				LOGGER.info("Ignore flag is ON so no wrapping");
				LOGGER.info("Queue call started for class {} ::  method {}", className, methodName);
				MDC.put(CommonConstants.MDC_TOKEN_KEY,Utils.generateUid(22));
				joinPoint.proceed(args);
			} else if (mqRequestResource != null) {
				MDC.put(CommonConstants.MDC_TOKEN_KEY,
						(StringUtils.isNotBlank(mqRequestResource.getMdcFilterToken())
								? mqRequestResource.getMdcFilterToken()
								: Utils.generateUid(22)));
				MDC.put(CommonConstants.MDC_UID_KEY, mqRequestResource.getUsername());
				SourceAdapter.setSource(mqRequestResource.getUsername());
				LOGGER.info("Queue call started for class {} ::  method {}", className, methodName);
				joinPoint.proceed(newArgObjs);
			} else {
				LOGGER.error("Queue Implementation is not at as per the guidelines");
			}
			long elapsedTime = System.currentTimeMillis() - start;
			LOGGER.info("Exiting Method {}.{} () with an execution time : {} ms", className, methodName, elapsedTime);
		} catch (TclCommonException | TclCommonRuntimeException e) {
			LOGGER.error("Error occured " + e.getMessage() + parseAllCause(e, MDC.get(CommonConstants.MDC_TOKEN_KEY)),
					e);
			constructErrorLog(e.getCause());
		} finally {
			MDC.remove(CommonConstants.MDC_TOKEN_KEY);
			MDC.remove(CommonConstants.MDC_UID_KEY);
			SourceAdapter.remove();
		}
	}

	/**
	 * getQueueName
	 * 
	 * @param signature
	 * @return
	 */
	private String getQueueName(MethodSignature signature) {
		RabbitListener rabbitListener = signature.getMethod().getAnnotation(RabbitListener.class);
		Queue[] queuesToDeclares = rabbitListener.queuesToDeclare();
		String queueName = null;
		if (queuesToDeclares != null)
			for (Queue queue : queuesToDeclares) {
				queueName = queue.value();
				LOGGER.info("Queues Declared {}", queueName);
			}
		return queueName;
	}

	@SuppressWarnings("unchecked")
	private Message<String> constructMessage(MqRequestResource<String> mqRequestResource, Object arg) {
		final String req = mqRequestResource.getRequest();
		final MessageHeaders headers = ((Message<String>) arg).getHeaders();
		Message<String> message = new Message<String>() {

			@Override
			public String getPayload() {
				return req;
			}

			@Override
			public MessageHeaders getHeaders() {
				return headers;
			}
		};
		return message;
	}

	@SuppressWarnings("unchecked")
	@Around("methodOfAnnotatedClass()")
	public Object adviseAsyncAnnotatedMethods(ProceedingJoinPoint joinPoint) throws Throwable {
		LOGGER.info("Entering aspect for queue");
		long start = System.currentTimeMillis();
		try {
			String methodName = joinPoint.getSignature().getName();
			String className = joinPoint.getSignature().getDeclaringTypeName();
			MethodSignature signature = (MethodSignature) joinPoint.getSignature();
			String queueName = getQueueName(signature);
			LOGGER.info("Entering MQ aspect => {} : {}", className, methodName);
			boolean ignoreFlag = false;
			if (Arrays.asList(MQIgnore.IGNORE_QUEUES).contains(queueName)) {
				ignoreFlag = true;
			}
			Object[] args = joinPoint.getArgs();
			MqRequestResource<String> mqRequestResource = null;
			Object[] newArgObjs = args;
			if (!ignoreFlag) {
				for (Object arg : args) {
					if (arg instanceof String) {
						mqRequestResource = Utils.convertJsonToObject((String) arg, MqRequestResource.class);
						newArgObjs = new Object[] { mqRequestResource.getRequest() };
					} else if (arg instanceof Message) {
						mqRequestResource = Utils.convertJsonToObject(((Message<String>) arg).getPayload(),
								MqRequestResource.class);
						Message<String> message = constructMessage(mqRequestResource, arg);
						newArgObjs = new Object[] { message };
					}
				}
			}
			if (ignoreFlag) {
				LOGGER.info("Ignore flag is ON so no wrapping");
				LOGGER.info("Queue call started for class {} ::  method {}", className, methodName);
				joinPoint.proceed(args);
			} else if (mqRequestResource != null) {
				MDC.put(CommonConstants.MDC_TOKEN_KEY,
						(StringUtils.isNotBlank(mqRequestResource.getMdcFilterToken())
								? mqRequestResource.getMdcFilterToken()
								: Utils.generateUid(22)));
				MDC.put(CommonConstants.MDC_UID_KEY, mqRequestResource.getUsername());
				SourceAdapter.setSource(mqRequestResource.getUsername());
				LOGGER.info("Queue call started for class {} ::  method {}", className, methodName);
				Object result = joinPoint.proceed(newArgObjs);
				long elapsedTime = System.currentTimeMillis() - start;
				LOGGER.info("Exiting Method {}.{} () with an execution time : {} ms", className, methodName,
						elapsedTime);
				LOGGER.info("User initiated {}", Utils.getSource());
				if (result == null) {
					return CommonConstants.EMPTY;
				}
				return result;
			} else {
				LOGGER.error("Queue Implementation is not at as per the guidelines");
			}
		} catch (TclCommonException | TclCommonRuntimeException e) {
			LOGGER.error("Error occured " + e.getMessage() + parseAllCause(e, MDC.get(CommonConstants.MDC_TOKEN_KEY)),
					e);
			constructErrorLog(e.getCause());
		} finally {
			MDC.remove(CommonConstants.MDC_TOKEN_KEY);
			MDC.remove(CommonConstants.MDC_UID_KEY);
			SourceAdapter.remove();
		}
		return CommonConstants.EMPTY;
	}

	/**
	 * Around -> Any method within resource annotated with @Controller annotation
	 * 
	 * @param joinPoint
	 * @return
	 * @throws Throwable
	 */
	@Around("controller() && allMethod()")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {

		long start = System.currentTimeMillis();
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getResponse();
		Map<String, Object> auditObj = new ConcurrentHashMap<>();
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();
		String sessionState = null;
		try {
			processQueryParam(request);
			LOGGER.info("Entering in Method : {} ", joinPoint.getSignature().getName());
			LOGGER.info("Class Name :  {}", joinPoint.getSignature().getDeclaringTypeName());
			String args = Arrays.toString(joinPoint.getArgs());
			LOGGER.info("Arguments :  {}", args);
			LOGGER.info("Target class : {}", joinPoint.getTarget().getClass().getName());
			if (null != request) {
				LOGGER.info("Http Method : {}", request.getMethod());
				LOGGER.info("Remote Address {} :: Remote host {}", request.getRemoteAddr(), request.getRemoteHost());
				Enumeration<?> headerNames = request.getHeaderNames();
				while (headerNames.hasMoreElements()) {
					String headerName = (String) headerNames.nextElement();
					String headerValue = request.getHeader(headerName);
					if (headerName.equalsIgnoreCase(CommonConstants.AUTHORIZATION)) {
						sessionState = Utils.getSessionState(headerValue);
						headerValue = sessionState;
					}

					if (headerName.equalsIgnoreCase("rid")) {
						MDC.put(CommonConstants.MDC_TOKEN_KEY, headerValue);
					}
					LOGGER.info("Header Name: {} => Header Value : {}", headerName, headerValue);
				}
				LOGGER.info("Request Path info : {}", request.getServletPath());
			}
			auditObj = processAudit(joinPoint);
			String className = joinPoint.getSignature().getDeclaringTypeName();
			String methodName = joinPoint.getSignature().getName();
			Object result = joinPoint.proceed();
			if (result != null) {
				auditObj.put("responseBody", Utils.convertObjectToJson(result));
			}
			String returnValue = this.getValue(result);
			LOGGER.info("Method Return value : {}", returnValue);
			long elapsedTime = System.currentTimeMillis() - start;
			auditObj.put("roundTripTime", elapsedTime);
			LOGGER.info("Exiting Method {}.{} () with an execution time : {} ms", className, methodName, elapsedTime);
			return result;
		} catch (TclCommonException e) {
			LOGGER.error("Error occured " + e.getMessage() + parseAllCause(e, MDC.get(CommonConstants.MDC_TOKEN_KEY)),
					e);
			response.setStatus((e.getStatusCode() != 0 ? e.getStatusCode() : ResponseResource.R_CODE_ERROR));
			return new ResponseResource<>((e.getStatusCode() != 0 ? e.getStatusCode() : ResponseResource.R_CODE_ERROR),
					e.getMessage(), Status.FAILURE);
		} catch (TclCommonRuntimeException e) {
			LOGGER.error("Error occured " + e.getMessage() + parseAllCause(e, MDC.get(CommonConstants.MDC_TOKEN_KEY)),
					e);
			response.setStatus((e.getStatusCode() != 0 ? e.getStatusCode() : ResponseResource.R_CODE_ERROR));
			return new ResponseResource<>((e.getStatusCode() != 0 ? e.getStatusCode() : ResponseResource.R_CODE_ERROR),
					e.getMessage(), Status.FAILURE);
		} catch (Exception e) {
			LOGGER.error("Error occured " + e.getMessage() + parseAllCause(e, MDC.get(CommonConstants.MDC_TOKEN_KEY)),
					e);
			response.setStatus(ResponseResource.R_CODE_ERROR);
			return new ResponseResource<>(ResponseResource.R_CODE_ERROR, e.getMessage(), Status.ERROR);
		} finally {
			LOGGER.info("Before Persisting data into the audit");
			processPersistAudit(response.getStatus(), auditObj, Utils.getSource(), request, sessionState);
			LOGGER.info("Persisted into the audit");
		}
	}

	private String parseAllCause(Throwable rootCause, String mdcToken) {
		StringBuilder sb = new StringBuilder("");
		int causeCounter = 1;
		while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
			sb.append("mdcToken: " + mdcToken + " Cause : " + causeCounter + " ::: "
					+ ExceptionUtils.getStackTrace(rootCause.getCause()));
			rootCause = rootCause.getCause();
			if (causeCounter >= 15) {
				break;
			}
			causeCounter++;
		}
		return sb.toString();
	}

	/**
	 * extractQueryParam
	 */
	private void processQueryParam(HttpServletRequest request) {
		try {
			Map<String, String> queryParameters = new HashMap<>();
			String queryString = request.getQueryString();
			if (StringUtils.isNotEmpty(queryString)) {
				String[] parameters = queryString.split("&");

				for (String parameter : parameters) {
					if (parameter.contains("=")) {
						String[] keyValuePair = parameter.split("=");
						if (keyValuePair.length >= 2) {
							queryParameters.put(keyValuePair[0], keyValuePair[1]);
							LOGGER.info("Raw QueryParam Received {} = {} ", keyValuePair[0], keyValuePair[1]);
							org.jsoup.nodes.Document valueDoc = processJsoup(request, keyValuePair);
							LOGGER.info("After striping QueryParam Received {} = {} ", keyValuePair[1],
									valueDoc.text());
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error in correcting the queryparam", e);
		}
	}

	/**
	 * processJsoup
	 * 
	 * @param request
	 * @param keyValuePair
	 * @return
	 */
	private org.jsoup.nodes.Document processJsoup(HttpServletRequest request, String[] keyValuePair) {
		org.jsoup.nodes.Document valueDoc = Jsoup.parse(keyValuePair[1]);
		if (StringUtils.isNotBlank(keyValuePair[1]) && StringUtils.isNotBlank(valueDoc.text())) {
			request.setAttribute(keyValuePair[1], valueDoc.text());
		} else {
			LOGGER.info("The tag contains suspicious so ignoring for tag {}", keyValuePair[1]);
			request.removeAttribute(keyValuePair[1]);
		}
		return valueDoc;
	}

	/**
	 * processPersistAudit
	 * 
	 * @param responseCode
	 * @param auditObj
	 * @param user
	 * @param request
	 * @throws TclCommonException
	 */
	private void processPersistAudit(int responseCode, Map<String, Object> auditObj, String user,
			HttpServletRequest request, String sessionState) throws TclCommonException {
		LOGGER.info("MDC Filter token value in before Queue call to save audit data : {}",
				MDC.get(CommonConstants.MDC_TOKEN_KEY));
		if (auditObj != null) {
			String toValue = (String) auditObj.get("toValue");
			Object responseBodyObj =  auditObj.get("responseBody");
			String responseBody=null;
			if(responseBodyObj!=null) {
				responseBody=(String)responseBodyObj;
			}
			QuoteDataBean quoteDataBean = (QuoteDataBean) auditObj.get("quoteDataBean");
			String time = String.valueOf(auditObj.get("roundTripTime"));
			String url = null;
			if (request.getQueryString() == null) {
				url = request.getRequestURI();
			} else {
				url = request.getRequestURI().concat("?").concat(request.getQueryString());
			}
			AuditRequestBean auditRequestBean = new AuditRequestBean(MDC.get(CommonConstants.MDC_TOKEN_KEY),
					quoteDataBean.getQuoteCode(), quoteDataBean.getStage(), toValue, "Comments", user, responseCode,
					quoteDataBean.getFromValue(), request.getMethod(), url, time, sessionState,toValue,responseBody);
			mqUtils.send(auditSaveQueue, Utils.convertObjectToJson(auditRequestBean));
		}
	}

	/**
	 * processAudit
	 * 
	 * @param joinPoint
	 * @param toValue
	 * @return
	 * @throws TclCommonException
	 */
	private Map<String, Object> processAudit(ProceedingJoinPoint joinPoint) throws TclCommonException {
		long start = System.currentTimeMillis();
		Map<String, Object> auditObjects = new HashMap<>();
		QuoteDataBean quoteDataBean = new QuoteDataBean();
		Map<String, Integer> requestVariables = new ConcurrentHashMap<>();
		Object[] jointPointAgr = joinPoint.getArgs();
		MethodSignature methodSignature = (MethodSignature) joinPoint.getStaticPart().getSignature();
		Method method = methodSignature.getMethod();
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		assert jointPointAgr.length == parameterAnnotations.length;
		requestVariables.put("isOms", 0);
		contructBody(auditObjects, requestVariables, jointPointAgr, parameterAnnotations);
		requestVariables.put("isOms", 0);//Disabling this part
		if (!requestVariables.isEmpty() && requestVariables.get("isOms") != 0) {
			LOGGER.info("Entering Method getQuoteData to get quote/Order data : {}", requestVariables);
			quoteDataBean = commonService.getQuoteData(requestVariables);
			LOGGER.info("getQuoteData() Method Return value : {}", quoteDataBean);
		}
		auditObjects.put("quoteDataBean", quoteDataBean);
		LOGGER.info("Exiting processAudit with an execution time : {} ms", System.currentTimeMillis() - start);
		return auditObjects;
	}

	/**
	 * contructBody
	 * 
	 * @param auditObjects
	 * @param requestVariables
	 * @param jointPointAgr
	 * @param parameterAnnotations
	 * @throws TclCommonException
	 */
	private void contructBody(Map<String, Object> auditObjects, Map<String, Integer> requestVariables,
			Object[] jointPointAgr, Annotation[][] parameterAnnotations) throws TclCommonException {
		for (int argIndex = 0; argIndex < jointPointAgr.length; argIndex++) {
			for (Annotation annotation : parameterAnnotations[argIndex]) {
				if (!(annotation instanceof PathVariable) && !(annotation instanceof RequestParam)
						&& !(annotation instanceof RequestBody))
					continue;
				if (annotation instanceof PathVariable) {
					PathVariable pathParam = (PathVariable) annotation;
					getRequestVariables(requestVariables, jointPointAgr, argIndex, pathParam.value());
				}
				if (annotation instanceof RequestParam) {
					RequestParam requestParam = (RequestParam) annotation;
					getRequestVariables(requestVariables, jointPointAgr, argIndex, requestParam.value());
				}
				if (annotation instanceof RequestBody) {
					Object requestBody = jointPointAgr[argIndex];
					if (requestBody != null) {
						auditObjects.put("toValue", Utils.convertObjectToJson(requestBody));
					}
				}
			}
		}
	}

	/**
	 * getRequestVariables
	 * 
	 * @param requestVariables
	 * @param jointPointAgr
	 * @param argIndex
	 * @param pathParam
	 */
	private void getRequestVariables(Map<String, Integer> requestVariables, Object[] jointPointAgr, int argIndex,
			String variable) {
		switch (variable) {
		case CommonConstants.ORDERID:
			Integer orderId = (Integer) jointPointAgr[argIndex];
			requestVariables.put(CommonConstants.ORDERID, orderId);
			requestVariables.put("isOms", 1);
			break;
		case CommonConstants.ORDERLEID:
			Integer orderLeId = (Integer) jointPointAgr[argIndex];
			requestVariables.put(CommonConstants.ORDERLEID, orderLeId);
			requestVariables.put("isOms", 1);
			break;
		case CommonConstants.QUOTEID:
			Integer quoteId = (Integer) jointPointAgr[argIndex];
			requestVariables.put(CommonConstants.QUOTEID, quoteId);
			requestVariables.put("isOms", 1);
			break;
		case CommonConstants.QUOTELEID:
			Integer quoteLeId = (Integer) jointPointAgr[argIndex];
			requestVariables.put(CommonConstants.QUOTELEID, quoteLeId);
			requestVariables.put("isOms", 1);
			break;
		default:
			break;
		}
	}

	/**
	 * Constructing the error log to display the timestamp information in all the
	 * lines of the error stack trace.
	 * 
	 * @param stackTraces
	 */
	public void constructErrorLog(Throwable cause) {
		for (StackTraceElement stackTrace : cause.getStackTrace()) {
			LOGGER.error(stackTrace.getClassName() + "." + stackTrace.getMethodName() + "(" + stackTrace.getFileName()
					+ ":" + stackTrace.getLineNumber() + ")" + ":::" + stackTrace.toString());
		}
	}

	private String getValue(Object result) {
		String returnValue = null;
		if (null != result) {
			if (result.toString().endsWith("@" + Integer.toHexString(result.hashCode()))) {
				returnValue = ReflectionToStringBuilder.toString(result);
			} else {
				returnValue = result.toString();
			}
		}
		return returnValue;
	}

}
