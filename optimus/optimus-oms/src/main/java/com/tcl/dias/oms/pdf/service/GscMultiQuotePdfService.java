package com.tcl.dias.oms.pdf.service;

import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.DOMESTIC_VOICE_SITE_ADDRESS;
import static com.tcl.dias.oms.gsc.util.GscConstants.DOMESTIC_VOICE;
import static com.tcl.dias.oms.gsc.util.GscConstants.ORDER_TYPE_MACD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.ImmutableList;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.QuoteGsc;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.QuoteGscRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.gsc.beans.GscQuoteBean;
import com.tcl.dias.oms.gsc.beans.GscSolutionBean;
import com.tcl.dias.oms.gsc.pdf.beans.GscTerminationBean;
import com.tcl.dias.oms.gsc.util.GscConstants;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRCofPdfBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRMultiQuoteLeBean;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * Service for handling Multi-LE GSC quote/cof pdf requests
 * 
 * @author Srinivasa Raghavan
 */
@Service
@Transactional
public class GscMultiQuotePdfService {

	Logger LOGGER = LoggerFactory.getLogger(GscMultiQuotePdfService.class);

	@Autowired
	QuoteProductComponentRepository quoteProductComponentRepository;

	@Autowired
	QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@Autowired
	QuoteGscRepository quoteGscRepository;

	@Autowired
	MstProductComponentRepository mstProductComponentRepository;

	@Autowired
	ProductAttributeMasterRepository productAttributeMasterRepository;

	/**
	 * Construct vaollume commitment
	 *
	 * @param teamsDRCofPdfBean
	 * @param quoteId
	 */
	public void constructVolumeCommitment(TeamsDRCofPdfBean teamsDRCofPdfBean, Integer quoteId) {

		List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
				.findByReferenceIdAndType(quoteId, GscConstants.GSC_COMMON_PRODUCT_COMPONENT_TYPE.toUpperCase());

		List<QuoteProductComponentsAttributeValue> inboundVolume = quoteProductComponents.stream().findFirst()
				.map(QuoteProductComponent::getId)
				.map(integer -> quoteProductComponentsAttributeValueRepository
						.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(integer,
								GscConstants.ATTRIBUTE_VALUE_INBOUND_VOLUME))
				.orElse(ImmutableList.of());

		if (inboundVolume != null && !inboundVolume.isEmpty() && !inboundVolume.stream().findFirst().get()
				.getAttributeValues().toString().equalsIgnoreCase(GscConstants.STRING_ZERO)) {
			teamsDRCofPdfBean.setInboundVolume(inboundVolume.stream().findFirst().get().getAttributeValues().toString()
					+ GscConstants.UNIT_CONSTANT_VOLUME_COMMITMENT);
			List<QuoteProductComponentsAttributeValue> inboundVolumeContries = quoteProductComponents.stream()
					.findFirst().map(QuoteProductComponent::getId)
					.map(integer -> quoteProductComponentsAttributeValueRepository
							.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(integer, "Inbound Countries"))
					.orElse(ImmutableList.of());
			teamsDRCofPdfBean.setInboundVolumeCountry(inboundVolumeContries.stream()
					.map(QuoteProductComponentsAttributeValue::getAttributeValues).collect(Collectors.joining(",")));
		} else {
			teamsDRCofPdfBean.setInboundVolume(GscConstants.NOT_APPLICABLE);
			teamsDRCofPdfBean.setInboundVolumeCountry(GscConstants.NOT_APPLICABLE);
		}

		List<QuoteProductComponentsAttributeValue> outboundVolume = quoteProductComponents.stream().findFirst()
				.map(QuoteProductComponent::getId)
				.map(integer -> quoteProductComponentsAttributeValueRepository
						.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(integer,
								GscConstants.ATTRIBUTE_VALUE_OUTBOUND_VOLUME))
				.orElse(ImmutableList.of());

		if (outboundVolume != null && !outboundVolume.isEmpty() && !outboundVolume.stream().findFirst().get()
				.getAttributeValues().toString().equalsIgnoreCase(GscConstants.STRING_ZERO)) {
			teamsDRCofPdfBean
					.setOutboundVolume(outboundVolume.stream().findFirst().get().getAttributeValues().toString()
							+ GscConstants.UNIT_CONSTANT_VOLUME_COMMITMENT);
			List<QuoteProductComponentsAttributeValue> outboundVolumeContries = quoteProductComponents.stream()
					.findFirst().map(QuoteProductComponent::getId)
					.map(integer -> quoteProductComponentsAttributeValueRepository
							.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(integer,
									"Outbound Countries"))
					.orElse(ImmutableList.of());
			teamsDRCofPdfBean.setOutboundVolumeCountry(outboundVolumeContries.stream()
					.map(QuoteProductComponentsAttributeValue::getAttributeValues).collect(Collectors.joining(",")));
		} else {
			teamsDRCofPdfBean.setOutboundVolume(GscConstants.NOT_APPLICABLE);
			teamsDRCofPdfBean.setOutboundVolumeCountry(GscConstants.NOT_APPLICABLE);
		}
	}

