package com.tcl.dias.networkaugmentation.service;

import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.javaswift.joss.client.factory.AccountConfig;
import org.javaswift.joss.client.factory.AuthenticationMethod;
import org.javaswift.joss.client.impl.ClientImpl;
import org.javaswift.joss.headers.Header;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.model.StoredObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.TempUploadUrlInfo;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.ExceptionConstants;
import com.tcl.dias.common.utils.EncryptionUtil;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Service
public class AttachmentFileStorageService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AttachmentFileStorageService.class);

	@Value("${swift.api.authurl}")
	String swiftApiAuthUrl;

	@Value("${swift.api.username}")
	String swiftApiUserName;

	@Value("${swift.api.password}")
	String swiftApiPassword;

	@Value("${swift.api.tenantname}")
	String swiftApiTenantName;

	@Value("${swift.api.container}")
	String swiftApiContainer;

	@Value("${swift.api.hash.password}")
	String swiftApiHashPassword;

	@Value("${temp.upload.url.expiryWindow}")
	String tempUploadUrlExpiryWindow;

	@Value("${temp.download.url.expiryWindow}")
	String tempDownloadUrlExpiryWindow;

	@Autowired
	UserInfoUtils userInfoUtils;

	public TempUploadUrlInfo getTempUploadUrl(long expiryWindow, String customerCode, String customerLeCode,
			boolean isInternal) throws TclCommonException {
		try {
			Container container = getStorageContainer(customerCode, customerLeCode);
			String requestId = UUID.randomUUID().toString();
			String temporaryUrl;
			StoredObject storedObject = container.getObject(requestId);
			temporaryUrl = getTempUrl("PUT", storedObject, container.getAccount(), expiryWindow, isInternal);
			TempUploadUrlInfo response = new TempUploadUrlInfo();
			response.setRequestId(requestId);
			response.setTemporaryUploadUrl(temporaryUrl);
			response.setSecondsTillUrlExpiry(expiryWindow);
			response.setContainerName(container.getName());
			return response;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	public TempUploadUrlInfo getTempUploadUrl(String fileName, long expiryWindow, String productName)
			throws TclCommonException {
		try {
			Container container = getStorageContainer(productName);
			String requestId = UUID.randomUUID().toString() +"@"+ fileName;
			String temporaryUrl;
			StoredObject storedObject = container.getObject(requestId);
			temporaryUrl = getTempUrl("PUT", storedObject, container.getAccount(), expiryWindow, false);
			System.out.println("temporaryUrl : "+temporaryUrl);
			TempUploadUrlInfo response = new TempUploadUrlInfo();
			response.setRequestId(requestId);
			response.setTemporaryUploadUrl(temporaryUrl);
			response.setSecondsTillUrlExpiry(expiryWindow);
			response.setContainerName(container.getName());
			System.out.println("response : "+response.getTemporaryUploadUrl());
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	private String getTempUrl(String method, StoredObject storedObject, Account account, long expiryWindow,
			boolean isInternal) throws Exception {
		String path = storedObject.getPublicURL();
		path = path.substring(path.indexOf("/v1"));
		path = URLDecoder.decode(path, "UTF-8");
		Long expiresAt = account.getActualServerTimeInSeconds(expiryWindow);

		String plainTextToHash = method + "\n" + expiresAt + "\n" + path;
		String hash = EncryptionUtil.getHmacShaHash(plainTextToHash, account.getHashPassword()==null?swiftApiHashPassword:account.getHashPassword());
		String publicUrl = storedObject.getPublicURL();
		String remainder = publicUrl;
		if (!isInternal) {
			remainder = publicUrl.substring(publicUrl.indexOf("swift"), publicUrl.length());
		}
		LOGGER.info("Remainder :: {}", remainder);
		return remainder + "?temp_url_sig=" + hash + "&temp_url_expires=" + expiresAt;
	}

	public String getTempDownloadUrl(String fileName, long expiryWindowInSeconds, String containerUrl,
			boolean isInternal) throws TclCommonException {
		try {
			// Container container = getStorageContainer(customerCode, customerLeCode);
			Container container = getExistingStorageContainer(containerUrl);
			return getTempUrl("GET", container.getObject(fileName), container.getAccount(), expiryWindowInSeconds,
					isInternal);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	private Account getAccount() {
		AccountConfig config = new AccountConfig();
		config.setAuthUrl(swiftApiAuthUrl);
		config.setAuthenticationMethod(AuthenticationMethod.BASIC);
		config.setUsername(swiftApiUserName);
		config.setPassword(swiftApiPassword);
		config.setHashPassword(swiftApiHashPassword);
		config.setUseProxy(false);
		// config.setTenantName(swiftApiTenantName);
		// config.setHashPassword(storageContainer.getHashPassword());
		// Account account = new AccountFactory(config).setMock(true).createAccount();
		Account account = new ClientImpl(config).authenticate();

		// LOGGER.info("hash password {}", account.getHashPassword());
		return account;
	}

	private Container getStorageContainer(String customerCode, String customerLeCode) {
		Account account = getAccount();
		String path = swiftApiContainer + CommonConstants.HYPHEN + customerCode + CommonConstants.HYPHEN
				+ customerLeCode;
		Container myContainer = account.getContainer(path);
		if (!myContainer.exists()) {
			myContainer.create();
			myContainer.makePublic();
		}
		if(StringUtils.isNotBlank(myContainer.getContainerReadPermission()) || StringUtils.isNotBlank(myContainer.getcontainerWritePermission())){
			LOGGER.info("Read Permission is set so resetting it- {}",path);
			myContainer.setContainerRights("", "");
		}
		return myContainer;
	}
	
	private Container getStorageContainer(String productName) {
		Map<String, String> objsList = new HashMap<>();
		Account account = getAccount();
		String path = swiftApiContainer + CommonConstants.HYPHEN + productName;
		Container myContainer = account.getContainer(path);
		// if (!myContainer.list().isEmpty()) {
		// myContainer.list().stream().forEach(obj -> {
		// objsList.put(obj.getName(), obj.getPath());
		// LOGGER.info("Name :: {}, Path :: {}", obj.getName(), obj.getPath());
		// });
		// }
		if (!myContainer.exists()) {
			myContainer.create();
			myContainer.makePublic();
		}
		if(StringUtils.isNotBlank(myContainer.getContainerReadPermission()) || StringUtils.isNotBlank(myContainer.getcontainerWritePermission())){
			LOGGER.info("Read Permission is set so resetting it- {}",path);
			myContainer.setContainerRights("", "");
		}
		return myContainer;
	}

	private Container getExistingStorageContainer(String path) {
		LOGGER.info("Into existing storage container {}",path);
		Map<String, String> objsList = new HashMap<>();
		Account account = getAccount();
		LOGGER.info(" After Account {}",path);
		Container myContainer = account.getContainer(path);
		if (!myContainer.list().isEmpty()) {
			myContainer.list().stream().forEach(obj -> {
				objsList.put(obj.getName(), obj.getPath());
				LOGGER.debug("Name :: {}, Path :: {}", obj.getName(), obj.getPath());
			});
			if(StringUtils.isNotBlank(myContainer.getContainerReadPermission()) || StringUtils.isNotBlank(myContainer.getcontainerWritePermission())){
				LOGGER.info("Read Permission is set so resetting it- {}",path);
				myContainer.setContainerRights("", "");
			}
		}
		
		// if (!myContainer.exists()) {
		// myContainer.create();
		// myContainer.makePublic();
		// }
		return myContainer;
	}

	/**
	 * Upload GSC Specific generated PDF in Object Storage
	 *
	 * @param uniqueName
	 * @param inputStream
	 * @param customerCode
	 * @param customerLeCode
	 * @return {@link StoredObject}
	 * @throws TclCommonException
	 */
	public StoredObject uploadGscObject(String uniqueName, InputStream inputStream, String customerCode,
			String customerLeCode) throws TclCommonException {
		StoredObject storedObject = null;
		try {
			Container container = getStorageContainer(customerCode, customerLeCode);
			String requestId = UUID.randomUUID().toString() + "_" + uniqueName;
			storedObject = container.getObject(requestId);
			if (storedObject.exists()) {
				throw new TclCommonException(ExceptionConstants.OBJECT_ALREADY_EXISTS, ResponseResource.R_CODE_ERROR);
			}
			storedObject.uploadObject(inputStream);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return storedObject;

	}

	public StoredObject uploadObject(String uniqueName, InputStream inputStream, String customerCode,
			String customerLeCode) throws TclCommonException {
		StoredObject storedObject = null;
		try {
			Container container = getStorageContainer(customerCode, customerLeCode);
			String requestId = UUID.randomUUID().toString();
			storedObject = container.getObject(requestId);
			if (storedObject.exists()) {
				throw new TclCommonException(ExceptionConstants.OBJECT_ALREADY_EXISTS, ResponseResource.R_CODE_ERROR);
			}
			storedObject.uploadObject(inputStream);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return storedObject;

	}
	
	public StoredObject uploadObjectWithExten(String uniqueName, InputStream inputStream, String customerCode,
			String customerLeCode) throws TclCommonException {
		StoredObject storedObject = null;
		try {
			Container container = getStorageContainer(customerCode, customerLeCode);
			String requestId = UUID.randomUUID().toString()+""+uniqueName;
			storedObject = container.getObject(requestId);
			if (storedObject.exists()) {
				throw new TclCommonException(ExceptionConstants.OBJECT_ALREADY_EXISTS, ResponseResource.R_CODE_ERROR);
			}
			storedObject.uploadObject(inputStream);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return storedObject;

	}
	public StoredObject uploadObjectWithFileName(String fileName, InputStream inputStream, String customerCode,
			String customerLeCode) throws TclCommonException {
		StoredObject storedObject = null;
		try {
			Container container = getStorageContainer(customerCode, customerLeCode);
			String requestId = fileName;
			storedObject = container.getObject(requestId);
			if (storedObject.exists()) {
				throw new TclCommonException(ExceptionConstants.OBJECT_ALREADY_EXISTS, ResponseResource.R_CODE_ERROR);
			}
			storedObject.uploadObject(inputStream);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return storedObject;

	}

	public StoredObject uploadCountryObject(String uniqueName, InputStream inputStream) throws TclCommonException {
		StoredObject storedObject = null;
		try {
			Container container = getStorageContainer("GSIP-COUNTRY-FILES");
			String requestId = UUID.randomUUID().toString();
			storedObject = container.getObject(requestId);
			if (storedObject.exists()) {
				throw new TclCommonException(ExceptionConstants.OBJECT_ALREADY_EXISTS, ResponseResource.R_CODE_ERROR);
			}
			storedObject.uploadObject(inputStream);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return storedObject;

	}

	public Container createStorageContainer(String customerCode, String customerLeCode) {
		Account account = getAccount();
		String path = swiftApiContainer + CommonConstants.HYPHEN + customerCode + CommonConstants.HYPHEN
				+ customerLeCode;
		Container myContainer = account.getContainer(path);
		if (!myContainer.exists()) {
			myContainer.create();
			myContainer.makePublic();
		}
		if(StringUtils.isNotBlank(myContainer.getContainerReadPermission()) || StringUtils.isNotBlank(myContainer.getcontainerWritePermission())){
			LOGGER.info("Read Permission is set so resetting it- {}",path);
			myContainer.setContainerRights("", "");
		}
		return myContainer;
	}

	public Map<String, String> getObjectsFromContainer(String customerCode, String customerLeCode) {
		Map<String, String> objsList = new HashMap<>();
		Account account = getAccount();
		String path = swiftApiContainer + CommonConstants.HYPHEN + customerCode + CommonConstants.HYPHEN
				+ customerLeCode;
		Container myContainer = account.getContainer(path);
		myContainer.list().stream().forEach(obj -> {
			objsList.put(obj.getName(), obj.getPath());
			LOGGER.info("Name :: {}, Path :: {}", obj.getName(), obj.getPath());
		});
		return objsList;
	}

	public Map<String, String> getObjectsFromContainer(String productName) {
		Map<String, String> objsList = new HashMap<>();
		Account account = getAccount();
		String path = swiftApiContainer + CommonConstants.HYPHEN + productName;
		Container myContainer = account.getContainer(path);
		myContainer.list().stream().forEach(obj -> {
			objsList.put(obj.getName(), path);
			LOGGER.info("Name :: {}, Path :: {}", obj.getName(), obj.getPath());
		});
		return objsList;
	}

	public StoredObject uploadFiles(String fileName, String productName, InputStream inputStream)
			throws TclCommonException {
		StoredObject storedObject = null;
		try {
			Container container = getStorageContainer(productName);
			String requestId = UUID.randomUUID().toString() + fileName;
			storedObject = container.getObject(requestId);
			if (storedObject.exists()) {
				throw new TclCommonException(ExceptionConstants.OBJECT_ALREADY_EXISTS, ResponseResource.R_CODE_ERROR);
			}
			storedObject.uploadObject(inputStream);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return storedObject;
	}

}
