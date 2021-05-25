package com.tcl.dias.oms.bulkfeasibility.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.tcl.dias.common.beans.PagedResult;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.bulkfeasibility.beans.BulkFeasibilityInfoBean;
import com.tcl.dias.oms.bulkfeasibility.beans.StatusInfoResponseBean;
import com.tcl.dias.oms.bulkfeasibility.constants.BulkFeasibilityConstant;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.entity.entities.BulkFeasibilityInfo;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.repository.BulkFeasibilityInfoRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * This file contains the BulkFeasibilityService.java class.
 *
 * @author Nithya S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
@Transactional
public class BulkFeasibilityService {

	private static final Logger LOGGER = LoggerFactory.getLogger(BulkFeasibilityService.class);

	@Value("${bulk.feasibility.file.getstatus.url}")
	String getStatusOfUploadedFileUrl;

	@Value("${bulk.feasibility.file.upload.url}")
	String fileUploadUrl;

	@Value("${bulk.feasibility.output.file.download.url}")
	String outputFileDownloadUrl;

	@Autowired
	RestClientService restClientService;

	@Autowired
	BulkFeasibilityInfoRepository bulkFeasibilityInfoRepository;

	@Autowired
	UserRepository userRepository;

	/**
	 * Method to get status of the uploaded file
	 * @param jobId
	 * @return StatusInfoResponseBean
	 * @author krutsrin
	 */
	public StatusInfoResponseBean getStatusOfUploadedFile(Integer jobId) {
		LOGGER.info("Inside BulkFeasibilityService getStatusOfUploadedFile method for jobId {}", jobId);
		String sustitutedURL = MessageFormat.format(getStatusOfUploadedFileUrl, Integer.toString(jobId));
		RestResponse restResponse = restClientService.getWithoutAuth(sustitutedURL);
		StatusInfoResponseBean responseBean = new StatusInfoResponseBean();
		if (restResponse.getStatus() == Status.SUCCESS) {
			try {
				LOGGER.info("Response on success {}",restResponse.getData());
				responseBean = Utils.convertJsonToObject(restResponse.getData(), StatusInfoResponseBean.class);
				if(responseBean!=null && responseBean.getJobId()!=null) {
					BulkFeasibilityInfo bulkFeasiilityJobInfo = bulkFeasibilityInfoRepository.findByJobId(responseBean.getJobId());
					if(bulkFeasiilityJobInfo!=null) {
						LOGGER.info("The latest status of jobId {} is {}",responseBean.getJobId(), responseBean.getStatus());
						bulkFeasiilityJobInfo.setStatus(responseBean.getStatus());
						bulkFeasiilityJobInfo.setUpdatedTime(new Timestamp(new Date().getTime()));
						bulkFeasibilityInfoRepository.save(bulkFeasiilityJobInfo);
					}
				}
			} catch (TclCommonException e) {
				LOGGER.warn("Error on converting the response json string to object {} ", e.getMessage());
			}
		}

		if (restResponse.getStatus() == Status.ERROR) {
			JSONObject jsonObj = parseAJson(restResponse.getData());
			if (jsonObj.containsKey(BulkFeasibilityConstant.ERROR)) {
				LOGGER.info("Error from downstream {}", jsonObj.get(BulkFeasibilityConstant.ERROR));
				if (jsonObj.get(BulkFeasibilityConstant.ERROR) != null)
					responseBean.setErrorFromDownStream(String.valueOf(jsonObj.get(BulkFeasibilityConstant.ERROR)));
			} else {
				responseBean.setErrorFromDownStream(restResponse.getErrorMessage());
			}
		}
		return responseBean;
	}

