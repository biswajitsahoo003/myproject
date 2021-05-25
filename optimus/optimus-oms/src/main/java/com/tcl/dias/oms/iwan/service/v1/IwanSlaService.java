package com.tcl.dias.oms.iwan.service.v1;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.LocationDetail;
import com.tcl.dias.common.beans.ProductSlaBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.SLaDetailsBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.SlaConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.AddressDetail;
import com.tcl.dias.oms.constants.AttributeConstants;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.FPConstants;
import com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteIllSiteSla;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.entities.SiteFeasibility;
import com.tcl.dias.oms.entity.entities.SlaMaster;
import com.tcl.dias.oms.entity.repository.LeProductSlaRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteSlaRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.SlaMasterRepository;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the IwanSlaService.java class.
 * 
 * used for SLa realted functionalities
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Service
@Transactional
public class IwanSlaService {

	private static final Logger LOGGER = LoggerFactory.getLogger(IwanSlaService.class);

	@Autowired
	private LeProductSlaRepository leProductSlaRepository;

	@Autowired
	private QuoteIllSiteSlaRepository quoteIllSiteSlaRepository;

	@Autowired
	private SlaMasterRepository slaMasterRepository;

	@Autowired
	MQUtils mqUtils;

	@Value("${rabbitmq.location.detail}")
	String locationQueue;

	@Value("${rabbitmq.sla.queue}")
	String producSlaQueue;

	@Value("${rabbitmq.poplocation.detail}")
	String poplocationQueue;

	@Value("${rabbitmq.sla.city.queue}")
	String productSlaCityQueue;

	@Autowired
	QuoteProductComponentRepository quoteProductComponentRepository;

	@Autowired
	QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@Autowired
	QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;

	@Autowired
	ProductAttributeMasterRepository productAttributeMasterRepository;

	@Autowired
	SiteFeasibilityRepository siteFeasibilityRepository;

	@Value("${standard.value}")
	String standardValue;

	@Value("${premium.value}")
	String premiumValue;
	
	@Value("${compressed.value}")
	String compressedValue;
	
	//TODO: calling ias only, as there was issue seen after deployment that iwan property was not found
	@Value("${rabbitmq.ias.packetdrop.queue}")
	String packetDropQueue;

