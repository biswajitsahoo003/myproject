package com.tcl.dias.oms.gsc.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.Ints;
import com.tcl.dias.common.beans.BillingContact;
import com.tcl.dias.common.beans.CustomerLeAttributeRequestBean;
import com.tcl.dias.common.beans.CustomerLeContactDetailBean;
import com.tcl.dias.common.beans.CustomerLeDetailsBean;
import com.tcl.dias.common.beans.LeSapCodeBean;
import com.tcl.dias.common.beans.LeSapCodeResponse;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderGsc;
import com.tcl.dias.oms.entity.entities.OrderGscDetail;
import com.tcl.dias.oms.entity.entities.OrderGscTfn;
import com.tcl.dias.oms.entity.entities.OrderProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.OrderToLeProductFamily;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.repository.AdditionalServiceParamRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderGscDetailRepository;
import com.tcl.dias.oms.entity.repository.OrderGscRepository;
import com.tcl.dias.oms.entity.repository.OrderGscTfnRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.OrderProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.gsc.exception.TCLException;
import com.tcl.dias.oms.gsc.macd.MACDOrderRequest;
import com.tcl.dias.oms.gsc.service.v1.GscOrderDetailService;
import com.tcl.dias.oms.gsc.service.v1.GscOrderService;
import com.tcl.dias.oms.gsc.tiger.TigerServiceHelper;
import com.tcl.dias.oms.gsc.tiger.beans.AccessNumberItem;
import com.tcl.dias.oms.gsc.tiger.beans.AccessServiceOrderItem;
import com.tcl.dias.oms.gsc.tiger.beans.Address;
import com.tcl.dias.oms.gsc.tiger.beans.BaseOrderManagementBean;
import com.tcl.dias.oms.gsc.tiger.beans.BillingEntity;
import com.tcl.dias.oms.gsc.tiger.beans.BillingProfile;
import com.tcl.dias.oms.gsc.tiger.beans.BusinessEntity;
import com.tcl.dias.oms.gsc.tiger.beans.CallType;
import com.tcl.dias.oms.gsc.tiger.beans.Contact;
import com.tcl.dias.oms.gsc.tiger.beans.ContractingEntity;
import com.tcl.dias.oms.gsc.tiger.beans.InterConnectDetail;
import com.tcl.dias.oms.gsc.tiger.beans.InternationalOrderManagementRequest;
import com.tcl.dias.oms.gsc.tiger.beans.Number;
import com.tcl.dias.oms.gsc.tiger.beans.Party;
import com.tcl.dias.oms.gsc.tiger.beans.TerminationDetail;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;
import io.vavr.Tuple;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.tcl.dias.common.constants.CommonConstants.COLON;
import static com.tcl.dias.common.constants.CommonConstants.HYPHEN;
import static com.tcl.dias.oms.gsc.macd.MACDOrderRequest.REQUEST_TYPE_CHANGE_OUTPULSE;
import static com.tcl.dias.oms.gsc.macd.MACDOrderRequest.REQUEST_TYPE_NUMBER_REMOVE;
import static com.tcl.dias.oms.gsc.tiger.TigerServiceClientConfiguration.TIGER_SERVICE_DATE_FORMAT;
import static com.tcl.dias.oms.gsc.tiger.TigerServiceConstants.ACCESS_SERVICE_ACCESS_TYPE_DIRECT_CONNXN;
import static com.tcl.dias.oms.gsc.tiger.TigerServiceConstants.ACCESS_SERVICE_ACCESS_TYPE_NEW_SERVICE;
import static com.tcl.dias.oms.gsc.tiger.TigerServiceConstants.ACCESS_SERVICE_REQUEST_TYPE_ADD_COUNTRIES;
import static com.tcl.dias.oms.gsc.tiger.TigerServiceConstants.ACCESS_SERVICE_REQUEST_TYPE_CANCEL_NUMBER;
import static com.tcl.dias.oms.gsc.tiger.TigerServiceConstants.ACCESS_SERVICE_REQUEST_TYPE_CHANGE_OUTPULSE;
import static com.tcl.dias.oms.gsc.tiger.TigerServiceConstants.ACCESS_SERVICE_REQUEST_TYPE_NEW_NUMBER;
import static com.tcl.dias.oms.gsc.tiger.TigerServiceConstants.CALL_TYPE_CLID_REALTIME;
import static com.tcl.dias.oms.gsc.tiger.TigerServiceConstants.CALL_TYPE_CONNXN_DTG_HEADER;
import static com.tcl.dias.oms.gsc.tiger.TigerServiceConstants.CALL_TYPE_FIXED;
import static com.tcl.dias.oms.gsc.tiger.TigerServiceConstants.CALL_TYPE_OUTPULSE_TYPE_CUSTOM;
import static com.tcl.dias.oms.gsc.tiger.TigerServiceConstants.SAME_AS_NUMBER;
import static com.tcl.dias.oms.gsc.tiger.TigerServiceConstants.SAME_AS_NUMBER_DID;
import static com.tcl.dias.oms.gsc.tiger.TigerServiceConstants.SAME_AS_NUMBER_PREFIX;
import static com.tcl.dias.oms.gsc.tiger.TigerServiceConstants.SAME_AS_NUMBER_PREFIX_DID;
import static com.tcl.dias.oms.gsc.tiger.TigerServiceConstants.SAME_AS_NUMBER_PREFIX_TOLL_FREE;
import static com.tcl.dias.oms.gsc.tiger.TigerServiceConstants.SAME_AS_NUMBER_TOLL_FREE;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.ATTR_CUSTOMER_SECS_ID;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.ATTR_REQUESTOR_DATE_FOR_SERVICE;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.ATTR_TERMINATION_NUMBER_OUTPULSE;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.GSC_MACD_PRODUCT_REFERENCE_ORDER_ID;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.INTERCONNECT_ID;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.TERMINATION_OUTPULSE_TYPE;
import static com.tcl.dias.oms.gsc.util.GscConstants.ACANS;
import static com.tcl.dias.oms.gsc.util.GscConstants.ACDTFS;
import static com.tcl.dias.oms.gsc.util.GscConstants.CHANGING_OUTPULSE;
import static com.tcl.dias.oms.gsc.util.GscConstants.DELETE_NUMBER;
import static com.tcl.dias.oms.gsc.util.GscConstants.GSC_CONFIG_PRODUCT_COMPONENT_TYPE;
import static com.tcl.dias.oms.gsc.util.GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE;
import static com.tcl.dias.oms.gsc.util.GscConstants.LNS;
import static com.tcl.dias.oms.gsc.util.GscConstants.MPLS;
import static com.tcl.dias.oms.gsc.util.GscConstants.PSTN;
import static com.tcl.dias.oms.gsc.util.GscConstants.REQUIRED_PORTING_NUMBERS;
import static com.tcl.dias.oms.gsc.util.GscConstants.STATUS_ACTIVE;
import static com.tcl.dias.oms.gsc.util.GscConstants.STATUS_ACTIVE_BYTE;
import static com.tcl.dias.oms.gsc.util.GscConstants.STATUS_INACTIVE_BYTE;

