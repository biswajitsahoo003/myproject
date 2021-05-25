package com.tcl.dias.oms.ipc.service.v1;

import static com.tcl.dias.common.constants.CommonConstants.BACTIVE;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.QuoteCloud;
import com.tcl.dias.oms.entity.entities.QuotePrice;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.enums.FPStatus;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.QuoteCloudRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.ipc.beans.pricebean.Component;
import com.tcl.dias.oms.ipc.beans.pricebean.IpcCommercialBean;
import com.tcl.dias.oms.ipc.constants.IPCQuoteConstants;
import com.tcl.dias.oms.service.OmsUtilService;

@Service
@Transactional
public class IPCCommercialService {

	private static final Logger LOGGER = LoggerFactory.getLogger(IPCCommercialService.class);

	@Autowired
	QuoteProductComponentRepository quoteProductComponentRepository;

	@Autowired
	QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@Autowired
	ProductAttributeMasterRepository productAttributeMasterRepository;

	@Autowired
	QuoteCloudRepository quoteCloudRepository;

	@Autowired
	QuotePriceRepository quotePriceRepository;

	@Autowired
	OmsUtilService omsUtilService;

	@Autowired
	IPCPricingService iPCPricingService;

	public void updateDiscountedQuotePrice(Integer quoteId, Integer quoteToLeId, IpcCommercialBean ipcCommercialBean) {
		LOGGER.info("updateDiscountedQuotePrice start");
		List<Component> productComponent = ipcCommercialBean.getProductComponents();
		List<QuoteCloud> quoteClouds = quoteCloudRepository.findByQuoteToLeIdAndStatus(quoteToLeId, (byte) 1);
		Map<Integer, Component> productComponentMap = productComponent.stream()
				.collect(Collectors.toMap(Component::getId, x -> x));
		LOGGER.info("productComponentMap {}", productComponentMap);
		quoteClouds.forEach(quoteCloud -> {
			List<QuoteProductComponent> quoteProductComponentList = quoteProductComponentRepository
					.findByReferenceIdAndMstProductFamily_Name(quoteCloud.getId(), IPCQuoteConstants.PRODUCT_NAME);
			if (IPCQuoteConstants.SOLUTION_IPC_ADDON.equals(quoteCloud.getResourceDisplayName())) {
				LOGGER.info("IPC Addon Flow - Save Call");
				makeEntryForUpdatedAddons(quoteId, quoteCloud, productComponentMap, quoteProductComponentList);
			} else {
				LOGGER.info("CloudVm and Access Flow - Save Call");
				makeEntryForVmsAndAccess(quoteCloud, productComponentMap, quoteProductComponentList);
			}
		});
		LOGGER.info("updateDiscountedQuotePrice end");
	}

	private void makeEntryForVmsAndAccess(QuoteCloud quoteCloud, Map<Integer, Component> productComponentMap,
			List<QuoteProductComponent> quoteProductComponentList) {
		quoteProductComponentList.stream().filter(component -> productComponentMap.containsKey(component.getId()))
				.forEach(component -> {
					Component componentFromReq = productComponentMap.get(component.getId());
					if (null != componentFromReq.getAskedMrc()
							&& quoteCloud.getMrc().compareTo(componentFromReq.getAskedMrc()) != 0) {
						processQuoteProductComponentAttributeValue(component, IPCQuoteConstants.IPC_ASK_MRC,
								componentFromReq.getAskedMrc());
					} else if (null != componentFromReq.getAskedNrc()
							&& quoteCloud.getNrc().compareTo(componentFromReq.getAskedNrc()) != 0) {
						processQuoteProductComponentAttributeValue(component, IPCQuoteConstants.IPC_ASK_NRC,
								componentFromReq.getAskedNrc());
					} else if (null != componentFromReq.getAskedPpuRate()
							&& quoteCloud.getPpuRate().compareTo(componentFromReq.getAskedPpuRate()) != 0) {
						processQuoteProductComponentAttributeValue(component, IPCQuoteConstants.IPC_ASK_PPU_RATE,
								componentFromReq.getAskedPpuRate());
					}
				});
	}

