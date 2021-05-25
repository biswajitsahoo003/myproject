package com.tcl.dias.networkaugmentation.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import com.tcl.dias.networkaugment.entity.entities.*;
import com.tcl.dias.networkaugment.entity.repository.*;
import com.tcl.dias.servicefulfillmentutils.beans.MfTaskTrailBean;
import com.tcl.dias.networkaugmentation.beans.NwaOrderDetailsBean;
import com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.NetworkAugmentationWorkFlowService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskCacheService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;
import liquibase.util.StringUtils;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.DelegateExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import static com.tcl.dias.networkaugmentation.constants.MasterDefConstants.*;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;


@Service
@Transactional(readOnly = false,isolation=Isolation.READ_COMMITTED)
public class NetworkAugmentationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(NetworkAugmentationService.class);

	@Autowired
	NwaOrderDetailsRepository nwaOrderDetailsRepository;

	@Autowired
	protected TaskService flowableTaskService;

	@Autowired
	RuntimeService runtimeService;

	@Autowired
	OrderDetailsRepository orderDetailsRepository;

	@Autowired
	TaskRepository taskRepository;

	@Autowired
	MstStatusRepository mstStatusRepository;

	@Autowired
	ScOrderRepository scOrderRepository;

	@Autowired
	SeqGenService seqGenService;

	@Autowired
	com.tcl.dias.servicefulfillmentutils.service.v1.TaskService taskService;

	@Autowired
	TaskCacheService taskCacheService;

	@Autowired
	MfTaskTrailRepository mfTaskTrailRepository;

	@Autowired
	NetworkInventoryRepository networkInventoryRepository;

	@Autowired
	NwaMakeModelRepository nwaMakeModelRepository;

	@Autowired
	NetworkAugmentationWorkFlowService workFlowService;

	public String padLeftZeros(String inputString, int length) {
		if (inputString.length() >= length) {
			return inputString;
		}
		StringBuilder sb = new StringBuilder();
		while (sb.length() < length - inputString.length()) {
			sb.append('0');
		}
		sb.append(inputString);

		return sb.toString();
	}

	public static class ProcessVariables {
		public static final String EOR_IP_CARD = "eorIpCard";
		public static final String EOR_EQUIP_IP = "eorEquipIp";
		public static final String EOR_TX_CARD = "eorTxCardPro";
		public static final String EOR_TX_PRO = "eorTxProvisioning";
		public static final String EOR_ETH_PRO = "eorEThProvisioning";
		public static final String START_PROCESS_ICARD_PROV = "eor_stage_workflow";
		public static final String EOR_IP_TERMINATON = "eorIpTermination";
		public static final String EOR_EQUIP_WL_PRO = "eorEqpWirelessPro";
	}

	public Task getTaskByExecution(DelegateExecution execution) {
		Map<String, Object> varibleMap = execution.getVariables();
		Task task = null;
		Integer serviceId = (Integer) varibleMap.get(SERVICE_ID);
		LOGGER.info("task by execution service id {}, current activity id {}", serviceId,
				execution.getCurrentActivityId());
		task = taskRepository.findByServiceIdAndMstStatus_codeAndMstTaskDef_key(serviceId, TaskStatusConstants.OPENED,
				execution.getCurrentActivityId());
		if (Objects.nonNull(task)) {
			return task;
		} else {
			LOGGER.info("no task found {}", task);
			return task;
		}
	}

	@Transactional(readOnly = false,isolation=Isolation.READ_UNCOMMITTED)
	public Map<String, String> initiateIpProvisioning(NwaOrderDetailsBean nwaOrderDetailsBean) {

		Map<String, String> map = new HashMap();
		try {

			Map<String, Object> processVar = new HashMap<>();
			LOGGER.info("initiateIpProvisioning started");

			Integer seqNo = seqGenService.getSequenceNo("EOR");
			String orderSeqNo = seqNo.toString();
			LOGGER.info("Sequence No"+seqNo);

			String processType = "EOR";
			//orderSeqNo = (processType+"_"+padLeftZeros(orderSeqNo,10));
			orderSeqNo = (processType+"_"+orderSeqNo);
			String orderCode = orderSeqNo;
			LOGGER.info("orderCode = "+orderCode);
			nwaOrderDetailsBean.setOrderCode(orderCode);
			seqGenService.updateSequenceNo("EOR", seqNo+1);

			processVar.put("eorEquipIp", true);
			processVar.put("orderCode",orderCode);
			processVar.put("rejection",false);

			UUID uuid=UUID.randomUUID();

			ScOrder scOrder = new ScOrder();
			scOrder.setUuid(uuid.toString());
			scOrder.setOpOrderCode((String) orderCode);
			scOrder.setSubject((String) nwaOrderDetailsBean.getSubject());
			scOrder.setProjectType((String) nwaOrderDetailsBean.getProjectType());
			//scOrder.setOrderType((String) nwaOrderDetailsBean.getOrderType());
			scOrder.setOrderType("EOREQUIPIP");
			scOrder.setProcessType("EOR");
			scOrder.setScenarioType("PROVISION");
			scOrder.setTechnologyType("IP");
			scOrder.setPmName((String) nwaOrderDetailsBean.getPmName());
			scOrder.setPmContactEmail((String) nwaOrderDetailsBean.getPmContactEmail());
			scOrder.setOrderStatus("12");
			scOrder.setOriginatorName((String) nwaOrderDetailsBean.getOriginatorName());
			scOrder.setOriginatorGroupId((String) nwaOrderDetailsBean.getOriginatorGroupId());
			scOrder.setOriginatorContactNumber((String) nwaOrderDetailsBean.getOriginatOrContactNumber());
			//scOrder.setOrderCreationDate((Timestamp) nwaOrderDetailsBean.getOrderCreationDate());
			Timestamp orderCreatedDate = Timestamp.valueOf(LocalDateTime.now());
			System.out.println(orderCreatedDate);
			System.out.println("Java Data is :"+Timestamp.valueOf(LocalDateTime.now()));
			scOrder.setOrderCreationDate(Timestamp.valueOf(LocalDateTime.now()));

			UUID uuid2=UUID.randomUUID();
			Set<ScServiceDetail> tSsD = new HashSet<>();
			ScServiceDetail scServiceDetail = new ScServiceDetail();
			scServiceDetail.setUuid(uuid2.toString());
			scServiceDetail.setServiceGroupId((String) nwaOrderDetailsBean.getOriginatorGroupId());
			scServiceDetail.setScOrderUuid((String) scOrder.getUuid());
			scServiceDetail.setSmName((String) nwaOrderDetailsBean.getPmName());
			scServiceDetail.setSmEmail((String) nwaOrderDetailsBean.getPmContactEmail());
			scServiceDetail.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.PROVISIONING));

			scServiceDetail.setScOrder(scOrder);
			tSsD.add(scServiceDetail);
			scOrder.setScServiceDetails(tSsD);

			scOrderRepository.save(scOrder);
			LOGGER.info("Order Saved ");
			processVar.put(ORDER_TYPE,scServiceDetail.getScOrder().getOrderType());
			processVar.put(ORDER_CODE, scServiceDetail.getScOrder().getOpOrderCode());
			processVar.put(SERVICE_CODE, StringUtils.trimToEmpty(scServiceDetail.getUuid()));
			processVar.put(SERVICE_ID, scServiceDetail.getId());
			Integer serviceId = (Integer) processVar.get(SERVICE_ID);

			runtimeService.startProcessInstanceByKey("eor_stage_workflow", processVar);
			System.out.println("=========checking variables"+runtimeService);
			LOGGER.info("initiateIpProvisioning completed");

			//Task task = taskService.findByOrderCode(orderCode);
			List<Task>  task1 = taskRepository.findByServiceIdAndMstStatus_code(serviceId,TaskStatusConstants.OPENED);
			Task task = task1.get(0);
			System.out.println("====== Task created with id "+task.getId());
			map.put("ORDER_CODE", orderCode);
			map.put("TASK_ID", task.getId()+"");
			map.put("WF_TASK_ID", task.getWfTaskId());
			map.put("TASK_NAME", task.getMstTaskDef().getName());
			System.out.println("Order Status ++++++++++++++++++++++++");
			map.put("orderStatus", task.getMstStatus().getCode());
			map.put("taskStatus", task.getMstStatus().getCode());
			System.out.println("Task table updated !");

			MfTaskTrailBean mfTaskTrailBean = new MfTaskTrailBean();
			mfTaskTrailBean.setTaskId(task.getId());
			mfTaskTrailBean.setTaskName(task.getMstTaskDef().getName());
			mfTaskTrailBean.setUserGroup(nwaOrderDetailsBean.getOriginatorGroupId());
			mfTaskTrailBean.setAction("IP Provisioning");
			mfTaskTrailBean.setScenario("PROVISION");
			mfTaskTrailBean.setCompletedBy(nwaOrderDetailsBean.getOriginatorName());
			mfTaskTrailBean.setCreatedTime(Timestamp.valueOf(LocalDateTime.now()));
			mfTaskTrailBean.setDescription("Work Flow triggered");
			//mfTaskTrailBean.setComments("Work Flow triggered");
			taskService.setTaskTrail(mfTaskTrailBean);
			System.out.println("Task trail table updated !");



			return map;

		}
		catch (Exception e){
			LOGGER.info("Service Id {}, Task Id {}, Task {}, Wf Executor Id {}");
		}
		return map;

	}

	public Map<String, String> initiateProvisioning(String processToStart,
													NwaOrderDetailsBean nwaOrderDetailsBean) {

		Map<String, String> map = new HashMap();
		try {
			Map<String, Object> processVar = new HashMap<>();
			//constructPrecedersTime(serviceId,null, processVar);
			LOGGER.info("Workflow initiated for ..."+processToStart);
			Integer seqNo = seqGenService.getSequenceNo("EOR");
			String seq_no = seqNo.toString();
			LOGGER.info("Sequence No"+seqNo);
			String processType = "EOR";//TODO Enum?
			seq_no = (processType+"_"+seq_no);
			String orderCode = seq_no;
			LOGGER.info("orderCode = "+orderCode);
			nwaOrderDetailsBean.setOrderCode(orderCode);
			seqGenService.updateSequenceNo("EOR", seqNo+1);
			UUID uuid=UUID.randomUUID();
			ScOrder scOrder = new ScOrder();
			scOrder.setUuid(uuid.toString());
			scOrder.setOpOrderCode((String) orderCode);
			//scOrder.setOrderType((String) nwaOrderDetailsBean.getOrderType());
			scOrder.setSubject((String) nwaOrderDetailsBean.getSubject());
			scOrder.setProjectType((String) nwaOrderDetailsBean.getProjectType());
			//scOrder.setProcessType(processType);
			//scOrder.setScenarioType("PROVISION"); //TODO Enum??
			switch (processToStart) {
				case "equip-tx-provisioning":
					processVar.put("eorTxProvisioning", true);
					processVar.put(ProcessVariables.EOR_TX_PRO, true);
					processVar.put("eorEquipIp", false);
					processVar.put("eorIpCard", false);
					processVar.put("eorTxCardPro", false);

					scOrder.setOrderType("EOREQUIPTX");
					scOrder.setProcessType("EOR");
					scOrder.setScenarioType("PROVISION");
					scOrder.setTechnologyType("TX");
					break;

				case "ethernet-provisioning":
					processVar.put("eorEThProvisioning",true);
					processVar.put(ProcessVariables.EOR_ETH_PRO, true);
					scOrder.setOrderType("EOREQUIPETH");
					scOrder.setProcessType("EOR");
					scOrder.setScenarioType("PROVISION");
					scOrder.setTechnologyType("ETH");// TODO enum??
					break;

				case "ip-card-provisioning":
					processVar.put("eorIpCard", true);
					processVar.put(ProcessVariables.EOR_IP_CARD, true);
					scOrder.setOrderType("EORIP");
					scOrder.setProcessType("EOR");
					scOrder.setScenarioType("PROVISION");
					scOrder.setTechnologyType("IP");// TODO enum??
					break;

				//case "equip-wireless-ip-pool-allocation":
				//break;
				case "tx-card-provisioning":
				case "eor_tx_card_provisioning_V20":
					processVar.put("eorTxCardPro", true);
					processVar.put("eorEquipIp", false);
					processVar.put("eorIpCard", false);
					processVar.put("eorTxProvisioning", false);
					processVar.put("originatorGSPI",false);
					scOrder.setOrderType("EORTX");
					scOrder.setProcessType("EOR");
					scOrder.setScenarioType("PROVISION");
					scOrder.setTechnologyType("TX");// TODO enum??
				break;
				case "eth-card-provisioning":
					processVar.put("eorEThCard",true);
					processVar.put("originatorGSPI",false);
					scOrder.setOrderType("EORETH");
					scOrder.setProcessType("EOR");
					scOrder.setScenarioType("PROVISION");
					scOrder.setTechnologyType("ETH");
				break;
				case "equip-wireless-provisioning":
				case "eor_equip_wireless_provisioning":
					processVar.put("eorEqpWirelessPro", true);
					processVar.put("ipProvRequired",false);
					processVar.put("deviceType","BACKHAUL");
					processVar.put("rejection",false);
					processVar.put("tacacsConfig",false);
					processVar.put("rFMorRfs","RFM");
					processVar.put(ProcessVariables.EOR_EQUIP_WL_PRO, true);
					scOrder.setOrderType("EORWIRELESS");
					scOrder.setProcessType("EOR");
					scOrder.setScenarioType("PROVISION");
					scOrder.setTechnologyType("WIRELESS");//
				break;
				case "wireless-ip-pool":
					processVar.put("eorwlipPoolalloc",true);
					processVar.put("rejection",false);
					scOrder.setOrderType("EORWIRELESS");
					scOrder.setProcessType("EOR");
					scOrder.setScenarioType("IPALLOCATION");
					scOrder.setTechnologyType("WIRELESS");
					break;
				default:
					throw new TclCommonRuntimeException("Cannot deal with start process: " + processToStart);
			}
			processVar.put(MasterDefConstants.ORDER_CODE,orderCode);

			processVar.put("orderCode",orderCode);
			processVar.put("rejection",false);

			//scOrder.setScenarioType((String) nwaOrderDetailsBean.getScenarioType());
			//scOrder.setTechnologyType((String) nwaOrderDetailsBean.getTechnologyType());
			scOrder.setPmName((String) nwaOrderDetailsBean.getPmName());
			scOrder.setPmContactEmail((String) nwaOrderDetailsBean.getPmContactEmail());
			System.out.println("===========PmContactEmail "+ nwaOrderDetailsBean.getPmContactEmail());
			scOrder.setOrderStatus("12");
			scOrder.setOriginatorName((String) nwaOrderDetailsBean.getOriginatorName());
			scOrder.setOriginatorGroupId((String) nwaOrderDetailsBean.getOriginatorGroupId());
			scOrder.setOriginatorContactNumber((String) nwaOrderDetailsBean.getOriginatOrContactNumber());
			scOrder.setOrderCreationDate(Timestamp.valueOf(LocalDateTime.now()));
			//scOrder.setOrderCreationDate((Timestamp) nwaOrderDetailsBean.getOrderCreationDate());
			UUID uuid2=UUID.randomUUID();
			Set<ScServiceDetail> tSsD = new HashSet<>();
			ScServiceDetail scServiceDetail = new ScServiceDetail();
			scServiceDetail.setUuid(uuid2.toString());
			scServiceDetail.setServiceGroupId((String) nwaOrderDetailsBean.getOriginatorGroupId());
			scServiceDetail.setScOrderUuid((String) scOrder.getUuid());
			scServiceDetail.setSmName((String) nwaOrderDetailsBean.getPmName());
			scServiceDetail.setSmEmail((String) nwaOrderDetailsBean.getPmContactEmail());
			scServiceDetail.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.PROVISIONING));
			scServiceDetail.setScOrder(scOrder);
			tSsD.add(scServiceDetail);
			scOrder.setScServiceDetails(tSsD);
			scOrderRepository.save(scOrder);
			LOGGER.info("Order Saved ");
			processVar.put(ORDER_TYPE,scServiceDetail.getScOrder().getOrderType());
			processVar.put(ORDER_CODE, scServiceDetail.getScOrder().getOpOrderCode());
			processVar.put(SERVICE_CODE, StringUtils.trimToEmpty(scServiceDetail.getUuid()));
			processVar.put(SERVICE_ID, scServiceDetail.getId());
			processVar.put("rejection", false);

			System.out.println("======ProcessVariables "+ processVar);

			runtimeService.startProcessInstanceByKey(ProcessVariables.START_PROCESS_ICARD_PROV, processVar);

			LOGGER.info("initiateCardProvisioning completed");

			Task task = taskService.findByOrderCode(orderCode);
			map.put("ORDER_CODE", orderCode);
			map.put("TASK_ID", task.getId()+"");
			map.put("WF_TASK_ID", task.getWfTaskId());
			map.put("TASK_NAME", task.getMstTaskDef().getName());
			System.out.println("Order Status ++++++++++++++++++++++++");
			map.put("orderStatus", task.getMstStatus().getCode());
			map.put("taskStatus", task.getMstStatus().getCode());

			MfTaskTrailBean mfTaskTrailBean = new MfTaskTrailBean();
			mfTaskTrailBean.setTaskId(task.getId());
			mfTaskTrailBean.setTaskName(task.getMstTaskDef().getName());
			mfTaskTrailBean.setUserGroup(nwaOrderDetailsBean.getOriginatorGroupId());
			mfTaskTrailBean.setAction(processToStart);
			mfTaskTrailBean.setScenario(scOrder.getScenarioType());
			mfTaskTrailBean.setCompletedBy(nwaOrderDetailsBean.getOriginatorName());
			mfTaskTrailBean.setCreatedTime(Timestamp.valueOf(LocalDateTime.now()));
			mfTaskTrailBean.setDescription("Work Flow triggered");
			//mfTaskTrailBean.setComments("Work Flow triggered");
			taskService.setTaskTrail(mfTaskTrailBean);
			System.out.println("Task trail table updated !");

			return map;
		}
		catch (Exception e){
			e.printStackTrace();
			LOGGER.error("Service Id {}, Task Id {}, Task {}, Wf Executor Id {}"+e.getMessage());
		}
		return map;
	}

	public Map<String, String> initiateTermination(String processToTerminate,
													NwaOrderDetailsBean nwaOrderDetailsBean) {

		Map<String, String> map = new HashMap();
		try {
			Map<String, Object> processVar = new HashMap<>();
			LOGGER.info("Workflow initiated for ..."+processToTerminate);
			ScOrder scOrder = scOrderRepository.findByOpOrderCode(nwaOrderDetailsBean.getOrderCode());
			if(scOrder==null) {
				throw new TclCommonRuntimeException("Cannot found order to terminate: "+nwaOrderDetailsBean.getOrderCode());
			}
			String orderCode = scOrder.getOpOrderCode();
			scOrder.setScenarioType("TERMINATION"); //TODO Enum??
			switch (processToTerminate) {
				case "ip-termination":
					processVar.put("eorEquipIp", false);
					processVar.put(ProcessVariables.EOR_IP_CARD, false);
					processVar.put(ProcessVariables.EOR_IP_TERMINATON, true);
					break;
				case "card-removal":
				case "eor_ip_card_removal":
					processVar.put(ProcessVariables.EOR_IP_CARD, false);
					break;
				//case "equip-wireless-ip-pool-allocation":
				//break;
				case "tx-termination":
					processVar.put(ProcessVariables.EOR_IP_CARD, false);
					scOrder.setTechnologyType("TXCARD");// TODO enum??
					break;
				case "tx-card-removal":
				case "eor_equip_tx_card_removal":
					processVar.put(ProcessVariables.EOR_IP_CARD, true);
					scOrder.setTechnologyType("WICARD");// TODO enum??
					break;
				default:
					throw new RuntimeException("Cannot deal with start process: " + processToTerminate);
			}
			processVar.put(MasterDefConstants.ORDER_CODE,scOrder.getOpOrderCode());
			processVar.put("eorIpTermination", true);
			processVar.put("orderCode",scOrder.getOpOrderCode());
			processVar.put("rejection",false);

			scOrderRepository.save(scOrder);

			LOGGER.info("Order Updated ");
			processVar.put(ORDER_TYPE,(String) scOrder.getOrderType());
			processVar.put(ORDER_CODE,(String) scOrder.getOpOrderCode());
			processVar.put(SERVICE_CODE, StringUtils.trimToEmpty(scOrder.getUuid()));
			processVar.put(SERVICE_ID,(Integer) scOrder.getId());
			processVar.put("rejection", false);

			System.out.println("======ProcessVariables "+ processVar);

			runtimeService.startProcessInstanceByKey(ProcessVariables.START_PROCESS_ICARD_PROV, processVar);

			LOGGER.info("initiate Termination completed for "+processToTerminate);



			Task task = taskService.findByOrderId(scOrder.getId());
			map.put("ORDER_CODE", orderCode);
			map.put("TASK_ID", task.getId()+"");
			map.put("WF_TASK_ID", task.getWfTaskId());
			map.put("TASK_NAME", task.getMstTaskDef().getName());
			System.out.println("Order Status ++++++++++++++++++++++++");
			map.put("orderStatus", task.getMstStatus().getCode());
			map.put("taskStatus", task.getMstStatus().getCode());

			MfTaskTrailBean mfTaskTrailBean = new MfTaskTrailBean();
			mfTaskTrailBean.setTaskId(task.getId());
			mfTaskTrailBean.setTaskName(task.getMstTaskDef().getName());
			mfTaskTrailBean.setUserGroup(nwaOrderDetailsBean.getOriginatorGroupId());
			mfTaskTrailBean.setAction(processToTerminate);
			mfTaskTrailBean.setCompletedBy(nwaOrderDetailsBean.getOriginatorName());
			mfTaskTrailBean.setCreatedTime(Timestamp.valueOf(LocalDateTime.now()));
			mfTaskTrailBean.setDescription("Termination Work Flow triggered");
			mfTaskTrailBean.setScenario(scOrder.getScenarioType());
			//mfTaskTrailBean.setComments("Termination Work Flow triggered");
			taskService.setTaskTrail(mfTaskTrailBean);
			System.out.println("Task trail table updated !");

			return map;
		}
		catch (Exception e){
			e.printStackTrace();
			LOGGER.error("Service Id {}, Task Id {}, Task {}, Wf Executor Id {}:: "+e.getMessage());
		}
		return map;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public String completeTask(String taskId, Map<String, Object> wfMap) {
		try {
			Task task = taskRepository.findByWfTaskId(taskId);
			MstStatus closedStatus = mstStatusRepository.findByCode("CLOSED");
			task.setMstStatus(closedStatus);
			taskRepository.save(task);

			flowableTaskService.complete(taskId, wfMap);

			return "SUCCESS";
		} catch (Exception ee) {
			LOGGER.error(ee.getMessage(), ee);
			return ee.getMessage();
		}
	}

	public List<NwaMakeModel> getModelByMake(String make){
		List<NwaMakeModel>  nwaMakeModel = nwaMakeModelRepository.findByMake(make);

		return nwaMakeModel;
	}

}
