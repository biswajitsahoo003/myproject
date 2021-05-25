package com.tcl.dias.billing.service.v1;

import static com.tcl.dias.billing.constants.BillingConstants.CBF;
import static com.tcl.dias.billing.constants.BillingConstants.GBS;
import static com.tcl.dias.billing.constants.BillingConstants.PARTNER;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcl.dias.billing.beans.GBSInvoiceBean;
import com.tcl.dias.billing.beans.InvoiceSearchRequestBean;
import com.tcl.dias.billing.constants.BillingConstants;
import com.tcl.dias.billing.constants.ExceptionConstants;
import com.tcl.dias.billing.utils.BillingUtils;
import com.tcl.dias.common.beans.ClassificationBean;
import com.tcl.dias.common.beans.LeSapCodeResponse;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.beans.SapCodeRequest;
import com.tcl.dias.common.beans.LeSapCodeBean;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.redis.beans.PartnerDetail;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.utils.EncryptionUtil;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;

/**
 * This file contains the Component details for BillingService
 * 
 * @author Deepika Sivalingam
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class BillingService {

	@Value("${rabbitmq.customer.billing.account.number}")
	private String customerQueue;

	@Value("${rabbitmq.partner.billing.account.number}")
	private String partnerQueue;

	@Value("${cbf.server.path}")
	private String cbfPath;

	@Value("${cbf.table.filename}")
	private String colFileName;

	@Value("${cbf.table.filelink}")
	private String colFileLink;

	@Value("${gbs.invoices.root.path}")
	private String rootPath;

	@Value("${gbs.invoice.domain}")
	String domain;

	@Value("${gbs.invoice.username}")
	String userName;

	@Value("${gbs.invoice.password}")
	String password;

	@Autowired
	MQUtils mqUtils;

	@Value("${rabbitmq.le.sap.queue}")
	private String leSapQueue;

	@Value("${rabbitmq.le.cpny.sap.queue}")
	private String leCpnySapQueue;
	
	@Value("${rabbitmq.partner.le.sap.queue}")
	private String partnerLeSapQueue;

	@Autowired
	UserInfoUtils userInfoUtils;

	@Autowired
	@Qualifier("invoicesTemplate")
	JdbcTemplate invoicesTemplate;

	@Autowired
	@Qualifier("gbsInvoicesTemplate")
	JdbcTemplate gbsInvoicesTemplate;

	@Autowired
	@Value("${invoice.threshold}")
	Integer invoiceThreshold;
	
	@Autowired
	ExecutorService executor;
	
	@Value("${gbs.invoices.reports.api}")
	private String gbsInvoicesreports;
	
	@Value("${gbs.invoices.temp.location.api}")
	private String gbsLocation;
	
	@Value("${gbs.invoices.file.download.api}")
	private String gbsfileDownloadapi;
	
	@Value("${gbs.invoices.cdr.location.api}")
	private String gbsCdrLocation;
	
	

	private static final Logger LOGGER = LoggerFactory.getLogger(BillingService.class);
	
	private DateFormat cbfDateformatter = new SimpleDateFormat("yyyyMMdd");
	private DateFormat gbsDateformatter = new SimpleDateFormat("dd-MM-yyyy");

	@Value("${rabbitmq.partner.sap.code.name}")
	private String lePartnerSapNameQueue;

	@Value("${rabbitmq.partner.billing.code.name}")
	private String leBillingPartnerNameQueue;

	@Value("${rabbitmq.customer.sap.code.name}")
	private String leCustomerSapNameQueue;

	@Value("${rabbitmq.customer.billing.code.name}")
	private String leBillingCustomerNameQueue;
	
	@Autowired
	RestClientService restClientService;

	private static final String ACCOUNT_NO = "account_no";
	private static final String PARTNER_LEGAL_ENTITY_NAME = "partner_legal_entity_name";
	private static final String PARTNER_LEGAL_ENTITY_ID = "partner_legal_entity_id";
	private static final String CUSTOMER_LEGAL_ENTITY_NAME = "customer_legal_entity_name";
	private static final String CUSTOMER_LEGAL_ENTITY_ID = "customer_legal_entity_id";
	private static final String BILLING_ACCOUNT_IDS = "billingAccountIds";
	private static final String SAP_CODES = "sapCodes";
	private static final String CLASSIFICATION = "classification";
	private static final String ORG_NO = "org_no";
	private static final String SRC = "src";

	/**
	 * Get all the Active invoices for the current logged in user.
	 * 
	 * @return List<Map<String, Object>> containing all the Invoice details.
	 * @throws TclCommonException
	 */
	public List<Map<String, Object>> getInvoicesForUser(List<Integer> inputLEIds, String classification) throws TclCommonException {
		List<Map<String, Object>> invoices = null;
		try {
			Map<String, List<String>> userTypeBasedLeIds = getUserTypeBasedLeIds(inputLEIds);
			invoices = queryActiveInvoices(userTypeBasedLeIds.get(BILLING_ACCOUNT_IDS),
					userTypeBasedLeIds.get(SAP_CODES));
		} catch (Exception ex) {
			LOGGER.error(ExceptionConstants.ERROR_GET_ALL_INVOICES, ex);
			throw new TclCommonException(ExceptionConstants.ERROR_GET_ALL_INVOICES, ex, ResponseResource.R_CODE_ERROR);
		}

		/** For Partner Portal, filter the invoices by given classification mode */
		if (Objects.nonNull(classification)) {
			invoices = invoices.stream().filter(invoice -> classification.equalsIgnoreCase(String.valueOf(invoice.get(CLASSIFICATION))))
					.collect(Collectors.toList());
		}
		return invoices;
	}

	public List<Map<String, Object>> getInvoicesForPartnerUser(Integer partnerLeID,Integer customerLeId, String classification) throws TclCommonException {
		List<Map<String, Object>> invoices = null;
		List<Integer> inputLEIds= new ArrayList<>();
		try {
			if(partnerLeID!=null){
				inputLEIds.add(partnerLeID);
			}
			Map<String, List<String>> userTypeBasedLeIds = getUserTypeBasedPartnerLeIds(inputLEIds,partnerLeID);
			invoices = queryActivePartnerInvoices(userTypeBasedLeIds.get(BILLING_ACCOUNT_IDS),
					userTypeBasedLeIds.get(SAP_CODES),customerLeId);
		} catch (Exception ex) {
			LOGGER.error(ExceptionConstants.ERROR_GET_ALL_INVOICES, ex);
			throw new TclCommonException(ExceptionConstants.ERROR_GET_ALL_INVOICES, ex, ResponseResource.R_CODE_ERROR);
		}

		/** For Partner Portal, filter the invoices by given classification mode */
		if (Objects.nonNull(classification)) {
			invoices = invoices.stream().filter(invoice -> classification.equalsIgnoreCase(String.valueOf(invoice.get(CLASSIFICATION))))
					.collect(Collectors.toList());
		}
		return invoices;
	}

	private Map<String, List<String>> getUserTypeBasedPartnerLeIds(List<Integer> inputLeIds,Integer partnerLeID) throws TclCommonException {
		Map<String, List<String>> userBasedLeIds = new HashMap<>();
		if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			if(CollectionUtils.isEmpty(inputLeIds)) {
				userBasedLeIds.put(BILLING_ACCOUNT_IDS, getPartnerBillingAccountId());
				userBasedLeIds.put(SAP_CODES, getPartnerSapCodes());

			} else {
				if(partnerLeID!=null){
					userBasedLeIds.put(BILLING_ACCOUNT_IDS, getPartnerBillingAccountIdByPartnerLeId(partnerLeID));
					userBasedLeIds.put(SAP_CODES, getPartnerSapCodesByPartnerId(partnerLeID));
				}
				else {
					// Need to get customer le id information only for partner login
					userBasedLeIds.put(BILLING_ACCOUNT_IDS, getBillingAccountIds(inputLeIds));
					userBasedLeIds.put(SAP_CODES, getSapCodes(inputLeIds));
				}
			}
		} else {
			UserInformation userInfo = userInfoUtils.getUserInformation(Utils.getSource());
			if (Objects.isNull(userInfo)) {
				throw new TclCommonException(ExceptionConstants.ERROR_FETCHING_USERINFO);
			}

			List<CustomerDetail> customers = userInfo.getCustomers();
			if (Objects.isNull(customers)) {
				throw new TclCommonException(ExceptionConstants.ERROR_FECTHING_CUSTOMERS_FOR_USER);
			}

			List<Integer> leIds = customers.stream().map(cust -> cust.getCustomerLeId()).collect(Collectors.toList());
			if (inputLeIds != null && !inputLeIds.isEmpty() && leIds.contains(inputLeIds.get(0))) {
				leIds = inputLeIds;
			}

			userBasedLeIds.put(BILLING_ACCOUNT_IDS, getBillingAccountIds(leIds));
			userBasedLeIds.put(SAP_CODES, getSapCodes(leIds));
		}
		return userBasedLeIds;
	}

	private Map<String, List<String>> getUserTypeBasedLeIds(List<Integer> inputLeIds) throws TclCommonException {
		Map<String, List<String>> userBasedLeIds = new HashMap<>();
		if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			if(CollectionUtils.isEmpty(inputLeIds)) {
				userBasedLeIds.put(BILLING_ACCOUNT_IDS, getPartnerBillingAccountId());
				userBasedLeIds.put(SAP_CODES, getPartnerSapCodes());
			} else {
				// Need to get customer le id information only for partner login
				userBasedLeIds.put(BILLING_ACCOUNT_IDS, getBillingAccountIds(inputLeIds));
				userBasedLeIds.put(SAP_CODES, getSapCodes(inputLeIds));
			}
		} else {
			UserInformation userInfo = userInfoUtils.getUserInformation(Utils.getSource());
			if (Objects.isNull(userInfo)) {
				throw new TclCommonException(ExceptionConstants.ERROR_FETCHING_USERINFO);
			}

			List<CustomerDetail> customers = userInfo.getCustomers();
			if (Objects.isNull(customers)) {
				throw new TclCommonException(ExceptionConstants.ERROR_FECTHING_CUSTOMERS_FOR_USER);
			}

			List<Integer> leIds = customers.stream().map(cust -> cust.getCustomerLeId()).collect(Collectors.toList());
			if (inputLeIds != null && !inputLeIds.isEmpty() && leIds.contains(inputLeIds.get(0))) {
				leIds = inputLeIds;
			}

			userBasedLeIds.put(BILLING_ACCOUNT_IDS, getBillingAccountIds(leIds));
			userBasedLeIds.put(SAP_CODES, getSapCodes(leIds));
		}
		return userBasedLeIds;
	}

	private List<String> getPartnerBillingAccountId() {
		String partnerLeId = userInfoUtils.getPartnerDetails().stream().map(PartnerDetail::getPartnerLeId)
				.collect(Collectors.toList()).stream().map(String::valueOf).collect(Collectors.joining(","));
		String response;
		try {
			response = (String) mqUtils.sendAndReceive(partnerQueue, partnerLeId);
			List<String> billingAccNos = BillingUtils.fromJson(response,
					new TypeReference<List<String>>() {
					});
			LOGGER.info("Fetched the Billing Account No's: {}", billingAccNos.toString());
			return billingAccNos;
		} catch (TclCommonException e) {
			LOGGER.error(String.format(ExceptionConstants.ERROR_FETCHING_BILLING_IDS), e);
			throw new TclCommonRuntimeException(ExceptionConstants.ERROR_FETCHING_BILLING_IDS, e, ResponseResource.R_CODE_ERROR);
		}
	}

	private List<String> getPartnerBillingAccountIdByPartnerLeId(Integer partnerLegalEntityId) {
		String partnerLeId =partnerLegalEntityId.toString();
		String response;
		try {
			response = (String) mqUtils.sendAndReceive(partnerQueue, partnerLeId);
			List<String> billingAccNos = BillingUtils.fromJson(response,
					new TypeReference<List<String>>() {
					});
			LOGGER.info("Fetched the Billing Account No's: {}", billingAccNos.toString());
			return billingAccNos;
		} catch (TclCommonException e) {
			LOGGER.error(String.format(ExceptionConstants.ERROR_FETCHING_BILLING_IDS), e);
			throw new TclCommonRuntimeException(ExceptionConstants.ERROR_FETCHING_BILLING_IDS, e, ResponseResource.R_CODE_ERROR);
		}
	}

	private List<String> getPartnerSapCodes() {
		List<String> sapCodes = new ArrayList<>();
		try {
			List<Integer> partnerLeIds = userInfoUtils.getPartnerDetails().stream().map(PartnerDetail::getPartnerLeId)
					.collect(Collectors.toList());
			SapCodeRequest request = new SapCodeRequest();
			request.setType("SECS Code");
			request.setCustomerLeIds(partnerLeIds);
			String resp = (String) mqUtils.sendAndReceive(partnerLeSapQueue, Utils.convertObjectToJson(request));
			if (StringUtils.isNotBlank(resp)) {
				LeSapCodeResponse response = (LeSapCodeResponse) Utils.convertJsonToObject(resp, LeSapCodeResponse.class);
				if (response != null && !response.getLeSapCodes().isEmpty()) {
					response.getLeSapCodes().forEach(sap -> {
						sapCodes.add(sap.getCodeValue());
					});
				}
			}
			LOGGER.info("Fetch SAP Code :: {} ", sapCodes.toString());
		} catch (Exception e) {
			LOGGER.error(String.format(ExceptionConstants.ERROR_FETCHING_BILLING_IDS), e);
			throw new TclCommonRuntimeException(ExceptionConstants.ERROR_FETCHING_BILLING_IDS, e, ResponseResource.R_CODE_ERROR);
		}
		return sapCodes;
	}


	private List<String> getPartnerSapCodesByPartnerId(Integer partnerLeId) {
		List<String> sapCodes = new ArrayList<>();
		try {
			List<Integer> partnerLeIds = new ArrayList<>();
			partnerLeIds.add(partnerLeId);
			SapCodeRequest request = new SapCodeRequest();
			request.setType("SECS Code");
			request.setCustomerLeIds(partnerLeIds);
			String resp = (String) mqUtils.sendAndReceive(partnerLeSapQueue, Utils.convertObjectToJson(request));
			if (StringUtils.isNotBlank(resp)) {
				LeSapCodeResponse response = (LeSapCodeResponse) Utils.convertJsonToObject(resp, LeSapCodeResponse.class);
				if (response != null && !response.getLeSapCodes().isEmpty()) {
					response.getLeSapCodes().forEach(sap -> {
						sapCodes.add(sap.getCodeValue());
					});
				}
			}
			LOGGER.info("Fetch SAP Code :: {} ", sapCodes.toString());
		} catch (Exception e) {
			LOGGER.error(String.format(ExceptionConstants.ERROR_FETCHING_BILLING_IDS), e);
			throw new TclCommonRuntimeException(ExceptionConstants.ERROR_FETCHING_BILLING_IDS, e, ResponseResource.R_CODE_ERROR);
		}
		return sapCodes;
	}
	/**
	 * Fetch the requested invoice
	 *
	 * @param invoiceId
	 * @return
	 * @return
	 * @return {@InputStream} containing the invoice data.
	 * @throws TclCommonException
	 */
	public byte[] downloadInvoice1(Map<String, String> invoiceDetail) throws TclCommonException {
		if (Objects.isNull(invoiceDetail))
			throw new TclCommonException(ExceptionConstants.ERROR_FETCHING_INVOICE, ResponseResource.R_CODE_ERROR);

		String source = invoiceDetail.get("source");
		String filePath = invoiceDetail.get("filePath");
		String serviceType = invoiceDetail.get("serviceType");

		if (source.equals("GBS")) {

			return callGBS(filePath);
		} else if (source.equals("CBF")) {

			return callCBF(filePath,serviceType);
		} else {
			throw new TclCommonException(ExceptionConstants.INVALID_SOURCE_TYPE,
					new TclCommonException(ExceptionConstants.INVALID_SOURCE_TYPE), ResponseResource.R_CODE_ERROR);
		}

	}

	/**
	 * Fetch the requested invoice
	 *
	 * @param invoiceId
	 * @return
	 * @return
	 * @return {@InputStream} containing the invoice data.
	 * @throws TclCommonException
	 */
	public byte[] downloadInvoice(Map<String, String> invoiceDetail) throws TclCommonException {
		if (Objects.isNull(invoiceDetail))
			throw new TclCommonException(ExceptionConstants.ERROR_FETCHING_INVOICE, ResponseResource.R_CODE_ERROR);
		LOGGER.info("Entered in to downloadInvoice {}",invoiceDetail);
		String source = invoiceDetail.get("source");
		String filePath = invoiceDetail.get("filePath");
		String serviceType = invoiceDetail.get("serviceType");
		String fileName = invoiceDetail.get("fileName");
		Map<String,String> newFileName=new HashMap<>();
		String invoiceNo=invoiceDetail.get("invoiceNo");
		LOGGER.info("In downloadInvoice:::  source {},filePath {}, serviceType {}, invoiceNo {}", source,
				filePath, serviceType, invoiceNo);
		if (source.equals("GBS")) {
			byte[] gbsData=getGBSFile(invoiceNo,fileName,newFileName);
			if(newFileName.get("fileName")!=null && newFileName.get("fileName")!="" && !newFileName.get("fileName").isEmpty()) {
				invoiceDetail.put("fileName", newFileName.get("fileName"));
			}
			
			return gbsData;
		} else if (source.equals("CBF")) {

			return getFileDownloadApi(getGBSFileTempLocation(invoiceNo, BillingConstants.GENEVA));
		} else {
			throw new TclCommonException(ExceptionConstants.INVALID_SOURCE_TYPE,
					new TclCommonException(ExceptionConstants.INVALID_SOURCE_TYPE), ResponseResource.R_CODE_ERROR);
		}

	}
	/**
	 * Method to get the Byte Array of GBS File
	 * @param invoiceNo
	 * @param fileName
	 * @param newFileName 
	 * @return
	 * @throws TclCommonException
	 */

	private byte[] getGBSFile(String invoiceNo, String fileName, Map<String, String> newFileName) throws TclCommonException {
		LOGGER.info("Inside Get GBS File :: invoiceNo::{},fileName::{}", invoiceNo, fileName);
		String extension = FilenameUtils.getExtension(fileName);
		LOGGER.info("extension of fileName:: {}", extension);
		Map<String, String> fpath = getGBSInvoiceFilePath(invoiceNo, fileName);
		
		if (Objects.isNull(fpath.get("folderPath")) && Objects.isNull(fpath.get("fileName")))
			throw new TclCommonException(ExceptionConstants.ERROR_FETCHING_INVOICE, 
					ResponseResource.R_CODE_ERROR);
		String folderpathLocation = fpath.get("folderPath") + "/" + fpath.get("fileName");
		
		LOGGER.info("folderpathLoc:: {}", folderpathLocation);
		if ("z".equalsIgnoreCase(extension)) {
			String cdrLocation = getCDRFileLocation(fpath, fileName);
			if (Objects.isNull(cdrLocation))
				throw new TclCommonException(ExceptionConstants.ERROR_FETCHING_INVOICE, 
						ResponseResource.R_CODE_ERROR);
			folderpathLocation = fpath.get("folderPath") + "/" + cdrLocation;
		}
		
		String fileLocation = getGBSFileTempLocation(folderpathLocation, "gbs");
		if ("xml".equalsIgnoreCase(extension)) {
		checkingForXmlFileExtension(fileLocation,fileName,newFileName);
		 
		}
		if (Objects.isNull(fileLocation))
			throw new TclCommonException(ExceptionConstants.ERROR_FETCHING_INVOICE, 
					ResponseResource.R_CODE_ERROR);
		return getFileDownloadApi(fileLocation);
	}

	
	private void checkingForXmlFileExtension(String fileLocation, String fileName, Map<String, String> newFileName) {
		String newFileNameExtension=FilenameUtils.getExtension(fileLocation).toLowerCase();
		if(!("xml".equalsIgnoreCase(newFileNameExtension))) {
			fileName=fileName+"."+newFileNameExtension;
			newFileName.put("fileName", fileName);
		}
		
	}

	/**
	 * Get GBS Invoice Report details
	 * @param invoiceNo
	 * @param fileName
	 * @return
	 * @throws TclCommonException
	 */
	@SuppressWarnings("unchecked")
	private Map<String,String> getGBSInvoiceFilePath(String invoiceNo,String fileName) throws TclCommonException {
		Map<String,String> foldPath= new HashMap<>();
		try {
		LOGGER.info("Inside gbsInvoicesreports:: {}", gbsInvoicesreports);
		RestResponse response= restClientService.getWithQueryParam(gbsInvoicesreports, 
				constructQueryParams(invoiceNo), new HttpHeaders());
		LOGGER.info("Inside getGBSInvoiceFilePath Reports RestResponse ::{}", response);
		if (response.getStatus() == Status.SUCCESS) {
			List<Map<String, String>>  gbsinvoiceMap = (List<Map<String, String>>) Utils
					.convertJsonToObject(response.getData(), List.class);
			LOGGER.info("gbsinvoiceMap::{}",gbsinvoiceMap);
			 ObjectMapper objectMapper = new ObjectMapper();
			 if(Objects.isNull(gbsinvoiceMap))
				 throw new TclCommonException(ExceptionConstants.ERROR_FETCHING_INVOICE_DETAILS, ResponseResource.R_CODE_ERROR);
			gbsinvoiceMap.stream().forEach(map -> {
			GBSInvoiceBean gbsInvoiceBean = objectMapper.convertValue(map,
						GBSInvoiceBean.class);
				if (gbsInvoiceBean.getFileNm().equalsIgnoreCase(fileName) ) {
					foldPath.put("folderPath",gbsInvoiceBean.getFolderPath());
					foldPath.put("fileName",gbsInvoiceBean.getFileNm());
					foldPath.put("billingEntity", gbsInvoiceBean.getCpnyId());
					foldPath.put("sapCode", gbsInvoiceBean.getOrgNo());
				 }
				 });
		}
		}catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		LOGGER.info("Inside getGBSInvoiceFilePath Reports FolderPath Response ::{}", foldPath);
		return foldPath;
	}
	/**
	 * constructing query params for getGBSInvoiceFilePath
	 * @param invoiceNo
	 * @return
	 */

	private Map<String, String> constructQueryParams( String invoiceNo) {
		Map<String, String> queryParams= new HashMap<>();
		queryParams.put("customerName", "");
		queryParams.put("billingEntity", "");
		queryParams.put("SAPcode", "");
		queryParams.put("invoiceNo", invoiceNo);
		queryParams.put("invoiceType", "All");
		queryParams.put("fromDate", "");
		queryParams.put("toDate", "");
		queryParams.put("currentUser", "");
		return queryParams;
	}
	/**
	 * getGBSFileTempLocation by passing fileLocation from GBS Invoice Report details
	 * @param fileLoc
	 * @return
	 * @throws TclCommonException
	 */
	private String getGBSFileTempLocation(String fileLoc,String type) throws TclCommonException {
		LOGGER.info("Inside getGBSFileTempLocation fileLoc::{}", fileLoc);
		String fileLocation="";
		try {
		RestResponse response= restClientService.post(gbsLocation+"?invoiceNo="+fileLoc+"&type="+type,null);
		LOGGER.info("response for getGBSFileTempLocation response:: {}", response);
		if (response.getStatus() == Status.SUCCESS) {
			fileLocation=response.getData();
		}
		}catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		LOGGER.info("Output from getGBSFileTempLocation fileLoc::{}", fileLocation);
		return fileLocation;
	}
	/**
	 * getCDRFileLocation if the extension of file name is .z
	 * @param fpath
	 * @param fileName
	 * @return
	 * @throws TclCommonException
	 */
	
	private String getCDRFileLocation(Map<String, String> fpath, String fileName) throws TclCommonException {
		LOGGER.info("Inside getCDRFileLocation fpath::{}, fileName::{}",fpath,fileName);
		String cdrfileName="";
		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("billingEntity",fpath.get("billingEntity"));
		queryParams.put("SAPcode", fpath.get("sapCode"));
		queryParams.put("fileName", fileName);
		
		try {
		RestResponse response= restClientService.getWithQueryParam(gbsCdrLocation, 
				queryParams, new HttpHeaders());
		LOGGER.info("RestResponse for getCDRFileLocation:: response::{}",response);
		if (response.getStatus() == Status.SUCCESS) {
			cdrfileName=response.getData();
		}
		}catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return cdrfileName;
	}
	/**
	 * getFileDownloadApi for xml/pdf/z files by passing 
	 * input from getCDRFileLocation/getGBSFileTempLocation
	 * @param fileLocation
	 * @return
	 * @throws TclCommonException
	 */
	private byte[] getFileDownloadApi(String fileLocation) throws TclCommonException {
		LOGGER.info("Inside getFileDownloadApi fileLocation::{}",fileLocation);
		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("fileName", fileLocation);
		try {
			InputStream is = null;
			Client client = ClientBuilder.newClient();
			LOGGER.info("Fetching input stream with Url:: {}",gbsfileDownloadapi+"?fileName="+fileLocation);
			is = (InputStream) client.target(gbsfileDownloadapi+"?fileName="+fileLocation).request()
					.header("ContentType", "application/octet-stream").get().getEntity();
			LOGGER.info("After fetching the input stream");
			 return IOUtils.toByteArray(is);
		}
		 catch (Exception ex) {
				LOGGER.error(ExceptionConstants.ERROR_FETCHING_INVOICE, ex);
				throw new TclCommonException(ExceptionConstants.ERROR_FETCHING_INVOICE, ex, ResponseResource.R_CODE_ERROR);
			}
	}


	/**
	 * Fetch the requested invoice
	 *
	 * @param invoiceId
	 * @return
	 * @return
	 * @return {@InputStream} containing the invoice data.
	 * @throws TclCommonException
	 */
	public byte[] downloadInvoice(String source, String fileName, String filePath, String serviceType, String invoiceNo,
			Map<String, String> newFileName) throws TclCommonException {
		if (Objects.isNull(source) || Objects.isNull(fileName) || Objects.isNull(filePath))
			throw new TclCommonException(ExceptionConstants.ERROR_FETCHING_INVOICE, ResponseResource.R_CODE_ERROR);
		LOGGER.info("Entered in downloadInvoice source::{},fileName{},filePath",
				source,fileName,filePath);
		if (source.equals("GBS")) {
			byte[] gbsData= getGBSFile(invoiceNo, fileName,newFileName);
			return gbsData;
		} else if (source.equals("CBF")) {
			return getFileDownloadApi(getGBSFileTempLocation(invoiceNo, BillingConstants.GENEVA));
		} else {
			throw new TclCommonException(ExceptionConstants.INVALID_SOURCE_TYPE,
					new TclCommonException(ExceptionConstants.INVALID_SOURCE_TYPE), ResponseResource.R_CODE_ERROR);
		}

	}

	/**
	 * Fetch the requested invoice/cdr files
	 *
	 * @author KRUTHIKA S
	 *
	 * @param invoiceDetails
	 *            containing invoice/cdr filename, fileUrl
	 * @return {@InputStream} of the zip file containing all the fetched
	 *         invoices/cdr's .
	 * @throws TclCommonException
	 */
	public byte[] downloadInvoices(List<Map<String, String>> invoiceDetails) throws TclCommonException, IOException {
		if (Objects.isNull(invoiceDetails))
			throw new TclCommonException(ExceptionConstants.ERROR_FETCHING_INVOICE, ResponseResource.R_CODE_ERROR);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);
		try {
			Map<String, Future<byte[]>> futureMap = new HashMap<String, Future<byte[]>>();
			invoiceDetails.stream().forEach(invoice -> {
				String source = invoice.get(BillingConstants.SOURCE);
				String filePath = invoice.get(BillingConstants.FILEPATH);
				String fileName = invoice.get(BillingConstants.FILENAME);
				String serviceType = invoice.get("serviceType");
				String invoiceNo = invoice.get("invoiceNo");

				// if the number of invoices requested is less, don't use executor service
				// normal flow can be followed.
				if (invoiceDetails.size() <= invoiceDetails.size()) {
					normalLoad(source, filePath, fileName, zos, serviceType,invoiceNo);
				} else {
					// for bulk invoices
					Future<byte[]> futures = executor.submit(() -> {
						byte[] byteArr = null;
						if (source.equals(CBF)) {
							byteArr =  getFileDownloadApi(getGBSFileTempLocation(invoiceNo, BillingConstants.GENEVA));
						} else if (source.equals(BillingConstants.GBS)) {
							byteArr = getGBSFile(invoiceNo, fileName,new HashMap<>());
						}
						return byteArr;
					});
					futureMap.put(fileName, futures);
				}
			});
			// Iterate futureMap to get zip file
			futureMap.forEach((key, value) -> {
				ZipEntry entry = new ZipEntry(key);
				try {
					byte [] byteArr=value.get();
					entry.setSize(byteArr.length);
					zos.putNextEntry(entry);
					zos.write(byteArr);
				} catch (IOException | InterruptedException | ExecutionException e) {
					LOGGER.error(ExceptionConstants.ERROR_FETCHING_INVOICE, e);
				}
			});

		} finally {
			zos.closeEntry();
			zos.close();
		}
		return baos.toByteArray();
	}

	/**
	 * To get the Service id's and their details provided an invoiceId.
	 *
	 * @param invoiceNo, the Invoice number.
	 * @return {@List<Map<String, Object>>} contains ServiceId with details.
	 * @throws TclCommonException
	 */
	public List<Map<String, Object>> getAllServiceIds(String invoiceNo) throws TclCommonException {
		try {
			/*UserInformation userInfo = userInfoUtils.getUserInformation(Utils.getSource());
			if (Objects.isNull(userInfo))
				throw new TclCommonException(ExceptionConstants.ERROR_FETCHING_USERINFO);

			List<CustomerDetail> customers = userInfo.getCustomers();
			if (Objects.isNull(customers))
				throw new TclCommonException(ExceptionConstants.ERROR_FECTHING_CUSTOMERS_FOR_USER);

			List<String> billingAccIds = new ArrayList<>();
			List<String> sapCodes = new ArrayList<>();
			if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
				billingAccIds = getPartnerBillingAccountId(null);
				sapCodes = getPartnerSapCodes(null);
			} else {
				List<Integer> leIds = customers.stream().map(cust -> cust.getCustomerLeId()).collect(Collectors.toList());
				billingAccIds = getBillingAccountIds(leIds);
				sapCodes = getSapCodes(leIds);
			}*/

			return getServiceIds(invoiceNo, getUserTypeBasedLeIds(null).get(BILLING_ACCOUNT_IDS),
					getUserTypeBasedLeIds(null).get(SAP_CODES));
		} catch (Exception ex) {
			LOGGER.error(String.format(ExceptionConstants.ERROR_FETCHING_INVOICE_DETAILS), ex);
			throw new TclCommonRuntimeException(ExceptionConstants.ERROR_FETCHING_INVOICE_DETAILS, ex, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * API to search the invoice details using InvoiceNo or ServiceId or PO Number or Alias.
	 *
	 * @param invoiceNo, search by invoice no.
	 * @param serviceId, search by service id.
	 * @param poNumber, searchS by PO no.
	 * @param alias, search by alias.
	 * @param bean, InvoiceSearchRequestBean contains the input details.
	 * @return, contains ServiceId with the details.
	 * @throws TclCommonException
	 */
	public List<Map<String, Object>> searchByParam(String invoiceNo, String serviceId, String poNumber, String alias, InvoiceSearchRequestBean bean) throws TclCommonException {
		if (invoiceNo.isEmpty() && serviceId.isEmpty() && poNumber.isEmpty() && alias.isEmpty()) {
			throw new TclCommonException(com.tcl.dias.common.constants.ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
		}
		try {
			/*UserInformation userInfo = userInfoUtils.getUserInformation(Utils.getSource());
			if (Objects.isNull(userInfo))
				throw new TclCommonException(ExceptionConstants.ERROR_FETCHING_USERINFO);

			List<CustomerDetail> customers = userInfo.getCustomers();
			if (Objects.isNull(customers))
				throw new TclCommonException(ExceptionConstants.ERROR_FECTHING_CUSTOMERS_FOR_USER);

			List<String> billingAccIds = new ArrayList<>();
			List<String> sapCodes = new ArrayList<>();
			if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
				billingAccIds = getPartnerBillingAccountId(null);
				sapCodes = getPartnerSapCodes(null);
			} else {
				List<Integer> leIds = customers.stream().map(cust -> cust.getCustomerLeId()).collect(Collectors.toList());
				billingAccIds = getBillingAccountIds(leIds);
				sapCodes = getSapCodes(leIds);
			}*/

			String key = "";
			String value = "";
			if (!invoiceNo.isEmpty()) {
				key = "InvoiceNo";
				value = invoiceNo;
			} else if (!serviceId.isEmpty()) {
				key = "ServiceId";
				value = serviceId;
			} else if (!poNumber.isEmpty()) {
				key = "PoNo";
				value = poNumber;
			}
			List<Map<String, Object>> result = new ArrayList<>();
			result = searchByGivenKeyInCBF(key, value, getUserTypeBasedLeIds(null).get(BILLING_ACCOUNT_IDS),
					bean.getFromDate(), bean.getToDate());
			if(result.isEmpty() && !invoiceNo.isEmpty()) {
				result = searchByGivenKeyInGBS(invoiceNo, getUserTypeBasedLeIds(null).get(SAP_CODES),
						bean.getFromDate(), bean.getToDate());
			}
			return result;
		} catch (Exception ex) {
			LOGGER.error(String.format(ExceptionConstants.ERROR_FETCHING_INVOICES));
			throw new TclCommonRuntimeException(ExceptionConstants.ERROR_FETCHING_INVOICES, ex, ResponseResource.R_CODE_ERROR);
		}
	}

	private List<String> getBillingAccountIds(List<Integer> leIds) {
		List<String> billingAccNos = null;
		try {
			String ids = leIds.stream().map(x -> String.valueOf(x)).collect(Collectors.joining(","));
			String response = (String) mqUtils.sendAndReceive(customerQueue, ids );
			billingAccNos = BillingUtils.fromJson(response,
					new TypeReference<List<String>>() {
					});
			LOGGER.trace("Fetched the Billing Account No's: {}", billingAccNos);
			return billingAccNos;
		} catch (Exception e) {
			LOGGER.error(String.format(ExceptionConstants.ERROR_FETCHING_BILLING_IDS), e);
		}
		return billingAccNos;
	}

	/*private List<String> getSapCodes(List<Integer> leIds) throws TclCommonException {
		List<String> sapCodes = new ArrayList<>();
		SapCodeRequest request = new SapCodeRequest();
		request.setType("SECS Code");
		request.setCustomerLeIds(leIds);
		String resp = (String) mqUtils.sendAndReceive(leSapQueue, Utils.convertObjectToJson(request),
				MDC.get(CommonConstants.MDC_TOKEN_KEY));
		if (resp != null) {
			LeSapCodeResponse response = (LeSapCodeResponse) Utils.convertJsonToObject(resp, LeSapCodeResponse.class);
			if (response != null && !response.getLeSapCodes().isEmpty()) {
				response.getLeSapCodes().forEach(sap -> {
					sapCodes.add(sap.getCodeValue());
				});
			}
		}

		return sapCodes;
	}*/

	private List<String> getSapCodes(List<Integer> leIds) throws TclCommonException {
		SapCodeRequest request = new SapCodeRequest();
		List<String> sapcodes= new ArrayList<>();
		request.setType("SECS Code");
		request.setCustomerLeIds(leIds);
		String resp = (String) mqUtils.sendAndReceive(leCpnySapQueue, Utils.convertObjectToJson(request));
		sapcodes = BillingUtils.fromJson(resp,
				new TypeReference<List<String>>() {
				});
		return sapcodes;
	}

	private List<String> getLeSapCodes(List<Integer> leIds) throws TclCommonException {
		SapCodeRequest request = new SapCodeRequest();
		request.setType("SAP Code");
		request.setCustomerLeIds(leIds);
		String response = (String) mqUtils.sendAndReceive(leSapQueue, Utils.convertObjectToJson(request));
		LeSapCodeResponse LeSAPCodeslist = BillingUtils.fromJson(response,
				new TypeReference<LeSapCodeResponse>() {
				});
		List<String> sapCodes=LeSAPCodeslist.getLeSapCodes().stream().map(LeSapCodeBean::getCodeValue).collect(Collectors.toList());
		return sapCodes;
	}
	
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> queryActiveInvoices(List<String> billingAccIds, List<String> sapCodes)
			throws DataAccessException {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.YEAR, -2);
		c.add(Calendar.DAY_OF_MONTH, +1);
		Timestamp beforeDate = new Timestamp(c.getTime().getTime());
		List<Map<String, Object>> cbfInvoices = new ArrayList<>();
		List<Map<String, Object>> gbsInvoices = new ArrayList<>();
		
		LOGGER.info("USER TYPE:: {} ", userInfoUtils.getUserType().toString());
		LOGGER.info("Input BA id's :: {}", billingAccIds);
		LOGGER.info("Input SC id's :: {}", sapCodes);

		if(Objects.nonNull(billingAccIds) && !billingAccIds.isEmpty()) {
			String bas = billingAccIds.stream().map(billingAccountId -> "'" + billingAccountId + "'").collect(Collectors.joining(","));
			LOGGER.info("Billing Account Ids :: {}", bas);

			if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
				String queryForCBFInvoices = "select distinct 'CBF' src, BM.INVOICE_NO, PDT.po_no, " +
						"TRIM(BM.ACCOUNT_NO) ACCOUNT_NO, TRIM(BM.ACCOUNT_NO) ORG_NO, BM.BILL_DUE_DATE, BM.INVTOTAL, \r\n" + 
						"BM.billing_currency currency, BM.new_bill_date bill_date, \r\n" + 
						"BM.INVOICE_LINK, BM.INVOICE_FILE_NAME, BM.cdr_invoice_filename, BM.cdr_invoice_link, " +
						"BM.account_no account_no, BM.SERVICE_TYPE, \r\n" + 
						"PDT.opty_classification classification \r\n" + 
						"from \"CBF\".bill_master BM, \"CBF\".product_details PDT \r\n" + 
						"where BM.ID = PDT.Process_ID \r\n" + 
						"and BM.account_no = PDT.account_no \r\n" + 
						"and BM.INVOICE_NO = PDT.INVOICE_NO \r\n" + 
						"and BM.account_no in ("+bas+") \r\n" + 
						"and bill_type='Production' and isactive='Y' \r\n" + 
						"and BM.new_bill_date >= '"+cbfDateformatter.format(beforeDate)+"' order by BM.NEW_BILL_DATE DESC ";
				
				LOGGER.info("Executing query to fetch CBF invoice(Partner) ::\n {}\n", queryForCBFInvoices);
				cbfInvoices = invoicesTemplate.queryForList(queryForCBFInvoices);
				LOGGER.info("Result: CBF invoices(Partner) :: {}", cbfInvoices.toString());
			} else {
				String queryForCBFInvoices = "select DISTINCT 'CBF' src,BM.INVOICE_NO,TRIM(BM.SAP_CODE) SAP_CODE, TRIM(BM.SAP_CODE) ORG_NO,\r\n"
						+ "BM.BILL_DUE_DATE, BM.INVTOTAL,\r\n"
						+ "BM.billing_currency currency, BM.new_bill_date bill_date,\r\n"
						+ "BM.INVOICE_LINK, BM.INVOICE_FILE_NAME, BM.cdr_invoice_filename, BM.cdr_invoice_link,\r\n"
						+ "BM.account_no account_no, BM.customer_name, BM.SERVICE_TYPE,\r\n"
						+ "po_no,product_variant classification\r\n"
						+ "from \"CBF\".bill_master BM\r\n"
						+ "LEFT JOIN\r\n"
						+ "(select distinct process_id,account_no,invoice_no,string_agg(distinct pdt.po_no,'|') po_no,\r\n"
						+ "string_agg(distinct PDT.product_variant,'|')product_variant\r\n"
						+ "FROM \"CBF\".product_details pdt\r\n"
						+ "where PDT.account_no in ("+bas+") \r\n"
						+ "and invoice_no not like '%XX%'\r\n"
						+ "group by process_id,account_no,invoice_no) pd\r\n"
						+ "on bm.id = pd.process_id\r\n"
						+ "where bm.account_no in ("+bas+") \r\n"
						+ "and bm.invoice_no not like '%XX%'\r\n"
						+ "and bill_type='Production'\r\n"
						+ "and BM.new_bill_date >= '"+cbfDateformatter.format(beforeDate)+"' ";
				
				LOGGER.info("Executing query to fetch CBF invoice(Customer) ::\n {}\n", queryForCBFInvoices);
				cbfInvoices = invoicesTemplate.queryForList(queryForCBFInvoices);
				LOGGER.info("Result: CBF invoices(Customer) :: {}", cbfInvoices.toString());
			}
		} else {
			LOGGER.info("Billing Account ids empty for this customer");
		}
		
		if(Objects.nonNull(sapCodes) && !sapCodes.isEmpty()) {
			String orgNo = sapCodes.stream().map(sapCode -> "'" + sapCode + "'").collect(Collectors.joining(","));
			LOGGER.info("Sap Code Value :: {}", orgNo);
			
			if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
				String queryForGBSInvoices = "select distinct \r\n" + 
						"'GBS' src, '' po_no, \r\n" +
						" BM.full_invce_no invoice_no, \r\n" +
						" CONCAT(BM.CPNY_ID, BM.org_no) ACCOUNT_NO, BM.org_no ORG_NO, \r\n" +
						" BM.DUE_DT bill_due_date, \r\n" +
						" BM.amt invtotal, \r\n" +
						" BM.prfilcurrency currency, \r\n" +
						" BM.invce_dt bill_date, \r\n" +
						" BM.mdium_cd file_type, \r\n" +
						" BM.RAPIDCDR_ID || \r\n" +
						" BM.FILE_NM file_link, \r\n" +
						" BM.FILE_NM file_name \r\n" +
						" from \r\n" +
						" SEBS.GBS_INVCE_FOR_NGP BM \r\n" +
						" where \r\n" +
						" bm.invce_dt >= to_date('"+gbsDateformatter.format(beforeDate)+"', 'DD-MM-YYYY') \r\n" + 
						" and concat (bm.cpny_id, bm.org_no) in ("+orgNo+") \r\n" +
						" order by bm.invce_dt desc";
				
				LOGGER.info("Executing query to fetch GBS invoice(Partner) ::\n {}\n", queryForGBSInvoices);
				gbsInvoices = gbsInvoicesTemplate.queryForList(queryForGBSInvoices);
				LOGGER.info("Result: GBS invoices(Partner) :: {}", gbsInvoices.toString());
			} else {
				String queryForGBSInvoices = "select distinct \r\n" + 
						"'GBS' src, '' po_no, \r\n" + 
						" BM.full_invce_no invoice_no, \r\n" + 
						" CONCAT(BM.CPNY_ID, BM.org_no) SAP_CODE, BM.org_no ORG_NO, \r\n" + 
						" BM.DUE_DT bill_due_date, \r\n" + 
						" BM.amt invtotal, \r\n" + 
						" BM.prfilcurrency currency, \r\n" + 
						" BM.invce_dt bill_date, \r\n" + 
						" BM.mdium_cd file_type, \r\n" + 
						" BM.RAPIDCDR_ID|| \r\n" + 
						"'\\\\'||BM.FILE_NM file_link, \r\n" + 
						" BM.FILE_NM file_name \r\n" + 
						" from \r\n" + 
						" SEBS.GBS_INVCE_FOR_NGP BM \r\n" + 
						" where \r\n" + 
						" bm.invce_dt >= to_date('"+ gbsDateformatter.format(beforeDate)+"', 'DD-MM-YYYY') \r\n" + 
						" and concat (bm.cpny_id, bm.org_no) in ("+orgNo+") \r\n" + 
						" order by bm.invce_dt desc";
				
				LOGGER.info("Executing query to fetch GBS invoice(Customer) ::\n {}\n", queryForGBSInvoices);
				gbsInvoices = gbsInvoicesTemplate.queryForList(queryForGBSInvoices);
				LOGGER.info("Result: GBS invoices(Customer) :: {}", gbsInvoices.toString());
			}
		} else {
			LOGGER.info("SAP Codes not available for this customer");
		}
		
		
		Map<String, Map<String,Object>> merged = new HashMap<>();
		for (Map<String, Object> map : gbsInvoices) {
			String fileType = (String)map.remove("FILE_TYPE");
			String fileLink = (String)map.remove("FILE_LINK");
			String fileName = (String)map.remove("FILE_NAME");
			String key = String.valueOf(map.get("INVOICE_NO"));
			if(merged.containsKey(key)) {
				map = merged.get(key);
			} else {
				merged.put(key, map);
				map.put("src", map.remove("SRC"));
				map.put("invoice_no", map.remove("INVOICE_NO"));
				map.put("po_no", map.remove("PO_NO"));
				if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())){
					map.put("account_no", String.valueOf(map.remove("ACCOUNT_NO")));
				}
				else {
					map.put("sap_code", String.valueOf(map.remove("SAP_CODE")));
				}
				map.put("bill_due_date", map.remove("BILL_DUE_DATE"));
				map.put("invtotal", map.remove("INVTOTAL"));
				map.put("currency", map.remove("CURRENCY"));
				map.put(ORG_NO, map.remove("ORG_NO"));
				map.put("cdr_invoice_link", new LinkedList<String>());
				map.put("cdr_invoice_filename", new LinkedList<String>());
				map.put("cdr_invoice_filetype", new LinkedList<String>());
				map.put("bill_date", cbfDateformatter.format(new Date(((Timestamp)map.get("BILL_DATE")).getTime())));
				map.put(CLASSIFICATION, "");
			}
			
			if (fileType.equals("PAP")) {
				map.put("invoice_link", fileLink);
				map.put("invoice_file_name", fileName);
			} else if (fileType.equals("XML")) {
				map.put("xml_invoice_link", fileLink);
				map.put("xml_invoice_file_name", fileName);
			} else {
				((List<String>)map.get("cdr_invoice_link")).add(fileLink);
				((List<String>)map.get("cdr_invoice_filename")).add(fileName);
				((List<String>)map.get("cdr_invoice_filetype")).add(fileType);
			}
		}
		
		List<Map<String, Object>> finalInvoices = new LinkedList<Map<String,Object>>();
		finalInvoices.addAll(cbfInvoices);
		finalInvoices.addAll(merged.values());
		finalInvoices.sort((Map<String,Object> m1, Map<String,Object> m2) -> ((String)m2.get("bill_date")).compareTo((String)m1.get("bill_date")));
        
		if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType()))
            finalInvoices = collectPartnerInvoiceAccountIds(finalInvoices);
		
		LOGGER.info("Result: Final Invoices:: {}", finalInvoices.toString());
		return finalInvoices;
	}

	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> queryActivePartnerInvoices(List<String> billingAccIds, List<String> sapCodes,Integer customerLeId)
			throws DataAccessException {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.YEAR, -2);
		c.add(Calendar.DAY_OF_MONTH, +1);
		Timestamp beforeDate = new Timestamp(c.getTime().getTime());
		List<Map<String, Object>> cbfInvoices = new ArrayList<>();
		List<Map<String, Object>> gbsInvoices = new ArrayList<>();

		LOGGER.info("USER TYPE:: {} ", userInfoUtils.getUserType().toString());
		LOGGER.info("Input BA id's :: {}", billingAccIds);
		LOGGER.info("Input SC id's :: {}", sapCodes);

		if(Objects.nonNull(billingAccIds) && !billingAccIds.isEmpty()) {
			String bas = billingAccIds.stream().map(billingAccountId -> "'" + billingAccountId + "'").collect(Collectors.joining(","));
			LOGGER.info("Billing Account Ids :: {}", bas);

			if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
				String queryForCBFInvoices = "select distinct 'CBF' src, BM.INVOICE_NO, PDT.po_no,BM.customer_name ,BM.customer_id, " +
						"BM.circuit_id, BM.sap_code, BM.period_end_dt, BM.billmonth, BM.billing_frequency, \r\n"+
						"TRIM(BM.ACCOUNT_NO) ACCOUNT_NO, TRIM(BM.ACCOUNT_NO) ORG_NO, BM.BILL_DUE_DATE, BM.INVTOTAL, \r\n" +
						"BM.billing_currency currency, BM.new_bill_date bill_date, \r\n" +
						"BM.INVOICE_LINK, BM.INVOICE_FILE_NAME, BM.cdr_invoice_filename, BM.cdr_invoice_link, " +
						"BM.account_no account_no, \r\n" +
						"PDT.opty_classification classification \r\n" +
						"from \"CBF\".bill_master BM, \"CBF\".product_details PDT \r\n" +
						"where BM.ID = PDT.Process_ID \r\n" +
						"and BM.account_no = PDT.account_no \r\n" +
						"and BM.INVOICE_NO = PDT.INVOICE_NO \r\n" +
						"and BM.account_no in ("+bas+") \r\n" +
						"and bill_type='Production' and isactive='Y' and BM.SERVICE_TYPE !='TCLi' \r\n" +
						"and BM.new_bill_date >= '"+cbfDateformatter.format(beforeDate)+"' order by BM.NEW_BILL_DATE DESC ";

				LOGGER.info("Executing query to fetch CBF invoice(Partner) ::\n {}\n", queryForCBFInvoices);
				cbfInvoices = invoicesTemplate.queryForList(queryForCBFInvoices);
				LOGGER.info("Result: CBF invoices(Partner) :: {}", cbfInvoices.toString());
			} else {
				String queryForCBFInvoices = "select distinct 'CBF' src, BM.INVOICE_NO, PDT.po_no, \r\n" +
						"TRIM(BM.SAP_CODE) SAP_CODE, TRIM(BM.SAP_CODE) ORG_NO, BM.BILL_DUE_DATE, BM.INVTOTAL, \r\n" +
						"BM.billing_currency currency, BM.new_bill_date bill_date, \r\n" +
						"BM.INVOICE_LINK, BM.INVOICE_FILE_NAME, BM.cdr_invoice_filename, BM.cdr_invoice_link, " +
						"BM.account_no account_no, BM.customer_name, \r\n" +
						"PDT.product_variant classification \r\n" +
						"from \"CBF\".bill_master BM, \"CBF\".product_details PDT \r\n" +
						"where BM.ID = PDT.Process_ID \r\n" +
						"and BM.account_no = PDT.account_no \r\n" +
						"and BM.INVOICE_NO = PDT.INVOICE_NO \r\n" +
						"and BM.account_no in ("+bas+") \r\n" +
						"and bill_type='Production' and isactive='Y' and BM.SERVICE_TYPE !='TCLi' \r\n" +
						"and BM.new_bill_date >= '"+cbfDateformatter.format(beforeDate)+"' order by BM.NEW_BILL_DATE DESC ";

				LOGGER.info("Executing query to fetch CBF invoice(Customer) ::\n {}\n", queryForCBFInvoices);
				cbfInvoices = invoicesTemplate.queryForList(queryForCBFInvoices);
				LOGGER.info("Result: CBF invoices(Customer) :: {}", cbfInvoices.toString());
			}
		} else {
			LOGGER.info("Billing Account ids empty for this customer");
		}

		if(Objects.nonNull(sapCodes) && !sapCodes.isEmpty()) {
			String orgNo = sapCodes.stream().map(sapCode -> "'" + sapCode + "'").collect(Collectors.joining(","));
			LOGGER.info("Sap Code Value :: {}", orgNo);

			if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
				String queryForGBSInvoices = "select distinct \r\n" +
						"'GBS' src, '' po_no, \r\n" +
						" BM.full_invce_no invoice_no, \r\n" +
						" CONCAT(BM.CPNY_ID, BM.org_no) ACCOUNT_NO, BM.org_no ORG_NO, \r\n" +
						" BM.DUE_DT bill_due_date, \r\n" +
						" BM.amt invtotal, \r\n" +
						" BM.prfilcurrency currency, \r\n" +
						" BM.invce_dt bill_date, \r\n" +
						" BM.mdium_cd file_type, \r\n" +
						" BM.RAPIDCDR_ID || \r\n" +
						" BM.FILE_NM file_link, \r\n" +
						" BM.FILE_NM file_name \r\n" +
						" from \r\n" +
						" SEBS.GBS_INVCE_FOR_NGP BM \r\n" +
						" where \r\n" +
						" bm.invce_dt >= to_date('"+gbsDateformatter.format(beforeDate)+"', 'DD-MM-YYYY') \r\n" +
						" and concat (bm.cpny_id, bm.org_no) in ("+orgNo+") \r\n" +
						" order by bm.invce_dt desc";

				LOGGER.info("Executing query to fetch GBS invoice(Partner) ::\n {}\n", queryForGBSInvoices);
				gbsInvoices = gbsInvoicesTemplate.queryForList(queryForGBSInvoices);
				LOGGER.info("Result: GBS invoices(Partner) :: {}", gbsInvoices.toString());
			} else {
				String queryForGBSInvoices = "select distinct \r\n" +
						"'GBS' src, '' po_no, \r\n" +
						" BM.full_invce_no invoice_no, \r\n" +
						" CONCAT(BM.CPNY_ID, BM.org_no) SAP_CODE, BM.org_no ORG_NO, \r\n" +
						" BM.DUE_DT bill_due_date, \r\n" +
						" BM.amt invtotal, \r\n" +
						" BM.prfilcurrency currency, \r\n" +
						" BM.invce_dt bill_date, \r\n" +
						" BM.mdium_cd file_type, \r\n" +
						" BM.RAPIDCDR_ID|| \r\n" +
						"'\\\\'||BM.FILE_NM file_link, \r\n" +
						" BM.FILE_NM file_name \r\n" +
						" from \r\n" +
						" SEBS.GBS_INVCE_FOR_NGP BM \r\n" +
						" where \r\n" +
						" bm.invce_dt >= to_date('"+ gbsDateformatter.format(beforeDate)+"', 'DD-MM-YYYY') \r\n" +
						" and concat (bm.cpny_id, bm.org_no) in ("+orgNo+") \r\n" +
						" order by bm.invce_dt desc";

				LOGGER.info("Executing query to fetch GBS invoice(Customer) ::\n {}\n", queryForGBSInvoices);
				gbsInvoices = gbsInvoicesTemplate.queryForList(queryForGBSInvoices);
				LOGGER.info("Result: GBS invoices(Customer) :: {}", gbsInvoices.toString());
			}
		} else {
			LOGGER.info("SAP Codes not available for this customer");
		}


		Map<String, Map<String,Object>> merged = new HashMap<>();
		for (Map<String, Object> map : gbsInvoices) {
			String fileType = (String)map.remove("FILE_TYPE");
			String fileLink = (String)map.remove("FILE_LINK");
			String fileName = (String)map.remove("FILE_NAME");
			String key = String.valueOf(map.get("INVOICE_NO"));
			if(merged.containsKey(key)) {
				map = merged.get(key);
			} else {
				merged.put(key, map);
				map.put("src", map.remove("SRC"));
				map.put("invoice_no", map.remove("INVOICE_NO"));
				map.put("po_no", map.remove("PO_NO"));
				if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())){
					map.put("account_no", String.valueOf(map.remove("ACCOUNT_NO")));
				}
				else {
					map.put("sap_code", String.valueOf(map.remove("SAP_CODE")));
				}
				map.put("bill_due_date", map.remove("BILL_DUE_DATE"));
				map.put("invtotal", map.remove("INVTOTAL"));
				map.put("currency", map.remove("CURRENCY"));
				map.put(ORG_NO, map.remove("ORG_NO"));
				map.put("cdr_invoice_link", new LinkedList<String>());
				map.put("cdr_invoice_filename", new LinkedList<String>());
				map.put("cdr_invoice_filetype", new LinkedList<String>());
				map.put("bill_date", cbfDateformatter.format(new Date(((Timestamp)map.get("BILL_DATE")).getTime())));
				map.put(CLASSIFICATION, "");
			}

			if (fileType.equals("PAP")) {
				map.put("invoice_link", fileLink);
				map.put("invoice_file_name", fileName);
			} else if (fileType.equals("XML")) {
				map.put("xml_invoice_link", fileLink);
				map.put("xml_invoice_file_name", fileName);
			} else {
				((List<String>)map.get("cdr_invoice_link")).add(fileLink);
				((List<String>)map.get("cdr_invoice_filename")).add(fileName);
				((List<String>)map.get("cdr_invoice_filetype")).add(fileType);
			}
		}

		List<Map<String, Object>> finalInvoices = new LinkedList<Map<String,Object>>();
		finalInvoices.addAll(cbfInvoices);
		finalInvoices.addAll(merged.values());
		finalInvoices.sort((Map<String,Object> m1, Map<String,Object> m2) -> ((String)m2.get("bill_date")).compareTo((String)m1.get("bill_date")));

		if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			finalInvoices = collectPartnerInvoiceAccountIds(finalInvoices);
			if(customerLeId!=null) {
				List<Map<String, Object>> finalPartnerInvoices = new LinkedList<Map<String,Object>>();
				try {
					List<Integer> customerLeIds = new ArrayList<>();
					customerLeIds.add(customerLeId);
					List<String> listOfSapCodes = getLeSapCodes(customerLeIds);
					//customerLeId

					for (String sapcodes : listOfSapCodes) {
						List<Map<String, Object>> filteredinvoicelist = new ArrayList<>();
						filteredinvoicelist = finalInvoices.stream().filter(invoice -> invoice.get("customer_id").equals(sapcodes)).collect(Collectors.toList());
						finalPartnerInvoices.addAll(filteredinvoicelist);
					}
					return finalPartnerInvoices;
				} catch (TclCommonException e) {
					LOGGER.warn("Exception Occured :: {}", e);
				}
			}

		}

		LOGGER.info("Result: Final Invoices:: {}", finalInvoices.toString());
		return finalInvoices;
	}

	private List<Map<String, Object>> collectPartnerInvoiceAccountIds(List<Map<String, Object>> finalInvoices) {
		Set<String> sapCodes = new HashSet<>();
		Set<String> billingAccIds = new HashSet<>();
		for (Map<String, Object> invoice : finalInvoices) {
			if (CBF.equalsIgnoreCase(String.valueOf(invoice.get(SRC)))) {
					billingAccIds.add((String.valueOf(invoice.get(ACCOUNT_NO))));
			} else if (GBS.equalsIgnoreCase(String.valueOf(invoice.get(SRC)))) {
				sapCodes.add(String.valueOf(invoice.get(ORG_NO)));
			}
		}
		LOGGER.info("Sap Code :: {}", sapCodes.toString());
		LOGGER.info("Billing Code :: {}", billingAccIds.toString());
		if (!CollectionUtils.isEmpty(sapCodes)) {
			finalInvoices = getPartnerLeNameBySapIds(sapCodes, finalInvoices);
		}
		if (!CollectionUtils.isEmpty(billingAccIds)) {
			finalInvoices = getPartnerLeNameByBillingIds(billingAccIds, finalInvoices);
		}
		return finalInvoices;
	}


	private List<Map<String, Object>> collectCustomerInvoiceAccountIds(List<Map<String, Object>> finalInvoices) {
		Set<String> sapCodes = new HashSet<>();
		Set<String> billingAccIds = new HashSet<>();
		for (Map<String, Object> invoice : finalInvoices) {
			if (CBF.equalsIgnoreCase(String.valueOf(invoice.get(SRC)))) {
				billingAccIds.add((String.valueOf(invoice.get(SAP_CODES))));
			} else if (GBS.equalsIgnoreCase(String.valueOf(invoice.get(SRC)))) {
				sapCodes.add(String.valueOf(invoice.get(ORG_NO)));
			}
		}
		LOGGER.info("Sap Code :: {}", sapCodes.toString());
		LOGGER.info("Billing Code :: {}", billingAccIds.toString());
		if (!CollectionUtils.isEmpty(sapCodes)) {
			finalInvoices = getCustomerLeNameBySapIds(sapCodes, finalInvoices);
		}
		if (!CollectionUtils.isEmpty(billingAccIds)) {
			finalInvoices = getCustomerLeNameByBillingIds(billingAccIds, finalInvoices);
		}
		return finalInvoices;
	}

	private List<Map<String, Object>> getPartnerLeNameBySapIds(Set<String> sapCodes, List<Map<String, Object>> finalInvoices) {
		try {
			String response = (String) mqUtils.sendAndReceive(lePartnerSapNameQueue, Utils.convertObjectToJson(sapCodes));
			LOGGER.info("Partner Queue Response :: {}", response);
			if (StringUtils.isNotBlank(response)) {
				List<ClassificationBean> classificationBeans = BillingUtils.fromJson(response,
						new TypeReference<List<ClassificationBean>>() {
						});

				finalInvoices.forEach(invoice -> {
					String sapCode = String.valueOf(invoice.get(ORG_NO));
					Optional<ClassificationBean> bean = classificationBeans.stream().filter(classificationBean -> classificationBean.getSapCode().equalsIgnoreCase(sapCode)).findFirst();
					if (bean.isPresent()) {
						invoice.put(PARTNER_LEGAL_ENTITY_NAME, bean.get().getPartnerLeName());
						invoice.put(PARTNER_LEGAL_ENTITY_ID, bean.get().getPartnerLeId());
					}
				});
			}
		} catch (TclCommonException e) {
			LOGGER.warn("Exception Occured :: {}", e);
		}

		return finalInvoices;
	}

	private List<Map<String, Object>> getCustomerLeNameBySapIds(Set<String> sapCodes, List<Map<String, Object>> finalInvoices) {
		try {
			String response = (String) mqUtils.sendAndReceive(leCustomerSapNameQueue, Utils.convertObjectToJson(sapCodes));
			LOGGER.info("Partner Queue Response :: {}", response);
			if (StringUtils.isNotBlank(response)) {
				List<ClassificationBean> classificationBeans = BillingUtils.fromJson(response,
						new TypeReference<List<ClassificationBean>>() {
						});

				finalInvoices.forEach(invoice -> {
					String sapCode = String.valueOf(invoice.get(ORG_NO));
					Optional<ClassificationBean> bean = classificationBeans.stream().filter(classificationBean -> classificationBean.getSapCode().equalsIgnoreCase(sapCode)).findFirst();
					if (bean.isPresent()) {
						invoice.put(CUSTOMER_LEGAL_ENTITY_NAME, bean.get().getCustomerLegalName());
						invoice.put(CUSTOMER_LEGAL_ENTITY_ID, bean.get().getCustomerLeId());
					}
				});

			}
		} catch (TclCommonException e) {
			LOGGER.warn("Exception Occured :: {}", e);
		}

		return finalInvoices;
	}

	private List<Map<String, Object>> getPartnerLeNameByBillingIds(Set<String> sapCodes, List<Map<String, Object>> finalInvoices) {
		try {
			String response = response = (String) mqUtils.sendAndReceive(leBillingPartnerNameQueue, Utils.convertObjectToJson(sapCodes));
			LOGGER.info("Queue Response :: {}", response);
			if (StringUtils.isNotBlank(response)) {
				List<ClassificationBean> classificationBeans = BillingUtils.fromJson(response,
						new TypeReference<List<ClassificationBean>>() {
						});

				finalInvoices.forEach(invoice -> {
					String sapCode = String.valueOf(invoice.get(ACCOUNT_NO));
					Optional<ClassificationBean> bean = classificationBeans.stream().filter(classificationBean -> classificationBean.getSapCode().equalsIgnoreCase(sapCode)).findFirst();
					if (bean.isPresent()) {
						invoice.put(PARTNER_LEGAL_ENTITY_NAME, bean.get().getPartnerLeName());
						invoice.put(PARTNER_LEGAL_ENTITY_ID, bean.get().getPartnerLeId());
					}
				});

			}
		} catch (TclCommonException e) {
			LOGGER.warn("Exception Occured :: {}", e);
		}

		return finalInvoices;
	}

	private List<Map<String, Object>> getCustomerLeNameByBillingIds(Set<String> sapCodes, List<Map<String, Object>> finalInvoices) {
		try {
			String response = response = (String) mqUtils.sendAndReceive(leBillingCustomerNameQueue, Utils.convertObjectToJson(sapCodes));
			LOGGER.info("Queue Response :: {}", response);
			if (StringUtils.isNotBlank(response)) {
				List<ClassificationBean> classificationBeans = BillingUtils.fromJson(response,
						new TypeReference<List<ClassificationBean>>() {
						});

				finalInvoices.forEach(invoice -> {
					String sapCode = String.valueOf(invoice.get(SAP_CODES));
					Optional<ClassificationBean> bean = classificationBeans.stream().filter(classificationBean -> classificationBean.getSapCode().equalsIgnoreCase(sapCode)).findFirst();
					if (bean.isPresent()) {
						invoice.put(CUSTOMER_LEGAL_ENTITY_NAME, bean.get().getCustomerLegalName());
						invoice.put(CUSTOMER_LEGAL_ENTITY_ID, bean.get().getCustomerLeId());
					}
				});

			}
		} catch (TclCommonException e) {
			LOGGER.warn("Exception Occured :: {}", e);
		}

		return finalInvoices;
	}

	private List<Map<String, Object>> getServiceIds(String invoiceNo, List<String> billingAccIds, List<String> sapCodes) throws TclCommonException {
		BillingUtils.validateSqlInjectionInput(invoiceNo);//handle sql injection
		String bas = billingAccIds.stream().map(ba -> "'" + ba + "'").collect(Collectors.joining(","));
		Calendar c = Calendar.getInstance();
		c.add(Calendar.YEAR, -2);
		c.add(Calendar.DAY_OF_MONTH, +1);
		Timestamp beforeDate = new Timestamp(c.getTime().getTime());

		String cbfQuery = "select distinct\n" + 
				"	bm.invoice_no as InvoiceNo, pdt.service_id as ServiceId, \n" + 
				"	decode(pdt.Tax_details,'Tax exempt','Tax-Exempt','Tax-Exempt') as TaxDetails, \n" + 
				"	bm.nettotal as NetTotal, bm.Tax_total as TaxTotal, bm.Invtotal as InvoiceTotal\n" + 
				"from\n" + 
				"	\"CBF\".product_details pdt, \"CBF\".bill_master bm  \n" + 
				"where\n" + 
				"	bm.id = pdt.process_id\n" + 
				"	and BM.account_no = PDT.account_no\n" + 
				"	and BM.INVOICE_NO = PDT.INVOICE_NO\n" + 
				"   and BM.account_no in (" + bas + ") \n" +
				"	and bm.bill_type='Production' and isactive='Y' and bm.SERVICE_TYPE !='TCLi'   \n" + 
				"	and BM.new_bill_date >= " + cbfDateformatter.format(beforeDate) + " \n" + 
				"	and pdt.invoice_no = '" + invoiceNo + "'";
		try {
			LOGGER.debug("Query for: Get ServiceIds: {}", cbfQuery);
			return invoicesTemplate.queryForList(cbfQuery);
		} catch (DataAccessException ex) {
			LOGGER.error(ExceptionConstants.ERROR_FETCHING_INVOICE, ex);
			throw new TclCommonException(ExceptionConstants.ERROR_FETCHING_INVOICE, ex, ResponseResource.R_CODE_ERROR);
		}
	}

	// For CBF sytems, search by InvoiceNo, PoNo, ServiceId is applicable.
	private List<Map<String, Object>> searchByGivenKeyInCBF(String key, String value, List<String> billingIds, String fromDate, String toDate) throws TclCommonException {
		String bas = billingIds.stream().map(ba -> "'" + ba + "'").collect(Collectors.joining(","));
		String query;
		String suffix = "";
		BillingUtils.validateSqlInjectionInput(value);//handle sql injection
		BillingUtils.validateSqlInjectionInput(fromDate);//handle sql injection
		BillingUtils.validateSqlInjectionInput(toDate);//handle sql injection
		if("PoNo".equals(key)) {
			suffix = "	and PDT.po_no = '" + value + "'";
		} else if("ServiceId".equals(key)) {
			suffix = "	and PDT.service_id = '" + value + "'";
		} else if ("InvoiceNo".equals(key)) {
			suffix = "	and BM.INVOICE_NO = '" + value + "'";
		}
		
		try {
			if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())){
				 query = "select distinct "
						+ "'CBF' src, BM.INVOICE_NO, PDT.po_no, TRIM(BM.ACCOUNT_NO) ACCOUNT_NO, BM.BILL_DUE_DATE, BM.INVTOTAL,\r\n" +
						"	BM.billing_currency currency, BM.new_bill_date bill_date,\r\n" +
						"	BM.INVOICE_LINK, BM.INVOICE_FILE_NAME, BM.cdr_invoice_filename, BM.cdr_invoice_link\r\n" +
						"   PDT.opty_classification classification " +
						"from \r\n" +
						"	\"CBF\".bill_master BM, \"CBF\".product_details PDT \r\n" +
						"where\r\n" +
						"    BM.ID = PDT.Process_ID \r\n" +
						"    and BM.account_no = PDT.account_no \r\n" +
						"	and BM.INVOICE_NO = PDT.INVOICE_NO \r\n" +
						"	and BM.account_no in (" + bas + ") " +
						suffix + " \r\n" +
						"	and bill_type='Production' and isactive='Y' and BM.SERVICE_TYPE !='TCLi' \r\n" +
						"	and BM.new_bill_date >= '" + fromDate + "' \r\n" +
						"	and BM.new_bill_date <= '" + toDate + "' \r\n" +
						"order by BM.NEW_BILL_DATE DESC ";
			}
			else{
				 query = "select distinct "
						+ "'CBF' src, BM.INVOICE_NO, PDT.po_no, TRIM(BM.SAP_CODE) SAP_CODE, BM.BILL_DUE_DATE, BM.INVTOTAL,\r\n" +
						"	BM.billing_currency currency, BM.new_bill_date bill_date,\r\n" +
						"	BM.INVOICE_LINK, BM.INVOICE_FILE_NAME, BM.cdr_invoice_filename, BM.cdr_invoice_link\r\n" +
						"from \r\n" +
						"	\"CBF\".bill_master BM, \"CBF\".product_details PDT \r\n" +
						"where\r\n" +
						"    BM.ID = PDT.Process_ID \r\n" +
						"    and BM.account_no = PDT.account_no \r\n" +
						"	and BM.INVOICE_NO = PDT.INVOICE_NO \r\n" +
						"	and BM.account_no in (" + bas + ") " +
						suffix + " \r\n" +
						"	and bill_type='Production' and isactive='Y' and BM.SERVICE_TYPE !='TCLi' \r\n" +
						"	and BM.new_bill_date >= '" + fromDate + "' \r\n" +
						"	and BM.new_bill_date <= '" + toDate + "' \r\n" +
						"order by BM.NEW_BILL_DATE DESC ";
			}

			LOGGER.debug("Query for: SearchBy: {}", query);
			return invoicesTemplate.queryForList(query);
		} catch (Exception ex) {
			LOGGER.error(ExceptionConstants.ERROR_FETCHING_INVOICE, ex);
			throw new TclCommonException(ExceptionConstants.ERROR_FETCHING_INVOICE, ex, ResponseResource.R_CODE_ERROR);
		}
	}
	
	// For GBS system, search by InvoiceNo is only applicable. 
	private List<Map<String, Object>> searchByGivenKeyInGBS(String invoiceNo, List<String> sapCodes, String fromDate, String toDate) throws TclCommonException {
		String codes = sapCodes.stream().map(sc -> "'" + sc + "'").collect(Collectors.joining(","));
		Stream<String> params;
		try {
			BillingUtils.validateSqlInjectionInput(invoiceNo);//handle sql injection
			java.util.Date from = cbfDateformatter.parse(fromDate);
			String query;
 			if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())){
				query = "select distinct 'GBS' src, BM.full_invce_no invoice_no, CONCAT(BM.CPNY_ID, BM.org_no) ACCOUNT_NO, BM.org_no ORG_NO, BM.DUE_DT bill_due_date, \r\n" +
						"BM.amt invtotal, BM.prfilcurrency currency, BM.invce_dt bill_date, BM.mdium_cd file_type,\r\n" +
						"BM.RAPIDCDR_ID || BM.FILE_NM file_link, BM.FILE_NM file_name\r\n" +
						"from\r\n" +
						"    SEBS.GBS_INVCE_FOR_NGP BM\r\n" +
						"where\r\n" +
						"    bm.invce_dt >= to_date('"+ gbsDateformatter.format(from) +"', 'DD-MM-YYYY')\r\n" +
						"    and bm.org_no in (" + codes + ")\r\n" +
						"    and bm.full_invce_no = '"+ invoiceNo +"'";
				params = Arrays.stream(new String[]{"SRC", "INVOICE_NO", "PO_NO", "ACCOUNT_NO", "BILL_DUE_DATE", "INVTOTAL", "CURRENCY", "BILL_DATE"});
			}
			else{
				query = "select distinct 'GBS' src, BM.full_invce_no invoice_no, CONCAT(BM.CPNY_ID, BM.org_no) SAP_CODE, BM.org_no ORG_NO, BM.DUE_DT bill_due_date, \r\n" +
						"BM.amt invtotal, BM.prfilcurrency currency, BM.invce_dt bill_date, BM.mdium_cd file_type,\r\n" +
						"BM.RAPIDCDR_ID || BM.FILE_NM file_link, BM.FILE_NM file_name\r\n" +
						"from\r\n" +
						"    SEBS.GBS_INVCE_FOR_NGP BM\r\n" +
						"where\r\n" +
						"    bm.invce_dt >= to_date('"+ gbsDateformatter.format(from) +"', 'DD-MM-YYYY')\r\n" +
						"    and bm.org_no in (" + codes + ")\r\n" +
						"    and bm.full_invce_no = '"+ invoiceNo +"'";
				params = Arrays.stream(new String[]{"SRC", "INVOICE_NO", "PO_NO", "SAP_CODE", "BILL_DUE_DATE", "INVTOTAL", "CURRENCY", "BILL_DATE"});

			}
			LOGGER.debug("Query for: SearchBy: {}", query);

			List<Map<String, Object>> gbsInvoices = gbsInvoicesTemplate.queryForList(query);
			Map<String, Object> valMap = new HashMap<>();
			gbsInvoices.stream().forEach(row -> {
				String fileType = (String) row.get("FILE_TYPE");
				params.forEach(param -> {
					if(param.equals("ACCOUNT_NO")) {
						valMap.put(param.toLowerCase(), String.valueOf(row.get(param)));
					}else if(param.equals("SAP_CODE")) {
						valMap.put(param.toLowerCase(), String.valueOf(row.get(param)));
					}else if(param.equals("BILL_DATE")){
						valMap.put(param.toLowerCase(), new Date(((Timestamp)row.get(param)).getTime()));
					} else {
						valMap.put(param.toLowerCase(), row.get(param));
					}
				});
				
				if (fileType.equals("PAP")) {
					valMap.put("invoice_link", row.get("FILE_LINK"));
					valMap.put("invoice_file_name", row.get("FILE_NAME"));
				} else if (fileType.equals("XML")) {
					valMap.put("xml_invoice_link", row.get("FILE_LINK"));
					valMap.put("xml_invoice_file_name", row.get("FILE_NAME"));
				} else {
					MapUtils.putAll(new HashMap<String, String>(), new String[][] {
							{"cdr_invoice_link", "FILE_LINK"},
							{"cdr_invoice_filename", "FILE_NAME"},
							{"cdr_invoice_filetype", "FILE_TYPE"}
					}).entrySet().forEach(e -> {
						String key = e.getKey();
						String val = e.getValue();
						List<String> list = new ArrayList<>();
						list.add((String) row.get(val));
						valMap.put(key, list);
					});
				}
			});
			List<Map<String, Object>> l = new ArrayList<>();
			l.add(valMap);
			return l;
		} catch (Exception ex) {
			LOGGER.error(ExceptionConstants.ERROR_FETCHING_INVOICE, ex);
			throw new TclCommonException(ExceptionConstants.ERROR_FETCHING_INVOICE, ex, ResponseResource.R_CODE_ERROR);
		}
	}
	
	/**
	 * @param filePath
	 * @return
	 * @throws TclCommonException
	 */
	private byte[] callGBS(String filePath) throws TclCommonException {
		try {
			long start = System.currentTimeMillis();
			LOGGER.info("Entered In callGBS");
			NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(domain, userName,
					EncryptionUtil.decrypt(password)); // Authentication
			String path = BillingConstants.SMB_PREFIX + rootPath + filePath;
			LOGGER.info("path in callGBS {}, domain{},userName{}, password:: {}",path,
					domain, userName,EncryptionUtil.decrypt(password));
			SmbFile sf = new SmbFile(path, auth);
			LOGGER.info("Fetching the byterray");
			byte[] gbsdata= IOUtils.toByteArray(sf.getInputStream());
			long elapsedTime = System.currentTimeMillis() - start;
			LOGGER.info("Exiting callGBS  with an execution time : {} ms", elapsedTime);
			return gbsdata ;
		} catch (Exception ex) {
			LOGGER.error(ExceptionConstants.ERROR_FETCHING_INVOICE, ex);
			throw new TclCommonException(ExceptionConstants.ERROR_FETCHING_INVOICE, ex, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * @param filePath
	 * @return
	 * @throws TclCommonException
	 */
	private byte[] callCBF(String filePath, String serviceType) throws TclCommonException {
		LOGGER.info("Entered in to callCBF filepath {} serviceType{}",filePath,serviceType);
		try {
			String tcliPath=cbfPath;
			LOGGER.info("cbfpath:: {}",cbfPath);
			if(serviceType!=null && serviceType.equalsIgnoreCase("TCLi")) {
				tcliPath=cbfPath.replace("DYNAMICSERVICETYPE", "SERV_TYPE=TCLi&");
			}
			else {
				tcliPath=cbfPath.replace("DYNAMICSERVICETYPE", "");
			}
			return IOUtils.toByteArray(BillingUtils.fetchFileFromClient(tcliPath.concat(filePath)));
	
		} catch (Exception ex) {
			LOGGER.error(ExceptionConstants.ERROR_FETCHING_INVOICE, ex);
			throw new TclCommonException(ExceptionConstants.ERROR_FETCHING_INVOICE, ex, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * method for normal load. ie less number of invoices requested
	 * 
	 * @param source
	 * @param filePath
	 * @param fileName
	 * @param zos
	 */
	private void normalLoad(String source, String filePath, String fileName, ZipOutputStream zos, String serviceType
			,String invoiceNo) {

		LOGGER.info("Entered in normal Load source:: {} ,filePath::{},fileName{}",
				source,filePath,fileName);
		byte[] b = null;
		try {
			if (source.equals(CBF)) {
				b = getFileDownloadApi(getGBSFileTempLocation(invoiceNo, BillingConstants.GENEVA));
			} else if (source.equals(BillingConstants.GBS)) {
				b = getGBSFile(invoiceNo, fileName,new HashMap<>());
			}
			ZipEntry entry = new ZipEntry(fileName);
			entry.setSize(b.length);
			zos.putNextEntry(entry);
			zos.write(b);
		} catch (TclCommonException | IOException e) {
			LOGGER.error(ExceptionConstants.ERROR_FETCHING_INVOICE, e);
		}
	}

}
