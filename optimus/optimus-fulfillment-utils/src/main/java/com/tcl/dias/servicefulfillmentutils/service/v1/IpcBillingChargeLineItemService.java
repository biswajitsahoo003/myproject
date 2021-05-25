package com.tcl.dias.servicefulfillmentutils.service.v1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.servicefulfillment.entity.entities.IpcChargeLineitem;
import com.tcl.dias.servicefulfillment.entity.entities.ScContractInfo;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScProductDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ScProductDetailAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceCommericalComponent;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.GenevaIpcOrderEntryRepository;
import com.tcl.dias.servicefulfillment.entity.repository.IpcChargeLineitemRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScContractInfoRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScProductDetailAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScProductDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceCommercialRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceCommericalComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ServicehandoverAuditRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.beans.DeletedLineItemDetailsBean;
import com.tcl.dias.servicefulfillmentutils.beans.LineItemDetailsBean;
import com.tcl.dias.servicefulfillmentutils.constants.IpcConstants;

/**
 * @author yogesh
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited used for the document related
 *            service
 */
@Service
@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
public class IpcBillingChargeLineItemService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IpcBillingChargeLineItemService.class);

	@Autowired
	ScServiceCommercialRepository scServiceCommercialRepository;

	@Autowired
	GenevaIpcOrderEntryRepository genevaIpcOrderEntryRepository;

	@Autowired
	ServicehandoverAuditRepository servicehandoverAuditRepository;

	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	@Autowired
	ScProductDetailRepository scProductDetailRepository;

	@Autowired
	ScProductDetailAttributeRepository scProductDetailAttributeRepository;

	@Autowired
	TaskRepository taskRepository;

	@Autowired
	ScOrderRepository scOrderRepository;

	@Autowired
	ScOrderAttributeRepository scOrderAttributeRepository;

	@Autowired
	ScContractInfoRepository scContractInfoRepository;

	@Autowired
	ScServiceCommericalComponentRepository scServiceCommericalComponentRepository;
	
	@Autowired
	IpcChargeLineitemRepository ipcChargeLineitemRepository;
	
	Map<String, LineItemDetailsBean> prevOdrCloudCodeVMLineItemBeanM;
	
	List<String> upgradeDowngradeCloudCodeL;
	
	Map<String, LineItemDetailsBean> deletedLineItemDetailsBeanM;
	
	private String cpu;
	private String ram;
	private String storage;
	private String hypervisor;
	private String osType;
	private String osVersion;
	private String accessOption;
	private String accessOptionBw;
	private String accessOptionMinComm;
	private String managed;
	private String storageType;
	private String storageValue;
	private String iopsValue;
	private String storageDesc;
	private String billingMethod;
	private static final List<String> COMPUTES = Arrays.asList("L", "C", "G", "X", "M", "B", "H");
	private static final List<String> VARIANTS = Arrays.asList("Nickel", "Bronze", "Silver", "Cobalt", "Gold",
			"Platinum", "Titanium");
	public static final List<String> SOLUTIONS_NAMES = new ArrayList<>();

	static {
		COMPUTES.forEach(compute -> VARIANTS.forEach(variant -> SOLUTIONS_NAMES.add(compute + "." + variant + IpcConstants.VM_CHARGES)));
		SOLUTIONS_NAMES.add(IpcConstants.CARBON_VM + IpcConstants.VM_CHARGES);
		SOLUTIONS_NAMES.add(IpcConstants.DATA_TRANSFER_COMM);
		SOLUTIONS_NAMES.add(IpcConstants.FIXED_BW);
		SOLUTIONS_NAMES.add(IpcConstants.VDOM_SMALL);
		SOLUTIONS_NAMES.add(IpcConstants.ADDITIONAL_IP_CHARGES);
		SOLUTIONS_NAMES.add(IpcConstants.VPN_CLIENT_SITE);
		SOLUTIONS_NAMES.add(IpcConstants.VPN_SITE_SITE);
		SOLUTIONS_NAMES.add(IpcConstants.BACKUP_FE_VOL);
		SOLUTIONS_NAMES.add(IpcConstants.DATABASE_LICENSE_CHARGES);
		SOLUTIONS_NAMES.add(IpcConstants.DR_LICENSE_CHARGES);
		SOLUTIONS_NAMES.add(IpcConstants.HYBRID_CONNECTION_CHARGES);
		SOLUTIONS_NAMES.add(IpcConstants.EARLY_TERMINATION_CHARGES);
	}

	public List<LineItemDetailsBean> loadLineItems(Boolean isTriggeredFromUI, Integer taskId) {
		Map< String, LineItemDetailsBean> lineItemDetailsBeanM = new HashMap<>();
		Optional<Task> task = taskRepository.findById(Integer.valueOf(taskId));
		ScOrder scOrder = scOrderRepository.findDistinctByOpOrderCode(task.get().getOrderCode());
		Optional<ScServiceDetail> scServiceDetail = scServiceDetailRepository.findById(task.get().getServiceId());
		List<ScProductDetail> scProductDetails = scProductDetailRepository
				.findByScServiceDetailId(scServiceDetail.get().getId());
		Optional<ScProductDetail> productDetail = scProductDetailRepository
				.findByScServiceDetailIdAndSolutionName(scServiceDetail.get().getId(), "IPC addon");
		List<IpcChargeLineitem> ipcChargeLineitemL = ipcChargeLineitemRepository.findByServiceIdAndServiceType(task.get().getServiceId().toString(), task.get().getServiceType());
		String tempAccountNumber = !ipcChargeLineitemL.isEmpty() ? ipcChargeLineitemL.get(0).getAccountNumber() : "OPTACC_".concat(scServiceDetail.get().getScOrderUuid());
		managed = ", Unmanaged VM";
		if(productDetail.isPresent()) {
			List<ScProductDetailAttribute> managedDetailAttributes = scProductDetailAttributeRepository
					.findByScProductDetail_idAndCategory(productDetail.get().getId(), "managed");
			managedDetailAttributes.forEach(detailAttribute -> {
				if ("managed".equals(detailAttribute.getAttributeName()))
					if ("true".equalsIgnoreCase(detailAttribute.getAttributeValue()))
						managed = ", Managed VM";
			});
		}
		billingMethod = "Advance";
		ScContractInfo scContractInfo = scContractInfoRepository.findFirstByScOrder_id(scOrder.getId());
		if (scContractInfo != null) {
			billingMethod = scContractInfo.getBillingMethod();
		}

		List<LineItemDetailsBean> lineItemDetailsBeans = new ArrayList<>();
		scProductDetails.forEach(scProductDetail -> {
			if(scProductDetail.getSolutionName() != null && !("IPC Discount").equals(scProductDetail.getSolutionName())) {
				LineItemDetailsBean lineItemDetailsBean = new LineItemDetailsBean();

				ScProductDetailAttribute migParentServiceCode = scProductDetailAttributeRepository
						.findFirstByScProductDetail_idAndAttributeNameOrderByIdDesc(scProductDetail.getId(), IpcConstants.OLD_SERVICE_ID);
				lineItemDetailsBean.setMigParentServiceCode(Objects.nonNull(migParentServiceCode) ? migParentServiceCode.getAttributeValue() :null);
				
				if (IpcConstants.ADDON.equalsIgnoreCase(scProductDetail.getType())) {

					ScProductDetailAttribute additionalIp = scProductDetailAttributeRepository
							.findByScProductDetail_idAndAttributeNameAndCategory(scProductDetail.getId(),
									IpcConstants.IP_QUANTITY, IpcConstants.ADDITIONAL_IP);
					
					if (Objects.nonNull(additionalIp)) {
						ScServiceCommericalComponent additionalIpComp = scServiceCommericalComponentRepository
								.findByScProductDetailIdAndItem(scProductDetail.getId(), IpcConstants.ADDITIONAL_IP);
						Double additionalIpCompMrc = 0.0;
						Double additionalIpCompNrc = 0.0;
						Double additionalIpCompArc = 0.0;
						if (Objects.nonNull(additionalIpComp)) {
							additionalIpCompMrc = additionalIpComp.getMrc();
							additionalIpCompNrc = additionalIpComp.getNrc();
							additionalIpCompArc = additionalIpComp.getArc();
						}
						LineItemDetailsBean lineItemDetailsBeanAddon = new LineItemDetailsBean();
						lineItemDetailsBeanAddon.setLineitem(IpcConstants.ADDITIONAL_IP_CHARGES);
						lineItemDetailsBeanAddon.setComponent("IPC Addon");
						lineItemDetailsBeanAddon.setDescription(IpcConstants.ADDITIONAL_IP_DESC);
						lineItemDetailsBeanAddon.setUnitPriceMrc(String.valueOf(round(additionalIpCompMrc / Integer.parseInt(additionalIp.getAttributeValue()))));
						lineItemDetailsBeanAddon.setUnitPriceNrc(String.valueOf(round(additionalIpCompNrc / Integer.parseInt(additionalIp.getAttributeValue()))));
						lineItemDetailsBeanAddon.setUnitPriceArc(String.valueOf(round(additionalIpCompArc / Integer.parseInt(additionalIp.getAttributeValue()))));
						lineItemDetailsBeanAddon.setUnitPpuRate(IpcConstants.STRING_ZERO);
						lineItemDetailsBeanAddon.setMrc(String.valueOf(additionalIpCompMrc));
						lineItemDetailsBeanAddon.setNrc(String.valueOf(additionalIpCompNrc));
						lineItemDetailsBeanAddon.setArc(String.valueOf(additionalIpCompArc));
						lineItemDetailsBeanAddon.setPpuRate(IpcConstants.STRING_ZERO);
						lineItemDetailsBeanAddon.setQuantity(additionalIp.getAttributeValue());
						lineItemDetailsBeanAddon.setIsProrated("Yes");
						lineItemDetailsBeanAddon.setBillingMethod(billingMethod);
						lineItemDetailsBeanAddon.setUnitOfMeasurement("NA");
						lineItemDetailsBeanAddon.setServiceType(IpcConstants.IPC);
						lineItemDetailsBeanAddon.setHsnCode(IpcConstants.HSN_CODE);
						lineItemDetailsBeanAddon.setAccountNumber(tempAccountNumber);
						lineItemDetailsBeanAddon.setCloudCode(scProductDetail.getCloudCode());
						lineItemDetailsBeanAddon.setParentCloudCode(scProductDetail.getParentCloudCode());
						lineItemDetailsBeanAddon.setMigParentServiceCode(Objects.nonNull(migParentServiceCode) ? migParentServiceCode.getAttributeValue() :null);
						lineItemDetailsBeans.add(lineItemDetailsBeanAddon);
					}

					ScProductDetailAttribute backup = scProductDetailAttributeRepository
							.findByScProductDetail_idAndAttributeNameAndCategory(scProductDetail.getId(),
									IpcConstants.FRONT_VOL_SIZE, IpcConstants.BACKUP);
					ScProductDetailAttribute backupLoc = scProductDetailAttributeRepository
							.findByScProductDetail_idAndAttributeNameAndCategory(scProductDetail.getId(),
									IpcConstants.BACKUP_FE_LOC, IpcConstants.BACKUP);
					if (Objects.nonNull(backup) && Objects.nonNull(backupLoc)) {
						ScServiceCommericalComponent backupComp = scServiceCommericalComponentRepository
								.findByScProductDetailIdAndItem(scProductDetail.getId(), IpcConstants.BACKUP);
						LineItemDetailsBean lineItemDetailsBeanAddon = new LineItemDetailsBean();
						lineItemDetailsBeanAddon.setLineitem(IpcConstants.BACKUP_FE_VOL);
						lineItemDetailsBeanAddon.setComponent("IPC Addon");
						lineItemDetailsBeanAddon.setDescription("Front Volume Size: "
								+ backup.getAttributeValue() + " GB" + ", Target Data Storage: " + backupLoc.getAttributeValue());
						lineItemDetailsBeanAddon.setUnitPriceMrc(String.valueOf(backupComp.getMrc()));
						lineItemDetailsBeanAddon.setUnitPriceNrc(String.valueOf(backupComp.getNrc()));
						lineItemDetailsBeanAddon.setUnitPriceArc(String.valueOf(backupComp.getArc()));
						lineItemDetailsBeanAddon.setUnitPpuRate(IpcConstants.STRING_ZERO);
						lineItemDetailsBeanAddon.setMrc(String.valueOf(backupComp.getMrc()));
						lineItemDetailsBeanAddon.setNrc(String.valueOf(backupComp.getNrc()));
						lineItemDetailsBeanAddon.setArc(String.valueOf(backupComp.getArc()));
						lineItemDetailsBeanAddon.setPpuRate(IpcConstants.STRING_ZERO);
						lineItemDetailsBeanAddon.setQuantity(IpcConstants.ONE);
						lineItemDetailsBeanAddon.setIsProrated("Yes");
						lineItemDetailsBeanAddon.setBillingMethod(billingMethod);
						lineItemDetailsBeanAddon.setUnitOfMeasurement("GB");
						lineItemDetailsBeanAddon.setServiceType(IpcConstants.IPC);
						lineItemDetailsBeanAddon.setHsnCode(IpcConstants.HSN_CODE);
						lineItemDetailsBeanAddon.setAccountNumber(tempAccountNumber);
						lineItemDetailsBeanAddon.setCloudCode(scProductDetail.getCloudCode());
						lineItemDetailsBeanAddon.setParentCloudCode(scProductDetail.getParentCloudCode());
						lineItemDetailsBeanAddon.setMigParentServiceCode(Objects.nonNull(migParentServiceCode) ? migParentServiceCode.getAttributeValue() :null);
						lineItemDetailsBeans.add(lineItemDetailsBeanAddon);
					}

					ScProductDetailAttribute vdom = scProductDetailAttributeRepository
							.findByScProductDetail_idAndAttributeNameAndCategory(scProductDetail.getId(),
									IpcConstants.QUANTITY, IpcConstants.VDOM);

					if (Objects.nonNull(vdom)) {

						ScServiceCommericalComponent vdomComp = scServiceCommericalComponentRepository
								.findByScProductDetailIdAndItem(scProductDetail.getId(), IpcConstants.VDOM);
						LineItemDetailsBean lineItemDetailsBeanAddon = new LineItemDetailsBean();
						lineItemDetailsBeanAddon.setLineitem(IpcConstants.VDOM_SMALL);
						lineItemDetailsBeanAddon.setComponent("IPC Addon");
						lineItemDetailsBeanAddon.setDescription("Type: Shared");
						lineItemDetailsBeanAddon.setUnitPriceMrc(String.valueOf(vdomComp.getMrc()));
						lineItemDetailsBeanAddon.setUnitPriceNrc(String.valueOf(vdomComp.getNrc()));
						lineItemDetailsBeanAddon.setUnitPriceArc(String.valueOf(vdomComp.getArc()));
						lineItemDetailsBeanAddon.setUnitPpuRate(IpcConstants.STRING_ZERO);
						lineItemDetailsBeanAddon.setMrc(String.valueOf(vdomComp.getMrc()));
						lineItemDetailsBeanAddon.setNrc(String.valueOf(vdomComp.getNrc()));
						lineItemDetailsBeanAddon.setArc(String.valueOf(vdomComp.getArc()));
						lineItemDetailsBeanAddon.setPpuRate(IpcConstants.STRING_ZERO);
						lineItemDetailsBeanAddon.setQuantity(IpcConstants.ONE);
						lineItemDetailsBeanAddon.setIsProrated("Yes");
						lineItemDetailsBeanAddon.setBillingMethod(billingMethod);
						lineItemDetailsBeanAddon.setUnitOfMeasurement("NA");
						lineItemDetailsBeanAddon.setServiceType(IpcConstants.IPC);
						lineItemDetailsBeanAddon.setHsnCode(IpcConstants.HSN_CODE);
						lineItemDetailsBeanAddon.setAccountNumber(tempAccountNumber);
						lineItemDetailsBeanAddon.setCloudCode(scProductDetail.getCloudCode());
						lineItemDetailsBeanAddon.setParentCloudCode(scProductDetail.getParentCloudCode());
						lineItemDetailsBeanAddon.setMigParentServiceCode(Objects.nonNull(migParentServiceCode) ? migParentServiceCode.getAttributeValue() :null);
						lineItemDetailsBeans.add(lineItemDetailsBeanAddon);
					}

					ScProductDetailAttribute vpnClient = scProductDetailAttributeRepository
							.findByScProductDetail_idAndAttributeNameAndCategory(scProductDetail.getId(),
									IpcConstants.CLIENT_SITE, IpcConstants.VPN_CONNECTION);
					ScProductDetailAttribute vpnSite = scProductDetailAttributeRepository
							.findByScProductDetail_idAndAttributeNameAndCategory(scProductDetail.getId(),
									IpcConstants.SITE_SITE, IpcConstants.VPN_CONNECTION);
					
					//VPN Connection Charges Changes(temp.)
					ScServiceCommericalComponent vpnComp = scServiceCommericalComponentRepository
							.findByScProductDetailIdAndItem(scProductDetail.getId(), IpcConstants.VPN_CONNECTION);
					Double vpnClientMrc = 0.0;
					Double vpnClientNrc = 0.0;
					Double vpnClientArc = 0.0;
					Double vpnSiteMrc = 0.0;
					Double vpnSiteNrc = 0.0;
					Double vpnSiteArc = 0.0;
					if(Objects.nonNull(vpnClient) && Objects.nonNull(vpnSite) && Objects.nonNull(vpnComp) && vpnComp.getMrc().compareTo(0.0) != 0) {
						if(Integer.parseInt(vpnSite.getAttributeValue()) != 0 && Integer.parseInt(vpnClient.getAttributeValue()) != 0) {
							vpnClientMrc = vpnComp.getMrc()/2;
							vpnClientNrc = vpnComp.getNrc()/2;
							vpnClientArc = vpnComp.getArc()/2;
							vpnSiteMrc = vpnComp.getMrc()/2;
							vpnSiteNrc = vpnComp.getNrc()/2;
							vpnSiteArc = vpnComp.getArc()/2;
						} else if (Integer.parseInt(vpnClient.getAttributeValue()) != 0) {
							vpnClientMrc = vpnComp.getMrc();
							vpnClientNrc = vpnComp.getNrc();
							vpnClientArc = vpnComp.getArc();
							vpnSiteMrc = 0.0;
							vpnSiteNrc = 0.0;
							vpnSiteArc = 0.0;
						} else if (Integer.parseInt(vpnSite.getAttributeValue()) != 0) {
							vpnClientMrc = 0.0;
							vpnClientNrc = 0.0;
							vpnClientArc = 0.0;
							vpnSiteMrc = vpnComp.getMrc();
							vpnSiteNrc = vpnComp.getNrc();
							vpnSiteArc = vpnComp.getArc();
						}
					}
					
					if (Objects.nonNull(vpnClient) && Objects.nonNull(vpnSite)) {
						LineItemDetailsBean lineItemDetailsBeanAddon = new LineItemDetailsBean();
						lineItemDetailsBeanAddon.setLineitem(IpcConstants.VPN_CLIENT_SITE);
						lineItemDetailsBeanAddon.setComponent("IPC Addon");
						lineItemDetailsBeanAddon.setDescription(IpcConstants.IPC_DESC_CLIENT_TO_SITE);
						lineItemDetailsBeanAddon.setUnitPriceMrc(String.valueOf(round(vpnClientMrc / Integer.parseInt(vpnClient.getAttributeValue()))));
						lineItemDetailsBeanAddon.setUnitPriceNrc(String.valueOf(round(vpnClientNrc / Integer.parseInt(vpnClient.getAttributeValue()))));
						lineItemDetailsBeanAddon.setUnitPriceArc(String.valueOf(round(vpnClientArc / Integer.parseInt(vpnClient.getAttributeValue()))));
						lineItemDetailsBeanAddon.setUnitPpuRate(IpcConstants.STRING_ZERO);
						lineItemDetailsBeanAddon.setMrc(String.valueOf(vpnClientMrc));
						lineItemDetailsBeanAddon.setNrc(String.valueOf(vpnClientNrc));
						lineItemDetailsBeanAddon.setArc(String.valueOf(vpnClientArc));
						lineItemDetailsBeanAddon.setPpuRate(IpcConstants.STRING_ZERO);
						lineItemDetailsBeanAddon.setQuantity(vpnClient.getAttributeValue());
						lineItemDetailsBeanAddon.setIsProrated("Yes");
						lineItemDetailsBeanAddon.setBillingMethod(billingMethod);
						lineItemDetailsBeanAddon.setUnitOfMeasurement("NA");
						lineItemDetailsBeanAddon.setServiceType(IpcConstants.IPC);
						lineItemDetailsBeanAddon.setHsnCode(IpcConstants.HSN_CODE);
						lineItemDetailsBeanAddon.setAccountNumber(tempAccountNumber);
						lineItemDetailsBeanAddon.setCloudCode(scProductDetail.getCloudCode());
						lineItemDetailsBeanAddon.setParentCloudCode(scProductDetail.getParentCloudCode());
						lineItemDetailsBeanAddon.setMigParentServiceCode(Objects.nonNull(migParentServiceCode) ? migParentServiceCode.getAttributeValue() :null);
						lineItemDetailsBeans.add(lineItemDetailsBeanAddon);

					}
					if (Objects.nonNull(vpnClient) && Objects.nonNull(vpnSite)) {
						LineItemDetailsBean lineItemDetailsBeanAddon = new LineItemDetailsBean();
						lineItemDetailsBeanAddon.setLineitem(IpcConstants.VPN_SITE_SITE);
						lineItemDetailsBeanAddon.setComponent("IPC Addon");
						lineItemDetailsBeanAddon.setDescription(IpcConstants.IPC_DESC_SITE_TO_SITE);
						lineItemDetailsBeanAddon.setUnitPriceMrc(String.valueOf(round(vpnSiteMrc / Integer.parseInt(vpnSite.getAttributeValue()))));
						lineItemDetailsBeanAddon.setUnitPriceNrc(String.valueOf(round(vpnSiteNrc / Integer.parseInt(vpnSite.getAttributeValue()))));
						lineItemDetailsBeanAddon.setUnitPriceArc(String.valueOf(round(vpnSiteArc / Integer.parseInt(vpnSite.getAttributeValue()))));
						lineItemDetailsBeanAddon.setUnitPpuRate(IpcConstants.STRING_ZERO);
						lineItemDetailsBeanAddon.setMrc(String.valueOf(vpnSiteMrc));
						lineItemDetailsBeanAddon.setNrc(String.valueOf(vpnSiteNrc));
						lineItemDetailsBeanAddon.setArc(String.valueOf(vpnSiteArc));
						lineItemDetailsBeanAddon.setPpuRate(IpcConstants.STRING_ZERO);
						lineItemDetailsBeanAddon.setQuantity(vpnSite.getAttributeValue());
						lineItemDetailsBeanAddon.setIsProrated("Yes");
						lineItemDetailsBeanAddon.setBillingMethod(billingMethod);
						lineItemDetailsBeanAddon.setUnitOfMeasurement("NA");
						lineItemDetailsBeanAddon.setServiceType(IpcConstants.IPC);
						lineItemDetailsBeanAddon.setHsnCode(IpcConstants.HSN_CODE);
						lineItemDetailsBeanAddon.setAccountNumber(tempAccountNumber);
						lineItemDetailsBeanAddon.setCloudCode(scProductDetail.getCloudCode());
						lineItemDetailsBeanAddon.setParentCloudCode(scProductDetail.getParentCloudCode());
						lineItemDetailsBeanAddon.setMigParentServiceCode(Objects.nonNull(migParentServiceCode) ? migParentServiceCode.getAttributeValue() :null);
						lineItemDetailsBeans.add(lineItemDetailsBeanAddon);

					}
					
					Map<String, Map<String, String>> dataBaseMap = new HashMap<>();
					scProductDetailAttributeRepository.findByScProductDetail_id(scProductDetail.getId()).stream()
							.filter(scProdAttribute -> (scProdAttribute.getCategory().startsWith(IpcConstants.MYSQL)
									|| scProdAttribute.getCategory().startsWith(IpcConstants.MS_SQL_SERVER)
									|| scProdAttribute.getCategory().startsWith(IpcConstants.POSTGRESQL)))
							.forEach(scProdAttribute -> {
								if (dataBaseMap.containsKey(scProdAttribute.getCategory())) {
									dataBaseMap.get(scProdAttribute.getCategory()).put(
											scProdAttribute.getAttributeName(), scProdAttribute.getAttributeValue());
								} else {
									Map<String, String> dbAttributeValueM = new HashMap<>();
									dbAttributeValueM.put(scProdAttribute.getAttributeName(),
											scProdAttribute.getAttributeValue());
									dataBaseMap.put(scProdAttribute.getCategory(), dbAttributeValueM);
								}
							});
					
					if (Objects.nonNull(dataBaseMap)) {
						dataBaseMap.entrySet().forEach(dataBaseLiscence -> {
							ScServiceCommericalComponent dataBaseComp = scServiceCommericalComponentRepository
									.findByScProductDetailIdAndItem(scProductDetail.getId(), dataBaseLiscence.getKey());
							LineItemDetailsBean lineItemDetailsBeanAddon = new LineItemDetailsBean();
							lineItemDetailsBeanAddon.setLineitem(IpcConstants.DATABASE_LICENSE_CHARGES);
							lineItemDetailsBeanAddon.setComponent("IPC Addon");
							lineItemDetailsBeanAddon.setQuantity(dataBaseLiscence.getValue().get(IpcConstants.QUANTITY));
							lineItemDetailsBeanAddon.setDescription(("Type: ").concat(dataBaseLiscence.getKey().split("\\(")[0].trim())
									.concat(IpcConstants.SPECIAL_CHARACTER_COMMA_WITH_SPACE)
									.concat(IpcConstants.ATTRIBUTE_NAME_DB_VERSION).concat(IpcConstants.SPECIAL_CHARACTER_COLON_WITH_SPACE).concat(dataBaseLiscence.getValue().get(IpcConstants.ATTRIBUTE_NAME_DB_VERSION))
									.concat(IpcConstants.SPECIAL_CHARACTER_COMMA_WITH_SPACE)
									.concat((dataBaseLiscence.getValue().containsKey(IpcConstants.ATTRIBUTE_NAME_MANAGED) && dataBaseLiscence.getValue().get(IpcConstants.ATTRIBUTE_NAME_MANAGED).equalsIgnoreCase(IpcConstants.NO_LOWER_CASE)) ? IpcConstants.UN_MANAGED_DB : IpcConstants.MANAGED_DB)
									.concat(", license as per core licensing policy"));
							lineItemDetailsBeanAddon.setUnitPriceMrc(String.valueOf(round(dataBaseComp.getMrc() / Integer.parseInt(lineItemDetailsBeanAddon.getQuantity()))));
							lineItemDetailsBeanAddon.setUnitPriceNrc(String.valueOf(round(dataBaseComp.getNrc() / Integer.parseInt(lineItemDetailsBeanAddon.getQuantity()))));
							lineItemDetailsBeanAddon.setUnitPriceArc(String.valueOf(round(dataBaseComp.getArc() / Integer.parseInt(lineItemDetailsBeanAddon.getQuantity()))));
							lineItemDetailsBeanAddon.setUnitPpuRate(IpcConstants.STRING_ZERO);
							lineItemDetailsBeanAddon.setMrc(String.valueOf(dataBaseComp.getMrc()));
							lineItemDetailsBeanAddon.setNrc(String.valueOf(dataBaseComp.getNrc()));
							lineItemDetailsBeanAddon.setArc(String.valueOf(dataBaseComp.getArc()));
							lineItemDetailsBeanAddon.setPpuRate(IpcConstants.STRING_ZERO);
							lineItemDetailsBeanAddon.setIsProrated("Yes");
							lineItemDetailsBeanAddon.setBillingMethod(billingMethod);
							lineItemDetailsBeanAddon.setUnitOfMeasurement("NA");
							lineItemDetailsBeanAddon.setServiceType(IpcConstants.IPC);
							lineItemDetailsBeanAddon.setHsnCode(IpcConstants.HSN_CODE);
							lineItemDetailsBeanAddon.setAccountNumber(tempAccountNumber);
							lineItemDetailsBeanAddon.setCloudCode(scProductDetail.getCloudCode());
							lineItemDetailsBeanAddon.setParentCloudCode(scProductDetail.getParentCloudCode());
							lineItemDetailsBeanAddon.setMigParentServiceCode(Objects.nonNull(migParentServiceCode) ? migParentServiceCode.getAttributeValue() :null);
							lineItemDetailsBeans.add(lineItemDetailsBeanAddon);
						});
					}
					
					Map<String, Map<String, String>> drMap = new HashMap<>();
					scProductDetailAttributeRepository.findByScProductDetail_id(scProductDetail.getId()).stream()
							.filter(scProdAttribute -> (scProdAttribute.getCategory().startsWith(IpcConstants.DR_ZERTO)
									|| scProdAttribute.getCategory().startsWith(IpcConstants.DR_DOUBLE_TAKE)))
							.forEach(scProdAttribute -> {
								if (drMap.containsKey(scProdAttribute.getCategory())) {
									drMap.get(scProdAttribute.getCategory()).put(
											scProdAttribute.getAttributeName(), scProdAttribute.getAttributeValue());
								} else {
									Map<String, String> drAttributeValueM = new HashMap<>();
									drAttributeValueM.put(scProdAttribute.getAttributeName(),
											scProdAttribute.getAttributeValue());
									drMap.put(scProdAttribute.getCategory(), drAttributeValueM);
								}
							});
					
					if (Objects.nonNull(drMap)) {
						drMap.entrySet().forEach(drLicense -> {
							ScServiceCommericalComponent drComp = scServiceCommericalComponentRepository
									.findByScProductDetailIdAndItem(scProductDetail.getId(), drLicense.getKey());
							LineItemDetailsBean lineItemDetailsBeanAddon = new LineItemDetailsBean();
							lineItemDetailsBeanAddon.setLineitem(IpcConstants.DR_LICENSE_CHARGES);
							lineItemDetailsBeanAddon.setComponent("IPC Addon");
							lineItemDetailsBeanAddon.setQuantity(drLicense.getValue().get(IpcConstants.QUANTITY));
							lineItemDetailsBeanAddon.setDescription(("Type: ").concat(drLicense.getKey().split("\\(")[0].trim())
									.concat(IpcConstants.SPECIAL_CHARACTER_COMMA_WITH_SPACE)
									.concat(IpcConstants.ATTRIBUTE_NAME_VARIANT).concat(IpcConstants.SPECIAL_CHARACTER_COLON_WITH_SPACE)
									.concat(drLicense.getValue().get(IpcConstants.ATTRIBUTE_NAME_VARIANT))
									.concat(IpcConstants.SPECIAL_CHARACTER_COMMA_WITH_SPACE).concat(IpcConstants.DR_LICENSE_DESCRIPTION_FOR_PROTECTED_VM));
							lineItemDetailsBeanAddon.setUnitPriceMrc(String.valueOf(round(drComp.getMrc() / Integer.parseInt(lineItemDetailsBeanAddon.getQuantity()))));
							lineItemDetailsBeanAddon.setUnitPriceNrc(String.valueOf(round(drComp.getNrc() / Integer.parseInt(lineItemDetailsBeanAddon.getQuantity()))));
							lineItemDetailsBeanAddon.setUnitPriceArc(String.valueOf(round(drComp.getArc() / Integer.parseInt(lineItemDetailsBeanAddon.getQuantity()))));
							lineItemDetailsBeanAddon.setUnitPpuRate(IpcConstants.STRING_ZERO);
							lineItemDetailsBeanAddon.setMrc(String.valueOf(drComp.getMrc()));
							lineItemDetailsBeanAddon.setNrc(String.valueOf(drComp.getNrc()));
							lineItemDetailsBeanAddon.setArc(String.valueOf(drComp.getArc()));
							lineItemDetailsBeanAddon.setPpuRate(IpcConstants.STRING_ZERO);
							lineItemDetailsBeanAddon.setIsProrated("Yes");
							lineItemDetailsBeanAddon.setBillingMethod(billingMethod);
							lineItemDetailsBeanAddon.setUnitOfMeasurement("NA");
							lineItemDetailsBeanAddon.setServiceType(IpcConstants.IPC);
							lineItemDetailsBeanAddon.setHsnCode(IpcConstants.HSN_CODE);
							lineItemDetailsBeanAddon.setAccountNumber(tempAccountNumber);
							lineItemDetailsBeanAddon.setCloudCode(scProductDetail.getCloudCode());
							lineItemDetailsBeanAddon.setParentCloudCode(scProductDetail.getParentCloudCode());
							lineItemDetailsBeanAddon.setMigParentServiceCode(Objects.nonNull(migParentServiceCode) ? migParentServiceCode.getAttributeValue() :null);
							lineItemDetailsBeans.add(lineItemDetailsBeanAddon);
						});
					}
					
					//Changes for Hybrid COnnection LineItem
					Map<String, Map<String, String>> hybridConnectionMap = new HashMap<>();
					scProductDetailAttributeRepository.findByScProductDetail_id(scProductDetail.getId()).stream()
							.filter(scProdAttribute -> (scProdAttribute.getCategory().startsWith(IpcConstants.HYBRID_CONNECTION)))
							.forEach(scProdAttribute -> {
								if (hybridConnectionMap.containsKey(scProdAttribute.getCategory())) {
									hybridConnectionMap.get(scProdAttribute.getCategory()).put(
											scProdAttribute.getAttributeName(), scProdAttribute.getAttributeValue());
								} else {
									Map<String, String> hybridConnAttributeValueM = new HashMap<>();
									hybridConnAttributeValueM.put(scProdAttribute.getAttributeName(),
											scProdAttribute.getAttributeValue());
									hybridConnectionMap.put(scProdAttribute.getCategory(), hybridConnAttributeValueM);
								}
							});
					
					if (Objects.nonNull(hybridConnectionMap)) {
						hybridConnectionMap.entrySet().forEach(hybridConn -> {
							ScServiceCommericalComponent hybridConnComp = scServiceCommericalComponentRepository
									.findByScProductDetailIdAndItem(scProductDetail.getId(), hybridConn.getKey());
							LineItemDetailsBean lineItemDetailsBeanAddon = new LineItemDetailsBean();
							lineItemDetailsBeanAddon.setLineitem(IpcConstants.HYBRID_CONNECTION_CHARGES);
							lineItemDetailsBeanAddon.setComponent("IPC Addon");
							lineItemDetailsBeanAddon.setQuantity(IpcConstants.ONE);
							lineItemDetailsBeanAddon.setDescription((IpcConstants.CONNECTIVE_TYPE).concat(IpcConstants.SPECIAL_CHARACTER_COLON_WITH_SPACE).concat(hybridConn.getValue().get(IpcConstants.CONNECTIVITY_TYPE))
									.concat(IpcConstants.SPECIAL_CHARACTER_COMMA_WITH_SPACE)
									.concat(IpcConstants.CABLE_TYPE).concat(IpcConstants.SPECIAL_CHARACTER_COLON_WITH_SPACE).concat(hybridConn.getValue().get(IpcConstants.CABLE_TYPE))
									.concat(IpcConstants.SPECIAL_CHARACTER_COMMA_WITH_SPACE)
									.concat(IpcConstants.L2_THROUGHPUT).concat(IpcConstants.SPECIAL_CHARACTER_COLON_WITH_SPACE).concat(hybridConn.getValue().get(IpcConstants.L2_THROUGHPUT))
									.concat(IpcConstants.SINGLE_SPACE)
									.concat(IpcConstants.WITH_SHARED_SWITCH_PORTS).concat(IpcConstants.SPECIAL_CHARACTER_COLON_WITH_SPACE).concat(hybridConn.getValue().get(IpcConstants.SHARED_SWITCH_PORT))
									.concat(IpcConstants.SINGLE_SPACE)
									.concat(hybridConn.getValue().containsKey(IpcConstants.REDUNDANCY) && hybridConn.getValue().get(IpcConstants.REDUNDANCY).equalsIgnoreCase(CommonConstants.YES) ? IpcConstants.WITH_REDUNDANCY : IpcConstants.WITHOUT_REDUNDANCY));
							lineItemDetailsBeanAddon.setUnitPriceMrc(hybridConnComp != null ? String.valueOf(round(hybridConnComp.getMrc())) : IpcConstants.STRING_ZERO);
							lineItemDetailsBeanAddon.setUnitPriceNrc(hybridConnComp != null ? String.valueOf(round(hybridConnComp.getNrc())) : IpcConstants.STRING_ZERO);
							lineItemDetailsBeanAddon.setUnitPriceArc(hybridConnComp != null ? String.valueOf(round(hybridConnComp.getArc())) : IpcConstants.STRING_ZERO);
							lineItemDetailsBeanAddon.setUnitPpuRate(IpcConstants.STRING_ZERO);
							lineItemDetailsBeanAddon.setMrc(hybridConnComp != null ? String.valueOf(round(hybridConnComp.getMrc())) : IpcConstants.STRING_ZERO);
							lineItemDetailsBeanAddon.setNrc(hybridConnComp != null ? String.valueOf(round(hybridConnComp.getNrc())) : IpcConstants.STRING_ZERO);
							lineItemDetailsBeanAddon.setArc(hybridConnComp != null ? String.valueOf(round(hybridConnComp.getArc())) : IpcConstants.STRING_ZERO);
							lineItemDetailsBeanAddon.setPpuRate(IpcConstants.STRING_ZERO);
							lineItemDetailsBeanAddon.setIsProrated("Yes");
							lineItemDetailsBeanAddon.setBillingMethod(billingMethod);
							lineItemDetailsBeanAddon.setUnitOfMeasurement("NA");
							lineItemDetailsBeanAddon.setServiceType(IpcConstants.IPC);
							lineItemDetailsBeanAddon.setHsnCode(IpcConstants.HSN_CODE);
							lineItemDetailsBeanAddon.setAccountNumber(tempAccountNumber);
							lineItemDetailsBeanAddon.setCloudCode(scProductDetail.getCloudCode());
							lineItemDetailsBeanAddon.setParentCloudCode(scProductDetail.getParentCloudCode());
							lineItemDetailsBeanAddon.setMigParentServiceCode(Objects.nonNull(migParentServiceCode) ? migParentServiceCode.getAttributeValue() :null);
							lineItemDetailsBeans.add(lineItemDetailsBeanAddon);
						});
					}
				} else if (IpcConstants.ACCESS.equalsIgnoreCase(scProductDetail.getType())) {
					lineItemDetailsBean.setComponent("Access");
					List<ScProductDetailAttribute> accessDetailAttributes = scProductDetailAttributeRepository
							.findByScProductDetail_idAndCategory(scProductDetail.getId(), "Access Type");
					accessDetailAttributes.forEach(detailAttribute -> {
						if ("accessOption".equals(detailAttribute.getAttributeName()))
							accessOption = detailAttribute.getAttributeValue();
						if ("minimumCommitment".equals(detailAttribute.getAttributeName()))
							accessOptionMinComm = detailAttribute.getAttributeValue();
						if ("portBandwidth".equals(detailAttribute.getAttributeName()))
							accessOptionBw = detailAttribute.getAttributeValue();
					});
					
					String accessType = "Data Transfer".equals(accessOption) ? "Minimum Commitment: " + accessOptionMinComm + " GB"
							: "Port Bandwidth: " + accessOptionBw;
					String productName = "Data Transfer".equals(accessOption) ? IpcConstants.DATA_TRANSFER_COMM
							: IpcConstants.FIXED_BW;
					lineItemDetailsBean.setLineitem(productName);
					lineItemDetailsBean.setDescription(accessType + ", Access Option: " + accessOption
							+ ", 1 Client to Site VPN is bundled, 2 Public IPs is bundled.");
					lineItemDetailsBean.setUnitPriceMrc(scProductDetail.getMrc().toString());
					lineItemDetailsBean.setUnitPriceNrc(scProductDetail.getNrc().toString());
					lineItemDetailsBean.setUnitPriceArc(scProductDetail.getArc().toString());
					lineItemDetailsBean.setUnitPpuRate(IpcConstants.STRING_ZERO);
					lineItemDetailsBean.setArc(scProductDetail.getArc().toString());
					lineItemDetailsBean.setMrc(scProductDetail.getMrc().toString());
					lineItemDetailsBean.setNrc(scProductDetail.getNrc().toString());
					lineItemDetailsBean.setPpuRate(IpcConstants.STRING_ZERO);
					lineItemDetailsBean.setQuantity(IpcConstants.ONE);
					lineItemDetailsBean.setIsProrated("Yes");
					lineItemDetailsBean.setBillingMethod(billingMethod);
					lineItemDetailsBean.setServiceType(IpcConstants.IPC);
					lineItemDetailsBean.setHsnCode(IpcConstants.HSN_CODE);
					lineItemDetailsBean.setAccountNumber(tempAccountNumber);
					if( lineItemDetailsBean.getLineitem().equals(IpcConstants.DATA_TRANSFER_COMM)) {
						lineItemDetailsBean.setAdditionalParam(accessOptionMinComm);
						lineItemDetailsBean.setUnitOfMeasurement("GB");
					} else {
						lineItemDetailsBean.setUnitOfMeasurement("NA");
					}
					lineItemDetailsBean.setCloudCode(scProductDetail.getCloudCode());
					lineItemDetailsBean.setParentCloudCode(scProductDetail.getParentCloudCode());
					lineItemDetailsBeans.add(lineItemDetailsBean);
				} else if (IpcConstants.EARLY_TERMINATION_CHARGES.equals(scProductDetail.getSolutionName())) {
					List<ScProductDetailAttribute> etcAttributes = scProductDetailAttributeRepository
							.findByScProductDetail_idAndCategory(scProductDetail.getId(), IpcConstants.ETC);
					Map<String, String> etcAttributeNameVsValue = CommonFulfillmentUtils.parseProductDetailAttributeIntoMap(etcAttributes);
					String description = "";
					if(!etcAttributeNameVsValue.isEmpty()) {
						if (etcAttributeNameVsValue.containsKey(CommonConstants.ATTRIBUTE_REASON)
								&& etcAttributeNameVsValue.containsKey(CommonConstants.ATTRIBUTE_FROM_DATE)
								&& etcAttributeNameVsValue.containsKey(CommonConstants.ATTRIBUTE_TO_DATE)) {
							description = CommonConstants.ETC_REASON.concat(IpcConstants.SPECIAL_CHARACTER_COLON_WITH_SPACE).concat(etcAttributeNameVsValue.get(CommonConstants.ATTRIBUTE_REASON))
									.concat(IpcConstants.SPECIAL_CHARACTER_COMMA_WITH_SPACE).concat(CommonConstants.ETC_PERIOD).concat(IpcConstants.SPECIAL_CHARACTER_COLON_WITH_SPACE).concat(etcAttributeNameVsValue.get(CommonConstants.ATTRIBUTE_FROM_DATE))
									.concat(IpcConstants.SINGLE_SPACE).concat(CommonConstants.TO_CAMEL_CASE).concat(IpcConstants.SINGLE_SPACE).concat(etcAttributeNameVsValue.get(CommonConstants.ATTRIBUTE_TO_DATE));
						}
					}
					LineItemDetailsBean earlyTerminationChargesLineItem = new LineItemDetailsBean();
					earlyTerminationChargesLineItem.setPricingModel(IpcConstants.PRICING_MODEL_RESERVED);
					earlyTerminationChargesLineItem.setMigParentServiceCode(Objects.nonNull(migParentServiceCode) ? migParentServiceCode.getAttributeValue() :null);
					earlyTerminationChargesLineItem.setComponent("Virtual Machine");
					earlyTerminationChargesLineItem.setLineitem(scProductDetail.getSolutionName());
					earlyTerminationChargesLineItem.setUnitPriceMrc(scProductDetail.getMrc().toString());
					earlyTerminationChargesLineItem.setUnitPriceNrc(scProductDetail.getNrc().toString());
					earlyTerminationChargesLineItem.setUnitPriceArc(scProductDetail.getArc().toString());
					earlyTerminationChargesLineItem.setUnitPpuRate(IpcConstants.STRING_ZERO);
					earlyTerminationChargesLineItem.setDescription(description);
					earlyTerminationChargesLineItem.setArc(scProductDetail.getArc().toString());
					earlyTerminationChargesLineItem.setMrc(scProductDetail.getMrc().toString());
					earlyTerminationChargesLineItem.setNrc(scProductDetail.getNrc().toString());
					earlyTerminationChargesLineItem.setPpuRate(IpcConstants.STRING_ZERO);
					earlyTerminationChargesLineItem.setQuantity(IpcConstants.ONE);
					earlyTerminationChargesLineItem.setIsProrated("Yes");
					earlyTerminationChargesLineItem.setBillingMethod(billingMethod);
					earlyTerminationChargesLineItem.setUnitOfMeasurement("NA");
					earlyTerminationChargesLineItem.setServiceType(IpcConstants.IPC);
					earlyTerminationChargesLineItem.setHsnCode(IpcConstants.HSN_CODE);
					earlyTerminationChargesLineItem.setAccountNumber(tempAccountNumber);
					earlyTerminationChargesLineItem.setCloudCode(scProductDetail.getCloudCode());
					lineItemDetailsBeans.add(earlyTerminationChargesLineItem);
				} else {
					List<ScProductDetailAttribute> flavourDetailAttributes = scProductDetailAttributeRepository
						.findByScProductDetail_idAndCategory(scProductDetail.getId(), "Flavor");
					flavourDetailAttributes.forEach(detailAttribute -> {
						if ("vCPU".equals(detailAttribute.getAttributeName()))
							cpu = " vCPU: " + detailAttribute.getAttributeValue();
						if ("vRAM".equals(detailAttribute.getAttributeName()))
							ram = ", vRAM: " + detailAttribute.getAttributeValue() + " GB";
						if ("Storage".equals(detailAttribute.getAttributeName()))
							storage = ", Storage: " + detailAttribute.getAttributeValue() + " GB";
						if ("Hypervisor".equals(detailAttribute.getAttributeName()))
							hypervisor = ", Hypervisor: " + detailAttribute.getAttributeValue();
					});
					List<ScProductDetailAttribute> detailAttributes = scProductDetailAttributeRepository
						.findByScProductDetail_idAndCategory(scProductDetail.getId(), "OS");
					detailAttributes.forEach(detailAttribute -> {
						if ("Type".equals(detailAttribute.getAttributeName()))
							osType = ", OS Type: " + detailAttribute.getAttributeValue();
						if ("Version".equals(detailAttribute.getAttributeName()))
							osVersion = ", OS Version: " + detailAttribute.getAttributeValue();
					});
					List<ScProductDetailAttribute> ipcCommonAttributes = scProductDetailAttributeRepository
						.findByScProductDetail_idAndCategory(scProductDetail.getId(), IpcConstants.IPC_COMMON);
					ipcCommonAttributes.forEach(ipcComAttr -> {
						if ("pricingModel".equals(ipcComAttr.getAttributeName()))
							lineItemDetailsBean.setPricingModel(ipcComAttr.getAttributeValue());
					});
				
					List<ScProductDetailAttribute> additionalStorageDetailAttributes = scProductDetailAttributeRepository
						.findByScProductDetail_idAndCategory(scProductDetail.getId(), IpcConstants.ADDITIONAL_STORAGE);
					storageType = null;
					storageValue = null;
					iopsValue = null;
					storageDesc = null;
					additionalStorageDetailAttributes.forEach(detailAttribute -> {
						if (IpcConstants.STORAGE_TYPE.equals(detailAttribute.getAttributeName()))
							storageType = "Additional Storage Type: " + detailAttribute.getAttributeValue();
						if (IpcConstants.STORAGE_VALUE.equals(detailAttribute.getAttributeName()))
							storageValue = ", Additional Storage Value: " + detailAttribute.getAttributeValue()+" GB";
						if (IpcConstants.IOPS_VALUE.equals(detailAttribute.getAttributeName()) && detailAttribute.getAttributeValue() != null && Integer.valueOf(detailAttribute.getAttributeValue()) > 0) {
							iopsValue = ", IOPS Value: " + detailAttribute.getAttributeValue();
						}
					});
					if( storageType != null) {
						storageDesc = storageType + storageValue + (iopsValue!=null ? iopsValue : "");
					}
					String lineItemName = (scProductDetail.getSolutionName().contains(IpcConstants.CARBON_VM) ?  IpcConstants.CARBON_VM : scProductDetail.getSolutionName()).concat(IpcConstants.VM_CHARGES);
					if(lineItemDetailsBean.getPricingModel()!=null 
						&& !lineItemDetailsBean.getPricingModel().equals(IpcConstants.PRICING_MODEL_RESERVED)) {
						if(storageDesc!=null) {
							LineItemDetailsBean addStoragePpuVM = new LineItemDetailsBean();
							addStoragePpuVM.setPricingModel(IpcConstants.PRICING_MODEL_RESERVED);
							addStoragePpuVM.setMigParentServiceCode(Objects.nonNull(migParentServiceCode) ? migParentServiceCode.getAttributeValue() :null);
							addStoragePpuVM.setComponent("Virtual Machine");
							addStoragePpuVM.setLineitem(lineItemName);
							addStoragePpuVM.setUnitPriceMrc(scProductDetail.getMrc().toString());
							addStoragePpuVM.setUnitPriceNrc(scProductDetail.getNrc().toString());
							addStoragePpuVM.setUnitPriceArc(scProductDetail.getArc().toString());
							addStoragePpuVM.setUnitPpuRate(IpcConstants.STRING_ZERO);
							addStoragePpuVM.setDescription(storageDesc);
							addStoragePpuVM.setProductDescription(cpu + ram + storage + hypervisor + managed + osType + osVersion + IpcConstants.SPECIAL_CHARACTER_COMMA_WITH_SPACE + lineItemDetailsBean.getPricingModel() + IpcConstants.SPECIAL_CHARACTER_COMMA_WITH_SPACE + storageDesc);
							addStoragePpuVM.setArc(scProductDetail.getArc().toString());
							addStoragePpuVM.setMrc(scProductDetail.getMrc().toString());
							addStoragePpuVM.setNrc(scProductDetail.getNrc().toString());
							addStoragePpuVM.setPpuRate(IpcConstants.STRING_ZERO);
							addStoragePpuVM.setQuantity(IpcConstants.ONE);
							addStoragePpuVM.setIsProrated("Yes");
							addStoragePpuVM.setBillingMethod(billingMethod);
							addStoragePpuVM.setUnitOfMeasurement("NA");
							addStoragePpuVM.setServiceType(IpcConstants.IPC);
							addStoragePpuVM.setHsnCode(IpcConstants.HSN_CODE);
							addStoragePpuVM.setAccountNumber(tempAccountNumber);
							addStoragePpuVM.setCloudCode(scProductDetail.getCloudCode());
							addStoragePpuVM.setParentCloudCode(scProductDetail.getParentCloudCode());
							lineItemDetailsBeans.add(addStoragePpuVM);
						}
						LineItemDetailsBean ppuVM = new LineItemDetailsBean();
						ppuVM.setPricingModel(lineItemDetailsBean.getPricingModel());
						ppuVM.setMigParentServiceCode(Objects.nonNull(migParentServiceCode) ? migParentServiceCode.getAttributeValue() :null);
						ppuVM.setComponent("Virtual Machine");
						ppuVM.setLineitem(lineItemName);
						ppuVM.setDescription(cpu + ram + storage + hypervisor + managed + osType + osVersion + (storageDesc!=null? (IpcConstants.SEMI_COLON_WITH_SPACE + storageDesc) : ""));
						ppuVM.setProductDescription(getDescription( ppuVM.getLineitem(), ppuVM.getPricingModel()));
						ppuVM.setUnitPriceMrc(IpcConstants.STRING_ZERO);
						ppuVM.setUnitPriceNrc(IpcConstants.STRING_ZERO);
						ppuVM.setUnitPriceArc(IpcConstants.STRING_ZERO);
						ppuVM.setUnitPpuRate(scProductDetail.getPpuRate().toString());
						ppuVM.setArc(IpcConstants.STRING_ZERO);
						ppuVM.setMrc(IpcConstants.STRING_ZERO);
						ppuVM.setNrc(IpcConstants.STRING_ZERO);
						ppuVM.setPpuRate(scProductDetail.getPpuRate().toString());
						ppuVM.setQuantity(IpcConstants.ONE);
						ppuVM.setIsProrated("Yes");
						ppuVM.setBillingMethod(billingMethod);
						ppuVM.setUnitOfMeasurement("NA");
						ppuVM.setServiceType(IpcConstants.IPC);
						ppuVM.setHsnCode(IpcConstants.HSN_CODE);
						ppuVM.setAccountNumber(tempAccountNumber);
						ppuVM.setCloudCode(scProductDetail.getCloudCode());
						ppuVM.setParentCloudCode(scProductDetail.getParentCloudCode());
						lineItemDetailsBeans.add(ppuVM);
					} else {
						lineItemDetailsBean.setPricingModel(lineItemDetailsBean.getPricingModel() != null ? lineItemDetailsBean.getPricingModel() : IpcConstants.PRICING_MODEL_RESERVED);
						lineItemDetailsBean.setComponent("Virtual Machine");
						lineItemDetailsBean.setLineitem(lineItemName);
						lineItemDetailsBean.setDescription(cpu + ram + storage + hypervisor + managed + osType + osVersion + (storageDesc != null ? (", " + storageDesc) : ""));
						lineItemDetailsBean.setUnitPriceMrc(scProductDetail.getMrc().toString());
						lineItemDetailsBean.setUnitPriceNrc(scProductDetail.getNrc().toString());
						lineItemDetailsBean.setUnitPriceArc(scProductDetail.getArc().toString());
						lineItemDetailsBean.setUnitPpuRate(IpcConstants.STRING_ZERO);
						lineItemDetailsBean.setArc(scProductDetail.getArc().toString());
						lineItemDetailsBean.setMrc(scProductDetail.getMrc().toString());
						lineItemDetailsBean.setNrc(scProductDetail.getNrc().toString());
						lineItemDetailsBean.setPpuRate(IpcConstants.STRING_ZERO);
						lineItemDetailsBean.setQuantity(IpcConstants.ONE);
						lineItemDetailsBean.setIsProrated("Yes");
						lineItemDetailsBean.setBillingMethod(billingMethod);
						lineItemDetailsBean.setUnitOfMeasurement("NA");
						lineItemDetailsBean.setServiceType(IpcConstants.IPC);
						lineItemDetailsBean.setHsnCode(IpcConstants.HSN_CODE);
						lineItemDetailsBean.setAccountNumber(tempAccountNumber);
						lineItemDetailsBean.setCloudCode(scProductDetail.getCloudCode());
						lineItemDetailsBean.setParentCloudCode(scProductDetail.getParentCloudCode());
						lineItemDetailsBeans.add(lineItemDetailsBean);
					}
				}
			}
		});
		if(isTriggeredFromUI) {
			if(task.get().getOrderType().equals(IpcConstants.MACD)) {
				prevOdrCloudCodeVMLineItemBeanM = new HashMap<>();
				deletedLineItemDetailsBeanM = new HashMap<>();
				upgradeDowngradeCloudCodeL = new ArrayList<>();
				formatLineItemsForMacdOrder(lineItemDetailsBeanM, lineItemDetailsBeans, fetchLineItemsTillPreviousOrder(task.get(), tempAccountNumber), task, scOrder);
				List<LineItemDetailsBean> resultList = new ArrayList<LineItemDetailsBean>(lineItemDetailsBeanM.values());
				resultList.addAll(new ArrayList<LineItemDetailsBean>(deletedLineItemDetailsBeanM.values().stream()
						.filter(lineItem -> ((lineItem.getActionType() != null
								&& lineItem.getActionType().equals(IpcConstants.ACTION_TYPE_DELETE))
								|| IpcConstants.DELETE_VM.equals(scOrder.getOrderCategory())))
						.map(ipcLineItem -> {
							ipcLineItem.setActionType(IpcConstants.ACTION_TYPE_DELETE);
							return ipcLineItem;
						}).collect(Collectors.toList())));
				return getSortedLineItemDetails(resultList);
			} else {
				formatLineItems(lineItemDetailsBeanM, lineItemDetailsBeans, task, IpcConstants.ACTION_TYPE_NEW);
				return getSortedLineItemDetails(new ArrayList<LineItemDetailsBean>(lineItemDetailsBeanM.values()));
			}
		}
		return lineItemDetailsBeans;
	}

	private List<LineItemDetailsBean> formatLineItems(
			Map<String, LineItemDetailsBean> lineItemDetailsBeanM, List<LineItemDetailsBean> lineItemDetailsBeans,
			Optional<Task> task, String actionType) {
		LOGGER.info(" Inside formatLineItemsForNewOrder- List Size : {}", lineItemDetailsBeans.size());
		lineItemDetailsBeans.forEach(lineItemBean -> {
			String key = "";
			if (!Arrays.asList(IpcConstants.DATA_TRANSFER_COMM, IpcConstants.VDOM_SMALL, IpcConstants.BACKUP_FE_VOL, IpcConstants.FIXED_BW, IpcConstants.ADDITIONAL_IP_CHARGES, IpcConstants.DATABASE_LICENSE_CHARGES, IpcConstants.DR_LICENSE_CHARGES, IpcConstants.VPN_CLIENT_SITE, IpcConstants.VPN_SITE_SITE, IpcConstants.HYBRID_CONNECTION_CHARGES).contains(lineItemBean.getLineitem())) {
				key = frameKey(Arrays.asList(lineItemBean.getPricingModel()!=null ? lineItemBean.getPricingModel() : "", lineItemBean.getLineitem(),
						lineItemBean.getProductDescription() != null ? lineItemBean.getProductDescription() : "", lineItemBean.getDescription() != null ? lineItemBean.getDescription() : "", lineItemBean.getUnitPriceMrc(), lineItemBean.getUnitPriceNrc(), lineItemBean.getUnitPriceArc(), lineItemBean.getPpuRate()));
			} else if (IpcConstants.DATABASE_LICENSE_CHARGES.equals(lineItemBean.getLineitem())
					|| IpcConstants.DR_LICENSE_CHARGES.equals(lineItemBean.getLineitem())
					|| IpcConstants.HYBRID_CONNECTION_CHARGES.equals(lineItemBean.getLineitem())) {
				key = frameKey(Arrays.asList(lineItemBean.getLineitem(), lineItemBean.getDescription() != null ? lineItemBean.getDescription() : "", lineItemBean.getUnitPriceMrc(), lineItemBean.getUnitPriceNrc(), lineItemBean.getUnitPriceArc()));
			} else {
				key = frameKey(Arrays.asList(lineItemBean.getLineitem()));
			}
			LOGGER.info(" Inside formatLineItemsForNewOrder- key Framed : {}", key);
			lineItemBean.setActionType(actionType);
			if (lineItemDetailsBeanM.containsKey(key)) {
				lineItemBean = lineItemDetailsBeanM.get(key);
				lineItemBean.setQuantity(
						String.valueOf(Integer.parseInt(lineItemBean.getQuantity()) + IpcConstants.NUMBER_ONE));
			}
			lineItemBean.setDescription(lineItemBean.getDescription() != null ? lineItemBean.getDescription().split(IpcConstants.SEMI_COLON_WITH_SPACE)[0] : "");
			lineItemBean.setMrc(String.valueOf(round(Double.parseDouble(lineItemBean.getUnitPriceMrc())
					* Integer.parseInt(lineItemBean.getQuantity()))));
			lineItemBean.setNrc(String.valueOf(round(Double.parseDouble(lineItemBean.getUnitPriceNrc())
					* Integer.parseInt(lineItemBean.getQuantity()))));
			lineItemBean.setArc(String.valueOf(round(Double.parseDouble(lineItemBean.getUnitPriceArc())
					* Integer.parseInt(lineItemBean.getQuantity()))));
			lineItemBean.setPpuRate(String.valueOf(round(
					Double.parseDouble(lineItemBean.getUnitPpuRate()) * Integer.parseInt(lineItemBean.getQuantity()))));
			lineItemDetailsBeanM.put(key, lineItemBean);
		});
		LOGGER.info(" Inside formatLineItemsForNewOrder- ipcChargeLineItemBeanM Final Size : {}", lineItemDetailsBeanM.size());
		for( Map.Entry<String, LineItemDetailsBean> map : lineItemDetailsBeanM.entrySet()) {
			LOGGER.info(" Inside formatLineItemsForNewOrder- key : {}", map.getKey());
			LOGGER.info(" Inside formatLineItemsForNewOrder- value : {}", map.getValue());
		}
		return new ArrayList<LineItemDetailsBean>(lineItemDetailsBeanM.values());
	}
	
	private List<LineItemDetailsBean> fetchLineItemsTillPreviousOrder(Task task, String tempAccNum){
		List<LineItemDetailsBean> lineItemDetailsBeans = new ArrayList<>();
		Integer prevOdrScServiceDetailId = scServiceDetailRepository.findPrevServiceDetailIdsByServiceCodeAndServiceId(task.getServiceCode(), task.getServiceId());
		List<IpcChargeLineitem> ipcChargeLineitems = ipcChargeLineitemRepository.findByServiceId(String.valueOf(prevOdrScServiceDetailId));
		ipcChargeLineitems.stream().filter(ipcChargeLineitem -> !Arrays.asList(IpcConstants.DATA_TRANSFER_USAGE,IpcConstants.EARLY_TERMINATION_CHARGES).contains(ipcChargeLineitem.getChargeLineitem()) && Integer.parseInt(ipcChargeLineitem.getQuantity()) != 0).forEach(ipcChargeLineitem -> {
			LineItemDetailsBean lineItemDetailsBean = new LineItemDetailsBean();
			lineItemDetailsBean.setLineitem(ipcChargeLineitem.getChargeLineitem().equals(IpcConstants.VM_USAGE_CHARGES)?ipcChargeLineitem.getProductDescription()+IpcConstants.SINGLE_SPACE+IpcConstants.CHARGES : ipcChargeLineitem.getChargeLineitem());
			lineItemDetailsBean.setComponent(ipcChargeLineitem.getComponent());
			lineItemDetailsBean.setPricingModel(ipcChargeLineitem.getPricingModel());
			lineItemDetailsBean.setDescription(ipcChargeLineitem.getDescription());
			lineItemDetailsBean.setProductDescription(ipcChargeLineitem.getProductDescription());
			Integer quantity = Integer.parseInt(ipcChargeLineitem.getQuantity());
			if(Arrays.asList(IpcConstants.ADDITIONAL_IP_CHARGES, IpcConstants.VPN_CLIENT_SITE, IpcConstants.VPN_SITE_SITE, IpcConstants.DATABASE_LICENSE_CHARGES, IpcConstants.DR_LICENSE_CHARGES, IpcConstants.HYBRID_CONNECTION_CHARGES).contains(ipcChargeLineitem.getChargeLineitem())) {
				if(ipcChargeLineitem.getDescription() != null 
						&& (ipcChargeLineitem.getDescription().contains(IpcConstants.QUANTITY)
						|| ipcChargeLineitem.getDescription().contains(IpcConstants.SITE_TO_SITE)
						|| ipcChargeLineitem.getDescription().contains(IpcConstants.CLIENT_TO_SITE))) {
					quantity = Integer.parseInt(ipcChargeLineitem.getDescription().split(": ")[1]);
				}
				lineItemDetailsBean.setUnitPriceMrc(String.valueOf(round(Double.parseDouble(ipcChargeLineitem.getMrc()) / quantity)));
				lineItemDetailsBean.setUnitPriceNrc(IpcConstants.STRING_ZERO);
				lineItemDetailsBean.setUnitPriceArc(String.valueOf(round(Double.parseDouble(ipcChargeLineitem.getArc()) / quantity)));
				if (IpcConstants.ADDITIONAL_IP_CHARGES.equals(ipcChargeLineitem.getChargeLineitem())) {
					lineItemDetailsBean.setDescription(IpcConstants.ADDITIONAL_IP_DESC);
				} else if (IpcConstants.VPN_CLIENT_SITE.equals(ipcChargeLineitem.getChargeLineitem())) {
					lineItemDetailsBean.setDescription(IpcConstants.IPC_DESC_CLIENT_TO_SITE);
				}else if (IpcConstants.VPN_SITE_SITE.equals(ipcChargeLineitem.getChargeLineitem())) {
					lineItemDetailsBean.setDescription(IpcConstants.IPC_DESC_SITE_TO_SITE);
				}
			} else {
				lineItemDetailsBean.setUnitPriceMrc(ipcChargeLineitem.getMrc());
				lineItemDetailsBean.setUnitPriceNrc(IpcConstants.STRING_ZERO);
				lineItemDetailsBean.setUnitPriceArc(ipcChargeLineitem.getArc());
			}
			lineItemDetailsBean.setUnitPpuRate(ipcChargeLineitem.getPpuRate());
			lineItemDetailsBean.setMrc(ipcChargeLineitem.getMrc());
			lineItemDetailsBean.setNrc(IpcConstants.STRING_ZERO);
			lineItemDetailsBean.setArc(ipcChargeLineitem.getArc());
			lineItemDetailsBean.setPpuRate(ipcChargeLineitem.getPpuRate());
			lineItemDetailsBean.setQuantity(String.valueOf(quantity));
			lineItemDetailsBean.setIsProrated(ipcChargeLineitem.getIsProrated());
			lineItemDetailsBean.setBillingMethod(ipcChargeLineitem.getBillingMethod());
			lineItemDetailsBean.setUnitOfMeasurement(ipcChargeLineitem.getUnitOfMeasurement());
			lineItemDetailsBean.setServiceType(ipcChargeLineitem.getServiceType());
			lineItemDetailsBean.setHsnCode(ipcChargeLineitem.getHsnCode());
			lineItemDetailsBean.setAccountNumber(tempAccNum);
			lineItemDetailsBean.setCloudCode(ipcChargeLineitem.getCloudCode());
			lineItemDetailsBean.setParentCloudCode(ipcChargeLineitem.getParentCloudCode());
			lineItemDetailsBean.setMigParentServiceCode(ipcChargeLineitem.getMigParentServiceCode());
			lineItemDetailsBeans.add(lineItemDetailsBean);
			if( lineItemDetailsBean.getLineitem().contains(IpcConstants.VM)) {
				lineItemDetailsBean.setPricingModel(lineItemDetailsBean.getPricingModel() != null ? lineItemDetailsBean.getPricingModel() : IpcConstants.PRICING_MODEL_RESERVED);
				String key = (lineItemDetailsBean.getPricingModel() != null && lineItemDetailsBean.getPricingModel().equals(IpcConstants.PRICING_MODEL_RESERVED) && lineItemDetailsBean.getProductDescription() != null) 
						? lineItemDetailsBean.getCloudCode().concat(IpcConstants.UNDERSCORE).concat(IpcConstants.ADDITIONAL_STORAGE)
								: lineItemDetailsBean.getCloudCode();
				prevOdrCloudCodeVMLineItemBeanM.put(key, lineItemDetailsBean.cloneLineItemDetailsBean(lineItemDetailsBean));
			}
		});
		return lineItemDetailsBeans;
	}
	
	private void formatLineItemsForMacdOrder(Map<String, LineItemDetailsBean> lineItemDetailsBeanM,
			List<LineItemDetailsBean> currentOrderLineItemDetailsBeans,
			List<LineItemDetailsBean> prevOrderLineItemDetailsBeans, Optional<Task> task, ScOrder scOrder) {
		formatLineItems(lineItemDetailsBeanM, prevOrderLineItemDetailsBeans, task, IpcConstants.ACTION_TYPE_NO_CHANGE);
		
		currentOrderLineItemDetailsBeans.stream()
				.forEach(lineItemBean -> {
					String key = null;
					if(lineItemBean.getParentCloudCode() != null) {
						String cloudCodeValue = (lineItemBean.getPricingModel() != null && lineItemBean.getPricingModel().equals(IpcConstants.PRICING_MODEL_RESERVED) && lineItemBean.getProductDescription() != null) 
								? lineItemBean.getParentCloudCode().concat(IpcConstants.UNDERSCORE).concat(IpcConstants.ADDITIONAL_STORAGE)
										: lineItemBean.getParentCloudCode();
						upgradeDowngradeCloudCodeL.add(cloudCodeValue);
					}
					if(IpcConstants.DELETE_VM.equals(scOrder.getOrderCategory()) && !IpcConstants.EARLY_TERMINATION_CHARGES.equals(lineItemBean.getLineitem())) {
						return;
					}
					if (!Arrays.asList(IpcConstants.DATA_TRANSFER_COMM, IpcConstants.VDOM_SMALL, IpcConstants.BACKUP_FE_VOL, IpcConstants.FIXED_BW, IpcConstants.ADDITIONAL_IP_CHARGES, IpcConstants.DATABASE_LICENSE_CHARGES, IpcConstants.DR_LICENSE_CHARGES, IpcConstants.VPN_CLIENT_SITE, IpcConstants.VPN_SITE_SITE, IpcConstants.HYBRID_CONNECTION_CHARGES).contains(lineItemBean.getLineitem())) {
						key = frameKey(Arrays.asList(lineItemBean.getPricingModel()!=null ? lineItemBean.getPricingModel() : "", lineItemBean.getLineitem(),
								lineItemBean.getProductDescription() != null ? lineItemBean.getProductDescription() : "", lineItemBean.getDescription()!= null ? lineItemBean.getDescription() : "", lineItemBean.getUnitPriceMrc(), lineItemBean.getUnitPriceNrc(), lineItemBean.getUnitPriceArc(), lineItemBean.getPpuRate()));
					} else if (IpcConstants.DATABASE_LICENSE_CHARGES.equals(lineItemBean.getLineitem())
							|| IpcConstants.DR_LICENSE_CHARGES.equals(lineItemBean.getLineitem())
							|| IpcConstants.HYBRID_CONNECTION_CHARGES.equals(lineItemBean.getLineitem())) {
						key = frameKey(Arrays.asList(lineItemBean.getLineitem(), lineItemBean.getDescription() != null ? lineItemBean.getDescription() : "", lineItemBean.getUnitPriceMrc(), lineItemBean.getUnitPriceNrc(), lineItemBean.getUnitPriceArc()));
					} else {
						key = frameKey(Arrays.asList(lineItemBean.getLineitem()));
					}
					if (lineItemDetailsBeanM.containsKey(key)) {
						LineItemDetailsBean lineItemBeanFromMap = lineItemDetailsBeanM.get(key);
						if(lineItemBeanFromMap.getActionType().equals(IpcConstants.ACTION_TYPE_NO_CHANGE)) {
							lineItemBean.setActionType(IpcConstants.ACTION_TYPE_MODIFY);
						} else {
							lineItemBean.setActionType(lineItemBeanFromMap.getActionType());
						}
						if(!Arrays.asList(IpcConstants.DATA_TRANSFER_COMM, IpcConstants.VDOM_SMALL, IpcConstants.BACKUP_FE_VOL, IpcConstants.FIXED_BW).contains(lineItemBeanFromMap.getLineitem())) {
							lineItemBean.setQuantity(String.valueOf(Integer.parseInt(lineItemBean.getQuantity())+Integer.parseInt(lineItemBeanFromMap.getQuantity())));
							lineItemBean.setMrc(String.valueOf(addDoubleValueAndRoundOff(lineItemBean.getMrc(),lineItemBeanFromMap.getMrc())));
							lineItemBean.setNrc(String.valueOf(addDoubleValueAndRoundOff(lineItemBean.getNrc(),lineItemBeanFromMap.getNrc())));
							lineItemBean.setArc(String.valueOf(addDoubleValueAndRoundOff(lineItemBean.getArc(),lineItemBeanFromMap.getArc())));
							lineItemBean.setPpuRate(String.valueOf(addDoubleValueAndRoundOff(lineItemBean.getPpuRate(),lineItemBeanFromMap.getPpuRate())));
							lineItemBean.setUnitPriceMrc(String.valueOf(round(Double.valueOf(lineItemBean.getMrc()) / Integer.valueOf(lineItemBean.getQuantity()))));
							lineItemBean.setUnitPriceNrc(String.valueOf(round(Double.valueOf(lineItemBean.getNrc()) / Integer.valueOf(lineItemBean.getQuantity()))));
							lineItemBean.setUnitPriceArc(String.valueOf(round(Double.valueOf(lineItemBean.getArc()) / Integer.valueOf(lineItemBean.getQuantity()))));
							lineItemBean.setUnitPpuRate(String.valueOf(round(Double.valueOf(lineItemBean.getPpuRate()) / Integer.valueOf(lineItemBean.getQuantity()))));
						}
					} else {
						lineItemBean.setActionType(IpcConstants.ACTION_TYPE_ADD);
					}
					lineItemBean.setDescription(lineItemBean.getDescription() != null ? lineItemBean.getDescription().split(IpcConstants.SEMI_COLON_WITH_SPACE)[0] : "");
					lineItemDetailsBeanM.put(key, lineItemBean);
				});
		
		LOGGER.info("upgradeDowngradeCloudCodeL- size: {} and Values: ", upgradeDowngradeCloudCodeL.size());
		upgradeDowngradeCloudCodeL.forEach(res -> LOGGER.info(res));
		LOGGER.info("prevOdrCloudCodeVMLineItemBeanM- size: {} and Values: ", prevOdrCloudCodeVMLineItemBeanM.size());
		prevOdrCloudCodeVMLineItemBeanM.forEach((k,v) -> LOGGER.info("Key: {}, Value: {}", k, v.toString()));
		
		upgradeDowngradeCloudCodeL.forEach(cloudCode -> {
			if(prevOdrCloudCodeVMLineItemBeanM.containsKey(cloudCode)) {
				LineItemDetailsBean prevOrderLineItemDetailsBean = prevOdrCloudCodeVMLineItemBeanM.get(cloudCode);
				String key = frameKey(Arrays.asList(prevOrderLineItemDetailsBean.getPricingModel()!=null ? prevOrderLineItemDetailsBean.getPricingModel() : "", prevOrderLineItemDetailsBean.getLineitem(),
						prevOrderLineItemDetailsBean.getProductDescription() != null ? prevOrderLineItemDetailsBean.getProductDescription() : "", prevOrderLineItemDetailsBean.getDescription() != null ? prevOrderLineItemDetailsBean.getDescription() : "", prevOrderLineItemDetailsBean.getUnitPriceMrc(), prevOrderLineItemDetailsBean.getUnitPriceNrc(), prevOrderLineItemDetailsBean.getUnitPriceArc(), prevOrderLineItemDetailsBean.getPpuRate()));
				LOGGER.info("Key framed inside upgradeDowngrade stream: {}", key);
				if(lineItemDetailsBeanM.containsKey(key)) {
					LineItemDetailsBean mainMapLineItemDetailsBean = lineItemDetailsBeanM.get(key);
					if(Integer.valueOf(mainMapLineItemDetailsBean.getQuantity()) == 1) {
						prevOrderLineItemDetailsBean.setActionType(IpcConstants.ACTION_TYPE_DELETE);
						lineItemDetailsBeanM.remove(key);
					} else {
						Integer updatedQuantity = Integer.valueOf(mainMapLineItemDetailsBean.getQuantity()) -1;
						mainMapLineItemDetailsBean.setActionType(IpcConstants.ACTION_TYPE_MODIFY);
						mainMapLineItemDetailsBean.setQuantity(String.valueOf(updatedQuantity));
						mainMapLineItemDetailsBean.setMrc(String.valueOf(multiplyDoubleValueWithIntegerAndRoundOff(mainMapLineItemDetailsBean.getUnitPriceMrc(), mainMapLineItemDetailsBean.getQuantity())));
						mainMapLineItemDetailsBean.setNrc(String.valueOf(multiplyDoubleValueWithIntegerAndRoundOff(mainMapLineItemDetailsBean.getUnitPriceNrc(), mainMapLineItemDetailsBean.getQuantity())));
						mainMapLineItemDetailsBean.setArc(String.valueOf(multiplyDoubleValueWithIntegerAndRoundOff(mainMapLineItemDetailsBean.getUnitPriceArc(), mainMapLineItemDetailsBean.getQuantity())));
						mainMapLineItemDetailsBean.setPpuRate(String.valueOf(multiplyDoubleValueWithIntegerAndRoundOff(mainMapLineItemDetailsBean.getUnitPpuRate(), mainMapLineItemDetailsBean.getQuantity())));
						lineItemDetailsBeanM.put(key, mainMapLineItemDetailsBean);
					}
					if(deletedLineItemDetailsBeanM.containsKey(key)) {
						Integer updatedQuantity = Integer.valueOf(deletedLineItemDetailsBeanM.get(key).getQuantity()) + 1;
						prevOrderLineItemDetailsBean.setQuantity(String.valueOf(updatedQuantity));
					}
					prevOrderLineItemDetailsBean.setUnitPriceMrc(IpcConstants.STRING_ZERO);
					prevOrderLineItemDetailsBean.setUnitPriceNrc(IpcConstants.STRING_ZERO);
					prevOrderLineItemDetailsBean.setUnitPriceArc(IpcConstants.STRING_ZERO);
					prevOrderLineItemDetailsBean.setUnitPpuRate(IpcConstants.STRING_ZERO);
					prevOrderLineItemDetailsBean.setMrc(IpcConstants.STRING_ZERO);
					prevOrderLineItemDetailsBean.setNrc(IpcConstants.STRING_ZERO);
					prevOrderLineItemDetailsBean.setArc(IpcConstants.STRING_ZERO);
					prevOrderLineItemDetailsBean.setPpuRate(IpcConstants.STRING_ZERO);
					prevOrderLineItemDetailsBean.setDescription(prevOrderLineItemDetailsBean.getDescription() != null ? prevOrderLineItemDetailsBean.getDescription().split(IpcConstants.SEMI_COLON_WITH_SPACE)[0] : "");
					deletedLineItemDetailsBeanM.put(key, prevOrderLineItemDetailsBean);
				}
			}
		});
		
	}

	private String frameKey(List<String> keyL) {
		String result = "";
		for (String inpStr : keyL) {
			result = result.concat(inpStr).concat(IpcConstants.UNDERSCORE);
		}
		return result;
	}

	public static double round(double value) {
		return (double) Math.round(value * 100) / 100;
	}
	
	private List<LineItemDetailsBean> getSortedLineItemDetails(List<LineItemDetailsBean> lineItemDetailsBeanL) {
		Collections.sort(lineItemDetailsBeanL, (final LineItemDetailsBean o1, final LineItemDetailsBean o2) -> {
			Integer firstIndex = SOLUTIONS_NAMES.indexOf(o1.getLineitem());
			Integer secondIndex = SOLUTIONS_NAMES.indexOf(o2.getLineitem());
			return firstIndex.compareTo(secondIndex);
		});
		return lineItemDetailsBeanL;
	}
	
	private String getDescription(String lineItemName, String pricingModel) {
		if(lineItemName.contains(IpcConstants.VM) && !pricingModel.equals(IpcConstants.PRICING_MODEL_RESERVED)) {
			String[] lineItemArray = lineItemName.split(IpcConstants.SINGLE_SPACE);
			return lineItemArray[0]+IpcConstants.SINGLE_SPACE+lineItemArray[1];
		}
		return null;
	}
	
	public Double addDoubleValueAndRoundOff(String valueOne, String valueTwo) {
		return round(Double.parseDouble(valueOne) + Double.parseDouble(valueTwo));
	}
	
	public Double multiplyDoubleValueWithIntegerAndRoundOff(String doubleValue, String integerValue) {
		return round(Double.parseDouble(doubleValue) * Integer.parseInt(integerValue));
	}
	
	public List<DeletedLineItemDetailsBean> loadDeletedLineItems(Integer taskId) {
		Map<String, DeletedLineItemDetailsBean> lineItemDetailsBeanM = new HashMap<>();
		Optional<Task> task = taskRepository.findById(Integer.valueOf(taskId));
		if (task.isPresent() && IpcConstants.MACD.equals(task.get().getOrderType()) && IpcConstants.DELETE_VM.equals(task.get().getOrderCategory())) {
			Optional<ScServiceDetail> scServiceDetail = scServiceDetailRepository.findById(task.get().getServiceId());
			List<ScProductDetail> scProductDetails = scProductDetailRepository
					.findByScServiceDetailId(scServiceDetail.get().getId());
			scProductDetails.stream().filter(x -> x.getType().equals(IpcConstants.CLOUD) && !x.getSolutionName().equals(IpcConstants.EARLY_TERMINATION_CHARGES) && x.getParentCloudCode() != null).forEach(scProductDetail -> {
				ScProductDetailAttribute scProductDetailAttribute = scProductDetailAttributeRepository.findByScProductDetail_idAndAttributeNameAndCategory(scProductDetail.getId(), IpcConstants.HOST_NAME, IpcConstants.IPC_COMMON);
				if(scProductDetailAttribute != null) {
					lineItemDetailsBeanM.put( scProductDetail.getParentCloudCode(), new DeletedLineItemDetailsBean(scProductDetail.getSolutionName()+IpcConstants.SINGLE_SPACE+IpcConstants.VM, scProductDetailAttribute.getAttributeValue()));
				}
			});
		}
		return new ArrayList<DeletedLineItemDetailsBean>(lineItemDetailsBeanM.values());
	}
	
	@Transactional
	public List<DeletedLineItemDetailsBean> formatAndFrameFinalIpcLineItemsForGSMCTask(Integer taskId) {
		Map<String, DeletedLineItemDetailsBean> deletedLineItemDetailsBeanM = new LinkedHashMap<>();
		Task task = taskRepository.findById(Integer.valueOf(taskId)).get();
		List<LineItemDetailsBean> lineItems = loadLineItems( false, taskId);
		Integer maxAvailableVersion = ipcChargeLineitemRepository
				.findLatestVersionByServiceCodeAndActionType(task.getServiceCode(), IpcConstants.CREATE);
		List<IpcChargeLineitem> ipcChargeLineitemsFromDB = ipcChargeLineitemRepository
				.findByServiceCodeAndVersionAndActionType(task.getServiceCode(), maxAvailableVersion,
						IpcConstants.CREATE);

		// Iterating input lineitems(Since preference is for the input lineitems) and
		// saving IPCChargeLineItem in DB with incremented version
		lineItems.stream().filter(x -> (x.getParentCloudCode() != null && x.getLineitem().contains(IpcConstants.VM))).forEach(lineItem -> {
			String actionType = IpcConstants.ACTION_TYPE_DELETE;
			deletedLineItemDetailsBeanM.put( lineItem.getParentCloudCode(), new DeletedLineItemDetailsBean(
					getFlavourName(lineItem.getLineitem()),
					getScAttributeValueByAttributeName(lineItem.getCloudCode(), IpcConstants.HOST_NAME),
					actionType, getScAttributeValueByAttributeName(lineItem.getCloudCode(), IpcConstants.DATA_RETENTION_ENABLED),
					getScAttributeValueByAttributeName(lineItem.getCloudCode(), IpcConstants.DATA_RETENTION_PERIOD)));
		});

		ipcChargeLineitemsFromDB.stream().filter(x -> x.getChargeLineitem().contains(IpcConstants.VM) && !deletedLineItemDetailsBeanM.containsKey(x.getCloudCode())).forEach(lineItem -> {
			deletedLineItemDetailsBeanM.put( lineItem.getCloudCode(), new DeletedLineItemDetailsBean(
					getFlavourName(lineItem.getChargeLineitem().equals(IpcConstants.VM_USAGE_CHARGES) ? lineItem.getProductDescription(): lineItem.getChargeLineitem()),
					getScAttributeValueByAttributeName(lineItem.getCloudCode(), IpcConstants.HOST_NAME), IpcConstants.IPC_ACTIVE, IpcConstants.N_BACKSLASH_A, IpcConstants.N_BACKSLASH_A));
		});
		return new ArrayList<DeletedLineItemDetailsBean>(deletedLineItemDetailsBeanM.values());
	}
	
	private String getFlavourName(String flavour) {
		if( flavour.contains(IpcConstants.VM)) {
			return flavour.split(IpcConstants.SINGLE_SPACE + IpcConstants.VM)[0] + IpcConstants.SINGLE_SPACE + IpcConstants.VM;
		}
		return flavour;
	}

	private String getScAttributeValueByAttributeName( String cloudCode, String attributeName) {
		ScProductDetail scProductDetail = scProductDetailRepository.findFirstByCloudCodeOrderByIdDesc(cloudCode);
		ScProductDetailAttribute scProductDetailAttribute = scProductDetailAttributeRepository.findByScProductDetail_idAndAttributeNameAndCategory(scProductDetail.getId(), attributeName, IpcConstants.IPC_COMMON);
		if(scProductDetailAttribute != null) {
			return scProductDetailAttribute.getAttributeValue();
		}
		return "";
	}
}