import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.entity.entities.OdrAdditionalServiceParam;
import com.tcl.dias.oms.entity.repository.OdrAdditionalServiceParamRepository;
import com.tcl.dias.oms.gsc.beans.OdrAdditionalServiceParamBean;

@Component
public class GscOrderManagementRequestBuilder {

    public static final Logger LOGGER = LoggerFactory.getLogger(GscAttachmentHelper.class);

    @Autowired
    OrderGscRepository orderGscRepository;

    @Autowired
    OrderToLeRepository orderToLeRepository;

    @Autowired
    MQUtils mqUtils;

    @Value("${rabbitmq.customer.billing.contact.queue}")
    String billingContactQueueName;

    @Value("${rabbitmq.customer.contact.details.queue}")
    String customerLeContactQueueName;

    @Value("${rabbitmq.customerleattr.product.queue}")
    String customerLeAttrQueueProduct;

    @Value("${rabbitmq.supplier.le.attr.product.queue}")
    String supplierLeAttrQueueProduct;

    @Autowired
    TigerServiceHelper tigerServiceHelper;

    @Autowired
    MstOmsAttributeRepository mstOmsAttributeRepository;

    @Autowired
    OrdersLeAttributeValueRepository ordersLeAttributeValueRepository;

    @Autowired
    OrderProductComponentRepository orderProductComponentRepository;

    @Autowired
    OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;

    @Autowired
    MstProductComponentRepository mstProductComponentRepository;

    @Autowired
    ProductAttributeMasterRepository productAttributeMasterRepository;

    @Autowired
    OrderToLeProductFamilyRepository orderToLeProductFamilyRepository;

    @Autowired
    OrderProductSolutionRepository orderProductSolutionRepository;

    @Autowired
    OrderGscDetailRepository orderGscDetailRepository;

    @Autowired
    OrderGscTfnRepository orderGscTfnRepository;

    @Autowired
    GscRepcCountries gscRepcCountries;

    @Autowired
    GscOrderService gscOrderService;

    @Autowired
    GscOrderDetailService gscOrderDetailService;

    @Autowired
    AdditionalServiceParamRepository additionalServiceParamRepository;

    private static final Map<String, String> CODEC_TRANSLATION_TABLE = ImmutableMap.<String, String>builder()
            .put("AMR-NB", "AMR-NB")
            .put("G711u-law", "G.711")
            .put("G.722", "G.722")
            .put("G.722.2", "G.722.2")
            .put("G.729", "G.729")
            .put("G11a-law", "G11a-law")
            .build();

    private Optional<BillingContact> getBillingContact(OrderToLe orderToLe)
            throws TclCommonException, IllegalArgumentException {
        LOGGER.info("MDC Filter token value in before Queue call getBillingContact {} :",
                MDC.get(CommonConstants.MDC_TOKEN_KEY));
        String response = (String) mqUtils.sendAndReceive(billingContactQueueName,
                String.valueOf(orderToLe.getErfCusCustomerLegalEntityId()));
        List<BillingContact> billingContacts = GscUtils.fromJson(response, new TypeReference<List<BillingContact>>() {
        });
        return CollectionUtils.isEmpty(billingContacts) ? Optional.empty()
                : Optional.ofNullable(billingContacts.get(0));
    }

    private Optional<CustomerLeContactDetailBean> getCustomerLeContact(OrderToLe orderToLe)
            throws TclCommonException, IllegalArgumentException {
        LOGGER.info("MDC Filter token value in before Queue call getCustomerLeContact {} :",
                MDC.get(CommonConstants.MDC_TOKEN_KEY));
        String response = (String) mqUtils.sendAndReceive(customerLeContactQueueName,
                String.valueOf(orderToLe.getErfCusCustomerLegalEntityId()));
        List<CustomerLeContactDetailBean> customerLeContacts = GscUtils.fromJson(response,
                new TypeReference<List<CustomerLeContactDetailBean>>() {
                });
        return CollectionUtils.isEmpty(customerLeContacts) ? Optional.empty()
                : Optional.ofNullable(customerLeContacts.get(0));
    }

    private BillingEntity getBillingEntity(Optional<BillingContact> billingContactOpt,
                                           CustomerLeDetailsBean customerLeDetailsBean, String orgId, Integer orderId) {
        BillingEntity billingEntity = new BillingEntity();
        billingContactOpt.ifPresent(billingContact -> {
            Address billingAddress = new Address();
            billingAddress.setAddressLine1(billingContact.getBillAddr());
            Contact contact = new Contact();
            if (Objects.nonNull(billingContact.getEmailId())) {
                contact.setEmail(billingContact.getEmailId());
            } else {
                customerLeDetailsBean.getAttributes().stream().filter(
                        attributes -> LeAttributesConstants.LE_EMAIL.equalsIgnoreCase(attributes.getAttributeName()))
                        .findFirst().ifPresent(attributes -> contact.setEmail(attributes.getAttributeValue()));
            }
            contact.setMobile(Optional.ofNullable(billingContact.getMobileNumber())
                    .orElse(Optional.ofNullable(billingContact.getPhoneNumber()).orElse("")));
            contact.setName(billingContact.getFname() + " " + billingContact.getLname());
            billingEntity.setAddress(billingAddress);
            billingEntity.setContact(contact);
            BillingProfile billingProfile = tigerServiceHelper.getBillingProfileForOrgId(orderId, orgId);
            billingEntity.setBillingProfileId(billingProfile.getProfileRelNo());
            billingEntity.setBillingEntityName(customerLeDetailsBean.getLegalEntityName());
        });
        return billingEntity;
    }

    private BusinessEntity getTechnicalEntity(Optional<CustomerLeContactDetailBean> customerLeContactOpt) {
        BusinessEntity businessEntity = new BusinessEntity();
        customerLeContactOpt.ifPresent(customerLeContact -> {
            Address address = new Address();
            address.setAddressLine1(customerLeContact.getAddress());
            Contact contact = new Contact();
            contact.setName(customerLeContact.getName());
            contact.setMobile(Optional.ofNullable(customerLeContact.getMobilePhone())
                    .orElse(Optional.ofNullable(customerLeContact.getHomePhone())
                            .orElse(Optional.ofNullable(customerLeContact.getOtherPhone()).orElse(""))));
            contact.setEmail(customerLeContact.getEmailId());
            contact.setFax(customerLeContact.getFax());
            businessEntity.setAddress(address);
            businessEntity.setContact(contact);
        });
        return businessEntity;
    }

    private ContractingEntity getContractingEntity(BusinessEntity technicalEntity, String customerName) {
        ContractingEntity contractingEntity = new ContractingEntity();
        contractingEntity.setAddress(technicalEntity.getAddress());
        contractingEntity.setContact(technicalEntity.getContact());
        contractingEntity.setEntityName(customerName);
        return contractingEntity;
    }

    private Party getTataParty(String orgId) {
        Party party = new Party();
        party.setRequestor(Boolean.FALSE);
        party.setRole("tataEntity");
        party.setOrgId(orgId);
        return party;
    }