	private void makeEntryForUpdatedAddons(Integer quoteId, QuoteCloud quoteCloud, Map<Integer, Component> productComponentMap,
			List<QuoteProductComponent> quoteProductComponentList) {
		quoteProductComponentList.stream().filter(component -> productComponentMap.containsKey(component.getId()))
				.forEach(component -> {
					QuotePrice compPrice = quotePriceRepository.findByReferenceIdAndReferenceName(
							String.valueOf(component.getId()), QuoteConstants.COMPONENTS.toString());
					Component componentFromReq = productComponentMap.get(component.getId());
					if (null != componentFromReq.getAskedMrc()
							&& compPrice.getEffectiveMrc().compareTo(componentFromReq.getAskedMrc()) != 0) {
						processQuoteProductComponentAttributeValue(component, IPCQuoteConstants.IPC_ASK_MRC,
								componentFromReq.getAskedMrc());
					} else if (null != componentFromReq.getAskedNrc()
							&& compPrice.getEffectiveNrc().compareTo(componentFromReq.getAskedNrc()) != 0) {
						processQuoteProductComponentAttributeValue(component, IPCQuoteConstants.IPC_ASK_NRC,
								componentFromReq.getAskedNrc());
					}
				});
	}

	private void processQuoteProductComponentAttributeValue(QuoteProductComponent quoteProductComponent,
			String attributeName, Double attributeValue) {
		LOGGER.info("processQuoteComponentAttributeValue Start");
		ProductAttributeMaster productAttributeMaster = getProductAttributes(attributeName);
		List<QuoteProductComponentsAttributeValue> quoteComponentAttrValue = quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponentAndProductAttributeMaster(quoteProductComponent, productAttributeMaster);
		QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = null;
		if (null != quoteComponentAttrValue && !quoteComponentAttrValue.isEmpty()) {
			quoteProductComponentsAttributeValue = quoteComponentAttrValue.get(0);
		} else {
			quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
			quoteProductComponentsAttributeValue.setQuoteProductComponent(quoteProductComponent);
			quoteProductComponentsAttributeValue.setProductAttributeMaster(productAttributeMaster);
		}
		quoteProductComponentsAttributeValue.setAttributeValues(String.valueOf(attributeValue));
		quoteProductComponentsAttributeValueRepository.save(quoteProductComponentsAttributeValue);
		LOGGER.info("processQuoteComponentAttributeValue End");
	}

	private ProductAttributeMaster getProductAttributes(String attributeName) {
		ProductAttributeMaster productAttributeMaster = null;
		List<ProductAttributeMaster> productAttributeMasters = productAttributeMasterRepository
				.findByNameAndStatus(attributeName, (byte) 1);
		if (productAttributeMasters != null && !productAttributeMasters.isEmpty()) {
			productAttributeMaster = productAttributeMasters.get(0);
		} else {
			productAttributeMaster = new ProductAttributeMaster();
			productAttributeMaster.setName(attributeName);
			productAttributeMaster.setDescription(attributeName);
			productAttributeMaster.setStatus((byte) 1);
			productAttributeMaster.setCreatedBy(Utils.getSource());
			productAttributeMasterRepository.save(productAttributeMaster);
		}
		return productAttributeMaster;
	}

	public void processCommercialQuoteRejection(Integer quoteId, Integer quoteToLeId) {
		LOGGER.info("Entered into processCommercialQuoteRejection..");
		List<QuoteCloud> quoteClouds = quoteCloudRepository.findByQuoteToLeIdAndStatus(quoteToLeId, (byte) 1);
		List<Integer> productMasterIds = productAttributeMasterRepository
				.findByNameInAndStatus(Arrays.asList(IPCQuoteConstants.IPC_ASK_MRC, IPCQuoteConstants.IPC_ASK_NRC,
						IPCQuoteConstants.IPC_ASK_PPU_RATE), BACTIVE)
				.stream().map(x -> x.getId()).collect(Collectors.toList());
		if (!productMasterIds.isEmpty()) {
			quoteClouds.forEach(quoteCloud -> {
				List<QuoteProductComponent> productComponents = quoteProductComponentRepository
						.findByReferenceIdAndMstProductFamily_Name(quoteCloud.getId(), IPCQuoteConstants.PRODUCT_NAME);
				productComponents.forEach(component -> {
					List<QuoteProductComponentsAttributeValue> quotePrdCompAttrValues = quoteProductComponentsAttributeValueRepository
							.findByQuoteProductComponent_IdInAndProductAttributeMaster_IdIn(
									Arrays.asList(component.getId()), productMasterIds);
					quotePrdCompAttrValues.forEach(value -> {
						LOGGER.info(
								"Attributes getting deleted via Commercial : Id {} ComponentId {} Name {} and Value {}",
								value.getId(), value.getQuoteProductComponent().getId(),
								value.getProductAttributeMaster().getName(), value.getAttributeValues());
						quoteProductComponentsAttributeValueRepository.delete(value);
					});
				});
			});
		}
	}

