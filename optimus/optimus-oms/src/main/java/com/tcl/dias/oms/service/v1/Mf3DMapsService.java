package com.tcl.dias.oms.service.v1;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import com.tcl.dias.oms.beans.FeasibilitySiteDetailBean;
import com.tcl.dias.oms.beans.MfFeasibility3DRequestDataBean;
import com.tcl.dias.oms.beans.MstMfProductBean;
import com.tcl.dias.oms.entity.entities.Customer;
import com.tcl.dias.oms.entity.entities.MstMfPrefeasibleBw;
import com.tcl.dias.oms.entity.entities.MstMfProductEntity;
import com.tcl.dias.oms.entity.repository.CustomerRepository;
import com.tcl.dias.oms.entity.repository.MstMfPrefeasibleBwRepository;
import com.tcl.dias.oms.entity.repository.MstMfProductRepository;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.Mf3DResponse;
import com.tcl.dias.common.beans.Mf3DSiteDetailBean;
import com.tcl.dias.common.beans.MfDetailAttributes;
import com.tcl.dias.common.beans.MfDetailsBean;
import com.tcl.dias.common.beans.MfFeasibility3DRequestBean;
import com.tcl.dias.common.beans.OpportunityBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.SiteDetail;
import com.tcl.dias.common.beans.ThirdPartyResponseBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.sfdc.constants.SfdcServiceTypeConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.ThirdPartySource;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.AddressDetail;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.constants.ManualFeasibilityConstants;
import com.tcl.dias.oms.entity.entities.PreMfRequest;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.SiteFeasibility;
import com.tcl.dias.oms.entity.entities.ThirdPartyServiceJob;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.repository.PreMfRequestRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.ill.service.v1.IllPricingFeasibilityService;
import com.tcl.dias.oms.pricing.bean.FeasibilityResponse;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * Service class used for 3d maps  related functions
 *
 
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
@Transactional
public class Mf3DMapsService {
	private static final Logger LOGGER = LoggerFactory.getLogger(Mf3DMapsService.class);
	
	@Autowired
	MQUtils mqUtils;
	
	@Value("${rabbitmq.location.detail}")
	String locationQueue;

	@Autowired
	private PreMfRequestRepository preMfRequestRepository;
	
	@Autowired
	UserRepository userRepository;

	
	@Value("${rabbitmq.manual.feasibility.request}")
	private String manualFeasibilityWorkflowQueue;