    private Party getCustomerParty(String orgNo, Optional<CustomerLeContactDetailBean> customerLeContactOpt) {
        Party party = new Party();
        try {
            party.setOrgId(orgNo);
            party.setRequestor(Boolean.TRUE);
            party.setRole("customerEntity");
            party.setContact(getTechnicalEntity(customerLeContactOpt).getContact());
        } catch (Exception ex) {
            throw new TclCommonRuntimeException("Customer orgId not found for order"+ex.getMessage(),ex);
        }
        return party;
    }

    /**
     * Return max(requestor service date) from all configurations
     *
     * @param orderGscDetails
     * @return
     */
    private String getRequestedCompletionDate(Collection<OrderGscDetail> orderGscDetails) {
        LocalDateTime dateTime = orderGscDetails.stream()
                .map(orderGscDetail -> getProductComponentAttributes(GSC_CONFIG_PRODUCT_COMPONENT_TYPE.toUpperCase(),
                        GSC_CONFIG_PRODUCT_COMPONENT_TYPE, orderGscDetail, ATTR_REQUESTOR_DATE_FOR_SERVICE))
                .map(map -> map.get(ATTR_REQUESTOR_DATE_FOR_SERVICE))
                .map(dateStr -> Objects.nonNull(dateStr)
                        ? LocalDateTime.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                        : null)
                .filter(Objects::nonNull).max(Comparator.naturalOrder()).orElse(LocalDateTime.now());
        return new SimpleDateFormat(TIGER_SERVICE_DATE_FORMAT)
                .format(Date.from(dateTime.toInstant(ZoneOffset.UTC)));
    }

    public Integer getOrderId(BaseOrderManagementBean baseOrderManagementBean) {
        return Ints.tryParse(baseOrderManagementBean.getOrderName().split("-")[1]);
    }

    private <T extends BaseOrderManagementBean> T buildBaseOrderValues(T orderManagementRequest, OrderToLe orderToLe)
            throws TclCommonException {
        Objects.requireNonNull(orderToLe.getErfCusCustomerLegalEntityId(), "Customer LE Id cannot be null");
        Objects.requireNonNull(orderToLe.getErfCusSpLegalEntityId(), "Supplier LE Id cannot be null");
        orderManagementRequest.setCorrID(UUID.randomUUID().toString());
        orderManagementRequest.setDescription(
                String.format("%s - %s", GSC_ORDER_PRODUCT_COMPONENT_TYPE, orderToLe.getOrder().getOrderCode()));
        orderManagementRequest.setState(GscConstants.ORDER_STATE_SUBMIT);
        orderManagementRequest.setRequestedCompletionDate(null);
        orderManagementRequest.setExpectedCompletionDate(null);
        orderManagementRequest.setOrderName(String.format("%s-%s-%s", GSC_ORDER_PRODUCT_COMPONENT_TYPE,
                orderToLe.getOrder().getId(), orderToLe.getOrder().getOrderCode()));

        orderManagementRequest.setOptimusId(orderToLe.getOrder().getOrderCode());
        //orderManagementRequest.setSolutionId("123456789");
        orderManagementRequest.setSolutionName(GscConstants.GSC_PRODUCT_NAME.toUpperCase());
        orderManagementRequest.setProductIdentifier(GscConstants.TIGER_OPTIMUS_PRODUCTIDENTIFIER);

        // Get customer legal entity's SFDC - CUID
        CustomerLeAttributeRequestBean customerLeAttributeRequestBean = new CustomerLeAttributeRequestBean();
        customerLeAttributeRequestBean.setCustomerLeId(orderToLe.getErfCusCustomerLegalEntityId());
        customerLeAttributeRequestBean.setProductName(GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE.toUpperCase());
        LOGGER.info("MDC Filter token value in before Queue call buildBaseOrderValues {} :",
                MDC.get(CommonConstants.MDC_TOKEN_KEY));
        String customerLeAttributesResponse = (String) mqUtils.sendAndReceive(customerLeAttrQueueProduct,
                GscUtils.toJson(customerLeAttributeRequestBean));
        CustomerLeDetailsBean customerLeDetailsBean = GscUtils.fromJson(customerLeAttributesResponse,
                CustomerLeDetailsBean.class);
//        Optional<Organisation> customerOrganisationOpt = tigerServiceHelper
//                .getOrganisationByCuId(customerLeDetailsBean.getAccounCuId(), orderToLe.getOrder().getId(), "order");
//        LeSapCodeResponse leSapCodeResponse = gscOrderDetailService.getCustomerLeSapCodeDetails(orderToLe.getErfCusCustomerLegalEntityId());
//        LeSapCodeBean leSapCodeBean = leSapCodeResponse.getLeSapCodes().stream().findFirst().get();
//        LOGGER.info("leSapCodeBean :: {}", leSapCodeBean.toString());
//        // SECS Code and Org No both are same
//        String orgNo = leSapCodeBean.getCodeValue();

        // Get supplier legal entity's SFDC - CUID
        CustomerLeAttributeRequestBean supplierAttributeRequest = new CustomerLeAttributeRequestBean();
        supplierAttributeRequest.setProductName(GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE.toUpperCase());
        supplierAttributeRequest.setCustomerLeId(orderToLe.getErfCusSpLegalEntityId());
        LOGGER.info("MDC Filter token value in before Queue call buildBaseOrderValues {} :",
                MDC.get(CommonConstants.MDC_TOKEN_KEY));
        String supplierLeAttributesResponse = (String) mqUtils.sendAndReceive(supplierLeAttrQueueProduct,
                GscUtils.toJson(supplierAttributeRequest));
        CustomerLeDetailsBean supplierLeDetailsBean = GscUtils.fromJson(supplierLeAttributesResponse,
                CustomerLeDetailsBean.class);
//        Optional<Organisation> supplierOrganisationOpt = tigerServiceHelper
//                .getOrganisationByCuId(supplierLeDetailsBean.getAccounCuId(), orderToLe.getOrder().getId(), "order");
        LeSapCodeResponse leSapCodeResponseSupplier = gscOrderDetailService.getSupplierLeSapCodeDetails(orderToLe.getErfCusSpLegalEntityId());
        LeSapCodeBean leSapCodeBeanSupplier = leSapCodeResponseSupplier.getLeSapCodes().stream().findFirst().get();
        LOGGER.info("leSapCodeBean :: {}", leSapCodeBeanSupplier.toString());
        // SECS Code and Org No both are same
        String supplierSECSCode = leSapCodeBeanSupplier.getCodeValue();
        LOGGER.info("supplierSECSCode :: {}", supplierSECSCode);

        Optional<BillingContact> billingContactOpt = getBillingContact(orderToLe);
        BillingEntity billingEntity = getBillingEntity(billingContactOpt, customerLeDetailsBean, getSecsId(orderToLe), orderToLe.getOrder().getId());
        orderManagementRequest.setBillingEntity(billingEntity);
        Optional<CustomerLeContactDetailBean> customerLeContact = getCustomerLeContact(orderToLe);
        BusinessEntity technicalEntity = getTechnicalEntity(customerLeContact);
        orderManagementRequest.setTechnicalEntity(technicalEntity);
        orderManagementRequest.setContractingEntity(
                getContractingEntity(technicalEntity, customerLeDetailsBean.getLegalEntityName()));
        Party customerParty = getCustomerParty(getSecsId(orderToLe), customerLeContact);
        List<Party> relatedParties = new ArrayList<>();
        relatedParties.add(customerParty);

        Party supplierParty = getTataParty(supplierSECSCode);
        relatedParties.add(supplierParty);
        // set party ids in increasing sequence
        for (int i = 0; i < relatedParties.size(); i++) {
            relatedParties.get(i).setPartyId(String.valueOf(i + 1));
        }

        orderManagementRequest.setRelatedParties(relatedParties);
        return orderManagementRequest;
    }

