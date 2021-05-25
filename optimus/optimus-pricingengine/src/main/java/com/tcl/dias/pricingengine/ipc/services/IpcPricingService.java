package com.tcl.dias.pricingengine.ipc.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.criteria.Predicate;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.AddressDetail;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.pricingengine.ipc.beans.Access;
import com.tcl.dias.pricingengine.ipc.beans.AdditionalStorage;
import com.tcl.dias.pricingengine.ipc.beans.Addon;
import com.tcl.dias.pricingengine.ipc.beans.Cloudvm;
import com.tcl.dias.pricingengine.ipc.beans.Component;
import com.tcl.dias.pricingengine.ipc.beans.CrossBorderBean;
import com.tcl.dias.pricingengine.ipc.beans.IpcDiscountBean;
import com.tcl.dias.pricingengine.ipc.beans.PricingBean;
import com.tcl.dias.pricingengine.ipc.beans.PricingInputDatum;
import com.tcl.dias.pricingengine.ipc.beans.PricingRequest;
import com.tcl.dias.pricingengine.ipc.beans.PricingResponse;
import com.tcl.dias.pricingengine.ipc.beans.Quote;
import com.tcl.dias.pricingengine.ipc.beans.RateCard;
import com.tcl.dias.pricingengine.ipc.beans.RateCardAttribute;
import com.tcl.dias.pricingengine.ipc.beans.RateCardItem;
import com.tcl.dias.pricingengine.ipc.beans.Result;
import com.tcl.dias.pricingengine.ipc.constants.IpcPricingConstants;
import com.tcl.dias.pricingengine.ipc.utils.IpcUtils;
import com.tcl.dias.productcatelog.entity.entities.PricingIpcAttribute;
import com.tcl.dias.productcatelog.entity.entities.PricingIpcCrossBorderWhTax;
import com.tcl.dias.productcatelog.entity.entities.PricingIpcCustomerNetMargin;
import com.tcl.dias.productcatelog.entity.entities.PricingIpcDatatransfer;
import com.tcl.dias.productcatelog.entity.entities.PricingIpcItem;
import com.tcl.dias.productcatelog.entity.entities.PricingIpcLocation;
import com.tcl.dias.productcatelog.entity.entities.PricingIpcPartnerCommission;
import com.tcl.dias.productcatelog.entity.entities.PricingIpcRateCard;
import com.tcl.dias.productcatelog.entity.entities.PricingIpcRateCardAttribute;
import com.tcl.dias.productcatelog.entity.repository.PricingIpcAttributesRepository;
import com.tcl.dias.productcatelog.entity.repository.PricingIpcCrossBorderWhTaxRepository;
import com.tcl.dias.productcatelog.entity.repository.PricingIpcCustomerNetMarginRepository;
import com.tcl.dias.productcatelog.entity.repository.PricingIpcDatatransferRepository;
import com.tcl.dias.productcatelog.entity.repository.PricingIpcItemsRepository;
import com.tcl.dias.productcatelog.entity.repository.PricingIpcLocationRepository;
import com.tcl.dias.productcatelog.entity.repository.PricingIpcPartnerCommissionRespository;
import com.tcl.dias.productcatelog.entity.repository.PricingIpcRateCardAttributesRepository;
import com.tcl.dias.productcatelog.entity.repository.PricingIpcRateCardRepository;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This class is to used to give the IPC pricing
 * 
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Service
public class IpcPricingService {

	private static final Logger LOGGER = LoggerFactory.getLogger(IpcPricingService.class);

	@Autowired
	private RestClientService restClientService;

	@Value("${pricing.request.url}")
	private String rPricingUrl;

	@Value("${rabbitmq.mstaddrbylocationid.detail}")
	private String locationDetailsQueue;

	@Value("${rabbitmq.currency.conversion.rate}")
	private String currencyConversionRateQueue;

	@Autowired
	private MQUtils mqUtils;

	@Autowired
	private PricingIpcLocationRepository pricingIpcLocationRepository;

	@Autowired
	private PricingIpcDatatransferRepository pricingIpcDatatransferRepository;

	@Autowired
	private PricingIpcCrossBorderWhTaxRepository pricingIpcCrossBorderWhTaxRepository;

	@Autowired
	private PricingIpcCustomerNetMarginRepository pricingIpcCustomerNetMarginRepository;

	@Autowired
	private PricingIpcPartnerCommissionRespository pricingIpcPartnerCommissionRespository;

	@Autowired
	private PricingIpcRateCardRepository pricingIpcRateCardRepository;

	@Autowired
	private PricingIpcRateCardAttributesRepository pricingIpcRateCardAttributesRepository;

	@Autowired
	private PricingIpcItemsRepository pricingIpcItemsRepository;

	@Autowired
	private PricingIpcAttributesRepository pricingIpcAttributesRepository;

	/**
	 * processIpcPricing - This method processes the IPC pricing
	 * 
	 * @param pricingRequest
	 * @return String
	 * @throws TclCommonException
	 */
	@Transactional
	public String processIpcPricing(String pricingRequest) throws TclCommonException {
		String pricingResponse = "";

		LOGGER.info("IPC Pricing Request: {}", pricingRequest);
		PricingBean pricingBean = Utils.convertJsonToObject(pricingRequest, PricingBean.class);

		Map<String, Object> cloudMapper = new HashMap<>();
		initiateVmMapper(cloudMapper);

		try {
			for (Quote quote : pricingBean.getQuotes()) {

				enrichQuote(quote);

				enrichCloudMapper(quote, cloudMapper);

				if (validateCloudMapper(cloudMapper) || quote.getAddon().size() > 0) {
					JSONObject vmPricingResponse = getVmPricingFromRateCard(cloudMapper, quote);
					pricingBean.setVmPricingResponse(vmPricingResponse.toJSONString());
					LOGGER.info("Pricing from Rate Card is {}", vmPricingResponse.toJSONString());

					if (vmPricingResponse != null && !vmPricingResponse.containsKey("error")) {
						Map<String, Map<String, Object>> salePriceMapper = getSalePriceMapper(vmPricingResponse);
						Map<String, String> attributeMapper = getPricingAttributeMapper(vmPricingResponse);

						calculatePricingForVMs(quote, salePriceMapper, attributeMapper);

						calculatePricingForAccess(quote, cloudMapper);

						calculatePricingForAddons(quote, cloudMapper, salePriceMapper);

						calculateCrossBorderTax(quote, cloudMapper, pricingBean);
					} else {
						pricingBean.setErrorResponse("Pricing request failed caused due to error in data");
					}
				} else {
					calculateCrossBorderTax(quote, cloudMapper, pricingBean);
					pricingBean.setErrorResponse("Pricing not calculated (MACD case)");
					pricingBean.setVmPricingResponse("{}");
				}
			}
		} catch (Exception e) {
			pricingBean.setErrorResponse(e.getMessage());
			LOGGER.error("Error in IPC Pricing: {}", e);
		}

		pricingResponse = Utils.convertObjectToJson(pricingBean);
		LOGGER.info("IPC Pricing Response: {} ", pricingResponse);

		return pricingResponse;
	}

	private void calculateCrossBorderTax(Quote quote, Map<String, Object> cloudMapper, PricingBean pricingBean) {
		if (!"".equals(quote.getCustomerLeCountry()) && quote.getPerformTaxCalculation()) {
			String dcLocationCountry = (String) cloudMapper.get(IpcPricingConstants.SHEET);
			LOGGER.info("Incoming Tax percentage from OMS {}", quote.getCrossBorderWhTaxPercentage());
			if (quote.getCrossBorderWhTaxPercentage() > 0) {
				quote.setCrossBorderWhTaxPercentage(quote.getCrossBorderWhTaxPercentage());
				applyCrossBorderWithHoldingTax(pricingBean, quote.getCrossBorderWhTaxPercentage());
			} else if (quote.getCrossBorderWhTaxPercentage().compareTo(new Double(-1.0)) == 0) {
				quote.setCrossBorderWhTaxPercentage(0.0);
				applyCrossBorderWithHoldingTax(pricingBean, quote.getCrossBorderWhTaxPercentage());
			} else {
				Optional<PricingIpcCrossBorderWhTax> optIpcCrossBorderWhTax = pricingIpcCrossBorderWhTaxRepository
						.findByCustomerLeCountryAndDcLocationCountry(quote.getCustomerLeCountry(), dcLocationCountry);
				optIpcCrossBorderWhTax.ifPresent(ipcCrossBorderWhTax -> {
					if (ipcCrossBorderWhTax.getTaxPercentage() > 0) {
						quote.setCrossBorderWhTaxPercentage(ipcCrossBorderWhTax.getTaxPercentage());
						applyCrossBorderWithHoldingTax(pricingBean, ipcCrossBorderWhTax.getTaxPercentage());
					}
				});
			}
		}
	}

	private void applyCrossBorderWithHoldingTax(PricingBean pricingBean, Double taxPerecentage) {
		pricingBean.getQuotes().forEach(quote -> {
			quote.getCloudvm().forEach(cloudVm -> {
				cloudVm.setMrc(calculateTaxedAmount(cloudVm.getMrc(), taxPerecentage));
				cloudVm.setNrc(calculateTaxedAmount(cloudVm.getNrc(), taxPerecentage));
			});
			quote.getAccess().forEach(access -> {
				access.setMrc(calculateTaxedAmount(access.getMrc(), taxPerecentage));
				access.setNrc(calculateTaxedAmount(access.getNrc(), taxPerecentage));
			});
			quote.getAddon().forEach(addon -> {
				addon.setMrc(calculateTaxedAmount(addon.getMrc(), taxPerecentage));
				addon.setNrc(calculateTaxedAmount(addon.getNrc(), taxPerecentage));
				addon.getPriceBreakup().forEach((k, v) -> {
					v.setMrc(calculateTaxedAmount(v.getMrc(), taxPerecentage));
					v.setNrc(calculateTaxedAmount(v.getNrc(), taxPerecentage));
				});
			});
		});
	}

	private Double calculateTaxedAmount(Double actualAmount, Double taxPerecentage) {
		Double revisedAmount = 0D;
		if (actualAmount > 0) {
			revisedAmount = IpcUtils.roundOff2((actualAmount * 100) / (100 - taxPerecentage));
			LOGGER.info("Actual Amount: {} & Revised Amount: {}.", actualAmount, revisedAmount); // NOSONAR

		} else {
			LOGGER.info("Actual Amount is actually zero.");
		}
		return revisedAmount;
	}

