package com.tcl.dias.serviceactivation.activation.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.Execution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.serviceactivation.activation.constants.AceConstants;
import com.tcl.dias.serviceactivation.beans.ConfigurationRequest;
import com.tcl.dias.serviceactivation.beans.IPServiceEndDateBean;
import com.tcl.dias.serviceactivation.beans.IpDetailsBean;
import com.tcl.dias.serviceactivation.beans.SatcoServiceDataRefreshBean;
import com.tcl.dias.serviceactivation.constants.ExceptionConstants;
import com.tcl.dias.serviceactivation.entity.entities.AclPolicyCriteria;
import com.tcl.dias.serviceactivation.entity.entities.AluSchedulerPolicy;
import com.tcl.dias.serviceactivation.entity.entities.Bgp;
import com.tcl.dias.serviceactivation.entity.entities.CambiumLastmile;
import com.tcl.dias.serviceactivation.entity.entities.ChannelizedE1serialInterface;
import com.tcl.dias.serviceactivation.entity.entities.ChannelizedSdhInterface;
import com.tcl.dias.serviceactivation.entity.entities.CiscoImportMap;
import com.tcl.dias.serviceactivation.entity.entities.Cpe;
import com.tcl.dias.serviceactivation.entity.entities.Eigrp;
import com.tcl.dias.serviceactivation.entity.entities.EthernetInterface;
import com.tcl.dias.serviceactivation.entity.entities.HsrpVrrpProtocol;
import com.tcl.dias.serviceactivation.entity.entities.InterfaceProtocolMapping;
import com.tcl.dias.serviceactivation.entity.entities.IpAddressDetail;
import com.tcl.dias.serviceactivation.entity.entities.IpaddrLanv4Address;
import com.tcl.dias.serviceactivation.entity.entities.IpaddrLanv6Address;
import com.tcl.dias.serviceactivation.entity.entities.IpaddrWanv4Address;
import com.tcl.dias.serviceactivation.entity.entities.IpaddrWanv6Address;
import com.tcl.dias.serviceactivation.entity.entities.LmComponent;
import com.tcl.dias.serviceactivation.entity.entities.Multicasting;
import com.tcl.dias.serviceactivation.entity.entities.NeighbourCommunityConfig;
import com.tcl.dias.serviceactivation.entity.entities.OrderDetail;
import com.tcl.dias.serviceactivation.entity.entities.Ospf;
import com.tcl.dias.serviceactivation.entity.entities.PolicyCriteria;
import com.tcl.dias.serviceactivation.entity.entities.PolicyType;
import com.tcl.dias.serviceactivation.entity.entities.PolicyTypeCriteriaMapping;
import com.tcl.dias.serviceactivation.entity.entities.PolicycriteriaProtocol;
import com.tcl.dias.serviceactivation.entity.entities.PrefixlistConfig;
import com.tcl.dias.serviceactivation.entity.entities.RadwinLastmile;
import com.tcl.dias.serviceactivation.entity.entities.RegexAspathConfig;
import com.tcl.dias.serviceactivation.entity.entities.Rip;
import com.tcl.dias.serviceactivation.entity.entities.RouterDetail;
import com.tcl.dias.serviceactivation.entity.entities.RouterUplinkport;
import com.tcl.dias.serviceactivation.entity.entities.ServiceCosCriteria;
import com.tcl.dias.serviceactivation.entity.entities.ServiceDetail;
import com.tcl.dias.serviceactivation.entity.entities.ServiceQo;
import com.tcl.dias.serviceactivation.entity.entities.StaticProtocol;
import com.tcl.dias.serviceactivation.entity.entities.Topology;
import com.tcl.dias.serviceactivation.entity.entities.UniswitchDetail;
import com.tcl.dias.serviceactivation.entity.entities.VlanQosProfile;
import com.tcl.dias.serviceactivation.entity.entities.VpnMetatData;
import com.tcl.dias.serviceactivation.entity.entities.VpnSolution;
import com.tcl.dias.serviceactivation.entity.entities.Vrf;
import com.tcl.dias.serviceactivation.entity.entities.WanStaticRoute;
import com.tcl.dias.serviceactivation.entity.entities.WimaxLastmile;
import com.tcl.dias.serviceactivation.entity.repository.AclPolicyCriteriaRepository;
import com.tcl.dias.serviceactivation.entity.repository.AluSchedulerPolicyRepository;
import com.tcl.dias.serviceactivation.entity.repository.BgpRepository;
import com.tcl.dias.serviceactivation.entity.repository.CambiumLastmileRepository;
import com.tcl.dias.serviceactivation.entity.repository.ChannelizedE1serialInterfaceRepository;
import com.tcl.dias.serviceactivation.entity.repository.ChannelizedSdhInterfaceRepository;
import com.tcl.dias.serviceactivation.entity.repository.CiscoImportMapRepository;
import com.tcl.dias.serviceactivation.entity.repository.CpeRepository;
import com.tcl.dias.serviceactivation.entity.repository.EigrpRepository;
import com.tcl.dias.serviceactivation.entity.repository.EthernetInterfaceRepository;
import com.tcl.dias.serviceactivation.entity.repository.HsrpVrrpProtocolRepository;
import com.tcl.dias.serviceactivation.entity.repository.InterfaceProtocolMappingRepository;
import com.tcl.dias.serviceactivation.entity.repository.IpAddressDetailRepository;
import com.tcl.dias.serviceactivation.entity.repository.IpaddrLanv4AddressRepository;
import com.tcl.dias.serviceactivation.entity.repository.IpaddrLanv6AddressRepository;
import com.tcl.dias.serviceactivation.entity.repository.IpaddrWanv4AddressRepository;
import com.tcl.dias.serviceactivation.entity.repository.IpaddrWanv6AddressRepository;
import com.tcl.dias.serviceactivation.entity.repository.LmComponentRepository;
import com.tcl.dias.serviceactivation.entity.repository.MulticastingRepository;
import com.tcl.dias.serviceactivation.entity.repository.NeighbourCommunityConfigRepository;
import com.tcl.dias.serviceactivation.entity.repository.OrderDetailRepository;
import com.tcl.dias.serviceactivation.entity.repository.OspfRepository;
import com.tcl.dias.serviceactivation.entity.repository.PolicyCriteriaRepository;
import com.tcl.dias.serviceactivation.entity.repository.PolicyTypeCriteriaMappingRepository;
import com.tcl.dias.serviceactivation.entity.repository.PolicyTypeRepository;
import com.tcl.dias.serviceactivation.entity.repository.PolicycriteriaProtocolRepository;
import com.tcl.dias.serviceactivation.entity.repository.PrefixlistConfigRepository;
import com.tcl.dias.serviceactivation.entity.repository.RadwinLastmileRepository;
import com.tcl.dias.serviceactivation.entity.repository.RegexAspathConfigRepository;
import com.tcl.dias.serviceactivation.entity.repository.RipRepository;
import com.tcl.dias.serviceactivation.entity.repository.RouterDetailRepository;
import com.tcl.dias.serviceactivation.entity.repository.RouterUplinkPortRepository;
import com.tcl.dias.serviceactivation.entity.repository.ServiceCosCriteriaRepository;
import com.tcl.dias.serviceactivation.entity.repository.ServiceDetailRepository;
import com.tcl.dias.serviceactivation.entity.repository.ServiceQoRepository;
import com.tcl.dias.serviceactivation.entity.repository.StaticProtocolRepository;
import com.tcl.dias.serviceactivation.entity.repository.TopologyRepository;
import com.tcl.dias.serviceactivation.entity.repository.UniswitchDetailRepository;
import com.tcl.dias.serviceactivation.entity.repository.VlanQosProfileRepository;
import com.tcl.dias.serviceactivation.entity.repository.VpnMetatDataRepository;
import com.tcl.dias.serviceactivation.entity.repository.VpnSolutionRepository;
import com.tcl.dias.serviceactivation.entity.repository.VrfRepository;
import com.tcl.dias.serviceactivation.entity.repository.WanStaticRouteRepository;
import com.tcl.dias.serviceactivation.entity.repository.WimaxLastmileRepository;
import com.tcl.dias.serviceactivation.service.ActivationService;
import com.tcl.dias.serviceactivation.service.CramerService;
import com.tcl.dias.serviceactivation.service.ServiceActivationService;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.Stg0SapPoDtlOptimus;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScContractInfoRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.Stg0SapPoDtlOptimusRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.beans.AclPolicyCriteriaBean;
import com.tcl.dias.servicefulfillmentutils.beans.AluSchedulerPolicyBean;
import com.tcl.dias.servicefulfillmentutils.beans.BSODetailsBean;
import com.tcl.dias.servicefulfillmentutils.beans.BgpBean;
import com.tcl.dias.servicefulfillmentutils.beans.CEDetailsBean;
import com.tcl.dias.servicefulfillmentutils.beans.CambiumLastmileBean;
import com.tcl.dias.servicefulfillmentutils.beans.ChannelizedE1serialInterfaceBean;
import com.tcl.dias.servicefulfillmentutils.beans.ChannelizedSdhInterfaceBean;
import com.tcl.dias.servicefulfillmentutils.beans.CiscoImportMapBean;
import com.tcl.dias.servicefulfillmentutils.beans.CpeBean;
import com.tcl.dias.servicefulfillmentutils.beans.EigrpBean;
import com.tcl.dias.servicefulfillmentutils.beans.EthernetInterfaceBean;
import com.tcl.dias.servicefulfillmentutils.beans.HsrpVrrpProtocolBean;
import com.tcl.dias.servicefulfillmentutils.beans.InterfaceDetailBean;
import com.tcl.dias.servicefulfillmentutils.beans.InterfaceProtocolMappingBean;
import com.tcl.dias.servicefulfillmentutils.beans.IpAddressDetailBean;
import com.tcl.dias.servicefulfillmentutils.beans.IpaddrLanv4AddressBean;
import com.tcl.dias.servicefulfillmentutils.beans.IpaddrLanv6AddressBean;
import com.tcl.dias.servicefulfillmentutils.beans.IpaddrWanv4AddressBean;
import com.tcl.dias.servicefulfillmentutils.beans.IpaddrWanv6AddressBean;
import com.tcl.dias.servicefulfillmentutils.beans.LmComponentBean;
import com.tcl.dias.servicefulfillmentutils.beans.MulticastingBean;
import com.tcl.dias.servicefulfillmentutils.beans.NeighbourCommunityConfigBean;
import com.tcl.dias.servicefulfillmentutils.beans.OrderDetailBean;
import com.tcl.dias.servicefulfillmentutils.beans.OspfBean;
import com.tcl.dias.servicefulfillmentutils.beans.PEDetailsBean;
import com.tcl.dias.servicefulfillmentutils.beans.PolicyCriteriaBean;
import com.tcl.dias.servicefulfillmentutils.beans.PolicyTypeBean;
import com.tcl.dias.servicefulfillmentutils.beans.PolicyTypeCriteriaMappingBean;
import com.tcl.dias.servicefulfillmentutils.beans.PrefixlistConfigBean;
import com.tcl.dias.servicefulfillmentutils.beans.RadwinLastmileBean;
import com.tcl.dias.servicefulfillmentutils.beans.RegexAspathConfigBean;
import com.tcl.dias.servicefulfillmentutils.beans.RipBean;
import com.tcl.dias.servicefulfillmentutils.beans.RouterDetailBean;
import com.tcl.dias.servicefulfillmentutils.beans.RouterUplinkportBean;
import com.tcl.dias.servicefulfillmentutils.beans.ServiceCosCriteriaBean;
import com.tcl.dias.servicefulfillmentutils.beans.ServiceDetailBean;
import com.tcl.dias.servicefulfillmentutils.beans.ServiceOrderDetailsBean;
import com.tcl.dias.servicefulfillmentutils.beans.ServiceQoBean;
import com.tcl.dias.servicefulfillmentutils.beans.StaticProtocolBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskBean;
import com.tcl.dias.servicefulfillmentutils.beans.TopologyBean;
import com.tcl.dias.servicefulfillmentutils.beans.TxDetailsBean;
import com.tcl.dias.servicefulfillmentutils.beans.UniswitchDetailBean;
import com.tcl.dias.servicefulfillmentutils.beans.VlanQosProfileBean;
import com.tcl.dias.servicefulfillmentutils.beans.VpnSolutionBean;
import com.tcl.dias.servicefulfillmentutils.beans.VrfBean;
import com.tcl.dias.servicefulfillmentutils.beans.WanStaticRouteBean;
import com.tcl.dias.servicefulfillmentutils.beans.WimaxLastmileBean;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.IpcConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.FlowableBaseService;
import com.tcl.dias.servicefulfillmentutils.service.v1.ServiceFulfillmentBaseService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Service
@Transactional(readOnly = true)
public class IPDetailsService extends ServiceFulfillmentBaseService {
	private static final Logger logger = LoggerFactory.getLogger(IPDetailsService.class);
	
	@Autowired
	ServiceDetailRepository serviceDetailRepository;

	@Autowired
	InterfaceProtocolMappingRepository interfaceProtocolMappingRepository;

	@Autowired
	TaskRepository taskRepository;

	@Autowired
	TaskService taskService;

	@Autowired
	PolicyCriteriaRepository policyCriteriaRepository;

	@Autowired
	OrderDetailRepository orderDetailRepository;

	@Autowired
	CiscoImportMapRepository ciscoImportMapRepository;

	@Autowired
	AluSchedulerPolicyRepository aluSchedulerPolicyRepository;

	@Autowired
	IpAddressDetailRepository ipAddressDetailRepository;

	@Autowired
	BgpRepository bgpRepository;

	@Autowired
	LmComponentRepository lmComponentRepository;

	@Autowired
	IpaddrLanv4AddressRepository ipaddrLanv4AddressRepository;

	@Autowired
	IpaddrLanv6AddressRepository ipaddrLanv6AddressRepository;

	@Autowired
	IpaddrWanv4AddressRepository ipaddrWanv4AddressRepository;

	@Autowired
	IpaddrWanv6AddressRepository ipaddrWanv6AddressRepository;

	@Autowired
	MulticastingRepository multicastingRepository;

	@Autowired
	ServiceQoRepository serviceQoRepository;

	@Autowired
	ServiceCosCriteriaRepository serviceCosCriteriaRepository;

	@Autowired
	VpnSolutionRepository vpnSolutionRepository;

	@Autowired
	VpnMetatDataRepository vpnMetatDataRepository;

	@Autowired
	VrfRepository vrfRepository;

	@Autowired
	PolicyTypeRepository policyTypeRepository;

	@Autowired
	TopologyRepository topologyRepository;

	@Autowired
	RouterUplinkPortRepository routerUplinkPortRepository;

	@Autowired
	UniswitchDetailRepository uniswitchDetailRepository;

	@Autowired
	EigrpRepository eigrpRepository;

	@Autowired
	CpeRepository cpeRepository;

	@Autowired
	EthernetInterfaceRepository ethernetInterfaceRepository;

	@Autowired
	ChannelizedE1serialInterfaceRepository channelizedE1serialInterfaceRepository;

	@Autowired
	ChannelizedSdhInterfaceRepository channelizedSdhInterfaceRepository;

	@Autowired
	OspfRepository ospfRepository;

	@Autowired
	StaticProtocolRepository staticProtocolRepository;

	@Autowired
	RipRepository ripRepository;

	@Autowired
	RouterDetailRepository routerDetailRepository;

	@Autowired
	WanStaticRouteRepository wanStaticRouteRepository;

	@Autowired
	AclPolicyCriteriaRepository aclPolicyCriteriaRepository;

	@Autowired
	PolicyTypeCriteriaMappingRepository policyTypeCriteriaMappingRepository;

	@Autowired
	HsrpVrrpProtocolRepository hsrpVrrpProtocolRepository;

	@Autowired
	NeighbourCommunityConfigRepository neighbourCommunityConfigRepository;

	@Autowired
	PrefixlistConfigRepository prefixlistConfigRepository;

	@Autowired
	RegexAspathConfigRepository regexAspathConfigRepository;

	@Autowired
	CambiumLastmileRepository cambiumLastmileRepository;

	@Autowired
	WimaxLastmileRepository wimaxLastmileRepository;

	@Autowired
	RadwinLastmileRepository radwinLastmileRepository;

	@Autowired
	VlanQosProfileRepository vlanQosProfileRepository;

	@Autowired
	PolicycriteriaProtocolRepository policyCriteriaProtocolRepository;

	@Autowired
	ActivationService activationService;
	

    @Autowired
    ComponentAndAttributeService componentAndAttributeService;
    

	@Autowired
	ScComponentAttributesRepository scComponentAttributesRepository;

	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;
	
	@Autowired
	CramerService cramerService;

	@Autowired
	Stg0SapPoDtlOptimusRepository stg0SapPoDtlOptimusRepository;

	@Autowired
	ScServiceAttributeRepository scServiceAttributeRepository;
	
	@Autowired
	FlowableBaseService flowableBaseService;
	  
	@Autowired
	RuntimeService runtimeService;
	
	@Autowired
	ScContractInfoRepository scContractInfoRepository;
	
	@Autowired
	ServiceActivationService serviceActivationService;
	
	@Autowired
	VpnMetatDataRepository vpnMetaDataRepository;

	/**
	 * Get the current service detils by service code.
	 *
	 * @param serviceCode
	 * @return
	 */
	
	Timestamp startDate = new Timestamp(new Date().getTime());
	Timestamp endDate = null;
	Timestamp lastModifiedDate = new Timestamp(new Date().getTime());
	
	String modifiedBy = "Optimus_Initial";
	
    public com.tcl.dias.common.beans.ServiceDetailBean getCurrentServiceDetailsByUuid(String serviceCode) throws TclCommonException {
    	logger.info("Service code received for getCurrentServiceDetailsByUuid : {}",serviceCode);
		com.tcl.dias.common.beans.ServiceDetailBean serviceDetailBean = new com.tcl.dias.common.beans.ServiceDetailBean();
		ServiceDetail serviceDetail = serviceDetailRepository.findFirstByServiceIdAndEndDateIsNull(serviceCode);
		if(Objects.nonNull(serviceDetail)) {
			serviceDetailBean.setServiceState(serviceDetail.getServiceState());
			BeanUtils.copyProperties(serviceDetail, serviceDetailBean);
		} 
		logger.info("Current service details : {}", Utils.convertObjectToJson(serviceDetailBean));
		return serviceDetailBean;
	}

	

	@Transactional(readOnly = true)
	public TaskBean getIpServiceDetails(Integer taskId, Integer version, String serviceId,String wfTaskId) throws TclCommonException {

		String serviceCode = null;
		String orderCode = null;
		ServiceDetail serviceDetail = null;
		TaskBean taskBean = null;
		Integer scServiceDetailId = null;

		boolean required = false;

		if(taskId!=null && wfTaskId!=null){
			logger.info("getIpServiceDetails invoked for Task Id {}",taskId);
			taskBean = taskService.getTaskBasedOnTaskId(taskId,wfTaskId);
			Optional<Task> taskOptional = taskRepository.findById(taskId);
			if (taskOptional.isPresent()) {
				Task task = taskOptional.get();
				serviceCode = task.getServiceCode();
				orderCode=task.getOrderCode();
				scServiceDetailId = task.getServiceId();
			}
			Integer versionLimit = serviceDetailRepository.countByServiceId(serviceCode).intValue();
			List<Integer> versionList = serviceDetailRepository.getVersionByServiceCode(serviceCode);
			if(taskBean.getCommonData()!=null){
				Map<String, Object> attrMap = taskBean.getCommonData();
				try {
					if (attrMap.containsKey("portBandwidth")) {
						attrMap.put("portBandwidth", Double.valueOf(attrMap.get("portBandwidth").toString()));
					}
					if (attrMap.containsKey("localLoopBandwidth")) {
						attrMap.put("localLoopBandwidth", Double.valueOf(attrMap.get("localLoopBandwidth").toString()));
					}
				} catch (Exception e) {
					logger.error("error converting double data for port bandwidth and local loop bandwidth : " + e);
				}
				attrMap.put("versionList", versionList);
				attrMap.put("versionLimit", versionLimit);
			}
		}
		else{
			taskBean = new TaskBean();
			serviceCode = serviceId;
		}

		if(version==null && Objects.isNull(orderCode)){
			logger.info("orderCode not exists, version not exists ");
			serviceDetail = serviceDetailRepository.findFirstByServiceIdAndServiceStateInAndEndDateIsNullOrderByVersionDesc(serviceCode,Arrays.asList(TaskStatusConstants.ISSUED,TaskStatusConstants.ACTIVE));
		}else if(version==null && Objects.nonNull(orderCode)){
			OrderDetail orderDetail=orderDetailRepository.findByExtOrderrefId(orderCode);
			if(Objects.nonNull(orderDetail)){
				logger.info("orderCode exists, version not exists {}",orderCode);
				serviceDetail = serviceDetailRepository.findFirstByServiceIdAndOrderDetailAndServiceStateInAndEndDateIsNullOrderByVersionDesc(serviceCode,orderDetail,Arrays.asList(TaskStatusConstants.ISSUED,TaskStatusConstants.ACTIVE));
			}
		}else if(Objects.nonNull(version) && Objects.nonNull(orderCode)){
			OrderDetail orderDetail=orderDetailRepository.findByExtOrderrefId(orderCode);
			if(Objects.nonNull(orderDetail)){
				logger.info("orderCode and version exists {},{}",orderCode,version);
				serviceDetail = serviceDetailRepository.findByServiceIdAndOrderDetailAndVersion(serviceCode,orderDetail,version);
				if (Objects.isNull(serviceDetail)) {
					serviceDetail = serviceDetailRepository.findFirstByServiceIdAndVersionOrderByIdDesc(serviceCode,version);
				}
				required = true;
			}
		}else{
			logger.info("version exists {}",version);
			serviceDetail = serviceDetailRepository.findFirstByServiceIdAndVersionOrderByIdDesc(serviceCode,version);
			required = true;
		}

		// If still record not found, simply pick from serviceId latest record (Issued or Active)
		if(Objects.isNull(serviceDetail)){
			serviceDetail = serviceDetailRepository.findFirstByServiceIdAndServiceStateInAndEndDateIsNullOrderByVersionDesc(serviceCode,
					Arrays.asList(TaskStatusConstants.ISSUED, TaskStatusConstants.ACTIVE));
		}

		OrderDetailBean bean = new OrderDetailBean();
		if(Objects.nonNull(serviceDetail)){
			logger.info("Service Detail exists::{}",serviceDetail.getId());
			if(Objects.nonNull(serviceDetail.getOrderDetail())){
				logger.info("Order Detail exists");
				setOrderDetailBean(bean, serviceDetail);
			}
			ServiceDetailBean serviceDetailBean = new ServiceDetailBean();
			setServiceDetailBean(serviceDetailBean, serviceDetail);
			//changes related to
			//Code changes for BSODetails
			
			constructTopologyAndUniDetails(serviceDetail, serviceDetailBean, required);
			constructServiceQo(serviceDetail, serviceDetailBean, required);
			//constructCiscoImport(serviceDetail, serviceDetailBean, required);
			constructInterfaceProtocolDetails(serviceDetail, serviceDetailBean, required);
			constructIpAddressDetails(serviceDetail, serviceDetailBean, required);
			constructMultiCastDetails(serviceDetail, serviceDetailBean, required);
			constructSchedulerPolicy(serviceDetail, serviceDetailBean, required);
			constructVrfDetails(serviceDetail, serviceDetailBean, required);
			constructVpnSolutionDetails(serviceDetail, serviceDetailBean, required);
			constructLmComponentDetails(serviceDetail, serviceDetailBean, required);
			if (serviceDetail.getScServiceDetailId()!=null) {
				constructMultiVrfAttribute(serviceDetail.getScServiceDetailId(), serviceDetailBean);
			}
			bean.getServiceDetailBeans().add(serviceDetailBean);
		}
		
		BSODetailsBean bsoDetailsBean =new BSODetailsBean();
		String lmType = null;
		String bsoCircuitId = null;
		Stg0SapPoDtlOptimus stg0SapPoDtlOptimus  = stg0SapPoDtlOptimusRepository.findFirstByTclServiceIdAndProductComponentOrderByPoCreationDateDesc(serviceCode);
		ScServiceDetail scServiceDetail;
		if(null != orderCode && null != scServiceDetailId && orderCode.startsWith(IpcConstants.IPC)) {
			scServiceDetail = scServiceDetailRepository.findById(scServiceDetailId).get();
		} else {
			scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode,"INPROGRESS");
		}
		if(scServiceDetail==null)scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode,"ACTIVE");
		if(scServiceDetail!=null) {
			ScComponentAttribute lmTypeAttribute = scComponentAttributesRepository.
				findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(scServiceDetail.getId(), "lmType", "LM", "A");
			ScComponentAttribute bsoCircuitIdAttribute = scComponentAttributesRepository.
				findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(scServiceDetail.getId(), "bsoCircuitId", "LM", "A");
			if(Objects.nonNull(bsoCircuitIdAttribute)) bsoCircuitId=bsoCircuitIdAttribute.getAttributeValue();
			if(Objects.nonNull(stg0SapPoDtlOptimus)) {
			if(Objects.isNull(bsoCircuitIdAttribute) || bsoCircuitId.isEmpty()) {
				bsoCircuitId=stg0SapPoDtlOptimus.getVendorRefIdOrderId();
			}
			}
			
			if((Objects.nonNull(lmTypeAttribute) && lmTypeAttribute.getAttributeValue()!=null && lmTypeAttribute.getAttributeValue().toLowerCase().contains("offnet"))) {
				if (Objects.nonNull(stg0SapPoDtlOptimus))
					setBSODetailsBean(bsoDetailsBean, stg0SapPoDtlOptimus, bsoCircuitId);
				else {
					if (Objects.nonNull(bsoCircuitId) && (!bsoCircuitId.isEmpty()))
						bsoDetailsBean.setVendorRefIdOrderId(bsoCircuitId);
				}

			}
		}else {
			if (Objects.nonNull(stg0SapPoDtlOptimus))
				setBSODetailsBean(bsoDetailsBean, stg0SapPoDtlOptimus, bsoCircuitId);
		}
		taskBean.setBsoDetailsBean(bsoDetailsBean);

		//taskBean.setOrderDetails(bean);
		TxDetailsBean txDetailsBean = new TxDetailsBean();
		txDetailsBean.setNodesToConfigure(activationService.getNodesToBeConfigured(taskBean.getServiceCode()));
		taskBean.setTxDetails(txDetailsBean);
		taskBean.setOrderDetails(bean);
		return taskBean;
	}
	
	@Transactional(readOnly = false)
	public ConfigurationRequest configureAction(ConfigurationRequest configurationRequest) throws TclCommonException{

		ConfigurationRequest request = new ConfigurationRequest();
		Map<String, Object> map = new HashMap<>();
		map.put("action", configurationRequest.getAction());
		map.put("serviceConfigurationAction", configurationRequest.getAction());
		taskService.manuallyCompleteTask(configurationRequest.getTaskId(), map);
		return request;
	}

	private Boolean convertByteToBoolean(Byte value){
		return value!=null && value!=0;
	}

	private Byte convertBooleanToByte(Boolean value){
		return (value==null || value==false) ? (byte) 0 : (byte) 1 ;
	}

	private InterfaceDetailBean constructInterfaceDetail(InterfaceProtocolMapping interfaceProtocolMapping){
		InterfaceDetailBean interfaceDetailBean = new InterfaceDetailBean();
		interfaceDetailBean.setEndDate(interfaceProtocolMapping.getEndDate());
		interfaceDetailBean.setInterfaceProtocolMappingId(interfaceProtocolMapping.getInterfaceProtocolMappingId());
		interfaceDetailBean.setIscpeLanInterface(convertByteToBoolean(interfaceProtocolMapping.getIscpeLanInterface()));
		interfaceDetailBean.setIscpeWanInterface(convertByteToBoolean(interfaceProtocolMapping.getIscpeWanInterface()));
		interfaceDetailBean.setModifiedBy(interfaceProtocolMapping.getModifiedBy());
		return interfaceDetailBean;
	}

	public void setLmComponentBean(LmComponentBean lmComponentBean, LmComponent lmComponent) {
		lmComponentBean.setLmComponentId(lmComponent.getLmComponentId());
		lmComponentBean.setLmOnwlProvider(lmComponent.getLmOnwlProvider());
		lmComponentBean.setModifiedBy(lmComponent.getModifiedBy());
		lmComponentBean.setRemarks(lmComponent.getRemarks());
		lmComponentBean.setVersion(lmComponent.getVersion());
		lmComponentBean.setStartDate(lmComponent.getStartDate());
		lmComponentBean.setEndDate(lmComponent.getEndDate());
		lmComponentBean.setLastModifiedDate(lmComponent.getLastModifiedDate());
	}

	private void constructLmComponentDetails(ServiceDetail serviceDetail, ServiceDetailBean serviceDetailBean, boolean required) {
		if(!CollectionUtils.isEmpty(serviceDetail.getLmComponents())){
			serviceDetail.getLmComponents().forEach(lmComponent -> {
				if(required || Objects.isNull(lmComponent.getEndDate())){
					LmComponentBean lmComponentBean = new LmComponentBean();
					setLmComponentBean(lmComponentBean, lmComponent);
					if(!CollectionUtils.isEmpty(lmComponent.getRadwinLastmiles())){
						lmComponent.getRadwinLastmiles().forEach(radwinLastmile -> {
							if(required || Objects.isNull(radwinLastmile.getEndDate())) {
								RadwinLastmileBean radwinLastmileBean = new RadwinLastmileBean();
								setRadwinLastmileBean(radwinLastmileBean, radwinLastmile);
								lmComponentBean.getRadwinLastmiles().add(radwinLastmileBean);
							}
						});
					}
					if(!CollectionUtils.isEmpty(lmComponent.getCambiumLastmiles())){
						lmComponent.getCambiumLastmiles().forEach(cambiumLastmile -> {
							if(required || Objects.isNull(cambiumLastmile.getEndDate())){
								CambiumLastmileBean cambiumLastmileBean = new CambiumLastmileBean();
								setCambiumLastmileBean(cambiumLastmileBean, cambiumLastmile);
								lmComponentBean.getCambiumLastmiles().add(cambiumLastmileBean);
							}
						});
					}
					if(!CollectionUtils.isEmpty(lmComponent.getWimaxLastmiles())){
						lmComponent.getWimaxLastmiles().forEach(wimaxLastmile -> {
							if(required || Objects.isNull(wimaxLastmile.getEndDate())){
								WimaxLastmileBean wimaxLastmileBean = new WimaxLastmileBean();
								setWimaxLastmileBean(wimaxLastmileBean, wimaxLastmile);
								if(!CollectionUtils.isEmpty(wimaxLastmile.getVlanQosProfiles())){
									wimaxLastmile.getVlanQosProfiles().forEach(vlanQosProfile -> {
										VlanQosProfileBean vlanQosProfileBean = new VlanQosProfileBean();
										setVlanQosProfileBean(vlanQosProfileBean, vlanQosProfile);
										wimaxLastmileBean.getVlanQosProfiles().add(vlanQosProfileBean);
									});
								}
								lmComponentBean.getWimaxLastmiles().add(wimaxLastmileBean);
							}
						});
					}
					serviceDetailBean.getLmComponentBeans().add(lmComponentBean);
				}
			});
		}
	}

	public void constructVpnSolutionDetails(ServiceDetail serviceDetail, ServiceDetailBean serviceDetailBean, boolean required) {
		logger.info("constructVpnSolutionDetails method invoked for service Id: {}", serviceDetail.getId());
		if(!CollectionUtils.isEmpty(serviceDetail.getVpnMetatDatas())){
			logger.info("vpnmetaData exists for service Id: {}", serviceDetail.getId());
			serviceDetail.getVpnMetatDatas().forEach(vpnMetatData -> {
				if(required || Objects.isNull(vpnMetatData.getEndDate())){
					logger.info("vpnmetaData endDate is null for Id: {}", vpnMetatData.getId());
					VpnSolutionBean vpnSolutionBean = new VpnSolutionBean();
					vpnSolutionBean = setVpnSolutionBean(vpnSolutionBean, vpnMetatData);
					serviceDetailBean.getVpnSolutionBeans().add(vpnSolutionBean);
				}
			});
		}
	}

	public void constructVrfDetails(ServiceDetail serviceDetail, ServiceDetailBean serviceDetailBean, boolean required) {
		if (!CollectionUtils.isEmpty(serviceDetail.getVrfs())) {
			serviceDetail.getVrfs().forEach(vrf -> {
				if (required || Objects.isNull(vrf.getEndDate())) {
					VrfBean vrfBean = new VrfBean();
					setVrfBean(vrfBean, vrf);
					if (!CollectionUtils.isEmpty(vrf.getPolicyTypes())) {
						vrf.getPolicyTypes().forEach(policyType -> {
							if (required || Objects.isNull(policyType.getEndDate())) {
								PolicyTypeBean policyTypeBean = new PolicyTypeBean();
								setPolicyTypeBean(policyTypeBean, policyType);
								vrfBean.getPolicyTypes().add(policyTypeBean);
							}
						});
					}
					serviceDetailBean.getVrfBeans().add(vrfBean);
				}
			});
		}
	}

	public void constructSchedulerPolicy(ServiceDetail serviceDetail, ServiceDetailBean serviceDetailBean, boolean required) {
		List<AluSchedulerPolicyBean> aluSchedulerPolicyBeans = new ArrayList<>();
    	if(!CollectionUtils.isEmpty(serviceDetail.getAluSchedulerPolicies())){
			serviceDetail.getAluSchedulerPolicies().forEach(aluSchedulerPolicy -> {
				if(required || Objects.isNull(aluSchedulerPolicy.getEndDate())) {
					AluSchedulerPolicyBean aluSchedulerPolicyBean = new AluSchedulerPolicyBean();
					aluSchedulerPolicyBean = setAluSchedulerPolicyBean(aluSchedulerPolicyBean, aluSchedulerPolicy);
					aluSchedulerPolicyBeans.add(aluSchedulerPolicyBean);
				}
			});
			serviceDetailBean.setAluSchedulerPolicyBeans(aluSchedulerPolicyBeans);
		}
	}

	public void constructMultiCastDetails(ServiceDetail serviceDetail, ServiceDetailBean serviceDetailBean, boolean required) {
		if(!CollectionUtils.isEmpty(serviceDetail.getMulticastings())){
			serviceDetail.getMulticastings().forEach(multicasting -> {
				if(required || Objects.isNull(multicasting.getEndDate())){
					MulticastingBean multicastingBean = new MulticastingBean();
					multicastingBean = setMulticastingBean(multicastingBean, multicasting);
					serviceDetailBean.getMulticastingBeans().add(multicastingBean);
				}
			});
		}
	}

	public void constructIpAddressDetails(ServiceDetail serviceDetail, ServiceDetailBean serviceDetailBean, boolean required) {
		logger.info("constructIpAddressDetails method invoked for serviceid::{}",serviceDetail.getId());
		if(!CollectionUtils.isEmpty(serviceDetail.getIpAddressDetails())){
			logger.info("serviceDetail.getIpAddressDetails() for serviceid::{}",serviceDetail.getId());
			serviceDetail.getIpAddressDetails().forEach(ipAddressDetail -> {
				if(required || Objects.isNull(ipAddressDetail.getEndDate())) {
					logger.info("ipAddressDetail exists::{} for serviceid::{}",ipAddressDetail.getIP_Address_Details(),serviceDetail.getId());
					IpAddressDetailBean ipAddressDetailBean = new IpAddressDetailBean();
					setIpAddressDetailBean(ipAddressDetailBean, ipAddressDetail);
					if (!CollectionUtils.isEmpty(ipAddressDetail.getIpaddrLanv4Addresses())) {
						ipAddressDetail.getIpaddrLanv4Addresses().forEach(ipaddrLanv4Address -> {
							if(required || Objects.isNull(ipaddrLanv4Address.getEndDate())) {
								IpaddrLanv4AddressBean ipaddrLanv4AddressBean = new IpaddrLanv4AddressBean();
								setIpaddrLanv4AddressBean(ipaddrLanv4AddressBean, ipaddrLanv4Address);
								ipAddressDetailBean.getIpaddrLanv4Addresses().add(ipaddrLanv4AddressBean);
							}
						});
					}
					if (!CollectionUtils.isEmpty(ipAddressDetail.getIpaddrLanv6Addresses())) {
						ipAddressDetail.getIpaddrLanv6Addresses().forEach(ipaddrLanv6Address -> {
							if(required || Objects.isNull(ipaddrLanv6Address.getEndDate())) {
								IpaddrLanv6AddressBean ipaddrLanv6AddressBean = new IpaddrLanv6AddressBean();
								setIpaddrLanv6AddressBean(ipaddrLanv6AddressBean, ipaddrLanv6Address);
								ipAddressDetailBean.getIpaddrLanv6Addresses().add(ipaddrLanv6AddressBean);
							}
						});
					}
					if (!CollectionUtils.isEmpty(ipAddressDetail.getIpaddrWanv4Addresses())) {
						ipAddressDetail.getIpaddrWanv4Addresses().forEach(ipaddrWanv4Address -> {
							if(required || Objects.isNull(ipaddrWanv4Address.getEndDate())) {
								IpaddrWanv4AddressBean ipaddrWanv4AddressBean = new IpaddrWanv4AddressBean();
								setIpaddrWanv4AddressBean(ipaddrWanv4AddressBean, ipaddrWanv4Address);
								ipAddressDetailBean.getIpaddrWanv4Addresses().add(ipaddrWanv4AddressBean);
							}
						});
					}
					if (!CollectionUtils.isEmpty(ipAddressDetail.getIpaddrWanv6Addresses())) {
						ipAddressDetail.getIpaddrWanv6Addresses().forEach(ipaddrWanv6Address -> {
							if(required || Objects.isNull(ipaddrWanv6Address.getEndDate())) {
								IpaddrWanv6AddressBean ipaddrWanv6AddressBean = new IpaddrWanv6AddressBean();
								setIpaddrWanv6AddressBean(ipaddrWanv6AddressBean, ipaddrWanv6Address);
								ipAddressDetailBean.getIpaddrWanv6Addresses().add(ipaddrWanv6AddressBean);
							}
						});
					}
					List<ScServiceAttribute> serviceAttributes = scServiceAttributeRepository
							.findByScServiceDetail_idAndAttributeNameIn(serviceDetail.getScServiceDetailId(),
									Arrays.asList("Additional IPs", "IP Address Arrangement", "IPv4 Address Pool Size", "IPv6 Address Pool Size", "IPv4 Address Pool Size for Additional IPs", "IPv6 Address Pool Size for Additional IPs"));
					Map<String, String> attributeMap =commonFulfillmentUtils.getServiceAttributesAttributes(serviceAttributes);
					String additionalIps = attributeMap.getOrDefault("Additional IPs", null);
					String ipAddressArrangement = attributeMap.getOrDefault("IP Address Arrangement", null);
					String ipv4AddressPoolSize = attributeMap.getOrDefault("IPv4 Address Pool Size", null);
					String ipv6AddressPoolSize = attributeMap.getOrDefault("IPv6 Address Pool Size", null);
					String ipv4AddressPoolSizeForAdditionalIps = attributeMap.getOrDefault("IPv4 Address Pool Size for Additional IPs", null);
					String ipv6AddressPoolSizeForAdditionalIps = attributeMap.getOrDefault("IPv6 Address Pool Size for Additional IPs", null);
					ipAddressDetailBean.setAdditionalIps(additionalIps);
					ipAddressDetailBean.setIpAddressArrangement(ipAddressArrangement);
					ipAddressDetailBean.setIpv4AddressPoolSize(ipv4AddressPoolSize);
					ipAddressDetailBean.setIpv6AddressPoolSize(ipv6AddressPoolSize);
					ipAddressDetailBean.setIpv4AddressPoolSizeForAdditionalIps(ipv4AddressPoolSizeForAdditionalIps);
					ipAddressDetailBean.setIpv6AddressPoolSizeForAdditionalIps(ipv6AddressPoolSizeForAdditionalIps);
					serviceDetailBean.getIpAddressDetailBeans().add(ipAddressDetailBean);
				}
			});
		}
	}

	private void constructInterfaceProtocolDetails(ServiceDetail serviceDetail, ServiceDetailBean serviceDetailBean, boolean required) {

		InterfaceProtocolMappingBean interfaceProtocolMappingBean = getInterfaceProtocolMappingBean(serviceDetail,
				required);
		serviceDetailBean.setInterfaceProtocolMappingBean(interfaceProtocolMappingBean);
	}



	private InterfaceProtocolMappingBean getInterfaceProtocolMappingBean(ServiceDetail serviceDetail,
			boolean required) {
		PEDetailsBean peDetailsBean = new PEDetailsBean();
		CEDetailsBean ceDetailsBean = new CEDetailsBean();

		InterfaceProtocolMappingBean interfaceProtocolMappingBean = new InterfaceProtocolMappingBean();

		if(!CollectionUtils.isEmpty(serviceDetail.getInterfaceProtocolMappings())){
			serviceDetail.getInterfaceProtocolMappings().forEach(ipm -> {
				if(required || Objects.isNull(ipm.getEndDate())){
					if(ipm.getIscpeWanInterface()!=null && ipm.getIscpeLanInterface()!=null
							&& ipm.getIscpeWanInterface()==0 && ipm.getIscpeLanInterface()==0 && ipm.getRouterDetail()!=null){

						if(Objects.nonNull(ipm.getChannelizedE1serialInterface()) && (required || Objects.isNull(ipm.getChannelizedE1serialInterface().getEndDate()))){
							peDetailsBean.getChannelizedE1serialInterfaceBeans().add(constructChannelizedE1InterfaceDetails(ipm,required));
						}
						if(Objects.nonNull(ipm.getChannelizedSdhInterface()) && (required || Objects.isNull(ipm.getChannelizedSdhInterface().getEndDate()))){
							peDetailsBean.getChannelizedSdhInterfaceBeans().add(constructChannelizedSdhInterfaceDetails(ipm, required));
						}
						if(Objects.nonNull(ipm.getEthernetInterface()) && (required || Objects.isNull(ipm.getEthernetInterface().getEndDate()))){
							peDetailsBean.getEthernetInterfaceBeans().add(constructEthernetInterfaceDetails(ipm, required));
						}
						if(Objects.nonNull(ipm.getBgp()) && (required || Objects.isNull(ipm.getBgp().getEndDate()))){
							peDetailsBean.getBgpBeans().add(constructBgpDetails(ipm, required));
						}
						if(Objects.nonNull(ipm.getStaticProtocol()) && (required || Objects.isNull(ipm.getStaticProtocol().getEndDate()))){
							peDetailsBean.getStaticProtocolBeans().add(constructStaticDetails(ipm, required));
						}
						if(Objects.nonNull(ipm.getCpe()) && (required || Objects.isNull(ipm.getCpe().getEndDate()))){
							peDetailsBean.getCpeBeans().add(constructCpeDetails(ipm));			// TODO
						}
						if(Objects.nonNull(ipm.getOspf()) && (required || Objects.isNull(ipm.getOspf().getEndDate()))){
							peDetailsBean.getOspfBeans().add(constructOspfDetails(ipm, required));
						}
						if(Objects.nonNull(ipm.getRip()) && (required || Objects.isNull(ipm.getRip().getEndDate()))){
							peDetailsBean.getRipBeans().add(constructRipDetails(ipm, required));
						}
						if(Objects.nonNull(ipm.getEigrp()) && (required || Objects.isNull(ipm.getEigrp().getEndDate()))){
							peDetailsBean.getEigrpBeans().add(constructEigrpDetails(ipm));
						}
						if(Objects.nonNull(ipm.getRouterDetail()) && (required || Objects.isNull(ipm.getRouterDetail().getEndDate()))){
							peDetailsBean.getRouterDetailBeans().add(constructRouterDetails(ipm));
						}
					}
					else if(ipm.getCpe()!=null && ipm.getIscpeLanInterface()!=null && ipm.getIscpeWanInterface()!=null &&
							(ipm.getIscpeLanInterface()==1 || ipm.getIscpeWanInterface()==1)){

						if(Objects.nonNull(ipm.getChannelizedE1serialInterface()) && (required || Objects.isNull(ipm.getChannelizedE1serialInterface().getEndDate()))){
							ceDetailsBean.getChannelizedE1serialInterfaceBeans().add(constructChannelizedE1InterfaceDetails(ipm,required));
						}
						if(Objects.nonNull(ipm.getChannelizedSdhInterface()) && (required || Objects.isNull(ipm.getChannelizedSdhInterface().getEndDate()))){
							ceDetailsBean.getChannelizedSdhInterfaceBeans().add(constructChannelizedSdhInterfaceDetails(ipm,required));
						}
						if(Objects.nonNull(ipm.getEthernetInterface()) && (required || Objects.isNull(ipm.getEthernetInterface().getEndDate()))){
							ceDetailsBean.getEthernetInterfaceBeans().add(constructEthernetInterfaceDetails(ipm,required));
						}
						if(Objects.nonNull(ipm.getBgp()) && (required || Objects.isNull(ipm.getBgp().getEndDate()))){
							ceDetailsBean.getBgpBeans().add(constructBgpDetails(ipm, required));
						}
						if(Objects.nonNull(ipm.getStaticProtocol()) && (required || Objects.isNull(ipm.getStaticProtocol().getEndDate()))){
							ceDetailsBean.getStaticProtocolBeans().add(constructStaticDetails(ipm, required));
						}
						if(Objects.nonNull(ipm.getCpe()) && (required || Objects.isNull(ipm.getCpe().getEndDate()))){
							ceDetailsBean.getCpeBeans().add(constructCpeDetails(ipm));            // TODO
						}
						if(Objects.nonNull(ipm.getOspf()) && (required || Objects.isNull(ipm.getOspf().getEndDate()))){
							ceDetailsBean.getOspfBeans().add(constructOspfDetails(ipm, required));
						}
						if(Objects.nonNull(ipm.getRip()) && (required || Objects.isNull(ipm.getRip().getEndDate()))){
							ceDetailsBean.getRipBeans().add(constructRipDetails(ipm, required));
						}
						if(Objects.nonNull(ipm.getEigrp()) && (required || Objects.isNull(ipm.getEigrp().getEndDate()))){
							ceDetailsBean.getEigrpBeans().add(constructEigrpDetails(ipm));
						}
						if(Objects.nonNull(ipm.getRouterDetail()) && (required || Objects.isNull(ipm.getRouterDetail().getEndDate()))){
							ceDetailsBean.getRouterDetailBeans().add(constructRouterDetails(ipm));
						}
					}
					interfaceProtocolMappingBean.setPeDetailsBean(peDetailsBean);
					interfaceProtocolMappingBean.setCeDetailsBean(ceDetailsBean);
				}
			});
		}
		return interfaceProtocolMappingBean;
	}

	private EigrpBean constructEigrpDetails(InterfaceProtocolMapping ipm){
		EigrpBean eigrpBean = new EigrpBean();
		setEigrpBean(eigrpBean,ipm.getEigrp());
		eigrpBean.setInterfaceDetailBean(constructInterfaceDetail(ipm));
		return eigrpBean;
	}

	public OspfBean constructOspfDetails(InterfaceProtocolMapping ipm, boolean required){

    	OspfBean ospfBean = new OspfBean();
		setOspfBean(ospfBean,ipm.getOspf());

		if(!CollectionUtils.isEmpty(ipm.getOspf().getPolicyTypes())){
			ipm.getOspf().getPolicyTypes().forEach(policyType -> {
				if(required || Objects.isNull(policyType.getEndDate())){
					PolicyTypeBean policyTypeBean = new PolicyTypeBean();
					setPolicyTypeBean(policyTypeBean, policyType);
					ospfBean.getPolicyTypes().add(policyTypeBean);
				}
			});
		}

		ospfBean.setInterfaceDetailBean(constructInterfaceDetail(ipm));
		return ospfBean;
	}

	private RipBean constructRipDetails(InterfaceProtocolMapping ipm, boolean required){
		RipBean ripBean = new RipBean();
		setRipBean(ripBean, ipm.getRip());

		if(!CollectionUtils.isEmpty(ipm.getRip().getPolicyTypes())){
			ipm.getRip().getPolicyTypes().forEach(policyType -> {
				if(required || Objects.isNull(policyType.getEndDate())){
					PolicyTypeBean policyTypeBean = new PolicyTypeBean();
					setPolicyTypeBean(policyTypeBean, policyType);
					ripBean.getPolicyTypes().add(policyTypeBean);
				}
			});
		}

		ripBean.setInterfaceDetailBean(constructInterfaceDetail(ipm));
		return ripBean;
	}

	private RouterDetailBean constructRouterDetails(InterfaceProtocolMapping interfaceProtocolMapping) {

		RouterDetailBean routerDetailBean = new RouterDetailBean();
		setRouterDetailBean(routerDetailBean, interfaceProtocolMapping.getRouterDetail());
		routerDetailBean.setInterfaceDetailBean(constructInterfaceDetail(interfaceProtocolMapping));
		return routerDetailBean;
    }

	private CpeBean constructCpeDetails(InterfaceProtocolMapping interfaceProtocolMapping) {

		CpeBean cpeBean = new CpeBean();

        if (interfaceProtocolMapping != null && interfaceProtocolMapping.getCpe()!=null) {

			setCpeBean(cpeBean, interfaceProtocolMapping.getCpe());
		}

        cpeBean.setInterfaceDetailBean(constructInterfaceDetail(interfaceProtocolMapping));

        return cpeBean;
	}

	private StaticProtocolBean constructStaticDetails(InterfaceProtocolMapping ipm, boolean required) {

		StaticProtocolBean staticProtocolBean = new StaticProtocolBean();
		setStaticProtocolBean(staticProtocolBean, ipm.getStaticProtocol());

		if (!CollectionUtils.isEmpty(ipm.getStaticProtocol().getWanStaticRoutes())) {
			ipm.getStaticProtocol().getWanStaticRoutes().forEach(wanStaticRoute -> {
				if (required || Objects.isNull(wanStaticRoute.getEndDate())) {
					WanStaticRouteBean wanStaticRouteBean = new WanStaticRouteBean();
					setWanStaticRouteBean(wanStaticRouteBean, wanStaticRoute);
					staticProtocolBean.getWanStaticRoutes().add(wanStaticRouteBean);
				}
			});
		}

		if(!CollectionUtils.isEmpty(ipm.getStaticProtocol().getPolicyTypes())){
			ipm.getStaticProtocol().getPolicyTypes().forEach(policyType -> {
				if(required || Objects.isNull(policyType.getEndDate())){
					PolicyTypeBean policyTypeBean = new PolicyTypeBean();
					setPolicyTypeBean(policyTypeBean, policyType);
					staticProtocolBean.getPolicyTypes().add(policyTypeBean);
				}
			});
		}
		staticProtocolBean.setInterfaceDetailBean(constructInterfaceDetail(ipm));
		return staticProtocolBean;
	}

	private BgpBean constructBgpDetails(InterfaceProtocolMapping ipm, boolean required) {
		BgpBean bgpBean = new BgpBean();
		setBgpBean(bgpBean, ipm.getBgp());

		if(!CollectionUtils.isEmpty(ipm.getBgp().getPolicyTypes())){
			ipm.getBgp().getPolicyTypes().forEach(policyType -> {
				if(required || Objects.isNull(policyType.getEndDate())){
					PolicyTypeBean policyTypeBean = new PolicyTypeBean();
					setPolicyTypeBean(policyTypeBean, policyType);
					bgpBean.getPolicyTypes().add(policyTypeBean);
				}
			});
		}
		bgpBean.setInterfaceDetailBean(constructInterfaceDetail(ipm));
		return bgpBean;
	}

	private ChannelizedE1serialInterfaceBean constructChannelizedE1InterfaceDetails(InterfaceProtocolMapping ipm, boolean required) {

		ChannelizedE1serialInterfaceBean channelizedE1serialInterfaceBean = new ChannelizedE1serialInterfaceBean();
		if(required || Objects.isNull(ipm.getChannelizedE1serialInterface().getEndDate())) {
			setChannelizedE1serialInterfaceBean(channelizedE1serialInterfaceBean, ipm.getChannelizedE1serialInterface());
		}
		if(!CollectionUtils.isEmpty(ipm.getChannelizedE1serialInterface().getAclPolicyCriterias())){
			ipm.getChannelizedE1serialInterface().getAclPolicyCriterias().forEach(aclPolicyCriteria -> {
				if(required || Objects.isNull(aclPolicyCriteria.getEndDate())) {
					AclPolicyCriteriaBean aclPolicyCriteriaBean = new AclPolicyCriteriaBean();
					setAclPolicyCriteriaBean(aclPolicyCriteriaBean, aclPolicyCriteria);
					channelizedE1serialInterfaceBean.getAclPolicyCriterias().add(aclPolicyCriteriaBean);
				}
			});
		}
		channelizedE1serialInterfaceBean.setInterfaceDetailBean(constructInterfaceDetail(ipm));
		return channelizedE1serialInterfaceBean;
	}

	private ChannelizedSdhInterfaceBean constructChannelizedSdhInterfaceDetails(InterfaceProtocolMapping ipm, boolean required) {

		ChannelizedSdhInterfaceBean channelizedSdhInterfaceBean = new ChannelizedSdhInterfaceBean();
		setChannelizedSdhInterfaceBean(channelizedSdhInterfaceBean, ipm.getChannelizedSdhInterface());

		if(!CollectionUtils.isEmpty(ipm.getChannelizedSdhInterface().getAclPolicyCriterias())){
			ipm.getChannelizedSdhInterface().getAclPolicyCriterias().forEach(aclPolicyCriteria -> {
				if(required || Objects.isNull(aclPolicyCriteria.getEndDate())) {
					AclPolicyCriteriaBean aclPolicyCriteriaBean = new AclPolicyCriteriaBean();
					setAclPolicyCriteriaBean(aclPolicyCriteriaBean, aclPolicyCriteria);
					channelizedSdhInterfaceBean.getAclPolicyCriterias().add(aclPolicyCriteriaBean);
				}
			});
		}
		channelizedSdhInterfaceBean.setInterfaceDetailBean(constructInterfaceDetail(ipm));
		return channelizedSdhInterfaceBean;
	}

	private EthernetInterfaceBean constructEthernetInterfaceDetails(InterfaceProtocolMapping ipm, boolean required) {

		EthernetInterfaceBean ethernetInterfaceBean = new EthernetInterfaceBean();
		setEthernetInterfaceBean(ethernetInterfaceBean, ipm.getEthernetInterface());

		if(!CollectionUtils.isEmpty(ipm.getEthernetInterface().getAclPolicyCriterias())){
			ipm.getEthernetInterface().getAclPolicyCriterias().forEach(aclPolicyCriteria -> {
				if(required || Objects.isNull(aclPolicyCriteria.getEndDate())) {
					AclPolicyCriteriaBean aclPolicyCriteriaBean = new AclPolicyCriteriaBean();
					setAclPolicyCriteriaBean(aclPolicyCriteriaBean, aclPolicyCriteria);
					ethernetInterfaceBean.getAclPolicyCriterias().add(aclPolicyCriteriaBean);
				}
			});
		}
		ethernetInterfaceBean.setInterfaceDetailBean(constructInterfaceDetail(ipm));
		return ethernetInterfaceBean;
	}

	private void constructCiscoImport(ServiceDetail serviceDetail, ServiceDetailBean serviceDetailBean, boolean required) {

    	logger.info("Constructing CiscoImportMaps for serviceCode {}", serviceDetail.getServiceId());
    	if(!CollectionUtils.isEmpty(serviceDetail.getCiscoImportMaps())){
    		serviceDetail.getCiscoImportMaps().forEach(ciscoImportMap -> {
    			if(required || Objects.isNull(ciscoImportMap.getEndDate())){
    				CiscoImportMapBean ciscoImportMapBean = new CiscoImportMapBean();
    				setCiscoImportMapBean(ciscoImportMapBean, ciscoImportMap);
    				if(!CollectionUtils.isEmpty(ciscoImportMap.getPolicyTypeCriteriaMappings())){
    					ciscoImportMap.getPolicyTypeCriteriaMappings().forEach(policyTypeCriteriaMapping -> {
							if(required || Objects.isNull(policyTypeCriteriaMapping.getEndDate())){
								PolicyTypeCriteriaMappingBean policyTypeCriteriaMappingBean = new PolicyTypeCriteriaMappingBean();
								setPolicyTypeCriteriaMappingBean(policyTypeCriteriaMappingBean, policyTypeCriteriaMapping);
								ciscoImportMapBean.getPolicyTypeCriteriaMappingBeans().add(policyTypeCriteriaMappingBean);
							}
						});
					}
    				serviceDetailBean.getCiscoImportMapBeans().add(ciscoImportMapBean);
				}
			});
		}
	}

	public void constructServiceQo(ServiceDetail serviceDetail, ServiceDetailBean serviceDetailBean, boolean required) {
    	if(!CollectionUtils.isEmpty(serviceDetail.getServiceQos())){
    		serviceDetail.getServiceQos().forEach(serviceQo -> {
				if (required || Objects.isNull(serviceQo.getEndDate())) {
					ServiceQoBean serviceQoBean = new ServiceQoBean();
					setServiceQoBean(serviceQoBean, serviceQo);
					if (!CollectionUtils.isEmpty(serviceQo.getServiceCosCriterias())) {
						serviceQo.getServiceCosCriterias().forEach(serviceCosCriteria -> {
							if(required || Objects.isNull(serviceCosCriteria.getEndDate())) {
								ServiceCosCriteriaBean serviceCosCriteriaBean = new ServiceCosCriteriaBean();
								setServiceCosCriteriaBean(serviceCosCriteriaBean, serviceCosCriteria);
								if (!CollectionUtils.isEmpty(serviceCosCriteria.getAclPolicyCriterias())) {
									serviceCosCriteria.getAclPolicyCriterias().forEach(aclPolicyCriteria -> {
										if(required || Objects.isNull(aclPolicyCriteria.getEndDate())) {
											AclPolicyCriteriaBean aclPolicyCriteriaBean = new AclPolicyCriteriaBean();
											setAclPolicyCriteriaBean(aclPolicyCriteriaBean, aclPolicyCriteria);
											serviceCosCriteriaBean.getAclPolicyCriterias().add(aclPolicyCriteriaBean);
										}
									});
								}
								serviceQoBean.getServiceCosCriterias().add(serviceCosCriteriaBean);
							}
						});
					}
					serviceDetailBean.getServiceQoBeans().add(serviceQoBean);
				}
			});
		}
	}

	public void constructTopologyAndUniDetails(ServiceDetail serviceDetail, ServiceDetailBean serviceDetailBean, boolean required) {
		logger.info("constructTopologyAndUniDetails method invoked for Service Id::{}",serviceDetail.getId());
    	if(!CollectionUtils.isEmpty(serviceDetail.getTopologies())){
    		logger.info("Topology exists for Service Id::{}",serviceDetail.getId());
    		serviceDetail.getTopologies().forEach(topology -> {
				if(required || Objects.isNull(topology.getEndDate())) {
					logger.info("Topology EndDate not exists for Service Id::{}",serviceDetail.getId());
					TopologyBean topologyBean = new TopologyBean();
					setTopologyBean(topologyBean, topology);
					if (!CollectionUtils.isEmpty(topology.getRouterUplinkports())) {
						topology.getRouterUplinkports().forEach(routerUplinkport -> {
							if(required || Objects.isNull(routerUplinkport.getEndDate())){
								RouterUplinkportBean routerUplinkportBean = new RouterUplinkportBean();
								setRouterUplinkportBean(routerUplinkportBean, routerUplinkport);
								topologyBean.getRouterUplinkports().add(routerUplinkportBean);
							}
						});
					}
					if (!CollectionUtils.isEmpty(topology.getUniswitchDetails())) {
						topology.getUniswitchDetails().forEach(uniswitchDetail -> {
							if(required || Objects.isNull(uniswitchDetail.getEndDate())) {
								UniswitchDetailBean uniswitchDetailBean = new UniswitchDetailBean();
								setUniswitchDetailBean(uniswitchDetailBean, uniswitchDetail);
								topologyBean.getUniswitchDetails().add(uniswitchDetailBean);
							}
						});
					}
					serviceDetailBean.getTopologyBeans().add(topologyBean);
				}
			});
		}
	}

	private OrderDetailBean setOrderDetailBean(OrderDetailBean bean, ServiceDetail serviceDetail) {
		OrderDetail orderDetail = serviceDetail.getOrderDetail();
		bean.setAccountId(orderDetail.getAccountId());
		bean.setAddressLine1(serviceDetail.getAddressLine1());
		bean.setAddressLine2(serviceDetail.getAddressLine2());
		bean.setAddressLine3(serviceDetail.getAddressLine3());
		bean.setAluCustomerId(orderDetail.getAluCustomerId());
		bean.setAsdOpptyFlag(convertByteToBoolean(orderDetail.getAsdOpptyFlag()));
		bean.setCity(serviceDetail.getCity());
		bean.setCopfId(orderDetail.getCopfId());
		bean.setCountry(serviceDetail.getCountry());
		bean.setCustomerCategory(orderDetail.getCustomerCategory());
		bean.setCustomerContact(orderDetail.getCustomerContact());
		bean.setCustomerEmail(orderDetail.getCustomerEmail());
		bean.setCustomerId(orderDetail.getCustomerCrnId());
		bean.setCustomerName(orderDetail.getCustomerName());
		bean.setCustomerPhoneno(orderDetail.getCustomerPhoneno());
		bean.setCustomerType(orderDetail.getCustomerType());
		bean.setCustCuId(orderDetail.getCustCuId());
		bean.setEndCustomerName(orderDetail.getEndCustomerName());
		bean.setEndDate(orderDetail.getEndDate());
		bean.setGroupId(orderDetail.getGroupId());
		bean.setLastModifiedDate(orderDetail.getLastModifiedDate());
		bean.setLocation(serviceDetail.getCity());
		bean.setModifiedBy(orderDetail.getModifiedBy());
		bean.setOptyBidCategory(orderDetail.getOptyBidCategory());
		bean.setOrderCategory(orderDetail.getOrderCategory());
		bean.setOrderId(orderDetail.getOrderId());
		bean.setOrderUuid(orderDetail.getOrderUuid());
		bean.setOrderStatus(orderDetail.getOrderStatus());
		bean.setOrderType(orderDetail.getOrderType());
		bean.setOrderUuid(orderDetail.getOrderUuid());
		bean.setOriginatorDate(orderDetail.getOriginatorDate());
		bean.setOriginatorName(orderDetail.getOriginatorName());
		bean.setPincode(serviceDetail.getPincode());
		bean.setSamCustomerDescription(orderDetail.getSamCustomerDescription());
		bean.setSfdcOpptyId(orderDetail.getSfdcOpptyId());
		bean.setStartDate(orderDetail.getStartDate());
		bean.setState(serviceDetail.getState());
		bean.setCustomerCrnId(orderDetail.getCustomerCrnId());
		bean.setProjectName(orderDetail.getProjectName());
		return bean;
	}
	//BSO Details Related changes
	private BSODetailsBean setBSODetailsBean(BSODetailsBean bean,Stg0SapPoDtlOptimus stg0SapPoDtlOptimus, String bsoCircuitId ) {
		bean.setPoNumber(stg0SapPoDtlOptimus.getPoNumber());
		bean.setPoLineNo(stg0SapPoDtlOptimus.getPoLineNo());
		bean.setParentPoNumber(stg0SapPoDtlOptimus.getParentPoNumber());
		bean.setChildPoNumber(stg0SapPoDtlOptimus.getChildPoNumber());
		bean.setTerminationType(stg0SapPoDtlOptimus.getTerminationType());
		bean.setVendorRefIdOrderId(bsoCircuitId);
		bean.setProductComponent(stg0SapPoDtlOptimus.getProductComponent());
		bean.setVendorNo(stg0SapPoDtlOptimus.getVendorNo());
		bean.setPoCreationDate(stg0SapPoDtlOptimus.getPoCreationDate());
		return bean;
	}

	private ServiceDetailBean setServiceDetailBean(ServiceDetailBean serviceDetailBean, ServiceDetail serviceDetail) {
		serviceDetailBean.setAluSvcid(serviceDetail.getAluSvcid());
		serviceDetailBean.setAluSvcName(serviceDetail.getAluSvcName());
		serviceDetailBean.setBurstableBw(serviceDetail.getBurstableBw());
		serviceDetailBean.setBurstableBwUnit(serviceDetail.getBurstableBwUnit());
		serviceDetailBean.setCsoSammgrId(Objects.nonNull(serviceDetail.getCssSammgrId()) ? new Integer(serviceDetail.getCssSammgrId()).toString() : null);
		serviceDetailBean.setDataTransferCommit(serviceDetail.getDataTransferCommit());
		serviceDetailBean.setDataTransferCommitUnit(serviceDetail.getDataTransferCommitUnit());
		serviceDetailBean.setDescription(serviceDetail.getDescription());
		serviceDetailBean.setEligibleForRevision(convertByteToBoolean(serviceDetail.getEligibleForRevision()));
		serviceDetailBean.setEndDate(serviceDetail.getEndDate());
		serviceDetailBean.setExpediteTerminate(convertByteToBoolean(serviceDetail.getExpediteTerminate()));
		serviceDetailBean.setExternalRefid(serviceDetail.getExternalRefid());
		serviceDetailBean.setId(serviceDetail.getId());
		serviceDetailBean.setIntefaceDescSvctag(serviceDetail.getIntefaceDescSvctag());
		serviceDetailBean.setIsIdcService(convertByteToBoolean(serviceDetail.getIsIdcService()));
		serviceDetailBean.setPortChanged(convertByteToBoolean(serviceDetail.getIsportChanged()));
		serviceDetailBean.setIscustomConfigReqd(convertByteToBoolean(serviceDetail.getIscustomConfigReqd()));
		serviceDetailBean.setIsdowntimeReqd(convertByteToBoolean(serviceDetail.getIsdowntimeReqd()));
		serviceDetailBean.setIsTxDowntimeReqd(convertByteToBoolean(serviceDetail.getIstxdowntimeReqd()));
		serviceDetailBean.setDowntimeDuration(serviceDetail.getDowntimeDuration());
		serviceDetailBean.setFromTime(serviceDetail.getFromTime());
		serviceDetailBean.setToTime(serviceDetail.getToTime());
		serviceDetailBean.setIsIdcService(convertByteToBoolean(serviceDetail.getIsIdcService()));
		serviceDetailBean.setIsrevised(convertByteToBoolean(serviceDetail.getIsrevised()));
		serviceDetailBean.setLastmileProvider(serviceDetail.getLastmileProvider());
		serviceDetailBean.setLastmileType(serviceDetail.getLastmileType());
		serviceDetailBean.setLastModifiedDate(serviceDetail.getLastModifiedDate());
		serviceDetailBean.setMgmtType(serviceDetail.getMgmtType()!=null?serviceDetail.getMgmtType():"UNMANAGED");
		serviceDetailBean.setModifiedBy(serviceDetail.getModifiedBy());
		serviceDetailBean.setNetpRefid(serviceDetail.getNetpRefid());
		serviceDetailBean.setOldServiceId(serviceDetail.getOldServiceId());
		serviceDetailBean.setRedundancyRole(serviceDetail.getRedundancyRole());
		serviceDetailBean.setScopeOfMgmt(serviceDetail.getScopeOfMgmt());
		serviceDetailBean.setServiceBandwidth(serviceDetail.getServiceBandwidth()!=null?serviceDetail.getServiceBandwidth():2);
		serviceDetailBean.setServiceBandwidthUnit(serviceDetail.getServiceBandwidthUnit()!=null?serviceDetail.getServiceBandwidthUnit():"Mbps");
		serviceDetailBean.setServiceComponenttype(serviceDetail.getServiceComponenttype());
		serviceDetailBean.setServiceId(serviceDetail.getServiceId());
		serviceDetailBean.setServiceState(serviceDetail.getServiceState());
		serviceDetailBean.setServiceSubtype(serviceDetail.getServiceSubtype());
		serviceDetailBean.setServiceType(serviceDetail.getServiceType());
		serviceDetailBean.setSkipDummyConfig(convertByteToBoolean(serviceDetail.getSkipDummyConfig()));
		serviceDetailBean.setSvclinkRole(serviceDetail.getSvclinkRole());
		serviceDetailBean.setSolutionId(serviceDetail.getSolutionId());
		serviceDetailBean.setSolutionName(serviceDetail.getSolutionName());
		serviceDetailBean.setStartDate(serviceDetail.getStartDate());
		serviceDetailBean.setSvclinkSrvid(serviceDetail.getSvclinkSrvid());
		serviceDetailBean.setTrfsDate(serviceDetail.getTrfsDate());
		serviceDetailBean.setTrfsTriggerDate(serviceDetail.getTrfsTriggerDate());
		serviceDetailBean.setTriggerNccmOrder(convertByteToBoolean(serviceDetail.getTriggerNccmOrder()));
		serviceDetailBean.setUsageModel(serviceDetail.getUsageModel());
		serviceDetailBean.setVersion(serviceDetail.getVersion());
		serviceDetailBean.setRouterMake(serviceDetail.getRouterMake());
		serviceDetailBean.setAddressLine1(serviceDetail.getAddressLine1());
		serviceDetailBean.setAddressLine2(serviceDetail.getAddressLine2());
		serviceDetailBean.setAddressLine3(serviceDetail.getAddressLine3());
		serviceDetailBean.setCity(serviceDetail.getCity());
		serviceDetailBean.setCountry(serviceDetail.getCountry());
		serviceDetailBean.setPincode(serviceDetail.getPincode());
		serviceDetailBean.setState(serviceDetail.getState());
		serviceDetailBean.setServiceOrderType(serviceDetail.getServiceOrderType());
		serviceDetailBean.setOrderSubCategory(serviceDetail.getOrderSubCategory());
		
		serviceDetailBean.setOrderType(serviceDetail.getOrderType()!=null?serviceDetail.getOrderType(): serviceDetail.getOrderDetail().getOrderType());
		serviceDetailBean.setOrderCategory(serviceDetail.getOrderCategory()!=null?serviceDetail.getOrderCategory(): serviceDetail.getOrderDetail().getOrderCategory());
		return serviceDetailBean;
	}

	private TopologyBean setTopologyBean(TopologyBean topologyBean, Topology topology) {
		topologyBean.setEndDate(topology.getEndDate());
		topologyBean.setLastModifiedDate(topology.getLastModifiedDate());
		topologyBean.setModifiedBy(topology.getModifiedBy());
		topologyBean.setStartDate(topology.getStartDate());
		topologyBean.setTopologyId(topology.getTopologyId());
		topologyBean.setServiceDetailsId(topology.getServiceDetail().getId());			// Check
		topologyBean.setTopologyName(topology.getTopologyName());
		return topologyBean;
	}

	private RouterUplinkportBean setRouterUplinkportBean(RouterUplinkportBean routerUplinkportBean,
			RouterUplinkport routerUplinkport) {
		routerUplinkportBean.setTopologyServiceDetailsId(routerUplinkport.getTopology().getServiceDetail().getId());
		routerUplinkportBean.setStartDate(routerUplinkport.getStartDate());
		routerUplinkportBean.setPhysicalPort2Name(routerUplinkport.getPhysicalPort2Name());
		routerUplinkportBean.setRouterUplinkportId(routerUplinkport.getRouterUplinkportId());
		routerUplinkportBean.setPhysicalPort1Name(routerUplinkport.getPhysicalPort1Name());
		routerUplinkportBean.setModifiedBy(routerUplinkport.getModifiedBy());
		routerUplinkportBean.setEndDate(routerUplinkport.getEndDate());
		return routerUplinkportBean;
	}

	private UniswitchDetailBean setUniswitchDetailBean(UniswitchDetailBean uniswitchDetailBean,
			UniswitchDetail uniswitchDetail) {
		uniswitchDetailBean.setAutonegotiationEnabled(uniswitchDetail.getAutonegotiationEnabled());
		uniswitchDetailBean.setDuplex(uniswitchDetail.getDuplex());
		uniswitchDetailBean.setEndDate(uniswitchDetail.getEndDate());
		uniswitchDetailBean.setHandoff(uniswitchDetail.getHandoff());
		uniswitchDetailBean.setInnerVlan(uniswitchDetail.getInnerVlan());
		uniswitchDetailBean.setHostName(uniswitchDetail.getHostName());
		uniswitchDetailBean.setInterfaceName(uniswitchDetail.getInterfaceName());
		uniswitchDetailBean.setLastModifiedDate(uniswitchDetail.getLastModifiedDate());
		uniswitchDetailBean.setMaxMacLimit(uniswitchDetail.getMaxMacLimit());
		uniswitchDetailBean.setMediaType(uniswitchDetail.getMediaType());
		uniswitchDetailBean.setMgmtIp(uniswitchDetail.getMgmtIp());
		uniswitchDetailBean.setMode(uniswitchDetail.getMode());
		uniswitchDetailBean.setModifiedBy(uniswitchDetail.getModifiedBy());
		uniswitchDetailBean.setOuterVlan(uniswitchDetail.getOuterVlan());
		uniswitchDetailBean.setPhysicalPort(uniswitchDetail.getPhysicalPort());
		uniswitchDetailBean.setPortType(uniswitchDetail.getPortType());
		uniswitchDetailBean.setSpeed(uniswitchDetail.getSpeed());
		uniswitchDetailBean.setStartDate(uniswitchDetail.getStartDate());
		uniswitchDetailBean.setSwitchModel(uniswitchDetail.getSwitchModel());
		uniswitchDetailBean.setSyncVlanReqd(convertByteToBoolean(uniswitchDetail.getSyncVlanReqd()));
		uniswitchDetailBean.setTopologyServiceDetailsId(uniswitchDetail.getTopology().getServiceDetail().getId());
		uniswitchDetailBean.setUniswitchId(uniswitchDetail.getUniswitchId());
		return uniswitchDetailBean;
	}

	private ServiceQoBean setServiceQoBean(ServiceQoBean serviceQoBean, ServiceQo serviceQo) {
		serviceQoBean.setCosPackage(serviceQo.getCosPackage());
		serviceQoBean.setCosProfile(serviceQo.getCosProfile());
		serviceQoBean.setCosType(serviceQo.getCosType());
		serviceQoBean.setCosUpdateAction(serviceQo.getCosUpdateAction());
		serviceQoBean.setEndDate(serviceQo.getEndDate());
		serviceQoBean.setFlexiCosIdentifier(serviceQo.getFlexiCosIdentifier());
		serviceQoBean.setIsbandwidthApplicable(convertByteToBoolean(serviceQo.getIsbandwidthApplicable()));
		serviceQoBean.setServiceQosId(serviceQo.getServiceQosId());
		serviceQoBean.setIsdefaultFc(convertByteToBoolean(serviceQo.getIsdefaultFc()));
		serviceQoBean.setIsflexicos(convertByteToBoolean(serviceQo.getIsflexicos()));
		serviceQoBean.setLastModifiedDate(serviceQo.getLastModifiedDate());
		serviceQoBean.setModifiedBy(serviceQo.getModifiedBy());
		serviceQoBean.setNcTraffic(convertByteToBoolean(serviceQo.getNcTraffic()));
		serviceQoBean.setPirBw(serviceQo.getPirBw());
		serviceQoBean.setPirBwUnit(serviceQo.getPirBwUnit());
		serviceQoBean.setQosTrafiicMode(serviceQo.getQosTrafiicMode());
		serviceQoBean.setSummationOfBw(serviceQo.getSummationOfBw());
		return serviceQoBean;
	}

	private ServiceCosCriteriaBean setServiceCosCriteriaBean(ServiceCosCriteriaBean serviceCosCriteriaBean,
			ServiceCosCriteria serviceCosCriteria) {
		serviceCosCriteriaBean.setBwBpsunit(serviceCosCriteria.getBwBpsunit());
		serviceCosCriteriaBean.setClassificationCriteria(serviceCosCriteria.getClassificationCriteria());
		serviceCosCriteriaBean.setCosName(serviceCosCriteria.getCosName());
		serviceCosCriteriaBean.setCosPercent(serviceCosCriteria.getCosPercent());
		serviceCosCriteriaBean.setCosName(serviceCosCriteria.getCosName());
		serviceCosCriteriaBean.setDhcpVal1(serviceCosCriteria.getDhcpVal1());
		serviceCosCriteriaBean.setDhcpVal2(serviceCosCriteria.getDhcpVal2());
		serviceCosCriteriaBean.setDhcpVal3(serviceCosCriteria.getDhcpVal3());
		serviceCosCriteriaBean.setDhcpVal4(serviceCosCriteria.getDhcpVal4());
		serviceCosCriteriaBean.setDhcpVal5(serviceCosCriteria.getDhcpVal5());
		serviceCosCriteriaBean.setDhcpVal6(serviceCosCriteria.getDhcpVal6());
		serviceCosCriteriaBean.setDhcpVal7(serviceCosCriteria.getDhcpVal7());
		serviceCosCriteriaBean.setDhcpVal8(serviceCosCriteria.getDhcpVal8());
		serviceCosCriteriaBean.setEndDate(serviceCosCriteria.getEndDate());
		serviceCosCriteriaBean.setIpprecedenceVal1(serviceCosCriteria.getIpprecedenceVal1());
		serviceCosCriteriaBean.setIpprecedenceVal2(serviceCosCriteria.getIpprecedenceVal2());
		serviceCosCriteriaBean.setIpprecedenceVal3(serviceCosCriteria.getIpprecedenceVal3());
		serviceCosCriteriaBean.setIpprecedenceVal4(serviceCosCriteria.getIpprecedenceVal4());
		serviceCosCriteriaBean.setIpprecedenceVal5(serviceCosCriteria.getIpprecedenceVal5());
		serviceCosCriteriaBean.setIpprecedenceVal6(serviceCosCriteria.getIpprecedenceVal6());
		serviceCosCriteriaBean.setIpprecedenceVal7(serviceCosCriteria.getIpprecedenceVal7());
		serviceCosCriteriaBean.setIpprecedenceVal8(serviceCosCriteria.getIpprecedenceVal8());
		serviceCosCriteriaBean.setLastModifiedDate(serviceCosCriteria.getLastModifiedDate());
		serviceCosCriteriaBean.setModifiedBy(serviceCosCriteria.getModifiedBy());
		serviceCosCriteriaBean.setServiceCosId(serviceCosCriteria.getServiceCosId());
		serviceCosCriteriaBean.setStartDate(serviceCosCriteria.getStartDate());
		return serviceCosCriteriaBean;
	}

	private AclPolicyCriteriaBean setAclPolicyCriteriaBean(AclPolicyCriteriaBean aclPolicyCriteriaBean,
			AclPolicyCriteria aclPolicyCriteria) {
		aclPolicyCriteriaBean.setAclPolicyId(aclPolicyCriteria.getAclPolicyId());
		aclPolicyCriteriaBean.setAction(aclPolicyCriteria.getAction());
		aclPolicyCriteriaBean.setDescription(aclPolicyCriteria.getDescription());
		aclPolicyCriteriaBean.setDestinationAny(convertByteToBoolean(aclPolicyCriteria.getDestinationAny()));
		aclPolicyCriteriaBean.setDestinationCondition(aclPolicyCriteria.getDestinationCondition());
		aclPolicyCriteriaBean.setDestinationPortnumber(aclPolicyCriteria.getDestinationPortnumber());
		aclPolicyCriteriaBean.setDestinationSubnet(aclPolicyCriteria.getDestinationSubnet());
		aclPolicyCriteriaBean.setEndDate(aclPolicyCriteria.getEndDate());
		aclPolicyCriteriaBean.setInboundIpv4AclName(aclPolicyCriteria.getInboundIpv4AclName());
		aclPolicyCriteriaBean.setInboundIpv6AclName(aclPolicyCriteria.getInboundIpv6AclName());
		aclPolicyCriteriaBean.setIsinboundaclIpv4Applied(convertByteToBoolean(aclPolicyCriteria.getIsinboundaclIpv4Applied()));
		aclPolicyCriteriaBean.setIsinboundaclIpv4Preprovisioned(convertByteToBoolean(aclPolicyCriteria.getIsinboundaclIpv4Preprovisioned()));
		aclPolicyCriteriaBean.setIsinboundaclIpv6Applied(convertByteToBoolean(aclPolicyCriteria.getIsinboundaclIpv6Applied()));
		aclPolicyCriteriaBean.setIsinboundaclIpv6Preprovisioned(convertByteToBoolean(aclPolicyCriteria.getIsinboundaclIpv6Preprovisioned()));
		aclPolicyCriteriaBean.setIsoutboundaclIpv4Applied(convertByteToBoolean(aclPolicyCriteria.getIsoutboundaclIpv4Applied()));
		aclPolicyCriteriaBean
				.setIsoutboundaclIpv4Preprovisioned(convertByteToBoolean(aclPolicyCriteria.getIsoutboundaclIpv4Preprovisioned()));
		aclPolicyCriteriaBean.setIsoutboundaclIpv6Applied(convertByteToBoolean(aclPolicyCriteria.getIsoutboundaclIpv6Applied()));
		aclPolicyCriteriaBean
				.setIsoutboundaclIpv6Preprovisioned(convertByteToBoolean(aclPolicyCriteria.getIsoutboundaclIpv6Preprovisioned()));
		aclPolicyCriteriaBean.setIssvcQosCoscriteriaIpaddrAcl(convertByteToBoolean(aclPolicyCriteria.getIssvcQosCoscriteriaIpaddrAcl()));
		aclPolicyCriteriaBean
				.setIssvcQosCoscriteriaIpaddrAclName(aclPolicyCriteria.getIssvcQosCoscriteriaIpaddrAclName());
		aclPolicyCriteriaBean.setIssvcQosCoscriteriaIpaddrPreprovisioned(convertByteToBoolean(
				aclPolicyCriteria.getIssvcQosCoscriteriaIpaddrPreprovisioned()));
		aclPolicyCriteriaBean.setLastModifiedDate(aclPolicyCriteria.getLastModifiedDate());
		aclPolicyCriteriaBean.setModifiedBy(aclPolicyCriteria.getModifiedBy());
		aclPolicyCriteriaBean.setOutboundIpv4AclName(aclPolicyCriteria.getOutboundIpv4AclName());
		aclPolicyCriteriaBean.setOutboundIpv6AclName(aclPolicyCriteria.getOutboundIpv6AclName());
		aclPolicyCriteriaBean.setProtocol(aclPolicyCriteria.getProtocol());
		aclPolicyCriteriaBean.setSequence(aclPolicyCriteria.getSequence());
		aclPolicyCriteriaBean.setSourceAny(convertByteToBoolean(aclPolicyCriteria.getSourceAny()));
		aclPolicyCriteriaBean.setSourceCondition(aclPolicyCriteria.getSourceCondition());
		aclPolicyCriteriaBean.setSourcePortnumber(aclPolicyCriteria.getSourcePortnumber());
		aclPolicyCriteriaBean.setSourceSubnet(aclPolicyCriteria.getSourceSubnet());
		aclPolicyCriteriaBean.setStartDate(aclPolicyCriteria.getStartDate());
		return aclPolicyCriteriaBean;
	}

	private CiscoImportMapBean setCiscoImportMapBean(CiscoImportMapBean ciscoImportMapBean,
			CiscoImportMap ciscoImportMap) {
		ciscoImportMapBean.setCiscoImportId(ciscoImportMap.getCiscoImportId());
		ciscoImportMapBean.setDescription(ciscoImportMap.getDescription());
		ciscoImportMapBean.setEndDate(ciscoImportMap.getEndDate());
		ciscoImportMapBean.setLastModifiedDate(ciscoImportMap.getLastModifiedDate());
		ciscoImportMapBean.setModifiedBy(ciscoImportMap.getModifiedBy());
		return ciscoImportMapBean;
	}

	private EthernetInterfaceBean setEthernetInterfaceBean(EthernetInterfaceBean ethernetInterfaceBean,
			EthernetInterface ethernetInterface) {
		ethernetInterfaceBean.setType(ethernetInterface.getMediaType());
		ethernetInterfaceBean.setAutonegotiationEnabled(ethernetInterface.getAutonegotiationEnabled());
		ethernetInterfaceBean.setBfdMultiplier(ethernetInterface.getBfdMultiplier());
		ethernetInterfaceBean.setBfdreceiveInterval(ethernetInterface.getBfdreceiveInterval());
		ethernetInterfaceBean.setBfdtransmitInterval(ethernetInterface.getBfdtransmitInterval());
		ethernetInterfaceBean.setDuplex(ethernetInterface.getDuplex());
		ethernetInterfaceBean.setEncapsulation(ethernetInterface.getEncapsulation());
		ethernetInterfaceBean.setEndDate(ethernetInterface.getEndDate());
		ethernetInterfaceBean.setEthernetInterfaceId(ethernetInterface.getEthernetInterfaceId());
		ethernetInterfaceBean.setFraming(ethernetInterface.getFraming());
		ethernetInterfaceBean.setHoldtimeDown(ethernetInterface.getHoldtimeDown());
		ethernetInterfaceBean.setHoldtimeUp(ethernetInterface.getHoldtimeUp());
		ethernetInterfaceBean.setIsbfdEnabled(convertByteToBoolean(ethernetInterface.getIsbfdEnabled()));
		ethernetInterfaceBean.setIshsrpEnabled(convertByteToBoolean(ethernetInterface.getIshsrpEnabled()));
		ethernetInterfaceBean.setIsvrrpEnabled(convertByteToBoolean(ethernetInterface.getIsvrrpEnabled()));
		ethernetInterfaceBean.setType("PEROUTERSIDE");
		ethernetInterfaceBean.setModifiedIpv4Address(ethernetInterface.getModifiedIpv4Address());
		ethernetInterfaceBean.setModifiedIpv6Address(ethernetInterface.getModifiedIpv6Address());
		ethernetInterfaceBean.setModifiedSecondaryIpv4Address(ethernetInterface.getModifiedSecondaryIpv4Address());
		ethernetInterfaceBean.setModifiedSecondaryIpv6Address(ethernetInterface.getModifiedSecondaryIpv6Address());
		ethernetInterfaceBean.setInnerVlan(ethernetInterface.getInnerVlan());
		ethernetInterfaceBean.setIpv4Address(ethernetInterface.getIpv4Address());
		ethernetInterfaceBean.setIpv6Address(ethernetInterface.getIpv6Address());
		ethernetInterfaceBean.setLastModifiedDate(ethernetInterface.getLastModifiedDate());
		ethernetInterfaceBean.setMediaType(ethernetInterface.getMediaType());
		ethernetInterfaceBean.setMode(ethernetInterface.getMode());
		ethernetInterfaceBean.setModifiedBy(ethernetInterface.getModifiedBy());
		ethernetInterfaceBean.setInterfaceName(ethernetInterface.getInterfaceName());
		ethernetInterfaceBean.setMtu(ethernetInterface.getMtu());
		ethernetInterfaceBean.setOuterVlan(ethernetInterface.getOuterVlan());
		ethernetInterfaceBean.setPhysicalPort(ethernetInterface.getPhysicalPort());
		ethernetInterfaceBean.setPortType(ethernetInterface.getPortType());
		ethernetInterfaceBean.setQosLoopinPassthrough(ethernetInterface.getQosLoopinPassthrough());
		ethernetInterfaceBean.setSecondaryIpv4Address(ethernetInterface.getSecondaryIpv4Address());
		ethernetInterfaceBean.setSecondaryIpv6Address(ethernetInterface.getSecondaryIpv6Address());
		ethernetInterfaceBean.setSpeed(ethernetInterface.getSpeed());
		ethernetInterfaceBean.setStartDate(ethernetInterface.getStartDate());
		return ethernetInterfaceBean;
	}

	private ChannelizedSdhInterfaceBean setChannelizedSdhInterfaceBean(
			ChannelizedSdhInterfaceBean channelizedSdhInterfaceBean, ChannelizedSdhInterface channelizedSdhInterface) {
		channelizedSdhInterfaceBean.setPhysicalPort(channelizedSdhInterface.getPhysicalPort());
		channelizedSdhInterfaceBean.set_64kFirstTimeSlot(channelizedSdhInterface.get_4Kfirsttime_slot());
		channelizedSdhInterfaceBean.set_64kLastTimeSlot(channelizedSdhInterface.get_4klasttimeSlot());
		channelizedSdhInterfaceBean.setBfdreceiveInterval(channelizedSdhInterface.getBfdreceiveInterval());
		channelizedSdhInterfaceBean.setType(channelizedSdhInterface.getPortType());
		channelizedSdhInterfaceBean.setType("PEROUTERSIDE");
		channelizedSdhInterfaceBean.set_64kFirstTimeSlot(channelizedSdhInterface.get_4Kfirsttime_slot());
		channelizedSdhInterfaceBean.set_64kLastTimeSlot(channelizedSdhInterface.get_4klasttimeSlot());
		channelizedSdhInterfaceBean.setBfdMultiplier(channelizedSdhInterface.getBfdMultiplier());
		channelizedSdhInterfaceBean.setBfdtransmitInterval(channelizedSdhInterface.getBfdtransmitInterval());
		channelizedSdhInterfaceBean.setChannelGroupNumber(channelizedSdhInterface.getChannelGroupNumber());
		channelizedSdhInterfaceBean.setDlciValue(channelizedSdhInterface.getDlciValue());
		channelizedSdhInterfaceBean.setDowncount(channelizedSdhInterface.getDowncount());
		channelizedSdhInterfaceBean.setEncapsulation(channelizedSdhInterface.getEncapsulation());
		channelizedSdhInterfaceBean.setEndDate(channelizedSdhInterface.getEndDate());
		channelizedSdhInterfaceBean.setFraming(channelizedSdhInterface.getFraming());
		channelizedSdhInterfaceBean.setHoldtimeDown(channelizedSdhInterface.getHoldtimeDown());
		channelizedSdhInterfaceBean.setHoldtimeUp(channelizedSdhInterface.getHoldtimeUp());
		channelizedSdhInterfaceBean.setInterfaceName(channelizedSdhInterface.getInterfaceName());
		channelizedSdhInterfaceBean.setIpv4Address(channelizedSdhInterface.getIpv4Address());
		channelizedSdhInterfaceBean.setIpv6Address(channelizedSdhInterface.getIpv6Address());
		channelizedSdhInterfaceBean.setIsbfdEnabled(convertByteToBoolean(channelizedSdhInterface.getIsbfdEnabled()));
		channelizedSdhInterfaceBean.setModifiedIipv6Address(channelizedSdhInterface.getModifiedIipv6Address());
		channelizedSdhInterfaceBean.setModifiedIpv4Address(channelizedSdhInterface.getModifiedIpv4Address());
		channelizedSdhInterfaceBean.setModifiedSecondaryIpv4Address(channelizedSdhInterface.getModifiedSecondaryIpv4Address());
		channelizedSdhInterfaceBean.setModifiedSecondaryIpv6Address(channelizedSdhInterface.getModifiedSecondaryIpv6Address());
		channelizedSdhInterfaceBean.setIsframed(convertByteToBoolean(channelizedSdhInterface.getIsframed()));
		channelizedSdhInterfaceBean.setIshdlcConfig(convertByteToBoolean(channelizedSdhInterface.getIshdlcConfig()));
		channelizedSdhInterfaceBean.setIshsrpEnabled(convertByteToBoolean(channelizedSdhInterface.getIshsrpEnabled()));
		channelizedSdhInterfaceBean.setIsvrrpEnabled(convertByteToBoolean(channelizedSdhInterface.getIsvrrpEnabled()));
		channelizedSdhInterfaceBean.setJ(channelizedSdhInterface.getJ());
		channelizedSdhInterfaceBean.setKeepalive(channelizedSdhInterface.getKeepalive());
		channelizedSdhInterfaceBean.setKlm(channelizedSdhInterface.getKlm());
		channelizedSdhInterfaceBean.setLastModifiedDate(channelizedSdhInterface.getLastModifiedDate());
		channelizedSdhInterfaceBean.setMode(channelizedSdhInterface.getMode());
		channelizedSdhInterfaceBean.setModifiedBy(channelizedSdhInterface.getModifiedBy());
		channelizedSdhInterfaceBean.setModifiedSecondaryIpv4Address(channelizedSdhInterface.getModifiedSecondaryIpv4Address());
		channelizedSdhInterfaceBean.setModifiedSecondaryIpv6Address(channelizedSdhInterface.getModifiedSecondaryIpv6Address());
		channelizedSdhInterfaceBean.setModifiedIpv4Address(channelizedSdhInterface.getModifiedIpv4Address());
		channelizedSdhInterfaceBean.setModifiedIipv6Address(channelizedSdhInterface.getModifiedIipv6Address());
		channelizedSdhInterfaceBean.setMtu(channelizedSdhInterface.getMtu());
		channelizedSdhInterfaceBean.setPortType(channelizedSdhInterface.getPortType());
		channelizedSdhInterfaceBean.setPosframing(channelizedSdhInterface.getPosframing());
		channelizedSdhInterfaceBean.setSdhInterfaceId(channelizedSdhInterface.getSdhInterfaceId());
		channelizedSdhInterfaceBean.setSecondaryIpv4Address(channelizedSdhInterface.getSecondaryIpv4Address());
		channelizedSdhInterfaceBean.setSecondaryIpv6Address(channelizedSdhInterface.getSecondaryIpv6Address());
		channelizedSdhInterfaceBean.setStartDate(channelizedSdhInterface.getStartDate());
		channelizedSdhInterfaceBean.setUpcount(channelizedSdhInterface.getUpcount());
		return channelizedSdhInterfaceBean;
	}

	private ChannelizedE1serialInterfaceBean setChannelizedE1serialInterfaceBean(
			ChannelizedE1serialInterfaceBean channelizedE1serialInterfaceBean,
			ChannelizedE1serialInterface channelizedE1serialInterface) {
		channelizedE1serialInterfaceBean.setBfdMultiplier(channelizedE1serialInterface.getBfdMultiplier());
		channelizedE1serialInterfaceBean.setBfdreceiveInterval(channelizedE1serialInterface.getBfdreceiveInterval());
		channelizedE1serialInterfaceBean.setBfdtransmitInterval(channelizedE1serialInterface.getBfdtransmitInterval());
		channelizedE1serialInterfaceBean.setChannelGroupNumber(channelizedE1serialInterface.getChannelGroupNumber());
		channelizedE1serialInterfaceBean.setCrcsize(channelizedE1serialInterface.getCrcsize());
		channelizedE1serialInterfaceBean.setDlciValue(channelizedE1serialInterface.getDlciValue());
		channelizedE1serialInterfaceBean.setDowncount(channelizedE1serialInterface.getDowncount());
		channelizedE1serialInterfaceBean.setE1serialInterfaceId(channelizedE1serialInterface.getE1serialInterfaceId());
		channelizedE1serialInterfaceBean.setEncapsulation(channelizedE1serialInterface.getEncapsulation());
		channelizedE1serialInterfaceBean.setEndDate(channelizedE1serialInterface.getEndDate());
		channelizedE1serialInterfaceBean.setFirsttimeSlot(channelizedE1serialInterface.getFirsttimeSlot());
		channelizedE1serialInterfaceBean.setFraming(channelizedE1serialInterface.getFraming());
		channelizedE1serialInterfaceBean.setHoldtimeDown(channelizedE1serialInterface.getHoldtimeDown());
		channelizedE1serialInterfaceBean.setHoldtimeUp(channelizedE1serialInterface.getHoldtimeUp());
		channelizedE1serialInterfaceBean.setInterfaceName(channelizedE1serialInterface.getInterfaceName());
		channelizedE1serialInterfaceBean.setIpv4Address(channelizedE1serialInterface.getIpv4Address());
		channelizedE1serialInterfaceBean.setIpv6Address(channelizedE1serialInterface.getIpv6Address());
		channelizedE1serialInterfaceBean.setIsbfdEnabled(convertByteToBoolean(channelizedE1serialInterface.getIsbfdEnabled()));
		channelizedE1serialInterfaceBean.setIscrcforenabled(convertByteToBoolean(channelizedE1serialInterface.getIscrcforenabled()));
		channelizedE1serialInterfaceBean.setIsframed(convertByteToBoolean(channelizedE1serialInterface.getIsframed()));
		channelizedE1serialInterfaceBean.setIshdlcConfig(convertByteToBoolean(channelizedE1serialInterface.getIshdlcConfig()));
		channelizedE1serialInterfaceBean.setIshsrpEnabled(convertByteToBoolean(channelizedE1serialInterface.getIshsrpEnabled()));
		channelizedE1serialInterfaceBean.setIsvrrpEnabled(convertByteToBoolean(channelizedE1serialInterface.getIsvrrpEnabled()));
		channelizedE1serialInterfaceBean.setKeepalive(channelizedE1serialInterface.getKeepalive());
		channelizedE1serialInterfaceBean.setLastModifiedDate(channelizedE1serialInterface.getLastModifiedDate());
		channelizedE1serialInterfaceBean.setLasttimeSlot(channelizedE1serialInterface.getLasttimeSlot());
		channelizedE1serialInterfaceBean.setMode(channelizedE1serialInterface.getMode());
		channelizedE1serialInterfaceBean.setModifiedBy(channelizedE1serialInterface.getModifiedBy());
		channelizedE1serialInterfaceBean.setMtu(channelizedE1serialInterface.getMtu());
		channelizedE1serialInterfaceBean.setPhysicalPort(channelizedE1serialInterface.getPhysicalPort());
		channelizedE1serialInterfaceBean.setPortType(channelizedE1serialInterface.getPortType());
		channelizedE1serialInterfaceBean
				.setSecondaryIpv4Address(channelizedE1serialInterface.getSecondaryIpv4Address());
		channelizedE1serialInterfaceBean
				.setSecondaryIpv6Address(channelizedE1serialInterface.getSecondaryIpv6Address());
		channelizedE1serialInterfaceBean.setStartDate(channelizedE1serialInterface.getStartDate());
		channelizedE1serialInterfaceBean.setUpcount(channelizedE1serialInterface.getUpcount());
		channelizedE1serialInterfaceBean.setType("PEROUTERSIDE");
		channelizedE1serialInterfaceBean.setModifiedIpv4Address(channelizedE1serialInterface.getModifiedIpv4Address());
		channelizedE1serialInterfaceBean.setModifiedIpv6Address(channelizedE1serialInterface.getModifiedIpv6Address());
		channelizedE1serialInterfaceBean
				.setModifiedSecondaryIpv4Address(channelizedE1serialInterface.getModifiedSecondaryIpv4Address());
		channelizedE1serialInterfaceBean
				.setModifiedSecondaryIpv6Address(channelizedE1serialInterface.getModifiedSecondaryIpv6Address());
		return channelizedE1serialInterfaceBean;
	}

	private BgpBean setBgpBean(BgpBean bgpBean, Bgp bgp) {
		bgpBean.setAluBackupPath(bgp.getAluBackupPath());
		bgpBean.setAluBgpPeerName(bgp.getBgpPeerName());
		bgpBean.setAluDisableBgpPeerGrpExtCommunity(convertByteToBoolean(bgp.getAluDisableBgpPeerGrpExtCommunity()));
		bgpBean.setAluKeepalive(bgp.getAluKeepalive());
		bgpBean.setAsoOverride(convertByteToBoolean(bgp.getAsoOverride()));
		bgpBean.setAsPath(bgp.getAsPath());
		bgpBean.setAuthenticationMode(bgp.getAuthenticationMode());
		bgpBean.setBgpId(bgp.getBgpId());
		bgpBean.setBgpMultihopReqd(convertByteToBoolean(bgp.getIsbgpMultihopReqd()));
		bgpBean.setBgpneighbourinboundv4routermapname(bgp.getBgpneighbourinboundv4routermapname());
		bgpBean.setBgpneighbourinboundv6routermapname(bgp.getBgpneighbourinboundv6routermapname());
		bgpBean.setEndDate(bgp.getEndDate());
		bgpBean.setHelloTime(bgp.getHelloTime());
		bgpBean.setHoldTime(bgp.getHoldTime());
		bgpBean.setIsauthenticationRequired(convertByteToBoolean(bgp.getIsauthenticationRequired()));
		bgpBean.setSplitHorizon(convertByteToBoolean(bgp.getSplitHorizon()));
		bgpBean.setIsbgpNeighbourInboundv4RoutemapEnabled(convertByteToBoolean(bgp.getIsbgpNeighbourInboundv4RoutemapEnabled()));
		bgpBean.setIsbgpNeighbourinboundv4RoutemapPreprovisioned(
				convertByteToBoolean(bgp.getIsbgpNeighbourinboundv4RoutemapPreprovisioned()));
		bgpBean.setIsbgpNeighbourInboundv6RoutemapEnabled(convertByteToBoolean(bgp.getIsbgpNeighbourInboundv6RoutemapEnabled()));
		bgpBean.setIsbgpNeighbourinboundv6RoutemapPreprovisioned(
				convertByteToBoolean(bgp.getIsbgpNeighbourinboundv6RoutemapPreprovisioned()));
		bgpBean.setIsebgpMultihopReqd(convertByteToBoolean(bgp.getIsebgpMultihopReqd()));
		bgpBean.setIsmultihopTtl(bgp.getIsmultihopTtl());
		bgpBean.setIsrtbh(convertByteToBoolean(bgp.getIsrtbh()));
		bgpBean.setLastModifiedDate(bgp.getLastModifiedDate());
		bgpBean.setLocalAsNumber(bgp.getLocalAsNumber());
		bgpBean.setLocalPreference(bgp.getLocalPreference());
		bgpBean.setLocalUpdateSourceInterface(bgp.getLocalUpdateSourceInterface());
		bgpBean.setLocalUpdateSourceIpv4Address(bgp.getLocalUpdateSourceIpv4Address());
		bgpBean.setLocalUpdateSourceIpv6Address(bgp.getLocalUpdateSourceIpv6Address());
		bgpBean.setMaxPrefix(bgp.getMaxPrefix());
		bgpBean.setMaxPrefixThreshold(bgp.getMaxPrefixThreshold());
		bgpBean.setMedValue(bgp.getMedValue());
		bgpBean.setModifiedBy(bgp.getModifiedBy());
		bgpBean.setNeighborOn(bgp.getNeighborOn());
		bgpBean.setNeighbourCommunity(bgp.getNeighbourCommunity());
		bgpBean.setNeighbourshutdownRequired(convertByteToBoolean(bgp.getNeighbourshutdownRequired()));
		bgpBean.setPassword(bgp.getPassword());
		bgpBean.setPeerType(bgp.getPeerType());
		bgpBean.setRedistributeConnectedEnabled(convertByteToBoolean(bgp.getRedistributeConnectedEnabled()));
		bgpBean.setRedistributeStaticEnabled(convertByteToBoolean(bgp.getRedistributeStaticEnabled()));
		if(bgp.getRemoteAsNumber()!=null) {
		bgpBean.setRemoteAsNumber(bgp.getRemoteAsNumber().toString());
		}
		bgpBean.setRemoteCeAsnumber(bgp.getRemoteCeAsnumber());
		bgpBean.setRemoteIpv4Address(bgp.getRemoteIpv4Address());
		bgpBean.setRemoteIpv6Address(bgp.getRemoteIpv6Address());
		bgpBean.setRemoteUpdateSourceInterface(bgp.getRemoteUpdateSourceInterface());
		bgpBean.setRemoteUpdateSourceIpv4Address(bgp.getRemoteUpdateSourceIpv4Address());
		bgpBean.setRemoteUpdateSourceIpv6Address(bgp.getRemoteUpdateSourceIpv6Address());
		bgpBean.setRoutesExchanged(bgp.getRoutesExchanged());
		bgpBean.setRtbhOptions(bgp.getRtbhOptions());
		bgpBean.setSsoRequired(convertByteToBoolean(bgp.getSooRequired()));
		bgpBean.setSplitHorizon(convertByteToBoolean(bgp.getSplitHorizon()));							//
		bgpBean.setStartDate(bgp.getStartDate());
		bgpBean.setV6LocalPreference(bgp.getV6LocalPreference());
		bgpBean.setWanStaticRoutes(constructWanstaticRouteDetailsForBgp(bgp, bgpBean));
//		bgpBean.setPolicyTypes(constructPolicyTypes(bgp, bgpBean));
		return bgpBean;
	}

	private Set<PolicyTypeBean> constructPolicyTypes(Bgp bgp, BgpBean bgpBean) {
		Set<PolicyTypeBean> policyTypeBeans = new HashSet<>();

		if (bgp.getPolicyTypes() != null && !bgp.getPolicyTypes().isEmpty()) {

			bgp.getPolicyTypes().forEach(policyType -> {
				if(policyType.getEndDate()==null) {
				PolicyTypeBean policyTypeBean = new PolicyTypeBean();
				policyTypeBean.setAludefaultoriginatev4Key(convertByteToBoolean(policyType.getAludefaultoriginatev4Key()));
				policyTypeBean.setAludefaultoriginatev6Key(convertByteToBoolean(policyType.getAludefaultoriginatev6Key()));
				policyTypeBean.setInboundIpv4PolicyName(policyType.getInboundIpv4PolicyName());
				policyTypeBean.setInboundIpv4Preprovisioned(convertByteToBoolean(policyType.getInboundIpv4Preprovisioned()));
				policyTypeBean.setInboundIpv6PolicyName(policyType.getInboundIpv6PolicyName());
				policyTypeBean.setInboundIpv6Preprovisioned(convertByteToBoolean(policyType.getInboundIpv6Preprovisioned()));
				policyTypeBean.setInboundIstandardpolicyv4(convertByteToBoolean(policyType.getInboundIstandardpolicyv4()));
				policyTypeBean.setInboundIstandardpolicyv6(convertByteToBoolean(policyType.getInboundIstandardpolicyv4()));
				policyTypeBean.setInboundRoutingIpv4Policy(convertByteToBoolean(policyType.getInboundRoutingIpv4Policy()));
				policyTypeBean.setInboundRoutingIpv6Policy(convertByteToBoolean(policyType.getInboundRoutingIpv6Policy()));
				policyTypeBean.setOutboundIpv4PolicyName(policyType.getOutboundIpv4PolicyName());
				policyTypeBean.setOutboundIpv4Preprovisioned(convertByteToBoolean(policyType.getOutboundIpv4Preprovisioned()));
				policyTypeBean.setOutboundIpv6PolicyName(policyType.getOutboundIpv6PolicyName());
				policyTypeBean.setOutboundIpv6Preprovisioned(convertByteToBoolean(policyType.getOutboundIpv6Preprovisioned()));
				policyTypeBean.setOutboundIstandardpolicyv4(convertByteToBoolean(policyType.getOutboundIstandardpolicyv4()));
				policyTypeBean.setOutboundIstandardpolicyv6(convertByteToBoolean(policyType.getOutboundIstandardpolicyv4()));
				policyTypeBean.setOutboundRoutingIpv4Policy(convertByteToBoolean(policyType.getOutboundRoutingIpv4Policy()));
				policyTypeBean.setOutboundRoutingIpv6Policy(convertByteToBoolean(policyType.getOutboundRoutingIpv6Policy()));
				policyTypeBean.setPolicyCriteria(constructPolicyCriterias(policyTypeBean, policyType));
				policyTypeBeans.add(policyTypeBean);
				}

			});
		}
		return policyTypeBeans;
	}

	private Set<PolicyCriteriaBean> constructPolicyCriterias(PolicyTypeBean policyTypeBean, PolicyType policyType) {
		Set<PolicyCriteriaBean> policyCriteriaBeans = new HashSet<>();

		if (policyType.getPolicyTypeCriteriaMappings() != null
				&& !policyType.getPolicyTypeCriteriaMappings().isEmpty()) {

			policyType.getPolicyTypeCriteriaMappings()
					.stream()
					.filter(mapping -> mapping.getEndDate()==null)
					.forEach(criteria -> {
				
				Optional<PolicyCriteria> oPolicyCriteria = policyCriteriaRepository
						.findById(criteria.getPolicyCriteriaId());
				if (oPolicyCriteria.isPresent()) {
					PolicyCriteria policyCriteria = oPolicyCriteria.get();
					if(policyCriteria.getEndDate()==null) {
						PolicyCriteriaBean policyCriteriaBean = new PolicyCriteriaBean();

						PolicycriteriaProtocol policycriteriaProtocol = policyCriteriaProtocolRepository
								.findFirstByPolicyCriteria_policyCriteriaId(policyCriteria.getPolicyCriteriaId());

						policyCriteriaBean.setPolicyCriteriaId(policyCriteria.getPolicyCriteriaId());
						policyCriteriaBean.setEndDate(policyCriteria.getEndDate());
						policyCriteriaBean.setMatchCriteriaPrefixListPreprovisioned(convertByteToBoolean(policyCriteria.getMatchcriteriaPprefixlistPreprovisioned()));
						policyCriteriaBean.setLastModifiedDate(policyCriteria.getLastModifiedDate());
						policyCriteriaBean.setName(policyCriteria.getName());
						policyCriteriaBean.setMatchcriteriaNeighbourCommunity(convertByteToBoolean(policyCriteria.getMatchcriteriaNeighbourCommunity()));
						policyCriteriaBean.setMatchCriteriaRegexAsPathAsPath(policyCriteria.getMatchcriteriaRegexAspathAspath());
						policyCriteriaBean.setMatchcriteriaPrefixlist(convertByteToBoolean(policyCriteria.getMatchcriteriaPrefixlist()));
						policyCriteriaBean.setMatchcriteriaProtocol(convertByteToBoolean(policyCriteria.getMatchcriteriaProtocol()));
						policyCriteriaBean.setMatchcriteriaRegexAspath(convertByteToBoolean(policyCriteria.getMatchcriteriaRegexAspath()));
						policyCriteriaBean.setMatchcriteriaRegexAspathName(policyCriteria.getMatchcriteriaRegexAspathName());
						if (policycriteriaProtocol != null && policycriteriaProtocol.getProtocolName() != null) {
							policyCriteriaBean.setMatchcriteriaProtocolName(policycriteriaProtocol.getProtocolName());
						}
						if (policycriteriaProtocol != null && policycriteriaProtocol.getProtocolValue() != null) {
							policyCriteriaBean.setMatchcriteriaProtocolvalue(policycriteriaProtocol.getProtocolValue());
						}
						policyCriteriaBean.setTermSetcriteriaAction(policyCriteria.getTermSetcriteriaAction());
						policyCriteriaBean.setTermName(policyCriteria.getTermName());
						policyCriteriaBean.setPrefixlistConfigs(constructPrefixConfig(policyCriteria, policyCriteriaBean));
						policyCriteriaBean.setNeighbourCommunityConfigs(constructNeighbour(policyCriteria, policyCriteriaBean));
						policyCriteriaBean.setSetCriteriaRegexAsPathAsPath(policyCriteria.getMatchcriteriaRegexAspathAspath());
						policyCriteriaBean.setMatchcriteriaPrefixlistName(policyCriteria.getMatchcriteriaPrefixlistName());
						policyCriteriaBean.setModifiedBy(policyCriteria.getModifiedBy());
						policyCriteriaBean.setSetcriteriaAspathPrependIndex(policyCriteria.getSetcriteriaAspathPrependIndex());
						policyCriteriaBean.setSetcriteriaAspathPrepend(convertByteToBoolean(policyCriteria.getSetcriteriaAspathPrepend()));
						policyCriteriaBean.setSetCriteriaRegexAsPathAsPath(policyCriteria.getMatchcriteriaRegexAspathAspath());
						policyCriteriaBean.setSetcriteriaAspathPrependName(policyCriteria.getSetcriteriaAspathPrependName());
						policyCriteriaBean.setSetcriteriaLocalPreference(convertByteToBoolean(policyCriteria.getSetcriteriaLocalPreference()));
						policyCriteriaBean.setSetcriteriaLocalpreferenceName(policyCriteria.getSetcriteriaLocalpreferenceName());
						policyCriteriaBean.setSetcriteriaMetric(convertByteToBoolean(policyCriteria.getSetcriteriaMetric()));
						policyCriteriaBean.setSetcriteriaMetricName(policyCriteria.getSetcriteriaMetricName());
						policyCriteriaBean.setSetcriteriaNeighbourCommunity(convertByteToBoolean(policyCriteria.getSetcriteriaNeighbourCommunity()));
						policyCriteriaBean.setSetcriteriaRegexAspath(convertByteToBoolean(policyCriteria.getSetcriteriaRegexAspath()));
						policyCriteriaBean.setSetcriteriaRegexAspathName(policyCriteria.getSetcriteriaRegexAspathName());
						policyCriteriaBean.setTermSetcriteriaAction(policyCriteria.getTermSetcriteriaAction());
						policyCriteriaBean.setRegexAspathConfigs(constructRegesAsPath(policyCriteria, policyCriteriaBean));

						policyCriteriaBeans.add(policyCriteriaBean);
					}
				}
			});
		}

		return policyCriteriaBeans;
	}

	private Set<RegexAspathConfigBean> constructRegesAsPath(PolicyCriteria policyCriteria,
			PolicyCriteriaBean policyCriteriaBean) {
		Set<RegexAspathConfigBean> regexAspathConfigBeans = new HashSet<>();

		if (policyCriteria.getRegexAspathConfigs() != null && !policyCriteria.getRegexAspathConfigs().isEmpty()) {
			policyCriteria.getRegexAspathConfigs().forEach(regas -> {
				if(regas.getEndDate()==null) {
				RegexAspathConfigBean regexAspathConfigBean = new RegexAspathConfigBean();
				regexAspathConfigBean.setAction(regas.getAction());
				regexAspathConfigBean.setAsPath(regas.getAsPath());
				regexAspathConfigBean.setEndDate(regas.getEndDate());
				regexAspathConfigBean.setLastModifiedDate(regas.getLastModifiedDate());
				regexAspathConfigBean.setName(regas.getName());
				regexAspathConfigBean.setRegexAspathid(regas.getRegexAspathid());
				regexAspathConfigBeans.add(regexAspathConfigBean);
				}

			});

		}
		return regexAspathConfigBeans;
	}

	private Set<NeighbourCommunityConfigBean> constructNeighbour(PolicyCriteria policyCriteria,
			PolicyCriteriaBean policyCriteriaBean) {
		Set<NeighbourCommunityConfigBean> neighbourCommunityConfigBeans = new HashSet<>();

		if (policyCriteria.getNeighbourCommunityConfigs() != null
				&& !policyCriteria.getNeighbourCommunityConfigs().isEmpty()) {
			policyCriteria.getNeighbourCommunityConfigs().forEach(neighbourCommunity -> {
				
				if(neighbourCommunity.getEndDate()==null) {
				NeighbourCommunityConfigBean communityConfigBean = new NeighbourCommunityConfigBean();
				communityConfigBean.setCommunity(neighbourCommunity.getCommunity());
				communityConfigBean.setIscommunityExtended(convertByteToBoolean(neighbourCommunity.getIscommunityExtended()));
				communityConfigBean.setAction(neighbourCommunity.getAction());
				communityConfigBean.setEndDate(neighbourCommunity.getEndDate());
				communityConfigBean.setLastModifiedDate(neighbourCommunity.getLastModifiedDate());
				communityConfigBean.setName(neighbourCommunity.getName());
				communityConfigBean.setNeighbourCommunityId(neighbourCommunity.getNeighbourCommunityId());
				neighbourCommunityConfigBeans.add(communityConfigBean);
				}
			});
		}
		return neighbourCommunityConfigBeans;
	}

	private Set<PrefixlistConfigBean> constructPrefixConfig(PolicyCriteria policyCriteria,
			PolicyCriteriaBean policyCriteriaBean) {
		Set<PrefixlistConfigBean> prefixlistConfigBeans = new HashSet<>();

		if (policyCriteria.getPrefixlistConfigs() != null && !policyCriteria.getPrefixlistConfigs().isEmpty()) {
			policyCriteria.getPrefixlistConfigs().forEach(prefixlistConfig -> {
				if(prefixlistConfig.getEndDate()==null) {
				PrefixlistConfigBean prefixlistConfigBean = new PrefixlistConfigBean();
				prefixlistConfigBean.setAction(prefixlistConfig.getAction());
				prefixlistConfigBean.setEndDate(prefixlistConfig.getEndDate());
				prefixlistConfigBean.setGeValue(prefixlistConfig.getGeValue());
				prefixlistConfigBean.setLastModifiedDate(prefixlistConfig.getLastModifiedDate());
				prefixlistConfigBean.setLeValue(prefixlistConfig.getLeValue());
				prefixlistConfigBean.setNetworkPrefix(prefixlistConfig.getNetworkPrefix()!=null?prefixlistConfig.getNetworkPrefix().trim():prefixlistConfig.getNetworkPrefix());
				prefixlistConfigBean.setPrefixlistId(prefixlistConfig.getPrefixlistId());
				prefixlistConfigBeans.add(prefixlistConfigBean);
				}

			});
		}

		return prefixlistConfigBeans;
	}

	private StaticProtocolBean setStaticProtocolBean(StaticProtocolBean staticProtocolBean,
			StaticProtocol staticProtocol) {

		staticProtocolBean.setEndDate(staticProtocol.getEndDate());
		staticProtocolBean.setIsroutemapEnabled(convertByteToBoolean(staticProtocol.getIsroutemapEnabled()));
		staticProtocolBean.setIsroutemapPreprovisioned(convertByteToBoolean(staticProtocol.getIsroutemapPreprovisioned()));
		staticProtocolBean.setLastModifiedDate(staticProtocol.getLastModifiedDate());
		staticProtocolBean.setLocalPreference(staticProtocol.getLocalPreference());
		staticProtocolBean.setLocalPreferenceV6(staticProtocol.getLocalPreferenceV6());
		staticProtocolBean.setModifiedBy(staticProtocol.getModifiedBy());
		staticProtocolBean.setRedistributeRoutemapIpv4(staticProtocol.getRedistributeRoutemapIpv4());
		staticProtocolBean.setStartDate(staticProtocol.getStartDate());
		staticProtocolBean.setStaticprotocolId(staticProtocol.getStaticprotocolId());

		return staticProtocolBean;
	}

	private WanStaticRouteBean setWanStaticRouteBean(WanStaticRouteBean wanStaticRouteBean,
													 WanStaticRoute wanStaticRoute){
		wanStaticRouteBean.setAdvalue(wanStaticRoute.getAdvalue());
		wanStaticRouteBean.setDescription(wanStaticRoute.getDescription());
		wanStaticRouteBean.setGlobal(convertByteToBoolean(wanStaticRoute.getGlobal()));
		wanStaticRouteBean.setIpsubnet(wanStaticRoute.getIpsubnet());
		wanStaticRouteBean.setNextHopid(wanStaticRoute.getNextHopid());
		wanStaticRouteBean.setPopCommunity(wanStaticRoute.getPopCommunity());
		wanStaticRouteBean.setRegionalCommunity(wanStaticRoute.getRegionalCommunity());
		wanStaticRouteBean.setServiceCommunity(wanStaticRoute.getServiceCommunity());
		wanStaticRouteBean.setWanstaticrouteId(wanStaticRoute.getWanstaticrouteId());
		wanStaticRouteBean.setEndDate(wanStaticRoute.getEndDate());
		wanStaticRouteBean.setIsCewan(convertByteToBoolean(wanStaticRoute.getIsCewan()));
		wanStaticRouteBean.setIsCpelanStaticroutes(convertByteToBoolean(wanStaticRoute.getIsCpelanStaticroutes()));
		wanStaticRouteBean.setIsCpewanStaticroutes(convertByteToBoolean(wanStaticRoute.getIsCpewanStaticroutes()));
		wanStaticRouteBean.setIsPewan(convertByteToBoolean(wanStaticRoute.getIsPewan()));
		wanStaticRouteBean.setLastModifiedDate(wanStaticRoute.getLastModifiedDate());
		wanStaticRouteBean.setModifiedBy(wanStaticRoute.getModifiedBy());
		wanStaticRouteBean.setStartDate(wanStaticRoute.getStartDate());
		return wanStaticRouteBean;
	}

	private Set<WanStaticRouteBean> constructWanstaticRouteDetails(StaticProtocol staticProtocol,
			StaticProtocolBean staticProtocolBean) {
		Set<WanStaticRouteBean> wanStaticRouteBeans = new HashSet<>();
		if (staticProtocol.getWanStaticRoutes() != null && !staticProtocol.getWanStaticRoutes().isEmpty()) {

			staticProtocol.getWanStaticRoutes().forEach(wanStaticRoute -> {
				
				if(wanStaticRoute.getEndDate()==null) {
					WanStaticRouteBean wanStaticRouteBean = new WanStaticRouteBean();

					setWanStaticRouteBean(wanStaticRouteBean,wanStaticRoute);

					wanStaticRouteBeans.add(wanStaticRouteBean);
				}
			});

		}
		staticProtocolBean.getWanStaticRoutes().addAll(wanStaticRouteBeans);

		return wanStaticRouteBeans;
	}

	private List<WanStaticRouteBean> constructWanstaticRouteDetailsForBgp(Bgp bgp, BgpBean bgpBean) {
		List<WanStaticRouteBean> wanStaticRouteBeans = new ArrayList<>();
		if (bgp.getWanStaticRoutes() != null && !bgp.getWanStaticRoutes().isEmpty()) {

			bgp.getWanStaticRoutes().forEach(wanStaticRoute -> {
				
				if(wanStaticRoute.getEndDate()==null) {
					WanStaticRouteBean wanStaticRouteBean = new WanStaticRouteBean();

					setWanStaticRouteBean(wanStaticRouteBean,wanStaticRoute);

					wanStaticRouteBeans.add(wanStaticRouteBean);
				}
				
			});

		}
		bgpBean.getWanStaticRoutes().addAll(wanStaticRouteBeans);

		return wanStaticRouteBeans;
	}

	private CpeBean setCpeBean(CpeBean cpeBean, Cpe cpe) {
		cpeBean.setCpeId(cpe.getCpeId());
		cpeBean.setCpeinitConfigparams(convertByteToBoolean(cpe.getCpeinitConfigparams()));
		cpeBean.setEndDate(cpe.getEndDate());
		cpeBean.setHostName(cpe.getHostName());
		cpeBean.setInitLoginpwd(cpe.getInitLoginpwd());
		cpeBean.setInitUsername(cpe.getInitUsername());
		cpeBean.setIsaceconfigurable(convertByteToBoolean(cpe.getIsaceconfigurable()));
		cpeBean.setLastModifiedDate(cpe.getLastModifiedDate());
		cpeBean.setLoopbackInterfaceName(cpe.getLoopbackInterfaceName());
		cpeBean.setMgmtLoopbackV4address(cpe.getMgmtLoopbackV4address());
		cpeBean.setMgmtLoopbackV6address(cpe.getMgmtLoopbackV6address());
		cpeBean.setMgmtLoopbackV6address(cpe.getMgmtLoopbackV6address());
		cpeBean.setModifiedBy(cpe.getModifiedBy());
		cpeBean.setNniCpeConfig(convertByteToBoolean(cpe.getNniCpeConfig()));
		cpeBean.setSendInittemplate(convertByteToBoolean(cpe.getSendInittemplate()));
		cpeBean.setServiceId(cpe.getServiceId());
		cpeBean.setSnmpServerCommunity(cpe.getSnmpServerCommunity());
		cpeBean.setStartDate(cpe.getStartDate());
		cpeBean.setUnmanagedCePartnerdeviceWanip(cpe.getUnmanagedCePartnerdeviceWanip());
		cpeBean.setVsatCpeConfig(convertByteToBoolean(cpe.getVsatCpeConfig()));
		cpeBean.setDeviceId(cpe.getDeviceId());
		cpeBean.setModel(cpe.getModel());
		cpeBean.setMake(cpe.getMake());
		return cpeBean;
	}

	private RouterDetailBean setRouterDetailBean(RouterDetailBean routerDetailBean, RouterDetail routerDetail) {
		routerDetailBean.setEndDate(routerDetail.getEndDate());
		routerDetailBean.setIpv4MgmtAddress(routerDetail.getIpv4MgmtAddress());
		routerDetailBean.setIpv6MgmtAddress(routerDetail.getIpv6MgmtAddress());
		routerDetailBean.setLastModifiedDate(routerDetail.getLastModifiedDate());
		routerDetailBean.setModifiedBy(routerDetail.getModifiedBy());
		routerDetailBean.setRouterHostname(routerDetail.getRouterHostname());
		routerDetailBean.setRouterId(routerDetail.getRouterId());
		routerDetailBean.setRouterMake(routerDetail.getRouterMake());
		routerDetailBean.setRouterModel(routerDetail.getRouterModel());
		routerDetailBean.setRouterRole(routerDetail.getRouterRole());
		routerDetailBean.setRouterType(routerDetail.getRouterType());
		routerDetailBean.setStartDate(routerDetail.getStartDate());
		return routerDetailBean;
	}

	private EigrpBean setEigrpBean(EigrpBean eigrpBean, Eigrp eigrp) {
		eigrpBean.setEigrpBwKbps(eigrp.getEigrpBwKbps());
		eigrpBean.setEigrpProtocolId(eigrp.getEigrpProtocolId());
		eigrpBean.setEndDate(eigrp.getEndDate());
		eigrpBean.setInterfaceDelay(eigrp.getInterfaceDelay());
		eigrpBean.setIsredistributeConnectedEnabled(convertByteToBoolean(eigrp.getIsredistributeConnectedEnabled()));
		eigrpBean.setIsredistributeStaticEnabled(convertByteToBoolean(eigrp.getIsredistributeStaticEnabled()));
		eigrpBean.setIsroutemapEnabled(convertByteToBoolean(eigrp.getIsroutemapEnabled()));
		eigrpBean.setIsroutemapPreprovisioned(convertByteToBoolean(eigrp.getIsroutemapPreprovisioned()));
		eigrpBean.setLastModifiedDate(eigrp.getLastModifiedDate());
		eigrpBean.setLoad(eigrp.getLoad());
		eigrpBean.setLocalAsnumber(eigrp.getLocalAsnumber());
		eigrpBean.setModifiedBy(eigrp.getModifiedBy());
		eigrpBean.setMtu(eigrp.getMtu());
		eigrpBean.setRedistributeRoutemapName(eigrp.getRedistributeRoutemapName());
		eigrpBean.setRedistributionDelay(eigrp.getRedistributionDelay());
		eigrpBean.setReliability(eigrp.getReliability());
		eigrpBean.setRemoteAsnumber(eigrp.getRemoteAsnumber());
		eigrpBean.setRemoteCeAsnumber(eigrp.getRemoteCeAsnumber());
		eigrpBean.setSooRequired(convertByteToBoolean(eigrp.getSooRequired()));
		eigrpBean.setStartDate(eigrp.getStartDate());
		return eigrpBean;
	}

	private OspfBean setOspfBean(OspfBean ospfBean, Ospf ospf) {
		ospfBean.setAreaId(ospf.getAreaId());
		ospfBean.setAuthenticationMode(ospf.getAuthenticationMode());
		ospfBean.setCost(ospf.getCost());
		ospfBean.setDomainId(ospf.getDomainId());
		ospfBean.setEndDate(ospf.getEndDate());
		ospfBean.setIsauthenticationRequired(convertByteToBoolean(ospf.getIsauthenticationRequired()));
		ospfBean.setIsenableShamlink(convertByteToBoolean(ospf.getIsenableShamlink()));
		ospfBean.setIsignoreipOspfMtu(convertByteToBoolean(ospf.getIsignoreipOspfMtu()));
		ospfBean.setIsnetworkP2p(convertByteToBoolean(ospf.getIsnetworkP2p()));
		ospfBean.setIsredistributeConnectedEnabled(convertByteToBoolean(ospf.getIsredistributeConnectedEnabled()));
		ospfBean.setIsredistributeStaticEnabled(convertByteToBoolean(ospf.getIsredistributeStaticEnabled()));
		ospfBean.setIsroutemapEnabled(convertByteToBoolean(ospf.getIsroutemapEnabled()));
		ospfBean.setIsroutemapPreprovisioned(ospf.getIsroutemapPreprovisioned());
		ospfBean.setLastModifiedDate(ospf.getLastModifiedDate());
		ospfBean.setLocalPreference(ospf.getLocalPreference());
		ospfBean.setLocalPreferenceV6(ospf.getLocalPreferenceV6());
		ospfBean.setModifiedBy(ospf.getModifiedBy());
		ospfBean.setOspfId(ospf.getOspfId());
		ospfBean.setOspfNetworkType(ospf.getOspfNetworkType());
		ospfBean.setPassword(ospf.getPassword());

		ospfBean.setProcessId(ospf.getProcessId());
		ospfBean.setRedistributeRoutermapname(ospf.getRedistributeRoutermapname());
		ospfBean.setShamlinkInterfaceName(ospf.getShamlinkInterfaceName());
		ospfBean.setShamlinkLocalAddress(ospf.getShamlinkLocalAddress());
		ospfBean.setShamlinkRemoteAddress(ospf.getShamlinkRemoteAddress());
		ospfBean.setStartDate(ospf.getStartDate());
		ospfBean.setVersion(ospf.getVersion());
		return ospfBean;
	}

	private RipBean setRipBean(RipBean ripBean, Rip rip) {
		ripBean.setEndDate(rip.getEndDate());
		ripBean.setGroupName(rip.getGroupName());
		ripBean.setLastModifiedDate(rip.getLastModifiedDate());
		ripBean.setLocalPreference(rip.getLocalPreference());
		ripBean.setLocalPreferenceV6(rip.getLocalPreferenceV6());
		ripBean.setModifiedBy(rip.getModifiedBy());
		ripBean.setRipId(rip.getRipId());
		ripBean.setStartDate(rip.getStartDate());
		ripBean.setVersion(rip.getVersion());
		return ripBean;
	}

	private IpAddressDetailBean setIpAddressDetailBean(IpAddressDetailBean ipAddressDetailBean,
			IpAddressDetail ipAddressDetail) {
		ipAddressDetailBean.setEndDate(ipAddressDetail.getEndDate());
		ipAddressDetailBean.setExtendedLanEnabled(convertByteToBoolean(ipAddressDetail.getExtendedLanEnabled()));
		ipAddressDetailBean.setIP_Address_Details(ipAddressDetail.getIP_Address_Details());
		ipAddressDetailBean.setLastModifiedDate(ipAddressDetail.getLastModifiedDate());
		ipAddressDetailBean.setModifiedBy(ipAddressDetail.getModifiedBy());
		ipAddressDetailBean.setNmsServiceIpv4Address(ipAddressDetail.getNmsServiceIpv4Address());
		ipAddressDetailBean.setNniVsatIpaddress(ipAddressDetail.getNniVsatIpaddress());
		ipAddressDetailBean.setNoMacAddress(ipAddressDetail.getNoMacAddress());
		ipAddressDetailBean.setPathIpType(ipAddressDetail.getPathIpType());
		ipAddressDetailBean.setPingAddress1(ipAddressDetail.getPingAddress1());
		ipAddressDetailBean.setPingAddress2(ipAddressDetail.getPingAddress2());
		ipAddressDetailBean.setStartDate(ipAddressDetail.getStartDate());
		ipAddressDetailBean.setPublicNATIpProvidedBy(ipAddressDetail.getPublicNatProvidedBy());
		ipAddressDetailBean.setPublicNATIp(ipAddressDetail.getPublicNat());
		return ipAddressDetailBean;
	}

	private MulticastingBean setMulticastingBean(MulticastingBean multicastingBean, Multicasting multicasting) {
		multicastingBean.setAutoDiscoveryOption(multicasting.getAutoDiscoveryOption());
		multicastingBean.setDataMdt(multicasting.getDataMdt());
		multicastingBean.setDataMdtThreshold(multicasting.getDataMdtThreshold());
		multicastingBean.setDefaultMdt(multicasting.getDefaultMdt());
		multicastingBean.setEndDate(multicasting.getEndDate());
		multicastingBean.setLastModifiedDate(multicasting.getLastModifiedDate());
		multicastingBean.setModifiedBy(multicasting.getModifiedBy());
		multicastingBean.setMulticatingId(multicasting.getMulticatingId());
		multicastingBean.setType(multicasting.getType());
		multicastingBean.setRpAddress(multicasting.getRpAddress());
		multicastingBean.setRpLocation(multicasting.getRpLocation());
		multicastingBean.setStartDate(multicasting.getStartDate());
		multicastingBean.setWanPimMode(multicasting.getWanPimMode());
		return multicastingBean;
	}

	private AluSchedulerPolicyBean setAluSchedulerPolicyBean(AluSchedulerPolicyBean aluSchedulerPolicyBean,
			AluSchedulerPolicy aluSchedulerPolicy) {
		aluSchedulerPolicyBean.setAluSchedulerPolicyId(aluSchedulerPolicy.getAluSchedulerPolicyId());
		aluSchedulerPolicyBean.setEndDate(aluSchedulerPolicy.getEndDate());
		aluSchedulerPolicyBean.setLastModifiedDate(aluSchedulerPolicy.getLastModifiedDate());
		aluSchedulerPolicyBean.setModifiedBy(aluSchedulerPolicy.getModifiedBy());
		aluSchedulerPolicyBean.setSapEgressPolicyname(aluSchedulerPolicy.getSapEgressPolicyname());
		aluSchedulerPolicyBean.setSapEgressPreprovisioned(convertByteToBoolean(aluSchedulerPolicy.getSapEgressPreprovisioned()));
		aluSchedulerPolicyBean.setSapIngressPolicyname(aluSchedulerPolicy.getSapIngressPolicyname());
		aluSchedulerPolicyBean
				.setSchedulerPolicyIspreprovisioned(convertByteToBoolean(aluSchedulerPolicy.getSchedulerPolicyIspreprovisioned()));
		aluSchedulerPolicyBean.setSchedulerPolicyName(aluSchedulerPolicy.getSchedulerPolicyName());
		aluSchedulerPolicyBean.setSapIngressPreprovisioned(convertByteToBoolean(aluSchedulerPolicy.getSapIngressPreprovisioned()));
		aluSchedulerPolicyBean.setStartDate(aluSchedulerPolicy.getStartDate());
		aluSchedulerPolicyBean.setTotalCirBw(aluSchedulerPolicy.getTotalCirBw());
		aluSchedulerPolicyBean.setTotalPirBw(aluSchedulerPolicy.getTotalPirBw());
		return aluSchedulerPolicyBean;
	}

	private VrfBean setVrfBean(VrfBean vrfBean, Vrf vrf) {
		vrfBean.setEndDate(vrf.getEndDate());
		vrfBean.setIsmultivrf(convertByteToBoolean(vrf.getIsmultivrf()));
		vrfBean.setIsvrfLiteEnabled(convertByteToBoolean(vrf.getIsvrfLiteEnabled()));
		vrfBean.setLastModifiedDate(vrf.getLastModifiedDate());
		vrfBean.setMastervrfServiceid(vrf.getMastervrfServiceid());
		vrfBean.setMaxRoutesValue(vrf.getMaxRoutesValue());
		vrfBean.setModifiedBy(vrf.getModifiedBy());
		vrfBean.setStartDate(vrf.getStartDate());
		vrfBean.setThreshold(vrf.getThreshold());
		vrfBean.setVrfId(vrf.getVrfId());
		vrfBean.setVrfName(vrf.getVrfName());
		vrfBean.setWarnOn(vrf.getWarnOn());
		vrfBean.setVrfProjectName(vrf.getVrfProjectName());
		vrfBean.setSlaveVrfServiceId(vrf.getSlaveVrfServiceId());
		return vrfBean;
	}

	private VpnSolutionBean setVpnSolutionBean(VpnSolutionBean vpnSolutionBean, VpnMetatData vpnMetatData) {
		vpnSolutionBean.setIsUa(vpnMetatData.getIsUa());
		vpnSolutionBean.setVpnSolutionId(vpnMetatData.getId());
		vpnSolutionBean.setVpnName(vpnMetatData.getVpnName());
		vpnSolutionBean.setVpnSolutionName(vpnMetatData.getVpnSolutionName());
		if(vpnMetatData.getVpnLegId()!=null){
			vpnSolutionBean.setVpnLegId(vpnMetatData.getVpnLegId().toString());
		}
		vpnSolutionBean.setLegRole(vpnMetatData.getL2oSiteRole());
		vpnSolutionBean.setVpnTopology(vpnMetatData.getL2OTopology());
		vpnSolutionBean.setManagementVpnType1(vpnMetatData.getManagementVpnType1());
		vpnSolutionBean.setManagementVpnType2(vpnMetatData.getManagementVpnType2());
		vpnSolutionBean.setIsE2eIntegrated(vpnMetatData.isE2eIntegrated());
		vpnSolutionBean.setIsenableUccService(vpnMetatData.isIsenableUccService());
		return vpnSolutionBean;
	}

	private HsrpVrrpProtocolBean setHsrpVrrpProtocolBean(HsrpVrrpProtocolBean hsrpVrrpProtocolBean,
			HsrpVrrpProtocol hsrpVrrpProtocol) {
		hsrpVrrpProtocolBean.setEndDate(hsrpVrrpProtocol.getEndDate());
		hsrpVrrpProtocolBean.setHelloValue(hsrpVrrpProtocol.getHelloValue());
		hsrpVrrpProtocolBean.setHoldValue(hsrpVrrpProtocol.getHoldValue());
		hsrpVrrpProtocolBean.setHsrpVrrpId(hsrpVrrpProtocol.getHsrpVrrpId());
		hsrpVrrpProtocolBean.setLastModifiedDate(hsrpVrrpProtocol.getLastModifiedDate());
		hsrpVrrpProtocolBean.setModifiedBy(hsrpVrrpProtocol.getModifiedBy());
		hsrpVrrpProtocolBean.setPriority(hsrpVrrpProtocol.getPriority());
		hsrpVrrpProtocolBean.setRole(hsrpVrrpProtocol.getRole());
		hsrpVrrpProtocolBean.setStartDate(hsrpVrrpProtocol.getStartDate());
		hsrpVrrpProtocolBean.setTimerUnit(hsrpVrrpProtocol.getTimerUnit());
		hsrpVrrpProtocolBean.setVirtualIpv4Address(hsrpVrrpProtocol.getVirtualIpv4Address());
		hsrpVrrpProtocolBean.setVirtualIpv6Address(hsrpVrrpProtocol.getVirtualIpv6Address());
		return hsrpVrrpProtocolBean;
	}

	private PolicyTypeCriteriaMappingBean setPolicyTypeCriteriaMappingBean(
			PolicyTypeCriteriaMappingBean policyTypeCriteriaMappingBean,
			PolicyTypeCriteriaMapping policyTypeCriteriaMapping) {
		policyTypeCriteriaMappingBean.setEndDate(policyTypeCriteriaMapping.getEndDate());
		policyTypeCriteriaMappingBean.setLastModifiedDate(policyTypeCriteriaMapping.getLastModifiedDate());
		policyTypeCriteriaMappingBean.setModifiedBy(policyTypeCriteriaMapping.getModifiedBy());
		policyTypeCriteriaMappingBean.setPolicyCriteriaId(policyTypeCriteriaMapping.getPolicyCriteriaId());
		policyTypeCriteriaMappingBean.setPolicyTypeCriteriaId(policyTypeCriteriaMapping.getPolicyTypeCriteriaId());
		policyTypeCriteriaMappingBean.setStartDate(policyTypeCriteriaMapping.getStartDate());
		policyTypeCriteriaMappingBean.setVersion(policyTypeCriteriaMapping.getVersion());
		return policyTypeCriteriaMappingBean;
	}

	private IpaddrWanv6AddressBean setIpaddrWanv6AddressBean(IpaddrWanv6AddressBean ipaddrWanv6AddressBean,
			IpaddrWanv6Address ipaddrWanv6Address) {
		ipaddrWanv6AddressBean.setEndDate(ipaddrWanv6Address.getEndDate());
		ipaddrWanv6AddressBean.setIscustomerprovided(convertByteToBoolean(ipaddrWanv6Address.getIscustomerprovided()));
		ipaddrWanv6AddressBean.setIssecondary(convertByteToBoolean(ipaddrWanv6Address.getIssecondary()));
		ipaddrWanv6AddressBean.setLastModifiedDate(ipaddrWanv6Address.getLastModifiedDate());
		ipaddrWanv6AddressBean.setModifiedBy(ipaddrWanv6Address.getModifiedBy());
		ipaddrWanv6AddressBean.setStartDate(ipaddrWanv6Address.getStartDate());
		ipaddrWanv6AddressBean.setWanv6Address(ipaddrWanv6Address.getWanv6Address());
		ipaddrWanv6AddressBean.setWanv6AddrId(ipaddrWanv6Address.getWanv6AddrId());
		return ipaddrWanv6AddressBean;
	}

	private IpaddrWanv4AddressBean setIpaddrWanv4AddressBean(IpaddrWanv4AddressBean ipaddrWanv4AddressBean,
			IpaddrWanv4Address ipaddrWanv4Address) {
		ipaddrWanv4AddressBean.setEndDate(ipaddrWanv4Address.getEndDate());
		ipaddrWanv4AddressBean.setIscustomerprovided(convertByteToBoolean(ipaddrWanv4Address.getIscustomerprovided()));
		ipaddrWanv4AddressBean.setIssecondary(convertByteToBoolean(ipaddrWanv4Address.getIssecondary()));
		ipaddrWanv4AddressBean.setLastModifiedDate(ipaddrWanv4Address.getLastModifiedDate());
		ipaddrWanv4AddressBean.setModifiedBy(ipaddrWanv4Address.getModifiedBy());
		ipaddrWanv4AddressBean.setStartDate(ipaddrWanv4Address.getStartDate());
		ipaddrWanv4AddressBean.setWanv4Address(ipaddrWanv4Address.getWanv4Address());
		ipaddrWanv4AddressBean.setWanv4AddrId(ipaddrWanv4Address.getWanv4AddrId());
		return ipaddrWanv4AddressBean;
	}

	private IpaddrLanv6AddressBean setIpaddrLanv6AddressBean(IpaddrLanv6AddressBean ipaddrLanv6AddressBean,
			IpaddrLanv6Address ipaddrLanv6Address) {
		ipaddrLanv6AddressBean.setEndDate(ipaddrLanv6Address.getEndDate());
		ipaddrLanv6AddressBean.setIscustomerprovided(convertByteToBoolean(ipaddrLanv6Address.getIscustomerprovided()));
		ipaddrLanv6AddressBean.setIssecondary(convertByteToBoolean(ipaddrLanv6Address.getIssecondary()));
		ipaddrLanv6AddressBean.setLanv6Address(ipaddrLanv6Address.getLanv6Address());
		ipaddrLanv6AddressBean.setLanv6AddrId(ipaddrLanv6Address.getLanv6AddrId());
		ipaddrLanv6AddressBean.setLastModifiedDate(ipaddrLanv6Address.getLastModifiedDate());
		ipaddrLanv6AddressBean.setModifiedBy(ipaddrLanv6AddressBean.getModifiedBy());
		ipaddrLanv6AddressBean.setStartDate(ipaddrLanv6AddressBean.getStartDate());
		return ipaddrLanv6AddressBean;
	}

	private IpaddrLanv4AddressBean setIpaddrLanv4AddressBean(IpaddrLanv4AddressBean ipaddrLanv4AddressBean,
			IpaddrLanv4Address ipaddrLanv4Address) {
		ipaddrLanv4AddressBean.setLanv4Address(ipaddrLanv4Address.getLanv4Address());
		ipaddrLanv4AddressBean.setIssecondary(convertByteToBoolean(ipaddrLanv4Address.getIssecondary()));
		ipaddrLanv4AddressBean.setIscustomerprovided(convertByteToBoolean(ipaddrLanv4Address.getIscustomerprovided()));
		ipaddrLanv4AddressBean.setEndDate(ipaddrLanv4Address.getEndDate());
		ipaddrLanv4AddressBean.setLanv4AddrId(ipaddrLanv4Address.getLanv4AddrId());
		ipaddrLanv4AddressBean.setStartDate(ipaddrLanv4Address.getStartDate());
		ipaddrLanv4AddressBean.setLastModifiedDate(ipaddrLanv4Address.getLastModifiedDate());
		return ipaddrLanv4AddressBean;
	}

	private PolicyTypeBean setPolicyTypeBean(PolicyTypeBean policyTypeBean, PolicyType policyType) {
		policyTypeBean.setIsadditionalpolicytermReqd(convertByteToBoolean(policyType.getIsadditionalpolicytermReqd()));
		policyTypeBean.setIslpinvpnpolicyconfigured(convertByteToBoolean(policyType.getIslpinvpnpolicyconfigured()));
		policyTypeBean.setModifiedBy(policyType.getModifiedBy());
		policyTypeBean.setIsvprnExportpolicyName(policyType.getIsvprnExportpolicyName());
		policyTypeBean.setIsvprnImportpolicy(convertByteToBoolean(policyType.getIsvprnImportpolicy()));
		policyTypeBean.setIsvprnExportPreprovisioned(convertByteToBoolean(policyType.getIsvprnExportPreprovisioned()));
		policyTypeBean.setIsvprnImportpolicyName(policyType.getIsvprnImportpolicyName());
		policyTypeBean.setIsvprnImportPreprovisioned(convertByteToBoolean(policyType.getIsvprnImportPreprovisioned()));
		policyTypeBean.setLastModifiedDate(policyType.getLastModifiedDate());
		policyTypeBean.setLocalpreferencev4Vpnpolicy(policyType.getLocalpreferencev4Vpnpolicy());
		policyTypeBean.setLocalpreferencev6Vpnpolicy(policyType.getLocalpreferencev6Vpnpolicy());
		policyTypeBean.setOriginatedefaultFlag(convertByteToBoolean(policyType.getOriginatedefaultFlag()));
		policyTypeBean.setOutboundIpv4PolicyName(policyType.getOutboundIpv4PolicyName());
		policyTypeBean.setOutboundIpv4Preprovisioned(convertByteToBoolean(policyType.getOutboundIpv4Preprovisioned()));
		policyTypeBean.setOutboundIpv6PolicyName(policyType.getOutboundIpv6PolicyName());
		policyTypeBean.setOutboundIpv6Preprovisioned(convertByteToBoolean(policyType.getOutboundIpv6Preprovisioned()));
		policyTypeBean.setOutboundIstandardpolicyv4(convertByteToBoolean(policyType.getOutboundIstandardpolicyv4()));
		policyTypeBean.setOutboundIstandardpolicyv6(convertByteToBoolean(policyType.getOutboundIstandardpolicyv6()));
		policyTypeBean.setOutboundRoutingIpv4Policy(convertByteToBoolean(policyType.getOutboundRoutingIpv4Policy()));
		policyTypeBean.setOutboundRoutingIpv6Policy(convertByteToBoolean(policyType.getOutboundRoutingIpv6Policy()));
		policyTypeBean.setPolicyId(policyType.getPolicyId());
		policyTypeBean.setStartDate(policyType.getStartDate());
		policyTypeBean.setInboundRoutingIpv6Policy(convertByteToBoolean(policyType.getInboundRoutingIpv6Policy()));
		policyTypeBean.setInboundRoutingIpv4Policy(convertByteToBoolean(policyType.getInboundRoutingIpv4Policy()));
		policyTypeBean.setInboundIstandardpolicyv6(convertByteToBoolean(policyType.getInboundIstandardpolicyv6()));
		policyTypeBean.setInboundIstandardpolicyv4(convertByteToBoolean(policyType.getInboundIstandardpolicyv4()));
		policyTypeBean.setInboundIpv6Preprovisioned(convertByteToBoolean(policyType.getInboundIpv6Preprovisioned()));
		policyTypeBean.setInboundIpv6PolicyName(policyType.getInboundIpv6PolicyName());
		policyTypeBean.setInboundIpv4Preprovisioned(convertByteToBoolean(policyType.getInboundIpv4Preprovisioned()));
		policyTypeBean.setInboundIpv4PolicyName(policyType.getInboundIpv4PolicyName());
		policyTypeBean.setEndDate(policyType.getEndDate());
		policyTypeBean.setAludefaultoriginatev6Key(convertByteToBoolean(policyType.getAludefaultoriginatev6Key()));
		policyTypeBean.setAludefaultoriginatev4Key(convertByteToBoolean(policyType.getAludefaultoriginatev4Key()));
		policyTypeBean.setPolicyCriteria(constructPolicyCriterias(policyTypeBean, policyType));
		return policyTypeBean;
	}
	
	private RadwinLastmileBean setRadwinLastmileBean(RadwinLastmileBean bean, RadwinLastmile radwinLastmile) {
		bean.setRadwinLastmileId(radwinLastmile.getRadwinLastmileId());
		bean.setAllowedVlanid(radwinLastmile.getAllowedVlanid());
		bean.setBtsIp(radwinLastmile.getBtsIp());
		bean.setBtsName(radwinLastmile.getBtsName());
		bean.setCustomerLocation(radwinLastmile.getCustomerLocation());
		bean.setDataVlanPriority(radwinLastmile.getDataVlanPriority());
		bean.setDataVlanid(radwinLastmile.getDataVlanid());
		bean.setEndDate(radwinLastmile.getEndDate());
		bean.setEthernet_Port_Config(radwinLastmile.getEthernet_Port_Config());
		bean.setFrequency(radwinLastmile.getFrequency());
		bean.setGatewayIp(radwinLastmile.getGatewayIp());
		bean.setHsuEgressTraffic(radwinLastmile.getHsuEgressTraffic());
		bean.setHsuIngressTraffic(radwinLastmile.getHsuIngressTraffic());
		bean.setHsuIp(radwinLastmile.getHsuIp());
		bean.setHsuMacAddr(radwinLastmile.getHsuMacAddr());
		bean.setHsuSubnet(radwinLastmile.getHsuSubnet());
		bean.setLastModifiedDate(radwinLastmile.getLastModifiedDate());
		bean.setMgmtVlanid(radwinLastmile.getMgmtVlanid());
		bean.setMirDl(radwinLastmile.getMirDl());
		bean.setMirUl(radwinLastmile.getMirUl());
		bean.setModifiedBy(radwinLastmile.getModifiedBy());
		bean.setNtpOffset(radwinLastmile.getNtpOffset());
		bean.setNtpServerIp(radwinLastmile.getNtpServerIp());
		bean.setProtocolSnmp(radwinLastmile.getProtocolSnmp());
		bean.setProtocolTelnet(radwinLastmile.getProtocolTelnet());
		bean.setProtocolWebinterface(radwinLastmile.getProtocolWebinterface());
		bean.setReqd_tx_power(radwinLastmile.getReqd_tx_power());
		bean.setSectorId(radwinLastmile.getSectorId());
		bean.setSiteContact(radwinLastmile.getSiteContact());
		bean.setSiteLat(radwinLastmile.getSiteLat());
		bean.setSiteLong(radwinLastmile.getSiteLong());
		bean.setSiteName(radwinLastmile.getSiteName());
		bean.setStartDate(radwinLastmile.getStartDate());
		bean.setUntagVlanId(radwinLastmile.getUntagVlanId());
		bean.setVlanMode(radwinLastmile.getVlanMode());
		
		return bean;
	}

	private WimaxLastmileBean setWimaxLastmileBean(WimaxLastmileBean bean, WimaxLastmile wimaxLastmile) {
		bean.setWimaxLastmileId(wimaxLastmile.getWimaxLastmileId());
		bean.setBtsIp(wimaxLastmile.getBtsIp());
		bean.setBtsName(wimaxLastmile.getBtsName());
		bean.setCuststaticWanip(wimaxLastmile.getCuststaticWanip());
		bean.setCuststaticWanipCustprovided(wimaxLastmile.getCuststaticWanipCustprovided());
		bean.setCuststaticWanipGateway(wimaxLastmile.getCuststaticWanipGateway());
		bean.setCuststaticWanipGatewayCustprovided(wimaxLastmile.getCuststaticWanipGatewayCustprovided());
		bean.setCuststaticWanipMask(wimaxLastmile.getCuststaticWanipMask());
		bean.setCuststaticWanipMaskCustprovided(wimaxLastmile.getCuststaticWanipMaskCustprovided());
		bean.setDescription1(wimaxLastmile.getDescription1());
		bean.setDescription2(wimaxLastmile.getDescription2());
		bean.setEndDate(wimaxLastmile.getEndDate());
		bean.setGatemgmtipCustprovided(wimaxLastmile.getGatemgmtipCustprovided());
		bean.setGatewayMgmtIp(wimaxLastmile.getGatewayMgmtIp());
		bean.setHomeRegion(wimaxLastmile.getHomeRegion());
		bean.setLastModifiedDate(wimaxLastmile.getLastModifiedDate());
		bean.setModifiedBy(wimaxLastmile.getModifiedBy());
		bean.setPortSpeed(wimaxLastmile.getPortSpeed());
		bean.setPortSpeedUnit(wimaxLastmile.getPortSpeedUnit());
		bean.setProvisioningMode(wimaxLastmile.getProvisioningMode());
		bean.setSsvlanTagging(wimaxLastmile.getSsvlanTagging());
		bean.setStartDate(wimaxLastmile.getStartDate());
		bean.setSuMgmtIp(wimaxLastmile.getSuMgmtIp());
		bean.setSuMgmtSubnet(wimaxLastmile.getSuMgmtSubnet());
		bean.setSumacAddress(wimaxLastmile.getSumacAddress());
		bean.setSumgmtipCustprovided(wimaxLastmile.getSumgmtipCustprovided());
		bean.setSumgmtsubnetCustprovided(wimaxLastmile.getSumgmtsubnetCustprovided());
		bean.setUniqueName(wimaxLastmile.getUniqueName());
	
		return bean;
	}

	private VlanQosProfileBean setVlanQosProfileBean(VlanQosProfileBean bean, VlanQosProfile vlanQosProfile) {
		bean.setVlanQosProfileId(vlanQosProfile.getVlanQosProfileId());
		bean.setDownstreamQosProfile(vlanQosProfile.getDownstreamQosProfile());
		bean.setEndDate(vlanQosProfile.getEndDate());
		bean.setLastModifiedDate(vlanQosProfile.getLastModifiedDate());
		bean.setModifiedBy(vlanQosProfile.getModifiedBy());
		bean.setStartDate(vlanQosProfile.getStartDate());
		bean.setUpstreamQosProfile(vlanQosProfile.getUpstreamQosProfile());
		bean.setVlanId(vlanQosProfile.getVlanId());
		return bean;
	}

	private CambiumLastmileBean setCambiumLastmileBean(CambiumLastmileBean bean, CambiumLastmile cambiumLastmile) {
		bean.setAuthenticationKey(cambiumLastmile.getAuthenticationKey());
		bean.setBsIp(cambiumLastmile.getBsIp());
		bean.setBsName(cambiumLastmile.getBsName());
		bean.setBwDownlinkSustainedRate(cambiumLastmile.getBwDownlinkSustainedRate());
		bean.setBwUplinkSustainedRate(cambiumLastmile.getBwDownlinkSustainedRate());
		bean.setCambiumLastmileId(cambiumLastmile.getCambiumLastmileId());
		if(cambiumLastmile.getColorCode1()!=null) {
		bean.setColorCode1(cambiumLastmile.getColorCode1().toString());
		}
		bean.setCustomRadioFrequencyList(cambiumLastmile.getCustomRadioFrequencyList());
		bean.setDefaultPortVid(cambiumLastmile.getDefaultPortVid());
		bean.setDeviceType(cambiumLastmile.getDeviceType());
		bean.setDownlinkBurstAllocation(cambiumLastmile.getDownlinkBurstAllocation());
		bean.setEndDate(cambiumLastmile.getEndDate());
		bean.setEnforceAuthentication(cambiumLastmile.getEnforceAuthentication());
		bean.setFrameTimingPulseGated((byte) 1);
		bean.setHipriorityChannel(cambiumLastmile.getHipriorityChannel());
		bean.setHipriorityDownlinkCir(cambiumLastmile.getHipriorityDownlinkCir());
		bean.setHipriorityUplinkCir(cambiumLastmile.getHipriorityUplinkCir());
		bean.setHomeRegion(cambiumLastmile.getHomeRegion());
		if (cambiumLastmile.getInstallationColorCode() == "Enable") {
			bean.setInstallationColorCode((byte) 1);
		} else {
			bean.setInstallationColorCode((byte) 0);
		}
		bean.setLastModifiedDate(cambiumLastmile.getLastModifiedDate());
		bean.setLatitudeSettings(cambiumLastmile.getLatitudeSettings());
		bean.setLinkSpeed(cambiumLastmile.getLinkSpeed());
		bean.setLongitudeSettings(cambiumLastmile.getLongitudeSettings());
		bean.setLowpriorityDownlinkCir(cambiumLastmile.getLowpriorityDownlinkCir());
		bean.setLowpriorityUplinkCir(cambiumLastmile.getLowpriorityUplinkCir());
		bean.setMappedVid2(cambiumLastmile.getMappedVid2()!=null ? new Integer(cambiumLastmile.getMappedVid2()).toString() :null);
		bean.setMgmtIpForSsSu(cambiumLastmile.getMgmtIpForSsSu());
		bean.setMgmtIpGateway(cambiumLastmile.getMgmtIpGateway());
		bean.setMgmtSubnetForSsSu(cambiumLastmile.getMgmtSubnetForSsSu());
		bean.setMgmtVid(cambiumLastmile.getMgmtVid());
		bean.setModifiedBy(cambiumLastmile.getModifiedBy());
		bean.setPortSpeed(cambiumLastmile.getPortSpeed());
		bean.setPortSpeedUnit(cambiumLastmile.getPortSpeedUnit());
		bean.setProviderVid(cambiumLastmile.getProviderVid()!=null ? new Integer(cambiumLastmile.getProviderVid()).toString() :null);
		bean.setRealm(cambiumLastmile.getRealm());
		bean.setRegion(cambiumLastmile.getRegion());
		bean.setRegionCode(cambiumLastmile.getRegionCode());
		bean.setSiteContact(cambiumLastmile.getSiteContact());
		bean.setSiteLocation(cambiumLastmile.getSiteLocation());
		bean.setSiteName(cambiumLastmile.getSiteName());
		bean.setSmHeight(cambiumLastmile.getSmHeight());
		bean.setStartDate(cambiumLastmile.getStartDate());
		bean.setSuMacAddress(cambiumLastmile.getSuMacAddress());
		bean.setUplinkBurstAllocation(cambiumLastmile.getUplinkBurstAllocation());
		bean.setDownlinkPlan(cambiumLastmile.getDownlinkPlan());
		bean.setUplinkPlan(cambiumLastmile.getUplinkPlan());
		bean.setWeight(cambiumLastmile.getWeight());
		bean.setUserLockModulation(cambiumLastmile.getUserLockModulation());
		bean.setLockModulation(cambiumLastmile.getLockModulation());
		bean.setThersholdModulation(cambiumLastmile.getThersholdModulation());
		bean.setPrioritizationGroup(cambiumLastmile.getPrioritizationGroup());
		return bean;
	}

	private void updateOrderDetails(OrderDetail orderDetail, OrderDetailBean orderDetailBean, boolean migration, ScServiceDetail scServiceDetail){

		orderDetail.setAccountId(orderDetailBean.getAccountId());
		orderDetail.setAddressLine1(orderDetailBean.getAddressLine1());
		orderDetail.setAddressLine2(orderDetailBean.getAddressLine2());
		orderDetail.setAddressLine3(orderDetailBean.getAddressLine3());
		orderDetail.setAluCustomerId(orderDetailBean.getAluCustomerId());
		orderDetail.setAsdOpptyFlag(convertBooleanToByte(orderDetailBean.getAsdOpptyFlag()));
		orderDetail.setCity(orderDetailBean.getCity());
		orderDetail.setCopfId(orderDetailBean.getCopfId());
		orderDetail.setCountry(orderDetailBean.getCountry());
		orderDetail.setCustomerCategory(orderDetailBean.getCustomerCategory());
		orderDetail.setCustomerContact(orderDetailBean.getCustomerContact());
		orderDetail.setCustomerEmail(orderDetailBean.getCustomerEmail());
		orderDetail.setCustomerCrnId(orderDetailBean.getCustomerCrnId());
		orderDetail.setCustomerName(orderDetailBean.getCustomerName());
		orderDetail.setCustomerPhoneno(orderDetailBean.getCustomerPhoneno());
		orderDetail.setCustomerType(orderDetailBean.getCustomerType());
		orderDetail.setCustCuId(orderDetailBean.getCustCuId());
		orderDetail.setEndCustomerName(orderDetailBean.getEndCustomerName());
		orderDetail.setExtOrderrefId(orderDetailBean.getExtOrderrefId());
		orderDetail.setEndDate(orderDetailBean.getEndDate());
		orderDetail.setGroupId(orderDetailBean.getGroupId());
		orderDetail.setLocation(orderDetailBean.getLocation());
		orderDetail.setModifiedBy(migration ? "FTI_Refresh" : "OPTIMUS_UI");
		orderDetail.setOptyBidCategory(orderDetailBean.getOptyBidCategory());
		orderDetail.setOrderCategory(orderDetailBean.getOrderCategory());
		orderDetail.setOrderId(orderDetailBean.getOrderId());
		orderDetail.setOrderStatus(orderDetailBean.getOrderStatus());	
		if(Objects.nonNull(scServiceDetail)) {
			orderDetail.setOrderUuid(scServiceDetail.getScOrder().getOpOrderCode());
			orderDetail.setOrderType(scServiceDetail.getScOrder().getOrderType());
		}
		orderDetail.setOriginatorDate(orderDetailBean.getOriginatorDate());
		orderDetail.setOriginatorName(orderDetailBean.getOriginatorName());
		orderDetail.setPincode(orderDetailBean.getPincode());
		orderDetail.setProjectName(orderDetailBean.getProjectName());
		orderDetail.setSamCustomerDescription(orderDetailBean.getSamCustomerDescription());
		orderDetail.setSfdcOpptyId(orderDetailBean.getSfdcOpptyId());
		orderDetail.setStartDate(migration ? new Timestamp(new Date().getTime()) :  orderDetailBean.getStartDate());
		orderDetail.setLastModifiedDate(migration ? new Timestamp(new Date().getTime()) : orderDetailBean.getLastModifiedDate());
		orderDetail.setState(orderDetailBean.getState());
		orderDetailRepository.saveAndFlush(orderDetail);
	}
	
	

	private void saveServiceDetails(ServiceDetail serviceDetail, ServiceDetailBean serviceDetailBean){
		logger.info("Inside saveServiceDetails method for serviceCode {}", serviceDetailBean.getServiceId());
		serviceDetail.setAluSvcid(serviceDetailBean.getAluSvcid());
		serviceDetail.setAddressLine1(serviceDetailBean.getAddressLine1());
		serviceDetail.setAddressLine2(serviceDetailBean.getAddressLine2());
		serviceDetail.setAddressLine3(serviceDetailBean.getAddressLine3());
		serviceDetail.setAluSvcName(serviceDetailBean.getAluSvcName());
		serviceDetail.setBurstableBw(serviceDetailBean.getBurstableBw());
		serviceDetail.setBurstableBwUnit(serviceDetailBean.getBurstableBwUnit());
		serviceDetail.setCity(serviceDetailBean.getCity());
		serviceDetail.setCountry(serviceDetailBean.getCountry());
		serviceDetail.setDataTransferCommit(serviceDetailBean.getDataTransferCommit());
		serviceDetail.setDataTransferCommitUnit(serviceDetailBean.getDataTransferCommitUnit());
		serviceDetail.setDescription(serviceDetailBean.getDescription());
		serviceDetail.setEligibleForRevision(convertBooleanToByte(serviceDetailBean.getEligibleForRevision()));
		serviceDetail.setEndDate(serviceDetailBean.getEndDate());
		serviceDetail.setExpediteTerminate(convertBooleanToByte(serviceDetailBean.getExpediteTerminate()));
		serviceDetail.setExternalRefid(serviceDetailBean.getExternalRefid());
		serviceDetail.setId(serviceDetailBean.getId());
		serviceDetail.setIntefaceDescSvctag(serviceDetailBean.getIntefaceDescSvctag());
		serviceDetail.setIsIdcService(convertBooleanToByte(serviceDetailBean.getIsIdcService()));
		serviceDetail.setIsportChanged(convertBooleanToByte(serviceDetailBean.getPortChanged()));
		serviceDetail.setIscustomConfigReqd(convertBooleanToByte(serviceDetailBean.getIscustomConfigReqd()));
		serviceDetail.setIsdowntimeReqd(convertBooleanToByte(serviceDetailBean.getIsdowntimeReqd()));
		serviceDetail.setIsIdcService(convertBooleanToByte(serviceDetailBean.getIsIdcService()));
		serviceDetail.setIsrevised(convertBooleanToByte(serviceDetailBean.getIsrevised()));
		serviceDetail.setLastmileProvider(serviceDetailBean.getLastmileProvider());
		serviceDetail.setLastmileType(serviceDetailBean.getLastmileType());
		serviceDetail.setLastModifiedDate(serviceDetailBean.getLastModifiedDate());
		serviceDetail.setMgmtType(serviceDetailBean.getMgmtType());
		serviceDetail.setModifiedBy("OPTIMUS_UI");
		serviceDetail.setNetpRefid(serviceDetailBean.getNetpRefid());
		serviceDetail.setOldServiceId(serviceDetailBean.getOldServiceId());
		serviceDetail.setPincode(serviceDetailBean.getPincode());
		serviceDetail.setRedundancyRole(serviceDetailBean.getRedundancyRole());
		serviceDetail.setScopeOfMgmt(serviceDetailBean.getScopeOfMgmt());
		serviceDetail.setServiceBandwidth(serviceDetailBean.getServiceBandwidth());
		serviceDetail.setServiceBandwidthUnit(serviceDetailBean.getServiceBandwidthUnit());
		serviceDetail.setServiceComponenttype(serviceDetailBean.getServiceComponenttype());
		serviceDetail.setServiceId(serviceDetailBean.getServiceId());
		serviceDetail.setServiceState(serviceDetailBean.getServiceState());
		serviceDetail.setServiceSubtype(serviceDetailBean.getServiceSubtype());
		serviceDetail.setServiceType(serviceDetailBean.getServiceType());
		serviceDetail.setSkipDummyConfig(convertBooleanToByte(serviceDetailBean.getSkipDummyConfig()));
		serviceDetail.setState(serviceDetailBean.getState());
		serviceDetail.setSvclinkRole(serviceDetailBean.getSvclinkRole());
		serviceDetail.setSolutionId(serviceDetailBean.getSolutionId());
		serviceDetail.setSolutionName(serviceDetailBean.getSolutionName());
		serviceDetail.setStartDate(serviceDetailBean.getStartDate());
		serviceDetail.setSvclinkSrvid(serviceDetailBean.getSvclinkSrvid());
		serviceDetail.setTrfsDate(serviceDetailBean.getTrfsDate());
		serviceDetail.setTrfsTriggerDate(serviceDetailBean.getTrfsTriggerDate());
		serviceDetail.setTriggerNccmOrder(convertBooleanToByte(serviceDetailBean.getTriggerNccmOrder()));
		serviceDetail.setUsageModel(serviceDetailBean.getUsageModel());
		serviceDetail.setVersion(serviceDetailBean.getVersion());
		serviceDetail.setRouterMake(serviceDetailBean.getRouterMake());
		serviceDetail.setServiceOrderType(serviceDetailBean.getServiceOrderType());
		serviceDetailRepository.saveAndFlush(serviceDetail);
	}

	private void updateCiscoMapDetails(CiscoImportMap ciscoImportMap, CiscoImportMapBean ciscoImportMapBean){
		ciscoImportMap.setCiscoImportId(ciscoImportMapBean.getCiscoImportId());
		ciscoImportMap.setDescription(ciscoImportMapBean.getDescription());
		ciscoImportMap.setEndDate(ciscoImportMapBean.getEndDate());
		ciscoImportMap.setLastModifiedDate(new Timestamp(new Date().getTime()));
		ciscoImportMap.setModifiedBy("OPTIMUS_UI");
		ciscoImportMapRepository.saveAndFlush(ciscoImportMap);
	}

	private void updateAluSchedulerPolicy(AluSchedulerPolicy aluSchedulerPolicy, AluSchedulerPolicyBean aluSchedulerPolicyBean){
		aluSchedulerPolicy.setAluSchedulerPolicyId(aluSchedulerPolicyBean.getAluSchedulerPolicyId());
		aluSchedulerPolicy.setEndDate(aluSchedulerPolicyBean.getEndDate());
		aluSchedulerPolicy.setLastModifiedDate(new Timestamp(new Date().getTime()));
		aluSchedulerPolicy.setModifiedBy("OPTIMUS_UI");
		aluSchedulerPolicy.setSapEgressPolicyname(aluSchedulerPolicyBean.getSapEgressPolicyname());
		aluSchedulerPolicy.setSapEgressPreprovisioned(convertBooleanToByte(aluSchedulerPolicyBean.getSapEgressPreprovisioned()));
		aluSchedulerPolicy.setSapIngressPolicyname(aluSchedulerPolicyBean.getSapIngressPolicyname());
		aluSchedulerPolicy
				.setSchedulerPolicyIspreprovisioned(convertBooleanToByte(aluSchedulerPolicyBean.getSchedulerPolicyIspreprovisioned()));
		aluSchedulerPolicy.setSchedulerPolicyName(aluSchedulerPolicyBean.getSchedulerPolicyName());
		aluSchedulerPolicy.setSapIngressPreprovisioned(convertBooleanToByte(aluSchedulerPolicyBean.getSapIngressPreprovisioned()));
		aluSchedulerPolicy.setStartDate(aluSchedulerPolicyBean.getStartDate());
		aluSchedulerPolicy.setTotalCirBw(aluSchedulerPolicyBean.getTotalCirBw());
		aluSchedulerPolicy.setTotalPirBw(aluSchedulerPolicyBean.getTotalPirBw());
		aluSchedulerPolicyRepository.saveAndFlush(aluSchedulerPolicy);
	}

	private void updateIpAddressDetails(IpAddressDetail ipAddressDetail, IpAddressDetailBean ipAddressDetailBean){
		ipAddressDetail.setEndDate(ipAddressDetailBean.getEndDate());
		ipAddressDetail.setExtendedLanEnabled(convertBooleanToByte(ipAddressDetailBean.getExtendedLanEnabled()));
		ipAddressDetail.setIP_Address_Details(ipAddressDetailBean.getIP_Address_Details());
		ipAddressDetail.setLastModifiedDate(new Timestamp(new Date().getTime()));
		ipAddressDetail.setModifiedBy("OPTIMUS_UI");
		ipAddressDetail.setNmsServiceIpv4Address(ipAddressDetailBean.getNmsServiceIpv4Address());
		ipAddressDetail.setNniVsatIpaddress(ipAddressDetailBean.getNniVsatIpaddress());
		ipAddressDetail.setNoMacAddress(ipAddressDetailBean.getNoMacAddress());
		ipAddressDetail.setPathIpType(ipAddressDetailBean.getPathIpType());
		ipAddressDetail.setPingAddress1(ipAddressDetailBean.getPingAddress1());
		ipAddressDetail.setPingAddress2(ipAddressDetailBean.getPingAddress2());
		ipAddressDetail.setStartDate(ipAddressDetailBean.getStartDate());
		ipAddressDetail.setPublicNatProvidedBy(ipAddressDetailBean.getPublicNATIpProvidedBy());
		ipAddressDetail.setPublicNat(ipAddressDetailBean.getPublicNATIp());
		ipAddressDetailRepository.saveAndFlush(ipAddressDetail);
	}

	private void updateIpAddrLanv4Details(IpaddrLanv4Address ipaddrLanv4Address, IpaddrLanv4AddressBean ipaddrLanv4AddressBean,
										  IpAddressDetail ipAddressDetail) {
		ipaddrLanv4Address.setLanv4Address(ipaddrLanv4AddressBean.getLanv4Address());
		ipaddrLanv4Address.setIssecondary(convertBooleanToByte(ipaddrLanv4AddressBean.getIssecondary()));
		ipaddrLanv4Address.setIscustomerprovided(convertBooleanToByte(ipaddrLanv4AddressBean.getIscustomerprovided()));
		ipaddrLanv4Address.setEndDate(ipaddrLanv4AddressBean.getEndDate());
		ipaddrLanv4Address.setLanv4AddrId(ipaddrLanv4AddressBean.getLanv4AddrId());
		ipaddrLanv4Address.setStartDate(ipaddrLanv4AddressBean.getStartDate());
		ipaddrLanv4Address.setLastModifiedDate(new Timestamp(new Date().getTime()));
		ipaddrLanv4Address.setModifiedBy("OPTIMUS_UI");
		if(Objects.nonNull(ipAddressDetail))
			ipaddrLanv4Address.setIpAddressDetail(ipAddressDetail);
		ipaddrLanv4AddressRepository.saveAndFlush(ipaddrLanv4Address);
	}

	private void updateIpAddrLanv6Details(IpaddrLanv6Address ipaddrLanv6Address, IpaddrLanv6AddressBean ipaddrLanv6AddressBean,
										  IpAddressDetail ipAddressDetail) {
		ipaddrLanv6Address.setEndDate(ipaddrLanv6AddressBean.getEndDate());
		ipaddrLanv6Address.setIscustomerprovided(convertBooleanToByte(ipaddrLanv6AddressBean.getIscustomerprovided()));
		ipaddrLanv6Address.setIssecondary(convertBooleanToByte(ipaddrLanv6AddressBean.getIssecondary()));
		ipaddrLanv6Address.setLanv6Address(ipaddrLanv6AddressBean.getLanv6Address());
		ipaddrLanv6Address.setLanv6AddrId(ipaddrLanv6AddressBean.getLanv6AddrId());
		ipaddrLanv6Address.setLastModifiedDate(new Timestamp(new Date().getTime()));
		ipaddrLanv6Address.setModifiedBy("OPTIMUS_UI");
		ipaddrLanv6Address.setStartDate(ipaddrLanv6AddressBean.getStartDate());
		if(Objects.nonNull(ipAddressDetail))
			ipaddrLanv6Address.setIpAddressDetail(ipAddressDetail);
		ipaddrLanv6AddressRepository.saveAndFlush(ipaddrLanv6Address);

	}

	private void updateIpAddrWanv4Details(IpaddrWanv4Address ipaddrWanv4Address, IpaddrWanv4AddressBean ipaddrWanv4AddressBean,
										  IpAddressDetail ipAddressDetail){
		ipaddrWanv4Address.setEndDate(ipaddrWanv4AddressBean.getEndDate());
		ipaddrWanv4Address.setIscustomerprovided(convertBooleanToByte(ipaddrWanv4AddressBean.getIscustomerprovided()));
		ipaddrWanv4Address.setIssecondary(convertBooleanToByte(ipaddrWanv4AddressBean.getIssecondary()));
		ipaddrWanv4Address.setLastModifiedDate(new Timestamp(new Date().getTime()));
		ipaddrWanv4Address.setModifiedBy("OPTIMUS_UI");
		ipaddrWanv4Address.setStartDate(ipaddrWanv4AddressBean.getStartDate());
		ipaddrWanv4Address.setWanv4Address(ipaddrWanv4AddressBean.getWanv4Address());
		ipaddrWanv4Address.setWanv4AddrId(ipaddrWanv4AddressBean.getWanv4AddrId());
		if(Objects.nonNull(ipAddressDetail))
			ipaddrWanv4Address.setIpAddressDetail(ipAddressDetail);
		ipaddrWanv4AddressRepository.saveAndFlush(ipaddrWanv4Address);
	}

	private void updateIpAddrWanv6Details(IpaddrWanv6Address ipaddrWanv6Address, IpaddrWanv6AddressBean ipaddrWanv6AddressBean,
										  IpAddressDetail ipAddressDetail){
		ipaddrWanv6Address.setEndDate(ipaddrWanv6AddressBean.getEndDate());
		ipaddrWanv6Address.setIscustomerprovided(convertBooleanToByte(ipaddrWanv6AddressBean.getIscustomerprovided()));
		ipaddrWanv6Address.setIssecondary(convertBooleanToByte(ipaddrWanv6AddressBean.getIssecondary()));
		ipaddrWanv6Address.setLastModifiedDate(new Timestamp(new Date().getTime()));
		ipaddrWanv6Address.setModifiedBy("OPTIMUS_UI");
		ipaddrWanv6Address.setStartDate(ipaddrWanv6AddressBean.getStartDate());
		ipaddrWanv6Address.setWanv6Address(ipaddrWanv6AddressBean.getWanv6Address());
		ipaddrWanv6Address.setWanv6AddrId(ipaddrWanv6AddressBean.getWanv6AddrId());
		if(Objects.nonNull(ipAddressDetail))
			ipaddrWanv6Address.setIpAddressDetail(ipAddressDetail);
		ipaddrWanv6AddressRepository.saveAndFlush(ipaddrWanv6Address);
	}

	private void updateMulticastingDetails(Multicasting multiCasting, MulticastingBean multiCastingBean){

		multiCasting.setAutoDiscoveryOption(multiCastingBean.getAutoDiscoveryOption());
		multiCasting.setDataMdt(multiCastingBean.getDataMdt());
		multiCasting.setDataMdtThreshold(multiCastingBean.getDataMdtThreshold());
		multiCasting.setDefaultMdt(multiCastingBean.getDefaultMdt());
		multiCasting.setType(multiCastingBean.getType());
		multiCasting.setEndDate(multiCastingBean.getEndDate());
		multiCasting.setLastModifiedDate(new Timestamp(new Date().getTime()));
		multiCasting.setModifiedBy("OPTIMUS_UI");
		multiCasting.setMulticatingId(multiCastingBean.getMulticatingId());
		multiCasting.setRpAddress(multiCastingBean.getRpAddress());
		multiCasting.setRpLocation(multiCastingBean.getRpLocation());
		multiCasting.setStartDate(multiCastingBean.getStartDate());
		multiCasting.setWanPimMode(multiCastingBean.getWanPimMode());
		multicastingRepository.saveAndFlush(multiCasting);

	}

	private void updateServiceQoDetails(ServiceQo serviceQo, ServiceQoBean serviceQoBean){
		logger.info("Inside updateServiceQoDetails method");
		serviceQo.setCosPackage(serviceQoBean.getCosPackage());
		serviceQo.setCosProfile(serviceQoBean.getCosProfile());
		serviceQo.setCosType(serviceQoBean.getCosType());
		serviceQo.setEndDate(serviceQoBean.getEndDate());
		serviceQo.setFlexiCosIdentifier(serviceQoBean.getFlexiCosIdentifier());
		serviceQo.setIsbandwidthApplicable(convertBooleanToByte(serviceQoBean.getIsbandwidthApplicable()));
		serviceQo.setIsdefaultFc(convertBooleanToByte(serviceQoBean.getIsdefaultFc()));
		serviceQo.setIsflexicos(convertBooleanToByte(serviceQoBean.getIsflexicos()));
		serviceQo.setLastModifiedDate(new Timestamp(new Date().getTime()));
		serviceQo.setModifiedBy("OPTIMUS_UI");
		serviceQo.setNcTraffic(convertBooleanToByte(serviceQoBean.getNcTraffic()));
		serviceQo.setPirBw(serviceQoBean.getPirBw());
		serviceQo.setPirBwUnit(serviceQoBean.getPirBwUnit());
		serviceQo.setQosTrafiicMode(serviceQoBean.getQosTrafiicMode());
		serviceQo.setSummationOfBw(serviceQoBean.getSummationOfBw());
		serviceQoRepository.saveAndFlush(serviceQo);
	}

	private void setServiceCosValuesToNull(ServiceCosCriteria serviceCosCriteria, ServiceCosCriteriaBean serviceCosCriteriaBean,
										   boolean ipPrecedenceStatus, boolean dscpStatus){
		if(ipPrecedenceStatus == true){
			serviceCosCriteria.setIpprecedenceVal1(null);
			serviceCosCriteria.setIpprecedenceVal2(null);
			serviceCosCriteria.setIpprecedenceVal3(null);
			serviceCosCriteria.setIpprecedenceVal4(null);
			serviceCosCriteria.setIpprecedenceVal5(null);
			serviceCosCriteria.setIpprecedenceVal6(null);
			serviceCosCriteria.setIpprecedenceVal7(null);
			serviceCosCriteria.setIpprecedenceVal8(null);
			serviceCosCriteriaBean.setIpprecedenceVal1(null);
			serviceCosCriteriaBean.setIpprecedenceVal2(null);
			serviceCosCriteriaBean.setIpprecedenceVal3(null);
			serviceCosCriteriaBean.setIpprecedenceVal4(null);
			serviceCosCriteriaBean.setIpprecedenceVal5(null);
			serviceCosCriteriaBean.setIpprecedenceVal6(null);
			serviceCosCriteriaBean.setIpprecedenceVal7(null);
			serviceCosCriteriaBean.setIpprecedenceVal8(null);
		}
		if(dscpStatus == true){
			serviceCosCriteria.setDhcpVal1(null);
			serviceCosCriteria.setDhcpVal2(null);
			serviceCosCriteria.setDhcpVal3(null);
			serviceCosCriteria.setDhcpVal4(null);
			serviceCosCriteria.setDhcpVal5(null);
			serviceCosCriteria.setDhcpVal6(null);
			serviceCosCriteria.setDhcpVal7(null);
			serviceCosCriteria.setDhcpVal8(null);
			serviceCosCriteriaBean.setDhcpVal1(null);
			serviceCosCriteriaBean.setDhcpVal2(null);
			serviceCosCriteriaBean.setDhcpVal3(null);
			serviceCosCriteriaBean.setDhcpVal4(null);
			serviceCosCriteriaBean.setDhcpVal5(null);
			serviceCosCriteriaBean.setDhcpVal6(null);
			serviceCosCriteriaBean.setDhcpVal7(null);
			serviceCosCriteriaBean.setDhcpVal8(null);
		}
	}

	private void setDhcpValues(ServiceCosCriteria serviceCosCriteria, ServiceCosCriteriaBean serviceCosCriteriaBean){
		serviceCosCriteria.setDhcpVal1(serviceCosCriteriaBean.getDhcpVal1());
		serviceCosCriteria.setDhcpVal2(serviceCosCriteriaBean.getDhcpVal2());
		serviceCosCriteria.setDhcpVal3(serviceCosCriteriaBean.getDhcpVal3());
		serviceCosCriteria.setDhcpVal4(serviceCosCriteriaBean.getDhcpVal4());
		serviceCosCriteria.setDhcpVal5(serviceCosCriteriaBean.getDhcpVal5());
		serviceCosCriteria.setDhcpVal6(serviceCosCriteriaBean.getDhcpVal6());
		serviceCosCriteria.setDhcpVal7(serviceCosCriteriaBean.getDhcpVal7());
		serviceCosCriteria.setDhcpVal8(serviceCosCriteriaBean.getDhcpVal8());
	}

	private void setIpprecedenceValues(ServiceCosCriteria serviceCosCriteria, ServiceCosCriteriaBean serviceCosCriteriaBean){
		serviceCosCriteria.setIpprecedenceVal1(serviceCosCriteriaBean.getIpprecedenceVal1());
		serviceCosCriteria.setIpprecedenceVal2(serviceCosCriteriaBean.getIpprecedenceVal2());
		serviceCosCriteria.setIpprecedenceVal3(serviceCosCriteriaBean.getIpprecedenceVal3());
		serviceCosCriteria.setIpprecedenceVal4(serviceCosCriteriaBean.getIpprecedenceVal4());
		serviceCosCriteria.setIpprecedenceVal5(serviceCosCriteriaBean.getIpprecedenceVal5());
		serviceCosCriteria.setIpprecedenceVal6(serviceCosCriteriaBean.getIpprecedenceVal6());
		serviceCosCriteria.setIpprecedenceVal7(serviceCosCriteriaBean.getIpprecedenceVal7());
		serviceCosCriteria.setIpprecedenceVal8(serviceCosCriteriaBean.getIpprecedenceVal8());
	}

	private void updateServiceCosCriteriaDetails(ServiceCosCriteria serviceCosCriteria, ServiceCosCriteriaBean serviceCosCriteriaBean, ServiceQoBean serviceQoBean){

		serviceCosCriteria.setBwBpsunit(serviceCosCriteriaBean.getBwBpsunit());

		if("IP Address".equalsIgnoreCase(serviceCosCriteria.getClassificationCriteria())){

			if("DSCP".equalsIgnoreCase(serviceCosCriteriaBean.getClassificationCriteria())){
				setDhcpValues(serviceCosCriteria, serviceCosCriteriaBean);
			}
			else if("Ipprecedence".equalsIgnoreCase(serviceCosCriteriaBean.getClassificationCriteria())){
				setIpprecedenceValues(serviceCosCriteria, serviceCosCriteriaBean);
			}
			else if("ANY".equalsIgnoreCase(serviceCosCriteriaBean.getClassificationCriteria())){
				setServiceCosValuesToNull(serviceCosCriteria, serviceCosCriteriaBean,true, true);
			}
		}

		else if("IP Address".equalsIgnoreCase(serviceCosCriteriaBean.getClassificationCriteria())){

			if("Ipprecedence".equalsIgnoreCase(serviceCosCriteria.getClassificationCriteria())){
				setServiceCosValuesToNull(serviceCosCriteria, serviceCosCriteriaBean, true, false);
			}
			else if("DSCP".equalsIgnoreCase(serviceCosCriteria.getClassificationCriteria())){
				setServiceCosValuesToNull(serviceCosCriteria, serviceCosCriteriaBean, false, true);
			}
			else if("ANY".equalsIgnoreCase(serviceCosCriteria.getClassificationCriteria())){
				setServiceCosValuesToNull(serviceCosCriteria, serviceCosCriteriaBean, true, true);
			}
		}

		if(Objects.nonNull(serviceCosCriteria) && Objects.nonNull(serviceCosCriteriaBean))
		{
			if (("ipprecedence".equalsIgnoreCase(serviceCosCriteria.getClassificationCriteria()) || "DSCP".equalsIgnoreCase(serviceCosCriteria.getClassificationCriteria()))
					&& "ANY".equalsIgnoreCase(serviceCosCriteriaBean.getClassificationCriteria())) {
				setServiceCosValuesToNull(serviceCosCriteria, serviceCosCriteriaBean, true, true);
			} else if ("ipprecedence".equalsIgnoreCase(serviceCosCriteria.getClassificationCriteria())
					&& "DSCP".equalsIgnoreCase(serviceCosCriteriaBean.getClassificationCriteria())) {
				setServiceCosValuesToNull(serviceCosCriteria, serviceCosCriteriaBean, true, false);
				setDhcpValues(serviceCosCriteria, serviceCosCriteriaBean);
			} else if ("DSCP".equalsIgnoreCase(serviceCosCriteria.getClassificationCriteria())
					&& "ipprecedence".equalsIgnoreCase(serviceCosCriteriaBean.getClassificationCriteria())) {
				setServiceCosValuesToNull(serviceCosCriteria, serviceCosCriteriaBean, false, true);
				setIpprecedenceValues(serviceCosCriteria, serviceCosCriteriaBean);
			} else if ("ANY".equalsIgnoreCase(serviceCosCriteria.getClassificationCriteria())
					&& "Ipprecedence".equalsIgnoreCase(serviceCosCriteriaBean.getClassificationCriteria())) {
				setIpprecedenceValues(serviceCosCriteria, serviceCosCriteriaBean);
			} else if ("ANY".equalsIgnoreCase(serviceCosCriteria.getClassificationCriteria())
					&& "DSCP".equalsIgnoreCase(serviceCosCriteriaBean.getClassificationCriteria())) {
				setDhcpValues(serviceCosCriteria, serviceCosCriteriaBean);
			} else if((serviceCosCriteria.getClassificationCriteria()!=null) && (serviceCosCriteriaBean.getClassificationCriteria()!=null) &&
				(serviceCosCriteria.getClassificationCriteria().equalsIgnoreCase(serviceCosCriteriaBean.getClassificationCriteria()))) {
				setDhcpValues(serviceCosCriteria, serviceCosCriteriaBean);
				setIpprecedenceValues(serviceCosCriteria, serviceCosCriteriaBean);
			}
		}

		serviceCosCriteria.setClassificationCriteria(serviceCosCriteriaBean.getClassificationCriteria());
		serviceCosCriteria.setCosName(serviceCosCriteriaBean.getCosName());
		serviceCosCriteria.setCosPercent(serviceCosCriteriaBean.getCosPercent());

		serviceCosCriteria.setEndDate(serviceCosCriteriaBean.getEndDate());
		serviceCosCriteria.setLastModifiedDate(new Timestamp(new Date().getTime()));
		serviceCosCriteria.setModifiedBy("OPTIMUS_UI");
		if(serviceCosCriteria.getServiceCosId()!=null){
			serviceCosCriteria.setServiceCosId(serviceCosCriteriaBean.getServiceCosId());
		}
		serviceCosCriteria.setStartDate(serviceCosCriteriaBean.getStartDate());

		ServiceQo serviceQo = serviceQoRepository.findById(serviceQoBean.getServiceQosId()).get();
		serviceCosCriteria.setServiceQo(serviceQo);

		serviceCosCriteriaRepository.saveAndFlush(serviceCosCriteria);
	}

	private void updateVpnSolutionDetails(VpnMetatData vpnMetatData, VpnSolutionBean vpnSolutionBean){
		vpnMetatData.setIsUa(vpnSolutionBean.getIsUa());
		vpnMetatData.setVpnName(vpnSolutionBean.getVpnName());
		vpnMetatData.setVpnSolutionName(vpnSolutionBean.getVpnSolutionName());
		if(StringUtils.isNotBlank(vpnSolutionBean.getVpnLegId())) {
			vpnMetatData.setVpnLegId(Integer.parseInt(vpnSolutionBean.getVpnLegId()));
		}
		vpnMetatData.setManagementVpnType1(vpnSolutionBean.getManagementVpnType1());
		vpnMetatData.setManagementVpnType2(vpnSolutionBean.getManagementVpnType2());
		vpnMetatData.setE2eIntegrated(vpnMetatData.isE2eIntegrated());
		vpnMetatData.setIsUa("NA");
		vpnMetatDataRepository.saveAndFlush(vpnMetatData);
	}

	private void updateVrfDetails(Vrf vrf, VrfBean vrfBean){
		vrf.setEndDate(vrfBean.getEndDate());
		vrf.setIsmultivrf(convertBooleanToByte(vrfBean.getIsmultivrf()));
		vrf.setIsvrfLiteEnabled(convertBooleanToByte(vrfBean.getIsvrfLiteEnabled()));
		vrf.setLastModifiedDate(new Timestamp(new Date().getTime()));
		vrf.setMastervrfServiceid(vrfBean.getMastervrfServiceid());
		vrf.setMaxRoutesValue(vrfBean.getMaxRoutesValue());
		vrf.setModifiedBy("OPTIMUS_UI");
		vrf.setStartDate(vrfBean.getStartDate());
		vrf.setThreshold(vrfBean.getThreshold());
		vrf.setVrfId(vrfBean.getVrfId());
		vrf.setVrfName(vrfBean.getVrfName());
		vrf.setWarnOn(vrfBean.getWarnOn());
		vrfRepository.saveAndFlush(vrf);
	}

	private void updatePolicyCriteriaDetails(PolicyCriteria policyCriteria, PolicyCriteriaBean policyCriteriaBean, PolicyType policyType,
											 PolicyTypeBean policyTypeBean, boolean criteriaMappingExists){
		logger.info("Populating new Policy Criteria Record");
    	policyCriteria.setName(policyCriteriaBean.getName());
		policyCriteria.setLastModifiedDate(new Timestamp(new Date().getTime()));
		policyCriteria.setMatchcriteriaNeighbourCommunity(convertBooleanToByte(policyCriteriaBean.getMatchcriteriaNeighbourCommunity()));
		policyCriteria.setMatchcriteriaPrefixlist(convertBooleanToByte(policyCriteriaBean.getMatchcriteriaPrefixlist()));
		policyCriteria.setMatchcriteriaPprefixlistPreprovisioned(convertBooleanToByte(policyCriteriaBean.getMatchCriteriaPrefixListPreprovisioned()));
		policyCriteria.setMatchcriteriaPrefixlistName(policyCriteriaBean.getMatchcriteriaPrefixlistName());
		policyCriteria.setMatchcriteriaProtocol(convertBooleanToByte(policyCriteriaBean.getMatchcriteriaProtocol()));
		policyCriteria.setMatchcriteriaRegexAspath(convertBooleanToByte(policyCriteriaBean.getMatchcriteriaRegexAspath()));
		policyCriteria.setMatchcriteriaRegexAspathAspath(policyCriteriaBean.getMatchCriteriaRegexAsPathAsPath());
		policyCriteria.setMatchcriteriaRegexAspathName(policyCriteriaBean.getMatchcriteriaRegexAspathName());
		policyCriteria.setSetcriteriaAspathPrepend(convertBooleanToByte(policyCriteriaBean.getSetcriteriaAspathPrepend()));
		policyCriteria.setSetcriteriaAspathPrependIndex(policyCriteriaBean.getSetcriteriaAspathPrependIndex());
		policyCriteria.setSetcriteriaLocalPreference(convertBooleanToByte(policyCriteriaBean.getSetcriteriaLocalPreference()));
		policyCriteria.setSetcriteriaLocalpreferenceName(policyCriteriaBean.getSetcriteriaLocalpreferenceName());
		policyCriteria.setSetcriteriaAspathPrependName(policyCriteriaBean.getSetcriteriaAspathPrependName());
		policyCriteria.setSetcriteriaMetric(convertBooleanToByte(policyCriteriaBean.getSetcriteriaMetric()));
		policyCriteria.setSetcriteriaMetricName(policyCriteriaBean.getSetcriteriaMetricName());
		policyCriteria.setSetcriteriaAspathPrependIndex(policyCriteria.getSetcriteriaAspathPrependIndex());
		policyCriteria.setSetcriteriaNeighbourCommunity(convertBooleanToByte(policyCriteriaBean.getSetcriteriaNeighbourCommunity()));
		policyCriteria.setSetcriteriaRegexAspath(convertBooleanToByte(policyCriteriaBean.getSetcriteriaRegexAspath()));
		policyCriteria.setSetcriteriaRegexAspathAspath(policyCriteriaBean.getMatchCriteriaRegexAsPathAsPath());
		policyCriteria.setSetcriteriaRegexAspathName(policyCriteriaBean.getSetcriteriaRegexAspathName());
		policyCriteria.setTermName(policyCriteriaBean.getTermName());
		policyCriteria.setTermSetcriteriaAction(policyCriteriaBean.getTermSetcriteriaAction());
		policyCriteria.setModifiedBy("OPTIMUS_UI");
		policyCriteriaRepository.saveAndFlush(policyCriteria);

		if((policyTypeBean.getInboundIpv4Preprovisioned()==false && policyType.getInboundIpv4Preprovisioned()!=null && policyType.getInboundIpv4Preprovisioned()==(byte)1) ||
				(policyTypeBean.getOutboundIpv4Preprovisioned()==false && policyType.getOutboundIpv4Preprovisioned()!=null && policyType.getOutboundIpv4Preprovisioned()==(byte)1)){
			PolicyTypeCriteriaMapping policyTypeCriteriaMapping = policyTypeCriteriaMappingRepository
					.findByPolicyTypeAndPolicyCriteriaId(policyType, policyCriteria.getPolicyCriteriaId());
			policyTypeCriteriaMapping.setEndDate(new Timestamp(new Date().getTime()));
			policyTypeCriteriaMapping.setLastModifiedDate(new Timestamp(new Date().getTime()));
			policyTypeCriteriaMappingRepository.saveAndFlush(policyTypeCriteriaMapping);
		}
	}

	private void updatePolicyTypeDetails(PolicyType policyType, PolicyTypeBean policyTypeBean){
		logger.info("Inside updatePolicyTypeDetails method");
    	policyType.setIsadditionalpolicytermReqd(convertBooleanToByte(policyTypeBean.getIsadditionalpolicytermReqd()));
		policyType.setIslpinvpnpolicyconfigured(convertBooleanToByte(policyTypeBean.getIslpinvpnpolicyconfigured()));
		policyType.setModifiedBy("OPTIMUS_UI");
		policyType.setIsvprnExportpolicyName(policyTypeBean.getIsvprnExportpolicyName());
		policyType.setIsvprnImportpolicy(convertBooleanToByte(policyTypeBean.getIsvprnImportpolicy()));
		policyType.setIsvprnExportPreprovisioned(convertBooleanToByte(policyTypeBean.getIsvprnExportPreprovisioned()));
		policyType.setIsvprnImportpolicyName(policyTypeBean.getIsvprnImportpolicyName());
		policyType.setIsvprnImportPreprovisioned(convertBooleanToByte(policyTypeBean.getIsvprnImportPreprovisioned()));
		policyType.setLastModifiedDate(new Timestamp(new Date().getTime()));
		policyType.setLocalpreferencev4Vpnpolicy(policyTypeBean.getLocalpreferencev4Vpnpolicy());
		policyType.setLocalpreferencev6Vpnpolicy(policyTypeBean.getLocalpreferencev6Vpnpolicy());
		policyType.setOriginatedefaultFlag(convertBooleanToByte(policyTypeBean.getOriginatedefaultFlag()));
		policyType.setOutboundIpv4PolicyName(policyTypeBean.getOutboundIpv4PolicyName());
		policyType.setOutboundIpv4Preprovisioned(convertBooleanToByte(policyTypeBean.getOutboundIpv4Preprovisioned()));
		policyType.setOutboundIpv6PolicyName(policyTypeBean.getOutboundIpv6PolicyName());
		policyType.setOutboundIpv6Preprovisioned(convertBooleanToByte(policyTypeBean.getOutboundIpv6Preprovisioned()));
		policyType.setOutboundIstandardpolicyv4(convertBooleanToByte(policyTypeBean.getOutboundIstandardpolicyv4()));
		policyType.setOutboundIstandardpolicyv6(convertBooleanToByte(policyTypeBean.getOutboundIstandardpolicyv6()));
		policyType.setOutboundRoutingIpv4Policy(convertBooleanToByte(policyTypeBean.getOutboundRoutingIpv4Policy()));
		policyType.setOutboundRoutingIpv6Policy(convertBooleanToByte(policyTypeBean.getOutboundRoutingIpv6Policy()));
		policyType.setPolicyId(policyTypeBean.getPolicyId());
		policyType.setStartDate(policyTypeBean.getStartDate());
		policyType.setInboundRoutingIpv6Policy(convertBooleanToByte(policyTypeBean.getInboundRoutingIpv6Policy()));
		policyType.setInboundRoutingIpv4Policy(convertBooleanToByte(policyTypeBean.getInboundRoutingIpv4Policy()));
		policyType.setInboundIstandardpolicyv6(convertBooleanToByte(policyTypeBean.getInboundIstandardpolicyv6()));
		policyType.setInboundIstandardpolicyv4(convertBooleanToByte(policyTypeBean.getInboundIstandardpolicyv4()));
		policyType.setInboundIpv6Preprovisioned(convertBooleanToByte(policyTypeBean.getInboundIpv6Preprovisioned()));
		policyType.setInboundIpv6PolicyName(policyTypeBean.getInboundIpv6PolicyName());
		policyType.setInboundIpv4Preprovisioned(convertBooleanToByte(policyTypeBean.getInboundIpv4Preprovisioned()));
		policyType.setInboundIpv4PolicyName(policyTypeBean.getInboundIpv4PolicyName());
		policyType.setEndDate(policyTypeBean.getEndDate());
		policyType.setAludefaultoriginatev6Key(convertBooleanToByte(policyTypeBean.getAludefaultoriginatev6Key()));
		policyType.setAludefaultoriginatev4Key(convertBooleanToByte(policyTypeBean.getAludefaultoriginatev4Key()));
		policyTypeRepository.saveAndFlush(policyType);
		logger.info("Policy Type updated");
	}

	private void updateTopologyDetails(Topology topology, TopologyBean topologyBean){

		topology.setEndDate(topologyBean.getEndDate());
		topology.setLastModifiedDate(new Timestamp(new Date().getTime()));
		topology.setModifiedBy("OPTIMUS_UI");
		topology.setStartDate(topologyBean.getStartDate());
		topology.setTopologyId(topologyBean.getTopologyId());
		topology.setTopologyName(topologyBean.getTopologyName());
		topologyRepository.saveAndFlush(topology);
	}

	private void updateRouterUplinkportDetails(RouterUplinkport routerUplinkport, RouterUplinkportBean routerUplinkportBean){

		routerUplinkport.setStartDate(routerUplinkportBean.getStartDate());
		routerUplinkport.setPhysicalPort2Name(routerUplinkportBean.getPhysicalPort2Name());
		routerUplinkport.setRouterUplinkportId(routerUplinkportBean.getRouterUplinkportId());
		routerUplinkport.setPhysicalPort1Name(routerUplinkportBean.getPhysicalPort1Name());
		routerUplinkport.setLastModifiedDate(new Timestamp(new Date().getTime()));
		routerUplinkport.setModifiedBy("OPTIMUS_UI");
		routerUplinkport.setEndDate(routerUplinkportBean.getEndDate());
		routerUplinkPortRepository.saveAndFlush(routerUplinkport);

	}

	private void updateUniswitchDetails(UniswitchDetail uniswitchDetail, UniswitchDetailBean uniswitchDetailBean){

		uniswitchDetail.setAutonegotiationEnabled(uniswitchDetailBean.getAutonegotiationEnabled());
		uniswitchDetail.setDuplex(uniswitchDetailBean.getDuplex());
		uniswitchDetail.setEndDate(uniswitchDetailBean.getEndDate());
		uniswitchDetail.setHandoff(uniswitchDetailBean.getHandoff());
		uniswitchDetail.setInnerVlan(uniswitchDetailBean.getInnerVlan());
		uniswitchDetail.setHostName(uniswitchDetailBean.getHostName());
		uniswitchDetail.setInterfaceName(uniswitchDetailBean.getInterfaceName());
		uniswitchDetail.setLastModifiedDate(new Timestamp(new Date().getTime()));
		uniswitchDetail.setMaxMacLimit(uniswitchDetailBean.getMaxMacLimit());
		uniswitchDetail.setMediaType(uniswitchDetailBean.getMediaType());
		uniswitchDetail.setMgmtIp(uniswitchDetailBean.getMgmtIp());
		uniswitchDetail.setMode(uniswitchDetailBean.getMode());
		uniswitchDetail.setModifiedBy("OPTIMUS_UI");
		uniswitchDetail.setOuterVlan(uniswitchDetailBean.getOuterVlan());
		uniswitchDetail.setPhysicalPort(uniswitchDetailBean.getPhysicalPort());
		uniswitchDetail.setPortType(uniswitchDetailBean.getPortType());
		uniswitchDetail.setSpeed(uniswitchDetailBean.getSpeed());
		uniswitchDetail.setStartDate(uniswitchDetailBean.getStartDate());
		uniswitchDetail.setSwitchModel(uniswitchDetailBean.getSwitchModel());
		uniswitchDetail.setSyncVlanReqd(convertBooleanToByte(uniswitchDetailBean.getSyncVlanReqd()));
		uniswitchDetail.setUniswitchId(uniswitchDetailBean.getUniswitchId());

		uniswitchDetailRepository.saveAndFlush(uniswitchDetail);

	}

	private void updateEigrpDetails(Eigrp eigrp, EigrpBean eigrpBean){

		eigrp.setEigrpBwKbps(eigrpBean.getEigrpBwKbps());
		eigrp.setEigrpProtocolId(eigrpBean.getEigrpProtocolId());
		eigrp.setEndDate(eigrpBean.getEndDate());
		eigrp.setInterfaceDelay(eigrpBean.getInterfaceDelay());
		eigrp.setIsredistributeConnectedEnabled(convertBooleanToByte(eigrpBean.getIsredistributeConnectedEnabled()));
		eigrp.setIsredistributeStaticEnabled(convertBooleanToByte(eigrpBean.getIsredistributeStaticEnabled()));
		eigrp.setIsroutemapEnabled(convertBooleanToByte(eigrpBean.getIsroutemapEnabled()));
		eigrp.setIsroutemapPreprovisioned(convertBooleanToByte(eigrpBean.getIsroutemapPreprovisioned()));
		eigrp.setLastModifiedDate(new Timestamp(new Date().getTime()));
		eigrp.setLoad(eigrpBean.getLoad());
		eigrp.setLocalAsnumber(eigrpBean.getLocalAsnumber());
		eigrp.setModifiedBy("OPTIMUS_UI");
		eigrp.setMtu(eigrpBean.getMtu());
		eigrp.setRedistributeRoutemapName(eigrpBean.getRedistributeRoutemapName());
		eigrp.setRedistributionDelay(eigrpBean.getRedistributionDelay());
		eigrp.setReliability(eigrpBean.getReliability());
		eigrp.setRemoteAsnumber(eigrpBean.getRemoteAsnumber());
		eigrp.setRemoteCeAsnumber(eigrpBean.getRemoteCeAsnumber());
		eigrp.setSooRequired(convertBooleanToByte(eigrpBean.getSooRequired()));
		eigrp.setStartDate(eigrpBean.getStartDate());

		eigrpRepository.saveAndFlush(eigrp);
	}

	private void updateRipDetails(Rip rip, RipBean ripBean){

		rip.setEndDate(ripBean.getEndDate());
		rip.setGroupName(ripBean.getGroupName());
		rip.setLastModifiedDate(new Timestamp(new Date().getTime()));
		rip.setLocalPreference(ripBean.getLocalPreference());
		rip.setLocalPreferenceV6(ripBean.getLocalPreferenceV6());
		rip.setModifiedBy("OPTIMUS_UI");
		rip.setRipId(ripBean.getRipId());
		rip.setStartDate(ripBean.getStartDate());
		rip.setVersion(ripBean.getVersion());
		ripRepository.saveAndFlush(rip);
	}

	private void updateOspfDetails(Ospf ospf, OspfBean ospfBean){

		ospf.setAreaId(ospfBean.getAreaId());
		ospf.setAuthenticationMode(ospfBean.getAuthenticationMode());
		ospf.setCost(ospfBean.getCost());
		ospf.setDomainId(ospfBean.getDomainId());
		ospf.setEndDate(ospfBean.getEndDate());
		ospf.setIsauthenticationRequired(convertBooleanToByte(ospfBean.getIsauthenticationRequired()));
		ospf.setIsenableShamlink(convertBooleanToByte(ospfBean.getIsenableShamlink()));
		ospf.setIsignoreipOspfMtu(convertBooleanToByte(ospfBean.getIsignoreipOspfMtu()));
		ospf.setIsnetworkP2p(convertBooleanToByte(ospfBean.getIsnetworkP2p()));
		ospf.setIsredistributeConnectedEnabled(convertBooleanToByte(ospfBean.getIsredistributeConnectedEnabled()));
		ospf.setIsredistributeStaticEnabled(convertBooleanToByte(ospfBean.getIsredistributeStaticEnabled()));
		ospf.setIsroutemapEnabled(convertBooleanToByte(ospfBean.getIsroutemapEnabled()));
		ospf.setIsroutemapPreprovisioned(ospfBean.getIsroutemapPreprovisioned());
		ospf.setLastModifiedDate(new Timestamp(new Date().getTime()));
		ospf.setLocalPreference(ospfBean.getLocalPreference());
		ospf.setLocalPreferenceV6(ospfBean.getLocalPreferenceV6());
		ospf.setModifiedBy("OPTIMUS_UI");
		ospf.setOspfId(ospfBean.getOspfId());
		ospf.setOspfNetworkType(ospfBean.getOspfNetworkType());
		ospf.setPassword(ospfBean.getPassword());
		ospf.setProcessId(ospfBean.getProcessId());
		ospf.setRedistributeRoutermapname(ospfBean.getRedistributeRoutermapname());
		ospf.setShamlinkInterfaceName(ospfBean.getShamlinkInterfaceName());
		ospf.setShamlinkLocalAddress(ospfBean.getShamlinkLocalAddress());
		ospf.setShamlinkRemoteAddress(ospfBean.getShamlinkRemoteAddress());
		ospf.setStartDate(ospfBean.getStartDate());
		ospf.setVersion(ospfBean.getVersion());
		ospfRepository.saveAndFlush(ospf);
	}

	private void updateCpeDetails(Cpe cpe, CpeBean cpeBean){

		cpe.setCpeId(cpeBean.getCpeId());
		cpe.setCpeinitConfigparams(convertBooleanToByte(cpeBean.getCpeinitConfigparams()));
		cpe.setEndDate(cpeBean.getEndDate());
		cpe.setHostName(cpeBean.getHostName());
		cpe.setInitLoginpwd(cpeBean.getInitLoginpwd());
		cpe.setInitUsername(cpeBean.getInitUsername());
		cpe.setIsaceconfigurable(convertBooleanToByte(cpeBean.getIsaceconfigurable()));
		cpe.setLastModifiedDate(new Timestamp(new Date().getTime()));
		cpe.setLoopbackInterfaceName(cpeBean.getLoopbackInterfaceName());
		cpe.setMgmtLoopbackV4address(cpeBean.getMgmtLoopbackV4address());
		cpe.setMgmtLoopbackV6address(cpeBean.getMgmtLoopbackV6address());
		cpe.setModifiedBy("OPTIMUS_UI");
		cpe.setNniCpeConfig(convertBooleanToByte(cpeBean.getNniCpeConfig()));
		cpe.setSendInittemplate(convertBooleanToByte(cpeBean.getSendInittemplate()));
		cpe.setServiceId(cpeBean.getServiceId());
		cpe.setSnmpServerCommunity(cpeBean.getSnmpServerCommunity());
		cpe.setStartDate(cpeBean.getStartDate());
		cpe.setUnmanagedCePartnerdeviceWanip(cpeBean.getUnmanagedCePartnerdeviceWanip());
		cpe.setVsatCpeConfig(convertBooleanToByte(cpeBean.getVsatCpeConfig()));
		cpe.setMake(cpeBean.getMake());
		cpe.setModel(cpeBean.getModel());
		cpe.setDeviceId(cpeBean.getDeviceId());

		cpeRepository.saveAndFlush(cpe);

	}

	private void updateRouterDetails(RouterDetail routerDetail, RouterDetailBean routerDetailBean){

		routerDetail.setEndDate(routerDetailBean.getEndDate());
		routerDetail.setIpv4MgmtAddress(routerDetailBean.getIpv4MgmtAddress());
		routerDetail.setIpv6MgmtAddress(routerDetailBean.getIpv6MgmtAddress());
		routerDetail.setLastModifiedDate(new Timestamp(new Date().getTime()));
		routerDetail.setModifiedBy("OPTIMUS_UI");
		routerDetail.setRouterHostname(routerDetailBean.getRouterHostname());
		routerDetail.setRouterId(routerDetailBean.getRouterId());
		routerDetail.setRouterMake(routerDetailBean.getRouterMake());
		routerDetail.setRouterModel(routerDetailBean.getRouterModel());
		routerDetail.setRouterRole(routerDetailBean.getRouterRole());
		routerDetail.setRouterType(routerDetailBean.getRouterType());
		routerDetail.setStartDate(routerDetailBean.getStartDate());

		routerDetailRepository.saveAndFlush(routerDetail);
	}

	private void updateStaticProtocolDetails(StaticProtocol staticProtocol, StaticProtocolBean staticProtocolBean){

		staticProtocol.setEndDate(staticProtocolBean.getEndDate());
		staticProtocol.setIsroutemapEnabled(convertBooleanToByte(staticProtocolBean.getIsroutemapEnabled()));
		staticProtocol.setIsroutemapPreprovisioned(convertBooleanToByte(staticProtocolBean.getIsroutemapPreprovisioned()));
		staticProtocol.setLastModifiedDate(new Timestamp(new Date().getTime()));
		staticProtocol.setLocalPreference(staticProtocolBean.getLocalPreference());
		staticProtocol.setLocalPreferenceV6(staticProtocolBean.getLocalPreferenceV6());
		staticProtocol.setModifiedBy("OPTIMUS_UI");
		staticProtocol.setRedistributeRoutemapIpv4(staticProtocolBean.getRedistributeRoutemapIpv4());
		staticProtocol.setStartDate(staticProtocolBean.getStartDate());
		staticProtocol.setStaticprotocolId(staticProtocolBean.getStaticprotocolId());

		staticProtocolRepository.saveAndFlush(staticProtocol);
	}

	private void updateWanStaticRouteDetails(WanStaticRoute wanStaticRoute, WanStaticRouteBean wanStaticRouteBean, StaticProtocol staticProtocol){
		logger.info("Inside updateWanStaticRouteDetails method");
		wanStaticRoute.setAdvalue(wanStaticRouteBean.getAdvalue());
		wanStaticRoute.setDescription(wanStaticRouteBean.getDescription());
		wanStaticRoute.setGlobal(convertBooleanToByte(wanStaticRouteBean.getGlobal()));
		wanStaticRoute.setIpsubnet(wanStaticRouteBean.getIpsubnet());
		wanStaticRoute.setNextHopid(wanStaticRouteBean.getNextHopid());
		wanStaticRoute.setPopCommunity(wanStaticRouteBean.getPopCommunity());
		wanStaticRoute.setRegionalCommunity(wanStaticRouteBean.getRegionalCommunity());
		wanStaticRoute.setServiceCommunity(wanStaticRouteBean.getServiceCommunity());
		wanStaticRoute.setWanstaticrouteId(wanStaticRouteBean.getWanstaticrouteId());
		wanStaticRoute.setEndDate(wanStaticRouteBean.getEndDate());
		wanStaticRoute.setIsCewan(convertBooleanToByte(wanStaticRouteBean.getIsCewan()));
		wanStaticRoute.setIsCpelanStaticroutes(convertBooleanToByte(wanStaticRouteBean.getIsCpelanStaticroutes()));
		wanStaticRoute.setIsCpewanStaticroutes(convertBooleanToByte(wanStaticRouteBean.getIsCpewanStaticroutes()));
		wanStaticRoute.setIsPewan(convertBooleanToByte(wanStaticRouteBean.getIsPewan()));
		wanStaticRoute.setLastModifiedDate(new Timestamp(new Date().getTime()));
		wanStaticRoute.setModifiedBy("OPTIMUS_UI");
		wanStaticRoute.setStartDate(wanStaticRouteBean.getStartDate());
		wanStaticRoute.setStaticProtocol(staticProtocol);
		wanStaticRouteRepository.saveAndFlush(wanStaticRoute);
	}

	private void updateEthernetInterfaceDetails(EthernetInterface ethernetInterface, EthernetInterfaceBean ethernetInterfaceBean){

		ethernetInterface.setAutonegotiationEnabled(ethernetInterfaceBean.getAutonegotiationEnabled());
		ethernetInterface.setBfdMultiplier(ethernetInterfaceBean.getBfdMultiplier());
		ethernetInterface.setBfdreceiveInterval(ethernetInterfaceBean.getBfdreceiveInterval());
		ethernetInterface.setBfdtransmitInterval(ethernetInterfaceBean.getBfdtransmitInterval());
		ethernetInterface.setDuplex(ethernetInterfaceBean.getDuplex());
		ethernetInterface.setEncapsulation(ethernetInterfaceBean.getEncapsulation());
		ethernetInterface.setEndDate(ethernetInterfaceBean.getEndDate());
		ethernetInterface.setEthernetInterfaceId(ethernetInterfaceBean.getEthernetInterfaceId());
		ethernetInterface.setFraming(ethernetInterfaceBean.getFraming());
		ethernetInterface.setHoldtimeDown(ethernetInterfaceBean.getHoldtimeDown());
		ethernetInterface.setHoldtimeUp(ethernetInterfaceBean.getHoldtimeUp());
		ethernetInterface.setIsbfdEnabled(convertBooleanToByte(ethernetInterfaceBean.getIsbfdEnabled()));
		ethernetInterface.setIshsrpEnabled(convertBooleanToByte(ethernetInterfaceBean.getIshsrpEnabled()));
		ethernetInterface.setIsvrrpEnabled(convertBooleanToByte(ethernetInterfaceBean.getIsvrrpEnabled()));
		ethernetInterface.setModifiedIpv4Address(ethernetInterfaceBean.getModifiedIpv4Address());
		ethernetInterface.setModifiedIpv6Address(ethernetInterfaceBean.getModifiedIpv6Address());
		ethernetInterface.setModifiedSecondaryIpv4Address(ethernetInterfaceBean.getModifiedSecondaryIpv4Address());
		ethernetInterface.setModifiedSecondaryIpv6Address(ethernetInterfaceBean.getModifiedSecondaryIpv6Address());
		ethernetInterface.setInnerVlan(ethernetInterfaceBean.getInnerVlan());
		ethernetInterface.setIpv4Address(ethernetInterfaceBean.getIpv4Address());
		ethernetInterface.setIpv6Address(ethernetInterfaceBean.getIpv6Address());
		ethernetInterface.setLastModifiedDate(new Timestamp(new Date().getTime()));
		ethernetInterface.setMediaType(ethernetInterfaceBean.getMediaType());
		ethernetInterface.setMode(ethernetInterfaceBean.getMode());
		ethernetInterface.setModifiedBy("OPTIMUS_UI");
		ethernetInterface.setInterfaceName(ethernetInterfaceBean.getInterfaceName());
		ethernetInterface.setMtu(ethernetInterfaceBean.getMtu());
		ethernetInterface.setOuterVlan(ethernetInterfaceBean.getOuterVlan());
		ethernetInterface.setPhysicalPort(ethernetInterfaceBean.getPhysicalPort());
		ethernetInterface.setPortType(ethernetInterfaceBean.getPortType());
		ethernetInterface.setQosLoopinPassthrough(ethernetInterfaceBean.getQosLoopinPassthrough());
		ethernetInterface.setSecondaryIpv4Address(ethernetInterfaceBean.getSecondaryIpv4Address());
		ethernetInterface.setSecondaryIpv6Address(ethernetInterfaceBean.getSecondaryIpv6Address());
		ethernetInterface.setSpeed(ethernetInterfaceBean.getSpeed());
		ethernetInterface.setStartDate(ethernetInterfaceBean.getStartDate());
		ethernetInterfaceRepository.saveAndFlush(ethernetInterface);

	}

	private void updateChannelizedSdhInterfaceDetails(ChannelizedSdhInterface channelizedSdhInterface, ChannelizedSdhInterfaceBean channelizedSdhInterfaceBean){

		channelizedSdhInterface.setPhysicalPort(channelizedSdhInterfaceBean.getPhysicalPort());
		channelizedSdhInterface.setBfdreceiveInterval(channelizedSdhInterfaceBean.getBfdreceiveInterval());
		channelizedSdhInterface.set_4Kfirsttime_slot(channelizedSdhInterfaceBean.get_64kFirstTimeSlot());
		channelizedSdhInterface.set_4klasttimeSlot(channelizedSdhInterfaceBean.get_64kLastTimeSlot());
		channelizedSdhInterface.setBfdMultiplier(channelizedSdhInterfaceBean.getBfdMultiplier());
		channelizedSdhInterface.setBfdtransmitInterval(channelizedSdhInterfaceBean.getBfdtransmitInterval());
		channelizedSdhInterface.setChannelGroupNumber(channelizedSdhInterfaceBean.getChannelGroupNumber());
		channelizedSdhInterface.setDlciValue(channelizedSdhInterfaceBean.getDlciValue());
		channelizedSdhInterface.setDowncount(channelizedSdhInterfaceBean.getDowncount());
		channelizedSdhInterface.setEncapsulation(channelizedSdhInterfaceBean.getEncapsulation());
		channelizedSdhInterface.setEndDate(channelizedSdhInterfaceBean.getEndDate());
		channelizedSdhInterface.setFraming(channelizedSdhInterfaceBean.getFraming());
		channelizedSdhInterface.setHoldtimeDown(channelizedSdhInterfaceBean.getHoldtimeDown());
		channelizedSdhInterface.setHoldtimeUp(channelizedSdhInterfaceBean.getHoldtimeUp());
		channelizedSdhInterface.setInterfaceName(channelizedSdhInterfaceBean.getInterfaceName());
		channelizedSdhInterface.setIpv4Address(channelizedSdhInterfaceBean.getIpv4Address());
		channelizedSdhInterface.setIpv6Address(channelizedSdhInterfaceBean.getIpv6Address());
		channelizedSdhInterface.setIsbfdEnabled(convertBooleanToByte(channelizedSdhInterfaceBean.getIsbfdEnabled()));
		channelizedSdhInterface.setModifiedIipv6Address(channelizedSdhInterfaceBean.getModifiedIipv6Address());
		channelizedSdhInterface.setModifiedIpv4Address(channelizedSdhInterfaceBean.getModifiedIpv4Address());
		channelizedSdhInterface.setModifiedSecondaryIpv4Address(channelizedSdhInterfaceBean.getModifiedSecondaryIpv4Address());
		channelizedSdhInterface.setModifiedSecondaryIpv6Address(channelizedSdhInterfaceBean.getModifiedSecondaryIpv6Address());
		channelizedSdhInterface.setIsframed(convertBooleanToByte(channelizedSdhInterfaceBean.getIsframed()));
		channelizedSdhInterface.setIshdlcConfig(convertBooleanToByte(channelizedSdhInterfaceBean.getIshdlcConfig()));
		channelizedSdhInterface.setIshsrpEnabled(convertBooleanToByte(channelizedSdhInterfaceBean.getIshsrpEnabled()));
		channelizedSdhInterface.setIsvrrpEnabled(convertBooleanToByte(channelizedSdhInterfaceBean.getIsvrrpEnabled()));
		channelizedSdhInterface.setJ(channelizedSdhInterfaceBean.getJ());
		channelizedSdhInterface.setKeepalive(channelizedSdhInterfaceBean.getKeepalive());
		channelizedSdhInterface.setKlm(channelizedSdhInterfaceBean.getKlm());
		channelizedSdhInterface.setLastModifiedDate(new Timestamp(new Date().getTime()));
		channelizedSdhInterface.setMode(channelizedSdhInterfaceBean.getMode());
		channelizedSdhInterface.setModifiedBy("OPTIMUS_UI");
		channelizedSdhInterface.setModifiedSecondaryIpv4Address(channelizedSdhInterfaceBean.getModifiedSecondaryIpv4Address());
		channelizedSdhInterface.setModifiedSecondaryIpv6Address(channelizedSdhInterfaceBean.getModifiedSecondaryIpv6Address());
		channelizedSdhInterface.setModifiedIpv4Address(channelizedSdhInterfaceBean.getModifiedIpv4Address());
		channelizedSdhInterface.setModifiedIipv6Address(channelizedSdhInterfaceBean.getModifiedIipv6Address());
		channelizedSdhInterface.setMtu(channelizedSdhInterfaceBean.getMtu());
		channelizedSdhInterface.setPortType(channelizedSdhInterfaceBean.getPortType());
		channelizedSdhInterface.setPosframing(channelizedSdhInterfaceBean.getPosframing());
		channelizedSdhInterface.setSdhInterfaceId(channelizedSdhInterfaceBean.getSdhInterfaceId());
		channelizedSdhInterface.setSecondaryIpv4Address(channelizedSdhInterfaceBean.getSecondaryIpv4Address());
		channelizedSdhInterface.setSecondaryIpv6Address(channelizedSdhInterfaceBean.getSecondaryIpv6Address());
		channelizedSdhInterface.setStartDate(channelizedSdhInterfaceBean.getStartDate());
		channelizedSdhInterface.setUpcount(channelizedSdhInterfaceBean.getUpcount());

		channelizedSdhInterfaceRepository.saveAndFlush(channelizedSdhInterface);

	}

	

	private void updateAclPolicyCriteriaDetails(AclPolicyCriteria aclPolicyCriteria, AclPolicyCriteriaBean aclPolicyCriteriaBean,
												ServiceCosCriteriaBean serviceCosCriteriaBean, boolean status){

		if(aclPolicyCriteria.getAclPolicyId()!=null){
			aclPolicyCriteria.setAclPolicyId(aclPolicyCriteriaBean.getAclPolicyId());
		}
		aclPolicyCriteria.setAction(aclPolicyCriteriaBean.getAction());
		aclPolicyCriteria.setDescription(aclPolicyCriteriaBean.getDescription());
		aclPolicyCriteria.setDestinationAny(convertBooleanToByte(aclPolicyCriteriaBean.getDestinationAny()));

		aclPolicyCriteria.setDestinationCondition(aclPolicyCriteriaBean.getDestinationCondition());

		aclPolicyCriteria.setDestinationPortnumber(aclPolicyCriteriaBean.getDestinationPortnumber());
		aclPolicyCriteria.setDestinationSubnet(aclPolicyCriteriaBean.getDestinationSubnet());
		if(status==true){
			aclPolicyCriteria.setEndDate(new Timestamp(new Date().getTime()));
		}
		else{
			aclPolicyCriteria.setEndDate(aclPolicyCriteriaBean.getEndDate());
		}
		aclPolicyCriteria.setInboundIpv4AclName(aclPolicyCriteriaBean.getInboundIpv4AclName());
		aclPolicyCriteria.setInboundIpv6AclName(aclPolicyCriteriaBean.getInboundIpv6AclName());
		aclPolicyCriteria.setIsinboundaclIpv4Applied(convertBooleanToByte(aclPolicyCriteriaBean.getIsinboundaclIpv4Applied()));
		aclPolicyCriteria.setIsinboundaclIpv4Preprovisioned(convertBooleanToByte(aclPolicyCriteriaBean.getIsinboundaclIpv4Preprovisioned()));
		aclPolicyCriteria.setIsinboundaclIpv6Applied(convertBooleanToByte(aclPolicyCriteriaBean.getIsinboundaclIpv6Applied()));
		aclPolicyCriteria.setIsinboundaclIpv6Preprovisioned(convertBooleanToByte(aclPolicyCriteriaBean.getIsinboundaclIpv6Preprovisioned()));
		aclPolicyCriteria.setIsoutboundaclIpv4Applied(convertBooleanToByte(aclPolicyCriteriaBean.getIsoutboundaclIpv4Applied()));
		aclPolicyCriteria.setIsoutboundaclIpv4Preprovisioned(convertBooleanToByte(aclPolicyCriteriaBean.getIsoutboundaclIpv4Preprovisioned()));
	 	aclPolicyCriteria.setIsoutboundaclIpv6Applied(convertBooleanToByte(aclPolicyCriteriaBean.getIsoutboundaclIpv6Applied()));
	 	aclPolicyCriteria.setIsoutboundaclIpv6Preprovisioned(convertBooleanToByte(aclPolicyCriteriaBean.getIsoutboundaclIpv6Preprovisioned()));
	 	aclPolicyCriteria.setIssvcQosCoscriteriaIpaddrAcl(convertBooleanToByte(aclPolicyCriteriaBean.getIssvcQosCoscriteriaIpaddrAcl()));
	 	aclPolicyCriteria.setIssvcQosCoscriteriaIpaddrAclName(aclPolicyCriteriaBean.getIssvcQosCoscriteriaIpaddrAclName());
	 	aclPolicyCriteria.setIssvcQosCoscriteriaIpaddrPreprovisioned(convertBooleanToByte(aclPolicyCriteriaBean.getIssvcQosCoscriteriaIpaddrPreprovisioned()));
	 	aclPolicyCriteria.setLastModifiedDate(new Timestamp(new Date().getTime()));
	 	aclPolicyCriteria.setModifiedBy("OPTIMUS_UI");
	 	aclPolicyCriteria.setOutboundIpv4AclName(aclPolicyCriteriaBean.getOutboundIpv4AclName());
	 	aclPolicyCriteria.setOutboundIpv6AclName(aclPolicyCriteriaBean.getOutboundIpv6AclName());
	 	aclPolicyCriteria.setProtocol(aclPolicyCriteriaBean.getProtocol());
	 	aclPolicyCriteria.setSequence(aclPolicyCriteriaBean.getSequence());
	 	if(serviceCosCriteriaBean!=null){
	 		Optional<ServiceCosCriteria> oServiceCosCriteria = serviceCosCriteriaRepository.findById(serviceCosCriteriaBean.getServiceCosId());
	 		if(oServiceCosCriteria.isPresent()){
				ServiceCosCriteria serviceCosCriteria = oServiceCosCriteria.get();
				aclPolicyCriteria.setServiceCosCriteria(serviceCosCriteria);
			}
		}
	 	aclPolicyCriteria.setSourceAny(convertBooleanToByte(aclPolicyCriteriaBean.getSourceAny()));
	 	aclPolicyCriteria.setSourceCondition(aclPolicyCriteriaBean.getSourceCondition());
	 	aclPolicyCriteria.setSourcePortnumber(aclPolicyCriteriaBean.getSourcePortnumber());
	 	aclPolicyCriteria.setSourceSubnet(aclPolicyCriteriaBean.getSourceSubnet());
	 	aclPolicyCriteria.setStartDate(aclPolicyCriteriaBean.getStartDate());

		aclPolicyCriteriaRepository.saveAndFlush(aclPolicyCriteria);
	}

	private void updateChannelizedE1SerialInterfaceDetails(ChannelizedE1serialInterface channelizedE1serialInterface, ChannelizedE1serialInterfaceBean channelizedE1serialInterfaceBean){

		channelizedE1serialInterface.setBfdMultiplier(channelizedE1serialInterfaceBean.getBfdMultiplier());
		channelizedE1serialInterface.setBfdreceiveInterval(channelizedE1serialInterfaceBean.getBfdreceiveInterval());
		channelizedE1serialInterface.setBfdtransmitInterval(channelizedE1serialInterfaceBean.getBfdtransmitInterval());
		channelizedE1serialInterface.setChannelGroupNumber(channelizedE1serialInterfaceBean.getChannelGroupNumber());
		channelizedE1serialInterface.setCrcsize(channelizedE1serialInterfaceBean.getCrcsize());
		channelizedE1serialInterface.setDlciValue(channelizedE1serialInterfaceBean.getDlciValue());
		channelizedE1serialInterface.setDowncount(channelizedE1serialInterfaceBean.getDowncount());
		channelizedE1serialInterface.setE1serialInterfaceId(channelizedE1serialInterfaceBean.getE1serialInterfaceId());
		channelizedE1serialInterface.setEncapsulation(channelizedE1serialInterfaceBean.getEncapsulation());
		channelizedE1serialInterface.setEndDate(channelizedE1serialInterfaceBean.getEndDate());
		channelizedE1serialInterface.setFirsttimeSlot(channelizedE1serialInterfaceBean.getFirsttimeSlot());
		channelizedE1serialInterface.setFraming(channelizedE1serialInterfaceBean.getFraming());
		channelizedE1serialInterface.setHoldtimeUp(channelizedE1serialInterfaceBean.getHoldtimeUp());
		channelizedE1serialInterface.setHoldtimeDown(channelizedE1serialInterfaceBean.getHoldtimeDown());
		channelizedE1serialInterface.setInterfaceName(channelizedE1serialInterfaceBean.getInterfaceName());
		channelizedE1serialInterface.setIpv4Address(channelizedE1serialInterfaceBean.getIpv4Address());
		channelizedE1serialInterface.setIpv6Address(channelizedE1serialInterfaceBean.getIpv6Address());
		channelizedE1serialInterface.setIsbfdEnabled(convertBooleanToByte(channelizedE1serialInterfaceBean.getIsbfdEnabled()));
		channelizedE1serialInterface.setIscrcforenabled(convertBooleanToByte(channelizedE1serialInterfaceBean.getIscrcforenabled()));
		channelizedE1serialInterface.setIsframed(convertBooleanToByte(channelizedE1serialInterfaceBean.getIsframed()));
		channelizedE1serialInterface.setIshdlcConfig(convertBooleanToByte(channelizedE1serialInterfaceBean.getIshdlcConfig()));
		channelizedE1serialInterface.setIshsrpEnabled(convertBooleanToByte(channelizedE1serialInterfaceBean.getIshsrpEnabled()));
		channelizedE1serialInterface.setIsvrrpEnabled(convertBooleanToByte(channelizedE1serialInterfaceBean.getIsvrrpEnabled()));
		channelizedE1serialInterface.setKeepalive(channelizedE1serialInterfaceBean.getKeepalive());
		channelizedE1serialInterface.setLastModifiedDate(new Timestamp(new Date().getTime()));
		channelizedE1serialInterface.setLasttimeSlot(channelizedE1serialInterfaceBean.getLasttimeSlot());
		channelizedE1serialInterface.setMode(channelizedE1serialInterfaceBean.getMode());
		channelizedE1serialInterface.setModifiedBy("OPTIMUS_UI");
		channelizedE1serialInterface.setModifiedIpv4Address(channelizedE1serialInterfaceBean.getModifiedIpv4Address());
		channelizedE1serialInterface.setModifiedIpv6Address(channelizedE1serialInterfaceBean.getModifiedIpv6Address());
		channelizedE1serialInterface.setModifiedSecondaryIpv4Address(channelizedE1serialInterfaceBean.getModifiedSecondaryIpv4Address());
		channelizedE1serialInterface.setModifiedSecondaryIpv6Address(channelizedE1serialInterfaceBean.getModifiedSecondaryIpv6Address());
		channelizedE1serialInterface.setMtu(channelizedE1serialInterfaceBean.getMtu());
		channelizedE1serialInterface.setPhysicalPort(channelizedE1serialInterfaceBean.getPhysicalPort());
		channelizedE1serialInterface.setPortType(channelizedE1serialInterfaceBean.getPortType());
		channelizedE1serialInterface.setSecondaryIpv4Address(channelizedE1serialInterfaceBean.getSecondaryIpv4Address());
		channelizedE1serialInterface.setSecondaryIpv6Address(channelizedE1serialInterfaceBean.getSecondaryIpv6Address());
		channelizedE1serialInterface.setStartDate(channelizedE1serialInterfaceBean.getStartDate());
		channelizedE1serialInterface.setUpcount(channelizedE1serialInterfaceBean.getUpcount());

		channelizedE1serialInterfaceRepository.saveAndFlush(channelizedE1serialInterface);
	}

	private void updateBgpDetails(Bgp bgp, BgpBean bgpBean){
		logger.info("Inside updateBgpDetails method");
		bgp.setAluBackupPath(bgpBean.getAluBackupPath());
		bgp.setBgpPeerName(bgpBean.getAluBgpPeerName());
		bgp.setAluDisableBgpPeerGrpExtCommunity(convertBooleanToByte(bgpBean.getAluDisableBgpPeerGrpExtCommunity()));
		bgp.setAluKeepalive(bgpBean.getAluKeepalive());
		bgp.setAsoOverride(convertBooleanToByte(bgpBean.getAsoOverride()));
		bgp.setAsPath(bgpBean.getAsPath());
		bgp.setAuthenticationMode(bgpBean.getAuthenticationMode());
		bgp.setBgpId(bgpBean.getBgpId());
		bgp.setIsbgpMultihopReqd(convertBooleanToByte(bgpBean.getBgpMultihopReqd()));
		bgp.setBgpneighbourinboundv4routermapname(bgpBean.getBgpneighbourinboundv4routermapname());
		bgp.setBgpneighbourinboundv6routermapname(bgpBean.getBgpneighbourinboundv6routermapname());
		bgp.setEndDate(bgpBean.getEndDate());
		bgp.setHelloTime(bgpBean.getHelloTime());
		bgp.setHoldTime(bgpBean.getHoldTime());
		bgp.setIsauthenticationRequired(convertBooleanToByte(bgpBean.getIsauthenticationRequired()));
		bgp.setSplitHorizon(convertBooleanToByte(bgpBean.getSplitHorizon()));
		bgp.setIsbgpNeighbourInboundv4RoutemapEnabled(convertBooleanToByte(bgpBean.getIsbgpNeighbourInboundv4RoutemapEnabled()));
		bgp.setIsbgpNeighbourinboundv4RoutemapPreprovisioned(
				convertBooleanToByte(bgpBean.getIsbgpNeighbourinboundv4RoutemapPreprovisioned()));
		bgp.setIsbgpNeighbourInboundv6RoutemapEnabled(convertBooleanToByte(bgpBean.getIsbgpNeighbourInboundv6RoutemapEnabled()));
		bgp.setIsbgpNeighbourinboundv6RoutemapPreprovisioned(
				convertBooleanToByte(bgpBean.getIsbgpNeighbourinboundv6RoutemapPreprovisioned()));
		bgp.setIsebgpMultihopReqd(convertBooleanToByte(bgpBean.getIsebgpMultihopReqd()));
		bgp.setIsmultihopTtl(bgpBean.getIsmultihopTtl());
		bgp.setIsrtbh(convertBooleanToByte(bgpBean.getIsrtbh()));
		bgp.setLastModifiedDate(new Timestamp(new Date().getTime()));
		bgp.setLocalAsNumber(bgpBean.getLocalAsNumber());
		bgp.setLocalPreference(bgpBean.getLocalPreference());
		bgp.setLocalUpdateSourceInterface(bgpBean.getLocalUpdateSourceInterface());
		bgp.setLocalUpdateSourceIpv4Address(bgpBean.getLocalUpdateSourceIpv4Address());
		bgp.setLocalUpdateSourceIpv6Address(bgpBean.getLocalUpdateSourceIpv6Address());
		bgp.setMaxPrefix(bgpBean.getMaxPrefix());
		bgp.setMaxPrefixThreshold(bgpBean.getMaxPrefixThreshold());
		bgp.setMedValue(bgpBean.getMedValue());
		bgp.setModifiedBy("OPTIMUS_UI");
		bgp.setNeighborOn(bgpBean.getNeighborOn());
		bgp.setNeighbourCommunity(bgpBean.getNeighbourCommunity());
		bgp.setNeighbourshutdownRequired(convertBooleanToByte(bgpBean.getNeighbourshutdownRequired()));
		bgp.setPassword(bgpBean.getPassword());
		bgp.setPeerType(bgpBean.getPeerType());
		bgp.setRedistributeConnectedEnabled(convertBooleanToByte(bgpBean.getRedistributeConnectedEnabled()));
		bgp.setRedistributeStaticEnabled(convertBooleanToByte(bgpBean.getRedistributeStaticEnabled()));
		bgp.setRemoteAsNumber(StringUtils.isNotBlank(bgpBean.getRemoteAsNumber()) ? Integer.parseInt(bgpBean.getRemoteAsNumber()) : null);
		bgp.setRemoteCeAsnumber(bgpBean.getRemoteCeAsnumber());
		bgp.setRemoteIpv4Address(bgpBean.getRemoteIpv4Address());
		bgp.setRemoteIpv6Address(bgpBean.getRemoteIpv6Address());
		bgp.setRemoteUpdateSourceInterface(bgpBean.getRemoteUpdateSourceInterface());
		bgp.setRemoteUpdateSourceIpv4Address(bgpBean.getRemoteUpdateSourceIpv4Address());
		bgp.setRemoteUpdateSourceIpv6Address(bgpBean.getRemoteUpdateSourceIpv6Address());
		bgp.setRoutesExchanged(bgpBean.getRoutesExchanged());
		bgp.setRtbhOptions(bgpBean.getRtbhOptions());
		bgp.setSooRequired(convertBooleanToByte(bgpBean.getSsoRequired()));
		bgp.setSplitHorizon(convertBooleanToByte(bgpBean.getSplitHorizon()));							//
		bgp.setStartDate(bgpBean.getStartDate());
		bgp.setV6LocalPreference(bgpBean.getV6LocalPreference());
		bgpRepository.saveAndFlush(bgp);
	}

	private void updatePolicyTypeCriteriaMappingDetails(PolicyTypeCriteriaMapping policyTypeCriteriaMapping, PolicyTypeCriteriaMappingBean policyTypeCriteriaMappingBean) {

		policyTypeCriteriaMapping.setEndDate(policyTypeCriteriaMappingBean.getEndDate());
		policyTypeCriteriaMapping.setLastModifiedDate(new Timestamp(new Date().getTime()));
		policyTypeCriteriaMapping.setModifiedBy("OPTIMUS_UI");
		policyTypeCriteriaMapping.setPolicyCriteriaId(policyTypeCriteriaMappingBean.getPolicyCriteriaId());
		policyTypeCriteriaMapping.setPolicyTypeCriteriaId(policyTypeCriteriaMappingBean.getPolicyTypeCriteriaId());
		policyTypeCriteriaMapping.setStartDate(policyTypeCriteriaMappingBean.getStartDate());
		policyTypeCriteriaMapping.setVersion(policyTypeCriteriaMappingBean.getVersion());

		policyTypeCriteriaMappingRepository.saveAndFlush(policyTypeCriteriaMapping);
	}

	private void updateHsrpVrrpProtocolDetails(HsrpVrrpProtocol hsrpVrrpProtocol, HsrpVrrpProtocolBean hsrpVrrpProtocolBean){

		hsrpVrrpProtocol.setEndDate(hsrpVrrpProtocolBean.getEndDate());
		hsrpVrrpProtocol.setHelloValue(hsrpVrrpProtocolBean.getHelloValue());
		hsrpVrrpProtocol.setHoldValue(hsrpVrrpProtocolBean.getHoldValue());
		hsrpVrrpProtocol.setHsrpVrrpId(hsrpVrrpProtocolBean.getHsrpVrrpId());
		hsrpVrrpProtocol.setLastModifiedDate(new Timestamp(new Date().getTime()));
		hsrpVrrpProtocol.setModifiedBy("OPTIMUS_UI");
		hsrpVrrpProtocol.setPriority(hsrpVrrpProtocolBean.getPriority());
		hsrpVrrpProtocol.setRole(hsrpVrrpProtocolBean.getRole());
		hsrpVrrpProtocol.setStartDate(hsrpVrrpProtocolBean.getStartDate());
		hsrpVrrpProtocol.setTimerUnit(hsrpVrrpProtocolBean.getTimerUnit());
		hsrpVrrpProtocol.setVirtualIpv4Address(hsrpVrrpProtocolBean.getVirtualIpv4Address());
		hsrpVrrpProtocol.setVirtualIpv6Address(hsrpVrrpProtocolBean.getVirtualIpv6Address());
		hsrpVrrpProtocolRepository.saveAndFlush(hsrpVrrpProtocol);
	}

	private void updatePrefixListConfigDetails(PrefixlistConfig prefixlistConfig, PrefixlistConfigBean prefixlistConfigBean, PolicyCriteria policyCriteria){
		prefixlistConfig.setAction(prefixlistConfigBean.getAction());
		prefixlistConfig.setEndDate(prefixlistConfigBean.getEndDate());
		prefixlistConfig.setGeValue(prefixlistConfigBean.getGeValue());
		prefixlistConfig.setLastModifiedDate(new Timestamp(new Date().getTime()));
		prefixlistConfig.setLeValue(prefixlistConfigBean.getLeValue());
		prefixlistConfig.setModifiedBy("OPTIMUS_UI");
		prefixlistConfig.setNetworkPrefix(prefixlistConfigBean.getNetworkPrefix());
		prefixlistConfig.setStartDate(prefixlistConfigBean.getStartDate());
		prefixlistConfig.setPolicyCriteria(policyCriteria);
		prefixlistConfigRepository.saveAndFlush(prefixlistConfig);
	}

	private void updateNeighbourCommunityConfigDetails(NeighbourCommunityConfig neighbourCommunityConfig, NeighbourCommunityConfigBean neighbourCommunityConfigBean,
													   PolicyCriteria policyCriteria){
		neighbourCommunityConfig.setAction(neighbourCommunityConfigBean.getAction());
		neighbourCommunityConfig.setCommunity(neighbourCommunityConfigBean.getCommunity());
		neighbourCommunityConfig.setEndDate(neighbourCommunityConfigBean.getEndDate());
		neighbourCommunityConfig.setIscommunityExtended(convertBooleanToByte(neighbourCommunityConfigBean.getIscommunityExtended()));
		neighbourCommunityConfig.setIspreprovisioned(convertBooleanToByte(neighbourCommunityConfigBean.getIspreprovisioned()));
		neighbourCommunityConfig.setLastModifiedDate(new Timestamp(new Date().getTime()));
		neighbourCommunityConfig.setModifiedBy("OPTIMUS_UI");
		neighbourCommunityConfig.setName(neighbourCommunityConfigBean.getName());
		neighbourCommunityConfig.setNeighbourCommunityId(neighbourCommunityConfigBean.getNeighbourCommunityId());
		neighbourCommunityConfig.setOption(neighbourCommunityConfigBean.getOption());
		neighbourCommunityConfig.setStartDate(neighbourCommunityConfigBean.getStartDate());
		neighbourCommunityConfig.setType(neighbourCommunityConfigBean.getType());
		if(Objects.nonNull(policyCriteria))
			neighbourCommunityConfig.setPolicyCriteria(policyCriteria);
		neighbourCommunityConfigRepository.saveAndFlush(neighbourCommunityConfig);
	}

	private void updateRegexAsPathConfigDetails(RegexAspathConfig regexAspathConfig, RegexAspathConfigBean regexAspathConfigBean, PolicyCriteria policyCriteria){
		regexAspathConfig.setAction(regexAspathConfigBean.getAction());
		regexAspathConfig.setAsPath(regexAspathConfigBean.getAsPath());
		regexAspathConfig.setEndDate(regexAspathConfigBean.getEndDate());
		regexAspathConfig.setLastModifiedDate(new Timestamp(new Date().getTime()));
		regexAspathConfig.setModifiedBy("OPTIMUS_UI");
		regexAspathConfig.setName(regexAspathConfigBean.getName());
		regexAspathConfig.setPolicyCriteria(policyCriteria);
		regexAspathConfigRepository.saveAndFlush(regexAspathConfig);
	}

	private void updateVlanQosProfileDetails(VlanQosProfile vlanQosProfile, VlanQosProfileBean vlanQosProfileBean) {
		vlanQosProfile.setVlanQosProfileId(vlanQosProfileBean.getVlanQosProfileId());
		vlanQosProfile.setDownstreamQosProfile(vlanQosProfileBean.getDownstreamQosProfile());
		vlanQosProfile.setEndDate(vlanQosProfileBean.getEndDate());
		vlanQosProfile.setLastModifiedDate(new Timestamp(new Date().getTime()));
		vlanQosProfile.setModifiedBy("OPTIMUS_UI");
		vlanQosProfile.setStartDate(vlanQosProfileBean.getStartDate());
		vlanQosProfile.setUpstreamQosProfile(vlanQosProfileBean.getUpstreamQosProfile());
		vlanQosProfile.setVlanId(vlanQosProfileBean.getVlanId());
		vlanQosProfileRepository.saveAndFlush(vlanQosProfile);

	}

	private void updateRadwinLastmileDetails(RadwinLastmile radwinLastmile, RadwinLastmileBean radwinLastmileBean) {
		radwinLastmile.setRadwinLastmileId(radwinLastmileBean.getRadwinLastmileId());
		radwinLastmile.setAllowedVlanid(radwinLastmileBean.getAllowedVlanid());
		radwinLastmile.setBtsIp(radwinLastmileBean.getBtsIp());
		radwinLastmile.setBtsName(radwinLastmileBean.getBtsName());
		radwinLastmile.setCustomerLocation(radwinLastmileBean.getCustomerLocation());
		radwinLastmile.setDataVlanPriority(radwinLastmileBean.getDataVlanPriority());
		radwinLastmile.setDataVlanid(radwinLastmileBean.getDataVlanid());
		radwinLastmile.setEndDate(radwinLastmileBean.getEndDate());
		radwinLastmile.setEthernet_Port_Config(radwinLastmileBean.getEthernet_Port_Config());
		radwinLastmile.setFrequency(radwinLastmileBean.getFrequency());
		radwinLastmile.setGatewayIp(radwinLastmileBean.getGatewayIp());
		radwinLastmile.setHsuEgressTraffic(radwinLastmileBean.getHsuEgressTraffic());
		radwinLastmile.setHsuIngressTraffic(radwinLastmileBean.getHsuIngressTraffic());
		radwinLastmile.setHsuIp(radwinLastmileBean.getHsuIp());
		radwinLastmile.setHsuMacAddr(radwinLastmileBean.getHsuMacAddr());
		if(Objects.nonNull(radwinLastmileBean.getHsuMacAddr()) && !radwinLastmileBean.getHsuMacAddr().isEmpty()){
			radwinLastmile.setHsuMacAddr(radwinLastmileBean.getHsuMacAddr().toLowerCase()); }
		radwinLastmile.setHsuSubnet(radwinLastmileBean.getHsuSubnet());
		radwinLastmile.setLastModifiedDate(new Timestamp(new Date().getTime()));
		radwinLastmile.setMgmtVlanid(radwinLastmileBean.getMgmtVlanid());
		radwinLastmile.setMirDl(radwinLastmileBean.getMirDl());
		radwinLastmile.setMirUl(radwinLastmileBean.getMirUl());
		radwinLastmile.setModifiedBy("OPTIMUS_UI");
		radwinLastmile.setNtpOffset(radwinLastmileBean.getNtpOffset());
		radwinLastmile.setNtpServerIp(radwinLastmileBean.getNtpServerIp());
		radwinLastmile.setProtocolSnmp(radwinLastmileBean.getProtocolSnmp());
		radwinLastmile.setProtocolTelnet(radwinLastmileBean.getProtocolTelnet());
		radwinLastmile.setProtocolWebinterface(radwinLastmileBean.getProtocolWebinterface());
		radwinLastmile.setReqd_tx_power(radwinLastmileBean.getReqd_tx_power());
		radwinLastmile.setSectorId(radwinLastmileBean.getSectorId());
		radwinLastmile.setSiteContact(radwinLastmileBean.getSiteContact());
		radwinLastmile.setSiteLat(radwinLastmileBean.getSiteLat());
		radwinLastmile.setSiteLong(radwinLastmileBean.getSiteLong());
		radwinLastmile.setSiteName(radwinLastmileBean.getSiteName());
		radwinLastmile.setStartDate(radwinLastmileBean.getStartDate());
		radwinLastmile.setUntagVlanId(radwinLastmileBean.getUntagVlanId());
		radwinLastmile.setVlanMode(radwinLastmileBean.getVlanMode());
		radwinLastmileRepository.saveAndFlush(radwinLastmile);

	}

	private void updateWimaxLastmileDetails(WimaxLastmile wimaxLastmile, WimaxLastmileBean wimaxLastmileBean) {
		wimaxLastmile.setWimaxLastmileId(wimaxLastmileBean.getWimaxLastmileId());
		wimaxLastmile.setBtsIp(wimaxLastmileBean.getBtsIp());
		wimaxLastmile.setBtsName(wimaxLastmileBean.getBtsName());
		wimaxLastmile.setCuststaticWanip(wimaxLastmileBean.getCuststaticWanip());
		wimaxLastmile.setCuststaticWanipCustprovided(wimaxLastmileBean.getCuststaticWanipCustprovided());
		wimaxLastmile.setCuststaticWanipGateway(wimaxLastmileBean.getCuststaticWanipGateway());
		wimaxLastmile.setCuststaticWanipGatewayCustprovided(wimaxLastmileBean.getCuststaticWanipGatewayCustprovided());
		wimaxLastmile.setCuststaticWanipMask(wimaxLastmileBean.getCuststaticWanipMask());
		wimaxLastmile.setCuststaticWanipMaskCustprovided(wimaxLastmileBean.getCuststaticWanipMaskCustprovided());
		wimaxLastmile.setDescription1(wimaxLastmileBean.getDescription1());
		wimaxLastmile.setDescription2(wimaxLastmileBean.getDescription2());
		wimaxLastmile.setEndDate(wimaxLastmileBean.getEndDate());
		wimaxLastmile.setGatemgmtipCustprovided(wimaxLastmileBean.getGatemgmtipCustprovided());
		wimaxLastmile.setGatewayMgmtIp(wimaxLastmileBean.getGatewayMgmtIp());
		wimaxLastmile.setHomeRegion(wimaxLastmileBean.getHomeRegion());
		wimaxLastmile.setLastModifiedDate(new Timestamp(new Date().getTime()));
		wimaxLastmile.setModifiedBy("OPTIMUS_UI");
		wimaxLastmile.setPortSpeed(wimaxLastmileBean.getPortSpeed());
		wimaxLastmile.setPortSpeedUnit(wimaxLastmileBean.getPortSpeedUnit());
		wimaxLastmile.setProvisioningMode(wimaxLastmileBean.getProvisioningMode());
		wimaxLastmile.setSsvlanTagging(wimaxLastmileBean.getSsvlanTagging());
		wimaxLastmile.setStartDate(wimaxLastmileBean.getStartDate());
		wimaxLastmile.setSuMgmtIp(wimaxLastmileBean.getSuMgmtIp());
		wimaxLastmile.setSuMgmtSubnet(wimaxLastmileBean.getSuMgmtSubnet());
		wimaxLastmile.setSumacAddress(wimaxLastmileBean.getSumacAddress());
		wimaxLastmile.setSumgmtipCustprovided(wimaxLastmileBean.getSumgmtipCustprovided());
		wimaxLastmile.setSumgmtsubnetCustprovided(wimaxLastmileBean.getSumgmtsubnetCustprovided());
		wimaxLastmile.setUniqueName(wimaxLastmileBean.getUniqueName());
		wimaxLastmileRepository.saveAndFlush(wimaxLastmile);

	}

	private void updateCambiumLastmileDetails(CambiumLastmile cambiumLastmile,CambiumLastmileBean cambiumLastmileBean) {
		cambiumLastmile.setAuthenticationKey(cambiumLastmileBean.getAuthenticationKey());
		cambiumLastmile.setBsIp(cambiumLastmileBean.getBsIp());
		cambiumLastmile.setBsName(cambiumLastmileBean.getBsName());
		cambiumLastmile.setBwDownlinkSustainedRate(cambiumLastmileBean.getBwDownlinkSustainedRate());
		cambiumLastmile.setBwUplinkSustainedRate(cambiumLastmileBean.getBwDownlinkSustainedRate());
		cambiumLastmile.setColorCode1(cambiumLastmileBean.getColorCode1());
		cambiumLastmile.setCustomRadioFrequencyList(cambiumLastmileBean.getCustomRadioFrequencyList());
		cambiumLastmile.setDefaultPortVid(cambiumLastmileBean.getDefaultPortVid());
		cambiumLastmile.setDeviceType(cambiumLastmileBean.getDeviceType());
		cambiumLastmile.setDownlinkBurstAllocation(cambiumLastmileBean.getDownlinkBurstAllocation());
		cambiumLastmile.setEndDate(cambiumLastmileBean.getEndDate());
		cambiumLastmile.setEnforceAuthentication(cambiumLastmileBean.getEnforceAuthentication());
		cambiumLastmile.setFrameTimingPulseGated("ENABLE");
		cambiumLastmile.setHipriorityChannel(cambiumLastmileBean.getHipriorityChannel());
		cambiumLastmile.setHipriorityDownlinkCir(cambiumLastmileBean.getHipriorityDownlinkCir());
		cambiumLastmile.setHipriorityUplinkCir(cambiumLastmileBean.getHipriorityUplinkCir());
		cambiumLastmile.setHomeRegion(cambiumLastmileBean.getHomeRegion());
		if (cambiumLastmileBean.getInstallationColorCode() == 1) {
			cambiumLastmile.setInstallationColorCode("Enable");
		} else {
			cambiumLastmile.setInstallationColorCode("Disable");
		}
		cambiumLastmile.setLastModifiedDate(new Timestamp(new Date().getTime()));
		cambiumLastmile.setLatitudeSettings(cambiumLastmileBean.getLatitudeSettings());
		cambiumLastmile.setLinkSpeed(cambiumLastmileBean.getLinkSpeed());
		cambiumLastmile.setLongitudeSettings(cambiumLastmileBean.getLongitudeSettings());
		cambiumLastmile.setLowpriorityDownlinkCir(cambiumLastmileBean.getLowpriorityDownlinkCir());
		cambiumLastmile.setLowpriorityUplinkCir(cambiumLastmileBean.getLowpriorityUplinkCir());
		cambiumLastmile.setMappedVid2(StringUtils.isNotBlank(cambiumLastmileBean.getMappedVid2()) ? Integer.parseInt(cambiumLastmileBean.getMappedVid2()) : null);
		cambiumLastmile.setMgmtIpForSsSu(cambiumLastmileBean.getMgmtIpForSsSu());
		cambiumLastmile.setMgmtIpGateway(cambiumLastmileBean.getMgmtIpGateway());
		cambiumLastmile.setMgmtSubnetForSsSu(cambiumLastmileBean.getMgmtSubnetForSsSu());
		cambiumLastmile.setMgmtVid(cambiumLastmileBean.getMgmtVid());
		cambiumLastmile.setModifiedBy("OPTIMUS_UI");
		cambiumLastmile.setPortSpeed(cambiumLastmileBean.getPortSpeed());
		cambiumLastmile.setPortSpeedUnit(cambiumLastmileBean.getPortSpeedUnit());
		cambiumLastmile.setProviderVid(StringUtils.isNotBlank(cambiumLastmileBean.getProviderVid()) ? Integer.parseInt(cambiumLastmileBean.getProviderVid()) : null);
		cambiumLastmile.setRealm(cambiumLastmileBean.getRealm());
		cambiumLastmile.setRegion(cambiumLastmileBean.getRegion());
		cambiumLastmile.setRegionCode(cambiumLastmileBean.getRegionCode());
		cambiumLastmile.setSiteContact(cambiumLastmileBean.getSiteContact());
		cambiumLastmile.setSiteLocation(cambiumLastmileBean.getSiteLocation());
		cambiumLastmile.setSiteName(cambiumLastmileBean.getSiteName());
		cambiumLastmile.setSmHeight(cambiumLastmileBean.getSmHeight());
		cambiumLastmile.setStartDate(cambiumLastmileBean.getStartDate());
		if(Objects.nonNull(cambiumLastmileBean.getSuMacAddress()) && !cambiumLastmileBean.getSuMacAddress().isEmpty()){
			cambiumLastmile.setSuMacAddress(cambiumLastmileBean.getSuMacAddress().toLowerCase());
		}
		cambiumLastmile.setUplinkBurstAllocation(cambiumLastmileBean.getUplinkBurstAllocation());
		cambiumLastmileRepository.saveAndFlush(cambiumLastmile);

	}

	private void updateLmComponentDetails(LmComponent lmComponent, LmComponentBean lmComponentBean) {
		lmComponent.setLmComponentId(lmComponentBean.getLmComponentId());
		lmComponent.setLmOnwlProvider(lmComponentBean.getLmOnwlProvider());
		lmComponent.setModifiedBy("OPTIMUS_UI");
		lmComponent.setRemarks(lmComponentBean.getRemarks());
		lmComponent.setVersion(lmComponentBean.getVersion());
		lmComponent.setStartDate(lmComponentBean.getStartDate());
		lmComponent.setEndDate(lmComponentBean.getEndDate());
		lmComponent.setLastModifiedDate(new Timestamp(new Date().getTime()));
		lmComponentRepository.saveAndFlush(lmComponent);

	}

	private void saveAluSchedulerPolicies(ServiceDetailBean serviceDetailBean){
		logger.info("Inside saveAluSchedulerPolicies method");
		if(!CollectionUtils.isEmpty(serviceDetailBean.getAluSchedulerPolicyBeans())){
			serviceDetailBean.getAluSchedulerPolicyBeans().forEach(aluSchedulerPolicyBean -> {
				if(aluSchedulerPolicyBean.isEdited()){
					Optional<AluSchedulerPolicy> oAluSchedulerPolicy = aluSchedulerPolicyRepository.findById(aluSchedulerPolicyBean.getAluSchedulerPolicyId());
					if(oAluSchedulerPolicy.isPresent()){
						AluSchedulerPolicy aluSchedulerPolicy = oAluSchedulerPolicy.get();
						updateAluSchedulerPolicy(aluSchedulerPolicy,aluSchedulerPolicyBean);
					}
					aluSchedulerPolicyBean.setEdited(false);
					aluSchedulerPolicyBean.setLastModifiedDate(new Timestamp(new Date().getTime()));
					aluSchedulerPolicyBean.setModifiedBy("OPTIMUS_UI");
				}
			});
		}
		logger.info("Exiting saveAluSchedulerPolicies method");
	}

	private void savePolicyTypeCriteriaMappings(PolicyTypeCriteriaMappingBean policyTypeCriteriaMappingBean){

		if(policyTypeCriteriaMappingBean.isEdited()){

			Optional<PolicyTypeCriteriaMapping> oPolicyTypeCriteriaMapping = policyTypeCriteriaMappingRepository
					.findById(policyTypeCriteriaMappingBean.getPolicyTypeCriteriaId());

			if(oPolicyTypeCriteriaMapping.isPresent()){

				PolicyTypeCriteriaMapping policyTypeCriteriaMapping = oPolicyTypeCriteriaMapping.get();

				updatePolicyTypeCriteriaMappingDetails(policyTypeCriteriaMapping, policyTypeCriteriaMappingBean);
			}

			policyTypeCriteriaMappingBean.setEdited(false);
			policyTypeCriteriaMappingBean.setLastModifiedDate(new Timestamp(new Date().getTime()));
			policyTypeCriteriaMappingBean.setModifiedBy("OPTIMUS_UI");

		}

	}

	private void saveAclPolicyCriterias(AclPolicyCriteriaBean aclPolicyCriteriaBean,
										ServiceCosCriteriaBean serviceCosCriteriaBean, boolean status){
		if(aclPolicyCriteriaBean.isEdited()){
			// New Acl Policy Criteria
			if(status==false && aclPolicyCriteriaBean.getAclPolicyId()==null){
				AclPolicyCriteria aclPolicyCriteria = new AclPolicyCriteria();
				updateAclPolicyCriteriaDetails(aclPolicyCriteria, aclPolicyCriteriaBean, serviceCosCriteriaBean, status);
				aclPolicyCriteriaBean.setAclPolicyId(aclPolicyCriteria.getAclPolicyId());
			}
			else{
				Optional<AclPolicyCriteria> oAclPolicyCriteria = aclPolicyCriteriaRepository.findById(aclPolicyCriteriaBean.getAclPolicyId());
				if(oAclPolicyCriteria.isPresent()){
					AclPolicyCriteria aclPolicyCriteria = oAclPolicyCriteria.get();
					updateAclPolicyCriteriaDetails(aclPolicyCriteria, aclPolicyCriteriaBean, serviceCosCriteriaBean, status);
				}

			}

			aclPolicyCriteriaBean.setEdited(false);
			aclPolicyCriteriaBean.setModifiedBy("OPTIMUS_UI");
			aclPolicyCriteriaBean.setLastModifiedDate(new Timestamp(new Date().getTime()));

		}
	}

	private void saveCiscoImportMaps(ServiceDetailBean serviceDetailBean){

		if(serviceDetailBean.getCiscoImportMapBeans()!=null && !serviceDetailBean.getCiscoImportMapBeans().isEmpty()){
			serviceDetailBean.getCiscoImportMapBeans().forEach(
					ciscoImportMapBean -> {
						if(ciscoImportMapBean.isEdited()){
							Optional<CiscoImportMap> oCiscoImportMap = ciscoImportMapRepository.findById(ciscoImportMapBean.getCiscoImportId());
							if(oCiscoImportMap.isPresent()){
								CiscoImportMap ciscoImportMap = oCiscoImportMap.get();
								updateCiscoMapDetails(ciscoImportMap,ciscoImportMapBean);
							}
							ciscoImportMapBean.setEdited(false);
							ciscoImportMapBean.setLastModifiedDate(new Timestamp(new Date().getTime()));
							ciscoImportMapBean.setModifiedBy("OPTIMUS_UI");
						}

						if(ciscoImportMapBean.getPolicyTypeCriteriaMappingBeans()!=null){
							ciscoImportMapBean.getPolicyTypeCriteriaMappingBeans().forEach(
									policyTypeCriteriaMappingBean -> {
										savePolicyTypeCriteriaMappings(policyTypeCriteriaMappingBean);
									}
							);

						}

					}

			);

		}

	}

	private void saveIpAddrLanv4Details(IpaddrLanv4AddressBean ipaddrLanv4AddressBean, IpAddressDetail ipAddressDetail){
		logger.info("Inside saveIpAddrLanv4Details method");
    	if(ipaddrLanv4AddressBean.isEdited()){
    		if(Objects.nonNull(ipaddrLanv4AddressBean.getLanv4AddrId())){
    			logger.info("Updating existing Lanv4 Record");
				Optional<IpaddrLanv4Address> oIpaddrLanv4Address = ipaddrLanv4AddressRepository.findById(ipaddrLanv4AddressBean.getLanv4AddrId());
				if(oIpaddrLanv4Address.isPresent()){
					IpaddrLanv4Address ipaddrLanv4Address = oIpaddrLanv4Address.get();
					logger.info("IpaddrLanv4Address record found : {}", ipaddrLanv4Address.getLanv4AddrId());
					updateIpAddrLanv4Details(ipaddrLanv4Address, ipaddrLanv4AddressBean, null);
				}
			}
    		else{
				logger.info("Creating new Lanv4 Record");
				IpaddrLanv4Address ipaddrLanv4Address = new IpaddrLanv4Address();
				updateIpAddrLanv4Details(ipaddrLanv4Address, ipaddrLanv4AddressBean, ipAddressDetail);
			}
		}
	}

	private void saveIpAddrLanv6Details(IpaddrLanv6AddressBean ipaddrLanv6AddressBean, IpAddressDetail ipAddressDetail) {
		logger.info("Inside saveIpAddrLanv6Details method");
    	if (ipaddrLanv6AddressBean.isEdited()) {
    		if(Objects.nonNull(ipaddrLanv6AddressBean.getLanv6AddrId())) {
				logger.info("Updating existing Lanv6 Record");
				Optional<IpaddrLanv6Address> oIpaddrLanv6Address = ipaddrLanv6AddressRepository.findById(ipaddrLanv6AddressBean.getLanv6AddrId());
				if (oIpaddrLanv6Address.isPresent()) {
					IpaddrLanv6Address ipaddrLanv6Address = oIpaddrLanv6Address.get();
					logger.info("IpaddrLanv6Address record found : {}", ipaddrLanv6Address.getLanv6AddrId());
					updateIpAddrLanv6Details(ipaddrLanv6Address, ipaddrLanv6AddressBean, null);
				}
			}
    		else{
				logger.info("Creating new Lanv6 Record");
    			IpaddrLanv6Address ipaddrLanv6Address = new IpaddrLanv6Address();
				updateIpAddrLanv6Details(ipaddrLanv6Address, ipaddrLanv6AddressBean, ipAddressDetail);
			}
		}
	}

	private void saveIpAddrWanv4Details(IpaddrWanv4AddressBean ipaddrWanv4AddressBean, IpAddressDetail ipAddressDetail) {
		logger.info("Inside saveIpAddrWanv4Details method");
    	if (ipaddrWanv4AddressBean.isEdited()) {
			if(Objects.nonNull(ipaddrWanv4AddressBean.getWanv4AddrId())) {
				logger.info("Updating existing Wanv4 Record");
				Optional<IpaddrWanv4Address> oIpaddrWanv4Address = ipaddrWanv4AddressRepository.findById(ipaddrWanv4AddressBean.getWanv4AddrId());
				if (oIpaddrWanv4Address.isPresent()) {
					IpaddrWanv4Address ipaddrWanv4Address = oIpaddrWanv4Address.get();
					logger.info("IpaddrWanv4Address record found : {}", ipaddrWanv4Address.getWanv4AddrId());
					updateIpAddrWanv4Details(ipaddrWanv4Address, ipaddrWanv4AddressBean, null);
				}
			}
			else{
				logger.info("Creating new Wanv4 Record");
				IpaddrWanv4Address ipaddrWanv4Address = new IpaddrWanv4Address();
				updateIpAddrWanv4Details(ipaddrWanv4Address, ipaddrWanv4AddressBean, ipAddressDetail);
			}
		}
	}

	private void saveIpAddrWanv6Details(IpaddrWanv6AddressBean ipaddrWanv6AddressBean, IpAddressDetail ipAddressDetail){
		logger.info("Inside saveIpAddrWanv6Details method");
    	if(ipaddrWanv6AddressBean.isEdited()){
			if(Objects.nonNull(ipaddrWanv6AddressBean.getWanv6AddrId())) {
				logger.info("Updating existing Wanv6 Record");
				Optional<IpaddrWanv6Address> oIpaddrWanv6Address = ipaddrWanv6AddressRepository.findById(ipaddrWanv6AddressBean.getWanv6AddrId());
				if (oIpaddrWanv6Address.isPresent()) {
					IpaddrWanv6Address ipaddrWanv6Address = oIpaddrWanv6Address.get();
					logger.info("IpaddrWanv6Address record found : {}", ipaddrWanv6Address.getWanv6AddrId());
					updateIpAddrWanv6Details(ipaddrWanv6Address, ipaddrWanv6AddressBean, null);
				}
			}
			else{
				logger.info("Creating new Wanv6 Record");
				IpaddrWanv6Address ipaddrWanv6Address = new IpaddrWanv6Address();
				updateIpAddrWanv6Details(ipaddrWanv6Address, ipaddrWanv6AddressBean, ipAddressDetail);
			}
		}
	}

	private void saveIpAddressDetails(ServiceDetailBean serviceDetailBean){
		logger.info("Inside saveIpAddressDetails for service Code {}", serviceDetailBean.getServiceId());
		IpAddressDetail ipAddressDetail = ipAddressDetailRepository.findByServiceDetailId(serviceDetailBean.getId());
		if(Objects.nonNull(ipAddressDetail)){
			logger.info("IpAddressDetail record found : {}", ipAddressDetail.getIP_Address_Details());
		}
		if(!CollectionUtils.isEmpty(serviceDetailBean.getIpAddressDetailBeans())){
			serviceDetailBean.getIpAddressDetailBeans().forEach(ipAddressDetailBean -> {
				if(ipAddressDetailBean.isEdited()){
					if(ipAddressDetail!=null){
						updateIpAddressDetails(ipAddressDetail, ipAddressDetailBean);
					}
				}
				if(!CollectionUtils.isEmpty(ipAddressDetailBean.getIpaddrLanv4Addresses())){
					ipAddressDetailBean.getIpaddrLanv4Addresses().forEach(ipaddrLanv4AddressBean -> {
						saveIpAddrLanv4Details(ipaddrLanv4AddressBean, ipAddressDetail);
					});
				}
				if(!CollectionUtils.isEmpty(ipAddressDetailBean.getIpaddrLanv6Addresses())){
					ipAddressDetailBean.getIpaddrLanv6Addresses().forEach(ipaddrLanv6AddressBean -> {
						saveIpAddrLanv6Details(ipaddrLanv6AddressBean, ipAddressDetail);
					});
				}
				if(!CollectionUtils.isEmpty(ipAddressDetailBean.getIpaddrWanv4Addresses())){
					ipAddressDetailBean.getIpaddrWanv4Addresses().forEach(ipaddrWanv4AddressBean -> {
						saveIpAddrWanv4Details(ipaddrWanv4AddressBean, ipAddressDetail);
					});
				}
				if(!CollectionUtils.isEmpty(ipAddressDetailBean.getIpaddrWanv6Addresses())){
					ipAddressDetailBean.getIpaddrWanv6Addresses().forEach(ipaddrWanv6AddressBean -> {
						saveIpAddrWanv6Details(ipaddrWanv6AddressBean, ipAddressDetail);
					});
				}
				ipAddressDetailBean.setEdited(false);
				ipAddressDetailBean.setLastModifiedDate(new Timestamp(new Date().getTime()));
				ipAddressDetailBean.setModifiedBy("OPTIMUS_UI");
			});
		}
		logger.info("Exiting saveIpAddressDetails for service Code {}", serviceDetailBean.getServiceId());
	}

	private void saveMulticastingDetails(ServiceDetailBean serviceDetailBean){
		logger.info("Inside saveMulticastingDetails method for service Code {}", serviceDetailBean.getServiceId());
		if(!CollectionUtils.isEmpty(serviceDetailBean.getMulticastingBeans())){
			serviceDetailBean.getMulticastingBeans().forEach(multicastingBean -> {
				if(multicastingBean.isEdited()){
					Optional<Multicasting> oMulticasting = multicastingRepository.findById(multicastingBean.getMulticatingId());
					if(oMulticasting.isPresent()){
						Multicasting multicasting = oMulticasting.get();
						updateMulticastingDetails(multicasting, multicastingBean);
					}
					multicastingBean.setEdited(false);
					multicastingBean.setLastModifiedDate(new Timestamp(new Date().getTime()));
					multicastingBean.setModifiedBy("OPTIMUS_UI");
				}
			});
		}

	}

	private void saveServiceCosDetails(ServiceCosCriteriaBean serviceCosCriteriaBean, ServiceQoBean serviceQoBean){
		logger.info("Inside saveServiceCosDetails method");
		if(serviceCosCriteriaBean.isEdited() && serviceCosCriteriaBean.getServiceCosId()==null){
			ServiceCosCriteria serviceCosCriteria = new ServiceCosCriteria();
			updateServiceCosCriteriaDetails(serviceCosCriteria, serviceCosCriteriaBean, serviceQoBean);
			serviceCosCriteriaBean.setServiceCosId(serviceCosCriteria.getServiceCosId());
		}
		else {
			Optional<ServiceCosCriteria> oServiceCosCriteria = serviceCosCriteriaRepository
					.findById(serviceCosCriteriaBean.getServiceCosId());

			if(oServiceCosCriteria.isPresent()){
				ServiceCosCriteria serviceCosCriteria = oServiceCosCriteria.get();
				logger.info("ServiceCosCriteria record found : {}", serviceCosCriteria.getServiceCosId());
				updateServiceCosCriteriaDetails(serviceCosCriteria, serviceCosCriteriaBean, serviceQoBean);

				if(!CollectionUtils.isEmpty(serviceCosCriteriaBean.getAclPolicyCriterias())){
					serviceCosCriteriaBean.getAclPolicyCriterias().forEach(aclPolicyCriteriaBean -> {
						// Changing "from" IP Address to some other criteria
						if("IP Address".equalsIgnoreCase(serviceCosCriteria.getClassificationCriteria())
							&& !("IP Address".equalsIgnoreCase(serviceCosCriteriaBean.getClassificationCriteria()))){
							saveAclPolicyCriterias(aclPolicyCriteriaBean,serviceCosCriteriaBean,true);
						}

						// Changing "to" IP Address
						else if("IP Address".equalsIgnoreCase(serviceCosCriteria.getClassificationCriteria())
							|| "IP Address".equalsIgnoreCase(serviceCosCriteriaBean.getClassificationCriteria())){
							saveAclPolicyCriterias(aclPolicyCriteriaBean,serviceCosCriteriaBean,false);
						}
					});
					serviceCosCriteriaBean.setEdited(false);
				}
			}
		}
	}

	private void saveServiceQos(ServiceDetailBean serviceDetailBean){
    	logger.info("Inside saveServiceQos method for serviceCode {}", serviceDetailBean.getServiceId());
		if(!CollectionUtils.isEmpty(serviceDetailBean.getServiceQoBeans())){
			serviceDetailBean.getServiceQoBeans().forEach(serviceQoBean -> {
				if(serviceQoBean.isEdited()){
					Optional<ServiceQo> oServiceQo = serviceQoRepository.findById(serviceQoBean.getServiceQosId());
					if(oServiceQo.isPresent()){
						ServiceQo serviceQo = oServiceQo.get();
						logger.info("ServiceQo record found : {}", serviceQo.getServiceQosId());
						updateServiceQoDetails(serviceQo, serviceQoBean);
					}
					serviceQoBean.setEdited(false);
				}
				if(!CollectionUtils.isEmpty(serviceQoBean.getServiceCosCriterias())){
					serviceQoBean.getServiceCosCriterias().forEach(serviceCosCriteriaBean -> {
						saveServiceCosDetails(serviceCosCriteriaBean, serviceQoBean);
					});
				}
			});
		}
	}

	private void saveVpnSolutions(ServiceDetailBean serviceDetailBean){
		logger.info("Inside saveVpnSolutions method for serviceCode {}", serviceDetailBean.getServiceId());
    	if(!CollectionUtils.isEmpty(serviceDetailBean.getVpnSolutionBeans())){
			serviceDetailBean.getVpnSolutionBeans().forEach(vpnSolutionBean -> {
				if(vpnSolutionBean.isEdited() && vpnSolutionBean.getVpnSolutionId()!=null){
					Optional<VpnMetatData> oVpnMetatData = vpnMetatDataRepository.findById(vpnSolutionBean.getVpnSolutionId());
					if(oVpnMetatData.isPresent()){
						VpnMetatData vpnMetatData = oVpnMetatData.get();
						updateVpnSolutionDetails(vpnMetatData, vpnSolutionBean);
					}
					vpnSolutionBean.setEdited(false);
				}
			});
		}
	}

	private void saveRegexAsPathConfigDetails(RegexAspathConfigBean regexAspathConfigBean, PolicyCriteria policyCriteria){
		if(regexAspathConfigBean.getRegexAspathid()==null){
			RegexAspathConfig regexAspathConfig = new RegexAspathConfig();
			updateRegexAsPathConfigDetails(regexAspathConfig, regexAspathConfigBean, policyCriteria);
		}
		else{
			Optional<RegexAspathConfig> oRegexAspathConfig = regexAspathConfigRepository.findById(regexAspathConfigBean.getRegexAspathid());
			if(oRegexAspathConfig.isPresent()){
				RegexAspathConfig regexAspathConfig = oRegexAspathConfig.get();
				updateRegexAsPathConfigDetails(regexAspathConfig, regexAspathConfigBean, policyCriteria);
			}
		}
	}

	private void saveNeighbourCommunityConfigs(NeighbourCommunityConfigBean neighbourCommunityConfigBean, PolicyCriteria policyCriteria){
		if(neighbourCommunityConfigBean.getNeighbourCommunityId()==null){
			NeighbourCommunityConfig neighbourCommunityConfig = new NeighbourCommunityConfig();
			updateNeighbourCommunityConfigDetails(neighbourCommunityConfig, neighbourCommunityConfigBean, policyCriteria);
		}
		else{
			Optional<NeighbourCommunityConfig> oNeighbourCommunityConfig = neighbourCommunityConfigRepository
					.findById(neighbourCommunityConfigBean.getNeighbourCommunityId());
			if(oNeighbourCommunityConfig.isPresent()){
				NeighbourCommunityConfig neighbourCommunityConfig = oNeighbourCommunityConfig.get();
				updateNeighbourCommunityConfigDetails(neighbourCommunityConfig, neighbourCommunityConfigBean, null);
			}
		}
	}

	private void savePrefixistConfigs(PrefixlistConfigBean prefixlistConfigBean, PolicyCriteria policyCriteria){
		logger.info("Inside savePrefixListConfigs to update or create PrefixListConfig Records");
    	if(prefixlistConfigBean.getPrefixlistId()==null){
    		logger.info("New PrefixListConfig record to be created");
			PrefixlistConfig prefixlistConfig = new PrefixlistConfig();
			updatePrefixListConfigDetails(prefixlistConfig, prefixlistConfigBean, policyCriteria);
		}
		else{
			
			Optional<PrefixlistConfig> oPrefixlistConfig = prefixlistConfigRepository
					.findById(prefixlistConfigBean.getPrefixlistId());
			if (oPrefixlistConfig.isPresent()) {
				PrefixlistConfig prefixlistConfig = oPrefixlistConfig.get();
				logger.info("PrefixList Record Exists : {}", prefixlistConfig.getPrefixlistId());
				updatePrefixListConfigDetails(prefixlistConfig, prefixlistConfigBean, policyCriteria);
			}
		}
	}

	private void savePolicyCriteria(PolicyCriteriaBean policyCriteriaBean, PolicyType policyType, PolicyTypeBean policyTypeBean){
		logger.info("Inside savePolicyCriteria to update or create Match and Set Criterias");
    	PolicyCriteria policyCriteria = null;
    	boolean newCriteriaMappingRequired = false;
		if(Objects.nonNull(policyCriteriaBean.getPolicyCriteriaId())) {
			policyCriteria = policyCriteriaRepository.findByPolicyCriteriaId(policyCriteriaBean.getPolicyCriteriaId());
			if(Objects.isNull(policyCriteria)){
				logger.info("PolicyCriteria record not found");
			}
		}
		PolicycriteriaProtocol policyCriteriaProtocol = null;
		logger.info("PolicyCriteriaBean has been edited. Updating the records");
		// Update existing record
		if (Objects.nonNull(policyCriteria)) {
			// Match Criterias
			// 1. Protocol
			// Case : User has unchecked the Criteria Protocol - End Date the Protocol Record
			if (policyCriteriaBean.getMatchcriteriaProtocol() == false && policyCriteria.getMatchcriteriaProtocol()!=null && policyCriteria.getMatchcriteriaProtocol() == (byte) 1) {
				logger.info("Policy Criteria Protocol Record to be end-dated");
				policyCriteriaProtocol = policyCriteriaProtocolRepository
						.findFirstByPolicyCriteria_policyCriteriaId(policyCriteria.getPolicyCriteriaId());
				if (Objects.nonNull(policyCriteriaProtocol)) {
					logger.info("PolicyCriteriaProtocol record found : {}", policyCriteriaProtocol.getPolicyProtocolId());
					policyCriteriaProtocol.setEndDate(new Timestamp(new Date().getTime()));
				}
				policyCriteriaProtocolRepository.saveAndFlush(policyCriteriaProtocol);
				policyCriteria.setMatchcriteriaProtocol(convertBooleanToByte(false));
			}
			// Case : User has selected Criteria Protocol
			else if (policyCriteriaBean.getMatchcriteriaProtocol() == true && policyCriteria.getMatchcriteriaProtocol()!=null && policyCriteria.getMatchcriteriaProtocol() == (byte) 0) {
				logger.info("New Policy Criteria Protocol record to be created");
				policyCriteriaProtocol = new PolicycriteriaProtocol();
				policyCriteriaProtocol.setProtocolName(policyCriteriaBean.getMatchcriteriaProtocolName());
				policyCriteriaProtocol.setLastModifiedDate(new Timestamp(new Date().getTime()));
				policyCriteriaProtocol.setPolicyCriteria(policyCriteria);
				policyCriteria.setMatchcriteriaProtocol(convertBooleanToByte(true));
			}
			// 2. PrefixList
			// Case : User has unchecked the Criteria Prefix List - End Date the Prefix Record
			if ((policyCriteriaBean.getMatchcriteriaPrefixlist() == false || (policyCriteriaBean.getMatchCriteriaPrefixListPreprovisioned()!=null && 
					policyCriteriaBean.getMatchCriteriaPrefixListPreprovisioned()==true))
					&& policyCriteria.getMatchcriteriaPrefixlist()!=null && policyCriteria.getMatchcriteriaPrefixlist() == (byte) 1) {
				logger.info("Match Criteria Prefix List records to be end-dated");
				Set<PrefixlistConfig> prefixlistConfigs = prefixlistConfigRepository.findByPolicyCriteria(policyCriteria);
				if (!CollectionUtils.isEmpty(prefixlistConfigs)) {
					prefixlistConfigs.forEach(prefixlistConfig -> {
						prefixlistConfig.setEndDate(new Timestamp(new Date().getTime()));
						prefixlistConfigRepository.saveAndFlush(prefixlistConfig);
					});
				}
				policyCriteria.setMatchcriteriaPrefixlist(convertBooleanToByte(false));
			}
			// Case : User has selected Prefix List - Create new PrefixList Record
			if (policyCriteriaBean.getMatchcriteriaPrefixlist() == true) {
				logger.info("New Match Criteria Prefix List to be created");
				if (!CollectionUtils.isEmpty(policyCriteriaBean.getPrefixlistConfigs())) {
					for (PrefixlistConfigBean prefixlistConfigBean : policyCriteriaBean.getPrefixlistConfigs()) {
						if(prefixlistConfigBean.isEdited()) {
							savePrefixistConfigs(prefixlistConfigBean, policyCriteria);
						}
					}
				}
			}

			// 3. RegexAsPathConfigs
			// Case : User has unchecked RegexAsPath - End Date RegexAsPathConfig record
			if (policyCriteriaBean.getMatchcriteriaRegexAspath() == false && policyCriteria.getMatchcriteriaRegexAspath()!=null && policyCriteria.getMatchcriteriaRegexAspath() == (byte) 1) {
				logger.info("Match Criteria RegexAsPaths records to be end-dated");
				Set<RegexAspathConfig> regexAspathConfigs = regexAspathConfigRepository.findByPolicyCriteria(policyCriteria);
				if (!CollectionUtils.isEmpty(regexAspathConfigs)) {
					regexAspathConfigs.forEach(regexAspathConfig -> {
						regexAspathConfig.setEndDate(new Timestamp(new Date().getTime()));
						regexAspathConfigRepository.saveAndFlush(regexAspathConfig);
					});
				}
				policyCriteria.setMatchcriteriaRegexAspath(convertBooleanToByte(false));
			}

			// Case : User has added RegexAsPath - Create RegexAsPathConfig record
			if (policyCriteriaBean.getMatchcriteriaRegexAspath() == true && policyCriteria.getMatchcriteriaRegexAspath()!=null && policyCriteria.getMatchcriteriaRegexAspath() == (byte) 0) {
				logger.info("New Match Criteria RegexAsPathConfig to be created");
				if (!CollectionUtils.isEmpty(policyCriteriaBean.getRegexAspathConfigs())) {
					for (RegexAspathConfigBean regexAspathConfigBean : policyCriteriaBean.getRegexAspathConfigs()) {
						// No need to check edited flag here
						saveRegexAsPathConfigDetails(regexAspathConfigBean, policyCriteria);
					}
				}
				policyCriteria.setMatchcriteriaRegexAspath(convertBooleanToByte(true));
			}

			// Set Criterias
			// 1. NeighbourCommunity
			// Case : User has unchecked Neighbour Community
			if (policyCriteriaBean.getSetcriteriaNeighbourCommunity() == false && policyCriteria.getSetcriteriaNeighbourCommunity()!=null && policyCriteria.getSetcriteriaNeighbourCommunity() == (byte) 1) {
				logger.info("Set Criteria NeighbourCommunityConfig records to be end-dated");
				Set<NeighbourCommunityConfig> neighbourCommunityConfigs = neighbourCommunityConfigRepository.findByPolicyCriteria(policyCriteria);
				if (!CollectionUtils.isEmpty(neighbourCommunityConfigs)) {
					neighbourCommunityConfigs.forEach(neighbourCommunityConfig -> {
						neighbourCommunityConfig.setEndDate(new Timestamp(new Date().getTime()));
						neighbourCommunityConfigRepository.saveAndFlush(neighbourCommunityConfig);
					});
				}
				policyCriteria.setSetcriteriaNeighbourCommunity(convertBooleanToByte(false));
			}

			// Case : User has checked Neighbour Community
			if (policyCriteriaBean.getSetcriteriaNeighbourCommunity() == true && (policyCriteria.getSetcriteriaNeighbourCommunity() ==null || (policyCriteria.getSetcriteriaNeighbourCommunity()!=null && (policyCriteria.getSetcriteriaNeighbourCommunity() == (byte) 0
					|| policyCriteria.getSetcriteriaNeighbourCommunity() == (byte) 1)))) {
				logger.info("New NeighbourCommunityConfig records to be created");
				//newCriteriaMappingRequired = true;
				if (!CollectionUtils.isEmpty(policyCriteriaBean.getNeighbourCommunityConfigs())) {
					for (NeighbourCommunityConfigBean neighbourCommunityConfigBean : policyCriteriaBean.getNeighbourCommunityConfigs()) {
						saveNeighbourCommunityConfigs(neighbourCommunityConfigBean, policyCriteria);
					}
				}
				policyCriteria.setSetcriteriaNeighbourCommunity(convertBooleanToByte(true));
			}
			if (Objects.nonNull(policyCriteriaProtocol)) {
				logger.info("Saving Policy Criteria Protocol");
				policyCriteriaProtocol.setPolicyCriteria(policyCriteria);
				policyCriteriaProtocolRepository.saveAndFlush(policyCriteriaProtocol);
			}
			policyCriteria.setLastModifiedDate(new Timestamp(new Date().getTime()));
			policyCriteriaRepository.saveAndFlush(policyCriteria);
			updatePolicyCriteriaDetails(policyCriteria, policyCriteriaBean, policyType, policyTypeBean, newCriteriaMappingRequired);

			// If Pre Provisioned is removed
			if(newCriteriaMappingRequired || (policyTypeBean.getInboundIpv4Preprovisioned()==true && policyType.getInboundIpv4Preprovisioned()!=null && policyType.getInboundIpv4Preprovisioned()==(byte)0) ||
					(policyTypeBean.getOutboundIpv4Preprovisioned()==true && policyType.getOutboundIpv4Preprovisioned()!=null && policyType.getOutboundIpv4Preprovisioned()==(byte)0)){
				PolicyTypeCriteriaMapping policyTypeCriteriaMapping = new PolicyTypeCriteriaMapping();
				policyTypeCriteriaMapping.setPolicyType(policyType);
				policyTypeCriteriaMapping.setPolicyCriteriaId(policyCriteria.getPolicyCriteriaId());
				policyTypeCriteriaMapping.setVersion(1);
				policyTypeCriteriaMapping.setLastModifiedDate(new Timestamp(new Date().getTime()));
				policyTypeCriteriaMappingRepository.saveAndFlush(policyTypeCriteriaMapping);
			}
		}
		logger.info("Exiting savePolicyCriteria method");
	}

	private void savePolicyTypes(PolicyTypeBean policyTypeBean){
		logger.info("Inside savePolicyTypes method");
		Optional<PolicyType> oPolicyType = policyTypeRepository.findById(policyTypeBean.getPolicyId());
		if(oPolicyType.isPresent()) {
			PolicyType policyType = oPolicyType.get();
			logger.info("PolicyType Record found {}", policyType.getPolicyId());
			if(policyTypeBean.isEdited()) {
				updatePolicyTypeDetails(policyType, policyTypeBean);
			}
			// Updating the PolicyCriteria records
			try {
				if (!CollectionUtils.isEmpty(policyTypeBean.getPolicyCriteria())) {
					logger.info("PolicyCriteria exists for given PolicyType");
					policyTypeBean.getPolicyCriteria().forEach(policyCriteriaBean -> {
						savePolicyCriteria(policyCriteriaBean, policyType, policyTypeBean);
					});
				}
			}
			catch (Exception ex){
				logger.error("Exception occured in savePolicyCriterias {}", ex.getMessage());
			}
		}
		logger.info("Exiting savePolicyTypes method");
	}

	private void saveTopologies(ServiceDetailBean serviceDetailBean){
    	logger.info("Inside saveTopologies method");
		if(!CollectionUtils.isEmpty(serviceDetailBean.getTopologyBeans())){
			serviceDetailBean.getTopologyBeans().forEach(topologyBean -> {
				if(topologyBean.isEdited()){
					Optional<Topology> oTopology = topologyRepository.findById(topologyBean.getTopologyId());
					if(oTopology.isPresent()){
						Topology topology = oTopology.get();
						logger.info("Topology Record found for update : {}", topology.getTopologyId());
						updateTopologyDetails(topology, topologyBean);
					}
				}
				if(!CollectionUtils.isEmpty(topologyBean.getRouterUplinkports())){
					topologyBean.getRouterUplinkports().forEach(routerUplinkportBean -> {
						if(routerUplinkportBean.isEdited()){
							Optional<RouterUplinkport> oRouterUplinkport = routerUplinkPortRepository
									.findById(routerUplinkportBean.getRouterUplinkportId());
							if(oRouterUplinkport.isPresent()){
								RouterUplinkport routerUplinkport = oRouterUplinkport.get();
								logger.info("RouterUplinkport Record found for update : {}", routerUplinkport.getRouterUplinkportId());
								updateRouterUplinkportDetails(routerUplinkport, routerUplinkportBean);
							}
						}
					});
				}
				if(!CollectionUtils.isEmpty(topologyBean.getUniswitchDetails())){
					topologyBean.getUniswitchDetails().forEach(uniswitchDetailBean -> {
						if(uniswitchDetailBean.isEdited()){
							Optional<UniswitchDetail> oUniswitchDetail = uniswitchDetailRepository.findById(uniswitchDetailBean.getUniswitchId());
							if(oUniswitchDetail.isPresent()){
								UniswitchDetail uniswitchDetail = oUniswitchDetail.get();
								logger.info("UniswitchDetail Record found for update : {}", uniswitchDetail.getUniswitchId());
								updateUniswitchDetails(uniswitchDetail, uniswitchDetailBean);
							}
						}
					});
				}
			});
		}
	}

	private void saveVrfs(ServiceDetailBean serviceDetailBean){
    	logger.info("Inside saveVrfs method");
		if(!CollectionUtils.isEmpty(serviceDetailBean.getVrfBeans())){
			serviceDetailBean.getVrfBeans().forEach(vrfBean -> {
				if(vrfBean.isEdited()){
					Optional<Vrf> oVrf = vrfRepository.findById(vrfBean.getVrfId());
					if(oVrf.isPresent()){
						Vrf vrf = oVrf.get();
						logger.info("Vrf Record found : {}", vrf.getVrfId());
						updateVrfDetails(vrf,vrfBean);
					}
				}
				if(!CollectionUtils.isEmpty(vrfBean.getPolicyTypes())){
					vrfBean.getPolicyTypes().forEach(policyTypeBean -> {
						savePolicyTypes(policyTypeBean);
					});
				}
			});
		}
	}

	private void saveHsrpVrrpProtocolDetails(HsrpVrrpProtocolBean hsrpVrrpProtocolBean){
		logger.info("Inside saveHsrpVrrpProtocolDetails method");
    	if(hsrpVrrpProtocolBean.isEdited()){
			Optional<HsrpVrrpProtocol> oHsrpVrrpProtocol = hsrpVrrpProtocolRepository
					.findById(hsrpVrrpProtocolBean.getHsrpVrrpId());
			if(oHsrpVrrpProtocol.isPresent()){
				HsrpVrrpProtocol hsrpVrrpProtocol = oHsrpVrrpProtocol.get();
				logger.info("HsrpVrrpProtocol Record found");
				updateHsrpVrrpProtocolDetails(hsrpVrrpProtocol, hsrpVrrpProtocolBean);
			}
		}
	}

	private void saveEigrpDetails(EigrpBean eigrpBean){
		logger.info("Inside saveEigrpDetails method");
		if(eigrpBean.isEdited()){
			Optional<Eigrp> oEigrp = eigrpRepository.findById(eigrpBean.getEigrpProtocolId());
			if(oEigrp.isPresent()){
				Eigrp eigrp = oEigrp.get();
				logger.info("Eigrp Record found : {}", eigrp.getEigrpProtocolId());
				updateEigrpDetails(eigrp, eigrpBean);
			}
		}
	}

	private void saveRipDetails(RipBean ripBean){
		logger.info("Inside saveRipDetails method");
    	if(ripBean.isEdited()){
			Optional<Rip> oRip = ripRepository.findById(ripBean.getRipId());
			if(oRip.isPresent()){
				Rip rip = oRip.get();
				logger.info("Rip Record found : {}", rip.getRipId());
				updateRipDetails(rip, ripBean);
			}
		}
		if(!CollectionUtils.isEmpty(ripBean.getPolicyTypes())){
			ripBean.getPolicyTypes().forEach(policyTypeBean -> {
				savePolicyTypes(policyTypeBean);
			});
		}
	}

	private void saveOspfDetails(OspfBean ospfBean){
		if(ospfBean.isEdited()){
			Optional<Ospf> oOspf = ospfRepository.findById(ospfBean.getOspfId());
			if(oOspf.isPresent()){
				Ospf ospf = oOspf.get();
				updateOspfDetails(ospf, ospfBean);
			}
			ospfBean.setEdited(false);
		}
		if(ospfBean.getPolicyTypes()!=null && !ospfBean.getPolicyTypes().isEmpty()){
			ospfBean.getPolicyTypes().forEach(policyTypeBean -> {
				savePolicyTypes(policyTypeBean);
			});
		}
	}

	private void saveCpeDetails(CpeBean cpeBean){
		if(cpeBean.isEdited()){
			Optional<Cpe> oCpe = cpeRepository.findById(cpeBean.getCpeId());
			if(oCpe.isPresent()){
				Cpe cpe = oCpe.get();
				updateCpeDetails(cpe, cpeBean);
			}
			cpeBean.setEdited(false);
		}
	}

	private void saveRouterDetails(RouterDetailBean routerDetailBean){
		if(routerDetailBean.isEdited()){
			Optional<RouterDetail> oRouterDetail = routerDetailRepository.findById(routerDetailBean.getRouterId());
			if(oRouterDetail.isPresent()){
				RouterDetail routerDetail = routerDetailRepository.findById(routerDetailBean.getRouterId()).get();
				updateRouterDetails(routerDetail, routerDetailBean);
			}
			routerDetailBean.setEdited(false);
		}
	}

	private void saveWanStaticRouteDetails(WanStaticRouteBean wanStaticRouteBean, StaticProtocol staticProtocol){
		logger.info("Inside saveWanStaticRouteDetails method with static protcol as {}",staticProtocol);
		if(wanStaticRouteBean.isEdited()){
			// New Record
			if(wanStaticRouteBean.getWanstaticrouteId()==null){
				WanStaticRoute wanStaticRoute = new WanStaticRoute();
				updateWanStaticRouteDetails(wanStaticRoute, wanStaticRouteBean, staticProtocol);
				wanStaticRouteBean.setWanstaticrouteId(wanStaticRoute.getWanstaticrouteId());
			}
			else{
				Optional<WanStaticRoute> oWanStaticRoute = wanStaticRouteRepository.findById(wanStaticRouteBean.getWanstaticrouteId());
				if(oWanStaticRoute.isPresent()){
					WanStaticRoute wanStaticRoute = oWanStaticRoute.get();
					updateWanStaticRouteDetails(wanStaticRoute, wanStaticRouteBean, staticProtocol);
				}
			}
			wanStaticRouteBean.setEdited(false);
		}
	}

	private void updateHsrpVrrpBasedOnRouter(EthernetInterfaceBean ethernetInterfaceBean, String routerMake){

		/*
			Corresponding HSRP and VRRP records have to be end dated if any of the 3 conditions are satisfied :
			a)	If routerMake is "CISCO IP" and Hsrp flag is null or false
			b) If routerMake is "CISCO IP" and both Hsrp and Vrrp flags are either false or null
			c) If routerMake is not "CISCO IP" and Vrrp flag is null or false
		 */

		if((routerMake.equalsIgnoreCase("CISCO IP") && (ethernetInterfaceBean.getIshsrpEnabled()==null || ethernetInterfaceBean.getIshsrpEnabled()==false)) ||
			(!routerMake.equalsIgnoreCase("CISCO IP") && (ethernetInterfaceBean.getIsvrrpEnabled()==null || ethernetInterfaceBean.getIsvrrpEnabled()==false)) ||
			(routerMake.equalsIgnoreCase("CISCO IP") && ((ethernetInterfaceBean.getIsvrrpEnabled()==false && ethernetInterfaceBean.getIsvrrpEnabled()==null) ||
					(ethernetInterfaceBean.getIshsrpEnabled()==false && ethernetInterfaceBean.getIshsrpEnabled()==null)))){

			HsrpVrrpProtocol hsrpVrrpProtocol = hsrpVrrpProtocolRepository
					.findByEthernetInterface_ethernetInterfaceId(ethernetInterfaceBean.getEthernetInterfaceId());

			if(Objects.nonNull(hsrpVrrpProtocol)) {
				hsrpVrrpProtocol.setEndDate(new Timestamp(new Date().getTime()));
				hsrpVrrpProtocolRepository.saveAndFlush(hsrpVrrpProtocol);
			}
		}
	}

	private void saveEthernetInterfaceDetails(EthernetInterfaceBean ethernetInterfaceBean, ServiceDetailBean serviceDetailBean){

		if(ethernetInterfaceBean.isEdited()){

			Optional<EthernetInterface> oEthernetInterface = ethernetInterfaceRepository.findById(ethernetInterfaceBean.getEthernetInterfaceId());

			if(oEthernetInterface.isPresent()){

				EthernetInterface ethernetInterface = oEthernetInterface.get();

				updateEthernetInterfaceDetails(ethernetInterface, ethernetInterfaceBean);

			}

			ServiceDetail serviceDetail = serviceDetailRepository.findFirstByServiceIdAndServiceStateOrderByVersionDesc(serviceDetailBean.getServiceId(),TaskStatusConstants.ISSUED);
			if(serviceDetail!=null){

				String routerMake = serviceDetail.getRouterMake();
				updateHsrpVrrpBasedOnRouter(ethernetInterfaceBean, routerMake);
			}
			ethernetInterfaceBean.setEdited(false);
		}

		if(!CollectionUtils.isEmpty(ethernetInterfaceBean.getAclPolicyCriterias())){
			ethernetInterfaceBean.getAclPolicyCriterias().forEach(aclPolicyCriteriaBean -> {
				if(aclPolicyCriteriaBean.getIsinboundaclIpv4Applied()==false){
					saveAclPolicyCriterias(aclPolicyCriteriaBean, null,true);
				}
				else{
					saveAclPolicyCriterias(aclPolicyCriteriaBean, null,false);
				}
			});
		}

		if(ethernetInterfaceBean.getHsrpVrrpProtocols()!=null){
			ethernetInterfaceBean.getHsrpVrrpProtocols().forEach(hsrpVrrpProtocolBean -> {
				saveHsrpVrrpProtocolDetails(hsrpVrrpProtocolBean);
			});
		}
	}

	private void saveStaticProtocolDetails(StaticProtocolBean staticProtocolBean){
		StaticProtocol staticProtocol = null;
		Optional<StaticProtocol> oStaticProtocol = staticProtocolRepository.findById(staticProtocolBean.getStaticprotocolId());
		if(oStaticProtocol.isPresent()){
			staticProtocol = oStaticProtocol.get();
		}
    	if(staticProtocolBean.isEdited()){
			//Optional<StaticProtocol> oStaticProtocol = staticProtocolRepository.findById(staticProtocolBean.getStaticprotocolId());
			//if(oStaticProtocol.isPresent()){
			//	staticProtocol = oStaticProtocol.get();
				updateStaticProtocolDetails(staticProtocol, staticProtocolBean);
			//}
			staticProtocolBean.setEdited(false);
		}
		if(!CollectionUtils.isEmpty(staticProtocolBean.getWanStaticRoutes())){
			for(WanStaticRouteBean wanStaticRouteBean : staticProtocolBean.getWanStaticRoutes()) {
				saveWanStaticRouteDetails(wanStaticRouteBean, staticProtocol);
			}
		}
		if(!CollectionUtils.isEmpty(staticProtocolBean.getPolicyTypes())){
			staticProtocolBean.getPolicyTypes().forEach(policyTypeBean -> {
				savePolicyTypes(policyTypeBean);
			});
		}
	}

	private void saveChannelizedSdhInterfaceDetails(ChannelizedSdhInterfaceBean channelizedSdhInterfaceBean){
		if(channelizedSdhInterfaceBean.isEdited()){
			Optional<ChannelizedSdhInterface> oChannelizedSdhInterface = channelizedSdhInterfaceRepository
					.findById(channelizedSdhInterfaceBean.getSdhInterfaceId());

			if(oChannelizedSdhInterface.isPresent()){
				ChannelizedSdhInterface channelizedSdhInterface = oChannelizedSdhInterface.get();
				updateChannelizedSdhInterfaceDetails(channelizedSdhInterface, channelizedSdhInterfaceBean);
			}
			channelizedSdhInterfaceBean.setEdited(false);
		}

		if(!CollectionUtils.isEmpty(channelizedSdhInterfaceBean.getAclPolicyCriterias())){
			channelizedSdhInterfaceBean.getAclPolicyCriterias().forEach(aclPolicyCriteriaBean -> {
				if(aclPolicyCriteriaBean.getIsinboundaclIpv4Applied()==false){
					saveAclPolicyCriterias(aclPolicyCriteriaBean, null,true);
				}
				else{
					saveAclPolicyCriterias(aclPolicyCriteriaBean, null,false);
				}
			});
		}
		if(!CollectionUtils.isEmpty(channelizedSdhInterfaceBean.getHsrpVrrpProtocols())){
			channelizedSdhInterfaceBean.getHsrpVrrpProtocols().forEach(hsrpVrrpProtocolBean -> {
				saveHsrpVrrpProtocolDetails(hsrpVrrpProtocolBean);
			});
		}

	}

	private void saveChannelizedE1InterfaceDetails(ChannelizedE1serialInterfaceBean channelizedE1serialInterfaceBean){

		if(channelizedE1serialInterfaceBean.isEdited()){

			Optional<ChannelizedE1serialInterface> oChannelizedE1serialInterface = channelizedE1serialInterfaceRepository
					.findById(channelizedE1serialInterfaceBean.getE1serialInterfaceId());

			if(oChannelizedE1serialInterface.isPresent()){

				ChannelizedE1serialInterface channelizedE1serialInterface = oChannelizedE1serialInterface.get();

				updateChannelizedE1SerialInterfaceDetails(channelizedE1serialInterface, channelizedE1serialInterfaceBean);

			}

			channelizedE1serialInterfaceBean.setEdited(false);

		}

		if(channelizedE1serialInterfaceBean.getAclPolicyCriterias()!=null){
			channelizedE1serialInterfaceBean.getAclPolicyCriterias().forEach(aclPolicyCriteriaBean -> {
				if(aclPolicyCriteriaBean.getIsinboundaclIpv4Applied()==false){
					saveAclPolicyCriterias(aclPolicyCriteriaBean, null,true);
				}
				else{
					saveAclPolicyCriterias(aclPolicyCriteriaBean, null,false);
				}
			});
		}
	}

	private void saveBgpDetails(BgpBean bgpBean){
		logger.info("Inside saveBgpDetails method");
		if(bgpBean.isEdited()){
			Optional<Bgp> oBgp = bgpRepository.findById(bgpBean.getBgpId());
			if(oBgp.isPresent()){
				Bgp bgp = oBgp.get();
				updateBgpDetails(bgp, bgpBean);
			}
			bgpBean.setEdited(false);
		}

		if(!CollectionUtils.isEmpty(bgpBean.getPolicyTypes())){
			logger.info("Invoking savePolicyTypes method for Bgp Protocol");
			bgpBean.getPolicyTypes().forEach(policyTypeBean -> {
				savePolicyTypes(policyTypeBean);
			});
		}
	}

	private void savePEDetails(PEDetailsBean peDetailsBean, ServiceDetailBean serviceDetailBean){
		logger.info("Inside savePEDetails for serviceCode {}", serviceDetailBean.getServiceId());
    	if(!CollectionUtils.isEmpty(peDetailsBean.getEigrpBeans())){
			peDetailsBean.getEigrpBeans().forEach(eigrpBean -> {
				saveEigrpDetails(eigrpBean);
			});
		}
		if(!CollectionUtils.isEmpty(peDetailsBean.getRipBeans())){
			peDetailsBean.getRipBeans().forEach(ripBean ->{
				saveRipDetails(ripBean);
			});
		}
		if(!CollectionUtils.isEmpty(peDetailsBean.getOspfBeans())){
			peDetailsBean.getOspfBeans().forEach(ospfBean -> {
				saveOspfDetails(ospfBean);
			});
		}
		if(!CollectionUtils.isEmpty(peDetailsBean.getCpeBeans())){
			peDetailsBean.getCpeBeans().forEach(cpeBean -> {
				saveCpeDetails(cpeBean);
			});
		}
		if(!CollectionUtils.isEmpty(peDetailsBean.getRouterDetailBeans())){
			peDetailsBean.getRouterDetailBeans().forEach(routerDetailBean -> {
				saveRouterDetails(routerDetailBean);
			});
		}
		if(!CollectionUtils.isEmpty(peDetailsBean.getStaticProtocolBeans())){
			peDetailsBean.getStaticProtocolBeans().forEach(staticProtocolBean -> {
				saveStaticProtocolDetails(staticProtocolBean);
			});
		}
		if(!CollectionUtils.isEmpty(peDetailsBean.getEthernetInterfaceBeans())){
			peDetailsBean.getEthernetInterfaceBeans().forEach(ethernetInterfaceBean -> {
				saveEthernetInterfaceDetails(ethernetInterfaceBean, serviceDetailBean);
			});
		}
		if(!CollectionUtils.isEmpty(peDetailsBean.getChannelizedSdhInterfaceBeans())){
			peDetailsBean.getChannelizedSdhInterfaceBeans().forEach(channelizedSdhInterfaceBean -> {
				saveChannelizedSdhInterfaceDetails(channelizedSdhInterfaceBean);
			});
		}
		if(!CollectionUtils.isEmpty(peDetailsBean.getChannelizedE1serialInterfaceBeans())){
			peDetailsBean.getChannelizedE1serialInterfaceBeans().forEach(channelizedE1serialInterfaceBean -> {
				saveChannelizedE1InterfaceDetails(channelizedE1serialInterfaceBean);
			});
		}
		if(!CollectionUtils.isEmpty(peDetailsBean.getBgpBeans())){
			peDetailsBean.getBgpBeans().forEach(bgpBean -> {
				saveBgpDetails(bgpBean);
			});
		}
	}

	private void saveCEDetails(CEDetailsBean ceDetailsBean, ServiceDetailBean serviceDetailBean){
		logger.info("Inside saveCEDetails method for service Code {}", serviceDetailBean.getServiceId());
    	if(!CollectionUtils.isEmpty(ceDetailsBean.getEigrpBeans())){
			ceDetailsBean.getEigrpBeans().forEach(eigrpBean -> {
				saveEigrpDetails(eigrpBean);
			});
		}
		if(!CollectionUtils.isEmpty(ceDetailsBean.getRipBeans())){
			ceDetailsBean.getRipBeans().forEach(ripBean ->{
				saveRipDetails(ripBean);
			});
		}
		if(!CollectionUtils.isEmpty(ceDetailsBean.getOspfBeans())){
			ceDetailsBean.getOspfBeans().forEach(ospfBean -> {
				saveOspfDetails(ospfBean);
			});
		}
		if(!CollectionUtils.isEmpty(ceDetailsBean.getCpeBeans())){
			ceDetailsBean.getCpeBeans().forEach(cpeBean -> {
				saveCpeDetails(cpeBean);
			});
		}
		if(!CollectionUtils.isEmpty(ceDetailsBean.getRouterDetailBeans())){
			ceDetailsBean.getRouterDetailBeans().forEach(routerDetailBean -> {
				saveRouterDetails(routerDetailBean);
			});
		}
		if(!CollectionUtils.isEmpty(ceDetailsBean.getStaticProtocolBeans())){
			ceDetailsBean.getStaticProtocolBeans().forEach(staticProtocolBean -> {
				saveStaticProtocolDetails(staticProtocolBean);
			});
		}
		if(!CollectionUtils.isEmpty(ceDetailsBean.getEthernetInterfaceBeans())){
			ceDetailsBean.getEthernetInterfaceBeans().forEach(ethernetInterfaceBean -> {
				saveEthernetInterfaceDetails(ethernetInterfaceBean, serviceDetailBean);
			});
		}
		if(!CollectionUtils.isEmpty(ceDetailsBean.getChannelizedSdhInterfaceBeans())){
			ceDetailsBean.getChannelizedSdhInterfaceBeans().forEach(channelizedSdhInterfaceBean -> {
				saveChannelizedSdhInterfaceDetails(channelizedSdhInterfaceBean);
			});
		}

		if(!CollectionUtils.isEmpty(ceDetailsBean.getChannelizedE1serialInterfaceBeans())){
			ceDetailsBean.getChannelizedE1serialInterfaceBeans().forEach(channelizedE1serialInterfaceBean -> {
				saveChannelizedE1InterfaceDetails(channelizedE1serialInterfaceBean);
			});
		}

		if(!CollectionUtils.isEmpty(ceDetailsBean.getBgpBeans())){
			ceDetailsBean.getBgpBeans().forEach(bgpBean -> {
				saveBgpDetails(bgpBean);
			});
		}
	}

	private void saveInterfaceProtocolMappings(ServiceDetailBean serviceDetailBean){
		logger.info("Inside saveInterfaceProtocolMappings method for serviceCode {}", serviceDetailBean.getServiceId());
    	if(serviceDetailBean.getInterfaceProtocolMappingBean()!=null){
			InterfaceProtocolMappingBean interfaceProtocolMappingBean = serviceDetailBean.getInterfaceProtocolMappingBean();
			if(interfaceProtocolMappingBean.getPeDetailsBean()!=null){
				PEDetailsBean peDetailsBean = interfaceProtocolMappingBean.getPeDetailsBean();
				savePEDetails(peDetailsBean, serviceDetailBean);
			}
			if(interfaceProtocolMappingBean.getCeDetailsBean()!=null){
				CEDetailsBean ceDetailsBean = interfaceProtocolMappingBean.getCeDetailsBean();
				saveCEDetails(ceDetailsBean, serviceDetailBean);
			}
		}
	}

	private void saveLmComponentDetails(ServiceDetailBean serviceDetailBean){
		logger.info("Inside saveLmComponentDetails method");
    	if(!CollectionUtils.isEmpty(serviceDetailBean.getLmComponentBeans())) {
			serviceDetailBean.getLmComponentBeans().forEach(lmComponentBean->{
				if(lmComponentBean.isEdited()) {
					Optional<LmComponent> oLmComponent=lmComponentRepository.findById(lmComponentBean.getLmComponentId());
					if(oLmComponent.isPresent()){
						LmComponent lmComponent = oLmComponent.get();
						updateLmComponentDetails(lmComponent,lmComponentBean);
					}
				}
				if(!CollectionUtils.isEmpty(lmComponentBean.getCambiumLastmiles())) {
					lmComponentBean.getCambiumLastmiles().forEach(cambiumLastmileBean->{
						if(cambiumLastmileBean.isEdited()) {
							Optional<CambiumLastmile> oCambiumLastmile = cambiumLastmileRepository.findById(cambiumLastmileBean.getCambiumLastmileId());
							if(oCambiumLastmile.isPresent()){
								CambiumLastmile cambiumLastmile = oCambiumLastmile.get();
								updateCambiumLastmileDetails(cambiumLastmile,cambiumLastmileBean);
							}
						}
					});
				}

				if(!CollectionUtils.isEmpty(lmComponentBean.getWimaxLastmiles())) {
					lmComponentBean.getWimaxLastmiles().forEach(wimaxLastmileBean->{
						if(wimaxLastmileBean.isEdited()) {
							Optional<WimaxLastmile> oWimaxLastmile = wimaxLastmileRepository.findById(wimaxLastmileBean.getWimaxLastmileId());
							if(oWimaxLastmile.isPresent()){
								WimaxLastmile wimaxLastmile = oWimaxLastmile.get();
								updateWimaxLastmileDetails(wimaxLastmile,wimaxLastmileBean);
							}
						}
						if(!CollectionUtils.isEmpty(wimaxLastmileBean.getVlanQosProfiles())) {
							wimaxLastmileBean.getVlanQosProfiles().forEach(vlanQosProfileBean->{
								if(vlanQosProfileBean.isEdited()) {
									Optional<VlanQosProfile> oVlanQosProfile = vlanQosProfileRepository
											.findById(vlanQosProfileBean.getVlanQosProfileId());
									if(oVlanQosProfile.isPresent()){
										VlanQosProfile vlanQosProfile = oVlanQosProfile.get();
										updateVlanQosProfileDetails(vlanQosProfile,vlanQosProfileBean);
									}
								}
							});
						}
					});
				}
				if(!CollectionUtils.isEmpty(lmComponentBean.getRadwinLastmiles())) {
					lmComponentBean.getRadwinLastmiles().forEach(radwinLastmileBean->{
						if(radwinLastmileBean.isEdited()) {
							RadwinLastmile radwinLastmile=radwinLastmileRepository.findById(radwinLastmileBean.getRadwinLastmileId()).get();
							updateRadwinLastmileDetails(radwinLastmile,radwinLastmileBean);
						}
					});
				}

			});
		}
	}
	
	@Transactional(readOnly = false)
	public TaskBean updateServiceIpDetails(TaskBean taskBean) throws TclCommonException {

		OrderDetailBean orderDetailBean = taskBean.getOrderDetails();
		
		
			logger.info("updateServiceIpDetails invoked for orderDetailsBean", orderDetailBean.getOrderUuid());
	
			if(orderDetailBean!=null){
	
				if(orderDetailBean.isEdited()) {
					Optional<OrderDetail> oOrderDetail = orderDetailRepository.findById(orderDetailBean.getOrderId());
	
					if(oOrderDetail.isPresent()){
	
						OrderDetail orderDetail = oOrderDetail.get();
	
						updateOrderDetails(orderDetail, orderDetailBean, false, null);
	
						orderDetailBean.setEdited(false);
	
					}
	
				}
	
				if(orderDetailBean.getServiceDetailBeans()!=null && !orderDetailBean.getServiceDetailBeans().isEmpty()){
	
						List<ServiceDetailBean> serviceDetailBeans = orderDetailBean.getServiceDetailBeans();
	
						serviceDetailBeans.forEach(serviceDetailBean -> {
	
							if(serviceDetailBean.isEdited()){
	
								ServiceDetail serviceDetail =  serviceDetailRepository.findById(serviceDetailBean.getId()).get();
	
								saveServiceDetails(serviceDetail, serviceDetailBean);
	
								serviceDetailBean.setEdited(false);
	
							}
	
							saveAluSchedulerPolicies(serviceDetailBean);
	
							saveCiscoImportMaps(serviceDetailBean);
	
							saveIpAddressDetails(serviceDetailBean);
	
							saveMulticastingDetails(serviceDetailBean);
	
							saveServiceQos(serviceDetailBean);
	
							saveVpnSolutions(serviceDetailBean);
	
							saveVrfs(serviceDetailBean);
	
							saveTopologies(serviceDetailBean);
	
							saveInterfaceProtocolMappings(serviceDetailBean);
	
							saveLmComponentDetails(serviceDetailBean);
	
						});
	
					}
	
			}
			
		
		
		return taskBean;
	}

	@Transactional(readOnly = false)
	public TaskBean updateIPDetails(TaskBean taskBean) throws TclCommonException {
		logger.info("Inside updateIPDetails method for serviceCode {} with TaskBean {}", taskBean.getServiceCode(),
				Utils.convertObjectToJson(taskBean));
		OrderDetailBean orderDetailBean = taskBean.getOrderDetails();

		Task task = taskRepository.findById(taskBean.getTaskId()).orElse(null);

		if (task != null) {
			String taskKey = task.getMstTaskDef().getKey();
			String action = StringUtils.trimToEmpty(taskBean.getAction());
			logger.info("updateIPDetails invoked for task={} action={}", taskKey, action);

			if (orderDetailBean != null && !taskKey.equalsIgnoreCase("raise-turnup-request")
					&& !taskKey.equalsIgnoreCase("service-issue") && !"BOP".equalsIgnoreCase(action)
					&& !"CIM".equalsIgnoreCase(action)) {

				if (orderDetailBean.isEdited()) {
					Optional<OrderDetail> oOrderDetail = orderDetailRepository.findById(orderDetailBean.getOrderId());
					if (oOrderDetail.isPresent()) {
						OrderDetail orderDetail = oOrderDetail.get();
						logger.info("OrderDetail Record exists : {}", orderDetail.getOrderId());
						updateOrderDetails(orderDetail, orderDetailBean, false, null);
					}
				}
				if (!CollectionUtils.isEmpty(orderDetailBean.getServiceDetailBeans())) {
					List<ServiceDetailBean> serviceDetailBeans = orderDetailBean.getServiceDetailBeans();
					serviceDetailBeans.forEach(serviceDetailBean -> {
						if (serviceDetailBean.isEdited()) {
							logger.info("ServiceDetailBean has been edited for update");
							ServiceDetail serviceDetail = serviceDetailRepository.findById(serviceDetailBean.getId())
									.get();
							if (Objects.nonNull(serviceDetail)) {
								logger.info("ServiceDetail record found : {}", serviceDetail.getServiceId());
								saveServiceDetails(serviceDetail, serviceDetailBean);
							}
						}

						saveAluSchedulerPolicies(serviceDetailBean);
						saveCiscoImportMaps(serviceDetailBean);
						saveIpAddressDetails(serviceDetailBean);
						saveMulticastingDetails(serviceDetailBean);
						saveServiceQos(serviceDetailBean);
						saveVpnSolutions(serviceDetailBean);
						saveVrfs(serviceDetailBean);
						saveTopologies(serviceDetailBean);
						saveInterfaceProtocolMappings(serviceDetailBean);
						saveLmComponentDetails(serviceDetailBean);
					});
				}

			}
			
			if(action!=null){
				if("BOP".equalsIgnoreCase(action)){
						componentAndAttributeService.updateAdditionalAttributes(task.getServiceId(),
								"serviceDesignIpInfoCallFailureReason",
								componentAndAttributeService.getErrorMessageDetails(taskBean.getRemarks(), taskBean.getReason()),
								AttributeConstants.ERROR_MESSAGE, "enrich-service-design");	
				}else if("CIM".equalsIgnoreCase(action)){
					componentAndAttributeService.updateAdditionalAttributes(task.getServiceId(),
							"techDetailsFailureReason",
							componentAndAttributeService.getErrorMessageDetails(taskBean.getRemarks(), "IP1002"),
							AttributeConstants.ERROR_MESSAGE, "advanced-enrichment-rejected");
				} else if (taskKey.equalsIgnoreCase("raise-turnup-request") || taskKey.equalsIgnoreCase("service-issue")) {
					String commissioningDate =StringUtils.trimToEmpty(taskBean.getCommissioningDate());
					
					Map<String, String> atMap = new HashMap<>();
					 if(taskKey.equalsIgnoreCase("raise-turnup-request")) {
						 atMap.put("turnupRequested","Yes");
						 atMap.put("turnupCompletedDate",DateUtil.convertDateToString(new Date()));
						 componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());
						
					 }
					 
					 Optional<ScServiceDetail> scServiceDetailOp = scServiceDetailRepository.findById(task.getServiceId());
					 if(scServiceDetailOp.isPresent()) {
						 cramerService.generateBillStartDate(scServiceDetailOp.get(),commissioningDate,null,null);
					 }
				} else if ("ACTIVITY_GO_AHEAD".equalsIgnoreCase(action)
						&& !task.getOrderCode().toLowerCase().contains("izosdwan")) {
					logger.info("ACTIVITY GO AHEAD::{}", task.getServiceId());
					ScComponentAttribute scComponentIpAttribute = scComponentAttributesRepository
							.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
									task.getServiceId(), "isIpDownTimeRequired", "LM", "A");
					if (scComponentIpAttribute != null && scComponentIpAttribute.getAttributeValue() != null
							&& !scComponentIpAttribute.getAttributeValue().isEmpty()) {
						logger.info("ACTIVITY GO AHEAD IPDOWNTIME Required exists::{}", task.getServiceId());
						componentAndAttributeService.updateComponentAttr("isIpDownTimeRequired", "false",
								scComponentIpAttribute, "OPTIMUS");
					}
					ServiceDetail serviceDetail = serviceDetailRepository
							.findFirstByScServiceDetailIdAndServiceStateOrderByIdDesc(task.getServiceId(), "ISSUED");
					if (serviceDetail != null) {
						logger.info("ACTIVITY GO AHEAD Service Detail exists::{}", task.getServiceId());
						serviceDetail.setIsdowntimeReqd((byte) 0);
						serviceDetailRepository.saveAndFlush(serviceDetail);
					}
					 List<Task> waitForCustomerTasks=taskRepository.findByServiceIdAndMstTaskDef_key(task.getServiceId(), "wait-for-downtime-from-customer");
					 for(Task waitForCustomerTask: waitForCustomerTasks){
						 Execution execution = runtimeService.createExecutionQuery().processInstanceId(waitForCustomerTask.getWfProcessInstId())
									.activityId("wait-for-downtime-from-customer").singleResult();
						 if(execution!=null){
							 logger.info("Wait downtime Execution exists during Activity go Ahead::{}",execution.getId());
							 runtimeService.setVariable(execution.getId(),"checkDowntimeAction", "manual");	
							 runtimeService.trigger(execution.getId());
						 }
					 }
				}
			}
		}
		if (taskBean.getDocumentIds() != null && !taskBean.getDocumentIds().isEmpty()) {
			taskBean.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		}
		logger.info("Exiting updateIPDetails method for serviceCode : {}", taskBean.getServiceCode());
		return taskBean;
	}

	@Transactional(readOnly = false)
	public TaskBean makeTaskDataEntry(Integer taskId, TaskBean taskBean, Map<String, Object> wfMap) throws TclCommonException {
		Task task = taskRepository.findById(taskId).get();
		try {
			if ("rf-configuration-jeopardy".equals(taskBean.getTaskDefKey())) {
				Map<String, String> atMap = new HashMap<>();
				String suMacAddress = null;
				if (taskBean.getReason() != null) atMap.put("reason", taskBean.getReason());
				if (taskBean.getOwner() != null) atMap.put("owner", taskBean.getOwner());
				if (taskBean.getRemarks() != null) atMap.put("remarks", taskBean.getRemarks());
				List<ServiceDetailBean> serviceDetailBeans = taskBean.getOrderDetails().getServiceDetailBeans();
				if (!CollectionUtils.isEmpty(serviceDetailBeans)) {
					ServiceDetailBean serviceDetailBean = serviceDetailBeans.stream().findFirst().get();
					if (!CollectionUtils.isEmpty(serviceDetailBean.getLmComponentBeans())) {
						LmComponentBean lmComponentBean = serviceDetailBean.getLmComponentBeans().get(0);
						if (!CollectionUtils.isEmpty(lmComponentBean.getRadwinLastmiles())) {
							RadwinLastmileBean radwinLastmileBean = lmComponentBean.getRadwinLastmiles().stream().findFirst().get();
							if (radwinLastmileBean.getHsuMacAddr() != null && radwinLastmileBean.isEdited())
								suMacAddress = radwinLastmileBean.getHsuMacAddr();
						} else if (!CollectionUtils.isEmpty(lmComponentBean.getCambiumLastmiles())) {
							CambiumLastmileBean cambiumLastmileBean = lmComponentBean.getCambiumLastmiles().stream().findFirst().get();
							if (cambiumLastmileBean.getSuMacAddress() != null && cambiumLastmileBean.isEdited())
								suMacAddress = cambiumLastmileBean.getSuMacAddress();
						} else if (!CollectionUtils.isEmpty(lmComponentBean.getWimaxLastmiles())) {
							WimaxLastmileBean wimaxLastmileBean = lmComponentBean.getWimaxLastmiles().stream().findFirst().get();
							if (wimaxLastmileBean.getSumacAddress() != null && wimaxLastmileBean.isEdited())
								suMacAddress = wimaxLastmileBean.getSumacAddress();
						}
					}
				}
				if (suMacAddress != null) atMap.put("suMacAddress", suMacAddress);
				componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM, task.getSiteType());
			}else if ("manual-service-configuration".equals(taskBean.getTaskDefKey())) {
				logger.info("Manual Service Configuration for Service Id:: {}",taskBean.getServiceId());
				ScComponentAttribute ipDowntimeScComponentAttribute=scComponentAttributesRepository.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(task.getServiceId(), "isIpDownTimeRequired",AttributeConstants.COMPONENT_LM, task.getSiteType());
				if(Objects.nonNull(ipDowntimeScComponentAttribute) && Objects.nonNull(ipDowntimeScComponentAttribute.getAttributeValue()) && !ipDowntimeScComponentAttribute.getAttributeValue().isEmpty() && ipDowntimeScComponentAttribute.getAttributeValue().equals("true")){
					logger.info("Ip Downtime Exists for Service Id:: {}",taskBean.getServiceId());
					Map<String, String> atMap = new HashMap<>();
					atMap.put("isIpDownTimeRequired", "false");
					componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM, task.getSiteType());
					ServiceDetail serviceDetail=serviceDetailRepository.findFirstByScServiceDetailIdAndServiceStateOrderByIdDesc(task.getServiceId(),"ISSUED");
					if(serviceDetail!=null && serviceDetail.getIsdowntimeReqd()==1) {
						logger.info("Service Activtaion Ip Downtime Exists for Service Id:: {}",taskBean.getServiceId());
						serviceDetail.setIsdowntimeReqd((byte) 0);
						serviceDetailRepository.save(serviceDetail);
					}
				}
			}
		} catch (Exception e){
		logger.error("Error in RF Configuration Jeopardy {}",e.getMessage());
	}
    	return (TaskBean)flowableBaseService.taskDataEntry(task,taskBean,wfMap);
	}

	private void endDateAluSchedulerPolicies(Set<AluSchedulerPolicy> aluSchedulerPolicies, String modifiedBy, boolean status){
		if(aluSchedulerPolicies!=null && !aluSchedulerPolicies.isEmpty()){
			aluSchedulerPolicies.forEach(aluSchedulerPolicy -> {
				aluSchedulerPolicy.setEndDate((status==true) ? new Timestamp(new Date().getTime()) : null);
				aluSchedulerPolicy.setModifiedBy(modifiedBy);
				aluSchedulerPolicyRepository.saveAndFlush(aluSchedulerPolicy);
			});
		}
	}

	private void endDateTopologies(Set<Topology> topologies, String modifiedBy, boolean status){
		if(topologies!=null && !topologies.isEmpty()){
			topologies.forEach(topology -> {
				topology.setEndDate((status==true) ? new Timestamp(new Date().getTime()) : null);
				topology.setModifiedBy(modifiedBy);
				topologyRepository.saveAndFlush(topology);
				if(topology.getRouterUplinkports()!=null && !topology.getRouterUplinkports().isEmpty()){
					topology.getRouterUplinkports().forEach(routerUplinkport -> {
						routerUplinkport.setEndDate((status==true) ? new Timestamp(new Date().getTime()) : null);
						routerUplinkport.setModifiedBy(modifiedBy);
						routerUplinkPortRepository.saveAndFlush(routerUplinkport);
					});
				}
				if(topology.getUniswitchDetails()!=null && !topology.getUniswitchDetails().isEmpty()){
					topology.getUniswitchDetails().forEach(uniswitchDetail -> {
						uniswitchDetail.setEndDate((status==true) ? new Timestamp(new Date().getTime()) : null);
						uniswitchDetail.setModifiedBy(modifiedBy);
						uniswitchDetailRepository.saveAndFlush(uniswitchDetail);
					});
				}
			});
		}
	}

	private void endDateCiscoImports(Set<CiscoImportMap> ciscoImportMaps, String modifiedBy, boolean status){
		if(ciscoImportMaps!=null && !ciscoImportMaps.isEmpty()){
			ciscoImportMaps.forEach(ciscoImportMap -> {
				ciscoImportMap.setEndDate((status==true) ? new Timestamp(new Date().getTime()) : null);
				ciscoImportMap.setModifiedBy(modifiedBy);
				ciscoImportMapRepository.saveAndFlush(ciscoImportMap);
				if(ciscoImportMap.getPolicyTypeCriteriaMappings()!=null && !ciscoImportMap.getPolicyTypeCriteriaMappings().isEmpty()){
					ciscoImportMap.getPolicyTypeCriteriaMappings().forEach(policyTypeCriteriaMapping -> {
						policyTypeCriteriaMapping.setEndDate((status==true) ? new Timestamp(new Date().getTime()) : null);
						policyTypeCriteriaMapping.setModifiedBy(modifiedBy);
						policyTypeCriteriaMappingRepository.saveAndFlush(policyTypeCriteriaMapping);
					});
				}
			});
		}
	}

	private void endDateServiceQos(Set<ServiceQo> serviceQos, String modifiedBy, boolean status){
		if(serviceQos!=null && !serviceQos.isEmpty()){
			serviceQos.forEach(serviceQo -> {
				serviceQo.setEndDate((status==true) ? new Timestamp(new Date().getTime()) : null);
				serviceQo.setModifiedBy(modifiedBy);
				serviceQoRepository.saveAndFlush(serviceQo);
				if(serviceQo.getServiceCosCriterias()!=null && !serviceQo.getServiceCosCriterias().isEmpty()){
					serviceQo.getServiceCosCriterias().forEach(serviceCosCriteria -> {
						serviceCosCriteria.setEndDate((status==true) ? new Timestamp(new Date().getTime()) : null);
						serviceCosCriteria.setModifiedBy(modifiedBy);
						serviceCosCriteriaRepository.saveAndFlush(serviceCosCriteria);
						if(serviceCosCriteria.getAclPolicyCriterias()!=null && !serviceCosCriteria.getAclPolicyCriterias().isEmpty()){
							serviceCosCriteria.getAclPolicyCriterias().forEach(aclPolicyCriteria -> {
								aclPolicyCriteria.setEndDate((status==true) ? new Timestamp(new Date().getTime()) : null);
								aclPolicyCriteria.setModifiedBy(modifiedBy);
								aclPolicyCriteriaRepository.saveAndFlush(aclPolicyCriteria);
							});
						}
					});
				}
			});
		}
	}

	private void endDatePolicyTypes(PolicyType policyType, String modifiedBy, boolean status){
		policyType.setEndDate((status==true) ? new Timestamp(new Date().getTime()) : null);
		policyType.setModifiedBy(modifiedBy);
		policyTypeRepository.saveAndFlush(policyType);
		if(!CollectionUtils.isEmpty(policyType.getPolicyTypeCriteriaMappings())){
			policyType.getPolicyTypeCriteriaMappings().forEach(policyTypeCriteriaMapping -> {
				policyTypeCriteriaMapping.setEndDate((status==true) ? new Timestamp(new Date().getTime()) : null);
				policyTypeCriteriaMapping.setModifiedBy(modifiedBy);
				policyTypeCriteriaMappingRepository.saveAndFlush(policyTypeCriteriaMapping);
				Optional<PolicyCriteria> oPolicyCriteria = policyCriteriaRepository.findById(policyTypeCriteriaMapping.getPolicyCriteriaId());
				if(oPolicyCriteria.isPresent()){
					PolicyCriteria policyCriteria = oPolicyCriteria.get();
					policyCriteria.setEndDate((status==true) ? new Timestamp(new Date().getTime()) : null);
					policyCriteria.setModifiedBy(modifiedBy);
					policyCriteriaRepository.saveAndFlush(policyCriteria);
					if(!CollectionUtils.isEmpty(policyCriteria.getPolicycriteriaProtocols())){
						policyCriteria.getPolicycriteriaProtocols().forEach(policycriteriaProtocol -> {
							policycriteriaProtocol.setEndDate((status==true) ? new Timestamp(new Date().getTime()) : null);
							policycriteriaProtocol.setModifiedBy(modifiedBy);
							policyCriteriaProtocolRepository.saveAndFlush(policycriteriaProtocol);
						});
					}
					if(!CollectionUtils.isEmpty(policyCriteria.getPrefixlistConfigs())){
						policyCriteria.getPrefixlistConfigs().forEach(prefixlistConfig -> {
							prefixlistConfig.setEndDate((status==true) ? new Timestamp(new Date().getTime()) : null);
							prefixlistConfig.setModifiedBy(modifiedBy);
							prefixlistConfigRepository.saveAndFlush(prefixlistConfig);
						});
					}
					if(!CollectionUtils.isEmpty(policyCriteria.getNeighbourCommunityConfigs())){
						policyCriteria.getNeighbourCommunityConfigs().forEach(neighbourCommunityConfig -> {
							neighbourCommunityConfig.setEndDate((status==true) ? new Timestamp(new Date().getTime()) : null);
							neighbourCommunityConfig.setModifiedBy(modifiedBy);
							neighbourCommunityConfigRepository.saveAndFlush(neighbourCommunityConfig);
						});
					}
					if(!CollectionUtils.isEmpty(policyCriteria.getRegexAspathConfigs())){
						policyCriteria.getRegexAspathConfigs().forEach(regexAspathConfig -> {
							regexAspathConfig.setEndDate((status==true) ? new Timestamp(new Date().getTime()) : null);
							regexAspathConfig.setModifiedBy(modifiedBy);
							regexAspathConfigRepository.saveAndFlush(regexAspathConfig);
						});
					}
				}
			});
		}
	}

	private void endDateAclPolicyCriterias(AclPolicyCriteria aclPolicyCriteria, String modifiedBy, boolean status){
		aclPolicyCriteria.setEndDate((status==true) ? new Timestamp(new Date().getTime()) : null);
		aclPolicyCriteria.setModifiedBy(modifiedBy);
		aclPolicyCriteriaRepository.saveAndFlush(aclPolicyCriteria);
	}

	private void endDateHsrpVrrpProtocols(HsrpVrrpProtocol hsrpVrrpProtocol, String modifiedBy, boolean status){
		hsrpVrrpProtocol.setEndDate((status==true) ? new Timestamp(new Date().getTime()) : null);
		hsrpVrrpProtocol.setModifiedBy(modifiedBy);
		hsrpVrrpProtocolRepository.saveAndFlush(hsrpVrrpProtocol);
	}

	private void endDateInterfaceProtocols(Set<InterfaceProtocolMapping> interfaceProtocolMappings, String modifiedBy, boolean status){
		logger.info("Inside endDateInterfaceProtocols method");
		if(interfaceProtocolMappings!=null && !interfaceProtocolMappings.isEmpty()){
			interfaceProtocolMappings.forEach(interfaceProtocolMapping -> {
				interfaceProtocolMapping.setEndDate((status==true) ? new Timestamp(new Date().getTime()) : null);
				interfaceProtocolMapping.setModifiedBy(modifiedBy);
				interfaceProtocolMappingRepository.saveAndFlush(interfaceProtocolMapping);
				if(interfaceProtocolMapping.getOspf()!=null){
					Ospf ospf = interfaceProtocolMapping.getOspf();
					ospf.setEndDate((status==true) ? new Timestamp(new Date().getTime()) : null);
					ospf.setModifiedBy(modifiedBy);
					ospfRepository.saveAndFlush(ospf);
					if(!CollectionUtils.isEmpty(ospf.getPolicyTypes())){
						ospf.getPolicyTypes().forEach(policyType -> {
							endDatePolicyTypes(policyType, modifiedBy, status);
						});
					}
				}
				if(interfaceProtocolMapping.getEthernetInterface()!=null){
					EthernetInterface ethernetInterface = interfaceProtocolMapping.getEthernetInterface();
					ethernetInterface.setEndDate((status==true) ? new Timestamp(new Date().getTime()) : null);
					ethernetInterface.setModifiedBy(modifiedBy);
					ethernetInterfaceRepository.saveAndFlush(ethernetInterface);
					if(!CollectionUtils.isEmpty(ethernetInterface.getAclPolicyCriterias())){
						ethernetInterface.getAclPolicyCriterias().forEach(aclPolicyCriteria -> {
							endDateAclPolicyCriterias(aclPolicyCriteria, modifiedBy, status);
						});
					}
					if(!CollectionUtils.isEmpty(ethernetInterface.getHsrpVrrpProtocols())){
						ethernetInterface.getHsrpVrrpProtocols().forEach(hsrpVrrpProtocol -> {
							endDateHsrpVrrpProtocols(hsrpVrrpProtocol, modifiedBy, status);
						});
					}
				}
				if(interfaceProtocolMapping.getBgp()!=null){
					Bgp bgp = interfaceProtocolMapping.getBgp();
					bgp.setEndDate((status==true) ? new Timestamp(new Date().getTime()) : null);
					bgp.setModifiedBy(modifiedBy);
					bgpRepository.saveAndFlush(bgp);
					if(!CollectionUtils.isEmpty(bgp.getPolicyTypes())){
						bgp.getPolicyTypes().forEach(policyType -> {
							endDatePolicyTypes(policyType, modifiedBy, status);
						});
					}
					if(!CollectionUtils.isEmpty(bgp.getWanStaticRoutes())){
						bgp.getWanStaticRoutes().forEach(wanStaticRoute -> {
							wanStaticRoute.setEndDate((status==true) ? new Timestamp(new Date().getTime()) : null);
							wanStaticRoute.setModifiedBy(modifiedBy);
							wanStaticRouteRepository.saveAndFlush(wanStaticRoute);
						});
					}
				}
				if(interfaceProtocolMapping.getChannelizedE1serialInterface()!=null){
					ChannelizedE1serialInterface channelizedE1serialInterface = interfaceProtocolMapping.getChannelizedE1serialInterface();
					channelizedE1serialInterface.setEndDate((status==true) ? new Timestamp(new Date().getTime()) : null);
					channelizedE1serialInterface.setModifiedBy(modifiedBy);
					channelizedE1serialInterfaceRepository.saveAndFlush(channelizedE1serialInterface);
					if(!CollectionUtils.isEmpty(channelizedE1serialInterface.getAclPolicyCriterias())){
						channelizedE1serialInterface.getAclPolicyCriterias().forEach(aclPolicyCriteria -> {
							endDateAclPolicyCriterias(aclPolicyCriteria, modifiedBy, status);
						});
					}
				}
				if(interfaceProtocolMapping.getChannelizedSdhInterface()!=null){
					ChannelizedSdhInterface channelizedSdhInterface = interfaceProtocolMapping.getChannelizedSdhInterface();
					channelizedSdhInterface.setEndDate((status==true) ? new Timestamp(new Date().getTime()) : null);
					channelizedSdhInterface.setModifiedBy(modifiedBy);
					channelizedSdhInterfaceRepository.saveAndFlush(channelizedSdhInterface);
					if(!CollectionUtils.isEmpty(channelizedSdhInterface.getAclPolicyCriterias())){
						channelizedSdhInterface.getAclPolicyCriterias().forEach(aclPolicyCriteria -> {
							endDateAclPolicyCriterias(aclPolicyCriteria,modifiedBy, status);
						});
					}
				}
				if(interfaceProtocolMapping.getCpe()!=null){
					Cpe cpe = interfaceProtocolMapping.getCpe();
					cpe.setEndDate((status==true) ? new Timestamp(new Date().getTime()) : null);
					cpe.setModifiedBy(modifiedBy);
					cpeRepository.saveAndFlush(cpe);
				}
				if(interfaceProtocolMapping.getEigrp()!=null){
					Eigrp eigrp = interfaceProtocolMapping.getEigrp();
					eigrp.setEndDate((status==true) ? new Timestamp(new Date().getTime()) : null);
					eigrp.setModifiedBy(modifiedBy);
					eigrpRepository.saveAndFlush(eigrp);
				}
				if(interfaceProtocolMapping.getRouterDetail()!=null){
					RouterDetail routerDetail = interfaceProtocolMapping.getRouterDetail();
					routerDetail.setEndDate((status==true) ? new Timestamp(new Date().getTime()) : null);
					routerDetail.setModifiedBy(modifiedBy);
					routerDetailRepository.saveAndFlush(routerDetail);
				}
				if(interfaceProtocolMapping.getRip()!=null){
					Rip rip = interfaceProtocolMapping.getRip();
					rip.setEndDate((status==true) ? new Timestamp(new Date().getTime()) : null);
					rip.setModifiedBy(modifiedBy);
					ripRepository.saveAndFlush(rip);
					if(!CollectionUtils.isEmpty(rip.getPolicyTypes())){
						rip.getPolicyTypes().forEach(policyType -> {
							endDatePolicyTypes(policyType, modifiedBy, status);
						});
					}
				}
				if(interfaceProtocolMapping.getStaticProtocol()!=null){
					StaticProtocol staticProtocol = interfaceProtocolMapping.getStaticProtocol();
					staticProtocol.setEndDate((status==true) ? new Timestamp(new Date().getTime()) : null);
					staticProtocol.setModifiedBy(modifiedBy);
					staticProtocolRepository.saveAndFlush(staticProtocol);
					if(!CollectionUtils.isEmpty(staticProtocol.getPolicyTypes())){
						staticProtocol.getPolicyTypes().forEach(policyType -> {
							endDatePolicyTypes(policyType, modifiedBy, status);
						});
					}
					if(!CollectionUtils.isEmpty(staticProtocol.getWanStaticRoutes())){
						staticProtocol.getWanStaticRoutes().forEach(wanStaticRoute -> {
							wanStaticRoute.setEndDate((status==true) ? new Timestamp(new Date().getTime()) : null);
							wanStaticRoute.setModifiedBy(modifiedBy);
							wanStaticRouteRepository.saveAndFlush(wanStaticRoute);
						});
					}
				}
			});
		}
	}

	private void endDateIpAddresses(Set<IpAddressDetail> ipAddressDetails, String modifiedBy, boolean status){
		logger.info("Inside endDateIpAddresses method");
		if(ipAddressDetails!=null && !ipAddressDetails.isEmpty()){
			ipAddressDetails.forEach(ipAddressDetail -> {
				ipAddressDetail.setEndDate((status==true) ? new Timestamp(new Date().getTime()) : null);
				ipAddressDetail.setModifiedBy(modifiedBy);
				ipAddressDetailRepository.saveAndFlush(ipAddressDetail);
				if(ipAddressDetail.getIpaddrLanv4Addresses()!=null && !ipAddressDetail.getIpaddrLanv4Addresses().isEmpty()){
					ipAddressDetail.getIpaddrLanv4Addresses().forEach(ipaddrLanv4Address -> {
						ipaddrLanv4Address.setEndDate((status==true) ? new Timestamp(new Date().getTime()) : null);
						ipaddrLanv4Address.setModifiedBy(modifiedBy);
						ipaddrLanv4AddressRepository.saveAndFlush(ipaddrLanv4Address);
					});
				}
				if(ipAddressDetail.getIpaddrLanv6Addresses()!=null && !ipAddressDetail.getIpaddrLanv6Addresses().isEmpty()){
					ipAddressDetail.getIpaddrLanv6Addresses().forEach(ipaddrLanv6Address -> {
						ipaddrLanv6Address.setEndDate((status==true) ? new Timestamp(new Date().getTime()) : null);
						ipaddrLanv6Address.setModifiedBy(modifiedBy);
						ipaddrLanv6AddressRepository.saveAndFlush(ipaddrLanv6Address);
					});
				}
				if(ipAddressDetail.getIpaddrWanv4Addresses()!=null && !ipAddressDetail.getIpaddrWanv4Addresses().isEmpty()){
					ipAddressDetail.getIpaddrWanv4Addresses().forEach(ipaddrWanv4Address -> {
						ipaddrWanv4Address.setEndDate((status==true) ? new Timestamp(new Date().getTime()) : null);
						ipaddrWanv4Address.setModifiedBy(modifiedBy);
						ipaddrWanv4AddressRepository.saveAndFlush(ipaddrWanv4Address);
					});
				}
				if(ipAddressDetail.getIpaddrWanv6Addresses()!=null && !ipAddressDetail.getIpaddrWanv6Addresses().isEmpty()){
					ipAddressDetail.getIpaddrWanv6Addresses().forEach(ipaddrWanv6Address -> {
						ipaddrWanv6Address.setEndDate((status==true) ? new Timestamp(new Date().getTime()) : null);
						ipaddrWanv6Address.setModifiedBy(modifiedBy);
						ipaddrWanv6AddressRepository.saveAndFlush(ipaddrWanv6Address);
					});
				}
			});
		}
	}

	@Transactional(readOnly = false)
	private void endDateMulticastings(Set<Multicasting> multicastings, String modifiedBy, boolean status){
		logger.info("Inside endDateMulticastings method");
		if(multicastings!=null && !multicastings.isEmpty()){
			multicastings.forEach(multicasting -> {
				multicasting.setEndDate((status==true) ? new Timestamp(new Date().getTime()) : null);
				multicasting.setModifiedBy(modifiedBy);
				multicastingRepository.saveAndFlush(multicasting);
			});
		}
	}

	private void endDateSchedulerPolicies(Set<AluSchedulerPolicy> aluSchedulerPolicies, String modifiedBy, boolean status){
		logger.info("Inside endDateSchedulerPolicies method");
    	if(aluSchedulerPolicies!=null && !aluSchedulerPolicies.isEmpty()){
			aluSchedulerPolicies.forEach(aluSchedulerPolicy -> {
				aluSchedulerPolicy.setEndDate((status==true) ? new Timestamp(new Date().getTime()) : null);
				aluSchedulerPolicy.setModifiedBy(modifiedBy);
				aluSchedulerPolicyRepository.saveAndFlush(aluSchedulerPolicy);
			});
		}
	}

	private void endDateVrfs(Set<Vrf> vrfs, String modifiedBy, boolean status){
		logger.info("Inside endDateVrfs method");
    	if(vrfs!=null && !vrfs.isEmpty()){
			vrfs.forEach(vrf -> {
				vrf.setEndDate((status==true) ? new Timestamp(new Date().getTime()) : null);
				vrf.setModifiedBy(modifiedBy);
				vrfRepository.saveAndFlush(vrf);
				if(!CollectionUtils.isEmpty(vrf.getPolicyTypes())){
					vrf.getPolicyTypes().forEach(policyType -> {
						policyType.setEndDate((status==true) ? new Timestamp(new Date().getTime()) : null);
						policyType.setModifiedBy(modifiedBy);
						policyTypeRepository.saveAndFlush(policyType);
					});
				}
			});
		}
	}

	private void endDateLmComponents(Set<LmComponent> lmComponents, String modifiedBy, boolean status){
		logger.info("Inside endDateLmComponents method");
    	if(lmComponents!=null && !lmComponents.isEmpty()){
			lmComponents.forEach(lmComponent -> {
				lmComponent.setEndDate((status==true) ? new Timestamp(new Date().getTime()) : null);
				lmComponent.setModifiedBy(modifiedBy);
				lmComponentRepository.saveAndFlush(lmComponent);
				if(!CollectionUtils.isEmpty(lmComponent.getRadwinLastmiles())){
					lmComponent.getRadwinLastmiles().forEach(radwinLastmile -> {
						radwinLastmile.setEndDate((status==true) ? new Timestamp(new Date().getTime()) : null);
						radwinLastmile.setModifiedBy(modifiedBy);
						radwinLastmileRepository.saveAndFlush(radwinLastmile);
					});
				}
				if(lmComponent.getCambiumLastmiles()!=null && !lmComponent.getCambiumLastmiles().isEmpty()){
					lmComponent.getCambiumLastmiles().forEach(cambiumLastmile -> {
						cambiumLastmile.setEndDate((status==true) ? new Timestamp(new Date().getTime()) : null);
						cambiumLastmile.setModifiedBy(modifiedBy);
						cambiumLastmileRepository.saveAndFlush(cambiumLastmile);
					});
				}
				if(lmComponent.getWimaxLastmiles()!=null && !lmComponent.getWimaxLastmiles().isEmpty()){
					lmComponent.getWimaxLastmiles().forEach(wimaxLastmile -> {
						wimaxLastmile.setEndDate((status==true) ? new Timestamp(new Date().getTime()) : null);
						wimaxLastmile.setModifiedBy(modifiedBy);
						wimaxLastmileRepository.saveAndFlush(wimaxLastmile);
					});
				}
			});
		}
	}

	private void endDateVpnSolutions(Set<VpnSolution> vpnSolutions, String modifiedBy, boolean status){
		logger.info("Inside endDateVpnSolutions method");
    	if(vpnSolutions!=null && !vpnSolutions.isEmpty()){
			vpnSolutions.forEach(vpnSolution -> {
				vpnSolution.setEndDate((status==true) ? new Timestamp(new Date().getTime()) : null);
				vpnSolution.setModifiedBy(modifiedBy);
				vpnSolutionRepository.saveAndFlush(vpnSolution);
			});
		}
	}

	private void endDateVpnMetatDatas(Set<VpnMetatData> vpnMetatDatas, String modifiedBy, boolean status) {
		logger.info("Inside endDateVpnMetatDatas method");
    	if(!CollectionUtils.isEmpty(vpnMetatDatas)){
			vpnMetatDatas.forEach(vpnMetatData -> {
				vpnMetatData.setEndDate((status==true) ? new Timestamp(new Date().getTime()) : null);
				vpnMetatData.setModifiedBy(modifiedBy);
				vpnMetatDataRepository.saveAndFlush(vpnMetatData);
			});
		}
	}

	private void endDateServiceDetails(ServiceDetail serviceDetail, String modifiedBy, boolean status) {
		logger.info("Inside endDateServiceDetails method");
    	serviceDetail.setEndDate((status == true) ? new Timestamp(new Date().getTime()) : null);
		serviceDetail.setModifiedBy(modifiedBy);
		serviceDetail.setServiceState((status == true) ? "CANCELLED" : "ACTIVE");
		serviceDetailRepository.saveAndFlush(serviceDetail);
	}

	@Transactional(readOnly = false)
	public void endDateActivationRecords(ServiceDetail serviceDetail, String modifiedBy, boolean status, boolean serviceDetailsEnd){
		logger.info("Inside endDateActivationRecords for serviceCode {}", serviceDetail.getServiceId());
    	endDateAluSchedulerPolicies(serviceDetail.getAluSchedulerPolicies(), modifiedBy, status);
		endDateTopologies(serviceDetail.getTopologies(), modifiedBy,  status);
		endDateCiscoImports(serviceDetail.getCiscoImportMaps(), modifiedBy,  status);
		endDateServiceQos(serviceDetail.getServiceQos(), modifiedBy,  status);
		endDateInterfaceProtocols(serviceDetail.getInterfaceProtocolMappings(), modifiedBy,  status);
		endDateIpAddresses(serviceDetail.getIpAddressDetails(), modifiedBy,  status);
		endDateMulticastings(serviceDetail.getMulticastings(), modifiedBy,  status);
		endDateSchedulerPolicies(serviceDetail.getAluSchedulerPolicies(), modifiedBy,  status);
		endDateVrfs(serviceDetail.getVrfs(), modifiedBy,  status);
		endDateLmComponents(serviceDetail.getLmComponents(), modifiedBy,  status);
		endDateVpnSolutions(serviceDetail.getVpnSolutions(), modifiedBy,  status);
		endDateVpnMetatDatas(serviceDetail.getVpnMetatDatas(), modifiedBy, status);
		if(serviceDetailsEnd) {
			endDateServiceDetails(serviceDetail,modifiedBy,status);
		}
	}

	@Transactional(readOnly = false)
	public void endServiceConfigRecords(IPServiceEndDateBean ipServiceEndDateBean) throws TclCommonException {
		logger.info("endServiceConfigRecords method invoked for serviceCode {}", ipServiceEndDateBean.getServiceId());
		if (ipServiceEndDateBean.getServiceId() != null) {
			String serviceId = ipServiceEndDateBean.getServiceId();
			String modifiedBy = ipServiceEndDateBean.getModifiedBy();
			ServiceDetail serviceDetail = null;

			if (ipServiceEndDateBean.getVersion() == null) {
				logger.info("Version is not provided. Latest Issued Record to be picked");
				serviceDetail = serviceDetailRepository
						.findFirstByServiceIdAndServiceStateAndEndDateIsNullOrderByVersionDesc(serviceId,TaskStatusConstants.ISSUED);
			} else {
				serviceDetail = serviceDetailRepository.findFirstByServiceIdAndVersionOrderByIdDesc(serviceId,
						ipServiceEndDateBean.getVersion());
			}
			if(serviceDetail==null){
				logger.error("No ServiceDetail Record found to be end-dated");
				throw new TclCommonException(ExceptionConstants.NO_SERVICE_ID_RECORD, ResponseResource.R_CODE_ERROR);
			}
			else{
				if (ipServiceEndDateBean.getCurrentDate() == true) {
					logger.info("Activation Records will be end-dated for serviceCode {}", ipServiceEndDateBean.getServiceId());
					endDateActivationRecords(serviceDetail, modifiedBy, true,true);
				} else if (ipServiceEndDateBean.getReverseEndDate() == true) {
					logger.info("End Dates will be reversed for Activation Records for serviceCode {}", ipServiceEndDateBean.getServiceId());
					endDateActivationRecords(serviceDetail, modifiedBy, false,true);
				}
			}
		}
	}

	@Transactional(readOnly = false)
	public String rollbackAndCancel(IPServiceEndDateBean ipServiceEndDateBean) throws TclCommonException{
    	try {
			if (ipServiceEndDateBean.getServiceId() != null) {
				String serviceId = ipServiceEndDateBean.getServiceId();
				String modifiedBy = ipServiceEndDateBean.getModifiedBy();
				ServiceDetail currentServiceDetail = null;
				ServiceDetail lastActiveServiceDetail = null;
				if (ipServiceEndDateBean.getVersion() == null) {
					currentServiceDetail = serviceDetailRepository
							.findFirstByServiceIdAndEndDateIsNullOrderByVersionDesc(serviceId);
				} else {
					currentServiceDetail = serviceDetailRepository.findFirstByServiceIdAndVersionOrderByIdDesc(serviceId,
							ipServiceEndDateBean.getVersion());
				}
				lastActiveServiceDetail = serviceDetailRepository
						.findFirstByServiceIdAndEndDateIsNotNullOrderByEndDateDesc(serviceId);
				if (currentServiceDetail == null && lastActiveServiceDetail == null) {
					throw new TclCommonException(ExceptionConstants.NO_SERVICE_ID_RECORD, ResponseResource.R_CODE_ERROR);
				} else {
					endDateActivationRecords(currentServiceDetail, modifiedBy, true, true);
					endDateActivationRecords(lastActiveServiceDetail, modifiedBy, false, true);
				}
			}
			return "SUCCESS";
		} catch (Exception e){
    		return e.getMessage();
		}
	}

	private void saveMigrationAluSchedulerPolicies(ServiceDetail serviceDetail, ServiceDetailBean serviceDetailBean){

		if(!CollectionUtils.isEmpty(serviceDetailBean.getAluSchedulerPolicyBeans())){
			serviceDetailBean.getAluSchedulerPolicyBeans().forEach(aluSchedulerPolicyBean -> {
				AluSchedulerPolicy aluSchedulerPolicy = new AluSchedulerPolicy();
				aluSchedulerPolicy.setSapEgressPolicyname(aluSchedulerPolicyBean.getSapEgressPolicyname());
				aluSchedulerPolicy.setSapEgressPreprovisioned(convertBooleanToByte(aluSchedulerPolicyBean.getSapEgressPreprovisioned()));
				aluSchedulerPolicy.setSapIngressPolicyname(aluSchedulerPolicyBean.getSapIngressPolicyname());
				aluSchedulerPolicy
						.setSchedulerPolicyIspreprovisioned(convertBooleanToByte(aluSchedulerPolicyBean.getSchedulerPolicyIspreprovisioned()));
				aluSchedulerPolicy.setSchedulerPolicyName(aluSchedulerPolicyBean.getSchedulerPolicyName());
				aluSchedulerPolicy.setSapIngressPreprovisioned(convertBooleanToByte(aluSchedulerPolicyBean.getSapIngressPreprovisioned()));
				aluSchedulerPolicy.setEndDate(null);
				aluSchedulerPolicy.setStartDate(new Timestamp(new Date().getTime()));
				aluSchedulerPolicy.setLastModifiedDate(new Timestamp(new Date().getTime()));
				aluSchedulerPolicy.setModifiedBy("FTI_Refresh");
				aluSchedulerPolicy.setTotalCirBw(aluSchedulerPolicyBean.getTotalCirBw());
				aluSchedulerPolicy.setTotalPirBw(aluSchedulerPolicyBean.getTotalPirBw());
				aluSchedulerPolicy.setServiceDetail(serviceDetail);
				aluSchedulerPolicy.setStartDate(new Timestamp(new Date().getTime()));
				aluSchedulerPolicy.setLastModifiedDate(new Timestamp(new Date().getTime()));
				aluSchedulerPolicyRepository.saveAndFlush(aluSchedulerPolicy);
			});
		}

	}
	
	private void saveMigrationAluSchedulerPolicies(ServiceDetail serviceDetail, ServiceDetail existingServiceDetail){

		if(!CollectionUtils.isEmpty(existingServiceDetail.getAluSchedulerPolicies())){
			existingServiceDetail.getAluSchedulerPolicies().forEach(aluSchedulerPolicyBean -> {
				AluSchedulerPolicy aluSchedulerPolicy = new AluSchedulerPolicy();
				aluSchedulerPolicy.setSapEgressPolicyname(aluSchedulerPolicyBean.getSapEgressPolicyname());
				aluSchedulerPolicy.setSapEgressPreprovisioned(aluSchedulerPolicyBean.getSapEgressPreprovisioned());
				aluSchedulerPolicy.setSapIngressPolicyname(aluSchedulerPolicyBean.getSapIngressPolicyname());
				aluSchedulerPolicy
						.setSchedulerPolicyIspreprovisioned(aluSchedulerPolicyBean.getSchedulerPolicyIspreprovisioned());
				aluSchedulerPolicy.setSchedulerPolicyName(aluSchedulerPolicyBean.getSchedulerPolicyName());
				aluSchedulerPolicy.setSapIngressPreprovisioned(aluSchedulerPolicyBean.getSapIngressPreprovisioned());
				aluSchedulerPolicy.setEndDate(null);
				aluSchedulerPolicy.setStartDate(new Timestamp(new Date().getTime()));
				aluSchedulerPolicy.setLastModifiedDate(new Timestamp(new Date().getTime()));
				aluSchedulerPolicy.setModifiedBy("FTI_Refresh");
				aluSchedulerPolicy.setTotalCirBw(aluSchedulerPolicyBean.getTotalCirBw());
				aluSchedulerPolicy.setTotalPirBw(aluSchedulerPolicyBean.getTotalPirBw());
				aluSchedulerPolicy.setServiceDetail(serviceDetail);
				aluSchedulerPolicy.setStartDate(new Timestamp(new Date().getTime()));
				aluSchedulerPolicy.setLastModifiedDate(new Timestamp(new Date().getTime()));
				aluSchedulerPolicyRepository.saveAndFlush(aluSchedulerPolicy);
			});
		}

	}

	private CiscoImportMap getMigrationCiscoImportMap(ServiceDetail serviceDetail, CiscoImportMapBean ciscoImportMapBean){
		CiscoImportMap ciscoImportMap = new CiscoImportMap();
		ciscoImportMap.setDescription(ciscoImportMapBean.getDescription());
		ciscoImportMap.setEndDate(ciscoImportMapBean.getEndDate());
		ciscoImportMap.setLastModifiedDate(ciscoImportMapBean.getLastModifiedDate());
		ciscoImportMap.setModifiedBy(ciscoImportMapBean.getModifiedBy());
		ciscoImportMap.setServiceDetail(serviceDetail);
		return ciscoImportMapRepository.saveAndFlush(ciscoImportMap);
	}
	
	private CiscoImportMap getMigrationCiscoImportMap(ServiceDetail serviceDetail, CiscoImportMap ciscoImportMapBean){
		CiscoImportMap ciscoImportMap = new CiscoImportMap();
		ciscoImportMap.setDescription(ciscoImportMapBean.getDescription());
		ciscoImportMap.setEndDate(ciscoImportMapBean.getEndDate());
		ciscoImportMap.setLastModifiedDate(ciscoImportMapBean.getLastModifiedDate());
		ciscoImportMap.setModifiedBy(ciscoImportMapBean.getModifiedBy());
		ciscoImportMap.setServiceDetail(serviceDetail);
		return ciscoImportMapRepository.saveAndFlush(ciscoImportMap);
	}

	private void saveMigrationCiscoImportMaps(ServiceDetail serviceDetail, ServiceDetailBean serviceDetailBean) {
		if (!CollectionUtils.isEmpty(serviceDetailBean.getCiscoImportMapBeans())) {
			serviceDetailBean.getCiscoImportMapBeans().forEach(ciscoImportMapBean -> {
				CiscoImportMap ciscoImportMap = getMigrationCiscoImportMap(serviceDetail, ciscoImportMapBean);

				if (!CollectionUtils.isEmpty(ciscoImportMapBean.getPolicyTypeCriteriaMappingBeans())) {
					ciscoImportMapBean.getPolicyTypeCriteriaMappingBeans().forEach(policyTypeCriteriaMappingBean -> {
						PolicyTypeCriteriaMapping policyTypeCriteriaMapping = new PolicyTypeCriteriaMapping();
						policyTypeCriteriaMapping.setVersion(policyTypeCriteriaMappingBean.getVersion());
						policyTypeCriteriaMapping.setPolicyType(savePolicyType(policyTypeCriteriaMappingBean));
						policyTypeCriteriaMapping.setCiscoImportMap(ciscoImportMap);
						policyTypeCriteriaMappingRepository.saveAndFlush(policyTypeCriteriaMapping);
					});
				}
			});
		}
	}
	
	private void saveMigrationCiscoImportMaps(ServiceDetail serviceDetail, ServiceDetail serviceDetailBean) {
		if (!CollectionUtils.isEmpty(serviceDetailBean.getCiscoImportMaps())) {
			serviceDetailBean.getCiscoImportMaps().forEach(ciscoImportMapBean -> {
				CiscoImportMap ciscoImportMap = getMigrationCiscoImportMap(serviceDetail, ciscoImportMapBean);

				if (!CollectionUtils.isEmpty(ciscoImportMapBean.getPolicyTypeCriteriaMappings())) {
					ciscoImportMapBean.getPolicyTypeCriteriaMappings().forEach(policyTypeCriteriaMappingBean -> {
						PolicyTypeCriteriaMapping policyTypeCriteriaMapping = new PolicyTypeCriteriaMapping();
						policyTypeCriteriaMapping.setVersion(policyTypeCriteriaMappingBean.getVersion());
						policyTypeCriteriaMapping.setPolicyType(savePolicyType(policyTypeCriteriaMappingBean));
						policyTypeCriteriaMapping.setCiscoImportMap(ciscoImportMap);
						policyTypeCriteriaMappingRepository.saveAndFlush(policyTypeCriteriaMapping);
					});
				}
			});
		}
	}

	private PolicyType savePolicyType(PolicyTypeCriteriaMappingBean policyTypeCriteriaMappingBean) {
		PolicyType policyType = new PolicyType();
		return policyType;
	}
	
	private PolicyType savePolicyType(PolicyTypeCriteriaMapping policyTypeCriteriaMappingBean) {
		PolicyType policyType = new PolicyType();
		return policyType;
	}

	private IpAddressDetail getMigrationIpAddressDetail(ServiceDetail serviceDetail, IpAddressDetailBean ipAddressDetailBean){
		logger.info("getMigrationIpAddressDetail method invoked for serviceid::{}",serviceDetail.getId());
		IpAddressDetail ipAddressDetail = new IpAddressDetail();
		ipAddressDetail.setExtendedLanEnabled(convertBooleanToByte(ipAddressDetailBean.getExtendedLanEnabled()));
		ipAddressDetail.setNmsServiceIpv4Address(ipAddressDetailBean.getNmsServiceIpv4Address());
		ipAddressDetail.setNniVsatIpaddress(ipAddressDetailBean.getNniVsatIpaddress());
		ipAddressDetail.setNoMacAddress(ipAddressDetailBean.getNoMacAddress());
		ipAddressDetail.setPathIpType(ipAddressDetailBean.getPathIpType());
		ipAddressDetail.setPingAddress1(ipAddressDetailBean.getPingAddress1());
		ipAddressDetail.setPingAddress2(ipAddressDetailBean.getPingAddress2());
		ipAddressDetail.setServiceDetail(serviceDetail);
		ipAddressDetail.setEndDate(null);
		ipAddressDetail.setStartDate(new Timestamp(new Date().getTime()));
		ipAddressDetail.setLastModifiedDate(new Timestamp(new Date().getTime()));
		ipAddressDetail.setModifiedBy("FTI_Refresh");
		return ipAddressDetailRepository.saveAndFlush(ipAddressDetail);
	}
	
	private IpAddressDetail getMigrationIpAddressDetail(ServiceDetail serviceDetail, IpAddressDetail ipAddressDetailBean){
		IpAddressDetail ipAddressDetail = new IpAddressDetail();
		ipAddressDetail.setExtendedLanEnabled(ipAddressDetailBean.getExtendedLanEnabled());
		ipAddressDetail.setNmsServiceIpv4Address(ipAddressDetailBean.getNmsServiceIpv4Address());
		ipAddressDetail.setNniVsatIpaddress(ipAddressDetailBean.getNniVsatIpaddress());
		ipAddressDetail.setNoMacAddress(ipAddressDetailBean.getNoMacAddress());
		ipAddressDetail.setPathIpType(ipAddressDetailBean.getPathIpType());
		ipAddressDetail.setPingAddress1(ipAddressDetailBean.getPingAddress1());
		ipAddressDetail.setPingAddress2(ipAddressDetailBean.getPingAddress2());
		ipAddressDetail.setServiceDetail(serviceDetail);
		ipAddressDetail.setEndDate(null);
		ipAddressDetail.setStartDate(new Timestamp(new Date().getTime()));
		ipAddressDetail.setLastModifiedDate(new Timestamp(new Date().getTime()));
		ipAddressDetail.setModifiedBy("FTI_Refresh");
		return ipAddressDetailRepository.saveAndFlush(ipAddressDetail);
	}

	private void saveMigrationIpAddresses(ServiceDetail serviceDetail, ServiceDetailBean serviceDetailBean){
		logger.info("saveMigrationIpAddresses method invoked for serviceid::{}",serviceDetail.getId());
		if(serviceDetailBean.getIpAddressDetailBeans()!=null && !serviceDetailBean.getIpAddressDetailBeans().isEmpty()){
			logger.info("serviceDetailBean.getIpAddressDetailBeans() exists for serviceid::{}",serviceDetail.getId());
			serviceDetailBean.getIpAddressDetailBeans().forEach(ipAddressDetailBean -> {
				
				IpAddressDetail ipAddressDetail = getMigrationIpAddressDetail(serviceDetail, ipAddressDetailBean);

				if(ipAddressDetailBean.getIpaddrLanv4Addresses()!=null && !ipAddressDetailBean.getIpaddrLanv4Addresses().isEmpty()){
					ipAddressDetailBean.getIpaddrLanv4Addresses().forEach(ipaddrLanv4AddressBean -> {
						IpaddrLanv4Address ipaddrLanv4Address = new IpaddrLanv4Address();
						ipaddrLanv4Address.setLanv4Address(ipaddrLanv4AddressBean.getLanv4Address());
						ipaddrLanv4Address.setIssecondary(convertBooleanToByte(ipaddrLanv4AddressBean.getIssecondary()));
						ipaddrLanv4Address.setIscustomerprovided(convertBooleanToByte(ipaddrLanv4AddressBean.getIscustomerprovided()));
						ipaddrLanv4Address.setEndDate(null);
						ipaddrLanv4Address.setStartDate(new Timestamp(new Date().getTime()));
						ipaddrLanv4Address.setLastModifiedDate(new Timestamp(new Date().getTime()));
						ipaddrLanv4Address.setModifiedBy("FTI_Refresh");
						ipaddrLanv4Address.setIpAddressDetail(ipAddressDetail);
						ipaddrLanv4Address.setStartDate(new Timestamp(new Date().getTime()));
						ipaddrLanv4Address.setLastModifiedDate(new Timestamp(new Date().getTime()));
						ipaddrLanv4AddressRepository.saveAndFlush(ipaddrLanv4Address);
					});
				}

				if(ipAddressDetailBean.getIpaddrLanv6Addresses()!=null && !ipAddressDetailBean.getIpaddrLanv6Addresses().isEmpty()){
					ipAddressDetailBean.getIpaddrLanv6Addresses().forEach(ipaddrLanv6AddressBean -> {
						IpaddrLanv6Address ipaddrLanv6Address = new IpaddrLanv6Address();
						ipaddrLanv6Address.setIscustomerprovided(convertBooleanToByte(ipaddrLanv6AddressBean.getIscustomerprovided()));
						ipaddrLanv6Address.setIssecondary(convertBooleanToByte(ipaddrLanv6AddressBean.getIssecondary()));
						ipaddrLanv6Address.setLanv6Address(ipaddrLanv6AddressBean.getLanv6Address());
						ipaddrLanv6Address.setEndDate(null);
						ipaddrLanv6Address.setStartDate(new Timestamp(new Date().getTime()));
						ipaddrLanv6Address.setLastModifiedDate(new Timestamp(new Date().getTime()));
						ipaddrLanv6Address.setModifiedBy("FTI_Refresh");
						ipaddrLanv6Address.setIpAddressDetail(ipAddressDetail);
						ipaddrLanv6Address.setStartDate(new Timestamp(new Date().getTime()));
						ipaddrLanv6Address.setLastModifiedDate(new Timestamp(new Date().getTime()));
						ipaddrLanv6AddressRepository.saveAndFlush(ipaddrLanv6Address);
					});
				}

				if(ipAddressDetailBean.getIpaddrWanv4Addresses()!=null && !ipAddressDetailBean.getIpaddrWanv4Addresses().isEmpty()){
					ipAddressDetailBean.getIpaddrWanv4Addresses().forEach(ipaddrWanv4AddressBean -> {
						IpaddrWanv4Address ipaddrWanv4Address = new IpaddrWanv4Address();
						ipaddrWanv4Address.setIscustomerprovided(convertBooleanToByte(ipaddrWanv4AddressBean.getIscustomerprovided()));
						ipaddrWanv4Address.setIssecondary(convertBooleanToByte(ipaddrWanv4AddressBean.getIssecondary()));
						ipaddrWanv4Address.setEndDate(null);
						ipaddrWanv4Address.setStartDate(new Timestamp(new Date().getTime()));
						ipaddrWanv4Address.setLastModifiedDate(new Timestamp(new Date().getTime()));
						ipaddrWanv4Address.setModifiedBy("FTI_Refresh");
						ipaddrWanv4Address.setWanv4Address(ipaddrWanv4AddressBean.getWanv4Address());
						ipaddrWanv4Address.setIpAddressDetail(ipAddressDetail);
						ipaddrWanv4Address.setStartDate(new Timestamp(new Date().getTime()));
						ipaddrWanv4Address.setLastModifiedDate(new Timestamp(new Date().getTime()));
						ipaddrWanv4AddressRepository.saveAndFlush(ipaddrWanv4Address);
					});
				}

				if(ipAddressDetailBean.getIpaddrWanv6Addresses()!=null && !ipAddressDetailBean.getIpaddrWanv6Addresses().isEmpty()){
					ipAddressDetailBean.getIpaddrWanv6Addresses().forEach(ipaddrWanv6AddressBean -> {
						IpaddrWanv6Address ipaddrWanv6Address = new IpaddrWanv6Address();
						ipaddrWanv6Address.setIscustomerprovided(convertBooleanToByte(ipaddrWanv6AddressBean.getIscustomerprovided()));
						ipaddrWanv6Address.setIssecondary(convertBooleanToByte(ipaddrWanv6AddressBean.getIssecondary()));
						ipaddrWanv6Address.setEndDate(null);
						ipaddrWanv6Address.setStartDate(new Timestamp(new Date().getTime()));
						ipaddrWanv6Address.setLastModifiedDate(new Timestamp(new Date().getTime()));
						ipaddrWanv6Address.setModifiedBy("FTI_Refresh");
						ipaddrWanv6Address.setWanv6Address(ipaddrWanv6AddressBean.getWanv6Address());
						ipaddrWanv6Address.setIpAddressDetail(ipAddressDetail);
						ipaddrWanv6Address.setStartDate(new Timestamp(new Date().getTime()));
						ipaddrWanv6Address.setLastModifiedDate(new Timestamp(new Date().getTime()));
						ipaddrWanv6AddressRepository.saveAndFlush(ipaddrWanv6Address);
					});
				}

			});
		}
	}
	
	
	private void saveMigrationIpAddresses(ServiceDetail serviceDetail, ServiceDetail existingServiceDetail){
		if(existingServiceDetail.getIpAddressDetails()!=null && !existingServiceDetail.getIpAddressDetails().isEmpty()){
			existingServiceDetail.getIpAddressDetails().forEach(ipAddressDetailBean -> {

				IpAddressDetail ipAddressDetail = getMigrationIpAddressDetail(serviceDetail, ipAddressDetailBean);

				if(ipAddressDetailBean.getIpaddrLanv4Addresses()!=null && !ipAddressDetailBean.getIpaddrLanv4Addresses().isEmpty()){
					ipAddressDetailBean.getIpaddrLanv4Addresses().forEach(ipaddrLanv4AddressBean -> {
						IpaddrLanv4Address ipaddrLanv4Address = new IpaddrLanv4Address();
						ipaddrLanv4Address.setLanv4Address(ipaddrLanv4AddressBean.getLanv4Address());
						ipaddrLanv4Address.setIssecondary(ipaddrLanv4AddressBean.getIssecondary());
						ipaddrLanv4Address.setIscustomerprovided(ipaddrLanv4AddressBean.getIscustomerprovided());
						ipaddrLanv4Address.setEndDate(null);
						ipaddrLanv4Address.setStartDate(new Timestamp(new Date().getTime()));
						ipaddrLanv4Address.setLastModifiedDate(new Timestamp(new Date().getTime()));
						ipaddrLanv4Address.setModifiedBy("FTI_Refresh");
						ipaddrLanv4Address.setIpAddressDetail(ipAddressDetail);
						ipaddrLanv4Address.setStartDate(new Timestamp(new Date().getTime()));
						ipaddrLanv4Address.setLastModifiedDate(new Timestamp(new Date().getTime()));
						ipaddrLanv4AddressRepository.saveAndFlush(ipaddrLanv4Address);
					});
				}

				if(ipAddressDetailBean.getIpaddrLanv6Addresses()!=null && !ipAddressDetailBean.getIpaddrLanv6Addresses().isEmpty()){
					ipAddressDetailBean.getIpaddrLanv6Addresses().forEach(ipaddrLanv6AddressBean -> {
						IpaddrLanv6Address ipaddrLanv6Address = new IpaddrLanv6Address();
						ipaddrLanv6Address.setIscustomerprovided(ipaddrLanv6AddressBean.getIscustomerprovided());
						ipaddrLanv6Address.setIssecondary(ipaddrLanv6AddressBean.getIssecondary());
						ipaddrLanv6Address.setLanv6Address(ipaddrLanv6AddressBean.getLanv6Address());
						ipaddrLanv6Address.setEndDate(null);
						ipaddrLanv6Address.setStartDate(new Timestamp(new Date().getTime()));
						ipaddrLanv6Address.setLastModifiedDate(new Timestamp(new Date().getTime()));
						ipaddrLanv6Address.setModifiedBy("FTI_Refresh");
						ipaddrLanv6Address.setIpAddressDetail(ipAddressDetail);
						ipaddrLanv6Address.setStartDate(new Timestamp(new Date().getTime()));
						ipaddrLanv6Address.setLastModifiedDate(new Timestamp(new Date().getTime()));
						ipaddrLanv6AddressRepository.saveAndFlush(ipaddrLanv6Address);
					});
				}

				if(ipAddressDetailBean.getIpaddrWanv4Addresses()!=null && !ipAddressDetailBean.getIpaddrWanv4Addresses().isEmpty()){
					ipAddressDetailBean.getIpaddrWanv4Addresses().forEach(ipaddrWanv4AddressBean -> {
						IpaddrWanv4Address ipaddrWanv4Address = new IpaddrWanv4Address();
						ipaddrWanv4Address.setIscustomerprovided(ipaddrWanv4AddressBean.getIscustomerprovided());
						ipaddrWanv4Address.setIssecondary(ipaddrWanv4AddressBean.getIssecondary());
						ipaddrWanv4Address.setEndDate(null);
						ipaddrWanv4Address.setStartDate(new Timestamp(new Date().getTime()));
						ipaddrWanv4Address.setLastModifiedDate(new Timestamp(new Date().getTime()));
						ipaddrWanv4Address.setModifiedBy("FTI_Refresh");
						ipaddrWanv4Address.setWanv4Address(ipaddrWanv4AddressBean.getWanv4Address());
						ipaddrWanv4Address.setIpAddressDetail(ipAddressDetail);
						ipaddrWanv4Address.setStartDate(new Timestamp(new Date().getTime()));
						ipaddrWanv4Address.setLastModifiedDate(new Timestamp(new Date().getTime()));
						ipaddrWanv4AddressRepository.saveAndFlush(ipaddrWanv4Address);
					});
				}

				if(ipAddressDetailBean.getIpaddrWanv6Addresses()!=null && !ipAddressDetailBean.getIpaddrWanv6Addresses().isEmpty()){
					ipAddressDetailBean.getIpaddrWanv6Addresses().forEach(ipaddrWanv6AddressBean -> {
						IpaddrWanv6Address ipaddrWanv6Address = new IpaddrWanv6Address();
						ipaddrWanv6Address.setIscustomerprovided(ipaddrWanv6AddressBean.getIscustomerprovided());
						ipaddrWanv6Address.setIssecondary(ipaddrWanv6AddressBean.getIssecondary());
						ipaddrWanv6Address.setEndDate(null);
						ipaddrWanv6Address.setStartDate(new Timestamp(new Date().getTime()));
						ipaddrWanv6Address.setLastModifiedDate(new Timestamp(new Date().getTime()));
						ipaddrWanv6Address.setModifiedBy("FTI_Refresh");
						ipaddrWanv6Address.setWanv6Address(ipaddrWanv6AddressBean.getWanv6Address());
						ipaddrWanv6Address.setIpAddressDetail(ipAddressDetail);
						ipaddrWanv6Address.setStartDate(new Timestamp(new Date().getTime()));
						ipaddrWanv6Address.setLastModifiedDate(new Timestamp(new Date().getTime()));
						ipaddrWanv6AddressRepository.saveAndFlush(ipaddrWanv6Address);
					});
				}

			});
		}
	}

	private void saveMigrationMulticastings(ServiceDetail serviceDetail, ServiceDetailBean serviceDetailBean) {
		if(serviceDetailBean.getMulticastingBeans()!=null && !serviceDetailBean.getMulticastingBeans().isEmpty()){
			serviceDetailBean.getMulticastingBeans().forEach(multiCastingBean -> {
				Multicasting multiCasting = new Multicasting();
				multiCasting.setAutoDiscoveryOption(multiCastingBean.getAutoDiscoveryOption());
				multiCasting.setDataMdt(multiCastingBean.getDataMdt());
				multiCasting.setDataMdtThreshold(multiCastingBean.getDataMdtThreshold());
				multiCasting.setDefaultMdt(multiCastingBean.getDefaultMdt());
				multiCasting.setMulticatingId(multiCastingBean.getMulticatingId());
				multiCasting.setRpAddress(multiCastingBean.getRpAddress());
				multiCasting.setRpLocation(multiCastingBean.getRpLocation());
				multiCasting.setStartDate(new Timestamp(new Date().getTime()));
				multiCasting.setWanPimMode(multiCastingBean.getWanPimMode());
				multiCasting.setEndDate(null);
				multiCasting.setType(multiCastingBean.getType());
				multiCasting.setStartDate(new Timestamp(new Date().getTime()));
				multiCasting.setLastModifiedDate(new Timestamp(new Date().getTime()));
				multiCasting.setModifiedBy("FTI_Refresh");
				multiCasting.setServiceDetail(serviceDetail);
				multicastingRepository.saveAndFlush(multiCasting);
			});
		}
	}
	
	private void saveMigrationMulticastings(ServiceDetail serviceDetail, ServiceDetail existingServiceDetail) {
		if(existingServiceDetail.getMulticastings() !=null && !existingServiceDetail.getMulticastings().isEmpty()){
			existingServiceDetail.getMulticastings().forEach(multiCastingBean -> {
				Multicasting multiCasting = new Multicasting();
				multiCasting.setAutoDiscoveryOption(multiCastingBean.getAutoDiscoveryOption());
				multiCasting.setDataMdt(multiCastingBean.getDataMdt());
				multiCasting.setDataMdtThreshold(multiCastingBean.getDataMdtThreshold());
				multiCasting.setDefaultMdt(multiCastingBean.getDefaultMdt());
				multiCasting.setMulticatingId(multiCastingBean.getMulticatingId());
				multiCasting.setRpAddress(multiCastingBean.getRpAddress());
				multiCasting.setRpLocation(multiCastingBean.getRpLocation());
				multiCasting.setStartDate(new Timestamp(new Date().getTime()));
				multiCasting.setWanPimMode(multiCastingBean.getWanPimMode());
				multiCasting.setEndDate(null);
				multiCasting.setType(multiCastingBean.getType());
				multiCasting.setStartDate(new Timestamp(new Date().getTime()));
				multiCasting.setLastModifiedDate(new Timestamp(new Date().getTime()));
				multiCasting.setModifiedBy("FTI_Refresh");
				multiCasting.setServiceDetail(serviceDetail);
				multicastingRepository.saveAndFlush(multiCasting);
			});
		}
	}

	private ServiceQo getMigrationServiceQo(ServiceDetail serviceDetail, ServiceQoBean serviceQoBean){
		ServiceQo serviceQo = new ServiceQo();
		serviceQo.setCosPackage(serviceQoBean.getCosPackage());
		serviceQo.setCosProfile(serviceQoBean.getCosProfile());
		serviceQo.setCosType(serviceQoBean.getCosType());
		serviceQo.setCosUpdateAction(serviceQoBean.getCosUpdateAction());
		serviceQo.setFlexiCosIdentifier(serviceQoBean.getFlexiCosIdentifier());
		serviceQo.setIsbandwidthApplicable(convertBooleanToByte(serviceQoBean.getIsbandwidthApplicable()));
		serviceQo.setIsdefaultFc(convertBooleanToByte(serviceQoBean.getIsdefaultFc()));
		serviceQo.setIsflexicos(convertBooleanToByte(serviceQoBean.getIsflexicos()));
		serviceQo.setNcTraffic(convertBooleanToByte(serviceQoBean.getNcTraffic()));
		serviceQo.setPirBw(serviceQoBean.getPirBw());
		serviceQo.setPirBwUnit(serviceQoBean.getPirBwUnit());
		serviceQo.setQosPolicyname(serviceQoBean.getQosPolicyname());
		serviceQo.setChildqosPolicyname(serviceQoBean.getChildqosPolicyname());
		serviceQo.setQosTrafiicMode(serviceQoBean.getQosTrafiicMode());
		serviceQo.setSummationOfBw(serviceQoBean.getSummationOfBw());
		serviceQo.setEndDate(null);
		serviceQo.setStartDate(new Timestamp(new Date().getTime()));
		serviceQo.setLastModifiedDate(new Timestamp(new Date().getTime()));
		serviceQo.setModifiedBy("FTI_Refresh");
		serviceQo.setServiceDetail(serviceDetail);
		return serviceQoRepository.saveAndFlush(serviceQo);
	}
	
	private ServiceQo getMigrationServiceQo(ServiceDetail serviceDetail, ServiceQo serviceQoBean){
		ServiceQo serviceQo = new ServiceQo();
		serviceQo.setCosPackage(serviceQoBean.getCosPackage());
		serviceQo.setCosProfile(serviceQoBean.getCosProfile());
		serviceQo.setCosType(serviceQoBean.getCosType());
		serviceQo.setCosUpdateAction(serviceQoBean.getCosUpdateAction());
		serviceQo.setFlexiCosIdentifier(serviceQoBean.getFlexiCosIdentifier());
		serviceQo.setIsbandwidthApplicable(serviceQoBean.getIsbandwidthApplicable());
		serviceQo.setIsdefaultFc(serviceQoBean.getIsdefaultFc());
		serviceQo.setIsflexicos(serviceQoBean.getIsflexicos());
		serviceQo.setNcTraffic(serviceQoBean.getNcTraffic());
		serviceQo.setPirBw(serviceQoBean.getPirBw());
		serviceQo.setPirBwUnit(serviceQoBean.getPirBwUnit());
		serviceQo.setQosPolicyname(serviceQoBean.getQosPolicyname());
		serviceQo.setChildqosPolicyname(serviceQoBean.getChildqosPolicyname());
		serviceQo.setQosTrafiicMode(serviceQoBean.getQosTrafiicMode());
		serviceQo.setSummationOfBw(serviceQoBean.getSummationOfBw());
		serviceQo.setEndDate(null);
		serviceQo.setStartDate(new Timestamp(new Date().getTime()));
		serviceQo.setLastModifiedDate(new Timestamp(new Date().getTime()));
		serviceQo.setModifiedBy("FTI_Refresh");
		serviceQo.setServiceDetail(serviceDetail);
		return serviceQoRepository.saveAndFlush(serviceQo);
	}

	private ServiceCosCriteria getMigrationServiceCosCriteria(ServiceQo serviceQo, ServiceCosCriteriaBean serviceCosCriteriaBean){
		ServiceCosCriteria serviceCosCriteria = new ServiceCosCriteria();
		serviceCosCriteria.setBwBpsunit(serviceCosCriteriaBean.getBwBpsunit());
		serviceCosCriteria.setDhcpVal1(serviceCosCriteriaBean.getDhcpVal1());
		serviceCosCriteria.setDhcpVal2(serviceCosCriteriaBean.getDhcpVal2());
		serviceCosCriteria.setDhcpVal3(serviceCosCriteriaBean.getDhcpVal3());
		serviceCosCriteria.setDhcpVal4(serviceCosCriteriaBean.getDhcpVal4());
		serviceCosCriteria.setDhcpVal5(serviceCosCriteriaBean.getDhcpVal5());
		serviceCosCriteria.setDhcpVal6(serviceCosCriteriaBean.getDhcpVal6());
		serviceCosCriteria.setDhcpVal7(serviceCosCriteriaBean.getDhcpVal7());
		serviceCosCriteria.setDhcpVal8(serviceCosCriteriaBean.getDhcpVal8());
		serviceCosCriteria.setIpprecedenceVal1(serviceCosCriteriaBean.getIpprecedenceVal1());
		serviceCosCriteria.setIpprecedenceVal2(serviceCosCriteriaBean.getIpprecedenceVal2());
		serviceCosCriteria.setIpprecedenceVal3(serviceCosCriteriaBean.getIpprecedenceVal3());
		serviceCosCriteria.setIpprecedenceVal4(serviceCosCriteriaBean.getIpprecedenceVal4());
		serviceCosCriteria.setIpprecedenceVal5(serviceCosCriteriaBean.getIpprecedenceVal5());
		serviceCosCriteria.setIpprecedenceVal6(serviceCosCriteriaBean.getIpprecedenceVal6());
		serviceCosCriteria.setIpprecedenceVal7(serviceCosCriteriaBean.getIpprecedenceVal7());
		serviceCosCriteria.setIpprecedenceVal8(serviceCosCriteriaBean.getIpprecedenceVal8());
		serviceCosCriteria.setClassificationCriteria(serviceCosCriteriaBean.getClassificationCriteria()!=null?serviceCosCriteriaBean.getClassificationCriteria():"ANY");
		serviceCosCriteria.setCosName(serviceCosCriteriaBean.getCosName());
		serviceCosCriteria.setCosPercent(serviceCosCriteriaBean.getCosPercent());
		serviceCosCriteria.setServiceQo(serviceQo);
		serviceCosCriteria.setEndDate(null);
		serviceCosCriteria.setStartDate(new Timestamp(new Date().getTime()));
		serviceCosCriteria.setLastModifiedDate(new Timestamp(new Date().getTime()));
		serviceCosCriteria.setModifiedBy("FTI_Refresh");
		return serviceCosCriteriaRepository.saveAndFlush(serviceCosCriteria);
	}
	
	private ServiceCosCriteria getMigrationServiceCosCriteria(ServiceQo serviceQo, ServiceCosCriteria serviceCosCriteriaBean){
		ServiceCosCriteria serviceCosCriteria = new ServiceCosCriteria();
		serviceCosCriteria.setBwBpsunit(serviceCosCriteriaBean.getBwBpsunit());
		serviceCosCriteria.setDhcpVal1(serviceCosCriteriaBean.getDhcpVal1());
		serviceCosCriteria.setDhcpVal2(serviceCosCriteriaBean.getDhcpVal2());
		serviceCosCriteria.setDhcpVal3(serviceCosCriteriaBean.getDhcpVal3());
		serviceCosCriteria.setDhcpVal4(serviceCosCriteriaBean.getDhcpVal4());
		serviceCosCriteria.setDhcpVal5(serviceCosCriteriaBean.getDhcpVal5());
		serviceCosCriteria.setDhcpVal6(serviceCosCriteriaBean.getDhcpVal6());
		serviceCosCriteria.setDhcpVal7(serviceCosCriteriaBean.getDhcpVal7());
		serviceCosCriteria.setDhcpVal8(serviceCosCriteriaBean.getDhcpVal8());
		serviceCosCriteria.setIpprecedenceVal1(serviceCosCriteriaBean.getIpprecedenceVal1());
		serviceCosCriteria.setIpprecedenceVal2(serviceCosCriteriaBean.getIpprecedenceVal2());
		serviceCosCriteria.setIpprecedenceVal3(serviceCosCriteriaBean.getIpprecedenceVal3());
		serviceCosCriteria.setIpprecedenceVal4(serviceCosCriteriaBean.getIpprecedenceVal4());
		serviceCosCriteria.setIpprecedenceVal5(serviceCosCriteriaBean.getIpprecedenceVal5());
		serviceCosCriteria.setIpprecedenceVal6(serviceCosCriteriaBean.getIpprecedenceVal6());
		serviceCosCriteria.setIpprecedenceVal7(serviceCosCriteriaBean.getIpprecedenceVal7());
		serviceCosCriteria.setIpprecedenceVal8(serviceCosCriteriaBean.getIpprecedenceVal8());
		serviceCosCriteria.setClassificationCriteria(serviceCosCriteriaBean.getClassificationCriteria());
		serviceCosCriteria.setCosName(serviceCosCriteriaBean.getCosName());
		serviceCosCriteria.setCosPercent(serviceCosCriteriaBean.getCosPercent());
		serviceCosCriteria.setServiceQo(serviceQo);
		serviceCosCriteria.setEndDate(null);
		serviceCosCriteria.setStartDate(new Timestamp(new Date().getTime()));
		serviceCosCriteria.setLastModifiedDate(new Timestamp(new Date().getTime()));
		serviceCosCriteria.setModifiedBy("FTI_Refresh");
		return serviceCosCriteriaRepository.saveAndFlush(serviceCosCriteria);
	}

	private AclPolicyCriteria saveMigrationAclPolicyCriterias(AclPolicyCriteriaBean aclPolicyCriteriaBean){
		AclPolicyCriteria aclPolicyCriteria = new AclPolicyCriteria();
		aclPolicyCriteria.setAction(aclPolicyCriteriaBean.getAction());
		aclPolicyCriteria.setDescription(aclPolicyCriteriaBean.getDescription());
		aclPolicyCriteria.setDestinationAny(convertBooleanToByte(aclPolicyCriteriaBean.getDestinationAny()));
		aclPolicyCriteria.setDestinationCondition(aclPolicyCriteriaBean.getDestinationCondition());
		aclPolicyCriteria.setDestinationPortnumber(aclPolicyCriteriaBean.getDestinationPortnumber());
		aclPolicyCriteria.setDestinationSubnet(aclPolicyCriteriaBean.getDestinationSubnet());
		aclPolicyCriteria.setInboundIpv4AclName(aclPolicyCriteriaBean.getInboundIpv4AclName());
		aclPolicyCriteria.setInboundIpv6AclName(aclPolicyCriteriaBean.getInboundIpv6AclName());
		aclPolicyCriteria.setIsinboundaclIpv4Applied(convertBooleanToByte(aclPolicyCriteriaBean.getIsinboundaclIpv4Applied()));
		aclPolicyCriteria.setIsinboundaclIpv4Preprovisioned(convertBooleanToByte(aclPolicyCriteriaBean.getIsinboundaclIpv4Preprovisioned()));
		aclPolicyCriteria.setIsinboundaclIpv6Applied(convertBooleanToByte(aclPolicyCriteriaBean.getIsinboundaclIpv6Applied()));
		aclPolicyCriteria.setIsinboundaclIpv6Preprovisioned(convertBooleanToByte(aclPolicyCriteriaBean.getIsinboundaclIpv6Preprovisioned()));
		aclPolicyCriteria.setIsoutboundaclIpv4Applied(convertBooleanToByte(aclPolicyCriteriaBean.getIsoutboundaclIpv4Applied()));
		aclPolicyCriteria.setIsoutboundaclIpv4Preprovisioned(convertBooleanToByte(aclPolicyCriteriaBean.getIsoutboundaclIpv4Preprovisioned()));
		aclPolicyCriteria.setIsoutboundaclIpv6Applied(convertBooleanToByte(aclPolicyCriteriaBean.getIsoutboundaclIpv6Applied()));
		aclPolicyCriteria.setIsoutboundaclIpv6Preprovisioned(convertBooleanToByte(aclPolicyCriteriaBean.getIsoutboundaclIpv6Preprovisioned()));
		aclPolicyCriteria.setIssvcQosCoscriteriaIpaddrAcl(convertBooleanToByte(aclPolicyCriteriaBean.getIssvcQosCoscriteriaIpaddrAcl()));
		aclPolicyCriteria.setIssvcQosCoscriteriaIpaddrAclName(aclPolicyCriteriaBean.getIssvcQosCoscriteriaIpaddrAclName());
		aclPolicyCriteria.setIssvcQosCoscriteriaIpaddrPreprovisioned(convertBooleanToByte(aclPolicyCriteriaBean.getIssvcQosCoscriteriaIpaddrPreprovisioned()));
		aclPolicyCriteria.setOutboundIpv4AclName(aclPolicyCriteriaBean.getOutboundIpv4AclName());
		aclPolicyCriteria.setOutboundIpv6AclName(aclPolicyCriteriaBean.getOutboundIpv6AclName());
		aclPolicyCriteria.setProtocol(aclPolicyCriteriaBean.getProtocol());
		aclPolicyCriteria.setSequence(aclPolicyCriteriaBean.getSequence());
		aclPolicyCriteria.setSourceAny(convertBooleanToByte(aclPolicyCriteriaBean.getSourceAny()));
		aclPolicyCriteria.setSourceCondition(aclPolicyCriteriaBean.getSourceCondition());
		aclPolicyCriteria.setSourcePortnumber(aclPolicyCriteriaBean.getSourcePortnumber());
		aclPolicyCriteria.setSourceSubnet(aclPolicyCriteriaBean.getSourceSubnet());
		aclPolicyCriteria.setEndDate(null);
		aclPolicyCriteria.setStartDate(new Timestamp(new Date().getTime()));
		aclPolicyCriteria.setLastModifiedDate(new Timestamp(new Date().getTime()));
		aclPolicyCriteria.setModifiedBy("FTI_Refresh");
		return aclPolicyCriteria;
	}
	
	private AclPolicyCriteria saveMigrationAclPolicyCriterias(AclPolicyCriteria aclPolicyCriteriaBean){
		AclPolicyCriteria aclPolicyCriteria = new AclPolicyCriteria();
		aclPolicyCriteria.setAction(aclPolicyCriteriaBean.getAction());
		aclPolicyCriteria.setDescription(aclPolicyCriteriaBean.getDescription());
		aclPolicyCriteria.setDestinationAny(aclPolicyCriteriaBean.getDestinationAny());
		aclPolicyCriteria.setDestinationCondition(aclPolicyCriteriaBean.getDestinationCondition());
		aclPolicyCriteria.setDestinationPortnumber(aclPolicyCriteriaBean.getDestinationPortnumber());
		aclPolicyCriteria.setDestinationSubnet(aclPolicyCriteriaBean.getDestinationSubnet());
		aclPolicyCriteria.setInboundIpv4AclName(aclPolicyCriteriaBean.getInboundIpv4AclName());
		aclPolicyCriteria.setInboundIpv6AclName(aclPolicyCriteriaBean.getInboundIpv6AclName());
		aclPolicyCriteria.setIsinboundaclIpv4Applied(aclPolicyCriteriaBean.getIsinboundaclIpv4Applied());
		aclPolicyCriteria.setIsinboundaclIpv4Preprovisioned(aclPolicyCriteriaBean.getIsinboundaclIpv4Preprovisioned());
		aclPolicyCriteria.setIsinboundaclIpv6Applied(aclPolicyCriteriaBean.getIsinboundaclIpv6Applied());
		aclPolicyCriteria.setIsinboundaclIpv6Preprovisioned(aclPolicyCriteriaBean.getIsinboundaclIpv6Preprovisioned());
		aclPolicyCriteria.setIsoutboundaclIpv4Applied(aclPolicyCriteriaBean.getIsoutboundaclIpv4Applied());
		aclPolicyCriteria.setIsoutboundaclIpv4Preprovisioned(aclPolicyCriteriaBean.getIsoutboundaclIpv4Preprovisioned());
		aclPolicyCriteria.setIsoutboundaclIpv6Applied(aclPolicyCriteriaBean.getIsoutboundaclIpv6Applied());
		aclPolicyCriteria.setIsoutboundaclIpv6Preprovisioned(aclPolicyCriteriaBean.getIsoutboundaclIpv6Preprovisioned());
		aclPolicyCriteria.setIssvcQosCoscriteriaIpaddrAcl(aclPolicyCriteriaBean.getIssvcQosCoscriteriaIpaddrAcl());
		aclPolicyCriteria.setIssvcQosCoscriteriaIpaddrAclName(aclPolicyCriteriaBean.getIssvcQosCoscriteriaIpaddrAclName());
		aclPolicyCriteria.setIssvcQosCoscriteriaIpaddrPreprovisioned(aclPolicyCriteriaBean.getIssvcQosCoscriteriaIpaddrPreprovisioned());
		aclPolicyCriteria.setOutboundIpv4AclName(aclPolicyCriteriaBean.getOutboundIpv4AclName());
		aclPolicyCriteria.setOutboundIpv6AclName(aclPolicyCriteriaBean.getOutboundIpv6AclName());
		aclPolicyCriteria.setProtocol(aclPolicyCriteriaBean.getProtocol());
		aclPolicyCriteria.setSequence(aclPolicyCriteriaBean.getSequence());
		aclPolicyCriteria.setSourceAny(aclPolicyCriteriaBean.getSourceAny());
		aclPolicyCriteria.setSourceCondition(aclPolicyCriteriaBean.getSourceCondition());
		aclPolicyCriteria.setSourcePortnumber(aclPolicyCriteriaBean.getSourcePortnumber());
		aclPolicyCriteria.setSourceSubnet(aclPolicyCriteriaBean.getSourceSubnet());
		aclPolicyCriteria.setEndDate(null);
		aclPolicyCriteria.setStartDate(new Timestamp(new Date().getTime()));
		aclPolicyCriteria.setLastModifiedDate(new Timestamp(new Date().getTime()));
		aclPolicyCriteria.setModifiedBy("FTI_Refresh");
		return aclPolicyCriteria;
	}

	private void saveMigrationServiceQos(ServiceDetail serviceDetail, ServiceDetailBean serviceDetailBean) {
		if(serviceDetailBean.getServiceQoBeans()!=null && !serviceDetailBean.getServiceQoBeans().isEmpty()){
			serviceDetailBean.getServiceQoBeans().forEach(serviceQoBean -> {
				ServiceQo serviceQo = getMigrationServiceQo(serviceDetail, serviceQoBean);

				if(serviceQoBean.getServiceCosCriterias()!=null && !serviceQoBean.getServiceCosCriterias().isEmpty()){
					serviceQoBean.getServiceCosCriterias().forEach(serviceCosCriteriaBean -> {
						ServiceCosCriteria serviceCosCriteria = getMigrationServiceCosCriteria(serviceQo, serviceCosCriteriaBean);

						if(serviceCosCriteriaBean.getAclPolicyCriterias()!=null && !serviceCosCriteriaBean.getAclPolicyCriterias().isEmpty()){
							serviceCosCriteriaBean.getAclPolicyCriterias().forEach(aclPolicyCriteriaBean -> {

								AclPolicyCriteria aclPolicyCriteria = saveMigrationAclPolicyCriterias(aclPolicyCriteriaBean);
								aclPolicyCriteria.setServiceCosCriteria(serviceCosCriteria);
								aclPolicyCriteriaRepository.saveAndFlush(aclPolicyCriteria);

							});
						}
					});
				}
			});
		}
	}
	
	private void saveMigrationServiceQos(ServiceDetail serviceDetail, ServiceDetail existingServiceDetail) {
		if(existingServiceDetail.getServiceQos() !=null && !existingServiceDetail.getServiceQos().isEmpty()){
			existingServiceDetail.getServiceQos().forEach(serviceQoBean -> {
				ServiceQo serviceQo = getMigrationServiceQo(serviceDetail, serviceQoBean);

				if(serviceQoBean.getServiceCosCriterias()!=null && !serviceQoBean.getServiceCosCriterias().isEmpty()){
					serviceQoBean.getServiceCosCriterias().forEach(serviceCosCriteriaBean -> {
						ServiceCosCriteria serviceCosCriteria = getMigrationServiceCosCriteria(serviceQo, serviceCosCriteriaBean);

						if(serviceCosCriteriaBean.getAclPolicyCriterias()!=null && !serviceCosCriteriaBean.getAclPolicyCriterias().isEmpty()){
							serviceCosCriteriaBean.getAclPolicyCriterias().forEach(aclPolicyCriteriaBean -> {

								AclPolicyCriteria aclPolicyCriteria = saveMigrationAclPolicyCriterias(aclPolicyCriteriaBean);
								aclPolicyCriteria.setServiceCosCriteria(serviceCosCriteria);
								aclPolicyCriteriaRepository.saveAndFlush(aclPolicyCriteria);

							});
						}
					});
				}
			});
		}
	}

	private void saveMigrationVpnSolutions(ServiceDetail serviceDetail, ServiceDetailBean serviceDetailBean){
		logger.info("saveMigrationVpnSolutions method invoked for serviceId: {}", serviceDetail.getServiceId());
    	if(!CollectionUtils.isEmpty(serviceDetailBean.getVpnSolutionBeans())){
    		logger.info("VpnSolutionBeans exists for serviceId: {}", serviceDetail.getServiceId());
			serviceDetailBean.getVpnSolutionBeans().forEach(vpnSolutionBean -> {
				if(StringUtils.isNotEmpty(vpnSolutionBean.getLegRole()) && !"NONE".equalsIgnoreCase(vpnSolutionBean.getLegRole())){
					logger.info("vpnSolutionBean legRole::{} exists for serviceId: {}", vpnSolutionBean.getLegRole(),serviceDetail.getServiceId());
					VpnSolution vpnSolution = new VpnSolution();
					VpnMetatData vpnMetatData = new VpnMetatData();
					vpnSolution.setInstanceId(vpnSolutionBean.getInstanceId());
					vpnSolution.setInterfaceName(vpnSolutionBean.getInterfaceName());
					if(StringUtils.isNotEmpty(vpnSolutionBean.getLegRole())){
						if(vpnSolutionBean.getLegRole().toLowerCase().contains("mesh")) {
							vpnSolution.setLegRole("FULL-MESH");
							vpnSolution.setVpnTopology("MESH");
							vpnSolution.setVpnType("CUSTOMER");
							vpnMetatData.setL2OTopology("FULL MESH");
							vpnMetatData.setL2oSiteRole("MESH");
						}
						else if(vpnSolutionBean.getLegRole().equalsIgnoreCase("SPOKE")) {
							vpnSolution.setLegRole("SPOKE");
							vpnSolution.setVpnTopology("HUBnSPOKE");
							vpnSolution.setVpnType("CUSTOMER");
							vpnMetatData.setL2OTopology("Hub & Spoke");
							vpnMetatData.setL2oSiteRole("SPOKE");
						}
						else if(vpnSolutionBean.getLegRole().equalsIgnoreCase("HUB")) {
							vpnSolution.setLegRole("HUB");
							vpnSolution.setVpnTopology("HUBnSPOKE");
							vpnSolution.setVpnType("CUSTOMER");
							vpnMetatData.setL2OTopology("Hub & Spoke");
							vpnMetatData.setL2oSiteRole("HUB");
						}
						else{
							vpnSolution.setLegRole(vpnSolutionBean.getLegRole());
							vpnSolution.setVpnTopology(vpnSolutionBean.getVpnTopology());
							vpnSolution.setVpnType(vpnSolutionBean.getVpnType());
							vpnMetatData.setL2OTopology(vpnSolutionBean.getVpnTopology());
							vpnMetatData.setL2oSiteRole(vpnSolutionBean.getLegRole());
						}
					}
					vpnSolution.setSiteId(vpnSolutionBean.getSiteId());
					vpnSolution.setVpnLegId(vpnSolutionBean.getVpnLegId());
					vpnSolution.setVpnName(vpnSolutionBean.getVpnName());
					if(StringUtils.isNotEmpty(vpnSolutionBean.getVpnName())){
						if(vpnSolutionBean.getVpnName().contains("_")) {
							int index = vpnSolutionBean.getVpnName().lastIndexOf('_');
							vpnSolution.setVpnSolutionName(vpnSolutionBean.getVpnName().substring(0, index));
							vpnMetatData.setVpnSolutionName(vpnSolutionBean.getVpnName().substring(0, index));
						}
					}
					vpnSolution.setEndDate(null);
					vpnSolution.setStartDate(new Timestamp(new Date().getTime()));
					vpnSolution.setLastModifiedDate(new Timestamp(new Date().getTime()));
					vpnSolution.setModifiedBy("FTI_Refresh");
					vpnSolution.setServiceDetail(serviceDetail);
					vpnSolutionRepository.saveAndFlush(vpnSolution);
					vpnMetatData.setVpnName(vpnSolution.getVpnName());
					vpnMetatData.setVpnAlias(vpnSolution.getVpnName());
					vpnMetatData.setIsUa("NA");
					vpnMetatData.setVpnLegId(StringUtils.isNotBlank(vpnSolutionBean.getVpnLegId()) ? Integer.parseInt(vpnSolutionBean.getVpnLegId()) : null);
					vpnMetatData.setManagementVpnType1(vpnSolutionBean.getManagementVpnType1());
					vpnMetatData.setManagementVpnType2(vpnSolutionBean.getManagementVpnType2());
					vpnMetatData.setE2eIntegrated(Objects.nonNull(vpnSolutionBean.getIsE2eIntegrated()) ? vpnSolutionBean.getIsE2eIntegrated() : false);
					vpnMetatData.setIsenableUccService(Objects.nonNull(vpnSolutionBean.getIsenableUccService()) ? vpnSolutionBean.getIsenableUccService() : false);
					vpnMetatData.setServiceDetail(serviceDetail);
					vpnMetatData.setModifiedBy("FTI_Refresh");
					vpnMetatDataRepository.saveAndFlush(vpnMetatData);
				}
			});
		}
	}
	
	private void saveMigrationVpnSolutions(ServiceDetail serviceDetail, ServiceDetail existingServiceDetail){
    	if(!CollectionUtils.isEmpty(existingServiceDetail.getVpnMetatDatas())){
    		existingServiceDetail.getVpnMetatDatas().forEach(vpnSolutionBean -> {
				VpnSolution vpnSolution = new VpnSolution();
				VpnMetatData vpnMetatData = new VpnMetatData();
			//	vpnSolution.setInstanceId(vpnSolutionBean.getInstanceId());
			//	vpnSolution.setInterfaceName(vpnSolutionBean.getInterfaceName());
				if(StringUtils.isNotEmpty(vpnSolutionBean.getL2oSiteRole())){
					if(vpnSolutionBean.getL2oSiteRole().toLowerCase().contains("mesh")) {
						vpnSolution.setLegRole("MESH");
						vpnSolution.setVpnTopology("MESH");
						vpnSolution.setVpnType("CUSTOMER");
						vpnMetatData.setL2OTopology("FULL MESH");
						vpnMetatData.setL2oSiteRole("MESH");
					}
					else if(vpnSolutionBean.getL2oSiteRole().equalsIgnoreCase("SPOKE")) {
						vpnSolution.setLegRole("SPOKE");
						vpnSolution.setVpnTopology("HUBnSPOKE");
						vpnSolution.setVpnType("CUSTOMER");
						vpnMetatData.setL2OTopology("Hub & Spoke");
						vpnMetatData.setL2oSiteRole("SPOKE");
					}
					else if(vpnSolutionBean.getL2oSiteRole().equalsIgnoreCase("HUB")) {
						vpnSolution.setLegRole("HUB");
						vpnSolution.setVpnTopology("HUBnSPOKE");
						vpnSolution.setVpnType("CUSTOMER");
						vpnMetatData.setL2OTopology("Hub & Spoke");
						vpnMetatData.setL2oSiteRole("HUB");
					}
					else{
						vpnSolution.setLegRole(vpnSolutionBean.getL2oSiteRole());
						vpnSolution.setVpnTopology(vpnSolutionBean.getL2OTopology());
					//	vpnSolution.setVpnType(vpnSolutionBean.getVpnType());
						vpnMetatData.setL2OTopology(vpnSolutionBean.getL2OTopology());
						vpnMetatData.setL2oSiteRole(vpnSolutionBean.getL2oSiteRole());
					}
				}
			//	vpnSolution.setSiteId(vpnSolutionBean.getSiteId());
				vpnSolution.setVpnLegId( vpnSolutionBean.getVpnLegId() != null ? vpnSolutionBean.getVpnLegId().toString(): null);
				vpnSolution.setVpnName(vpnSolutionBean.getVpnName());
				if(StringUtils.isNotEmpty(vpnSolutionBean.getVpnName())){
					if(vpnSolutionBean.getVpnName().contains("_")) {
						int index = vpnSolutionBean.getVpnName().lastIndexOf('_');
						vpnSolution.setVpnSolutionName(vpnSolutionBean.getVpnName().substring(0, index));
						vpnMetatData.setVpnSolutionName(vpnSolutionBean.getVpnName().substring(0, index));
					}
				}
				vpnSolution.setEndDate(null);
				vpnSolution.setStartDate(new Timestamp(new Date().getTime()));
				vpnSolution.setLastModifiedDate(new Timestamp(new Date().getTime()));
				vpnSolution.setModifiedBy("FTI_Refresh");
				vpnSolution.setServiceDetail(serviceDetail);
				vpnSolutionRepository.saveAndFlush(vpnSolution);
				vpnMetatData.setVpnName(vpnSolution.getVpnName());
				vpnMetatData.setVpnAlias(vpnSolution.getVpnName());
				vpnMetatData.setIsUa("NA");
				vpnMetatData.setVpnLegId(vpnSolutionBean.getVpnLegId());
				//Need to Ask Vivek
				vpnMetatData.setManagementVpnType1(vpnSolutionBean.getManagementVpnType1());
				vpnMetatData.setManagementVpnType2(vpnSolutionBean.getManagementVpnType2());
				vpnMetatData.setE2eIntegrated(Objects.nonNull(vpnSolutionBean.isE2eIntegrated()) ? vpnSolutionBean.isE2eIntegrated() : false);
				vpnMetatData.setIsenableUccService(Objects.nonNull(vpnSolutionBean.isIsenableUccService()) ? vpnSolutionBean.isIsenableUccService() : false);
				vpnMetatData.setServiceDetail(serviceDetail);
				vpnMetatData.setModifiedBy("FTI_Refresh");
				vpnMetatDataRepository.saveAndFlush(vpnMetatData);
			});
		}
	}

	private Vrf getMigrationVrf(ServiceDetail serviceDetail, VrfBean vrfBean){
		if(vrfBean.getVrfName()!=null) {
			Vrf vrf = new Vrf();
			vrf.setIsmultivrf(convertBooleanToByte(vrfBean.getIsmultivrf()));
			vrf.setIsvrfLiteEnabled(convertBooleanToByte(vrfBean.getIsvrfLiteEnabled()));
			vrf.setMastervrfServiceid(vrfBean.getMastervrfServiceid());
			vrf.setMaxRoutesValue(vrfBean.getMaxRoutesValue());
			vrf.setThreshold(vrfBean.getThreshold());
			vrf.setVrfName(vrfBean.getVrfName());
			vrf.setWarnOn(vrfBean.getWarnOn());
			vrf.setEndDate(null);
			vrf.setStartDate(new Timestamp(new Date().getTime()));
			vrf.setLastModifiedDate(new Timestamp(new Date().getTime()));
			vrf.setModifiedBy("FTI_Refresh");
			vrf.setServiceDetail(serviceDetail);
			return vrfRepository.saveAndFlush(vrf);
		}
		return null;
	}
	
	private Vrf getMigrationVrf(ServiceDetail serviceDetail, Vrf vrfBean){
		Vrf vrf = new Vrf();
		vrf.setIsmultivrf(vrfBean.getIsmultivrf());
		vrf.setMastervrfServiceid(vrfBean.getMastervrfServiceid());
		vrf.setMaxRoutesValue(vrfBean.getMaxRoutesValue());
		vrf.setIsvrfLiteEnabled(vrfBean.getIsvrfLiteEnabled());
		vrf.setThreshold(vrfBean.getThreshold());
		vrf.setVrfName(vrfBean.getVrfName());
		vrf.setWarnOn(vrfBean.getWarnOn());
		vrf.setEndDate(null);
		vrf.setStartDate(new Timestamp(new Date().getTime()));
		vrf.setLastModifiedDate(new Timestamp(new Date().getTime()));
		vrf.setModifiedBy("FTI_Refresh");
		vrf.setServiceDetail(serviceDetail);
		return vrfRepository.saveAndFlush(vrf);
	}

	private void saveMigrationPolicyTypes(PolicyTypeBean policyTypeBean, Object object, String type){
		PolicyType policyType = new PolicyType();
		policyType.setIsadditionalpolicytermReqd(convertBooleanToByte(policyTypeBean.getIsadditionalpolicytermReqd()));
		policyType.setIslpinvpnpolicyconfigured(convertBooleanToByte(policyTypeBean.getIslpinvpnpolicyconfigured()));
		policyType.setIsvprnExportpolicyName(policyTypeBean.getIsvprnExportpolicyName());
		policyType.setIsvprnImportpolicy(convertBooleanToByte(policyTypeBean.getIsvprnImportpolicy()));
		policyType.setIsvprnExportPreprovisioned(convertBooleanToByte(policyTypeBean.getIsvprnExportPreprovisioned()));
		policyType.setIsvprnImportpolicyName(policyTypeBean.getIsvprnImportpolicyName());
		policyType.setIsvprnImportPreprovisioned(convertBooleanToByte(policyTypeBean.getIsvprnImportPreprovisioned()));
		policyType.setLocalpreferencev4Vpnpolicy(policyTypeBean.getLocalpreferencev4Vpnpolicy());
		policyType.setLocalpreferencev6Vpnpolicy(policyTypeBean.getLocalpreferencev6Vpnpolicy());
		policyType.setOriginatedefaultFlag(convertBooleanToByte(policyTypeBean.getOriginatedefaultFlag()));
		policyType.setOutboundIpv4PolicyName(policyTypeBean.getOutboundIpv4PolicyName());
		policyType.setOutboundIpv4Preprovisioned(convertBooleanToByte(policyTypeBean.getOutboundIpv4Preprovisioned()));
		policyType.setOutboundIpv6PolicyName(policyTypeBean.getOutboundIpv6PolicyName());
		policyType.setOutboundIpv6Preprovisioned(convertBooleanToByte(policyTypeBean.getOutboundIpv6Preprovisioned()));
		policyType.setOutboundIstandardpolicyv4(convertBooleanToByte(policyTypeBean.getOutboundIstandardpolicyv4()));
		policyType.setOutboundIstandardpolicyv6(convertBooleanToByte(policyTypeBean.getOutboundIstandardpolicyv6()));
		policyType.setOutboundRoutingIpv4Policy(convertBooleanToByte(policyTypeBean.getOutboundRoutingIpv4Policy()));
		policyType.setOutboundRoutingIpv6Policy(convertBooleanToByte(policyTypeBean.getOutboundRoutingIpv6Policy()));
		policyType.setPolicyId(policyTypeBean.getPolicyId());
		policyType.setEndDate(null);
		policyType.setStartDate(new Timestamp(new Date().getTime()));
		policyType.setLastModifiedDate(new Timestamp(new Date().getTime()));
		policyType.setModifiedBy("FTI_Refresh");
		policyType.setInboundRoutingIpv6Policy(convertBooleanToByte(policyTypeBean.getInboundRoutingIpv6Policy()));
		policyType.setInboundRoutingIpv4Policy(convertBooleanToByte(policyTypeBean.getInboundRoutingIpv4Policy()));
		policyType.setInboundIstandardpolicyv6(convertBooleanToByte(policyTypeBean.getInboundIstandardpolicyv6()));
		policyType.setInboundIstandardpolicyv4(convertBooleanToByte(policyTypeBean.getInboundIstandardpolicyv4()));
		policyType.setInboundIpv6Preprovisioned(convertBooleanToByte(policyTypeBean.getInboundIpv6Preprovisioned()));
		policyType.setInboundIpv6PolicyName(policyTypeBean.getInboundIpv6PolicyName());
		policyType.setInboundIpv4Preprovisioned(convertBooleanToByte(policyTypeBean.getInboundIpv4Preprovisioned()));
		policyType.setInboundIpv4PolicyName(policyTypeBean.getInboundIpv4PolicyName());
		policyType.setAludefaultoriginatev6Key(convertBooleanToByte(policyTypeBean.getAludefaultoriginatev6Key()));
		policyType.setAludefaultoriginatev4Key(convertBooleanToByte(policyTypeBean.getAludefaultoriginatev4Key()));
		if(type.equalsIgnoreCase("vrf")){
			policyType.setVrf((Vrf) object);
		}
		if(type.equalsIgnoreCase("bgp")){
			policyType.setBgp((Bgp) object);
		}
		if(type.equalsIgnoreCase("ospf")){
			policyType.setOspf((Ospf) object);
		}
		if(type.equalsIgnoreCase("rip")){
			policyType.setRip((Rip) object);
		}
		policyTypeRepository.saveAndFlush(policyType);
		if(!CollectionUtils.isEmpty(policyTypeBean.getPolicyCriteria())){
			policyTypeBean.getPolicyCriteria().forEach(policyCriteriaBean -> {
				PolicyCriteria policyCriteria = getMigrationPolicyCriteria(policyCriteriaBean);
				PolicyTypeCriteriaMapping policyTypeCriteriaMapping = new PolicyTypeCriteriaMapping();
				policyTypeCriteriaMapping.setPolicyType(policyType);
				policyTypeCriteriaMapping.setPolicyCriteriaId(policyCriteria.getPolicyCriteriaId());
				policyTypeCriteriaMapping.setEndDate(null);
				policyTypeCriteriaMapping.setStartDate(new Timestamp(new Date().getTime()));
				policyTypeCriteriaMapping.setLastModifiedDate(new Timestamp(new Date().getTime()));
				policyTypeCriteriaMapping.setModifiedBy("FTI_Refresh");
				policyTypeCriteriaMapping.setVersion(1);
				policyTypeCriteriaMappingRepository.saveAndFlush(policyTypeCriteriaMapping);
			});
		}
	}
	
//	private void saveMigrationPolicyTypes(PolicyType policyTypeBean, Object object, String type){
//		PolicyType policyType = new PolicyType();
//		policyType.setIsadditionalpolicytermReqd(policyTypeBean.getIsadditionalpolicytermReqd());
//		policyType.setIslpinvpnpolicyconfigured(policyTypeBean.getIslpinvpnpolicyconfigured());
//		policyType.setIsvprnExportpolicyName(policyTypeBean.getIsvprnExportpolicyName());
//		policyType.setIsvprnImportpolicy(policyTypeBean.getIsvprnImportpolicy());
//		policyType.setIsvprnExportPreprovisioned(policyTypeBean.getIsvprnExportPreprovisioned());
//		policyType.setIsvprnImportpolicyName(policyTypeBean.getIsvprnImportpolicyName());
//		policyType.setIsvprnImportPreprovisioned(policyTypeBean.getIsvprnImportPreprovisioned());
//		policyType.setLocalpreferencev4Vpnpolicy(policyTypeBean.getLocalpreferencev4Vpnpolicy());
//		policyType.setLocalpreferencev6Vpnpolicy(policyTypeBean.getLocalpreferencev6Vpnpolicy());
//		policyType.setOriginatedefaultFlag(policyTypeBean.getOriginatedefaultFlag());
//		policyType.setOutboundIpv4PolicyName(policyTypeBean.getOutboundIpv4PolicyName());
//		policyType.setOutboundIpv4Preprovisioned(policyTypeBean.getOutboundIpv4Preprovisioned());
//		policyType.setOutboundIpv6PolicyName(policyTypeBean.getOutboundIpv6PolicyName());
//		policyType.setOutboundIpv6Preprovisioned(policyTypeBean.getOutboundIpv6Preprovisioned());
//		policyType.setOutboundIstandardpolicyv4(policyTypeBean.getOutboundIstandardpolicyv4());
//		policyType.setOutboundIstandardpolicyv6(policyTypeBean.getOutboundIstandardpolicyv6());
//		policyType.setOutboundRoutingIpv4Policy(policyTypeBean.getOutboundRoutingIpv4Policy());
//		policyType.setOutboundRoutingIpv6Policy(policyTypeBean.getOutboundRoutingIpv6Policy());
//		policyType.setPolicyId(policyTypeBean.getPolicyId());
//		policyType.setEndDate(null);
//		policyType.setStartDate(new Timestamp(new Date().getTime()));
//		policyType.setLastModifiedDate(new Timestamp(new Date().getTime()));
//		policyType.setModifiedBy("FTI_Refresh");
//		policyType.setInboundRoutingIpv6Policy(policyTypeBean.getInboundRoutingIpv6Policy());
//		policyType.setInboundRoutingIpv4Policy(policyTypeBean.getInboundRoutingIpv4Policy());
//		policyType.setInboundIstandardpolicyv6(policyTypeBean.getInboundIstandardpolicyv6());
//		policyType.setInboundIstandardpolicyv4(policyTypeBean.getInboundIstandardpolicyv4());
//		policyType.setInboundIpv6Preprovisioned(policyTypeBean.getInboundIpv6Preprovisioned());
//		policyType.setInboundIpv6PolicyName(policyTypeBean.getInboundIpv6PolicyName());
//		policyType.setInboundIpv4Preprovisioned(policyTypeBean.getInboundIpv4Preprovisioned());
//		policyType.setInboundIpv4PolicyName(policyTypeBean.getInboundIpv4PolicyName());
//		policyType.setAludefaultoriginatev6Key(policyTypeBean.getAludefaultoriginatev6Key());
//		policyType.setAludefaultoriginatev4Key(policyTypeBean.getAludefaultoriginatev4Key());
//		if(type.equalsIgnoreCase("vrf")){
//			policyType.setVrf((Vrf) object);
//		}
//		if(type.equalsIgnoreCase("bgp")){
//			policyType.setBgp((Bgp) object);
//		}
//		if(type.equalsIgnoreCase("ospf")){
//			policyType.setOspf((Ospf) object);
//		}
//		if(type.equalsIgnoreCase("rip")){
//			policyType.setRip((Rip) object);
//		}
//		policyTypeRepository.saveAndFlush(policyType);
//		if(!CollectionUtils.isEmpty(policyTypeBean.getPolicyTypeCriteriaMappings())){
//			policyTypeBean.getPolicyTypeCriteriaMappings().forEach(policyCriteriaBean -> {
//				PolicyCriteria policyCriteria = getMigrationPolicyCriteria(policyCriteriaBean);
//				PolicyTypeCriteriaMapping policyTypeCriteriaMapping = new PolicyTypeCriteriaMapping();
//				policyTypeCriteriaMapping.setPolicyType(policyType);
//				policyTypeCriteriaMapping.setPolicyCriteriaId(policyCriteria.getPolicyCriteriaId());
//				policyTypeCriteriaMapping.setEndDate(null);
//				policyTypeCriteriaMapping.setStartDate(new Timestamp(new Date().getTime()));
//				policyTypeCriteriaMapping.setLastModifiedDate(new Timestamp(new Date().getTime()));
//				policyTypeCriteriaMapping.setModifiedBy("FTI_Refresh");
//				policyTypeCriteriaMapping.setVersion(1);
//				policyTypeCriteriaMappingRepository.saveAndFlush(policyTypeCriteriaMapping);
//			});
//		}
//	}

	private void saveMigrationWanStaticRoutes(WanStaticRouteBean wanStaticRouteBean, Bgp bgp){
    	WanStaticRoute wanStaticRoute = new WanStaticRoute();
		wanStaticRoute.setAdvalue(wanStaticRouteBean.getAdvalue());
		wanStaticRoute.setDescription(wanStaticRouteBean.getDescription());
		wanStaticRoute.setGlobal(convertBooleanToByte(wanStaticRouteBean.getGlobal()));
		wanStaticRoute.setIpsubnet(wanStaticRouteBean.getIpsubnet());
		wanStaticRoute.setNextHopid(wanStaticRouteBean.getNextHopid());
		wanStaticRoute.setPopCommunity(wanStaticRouteBean.getPopCommunity());
		wanStaticRoute.setRegionalCommunity(wanStaticRouteBean.getRegionalCommunity());
		wanStaticRoute.setServiceCommunity(wanStaticRouteBean.getServiceCommunity());
		wanStaticRoute.setWanstaticrouteId(wanStaticRouteBean.getWanstaticrouteId());
		wanStaticRoute.setIsCewan(convertBooleanToByte(wanStaticRouteBean.getIsCewan()));
		wanStaticRoute.setIsCpelanStaticroutes(convertBooleanToByte(wanStaticRouteBean.getIsCpelanStaticroutes()));
		wanStaticRoute.setIsCpewanStaticroutes(convertBooleanToByte(wanStaticRouteBean.getIsCpewanStaticroutes()));
		wanStaticRoute.setIsPewan(convertBooleanToByte(wanStaticRouteBean.getIsPewan()));
		wanStaticRoute.setLastModifiedDate(new Timestamp(new Date().getTime()));
		wanStaticRoute.setEndDate(null);
		wanStaticRoute.setModifiedBy("FTI_Refresh");
		wanStaticRoute.setStartDate(new Timestamp(new Date().getTime()));
    	wanStaticRoute.setBgp(bgp);
    	wanStaticRouteRepository.saveAndFlush(wanStaticRoute);
	}

	private PolicyCriteria getMigrationPolicyCriteria(PolicyCriteriaBean policyCriteriaBean){
		PolicyCriteria policyCriteria = new PolicyCriteria();
		policyCriteria.setMatchcriteriaNeighbourCommunity(convertBooleanToByte(policyCriteriaBean.getMatchcriteriaNeighbourCommunity()));
		policyCriteria.setMatchcriteriaPrefixlist(convertBooleanToByte(policyCriteriaBean.getMatchcriteriaPrefixlist()));
		policyCriteria.setMatchcriteriaPprefixlistPreprovisioned(convertBooleanToByte(policyCriteriaBean.getMatchCriteriaPrefixListPreprovisioned()));
		policyCriteria.setMatchcriteriaPrefixlistName(policyCriteriaBean.getMatchcriteriaPrefixlistName());
		policyCriteria.setMatchcriteriaProtocol(convertBooleanToByte(policyCriteriaBean.getMatchcriteriaProtocol()));
		policyCriteria.setMatchcriteriaRegexAspath(convertBooleanToByte(policyCriteriaBean.getMatchcriteriaRegexAspath()));
		policyCriteria.setMatchcriteriaRegexAspathAspath(policyCriteriaBean.getMatchCriteriaRegexAsPathAsPath());
		policyCriteria.setMatchcriteriaRegexAspathName(policyCriteriaBean.getMatchcriteriaRegexAspathName());
		policyCriteria.setPolicyCriteriaId(policyCriteriaBean.getPolicyCriteriaId());
		policyCriteria.setSetcriteriaAspathPrepend(convertBooleanToByte(policyCriteriaBean.getSetcriteriaAspathPrepend()));
		policyCriteria.setSetcriteriaAspathPrependIndex(policyCriteriaBean.getSetcriteriaAspathPrependIndex());
		policyCriteria.setSetcriteriaLocalPreference(convertBooleanToByte(policyCriteriaBean.getSetcriteriaLocalPreference()));
		policyCriteria.setSetcriteriaLocalpreferenceName(policyCriteriaBean.getSetcriteriaLocalpreferenceName());
		policyCriteria.setSetcriteriaAspathPrependName(policyCriteriaBean.getSetcriteriaAspathPrependName());
		policyCriteria.setSetcriteriaMetric(convertBooleanToByte(policyCriteriaBean.getSetcriteriaMetric()));
		policyCriteria.setSetcriteriaMetricName(policyCriteriaBean.getSetcriteriaMetricName());
		policyCriteria.setSetcriteriaAspathPrependIndex(policyCriteria.getSetcriteriaAspathPrependIndex());
		policyCriteria.setSetcriteriaNeighbourCommunity(convertBooleanToByte(policyCriteriaBean.getSetcriteriaNeighbourCommunity()));
		policyCriteria.setSetcriteriaRegexAspath(convertBooleanToByte(policyCriteriaBean.getSetcriteriaRegexAspath()));
		policyCriteria.setSetcriteriaRegexAspathAspath(policyCriteriaBean.getMatchCriteriaRegexAsPathAsPath());
		policyCriteria.setSetcriteriaRegexAspathName(policyCriteriaBean.getSetcriteriaRegexAspathName());
		policyCriteria.setTermName(policyCriteriaBean.getTermName());
		policyCriteria.setTermSetcriteriaAction(policyCriteriaBean.getTermSetcriteriaAction());
		policyCriteria.setEndDate(null);
		policyCriteria.setStartDate(new Timestamp(new Date().getTime()));
		policyCriteria.setLastModifiedDate(new Timestamp(new Date().getTime()));
		policyCriteria.setModifiedBy("FTI_Refresh");
		return policyCriteriaRepository.saveAndFlush(policyCriteria);
	}
	
	private PolicyCriteria getMigrationPolicyCriteria(PolicyCriteria policyCriteriaBean){
		PolicyCriteria policyCriteria = new PolicyCriteria();
		policyCriteria.setMatchcriteriaNeighbourCommunity(policyCriteriaBean.getMatchcriteriaNeighbourCommunity());
		policyCriteria.setMatchcriteriaPrefixlist(policyCriteriaBean.getMatchcriteriaPrefixlist());
		policyCriteria.setMatchcriteriaPprefixlistPreprovisioned(policyCriteriaBean.getMatchcriteriaPprefixlistPreprovisioned());
		policyCriteria.setMatchcriteriaPrefixlistName(policyCriteriaBean.getMatchcriteriaPrefixlistName());
		policyCriteria.setMatchcriteriaProtocol(policyCriteriaBean.getMatchcriteriaProtocol());
		policyCriteria.setMatchcriteriaRegexAspath(policyCriteriaBean.getMatchcriteriaRegexAspath());
		policyCriteria.setMatchcriteriaRegexAspathAspath(policyCriteriaBean.getMatchcriteriaRegexAspathAspath());
		policyCriteria.setMatchcriteriaRegexAspathName(policyCriteriaBean.getMatchcriteriaRegexAspathName());
		policyCriteria.setPolicyCriteriaId(policyCriteriaBean.getPolicyCriteriaId());
		policyCriteria.setSetcriteriaAspathPrepend(policyCriteriaBean.getSetcriteriaAspathPrepend());
		policyCriteria.setSetcriteriaAspathPrependIndex(policyCriteriaBean.getSetcriteriaAspathPrependIndex());
		policyCriteria.setSetcriteriaLocalPreference(policyCriteriaBean.getSetcriteriaLocalPreference());
		policyCriteria.setSetcriteriaLocalpreferenceName(policyCriteriaBean.getSetcriteriaLocalpreferenceName());
		policyCriteria.setSetcriteriaAspathPrependName(policyCriteriaBean.getSetcriteriaAspathPrependName());
		policyCriteria.setSetcriteriaMetric(policyCriteriaBean.getSetcriteriaMetric());
		policyCriteria.setSetcriteriaMetricName(policyCriteriaBean.getSetcriteriaMetricName());
		policyCriteria.setSetcriteriaAspathPrependIndex(policyCriteria.getSetcriteriaAspathPrependIndex());
		policyCriteria.setSetcriteriaNeighbourCommunity(policyCriteriaBean.getSetcriteriaNeighbourCommunity());
		policyCriteria.setSetcriteriaRegexAspath(policyCriteriaBean.getSetcriteriaRegexAspath());
		policyCriteria.setSetcriteriaRegexAspathAspath(policyCriteriaBean.getSetcriteriaRegexAspathAspath());
		policyCriteria.setSetcriteriaRegexAspathName(policyCriteriaBean.getSetcriteriaRegexAspathName());
		policyCriteria.setTermName(policyCriteriaBean.getTermName());
		policyCriteria.setTermSetcriteriaAction(policyCriteriaBean.getTermSetcriteriaAction());
		policyCriteria.setEndDate(null);
		policyCriteria.setStartDate(new Timestamp(new Date().getTime()));
		policyCriteria.setLastModifiedDate(new Timestamp(new Date().getTime()));
		policyCriteria.setModifiedBy("FTI_Refresh");
		return policyCriteriaRepository.saveAndFlush(policyCriteria);
	}

	private void saveMigrationVrfs(ServiceDetail serviceDetail, ServiceDetailBean serviceDetailBean, Map<String, String> ftiDetails){
		if(serviceDetailBean.getVrfBeans()!=null && !serviceDetailBean.getVrfBeans().isEmpty()){
			logger.info("saveMigrationVrfs with size::{}",serviceDetailBean.getVrfBeans().size());
			serviceDetailBean.getVrfBeans().forEach(vrfBean -> {
				Vrf vrf = getMigrationVrf(serviceDetail, vrfBean);
				if (!serviceDetailBean.getServiceType().equalsIgnoreCase("GVPN") && vrf != null && (AceConstants.VRF.INTERNET_VPN.equalsIgnoreCase(vrf.getVrfName())
								|| AceConstants.VRF.PRIMUS_INTERNET.equalsIgnoreCase(vrf.getVrfName()))) {
						logger.info("Vrf::INTERNET_VPN or PRIMUS_INTERNET");
						List<VpnSolution> vpnSolList=vpnSolutionRepository.findByServiceDetail_IdAndEndDateIsNull(serviceDetail.getId());
						if(vpnSolList!=null && !vpnSolList.isEmpty()) {
							Set<VpnSolution> vpnSolutionSet= new HashSet<>(vpnSolList);
							endDateVpnSolutions(vpnSolutionSet, modifiedBy,  true);
						}
						List<VpnMetatData> vpnMetaDataList=vpnMetaDataRepository.findByServiceDetail_IdAndEndDateIsNull(serviceDetail.getId());
						if(vpnMetaDataList!=null && !vpnMetaDataList.isEmpty()) {
							Set<VpnMetatData> vpnMetaDataSet= new HashSet<>(vpnMetaDataList);
							endDateVpnMetatDatas(vpnMetaDataSet, modifiedBy,  true);
						}
						try {
							saveVpnMetaData(serviceDetail);
							saveVpnSolution(serviceDetail,vrf.getVrfName());
						} catch (Exception e) {
							logger.error("FTI Refresh.saveVpnMetaData or saveVpnSolution error occured",e);
						}
				}else {
					try {
						List<VpnMetatData> vpnMetaDataList=vpnMetaDataRepository.findByServiceDetail_IdAndEndDateIsNull(serviceDetail.getId());
						String vpnName = "";
						if(vpnMetaDataList!=null && !vpnMetaDataList.isEmpty() && ftiDetails!=null) {
							VpnMetatData vpnMetatData=vpnMetaDataList.get(0);
							logger.info("FTI Refresh for GVPN Vpn MetaData::{}",vpnMetatData.getId());
							if (ftiDetails.containsKey("pewan.l3vpn.community.sivcommtype") && ftiDetails.get("pewan.l3vpn.community.sivcommtype")!=null &&
									!ftiDetails.get("pewan.l3vpn.community.sivcommtype").isEmpty() 
									&& ftiDetails.get("pewan.l3vpn.community.sivcommtype").toLowerCase().contains("mesh")) {
								logger.info("pewan.l3vpn.community.sivcommtype Exists::{}",serviceDetail.getId());
								vpnMetatData.setL2oSiteRole("Mesh");
								vpnMetatData.setL2OTopology("Full Mesh");
							}else {
								logger.info("pewan.l3vpn.community.sivcommtype not Exists::{}",serviceDetail.getId());
								vpnMetatData.setL2OTopology("Hub & Spoke");
							}
							if (ftiDetails.containsKey("pewan.l3vpn.customervpn.name") || ftiDetails.containsKey("pewan.vprn.asdescription")) {
								if(ftiDetails.get("pewan.l3vpn.customervpn.name").toLowerCase().contains("vsnl")) {
									vpnName = ftiDetails.get("pewan.vprn.asdescription");
								}
								else{
									vpnName = ftiDetails.get("pewan.l3vpn.customervpn.name");
								}
								vpnMetatData.setVpnName(vpnName);
								vpnMetatData.setVpnSolutionName(vpnName);
							}
							vpnMetatData.setServiceDetail(serviceDetail);
							vpnMetaDataRepository.saveAndFlush(vpnMetatData);
							if(vpnMetatData.getL2oSiteRole()==null || vpnMetatData.getL2oSiteRole().isEmpty()) {
								logger.info("vpnL2oSiteRole not exists for service id::{}",serviceDetail.getId());
								vpnMetatData.setL2oSiteRole("Spoke");
								vpnMetaDataRepository.saveAndFlush(vpnMetatData);
							}
						}
					}catch(Exception e) {
						logger.error("FTI Refresh.saveVpnMetaData error occured",e);
					}
					
				}
				if(vrfBean.getPolicyTypes()!=null && !vrfBean.getPolicyTypes().isEmpty()){
					vrfBean.getPolicyTypes().forEach(policyTypeBean -> {
						saveMigrationPolicyTypes(policyTypeBean, vrf ,  "vrf");
					});
				}
			});
		}
	}
	
	private void saveMigrationVrfs(ServiceDetail serviceDetail, ServiceDetail existingServiceDetail){
		if(existingServiceDetail.getVrfs() !=null && !existingServiceDetail.getVrfs().isEmpty()){
			existingServiceDetail.getVrfs().forEach(vrfBean -> {
				Vrf vrf = getMigrationVrf(serviceDetail, vrfBean);

				if(vrfBean.getPolicyTypes()!=null && !vrfBean.getPolicyTypes().isEmpty()){
					vrfBean.getPolicyTypes().forEach(policyTypeBean -> {
					//	saveMigrationPolicyTypes(policyTypeBean, vrf ,  "vrf");
					});
				}
			});
		}
	}

	private Topology getMigrationTopology(ServiceDetail serviceDetail, TopologyBean topologyBean){
		Topology topology = new Topology();
		topology.setEndDate(null);
		topology.setStartDate(new Timestamp(new Date().getTime()));
		topology.setLastModifiedDate(new Timestamp(new Date().getTime()));
		topology.setModifiedBy("FTI_Refresh");
		topology.setTopologyId(topologyBean.getTopologyId());
		topology.setTopologyName(topologyBean.getTopologyName());
		topology.setServiceDetail(serviceDetail);
		return topologyRepository.saveAndFlush(topology);
	}
	
	private Topology getMigrationTopology(ServiceDetail serviceDetail, Topology topologyBean){
		Topology topology = new Topology();
		topology.setEndDate(null);
		topology.setStartDate(new Timestamp(new Date().getTime()));
		topology.setLastModifiedDate(new Timestamp(new Date().getTime()));
		topology.setModifiedBy("FTI_Refresh");
		topology.setTopologyId(topologyBean.getTopologyId());
		topology.setTopologyName(topologyBean.getTopologyName());
		topology.setServiceDetail(serviceDetail);
		return topologyRepository.saveAndFlush(topology);
	}


	private void saveMigrationTopologies(ServiceDetail serviceDetail, ServiceDetailBean serviceDetailBean){
		logger.info("Inside saveMigrationTopologies method for serviceCode {}", serviceDetail.getServiceId());
    	if(serviceDetailBean.getTopologyBeans()!=null && !serviceDetailBean.getTopologyBeans().isEmpty()){
			serviceDetailBean.getTopologyBeans().forEach(topologyBean -> {
				Topology topology = getMigrationTopology(serviceDetail, topologyBean);

				if(topologyBean.getRouterUplinkports()!=null && !topologyBean.getRouterUplinkports().isEmpty()){
					topologyBean.getRouterUplinkports().forEach(routerUplinkportBean -> {
						RouterUplinkport routerUplinkport = new RouterUplinkport();
						routerUplinkport.setPhysicalPort2Name(routerUplinkportBean.getPhysicalPort2Name());
						routerUplinkport.setRouterUplinkportId(routerUplinkportBean.getRouterUplinkportId());
						routerUplinkport.setPhysicalPort1Name(routerUplinkportBean.getPhysicalPort1Name());
						routerUplinkport.setEndDate(null);
						routerUplinkport.setStartDate(new Timestamp(new Date().getTime()));
						routerUplinkport.setLastModifiedDate(new Timestamp(new Date().getTime()));
						routerUplinkport.setModifiedBy("FTI_Refresh");
						routerUplinkport.setTopology(topology);
						routerUplinkPortRepository.saveAndFlush(routerUplinkport);
					});
				}

				if(topologyBean.getUniswitchDetails()!=null && !topologyBean.getUniswitchDetails().isEmpty()){
					topologyBean.getUniswitchDetails().forEach(uniswitchDetailBean -> {
						UniswitchDetail uniswitchDetail = new UniswitchDetail();
						uniswitchDetail.setAutonegotiationEnabled(uniswitchDetailBean.getAutonegotiationEnabled());
						uniswitchDetail.setDuplex(uniswitchDetailBean.getDuplex());
						uniswitchDetail.setHandoff(uniswitchDetailBean.getHandoff());
						uniswitchDetail.setInnerVlan(uniswitchDetailBean.getInnerVlan());
						uniswitchDetail.setHostName(uniswitchDetailBean.getHostName());
						uniswitchDetail.setInterfaceName(uniswitchDetailBean.getInterfaceName());
						uniswitchDetail.setMaxMacLimit(uniswitchDetailBean.getMaxMacLimit());
						uniswitchDetail.setMediaType(uniswitchDetailBean.getMediaType());
						uniswitchDetail.setMgmtIp(uniswitchDetailBean.getMgmtIp());
						uniswitchDetail.setMode(uniswitchDetailBean.getMode());
						uniswitchDetail.setOuterVlan(uniswitchDetailBean.getOuterVlan());
						uniswitchDetail.setPhysicalPort(uniswitchDetailBean.getPhysicalPort());
						uniswitchDetail.setPortType(uniswitchDetailBean.getPortType());
						uniswitchDetail.setSpeed(uniswitchDetailBean.getSpeed());
						uniswitchDetail.setSwitchModel(uniswitchDetailBean.getSwitchModel());
						uniswitchDetail.setSyncVlanReqd(convertBooleanToByte(uniswitchDetailBean.getSyncVlanReqd()));
						uniswitchDetail.setUniswitchId(uniswitchDetailBean.getUniswitchId());
						uniswitchDetail.setEndDate(null);
						uniswitchDetail.setStartDate(new Timestamp(new Date().getTime()));
						uniswitchDetail.setLastModifiedDate(new Timestamp(new Date().getTime()));
						uniswitchDetail.setModifiedBy("FTI_Refresh");
						uniswitchDetail.setTopology(topology);
						uniswitchDetailRepository.saveAndFlush(uniswitchDetail);
					});
				}
			});
		}
	}
	
	private void saveMigrationTopologies(ServiceDetail serviceDetail, ServiceDetail exitingServiceDetail){
		logger.info("Inside saveMigrationTopologies method for serviceCode {}", serviceDetail.getServiceId());
    	if(exitingServiceDetail.getTopologies()!=null && !exitingServiceDetail.getTopologies().isEmpty()){
    		exitingServiceDetail.getTopologies().forEach(topologyBean -> {
				Topology topology = getMigrationTopology(serviceDetail, topologyBean);

				if(topologyBean.getRouterUplinkports()!=null && !topologyBean.getRouterUplinkports().isEmpty()){
					topologyBean.getRouterUplinkports().forEach(routerUplinkportBean -> {
						RouterUplinkport routerUplinkport = new RouterUplinkport();
						routerUplinkport.setPhysicalPort2Name(routerUplinkportBean.getPhysicalPort2Name());
						routerUplinkport.setRouterUplinkportId(routerUplinkportBean.getRouterUplinkportId());
						routerUplinkport.setPhysicalPort1Name(routerUplinkportBean.getPhysicalPort1Name());
						routerUplinkport.setEndDate(null);
						routerUplinkport.setStartDate(new Timestamp(new Date().getTime()));
						routerUplinkport.setLastModifiedDate(new Timestamp(new Date().getTime()));
						routerUplinkport.setModifiedBy("FTI_Refresh");
						routerUplinkport.setTopology(topology);
						routerUplinkPortRepository.saveAndFlush(routerUplinkport);
					});
				}

				if(topologyBean.getUniswitchDetails()!=null && !topologyBean.getUniswitchDetails().isEmpty()){
					topologyBean.getUniswitchDetails().forEach(uniswitchDetailBean -> {
						UniswitchDetail uniswitchDetail = new UniswitchDetail();
						uniswitchDetail.setAutonegotiationEnabled(uniswitchDetailBean.getAutonegotiationEnabled());
						uniswitchDetail.setDuplex(uniswitchDetailBean.getDuplex());
						uniswitchDetail.setHandoff(uniswitchDetailBean.getHandoff());
						uniswitchDetail.setInnerVlan(uniswitchDetailBean.getInnerVlan());
						uniswitchDetail.setHostName(uniswitchDetailBean.getHostName());
						uniswitchDetail.setInterfaceName(uniswitchDetailBean.getInterfaceName());
						uniswitchDetail.setMaxMacLimit(uniswitchDetailBean.getMaxMacLimit());
						uniswitchDetail.setMediaType(uniswitchDetailBean.getMediaType());
						uniswitchDetail.setMgmtIp(uniswitchDetailBean.getMgmtIp());
						uniswitchDetail.setMode(uniswitchDetailBean.getMode());
						uniswitchDetail.setOuterVlan(uniswitchDetailBean.getOuterVlan());
						uniswitchDetail.setPhysicalPort(uniswitchDetailBean.getPhysicalPort());
						uniswitchDetail.setPortType(uniswitchDetailBean.getPortType());
						uniswitchDetail.setSpeed(uniswitchDetailBean.getSpeed());
						uniswitchDetail.setSwitchModel(uniswitchDetailBean.getSwitchModel());
						uniswitchDetail.setSyncVlanReqd(uniswitchDetailBean.getSyncVlanReqd());
						uniswitchDetail.setUniswitchId(uniswitchDetailBean.getUniswitchId());
						uniswitchDetail.setEndDate(null);
						uniswitchDetail.setStartDate(new Timestamp(new Date().getTime()));
						uniswitchDetail.setLastModifiedDate(new Timestamp(new Date().getTime()));
						uniswitchDetail.setModifiedBy("FTI_Refresh");
						uniswitchDetail.setTopology(topology);
						uniswitchDetailRepository.saveAndFlush(uniswitchDetail);
					});
				}
			});
		}
	}

	private LmComponent getMigrationLmComponent(ServiceDetail serviceDetail, LmComponentBean lmComponentBean){
		LmComponent lmComponent = new LmComponent();
		lmComponent.setLmComponentId(lmComponentBean.getLmComponentId());
		lmComponent.setLmOnwlProvider(lmComponentBean.getLmOnwlProvider());
		lmComponent.setModifiedBy(lmComponentBean.getModifiedBy());
		lmComponent.setRemarks(lmComponentBean.getRemarks());
		lmComponent.setVersion(lmComponentBean.getVersion());
		lmComponent.setEndDate(null);
		lmComponent.setStartDate(new Timestamp(new Date().getTime()));
		lmComponent.setLastModifiedDate(new Timestamp(new Date().getTime()));
		lmComponent.setModifiedBy("FTI_Refresh");
		lmComponent.setServiceDetail(serviceDetail);
		return lmComponentRepository.saveAndFlush(lmComponent);
	}
	
	private LmComponent getMigrationLmComponent(ServiceDetail serviceDetail, LmComponent lmComponentBean){
		LmComponent lmComponent = new LmComponent();
		lmComponent.setLmComponentId(lmComponentBean.getLmComponentId());
		lmComponent.setLmOnwlProvider(lmComponentBean.getLmOnwlProvider());
		lmComponent.setModifiedBy(lmComponentBean.getModifiedBy());
		lmComponent.setRemarks(lmComponentBean.getRemarks());
		lmComponent.setVersion(lmComponentBean.getVersion());
		lmComponent.setEndDate(null);
		lmComponent.setStartDate(new Timestamp(new Date().getTime()));
		lmComponent.setLastModifiedDate(new Timestamp(new Date().getTime()));
		lmComponent.setModifiedBy("FTI_Refresh");
		lmComponent.setServiceDetail(serviceDetail);
		return lmComponentRepository.saveAndFlush(lmComponent);
	}

	private void saveMigrationLmComponents(ServiceDetail serviceDetail, ServiceDetailBean serviceDetailBean){
		if(serviceDetailBean.getLmComponentBeans()!=null && !serviceDetailBean.getLmComponentBeans().isEmpty()){
			serviceDetailBean.getLmComponentBeans().forEach(lmComponentBean -> {

				LmComponent lmComponent = getMigrationLmComponent(serviceDetail, lmComponentBean);

				if(!CollectionUtils.isEmpty(lmComponentBean.getRadwinLastmiles())){
					lmComponentBean.getRadwinLastmiles().forEach(radwinLastmileBean -> {
						radwinLastmileRepository.findById(radwinLastmileBean.getRadwinLastmileId()).ifPresent(radwinLastmile -> {
							radwinLastmile.setLmComponent(lmComponent);
							radwinLastmileRepository.saveAndFlush(radwinLastmile);
						});
					});
				}
				if(!CollectionUtils.isEmpty(lmComponentBean.getWimaxLastmiles())){
					lmComponentBean.getWimaxLastmiles().forEach(wimaxLastmileBean -> {
						wimaxLastmileRepository.findById(wimaxLastmileBean.getWimaxLastmileId()).ifPresent(wimaxLastmile -> {
							wimaxLastmile.setLmComponent(lmComponent);
							wimaxLastmileRepository.saveAndFlush(wimaxLastmile);
						});
					});
				}
				if(!CollectionUtils.isEmpty(lmComponentBean.getCambiumLastmiles())){
					Set<CambiumLastmile> cambiumLastmiles = new HashSet<>();
					lmComponentBean.getCambiumLastmiles().forEach(cambiumLastmileBean -> {
						cambiumLastmileRepository.findById(cambiumLastmileBean.getCambiumLastmileId()).ifPresent(cambiumLastmile -> {
							cambiumLastmile.setLmComponent(lmComponent);
							cambiumLastmileRepository.saveAndFlush(cambiumLastmile);
						});
					});
				}
			});
		}
	}
	
	private void saveMigrationLmComponents(ServiceDetail serviceDetail, ServiceDetail existingServiceDetail){
		if(existingServiceDetail.getLmComponents()!=null && !existingServiceDetail.getLmComponents().isEmpty()){
			existingServiceDetail.getLmComponents().forEach(lmComponentBean -> {

				LmComponent lmComponent = getMigrationLmComponent(serviceDetail, lmComponentBean);

				if(!CollectionUtils.isEmpty(lmComponentBean.getRadwinLastmiles())){
					lmComponentBean.getRadwinLastmiles().forEach(radwinLastmileBean -> {
						radwinLastmileRepository.findById(radwinLastmileBean.getRadwinLastmileId()).ifPresent(radwinLastmile -> {
							radwinLastmile.setLmComponent(lmComponent);
							radwinLastmileRepository.saveAndFlush(radwinLastmile);
						});
					});
				}
				if(!CollectionUtils.isEmpty(lmComponentBean.getWimaxLastmiles())){
					lmComponentBean.getWimaxLastmiles().forEach(wimaxLastmileBean -> {
						wimaxLastmileRepository.findById(wimaxLastmileBean.getWimaxLastmileId()).ifPresent(wimaxLastmile -> {
							wimaxLastmile.setLmComponent(lmComponent);
							wimaxLastmileRepository.saveAndFlush(wimaxLastmile);
						});
					});
				}
				if(!CollectionUtils.isEmpty(lmComponentBean.getCambiumLastmiles())){
					Set<CambiumLastmile> cambiumLastmiles = new HashSet<>();
					lmComponentBean.getCambiumLastmiles().forEach(cambiumLastmileBean -> {
						cambiumLastmileRepository.findById(cambiumLastmileBean.getCambiumLastmileId()).ifPresent(cambiumLastmile -> {
							cambiumLastmile.setLmComponent(lmComponent);
							cambiumLastmileRepository.saveAndFlush(cambiumLastmile);
						});
					});
				}
			});
		}
	}


	private Bgp getMigrationBgp(BgpBean bgpBean){
		Bgp bgp = new Bgp();
		bgp.setAluBackupPath(bgpBean.getAluBackupPath());
		bgp.setBgpPeerName(bgpBean.getAluBgpPeerName());
		bgp.setAluDisableBgpPeerGrpExtCommunity(convertBooleanToByte(bgpBean.getAluDisableBgpPeerGrpExtCommunity()));
		bgp.setAluKeepalive(bgpBean.getAluKeepalive());
		bgp.setAsoOverride(convertBooleanToByte(bgpBean.getAsoOverride()));
		bgp.setAsPath(bgpBean.getAsPath());
		bgp.setAuthenticationMode(bgpBean.getAuthenticationMode());
		bgp.setBgpId(bgpBean.getBgpId());
		bgp.setIsbgpMultihopReqd(convertBooleanToByte(bgpBean.getBgpMultihopReqd()));
		bgp.setBgpneighbourinboundv4routermapname(bgpBean.getBgpneighbourinboundv4routermapname());
		bgp.setBgpneighbourinboundv6routermapname(bgpBean.getBgpneighbourinboundv6routermapname());
		bgp.setEndDate(null);
		bgp.setHelloTime(bgpBean.getHelloTime());
		bgp.setHoldTime(bgpBean.getHoldTime());
		bgp.setIsauthenticationRequired(convertBooleanToByte(bgpBean.getIsauthenticationRequired()));
		bgp.setSplitHorizon(convertBooleanToByte(bgpBean.getSplitHorizon()));
		bgp.setIsbgpNeighbourInboundv4RoutemapEnabled(convertBooleanToByte(bgpBean.getIsbgpNeighbourInboundv4RoutemapEnabled()));
		bgp.setIsbgpNeighbourinboundv4RoutemapPreprovisioned(
				convertBooleanToByte(bgpBean.getIsbgpNeighbourinboundv4RoutemapPreprovisioned()));
		bgp.setIsbgpNeighbourInboundv6RoutemapEnabled(convertBooleanToByte(bgpBean.getIsbgpNeighbourInboundv6RoutemapEnabled()));
		bgp.setIsbgpNeighbourinboundv6RoutemapPreprovisioned(
				convertBooleanToByte(bgpBean.getIsbgpNeighbourinboundv6RoutemapPreprovisioned()));
		bgp.setIsebgpMultihopReqd(convertBooleanToByte(bgpBean.getIsebgpMultihopReqd()));
		bgp.setIsrtbh(convertBooleanToByte(bgpBean.getIsrtbh()));
		bgp.setLastModifiedDate(new Timestamp(new Date().getTime()));
		bgp.setLocalAsNumber(bgpBean.getLocalAsNumber());
		bgp.setLocalPreference(bgpBean.getLocalPreference());
		bgp.setLocalUpdateSourceInterface(bgpBean.getLocalUpdateSourceInterface());
		bgp.setLocalUpdateSourceIpv4Address(bgpBean.getLocalUpdateSourceIpv4Address());
		bgp.setLocalUpdateSourceIpv6Address(bgpBean.getLocalUpdateSourceIpv6Address());
		bgp.setMaxPrefix(bgpBean.getMaxPrefix());
		bgp.setMaxPrefixThreshold(bgpBean.getMaxPrefixThreshold());
		bgp.setMedValue(bgpBean.getMedValue());
		bgp.setModifiedBy("FTI_Refresh");
		bgp.setNeighborOn(bgpBean.getNeighborOn());
		bgp.setNeighbourCommunity(bgpBean.getNeighbourCommunity());
		bgp.setNeighbourshutdownRequired(convertBooleanToByte(bgpBean.getNeighbourshutdownRequired()));
		bgp.setPassword(bgpBean.getPassword());
		bgp.setPeerType(bgpBean.getPeerType());
		bgp.setRedistributeConnectedEnabled(convertBooleanToByte(bgpBean.getRedistributeConnectedEnabled()));
		bgp.setRedistributeStaticEnabled(convertBooleanToByte(bgpBean.getRedistributeStaticEnabled()));
		bgp.setRemoteAsNumber(StringUtils.isNotBlank(bgpBean.getRemoteAsNumber()) ?  Integer.parseInt(bgpBean.getRemoteAsNumber()) : 0);
		bgp.setRemoteCeAsnumber(bgpBean.getRemoteCeAsnumber());
		bgp.setRemoteIpv4Address(bgpBean.getRemoteIpv4Address());
		bgp.setRemoteIpv6Address(bgpBean.getRemoteIpv6Address());
		bgp.setRemoteUpdateSourceInterface(bgpBean.getRemoteUpdateSourceInterface());
		bgp.setRemoteUpdateSourceIpv4Address(bgpBean.getRemoteUpdateSourceIpv4Address());
		bgp.setRemoteUpdateSourceIpv6Address(bgpBean.getRemoteUpdateSourceIpv6Address());
		bgp.setRoutesExchanged(bgpBean.getRoutesExchanged());
		bgp.setRtbhOptions(bgpBean.getRtbhOptions());
		bgp.setSooRequired(convertBooleanToByte(bgpBean.getSsoRequired()));
		bgp.setSplitHorizon(convertBooleanToByte(bgpBean.getSplitHorizon()));
		bgp.setStartDate(new Timestamp(new Date().getTime()));
		bgp.setV6LocalPreference(bgpBean.getV6LocalPreference());
		return bgpRepository.saveAndFlush(bgp);
	}

	private void saveMigrationInterfaceDetails(InterfaceProtocolMapping interfaceProtocolMapping,
											   InterfaceDetailBean interfaceDetailBean){
    	if(Objects.nonNull(interfaceDetailBean)){
			interfaceProtocolMapping.setIscpeLanInterface(convertBooleanToByte(interfaceDetailBean.getIscpeLanInterface()));
			interfaceProtocolMapping.setIscpeWanInterface(convertBooleanToByte(interfaceDetailBean.getIscpeWanInterface()));
			interfaceProtocolMapping.setVersion(interfaceDetailBean.getVersion());
			interfaceProtocolMapping.setEndDate(null);
			interfaceProtocolMapping.setStartDate(new Timestamp(new Date().getTime()));
			interfaceProtocolMapping.setLastModifiedDate(new Timestamp(new Date().getTime()));
			interfaceProtocolMapping.setModifiedBy("FTI_Refresh");
		}
	}

	private ChannelizedE1serialInterface getMigrationChannelizedE1SerialInterface(ChannelizedE1serialInterfaceBean channelizedE1serialInterfaceBean){
		ChannelizedE1serialInterface channelizedE1serialInterface = new ChannelizedE1serialInterface();
		channelizedE1serialInterface.setBfdMultiplier(channelizedE1serialInterfaceBean.getBfdMultiplier());
		channelizedE1serialInterface.setBfdreceiveInterval(channelizedE1serialInterfaceBean.getBfdreceiveInterval());
		channelizedE1serialInterface.setBfdtransmitInterval(channelizedE1serialInterfaceBean.getBfdtransmitInterval());
		channelizedE1serialInterface.setChannelGroupNumber(channelizedE1serialInterfaceBean.getChannelGroupNumber());
		channelizedE1serialInterface.setCrcsize(channelizedE1serialInterfaceBean.getCrcsize());
		channelizedE1serialInterface.setDlciValue(channelizedE1serialInterfaceBean.getDlciValue());
		channelizedE1serialInterface.setDowncount(channelizedE1serialInterfaceBean.getDowncount());
		channelizedE1serialInterface.setE1serialInterfaceId(channelizedE1serialInterfaceBean.getE1serialInterfaceId());
		channelizedE1serialInterface.setEncapsulation(channelizedE1serialInterfaceBean.getEncapsulation());
		channelizedE1serialInterface.setFirsttimeSlot(channelizedE1serialInterfaceBean.getFirsttimeSlot());
		channelizedE1serialInterface.setFraming(channelizedE1serialInterfaceBean.getFraming());
		channelizedE1serialInterface.setHoldtimeUp(channelizedE1serialInterfaceBean.getHoldtimeUp());
		channelizedE1serialInterface.setHoldtimeDown(channelizedE1serialInterfaceBean.getHoldtimeDown());
		channelizedE1serialInterface.setInterfaceName(channelizedE1serialInterfaceBean.getInterfaceName());
		channelizedE1serialInterface.setIpv4Address(channelizedE1serialInterfaceBean.getIpv4Address());
		channelizedE1serialInterface.setIpv6Address(channelizedE1serialInterfaceBean.getIpv6Address());
		channelizedE1serialInterface.setIsbfdEnabled(convertBooleanToByte(channelizedE1serialInterfaceBean.getIsbfdEnabled()));
		channelizedE1serialInterface.setIscrcforenabled(convertBooleanToByte(channelizedE1serialInterfaceBean.getIscrcforenabled()));
		channelizedE1serialInterface.setIsframed(convertBooleanToByte(channelizedE1serialInterfaceBean.getIsframed()));
		channelizedE1serialInterface.setIshdlcConfig(convertBooleanToByte(channelizedE1serialInterfaceBean.getIshdlcConfig()));
		channelizedE1serialInterface.setIshsrpEnabled(convertBooleanToByte(channelizedE1serialInterfaceBean.getIshsrpEnabled()));
		channelizedE1serialInterface.setIsvrrpEnabled(convertBooleanToByte(channelizedE1serialInterfaceBean.getIsvrrpEnabled()));
		channelizedE1serialInterface.setKeepalive(channelizedE1serialInterfaceBean.getKeepalive());
		channelizedE1serialInterface.setLasttimeSlot(channelizedE1serialInterfaceBean.getLasttimeSlot());
		channelizedE1serialInterface.setMode(channelizedE1serialInterfaceBean.getMode());
		channelizedE1serialInterface.setModifiedIpv4Address(channelizedE1serialInterfaceBean.getModifiedIpv4Address());
		channelizedE1serialInterface.setModifiedIpv6Address(channelizedE1serialInterfaceBean.getModifiedIpv6Address());
		channelizedE1serialInterface.setModifiedSecondaryIpv4Address(channelizedE1serialInterfaceBean.getModifiedSecondaryIpv4Address());
		channelizedE1serialInterface.setModifiedSecondaryIpv6Address(channelizedE1serialInterfaceBean.getModifiedSecondaryIpv6Address());
		channelizedE1serialInterface.setMtu(channelizedE1serialInterfaceBean.getMtu());
		channelizedE1serialInterface.setPhysicalPort(channelizedE1serialInterfaceBean.getPhysicalPort());
		channelizedE1serialInterface.setPortType(channelizedE1serialInterfaceBean.getPortType());
		channelizedE1serialInterface.setSecondaryIpv4Address(channelizedE1serialInterfaceBean.getSecondaryIpv4Address());
		channelizedE1serialInterface.setSecondaryIpv6Address(channelizedE1serialInterfaceBean.getSecondaryIpv6Address());
		channelizedE1serialInterface.setUpcount(channelizedE1serialInterfaceBean.getUpcount());
		channelizedE1serialInterface.setEndDate(null);
		channelizedE1serialInterface.setStartDate(new Timestamp(new Date().getTime()));
		channelizedE1serialInterface.setLastModifiedDate(new Timestamp(new Date().getTime()));
		channelizedE1serialInterface.setModifiedBy("FTI_Refresh");
		return channelizedE1serialInterfaceRepository.saveAndFlush(channelizedE1serialInterface);
	}

	private ChannelizedSdhInterface getMigrationChannelizedSdhInterface(ChannelizedSdhInterfaceBean channelizedSdhInterfaceBean){
		ChannelizedSdhInterface channelizedSdhInterface = new ChannelizedSdhInterface();
		channelizedSdhInterface.setPhysicalPort(channelizedSdhInterfaceBean.getPhysicalPort());
		channelizedSdhInterface.setBfdreceiveInterval(channelizedSdhInterfaceBean.getBfdreceiveInterval());
		channelizedSdhInterface.set_4Kfirsttime_slot(channelizedSdhInterfaceBean.get_64kFirstTimeSlot());
		channelizedSdhInterface.set_4klasttimeSlot(channelizedSdhInterfaceBean.get_64kLastTimeSlot());
		channelizedSdhInterface.setBfdMultiplier(channelizedSdhInterfaceBean.getBfdMultiplier());
		channelizedSdhInterface.setBfdtransmitInterval(channelizedSdhInterfaceBean.getBfdtransmitInterval());
		channelizedSdhInterface.setChannelGroupNumber(channelizedSdhInterfaceBean.getChannelGroupNumber());
		channelizedSdhInterface.setDlciValue(channelizedSdhInterfaceBean.getDlciValue());
		channelizedSdhInterface.setDowncount(channelizedSdhInterfaceBean.getDowncount());
		channelizedSdhInterface.setEncapsulation(channelizedSdhInterfaceBean.getEncapsulation());
		channelizedSdhInterface.setFraming(channelizedSdhInterfaceBean.getFraming());
		channelizedSdhInterface.setHoldtimeDown(channelizedSdhInterfaceBean.getHoldtimeDown());
		channelizedSdhInterface.setHoldtimeUp(channelizedSdhInterfaceBean.getHoldtimeUp());
		channelizedSdhInterface.setInterfaceName(channelizedSdhInterfaceBean.getInterfaceName());
		channelizedSdhInterface.setIpv4Address(channelizedSdhInterfaceBean.getIpv4Address());
		channelizedSdhInterface.setIpv6Address(channelizedSdhInterfaceBean.getIpv6Address());
		channelizedSdhInterface.setIsbfdEnabled(convertBooleanToByte(channelizedSdhInterfaceBean.getIsbfdEnabled()));
		channelizedSdhInterface.setModifiedIipv6Address(channelizedSdhInterfaceBean.getModifiedIipv6Address());
		channelizedSdhInterface.setModifiedIpv4Address(channelizedSdhInterfaceBean.getModifiedIpv4Address());
		channelizedSdhInterface.setModifiedSecondaryIpv4Address(channelizedSdhInterfaceBean.getModifiedSecondaryIpv4Address());
		channelizedSdhInterface.setModifiedSecondaryIpv6Address(channelizedSdhInterfaceBean.getModifiedSecondaryIpv6Address());
		channelizedSdhInterface.setIsframed(convertBooleanToByte(channelizedSdhInterfaceBean.getIsframed()));
		channelizedSdhInterface.setIshdlcConfig(convertBooleanToByte(channelizedSdhInterfaceBean.getIshdlcConfig()));
		channelizedSdhInterface.setIshsrpEnabled(convertBooleanToByte(channelizedSdhInterfaceBean.getIshsrpEnabled()));
		channelizedSdhInterface.setIsvrrpEnabled(convertBooleanToByte(channelizedSdhInterfaceBean.getIsvrrpEnabled()));
		channelizedSdhInterface.setJ(channelizedSdhInterfaceBean.getJ());
		channelizedSdhInterface.setKeepalive(channelizedSdhInterfaceBean.getKeepalive());
		channelizedSdhInterface.setKlm(channelizedSdhInterfaceBean.getKlm());
		channelizedSdhInterface.setMode(channelizedSdhInterfaceBean.getMode());
		channelizedSdhInterface.setModifiedSecondaryIpv4Address(channelizedSdhInterfaceBean.getModifiedSecondaryIpv4Address());
		channelizedSdhInterface.setModifiedSecondaryIpv6Address(channelizedSdhInterfaceBean.getModifiedSecondaryIpv6Address());
		channelizedSdhInterface.setModifiedIpv4Address(channelizedSdhInterfaceBean.getModifiedIpv4Address());
		channelizedSdhInterface.setModifiedIipv6Address(channelizedSdhInterfaceBean.getModifiedIipv6Address());
		channelizedSdhInterface.setMtu(channelizedSdhInterfaceBean.getMtu());
		channelizedSdhInterface.setPortType(channelizedSdhInterfaceBean.getPortType());
		channelizedSdhInterface.setPosframing(channelizedSdhInterfaceBean.getPosframing());
		channelizedSdhInterface.setSdhInterfaceId(channelizedSdhInterfaceBean.getSdhInterfaceId());
		channelizedSdhInterface.setSecondaryIpv4Address(channelizedSdhInterfaceBean.getSecondaryIpv4Address());
		channelizedSdhInterface.setSecondaryIpv6Address(channelizedSdhInterfaceBean.getSecondaryIpv6Address());
		channelizedSdhInterface.setUpcount(channelizedSdhInterfaceBean.getUpcount());
		channelizedSdhInterface.setEndDate(null);
		channelizedSdhInterface.setStartDate(new Timestamp(new Date().getTime()));
		channelizedSdhInterface.setLastModifiedDate(new Timestamp(new Date().getTime()));
		channelizedSdhInterface.setModifiedBy("FTI_Refresh");
		return channelizedSdhInterfaceRepository.saveAndFlush(channelizedSdhInterface);
	}

	private EthernetInterface getMigrationEthernetInterface(EthernetInterfaceBean ethernetInterfaceBean){
		EthernetInterface ethernetInterface = new EthernetInterface();
		ethernetInterface.setAutonegotiationEnabled(ethernetInterfaceBean.getAutonegotiationEnabled());
		ethernetInterface.setBfdMultiplier(ethernetInterfaceBean.getBfdMultiplier());
		ethernetInterface.setBfdreceiveInterval(ethernetInterfaceBean.getBfdreceiveInterval());
		ethernetInterface.setBfdtransmitInterval(ethernetInterfaceBean.getBfdtransmitInterval());
		ethernetInterface.setDuplex(ethernetInterfaceBean.getDuplex());
		ethernetInterface.setEncapsulation(ethernetInterfaceBean.getEncapsulation());
		ethernetInterface.setEthernetInterfaceId(ethernetInterfaceBean.getEthernetInterfaceId());
		ethernetInterface.setFraming("NOT_APPLICABLE");
		ethernetInterface.setHoldtimeDown(ethernetInterfaceBean.getHoldtimeDown());
		ethernetInterface.setHoldtimeUp(ethernetInterfaceBean.getHoldtimeUp());
		ethernetInterface.setIsbfdEnabled(convertBooleanToByte(ethernetInterfaceBean.getIsbfdEnabled()));
		ethernetInterface.setIshsrpEnabled(convertBooleanToByte(ethernetInterfaceBean.getIshsrpEnabled()));
		ethernetInterface.setIsvrrpEnabled(convertBooleanToByte(ethernetInterfaceBean.getIsvrrpEnabled()));
		ethernetInterface.setModifiedIpv4Address(ethernetInterfaceBean.getModifiedIpv4Address());
		ethernetInterface.setModifiedIpv6Address(ethernetInterfaceBean.getModifiedIpv6Address());
		ethernetInterface.setModifiedSecondaryIpv4Address(ethernetInterfaceBean.getModifiedSecondaryIpv4Address());
		ethernetInterface.setModifiedSecondaryIpv6Address(ethernetInterfaceBean.getModifiedSecondaryIpv6Address());
		ethernetInterface.setInnerVlan(ethernetInterfaceBean.getInnerVlan());
		ethernetInterface.setIpv4Address(ethernetInterfaceBean.getIpv4Address());
		ethernetInterface.setIpv6Address(ethernetInterfaceBean.getIpv6Address());
		ethernetInterface.setMediaType(ethernetInterfaceBean.getMediaType());
		ethernetInterface.setMode(ethernetInterfaceBean.getMode());
		ethernetInterface.setInterfaceName(ethernetInterfaceBean.getInterfaceName());
		ethernetInterface.setMtu(ethernetInterfaceBean.getMtu());
		ethernetInterface.setOuterVlan(ethernetInterfaceBean.getOuterVlan());
		ethernetInterface.setPhysicalPort(ethernetInterfaceBean.getPhysicalPort());
		ethernetInterface.setPortType(ethernetInterfaceBean.getPortType());
		ethernetInterface.setQosLoopinPassthrough(ethernetInterfaceBean.getQosLoopinPassthrough());
		ethernetInterface.setSecondaryIpv4Address(ethernetInterfaceBean.getSecondaryIpv4Address());
		ethernetInterface.setSecondaryIpv6Address(ethernetInterfaceBean.getSecondaryIpv6Address());
		ethernetInterface.setSpeed(ethernetInterfaceBean.getSpeed());
		ethernetInterface.setEndDate(null);
		ethernetInterface.setStartDate(new Timestamp(new Date().getTime()));
		ethernetInterface.setLastModifiedDate(new Timestamp(new Date().getTime()));
		ethernetInterface.setModifiedBy("FTI_Refresh");
		return ethernetInterfaceRepository.saveAndFlush(ethernetInterface);
	}

	private Ospf getMigrationOspf(OspfBean ospfBean){
		Ospf ospf = new Ospf();
		ospf.setAreaId(ospfBean.getAreaId());
		ospf.setAuthenticationMode(ospfBean.getAuthenticationMode());
		ospf.setCost(ospfBean.getCost());
		ospf.setDomainId(ospfBean.getDomainId());
		ospf.setIsauthenticationRequired(convertBooleanToByte(ospfBean.getIsauthenticationRequired()));
		ospf.setIsenableShamlink(convertBooleanToByte(ospfBean.getIsenableShamlink()));
		ospf.setIsignoreipOspfMtu(convertBooleanToByte(ospfBean.getIsignoreipOspfMtu()));
		ospf.setIsnetworkP2p(convertBooleanToByte(ospfBean.getIsnetworkP2p()));
		ospf.setIsredistributeConnectedEnabled(convertBooleanToByte(ospfBean.getIsredistributeConnectedEnabled()));
		ospf.setIsredistributeStaticEnabled(convertBooleanToByte(ospfBean.getIsredistributeStaticEnabled()));
		ospf.setIsroutemapEnabled(convertBooleanToByte(ospfBean.getIsroutemapEnabled()));
		ospf.setIsroutemapPreprovisioned(ospfBean.getIsroutemapPreprovisioned());
		ospf.setLocalPreference(ospfBean.getLocalPreference());
		ospf.setLocalPreferenceV6(ospfBean.getLocalPreferenceV6());
		ospf.setOspfId(ospfBean.getOspfId());
		ospf.setOspfNetworkType(ospfBean.getOspfNetworkType());
		ospf.setPassword(ospfBean.getPassword());
		ospf.setProcessId(ospfBean.getProcessId());
		ospf.setRedistributeRoutermapname(ospfBean.getRedistributeRoutermapname());
		ospf.setShamlinkInterfaceName(ospfBean.getShamlinkInterfaceName());
		ospf.setShamlinkLocalAddress(ospfBean.getShamlinkLocalAddress());
		ospf.setShamlinkRemoteAddress(ospfBean.getShamlinkRemoteAddress());
		ospf.setVersion(ospfBean.getVersion());
		ospf.setEndDate(null);
		ospf.setStartDate(new Timestamp(new Date().getTime()));
		ospf.setLastModifiedDate(new Timestamp(new Date().getTime()));
		ospf.setModifiedBy("FTI_Refresh");
		return ospfRepository.saveAndFlush(ospf);
	}

	private Rip getMigrationRip(RipBean ripBean){
		Rip rip = new Rip();
		rip.setGroupName(ripBean.getGroupName());
		rip.setLocalPreference(ripBean.getLocalPreference());
		rip.setLocalPreferenceV6(ripBean.getLocalPreferenceV6());
		rip.setRipId(ripBean.getRipId());
		rip.setVersion(ripBean.getVersion());
		rip.setEndDate(null);
		rip.setStartDate(new Timestamp(new Date().getTime()));
		rip.setLastModifiedDate(new Timestamp(new Date().getTime()));
		rip.setModifiedBy("FTI_Refresh");
		return ripRepository.saveAndFlush(rip);
	}

	private RouterDetail getMigrationRouterDetail(RouterDetailBean routerDetailBean){
		RouterDetail routerDetail = new RouterDetail();
		routerDetail.setIpv4MgmtAddress(routerDetailBean.getIpv4MgmtAddress());
		routerDetail.setIpv6MgmtAddress(routerDetailBean.getIpv6MgmtAddress());
		routerDetail.setRouterHostname(routerDetailBean.getRouterHostname());
		routerDetail.setRouterId(routerDetailBean.getRouterId());
		routerDetail.setRouterModel(routerDetailBean.getRouterModel());
		routerDetail.setRouterMake(routerDetailBean.getRouterMake());
		routerDetail.setRouterRole(routerDetailBean.getRouterRole());
		routerDetail.setRouterType(routerDetailBean.getRouterType());
		routerDetail.setEndDate(null);
		routerDetail.setStartDate(new Timestamp(new Date().getTime()));
		routerDetail.setLastModifiedDate(new Timestamp(new Date().getTime()));
		routerDetail.setModifiedBy("FTI_Refresh");
		return routerDetailRepository.saveAndFlush(routerDetail);
	}

	private StaticProtocol getMigrationStaticProtocol(StaticProtocolBean staticProtocolBean) {
		StaticProtocol staticProtocol = new StaticProtocol();
		staticProtocol.setIsroutemapEnabled(convertBooleanToByte(staticProtocolBean.getIsroutemapEnabled()));
		staticProtocol.setIsroutemapPreprovisioned(convertBooleanToByte(staticProtocolBean.getIsroutemapPreprovisioned()));
		staticProtocol.setLocalPreference(staticProtocolBean.getLocalPreference());
		staticProtocol.setLocalPreferenceV6(staticProtocolBean.getLocalPreferenceV6());
		staticProtocol.setRedistributeRoutemapIpv4(staticProtocolBean.getRedistributeRoutemapIpv4());
		staticProtocol.setEndDate(null);
		staticProtocol.setStartDate(new Timestamp(new Date().getTime()));
		staticProtocol.setLastModifiedDate(new Timestamp(new Date().getTime()));
		staticProtocol.setModifiedBy("FTI_Refresh");
		staticProtocol.setStaticprotocolId(staticProtocolBean.getStaticprotocolId());
		return staticProtocolRepository.saveAndFlush(staticProtocol);
	}

	private Cpe getMigrationCpe(CpeBean cpeBean) {
		Cpe cpe = new Cpe();
		cpe.setCpeinitConfigparams(convertBooleanToByte(cpeBean.getCpeinitConfigparams()));
		cpe.setHostName(cpeBean.getHostName());
		cpe.setMake(cpeBean.getMake());
		cpe.setInitLoginpwd(cpeBean.getInitLoginpwd());
		cpe.setInitUsername(cpeBean.getInitUsername());
		cpe.setIsaceconfigurable(convertBooleanToByte(cpeBean.getIsaceconfigurable()));
		cpe.setLastModifiedDate(new Timestamp(new Date().getTime()));
		cpe.setLoopbackInterfaceName(cpeBean.getLoopbackInterfaceName());
		cpe.setMgmtLoopbackV4address(cpeBean.getMgmtLoopbackV4address());
		cpe.setMgmtLoopbackV6address(cpeBean.getMgmtLoopbackV6address());
		cpe.setModifiedBy("FTI_Refresh");
		cpe.setNniCpeConfig(convertBooleanToByte(cpeBean.getNniCpeConfig()));
		cpe.setSendInittemplate(convertBooleanToByte(cpeBean.getSendInittemplate()));
		cpe.setServiceId(cpeBean.getServiceId());
		cpe.setSnmpServerCommunity(cpeBean.getSnmpServerCommunity());
		cpe.setStartDate(new Timestamp(new Date().getTime()));
		cpe.setUnmanagedCePartnerdeviceWanip(cpeBean.getUnmanagedCePartnerdeviceWanip());
		cpe.setVsatCpeConfig(convertBooleanToByte(cpeBean.getVsatCpeConfig()));
		return cpeRepository.saveAndFlush(cpe);
	}

	private Eigrp getMigrationEigrp(EigrpBean eigrpBean){
		Eigrp eigrp = new Eigrp();
		eigrp.setEigrpBwKbps(eigrpBean.getEigrpBwKbps());
		eigrp.setEigrpProtocolId(eigrpBean.getEigrpProtocolId());
		eigrp.setEndDate(null);
		eigrp.setInterfaceDelay(eigrpBean.getInterfaceDelay());
		eigrp.setIsredistributeConnectedEnabled(convertBooleanToByte(eigrpBean.getIsredistributeConnectedEnabled()));
		eigrp.setIsredistributeStaticEnabled(convertBooleanToByte(eigrpBean.getIsredistributeStaticEnabled()));
		eigrp.setIsroutemapEnabled(convertBooleanToByte(eigrpBean.getIsroutemapEnabled()));
		eigrp.setIsroutemapPreprovisioned(convertBooleanToByte(eigrpBean.getIsroutemapPreprovisioned()));
		eigrp.setLastModifiedDate(new Timestamp(new Date().getTime()));
		eigrp.setLoad(eigrpBean.getLoad());
		eigrp.setLocalAsnumber(eigrpBean.getLocalAsnumber());
		eigrp.setModifiedBy("FTI_Refresh");
		eigrp.setMtu(eigrpBean.getMtu());
		eigrp.setRedistributeRoutemapName(eigrpBean.getRedistributeRoutemapName());
		eigrp.setRedistributionDelay(eigrpBean.getRedistributionDelay());
		eigrp.setReliability(eigrpBean.getReliability());
		eigrp.setRemoteAsnumber(eigrpBean.getRemoteAsnumber());
		eigrp.setRemoteCeAsnumber(eigrpBean.getRemoteCeAsnumber());
		eigrp.setSooRequired(convertBooleanToByte(eigrpBean.getSooRequired()));
		eigrp.setStartDate(new Timestamp(new Date().getTime()));
		return eigrpRepository.saveAndFlush(eigrp);
	}

	private void saveMigrationInterfaceProtocolMapping(ServiceDetail serviceDetail, ServiceDetailBean serviceDetailBean){
		
		logger.info("saveMigrationInterfaceProtocolMapping-for-serviceid={} serviceCode={}",serviceDetail.getId(),serviceDetail.getServiceId());
		
		if(serviceDetailBean.getInterfaceProtocolMappingBean()!=null){
			InterfaceProtocolMappingBean interfaceProtocolMappingBean = serviceDetailBean.getInterfaceProtocolMappingBean();

			if(interfaceProtocolMappingBean.getPeDetailsBean()!=null){
				PEDetailsBean peDetailsBean = interfaceProtocolMappingBean.getPeDetailsBean();

				InterfaceProtocolMapping interfaceProtocolMapping = new InterfaceProtocolMapping();
				interfaceProtocolMapping.setServiceDetail(serviceDetail);

				if(!CollectionUtils.isEmpty(peDetailsBean.getBgpBeans())){
					peDetailsBean.getBgpBeans().forEach(bgpBean -> {
						if(interfaceProtocolMapping.getVersion()==null){
							saveMigrationInterfaceDetails(interfaceProtocolMapping, bgpBean.getInterfaceDetailBean());
						}
						Bgp bgp = getMigrationBgp(bgpBean);
						if(!CollectionUtils.isEmpty(bgpBean.getPolicyTypes())){
							bgpBean.getPolicyTypes().forEach(policyTypeBean -> {
								saveMigrationPolicyTypes(policyTypeBean,bgp,"bgp");
							});
						}
						if(!CollectionUtils.isEmpty(bgpBean.getWanStaticRoutes())){
							bgpBean.getWanStaticRoutes().forEach(wanStaticRouteBean -> {
								saveMigrationWanStaticRoutes(wanStaticRouteBean, bgp);
							});
						}
						interfaceProtocolMapping.setBgp(bgp);
					});
				}

				if(!CollectionUtils.isEmpty(peDetailsBean.getChannelizedE1serialInterfaceBeans())){
					peDetailsBean.getChannelizedE1serialInterfaceBeans().forEach(e1SerialInterfaceBean -> {
						if(interfaceProtocolMapping.getVersion()==null) {
							saveMigrationInterfaceDetails(interfaceProtocolMapping, e1SerialInterfaceBean.getInterfaceDetailBean());
						}
						ChannelizedE1serialInterface channelizedE1serialInterface = getMigrationChannelizedE1SerialInterface(e1SerialInterfaceBean);
						if(!CollectionUtils.isEmpty(e1SerialInterfaceBean.getAclPolicyCriterias())){
							e1SerialInterfaceBean.getAclPolicyCriterias().forEach(aclPolicyCriteriaBean -> {
								AclPolicyCriteria aclPolicyCriteria = saveMigrationAclPolicyCriterias(aclPolicyCriteriaBean);
								aclPolicyCriteria.setChannelizedE1serialInterface(channelizedE1serialInterface);
								aclPolicyCriteriaRepository.saveAndFlush(aclPolicyCriteria);
							});
						}
						interfaceProtocolMapping.setChannelizedE1serialInterface(channelizedE1serialInterface);
					});
				}

				if(!CollectionUtils.isEmpty(peDetailsBean.getChannelizedSdhInterfaceBeans())){
					peDetailsBean.getChannelizedSdhInterfaceBeans().forEach(channelizedSdhInterfaceBean -> {
						if(interfaceProtocolMapping.getVersion()==null) {
							saveMigrationInterfaceDetails(interfaceProtocolMapping, channelizedSdhInterfaceBean.getInterfaceDetailBean());
						}
						ChannelizedSdhInterface channelizedSdhInterface = getMigrationChannelizedSdhInterface(channelizedSdhInterfaceBean);
						if(!CollectionUtils.isEmpty(channelizedSdhInterfaceBean.getAclPolicyCriterias())){
							channelizedSdhInterfaceBean.getAclPolicyCriterias().forEach(aclPolicyCriteriaBean -> {
								AclPolicyCriteria aclPolicyCriteria = saveMigrationAclPolicyCriterias(aclPolicyCriteriaBean);
								aclPolicyCriteria.setChannelizedSdhInterface(channelizedSdhInterface);
								aclPolicyCriteriaRepository.saveAndFlush(aclPolicyCriteria);
							});
						}
						interfaceProtocolMapping.setChannelizedSdhInterface(channelizedSdhInterface);
					});
				}

				if(!CollectionUtils.isEmpty(peDetailsBean.getEigrpBeans())){
					peDetailsBean.getEigrpBeans().forEach(eigrpBean -> {
						if(interfaceProtocolMapping.getVersion()==null) {
							saveMigrationInterfaceDetails(interfaceProtocolMapping, eigrpBean.getInterfaceDetailBean());
						}
						Eigrp eigrp = getMigrationEigrp(eigrpBean);
						interfaceProtocolMapping.setEigrp(eigrp);
					});
				}

				if(!CollectionUtils.isEmpty(peDetailsBean.getEthernetInterfaceBeans())){
					peDetailsBean.getEthernetInterfaceBeans().forEach(ethernetInterfaceBean -> {
						if(interfaceProtocolMapping.getVersion()==null) {
							saveMigrationInterfaceDetails(interfaceProtocolMapping, ethernetInterfaceBean.getInterfaceDetailBean());
						}
						EthernetInterface ethernetInterface = getMigrationEthernetInterface(ethernetInterfaceBean);
						if(!CollectionUtils.isEmpty(ethernetInterfaceBean.getHsrpVrrpProtocols())){
							ethernetInterfaceBean.getHsrpVrrpProtocols().forEach(hsrpVrrpProtocolBean -> {
								HsrpVrrpProtocol hsrpVrrpProtocol = new HsrpVrrpProtocol();
								hsrpVrrpProtocol.setHelloValue(hsrpVrrpProtocolBean.getHelloValue());
								hsrpVrrpProtocol.setHoldValue(hsrpVrrpProtocolBean.getHoldValue());
								hsrpVrrpProtocol.setHsrpVrrpId(hsrpVrrpProtocolBean.getHsrpVrrpId());
								hsrpVrrpProtocol.setPriority(hsrpVrrpProtocolBean.getPriority());
								hsrpVrrpProtocol.setRole(hsrpVrrpProtocolBean.getRole());
								hsrpVrrpProtocol.setEndDate(null);
								hsrpVrrpProtocol.setStartDate(new Timestamp(new Date().getTime()));
								hsrpVrrpProtocol.setLastModifiedDate(new Timestamp(new Date().getTime()));
								hsrpVrrpProtocol.setModifiedBy("FTI_Refresh");
								hsrpVrrpProtocol.setTimerUnit(hsrpVrrpProtocolBean.getTimerUnit());
								hsrpVrrpProtocol.setVirtualIpv4Address(hsrpVrrpProtocolBean.getVirtualIpv4Address());
								hsrpVrrpProtocol.setVirtualIpv6Address(hsrpVrrpProtocolBean.getVirtualIpv6Address());
								hsrpVrrpProtocol.setEthernetInterface(ethernetInterface);
								hsrpVrrpProtocolRepository.saveAndFlush(hsrpVrrpProtocol);
							});
						}
						if(!CollectionUtils.isEmpty(ethernetInterfaceBean.getAclPolicyCriterias())){
							ethernetInterfaceBean.getAclPolicyCriterias().forEach(aclPolicyCriteriaBean -> {
								AclPolicyCriteria aclPolicyCriteria = saveMigrationAclPolicyCriterias(aclPolicyCriteriaBean);
								aclPolicyCriteria.setEthernetInterface(ethernetInterface);
								aclPolicyCriteriaRepository.saveAndFlush(aclPolicyCriteria);
							});
						}
						interfaceProtocolMapping.setEthernetInterface(ethernetInterface);
					});
				}

				if(!CollectionUtils.isEmpty(peDetailsBean.getOspfBeans())){
					peDetailsBean.getOspfBeans().forEach(ospfBean -> {
						if(interfaceProtocolMapping.getVersion()==null) {
							saveMigrationInterfaceDetails(interfaceProtocolMapping, ospfBean.getInterfaceDetailBean());
						}
						Ospf ospf = getMigrationOspf(ospfBean);
						if(!CollectionUtils.isEmpty(ospfBean.getPolicyTypes())){
							ospfBean.getPolicyTypes().forEach(policyTypeBean -> {
								saveMigrationPolicyTypes(policyTypeBean, ospf,"ospf");
							});
						}
						interfaceProtocolMapping.setOspf(ospf);
					});
				}

				if(!CollectionUtils.isEmpty(peDetailsBean.getRipBeans())){
					peDetailsBean.getRipBeans().forEach(ripBean -> {
						if(interfaceProtocolMapping.getVersion()==null) {
							saveMigrationInterfaceDetails(interfaceProtocolMapping, ripBean.getInterfaceDetailBean());
						}
						Rip rip = getMigrationRip(ripBean);
						if(!CollectionUtils.isEmpty(ripBean.getPolicyTypes())){
							ripBean.getPolicyTypes().forEach(policyTypeBean -> {
								saveMigrationPolicyTypes(policyTypeBean, rip, "rip");
							});
						}
						interfaceProtocolMapping.setRip(rip);
					});
				}

				if(!CollectionUtils.isEmpty(peDetailsBean.getRouterDetailBeans())){
					peDetailsBean.getRouterDetailBeans().forEach(routerDetailBean -> {
						if(interfaceProtocolMapping.getVersion()==null) {
							saveMigrationInterfaceDetails(interfaceProtocolMapping, routerDetailBean.getInterfaceDetailBean());
						}
						RouterDetail routerDetail = getMigrationRouterDetail(routerDetailBean);
						interfaceProtocolMapping.setRouterDetail(routerDetail);
					});
				}

				if(!CollectionUtils.isEmpty(peDetailsBean.getStaticProtocolBeans())){
					peDetailsBean.getStaticProtocolBeans().forEach(staticProtocolBean -> {
						if(interfaceProtocolMapping.getVersion()==null) {
							saveMigrationInterfaceDetails(interfaceProtocolMapping, staticProtocolBean.getInterfaceDetailBean());
						}
						StaticProtocol staticProtocol = getMigrationStaticProtocol(staticProtocolBean);
						if(!CollectionUtils.isEmpty(staticProtocolBean.getWanStaticRoutes())){
							staticProtocolBean.getWanStaticRoutes().forEach(wanStaticRouteBean -> {
								WanStaticRoute wanStaticRoute = new WanStaticRoute();
								wanStaticRoute.setAdvalue(wanStaticRouteBean.getAdvalue());
								wanStaticRoute.setDescription(wanStaticRouteBean.getDescription());
								wanStaticRoute.setGlobal(convertBooleanToByte(wanStaticRouteBean.getGlobal()));
								wanStaticRoute.setIpsubnet(wanStaticRouteBean.getIpsubnet());
								wanStaticRoute.setNextHopid(wanStaticRouteBean.getNextHopid());
								wanStaticRoute.setPopCommunity(wanStaticRouteBean.getPopCommunity());
								wanStaticRoute.setRegionalCommunity(wanStaticRouteBean.getRegionalCommunity());
								wanStaticRoute.setServiceCommunity(wanStaticRouteBean.getServiceCommunity());
								wanStaticRoute.setWanstaticrouteId(wanStaticRouteBean.getWanstaticrouteId());
								wanStaticRoute.setIsCewan(convertBooleanToByte(wanStaticRouteBean.getIsCewan()));
								wanStaticRoute.setIsCpelanStaticroutes(convertBooleanToByte(wanStaticRouteBean.getIsCpelanStaticroutes()));
								wanStaticRoute.setIsCpewanStaticroutes(convertBooleanToByte(wanStaticRouteBean.getIsCpewanStaticroutes()));
								wanStaticRoute.setIsPewan(convertBooleanToByte(wanStaticRouteBean.getIsPewan()));
								wanStaticRoute.setEndDate(null);
								wanStaticRoute.setLastModifiedDate(new Timestamp(new Date().getTime()));
								wanStaticRoute.setModifiedBy("FTI_Refresh");
								wanStaticRoute.setStartDate(new Timestamp(new Date().getTime()));
								wanStaticRoute.setStaticProtocol(staticProtocol);
								wanStaticRouteRepository.saveAndFlush(wanStaticRoute);
							});
						}
						interfaceProtocolMapping.setStaticProtocol(staticProtocol);
					});
				}

				if(!CollectionUtils.isEmpty(peDetailsBean.getCpeBeans())){
					peDetailsBean.getCpeBeans().forEach(cpeBean -> {
						saveMigrationInterfaceDetails(interfaceProtocolMapping, cpeBean.getInterfaceDetailBean());
						Cpe cpe = getMigrationCpe(cpeBean);
						interfaceProtocolMapping.setCpe(cpe);
					});
				}
				if(interfaceProtocolMapping.getVersion()==null){
					interfaceProtocolMapping.setVersion(1);
				}
				logger.info("saveMigrationInterfaceProtocolMapping-getPeDetailsBean-for-serviceid={} serviceCode={}",serviceDetail.getId(),serviceDetail.getServiceId());
				
				interfaceProtocolMappingRepository.saveAndFlush(interfaceProtocolMapping);
				
				logger.info("saveMigrationInterfaceProtocolMapping-getPeDetailsBean-for-serviceid={} serviceCode={} interfaceProtocolMapping={}",serviceDetail.getId(),serviceDetail.getServiceId(),interfaceProtocolMapping.getInterfaceProtocolMappingId());
			}

			if(interfaceProtocolMappingBean.getCeDetailsBean()!=null){
				CEDetailsBean ceDetailsBean = interfaceProtocolMappingBean.getCeDetailsBean();
				InterfaceProtocolMapping interfaceProtocolMapping = new InterfaceProtocolMapping();
				interfaceProtocolMapping.setServiceDetail(serviceDetail);

				if(!CollectionUtils.isEmpty(ceDetailsBean.getEthernetInterfaceBeans())){
					ceDetailsBean.getEthernetInterfaceBeans().forEach(ethernetInterfaceBean -> {
						saveMigrationInterfaceDetails(interfaceProtocolMapping, ethernetInterfaceBean.getInterfaceDetailBean());
						EthernetInterface ethernetInterface = getMigrationEthernetInterface(ethernetInterfaceBean);
						if(!CollectionUtils.isEmpty(ethernetInterfaceBean.getAclPolicyCriterias())){
							ethernetInterfaceBean.getAclPolicyCriterias().forEach(aclPolicyCriteriaBean -> {
								AclPolicyCriteria aclPolicyCriteria = saveMigrationAclPolicyCriterias(aclPolicyCriteriaBean);
								aclPolicyCriteria.setEthernetInterface(ethernetInterface);
								aclPolicyCriteriaRepository.saveAndFlush(aclPolicyCriteria);
							});
						}
						interfaceProtocolMapping.setEthernetInterface(ethernetInterface);
					});
				}else {
					logger.info("saveMigrationInterfaceProtocolMapping.CE EthernetInterface not exists for serviceId::{}",serviceDetail.getId());
					EthernetInterface ethernetInterface = new EthernetInterface();
					ethernetInterface.setEndDate(null);
					ethernetInterface.setStartDate(new Timestamp(new Date().getTime()));
					ethernetInterface.setLastModifiedDate(new Timestamp(new Date().getTime()));
					ethernetInterface.setModifiedBy("FTI_Refresh");
					ethernetInterfaceRepository.saveAndFlush(ethernetInterface);
					interfaceProtocolMapping.setEthernetInterface(ethernetInterface);
				}

				if(!CollectionUtils.isEmpty(ceDetailsBean.getCpeBeans())){
					ceDetailsBean.getCpeBeans().forEach(cpeBean -> {
						saveMigrationInterfaceDetails(interfaceProtocolMapping, cpeBean.getInterfaceDetailBean());
						Cpe cpe = getMigrationCpe(cpeBean);
						interfaceProtocolMapping.setCpe(cpe);
					});
				}else {
					logger.info("saveMigrationInterfaceProtocolMapping.CPE not exists for serviceId::{}",serviceDetail.getId());
					Cpe cpe = new Cpe();
					cpe.setServiceId(serviceDetail.getServiceId());
					cpe.setStartDate(new Timestamp(new Date().getTime()));
					cpeRepository.saveAndFlush(cpe);
					interfaceProtocolMapping.setServiceDetail(serviceDetail);
					interfaceProtocolMapping.setCpe(cpe);
					interfaceProtocolMapping.setIscpeLanInterface((byte)0);
					interfaceProtocolMapping.setIscpeWanInterface((byte)1);
				}

				if(interfaceProtocolMapping.getVersion()==null){
					interfaceProtocolMapping.setVersion(1);
				}
				
				logger.info("saveMigrationInterfaceProtocolMapping-getCeDetailsBean-for-serviceid={} serviceCode={}",serviceDetail.getId(),serviceDetail.getServiceId());
				
				interfaceProtocolMappingRepository.saveAndFlush(interfaceProtocolMapping);
				
				logger.info("saveMigrationInterfaceProtocolMapping-getCeDetailsBean-for-serviceid={} serviceCode={} interfaceProtocolMapping={}",serviceDetail.getId(),serviceDetail.getServiceId(),interfaceProtocolMapping.getInterfaceProtocolMappingId());
			}

		}
	}
	
	
private void saveMigrationInterfaceProtocolMapping(ServiceDetail serviceDetail, InterfaceProtocolMappingBean interfaceProtocolMappingBean){
		
		logger.info("saveMigrationInterfaceProtocolMapping-for-serviceid={} serviceCode={}",serviceDetail.getId(),serviceDetail.getServiceId());
		
		if(interfaceProtocolMappingBean!=null){

			if(interfaceProtocolMappingBean.getPeDetailsBean()!=null){
				PEDetailsBean peDetailsBean = interfaceProtocolMappingBean.getPeDetailsBean();

				InterfaceProtocolMapping interfaceProtocolMapping = new InterfaceProtocolMapping();
				interfaceProtocolMapping.setServiceDetail(serviceDetail);

				if(!CollectionUtils.isEmpty(peDetailsBean.getBgpBeans())){
					peDetailsBean.getBgpBeans().forEach(bgpBean -> {
						if(interfaceProtocolMapping.getVersion()==null){
							saveMigrationInterfaceDetails(interfaceProtocolMapping, bgpBean.getInterfaceDetailBean());
						}
						Bgp bgp = getMigrationBgp(bgpBean);
						if(!CollectionUtils.isEmpty(bgpBean.getPolicyTypes())){
							bgpBean.getPolicyTypes().forEach(policyTypeBean -> {
								saveMigrationPolicyTypes(policyTypeBean,bgp,"bgp");
							});
						}
						if(!CollectionUtils.isEmpty(bgpBean.getWanStaticRoutes())){
							bgpBean.getWanStaticRoutes().forEach(wanStaticRouteBean -> {
								saveMigrationWanStaticRoutes(wanStaticRouteBean, bgp);
							});
						}
						interfaceProtocolMapping.setBgp(bgp);
					});
				}

				if(!CollectionUtils.isEmpty(peDetailsBean.getChannelizedE1serialInterfaceBeans())){
					peDetailsBean.getChannelizedE1serialInterfaceBeans().forEach(e1SerialInterfaceBean -> {
						if(interfaceProtocolMapping.getVersion()==null) {
							saveMigrationInterfaceDetails(interfaceProtocolMapping, e1SerialInterfaceBean.getInterfaceDetailBean());
						}
						ChannelizedE1serialInterface channelizedE1serialInterface = getMigrationChannelizedE1SerialInterface(e1SerialInterfaceBean);
						if(!CollectionUtils.isEmpty(e1SerialInterfaceBean.getAclPolicyCriterias())){
							e1SerialInterfaceBean.getAclPolicyCriterias().forEach(aclPolicyCriteriaBean -> {
								AclPolicyCriteria aclPolicyCriteria = saveMigrationAclPolicyCriterias(aclPolicyCriteriaBean);
								aclPolicyCriteria.setChannelizedE1serialInterface(channelizedE1serialInterface);
								aclPolicyCriteriaRepository.saveAndFlush(aclPolicyCriteria);
							});
						}
						interfaceProtocolMapping.setChannelizedE1serialInterface(channelizedE1serialInterface);
					});
				}

				if(!CollectionUtils.isEmpty(peDetailsBean.getChannelizedSdhInterfaceBeans())){
					peDetailsBean.getChannelizedSdhInterfaceBeans().forEach(channelizedSdhInterfaceBean -> {
						if(interfaceProtocolMapping.getVersion()==null) {
							saveMigrationInterfaceDetails(interfaceProtocolMapping, channelizedSdhInterfaceBean.getInterfaceDetailBean());
						}
						ChannelizedSdhInterface channelizedSdhInterface = getMigrationChannelizedSdhInterface(channelizedSdhInterfaceBean);
						if(!CollectionUtils.isEmpty(channelizedSdhInterfaceBean.getAclPolicyCriterias())){
							channelizedSdhInterfaceBean.getAclPolicyCriterias().forEach(aclPolicyCriteriaBean -> {
								AclPolicyCriteria aclPolicyCriteria = saveMigrationAclPolicyCriterias(aclPolicyCriteriaBean);
								aclPolicyCriteria.setChannelizedSdhInterface(channelizedSdhInterface);
								aclPolicyCriteriaRepository.saveAndFlush(aclPolicyCriteria);
							});
						}
						interfaceProtocolMapping.setChannelizedSdhInterface(channelizedSdhInterface);
					});
				}

				if(!CollectionUtils.isEmpty(peDetailsBean.getEigrpBeans())){
					peDetailsBean.getEigrpBeans().forEach(eigrpBean -> {
						if(interfaceProtocolMapping.getVersion()==null) {
							saveMigrationInterfaceDetails(interfaceProtocolMapping, eigrpBean.getInterfaceDetailBean());
						}
						Eigrp eigrp = getMigrationEigrp(eigrpBean);
						interfaceProtocolMapping.setEigrp(eigrp);
					});
				}

				if(!CollectionUtils.isEmpty(peDetailsBean.getEthernetInterfaceBeans())){
					peDetailsBean.getEthernetInterfaceBeans().forEach(ethernetInterfaceBean -> {
						if(interfaceProtocolMapping.getVersion()==null) {
							saveMigrationInterfaceDetails(interfaceProtocolMapping, ethernetInterfaceBean.getInterfaceDetailBean());
						}
						EthernetInterface ethernetInterface = getMigrationEthernetInterface(ethernetInterfaceBean);
						if(!CollectionUtils.isEmpty(ethernetInterfaceBean.getHsrpVrrpProtocols())){
							ethernetInterfaceBean.getHsrpVrrpProtocols().forEach(hsrpVrrpProtocolBean -> {
								HsrpVrrpProtocol hsrpVrrpProtocol = new HsrpVrrpProtocol();
								hsrpVrrpProtocol.setHelloValue(hsrpVrrpProtocolBean.getHelloValue());
								hsrpVrrpProtocol.setHoldValue(hsrpVrrpProtocolBean.getHoldValue());
								hsrpVrrpProtocol.setHsrpVrrpId(hsrpVrrpProtocolBean.getHsrpVrrpId());
								hsrpVrrpProtocol.setPriority(hsrpVrrpProtocolBean.getPriority());
								hsrpVrrpProtocol.setRole(hsrpVrrpProtocolBean.getRole());
								hsrpVrrpProtocol.setEndDate(null);
								hsrpVrrpProtocol.setStartDate(new Timestamp(new Date().getTime()));
								hsrpVrrpProtocol.setLastModifiedDate(new Timestamp(new Date().getTime()));
								hsrpVrrpProtocol.setModifiedBy("FTI_Refresh");
								hsrpVrrpProtocol.setTimerUnit(hsrpVrrpProtocolBean.getTimerUnit());
								hsrpVrrpProtocol.setVirtualIpv4Address(hsrpVrrpProtocolBean.getVirtualIpv4Address());
								hsrpVrrpProtocol.setVirtualIpv6Address(hsrpVrrpProtocolBean.getVirtualIpv6Address());
								hsrpVrrpProtocol.setEthernetInterface(ethernetInterface);
								hsrpVrrpProtocolRepository.saveAndFlush(hsrpVrrpProtocol);
							});
						}
						if(!CollectionUtils.isEmpty(ethernetInterfaceBean.getAclPolicyCriterias())){
							ethernetInterfaceBean.getAclPolicyCriterias().forEach(aclPolicyCriteriaBean -> {
								AclPolicyCriteria aclPolicyCriteria = saveMigrationAclPolicyCriterias(aclPolicyCriteriaBean);
								aclPolicyCriteria.setEthernetInterface(ethernetInterface);
								aclPolicyCriteriaRepository.saveAndFlush(aclPolicyCriteria);
							});
						}
						interfaceProtocolMapping.setEthernetInterface(ethernetInterface);
					});
				}

				if(!CollectionUtils.isEmpty(peDetailsBean.getOspfBeans())){
					peDetailsBean.getOspfBeans().forEach(ospfBean -> {
						if(interfaceProtocolMapping.getVersion()==null) {
							saveMigrationInterfaceDetails(interfaceProtocolMapping, ospfBean.getInterfaceDetailBean());
						}
						Ospf ospf = getMigrationOspf(ospfBean);
						if(!CollectionUtils.isEmpty(ospfBean.getPolicyTypes())){
							ospfBean.getPolicyTypes().forEach(policyTypeBean -> {
								saveMigrationPolicyTypes(policyTypeBean, ospf,"ospf");
							});
						}
						interfaceProtocolMapping.setOspf(ospf);
					});
				}

				if(!CollectionUtils.isEmpty(peDetailsBean.getRipBeans())){
					peDetailsBean.getRipBeans().forEach(ripBean -> {
						if(interfaceProtocolMapping.getVersion()==null) {
							saveMigrationInterfaceDetails(interfaceProtocolMapping, ripBean.getInterfaceDetailBean());
						}
						Rip rip = getMigrationRip(ripBean);
						if(!CollectionUtils.isEmpty(ripBean.getPolicyTypes())){
							ripBean.getPolicyTypes().forEach(policyTypeBean -> {
								saveMigrationPolicyTypes(policyTypeBean, rip, "rip");
							});
						}
						interfaceProtocolMapping.setRip(rip);
					});
				}

				if(!CollectionUtils.isEmpty(peDetailsBean.getRouterDetailBeans())){
					peDetailsBean.getRouterDetailBeans().forEach(routerDetailBean -> {
						if(interfaceProtocolMapping.getVersion()==null) {
							saveMigrationInterfaceDetails(interfaceProtocolMapping, routerDetailBean.getInterfaceDetailBean());
						}
						RouterDetail routerDetail = getMigrationRouterDetail(routerDetailBean);
						interfaceProtocolMapping.setRouterDetail(routerDetail);
					});
				}

				if(!CollectionUtils.isEmpty(peDetailsBean.getStaticProtocolBeans())){
					peDetailsBean.getStaticProtocolBeans().forEach(staticProtocolBean -> {
						if(interfaceProtocolMapping.getVersion()==null) {
							saveMigrationInterfaceDetails(interfaceProtocolMapping, staticProtocolBean.getInterfaceDetailBean());
						}
						StaticProtocol staticProtocol = getMigrationStaticProtocol(staticProtocolBean);
						if(!CollectionUtils.isEmpty(staticProtocolBean.getWanStaticRoutes())){
							staticProtocolBean.getWanStaticRoutes().forEach(wanStaticRouteBean -> {
								WanStaticRoute wanStaticRoute = new WanStaticRoute();
								wanStaticRoute.setAdvalue(wanStaticRouteBean.getAdvalue());
								wanStaticRoute.setDescription(wanStaticRouteBean.getDescription());
								wanStaticRoute.setGlobal(convertBooleanToByte(wanStaticRouteBean.getGlobal()));
								wanStaticRoute.setIpsubnet(wanStaticRouteBean.getIpsubnet());
								wanStaticRoute.setNextHopid(wanStaticRouteBean.getNextHopid());
								wanStaticRoute.setPopCommunity(wanStaticRouteBean.getPopCommunity());
								wanStaticRoute.setRegionalCommunity(wanStaticRouteBean.getRegionalCommunity());
								wanStaticRoute.setServiceCommunity(wanStaticRouteBean.getServiceCommunity());
								wanStaticRoute.setWanstaticrouteId(wanStaticRouteBean.getWanstaticrouteId());
								wanStaticRoute.setIsCewan(convertBooleanToByte(wanStaticRouteBean.getIsCewan()));
								wanStaticRoute.setIsCpelanStaticroutes(convertBooleanToByte(wanStaticRouteBean.getIsCpelanStaticroutes()));
								wanStaticRoute.setIsCpewanStaticroutes(convertBooleanToByte(wanStaticRouteBean.getIsCpewanStaticroutes()));
								wanStaticRoute.setIsPewan(convertBooleanToByte(wanStaticRouteBean.getIsPewan()));
								wanStaticRoute.setEndDate(null);
								wanStaticRoute.setLastModifiedDate(new Timestamp(new Date().getTime()));
								wanStaticRoute.setModifiedBy("FTI_Refresh");
								wanStaticRoute.setStartDate(new Timestamp(new Date().getTime()));
								wanStaticRoute.setStaticProtocol(staticProtocol);
								wanStaticRouteRepository.saveAndFlush(wanStaticRoute);
							});
						}
						interfaceProtocolMapping.setStaticProtocol(staticProtocol);
					});
				}

				if(!CollectionUtils.isEmpty(peDetailsBean.getCpeBeans())){
					peDetailsBean.getCpeBeans().forEach(cpeBean -> {
						saveMigrationInterfaceDetails(interfaceProtocolMapping, cpeBean.getInterfaceDetailBean());
						Cpe cpe = getMigrationCpe(cpeBean);
						interfaceProtocolMapping.setCpe(cpe);
					});
				}
				if(interfaceProtocolMapping.getVersion()==null){
					interfaceProtocolMapping.setVersion(1);
				}
				logger.info("saveMigrationInterfaceProtocolMapping-getPeDetailsBean-for-serviceid={} serviceCode={}",serviceDetail.getId(),serviceDetail.getServiceId());
				
				interfaceProtocolMappingRepository.saveAndFlush(interfaceProtocolMapping);
				
				logger.info("saveMigrationInterfaceProtocolMapping-getPeDetailsBean-for-serviceid={} serviceCode={} interfaceProtocolMapping={}",serviceDetail.getId(),serviceDetail.getServiceId(),interfaceProtocolMapping.getInterfaceProtocolMappingId());
			}

			if(interfaceProtocolMappingBean.getCeDetailsBean()!=null){
				CEDetailsBean ceDetailsBean = interfaceProtocolMappingBean.getCeDetailsBean();
				InterfaceProtocolMapping interfaceProtocolMapping = new InterfaceProtocolMapping();
				interfaceProtocolMapping.setServiceDetail(serviceDetail);

				if(!CollectionUtils.isEmpty(ceDetailsBean.getEthernetInterfaceBeans())){
					ceDetailsBean.getEthernetInterfaceBeans().forEach(ethernetInterfaceBean -> {
						saveMigrationInterfaceDetails(interfaceProtocolMapping, ethernetInterfaceBean.getInterfaceDetailBean());
						EthernetInterface ethernetInterface = getMigrationEthernetInterface(ethernetInterfaceBean);
						if(!CollectionUtils.isEmpty(ethernetInterfaceBean.getAclPolicyCriterias())){
							ethernetInterfaceBean.getAclPolicyCriterias().forEach(aclPolicyCriteriaBean -> {
								AclPolicyCriteria aclPolicyCriteria = saveMigrationAclPolicyCriterias(aclPolicyCriteriaBean);
								aclPolicyCriteria.setEthernetInterface(ethernetInterface);
								aclPolicyCriteriaRepository.saveAndFlush(aclPolicyCriteria);
							});
						}
						interfaceProtocolMapping.setEthernetInterface(ethernetInterface);
					});
				}

				if(!CollectionUtils.isEmpty(ceDetailsBean.getCpeBeans())){
					ceDetailsBean.getCpeBeans().forEach(cpeBean -> {
						saveMigrationInterfaceDetails(interfaceProtocolMapping, cpeBean.getInterfaceDetailBean());
						Cpe cpe = getMigrationCpe(cpeBean);
						interfaceProtocolMapping.setCpe(cpe);
					});
				}

				if(interfaceProtocolMapping.getVersion()==null){
					interfaceProtocolMapping.setVersion(1);
				}
				
				logger.info("saveMigrationInterfaceProtocolMapping-getCeDetailsBean-for-serviceid={} serviceCode={}",serviceDetail.getId(),serviceDetail.getServiceId());
				
				interfaceProtocolMappingRepository.saveAndFlush(interfaceProtocolMapping);
				
				logger.info("saveMigrationInterfaceProtocolMapping-getCeDetailsBean-for-serviceid={} serviceCode={} interfaceProtocolMapping={}",serviceDetail.getId(),serviceDetail.getServiceId(),interfaceProtocolMapping.getInterfaceProtocolMappingId());
			}

		}
	}

	private void persistMigrationData(ServiceDetail serviceDetail, ServiceDetailBean serviceDetailBean, Map<String, String> ftiAttributesMap){

		saveMigrationAluSchedulerPolicies(serviceDetail, serviceDetailBean);

//		saveMigrationCiscoImportMaps(serviceDetail, serviceDetailBean);

		saveMigrationIpAddresses(serviceDetail, serviceDetailBean);

		saveMigrationMulticastings(serviceDetail, serviceDetailBean);

		saveMigrationServiceQos(serviceDetail, serviceDetailBean);

		saveMigrationVpnSolutions(serviceDetail, serviceDetailBean);

		saveMigrationVrfs(serviceDetail, serviceDetailBean,ftiAttributesMap);

		saveMigrationTopologies(serviceDetail, serviceDetailBean);

		saveMigrationLmComponents(serviceDetail, serviceDetailBean);

		saveMigrationInterfaceProtocolMapping(serviceDetail, serviceDetailBean);
	}
	
	@Transactional(readOnly = false)
	public Boolean persistMigrationData(String serviceId,Integer scServiceDetailId) {

		logger.info("Migration Starts for serviceId {}", serviceId);

		Optional<ScServiceDetail>optionalSc=scServiceDetailRepository.findById(scServiceDetailId);
		
		
		
		ScServiceDetail scServiceDetail=optionalSc.get();
		ScOrder scOrderOr=scServiceDetail.getScOrder();
		ServiceDetail existingServiceDetail = serviceDetailRepository
				.findFirstByServiceIdAndServiceStateOrderByVersionDesc(serviceId, TaskStatusConstants.ACTIVE);
		if (existingServiceDetail != null) {
			
			Map<String, String> scComponentAttributesAMap =commonFulfillmentUtils.getComponentAttributes(
					existingServiceDetail.getScServiceDetailId(), AttributeConstants.COMPONENT_LM, "A");
			
			ServiceDetail serviceDetail = serviceActivationService.saveServiceDetailsActivation(
					existingServiceDetail.getServiceId(), existingServiceDetail.getVersion(), scOrderOr,
					scServiceDetail, scComponentAttributesAMap);
			
			logger.info("Migartion: serviceDetail saved successfully for given service id{}", serviceId);
			
			saveMigrationAluSchedulerPolicies(serviceDetail, existingServiceDetail);

			logger.info("Migartion: AluSchedulerPolicies saved successfully for given service id{}", serviceId);

			saveMigrationCiscoImportMaps(serviceDetail, existingServiceDetail);

			logger.info("Migartion: CiscoImportMaps saved successfully for given service id{}", serviceId);

			saveMigrationIpAddresses(serviceDetail, existingServiceDetail);

			logger.info("Migartion: IpAddresses saved successfully for given service id{}", serviceId);

			saveMigrationMulticastings(serviceDetail, existingServiceDetail);

			logger.info("Migartion: Multicastings saved successfully for given service id{}", serviceId);

			saveMigrationServiceQos(serviceDetail, existingServiceDetail);

			logger.info("Migartion: AluSchedulerPolicies saved successfully for given service id{}", serviceId);

			saveMigrationVpnSolutions(serviceDetail, existingServiceDetail);

			logger.info("Migartion: VpnSolutions saved successfully for given service id{}", serviceId);

			saveMigrationVrfs(serviceDetail, existingServiceDetail);

			logger.info("Migartion: Vrfs saved successfully for given service id{}", serviceId);

			saveMigrationTopologies(serviceDetail, existingServiceDetail);

			logger.info("Migartion: Topologies saved successfully for given service id{}", serviceId);

			saveMigrationLmComponents(serviceDetail, existingServiceDetail);
			logger.info("Migartion: LmComponents saved successfully for given service id{}", serviceId);

			saveMigrationInterfaceProtocolMapping(serviceDetail,
					getInterfaceProtocolMappingBean(existingServiceDetail, false));

			logger.info("Migartion: InterfaceProtocolMapping saved successfully for given service id{}", serviceId);
			
			existingServiceDetail.setServiceState(TaskStatusConstants.CANCELLED);

			logger.info("Migration ends for serviceId {}", serviceId);
			return true;
		} else {
			logger.error("Migartion: Service Detail is not found for given service id{}", serviceId);
		}

		return false;
	}

	private ServiceDetail saveMigrationServiceDetails(ServiceDetailBean serviceDetailBean, OrderDetail orderDetail, boolean migration, ScServiceDetail scServiceDetail){
		ServiceDetail serviceDetail = new ServiceDetail();
		serviceDetail.setAddressLine1(serviceDetailBean.getAddressLine1());
		serviceDetail.setAddressLine2(serviceDetailBean.getAddressLine2());
		serviceDetail.setAddressLine3(serviceDetailBean.getAddressLine3());
		serviceDetail.setAluSvcid(serviceDetailBean.getAluSvcid());
		serviceDetail.setAluSvcName(serviceDetailBean.getAluSvcName());
		serviceDetail.setBurstableBw(serviceDetailBean.getBurstableBw());
		serviceDetail.setBurstableBwUnit(serviceDetailBean.getBurstableBwUnit());
		serviceDetail.setCssSammgrId((serviceDetailBean.getCsoSammgrId()!=null) ? Integer.parseInt(serviceDetailBean.getCsoSammgrId()) : null);
		serviceDetail.setDataTransferCommit(serviceDetailBean.getDataTransferCommit());
		serviceDetail.setDataTransferCommitUnit(serviceDetailBean.getDataTransferCommitUnit());
		serviceDetail.setDescription(serviceDetailBean.getDescription());
		serviceDetail.setEligibleForRevision(convertBooleanToByte(serviceDetailBean.getEligibleForRevision()));
		serviceDetail.setEndDate(serviceDetailBean.getEndDate());
		serviceDetail.setExpediteTerminate(convertBooleanToByte(serviceDetailBean.getExpediteTerminate()));
		serviceDetail.setExternalRefid(serviceDetailBean.getExternalRefid());
		serviceDetail.setIntefaceDescSvctag(serviceDetailBean.getIntefaceDescSvctag());
		serviceDetail.setIscustomConfigReqd(convertBooleanToByte(serviceDetailBean.getIscustomConfigReqd()));
		serviceDetail.setIsdowntimeReqd(convertBooleanToByte(serviceDetailBean.getIsdowntimeReqd()));
		serviceDetail.setIsIdcService(convertBooleanToByte(serviceDetailBean.getIsIdcService()));
		serviceDetail.setIsportChanged(convertBooleanToByte(serviceDetailBean.getPortChanged()));
		serviceDetail.setIsrevised(convertBooleanToByte(serviceDetailBean.getIsrevised()));
		serviceDetail.setLastmileProvider(serviceDetailBean.getLastmileProvider());
		serviceDetail.setLastmileType(serviceDetailBean.getLastmileType());
		serviceDetail.setLastModifiedDate(serviceDetailBean.getLastModifiedDate());
		serviceDetail.setMgmtType(serviceDetailBean.getMgmtType());
		serviceDetail.setModifiedBy("FTI_Refresh");
		serviceDetail.setNetpRefid(serviceDetailBean.getNetpRefid());
		serviceDetail.setOldServiceId(serviceDetailBean.getOldServiceId());
		serviceDetail.setRedundancyRole(serviceDetailBean.getRedundancyRole());
		serviceDetail.setScServiceDetailId(serviceDetailBean.getScServiceDetailId());
		serviceDetail.setScopeOfMgmt(serviceDetailBean.getScopeOfMgmt());
		serviceDetail.setServiceBandwidth(serviceDetailBean.getServiceBandwidth());
		serviceDetail.setServiceBandwidthUnit(serviceDetailBean.getServiceBandwidthUnit());
		serviceDetail.setServiceComponenttype(serviceDetailBean.getServiceComponenttype());
		serviceDetail.setServiceState(migration ?  "ACTIVE" : serviceDetailBean.getServiceState());
		if(Objects.nonNull(scServiceDetail)) {
			serviceDetail.setServiceId(scServiceDetail.getUuid());
			serviceDetail.setScServiceDetailId(scServiceDetail.getId());
			serviceDetail.setServiceState("ACTIVE");
		}
		serviceDetail.setServiceSubtype(serviceDetailBean.getServiceSubtype());
		serviceDetail.setServiceType(serviceDetailBean.getServiceType());
		if("unknown".equalsIgnoreCase(serviceDetailBean.getServiceSubtype())){
			if("GVPN".equalsIgnoreCase(serviceDetailBean.getServiceType())) {
				serviceDetail.setServiceSubtype("GVPN");
			}else if("ILL".equalsIgnoreCase(serviceDetailBean.getServiceType())) {
				serviceDetail.setServiceSubtype("STDILL");
			}
		}
		serviceDetail.setSkipDummyConfig(convertBooleanToByte(serviceDetailBean.getSkipDummyConfig()));
		serviceDetail.setSvclinkRole(serviceDetailBean.getSvclinkRole());
		serviceDetail.setSolutionId(serviceDetailBean.getSolutionId());
		serviceDetail.setSolutionName(serviceDetailBean.getSolutionName());
		serviceDetail.setStartDate(serviceDetailBean.getStartDate());
		serviceDetail.setSvclinkSrvid(serviceDetailBean.getSvclinkSrvid());
		serviceDetail.setTrfsDate(serviceDetailBean.getTrfsDate());
		serviceDetail.setTrfsTriggerDate(serviceDetailBean.getTrfsTriggerDate());
		serviceDetail.setTriggerNccmOrder(convertBooleanToByte(serviceDetailBean.getTriggerNccmOrder()));
		serviceDetail.setUsageModel(serviceDetailBean.getUsageModel());
		serviceDetail.setVersion(serviceDetailBean.getVersion());
		serviceDetail.setRouterMake(serviceDetailBean.getRouterMake());
		serviceDetail.setCity(serviceDetailBean.getCity());
		serviceDetail.setCountry(serviceDetailBean.getCountry());
		serviceDetail.setPincode(serviceDetailBean.getPincode());
		serviceDetail.setState(serviceDetailBean.getState());
		serviceDetail.setLat(serviceDetailBean.getLat());
		serviceDetail.setLongiTude(serviceDetailBean.getLongitude());
		serviceDetail.setServiceOrderType(serviceDetailBean.getServiceOrderType());
		serviceDetail.setOrderDetail(orderDetail);
		serviceDetail.setStartDate(new Timestamp(new Date().getTime()));
		serviceDetail.setLastModifiedDate(new Timestamp(new Date().getTime()));
		return serviceDetailRepository.saveAndFlush(serviceDetail);
	}
	

	public void persistFTIRefreshData(TaskBean taskBean,String isTermination, Integer scServiceDetailId, Map<String, String> ftiAttributesMap) {
		logger.info("Inside persistFTIRefreshData Method for serviceCode {}", taskBean.getServiceCode());
		OrderDetailBean orderDetailBean = taskBean.getOrderDetails();

		ServiceDetailBean serviceDetailBean = orderDetailBean.getServiceDetailBeans().stream().findFirst().get();

		OrderDetail orderDetail = null;
		boolean migration = false;
		ScServiceDetail scServiceDetail = null;
		if(isTermination!=null && isTermination.equalsIgnoreCase("yes")&& scServiceDetailId!=null) {
			Optional<ScServiceDetail> serviceDetail = scServiceDetailRepository.findById(scServiceDetailId);
			if(serviceDetail.isPresent()) {
				scServiceDetail=serviceDetail.get();
			}
		}
		if(Objects.nonNull(orderDetailBean)) {
			// For Migration Scenario
			if(scServiceDetail==null){
			    Optional<ScServiceDetail> serviceDetail = scServiceDetailRepository.findById(scServiceDetailId);
    			if(serviceDetail.isPresent()) {
    				scServiceDetail=serviceDetail.get();
    			}
			}
			if (Objects.isNull(orderDetailBean.getOrderId())) {
				migration = true;
				orderDetail = constructOrderDetails(orderDetailBean, migration, scServiceDetail);
			} else {
				Optional<OrderDetail> optionalOrderDetail = orderDetailRepository.findById(orderDetailBean.getOrderId());
				if (optionalOrderDetail.isPresent()) {
					orderDetail = optionalOrderDetail.get();
				}
			}
			ServiceDetail serviceDetail = saveMigrationServiceDetails(serviceDetailBean, orderDetail, migration, scServiceDetail);
			persistMigrationData(serviceDetail, serviceDetailBean,ftiAttributesMap);
			logger.info("flush-serviceDetail-fti-MigrationData={},serviceCode={}",serviceDetail.getId(),serviceDetail.getServiceId());
			serviceDetailRepository.flush();
			if(serviceDetail.getServiceId()!=null && serviceDetail.getScServiceDetailId()!=null) {
				logger.info("fti-MigrationData calling satsoc for service id={},scServiceDetailId={}",serviceDetail.getServiceId(),serviceDetail.getScServiceDetailId());
				SatcoServiceDataRefreshBean satcoServiceDataRefreshBean=new SatcoServiceDataRefreshBean();
				satcoServiceDataRefreshBean.setServiceId(serviceDetail.getServiceId());
				satcoServiceDataRefreshBean.setScServiceDetailId(serviceDetail.getScServiceDetailId());
				serviceActivationService.refreshSatcoData(satcoServiceDataRefreshBean);
			}
			
		}
	}

	@Transactional(readOnly = false)
    public IpDetailsBean getIpDetails(Integer scServiceDetailId) {
    	IpDetailsBean ipDetailsBean = new IpDetailsBean();
		ServiceDetail serviceDetail = serviceDetailRepository.findFirstByScServiceDetailIdAndServiceStateInAndModifiedByNotInOrderByVersionDesc(
				scServiceDetailId, Arrays.asList(TaskStatusConstants.ISSUED, TaskStatusConstants.ACTIVE),
				Arrays.asList("Optimus_Port_ILL_migration", "Optimus_Port_GVPN_migration"));
		if (Objects.nonNull(serviceDetail)) {
			logger.info("getIpDetails method invoked for serviceCode {}", serviceDetail.getServiceId());
			OrderDetail orderDetail = serviceDetail.getOrderDetail();
			if (!CollectionUtils.isEmpty(serviceDetail.getIpAddressDetails())) {
				List<IpAddressDetailBean> ipAddressDetailBeans = new ArrayList<>();
				serviceDetail.getIpAddressDetails().forEach(ipAddressDetail -> {
					IpAddressDetailBean ipAddressDetailBean = new IpAddressDetailBean();
					setIpAddressDetailBean(ipAddressDetailBean, ipAddressDetail);
					if (!CollectionUtils.isEmpty(ipAddressDetail.getIpaddrLanv4Addresses())) {
						ipAddressDetail.getIpaddrLanv4Addresses().forEach(ipaddrLanv4Address -> {
							IpaddrLanv4AddressBean ipaddrLanv4AddressBean = new IpaddrLanv4AddressBean();
							setIpaddrLanv4AddressBean(ipaddrLanv4AddressBean, ipaddrLanv4Address);
							ipAddressDetailBean.getIpaddrLanv4Addresses().add(ipaddrLanv4AddressBean);
						});
					}
					if (!CollectionUtils.isEmpty(ipAddressDetail.getIpaddrLanv6Addresses())) {
						ipAddressDetail.getIpaddrLanv6Addresses().forEach(ipaddrLanv6Address -> {
							IpaddrLanv6AddressBean ipaddrLanv6AddressBean = new IpaddrLanv6AddressBean();
							setIpaddrLanv6AddressBean(ipaddrLanv6AddressBean, ipaddrLanv6Address);
							ipAddressDetailBean.getIpaddrLanv6Addresses().add(ipaddrLanv6AddressBean);
						});
					}
					if (!CollectionUtils.isEmpty(ipAddressDetail.getIpaddrWanv4Addresses())) {
						ipAddressDetail.getIpaddrWanv4Addresses().forEach(ipaddrWanv4Address -> {
							IpaddrWanv4AddressBean ipaddrWanv4AddressBean = new IpaddrWanv4AddressBean();
							setIpaddrWanv4AddressBean(ipaddrWanv4AddressBean, ipaddrWanv4Address);
							ipAddressDetailBean.getIpaddrWanv4Addresses().add(ipaddrWanv4AddressBean);
						});
					}
					if (!CollectionUtils.isEmpty(ipAddressDetail.getIpaddrWanv6Addresses())) {
						ipAddressDetail.getIpaddrWanv6Addresses().forEach(ipaddrWanv6Address -> {
							IpaddrWanv6AddressBean ipaddrWanv6AddressBean = new IpaddrWanv6AddressBean();
							setIpaddrWanv6AddressBean(ipaddrWanv6AddressBean, ipaddrWanv6Address);
							ipAddressDetailBean.getIpaddrWanv6Addresses().add(ipaddrWanv6AddressBean);
						});
					}
					ipAddressDetailBeans.add(ipAddressDetailBean);
				});
				ipDetailsBean.setIpAddressDetailBeans(ipAddressDetailBeans);
			}
			if (!CollectionUtils.isEmpty(serviceDetail.getInterfaceProtocolMappings())) {
				List<BgpBean> bgpBeans = new ArrayList<>();
				List<StaticProtocolBean> staticProtocolBeans = new ArrayList<>();
				List<RouterDetailBean> routerDetailBeans = new ArrayList<>();
				serviceDetail.getInterfaceProtocolMappings().forEach(ipm -> {
					if (Objects.nonNull(ipm.getBgp())) {
						BgpBean bgpBean = new BgpBean();
						setBgpBean(bgpBean, ipm.getBgp());
						bgpBeans.add(bgpBean);
					}
					if (Objects.nonNull(ipm.getStaticProtocol())) {
						StaticProtocolBean staticProtocolBean = new StaticProtocolBean();
						setStaticProtocolBean(staticProtocolBean, ipm.getStaticProtocol());
						staticProtocolBeans.add(staticProtocolBean);
					}
					if (Objects.nonNull(ipm.getRouterDetail())) {
						RouterDetailBean routerDetailBean = new RouterDetailBean();
						setRouterDetailBean(routerDetailBean, ipm.getRouterDetail());
						routerDetailBeans.add(routerDetailBean);
					}
				});
				ipDetailsBean.setStaticProtocolBeans(staticProtocolBeans);
				ipDetailsBean.setBgpBeans(bgpBeans);
				ipDetailsBean.setRouterDetailBeans(routerDetailBeans);
			}
			Optional<ScServiceDetail> oScServiceDetail = scServiceDetailRepository.findById(scServiceDetailId);
			if(oScServiceDetail.isPresent()){
				ScServiceDetail scServiceDetail = oScServiceDetail.get();
				Map<String, String> scComponentsAttrMapA = commonFulfillmentUtils
						.getComponentAttributes(scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A");
				if(!scComponentsAttrMapA.isEmpty()){
					ipDetailsBean.setMuxPortNo(scComponentsAttrMapA.get("endMuxNodePort"));
				}
			}
			ServiceOrderDetailsBean serviceOrderDetailsBean = new ServiceOrderDetailsBean();
			if(StringUtils.isNotEmpty(serviceDetail.getUsageModel())) {
				serviceOrderDetailsBean.setUsageModel(serviceDetail.getUsageModel());
			}
			if(StringUtils.isNotEmpty(serviceDetail.getServiceBandwidthUnit())) {
				serviceOrderDetailsBean.setServiceBandwidthUnit(serviceDetail.getServiceBandwidthUnit());
			}
			if(StringUtils.isNotEmpty(orderDetail.getSfdcOpptyId())) {
				serviceOrderDetailsBean.setSfdcOpportunityId(orderDetail.getSfdcOpptyId());
			}
			if(StringUtils.isNotEmpty(orderDetail.getOptyBidCategory())) {
				serviceOrderDetailsBean.setOptyBidCategory(orderDetail.getOptyBidCategory());
			}
			if(serviceDetail.getServiceBandwidth()!=null){
				serviceOrderDetailsBean.setServiceBandwidth(serviceDetail.getServiceBandwidth().toString());
			}
			ipDetailsBean.setServiceOrderDetailsBean(serviceOrderDetailsBean);
		}
        return ipDetailsBean;
	}

	private OrderDetail constructOrderDetails(OrderDetailBean orderDetailBean, boolean migration, ScServiceDetail scServiceDetail){
    	OrderDetail orderDetail = new OrderDetail();
    	updateOrderDetails(orderDetail, orderDetailBean, migration, scServiceDetail);
    	return orderDetail;
	}

	@Transactional(readOnly = false)
	public TaskBean test(Integer taskId) {
		
		TaskBean taskBean=new TaskBean();
		

		Task task = new Task();
		task.setCreatedTime(new Timestamp(new Date().getTime()));
		task.setUpdatedTime(new Timestamp(new Date().getTime()));
		task.setPriority(1);
		task.setOrderCode("OAD");
		taskRepository.saveAndFlush(task);
		
		String string=null;
		
		OrderDetail detail=new OrderDetail();
		
		detail.setOrderUuid("OAD");
		
		orderDetailRepository.saveAndFlush(detail);
		
		
		
		
		return taskBean;
		
	}


	public BSODetailsBean getBsoDetails(String serviceCode) {
		BSODetailsBean bsoDetailsBean =new BSODetailsBean();
		String lmType = null;
		String bsoCircuitId = null;
		ServiceDetail serviceDetail = serviceDetailRepository.findFirstByServiceIdOrderByVersionDesc(serviceCode);
		Stg0SapPoDtlOptimus stg0SapPoDtlOptimus  = stg0SapPoDtlOptimusRepository.
				findFirstByTclServiceIdAndProductComponentOrderByPoCreationDateDesc(serviceCode);

		ScComponentAttribute lmTypeAttribute = scComponentAttributesRepository.
				findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetail.getScServiceDetailId(), "lmType", "LM", "A");
		ScComponentAttribute bsoCircuitIdAttribute = scComponentAttributesRepository.
				findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetail.getScServiceDetailId(), "bsoCircuitId", "LM", "A");
		if(Objects.nonNull(bsoCircuitIdAttribute)) bsoCircuitId=bsoCircuitIdAttribute.getAttributeValue();
		if(Objects.nonNull(stg0SapPoDtlOptimus)) {
			if(Objects.isNull(bsoCircuitIdAttribute) || bsoCircuitId.isEmpty()) {
				bsoCircuitId=stg0SapPoDtlOptimus.getVendorRefIdOrderId();
			}
		}
		if((Objects.nonNull(lmTypeAttribute)) && (lmTypeAttribute.getAttributeValue().toLowerCase().contains("offnet"))) {
			if (Objects.nonNull(stg0SapPoDtlOptimus))
				setBSODetailsBean(bsoDetailsBean, stg0SapPoDtlOptimus, bsoCircuitId);

			else {
				if (Objects.nonNull(bsoCircuitId) && (!bsoCircuitId.isEmpty()))
					bsoDetailsBean.setVendorRefIdOrderId(bsoCircuitId);
			}
		}
		return bsoDetailsBean;
       }
	private void constructMultiVrfAttribute(Integer serviceId,ServiceDetailBean serviceDetailBean){
		logger.info("Inside constructMultiVrfAttribute method :{}",serviceId);
    	VrfBean vrfBean= new VrfBean();
		Optional<ScServiceDetail> scServiceDetail=scServiceDetailRepository.findById(serviceId);
		if(scServiceDetail.isPresent() && CommonConstants.GVPN.equalsIgnoreCase(scServiceDetail.get().getErfPrdCatalogProductName()) && scServiceDetail.get().getMultiVrfSolution()!=null && CommonConstants.Y.equalsIgnoreCase(scServiceDetail.get().getMultiVrfSolution())){
			List<String> vrfAttributes=Arrays.asList(CommonConstants.FLEXICOS,CommonConstants.MASTER_VRF_FLAG,CommonConstants.MULTI_VRF_SOLUTION,
					CommonConstants.TOTAL_VRF_BANDWIDTH_MBPS,CommonConstants.NUMBER_OF_VRFS,CommonConstants.SLAVE_VRF_SERVICE_ID,CommonConstants.MASTER_VRF_SERVICE_ID,
					CommonConstants.CUSTOMER_PROJECT_NAME,CommonConstants.VRF_BASED_BILLING);
			List<ScServiceAttribute> scServiceAttributes =scServiceAttributeRepository.findByScServiceDetail_idAndAttributeNameIn(scServiceDetail.get().getId(),vrfAttributes);
			logger.info("Multi vrf attributes list size :{}",scServiceAttributes.size());
			if(scServiceAttributes!=null && !scServiceAttributes.isEmpty()) {
				if (scServiceAttributes.stream().filter(vrfAttribute -> vrfAttribute.getAttributeName().equalsIgnoreCase(CommonConstants.CUSTOMER_PROJECT_NAME)).findFirst().isPresent()) {
					vrfBean.setVrfProjectName(scServiceAttributes.stream().filter(vrfAttribute -> vrfAttribute.getAttributeName().equalsIgnoreCase(CommonConstants.CUSTOMER_PROJECT_NAME)).findFirst().get().getAttributeValue());
				}
				if (scServiceAttributes.stream().filter(vrfAttribute -> vrfAttribute.getAttributeName().equalsIgnoreCase(CommonConstants.MASTER_VRF_SERVICE_ID)).findFirst().isPresent()) {
					vrfBean.setMastervrfServiceid(scServiceAttributes.stream().filter(vrfAttribute -> vrfAttribute.getAttributeName().equalsIgnoreCase(CommonConstants.MASTER_VRF_SERVICE_ID)).findFirst().get().getAttributeValue());
				}
				if (scServiceAttributes.stream().filter(vrfAttribute -> vrfAttribute.getAttributeName().equalsIgnoreCase(CommonConstants.SLAVE_VRF_SERVICE_ID)).findFirst().isPresent()) {
					vrfBean.setSlaveVrfServiceId(scServiceAttributes.stream().filter(vrfAttribute -> vrfAttribute.getAttributeName().equalsIgnoreCase(CommonConstants.SLAVE_VRF_SERVICE_ID)).findFirst().get().getAttributeValue());
				}
				if (scServiceAttributes.stream().filter(vrfAttribute -> vrfAttribute.getAttributeName().equalsIgnoreCase(CommonConstants.FLEXICOS)).findFirst().isPresent()) {
					serviceDetailBean.setFlexiCos(scServiceAttributes.stream().filter(vrfAttribute -> vrfAttribute.getAttributeName().equalsIgnoreCase(CommonConstants.FLEXICOS)).findFirst().get().getAttributeValue());
				}
				if (scServiceAttributes.stream().filter(vrfAttribute -> vrfAttribute.getAttributeName().equalsIgnoreCase(CommonConstants.MULTI_VRF_SOLUTION)).findFirst().isPresent()) {
					serviceDetailBean.setMultiVrfSolution(scServiceAttributes.stream().filter(vrfAttribute -> vrfAttribute.getAttributeName().equalsIgnoreCase(CommonConstants.MULTI_VRF_SOLUTION)).findFirst().get().getAttributeValue());
				}
				if (scServiceAttributes.stream().filter(vrfAttribute -> vrfAttribute.getAttributeName().equalsIgnoreCase(CommonConstants.NO_OF_VRFS)).findFirst().isPresent()) {
					serviceDetailBean.setNumberOfVrf(scServiceAttributes.stream().filter(vrfAttribute -> vrfAttribute.getAttributeName().equalsIgnoreCase(CommonConstants.NO_OF_VRFS)).findFirst().get().getAttributeValue());
				}
				if (scServiceAttributes.stream().filter(vrfAttribute -> vrfAttribute.getAttributeName().equalsIgnoreCase(CommonConstants.VRF_BASED_BILLING)).findFirst().isPresent()) {
					serviceDetailBean.setVrfBasedBilling(scServiceAttributes.stream().filter(vrfAttribute -> vrfAttribute.getAttributeName().equalsIgnoreCase(CommonConstants.VRF_BASED_BILLING)).findFirst().get().getAttributeValue());
				}
				if (scServiceAttributes.stream().filter(vrfAttribute -> vrfAttribute.getAttributeName().equalsIgnoreCase(CommonConstants.MASTER_VRF_FLAG)).findFirst().isPresent()) {
					serviceDetailBean.setMasterVrfFlag(scServiceAttributes.stream().filter(vrfAttribute -> vrfAttribute.getAttributeName().equalsIgnoreCase(CommonConstants.MASTER_VRF_FLAG)).findFirst().get().getAttributeValue());
				}
			}
			serviceDetailBean.getVrfBeans().add(vrfBean);
		}
	}
	
	public VpnMetatData saveVpnMetaData(ServiceDetail serviceDetail) throws TclCommonException {
		logger.info("Service Activation - VpnMetaData - started");
		try {
			VpnMetatData vpnMetatData = new VpnMetatData();
			vpnMetatData.setServiceDetail(serviceDetail);
			vpnMetatData.setE2eIntegrated(false);
			// note: if ILL then site role will be spoke and topogy will be Hub & Spoke by
			// default
			if (serviceDetail.getServiceType().equalsIgnoreCase("ILL")
					|| serviceDetail.getServiceType().equalsIgnoreCase("IAS")) {
				vpnMetatData.setL2oSiteRole("SPOKE");
				vpnMetatData.setL2OTopology("Hub & Spoke");
			} /*else if (serviceDetail.getServiceType().equalsIgnoreCase("GVPN")) {
				logger.info("CRAMER GVPN VPN METADATA");
				List<ScServiceAttribute>  serviceAttributes=	scServiceAttributeRepository.findByScServiceDetail_idAndAttributeNameIn(serviceDetail.getScServiceDetailId(),Arrays.asList("Site Type","VPN Topology"));
				Map<String, String> attributeMap =commonFulfillmentUtils.getServiceAttributesAttributes(serviceAttributes);
				String siteType=attributeMap.getOrDefault("Site Type", null);
				String vpnTopology=attributeMap.getOrDefault("VPN Topology", null);
				vpnMetatData.setL2oSiteRole(siteType!=null?siteType.toUpperCase():siteType);
				vpnMetatData.setL2OTopology(vpnTopology!=null?vpnTopology.toUpperCase():vpnTopology);
				vpnMetatData.setModifiedBy(modifiedBy);
			}*/
			vpnMetatData.setIsUa("NA");
			vpnMetatData = vpnMetaDataRepository.saveAndFlush(vpnMetatData);
			logger.info("Service Activation - VpnMetaData - ended");
			return vpnMetatData;
		} catch (Exception e) {
			logger.error("Exception in saveVpnSolution => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}
	
	
	public VpnSolution saveVpnSolution(ServiceDetail serviceDetail, String vrfName) throws TclCommonException {
		logger.info("Service Activation - saveVpnSolution - started");
		try {
			VpnSolution vpnSolution = new VpnSolution();
			vpnSolution.setVpnLegId(serviceDetail.getServiceId());
			if (serviceDetail.getServiceType().equalsIgnoreCase("IAS")
					|| serviceDetail.getServiceType().equalsIgnoreCase("ILL")) {
				vpnSolution.setVpnTopology("HUBnSPOKE");
				vpnSolution.setLegRole("SPOKE");

			}
			/*else if (serviceDetail.getServiceType().equalsIgnoreCase("GVPN")) {

				List<ScServiceAttribute> serviceAttributes = scServiceAttributeRepository
						.findByScServiceDetail_idAndAttributeNameIn(serviceDetail.getScServiceDetailId(),
								Arrays.asList("Site Type", "VPN Topology"));

				Map<String, String> attributeMap =commonFulfillmentUtils.getServiceAttributesAttributes(serviceAttributes);

				String siteType = attributeMap.getOrDefault("Site Type", null);
				String vpnTopology = attributeMap.getOrDefault("VPN Topology", null);
				if (vpnTopology != null) {
					vpnSolution.setVpnTopology(vpnTopology.toUpperCase().contains("HUB") ? "HUBnSPOKE" : vpnTopology.toUpperCase());
				}
				if (siteType != null) {
					vpnSolution.setLegRole(siteType.toUpperCase());
				}
			}*/
			vpnSolution.setVpnType("CUSTOMER");
			vpnSolution.setVpnSolutionName(serviceDetail.getServiceId());
			vpnSolution.setServiceDetail(serviceDetail);
			vpnSolution.setVpnName(vrfName != null ? vrfName.replaceAll(" ", "_") : "");
			vpnSolution.setInterfaceName(null);
			vpnSolution.setSiteId(serviceDetail.getServiceId());

			vpnSolution.setStartDate(startDate);
			vpnSolution.setLastModifiedDate(lastModifiedDate);
			vpnSolution.setModifiedBy(modifiedBy);
			vpnSolutionRepository.saveAndFlush(vpnSolution);
			logger.info("Service Activation - saveVpnSolution - completed");
			return vpnSolution;
		} catch (Exception e) {
			logger.error("Exception in saveVpnSolution => {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}
}
	