    private String getSecsId(OrderToLe orderToLe) {
        return ordersLeAttributeValueRepository.findByMstOmsAttribute_NameAndOrderToLe(ATTR_CUSTOMER_SECS_ID, orderToLe)
                .stream().findFirst().get().getAttributeValue();
    }

    private String getInterconnectId(OrderToLe orderToLe) {
        String interconnectValue = "";
        if(!(REQUEST_TYPE_CHANGE_OUTPULSE.equalsIgnoreCase(orderToLe.getOrderCategory()) ||
                REQUEST_TYPE_NUMBER_REMOVE.equalsIgnoreCase(orderToLe.getOrderCategory()))) {
            String interconnectId = ordersLeAttributeValueRepository.findByMstOmsAttribute_NameAndOrderToLe(INTERCONNECT_ID, orderToLe)
                    .stream().findFirst().get().getAttributeValue();
            interconnectValue = additionalServiceParamRepository.findById(Integer.valueOf(interconnectId)).get().getValue();
        }
        return interconnectValue;
    }

    @Autowired
    OdrAdditionalServiceParamRepository odrAdditionalServiceParamRepository;
    
    private Number buildNumber(OrderGsc orderGsc, OrderGscDetail orderGscDetail, OrderGscTfn orderGscTfn) {
        Number number = new Number();
        List<OrderProductComponentsAttributeValue> outpulseAttribute = getAllNumbers(orderGsc, orderGscDetail, ATTR_TERMINATION_NUMBER_OUTPULSE);
		String outplus = orderGscTfn.getTfnNumber().split(COLON)[0];
		outpulseAttribute.stream().forEach(action -> {
			try {
				String type ="";
				OdrAdditionalServiceParamBean oaspBean = new OdrAdditionalServiceParamBean();
				if (outplus.equalsIgnoreCase(action.getAttributeValues())) {
					OdrAdditionalServiceParam oasp = odrAdditionalServiceParamRepository
							.findByReferenceId(action.getId().toString());
					if (Objects.nonNull(oasp)) {
						type = oasp.getValue();
						oaspBean = Utils.convertJsonToObject(type,
							OdrAdditionalServiceParamBean.class);
					}else {
						oaspBean.setOriginNetwork(CALL_TYPE_FIXED);
					}

					// number.setAccessNumber(orderGscTfn.getTfnNumber());
					number.setPortingRequired(STATUS_ACTIVE_BYTE.equals(orderGscTfn.getIsPorted()));
					CallType callType = new CallType();
					callType.setClid(CALL_TYPE_CLID_REALTIME);
					callType.setOutpulseType(CALL_TYPE_OUTPULSE_TYPE_CUSTOM);
					callType.setConnectionDtgHeader(CALL_TYPE_CONNXN_DTG_HEADER);

					String referenceOrderId = getProductMasterAttribute(orderGsc, orderGscDetail,
							GSC_MACD_PRODUCT_REFERENCE_ORDER_ID);
					callType.setReferenceOrderId(referenceOrderId);

					if (orderGscTfn.getTfnNumber().contains(COLON)) {
						number.setAccessNumber(orderGscTfn.getTfnNumber().split(COLON)[1]);
						callType.setOutpulse(orderGscTfn.getTfnNumber().split(COLON)[0]);
						callType.setType(oaspBean.getOriginNetwork());
					} else {
						number.setAccessNumber("");
						callType.setOutpulse(orderGscTfn.getTfnNumber());
						callType.setType(oaspBean.getOriginNetwork());
					}
//			        if (PSTN.equalsIgnoreCase(orderGsc.getAccessType()) || CHANGING_OUTPULSE.equalsIgnoreCase(orderCategory)) {
//		            String outpulse = getProductMasterAttribute(orderGsc, orderGscDetail, ATTR_TERMINATION_NUMBER_OUTPULSE);
//		            String outpulseISDCode = getProductMasterAttribute(orderGsc, orderGscDetail, TERMINATION_NUMBER_ISD_CODE);
//		            outpulseISDCode = outpulseISDCode.replaceAll("\\+", "");
//		            callType.setOutpulse(outpulseISDCode.concat(outpulse));
//		        }
					number.setCallType(ImmutableList.of(callType));

				}
			} catch (Exception e) {
				LOGGER.debug("Error in converting OdrAdditionalServiceParamBean");
				e.printStackTrace();
			}
		});

        
        if (PSTN.equalsIgnoreCase(orderGsc.getAccessType())) {
            TerminationDetail terminationDetail = new TerminationDetail();
            String[] dest = orderGscDetail.getDest().split(COLON);
            String countryName = dest[0];
            GscRepcCountries.GscCountry destinationCountry = gscRepcCountries.forName(countryName);
            Objects.requireNonNull(destinationCountry, String.format("Country with name: %s not found", countryName));
            terminationDetail.setCountryAbbr(destinationCountry.getCode());
            terminationDetail.setTermDetailsId(String.valueOf(1));
            number.setTerminationDetails(ImmutableList.of(terminationDetail));
        } else {
            TerminationDetail terminationDetail = new TerminationDetail();
            terminationDetail.setTermDetailsId(String.valueOf(1));
            String interconnectId = getInterconnectId(orderGsc.getOrderToLe());
            Objects.requireNonNull(interconnectId, "Interconnect ID can not be null");
            terminationDetail.setExistingConnectionID(interconnectId);
            number.setTerminationDetails(ImmutableList.of(terminationDetail));
        }
        return number;
    }