	/**
	 * Check for domestic voice
	 *
	 * @param teamsDRCofPdfBean
	 * @return
	 */
	public TeamsDRCofPdfBean checkIsDomesticVoice(TeamsDRCofPdfBean teamsDRCofPdfBean) {
		teamsDRCofPdfBean.getGscSolutions().stream().forEach(solution -> {
			solution.getGscQuotes().stream().forEach(gscQuote -> {
				if (DOMESTIC_VOICE.equalsIgnoreCase(gscQuote.getProductName())) {
					teamsDRCofPdfBean.setIsDomesticVoice(true);
				}
			});
		});
		return teamsDRCofPdfBean;
	}

	/**
	 * Extract rate per minute attributes
	 *
	 * @param teamsDRCofPdfBean
	 * @param teamsDRMultiQuoteLeBean
	 */
	public void extractRpmAttributes(TeamsDRCofPdfBean teamsDRCofPdfBean,
			TeamsDRMultiQuoteLeBean teamsDRMultiQuoteLeBean) {

		teamsDRCofPdfBean.setSizeAttribute(0);
		final Integer[] quantityOfNumbers = new Integer[1];
		quantityOfNumbers[0] = 1;

		final Integer[] numbersPorted = new Integer[1];
		numbersPorted[0] = 1;

		teamsDRCofPdfBean.getGscSolutions().stream().forEach(solutions -> {

			if ("ITFS".equalsIgnoreCase(solutions.getProductName())
					|| "LNS".equalsIgnoreCase(solutions.getProductName())
					|| "UIFN".equalsIgnoreCase(solutions.getProductName())
					|| "ACANS".equalsIgnoreCase(solutions.getProductName())
					|| "ACDTFS".equalsIgnoreCase(solutions.getProductName())) {
				solutions.setRateColumn("Rate per minute(Payphone)");
			} else {
				solutions.setRateColumn("Rate per minute(Special)");
			}
			solutions.setPaymentCurrency(teamsDRCofPdfBean.getPaymentCurrency());
			solutions.setBillingCurrency(teamsDRCofPdfBean.getBillingCurrency());

			solutions.getGscQuotes().stream().forEach(gscQuotes -> {

				gscQuotes.getConfigurations().stream().forEach(configurations -> {
					quantityOfNumbers[0] = 0;
					numbersPorted[0] = 0;
					if (Objects.nonNull(teamsDRMultiQuoteLeBean.getQuoteCategory())
							&& teamsDRMultiQuoteLeBean.getQuoteType().equalsIgnoreCase(ORDER_TYPE_MACD)) {
						configurations.setNoOfConcurrentChannel("1");
					}

					configurations.getProductComponents().stream().forEach(productComponents -> {

						productComponents.getAttributes().stream().forEach(attribute -> {
							String rate = null;
							try {
								if (LeAttributesConstants.GLOBAL_OUTBOUND.equalsIgnoreCase(solutions.getProductName())
										&& LeAttributesConstants.QUANTITY_OF_NUMBERS
												.equalsIgnoreCase(attribute.getAttributeName())) {
									attribute.setAttributeName(LeAttributesConstants.DELETED);
								}
								switch (attribute.getAttributeName()) {
								case LeAttributesConstants.TERMINATION_NAME:
									attribute.setAttributeName(LeAttributesConstants.DELETED);
									if (Objects.nonNull(attribute.getValueString())
											&& StringUtils.isNotBlank(attribute.getValueString())) {
										configurations.setTerminationName(
												Arrays.asList(attribute.getValueString().split("\\s*,\\s*")));
									}
									break;
								case LeAttributesConstants.PHONE_TYPE:
									attribute.setAttributeName(LeAttributesConstants.DELETED);
									if (Objects.nonNull(attribute.getValueString())
											&& StringUtils.isNotBlank(attribute.getValueString())) {
										configurations.setPhoneType(
												Arrays.asList(attribute.getValueString().split("\\s*,\\s*")));
									}
									break;
								case LeAttributesConstants.TERMINATION_RATE:
									attribute.setAttributeName(LeAttributesConstants.DELETED);
									if (Objects.nonNull(attribute.getValueString())
											&& StringUtils.isNotBlank(attribute.getValueString())) {
										configurations.setTerminationRate(
												Arrays.asList(attribute.getValueString().split("\\s*,\\s*")));
									}
									break;
								case LeAttributesConstants.CHANNEL_MRC:
									configurations.setConcurrentChannelMRC(attribute.getValueString());
									break;
								case LeAttributesConstants.ORDER_SETUP_NRC:
									LOGGER.info("Order Setup NRC value is {} " + attribute.getValueString());
									configurations.setOrderSetupNRC(attribute.getValueString());
									break;
								case LeAttributesConstants.DID_ARC:
									attribute.setAttributeName(LeAttributesConstants.DELETED);
									break;
								case LeAttributesConstants.DID_MRC:
									configurations.setDomesticDIDMRC(attribute.getValueString());
									break;
								case LeAttributesConstants.CHANNEL_ARC:
									attribute.setAttributeName(LeAttributesConstants.DELETED);
									break;
								case LeAttributesConstants.RATE_PER_MIN_FIXED:
									attribute.setAttributeName(LeAttributesConstants.DELETED);
									if (Objects.nonNull(attribute.getValueString())
											&& StringUtils.isNotBlank(attribute.getValueString())) {
										rate = attribute.getValueString().split(",")[0];
										if (StringUtils.isNotBlank(rate)) {
											configurations.setRatePerMinFixed(Double.parseDouble(rate));
										} else {
											configurations.setRatePerMinFixed(0.0);
										}
									}
									break;
								case LeAttributesConstants.RATE_PER_MIN_SPECIAL:
									attribute.setAttributeName(LeAttributesConstants.DELETED);
									if (Objects.nonNull(attribute.getValueString())
											&& StringUtils.isNotBlank(attribute.getValueString())) {
										rate = attribute.getValueString().split(",")[0];
										if (StringUtils.isNotBlank(rate)) {
											configurations.setRatePerMinSpecial(Double.parseDouble(rate));
										} else {
											configurations.setRatePerMinSpecial(0.0);
										}
									}
									break;
								case LeAttributesConstants.RATE_PER_MIN_MOBILE:
									attribute.setAttributeName(LeAttributesConstants.DELETED);
									if (Objects.nonNull(attribute.getValueString())
											&& StringUtils.isNotBlank(attribute.getValueString())) {
										rate = attribute.getValueString().split(",")[0];
										if (StringUtils.isNotBlank(rate)) {
											configurations.setRatePerMinMobile(Double.parseDouble(rate));
										} else {
											configurations.setRatePerMinMobile(0.0);
										}
									}
									break;
								case LeAttributesConstants.RATE_PER_MIN:
									attribute.setAttributeName(LeAttributesConstants.DELETED);
									if (Objects.nonNull(attribute.getValueString())
											&& StringUtils.isNotBlank(attribute.getValueString())) {
										rate = attribute.getValueString().split(",")[0];
										if (StringUtils.isNotBlank(rate)) {
											configurations.setRatePerMinute(Double.parseDouble(rate));
										} else {
											configurations.setRatePerMinute(0.0);
										}
									}
									break;
								case LeAttributesConstants.QUANTITY_OF_NUMBERS:
									if (Objects.nonNull(attribute.getDisplayValue())
											&& StringUtils.isNotBlank(attribute.getDisplayValue())) {
										quantityOfNumbers[0] = quantityOfNumbers[0]
												+ Integer.parseInt(attribute.getDisplayValue());
									}
									teamsDRCofPdfBean.setSizeAttribute(1);
									break;
								case LeAttributesConstants.LIST_OF_NUMBERS_TO_BE_PORTED:
									if (Objects.nonNull(attribute.getDisplayValue())
											&& StringUtils.isNotBlank(attribute.getDisplayValue())) {
										numbersPorted[0] = numbersPorted[0]
												+ Integer.parseInt(attribute.getDisplayValue());
									}
									teamsDRCofPdfBean.setSizeAttribute(1);
									break;
								case LeAttributesConstants.NO_OF_CONCURRENT_CHANNEL:
									configurations.setNoOfConcurrentChannel(attribute.getValueString());
									break;
								case LeAttributesConstants.UIFN_REGISTRATION_CHARGE:
									if (StringUtils.isNotBlank(attribute.getValueString())) {
										configurations.setUifnRegistrationCharge(
												Double.parseDouble(attribute.getValueString()));
									} else {
										configurations.setUifnRegistrationCharge(0.0);
									}
									break;
								default:
									teamsDRCofPdfBean.setSizeAttribute(1);
									break;
								}
							} catch (Exception e) {
								throw new TclCommonRuntimeException(ExceptionConstants.GSC_QUOTE_VALIDATION_ERROR, e,
										ResponseResource.R_CODE_ERROR);
							}
						});
					});

					configurations.setQuantityOfNumbers(quantityOfNumbers[0]);
					configurations.setNumbersPorted(numbersPorted[0]);
				});
			});

		});

		teamsDRCofPdfBean.getGscSolutions().stream().forEach(solutions -> {
			solutions.getGscQuotes().stream().forEach(gscQuotes -> {

				gscQuotes.getConfigurations().stream().forEach(configuration -> {
					int[] i = new int[1];
					i[0] = 0;
					try {
						if (Objects.nonNull(configuration.getTerminationName())
								&& Objects.nonNull(configuration.getPhoneType())
								&& Objects.nonNull(configuration.getTerminationRate())) {

							if (configuration.getTerminationName().size() == configuration.getPhoneType().size()
									&& configuration.getTerminationName().size() == configuration.getTerminationRate()
											.size()) {
								if (!configuration.getTerminationName().isEmpty()
										&& !configuration.getPhoneType().isEmpty()
										&& !configuration.getTerminationRate().isEmpty()) {
									/*
									 * List<GscOutboundPriceBean> gscOutboundPriceBean =
									 * gscPricingFeasibilityService.processOutboundPriceData(configuration.
									 * getTerminationName()); List<GscOutboundPriceBean> gscOutboundPriceBean =
									 * null; if (Objects.nonNull(gscOutboundPriceBean) &&
									 * !gscOutboundPriceBean.isEmpty()) {
									 */

									while (i[0] < configuration.getTerminationName().size()) {
										GscTerminationBean gscTerminationBean = new GscTerminationBean();
										gscTerminationBean
												.setTerminationName(configuration.getTerminationName().get(i[0]));
										/*
										 * for (GscOutboundPriceBean gscOutboundPrice : gscOutboundPriceBean) { if
										 * (gscOutboundPrice.getDestinationName().equalsIgnoreCase(configuration.
										 * getTerminationName().get(i[0]))) {
										 * gscTerminationBean.setTerminationId(gscOutboundPrice.getDestId().toString());
										 * gscTerminationBean.setComments(gscOutboundPrice.getComments()); } }
										 */

										gscTerminationBean.setPhoneType(configuration.getPhoneType().get(i[0]));
										gscTerminationBean
												.setTerminationRate(configuration.getTerminationRate().get(i[0]));
										configuration.getTerminations().add(gscTerminationBean);
										i[0]++;
									}
									/* } */
								}

							}
						}

						checkAndSetDefaultValues(teamsDRCofPdfBean.getGscSolutions());

					} catch (Exception e) {
						throw new TclCommonRuntimeException(ExceptionConstants.GSC_QUOTE_VALIDATION_ERROR, e,
								ResponseResource.R_CODE_ERROR);
					}
				});
			});
		});
	}

