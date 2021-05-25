package com.tcl.dias.servicefulfillmentutils.service.v1;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.entities.IpcProcurementDetails;
import com.tcl.dias.servicefulfillment.entity.entities.ScProductDetail;
import com.tcl.dias.servicefulfillment.entity.repository.IpcProcurementDetailsRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScProductDetailRepository;
import com.tcl.dias.servicefulfillmentutils.beans.ProcurementBean;
import com.tcl.dias.servicefulfillmentutils.beans.ProcurementDetails;
import com.tcl.dias.servicefulfillmentutils.beans.ProcurementResponse;
import com.tcl.dias.servicefulfillmentutils.beans.ProcurementSolutionDetailBean;
import com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants;
import com.tcl.dias.servicefulfillmentutils.constants.IpcConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Service
@Transactional(isolation = Isolation.READ_UNCOMMITTED)
public class IpcServiceDeliveryService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(IpcServiceDeliveryService.class);

	@Autowired
    IpcProcurementDetailsRepository ipcProcurementDetailsRepository;
	
	@Autowired
	ScProductDetailRepository scProductDetailRepository;
	
	@Autowired
	private SpringTemplateEngine templateEngine;

	/**
	 * @param procurementData
	 * @return
	 * @throws TclCommonException
	 */
	public ProcurementResponse createProcurementDetail(ProcurementBean procurementData) throws TclCommonException{
		ProcurementResponse response = new ProcurementResponse();
		try {
			LOGGER.info("Inside Create Procurement Data..");
			validateProcurementDetail(procurementData);
			IpcProcurementDetails ipcProcurement = processProcurement("CREATE", procurementData, null);
			response.setProcurementId(ipcProcurement.getId());
			response.setScOrderId(ipcProcurement.getScOrderId());
			response.setStatus(Status.SUCCESS);
		}catch(Exception e) {
			response.setStatus(Status.ERROR);
			LOGGER.error("error while creating a Procurement :: {}", e);
		}
		return response;
	}

	/**
	 * @param requestType
	 * @param procurementData
	 * @param ipcProcurement
	 * @return
	 */
	private IpcProcurementDetails processProcurement(String requestType, ProcurementBean procurementData,
			IpcProcurementDetails ipcProcurement) {
		LOGGER.info("processProcurement based on requestType {}", requestType);
		IpcProcurementDetails ipcProcurementDetail = new IpcProcurementDetails();
		String user = Utils.getSource();
		if ("CREATE".equals(requestType) && null != procurementData) {
			ipcProcurementDetail = commonDataProcurementUpdate(procurementData, ipcProcurementDetail);
			ipcProcurementDetail.setCreatedBy(user);
			ipcProcurementDetail.setCreatedDate(new Timestamp(new Date().getTime()));
			ipcProcurementDetail.setUpdatedBy(user);
			ipcProcurementDetail.setUpdatedDate(new Timestamp(new Date().getTime()));
			ipcProcurementDetailsRepository.save(ipcProcurementDetail);
		} else if ("UPDATE".equals(requestType) && null != procurementData && null != ipcProcurement) {
			ipcProcurement = commonDataProcurementUpdate(procurementData, ipcProcurement);
			ipcProcurement.setUpdatedBy(user);
			ipcProcurement.setUpdatedDate(new Timestamp(new Date().getTime()));
			ipcProcurementDetailsRepository.save(ipcProcurement);
			ipcProcurementDetail = ipcProcurement;
		} else if ("DELETE".equals(requestType) && null != ipcProcurement) {
			ipcProcurementDetailsRepository.delete(ipcProcurement);
		}
		return ipcProcurementDetail;
	}

	/**
	 * @param procurementData
	 * @param ipcProcurement
	 * @return
	 */
	private IpcProcurementDetails commonDataProcurementUpdate(ProcurementBean procurementData, IpcProcurementDetails ipcProcurement) {
		ipcProcurement.setScOrderId(procurementData.getScOrderId());
		ipcProcurement.setSolutionName(procurementData.getSolutionName());
		ipcProcurement.setCloudCode(procurementData.getCloudCode());
		ipcProcurement.setWbsNumber(procurementData.getWbsNumber());
		ipcProcurement.setWbsValue(procurementData.getWbsValue());
		ipcProcurement.setGlccNumber(procurementData.getGlccNumber());
		ipcProcurement.setGlccValue(procurementData.getGlccValue());
		ipcProcurement.setPoNumber(procurementData.getPoNumber());
		ipcProcurement.setPoValue(procurementData.getPoValue());
		ipcProcurement.setProcurementType(procurementData.getProcurementType());
		ipcProcurement.setPoReleaseDate(
				new Timestamp(DateUtil.convertStringToDateYYMMDD(procurementData.getPoReleaseDate()).getTime()));
		ipcProcurement.setReceiptDate(
				new Timestamp(DateUtil.convertStringToDateYYMMDD(procurementData.getReceiptDate()).getTime()));
		ipcProcurement.setContractStartDate(
				new Timestamp(DateUtil.convertStringToDateYYMMDD(procurementData.getContractStartDate()).getTime()));
		ipcProcurement.setContractEndDate(
				new Timestamp(DateUtil.convertStringToDateYYMMDD(procurementData.getContractEndDate()).getTime()));
		return ipcProcurement;
	}

	/**
	 * @param procurementId
	 * @return
	 * @throws TclCommonException
	 */
	public ProcurementBean getProcurementDetail(Integer procurementId) throws TclCommonException {
		LOGGER.info("Inside getProcurementDetail..");
		ProcurementBean response = null;
		try {
			validateProcurement(procurementId);
			IpcProcurementDetails ipcProcurement = getProcurement(procurementId);
			response = constructProcurement(ipcProcurement);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return response;
	}

	/**
	 * @param ipcProcurement
	 * @return
	 */
	private ProcurementBean constructProcurement(IpcProcurementDetails ipcProcurement) {
		ProcurementBean procurementBean = new ProcurementBean();
		procurementBean.setProcurementId(ipcProcurement.getId());
		procurementBean.setScOrderId(ipcProcurement.getScOrderId());
		procurementBean.setSolutionName(ipcProcurement.getSolutionName());
		procurementBean.setCloudCode(ipcProcurement.getCloudCode());
		procurementBean.setWbsNumber(ipcProcurement.getWbsNumber());
		procurementBean.setWbsValue(ipcProcurement.getWbsValue());
		procurementBean.setGlccNumber(ipcProcurement.getGlccNumber());
		procurementBean.setGlccValue(ipcProcurement.getGlccValue());
		procurementBean.setPoNumber(ipcProcurement.getPoNumber());
		procurementBean.setPoValue(ipcProcurement.getPoValue());
		procurementBean.setPoReleaseDate(ipcProcurement.getPoReleaseDate().toString());
		procurementBean.setProcurementType(ipcProcurement.getProcurementType());
		procurementBean.setReceiptDate(ipcProcurement.getReceiptDate().toString());
		procurementBean.setContractStartDate(ipcProcurement.getContractStartDate().toString());
		procurementBean.setContractEndDate(ipcProcurement.getContractEndDate().toString());
		return procurementBean;
	}

	/**
	 * @param procurementId
	 * @return
	 * @throws TclCommonException
	 */
	private IpcProcurementDetails getProcurement(Integer procurementId) throws TclCommonException {
		Optional<IpcProcurementDetails> optIpcProucrement = ipcProcurementDetailsRepository.findById(procurementId);
		IpcProcurementDetails ipcProcurement = null;
		if(optIpcProucrement.isPresent()) {
			ipcProcurement = optIpcProucrement.get();
		}else {
			throw new TclCommonException(ExceptionConstants.PROCUREMENT_VALIDATION_ERROR, ResponseResource.R_CODE_NOT_FOUND);
		}
		return ipcProcurement;
	}

	private void validateProcurement(Integer procurementId) throws TclCommonException {
		if ((procurementId == null)) {
			throw new TclCommonException(ExceptionConstants.PROCUREMENT_VALIDATION_ERROR, ResponseResource.R_CODE_NOT_FOUND);
		}
	}

	/**
	 * @param procurementId
	 * @param procurementData
	 * @return
	 * @throws TclCommonException
	 */
	public ProcurementResponse updateProcurementDetail(Integer procurementId, ProcurementBean procurementData) throws TclCommonException {
		ProcurementResponse response = new ProcurementResponse();
		LOGGER.info("Inside updateProcurementDetail..");
		try {
			validateProcurement(procurementId);
			IpcProcurementDetails ipcProcurement = getProcurement(procurementId);
			processProcurement("UPDATE", procurementData, ipcProcurement);
			response.setStatus(Status.SUCCESS);
		} catch (Exception e) {
			response.setStatus(Status.ERROR);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return response;
	}
	
	/**
	 * @param procurementData
	 * @throws TclCommonException
	 */
	public void validateProcurementDetail(ProcurementBean procurementData) throws TclCommonException {
		if ((procurementData == null)) {
			throw new TclCommonException(ExceptionConstants.PROCUREMENT_VALIDATION_ERROR, ResponseResource.R_CODE_NOT_FOUND);
		}
	}

	/**
	 * @param scOrderId
	 * @return
	 * @throws TclCommonException
	 */
	public ProcurementDetails fetchProcurementByScOrder(Integer scOrderId) throws TclCommonException {
		LOGGER.info("Inside updateProcurementDetail..");
		ProcurementDetails detail = new ProcurementDetails();
		try {
			validateProcurement(scOrderId);
			List<IpcProcurementDetails> ipcProcurementLst = ipcProcurementDetailsRepository.findByScOrderId(scOrderId);
			detail.setProcurementLst(ipcProcurementLst.stream().map(ipcProcurement -> constructProcurement(ipcProcurement)).collect(Collectors.toList()));
		}catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return detail;
	}

	/**
	 * @param procurementId
	 * @return
	 * @throws TclCommonException
	 */
	public ProcurementResponse deleteProcurementDetail(Integer procurementId) throws TclCommonException {
		ProcurementResponse response = new ProcurementResponse();
		LOGGER.info("Inside deleteProcurementDetail..");
		try {
			validateProcurement(procurementId);
			IpcProcurementDetails ipcProcurement = getProcurement(procurementId);
			response.setStatus(Status.SUCCESS);
			processProcurement("DELETE", null, ipcProcurement);
		}catch (Exception e) {
			response.setStatus(Status.ERROR);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		
		return response;
	}

	@Transactional
	public List<ProcurementSolutionDetailBean> fetchSolutionDetailForServiceId(Integer scServiceDetailId) {
		Map<String, ProcurementSolutionDetailBean> procurementSolutionDetailBeanMap = new HashMap<>();
		List<ScProductDetail> scProductDetails = scProductDetailRepository.findByScServiceDetailId(scServiceDetailId);
		scProductDetails.stream().filter(scProductDetail -> IpcConstants.ADDON.equalsIgnoreCase(scProductDetail.getType())).forEach(scProductDetail -> {
			scProductDetail.getScProductDetailAttributes().stream().filter(scProdAttributes -> checkForProcurableItems(scProdAttributes.getCategory())).forEach(scProdAttributes -> {
				ProcurementSolutionDetailBean procurementSolutionDetailBean;
				if(procurementSolutionDetailBeanMap.containsKey(scProdAttributes.getCategory())) {
					procurementSolutionDetailBean = procurementSolutionDetailBeanMap.get(scProdAttributes.getCategory());
				} else {
					procurementSolutionDetailBean = new ProcurementSolutionDetailBean();
				}
				procurementSolutionDetailBean.setSolutionName(scProdAttributes.getCategory());
				procurementSolutionDetailBean.getAttributes().put(scProdAttributes.getAttributeName(), scProdAttributes.getAttributeValue());
				procurementSolutionDetailBeanMap.put(scProdAttributes.getCategory(), procurementSolutionDetailBean);
			});
		});
		return new ArrayList<ProcurementSolutionDetailBean>(procurementSolutionDetailBeanMap.values());
	}
	
	private Boolean checkForProcurableItems(String solutionName){
		if(solutionName.startsWith(IpcConstants.MYSQL) || solutionName.startsWith(IpcConstants.MS_SQL_SERVER) || solutionName.startsWith(IpcConstants.POSTGRESQL) 
				|| solutionName.startsWith(IpcConstants.DR_ZERTO) || solutionName.startsWith(IpcConstants.DR_DOUBLE_TAKE)) {
			return true;
		}
		return false;
	}
	
	public String getCustomerEmailContent(Map<String, String> emailAttributes) {
		String html = "";
		if(!CollectionUtils.isEmpty(emailAttributes)) {
			Map<String, Object> variables = new HashMap<>();
			Context context = new Context();
			
			variables.put("customerName", emailAttributes.get("customerName"));
			variables.put("pmName", emailAttributes.get("pmName"));
			variables.put("pmEmailId", emailAttributes.get("pmEmailId"));
			variables.put("orderId", emailAttributes.get("orderId"));
			variables.put("customerExpectedDeliveryDate", emailAttributes.get("customerExpectedDeliveryDate"));
			
			context.setVariables(variables);
			
			html = templateEngine.process("project_initiation_customer_email_template", context);
		}
		return html;
	}
}