	public void processCommercialQuoteApprovalPrice(QuoteToLe quoteToLe) {
		LOGGER.info("Entered into processCommercialQuoteApprovalPrice..");
		List<QuoteCloud> quoteClouds = quoteCloudRepository.findByQuoteToLeIdAndStatus(quoteToLe.getId(), (byte) 1);
		List<Integer> productMasterIds = productAttributeMasterRepository
				.findByNameInAndStatus(Arrays.asList(IPCQuoteConstants.IPC_ASK_MRC, IPCQuoteConstants.IPC_ASK_NRC,
						IPCQuoteConstants.IPC_ASK_PPU_RATE), BACTIVE)
				.stream().map(x -> x.getId()).collect(Collectors.toList());
		if (!productMasterIds.isEmpty()) {
			quoteClouds.forEach(quoteCloud -> {
				List<QuoteProductComponent> productComponents = quoteProductComponentRepository
						.findByReferenceIdAndMstProductFamily_Name(quoteCloud.getId(), IPCQuoteConstants.PRODUCT_NAME);
				if (IPCQuoteConstants.SOLUTION_IPC_ADDON.equals(quoteCloud.getResourceDisplayName())) {
					LOGGER.info("IPC Addon Flow");
					processIPCAddonQuoteDetails(quoteToLe, quoteCloud, productMasterIds, productComponents);
				} else {
					LOGGER.info("CloudVm and Access Flow");
					processCloudVmAndAccessDetails(quoteToLe, quoteCloud, productMasterIds, productComponents);
				}
			});
		}
		iPCPricingService.recalculate(quoteToLe);
		LOGGER.info("Recalculation Logic for QuoteToLe Done - Processing component removal");
		// Removing Components used for Commercial Once Approved at final level
		processCommercialQuoteRejection(null, quoteToLe.getId());
	}

	private void processCloudVmAndAccessDetails(QuoteToLe quoteToLe, QuoteCloud quoteCloud,
			List<Integer> productMasterIds, List<QuoteProductComponent> productComponents) {
		LOGGER.info("Entered into processCloudVmAndAccessDetails..");
		productComponents.forEach(component -> {
			List<QuoteProductComponentsAttributeValue> quotePrdCompAttrValues = quoteProductComponentsAttributeValueRepository
					.findByQuoteProductComponent_IdInAndProductAttributeMaster_IdIn(Arrays.asList(component.getId()),
							productMasterIds);
			if(!quotePrdCompAttrValues.isEmpty()) {
				quotePrdCompAttrValues.forEach(value -> {
					processAndUpdateQuoteCloudPrice(quoteToLe, quoteCloud, value);
				});
				quoteCloud.setFpStatus(FPStatus.MP.toString());
				quoteCloudRepository.save(quoteCloud);
			}
		});
		LOGGER.info("End : processCloudVmAndAccessDetails..");
	}

	private void processIPCAddonQuoteDetails(QuoteToLe quoteToLe, QuoteCloud quoteCloud, List<Integer> productMasterIds,
			List<QuoteProductComponent> productComponents) {
		LOGGER.info("Entered into processIPCAddonQuoteDetails..");
		int [] flag = {0};
		productComponents.forEach(component -> {
			List<QuoteProductComponentsAttributeValue> quotePrdCompAttrValues = quoteProductComponentsAttributeValueRepository
					.findByQuoteProductComponent_IdInAndProductAttributeMaster_IdIn(Arrays.asList(component.getId()),
							productMasterIds);
			quotePrdCompAttrValues.forEach(value -> {
				LOGGER.info("value details - component : {} - id : {} - attribute : Id {} - attibute value : {}",
						component.getId(), value.getId(), value.getProductAttributeMaster().getName(),
						value.getAttributeValues());
				processAndUpdateQuotePrice(quoteToLe, value, component);
				flag[0] = 1;
			});

		});
		if(flag[0] == 1) {
			List<QuotePrice> quotePrices = quotePriceRepository.findByQuoteId(quoteToLe.getQuote().getId());
			reCalculateCloudPrice(quoteCloud, quotePrices);
		}
		LOGGER.info("End : processIPCAddonQuoteDetails..");
	}