	@Autowired
	MstMfProductRepository mstMfProductRepository;
	
	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	private MstMfPrefeasibleBwRepository mstMfPrefeasibleBwRepository;


	
	/**
	 * used to process 3DMf Request
	 * @param MfFeasibility3DRequestBean
	 * @return
	 * @throws TclCommonException
	 */
	public Mf3DResponse process3DMfRequest(MfFeasibility3DRequestBean mfFeasibility3DRequestBean)
			throws TclCommonException {
		LOGGER.info("Entered into process3DMfRequest ");
		try {
			final Integer[] quoteId = { 0 };
			final String[] quoteCode = { "" };
			Mf3DResponse response = new Mf3DResponse();
			List<Mf3DSiteDetailBean> sitedetails = mfFeasibility3DRequestBean.getSiteDetails();
			List<SiteDetail> sites=new ArrayList<SiteDetail>();
			
			if (!sitedetails.isEmpty()) {
				sitedetails.stream().forEach(siteInfo -> {
					if (isSiteDetailsValidate(siteInfo)) {
						SiteDetail site = new SiteDetail();
						PreMfRequest request = new PreMfRequest();
						request.setSiteCode(Utils.generateUid());
						request.setSiteType(siteInfo.getSiteType());
						request.setErfLocSiteaLocationId(Integer.parseInt(siteInfo.getLocationId()));
						request.setCustomerId(mfFeasibility3DRequestBean.getCustomerId());
						if (!mfFeasibility3DRequestBean.getOpportunityId().isEmpty()) {
							request.setOpportunityId(Integer.parseInt(mfFeasibility3DRequestBean.getOpportunityId()));
						}
						if (!mfFeasibility3DRequestBean.getBandwidth().isEmpty()) {
							request.setBandwidth(mfFeasibility3DRequestBean.getBandwidth());
						}
						request.setMfProductId(Integer.parseInt(mfFeasibility3DRequestBean.getProductId()));
						if(!siteInfo.getLconName().isEmpty()) {
						 request.setLocalContactName(siteInfo.getLconName());
						}
						if(!siteInfo.getLconNumber().isEmpty()) {
						  request.setLocalContactNumber(siteInfo.getLconNumber());
						}
						request.setStatus("NT");
						request.setFeasibility((byte) 0);
						if (quoteId[0] == 0) {
							Random rand = new Random();
							int rand_int = rand.nextInt(10000);
							request.setMfQuoteId(rand_int);
						} else {
							request.setMfQuoteId(quoteId[0]);
						}
						if (quoteCode[0].isEmpty()) {
							request.setMfQuoteCode(
									Utils.generateRefId(mfFeasibility3DRequestBean.getProductName().toUpperCase()));
						} else {
							request.setMfQuoteCode(quoteCode[0]);
						}
						request.setRequestData(mfFeasibility3DRequestBean.getRequestData());
						request.setCreatedTime(new Date());
						User user = getUserId(Utils.getSource());
						if (user != null) {
							request.setCreated_by(user.getId());
						}
						request = preMfRequestRepository.save(request);

						quoteId[0] = request.getMfQuoteId();
						quoteCode[0] = request.getMfQuoteCode();
						response.setQuoteCode(quoteCode[0]);
						response.setQuoteId(quoteId[0]);
						site.setSiteId(request.getId());
						site.setSiteCode(request.getSiteCode());
						sites.add(site);
						
					}
					
				});
				
				response.setSiteDetails(sites);
			}

			return response;

		} catch (Exception e) {
			if (e instanceof TclCommonRuntimeException) {
				throw e;
			} else
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

	}
	
	/**
	 * used to validate sitedetails
	 * @param MfFeasibility3DRequestBean
	 * @return
	 * @throws TclCommonException
	 */
	public boolean isSiteDetailsValidate(Mf3DSiteDetailBean mf3DSiteDetailBean) {
		boolean site=true;
		if (mf3DSiteDetailBean.getSiteType().equalsIgnoreCase("site B")) {
			if (mf3DSiteDetailBean.getAddress().isEmpty() && mf3DSiteDetailBean.getCity().isEmpty()
					&& mf3DSiteDetailBean.getCountry().isEmpty() && mf3DSiteDetailBean.getLatitude().isEmpty()
					&& mf3DSiteDetailBean.getLconName().isEmpty() && mf3DSiteDetailBean.getLconNumber().isEmpty()
					&& mf3DSiteDetailBean.getLocality().isEmpty() && mf3DSiteDetailBean.getLocationId().isEmpty()
					&& mf3DSiteDetailBean.getLongitude().isEmpty() && mf3DSiteDetailBean.getPincode().isEmpty()
					&& mf3DSiteDetailBean.getRemarks().isEmpty()) {
				LOGGER.info("enter into empty site");
				site = false;
			}
		}
		return site;
		
	}
	
	/**
	 * 
	 * getUserId-This method get the user details if present or persist the user and
	 * get the entity
	 * 
	 * @param userData
	 * @return User
	 */
	protected User getUserId(String username) {
		return userRepository.findByUsernameAndStatus(username, 1);
	}
	
	
	private Double getCharges(Object charge) {
		Double mfCharge = 0.0D;
		if (charge != null) {
			if (charge instanceof Double) {
				mfCharge = (Double) charge;
			} else if (charge instanceof String && !charge.equals("")) {
				mfCharge = new Double((String) charge);
			} else if (charge instanceof Long) {
				mfCharge = new Double((Long) charge);
			} else if (charge instanceof Integer) {
				mfCharge = new Double((Integer) charge);
			}
		}
		return mfCharge;
	}
	
	
	/**
	 * used to process 3DMf Request
	 * @param MfFeasibility3DRequestBean
	 * @return
	 * @throws TclCommonException
	 */
	public Mf3DResponse trigger3DMfTask(Mf3DResponse response) throws TclCommonException {
		LOGGER.info("Entered into process3DMfRequest ");
		try {
			List<PreMfRequest> preMfRequestList = new ArrayList<PreMfRequest>();
			Mf3DResponse mf3DResponse = new Mf3DResponse();
			if (!response.getQuoteCode().isEmpty() && response.getQuoteCode() != null) {
				preMfRequestList = preMfRequestRepository.findByMfQuoteCode(response.getQuoteCode());
				if (!preMfRequestList.isEmpty()) {
					preMfRequestList.stream().forEach(request -> {
						MfFeasibility3DRequestBean mfFeasibility3DRequestBean=new MfFeasibility3DRequestBean();
						try {
						 mfFeasibility3DRequestBean = (MfFeasibility3DRequestBean) Utils
								.convertJsonToObject(request.getRequestData(), MfFeasibility3DRequestBean.class);
						}
						catch(Exception e) {
							LOGGER.warn("trigger3DMfTask: Error in converting string to bean {}",
									ExceptionUtils.getStackTrace(e));
						}
						if (Objects.nonNull(mfFeasibility3DRequestBean)) {
							List<Mf3DSiteDetailBean> sitedetails = mfFeasibility3DRequestBean.getSiteDetails();
							MfDetailsBean mfDetailsBean = new MfDetailsBean();
							MfDetailAttributes mfDetailAttributes = new MfDetailAttributes();
							for (Mf3DSiteDetailBean siteInfo : sitedetails) {
								if (request.getSiteType().equalsIgnoreCase(siteInfo.getSiteType())) {
									LOGGER.info("sitetype request and site" + request.getSiteType() + ":"
											+ siteInfo.getSiteType());
									mfDetailsBean.setAssignedTo(ManualFeasibilityConstants.AFM);
									mfDetailsBean.setCreatedBy(Utils.getSource());
									User user = getUserId(Utils.getSource());
									if (user != null) {
										mfDetailsBean.setCreatedByEmail(user.getEmailId());
									}
									mfDetailsBean.setCreatedTime(new Date());
									mfDetailsBean.setIs3DMaps(true);
									mfDetailsBean.setIsActive(ManualFeasibilityConstants.ACTIVE);
									mfDetailsBean.setProductName(mfFeasibility3DRequestBean.getProductName());
									mfDetailsBean.setQuoteCode(request.getMfQuoteCode());
									mfDetailsBean.setQuoteId(request.getMfQuoteId());
									mfDetailsBean.setSiteCode(request.getSiteCode());
									mfDetailsBean.setSiteId(request.getId());
									mfDetailsBean.setSiteType(request.getSiteType());
									mfDetailsBean.setUpdatedBy(Utils.getSource());
									mfDetailsBean.setUpdatedTime(new Date());
									mfDetailsBean.setStatus(ManualFeasibilityConstants.OPEN_STATUS);

									AddressDetail addressDetail = new AddressDetail();
									try {
										LOGGER.info("Before location queuecall" + request.getErfLocSiteaLocationId());
										String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
												String.valueOf(request.getErfLocSiteaLocationId()));
										LOGGER.info(" location response" + locationResponse);
										if (locationResponse != null && !locationResponse.isEmpty()) {
											addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
													AddressDetail.class);
											// Adding address details to mfAttributes.
											if (addressDetail != null) {
												LOGGER.info("Entered into addresdetails block");
												if(StringUtils.isNoneBlank(addressDetail.getAddressLineOne())) {
												   mfDetailAttributes.setAddressLineOne(addressDetail.getAddressLineOne());
												}
												if (StringUtils.isNoneBlank(addressDetail.getAddressLineTwo())) {
													mfDetailAttributes.setAddressLineTwo(addressDetail.getAddressLineTwo());
												}
												mfDetailAttributes.setCity(addressDetail.getCity());
												mfDetailAttributes.setState(addressDetail.getState());
												mfDetailAttributes.setPincode(addressDetail.getPincode());
												mfDetailAttributes.setCountry(addressDetail.getCountry());
												
												if (StringUtils.isNoneBlank(addressDetail.getLatLong())){
												  mfDetailAttributes.setLatLong(addressDetail.getLatLong());
												}
												
												mfDetailAttributes.setLocationId(request.getErfLocSiteaLocationId());
												
												if (StringUtils.isNoneBlank(addressDetail.getLocality())){
													mfDetailAttributes.setLocality(addressDetail.getLocality());
												}
												mfDetailsBean.setRegion(addressDetail.getRegion());

											}
											LOGGER.info("Region for the locationId {} : {} ",
													request.getErfLocSiteaLocationId(), addressDetail.getRegion());
										} else {
											LOGGER.warn("Location data not found for the locationId {} ",
													request.getErfLocSiteaLocationId());
										}
									} catch (Exception e) {
										LOGGER.warn("process3dMFeasibilityRequest: Error in invoking locationQueue {}",
												ExceptionUtils.getStackTrace(e));
									}
									
									//IF LOACTION QUEUE IS BLOCKED
									if(StringUtils.isEmpty(mfDetailsBean.getRegion())) {
										mfDetailsBean.setRegion("RON");
								     }
									
									//PRV TASK ASSIGN IF BW EXCEED preFeasibleBw
									/*if (mfFeasibility3DRequestBean.getProductName().equalsIgnoreCase("IAS")
											|| mfFeasibility3DRequestBean.getProductName().equalsIgnoreCase("GVPN")) {
										if (!mfFeasibility3DRequestBean.getBandwidth().isEmpty()) {
											String[] bandwidth = mfFeasibility3DRequestBean.getBandwidth().split(" ");
											int portCapacity = Integer.parseInt(bandwidth[0]);
											Integer preFeasibleBw = 0;
											String assinedTo = "";
											MstMfPrefeasibleBw bw = mstMfPrefeasibleBwRepository
													.findByLocationAndProduct(addressDetail.getCity(),
															mfFeasibility3DRequestBean.getProductName());
											if (bw != null) {
												preFeasibleBw = bw.getPreFeasibleBwNew();
											}
											if (portCapacity != 0 && preFeasibleBw != 0) {
												if (portCapacity > preFeasibleBw) {
													assinedTo = ManualFeasibilityConstants.PRV;
												} else if (portCapacity <= preFeasibleBw) {
													assinedTo = ManualFeasibilityConstants.AFM;
													if (StringUtils.isEmpty(mfDetailsBean.getRegion()))
														mfDetailsBean.setRegion("RON");
												}
											}
											mfDetailsBean.setAssignedTo(assinedTo);
										}
									}*/
									//

									mfDetailAttributes.setCustomerCode(mfFeasibility3DRequestBean.getCustomerCode());
									if (!mfFeasibility3DRequestBean.getLastMileContactTerm().isEmpty()) {
										mfDetailAttributes.setLastMileContractTerm(
												mfFeasibility3DRequestBean.getLastMileContactTerm());
									}
									if (!siteInfo.getLconNumber().isEmpty()) {
										mfDetailAttributes.setLconContactNum(siteInfo.getLconNumber());
									}
									if (!siteInfo.getLconName().isEmpty()) {
										mfDetailAttributes.setLconName(siteInfo.getLconName());
									}
									if (!siteInfo.getRemarks().isEmpty()) {
										mfDetailAttributes.setLconSalesRemarks(siteInfo.getRemarks());
									}
									mfDetailAttributes
											.setOpportunityAccountName(mfFeasibility3DRequestBean.getCustomerName());
									if (!mfFeasibility3DRequestBean.getBandwidth().isEmpty()) {
										mfDetailAttributes.setBandwidth(mfFeasibility3DRequestBean.getBandwidth());
									}
									mfDetailAttributes.setQuoteType("NEW");
									mfDetailAttributes.setCustomerSegment("");
									mfDetailAttributes.setChangeBandwidthFlag("");
									mfDetailAttributes.setLocalLoopBandwidth(0.0);
									mfDetailAttributes.setLocalLoopInterface("");
									mfDetailAttributes.setMfInterface("");
									mfDetailAttributes.setOpportunityStage("");
									mfDetailAttributes.setOppurtunityAccountEmail("");
									mfDetailAttributes.setParallelBuild("");
									mfDetailAttributes.setParallelRunDays("");
									mfDetailAttributes.setQuoteCategory("");
									mfDetailAttributes.setQuoteStage("");
									mfDetailAttributes.setCustomerName(mfFeasibility3DRequestBean.getCustomerName());

									mfDetailsBean.setMfDetails(mfDetailAttributes);
									try {
										LOGGER.info("process3DManualFeasibilityRequest : invoking workflow queue {}  ",
												manualFeasibilityWorkflowQueue);
										mqUtils.send(manualFeasibilityWorkflowQueue,
												Utils.convertObjectToJson(mfDetailsBean));
										LOGGER.info("After queue call Task Triggered to MF Workbench");
										
										request.setStatus("MF");
										preMfRequestRepository.save(request);
										
										mf3DResponse.setQuoteCode(request.getMfQuoteCode());
										mf3DResponse.setQuoteId(request.getMfQuoteId());
										mf3DResponse.setSiteCode(request.getSiteCode());
										mf3DResponse.setIsTaskTrigger(true);

									} catch (Exception e) {
										LOGGER.warn("process3DManualFeasibilityRequest: Error in FP {}",
												ExceptionUtils.getStackTrace(e));
									}

								}

							}

						}

					});
				}

			}
			
			return mf3DResponse;

		} catch (Exception e) {
			if (e instanceof TclCommonRuntimeException) {
				throw e;
			} else
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		
	}