	public String processCrossBorderTax(String request) throws TclCommonException {
		CrossBorderBean crossBorderBean = Utils.convertJsonToObject(request, CrossBorderBean.class);
		LOGGER.info("Input Received for Cross Border Bean request is {}", crossBorderBean);
		if (!StringUtils.isBlank(crossBorderBean.getDcLocationId())
				&& !StringUtils.isBlank(crossBorderBean.getCustomerLeCountry())) {
			List<PricingIpcLocation> pricingIpcLocationList = pricingIpcLocationRepository
					.findByCityCode(crossBorderBean.getDcLocationId());
			if (!CollectionUtils.isEmpty(pricingIpcLocationList)) {
				PricingIpcLocation pricingIpcLocation = pricingIpcLocationList.get(0);
				crossBorderBean.setDcLocationCountry(pricingIpcLocation.getCountryCode());
				Optional<PricingIpcCrossBorderWhTax> optIpcCrossBorderWhTax = pricingIpcCrossBorderWhTaxRepository
						.findByCustomerLeCountryAndDcLocationCountry(crossBorderBean.getCustomerLeCountry(),
								pricingIpcLocation.getCountryCode());
				optIpcCrossBorderWhTax.ifPresent(ipcCrossBorderWhTax -> {
					crossBorderBean.setIsCrossBorderTaxApplicable(ipcCrossBorderWhTax.getTaxPercentage() > 0);
					crossBorderBean.setCrossBorderWhTaxPercentage(ipcCrossBorderWhTax.getTaxPercentage());
				});
			}
		}
		LOGGER.info("Response for Cross Border Bean request is {}", crossBorderBean);
		return Utils.convertObjectToJson(crossBorderBean);
	}

	@Transactional
	public Boolean processCustomerNetmargin(String request) throws TclCommonException {

		boolean success = false;
		IpcDiscountBean ipcDiscountBean = Utils.convertJsonToObject(request, IpcDiscountBean.class);

		int modifiedRecordCount = pricingIpcCustomerNetMarginRepository.updateCustomerNetMargin(
				ipcDiscountBean.getAdditionalDiscountPercentage(), null, ipcDiscountBean.getCountryCode(),
				ipcDiscountBean.getCityCode(), ipcDiscountBean.getCustomerId());

		success = (modifiedRecordCount > 0);

		if (!success) {
			Optional<List<PricingIpcCustomerNetMargin>> optNetMargins = pricingIpcCustomerNetMarginRepository
					.findByCountryCodeAndCityCodeAndCustomerIdIsNull(ipcDiscountBean.getCountryCode(),
							ipcDiscountBean.getCityCode());
			optNetMargins.ifPresent(customerNetMargins -> {
				List<PricingIpcCustomerNetMargin> ipcCustomerNetMargins = new ArrayList<>();
				customerNetMargins.forEach(customerNetMargin -> {
					PricingIpcCustomerNetMargin ipcCustomerNetMargin = new PricingIpcCustomerNetMargin();
					ipcCustomerNetMargin.setCountryCode(customerNetMargin.getCountryCode());
					ipcCustomerNetMargin.setCityCode(customerNetMargin.getCityCode());
					ipcCustomerNetMargin.setCustomerId(ipcDiscountBean.getCustomerId());
					ipcCustomerNetMargin.setNetMarginPercentage(ipcDiscountBean.getAdditionalDiscountPercentage());
					ipcCustomerNetMargin.setFinalDiscountPercentage(null);
					ipcCustomerNetMargins.add(ipcCustomerNetMargin);
				});
				pricingIpcCustomerNetMarginRepository.saveAll(ipcCustomerNetMargins);
			});
			success = true;
		}
		return success;
	}

	public Map<String, Object> getIPCLocationBasedOnCityCode(String cityCode) throws TclCommonException {
		Map<String, Object> localIpcLocationMapper = new HashMap<>();

		PricingIpcLocation dcLocation = getDcLocation(cityCode);

		localIpcLocationMapper.put(IpcPricingConstants.CITY_CODE, dcLocation.getCityCode());
		localIpcLocationMapper.put(IpcPricingConstants.CITY, dcLocation.getCity());
		localIpcLocationMapper.put(IpcPricingConstants.COUNTRY_CODE, dcLocation.getCountryCode());
		localIpcLocationMapper.put(IpcPricingConstants.LOCATION_ID, dcLocation.getLocationId());

		if (dcLocation.getLocationId() != null) {
			try {
				String addressDetails = (String) mqUtils.sendAndReceive(locationDetailsQueue,
						dcLocation.getLocationId().toString());
				if (StringUtils.isNotBlank(addressDetails)) {
					AddressDetail addressDetail = Utils.convertJsonToObject(addressDetails, AddressDetail.class);
					if (Objects.nonNull(addressDetail)) {
						localIpcLocationMapper.put(IpcPricingConstants.STATE, addressDetail.getState());
					}
				}
			} catch (TclCommonException e) {
				LOGGER.error("Error occured in {}: {}", locationDetailsQueue, e);
			}
		}
		return localIpcLocationMapper;
	}

