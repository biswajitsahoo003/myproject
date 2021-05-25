package com.tcl.dias.oms.gsc.tiger;

import static com.tcl.dias.oms.gsc.util.GscConstants.HEADER_GSC_LOG_REFERENCE_ID;
import static com.tcl.dias.oms.gsc.util.GscConstants.HEADER_GSC_LOG_REFERENCE_TYPE;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.google.common.base.Stopwatch;
import com.google.common.base.Throwables;
import com.tcl.dias.oms.entity.entities.GSCServiceLog;
import com.tcl.dias.oms.entity.repository.GscServiceLogRepository;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;

/**
 * Custom OkHttp interceptor to log request/response to/from external service
 * HTTP calls into DB
 *
 * @author Ramasubramanian Sankar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GscServiceLogInterceptor implements Interceptor {

	public static final Logger LOGGER = LoggerFactory.getLogger(GscServiceLogRepository.class);

	private String serviceName;

	@Autowired
	GscServiceLogRepository gscServiceLogRepository;

	@Autowired
	private PlatformTransactionManager transactionManager;

	/**
	 * Intercepts the HTTP request and logs the request/responses to OMS table
	 * 
	 * @param chain
	 * @return
	 * @throws IOException
	 */
	@Override
	public Response intercept(Chain chain) throws IOException {
		Objects.requireNonNull(serviceName, "Service Name has not been set to a valid value");
		Request request = chain.request();
		HttpUrl requestUrl = request.url();
		String requestHttpMethod = request.method();
		RequestBody requestBody = request.body();
		int responseCode = 0;
		ResponseBody responseBody = null;
		long timeTakenInMs = 0;
		Stopwatch stopwatch = Stopwatch.createStarted();
		Response response = null;
		String status = "SUCCESS";
		Integer referenceId = Integer.valueOf(request.header(HEADER_GSC_LOG_REFERENCE_ID));
		String referenceType = request.header(HEADER_GSC_LOG_REFERENCE_TYPE);
		Objects.requireNonNull(referenceId, "Reference Id header cannot be null while calling tiger service");
		Objects.requireNonNull(referenceType, "Reference Type header cannot be null while calling tiger service");
		try {
			response = chain.proceed(request.newBuilder().removeHeader(HEADER_GSC_LOG_REFERENCE_ID)
					.removeHeader(HEADER_GSC_LOG_REFERENCE_TYPE).build());
			responseCode = response.code();
			responseBody = response.body();
		} catch (Exception e) {
			status = "FAILURE";
			Throwables.propagate(e);
		} finally {
			timeTakenInMs = stopwatch.stop().elapsed(TimeUnit.MILLISECONDS);
			GSCServiceLog gscServiceLog = new GSCServiceLog();
			gscServiceLog.setReferenceId(referenceId);
			gscServiceLog.setReferenceType(referenceType);
			gscServiceLog.setHttpMethod(requestHttpMethod);
			if (Objects.nonNull(requestBody)) {
				Buffer buffer = new Buffer();
				requestBody.writeTo(buffer);
				gscServiceLog.setRequestBody(buffer.readString(Charset.defaultCharset()));
			}
			gscServiceLog.setRequestUrl(requestUrl.toString());
			gscServiceLog.setResponseCode(String.valueOf(responseCode));
			gscServiceLog.setResponseTimeMs((int) timeTakenInMs);
			if (Objects.nonNull(responseBody)) {
				Headers responseHeaders = response.headers();
				BufferedSource source = responseBody.source();
				source.request(Long.MAX_VALUE);
				Buffer buffer = source.buffer().clone();
				if ("gzip".equalsIgnoreCase(responseHeaders.get("Content-Encoding"))) {
					GzipSource gzippedResponseBody = null;
					try (Buffer responseBuffer = new Buffer()) {
						gzippedResponseBody = new GzipSource(buffer.clone());
						responseBuffer.writeAll(gzippedResponseBody);
					} finally {
						if (gzippedResponseBody != null) {
							gzippedResponseBody.close();
						}
					}
				}
				gscServiceLog.setResponseBody(buffer.readString(Charset.defaultCharset()));
			}
			gscServiceLog.setServiceName(serviceName);
			gscServiceLog.setStatus(status);
			Timestamp now = Timestamp.valueOf(LocalDateTime.now());
			gscServiceLog.setCreatedBy(1);
			gscServiceLog.setCreatedTime(now);
			gscServiceLog.setUpdatedBy(1);
			gscServiceLog.setUpdatedTime(now);
			saveGscServiceLog(gscServiceLog);
		}
		return response;
	}

	private Integer saveGscServiceLog(GSCServiceLog gscServiceLog) {
		// This record has to be inserted even if the parent transaction has failed so
		// handling it in independent nested trasaction
		TransactionStatus ts = transactionManager
				.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
		try {
			gscServiceLog = gscServiceLogRepository.save(gscServiceLog);
			transactionManager.commit(ts);
			return gscServiceLog.getId();
		} catch (Exception e) {
			transactionManager.rollback(ts);
			LOGGER.warn("Error occurred while saving GSC service log", e);
			return null;
		}
	}

	/**
	 * Set the service name for this interceptor to use while logging
	 * 
	 * @param serviceName
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
}