	private void reCalculateCloudPrice(QuoteCloud quoteCloud, List<QuotePrice> quotePrices) {
		LOGGER.info("Entered into reCalculateCloudPrice..");
		Double effecArc = 0D;
		Double effecMrc = 0D;
		Double effecNrc = 0D;
		for (QuotePrice quotePrice : quotePrices) {
			if (quotePrice != null) {
				effecArc = effecArc + (quotePrice.getEffectiveArc() != null ? quotePrice.getEffectiveArc() : 0D);
				effecMrc = effecMrc + (quotePrice.getEffectiveMrc() != null ? quotePrice.getEffectiveMrc() : 0D);
				effecNrc = effecNrc + (quotePrice.getEffectiveNrc() != null ? quotePrice.getEffectiveNrc() : 0D);
			}
		}
		quoteCloud.setFpStatus(FPStatus.MP.toString());
		quoteCloud.setMrc(effecMrc);
		quoteCloud.setArc(effecArc);
		quoteCloud.setNrc(effecNrc);
		quoteCloudRepository.save(quoteCloud);
	}

	private void processAndUpdateQuoteCloudPrice(QuoteToLe quoteToLe, QuoteCloud quoteCloud,
			QuoteProductComponentsAttributeValue value) {
		LOGGER.info("Entered into processAndUpdateQuoteCloudPrice..");
		if (IPCQuoteConstants.IPC_ASK_MRC.equals(value.getProductAttributeMaster().getName())) {
			quoteCloud.setMrc(Double.valueOf(value.getAttributeValues()));
			quoteCloud.setArc(quoteCloud.getMrc() * 12);
		} else if (IPCQuoteConstants.IPC_ASK_NRC.equals(value.getProductAttributeMaster().getName())) {
			quoteCloud.setNrc(Double.valueOf(value.getAttributeValues()));
		} else if (IPCQuoteConstants.IPC_ASK_PPU_RATE.equals(value.getProductAttributeMaster().getName())) {
			quoteCloud.setPpuRate(Double.valueOf(value.getAttributeValues()));
		}
	}

	private void processAndUpdateQuotePrice(QuoteToLe quoteToLe, QuoteProductComponentsAttributeValue value,
			QuoteProductComponent component) {
		LOGGER.info("Entered into processAndUpdateQuotePrice..");
		if (IPCQuoteConstants.IPC_ASK_MRC.equals(value.getProductAttributeMaster().getName())) {
			processComponentPrice(quoteToLe, value.getQuoteProductComponent(), component.getMstProductComponent(),
					Double.valueOf(value.getAttributeValues()), null);
		} else if (IPCQuoteConstants.IPC_ASK_NRC.equals(value.getProductAttributeMaster().getName())) {
			processComponentPrice(quoteToLe, value.getQuoteProductComponent(), component.getMstProductComponent(), null,
					Double.valueOf(value.getAttributeValues()));
		} 
	}

	private void processComponentPrice(QuoteToLe quoteToLe, QuoteProductComponent component,
			MstProductComponent mstProductComponent, Double mrc, Double nrc) {
		LOGGER.info("Entered into processIPCAddonQuoteDetails..");
		QuotePrice compPrice = quotePriceRepository.findByReferenceIdAndReferenceName(String.valueOf(component.getId()),
				QuoteConstants.COMPONENTS.toString());
		if (!Objects.isNull(compPrice)) {
			if (null != mrc) {
				compPrice.setEffectiveMrc(round(mrc));
				compPrice.setEffectiveArc(round(mrc * 12));
			}
			if (null != nrc) {
				compPrice.setEffectiveNrc(round(nrc));
			}
			quotePriceRepository.save(compPrice);
		}
		LOGGER.info("End : processIPCAddonQuoteDetails..");
	}

	public static double round(double value) {
		return (double) Math.round(value * 100) / 100;
	}
}