    private Number buildNumberForCity(OrderGsc orderGsc, OrderGscDetail orderGscDetail, OrderGscTfn orderGscTfn) {
        Number number = new Number();
        //number.setAccessNumber(orderGscTfn.getTfnNumber());
        number.setPortingRequired(STATUS_ACTIVE_BYTE.equals(orderGscTfn.getIsPorted()));
        CallType callType = new CallType();
        callType.setType(CALL_TYPE_FIXED);
        callType.setClid(CALL_TYPE_CLID_REALTIME);
        callType.setOutpulseType(CALL_TYPE_OUTPULSE_TYPE_CUSTOM);
        callType.setConnectionDtgHeader(CALL_TYPE_CONNXN_DTG_HEADER);

        String referenceOrderId = getProductMasterAttribute(orderGsc, orderGscDetail, GSC_MACD_PRODUCT_REFERENCE_ORDER_ID);
        callType.setReferenceOrderId(referenceOrderId);
        // 6 types numbers will come - with number inventory
        // 1. 18001257:54789 - Outpulse and Number - done
        // 2. 18001257 - Outpulse - done
        // 3. Same As Number:54789 - Same As Number and Number
        // 4. Prefix-Same As Number:54789 - Same As Number with Prefix and Number
        // 5. Same As Number - Same As Number outpulse
        // 6. Prefix-Same As Number - Same As Number with Prefix outpulse

        // 1,3,4th scenario
        /*if(orderGscTfn.getTfnNumber().contains(COLON)) {
            if(orderGscTfn.getTfnNumber().split(COLON)[0].contains(SAME_AS_NUMBER)) {
                if(orderGscTfn.getTfnNumber().split(COLON)[0].contains(HYPHEN)) {
                    // 4th one
                    if(!LNS.equalsIgnoreCase(orderGsc.getProductName())) {
                        callType.setOutpulseType(SAME_AS_NUMBER_PREFIX_TOLL_FREE);
                    } else {
                        callType.setOutpulseType(SAME_AS_NUMBER_PREFIX_DID);
                    }
                } else {
                    // 3rd one
                    if(!LNS.equalsIgnoreCase(orderGsc.getProductName())) {
                        callType.setOutpulseType(SAME_AS_NUMBER_TOLL_FREE);
                    } else {
                        callType.setOutpulseType(SAME_AS_NUMBER_DID);
                    }
                }
            } else {
                // 1st one
                number.setAccessNumber(orderGscTfn.getTfnNumber().split(COLON)[1]);
                callType.setOutpulse(orderGscTfn.getTfnNumber().split(COLON)[0]);
            }
        } else {
            // 2nd one
            number.setAccessNumber("");
            callType.setOutpulse(orderGscTfn.getTfnNumber());
            // 5, 6th one
            if(orderGscTfn.getTfnNumber().contains(SAME_AS_NUMBER)) {

            }
        }*/

        // 3  types numbers will come - without number inventory
        // 1. 18001257 - Outpulse - done
        // 2. Same As Number - Same As Number outpulse
        // 3. Prefix-Same As Number - Same As Number with Prefix outpulse

        // To identify the type of request
        /*String[] split = orderGscTfn.getTfnNumber().split(COLON);

        if(split.length == 1) {
            number.setAccessNumber("");
            callType.setOutpulse(orderGscTfn.getTfnNumber());
        } else if (split.length == 2) {
            if(SAME_AS_NUMBER.equalsIgnoreCase(orderGscTfn.getTfnNumber().split(COLON)[0])) {
                number.setAccessNumber("");
                callType.setOutpulse("");
                callType.setOutpulseType(SAME_AS_NUMBER_DID);
                if(number.getPortingRequired()) {
                    number.setAccessNumber(orderGscTfn.getTfnNumber().split(COLON)[1]);
                }
            } else {
                number.setAccessNumber(orderGscTfn.getTfnNumber().split(COLON)[1]);
                callType.setOutpulse(orderGscTfn.getTfnNumber().split(COLON)[0]);
            }
        } else if(split.length == 3) {
            number.setAccessNumber("");
            callType.setOutpulse("");
            callType.setOutpulseType(SAME_AS_NUMBER_PREFIX_DID);
            callType.setOutpulsePrefix(orderGscTfn.getTfnNumber().split(COLON)[0]);
            if(number.getPortingRequired()) {
                number.setAccessNumber(orderGscTfn.getTfnNumber().split(COLON)[2]);
            }
        }*/

        if(orderGscTfn.getTfnNumber().contains(COLON)) {
            number.setAccessNumber(orderGscTfn.getTfnNumber().split(COLON)[1]);
            callType.setOutpulse(orderGscTfn.getTfnNumber().split(COLON)[0]);
        } else {
            number.setAccessNumber("");
            callType.setOutpulse(orderGscTfn.getTfnNumber());
        }

//        if (PSTN.equalsIgnoreCase(orderGsc.getAccessType()) || CHANGING_OUTPULSE.equalsIgnoreCase(orderCategory)) {
//            String outpulse = getProductMasterAttribute(orderGsc, orderGscDetail, ATTR_TERMINATION_NUMBER_OUTPULSE);
//            String outpulseISDCode = getProductMasterAttribute(orderGsc, orderGscDetail, TERMINATION_NUMBER_ISD_CODE);
//            outpulseISDCode = outpulseISDCode.replaceAll("\\+", "");
//            callType.setOutpulse(outpulseISDCode.concat(outpulse));
//        }
        number.setCallType(ImmutableList.of(callType));
        if (PSTN.equalsIgnoreCase(orderGsc.getAccessType())) {
            TerminationDetail terminationDetail = new TerminationDetail();
            String[] dest = orderGscDetail.getDest().split(COLON);
            String countryName = dest[0];
            GscRepcCountries.GscCountry destinationCountry = gscRepcCountries.forName(countryName);
            Objects.requireNonNull(destinationCountry, String.format("Country with name: %s not found", countryName));
            terminationDetail.setCountryAbbr(destinationCountry.getCode());
            terminationDetail.setTermDetailsId(String.valueOf(1));
            number.setTerminationDetails(ImmutableList.of(terminationDetail));
        } else {
            TerminationDetail terminationDetail = new TerminationDetail();
            terminationDetail.setTermDetailsId(String.valueOf(1));
            String interconnectId = getInterconnectId(orderGsc.getOrderToLe());
            Objects.requireNonNull(interconnectId, "Interconnect ID can not be null");
            terminationDetail.setExistingConnectionID(interconnectId);
            number.setTerminationDetails(ImmutableList.of(terminationDetail));
        }
        return number;
    }

    public String getProductMasterAttribute(OrderGsc orderGsc, OrderGscDetail orderGscDetail, String attributeName) {
        return mstProductComponentRepository
                .findByNameAndStatus(orderGsc.getAccessType(),
                        GscConstants.STATUS_ACTIVE)
                .stream().findFirst()
                .flatMap(mstProductComponent -> orderProductComponentRepository
                        .findByReferenceIdAndMstProductComponentAndType(orderGscDetail.getId(), mstProductComponent,
                                GSC_ORDER_PRODUCT_COMPONENT_TYPE)
                        .stream().findFirst())
                .map(orderProductComponent -> Tuple.of(orderProductComponent,
                        productAttributeMasterRepository
                                .findByNameAndStatus(attributeName, STATUS_ACTIVE).stream()
                                .findFirst().orElse(new ProductAttributeMaster())))
                .map(componentMasterAttributePair -> orderProductComponentsAttributeValueRepository
                        .findByOrderProductComponentAndProductAttributeMaster(componentMasterAttributePair._1,
                                componentMasterAttributePair._2))
                .flatMap(orderProductComponentsAttributeValues -> orderProductComponentsAttributeValues.stream()
                        .findFirst().map(OrderProductComponentsAttributeValue::getAttributeValues))
                .orElse("");
    }