	/**
	 * Method to get status of the uploaded file
	 * @param httpResponse
	 * @return StatusInfoResponseBean
	 * @author krutsrin
	 * @return 
	 * @throws TclCommonException 
	 * @throws IOException 
	 * @throws TclCommonRuntimeException 
	 */
	public void downloadTemplate(HttpServletResponse httpResponse) throws TclCommonException, TclCommonRuntimeException, IOException {
		LOGGER.info("Inside BulkFeasibilityService downloadTemplate method");
		String[] columns = { "S.NO","SFDC  Oppurtunity ID or Optimus Quote ID","CKT ID","Customer Name","Bandwidth", "Customer Address", "Customer Latitude", "Customer Longitude", "BTS ID", "Technology",
		"Sector IP"};
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		XSSFWorkbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Bulk Feasibility Input template");
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 12);
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);
		headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
		headerCellStyle.setFillForegroundColor(IndexedColors.CORNFLOWER_BLUE.getIndex());
		headerCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		Row headerRow = sheet.createRow(0);
		for (int i = 0; i < columns.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(columns[i]);
			cell.setCellStyle(headerCellStyle);
		}
		for (int i = 0; i < columns.length; i++) {
			sheet.autoSizeColumn(i);
		}
		generateExcel(httpResponse, "mf_bulk_feasibility.xlsx", outByteStream, workbook);
	}

	/**
	 * Helper method to parse Json
	 * @param responseJson
	 * @return JSONObject
	 */
	private JSONObject parseAJson(String responseJson) {
		JSONObject dataEnvelopeObj = null;
		JSONParser jsonParser = new JSONParser();
		try {
			dataEnvelopeObj = (JSONObject) jsonParser.parse(responseJson);
		} catch (org.json.simple.parser.ParseException e) {
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return dataEnvelopeObj;
	}



	public static class Context {
		Integer rowCount = 0;
		Integer cellCount = 0;
		Row row;
	}

	/**
	 * Method to generat created Excel
	 *
	 * @param response
	 * @param fileName
	 * @param outByteStream
	 * @param workbook
	 * @throws TclCommonException
	 */
	private void generateExcel(HttpServletResponse response, String fileName, ByteArrayOutputStream outByteStream,
			Workbook workbook) throws TclCommonException {
		LOGGER.info("Inside generateExcel downloadTemplate method");
		byte[] outArray;
		try {
			workbook.write(outByteStream);
			outArray = outByteStream.toByteArray();
			response.reset();
			response.setContentType(BulkFeasibilityConstant.APPLICATION_MS_EXCEL);
			response.setContentLength(outArray.length);
			response.setHeader("Expires:", "0");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			workbook.close();
			FileCopyUtils.copy(outArray, response.getOutputStream());
			outByteStream.flush();
			outByteStream.close();
		} catch (IOException e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * Method to upload file for feasibility check
	 * @param bulkFeasibilityInfoBean
	 * @return {StatusInfoResponseBean}
	 * @author Nithya S
	 */
	public StatusInfoResponseBean uploadFileForFeasibilityCheck(MultipartFile multipartFile) throws TclCommonException {
		LOGGER.info("Inside uploadFileForFeasibilityCheck method");
		StatusInfoResponseBean responseBean = new StatusInfoResponseBean();
		BulkFeasibilityInfo bulkFeasibilityInfo = new BulkFeasibilityInfo();
		User userEntity = userRepository.findByUsernameAndStatus(Utils.getSource(), CommonConstants.ACTIVE);
		Integer userId = userEntity.getId();
		bulkFeasibilityInfo.setUserId(userId);
		bulkFeasibilityInfo.setCreatedTime(new Timestamp(new Date().getTime()));
		bulkFeasibilityInfo.setStatus(BulkFeasibilityConstant.OPEN);
		bulkFeasibilityInfo = bulkFeasibilityInfoRepository.save(bulkFeasibilityInfo);

		Integer jobId = bulkFeasibilityInfo.getId();	
		LOGGER.info("Inside uploadFileForFeasibilityCheck userId {} jobId {} ", userId, jobId);		
		String[] str = {String.valueOf(userId), String.valueOf(jobId)};		
		String sustitutedURL = MessageFormat.format(fileUploadUrl, str);
		LOGGER.info("Inside sustitutedURL{}", sustitutedURL);
		RestResponse restResponse = restClientService.postMultipartFile(sustitutedURL, multipartFile);
		LOGGER.info("restResponse {}" + restResponse.getData());
		JSONObject jsonObj = parseAJson(restResponse.getData());
		if (restResponse.getStatus() == Status.SUCCESS) {
			try {
				if (jsonObj.containsKey(BulkFeasibilityConstant.MESSAGE)) 
					LOGGER.info("Message from downstream {}", jsonObj.get(BulkFeasibilityConstant.MESSAGE));
				if (jsonObj.get(BulkFeasibilityConstant.MESSAGE) != null) {
					responseBean.setMsgFromDownStream(String.valueOf(jsonObj.get(BulkFeasibilityConstant.MESSAGE)));
					bulkFeasibilityInfo.setStatus(String.valueOf(jsonObj.get(BulkFeasibilityConstant.MESSAGE)));
				}
				bulkFeasibilityInfo.setJobId(jobId);
				bulkFeasibilityInfo.setUpdatedTime(new Timestamp(new Date().getTime()));
				bulkFeasibilityInfo.setUploadedFileName(multipartFile.getOriginalFilename());
				bulkFeasibilityInfoRepository.save(bulkFeasibilityInfo);
				LOGGER.info("after saving data in bulkfeasibility info");
				responseBean.setJobId(jobId);
			} catch (Exception e) {
				LOGGER.error("Error in create New Request in Bulk feasibility info", e);
			}
		} else {
			responseBean.setErrorFromDownStream(BulkFeasibilityConstant.ERROR_FROM_BATCH_PROCESS);
			LOGGER.info("Message from ErrorFromDownStream {}", jsonObj.get(BulkFeasibilityConstant.ERROR));
			if (jsonObj.containsKey(BulkFeasibilityConstant.ERROR)) 
				responseBean.setErrorFromDownStream(String.valueOf(jsonObj.get(BulkFeasibilityConstant.ERROR)));
		}
		return responseBean;
	}

	public void outputFileDownload(String jobId, HttpServletResponse response) throws TclCommonException{
		LOGGER.info("Inside downloadOutputFile jobId{} " + jobId);
		String sustitutedURL = MessageFormat.format(outputFileDownloadUrl, jobId);
		ResponseEntity<byte[]> responseEntity = restClientService.getFile(sustitutedURL);
		LOGGER.info("responseEntity=============" + responseEntity);
		byte[] outArray = responseEntity.getBody();
		String fileName = responseEntity.getHeaders().getContentDisposition().getFilename();
		BulkFeasibilityInfo bulkFeasibilityInfo = bulkFeasibilityInfoRepository.findByJobId(Integer.valueOf(jobId));
		bulkFeasibilityInfo.setDownloadedFileName(fileName);
		bulkFeasibilityInfoRepository.save(bulkFeasibilityInfo);
		generateExcelOutputFile(response, fileName, outArray);
	}

	private void generateExcelOutputFile(HttpServletResponse response, String fileName, byte[] outArray) throws TclCommonException {
		LOGGER.info("Inside generateExcel downloadTemplate method");
		try {
			XSSFWorkbook workbook = new XSSFWorkbook();
			ByteArrayOutputStream baos = null;
			baos = new ByteArrayOutputStream();
			baos.write(outArray);
			workbook.write(baos);
			response.reset();
			response.setContentType(BulkFeasibilityConstant.APPLICATION_MS_EXCEL);
			response.setContentLength(outArray.length);
			response.setHeader("Expires:", "0");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			workbook.close();
			FileCopyUtils.copy(outArray, response.getOutputStream());
			baos.flush();
			baos.close();
		} catch (IOException e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * Method to get all bulk feasibility data
	 * @param page
	 * @param size
	 * @return BulkFeasibilityInfoBean
	 * @author Nithya S
	 */
	@Transactional(readOnly = true)
	public PagedResult<BulkFeasibilityInfoBean> getAllBulkFeasibility(Integer page, Integer size) {
		LOGGER.info("Inside getAllBulkFeasibility page {} size{}", page, size);
		List<BulkFeasibilityInfoBean> bulkFeasibilityInfoBean = new ArrayList<>();
		PageRequest pageable = null;
		if (page != null && size != null) {
			pageable = PageRequest.of(page, size);
		}
		Page<BulkFeasibilityInfo> pagedBulkFeasibillity = bulkFeasibilityInfoRepository.findAllByOrderByCreatedTimeDesc(pageable);
		List<BulkFeasibilityInfo> bulkFeasibilityInfo = pagedBulkFeasibillity.getContent();
		if(bulkFeasibilityInfo!=null && !bulkFeasibilityInfo.isEmpty()) {
			bulkFeasibilityInfo.stream().forEach(bulkFeasibility->{
				bulkFeasibilityInfoBean.add(constructBulkFeasibilityBean(bulkFeasibility));
			});
		}
		return new PagedResult<>(bulkFeasibilityInfoBean, pagedBulkFeasibillity.getTotalElements(), pagedBulkFeasibillity.getTotalPages());
	}

	private BulkFeasibilityInfoBean constructBulkFeasibilityBean(BulkFeasibilityInfo bulkFeasibilityInfo) {
		LOGGER.info("constructBulkFeasibilityBean jobId {} userId {} " , bulkFeasibilityInfo.getJobId(), bulkFeasibilityInfo.getUserId());
		BulkFeasibilityInfoBean bulkFeasibilityInfoBean = new BulkFeasibilityInfoBean();
		bulkFeasibilityInfoBean.setJobId(Objects.nonNull(String.valueOf(bulkFeasibilityInfo.getJobId())) ? String.valueOf(bulkFeasibilityInfo.getJobId()) : "");
		bulkFeasibilityInfoBean.setUserId(String.valueOf(bulkFeasibilityInfo.getUserId()));
		bulkFeasibilityInfoBean.setStatus(bulkFeasibilityInfo.getStatus());
		bulkFeasibilityInfoBean.setCreatedDate(String.valueOf(bulkFeasibilityInfo.getCreatedTime()));
		return bulkFeasibilityInfoBean;
	}
}