	/**
	 * 
	 * saveSla
	 * 
	 * @param quoteToLe
	 * @throws TclCommonException
	 */
	public void saveSla(QuoteToLe quoteToLe) throws TclCommonException {
		ProductSlaBean productBean = null;
		try {
			List<QuoteToLeProductFamily> mstProductFamily = quoteToLeProductFamilyRepository
					.findByQuoteToLe(quoteToLe.getId());
			for (QuoteToLeProductFamily quoteToLeProductFamily : mstProductFamily) {
				processQuoteLe(quoteToLe, quoteToLeProductFamily.getMstProductFamily().getName(), productBean);
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.SAVE_SLA_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	private void processQuoteLe(QuoteToLe quoteLE, String familName, ProductSlaBean productBean) {
		quoteLE.getQuoteToLeProductFamilies().stream()
				.forEach(quoProd -> processProductSolutions(quoProd, familName, productBean));
	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ processProductSolutions used to get
	 *       the product solution details
	 * @param illSiteDtos
	 * @param quoProd
	 * @param leProductSla
	 * @param familName
	 * @param productBean
	 */
	private void processProductSolutions(QuoteToLeProductFamily quoProd, String familName, ProductSlaBean productBean) {
		if (quoProd.getMstProductFamily().getName().equals(familName)) {
			quoProd.getProductSolutions().stream().forEach(prodSol -> processIllSites(prodSol, productBean));
		}
	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ processIllSites used to process the
	 *       ill site with sla details
	 * @param illSiteDtos
	 * @param prodSol
	 * @param leProductSla
	 * @param productBean
	 */
	private void processIllSites(ProductSolution prodSol, ProductSlaBean productBean) {
		prodSol.getQuoteIllSites().stream().forEach(ill -> processSLa(ill, productBean));

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ processSiteTaxExempted this is used
	 *       to process the ill site with Product sla
	 * @param illSiteDtos
	 * @param ill
	 * @param leProductSla
	 * @param productBean
	 */
	private void processSLa(QuoteIllSite ill, ProductSlaBean productBean) {
		if (ill.getStatus() == 1) {
			try {
				constructIllSiteSla(ill, productBean);
			} catch (TclCommonException e) {
				LOGGER.error("error in process Sla", e);

			}

		}
	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ getLeProductSla used to get the
	 *       LeProductSla
	 * @param customerDetails
	 * @param quoteToLe
	 * @param mstProductFamily
	 * @return
	 */

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ constructIllSiteSla used to
	 *       construct Site sla detials
	 * @param leProductSla
	 * @param quoteIllSite
	 * @param productBean
	 */
	public void constructIllSiteSla(QuoteIllSite quoteIllSite, ProductSlaBean productBean) throws TclCommonException {
		if (inPopAvailable(quoteIllSite)) {
			constructDataFromProductSla(quoteIllSite);
		} else {
			processSlaFromCity(quoteIllSite);
		}
	}

	private void processSlaFromCity(QuoteIllSite quoteIllSite) throws TclCommonException {
		ProductSlaBean productBean = null;
		LOGGER.info("MDC Filter token value in before Queue call processSlaFromCity {} :",
				MDC.get(CommonConstants.MDC_TOKEN_KEY));
		String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
				String.valueOf(quoteIllSite.getErfLocSitebLocationId()));
		AddressDetail addressDetail= null;
		if(StringUtils.isNotBlank(locationResponse)) {
			addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse, AddressDetail.class);
		}

		if (addressDetail != null) {
			List<SiteFeasibility> selectedFeasibility = siteFeasibilityRepository
					.findByQuoteIllSite_IdAndIsSelected(quoteIllSite.getId(), CommonConstants.BACTIVE);
			if (!selectedFeasibility.isEmpty()) {
				String feasibilityType = getFeasibilityType(selectedFeasibility);
				String serviceVariant = getServiceVariant(quoteIllSite);
				LOGGER.info("MDC Filter token value in before Queue call processSlaFromCity {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				String response = (String) mqUtils
						.sendAndReceive(productSlaCityQueue,
								addressDetail.getCity() + CommonConstants.COMMA + feasibilityType
										+ CommonConstants.COMMA + serviceVariant);
				LOGGER.info("Output Payload for for sla with city {}", response);
				if (response != null) {
					productBean = (ProductSlaBean) Utils.convertJsonToObject(response, ProductSlaBean.class);
				}
				if (productBean != null) {
					String networkUpTime = StringUtils.EMPTY;
					String roundTripDelay = StringUtils.EMPTY;
					List<SLaDetailsBean> slaDetailBeans = productBean.getsLaDetails();
					for (SLaDetailsBean sLaDetailsBean : slaDetailBeans) {
						if (sLaDetailsBean.getName().equalsIgnoreCase(SlaConstants.NETWORK_UP_TIME.getName())) {
							networkUpTime = sLaDetailsBean.getSlaValue();
						}

						if (sLaDetailsBean.getName().equalsIgnoreCase(SlaConstants.ROUND_TRIP_DELAY.getName())) {
							roundTripDelay = sLaDetailsBean.getSlaValue();
							persistSlaDetail(quoteIllSite, roundTripDelay,
									FPConstants.LATENCY_ROUND_TRIP_DELAY.toString());
						}
					}
					persistSlaDetail(quoteIllSite, networkUpTime, FPConstants.NETWORK_UPTIME.toString());
					persistPacketDrop(quoteIllSite);

				}
			}

		}
	}

	private boolean inPopAvailable(QuoteIllSite quoteIllSite) {
		return (quoteIllSite.getErfLocSiteaSiteCode() != null);
	}

	private void persistPacketDrop(QuoteIllSite quoteIllSite) {
		String serviceType = FPConstants.STANDARD.toString();
		String slaValue = CommonConstants.EMPTY;
		List<QuoteProductComponent> quoteComponents = quoteProductComponentRepository
				.findByReferenceIdAndMstProductComponent_NameAndReferenceName(quoteIllSite.getId(), "IWAN Common",QuoteConstants.IWANSITES.toString());

		for (QuoteProductComponent quoteProductComponent : quoteComponents) {
			List<QuoteProductComponentsAttributeValue> attributes = quoteProductComponentsAttributeValueRepository
					.findByQuoteProductComponent_Id(quoteProductComponent.getId());
			for (QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue : attributes) {
				Optional<ProductAttributeMaster> prodAttrMaster = productAttributeMasterRepository
						.findById(quoteProductComponentsAttributeValue.getProductAttributeMaster().getId());
				if (prodAttrMaster.isPresent()
						&& prodAttrMaster.get().getName().equals(FPConstants.SERVICE_VARIANT.toString())) {
					if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues()))
						serviceType = quoteProductComponentsAttributeValue.getAttributeValues();
					break;
				}
			}
		}
		String sltVariant = CommonConstants.EMPTY;
		if (FPConstants.STANDARD.toString().equals(serviceType)) {
			sltVariant = FPConstants.STANDARD.toString();
		} else if (FPConstants.PREMIUM.toString().equals(serviceType)) {
			sltVariant = FPConstants.PREMIUM.toString();
		}else if (FPConstants.COMPRESSED_INTERNET.toString().equals(serviceType)) {
			sltVariant = FPConstants.COMPRESSED.toString();
		}
		try {
		slaValue = (String) mqUtils.sendAndReceive(packetDropQueue, sltVariant);
		}catch(Exception e) {
			LOGGER.warn("Error in getting the Packet drop value",ExceptionUtils.getStackTrace(e));
		}
		persistSlaDetail(quoteIllSite, slaValue, FPConstants.PACKET_DROP_WITHOUT_PERCENTAGE.toString());
	}

	/**
	 * persistSlaDetail
	 * 
	 * @param quoteIllSite
	 * @param slaValue
	 */
	private void persistSlaDetail(QuoteIllSite quoteIllSite, String slaValue, String slaAttri) {
		SlaMaster master = slaMasterRepository.findBySlaName(slaAttri);
		if (master != null) {
			List<QuoteIllSiteSla> quoteIllSiteSlas = quoteIllSiteSlaRepository
					.findByQuoteIllSiteAndSlaMaster(quoteIllSite, master);
			if (quoteIllSiteSlas != null && !quoteIllSiteSlas.isEmpty()) {
				for (QuoteIllSiteSla quoteIllSiteSla : quoteIllSiteSlas) {
					quoteIllSiteSla.setQuoteIllSite(quoteIllSite);
					quoteIllSiteSla.setSlaMaster(master);
					quoteIllSiteSla.setSlaValue(slaValue);
					quoteIllSiteSlaRepository.save(quoteIllSiteSla);
				}
			} else {
				QuoteIllSiteSla illSiteSla = new QuoteIllSiteSla();
				illSiteSla.setQuoteIllSite(quoteIllSite);
				illSiteSla.setSlaMaster(master);
				illSiteSla.setSlaValue(slaValue);
				quoteIllSiteSlaRepository.save(illSiteSla);
			}
		}
	}

	private void constructDataFromProductSla(QuoteIllSite quoteIllSite) {
		try {
			List<SiteFeasibility> selectedFeasibility = siteFeasibilityRepository
					.findByQuoteIllSite_IdAndIsSelected(quoteIllSite.getId(), CommonConstants.BACTIVE);
			if (!selectedFeasibility.isEmpty()) {
				LOGGER.info("MDC Filter token value in before Queue call constructDataFromProductSla {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				String locationResponse = (String) mqUtils.sendAndReceive(poplocationQueue,
						quoteIllSite.getErfLocSiteaSiteCode());
				if (StringUtils.isNotBlank(locationResponse)) {
					LocationDetail locationDetails = (LocationDetail) Utils.convertJsonToObject(locationResponse,
							LocationDetail.class);
					String tier = locationDetails.getTier();
					String serviceVariant = getServiceVariant(quoteIllSite);
					String feasibilityType = getFeasibilityType(selectedFeasibility);
					LOGGER.info("MDC Filter token value in before Queue call constructDataFromProductSla {} :",
							MDC.get(CommonConstants.MDC_TOKEN_KEY));
					String response = (String) mqUtils.sendAndReceive(producSlaQueue,
							tier + CommonConstants.COMMA + feasibilityType + CommonConstants.COMMA + serviceVariant);

					LOGGER.info("Output Payload for for sla with tier {}", response);
					ProductSlaBean productBean = null;
					if (StringUtils.isNotBlank(response)) {
						productBean = (ProductSlaBean) Utils.convertJsonToObject(response, ProductSlaBean.class);
					}
					if (productBean != null) {
						String networkUpTime = StringUtils.EMPTY;
						String roundTripDelay = StringUtils.EMPTY;
						List<SLaDetailsBean> slaDetailBeans = productBean.getsLaDetails();
						for (SLaDetailsBean sLaDetailsBean : slaDetailBeans) {
							if (sLaDetailsBean.getName().equalsIgnoreCase(SlaConstants.NETWORK_UP_TIME.getName())) {
								networkUpTime = sLaDetailsBean.getSlaValue();
							}
							if (sLaDetailsBean.getName().equalsIgnoreCase(SlaConstants.ROUND_TRIP_DELAY.getName())) {
								roundTripDelay = sLaDetailsBean.getSlaValue();
								persistSlaDetail(quoteIllSite, roundTripDelay,
										FPConstants.LATENCY_ROUND_TRIP_DELAY.toString());
							}
						}
						persistSlaDetail(quoteIllSite, networkUpTime, FPConstants.NETWORK_UPTIME.toString());
						persistPacketDrop(quoteIllSite);

					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("error in getting sla detailsa details ", e);
		}

	}

	private String getServiceVariant(QuoteIllSite quoteIllSite) {
		String[] serviceVariant = { null };
		List<QuoteProductComponent> quoteProductComponentList = quoteProductComponentRepository
				.findByReferenceIdAndMstProductComponent_NameAndType(quoteIllSite.getId(), OrderDetailsExcelDownloadConstants.IWAN_COMMON,
						OrderDetailsExcelDownloadConstants.PRIMARY);
		quoteProductComponentList.stream().forEach(quoteProductComponent -> {
			List<QuoteProductComponentsAttributeValue> quoteProductComponentAttributeValueList = quoteProductComponentsAttributeValueRepository
					.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(
							quoteProductComponent.getId(),
							AttributeConstants.SERVICE_VARIANT.toString());
			serviceVariant[0] = quoteProductComponentAttributeValueList.stream().skip(quoteProductComponentAttributeValueList.size() - 1)
					.findFirst().get().getAttributeValues();
		});
		return serviceVariant[0];
	}

	private String getFeasibilityType(List<SiteFeasibility> selectedFeasibility) {
		String feasibilityType = SlaConstants.NONE.getName();
		if (selectedFeasibility.get(0).getFeasibilityMode().contains("Onnet")) {
			feasibilityType = SlaConstants.ONNET.getName();
		} else if (selectedFeasibility.get(0).getFeasibilityMode().contains("Offnet")) {
			feasibilityType = SlaConstants.OFFNET.getName();
		}
		return feasibilityType;

	}
}