    private AccessNumberItem buildAccessNumberItem(Order order, OrderGsc orderGsc, OrderGscDetail orderGscDetail) {
        AccessNumberItem accessNumberItem = new AccessNumberItem();
        String[] src = orderGscDetail.getSrc().split(COLON);
        String countryName = src[0];
        GscRepcCountries.GscCountry originCountry = gscRepcCountries.forName(countryName);
        Objects.requireNonNull(originCountry, String.format("Country with name: %s not found", countryName));
        accessNumberItem.setOriginCountryAbbr(originCountry.getCode());

        List<Number> numbers = new ArrayList<>();
        List<OrderGscTfn> orderGscTfns = new ArrayList<>();
        List<OrderProductComponentsAttributeValue> outpulseAttribute = getAllNumbers(orderGsc, orderGscDetail, ATTR_TERMINATION_NUMBER_OUTPULSE);
        List<OrderProductComponentsAttributeValue> portingAccessNumberAttribute = getAllNumbers(orderGsc, orderGscDetail, REQUIRED_PORTING_NUMBERS);
        // TODO: Check TFN Table and prepare number bank selection
//        List<OrderProductComponentsAttributeValue> numberBankAccessNumberAttribute = getAllNumbers(orderGsc, orderGscDetail, REQUIRED_PORTING_NUMBERS);

        //List<OrderGscTfn> outpulseNumbers = buildOrderGscTfnNumbers1(outpulseAttribute, portingAccessNumberAttribute, STATUS_INACTIVE_BYTE);
        List<OrderGscTfn> outpulseNumbers = buildOrderGscTfnNumbers(outpulseAttribute, STATUS_INACTIVE_BYTE);
        //List<OrderGscTfn> portingNumbers = buildOrderGscTfnNumbers(portingAccessNumberAttribute, STATUS_ACTIVE_BYTE);

        if (!CollectionUtils.isEmpty(portingAccessNumberAttribute)) {
            for (int i = 0; i < portingAccessNumberAttribute.size(); i++) {
                OrderGscTfn orderGscTfn = outpulseNumbers.get(i);
                orderGscTfn.setTfnNumber(orderGscTfn.getTfnNumber() +COLON+ portingAccessNumberAttribute.get(i).getAttributeValues());
                orderGscTfn.setIsPorted(STATUS_ACTIVE_BYTE);
            }
        }

        if (!CollectionUtils.isEmpty(outpulseNumbers)) {
            Optional.ofNullable(outpulseNumbers).ifPresent(orderGscTfns::addAll);
        }

//        if (!CollectionUtils.isEmpty(portingNumbers)) {
//            Optional.ofNullable(portingNumbers).ifPresent(orderGscTfns::addAll);
//        }

        String orderCategory = getOrderCategory(order);
        if(CHANGING_OUTPULSE.equalsIgnoreCase(orderCategory)) {
            orderGscTfns = getTollFreeNumberForChangeOutpulse(orderGscDetail, orderGscTfns);
        } else if(DELETE_NUMBER.equalsIgnoreCase(orderCategory)) {
            orderGscTfns = getTollFreeNumberOnlyForDeleteNumber(orderGscDetail, orderGscTfns);
        }

        if (CollectionUtils.isEmpty(orderGscTfns)) {
            orderGscTfns.add(new OrderGscTfn());
        }
//        List<OrderGscTfn> orderGscTfns = orderGscTfnRepository.findByOrderGscDetail(orderGscDetail);
        numbers = orderGscTfns.stream().map(orderGscTfn -> buildNumber(orderGsc, orderGscDetail, orderGscTfn)).collect(Collectors.toList());

        // if type is same as number
        // then change the logic after build number
        numbers = checkTerminationOutpulseType(orderGsc, orderGscDetail, numbers);

        for (int i = 0; i < numbers.size(); i++) {
            numbers.get(i).setNumberDetailsId(String.valueOf(i + 1));
        }
        accessNumberItem.setNumbers(numbers);
        return accessNumberItem;
    }

    private List<Number> checkTerminationOutpulseType(OrderGsc orderGsc, OrderGscDetail orderGscDetail, List<Number> numbers) {
        List<OrderProductComponentsAttributeValue> outpulseAttributeForType = getAllNumbers(orderGsc, orderGscDetail, TERMINATION_OUTPULSE_TYPE);
        if (!CollectionUtils.isEmpty(outpulseAttributeForType)) {
            String terminationType = outpulseAttributeForType.stream().findAny().get().getAttributeValues();
            if (terminationType.equalsIgnoreCase(SAME_AS_NUMBER) || terminationType.equalsIgnoreCase(SAME_AS_NUMBER_PREFIX)) {
                numbers.stream().forEach(number -> {
                    if (!number.getPortingRequired().booleanValue()) {
                        number.setAccessNumber("");
                    }
                    number.getCallType().stream().forEach(callType -> {
                        if (terminationType.equalsIgnoreCase(SAME_AS_NUMBER)) {
                            callType.setOutpulseType(SAME_AS_NUMBER_TOLL_FREE);
                            callType.setOutpulsePrefix("");
                        } else if (terminationType.equalsIgnoreCase(SAME_AS_NUMBER_PREFIX)) {
                            callType.setOutpulseType(SAME_AS_NUMBER_PREFIX_TOLL_FREE);
                            callType.setOutpulsePrefix(callType.getOutpulse().split(HYPHEN)[0]);
                        }
                        callType.setOutpulse("");
                    });
                });
            }
        }
        return numbers;
    }

    private List<Number> checkTerminationOutpulseTypeForCity(OrderGsc orderGsc, OrderGscDetail orderGscDetail, List<Number> numbers) {
        List<OrderProductComponentsAttributeValue> outpulseAttributeForType = getAllNumbers(orderGsc, orderGscDetail, TERMINATION_OUTPULSE_TYPE);
        if (!CollectionUtils.isEmpty(outpulseAttributeForType)) {
            String terminationType = outpulseAttributeForType.stream().findAny().get().getAttributeValues();
            if(terminationType.equalsIgnoreCase(SAME_AS_NUMBER) || terminationType.equalsIgnoreCase(SAME_AS_NUMBER_PREFIX)) {
                numbers.stream().forEach(number -> {
                    if (!number.getPortingRequired().booleanValue()) {
                        number.setAccessNumber("");
                    }
                    number.getCallType().stream().forEach(callType -> {
                        if (terminationType.equalsIgnoreCase(SAME_AS_NUMBER)) {
                            callType.setOutpulseType(SAME_AS_NUMBER_DID);
                            callType.setOutpulsePrefix("");
                        } else if (terminationType.equalsIgnoreCase(SAME_AS_NUMBER_PREFIX)) {
                            callType.setOutpulseType(SAME_AS_NUMBER_PREFIX_DID);
                            callType.setOutpulsePrefix(callType.getOutpulse());
                        }
                        callType.setOutpulse("");
                    });
                });
            }
        }
        return numbers;
    }

    private String getOrderCategory(Order order) {
        return order.getOrderToLes().stream().findFirst().get().getOrderCategory();
    }

    private List<OrderGscTfn> getTollFreeNumberOnlyForDeleteNumber(OrderGscDetail orderGscDetail, List<OrderGscTfn> orderGscTfns) {
        //if(DELETE_NUMBER.equalsIgnoreCase(orderCategory)) {
            List<OrderGscTfn> removeNumbers = orderGscTfnRepository.findByOrderGscDetail(orderGscDetail);
            if(!CollectionUtils.isEmpty(removeNumbers)) {
                //Optional.ofNullable(removeNumbers).ifPresent(orderGscTfns::addAll);
                orderGscTfns.addAll(removeNumbers.stream().map(orderGscTfn -> {
                    orderGscTfn.setTfnNumber("" + COLON + orderGscTfn.getTfnNumber());
                    return orderGscTfn;
                }).collect(Collectors.toList()));
            }
//        }
        return orderGscTfns;
    }

