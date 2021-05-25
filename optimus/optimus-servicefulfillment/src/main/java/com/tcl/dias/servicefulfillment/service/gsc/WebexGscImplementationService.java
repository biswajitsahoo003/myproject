package com.tcl.dias.servicefulfillment.service.gsc;

import static com.tcl.dias.servicefulfillment.constants.GscO2CConstants.ORDER_ID_NULL_MESSAGE;
import static com.tcl.dias.servicefulfillment.constants.GscO2CConstants.ORDER_NULL_MESSAGE;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.beans.gsc.CreateDpFormBean;
import com.tcl.dias.servicefulfillment.beans.gsc.CreateTigerOrderBean;
import com.tcl.dias.servicefulfillment.beans.gsc.CugConfigurationBean;
import com.tcl.dias.servicefulfillment.beans.gsc.DedicatedNumberBean;
import com.tcl.dias.servicefulfillment.beans.gsc.E2eVoiceTestingBean;
import com.tcl.dias.servicefulfillment.beans.gsc.ExcelBean;
import com.tcl.dias.servicefulfillment.beans.gsc.GSCServiceDetailBean;
import com.tcl.dias.servicefulfillment.beans.gsc.GscOfferingBean;
import com.tcl.dias.servicefulfillment.beans.gsc.NumberMappingBean;
import com.tcl.dias.servicefulfillment.beans.gsc.TdCreationForCugBean;
import com.tcl.dias.servicefulfillment.beans.gsc.VoiceAdvanceEnrichment;
import com.tcl.dias.servicefulfillment.beans.gsc.VoiceAdvanceEnrichmentBean;
import com.tcl.dias.servicefulfillment.constants.ExceptionConstants;
import com.tcl.dias.servicefulfillment.constants.GscO2CConstants;
import com.tcl.dias.servicefulfillment.entity.entities.MstTaskAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponent;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScContractInfo;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.MstTaskAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScContractInfoRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillmentutils.beans.TaskBean;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.AttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.FlowableBaseService;
import com.tcl.dias.servicefulfillmentutils.service.v1.ServiceFulfillmentBaseService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Service("WebexGscImplementationService")
@Transactional(readOnly = true)
public class WebexGscImplementationService extends ServiceFulfillmentBaseService {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebexGscImplementationService.class);
	
	@Autowired
	ComponentAndAttributeService componentAndAttributeService;

	@Autowired
	FlowableBaseService flowableBaseService;

	@Autowired
	ScOrderRepository scOrderRepository;

	@Autowired
	ScContractInfoRepository scContractInfoRepository;

	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;
	
	@Autowired
	AttributeService attributeService;
	
	@Autowired
	ScComponentRepository scComponentRepository;
	
	@Autowired
	MstTaskAttributeRepository mstTaskAttributeRepository;
	
	@Transactional(readOnly = false)
	public VoiceAdvanceEnrichmentBean advancedEnrichmentForVoice(VoiceAdvanceEnrichmentBean voiceAdvanceEnrichment)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(voiceAdvanceEnrichment.getTaskId(), voiceAdvanceEnrichment.getWfTaskId());

		validateInputs(task, voiceAdvanceEnrichment);

		Map<String, String> atMap = new HashMap<>();
		if (Objects.nonNull(voiceAdvanceEnrichment)) {
			if(Objects.nonNull(voiceAdvanceEnrichment.getCugDialInNumber())) {
				atMap.put("cugDialInNumber", voiceAdvanceEnrichment.getCugDialInNumber());
			}
			/*atMap.put("numberRangeAction", voiceAdvanceEnrichment.getNumberRangeAction());
			atMap.put("uploadWebexOnNetDialbackNumberRanges", voiceAdvanceEnrichment.getUploadWebexOnNetDialbackNumberRanges());*/
			if(Objects.nonNull(voiceAdvanceEnrichment.getCugDialOut())) {
				atMap.put("cugDialOut", Utils.convertObjectToJson(voiceAdvanceEnrichment.getCugDialOut()));
			}
			if(Objects.nonNull(voiceAdvanceEnrichment.getOnNetDialBack())) {
				atMap.put("onNetDialBack", Utils.convertObjectToJson(voiceAdvanceEnrichment.getOnNetDialBack()));
			}
		}

		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap,
				AttributeConstants.COMPONENT_WEBEXLICENSE, task.getSiteType());
		
		if(Objects.nonNull(voiceAdvanceEnrichment.getConfigurationDetails())) {
			for(VoiceAdvanceEnrichment voiceAdvanceEnrichments : voiceAdvanceEnrichment.getConfigurationDetails()) {
				atMap = new HashMap<>();
				atMap.put("+ required on A & B number (E.164)", voiceAdvanceEnrichments.getRequiredOnABnumber());
				atMap.put("DTMF Relay support", voiceAdvanceEnrichments.getDtmfRelaySupport());
				atMap.put("Supported SIP Privacy headers", voiceAdvanceEnrichments.getSupportSipPrivacyHeaders());
				atMap.put("Session Keep Alive Timer", voiceAdvanceEnrichments.getSessionKeepAliveTimer());
				atMap.put("Prefix addition", voiceAdvanceEnrichments.getPrefixAddition());
				atMap.put("Transport Protocol", voiceAdvanceEnrichments.getTransport());
				atMap.put("Codec", voiceAdvanceEnrichments.getCodec());
				atMap.put("No Of Concurrent channel", voiceAdvanceEnrichments.getNoOfConcurrentChannel());
				atMap.put("Customer Device IP", voiceAdvanceEnrichments.getCustomerPublicIp());
				atMap.put("Equipment address", voiceAdvanceEnrichments.getEquipmentAddress());
				atMap.put("Routing Topology", voiceAdvanceEnrichments.getRoutingTopology());
				atMap.put("IP Address Space", voiceAdvanceEnrichments.getIpAddressSpace());
				atMap.put("Calls Per Second (CPS)", voiceAdvanceEnrichments.getCallsPerSecond());
				atMap.put("Dial plan logic (Prefix or CLI)", voiceAdvanceEnrichments.getDialPlanLogic());
				atMap.put("FQDN", voiceAdvanceEnrichments.getFqdn());
				atMap.put("Additional Information", voiceAdvanceEnrichments.getAdditionalInformaiton());
				atMap.put("Certificate Authority Support", voiceAdvanceEnrichments.getCertificateAuthoritySupport());
				atMap.put("Termination Number (Working Outpulse)", voiceAdvanceEnrichments.getTerminationNumber());
				atMap.put("Working Outpulse", voiceAdvanceEnrichments.getWorkingOutpulse());
				atMap.put("certificateAuthoritySupportUserValue", Utils.convertObjectToJson(voiceAdvanceEnrichments.getCertificateAuthoritySupportUserValue()));
				componentAndAttributeService.updateAttributes(voiceAdvanceEnrichments.getId(), atMap, AttributeConstants.COMPONENT_WEBEXLICENSE, task.getSiteType());
			}
		}

		if (voiceAdvanceEnrichment.getDocumentIds() != null && !voiceAdvanceEnrichment.getDocumentIds().isEmpty())
			voiceAdvanceEnrichment.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));

		processTaskLogDetails(task, "CLOSED", voiceAdvanceEnrichment.getDelayReason(), null,voiceAdvanceEnrichment);
		return (VoiceAdvanceEnrichmentBean) flowableBaseService.taskDataEntry(task, voiceAdvanceEnrichment);
	}

	/**
	 * This method is used to TD creation for CUG
	 *
	 * @param tdCreationForCugBean
	 * @return TdCreationForCugBean
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public TdCreationForCugBean tdCreationForCug(TdCreationForCugBean tdCreationForCugBean) throws TclCommonException {
		
		Task task = getTaskByIdAndWfTaskId(tdCreationForCugBean.getTaskId(), tdCreationForCugBean.getWfTaskId());

		validateInputs(task, tdCreationForCugBean);

		Map<String, String> atMap = new HashMap<>();

		if (Objects.nonNull(tdCreationForCugBean)) {
			atMap.put("cugDialOut", Utils.convertObjectToJson(tdCreationForCugBean.getCugDialOut()));
			if(Objects.nonNull(tdCreationForCugBean.getCugDialInNumber())) {
				atMap.put("cugDialInNumber", tdCreationForCugBean.getCugDialInNumber());
			}
			if(Objects.nonNull(tdCreationForCugBean.getOnNetDialBack())) {
				atMap.put("onNetDialBack", Utils.convertObjectToJson(tdCreationForCugBean.getOnNetDialBack()));
			}
			atMap.put("cugCreationcompletionDate", tdCreationForCugBean.getCugCreationcompletionDate());
		}
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM, task.getSiteType());

		processTaskLogDetails(task, "CLOSED", tdCreationForCugBean.getDelayReason(), null,tdCreationForCugBean);
		return (TdCreationForCugBean) flowableBaseService.taskDataEntry(task, tdCreationForCugBean);
	}

	/**
	 * This method is used to create DP Form
	 *
	 * @param createDpFormBean
	 * @return CreateDpFormBean
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public CreateDpFormBean createDpForm(CreateDpFormBean createDpFormBean) throws TclCommonException {
		
        Task task = getTaskByIdAndWfTaskId(createDpFormBean.getTaskId(), createDpFormBean.getWfTaskId());
		
		validateInputs(task, createDpFormBean);
		
		Map<String, String> atMap = new HashMap<>();
		
		if (Objects.nonNull(createDpFormBean)) {
			atMap.put("dpFormWebLink", createDpFormBean.getDpFormWebLink());
			atMap.put("isDpFormCreationCompleted", createDpFormBean.getIsDpFormCreationCompleted());
			atMap.put("dpFormCompletionDate", createDpFormBean.getDpFormCompletionDate());
		}
		
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM, task.getSiteType());

		processTaskLogDetails(task, "CLOSED", createDpFormBean.getDelayReason(), null,createDpFormBean);
		return (CreateDpFormBean) flowableBaseService.taskDataEntry(task, createDpFormBean);
	}
	
	@Transactional(readOnly = false)
	public CreateTigerOrderBean createTigerOrder(CreateTigerOrderBean createTigerOrderBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(createTigerOrderBean.getTaskId(),
				createTigerOrderBean.getWfTaskId());

		validateInputs(task, createTigerOrderBean);

		Map<String, String> atMap = new HashMap<>();

		if (Objects.nonNull(createTigerOrderBean)) {
			atMap.put("tigerOrderId", createTigerOrderBean.getTigerOrderId());
			atMap.put("isTigerOrderCreationCompleted", createTigerOrderBean.getIsTigerOrderCreationCompleted());
			atMap.put("tigerOrderCompletionDate", createTigerOrderBean.getTigerOrderCompletionDate());
		}
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap,AttributeConstants.COMPONENT_LM,task.getSiteType());
		processTaskLogDetails(task, "CLOSED", createTigerOrderBean.getDelayReason(), null,createTigerOrderBean);
		return (CreateTigerOrderBean) flowableBaseService.taskDataEntry(task, createTigerOrderBean);
	}
	
	@Transactional(readOnly = false)
	public DedicatedNumberBean dedicatedNumber(DedicatedNumberBean dedicatedNumberBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(dedicatedNumberBean.getTaskId(), dedicatedNumberBean.getWfTaskId());

		validateInputs(task, dedicatedNumberBean);

		Map<String, String> atMap = new HashMap<>();

		if (Objects.nonNull(dedicatedNumberBean)) {
			atMap.put("numberType", dedicatedNumberBean.getNumberType());
			atMap.put("trunkGroup", dedicatedNumberBean.getTrunkGroup());
			atMap.put("numberProcurementCompletionDate", dedicatedNumberBean.getNumberProcurementCompletionDate());
		
			if(Objects.nonNull(dedicatedNumberBean.getListOfNumbers()) && !dedicatedNumberBean.getListOfNumbers().isEmpty()) 
			atMap.put("listOfNumbers", Utils.convertObjectToJson(dedicatedNumberBean.getListOfNumbers()));
		}
			
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap,AttributeConstants.COMPONENT_LM, task.getSiteType());
		
		if(Objects.nonNull(dedicatedNumberBean.getConfigurationDetails())) {
			for(VoiceAdvanceEnrichment voiceAdvanceEnrichments : dedicatedNumberBean.getConfigurationDetails()) {
				atMap = new HashMap<>();
				atMap.put("Termination Number (Working Outpulse)", voiceAdvanceEnrichments.getTerminationNumber());
				componentAndAttributeService.updateAttributes(voiceAdvanceEnrichments.getId(), atMap, AttributeConstants.COMPONENT_WEBEXLICENSE, task.getSiteType());
			}
		}
		
		processTaskLogDetails(task, "CLOSED", dedicatedNumberBean.getDelayReason(), null, dedicatedNumberBean);
		return (DedicatedNumberBean) flowableBaseService.taskDataEntry(task, dedicatedNumberBean);
	
  }

	/**
	 * This method is used for Number Mapping task
	 *
	 * @param numberMappingBean
	 * @return NumberMappingBean
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public NumberMappingBean numberMapping(NumberMappingBean numberMappingBean) throws TclCommonException {
		
        Task task = getTaskByIdAndWfTaskId(numberMappingBean.getTaskId(), numberMappingBean.getWfTaskId());
		
		validateInputs(task, numberMappingBean);
		
		Map<String, String> atMap = new HashMap<>();
		
		if (Objects.nonNull(numberMappingBean)) {
			atMap.put("isNumberMappingCompleted", numberMappingBean.getIsNumberMappingCompleted());
			atMap.put("numberMappingcompletionDate", numberMappingBean.getNumberMappingcompletionDate());
		}
		
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM, task.getSiteType());

		processTaskLogDetails(task, "CLOSED", numberMappingBean.getDelayReason(), null,numberMappingBean);
		return (NumberMappingBean) flowableBaseService.taskDataEntry(task, numberMappingBean);
	}
	
	@Transactional(readOnly = false)
	public CugConfigurationBean cugConfiguration(CugConfigurationBean cugConfigurationBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(cugConfigurationBean.getTaskId(),
				cugConfigurationBean.getWfTaskId());

		validateInputs(task, cugConfigurationBean);

		Map<String, String> atMap = new HashMap<>();

		if (Objects.nonNull(cugConfigurationBean)) {
			atMap.put("isCUGConfigurationCompleted", cugConfigurationBean.getIsCUGConfigurationCompleted());
			atMap.put("cugConfigurationCompletionDate", cugConfigurationBean.getCugConfigurationCompletionDate());
		}
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap,AttributeConstants.COMPONENT_LM,task.getSiteType());
		processTaskLogDetails(task, "CLOSED", cugConfigurationBean.getDelayReason(), null,cugConfigurationBean);
		return (CugConfigurationBean) flowableBaseService.taskDataEntry(task, cugConfigurationBean);
	}

	/*** E2E Voice Testing ***/
	@Transactional(readOnly = false)
	public E2eVoiceTestingBean e2EVoiceTesting(E2eVoiceTestingBean e2EVoiceTestingBean) throws TclCommonException {
		
		Task task = getTaskByIdAndWfTaskId(e2EVoiceTestingBean.getTaskId(), e2EVoiceTestingBean.getWfTaskId());
		validateInputs(task, e2EVoiceTestingBean);

		Map<String, String> atMap = new HashMap<>();

		if (Objects.nonNull(e2EVoiceTestingBean)) {
			atMap.put("e2EVoiceTestingStatus", e2EVoiceTestingBean.getE2EVoiceTestingStatus());
			atMap.put("e2EVoiceTestingCompletedDate", e2EVoiceTestingBean.getE2EVoiceTestingCompletedDate());
		}
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,
				task.getSiteType());

		if (e2EVoiceTestingBean.getDocumentIds() != null && !e2EVoiceTestingBean.getDocumentIds().isEmpty())
			e2EVoiceTestingBean.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		
		List<ScServiceDetail> serviceDetails = scServiceDetailRepository.findByScOrderIdAndErfCatalogProductName(task.getOrderCode(), "UCAAS");
		for(ScServiceDetail serviceDetail : serviceDetails) {
			if(serviceDetail.getErfPrdCatalogOfferingName().equalsIgnoreCase(AttributeConstants.WEBEX_LICENSE_OFFERNING_NAME)) {
				atMap = new HashMap<>();
				ScComponent scComponent = scComponentRepository
						.findFirstByScServiceDetailIdAndComponentNameAndSiteTypeOrderByIdAsc(task.getServiceId(), AttributeConstants.COMPONENT_LM, task.getSiteType());
				if (scComponent != null && scComponent.getScComponentAttributes() != null && !scComponent.getScComponentAttributes().isEmpty()) {
					for (ScComponentAttribute compAttr : scComponent.getScComponentAttributes()) {
						atMap.put(compAttr.getAttributeName(), compAttr.getAttributeValue());
					}
				}
				componentAndAttributeService.updateAttributes(serviceDetail.getId(), atMap, AttributeConstants.COMPONENT_LM, task.getSiteType());
			}
		}
		
		processTaskLogDetails(task, "CLOSED", e2EVoiceTestingBean.getDelayReason(), null, e2EVoiceTestingBean);
		return (E2eVoiceTestingBean) flowableBaseService.taskDataEntry(task, e2EVoiceTestingBean);
	}
	
	/**
	 * returnExcel - method holding functionality to export order details as LR
	 * Excel
	 *
	 * @param orderId
	 * @param response
	 * @throws IOException
	 * @throws TclCommonException
	 */
	public HttpServletResponse returnExcel(Integer orderId, HttpServletResponse response) throws IOException, TclCommonException {

		Objects.requireNonNull(orderId, ORDER_ID_NULL_MESSAGE);
		Objects.requireNonNull(response, GscO2CConstants.HTTP_SERVLET_RESPONSE_NULL_MESSAGE);
		List<ExcelBean> listBook = getExcelList(orderId);
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet();

		sheet.setColumnWidth(50000, 50000);
		int rowCount = 0;

		for (ExcelBean aBook : listBook) {
			Row row = sheet.createRow(rowCount);
			writeBook(aBook, row);
			rowCount++;
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		workbook.write(outByteStream);
		byte[] outArray = outByteStream.toByteArray();
		String fileName = "Export To Tiger-" + orderId + ".xls";
		response.reset();
		response.setContentType("application/ms-excel");
		response.setContentLength(outArray.length);
		response.setHeader("Expires:", "0");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		workbook.close();
		try {
			FileCopyUtils.copy(outArray, response.getOutputStream());
		} catch (Exception e) {

		}

		outByteStream.flush();
		outByteStream.close();
		return response;
	}

	/**
	 * getExcelList - converts order into list of excel bean
	 *
	 * @param orderId
	 * @return
	 * @throws TclCommonException
	 */
	public List<ExcelBean> getExcelList(Integer orderId) throws TclCommonException {

		Objects.requireNonNull(orderId, ORDER_ID_NULL_MESSAGE);
		List<ExcelBean> listBook = new ArrayList<>();
		Map<String,Object> scorder=scOrderRepository.findByScOrderId(orderId);
		String orderCode=(String)scorder.get("op_order_code");
		ScOrder order= scOrderRepository.findByOpOrderCodeAndIsActive(orderCode, "Y");
		Objects.requireNonNull(order, ORDER_NULL_MESSAGE);

		ExcelBean info = new ExcelBean(GscO2CConstants.LR_SECTION,
				GscO2CConstants.ATTRIBUTE_REQUIRED, GscO2CConstants.REMARKS);
		info.setOrder(0);
		info.setGscQuoteId(0);
		listBook.add(info);
		Map<String,String> orderAttributeValues=getOrderAttributeValues(order);
		ScContractInfo contractInfo=scContractInfoRepository.findFirstByScOrder_id(order.getId());

		createOrderDetails(listBook,order, orderAttributeValues, contractInfo);
		for(ScServiceDetail scServiceDetail:order.getScServiceDetails()) {
			if (scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("UCAAS")) {
				Map<String, String> scServiceAttributes = commonFulfillmentUtils.getServiceAttributesAttributes(
						scServiceDetail.getScServiceAttributes().stream().collect(Collectors.toList()));
				createWebExAttributes(listBook,order, scServiceAttributes, contractInfo);
				break;
			}
		}

		List<GscOfferingBean> gscOfferingBeanList=groupingGscOfferings(order,contractInfo.getOrderTermInMonths());



		gscOfferingBeanList.stream().forEach(gscOfferingBean -> {
			createGscDetails(listBook, gscOfferingBean);
			gscOfferingBean.getGscConfigurationDetails().stream().forEach(scServiceDetail -> {
				createQuoteGscConfigurationDetails(listBook, scServiceDetail);
				Map<String, String> scComponentAttributesAMap = commonFulfillmentUtils
						.getComponentAttributes(scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A");
				Map<String, String> scComponentAttributesWebExMap = commonFulfillmentUtils
						.getComponentAttributes(scServiceDetail.getId(), AttributeConstants.WEBEX_LICENSE, "A");
				createQuoteGscConfigurationAttributesDetails(listBook,gscOfferingBean.getProductName().trim(),scComponentAttributesAMap);
				createQuoteGscConfigurationAttributesDetails(listBook,gscOfferingBean.getAccessType().trim(),scComponentAttributesAMap);
				if(gscOfferingBean.getAccessType().equalsIgnoreCase("MPLS"))
				createCugDetails(listBook, scServiceDetail, scComponentAttributesAMap,scComponentAttributesWebExMap);
			});
		});

		for(ScServiceDetail scServiceDetail:order.getScServiceDetails()) {
			if (scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("GVPN")) {
				Map<String, String> scServiceAttributes = commonFulfillmentUtils.getServiceAttributesAttributes(
						scServiceDetail.getScServiceAttributes().stream().collect(Collectors.toList()));
				createGVPNAttributes(listBook,scServiceAttributes,orderAttributeValues,scServiceDetail);
				break;
			}

		}

		return listBook;
	}


	/**
	 * writeBook - writes data into excel
	 *
	 * @param aBook
	 * @param row
	 * @throws TclCommonException
	 */
	public void writeBook(ExcelBean aBook, Row row) throws TclCommonException {
		if (Objects.isNull(aBook) || Objects.isNull(row)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}
		Cell cell = row.createCell(1);
		if (aBook.getGscQuoteId() != null) {
			if (aBook.getGscQuoteId() == 0) {
				cell.setCellValue(aBook.getLrSection());
			} else if (aBook.getGscQuoteId() != 0) {
				cell.setCellValue(aBook.getLrSection() + ":" + aBook.getGscQuoteId());
			}

		} else {
			if (aBook.getGscQuoteDetailId() != null) {
				if (aBook.getGscQuoteDetailId() == 0) {
					cell.setCellValue(aBook.getLrSection());
				} else if (aBook.getGscQuoteDetailId() != 0) {
					cell.setCellValue(aBook.getLrSection() + ":" + aBook.getGscQuoteDetailId());
				}
			}
		}
		cell = row.createCell(2);
		cell.setCellValue(aBook.getAttributeName());

		cell = row.createCell(3);
		cell.setCellValue(aBook.getAttributeValue());

	}

	/**
	 * createOrderDetails - extracts order related details as excel bean
	 * @param listBook
	 * @param scOrder
	 * @param attributeValues
	 * @param contractInfo
	 */

	private void createOrderDetails(List<ExcelBean> listBook,ScOrder scOrder,Map<String,String> attributeValues,ScContractInfo contractInfo) {
		ExcelBean orderDetails = new ExcelBean(GscO2CConstants.ORDER_DETAILS,
				GscO2CConstants.ORDER_REF_ID, scOrder.getOpOrderCode());
		orderDetails.setOrder(1);
		orderDetails.setGscQuoteId(0);
		listBook.add(orderDetails);

		if (scOrder.getOrderType().equalsIgnoreCase(GscO2CConstants.ORDER_TYPE_MACD)) {
			ExcelBean orderSecIdDetails = new ExcelBean(GscO2CConstants.ORDER_DETAILS,
					GscO2CConstants.ORDER_SEC_ID, attributeValues.containsKey(GscO2CConstants.CUSTOMER_SECS_ID)
					? attributeValues.get(GscO2CConstants.CUSTOMER_SECS_ID)
					: GscO2CConstants.NOT_AVAILABLE);
			orderSecIdDetails.setOrder(1);
			orderSecIdDetails.setGscQuoteId(0);
			listBook.add(orderSecIdDetails);
		}else {
			ExcelBean orderSecIdDetails = new ExcelBean(GscO2CConstants.ORDER_DETAILS,
					GscO2CConstants.ORDER_SEC_ID, attributeValues.containsKey(GscO2CConstants.ORG_NO)
					? attributeValues.get(GscO2CConstants.ORG_NO)
					: GscO2CConstants.NOT_AVAILABLE);
			orderSecIdDetails.setOrder(1);
			orderSecIdDetails.setGscQuoteId(0);
			listBook.add(orderSecIdDetails);
		}

		ExcelBean supplierInfo = new ExcelBean(GscO2CConstants.ORDER_DETAILS,
				GscO2CConstants.GSC_SUPPLIER_NAME,
				attributeValues.containsKey(GscO2CConstants.GSC_SUPPLIER_NAME)
						? attributeValues.get(GscO2CConstants.GSC_SUPPLIER_NAME)
						: GscO2CConstants.TCL);
		supplierInfo.setOrder(1);
		supplierInfo.setGscQuoteId(0);
		listBook.add(supplierInfo);

		ExcelBean ownerInfo = new ExcelBean(GscO2CConstants.ORDER_DETAILS,
				GscO2CConstants.Le_Owner, contractInfo.getAccountManager());
		ownerInfo.setOrder(1);
		ownerInfo.setGscQuoteId(0);
		listBook.add(ownerInfo);


			ExcelBean customerInfo = new ExcelBean(GscO2CConstants.ORDER_DETAILS,
					GscO2CConstants.CUSTOMER_LE_NAME, contractInfo.getErfCustLeName());
			customerInfo.setOrder(1);
			customerInfo.setGscQuoteId(0);
			listBook.add(customerInfo);



		ExcelBean orderType = new ExcelBean(GscO2CConstants.ORDER_DETAILS,
				GscO2CConstants.ORDER_TYPE, scOrder.getOrderType());
		orderType.setOrder(1);
		orderType.setGscQuoteId(0);
		listBook.add(orderType);



			ExcelBean billingAddress = new ExcelBean(GscO2CConstants.ORDER_DETAILS,
					GscO2CConstants.CUSTOMER_CONTRACTING_BILLING_ADDRESS,
					attributeValues.containsKey(GscO2CConstants.BILLING_ADDRESS)
							? attributeValues.get(GscO2CConstants.BILLING_ADDRESS)
							: "NA");
			billingAddress.setOrder(1);
			billingAddress.setGscQuoteId(0);
			listBook.add(billingAddress);


		ExcelBean paymentCurrency = new ExcelBean(GscO2CConstants.ORDER_DETAILS,
				GscO2CConstants.PAYMENT_CURRENCY, attributeValues.containsKey(GscO2CConstants.PAYMENT_CURRENCY)
				? attributeValues.get(GscO2CConstants.PAYMENT_CURRENCY)
				: "NA");
		paymentCurrency.setOrder(1);
		paymentCurrency.setGscQuoteId(0);
		listBook.add(paymentCurrency);

		ExcelBean paymentMethod = new ExcelBean(GscO2CConstants.ORDER_DETAILS,
				GscO2CConstants.PAYMENT_METHOD,
				attributeValues.get(GscO2CConstants.INVOICE_METHOD));
		paymentMethod.setOrder(1);
		paymentMethod.setGscQuoteId(0);
		listBook.add(paymentMethod);

		ExcelBean paymentOptions = new ExcelBean(GscO2CConstants.ORDER_DETAILS,
				GscO2CConstants.PAYMENT_OPTIONS,
				attributeValues.getOrDefault(GscO2CConstants.PAYMENT_OPTIONS, GscO2CConstants.NA));
		paymentOptions.setOrder(1);
		paymentOptions.setGscQuoteId(0);
		listBook.add(paymentOptions);

		ExcelBean paymentTerm = new ExcelBean(GscO2CConstants.ORDER_DETAILS,
				GscO2CConstants.PAYMENT_TERM_VALUE, contractInfo.getPaymentTerm());
		paymentTerm.setOrder(1);
		paymentTerm.setGscQuoteId(0);
		listBook.add(paymentTerm);

		ExcelBean autoCofNumber = new ExcelBean(GscO2CConstants.ORDER_DETAILS,
				GscO2CConstants.AUTO_COF_NUMBER, scOrder.getOpOrderCode());
		autoCofNumber.setOrder(1);
		autoCofNumber.setGscQuoteId(0);
		listBook.add(autoCofNumber);

		ExcelBean sfdc = new ExcelBean(GscO2CConstants.ORDER_DETAILS,
				GscO2CConstants.SFDC_ID, scOrder.getTpsSfdcOptyId());
		sfdc.setOrder(1);
		sfdc.setGscQuoteId(0);
		listBook.add(sfdc);




		ExcelBean book6 = new ExcelBean(GscO2CConstants.ORDER_DETAILS,
				GscO2CConstants.CONTRACT_TERMS, contractInfo.getOrderTermInMonths() +" months");
		book6.setOrder(1);
		book6.setGscQuoteId(0);
		listBook.add(book6);
		if(Objects.nonNull(scOrder.getOpportunityClassification())) {
			ExcelBean book8 = new ExcelBean(GscO2CConstants.ORDER_DETAILS,
					GscO2CConstants.OPPORTUNITY_CLASSIFICATION,
					scOrder.getOpportunityClassification());
			book8.setOrder(1);
			book8.setGscQuoteId(0);
			listBook.add(book8);
		}else {

			ExcelBean book8 = new ExcelBean(GscO2CConstants.ORDER_DETAILS,
					GscO2CConstants.OPPORTUNITY_CLASSIFICATION,
					GscO2CConstants.SELL_TO);
			book8.setOrder(1);
			book8.setGscQuoteId(0);
			listBook.add(book8);
		}
		StringBuilder gstDetail = new StringBuilder();

		String gstAddress = "";
		String gstNo = "";


		if (attributeValues.containsKey(GscO2CConstants.LE_STATE_GST_NUMBER)) {
			gstNo= attributeValues.get(GscO2CConstants.LE_STATE_GST_NUMBER);

		}else if (attributeValues.containsKey(GscO2CConstants.GST_NUM)) {
			gstNo= attributeValues.get(GscO2CConstants.GST_NUM);

		}
		if (attributeValues.containsKey(GscO2CConstants.LE_STATE_GST_ADDRESS)) {
			gstAddress= attributeValues.get(GscO2CConstants.LE_STATE_GST_ADDRESS);
		}else if (attributeValues.containsKey(GscO2CConstants.GST_ADDR)) {
			gstAddress= attributeValues.get(GscO2CConstants.GST_ADDR);
		}

		if (gstDetail == null || gstDetail.toString().equalsIgnoreCase(CommonConstants.EMPTY)) {
			gstDetail.append(Objects.nonNull(gstNo) ? gstNo : CommonConstants.EMPTY);
			gstDetail.append(",")
					.append(Objects.nonNull(gstAddress) ? gstAddress : "NA");
		} else {
			if (gstDetail.toString().endsWith(CommonConstants.RIGHT_SLASH)) {
				gstDetail.setLength(gstDetail.length() - 1);
			}
		}


			ExcelBean book9 = new ExcelBean(GscO2CConstants.ORDER_DETAILS,
					GscO2CConstants.CUSTOMER_LE_GST,
					Objects.nonNull(gstDetail)?gstDetail.toString():CommonConstants.EMPTY);
			book9.setOrder(1);
			book9.setGscQuoteId(0);
			listBook.add(book9);


		ExcelBean book10 = new ExcelBean(GscO2CConstants.ORDER_DETAILS,
				GscO2CConstants.ACCOUNT_MANAGER,
				attributeValues.containsKey(GscO2CConstants.ACCOUNT_MANAGER)
						? attributeValues.get(GscO2CConstants.ACCOUNT_MANAGER)
						: "");
		book10.setOrder(1);
		book10.setGscQuoteId(0);
		listBook.add(book10);

		ExcelBean book12 = new ExcelBean(GscO2CConstants.ORDER_DETAILS,
				GscO2CConstants.CREDIT_LIMIT,
				attributeValues.containsKey(GscO2CConstants.CREDIT_LIMIT)
						? attributeValues.get(GscO2CConstants.CREDIT_LIMIT)
						: "NA");
		book12.setOrder(1);
		book12.setGscQuoteId(0);
		listBook.add(book12);

		ExcelBean book13 = new ExcelBean(GscO2CConstants.ORDER_DETAILS,
				GscO2CConstants.ADVANCE_AMOUNT,
				attributeValues.containsKey(GscO2CConstants.ADVANCE_AMOUNT)
						? attributeValues.get(GscO2CConstants.ADVANCE_AMOUNT)
						: "NA");
		book13.setOrder(1);
		book13.setGscQuoteId(0);
		listBook.add(book13);

		ExcelBean book14 = new ExcelBean(GscO2CConstants.ORDER_DETAILS,
				GscO2CConstants.BILLING_RATIO,
				attributeValues.containsKey(GscO2CConstants.MST_BILLING_RATIO)
						? attributeValues.get(GscO2CConstants.MST_BILLING_RATIO)
						: "NA");
		book14.setOrder(1);
		book14.setGscQuoteId(0);
		listBook.add(book14);

		ExcelBean book15 = new ExcelBean(GscO2CConstants.ORDER_DETAILS,
				GscO2CConstants.PO_NUMBER,
				attributeValues.containsKey(GscO2CConstants.PO_NUMBER_KEY)
						? attributeValues.get(GscO2CConstants.PO_NUMBER_KEY)
						: "NA");
		book15.setOrder(1);
		book15.setGscQuoteId(0);
		listBook.add(book15);

		ExcelBean book16 = new ExcelBean(GscO2CConstants.ORDER_DETAILS,
				GscO2CConstants.PO_DATE, attributeValues.containsKey(GscO2CConstants.PO_DATE_KEY)
				? attributeValues.get(GscO2CConstants.PO_DATE_KEY)
				: "NA");
		book16.setOrder(1);
		book16.setGscQuoteId(0);
		listBook.add(book16);

		ExcelBean book17 = new ExcelBean(GscO2CConstants.ORDER_DETAILS,
				GscO2CConstants.DEPARTMENT_BILLING,
				attributeValues.containsKey(GscO2CConstants.DEPARTMENT_BILLING)
						? attributeValues.get(GscO2CConstants.DEPARTMENT_BILLING)
						: CommonConstants.NO);
		book17.setOrder(1);
		book17.setGscQuoteId(0);
		listBook.add(book17);

		ExcelBean book18 = new ExcelBean(GscO2CConstants.ORDER_DETAILS,
				GscO2CConstants.DEPARTMENT_NAME,
				attributeValues.containsKey(GscO2CConstants.DEPARTMENT_NAME)
						? attributeValues.get(GscO2CConstants.DEPARTMENT_NAME)
						: "NA");
		book18.setOrder(1);
		book18.setGscQuoteId(0);
		listBook.add(book18);


	}

	/**
	 * getAttributeValues - retrieves attribute values for an ScOrder
	 *
	 * @param ScOrder
	 * @return
	 * @throws TclCommonException
	 */
	public Map<String, String> getOrderAttributeValues(ScOrder order) throws TclCommonException {
		Map<String, String> attributeMap = new HashMap<>();
		try {

			Objects.requireNonNull(order, ORDER_NULL_MESSAGE);

			order.getScOrderAttributes().forEach(attr -> {
				attributeMap.put(attr.getAttributeName(), attr.getAttributeValue());

			});

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.ATTRIBUTE_EMPTY, e, ResponseResource.R_CODE_ERROR);
		}
		return attributeMap;
	}

	/**
	 * Method to group gsc offerings
	 * @param order
	 * @param contractTerm
	 * @return
	 */
	public List<GscOfferingBean> groupingGscOfferings(ScOrder order,Double contractTerm)
	{

		Double finalMrc = 0D;
		Double finalNrc = 0D;
		Double tcv=0D;
		List<ScServiceDetail> scServiceDetails=new ArrayList<>();
		Set<Integer> offeringIdSet=new HashSet<>();
		List<GscOfferingBean> gscOfferingBeans=new ArrayList<>();

		for(ScServiceDetail scServiceDetail:order.getScServiceDetails())
		{
			if(scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("GSIP"))
			{
				scServiceDetails.add(scServiceDetail);
				offeringIdSet.add(scServiceDetail.getErfPrdCatalogOfferingId());
			}
		}

		GscOfferingBean gscOfferingData=null;
		for(Integer offeringId:offeringIdSet)
		{
			for(ScServiceDetail scServiceDetail:scServiceDetails)
			{
				if(offeringId==scServiceDetail.getErfPrdCatalogOfferingId())
				{
					if(Objects.isNull(gscOfferingData))
					 gscOfferingData=new GscOfferingBean();
					if(scServiceDetail != null) {
						LOGGER.info("Tiger Export - ScServiceDetail: {}, finalMrc: {}, finalNrc: {}", scServiceDetail, finalMrc, finalNrc);
						if(scServiceDetail.getMrc() != null)
							finalMrc=finalMrc+scServiceDetail.getMrc();
						if(scServiceDetail.getNrc() != null)
							finalNrc=finalNrc+scServiceDetail.getNrc();
						gscOfferingData.getGscConfigurationDetails().add(scServiceDetail);
						
					}
					
				}
			}
			tcv = (contractTerm * this.setPrecision(finalMrc, 2))
					+ this.setPrecision(finalNrc, 2);
			String offeringName=gscOfferingData.getGscConfigurationDetails().stream().findFirst().get().getErfPrdCatalogOfferingName();
//			String productName=null;
//			String on=null;
//			String accessType=null;
			if(Objects.nonNull(offeringName) && offeringName.toLowerCase().contains("on")) {
				LOGGER.info("Tiger Export offeringName: {}", offeringName);
				String[] processedOfferingName = offeringName.toLowerCase().split("on");
//				StringTokenizer tokens = new StringTokenizer(offeringName, " ");
//				while (tokens.hasMoreTokens()) {
//					if(offeringName.contains("Global")) {
//						productName = tokens.nextToken();
//						productName = productName + " " + tokens.nextToken();
//					} else {
//						productName = tokens.nextToken();
//					}
//					on = tokens.nextToken();
//					accessType = tokens.nextToken();
//				}
				
				if(processedOfferingName.length==2) {
					LOGGER.info("Tiger Export ProductName: {}, accessType: {}", processedOfferingName[0], processedOfferingName[1]);	
					gscOfferingData.setProductName(processedOfferingName[0]);
					gscOfferingData.setAccessType(processedOfferingName[1].trim());
				}else {
					LOGGER.info("Tiger Export else ProductName: {}", offeringName);
					gscOfferingData.setProductName(offeringName);
					gscOfferingData.setAccessType("");
				}
			} 
				
				gscOfferingData.setMrc(finalMrc);
				gscOfferingData.setNrc(finalNrc);
				gscOfferingData.setTcv(tcv);
				gscOfferingBeans.add(gscOfferingData);
				gscOfferingData=null;
				finalMrc=0D;
				finalNrc=0D;
				tcv=0D;


		}


		return gscOfferingBeans;
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
	 * Method to create quote gsc details
	 *
	 * @param listBook
	 * @param gscQuote
	 */
	private void createGscDetails(List<ExcelBean> listBook, GscOfferingBean gscOfferingBean) {
		ExcelBean book17 = new ExcelBean(GscO2CConstants.GSC_QUOTE_DETAILS,GscO2CConstants.PRODUCT_NAME, gscOfferingBean.getProductName());
		book17.setOrder(1);
		book17.setGscQuoteId(0);
		listBook.add(book17);

		ExcelBean book18 = new ExcelBean(GscO2CConstants.GSC_QUOTE_DETAILS, GscO2CConstants.ACCESS_TYPE, gscOfferingBean.getAccessType());
		book18.setOrder(1);
		book18.setGscQuoteId(0);
		listBook.add(book18);

		String mrc = "0.0";
		if (Objects.nonNull(gscOfferingBean.getMrc())) {
			mrc = String.format("%.2f", gscOfferingBean.getMrc());
		}

		ExcelBean book19 = new ExcelBean(GscO2CConstants.GSC_QUOTE_DETAILS, GscO2CConstants.GSC_QUOTE_MRC, mrc);
		book19.setOrder(1);
		book19.setGscQuoteId(0);
		listBook.add(book19);

		String nrc = "0.0";
		if (Objects.nonNull(gscOfferingBean.getNrc())) {
			nrc = String.format("%.2f", gscOfferingBean.getNrc());
		}
		ExcelBean book20 = new ExcelBean(GscO2CConstants.GSC_QUOTE_DETAILS, GscO2CConstants.GSC_QUOTE_NRC, nrc);
		book20.setOrder(1);
		book20.setGscQuoteId(0);
		listBook.add(book20);

		String tcv = "0.0";
		if (Objects.nonNull(gscOfferingBean.getTcv())) {
			tcv = String.format("%.2f", gscOfferingBean.getTcv());
		}
		ExcelBean book21 = new ExcelBean(GscO2CConstants.GSC_QUOTE_DETAILS, GscO2CConstants.GSC_QUOTE_TCV, tcv);
		book21.setOrder(1);
		book21.setGscQuoteId(0);
		listBook.add(book21);
	}

	private void createQuoteGscConfigurationDetails(List<ExcelBean> listBook, ScServiceDetail scServiceDetail) {
		ExcelBean book22 = new ExcelBean(GscO2CConstants.GSC_QUOTE_CONFIGURATION_DETAILS, GscO2CConstants.SOURCE_COUNTRY, scServiceDetail.getSourceCountry());
		book22.setOrder(2);
		book22.setGscQuoteDetailId(scServiceDetail.getId());
		listBook.add(book22);

		ExcelBean book23 = new ExcelBean(GscO2CConstants.GSC_QUOTE_CONFIGURATION_DETAILS, GscO2CConstants.DESTINATION_COUNTRY, scServiceDetail.getDestinationCountry());
		book23.setOrder(2);
		book23.setGscQuoteDetailId(scServiceDetail.getId());
		listBook.add(book23);

		String mrc = "0.0";
		if (Objects.nonNull(scServiceDetail.getMrc())) {
			mrc = String.format("%.2f", scServiceDetail.getMrc());
		}
		ExcelBean book24 = new ExcelBean(GscO2CConstants.GSC_QUOTE_CONFIGURATION_DETAILS, GscO2CConstants.GSC_QUOTE_DETAIL_MRC, mrc);
		book24.setOrder(2);
		book24.setGscQuoteDetailId(scServiceDetail.getId());
		listBook.add(book24);

		String nrc = "0.0";
		if (Objects.nonNull(scServiceDetail.getNrc())) {
			nrc = String.format("%.2f", scServiceDetail.getNrc());
		}
		ExcelBean book25 = new ExcelBean(GscO2CConstants.GSC_QUOTE_CONFIGURATION_DETAILS, GscO2CConstants.GSC_QUOTE_DETAIL_NRC, nrc);
		book25.setOrder(2);
		book25.setGscQuoteDetailId(scServiceDetail.getId());
		listBook.add(book25);
	}

	/**
	 * Method to create quotegscconfigurationAttributeDetails
	 * @param listBook
	 * @param productName
	 * @param scComponentAttributesAMap
	 */
	private void createQuoteGscConfigurationAttributesDetails(List<ExcelBean> listBook, String productName, Map<String, String> scComponentAttributesAMap) {


		switch (productName) {

			case GscO2CConstants.ITFS: {

				String rpmFixed = scComponentAttributesAMap.get(GscO2CConstants.RATE_PER_MINUTE_FIXED);
				if(Objects.nonNull(rpmFixed)) {
					ExcelBean book26 = new ExcelBean(GscO2CConstants.GSC_ATTRIBUTES_DETAILS,
							GscO2CConstants.RATE_PER_MINUTE_FIXED,
							rpmFixed);
					book26.setOrder(3);
					book26.setGscQuoteDetailId(0);
					listBook.add(book26);
				}

				String rpmMobile = scComponentAttributesAMap.get(GscO2CConstants.RATE_PER_MINUTE_MOBILE);
				if(Objects.nonNull(rpmMobile)) {
				ExcelBean book27 = new ExcelBean(GscO2CConstants.GSC_ATTRIBUTES_DETAILS,
						GscO2CConstants.RATE_PER_MINUTE_MOBILE,
						rpmMobile);
				book27.setOrder(3);
				book27.setGscQuoteDetailId(0);
				listBook.add(book27);}

				String rpmSpecial= scComponentAttributesAMap.get(GscO2CConstants.RATE_PER_MINUTE_SPECIAL);
				if(Objects.nonNull(rpmSpecial)) {
					ExcelBean book28 = new ExcelBean(GscO2CConstants.GSC_ATTRIBUTES_DETAILS,
							GscO2CConstants.RATE_PER_MINUTE_SPECIAL,
							rpmSpecial);
					book28.setOrder(3);
					book28.setGscQuoteDetailId(0);
					listBook.add(book28);
				}

				String quantityOfnumbers = scComponentAttributesAMap.get(GscO2CConstants.QUANTITY_OF_NUMBERS);
				if(Objects.nonNull(quantityOfnumbers))
				{
					ExcelBean qnBook = new ExcelBean(GscO2CConstants.GSC_ATTRIBUTES_DETAILS,
							GscO2CConstants.QUANTITY_OF_NUMBERS,
							quantityOfnumbers);
					qnBook.setOrder(3);
					qnBook.setGscQuoteDetailId(0);
					listBook.add(qnBook);
				}

				String portedNumbers = scComponentAttributesAMap.get(GscO2CConstants.LIST_OF_NUMBERS_TO_BE_PORTED);
				if(Objects.nonNull(portedNumbers)) {
					ExcelBean portBook = new ExcelBean(GscO2CConstants.GSC_ATTRIBUTES_DETAILS,
							GscO2CConstants.LIST_OF_NUMBERS_TO_BE_PORTED, portedNumbers);
					portBook.setOrder(3);
					portBook.setGscQuoteDetailId(0);
					listBook.add(portBook);
				}
				String terminationNumber = scComponentAttributesAMap.get(GscO2CConstants.TERMINATION_NUMBER_WORKING_OUTPULSE);
				if (Objects.nonNull(terminationNumber)) {
					ExcelBean terminationBook = new ExcelBean(GscO2CConstants.GSC_ATTRIBUTES_DETAILS,
							GscO2CConstants.TERMINATION_NUMBER_WORKING_OUTPULSE, terminationNumber);
					terminationBook.setOrder(3);
					terminationBook.setGscQuoteDetailId(0);
					listBook.add(terminationBook);
				}
				break;
			}
			case GscO2CConstants.LNS: {


				String rpmFixed = scComponentAttributesAMap.get(GscO2CConstants.RATE_PER_MINUTE_FIXED);
				if(Objects.nonNull(rpmFixed)) {
					ExcelBean book26 = new ExcelBean(GscO2CConstants.GSC_ATTRIBUTES_DETAILS,
							GscO2CConstants.RATE_PER_MINUTE_FIXED,
							rpmFixed);
					book26.setOrder(3);
					book26.setGscQuoteDetailId(0);
					listBook.add(book26);
				}

				String rpmMobile = scComponentAttributesAMap.get(GscO2CConstants.RATE_PER_MINUTE_MOBILE);
				if(Objects.nonNull(rpmMobile)) {
					ExcelBean book27 = new ExcelBean(GscO2CConstants.GSC_ATTRIBUTES_DETAILS,
							GscO2CConstants.RATE_PER_MINUTE_MOBILE,
							rpmMobile);
					book27.setOrder(3);
					book27.setGscQuoteDetailId(0);
					listBook.add(book27);}

				String rpmSpecial= scComponentAttributesAMap.get(GscO2CConstants.RATE_PER_MINUTE_SPECIAL);
				if(Objects.nonNull(rpmSpecial)) {
					ExcelBean book28 = new ExcelBean(GscO2CConstants.GSC_ATTRIBUTES_DETAILS,
							GscO2CConstants.RATE_PER_MINUTE_SPECIAL,
							rpmSpecial);
					book28.setOrder(3);
					book28.setGscQuoteDetailId(0);
					listBook.add(book28);
				}

				String quantityOfnumbers = scComponentAttributesAMap.get(GscO2CConstants.QUANTITY_OF_NUMBERS);
				if(Objects.nonNull(quantityOfnumbers))
				{
					ExcelBean qnBook = new ExcelBean(GscO2CConstants.GSC_ATTRIBUTES_DETAILS,
							GscO2CConstants.QUANTITY_OF_NUMBERS,
							quantityOfnumbers);
					qnBook.setOrder(3);
					qnBook.setGscQuoteDetailId(0);
					listBook.add(qnBook);
				}

				String portedNumbers = scComponentAttributesAMap.get(GscO2CConstants.LIST_OF_NUMBERS_TO_BE_PORTED);
				if(Objects.nonNull(portedNumbers)) {
					ExcelBean portBook = new ExcelBean(GscO2CConstants.GSC_ATTRIBUTES_DETAILS,
							GscO2CConstants.LIST_OF_NUMBERS_TO_BE_PORTED, portedNumbers);
					portBook.setOrder(3);
					portBook.setGscQuoteDetailId(0);
					listBook.add(portBook);
				}
				String terminationNumber = scComponentAttributesAMap.get(GscO2CConstants.TERMINATION_NUMBER_WORKING_OUTPULSE);
				if (Objects.nonNull(terminationNumber)) {
					ExcelBean terminationBook = new ExcelBean(GscO2CConstants.GSC_ATTRIBUTES_DETAILS,
							GscO2CConstants.TERMINATION_NUMBER_WORKING_OUTPULSE, terminationNumber);
					terminationBook.setOrder(3);
					terminationBook.setGscQuoteDetailId(0);
					listBook.add(terminationBook);
				}

				break;
			}
			case GscO2CConstants.GLOBAL_OUTBOUND: {

				String callType = scComponentAttributesAMap.get(GscO2CConstants.CALLING_SERVICE_TYPE);
				ExcelBean callServiceBook = new ExcelBean(GscO2CConstants.GSC_ATTRIBUTES_DETAILS,
						GscO2CConstants.CALLING_SERVICE_TYPE,
						callType);
				callServiceBook.setOrder(3);
				callServiceBook.setGscQuoteDetailId(0);
				listBook.add(callServiceBook);

				String terminationName = scComponentAttributesAMap.get(GscO2CConstants.TERM_NAME);
				ExcelBean globalOutboundTermNameBook = new ExcelBean(
						GscO2CConstants.GSC_ATTRIBUTES_DETAILS,
						GscO2CConstants.TERMINATION_NAME,
						terminationName);
				globalOutboundTermNameBook.setOrder(3);
				globalOutboundTermNameBook.setGscQuoteDetailId(0);
				listBook.add(globalOutboundTermNameBook);

				String terminationRate = scComponentAttributesAMap.get(GscO2CConstants.TERM_RATE);
				ExcelBean globalTermRateBook = new ExcelBean(GscO2CConstants.GSC_ATTRIBUTES_DETAILS,
						GscO2CConstants.TERMINATION_RATE,
						terminationRate);
				globalTermRateBook.setOrder(3);
				globalTermRateBook.setGscQuoteDetailId(0);
				listBook.add(globalTermRateBook);

				String phoneType = scComponentAttributesAMap.get(GscO2CConstants.PHONE_TYPE);
				ExcelBean globalPhoneTypeBook = new ExcelBean(GscO2CConstants.GSC_ATTRIBUTES_DETAILS,
						GscO2CConstants.PHONE_TYPE,
						phoneType);
				globalPhoneTypeBook.setOrder(3);
				globalPhoneTypeBook.setGscQuoteDetailId(0);
				listBook.add(globalPhoneTypeBook);

				String terminationNumber = scComponentAttributesAMap.get(GscO2CConstants.TERMINATION_NUMBER_WORKING_OUTPULSE);
				if (Objects.nonNull(terminationNumber)) {
					ExcelBean terminationBook = new ExcelBean(GscO2CConstants.GSC_ATTRIBUTES_DETAILS,
							GscO2CConstants.TERMINATION_NUMBER_WORKING_OUTPULSE, terminationNumber);
					terminationBook.setOrder(3);
					terminationBook.setGscQuoteDetailId(0);
					listBook.add(terminationBook);
				}
				break;
			}
			case GscO2CConstants.MPLS: {

				String requiredNumber = scComponentAttributesAMap.get(GscO2CConstants.REQUIRED_ON_A_AND_B_NUMBER);
				ExcelBean book26 = new ExcelBean(GscO2CConstants.GSC_SIP_ATTRIBUTES_DETAILS,
						GscO2CConstants.REQUIRED_ON_A_AND_B_NUMBER,
						requiredNumber);
				book26.setOrder(3);
				book26.setGscQuoteDetailId(0);
				listBook.add(book26);

				String dtmf = scComponentAttributesAMap.get(GscO2CConstants.DTMF_RELAY_SUPPORT);
				ExcelBean book27 = new ExcelBean(GscO2CConstants.GSC_SIP_ATTRIBUTES_DETAILS,
						GscO2CConstants.DTMF_RELAY_SUPPORT,
						dtmf);
				book27.setOrder(3);
				book27.setGscQuoteDetailId(0);
				listBook.add(book27);

				String privacyHeader = scComponentAttributesAMap.get(GscO2CConstants.SUPPORTED_SIP_PRIVACY_HEADERS);
				ExcelBean book28 = new ExcelBean(GscO2CConstants.GSC_SIP_ATTRIBUTES_DETAILS,
						GscO2CConstants.SUPPORTED_SIP_PRIVACY_HEADERS,
						privacyHeader);
				book28.setOrder(3);
				book28.setGscQuoteDetailId(0);
				listBook.add(book28);
				/* GscConstants.ADDITIONAL_INFORMATION */
				String sessionAlive = scComponentAttributesAMap.get(GscO2CConstants.SESSION_KEEP_ALIVE_TIMER);
				ExcelBean book29 = new ExcelBean(GscO2CConstants.GSC_SIP_ATTRIBUTES_DETAILS,
						GscO2CConstants.SESSION_KEEP_ALIVE_TIMER,
						sessionAlive);
				book29.setOrder(3);
				book29.setGscQuoteDetailId(0);
				listBook.add(book29);

				String prefixAd = scComponentAttributesAMap.get(GscO2CConstants.PREFIX_ADDITION);
				ExcelBean book30 = new ExcelBean(GscO2CConstants.GSC_SIP_ATTRIBUTES_DETAILS,
						GscO2CConstants.PREFIX_ADDITION,
						prefixAd);
				book30.setOrder(3);
				book30.setGscQuoteDetailId(0);
				listBook.add(book30);

				String transport = scComponentAttributesAMap.get(GscO2CConstants.TRANSPORT_PROTOCOL);
				ExcelBean book32 = new ExcelBean(GscO2CConstants.GSC_SIP_ATTRIBUTES_DETAILS,
						GscO2CConstants.TRANSPORT_PROTOCOL,
						transport);
				book32.setOrder(3);
				book32.setGscQuoteDetailId(0);
				listBook.add(book32);

				String codec = scComponentAttributesAMap.get(GscO2CConstants.CODEC);
				ExcelBean book33 = new ExcelBean(GscO2CConstants.GSC_SIP_ATTRIBUTES_DETAILS,
						GscO2CConstants.CODEC,
						codec);
				book33.setOrder(3);
				book33.setGscQuoteDetailId(0);
				listBook.add(book33);

				String channel = scComponentAttributesAMap.get(GscO2CConstants.NO_OF_CONCURRENT_CHANNEL);
				ExcelBean book34 = new ExcelBean(GscO2CConstants.GSC_SIP_ATTRIBUTES_DETAILS,
						GscO2CConstants.NO_OF_CONCURRENT_CHANNEL,
						channel);
				book34.setOrder(3);
				book34.setGscQuoteDetailId(0);
				listBook.add(book34);

				String equipment = scComponentAttributesAMap.get(GscO2CConstants.EQUIPMENT_ADDRESS);
				ExcelBean book35 = new ExcelBean(GscO2CConstants.GSC_SIP_ATTRIBUTES_DETAILS,
						GscO2CConstants.EQUIPMENT_ADDRESS,
						equipment);
				book35.setOrder(3);
				book35.setGscQuoteDetailId(0);
				listBook.add(book35);

				String routing = scComponentAttributesAMap.get(GscO2CConstants.ROUTING_TOPOLOGY);
				ExcelBean book36 = new ExcelBean(GscO2CConstants.GSC_SIP_ATTRIBUTES_DETAILS,
						GscO2CConstants.ROUTING_TOPOLOGY,
						routing);
				book36.setOrder(3);
				book36.setGscQuoteDetailId(0);
				listBook.add(book36);

				String dialPlan = scComponentAttributesAMap.get(GscO2CConstants.DIAL_PLAN_LOGIC);
				ExcelBean book37 = new ExcelBean(GscO2CConstants.GSC_SIP_ATTRIBUTES_DETAILS,
						GscO2CConstants.DIAL_PLAN_LOGIC,
						dialPlan);
				book37.setOrder(3);
				book37.setGscQuoteDetailId(0);
				listBook.add(book37);

				String calls = scComponentAttributesAMap.get(GscO2CConstants.CALLS_PER_SECOND);
				ExcelBean book38 = new ExcelBean(GscO2CConstants.GSC_SIP_ATTRIBUTES_DETAILS,
						GscO2CConstants.CALLS_PER_SECOND,
						calls);
				book38.setOrder(3);
				book38.setGscQuoteDetailId(0);
				listBook.add(book38);

				String certificate = scComponentAttributesAMap.get(GscO2CConstants.CERTIFICATE_AUTHORITY_SUPPORT);
				ExcelBean book39 = new ExcelBean(GscO2CConstants.GSC_SIP_ATTRIBUTES_DETAILS,
						GscO2CConstants.CERTIFICATE_AUTHORITY_SUPPORT,
						certificate);
				book39.setOrder(3);
				book39.setGscQuoteDetailId(0);
				listBook.add(book39);

				String ipAddress = scComponentAttributesAMap.get(GscO2CConstants.IP_ADDRESS_SPACE);
				ExcelBean book41 = new ExcelBean(GscO2CConstants.GSC_SIP_ATTRIBUTES_DETAILS,
						GscO2CConstants.IP_ADDRESS_SPACE,
						ipAddress);
				book41.setOrder(3);
				book41.setGscQuoteDetailId(0);
				listBook.add(book41);

				String cusDevice = scComponentAttributesAMap.get(GscO2CConstants.CUSTOMER_DEVICE_IP);
				ExcelBean book42 = new ExcelBean(GscO2CConstants.GSC_SIP_ATTRIBUTES_DETAILS,
						GscO2CConstants.CUSTOMER_DEVICE_IP,
						cusDevice);
				book42.setOrder(3);
				book42.setGscQuoteDetailId(0);
				listBook.add(book42);

				String adInfo = scComponentAttributesAMap.get(GscO2CConstants.ADDITIONAL_INFORMATION);
				ExcelBean book43 = new ExcelBean(GscO2CConstants.GSC_SIP_ATTRIBUTES_DETAILS,
						GscO2CConstants.ADDITIONAL_INFORMATION,
						adInfo);
				book43.setOrder(3);
				book43.setGscQuoteDetailId(0);
				listBook.add(book43);


				break;
			}
			default:

				LOGGER.info("Invalid component Name");

		}


	}
	public void createCugDetails(List<ExcelBean> listBook,ScServiceDetail scServiceDetail,Map<String, String> scComponentAttributesAMap,Map<String,String> scComponentAttributesWebExMap)
	{
		String dialInNumber = scComponentAttributesAMap.get("cugDialInNumber");
		ExcelBean book43 = new ExcelBean(GscO2CConstants.GSC_CUG_ATTRIBUTES_DETAILS,
				GscO2CConstants.CUG_DIAL_IN_NUMBER,
				dialInNumber);
		book43.setOrder(3);
		book43.setGscQuoteDetailId(0);
		listBook.add(book43);

		String cugDialOutResponse = scComponentAttributesAMap.get("cugDialOut");
		ExcelBean cugDialOut = new ExcelBean(GscO2CConstants.GSC_CUG_ATTRIBUTES_DETAILS,
				GscO2CConstants.CUG_DIAL_OUT,
				cugDialOutResponse);
		cugDialOut.setOrder(3);
		cugDialOut.setGscQuoteDetailId(0);
		listBook.add(cugDialOut);

		/*CugDialOutBeanDetails cugDialOutBeans=(CugDialOutBeanDetails) Utils.convertJsonToObject(cugDialOutResponse, CugDialOutBeanDetails.class);*/

		/*for(CugDialOutBean cugDialOut:cugDialOutBeans.getCugDialOut()) {
			ExcelBean cugLength = new ExcelBean(GscO2CConstants.GSC_CUG_ATTRIBUTES_DETAILS,
					GscO2CConstants.CUG_LENGTH,
					cugDialOut.getCugLength());
			cugLength.setOrder(3);
			cugLength.setGscQuoteDetailId(0);
			listBook.add(cugLength);

			ExcelBean cugPrefixLength = new ExcelBean(GscO2CConstants.GSC_CUG_ATTRIBUTES_DETAILS,
					GscO2CConstants.LOCATION_PREFIX_LENGTH,
					cugDialOut.getLocationPrefixLength());
			cugPrefixLength.setOrder(3);
			cugPrefixLength.setGscQuoteDetailId(0);
			listBook.add(cugPrefixLength);

			ExcelBean locPrefix = new ExcelBean(GscO2CConstants.GSC_CUG_ATTRIBUTES_DETAILS,
					GscO2CConstants.LOCATION_PREFIX,
					cugDialOut.getLocationPrefix());
			locPrefix.setOrder(3);
			locPrefix.setGscQuoteDetailId(0);
			listBook.add(locPrefix);


			ExcelBean name = new ExcelBean(GscO2CConstants.GSC_CUG_ATTRIBUTES_DETAILS,
					GscO2CConstants.NAME_LOCATION,
					cugDialOut.getName());
			name.setOrder(3);
			name.setGscQuoteDetailId(0);
			listBook.add(name);
		}*/
		String onnetDialback = scComponentAttributesAMap.get("onNetDialBack");
		ExcelBean dialbackBook = new ExcelBean(GscO2CConstants.GSC_CUG_ATTRIBUTES_DETAILS,
				GscO2CConstants.ONNET_DIAL_BACK,
				onnetDialback);
		dialbackBook.setOrder(3);
		dialbackBook.setGscQuoteDetailId(0);
		listBook.add(dialbackBook);

	}

	public void createGVPNAttributes(List<ExcelBean> listBook,Map<String,String> attributeValues,Map<String,String> orderAttributeValues,ScServiceDetail scServiceDetail)
	{
		String localItContactName = scServiceDetail.getLocalItContactName();
		ExcelBean localItContactBook = new ExcelBean(GscO2CConstants.GVPN_ATTRIBUTES_DETAILS,
				GscO2CConstants.LOCAL_IT_CONTACT_NAME,
				localItContactName);
		localItContactBook.setOrder(4);
		localItContactBook.setGscQuoteDetailId(0);
		listBook.add(localItContactBook);

		String localItContactEmail = scServiceDetail.getLocalItContactEmail();
		ExcelBean localItContactEmailBook = new ExcelBean(GscO2CConstants.GVPN_ATTRIBUTES_DETAILS,
				GscO2CConstants.LOCAL_IT_CONTACT_EMAIL_ID,
				localItContactName);
		localItContactEmailBook.setOrder(4);
		localItContactEmailBook.setGscQuoteDetailId(0);
		listBook.add(localItContactEmailBook);


		String localItContactNumber = scServiceDetail.getLocalItContactMobile();
		ExcelBean localItContactNumberBook = new ExcelBean(GscO2CConstants.GVPN_ATTRIBUTES_DETAILS,
				GscO2CConstants.LOCAL_IT_CONTACT_NUMBER,
				localItContactNumber);
		localItContactNumberBook.setOrder(4);
		localItContactNumberBook.setGscQuoteDetailId(0);
		listBook.add(localItContactNumberBook);

		String gstNo=null;
		String gstAddress=null;
		if (orderAttributeValues.containsKey(GscO2CConstants.LE_STATE_GST_NUMBER)) {
			gstNo = orderAttributeValues.get(GscO2CConstants.LE_STATE_GST_NUMBER);

		} else if (orderAttributeValues.containsKey(GscO2CConstants.GST_NUM)) {
			gstNo = orderAttributeValues.get(GscO2CConstants.GST_NUM);

		}
		if (orderAttributeValues.containsKey(GscO2CConstants.LE_STATE_GST_ADDRESS)) {
			gstAddress = orderAttributeValues.get(GscO2CConstants.LE_STATE_GST_ADDRESS);
		} else if (orderAttributeValues.containsKey(GscO2CConstants.GST_ADDR)) {
			gstAddress = orderAttributeValues.get(GscO2CConstants.GST_ADDR);
		}


		ExcelBean gstNumberBook = new ExcelBean(GscO2CConstants.GVPN_ATTRIBUTES_DETAILS,
				GscO2CConstants.GST_NUMBER,gstNo);
		gstNumberBook.setOrder(4);
		gstNumberBook.setGscQuoteId(0);
		listBook.add(gstNumberBook);

		ExcelBean gstAddressBook = new ExcelBean(GscO2CConstants.GVPN_ATTRIBUTES_DETAILS,
				GscO2CConstants.GST_ADDRESS,gstAddress);
		gstAddressBook.setOrder(4);
		gstAddressBook.setGscQuoteId(0);
		listBook.add(gstAddressBook);

		ExcelBean routingProtocolBook = new ExcelBean(GscO2CConstants.GVPN_ATTRIBUTES_DETAILS,
				GscO2CConstants.ROUTING_PROTOCOL_BGP,attributeValues.containsKey(GscO2CConstants.ROUTING_PROTOCOL_BGP)
				? attributeValues.get(GscO2CConstants.ROUTING_PROTOCOL_BGP): GscO2CConstants.NOT_AVAILABLE);
		routingProtocolBook.setOrder(4);
		routingProtocolBook.setGscQuoteId(0);
		listBook.add(routingProtocolBook);

		ExcelBean bgpPeeringOnBook = new ExcelBean(GscO2CConstants.GVPN_ATTRIBUTES_DETAILS,
				GscO2CConstants.BGP_PEERING_ON,attributeValues.containsKey(GscO2CConstants.BGP_PEERING_ON)
				? attributeValues.get(GscO2CConstants.BGP_PEERING_ON): GscO2CConstants.NOT_AVAILABLE);
		bgpPeeringOnBook.setOrder(4);
		bgpPeeringOnBook.setGscQuoteId(0);
		listBook.add(bgpPeeringOnBook);

		ExcelBean wanIpProvidedByBook = new ExcelBean(GscO2CConstants.GVPN_ATTRIBUTES_DETAILS,
				GscO2CConstants.WAN_IP_PROVIDED_BY,attributeValues.containsKey(GscO2CConstants.WAN_IP_PROVIDED_BY)
				? attributeValues.get(GscO2CConstants.WAN_IP_PROVIDED_BY): GscO2CConstants.NOT_AVAILABLE);
		wanIpProvidedByBook.setOrder(4);
		wanIpProvidedByBook.setGscQuoteId(0);
		listBook.add(wanIpProvidedByBook);

		ExcelBean multicastsBook = new ExcelBean(GscO2CConstants.GVPN_ATTRIBUTES_DETAILS,
				GscO2CConstants.MULTICASTS_EXISTS,attributeValues.containsKey(GscO2CConstants.MULTICASTS_EXISTS)
				? attributeValues.get(GscO2CConstants.MULTICASTS_EXISTS): GscO2CConstants.NOT_AVAILABLE);
		multicastsBook.setOrder(4);
		multicastsBook.setGscQuoteId(0);
		listBook.add(multicastsBook);

		ExcelBean siteTypeBook = new ExcelBean(GscO2CConstants.GVPN_ATTRIBUTES_DETAILS,
				GscO2CConstants.SITE_TYPE,attributeValues.containsKey(GscO2CConstants.SITE_TYPE)
				? attributeValues.get(GscO2CConstants.SITE_TYPE): GscO2CConstants.NOT_AVAILABLE);
		siteTypeBook.setOrder(4);
		siteTypeBook.setGscQuoteId(0);
		listBook.add(siteTypeBook);

		ExcelBean bfdRequiredBook = new ExcelBean(GscO2CConstants.GVPN_ATTRIBUTES_DETAILS,
				GscO2CConstants.BFD_REQUIRED,attributeValues.containsKey(GscO2CConstants.BFD_REQUIRED)
				? attributeValues.get(GscO2CConstants.BFD_REQUIRED): GscO2CConstants.NOT_AVAILABLE);
		bfdRequiredBook.setOrder(4);
		bfdRequiredBook.setGscQuoteId(0);
		listBook.add(bfdRequiredBook);


		ExcelBean asNumberBook = new ExcelBean(GscO2CConstants.GVPN_ATTRIBUTES_DETAILS,
				GscO2CConstants.AS_NUMBER,attributeValues.containsKey(GscO2CConstants.AS_NUMBER)
				? attributeValues.get(GscO2CConstants.AS_NUMBER): GscO2CConstants.NOT_AVAILABLE);
		asNumberBook.setOrder(4);
		asNumberBook.setGscQuoteId(0);
		listBook.add(asNumberBook);

		ExcelBean authRequiredBook = new ExcelBean(GscO2CConstants.GVPN_ATTRIBUTES_DETAILS,
				GscO2CConstants.IS_AUTHENTICATION_REQUIRED,attributeValues.containsKey(GscO2CConstants.IS_AUTHENTICATION_REQUIRED)
				? attributeValues.get(GscO2CConstants.IS_AUTHENTICATION_REQUIRED): GscO2CConstants.NOT_AVAILABLE);
		authRequiredBook.setOrder(4);
		authRequiredBook.setGscQuoteId(0);
		listBook.add(authRequiredBook);

		ExcelBean vpnTopologyBook = new ExcelBean(GscO2CConstants.GVPN_ATTRIBUTES_DETAILS,
				GscO2CConstants.VPN_TOPOLOGY,attributeValues.containsKey(GscO2CConstants.VPN_TOPOLOGY)
				? attributeValues.get(GscO2CConstants.VPN_TOPOLOGY): GscO2CConstants.NOT_AVAILABLE);
		vpnTopologyBook.setOrder(4);
		vpnTopologyBook.setGscQuoteId(0);
		listBook.add(vpnTopologyBook);

		ExcelBean accessTypeBook = new ExcelBean(GscO2CConstants.GVPN_ATTRIBUTES_DETAILS,
				GscO2CConstants.ACCESS_TYPE,scServiceDetail.getLastmileType());
		accessTypeBook.setOrder(4);
		accessTypeBook.setGscQuoteId(0);
		listBook.add(accessTypeBook);


		ExcelBean accessProviderBook = new ExcelBean(GscO2CConstants.GVPN_ATTRIBUTES_DETAILS,
				GscO2CConstants.ACCESS_PROVIDER,scServiceDetail.getLastmileProvider());
		accessProviderBook.setOrder(4);
		accessProviderBook.setGscQuoteId(0);
		listBook.add(accessProviderBook);


		ExcelBean feasibilityDateBook = new ExcelBean(GscO2CConstants.GVPN_ATTRIBUTES_DETAILS,
				GscO2CConstants.FEASIBILITY_CREATED_DATE,attributeValues.containsKey(GscO2CConstants.FEASIBILITY_RESPONSE_CREATED_DATE)
				? attributeValues.get(GscO2CConstants.FEASIBILITY_RESPONSE_CREATED_DATE): GscO2CConstants.NOT_AVAILABLE);
		feasibilityDateBook.setOrder(4);
		feasibilityDateBook.setGscQuoteId(0);
		listBook.add(feasibilityDateBook);



	}

	/**
	 * createOrderDetails - extracts order related details as excel bean
	 * @param listBook
	 * @param scOrder
	 * @param attributeValues
	 * @param contractInfo
	 */

	private void createWebExAttributes(List<ExcelBean> listBook,ScOrder scOrder,Map<String,String> attributeValues,ScContractInfo contractInfo) {
		ExcelBean custNameDetails = new ExcelBean(GscO2CConstants.WEBEX_ATTRIBUTES,
				GscO2CConstants.CUSTOMER_NAME, scOrder.getErfCustCustomerName());
		custNameDetails.setOrder(1);
		custNameDetails.setGscQuoteId(0);
		listBook.add(custNameDetails);

		ExcelBean serviceNameBook = new ExcelBean(GscO2CConstants.WEBEX_ATTRIBUTES,
				GscO2CConstants.SERVICE_NAME, GscO2CConstants.CISCO_WEBEX);
		serviceNameBook.setOrder(1);
		serviceNameBook.setGscQuoteId(0);
		listBook.add(serviceNameBook);

		ExcelBean serviceTypeBook = new ExcelBean(GscO2CConstants.WEBEX_ATTRIBUTES,
				GscO2CConstants.SERVICE_TYPE, GscO2CConstants.CCA);
		serviceTypeBook.setOrder(1);
		serviceTypeBook.setGscQuoteId(0);
		listBook.add(serviceTypeBook);

		ExcelBean numberTypeBook = new ExcelBean(GscO2CConstants.WEBEX_ATTRIBUTES,
				GscO2CConstants.NUMBER_TYPE, attributeValues.containsKey(GscO2CConstants.AUDIO_MODEL)
				? attributeValues.get(GscO2CConstants.AUDIO_MODEL)
				: GscO2CConstants.NOT_AVAILABLE);
		numberTypeBook.setOrder(1);
		numberTypeBook.setGscQuoteId(0);
		listBook.add(numberTypeBook);

		ExcelBean paymentTypeBook = new ExcelBean(GscO2CConstants.WEBEX_ATTRIBUTES,
				GscO2CConstants.PAYMENT_TYPE, attributeValues.containsKey(GscO2CConstants.PAYMENT_MODEL)
				? attributeValues.get(GscO2CConstants.PAYMENT_MODEL)
				: GscO2CConstants.NOT_AVAILABLE);
		paymentTypeBook.setOrder(1);
		paymentTypeBook.setGscQuoteId(0);
		listBook.add(paymentTypeBook);

		ExcelBean legalEntityBook = new ExcelBean(GscO2CConstants.WEBEX_ATTRIBUTES,
				GscO2CConstants.LEGAL_ENTITY, scOrder.getErfCustLeName());
		legalEntityBook.setOrder(1);
		legalEntityBook.setGscQuoteId(0);
		listBook.add(legalEntityBook);

		ExcelBean contractTermBook = new ExcelBean(GscO2CConstants.WEBEX_ATTRIBUTES,
				GscO2CConstants.CONTRACT_TERM, contractInfo.getOrderTermInMonths()+" months");
		contractTermBook.setOrder(1);
		contractTermBook.setGscQuoteId(0);
		listBook.add(contractTermBook);

	}

	@Transactional(readOnly = false)
	public List<GSCServiceDetailBean> getGSCServiceDetails(String orderCode) throws TclCommonException {
		List<GSCServiceDetailBean> gscServiceDetailBeans = new ArrayList<GSCServiceDetailBean>();
		List<ScServiceDetail> serviceDetails = scServiceDetailRepository.findByScOrderIdAndErfCatalogProductName(orderCode, "GSIP");
		GSCServiceDetailBean gscServiceDetailBean;
		for(ScServiceDetail serviceDetail : serviceDetails) {
			gscServiceDetailBean = new GSCServiceDetailBean();
			gscServiceDetailBean.setId(serviceDetail.getId());
			gscServiceDetailBean.setServiceCode(serviceDetail.getUuid());
			gscServiceDetailBean.setOfferingName(serviceDetail.getErfPrdCatalogOfferingName());
			gscServiceDetailBean.setSrcCountry(serviceDetail.getSourceCountry());
			gscServiceDetailBean.setDstCountry(serviceDetail.getDestinationCountry());
			gscServiceDetailBeans.add(gscServiceDetailBean);
		}
		return gscServiceDetailBeans;
	}
	
	@Transactional(readOnly = false)
	public TaskBean getGSCServiceAttributes(Integer serviceId, Integer taskId, String wfTaskId) throws TclCommonException {
		TaskBean taskbean = new TaskBean();
		Task task = getTaskByIdAndWfTaskId(taskId, wfTaskId);
		if (task != null) {
			Optional<ScServiceDetail> optionalServiceDetails = scServiceDetailRepository.findById(serviceId);
			if (optionalServiceDetails.isPresent()) {
				Map<String, Object> commonData = new HashMap<>();
				List<MstTaskAttribute> mstTaskAttributes = mstTaskAttributeRepository
						.findByMstTaskDef(task.getMstTaskDef());
				for (MstTaskAttribute mstTaskAttribute : mstTaskAttributes) {
					if (mstTaskAttribute.getCategory().equals(AttributeConstants.COMPONENT_ATTRIBUTES)) {
						ScComponent scComponent = scComponentRepository
								.findFirstByScServiceDetailIdAndComponentNameAndSiteTypeOrderByIdAsc(serviceId, mstTaskAttribute.getSubCategory(), task.getSiteType());
						if (scComponent != null && scComponent.getScComponentAttributes() != null && !scComponent.getScComponentAttributes().isEmpty()) {
							for (ScComponentAttribute compAttr : scComponent.getScComponentAttributes()) {
								if (compAttr.getAttributeName().equals(mstTaskAttribute.getAttributeName())) {
									commonData.put(mstTaskAttribute.getNodeName(), compAttr.getAttributeValue());
									break;
								}
							}
						}
					}
				}
				taskbean.setCommonData(commonData);
			}
		}
		return taskbean;
	}
}