	/**
	 * service layer for getting the product list
	 * @author Chetan Chaudhary
	 *
	 * @return
	 */
	public List<MstMfProductBean>getMstMfProductList(){
		LOGGER.info("Entered into MstMfProductList");
		List<MstMfProductBean> mstMfProductBeans=new ArrayList<>();
		try{
			List<MstMfProductEntity>mstMfProductEntities= mstMfProductRepository.findAll();
			if(Objects.nonNull(mstMfProductEntities)){
				mstMfProductEntities.stream().forEach(
						mstMfProductEntity->{
							MstMfProductBean mstMfProductBean=new MstMfProductBean();
							mstMfProductBean.setId(mstMfProductEntity.getId());
							mstMfProductBean.setProductName(mstMfProductEntity.getName());
							mstMfProductBean.setStatus(mstMfProductEntity.getStatus());
							mstMfProductBeans.add(mstMfProductBean);
						}
				);
			}
		}
		catch(Exception e){
			LOGGER.info(" Error in finding the product list{} " , e);
		}
		return mstMfProductBeans;
	}
	
	/**
	 * 
	 * Method to get oppurtunity Details for GVPN
	 * @param quoteId
	 * @param siteId
	 * @return
	 * @throws TclCommonException
	 */
	public OpportunityBean getMf3DOpportunityDetails(Integer quoteId, Integer siteId) throws TclCommonException {
		LOGGER.info(
				"Inside getMf3DOpportunityDetails.getOpportunityDetails to fetch opportunity details for the quoteId {} ",
				quoteId);
		OpportunityBean opporBean = new OpportunityBean();
		try {
			Optional<PreMfRequest> preMfRequest = preMfRequestRepository.findById(siteId);
			if (preMfRequest == null) {
				throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
			}
			Optional<Customer> customer = customerRepository.findById(preMfRequest.get().getCustomerId());
			Optional<User> user = userRepository.findById(preMfRequest.get().getCreated_by());
			opporBean.setOpportunityStage(SFDCConstants.CREATE_OPTY);
			opporBean.setOpportunityAccountName(customer.get().getCustomerName());
			opporBean.setOpportunityOwnerEmail(user.get().getEmailId());
			opporBean.setOpportunityOwnerName(user.get().getUsername());
			opporBean.setSiteContactName(preMfRequest.get().getLocalContactName());
			opporBean.setSiteLocalContactNumber(preMfRequest.get().getLocalContactNumber());
			MfFeasibility3DRequestBean mfFeasibility3DRequestBean = new MfFeasibility3DRequestBean();
			try {
				mfFeasibility3DRequestBean = (MfFeasibility3DRequestBean) Utils
						.convertJsonToObject(preMfRequest.get().getRequestData(), MfFeasibility3DRequestBean.class);
			} catch (Exception e) {
				LOGGER.warn("getMf3DOpportunityDetails: Error in converting string to bean {}",
						ExceptionUtils.getStackTrace(e));
			}
			List<Mf3DSiteDetailBean> sitedetails = mfFeasibility3DRequestBean.getSiteDetails();
			for (Mf3DSiteDetailBean siteInfo : sitedetails) {
				if (preMfRequest.get().getSiteType().equalsIgnoreCase(siteInfo.getSiteType())) {
					opporBean.setSalesRemarks(siteInfo.getRemarks());
				}
			}
			LOGGER.warn("opportunity id ****************"+preMfRequest.get().getOpportunityId());
				opporBean.setMf3DOptyId(preMfRequest.get().getOpportunityId());
			
		} catch (Exception e) {
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
					ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
		}

		return opporBean;
	}
    /**
     * Method to get feasible site details for 3D maps
     *
     * @param quoteId
     * @return mfFeasibility3DRequestDataBean
     * @throws TclCommonException
     * @author Chetan chaudhary
     */
    public MfFeasibility3DRequestDataBean getMf3DFeasibleSiteDetails(Integer quoteId) throws TclCommonException {
        LOGGER.info("Inside getMf3DFeasibleSiteDetails method of Mf3DMapService to fetch feasible site details for the quoteId {} ",
                quoteId);
        MfFeasibility3DRequestDataBean mfFeasibility3DRequestDataBean = new MfFeasibility3DRequestDataBean();
        List<PreMfRequest> preMfRequests = preMfRequestRepository.findByMfQuoteId(quoteId);
        if (Objects.isNull(preMfRequests)) {
            throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
        }
        try {
            LOGGER.info(" streaming the pre mf request list ");
            List<FeasibilitySiteDetailBean> feasibilitySiteDetailBeanList = new ArrayList<>();
            preMfRequests.stream().forEach(preMfRequest -> {
                mfFeasibility3DRequestDataBean.setQuoteId(preMfRequest.getMfQuoteId());
                mfFeasibility3DRequestDataBean.setQuoteCode(preMfRequest.getMfQuoteCode());
                mfFeasibility3DRequestDataBean.setCustomerId(preMfRequest.getCustomerId());
                mfFeasibility3DRequestDataBean.setBandwidth(preMfRequest.getBandwidth());
                mfFeasibility3DRequestDataBean.setProductId(preMfRequest.getMfProductId());
                mfFeasibility3DRequestDataBean.setOpportunityId(preMfRequest.getOpportunityId());
                MfFeasibility3DRequestBean mfFeasibility3DRequestBean = new MfFeasibility3DRequestBean();
                try {
                    mfFeasibility3DRequestBean = new MfFeasibility3DRequestBean();
                    mfFeasibility3DRequestBean = (MfFeasibility3DRequestBean) Utils
                            .convertJsonToObject(preMfRequest.getRequestData(), MfFeasibility3DRequestBean.class);

                } catch (Exception e) {
                    LOGGER.warn("getMf3DFeasibleSiteDetails: Error in converting string to bean {}",
                            ExceptionUtils.getStackTrace(e));
                }
                mfFeasibility3DRequestDataBean.setIsLatLong(mfFeasibility3DRequestBean.getIsLatLong());
                List<Mf3DSiteDetailBean> sitedetails = mfFeasibility3DRequestBean.getSiteDetails();
                for (Mf3DSiteDetailBean mf3DSiteDetailBean : sitedetails) {
                    if (mf3DSiteDetailBean.getSiteType().equalsIgnoreCase(preMfRequest.getSiteType())) {
                        mfFeasibility3DRequestDataBean.setProductName(mfFeasibility3DRequestBean.getProductName());
                        mfFeasibility3DRequestDataBean.setSfdcAccountId(mfFeasibility3DRequestBean.getSfdcAccountId());
                        mfFeasibility3DRequestDataBean.setCustomerName(mfFeasibility3DRequestBean.getCustomerName());
                        mfFeasibility3DRequestDataBean.setCustomerCode(mfFeasibility3DRequestBean.getCustomerCode());
                        mfFeasibility3DRequestDataBean.setLastMileContactTerms(mfFeasibility3DRequestBean.getLastMileContactTerm());
                        FeasibilitySiteDetailBean siteDetailBean = new FeasibilitySiteDetailBean();
                        siteDetailBean.setSiteId(preMfRequest.getId());
                        siteDetailBean.setSiteCode(preMfRequest.getSiteCode());
                        siteDetailBean.setFeasibility(preMfRequest.getFeasibility());
                        siteDetailBean.setSiteType(preMfRequest.getSiteType());
                        siteDetailBean.setLongitude(mf3DSiteDetailBean.getLongitude());
                        siteDetailBean.setLatitude(mf3DSiteDetailBean.getLatitude());
                        siteDetailBean.setFpstatus(preMfRequest.getStatus());
                        siteDetailBean.setCountry(mf3DSiteDetailBean.getCountry());
                        siteDetailBean.setCity(mf3DSiteDetailBean.getCity());
                        siteDetailBean.setPincode(mf3DSiteDetailBean.getPincode());
                        siteDetailBean.setLocality(mf3DSiteDetailBean.getLocality());
                        siteDetailBean.setAddress(mf3DSiteDetailBean.getAddress());
                        siteDetailBean.setIconNumber(mf3DSiteDetailBean.getLconNumber());
                        siteDetailBean.setIconName(mf3DSiteDetailBean.getLconName());
                        siteDetailBean.setRemarks(mf3DSiteDetailBean.getRemarks());
                        siteDetailBean.setLocationId(mf3DSiteDetailBean.getLocationId());
                        feasibilitySiteDetailBeanList.add(siteDetailBean);
                        mfFeasibility3DRequestDataBean.setFeasibilitySiteDetailBeanList(feasibilitySiteDetailBeanList);
                    }
                }
            });
        } catch (Exception e) {
            throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
                    ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
        }
        return mfFeasibility3DRequestDataBean;
    }
}