    private List<OrderGscTfn> getTollFreeNumberForChangeOutpulse(OrderGscDetail orderGscDetail, List<OrderGscTfn> orderGscTfns) {
        List<OrderGscTfn> accessNumbers = orderGscTfnRepository.findByOrderGscDetail(orderGscDetail);
        if(!CollectionUtils.isEmpty(accessNumbers)) {
            //Optional.ofNullable(accessNumbers).ifPresent(orderGscTfns::addAll);
            accessNumbers.stream().forEach(accessNumber -> {
                orderGscTfns.stream().forEach(outpulseNumber -> {
                    outpulseNumber.setTfnNumber(outpulseNumber.getTfnNumber() +COLON+ accessNumber.getTfnNumber());
                });
            });
        }
        return orderGscTfns;
    }

    private AccessNumberItem buildAccessNumberItemForCity(OrderGsc orderGsc, OrderGscDetail orderGscDetail, OrderGscTfn orderGscTfn) {
        AccessNumberItem accessNumberItem = new AccessNumberItem();
        String[] srcCity = orderGscTfn.getCountryCode().split(COLON);
        if (1 == srcCity.length) {
            return accessNumberItem;
        }
        String[] src = orderGscDetail.getSrc().split(COLON);
        String countryName = src[0];
        GscRepcCountries.GscCountry originCountry = gscRepcCountries.forName(countryName);
        Objects.requireNonNull(originCountry, String.format("Country with name: %s not found", countryName));
        accessNumberItem.setOriginCountryAbbr(originCountry.getCode());
        accessNumberItem.setOriginCityAbbr(orderGscTfn.getCountryCode().split(COLON)[1]);
        accessNumberItem.setSupplierID("");
        List<Number> numbers = Arrays.asList(buildNumber(orderGsc, orderGscDetail, orderGscTfn));

        // if type is same as number
        // then change the logic after build number
        numbers = checkTerminationOutpulseTypeForCity(orderGsc, orderGscDetail, numbers);

        for (int i = 0; i < numbers.size(); i++) {
            numbers.get(i).setNumberDetailsId(String.valueOf(i + 1));
        }
        accessNumberItem.setNumbers(numbers);
        return accessNumberItem;
    }