	public Map<String, String> getIPCDataTransferChargesBasedOnCountryCode(String countryCode)
			throws TclCommonException {
		if (Objects.isNull(countryCode) || countryCode.isEmpty())
			throw new TclCommonException(IpcPricingConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
		Map<String, String> dataTransferCharges = new LinkedHashMap<>();
		List<Map<String, Object>> dataTransferList = pricingIpcDatatransferRepository
				.findByLocationCodeGroupByPriceOrderByStartLimitAsc(countryCode);
		LOGGER.info("dataTransferList size {}", dataTransferList.size());
		for (int i = 0; i < dataTransferList.size(); i++) {
			Map<String, Object> pricingIpcDatatransfer = dataTransferList.get(i);
			if (i != (dataTransferList.size() - 1)) {
				dataTransferCharges.put(
						pricingIpcDatatransfer.get("start_limit") + " to " + pricingIpcDatatransfer.get("end_limit")
								+ " " + pricingIpcDatatransfer.get("unit"),
						String.valueOf(pricingIpcDatatransfer.get("price")));
			} else {
				dataTransferCharges.put(
						"> " + pricingIpcDatatransfer.get("start_limit") + " " + pricingIpcDatatransfer.get("unit"),
						String.valueOf(pricingIpcDatatransfer.get("price")));
			}

		}
		LOGGER.info("dataTransferCharges map {}", dataTransferCharges);
		return dataTransferCharges;
	}

	private void initiateVmMapper(Map<String, Object> cloudMapper) {
		cloudMapper.put(IpcPricingConstants.TERM_IN_MONTHS, 0);
		cloudMapper.put(IpcPricingConstants.TOTAL_VM_QTY_VALUE, 1);
		cloudMapper.put(IpcPricingConstants.PER_MB, 0);
		cloudMapper.put(IpcPricingConstants.MANAGED_UNMANAGED, IpcPricingConstants.MANAGED);
		cloudMapper.put(IpcPricingConstants.SHEET, null);
		cloudMapper.put(IpcPricingConstants.VCPU, 0);
		cloudMapper.put(IpcPricingConstants.VRAM, 0);
		cloudMapper.put(IpcPricingConstants.SSDIOPS, 0);
		cloudMapper.put(IpcPricingConstants.MGMT_CHARGER_PER_VM, 0);
		cloudMapper.put(IpcPricingConstants.PER_GB_VRAM_ESXI, 0);
		cloudMapper.put(IpcPricingConstants.MS_LICENCE, 0);
		cloudMapper.put(IpcPricingConstants.RHEL_LICENCE_SMALL, 0);
		cloudMapper.put(IpcPricingConstants.RHEL_LICENCE_LARGE, 0);
		cloudMapper.put(IpcPricingConstants.SUSE_LICENSE_SMALL, 0);
		cloudMapper.put(IpcPricingConstants.SUSE_LICENSE_MEDIUM, 0);
		cloudMapper.put(IpcPricingConstants.SUSE_LICENSE_LARGE, 0);
		cloudMapper.put(IpcPricingConstants.OEL_LICENCE, 0);
		cloudMapper.put(IpcPricingConstants.SSD, 0);
		cloudMapper.put(IpcPricingConstants.SAS, 0);
		cloudMapper.put(IpcPricingConstants.SATA, 0);
		cloudMapper.put(IpcPricingConstants.BASE_VDOM, 0);
	}

	private boolean validateCloudMapper(Map<String, Object> cloudMapper) {
		return !((Integer) cloudMapper.get(IpcPricingConstants.MGMT_CHARGER_PER_VM) == 0
				&& (Integer) cloudMapper.get(IpcPricingConstants.BASE_VDOM) == 0
				&& (Integer) cloudMapper.get(IpcPricingConstants.PER_MB) == 0);
	}

	private void enrichQuote(Quote quote) throws TclCommonException {
		quote.setCountry(getDcLocation(quote.getRegion()).getCountryCode());

		Double customerNetMarginPercentage = 0.0;
		if (quote.getAdditionalDiscountPercentage() == null) {
			customerNetMarginPercentage = getCustomerNetMarginPercentage(quote.getRegion(), quote.getCustomerId());
		}

		quote.setAdditionalDiscountPercentage(getAdditionalDiscountPercentage(quote.getAdditionalDiscountPercentage(),
				quote.getDelegationDiscountPercentage(), customerNetMarginPercentage));

		quote.setDelegationDiscountPercentage(
				getDelegationDiscountPercentage(quote.getDelegationDiscountPercentage(), customerNetMarginPercentage));

		quote.setVolumeDiscountPercentage(getVolumeDiscountPercentage(quote.getCloudvm().size()));

		quote.setTermDiscountPercentage(getTermDiscountPercentage(quote.getTerm()));

		if (quote.getPartnerProfileId() != null) {
			updatePartnerCommissionPercentage(quote);
		}
	}

	private void enrichCloudMapper(Quote quote, Map<String, Object> cloudMapper) {
		cloudMapper.put(IpcPricingConstants.SHEET, quote.getCountry());
		cloudMapper.put(IpcPricingConstants.TERM_IN_MONTHS, quote.getTerm());
		cloudMapper.put(IpcPricingConstants.ADDITIONAL_DISCOUNT_PERCENTAGE, quote.getAdditionalDiscountPercentage());

		for (Cloudvm cloudVm : quote.getCloudvm()) {
			enrichCloudMapperForVMBasics(cloudVm, cloudMapper);

			enrichCloudMapperForOS(cloudVm, cloudMapper);

			enrichCloudMapperForAdditionalStorage(cloudVm, cloudMapper);
		}

		if (quote.getAddon() != null) {
			for (Addon addon : quote.getAddon()) {
				enrichCloudMapperForAddons(addon, cloudMapper);
			}
		}

		cloudMapper.put(IpcPricingConstants.MGMT_CHARGER_PER_VM, quote.getCloudvm().size());
		cloudMapper.put(IpcPricingConstants.MANAGED_UNMANAGED,
				(quote.getManagementEnabled() ? IpcPricingConstants.MANAGED : IpcPricingConstants.UNMANAGED));
	}

	private void enrichCloudMapperForAddons(Addon addon, Map<String, Object> cloudMapper) {
		if (addon.getVdomcount() != null) {
			cloudMapper.put(IpcPricingConstants.BASE_VDOM,
					(Integer) cloudMapper.get(IpcPricingConstants.BASE_VDOM) + addon.getVdomcount());
		}
	}

	private void enrichCloudMapperForVMBasics(Cloudvm cloudVm, Map<String, Object> cloudMapper) {
		cloudMapper.put(IpcPricingConstants.VCPU,
				getIncrementedValue((Integer) cloudMapper.get(IpcPricingConstants.VCPU), cloudVm.getVcpu()));
		cloudMapper.put(IpcPricingConstants.VRAM,
				getIncrementedValue((Integer) cloudMapper.get(IpcPricingConstants.VRAM), cloudVm.getVram()));
		cloudMapper.put(IpcPricingConstants.SSDIOPS, getIncrementedValue(
				(Integer) cloudMapper.get(IpcPricingConstants.SSDIOPS), cloudVm.getPerGBAdditionalIOPSForSSD()));

		if (cloudVm.getRootStorage() != null
				&& IpcPricingConstants.SSD.equalsIgnoreCase(cloudVm.getRootStorage().getType())) {
			cloudMapper.put(IpcPricingConstants.SSD, getIncrementedValue(
					(Integer) cloudMapper.get(IpcPricingConstants.SSD), cloudVm.getRootStorage().getSize()));
		}

		if (StringUtils.isNotBlank(cloudVm.getHypervisor())
				&& cloudVm.getHypervisor().contains(IpcPricingConstants.ESXI)) {
			Integer maxRAM = Integer.valueOf(cloudVm.getVram()) < 24 ? Integer.valueOf(cloudVm.getVram()) : 24;
			cloudMapper.put(IpcPricingConstants.PER_GB_VRAM_ESXI,
					getIncrementedValue((Integer) cloudMapper.get(IpcPricingConstants.PER_GB_VRAM_ESXI), maxRAM));
		}
	}

	private void enrichCloudMapperForOS(Cloudvm cloudVm, Map<String, Object> cloudMapper) {
		if (IpcPricingConstants.WINDOWS.equalsIgnoreCase(cloudVm.getOs())) {
			cloudMapper.put(IpcPricingConstants.MS_LICENCE,
					getIncrementedValue((Integer) cloudMapper.get(IpcPricingConstants.MS_LICENCE), 1));
		} else if (IpcPricingConstants.RHEL.equalsIgnoreCase(cloudVm.getOs())) {
			if (Integer.valueOf(cloudVm.getVcpu()) <= 4) {
				cloudMapper.put(IpcPricingConstants.RHEL_LICENCE_SMALL,
						getIncrementedValue((Integer) cloudMapper.get(IpcPricingConstants.RHEL_LICENCE_SMALL), 1));
			} else {
				cloudMapper.put(IpcPricingConstants.RHEL_LICENCE_LARGE,
						getIncrementedValue((Integer) cloudMapper.get(IpcPricingConstants.RHEL_LICENCE_LARGE), 1));
			}
		} else if (IpcPricingConstants.SUSE.equalsIgnoreCase(cloudVm.getOs())) {
			if (Integer.valueOf(cloudVm.getVcpu()) <= 2) {
				cloudMapper.put(IpcPricingConstants.SUSE_LICENSE_SMALL,
						getIncrementedValue((Integer) cloudMapper.get(IpcPricingConstants.SUSE_LICENSE_SMALL), 1));
			} else if (Integer.valueOf(cloudVm.getVcpu()) <= 4) {
				cloudMapper.put(IpcPricingConstants.SUSE_LICENSE_MEDIUM,
						getIncrementedValue((Integer) cloudMapper.get(IpcPricingConstants.SUSE_LICENSE_MEDIUM), 1));
			} else {
				cloudMapper.put(IpcPricingConstants.SUSE_LICENSE_LARGE,
						getIncrementedValue((Integer) cloudMapper.get(IpcPricingConstants.SUSE_LICENSE_LARGE), 1));
			}
		} else if (IpcPricingConstants.OEL.equalsIgnoreCase(cloudVm.getOs())) {
			cloudMapper.put(IpcPricingConstants.OEL_LICENCE,
					getIncrementedValue((Integer) cloudMapper.get(IpcPricingConstants.OEL_LICENCE), 1));
		}
	}

	private void enrichCloudMapperForAdditionalStorage(Cloudvm cloudVm, Map<String, Object> cloudMapper) {
		if (cloudVm.getAdditionalStorages() != null) {
			for (AdditionalStorage additionalStorage : cloudVm.getAdditionalStorages()) {
				if (IpcPricingConstants.SSD.equalsIgnoreCase(additionalStorage.getType())) {
					cloudMapper.put(IpcPricingConstants.SSD, getIncrementedValue(
							(Integer) cloudMapper.get(IpcPricingConstants.SSD), additionalStorage.getSize()));
				} else if (IpcPricingConstants.SAS.equalsIgnoreCase(additionalStorage.getType())) {
					cloudMapper.put(IpcPricingConstants.SAS, getIncrementedValue(
							(Integer) cloudMapper.get(IpcPricingConstants.SAS), additionalStorage.getSize()));
				} else if (IpcPricingConstants.SATA.equalsIgnoreCase(additionalStorage.getType())) {
					cloudMapper.put(IpcPricingConstants.SATA, getIncrementedValue(
							(Integer) cloudMapper.get(IpcPricingConstants.SATA), additionalStorage.getSize()));
				}
			}
		}
	}

	private Integer getIncrementedValue(Integer existingValue, String additionalValue) {
		Integer incrementedValue = existingValue;
		if (StringUtils.isNotBlank(additionalValue) && StringUtils.isNumeric(additionalValue)) {
			incrementedValue = incrementedValue + Integer.valueOf(additionalValue);
		}
		return incrementedValue;
	}

	private Integer getIncrementedValue(Integer existingValue, Integer additionalValue) {
		return existingValue + additionalValue;
	}

	private PricingIpcLocation getDcLocation(String region) throws TclCommonException {
		List<PricingIpcLocation> ipcLocationsList = pricingIpcLocationRepository.findByCityCode(region);
		Optional<PricingIpcLocation> dcLocation = ipcLocationsList.stream().findFirst();
		if (!dcLocation.isPresent()) {
			throw new TclCommonException(IpcPricingConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
		}
		return dcLocation.get();
	}

	private Double getVolumeDiscountPercentage(int numOfVms) {
		Double volumeDiscountPercentage = 0.0;

		if (numOfVms >= 20 && numOfVms <= 50) {
			volumeDiscountPercentage = 5.0;
		} else if (numOfVms > 50) {
			volumeDiscountPercentage = 10.0;
		}

		LOGGER.info("Volume Discount Percentage for {} VMs is {} %", numOfVms, volumeDiscountPercentage);

		return volumeDiscountPercentage;
	}

	private Double getTermDiscountPercentage(int termInMonths) {
		Double termDiscountPercentage = 0.0;

		if (termInMonths < 12) {
			termDiscountPercentage = 3.0;
		} else if (termInMonths >= 12 && termInMonths < 24) {
			termDiscountPercentage = 5.0;
		} else if (termInMonths >= 24 && termInMonths < 36) {
			termDiscountPercentage = 7.5;
		} else if (termInMonths > 36) {
			termDiscountPercentage = 10.0;
		}

		LOGGER.info("Term Discount Percentage for {} Months is {} %", termInMonths, termDiscountPercentage);

		return termDiscountPercentage;
	}

	private Double getCustomerNetMarginPercentage(String region, int customerId) {
		Double customerNetMarginPercentage = 0.0;

		PricingIpcCustomerNetMargin ipcCustomerNetMargin = pricingIpcCustomerNetMarginRepository
				.getCustomerNetMargin(region, customerId);
		if (ipcCustomerNetMargin != null && ipcCustomerNetMargin.getNetMarginPercentage() != null) {
			customerNetMarginPercentage = ipcCustomerNetMargin.getNetMarginPercentage();
		}

		LOGGER.info("Customer Net Margin Percentage for Customer ID: {} and Region: {} is {} %", customerId, region,
				customerNetMarginPercentage);

		return customerNetMarginPercentage;
	}

	private Double getAdditionalDiscountPercentage(Double givenAdditionalDiscountPercentage,
			Double givenDelegationDiscountPercentage, Double customerNetMarginPercentage) {
		Double additionalDiscountPercentage = 0.0;

		if (givenDelegationDiscountPercentage == null) {
			givenDelegationDiscountPercentage = 0.0;
		}

		if (givenAdditionalDiscountPercentage != null && givenAdditionalDiscountPercentage != 0.0) {
			if (givenAdditionalDiscountPercentage > givenDelegationDiscountPercentage) {
				additionalDiscountPercentage = givenAdditionalDiscountPercentage - givenDelegationDiscountPercentage;
			}
		} else {
			additionalDiscountPercentage = customerNetMarginPercentage;
		}

		LOGGER.info("Additional Discount Percentage is {} %", additionalDiscountPercentage);

		return additionalDiscountPercentage;
	}

	private Double getDelegationDiscountPercentage(Double givenDelegationDiscountPercentage,
			Double customerNetMarginPercentage) {
		Double delegationDiscountPercentage = 0.0;

		if (customerNetMarginPercentage == 0.0) {
			delegationDiscountPercentage = 7.0;
		} else {
			delegationDiscountPercentage = givenDelegationDiscountPercentage;
		}

		LOGGER.info("Delegation Discount Percentage is {} %", delegationDiscountPercentage);

		return delegationDiscountPercentage;
	}

	private void updatePartnerCommissionPercentage(Quote quote) {
		double partnerCommissionPercentage = 0.0;
		double dealRegistrationCommissionPercentage = 0.0;
		double partnerMultiYearCommissionPercentage = 0.0;

		PricingIpcPartnerCommission commission = pricingIpcPartnerCommissionRespository
				.findByProfileId(quote.getPartnerProfileId());

		if (IpcPricingConstants.INDIA.equalsIgnoreCase(quote.getCountry())) {
			if (quote.getTerm() > 12) {
				partnerMultiYearCommissionPercentage = commission.getIndMultiYearCommission();
			}
			if (quote.getIsDealRegistration()) {
				dealRegistrationCommissionPercentage = commission.getIndDealRegCommission();
			}
			partnerCommissionPercentage = commission.getIndBaseCommission() + partnerMultiYearCommissionPercentage
					+ dealRegistrationCommissionPercentage;
		} else {
			if (quote.getTerm() > 12) {
				partnerMultiYearCommissionPercentage = commission.getIntlMultiYearCommission();
			}
			partnerCommissionPercentage = commission.getIntlBaseCommission() + partnerMultiYearCommissionPercentage;
		}
		quote.setPartnerCommissionPercentage(partnerCommissionPercentage);
		quote.setDealRegistrationCommissionPercentage(dealRegistrationCommissionPercentage);
		quote.setMultiYearCommissionPercentage(partnerMultiYearCommissionPercentage);

		LOGGER.info("Partner Commission Percentage for Profile ID {} is {} % , {} % , {} %",
				quote.getPartnerProfileId(), partnerCommissionPercentage, dealRegistrationCommissionPercentage,
				partnerMultiYearCommissionPercentage);
	}

	@SuppressWarnings("unchecked")
	private JSONObject getVmPricingFromRateCard(Map<String, Object> cloudMapper, Quote quote) {
		JSONObject vmPricing = new JSONObject();

		try {
			List<PricingIpcRateCard> pricingRateCard = pricingIpcRateCardRepository
					.findByCustomerAndRegionAndCountry(quote.getCustomerId(), quote.getRegion(), quote.getCountry());

			JSONObject details = new JSONObject();
			JSONObject uom = new JSONObject();
			JSONObject listPrice = new JSONObject();
			JSONObject salesPricePerUnit = new JSONObject();

			pricingRateCard.forEach(pricingRate -> {
				PricingIpcItem ipcItem = pricingRate.getItem();
				String index = String.valueOf(ipcItem.getId() - 1);
				Double itemPrice = pricingRate.getPrice();

				details.put(index, ipcItem.getName());
				uom.put(index, ipcItem.getUom());
				listPrice.put(index, itemPrice);

				Double volumeDiscountRate = 1.0;
				Double termDiscountRate = 1.0;

				if (ipcItem.getIsVolumeDiscountApplicable() == 1) {
					volumeDiscountRate = 1 - quote.getVolumeDiscountPercentage() / 100;
				}

				if (ipcItem.getIsTermDiscountApplicable() == 1) {
					termDiscountRate = 1 - quote.getTermDiscountPercentage() / 100;
				}

				salesPricePerUnit.put(index, (itemPrice * volumeDiscountRate) * termDiscountRate);
			});

			JSONObject computed = new JSONObject();
			computed.put("Details", details);
			computed.put("UoM", uom);
			computed.put("List Price", listPrice);
			computed.put("Sale Price Per Unit", salesPricePerUnit);

			vmPricing.put("computed", computed);
			vmPricing.put("volumeDiscount", quote.getVolumeDiscountPercentage());
			vmPricing.put("delegationDiscount", quote.getDelegationDiscountPercentage());
			vmPricing.put("termDiscount", quote.getTermDiscountPercentage());
			vmPricing.put("additionalDiscount", 0.0);

			List<PricingIpcRateCardAttribute> pricingRateCardAttributes = pricingIpcRateCardAttributesRepository
					.findByCustomerAndRegionAndCountry(quote.getCustomerId(), quote.getRegion(), quote.getCountry());

			JSONObject attributes = new JSONObject();
			pricingRateCardAttributes.forEach(pricingRateAttribute -> {
				PricingIpcAttribute ipcAttribute = pricingRateAttribute.getAttribute();
				attributes.put(ipcAttribute.getName(), pricingRateAttribute.getValue());
			});

			vmPricing.put("attributes", attributes);
		} catch (Exception e) {
			JSONObject error = new JSONObject();
			error.put("error", e);
			return error;
		}

		return vmPricing;
	}

	private Map<String, Map<String, Object>> getSalePriceMapper(JSONObject data) {
		Map<String, Map<String, Object>> salePriceMapper = new HashMap<>();
		Map<String, Map<String, Object>> computedMapper = getComputedSalePriceResponseMapper(data);
		for (Entry<String, Map<String, Object>> computeMapper : computedMapper.entrySet()) {
			salePriceMapper.put(((String) computedMapper.get(computeMapper.getKey()).get("detail")).trim(),
					computedMapper.get(computeMapper.getKey()));
		}
		return salePriceMapper;
	}

	private Map<String, String> getPricingAttributeMapper(JSONObject data) {
		Map<String, String> attributeMapper = new HashMap<>();
		JSONObject attributes = (JSONObject) data.get("attributes");
		for (Object key : attributes.keySet()) {
			attributeMapper.put((String) key, (String) attributes.get(key));
		}
		return attributeMapper;
	}

	private Map<String, Map<String, Object>> getComputedSalePriceResponseMapper(JSONObject data) {
		Map<String, Map<String, Object>> computedMapper = new HashMap<>();
		JSONObject computedObj = (JSONObject) data.get("computed");
		JSONObject detailsObj = (JSONObject) computedObj.get("Details");
		for (Object key : detailsObj.keySet()) {
			Map<String, Object> valueMapper = new HashMap<>();
			valueMapper.put("detail", detailsObj.get(key));
			computedMapper.put((String) key, valueMapper);
		}
		JSONObject salePricePerUnitObj = (JSONObject) computedObj.get("Sale Price Per Unit");
		for (Object key : salePricePerUnitObj.keySet()) {
			computedMapper.get((String) key).put("salePricePerUnit", salePricePerUnitObj.get(key));
		}
		JSONObject listPricePerUnitObj = (JSONObject) computedObj.get("List Price");
		for (Object key : listPricePerUnitObj.keySet()) {
			computedMapper.get((String) key).put("listPricePerUnit", listPricePerUnitObj.get(key));
		}
		return computedMapper;
	}

	private void calculatePricingForVMs(Quote quote, Map<String, Map<String, Object>> salePriceMapper,
			Map<String, String> attributeMapper) {
		for (Cloudvm cloudVm : quote.getCloudvm()) {
			Double netVmChargeWithoutAdditionalStorage = getTotalVmPriceWithoutAdditionalStorage(salePriceMapper,
					cloudVm, quote.getManagementEnabled(), quote.getRegion());
			LOGGER.info("Total Charges without Additional Storage for Item Number {}, is {}", cloudVm.getItemId(),
					netVmChargeWithoutAdditionalStorage);

			Double additionalStorageCostForVm = getAdditionalStorageCost(salePriceMapper, cloudVm);

			if (cloudVm.getPricingModel() == null
					|| IpcPricingConstants.RESERVED_VM.equalsIgnoreCase(cloudVm.getPricingModel())) {
				Double netVmCharge = netVmChargeWithoutAdditionalStorage + additionalStorageCostForVm;
				Double cloudVmDisc = netVmCharge
						- ((netVmCharge * quote.getAdditionalDiscountPercentage()) / Double.valueOf(100));
				cloudVm.setMrc(IpcUtils.roundOff2(cloudVmDisc));
				cloudVm.setPpuRate(0D);
				LOGGER.info("VM Pricing Details: [ID: {}, Pricing Model: {}, MRC: {}]", cloudVm.getItemId(),
						cloudVm.getPricingModel(), cloudVm.getMrc());
			} else {
				if (IpcPricingConstants.PPU_VM_MONTHLY.equalsIgnoreCase(cloudVm.getPricingModel())) {
					Double ppuRate = netVmChargeWithoutAdditionalStorage * Double.parseDouble(attributeMapper
							.getOrDefault(IpcPricingConstants.PRICING_ATTRIBUTE_PPU_VM_MONTHLY_PREMIUM_RATE, "1"));
					cloudVm.setPpuRate(IpcUtils.roundOff2(ppuRate));
					cloudVm.setMrc(IpcUtils.roundOff2(additionalStorageCostForVm));
				} else if (IpcPricingConstants.PPU_VM_DAILY.equalsIgnoreCase(cloudVm.getPricingModel())) {
					Double ppuRate = (netVmChargeWithoutAdditionalStorage * Double.parseDouble(attributeMapper
							.getOrDefault(IpcPricingConstants.PRICING_ATTRIBUTE_PPU_VM_DAILY_PREMIUM_RATE, "1"))) / 30;
					cloudVm.setPpuRate(IpcUtils.roundOff2(ppuRate));
					cloudVm.setMrc(IpcUtils.roundOff2(additionalStorageCostForVm));
				} else if (IpcPricingConstants.PPU_VM_HOURLY.equalsIgnoreCase(cloudVm.getPricingModel())) {
					Double ppuRate = (netVmChargeWithoutAdditionalStorage * Double.parseDouble(attributeMapper
							.getOrDefault(IpcPricingConstants.PRICING_ATTRIBUTE_PPU_VM_HOURLY_PREMIUM_RATE, "1")))
							/ 720;
					cloudVm.setPpuRate(IpcUtils.roundOff2(ppuRate));
					cloudVm.setMrc(IpcUtils.roundOff2(additionalStorageCostForVm));
				} else {
					cloudVm.setPpuRate(0D);
					cloudVm.setMrc(
							IpcUtils.roundOff2(netVmChargeWithoutAdditionalStorage + additionalStorageCostForVm));
				}
				LOGGER.info("VM Pricing Details: [ID: {}, Pricing Model: {}, MRC: {}, PPU Rate: {}]",
						cloudVm.getItemId(), cloudVm.getPricingModel(), cloudVm.getMrc(), cloudVm.getPpuRate());
			}
			cloudVm.setNrc(0D);
		}
	}

	private Double getTotalVmPriceWithoutAdditionalStorage(Map<String, Map<String, Object>> salePriceMapper,
			Cloudvm cloudVm, Boolean isManaged, String dcRegion) {
		Double vCpuUnitPrice = getSalePricePerUnit(salePriceMapper, IpcPricingConstants.PRICING_ITEM_PER_CPU);
		Double vRamUnitPrice = getSalePricePerUnit(salePriceMapper, IpcPricingConstants.PRICING_ITEM_PER_GB_RAM);
		Double additionalIOPSUnitPrice = getSalePricePerUnit(salePriceMapper,
				IpcPricingConstants.PRICING_ITEM_PER_GB_SSD_IOPS);
		Double ssdUnitPrice = getSalePricePerUnit(salePriceMapper, IpcPricingConstants.PRICING_ITEM_PER_GB_SSD);
		Double sataUnitPrice = getSalePricePerUnit(salePriceMapper, IpcPricingConstants.PRICING_ITEM_PER_GB_HDD_SATA);
		Double sasUnitPrice = getSalePricePerUnit(salePriceMapper, IpcPricingConstants.PRICING_ITEM_PER_GB_HDD_SAS);
		Double managementChargesUnitPrice = isManaged
				? getSalePricePerUnit(salePriceMapper, IpcPricingConstants.PRICING_ITEM_PER_VM_MGMT)
				: 0D;
		Double esxUnitPrice = getSalePricePerUnit(salePriceMapper, IpcPricingConstants.PRICING_ITEM_PER_GB_RAM_ESXI);
		Double msLicenceUnitPrice = getSalePricePerUnit(salePriceMapper,
				IpcPricingConstants.PRICING_ITEM_PER_MS_LICENSE);
		Double oelLicenceUnitPrice = getSalePricePerUnit(salePriceMapper,
				IpcPricingConstants.PRICING_ITEM_PER_OEL_LICENSE);
		Double rhelLicenseSmallUnitPrice = getSalePricePerUnit(salePriceMapper,
				IpcPricingConstants.PRICING_ITEM_PER_RHEL_LICENSE_SMALL);
		Double rhelLicenseLargeUnitPrice = getSalePricePerUnit(salePriceMapper,
				IpcPricingConstants.PRICING_ITEM_PER_RHEL_LICENSE_LARGE);
		Double suseLicenseSmallUnitPrice = getSalePricePerUnit(salePriceMapper,
				IpcPricingConstants.PRICING_ITEM_PER_SUSE_LICENSE_SMALL);
		Double suseLicenseMediumUnitPrice = getSalePricePerUnit(salePriceMapper,
				IpcPricingConstants.PRICING_ITEM_PER_SUSE_LICENSE_MEDIUM);
		Double suseLicenseLargeUnitPrice = getSalePricePerUnit(salePriceMapper,
				IpcPricingConstants.PRICING_ITEM_PER_SUSE_LICENSE_LARGE);

		// For SAP HANA
		if (dcRegion != null && dcRegion.endsWith("_HANA")) {
			vCpuUnitPrice = getSalePricePerUnit(salePriceMapper, IpcPricingConstants.PRICING_ITEM_PER_CPU_FOR_SAP);
			vRamUnitPrice = getSalePricePerUnit(salePriceMapper, IpcPricingConstants.PRICING_ITEM_PER_GB_RAM_FOR_SAP);
			rhelLicenseSmallUnitPrice = getSalePricePerUnit(salePriceMapper,
					IpcPricingConstants.PRICING_ITEM_PER_RHEL_LICENSE_SMALL_FOR_SAP);
			rhelLicenseLargeUnitPrice = getSalePricePerUnit(salePriceMapper,
					IpcPricingConstants.PRICING_ITEM_PER_RHEL_LICENSE_LARGE_FOR_SAP);
			suseLicenseSmallUnitPrice = getSalePricePerUnit(salePriceMapper,
					IpcPricingConstants.PRICING_ITEM_PER_SUSE_LICENSE_SMALL_FOR_SAP);
			suseLicenseMediumUnitPrice = getSalePricePerUnit(salePriceMapper,
					IpcPricingConstants.PRICING_ITEM_PER_SUSE_LICENSE_MEDIUM_FOR_SAP);
			suseLicenseLargeUnitPrice = getSalePricePerUnit(salePriceMapper,
					IpcPricingConstants.PRICING_ITEM_PER_SUSE_LICENSE_LARGE_FOR_SAP);
		}

		// For PPU VMs, there is no Term / Volume / Delegation discount
		if (!IpcPricingConstants.RESERVED_VM.equalsIgnoreCase(cloudVm.getPricingModel())) {
			vCpuUnitPrice = getListPricePerUnit(salePriceMapper, IpcPricingConstants.PRICING_ITEM_PER_CPU);
			vRamUnitPrice = getListPricePerUnit(salePriceMapper, IpcPricingConstants.PRICING_ITEM_PER_GB_RAM);
			additionalIOPSUnitPrice = getListPricePerUnit(salePriceMapper,
					IpcPricingConstants.PRICING_ITEM_PER_GB_SSD_IOPS);
			ssdUnitPrice = getListPricePerUnit(salePriceMapper, IpcPricingConstants.PRICING_ITEM_PER_GB_SSD);
			sataUnitPrice = getListPricePerUnit(salePriceMapper, IpcPricingConstants.PRICING_ITEM_PER_GB_HDD_SATA);
			sasUnitPrice = getListPricePerUnit(salePriceMapper, IpcPricingConstants.PRICING_ITEM_PER_GB_HDD_SAS);
			managementChargesUnitPrice = isManaged
					? getListPricePerUnit(salePriceMapper, IpcPricingConstants.PRICING_ITEM_PER_VM_MGMT)
					: 0D;
			esxUnitPrice = getListPricePerUnit(salePriceMapper, IpcPricingConstants.PRICING_ITEM_PER_GB_RAM_ESXI);
			msLicenceUnitPrice = getListPricePerUnit(salePriceMapper, IpcPricingConstants.PRICING_ITEM_PER_MS_LICENSE);
			oelLicenceUnitPrice = getListPricePerUnit(salePriceMapper,
					IpcPricingConstants.PRICING_ITEM_PER_OEL_LICENSE);
			rhelLicenseSmallUnitPrice = getListPricePerUnit(salePriceMapper,
					IpcPricingConstants.PRICING_ITEM_PER_RHEL_LICENSE_SMALL);
			rhelLicenseLargeUnitPrice = getListPricePerUnit(salePriceMapper,
					IpcPricingConstants.PRICING_ITEM_PER_RHEL_LICENSE_LARGE);
			suseLicenseSmallUnitPrice = getListPricePerUnit(salePriceMapper,
					IpcPricingConstants.PRICING_ITEM_PER_SUSE_LICENSE_SMALL);
			suseLicenseMediumUnitPrice = getListPricePerUnit(salePriceMapper,
					IpcPricingConstants.PRICING_ITEM_PER_SUSE_LICENSE_MEDIUM);
			suseLicenseLargeUnitPrice = getListPricePerUnit(salePriceMapper,
					IpcPricingConstants.PRICING_ITEM_PER_SUSE_LICENSE_LARGE);

			// For SAP HANA
			if (dcRegion != null && dcRegion.endsWith("_HANA")) {
				vCpuUnitPrice = getListPricePerUnit(salePriceMapper, IpcPricingConstants.PRICING_ITEM_PER_CPU_FOR_SAP);
				vRamUnitPrice = getListPricePerUnit(salePriceMapper,
						IpcPricingConstants.PRICING_ITEM_PER_GB_RAM_FOR_SAP);
				rhelLicenseSmallUnitPrice = getListPricePerUnit(salePriceMapper,
						IpcPricingConstants.PRICING_ITEM_PER_RHEL_LICENSE_SMALL_FOR_SAP);
				rhelLicenseLargeUnitPrice = getListPricePerUnit(salePriceMapper,
						IpcPricingConstants.PRICING_ITEM_PER_RHEL_LICENSE_LARGE_FOR_SAP);
				suseLicenseSmallUnitPrice = getListPricePerUnit(salePriceMapper,
						IpcPricingConstants.PRICING_ITEM_PER_SUSE_LICENSE_SMALL_FOR_SAP);
				suseLicenseMediumUnitPrice = getListPricePerUnit(salePriceMapper,
						IpcPricingConstants.PRICING_ITEM_PER_SUSE_LICENSE_MEDIUM_FOR_SAP);
				suseLicenseLargeUnitPrice = getListPricePerUnit(salePriceMapper,
						IpcPricingConstants.PRICING_ITEM_PER_SUSE_LICENSE_LARGE_FOR_SAP);
			}
		}

		Double rhelLicenseUnitPrice;
		if (Integer.valueOf(cloudVm.getVcpu()) <= 8) {
			rhelLicenseUnitPrice = rhelLicenseSmallUnitPrice;
		} else {
			rhelLicenseUnitPrice = rhelLicenseLargeUnitPrice;
		}

		Double suseLicenseUnitPrice;
		if (Integer.valueOf(cloudVm.getVcpu()) <= 2) {
			suseLicenseUnitPrice = suseLicenseSmallUnitPrice;
		} else if (Integer.valueOf(cloudVm.getVcpu()) <= 4) {
			suseLicenseUnitPrice = suseLicenseMediumUnitPrice;
		} else {
			suseLicenseUnitPrice = suseLicenseLargeUnitPrice;
		}

		Integer totalSsd = 0;
		Integer totalSas = 0;
		Integer totalSata = 0;
		Integer totalEsxi = 0;
		if (cloudVm.getRootStorage() != null
				&& IpcPricingConstants.SSD.equalsIgnoreCase(cloudVm.getRootStorage().getType())) {
			totalSsd = totalSsd + Integer.valueOf(cloudVm.getRootStorage().getSize());
		}
		if (cloudVm.getHypervisor() != null && cloudVm.getHypervisor().contains(IpcPricingConstants.ESXI)) {
			totalEsxi = Integer.valueOf(cloudVm.getVram()) < 24 ? Integer.valueOf(cloudVm.getVram()) : 24;
		}

		Integer windowsLicenceCount = 0;
		Integer rhelLicenceCount = 0;
		Integer oelLicenceCount = 0;
		Integer suseLicenceCount = 0;
		if (IpcPricingConstants.WINDOWS.equalsIgnoreCase(cloudVm.getOs())) {
			windowsLicenceCount++;
		} else if (IpcPricingConstants.RHEL.equalsIgnoreCase(cloudVm.getOs())) {
			rhelLicenceCount++;
		} else if (IpcPricingConstants.OEL.equalsIgnoreCase(cloudVm.getOs())) {
			oelLicenceCount++;
		} else if (IpcPricingConstants.SUSE.equalsIgnoreCase(cloudVm.getOs())) {
			suseLicenceCount++;
		}

		Double vCpuPrice = Double.valueOf(cloudVm.getVcpu()) * vCpuUnitPrice;
		Double vRamPrice = Double.valueOf(cloudVm.getVram()) * vRamUnitPrice;
		Double additionalIOPSPrice = Double.valueOf(cloudVm.getPerGBAdditionalIOPSForSSD()) * additionalIOPSUnitPrice;
		Double ssdPrice = Double.valueOf(totalSsd) * ssdUnitPrice;
		Double sataPrice = Double.valueOf(totalSata) * sataUnitPrice;
		Double sasPrice = Double.valueOf(totalSas) * sasUnitPrice;
		Double managementCharges = managementChargesUnitPrice;
		Double esxiCharges = Double.valueOf(totalEsxi) * esxUnitPrice;
		Double windowsCharges = Double.valueOf(windowsLicenceCount) * Double.valueOf(cloudVm.getVcpu())
				* msLicenceUnitPrice;
		Double rhelCharges = Double.valueOf(rhelLicenceCount) * rhelLicenseUnitPrice;
		Double oelCharges = Double.valueOf(oelLicenceCount) * oelLicenceUnitPrice;
		Double suseCharges = Double.valueOf(suseLicenceCount) * suseLicenseUnitPrice;

		return vCpuPrice + vRamPrice + additionalIOPSPrice + ssdPrice + sataPrice + sasPrice + managementCharges
				+ esxiCharges + windowsCharges + rhelCharges + suseCharges + oelCharges;
	}

	private Double getAdditionalStorageCost(Map<String, Map<String, Object>> salePriceMapper, Cloudvm cloudVm) {
		Double ssdUnitPrice = getSalePricePerUnit(salePriceMapper, IpcPricingConstants.PRICING_ITEM_PER_GB_SSD);
		Double sataUnitPrice = getSalePricePerUnit(salePriceMapper, IpcPricingConstants.PRICING_ITEM_PER_GB_HDD_SATA);
		Double sasUnitPrice = getSalePricePerUnit(salePriceMapper, IpcPricingConstants.PRICING_ITEM_PER_GB_HDD_SAS);

		Integer totalSsd = 0;
		Integer totalSas = 0;
		Integer totalSata = 0;
		if (cloudVm.getAdditionalStorages() != null) {
			for (AdditionalStorage additionalStorage : cloudVm.getAdditionalStorages()) {
				if (IpcPricingConstants.SSD.equalsIgnoreCase(additionalStorage.getType())) {
					totalSsd = totalSsd + Integer.valueOf(additionalStorage.getSize());
				} else if (IpcPricingConstants.SAS.equalsIgnoreCase(additionalStorage.getType())) {
					totalSas = totalSas + Integer.valueOf(additionalStorage.getSize());
				} else if (IpcPricingConstants.SATA.equalsIgnoreCase(additionalStorage.getType())) {
					totalSata = totalSata + Integer.valueOf(additionalStorage.getSize());
				}
			}
		}
		Double ssdPrice = Double.valueOf(totalSsd) * ssdUnitPrice;
		Double sataPrice = Double.valueOf(totalSata) * sataUnitPrice;
		Double sasPrice = Double.valueOf(totalSas) * sasUnitPrice;

		return ssdPrice + sataPrice + sasPrice;
	}

	private Double getSalePricePerUnit(Map<String, Map<String, Object>> salePriceMapper, String componentName) {
		Double salePricePerUnit = 0D;
		if (salePriceMapper.containsKey(componentName)) {
			Map<String, Object> componentPriceDetails = salePriceMapper.get(componentName);
			if (componentPriceDetails.containsKey("salePricePerUnit")) {
				salePricePerUnit = (Double) componentPriceDetails.get("salePricePerUnit");
			}
		}
		return salePricePerUnit;
	}

	private Double getListPricePerUnit(Map<String, Map<String, Object>> salePriceMapper, String componentName) {
		Double listPricePerUnit = 0D;
		if (salePriceMapper.containsKey(componentName)) {
			Map<String, Object> componentPriceDetails = salePriceMapper.get(componentName);
			if (componentPriceDetails.containsKey("listPricePerUnit")) {
				listPricePerUnit = (Double) componentPriceDetails.get("listPricePerUnit");
			}
		}
		return listPricePerUnit;
	}

	private void calculatePricingForAccess(Quote quote, Map<String, Object> cloudMapper) throws TclCommonException {
		for (Access access : quote.getAccess()) {
			if (access.getType().contains("data")) {
				LOGGER.info("Access Type = Data Transfer");
				Integer limit = Integer.valueOf(access.getLimit());

				PricingIpcDatatransfer pricingIpcDataTransfer = pricingIpcDatatransferRepository
						.findByLocationCodeAndStartAndEnd(quote.getCountry(), limit);

				if (pricingIpcDataTransfer != null) {
					Double totalPrice = IpcUtils.roundOff2(pricingIpcDataTransfer.getPrice() * limit);
					if (access.getAskedMrc() != null) {
						access.setMrc(access.getAskedMrc());
					} else {
						access.setMrc(totalPrice);
					}
					access.setNrc(0.0);
					LOGGER.info("MRC of Access is {}", access.getMrc());

					Integer bw = pricingIpcDataTransfer.getBandwidth();
					if (bw > 2) {
						Integer perMb = bw - 2;
						cloudMapper.put(IpcPricingConstants.PER_MB, perMb);
						LOGGER.info("Per MB is updated to {}", perMb);
					}
				} else {
					access.setMrc(0D);
					access.setNrc(0D);
				}
			} else {
				LOGGER.info("Access Type = Fixed Bandwidth");
				getPriceForPort(quote, access);
				Integer bw = Integer.valueOf(access.getLimit());
				if (bw > 2) {
					Integer perMb = bw - 2;
					cloudMapper.put(IpcPricingConstants.PER_MB, perMb);
					LOGGER.info("Per MB is updated to {}", perMb);
				}
			}
		}
	}

	private void getPriceForPort(Quote quote, Access access) throws TclCommonException {
		if (CommonConstants.EP_DUBAI.equals(quote.getRegion()) && access != null && "5".equals(access.getLimit())) {
			if (access.getAskedMrc() != null) {
				access.setMrc(access.getAskedMrc());
			} else {
				access.setMrc(1117D);
			}
			access.setNrc(0D);
		} else {
			PricingRequest rPricingRequest = constructPortPricingRequest(quote, null, access);
			String rPricingRequestStr = Utils.convertObjectToJson(rPricingRequest);
			LOGGER.info("Input request for R pricing {}", rPricingRequestStr);
			RestResponse rPricingResponse = restClientService.post(rPricingUrl, rPricingRequestStr);
			LOGGER.info("Output request for R pricing {}", rPricingResponse);
			if (rPricingResponse.getStatus() == Status.SUCCESS) {
				quote.setFixedBwpricingResponse(rPricingResponse.getData());
				PricingResponse rPricingResponseD = Utils.convertJsonToObject(rPricingResponse.getData(),
						PricingResponse.class);
				for (Result response : rPricingResponseD.getResults()) {
					double mrc = Double.valueOf(response.getiLLPortMRCAdjusted());
					if (access.getAskedMrc() != null) {
						access.setMrc(access.getAskedMrc());
					} else {
						access.setMrc(mrc);
					}
					access.setNrc(0D);
					break;
				}
			}
		}
	}

	private void calculatePricingForAddons(Quote quote, Map<String, Object> cloudMapper,
			Map<String, Map<String, Object>> salePriceMapper) {

		if (quote.getAddon() != null) {
			for (Addon addon : quote.getAddon()) {
				addon.setMrc(0.0D);
				addon.setNrc(0.0D);

				LOGGER.info("Initial Add-on MRC is {}", addon.getMrc());
				LOGGER.info("Initial Add-on NRC is {}", addon.getNrc());

				calculatePricingForVDOM(addon, quote, cloudMapper, salePriceMapper);

				calculatePricingForBackup(addon, quote, salePriceMapper);

				calculatePricingForAdditionalIPs(addon, salePriceMapper);

				calculatePricingForDatabase(addon, salePriceMapper);

				calculatePricingForDR(addon, salePriceMapper);

				calculatePricingForHybridConnections(addon, salePriceMapper);

				LOGGER.info("Final Add-on MRC is {}", addon.getMrc());
				LOGGER.info("Final Add-on NRC is {}", addon.getNrc());
			}
		}
	}

	private void calculatePricingForVDOM(Addon addon, Quote quote, Map<String, Object> cloudMapper,
			Map<String, Map<String, Object>> salePriceMapper) {
		Component component = addon.getPriceBreakup().get(IpcPricingConstants.VDOM);
		Integer perMBFromCloudMapper = (Integer) cloudMapper.get(IpcPricingConstants.PER_MB);

		if (Objects.nonNull(component)) {
			Double vdomUnitPrice = getListPricePerUnit(salePriceMapper, IpcPricingConstants.PRICING_ITEM_BASE_VDOM);
			Double perMbUnitPrice = getListPricePerUnit(salePriceMapper, IpcPricingConstants.PRICING_ITEM_PER_MB);

			Double vdomNetMrc = IpcUtils.roundOff2(addon.getVdomcount() * vdomUnitPrice);

			if (perMBFromCloudMapper != 0) {
				Double perMbNetMrc = perMBFromCloudMapper * perMbUnitPrice;
				vdomNetMrc = vdomNetMrc + perMbNetMrc;
			}

			Double vdomFinalMrc = vdomNetMrc - (vdomNetMrc * quote.getAdditionalDiscountPercentage() / 100);

			component.setMrc(IpcUtils.roundOff2(vdomFinalMrc));
			component.setNrc(0D);

			reCalculateAddonPricing(addon, component);
		}
	}

	private void calculatePricingForBackup(Addon addon, Quote quote, Map<String, Map<String, Object>> salePriceMapper) {
		Component component = addon.getPriceBreakup().get(IpcPricingConstants.BACKUP);

		if (Objects.nonNull(component)) {
			if (addon.getBackupsize() == null) {
				addon.setBackupsize("0");
			}

			Double commVaultUnitPrice = getSalePricePerUnit(salePriceMapper,
					IpcPricingConstants.PRICING_ITEM_PER_GB_COMMVAULT);
			Double commVaultNetMrc = commVaultUnitPrice * Integer.parseInt(addon.getBackupsize());
			Double commVaultFinalMrc = commVaultNetMrc
					- (commVaultNetMrc * quote.getAdditionalDiscountPercentage() / 100);

			Double icsPolicyFinalMrc = 0D;
			if (addon.getBackuplocation() != null) {
				Double icsPolicyUnitPrice = getSalePricePerUnit(salePriceMapper,
						IpcPricingConstants.PRICING_ITEM_PER_GB_ICS_GEO_RESILIENT_POLICY);
				if ("within same location".equalsIgnoreCase(addon.getBackuplocation())) {
					icsPolicyUnitPrice = getSalePricePerUnit(salePriceMapper,
							IpcPricingConstants.PRICING_ITEM_PER_GB_ICS_VALUE_BASED_POLICY);
				}

				Double icsPolicyNetMrc = icsPolicyUnitPrice * Integer.parseInt(addon.getBackupsize()) * 2.5;
				icsPolicyFinalMrc = icsPolicyNetMrc - (icsPolicyNetMrc * quote.getAdditionalDiscountPercentage() / 100);
			}

			Double backupFinalMrc = commVaultFinalMrc + icsPolicyFinalMrc;

			component.setMrc(IpcUtils.roundOff2(backupFinalMrc));
			component.setNrc(0D);

			reCalculateAddonPricing(addon, component);
		}
	}

	private void calculatePricingForAdditionalIPs(Addon addon, Map<String, Map<String, Object>> salePriceMapper) {
		Component component = addon.getPriceBreakup().get(IpcPricingConstants.ADDITIONAL_IP);

		if (Objects.nonNull(component)) {
			if (addon.getIpcount() == null) {
				addon.setIpcount(0);
			}

			Double ipUnitPrice = getListPricePerUnit(salePriceMapper, IpcPricingConstants.PRICING_ITEM_PER_PUBLIC_IPV4);
			Double ipFinalMrc = ipUnitPrice * addon.getIpcount();

			if (component.getAskedMrc() != null) {
				component.setMrc(component.getAskedMrc());
			} else {
				component.setMrc(IpcUtils.roundOff2(ipFinalMrc));
			}
			component.setNrc(0D);

			reCalculateAddonPricing(addon, component);
		}
	}

	private void calculatePricingForDatabase(Addon addon, Map<String, Map<String, Object>> salePriceMapper) {
		List<String> dbLicensesList = new ArrayList<String>(addon.getDbLicenseQuantity().keySet());
		for (String dbSolutionName : dbLicensesList) {
			String[] solutionNameArry = dbSolutionName.split("\\(");
			Double dbFinalMrc = 0D;
			if (solutionNameArry.length > 0) {
				Map<String, Integer> attributeMap = addon.getDbLicenseQuantity().get(dbSolutionName);
				Double dbUnitPrice = getListPricePerUnit(salePriceMapper, IpcPricingConstants.PRICING_ITEM_DB_PREFIX
						+ solutionNameArry[0].trim() + IpcPricingConstants.PRICING_ITEM_DB_UNMANAGED_SUFFIX);
				if (attributeMap.get(IpcPricingConstants.MAPPED_VM) != null) { // Database License is Managed
					dbUnitPrice = getListPricePerUnit(salePriceMapper, IpcPricingConstants.PRICING_ITEM_DB_PREFIX
							+ solutionNameArry[0].trim() + IpcPricingConstants.PRICING_ITEM_DB_MANAGED_SUFFIX);
				}
				dbFinalMrc = dbUnitPrice * attributeMap.get(IpcPricingConstants.QUANTITY);
			}

			Component component = addon.getPriceBreakup().get(dbSolutionName);
			if (Objects.nonNull(component)) {
				component.setMrc(IpcUtils.roundOff2(dbFinalMrc));
				component.setNrc(0D);
			}

			reCalculateAddonPricing(addon, component);
		}
	}

	private void calculatePricingForDR(Addon addon, Map<String, Map<String, Object>> salePriceMapper) {
		List<String> drLicensesList = new ArrayList<String>(addon.getDrLicenseQuantity().keySet());
		for (String drSolutionName : drLicensesList) {
			Double drFinalMrc = 0D;
			Map<String, String> attributeMap = addon.getDrLicenseQuantity().get(drSolutionName);
			Double dbUnitPrice = getListPricePerUnit(salePriceMapper, IpcPricingConstants.PRICING_ITEM_DR_PREFIX
					+ drSolutionName + IpcPricingConstants.PRICING_ITEM_DR_SUFFIX);
			drFinalMrc = dbUnitPrice * Integer.parseInt(attributeMap.get(IpcPricingConstants.QUANTITY));

			Component component = addon.getPriceBreakup().get(drSolutionName);
			if (Objects.nonNull(component)) {
				component.setMrc(IpcUtils.roundOff2(drFinalMrc));
				component.setNrc(0D);
			}

			reCalculateAddonPricing(addon, component);
		}
	}

	private void calculatePricingForHybridConnections(Addon addon, Map<String, Map<String, Object>> salePriceMapper) {
		List<String> hybridConnections = new ArrayList<String>(addon.getHybridConnections().keySet());
		for (String hybridConnection : hybridConnections) {
			Double hybridConnectionFinalMrc = 0D;
			Double hybridConnectionFinalNrc = 0D;

			Map<String, String> attributeMap = addon.getHybridConnections().get(hybridConnection);

			Double hybridConnectionUnitMrc = 0D;
			Double hybridConnectionUnitNrc = 0D;
			if (IpcPricingConstants.CABLE_TYPE_FIBER
					.equalsIgnoreCase(attributeMap.get(IpcPricingConstants.CABLE_TYPE))) {
				if (IpcPricingConstants.L2_THROUGHPUT_10GIG
						.equalsIgnoreCase(attributeMap.get(IpcPricingConstants.L2_THROUGHPUT))) {
					hybridConnectionUnitMrc = getListPricePerUnit(salePriceMapper,
							IpcPricingConstants.PRICING_ITEM_PER_10GIG_FIBER_CONNECTION_MRC);
					hybridConnectionUnitNrc = getListPricePerUnit(salePriceMapper,
							IpcPricingConstants.PRICING_ITEM_PER_10GIG_FIBER_CONNECTION_NRC);
				} else if (IpcPricingConstants.L2_THROUGHPUT_1GIG
						.equalsIgnoreCase(attributeMap.get(IpcPricingConstants.L2_THROUGHPUT))) {
					hybridConnectionUnitMrc = getListPricePerUnit(salePriceMapper,
							IpcPricingConstants.PRICING_ITEM_PER_1GIG_FIBER_CONNECTION_MRC);
					hybridConnectionUnitNrc = getListPricePerUnit(salePriceMapper,
							IpcPricingConstants.PRICING_ITEM_PER_1GIG_FIBER_CONNECTION_NRC);
				}
			} else if (IpcPricingConstants.CABLE_TYPE_COPPER
					.equalsIgnoreCase(attributeMap.get(IpcPricingConstants.CABLE_TYPE))) {
				if (IpcPricingConstants.L2_THROUGHPUT_1GIG
						.equalsIgnoreCase(attributeMap.get(IpcPricingConstants.L2_THROUGHPUT))) {
					hybridConnectionUnitMrc = getListPricePerUnit(salePriceMapper,
							IpcPricingConstants.PRICING_ITEM_PER_1GIG_COPPER_CONNECTION_MRC);
					hybridConnectionUnitNrc = getListPricePerUnit(salePriceMapper,
							IpcPricingConstants.PRICING_ITEM_PER_1GIG_COPPER_CONNECTION_NRC);
				}
			}

			Double sharedSwitchPortUnitMrc = getListPricePerUnit(salePriceMapper,
					IpcPricingConstants.PRICING_ITEM_PER_SHARED_SWITCH_PORT);
			Double sharedSwitchPortFinalMrc = convertUSDToGivenCurrency("INR", sharedSwitchPortUnitMrc)
					* Integer.parseInt(attributeMap.get(IpcPricingConstants.SHARED_SWITCH_PORT));

			hybridConnectionFinalMrc = hybridConnectionUnitMrc + sharedSwitchPortFinalMrc;
			hybridConnectionFinalNrc = hybridConnectionUnitNrc;

			Component component = addon.getPriceBreakup().get(hybridConnection);
			if (Objects.nonNull(component)) {
				component.setMrc(IpcUtils.roundOff2(hybridConnectionFinalMrc));
				component.setNrc(IpcUtils.roundOff2(hybridConnectionFinalNrc));
			}

			reCalculateAddonPricing(addon, component);
		}
	}

	private void reCalculateAddonPricing(Addon addon, Component component) {
		addon.setMrc(IpcUtils.roundOff2(addon.getMrc() + component.getMrc()));
		addon.setNrc(IpcUtils.roundOff2(addon.getNrc() + component.getNrc()));
	}

	private Double convertUSDToGivenCurrency(String givenCurrency, Double itemPrice) {
		Double currencyConversionRate = 1D;
		try {
			String currencyConversionRateValue = (String) mqUtils.sendAndReceive(currencyConversionRateQueue,
					givenCurrency);
			currencyConversionRate = Double.parseDouble(currencyConversionRateValue);
		} catch (TclCommonException e) {
			LOGGER.error("Exception caught in currencyConversionRateQueue: {}", e);
		}

		itemPrice = itemPrice * currencyConversionRate;

		return itemPrice;
	}

	private PricingRequest constructPortPricingRequest(Quote quote, Addon addon, Access access) {
		PricingInputDatum pricingInputDatum = new PricingInputDatum();
		pricingInputDatum.setSiteId("IPC_CLOUD");
		pricingInputDatum.setLatitudeFinal("");
		pricingInputDatum.setLongitudeFinal("");
		pricingInputDatum.setProspectName(quote.getProspectName());
		pricingInputDatum.setBwMbps(access != null ? access.getLimit() : "20");
		pricingInputDatum.setBurstableBw("");
		pricingInputDatum.setRespCity("");
		pricingInputDatum.setCustomerSegment(quote.getCustomerSegment());
		pricingInputDatum.setSalesOrg(quote.getSalesOrg());
		pricingInputDatum.setProductName("IPC");
		pricingInputDatum.setFeasibilityResponseCreatedDate("2019-06-20");
		pricingInputDatum.setLocalLoopInterface("");
		pricingInputDatum.setLastMileContractTerm("");
		pricingInputDatum.setAccountIdWith18Digit(quote.getAccountId());
		pricingInputDatum.setOpportunityTerm(quote.getTerm() + "");
		pricingInputDatum.setQuotetypeQuote("New Order");
		pricingInputDatum.setConnectionType("Standard");
		pricingInputDatum.setSumNoOfSitesUniLen("0");
		pricingInputDatum.setCpeVariant("");
		pricingInputDatum.setCpeManagementType("");
		pricingInputDatum.setCpeSupplyType("");
		pricingInputDatum.setTopology("primary_active");
		pricingInputDatum.setSumOnnetFlag("0");
		pricingInputDatum.setSumOffnetFlag("0");
		pricingInputDatum.setLmArcBwOnwl("0");
		pricingInputDatum.setLmNrcBwOnwl("0");
		pricingInputDatum.setLmNrcMuxOnwl("0");
		pricingInputDatum.setLmNrcInbldgOnwl("0");
		pricingInputDatum.setLmNrcOspcapexOnwl("0");
		pricingInputDatum.setLmNrcNerentalOnwl("0");
		pricingInputDatum.setLmArcBwProvOfrf("0");
		pricingInputDatum.setLmNrcBwProvOfrf("0");
		pricingInputDatum.setLmNrcMastOfrf("0");
		pricingInputDatum.setLmArcBwOnrf("0");
		pricingInputDatum.setLmNrcBwOnrf("0");
		pricingInputDatum.setLmNrcMastOnrf("0");
		pricingInputDatum.setOrchConnection("Wireline");
		pricingInputDatum.setOrchLMType("Onnet");
		pricingInputDatum.setIpv4AddressPoolSize("0");
		pricingInputDatum.setIpv6AddressPoolSize("0");
		pricingInputDatum.setCuLeId(quote.getCustomerLeId());
		pricingInputDatum.setLlChange("None");
		pricingInputDatum.setMacdOption("no");
		pricingInputDatum.setTriggerFeasibility("Yes");
		pricingInputDatum.setOldContractTerm("None");
		pricingInputDatum.setServiceCommissionedDate("None");
		pricingInputDatum.setLatLong("None");
		pricingInputDatum.setServiceId("None");
		pricingInputDatum.setBackupPortRequested("No");
		pricingInputDatum.setOldPortBw("None");
		pricingInputDatum.setOldLlBw("None");
		pricingInputDatum.setParallelRunDays("None");
		pricingInputDatum.setCpeChassisChanged("None");
		pricingInputDatum.setLocalLoopBw("");
		pricingInputDatum.setOrchCategory("");
		pricingInputDatum.setPartnerAccountIdWith18Digit("None");
		pricingInputDatum.setPartnerProfile("None");
		pricingInputDatum.setQuoteTypePartner("None");
		pricingInputDatum.setCompressedInternetRatio("0:0");
		if (addon != null) {
			pricingInputDatum.setNoOfAdditionalIp(addon.getIpcount() + "");
			pricingInputDatum.setAdditionalIpFlag("Yes");
			pricingInputDatum.setIpAddressArrangement("ipv4");
		} else {
			pricingInputDatum.setNoOfAdditionalIp("0");
			pricingInputDatum.setAdditionalIpFlag("No");
			pricingInputDatum.setIpAddressArrangement("None");
		}

		List<PricingInputDatum> pricingData = new ArrayList<>();
		pricingData.add(pricingInputDatum);

		PricingRequest pricingRequest = new PricingRequest();
		pricingRequest.setInputData(pricingData);

		return pricingRequest;
	}

	@Transactional
	public void uploadRateCard(RateCard rateCardRequest) throws TclCommonException {
		Integer customerId = rateCardRequest.getCustomerId() != null && rateCardRequest.getCustomerId() > 0
				? rateCardRequest.getCustomerId()
				: null;
		String region = StringUtils.isNotBlank(rateCardRequest.getRegion()) ? rateCardRequest.getRegion() : null;
		String country = StringUtils.isNotBlank(rateCardRequest.getCountry()) ? rateCardRequest.getCountry() : null;

		if (rateCardRequest.getItems() != null) {
			List<PricingIpcItem> pricingIpcItems = pricingIpcItemsRepository.findAll();

			List<PricingIpcRateCard> existingPricingIpcRateCard = getExistingPricingIpcRateCard(customerId, region,
					country);

			for (RateCardItem item : rateCardRequest.getItems()) {
				Optional<PricingIpcItem> matchingPricingIpcItem = pricingIpcItems.stream()
						.filter(pricingIpcItem -> pricingIpcItem.getName().equalsIgnoreCase(item.getName())
								&& pricingIpcItem.getUom().equalsIgnoreCase(item.getUom()))
						.findFirst();
				if (!matchingPricingIpcItem.isPresent()) {
					LOGGER.error("Item: {}, {} is not present in the master table.", item.getName(), item.getUom());
					throw new TclCommonException(IpcPricingConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
				}

				Optional<PricingIpcRateCard> existingPricingIpcRate = existingPricingIpcRateCard.stream().filter(
						pricingIpcRate -> pricingIpcRate.getItem().getId().equals(matchingPricingIpcItem.get().getId()))
						.findFirst();

				PricingIpcRateCard ipcRateCard;
				if (existingPricingIpcRate.isPresent()) {
					ipcRateCard = existingPricingIpcRate.get();
				} else {
					ipcRateCard = new PricingIpcRateCard();
					ipcRateCard.setCustomerId(customerId);
					ipcRateCard.setRegion(region);
					ipcRateCard.setCountry(country);
					ipcRateCard.setItem(matchingPricingIpcItem.get());
				}
				ipcRateCard.setPrice(item.getPrice() != null ? item.getPrice() : 0D);
				ipcRateCard.setCurrencyCode(item.getCurrency() != null ? item.getCurrency() : "USD");
				pricingIpcRateCardRepository.save(ipcRateCard);
			}
		}

		if (rateCardRequest.getAttributes() != null) {
			List<PricingIpcAttribute> pricingIpcAttributes = pricingIpcAttributesRepository.findAll();

			List<PricingIpcRateCardAttribute> existingPricingIpcRateCardAttributes = getExistingPricingIpcRateCardAttributes(
					customerId, region, country);

			for (RateCardAttribute attribute : rateCardRequest.getAttributes()) {
				Optional<PricingIpcAttribute> matchingPricingIpcAttribute = pricingIpcAttributes.stream().filter(
						pricingIpcAttribute -> pricingIpcAttribute.getName().equalsIgnoreCase(attribute.getName()))
						.findFirst();
				if (!matchingPricingIpcAttribute.isPresent()) {
					LOGGER.error("Attribute: {} is not present in the master table.", attribute.getName());
					throw new TclCommonException(IpcPricingConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
				}

				Optional<PricingIpcRateCardAttribute> existingPricingIpcRateAttribute = existingPricingIpcRateCardAttributes
						.stream().filter(pricingIpcRateAttribute -> pricingIpcRateAttribute.getAttribute().getId()
								.equals(matchingPricingIpcAttribute.get().getId()))
						.findFirst();

				PricingIpcRateCardAttribute ipcRateCardAttribute;
				if (existingPricingIpcRateAttribute.isPresent()) {
					ipcRateCardAttribute = existingPricingIpcRateAttribute.get();
				} else {
					ipcRateCardAttribute = new PricingIpcRateCardAttribute();
					ipcRateCardAttribute.setCustomerId(customerId);
					ipcRateCardAttribute.setRegion(region);
					ipcRateCardAttribute.setCountry(country);
					ipcRateCardAttribute.setAttribute(matchingPricingIpcAttribute.get());
				}
				ipcRateCardAttribute.setValue(attribute.getValue());
				pricingIpcRateCardAttributesRepository.save(ipcRateCardAttribute);
			}
		}
	}

	private List<PricingIpcRateCard> getExistingPricingIpcRateCard(Integer customerId, String region, String country) {
		Specification<PricingIpcRateCard> pricingIpcRateCardSpecification = (root, query, criteriaBuilder) -> {
			final List<Predicate> predicates = new ArrayList<>();
			if (customerId != null) {
				predicates.add(criteriaBuilder.equal(root.get("customerId"), customerId));
			} else {
				predicates.add(criteriaBuilder.isNull(root.get("customerId")));
			}
			if (region != null) {
				predicates.add(criteriaBuilder.equal(root.get("region"), region));
			} else {
				predicates.add(criteriaBuilder.isNull(root.get("region")));
			}
			if (country != null) {
				predicates.add(criteriaBuilder.equal(root.get("country"), country));
			} else {
				predicates.add(criteriaBuilder.isNull(root.get("country")));
			}
			return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
		};

		return pricingIpcRateCardRepository.findAll(pricingIpcRateCardSpecification);
	}

	private List<PricingIpcRateCardAttribute> getExistingPricingIpcRateCardAttributes(Integer customerId, String region,
			String country) {
		Specification<PricingIpcRateCardAttribute> pricingIpcRateCardAttributeSpecification = (root, query,
				criteriaBuilder) -> {
			final List<Predicate> predicates = new ArrayList<>();
			if (customerId != null) {
				predicates.add(criteriaBuilder.equal(root.get("customerId"), customerId));
			} else {
				predicates.add(criteriaBuilder.isNull(root.get("customerId")));
			}
			if (region != null) {
				predicates.add(criteriaBuilder.equal(root.get("region"), region));
			} else {
				predicates.add(criteriaBuilder.isNull(root.get("region")));
			}
			if (country != null) {
				predicates.add(criteriaBuilder.equal(root.get("country"), country));
			} else {
				predicates.add(criteriaBuilder.isNull(root.get("country")));
			}
			return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
		};

		return pricingIpcRateCardAttributesRepository.findAll(pricingIpcRateCardAttributeSpecification);
	}
}
