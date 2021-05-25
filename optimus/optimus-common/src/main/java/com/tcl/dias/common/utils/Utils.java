package com.tcl.dias.common.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.SoftException;
import org.aspectj.lang.reflect.MethodSignature;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.customannotations.BaseArgument;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * Contains common utility methods used across the project
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class Utils {

	private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	public static final ObjectMapper objectMapper = new ObjectMapper()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
			.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
	public static final String FIRST_DAY_OF_MONTH = "firstDayOfMonth";
	public static final String LAST_DAY_OF_MONTH = "lastDayOfMonth";
	public static final String FT = "FT";
	public static final String FRA = "FRA";
	// .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

	
	private Utils() {
	}

	public static final String COMMON_ERROR = "common.error";
	public static final String GETTER_PREFIX = "get";
	public static final String ERR_IN_EXCEL_CREATION = "error in generating excel sheet";
	private static String HOSTNAME, HOSTIP = null;
	private static DecimalFormat roundFormat = new DecimalFormat("#.##");
	
	
	
	public static void logMemory() {
		int mb = 1024*1024;
		Runtime runtime = Runtime.getRuntime();
		LOGGER.info("##### Heap utilization statistics [MB] #####");
		//Print used memory
		LOGGER.debug("Used Memory: {}" 
		,(runtime.totalMemory() - runtime.freeMemory()) / mb);

		//Print free memory
		LOGGER.info("Free Memory: {}" 
		, runtime.freeMemory() / mb);
		//Print total available memory
		LOGGER.info("Total Memory: {}", runtime.totalMemory() / mb);

		//Print Maximum available memory
		LOGGER.info("Max Memory: {}" , runtime.maxMemory() / mb);
	}

	/**
	 *
	 * mapMemoryStamp
	 */
	public static void mapMemoryStamp() {
		int mb = 1024 * 1024;

		// Getting the runtime reference from system
		Runtime runtime = Runtime.getRuntime();

		LOGGER.info("##### Heap utilization statistics [MB] #####");

		// Print used memory
		LOGGER.info("Used Memory: {}", (runtime.totalMemory() - runtime.freeMemory()) / mb);

		// Print free memory
		LOGGER.info("Free Memory: {}", runtime.freeMemory() / mb);

		// Print total available memory
		LOGGER.info("Total Memory: {}", runtime.totalMemory() / mb);

		// Print Maximum available memory
		LOGGER.info("Max Memory: {}", runtime.maxMemory() / mb);

	}

	/**
	 *
	 * callGc
	 */
	public static void callGc() {
		LOGGER.info("################################################################################################");
		mapMemoryStamp();
		System.gc();
		mapMemoryStamp();
		LOGGER.info("################################################################################################");

	}

	/**
	 * Method to generate the random uuid
	 *
	 * @return
	 */
	public static String generateUid() {
		int length = 16;
		return RandomStringUtils.random(length, true, true).toUpperCase();
	}

	public static String generateUid(int length) {
		return RandomStringUtils.random(length, true, true).toUpperCase();
	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
		Map<Object, Boolean> map = new ConcurrentHashMap<>();
		return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	@SafeVarargs
	public static <T> Predicate<T> distinctByKeys(Function<? super T, ?>... keyExtractors) {
		final Map<List<?>, Boolean> map = new ConcurrentHashMap<>();

		return t -> {
			final List<?> keys = Arrays.stream(keyExtractors).map(key -> key.apply(t)).collect(Collectors.toList());

			return map.putIfAbsent(keys, Boolean.TRUE) == null;
		};
	}

	public static boolean isValidEmail(String email) {
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);

		Matcher matcher = pattern.matcher(email);
		return matcher.matches();

	}

	/**
	 * Getting the hostName getHostName
	 */
	public static String getHostName() {
		if (StringUtils.isEmpty(HOSTNAME)) {
			setHostNameAndIp();
		}
		return HOSTNAME;
	}

	/**
	 * Getting the HostIp getHostIp
	 *
	 * @return
	 */
	public static String getHostIp() {
		if (StringUtils.isEmpty(HOSTIP)) {
			setHostNameAndIp();
		}
		return HOSTIP;
	}

	/**
	 * setHostNameAndIp
	 */
	private static void setHostNameAndIp() {
		try {
			InetAddress address = InetAddress.getLocalHost();
			HOSTIP = address.getHostAddress();
			HOSTNAME = address.getHostName();
			LOGGER.info("IP address: " + address.getHostAddress());
			LOGGER.info("Host name : " + address.getHostName());
		} catch (UnknownHostException uhEx) {
			LOGGER.warn("Could not find local address!");
		}
	}
	/**
	 *
	 * generateRefId
	 *
	 * @param productName
	 * @return
	 */
	public static String generateRefId(String productName) {
		if(productName.equalsIgnoreCase("IZO Internet WAN")) {
			productName = "IWAN";
		}
		int length = 10 - productName.length();
		String todayAsString = new SimpleDateFormat("ddMMyy").format(new Date());
		String alphaNumericRandom = RandomStringUtils.randomAlphanumeric(length).toUpperCase();
		return new StringBuilder(productName).append(todayAsString).append(alphaNumericRandom).toString().toUpperCase();
	}
	
	/**
	 *
	 * generateRefId
	 *
	 * @param productName
	 * @return
	 */
	public static String generateRefIdIzosdwan(String productName) {
		int length = 20 - productName.length();
		String todayAsString = new SimpleDateFormat("ddMMyy").format(new Date());
		String alphaNumericRandom = RandomStringUtils.randomAlphanumeric(length).toUpperCase();
		return new StringBuilder(productName).append(todayAsString).append(alphaNumericRandom).toString().toUpperCase();
	}
	
	/**
	 *
	 * generateServiceId
	 *
	 * @param productName
	 * @return
	 */
	public static String generateServiceIdByon(String productName) {
		int length = 15 - productName.length();
		String todayAsString = new SimpleDateFormat("MMdd").format(new Date());
		String numericRandom = RandomStringUtils.randomNumeric(length);
		return new StringBuilder(todayAsString).append(productName).append(numericRandom).toString().toUpperCase();
	}
	/**
	 *
	 * generatePasscode- Random Passcode generator
	 *
	 * @return
	 */
	public static String generatePasscode() {
		int length = 5;
		return new StringBuilder().append(RandomStringUtils.random(length, true, true)).append(CommonConstants.AT)
				.append(RandomStringUtils.random(length - 1, true, true)).toString();
	}

	/**
	 *
	 * @param input
	 * @return
	 * @throws TclCommonException
	 */
	public static String getMD5(String input) throws TclCommonException {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(input.getBytes());
			BigInteger number = new BigInteger(1, messageDigest);
			StringBuilder hashtext = new StringBuilder(number.toString(16));
			while (hashtext.length() < 32) {
				hashtext.insert(0, "0");
			}
			return hashtext.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new TclCommonException(e);
		}
	}

	/**
	 * 
	 * getMD5Code- It gets the last 8 digit of substring
	 * 
	 * @param sourceStr
	 * @return
	 */
	public static String getMD5Code(String sourceStr) {
		byte[] bytes = DigestUtils.md5(sourceStr);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			byte b = bytes[i];
			String hex = Integer.toHexString((int) 0x00FF & b);
			if (hex.length() == 1) {
				sb.append("0");
			}
			sb.append(hex);
		}
		return sb.toString().substring(sb.toString().length() - 8);
	}

	/**
	 * Method to hide/mask the password
	 *
	 * @param strText
	 * @param start
	 * @param end
	 * @param maskChar
	 * @return
	 * @throws Exception
	 */
	public static String maskPassword(String strText) {
		return strText.replaceAll(".*", "*");
	}

	/**
	 *
	 * @param strText
	 * @param maskChar
	 * @return
	 */
	public static String maskString(String strText) {
		return strText.replaceAll("(?<!^.?).(?!.?$)", "*");
	}

	public static String mask() {
		return CommonConstants.MASK_TXT;
	}

	/**
	 * convertObjectToJson- This method is used to convert the object To JSON
	 *
	 * @param object
	 * @return
	 * @throws TclCommonException
	 */
	public static <T> String convertObjectToJson(T object) throws TclCommonException {
		String json = null;
		try {
			json = objectMapper.writer().writeValueAsString(object);
		} catch (Exception e) {
			throw new TclCommonException(e);
		}
		return json;
	}

	public static <T> JsonNode toJsonNode(T obj) throws TclCommonException {
		try {
			return objectMapper.readTree(convertObjectToJson(obj));
		} catch (Exception e) {
			throw new TclCommonException(e);
		}
	}

	/**
	 * convertJsonToObject- This method is used to convert JSON to the given Object
	 *
	 * @param jsonString
	 * @param valueType
	 * @return
	 * @throws TclCommonException
	 */
	public static <T> T convertJsonToObject(String jsonString, Class<T> clazz) throws TclCommonException {
		T object = null;
		try {
			object = objectMapper.reader().forType(clazz).readValue(jsonString);
		} catch (Exception e) {
			throw new TclCommonException(e);
		}
		return object;
	}

	/**
	 *
	 * getSource-This method returns the username for the loggedin user
	 *
	 * @return String
	 */
	@SuppressWarnings("rawtypes")
	public static String getSource() {
		String principal = null;
		try {

			if (SecurityContextHolder.getContext().getAuthentication() == null
					|| SecurityContextHolder.getContext().getAuthentication().getPrincipal() == null) {
				LOGGER.warn("Could not get the logged in user, due to automated/async job ");
				String source = getRequestSource();
				if (source != null) {
					return source;
				}
				return "admin";
			}
			if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof KeycloakPrincipal) {
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
			return principal;
		} catch (Exception e) {
			LOGGER.warn("Could not get the logged in user, due to automated/async job.");
			String source = getRequestSource();
			if (source != null) {
				return source;
			}
			return "admin";
		}

	}

	/**
	 * getRequestSource
	 */
	private static String getRequestSource() {
		LOGGER.warn("This might be a Queue call , so trying to get from Request Handler scope");
		try {
			String source=SourceAdapter.getSource();
			LOGGER.info("Get Source have {} ==> {}", Thread.currentThread().getName(), source);
			return source;
		} catch (Exception e) {
			LOGGER.warn("Could not get the source in scope request");
		}
		return null;
	}

	/**
	 *
	 * disableSslVerification
	 */
	public static void disableSslVerification() {
		try {
			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[0];
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
					// Do Nothing
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
					// Do Nothing
				}
			} };

			// Install the all-trusting trust manager
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			// Do Nothing
		}
	}

	/**
	 * decode
	 *
	 * @throws UnsupportedEncodingException
	 */
	public static String base64Decode(String encodedString) throws UnsupportedEncodingException {
		byte[] asBytes = Base64.getDecoder().decode(encodedString);
		return new String(asBytes, "utf-8");
	}
	
	public static String base64encode(String plainString) throws UnsupportedEncodingException {
		byte[] asBytes = Base64.getEncoder().encode(plainString.getBytes());
		return new String(asBytes, "utf-8");
	}


	/*
	 * Method to get the argument annotated with @BaseArgument annotation
	 *
	 * @author Dinahar V
	 *
	 * @param JoinPoint
	 *
	 * @return Object
	 */
	public static Object getBaseArgument(JoinPoint joinPoint) {

		Object annotatedArg = null;
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		String methodName = signature.getMethod().getName();
		Class<?>[] parameterTypes = signature.getMethod().getParameterTypes();
		Annotation[][] annotations;
		try {
			annotations = joinPoint.getTarget().getClass().getMethod(methodName, parameterTypes)
					.getParameterAnnotations();
		} catch (Exception e) {
			throw new SoftException(e);
		}
		int i = 0;
		for (Object arg : joinPoint.getArgs()) {
			for (Annotation annotation : annotations[i]) {
				if (annotation.annotationType() == BaseArgument.class) {
					annotatedArg = arg;
					break;
				}
			}
			i++;
		}

		return annotatedArg;
	}

	public static String post(String url, String request) throws IOException {
		String json = request;
		URL iurl = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) iurl.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setRequestMethod("POST");
		OutputStream os = conn.getOutputStream();
		os.write(json.getBytes("UTF-8"));
		os.close();
		// read the response
		InputStream in = new BufferedInputStream(conn.getInputStream());
		StringWriter writer = new StringWriter();
		org.apache.commons.io.IOUtils.copy(in, writer, "UTF-8");
		in.close();
		conn.disconnect();
		return writer.toString();
	}

	/**
	 * invalidating all the cookies invalidateSession
	 *
	 * @param request
	 * @param response
	 */
	public static void invalidateSession(HttpServletRequest request, HttpServletResponse response) {

		// Request object to fetch the cookies
		Cookie[] cookies = request.getCookies();
		// Delete all the cookies
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				cookies[i].setValue(null);
				cookies[i].setMaxAge(0);
				response.addCookie(cookie);
			}
		}
	}

	/**
	 * getJsessionId
	 */
	public static String getJsessionId(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("JSESSIONID")) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	public static String getImpersonatedCookie(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("_pk_pmi.1.a7bf")) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	/**
	 * Gets the SessionState getSessionState
	 * 
	 * @param accessToken
	 * @return
	 */
	public static String getSessionState(String accessToken) {
		String sessionState = null;
		try {
			String[] split_string = accessToken.split("\\.");
			String base64EncodedBody = split_string[1];
			org.apache.commons.codec.binary.Base64 base64Url = new org.apache.commons.codec.binary.Base64(true);
			String body = new String(base64Url.decode(base64EncodedBody));
			JSONParser jsonParser = new JSONParser();
			JSONObject data = (JSONObject) jsonParser.parse(body);
			sessionState = (String) data.get("session_state");
		} catch (Exception e) {
			LOGGER.error("Error in parsing sessionState", e);
		}
		return sessionState;
	}

	/**
	 * isTestAccount
	 */
	public static boolean isTestAccount(String[] testEmails) {
		boolean isTestAccount = false;
		String userName = Utils.getSource();
		LOGGER.info("User name {}",userName);
		if (testEmails != null) {
			for (String email : testEmails) {
				if (userName != null && userName.contains(email)) {
					return true;
				}

			}
		}
		return isTestAccount;
	}

	/**
	 * convertEquals
	 */
	public static String convertEval(String inputValue) {
		if (StringUtils.isNotBlank(inputValue)) {
			inputValue = inputValue.replaceAll(CommonConstants.GREATER_EQUAL, "\u2265");
			inputValue = inputValue.replaceAll(CommonConstants.LESSTHAN_EQUAL, "\u2264");
		}
		return inputValue;
	}

	/**
	 * convertEqualsForHtml
	 */
	public static String convertEvalForHtml(String inputValue) {
		if (StringUtils.isNotBlank(inputValue)) {
			inputValue = inputValue.replaceAll(CommonConstants.GREATER_EQUAL, "");
			inputValue = inputValue.replaceAll(CommonConstants.LESSTHAN_EQUAL, "");
		}
		return inputValue;
	}

	/**
	 * Method to set precision
	 *
	 * @param value
	 * @param precision
	 * @return
	 */
	public static Double setPrecision(Double value, Integer precision) {
		Double result = 0.0;
		if (Objects.nonNull(value)) {
			if (precision == 2) {
				result = Math.round(value * 100.0) / 100.0;
				DecimalFormat df1 = new DecimalFormat(".##");
				result = Double.parseDouble(df1.format(result));
			} else if (precision == 4) {
				result = Math.round(value * 10000.0) / 10000.0;
				DecimalFormat df2 = new DecimalFormat(".####");
				result = Double.parseDouble(df2.format(result));
			}
		}
		return result;
	}

	/**
	 * used to encode the file encodeFileToBase64Binary
	 *
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String encodeFileToBase64Binary(MultipartFile file) throws IOException {

		byte[] bytes = file.getBytes();
		byte[] encoded = org.apache.commons.codec.binary.Base64.encodeBase64(bytes);
		String encodedString = new String(encoded);

		return encodedString;
	}
	
	/**
	 * @author KRUTSRIN A static class to initialize row and column count for excel
	 *         generation.
	 */
	public static class Context {
		Integer rowCount = 0;
		Integer columnCount = 0;
		Row row;
	}

	/**
	 * A Generic template to write an excel file.
	 *
	 * @author KRUTSRIN
	 * @param beanList
	 * @param sheetName
	 * @param excelFileName
	 * @param response
	 * @return byteArray
	 * @throws Exception
	 */
	public static <T> byte[] writeXLSXFile(List<T> beanList, String sheetName, String excelFileName, String message)
			throws TclCommonException, Exception {
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet(sheetName);

		// context class obj for initializing row and rowCount
		Context context = new Context();

		// Create a Font for styling header cells
		Font headerFont = wb.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 12);

		// Create a CellStyle with the font
		CellStyle headerCellStyle = wb.createCellStyle();
		headerCellStyle.setFont(headerFont);

		if (beanList == null || beanList.isEmpty()) {
			context.row = sheet.createRow(1);
			Cell cell = context.row.createCell(1);
			cell.setCellValue(message);
			cell.setCellStyle(headerCellStyle);
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 10));

		} else {

			Class<? extends Object> classz = beanList.get(0).getClass();
			List<List<String>> fieldNamesList = getFieldNamesForClass(classz);

			context.row = sheet.createRow(context.rowCount++);

			fieldNamesList.get(0).forEach(fieldName -> {
				Cell cell = context.row.createCell(context.columnCount++);
				cell.setCellValue(fieldName);
				cell.setCellStyle(headerCellStyle);
			});

			beanList.stream().forEach(t -> {
				context.row = sheet.createRow(context.rowCount++);
				context.columnCount = 0;
				fieldNamesList.get(1).stream().forEach(fieldName -> {
					Cell cell = context.row.createCell(context.columnCount);
					Method method = null;
					try {
						method = classz.getMethod(GETTER_PREFIX.concat(capitalize(fieldName)));
					} catch (NoSuchMethodException nme) {
						try {
							method = classz.getMethod(GETTER_PREFIX.concat(fieldName));
						} catch (NoSuchMethodException | SecurityException e) {
							LOGGER.error(ERR_IN_EXCEL_CREATION, ExceptionUtils.getStackTrace(e));
						}
					}
					Object value = null;
					try {
						value = method.invoke(t, (Object[]) null);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						LOGGER.error(ERR_IN_EXCEL_CREATION, ExceptionUtils.getStackTrace(e));
					}
					if (value != null) {
						if (value instanceof String) {
							cell.setCellValue((String) value);
						} else if (value instanceof Long) {
							cell.setCellValue((Long) value);
						} else if (value instanceof Integer) {
							cell.setCellValue((Integer) value);
						} else if (value instanceof Double) {
							cell.setCellValue((Double) value);
						} else {
							cell.setCellValue(String.valueOf(value));
						}
					}
					context.columnCount++;
				});
			});
		}
		// Resize all columns to fit the content size
		IntStream.range(0, context.columnCount).forEach(colCount -> {
			sheet.autoSizeColumn(colCount);
		});

		byte[] outArray = null;
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		wb.write(outByteStream);
		outArray = outByteStream.toByteArray();
		wb.close();
		outByteStream.flush();
		outByteStream.close();
		return outArray;
	}

	public static Long getCurrentTimeAsLong() {
		Long currentTime = (Long) (new Date().getTime() / 1000);
		return currentTime;
	}

	/**
	 * used to converst json string to json object
	 * 
	 * @param jsonString
	 * @return
	 */
	public static JSONObject convertJsonStingToJson(String jsonString) {
		JSONParser parser = new JSONParser();
		JSONObject json = null;
		try {
			json = (JSONObject) parser.parse(jsonString);
		} catch (ParseException e) {
			LOGGER.info("error parsing data");

		}
		return json;

	}

	// capitalize the first letter of the field name for retrieving value of the
	// field later
	private static String capitalize(String s) {
		if (s.length() == 0)
			return s;
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}

	// retrieve field names from a POJO class
	private static List<List<String>> getFieldNamesForClass(Class<?> clazz) throws Exception {

		List<List<String>> fieldLists = new ArrayList<List<String>>();
		List<String> captitalizedFieldNames = new ArrayList<String>();
		List<String> fieldNames = new ArrayList<String>();
		Field[] fields = clazz.getDeclaredFields();
		IntStream.range(0, fields.length).forEach(field -> {
			fieldNames.add(fields[field].getName());
			captitalizedFieldNames.add(StringUtils
					.join(StringUtils.splitByCharacterTypeCamelCase(fields[field].getName()), ' ').toUpperCase());
		});
		fieldLists.add(captitalizedFieldNames);
		fieldLists.add(fieldNames);
		return fieldLists;
	}

	/**
	 * Convert from json to object
	 *
	 * @param jsonStr
	 * @param valueType
	 * @param <T>
	 * @return
	 */
	public static <T> T fromJson(String jsonStr, TypeReference<T> valueType) {
		T object = null;
		try {
			object = new ObjectMapper().readValue(jsonStr, valueType);
		} catch (Exception e) {
			Throwables.propagate(e);
		}
		return object;
	}

	public static <T> T fromJson(String jsonStr, Class<T> valueType) {
		T object = null;
		try {
			object = new ObjectMapper().readValue(jsonStr, valueType);
		} catch (Exception e) {
			Throwables.propagate(e);
		}
		return object;
	}

	public static Timestamp addTimeToDate(Timestamp date, String time) {

		try {
			String actualTime = time.concat(":00:00");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String dateTime = sdf.format(date) + " " + actualTime;
			Timestamp requiredDate = Timestamp.valueOf(dateTime);
			return requiredDate;
		} catch (Exception e) {
			return date;
		}
	}

	public static String converTimeToString(Timestamp date) {
		try {
			String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
			return formattedDate;
		} catch (Exception e) {
			return null;
		}
	}

	public static <T> String convertObjectToXmlString(T obj, Class<T> clazz) throws JAXBException {
		Marshaller jaxbMarshaller = JAXBContext.newInstance(clazz).createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);

		StringWriter sw = new StringWriter();
		jaxbMarshaller.marshal(obj, sw);
		return sw.toString();
	}

	@SuppressWarnings("unchecked")
	public static <T> T convertXmlToObject(String xmlStr, Class<T> clazz) throws JAXBException, XMLStreamException {
		Unmarshaller unmarshaller = JAXBContext.newInstance(clazz).createUnmarshaller();

		XMLInputFactory xif = XMLInputFactory.newInstance();
		xif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
		xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);
		return (T) unmarshaller.unmarshal(xif.createXMLStreamReader(new StringReader(xmlStr)));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String getToken() {
		String accessToken = null;
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();
		if (null != request) {
			if ((request.getUserPrincipal()) != null) {
				KeycloakAuthenticationToken keycloakAuthenticationToken = (KeycloakAuthenticationToken) request
						.getUserPrincipal();
				if (keycloakAuthenticationToken != null && keycloakAuthenticationToken.getDetails() != null) {
					SimpleKeycloakAccount simpleKeycloakAccount = (SimpleKeycloakAccount) keycloakAuthenticationToken
							.getDetails();
					if (simpleKeycloakAccount != null && simpleKeycloakAccount.getPrincipal() != null) {
						KeycloakPrincipal<KeycloakSecurityContext> keycloakPrincipal = (KeycloakPrincipal) simpleKeycloakAccount
								.getPrincipal();
						if (keycloakPrincipal.getKeycloakSecurityContext() != null
								&& keycloakPrincipal.getKeycloakSecurityContext().getTokenString() != null) {
							accessToken = keycloakPrincipal.getKeycloakSecurityContext().getTokenString();
							LOGGER.info("User principal {} ", accessToken);
						}
					}
				}
			}
		}
		return accessToken;
	}

	public static String convertCamelCaseToTitleCase(String camelCaseString) {
		return StringUtils.capitalize(
				StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(camelCaseString), StringUtils.SPACE));
	}

	public static String convertStringToCamelCase(String string) {
		if(string != null && !string.isEmpty()) {
			String tmp = string.replaceAll("[^0-9a-zA-Z ]+", StringUtils.EMPTY);
			tmp = WordUtils.capitalizeFully(tmp).replace(StringUtils.SPACE, StringUtils.EMPTY);
			char c[] = tmp.toCharArray();
			c[0] = Character.toLowerCase(c[0]);
			tmp = new String(c);
			return tmp;
		}
		return string;
	}
	
	/**
	 * Convert to two decimal point
	 *
	 * @param value
	 * @return Double
	 */
	public static Double doRoundForTwoDecimal(Double value) {
		if (Objects.nonNull(value)) {
			value = Double.valueOf(roundFormat.format(value));
		}
		return value;
	}

	/**
	 * Get Start And end date of specific month
	 *
	 * @param month
	 * @return {@link Map<String, LocalDate>}
	 */
	public static Map<String, LocalDate> getStartAndEndDateOfSpecificMonth(Integer month) {
		Map<String, LocalDate> dates = new HashMap<>();
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		YearMonth yearMonth = YearMonth.of(year, month);
		dates.put(FIRST_DAY_OF_MONTH, yearMonth.atDay(1));
		dates.put(LAST_DAY_OF_MONTH, yearMonth.atEndOfMonth());
		return dates;
	}

	/**
	 * Method to generate task id
	 *
	 * @return {@link String}
	 */
	public static String generateTaskId() {
		LocalDate todaysDate = LocalDate.now();
		return String.valueOf(new StringBuilder(FT).append(CommonConstants.HYPHEN).append(todaysDate.getDayOfMonth())
				.append(CommonConstants.HYPHEN).append(todaysDate.getMonthValue()).append(CommonConstants.HYPHEN)
				.append(todaysDate.getYear()).append(CommonConstants.HYPHEN)
				.append(RandomStringUtils.randomNumeric(5)));
	}

	/**
	 * Method to generate task response id
	 *
	 * @return {@link String}
	 */
	public static String generateTaskResponseId() {
		LocalDate todaysDate = LocalDate.now();
		return String.valueOf(new StringBuilder(FRA).append(CommonConstants.HYPHEN).append(todaysDate.getDayOfMonth())
				.append(CommonConstants.HYPHEN).append(todaysDate.getMonthValue()).append(CommonConstants.HYPHEN)
				.append(todaysDate.getYear()).append(CommonConstants.HYPHEN)
				.append(RandomStringUtils.randomNumeric(5)));
	}
	
	
	
	/**
	 * Method to convert other data type to double
	 * @param charge
	 * @return
	 */
	public static Double getCharges(Object charge) {
		Double mfCharge = 0.0D;
		if (charge != null) {
			if (charge instanceof Double) {
				mfCharge = (Double) charge;
			} else if (charge instanceof String && !charge.equals("")) {
				mfCharge = new Double((String) charge);
			} else if (charge instanceof Long) {
				mfCharge = new Double((Long) charge);
			} else if (charge instanceof Integer) {
				mfCharge = new Double((Integer) charge);
			}
		}
		return mfCharge;
	}
	

	/**
	 * Extract Native Query Mapper into Object
	 *
	 * @param rowSupplier
	 * @param rowMapper
	 * @param <T>
	 * @return
	 */
	public static <T> List<T> mapRows(Supplier<List<Map<String, Object>>> rowSupplier,
			Function<Map<String, Object>, T> rowMapper) {
		Objects.requireNonNull(rowSupplier, "Row supplying function cannot be null");
		Objects.requireNonNull(rowMapper, "Row mapper cannot be null");
		List<Map<String, Object>> rows = rowSupplier.get();
		if (Objects.isNull(rows)) {
			return new ArrayList<>();
		}
		return rows.stream().map(rowMapper::apply).collect(Collectors.toList());
	}
	
    /**
     * Method to calculate number of IPs in subnet
     * @param ipPoolSize the input to be in format IPaddress/pool size in 2 digit
     * @return
     */
    public static String SubNetCalculator(String ipPoolSize) {
    	LOGGER.info("Inside Utils.SubNetCalculator to calculate IP count for {} ",ipPoolSize);
    	return "(IP="+ (int)Math.pow(2,(32-Integer.parseInt(StringUtils.substringAfterLast(ipPoolSize, "/")))) +")";
    }
    
    
    /**
     * Method to calculate number of IPs in subnet
     * @param ipPoolSize
     * @return
     */
    public static int SubNetIpCalculator(String ipPoolSize) {
    	LOGGER.info("Inside Utils.SubNetIpCalculator to calculate IP count for {} ",ipPoolSize);
    	return (int)Math.pow(2,(32-Integer.parseInt(StringUtils.substringAfterLast(ipPoolSize, "/")))) ;
    }
    
    public static Timestamp convertStringToDateTime(String stringToConvert) {
    	try {
    		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z");
    		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    		dateFormat1.parse(dateFormat1.format(dateFormat.parse(stringToConvert)));
    	} catch(java.text.ParseException e) {
    		
    	}
    	
    	
		return null;
    	
    }

    public static long convertStringToTimeStamp(String stringToConvert) {
    	long tm = 0;
    	try {
    		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//    		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z");
//    		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    		tm = dateFormat.parse(stringToConvert).getTime();
    	} catch(java.text.ParseException e) {
    		
    	}
    	
    	
		return tm;
    	
    }
    
    public static String convertTimeStampToString(Timestamp stringToConvert) {
    	String convertedTime = null;
    	try {
    		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//    		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z");
//    		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    		convertedTime = dateFormat.format(stringToConvert.getTime());
    	} catch(Exception e) {
    		LOGGER.info("Utils.convertTimeStampToString exception while converting timestamp to string", e);
    	}
		return convertedTime;
    	
    }
    
    
    public static int findDifferenceInDays(String startDate, String endDate) throws TclCommonException {
    	int daysBetween = 1;
    	try {
    		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        	Date formattedStartDate = dateFormat.parse(startDate);
        	Date formattedEndDate = dateFormat.parse(endDate);
        	long diff = Math.abs(formattedEndDate.getTime()- formattedStartDate.getTime());
//    		long diffSeconds = diff / 1000;
    		long daysBtw = (diff / (1000*60*60*24));
    		daysBetween = (int) daysBtw;
    	} catch(Exception p) {
    		throw new TclCommonException("Error while parsing schedule dates", p);
    	}
		return daysBetween;
    	
    }
    
    public static String generateFeasibilityTitle(String operationTitle, String quoteCode) {
    	String title = null;
    	try {
    		title = operationTitle+quoteCode+System.currentTimeMillis();
    	} catch(Exception e) {
    		LOGGER.error("generateFeasibilityTitle error", e);
    	}
		return title;
		
    	
    }
    
    public static Timestamp convertMillisStringToTimeStamp(String stringToConvert, int hrsToAdd) {
    	Timestamp time = null;
    	try {
    		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    		Calendar calendar = Calendar.getInstance();
    		calendar.setTime(dateFormat1.parse(dateFormat1.format(dateFormat.parse(stringToConvert))));
    		calendar.add(Calendar.HOUR_OF_DAY, hrsToAdd);
    		Date addedHrs = calendar.getTime();
    		time =  new Timestamp(addedHrs.getTime());
    		return time;
    	} catch(java.text.ParseException e) {
    		LOGGER.error("Utils.convertMillisStringToTimeStamp exception on adding hrs {} with given time {}",hrsToAdd, stringToConvert);
    	}
		return time;
    	
    }
    
    public static OffsetDateTime convertStringToOffSetTime(String timeWithOffset) throws java.text.ParseException {
    	OffsetDateTime odt = OffsetDateTime.parse(timeWithOffset);
    	LOGGER.info("Converted String {} to offsetTime {} ",timeWithOffset, odt);
		return odt;
    }
      
	public static String toJson(Object object) {
		String json = null;
		try {
			json = Utils.convertObjectToJson(object);
		} catch (Exception e) {
			Throwables.propagate(e);
		}
		return json;
	}

	
	public static boolean validateAlphaNumberic(String str) {
		// Regex to check string
		// contains only digits
		String regex = "^[0-9a-zA-Z]+$";

		// Compile the ReGex
		Pattern p = Pattern.compile(regex);

		// If the string is empty
		// return false
		if (str == null) {
			return false;
		}

		// Find match between given string
		// and regular expression
		// using Pattern.matcher()
		Matcher m = p.matcher(str);

		// Return if the string
		// matched the ReGex
		return m.matches();
	}

	public static String convertAsString(Object value) {
		return (value==null)||("null".equals(String.valueOf(value)))?"":String.valueOf(value);
	}
	
	/**
	 * 
	 * parseDouble - This is used to check the number and parse Double
	 * @param number
	 * @return
	 */
	public static Double parseDouble(String number) {
		if (NumberUtils.isCreatable(number)) {
			return Double.parseDouble(number);
		} else {
			return 0D;
		}
	}
	
	/**
	 * 
	 * removeUnicode
	 * 
	 * @param data
	 * @return
	 */
	public static String removeUnicode(String data) {
		if (StringUtils.isNotBlank(data)) {
			Pattern unicodeCharsPattern = Pattern.compile("\\\\u(\\p{XDigit}{4})");
			Matcher unicodeMatcher = unicodeCharsPattern.matcher(data);
			String cleanData = data;
			if (unicodeMatcher.find()) {
				cleanData = unicodeMatcher.replaceAll("");
			}
			return cleanData;
		} else {
			return data;
		}
	}
	  
}