    private List<OrderGscTfn> buildOrderGscTfnNumbers(List<OrderProductComponentsAttributeValue> attributeValues, Byte isPorting) {
        return attributeValues.stream().map(attributeValue -> {
            if (!StringUtils.isEmpty(attributeValue.getAttributeValues())) {
                OrderGscTfn orderGscTfn = new OrderGscTfn();
//                orderGscTfn.setOutpulseNumber(attributeValue.getAttributeValues());
                orderGscTfn.setTfnNumber(attributeValue.getAttributeValues());
                orderGscTfn.setIsPorted(isPorting);
                return orderGscTfn;
            }
            return null;
        }).collect(Collectors.toList()).stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    private List<OrderGscTfn> buildOrderGscTfnNumbers1(List<OrderProductComponentsAttributeValue> outpulseNumber,
                                                       List<OrderProductComponentsAttributeValue> portingNumber,
                                                       Byte isPorting) {
        return outpulseNumber.stream().map(attributeValue -> {
            if (!StringUtils.isEmpty(attributeValue.getAttributeValues())) {
                OrderGscTfn orderGscTfn = new OrderGscTfn();
                orderGscTfn.setTfnNumber(attributeValue.getAttributeValues());
                orderGscTfn.setIsPorted(isPorting);
                return orderGscTfn;
            }
            return null;
        }).collect(Collectors.toList()).stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    private List<OrderProductComponentsAttributeValue> getAllNumbers(OrderGsc orderGsc, OrderGscDetail orderGscDetail, String attribute) {
        return mstProductComponentRepository.findByNameAndStatus(orderGsc.getAccessType()
                , GscConstants.STATUS_ACTIVE).stream().findFirst()
                .flatMap(mstProductComponent -> orderProductComponentRepository
                        .findByReferenceIdAndMstProductComponentAndType(orderGscDetail.getId(), mstProductComponent,
                                GSC_ORDER_PRODUCT_COMPONENT_TYPE)
                        .stream().findFirst())
                .map(orderProductComponent -> orderProductComponentsAttributeValueRepository
                        .findByOrderProductComponentAndProductAttributeMaster(orderProductComponent,
                                productAttributeMasterRepository
                                        .findByNameAndStatus(attribute, STATUS_ACTIVE).stream().findFirst().get())).get();
    }


    private Map<String, String> getProductComponentAttributes(String componentName, String referenceType,
                                                              OrderGscDetail orderGscDetail, String... attributeNames) {
        List<ProductAttributeMaster> attributeMasters = productAttributeMasterRepository
                .findByNameIn(Arrays.asList(attributeNames));
        return mstProductComponentRepository.findByNameAndStatus(componentName, GscConstants.STATUS_ACTIVE).stream()
                .findFirst()
                .flatMap(mstProductComponent -> orderProductComponentRepository
                        .findByReferenceIdAndMstProductComponentAndType(orderGscDetail.getId(), mstProductComponent,
                                referenceType)
                        .stream().findFirst())
                .map(orderProductComponent -> orderProductComponentsAttributeValueRepository
                        .findAllByOrderProductComponentAndProductAttributeMasterIn(orderProductComponent,
                                attributeMasters))
                .map(orderProductComponentsAttributeValues -> orderProductComponentsAttributeValues.stream()
                        .collect(Collectors.toMap(attribute -> attribute.getProductAttributeMaster().getName(),
                                OrderProductComponentsAttributeValue::getAttributeValues)))
                .orElse(ImmutableMap.of());

    }

    private String getRequestType(OrderToLe orderToLe) {
        if (GscConstants.ORDER_TYPE_MACD.equalsIgnoreCase(orderToLe.getOrderType())) {
            switch (orderToLe.getOrderCategory()) {
                case MACDOrderRequest.REQUEST_TYPE_NUMBER_ADD:
                    return ACCESS_SERVICE_REQUEST_TYPE_NEW_NUMBER;
                case REQUEST_TYPE_NUMBER_REMOVE:
                    return ACCESS_SERVICE_REQUEST_TYPE_CANCEL_NUMBER;
                case MACDOrderRequest.REQUEST_TYPE_ADD_COUNTRY:
                    return ACCESS_SERVICE_REQUEST_TYPE_ADD_COUNTRIES;
                case REQUEST_TYPE_CHANGE_OUTPULSE:
                    return ACCESS_SERVICE_REQUEST_TYPE_CHANGE_OUTPULSE;
                case MACDOrderRequest.REQUEST_TYPE_INTERCONNECT_ATTRIBUTE:
                case MACDOrderRequest.REQUEST_TYPE_AGGREGATE_SIP_TRUNK:
                    return ACCESS_SERVICE_REQUEST_TYPE_NEW_NUMBER;
                default:
                    throw new RuntimeException(
                            String.format("Unsupported MACD request type: %s", orderToLe.getOrderCategory()));
            }
        } else {
            return ACCESS_SERVICE_ACCESS_TYPE_NEW_SERVICE;
        }
    }

    private AccessServiceOrderItem buildAccessServiceOrderItem(Order order, OrderToLe orderToLe, OrderGsc orderGsc) {
        AccessServiceOrderItem orderItem = new AccessServiceOrderItem();
        orderItem = setServiceName(orderItem, orderGsc);
        orderItem.setRequestType(getRequestType(orderToLe));
        if (PSTN.equalsIgnoreCase(orderGsc.getAccessType())) {
            orderItem.setAccessType(PSTN);
        } else {
            orderItem.setAccessType(ACCESS_SERVICE_ACCESS_TYPE_DIRECT_CONNXN);
            orderItem.setDirectConnectionType(MPLS);
        }
        orderItem.setWithICR("false");
        orderItem.setWithIVR("false");
        orderItem.setPreferredCurrency(orderToLe.getCurrencyCode());
        List<AccessNumberItem> accessNumberItems = orderGscDetailRepository.findByorderGsc(orderGsc).stream()
                .map(orderGscDetail -> buildAccessNumberItem(order, orderGsc, orderGscDetail))
                .collect(Collectors.toList());

        if (LNS.equalsIgnoreCase(orderGsc.getProductName()) || ACANS.equalsIgnoreCase(orderGsc.getProductName())) {
            List<AccessNumberItem> accessNumberItemsForCity = orderGscDetailRepository.findByorderGsc(orderGsc).stream()
                    .map(orderGscDetail -> orderGscTfnRepository.findByOrderGscDetail(orderGscDetail).stream()
                            .map(orderGscTfn -> buildAccessNumberItemForCity(orderGsc, orderGscDetail, orderGscTfn)))
                    .flatMap(accessNumberItem -> accessNumberItem)
                    .filter(accessNumberItem -> Objects.nonNull(accessNumberItem.getNumbers()))
                    .collect(Collectors.toList());
            accessNumberItemsForCity.stream().forEach(accessNumberItems::add);

        }
        for (int i = 0; i < accessNumberItems.size(); i++) {
            accessNumberItems.get(i).setNumberItemId(String.valueOf(i + 1));
        }
        orderItem.setAccessNumberItems(accessNumberItems);
        return orderItem;
    }

    private AccessServiceOrderItem setServiceName(AccessServiceOrderItem orderItem, OrderGsc orderGsc) {
        if (ACDTFS.equalsIgnoreCase(orderGsc.getProductName())) {
            orderItem.setServiceName("Audio Conferencing DTF Service");
        } else if (ACANS.equalsIgnoreCase(orderGsc.getProductName())) {
            orderItem.setServiceName("Audio Conferencing Access Number Service");
        } else {
            orderItem.setServiceName(orderGsc.getProductName());
        }
        return orderItem;
    }

    private <T extends InternationalOrderManagementRequest> T buildAccessServiceOrderItems(T orderManagementRequest,
                                                                                           Order order, OrderToLe orderToLe, List<OrderGsc> orderGscs) {
        List<AccessServiceOrderItem> accessServiceOrderItems = orderGscs.stream()
                .map(orderGsc -> buildAccessServiceOrderItem(order, orderToLe, orderGsc)).collect(Collectors.toList());
        orderManagementRequest.setAccessServiceOrderItems(accessServiceOrderItems);
        for (int i = 0; i < accessServiceOrderItems.size(); i++) {
            accessServiceOrderItems.get(i).setOrderItemId(String.valueOf(i + 1));
        }
        List<OrderGscDetail> orderGscDetails = orderGscs.stream().map(OrderGsc::getOrderGscDetails).flatMap(Set::stream)
                .collect(Collectors.toList());
        orderManagementRequest.setRequestedCompletionDate(getRequestedCompletionDate(orderGscDetails));
        orderManagementRequest.setSFDCOPTYID(orderToLe.getTpsSfdcCopfId());
        return orderManagementRequest;
    }

    /**
     * Build International Order Request for Tiger
     *
     * @param order
     * @return {@link InternationalOrderManagementRequest}
     */
    public InternationalOrderManagementRequest buildInternationalOrderRequest(Order order) {
        OrderToLe orderToLe = gscOrderService.getOrderToLeByOrder(order);
        List<OrderGsc> orderGscs = Optional
                .<Collection<OrderToLeProductFamily>>ofNullable(orderToLe.getOrderToLeProductFamilies())
                .orElseGet(() -> orderToLeProductFamilyRepository.findByOrderToLe(orderToLe)).stream()
                .map(orderProductSolutionRepository::findByOrderToLeProductFamily).flatMap(List::stream)
                .map(orderProductSolution -> orderGscRepository
                        .findByorderProductSolutionAndStatus(orderProductSolution, GscConstants.STATUS_ACTIVE))
                .flatMap(List::stream)
                .filter(orderGsc -> !GscConstants.DOMESTIC_VOICE.equalsIgnoreCase(orderGsc.getProductName())
                        && !GscConstants.GLOBAL_OUTBOUND.equalsIgnoreCase(orderGsc.getProductName()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(orderGscs)) {
            return new InternationalOrderManagementRequest();
        } else {
            return Try.success(new InternationalOrderManagementRequest())
                    .mapTry(request -> buildBaseOrderValues(request, orderToLe))
                    .map(request -> buildAccessServiceOrderItems(request, order, orderToLe, orderGscs))
                    .getOrElseThrow(e -> new TCLException(
                            String.format("Error occurred while building Order management request for order id: %s",
                                    order.getId()),
                            e));
        }
    }

    /**
     * Get Interconnect ID for each Order Gsc
     *
     * @param orderGscInterConnects
     * @param orderGscs
     */
    private void getOrderGscInterConnect(List<InterConnectDetail> orderGscInterConnects, List<OrderGsc> orderGscs) {
        Map<OrderGsc, String> mapInterconnect = new HashMap<>();
        orderGscs.stream().forEach(orderGsc -> {
            orderGscInterConnects.stream().filter(orderGscInterConnect -> orderGscInterConnect.getServiceType().equalsIgnoreCase(orderGsc.getProductName()))
                    .map(orderGscInterConnect -> mapInterconnect.put(orderGsc, orderGscInterConnect.getInterconnectID()));
        });
        setOrderGscInterconnect(mapInterconnect);
    }

    /**
     * Create OrderGsc level attributes
     *
     * @param orderGscInterConnects
     */
    private void setOrderGscInterconnect(Map<OrderGsc, String> orderGscInterConnects) {
        orderGscInterConnects.forEach((orderGsc, interConnectId) -> {
            final Map<String, String> orderAttributes = ImmutableMap.of(GscAttributeConstants.GSC_INTERCONNECT_DOWNSTREAM_ORDER_ID,
                    interConnectId);
            gscOrderService.saveOrderGscAttributes(orderGsc, orderAttributes);
        });
    }

}
