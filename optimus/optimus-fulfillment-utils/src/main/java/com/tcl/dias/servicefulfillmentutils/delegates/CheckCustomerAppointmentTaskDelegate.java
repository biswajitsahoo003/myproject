package com.tcl.dias.servicefulfillmentutils.delegates;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.engine.runtime.Execution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillment.entity.entities.DownTimeDetails;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScSolutionComponent;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.DownTimeDetailsRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScSolutionComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;

/**
 * @author dimples
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Component("checkCustomerAppointmentTaskDelegate")
public class CheckCustomerAppointmentTaskDelegate implements JavaDelegate {
	
	private static final Logger logger = LoggerFactory.getLogger(GetTxDownTimeDelegate.class);
	
	@Autowired
	WorkFlowService workFlowService;
	
	@Autowired
	TaskRepository taskRepository;
	
	@Autowired 
	ScComponentAttributesRepository scComponentAttributesRepository;
	
	@Autowired 
	ScSolutionComponentRepository scSolutionComponentRepository;
	
	@Autowired
	com.tcl.dias.servicefulfillment.entity.repository.ProcessRepository processRepository;
	
	@Autowired 
	DownTimeDetailsRepository downTimeDetailsRepository;
	
	@Autowired
	RuntimeService runtimeService;
	
	 @Override
	 public void execute(DelegateExecution execution) {
		 logger.info("CheckCustomerAppointmentTaskDelegate invoked for {} Id={}", execution.getCurrentActivityId(), execution.getId());
		 Map<String, Object> varibleMap = execution.getVariables();
		 Integer serviceId = (Integer) varibleMap.get(MasterDefConstants.SERVICE_ID);
		 String orderCode = (String) varibleMap.get(MasterDefConstants.ORDER_CODE);
		 String productName = StringUtils.trimToEmpty((String)varibleMap.get("productName"));
		 if(orderCode!=null && !orderCode.isEmpty()){
				 Integer solutionId = (Integer) varibleMap.get("solutionId");
				 logger.info("IZOSDWAN SolutionId::{},OrderCode::{},ProductName::{},ServiceId for CheckCustomerAppointmentTaskDelegate::{}", solutionId,orderCode,productName,serviceId);
				 Boolean isReadyForCustomerAppointment=false;
				 List<Integer> serviceIdList= new ArrayList<>();
				 Map<String, String> downTimeMap = new HashMap<>();
				 if("BYON Internet".equalsIgnoreCase(productName) || "IAS".equalsIgnoreCase(productName)
						 || "GVPN".equalsIgnoreCase(productName)  || "IZO Internet WAN".equalsIgnoreCase(productName)
						 || "DIA".equalsIgnoreCase(productName)){
					 Integer overlayId = (Integer) varibleMap.get("overlayId");
					 logger.info("CheckCustomerAppointmentTaskDelegate.Product Name::{} with OverlayId::{}", productName,overlayId);
					 isReadyForCustomerAppointment=checkCustomerAppointmentForSDWAN(solutionId,overlayId,serviceIdList);
				 }else if("IZOSDWAN".equalsIgnoreCase(productName) || "IZO SDWAN".equalsIgnoreCase(productName)){
					 logger.info("CheckCustomerAppointmentTaskDelegate.Product Name::{} with OverlayId::{}", productName,serviceId);
					 isReadyForCustomerAppointment=checkCustomerAppointmentForSDWAN(solutionId,serviceId,serviceIdList);
				 }
				 logger.info("isReadyForCustomerAppointment::{}", isReadyForCustomerAppointment);
				 if(isReadyForCustomerAppointment){
					 logger.info("ReadyForCustomerAppointment for solutionId::{},orderCode::{} and related serviceId::{}", solutionId,orderCode,serviceId);
					 Boolean isCustomerAppointmentTaskOpenedAlready=checkCustomerAppointmentTaskOpenedAlready(serviceIdList,orderCode);
					 if(isCustomerAppointmentTaskOpenedAlready){
						 logger.info("isCustomerAppointmentTaskOpenedAlready for solutionId::{},orderCode::{} and related serviceId::{}", solutionId,orderCode,serviceId);
						 if(isCustomerAppointmentTaskOpenedAlready){
							 logger.info("isCustomerAppointmentTaskOpenedAlready::",isCustomerAppointmentTaskOpenedAlready);
							 downTimeMap.put("customerAppointment", "skip");
							 Boolean isCustomerAppointmentTaskClosedAlready=checkCustomerAppointmentTaskClosedAlready(serviceIdList,orderCode);
							 if(!isCustomerAppointmentTaskClosedAlready){
								 logger.info("isCustomerAppointmentTask Opened but not Closed Already for solutionId::{},orderCode::{} and related serviceId::{}", solutionId,orderCode,serviceId);
								 downTimeMap.put("customerAppointment", "wait");
							 }
						 }else{
							 logger.info("isDownTimeTask Not OpenedAlready::",isCustomerAppointmentTaskOpenedAlready);
							 downTimeMap.put("customerAppointment", "open");
						 }
					 }else{
						 logger.info("isCustomerAppointmentTask Not OpenedAlready for solutionId::{},orderCode::{} and related serviceId::{}", solutionId,orderCode,serviceId);
						 downTimeMap.put("customerAppointment", "open");
					 }
				 }else{
					 logger.info("Not ReadyForDowntime for solutionId::{},orderCode::{} and related serviceId::{}", solutionId,orderCode,serviceId);
					 downTimeMap.put("customerAppointment", "wait");					
				 }
				 logger.info("CustomerAppointment::{}",downTimeMap.get("customerAppointment"));
				 execution.setVariable("customerAppointment", downTimeMap.get("customerAppointment"));
				 workFlowService.processServiceTaskCompletion(execution,"");
		 }
		
	 }

	private Boolean checkCustomerAppointmentTaskOpenedAlready(List<Integer> serviceIdList, String orderCode) {
		logger.info("checkCustomerAppointmentTaskOpenedAlready with serviceIdList::{},orderCode::{}",serviceIdList.size(),orderCode);
		Task customerAppointmentTask=taskRepository.findFirstByServiceIdInAndOrderCodeAndMstTaskDef_keyOrderByCreatedTimeDesc(serviceIdList, orderCode, "sdwan-customer-appointment-cpe-installation");
		if(customerAppointmentTask!=null){
			logger.info("CustomerAppointmentTask exists already::{}",customerAppointmentTask.getId());
			return true;
		}else{
			logger.info("CustomerAppointmentTask not exists for orderCode::{}",orderCode);
			return false;
		}
	}
	
	private Boolean checkCustomerAppointmentTaskClosedAlready(List<Integer> serviceIdList, String orderCode) {
		logger.info("checkCustomerAppointmentTaskClosedAlready with serviceIdList::{},orderCode::{}",serviceIdList.size(),orderCode);
		Task customerAppointmentTask=taskRepository.findFirstByServiceIdInAndOrderCodeAndMstTaskDef_keyAndMstStatus_codeOrderByCreatedTimeDesc(serviceIdList, orderCode, "sdwan-customer-appointment-cpe-installation","CLOSED");
		if(customerAppointmentTask!=null){
			logger.info("CustomerAppointmentTask closed already::{}",customerAppointmentTask.getId());
			return true;
		}else{
			logger.info("CustomerAppointmentTask not closed for orderCode::{}",orderCode);
			return false;
		}
	}

	private Boolean checkCustomerAppointmentForSDWAN(Integer solutionId, Integer overlayId, List<Integer> serviceIdList) {
		logger.info("checkCustomerAppointmentForSDWAN with solutionId::{},overlayId::{}",solutionId,overlayId);
		List<String> componentGroups = new ArrayList<>();
		componentGroups.add("OVERLAY");
		componentGroups.add("UNDERLAY");
		Boolean[] isReadyForCustomerAppointment={false};
		List<ScSolutionComponent> scSolutionComponentList=scSolutionComponentRepository.findByScServiceDetail3_idAndScServiceDetail2_idOrScServiceDetail1_idAndComponentGroupInAndIsActive(solutionId, overlayId,overlayId,componentGroups, "Y");
		if(scSolutionComponentList!=null && !scSolutionComponentList.isEmpty()){
			logger.info("checkCustomerAppointmentForSDWAN.ScSolutionComponentList size::{}",scSolutionComponentList.size());
			for(ScSolutionComponent scSolComp:scSolutionComponentList){
				if(("OVERLAY".equalsIgnoreCase(scSolComp.getComponentGroup()))|| ("BYON Internet".equalsIgnoreCase(scSolComp.getScServiceDetail1().getErfPrdCatalogProductName())) || (("IAS".equalsIgnoreCase(scSolComp.getScServiceDetail1().getErfPrdCatalogProductName())
						|| "GVPN".equalsIgnoreCase(scSolComp.getScServiceDetail1().getErfPrdCatalogProductName()) || "IZO Internet WAN".equalsIgnoreCase(scSolComp.getScServiceDetail1().getErfPrdCatalogProductName()) || "DIA".equalsIgnoreCase(scSolComp.getScServiceDetail1().getErfPrdCatalogProductName()))
						&& (scSolComp.getScServiceDetail1().getOrderType().equalsIgnoreCase("NEW") || 
								(scSolComp.getScServiceDetail1().getOrderType().equalsIgnoreCase("MACD")
								&& ((scSolComp.getScServiceDetail1().getOrderCategory()!=null && scSolComp.getScServiceDetail1().getOrderCategory().equalsIgnoreCase("ADD_SITE"))
										|| (scSolComp.getScServiceDetail1().getOrderSubCategory()!=null && scSolComp.getScServiceDetail1().getOrderSubCategory().toLowerCase().contains("parallel"))))))){
					logger.info("checkCustomerAppointmentForSDWAN.New Service Id size::{}",scSolComp.getScServiceDetail1().getId());
					serviceIdList.add(scSolComp.getScServiceDetail1().getId());
				}
			}
			for(ScSolutionComponent scSolComp:scSolutionComponentList){
				if("OVERLAY".equalsIgnoreCase(scSolComp.getComponentGroup())){
					isReadyForCustomerAppointment[0]=checkCustomerAppointmentforOverlay(solutionId,scSolComp.getScServiceDetail1().getId(),isReadyForCustomerAppointment[0]);
					logger.info("Overlay isReadyForCustomerAppointment::{}",isReadyForCustomerAppointment[0]);
				}else if("BYON Internet".equalsIgnoreCase(scSolComp.getScServiceDetail1().getErfPrdCatalogProductName())){
					isReadyForCustomerAppointment[0]=checkCustomerAppointmentforBYONInternet(solutionId,scSolComp.getScServiceDetail1().getId(),isReadyForCustomerAppointment[0]);
					logger.info("BYON Underlay isReadyForCustomerAppointment::{}",isReadyForCustomerAppointment[0]);
				}else if((("IZO Internet WAN".equalsIgnoreCase(scSolComp.getScServiceDetail1().getErfPrdCatalogProductName()) 
									&& !scSolComp.getScServiceDetail1().getDestinationCountry().equalsIgnoreCase("India"))
						|| "DIA".equalsIgnoreCase(scSolComp.getScServiceDetail1().getErfPrdCatalogProductName())
						|| ("GVPN".equalsIgnoreCase(scSolComp.getScServiceDetail1().getErfPrdCatalogProductName()) 
								&& !scSolComp.getScServiceDetail1().getDestinationCountry().equalsIgnoreCase("India")))
						&& (scSolComp.getScServiceDetail1().getOrderType().equalsIgnoreCase("NEW") || 
								(scSolComp.getScServiceDetail1().getOrderType().equalsIgnoreCase("MACD")
										&& ((scSolComp.getScServiceDetail1().getOrderCategory()!=null && scSolComp.getScServiceDetail1().getOrderCategory().equalsIgnoreCase("ADD_SITE"))
												|| (scSolComp.getScServiceDetail1().getOrderSubCategory()!=null && scSolComp.getScServiceDetail1().getOrderSubCategory().toLowerCase().contains("parallel")))))){
					isReadyForCustomerAppointment[0]=checkCustomerAppointmentforDIAIWANGVPNInternational(solutionId,scSolComp.getScServiceDetail1().getId(),isReadyForCustomerAppointment[0]);
					logger.info("IWANDIAGVPNIntl Underlay isReadyForCustomerAppointment::{}",isReadyForCustomerAppointment[0]);
				}else if(("IAS".equalsIgnoreCase(scSolComp.getScServiceDetail1().getErfPrdCatalogProductName())
								|| "GVPN".equalsIgnoreCase(scSolComp.getScServiceDetail1().getErfPrdCatalogProductName())) 
								&& (scSolComp.getScServiceDetail1().getOrderType().equalsIgnoreCase("NEW") || 
										(scSolComp.getScServiceDetail1().getOrderType().equalsIgnoreCase("MACD")
										&& ((scSolComp.getScServiceDetail1().getOrderCategory()!=null && scSolComp.getScServiceDetail1().getOrderCategory().equalsIgnoreCase("ADD_SITE"))
												|| (scSolComp.getScServiceDetail1().getOrderSubCategory()!=null && scSolComp.getScServiceDetail1().getOrderSubCategory().toLowerCase().contains("parallel")))))){
					isReadyForCustomerAppointment[0]=checkCustomerAppointmentforIASGVPN(solutionId,scSolComp.getScServiceDetail1().getId(),isReadyForCustomerAppointment[0]);
					logger.info("IAS or GVPN Underlay isReadyForCustomerAppointment::{}",isReadyForCustomerAppointment[0]);
				}else{
					logger.info("checkCustomerAppointmentForSDWAN skip service id::{} for group::{}",scSolComp.getScServiceDetail1().getId(),scSolComp.getComponentGroup());
					continue;
				}
				if(!isReadyForCustomerAppointment[0]){
					logger.info("isReadyForCustomerAppointment breaks for componentGroup::{} with service id::{}",scSolComp.getComponentGroup(),scSolComp.getScServiceDetail1().getId());
					break;
				}
			}
		}
		return isReadyForCustomerAppointment[0];
	}
	
	private Boolean checkCustomerAppointmentforDIAIWANGVPNInternational(Integer solutionId,Integer serviceId, Boolean isReadyForCustomerAppointment) {
		logger.info("checkCustomerAppointmentforDIAIWANGVPNInternational SolutionId::{},ServiceId::{}",solutionId,serviceId);
		DownTimeDetails downTimeDetails=downTimeDetailsRepository.findFirstBySolutionIdAndScServiceDetailIdOrderByIdDesc(solutionId, serviceId);
		if(downTimeDetails!=null && "Y".equalsIgnoreCase(downTimeDetails.getIsProvisionReadyForCustomerAppointment())){
			 logger.info("IWANDIAGVPNIntl Service Id::{} Ready For Customer Appointment::{}",serviceId,downTimeDetails.getId());
			 isReadyForCustomerAppointment=true;
		}else{
			 logger.info("IWANDIAGVPNIntl Service Id::{} Not Ready For Customer Appointment::{}",serviceId,downTimeDetails.getId());
			 isReadyForCustomerAppointment=false;
		}
		return isReadyForCustomerAppointment;
	}

	private Boolean checkCustomerAppointmentforIASGVPN(Integer solutionId,Integer serviceId, Boolean isReadyForCustomerAppointment) {
		logger.info("checkCustomerAppointmentforIASGVPN SolutionId::{},ServiceId::{}",solutionId,serviceId);
		DownTimeDetails downTimeDetails=downTimeDetailsRepository.findFirstBySolutionIdAndScServiceDetailIdOrderByIdDesc(solutionId, serviceId);
		if (downTimeDetails != null && "Y".equalsIgnoreCase(downTimeDetails.getIsLMTestRequired())) {
			logger.info("LM Test Required for service id::{}", serviceId);
			if ("Y".equalsIgnoreCase(downTimeDetails.getIsLMReadyForCustomerAppointment())) {
				logger.info("LM Test Ready for CustomerAppointment::{}", downTimeDetails.getId());
				isReadyForCustomerAppointment = true;
			} else {
				logger.info("LM Test  Not Ready for CustomerAppointment::{}", downTimeDetails.getId());
				isReadyForCustomerAppointment = false;
			}
		}
		return isReadyForCustomerAppointment;
	}

	private Boolean checkCustomerAppointmentforOverlay(Integer solutionId,Integer serviceId, Boolean isReadyForCustomerAppointment) {
		logger.info("checkCustomerAppointmentforOverlay SolutionId::{},ServiceId::{}",solutionId,serviceId);
		DownTimeDetails downTimeDetails=downTimeDetailsRepository.findFirstBySolutionIdAndScServiceDetailIdOrderByIdDesc(solutionId, serviceId);
		if(downTimeDetails!=null && "Y".equalsIgnoreCase(downTimeDetails.getIsCpeCustomerAppointmentRequired())){
			logger.info("Cpe Customer Appointment Required::{}",downTimeDetails.getId());
			if("Y".equalsIgnoreCase(downTimeDetails.getIsCpeReadyForCustomerAppointment())){
				 logger.info("Cpe Ready For Customer Appointment::{}",downTimeDetails.getId());
				 isReadyForCustomerAppointment=true;
			}else{
				 logger.info("Cpe Not Ready For Customer Appointment::{}",downTimeDetails.getId());
				 isReadyForCustomerAppointment=false;
			}
		}
		return isReadyForCustomerAppointment;
	}

	private Boolean checkCustomerAppointmentforBYONInternet(Integer solutionId,Integer serviceId, Boolean isReadyForCustomerAppointment) {
		logger.info("checkCustomerAppointmentforBYONInternet SolutionId::{},ServiceId::{}",solutionId,serviceId);
		DownTimeDetails downTimeDetails=downTimeDetailsRepository.findFirstBySolutionIdAndScServiceDetailIdOrderByIdDesc(solutionId, serviceId);
		if(downTimeDetails!=null && "Y".equalsIgnoreCase(downTimeDetails.getIsByonReadyForCustomerAppointment())){
			 logger.info("Byon Ready For Customer Appointment::{}",downTimeDetails.getId());
			 isReadyForCustomerAppointment=true;
		}else{
			 logger.info("Byon Not Ready For Customer Appointment::{}",downTimeDetails.getId());
			 isReadyForCustomerAppointment=false;
		}
		return isReadyForCustomerAppointment;
	}

}