	/**
	 * Check and set default values if null
	 *
	 * @param gscSolutionBeans
	 */
	public void checkAndSetDefaultValues(List<GscSolutionBean> gscSolutionBeans) {
		gscSolutionBeans.stream().forEach(solutions -> {
			solutions.getGscQuotes().stream().forEach(gscQuotes -> {

				if (Objects.isNull(gscQuotes.getArc())) {
					gscQuotes.setArc(0.0);
				}
				if (Objects.isNull(gscQuotes.getMrc())) {
					gscQuotes.setMrc(0.0);
				}
				if (Objects.isNull(gscQuotes.getNrc())) {
					gscQuotes.setNrc(0.0);
				}
				if (Objects.isNull(gscQuotes.getTcv())) {
					gscQuotes.setTcv(0.0);
				}
				gscQuotes.getConfigurations().stream().forEach(configuration -> {
					if (Objects.isNull(configuration.getArc())) {
						configuration.setArc(0.0);
					}
					if (Objects.isNull(configuration.getMrc())) {
						configuration.setMrc(0.0);
					}
					if (Objects.isNull(configuration.getNrc())) {
						configuration.setNrc(0.0);
					}
					if (StringUtils.isBlank(configuration.getDomesticDIDMRC())) {
						configuration.setDomesticDIDMRC("0.0");
					}
					if (StringUtils.isBlank(configuration.getOrderSetupNRC())) {
						configuration.setOrderSetupNRC("0.0");
						LOGGER.info("Default value of Order Setup NRC is {} " + configuration.getOrderSetupNRC());
					}
					if (StringUtils.isBlank(configuration.getConcurrentChannelMRC())) {
						configuration.setConcurrentChannelMRC("0.0");
					}

				});
			});
		});
	}

	/**
	 * Calculate price for voice solutions
	 *
	 * @param teamsDRCofPdfBean
	 * @param gscSolutionBeans
	 */
	public static void calculatePrice(TeamsDRCofPdfBean teamsDRCofPdfBean, List<GscSolutionBean> gscSolutionBeans) {
		double totalMRC[] = { 0.0D };
		double totalNRC[] = { 0.0D };
		gscSolutionBeans.forEach(gscSolutionBean -> {

			gscSolutionBean.getGscQuotes().forEach(gscQuoteBean -> {
				totalMRC[0] += gscQuoteBean.getMrc();
				totalNRC[0] += gscQuoteBean.getNrc();
			});

		});
		teamsDRCofPdfBean.setTotalVoiceMrc(String.valueOf(totalMRC[0]));
		teamsDRCofPdfBean.setTotalVoiceNrc(String.valueOf(totalNRC[0]));
	}

	public void checkInboundPresence(TeamsDRCofPdfBean teamsDRCofPdfBean, List<GscSolutionBean> gscSolutionBeans) {
		gscSolutionBeans.stream().forEach(solution -> {

			solution.getGscQuotes().stream().forEach(gscQuote -> {

				if (gscQuote.getProductName().equalsIgnoreCase(GscConstants.ITFS)
						|| gscQuote.getProductName().equalsIgnoreCase(GscConstants.LNS)
						|| gscQuote.getProductName().equalsIgnoreCase(GscConstants.UIFN)
						|| gscQuote.getProductName().equalsIgnoreCase(GscConstants.ACANS)
						|| gscQuote.getProductName().equalsIgnoreCase(GscConstants.ACDTFS)) {
					teamsDRCofPdfBean.setIsInbound(true);
				}
			});

		});
	}

	/**
	 * Get domestic voice site address
	 *
	 * @param gscQuoteBean
	 * @return
	 */
	public List<String> getDIDSiteAddress(GscQuoteBean gscQuoteBean) {
		List<String> siteAddress = new ArrayList<>();
		QuoteGsc quoteGsc = quoteGscRepository.findById(gscQuoteBean.getId()).get();
		Integer quoteGscDetailId = quoteGsc.getQuoteGscDetails().stream().findFirst().get().getId();
		List<MstProductComponent> mstProductComponents = mstProductComponentRepository
				.findByNameAndStatus(quoteGsc.getAccessType(), GscConstants.STATUS_ACTIVE);
		Optional<QuoteProductComponent> quoteProductComponent = quoteProductComponentRepository
				.findByReferenceIdAndMstProductComponentAndType(quoteGscDetailId,
						mstProductComponents.stream().findFirst().get(), GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE);
		List<ProductAttributeMaster> productAttributeMasters = productAttributeMasterRepository
				.findByNameAndStatus(DOMESTIC_VOICE_SITE_ADDRESS, GscConstants.STATUS_ACTIVE);
		if (!CollectionUtils.isEmpty(productAttributeMasters)) {
			List<QuoteProductComponentsAttributeValue> quoteProductComponentAndProductAttributeMaster = quoteProductComponentsAttributeValueRepository
					.findByQuoteProductComponentAndProductAttributeMaster(quoteProductComponent.get(),
							productAttributeMasters.stream().findFirst().get());
			if (!CollectionUtils.isEmpty(quoteProductComponentAndProductAttributeMaster)) {
				String domesticVoiceSiteAddress = quoteProductComponentAndProductAttributeMaster.stream().findFirst()
						.get().getAttributeValues();
				String[] siteAddressArray = domesticVoiceSiteAddress.split("\\|");
				return Arrays.asList(siteAddressArray);
			}
		}
		return siteAddress;
	}
}
