package com.tcl.dias.oms.crossconnect.service.v1;

import com.tcl.dias.common.beans.GstAddressBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.AttributeDetail;
import com.tcl.dias.oms.beans.OrderIllSiteBean;
import com.tcl.dias.oms.beans.OrderProductComponentBean;
import com.tcl.dias.oms.beans.OrderProductComponentsAttributeValueBean;
import com.tcl.dias.oms.beans.OrderSlaBean;
import com.tcl.dias.oms.beans.QuotePriceBean;
import com.tcl.dias.oms.beans.SlaMasterBean;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.IllSitePropertiesConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.entity.entities.AdditionalServiceParams;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.OrderIllSite;
import com.tcl.dias.oms.entity.entities.OrderPrice;
import com.tcl.dias.oms.entity.entities.OrderProductComponent;
import com.tcl.dias.oms.entity.entities.OrderProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.OrderProductSolution;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.SlaMaster;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.repository.AdditionalServiceParamRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSitesRepository;
import com.tcl.dias.oms.entity.repository.OrderPriceRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.gst.service.GstInService;
import com.tcl.dias.oms.npl.service.v1.NplOrderService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static com.tcl.dias.oms.npl.pdf.constants.NplPDFConstants.CROSS_CONNECT_LOCAL_DEMARCATION_ID;

@Service
@Transactional
public class CrossConnectOrderService {
    @Autowired
    OrderIllSitesRepository orderIllSitesRepository;

    @Autowired
    OrderProductComponentRepository orderProductComponentRepository;

    @Autowired
    OrderPriceRepository orderPriceRepository;

    @Autowired
    MstProductFamilyRepository mstProductFamilyRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MstProductComponentRepository mstProductComponentRepository;

    @Autowired
    ProductAttributeMasterRepository productAttributeMasterRepository;

    @Autowired
    OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;
    
    @Autowired
	GstInService gstInService;
	
	@Autowired
	AdditionalServiceParamRepository additionalServiceParamRepository;
	
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(CrossConnectOrderService.class);

    /**
     *
     * @link http://www.tatacommunications.com/ getSortedIllSiteDtos
     * @param illSites,version
     * @return List<QuoteIllSiteBean>
     */
    public List<OrderIllSiteBean> getSortedIllSiteDtos(List<OrderIllSiteBean> illSiteBeans) {
        if (illSiteBeans != null) {
            illSiteBeans.sort(Comparator.comparingInt(OrderIllSiteBean::getId));

        }

        return illSiteBeans;
    }
    /**
     * @link http://www.tatacommunications.com/ constructIllSiteDtos
     * @param illSites,version
     * @return List<OrderIllSiteBean>
     */
    public List<OrderIllSiteBean> constructIllSiteDtos(List<OrderIllSite> illSites) {
        List<OrderIllSiteBean> sites = new ArrayList<>();
        if (illSites != null) {
            for (OrderIllSite illSite : illSites) {
                if (illSite.getStatus() == 1) {
                    OrderIllSiteBean illSiteBean = new OrderIllSiteBean(illSite);
                    illSiteBean.setOrderSla(constructSlaDetails(illSite));
                    //illSiteBean.setSiteFeasibility(constructSiteFeasibility(illSite));
                    if (illSite.getMstOrderSiteStage() != null) {
                        illSiteBean.setCurrentStage(illSite.getMstOrderSiteStage().getName());
                    }
                    if (illSite.getMstOrderSiteStatus() != null) {
                        illSiteBean.setCurrentStatus(illSite.getMstOrderSiteStatus().getName());
                    }
                    if(illSite.getErfServiceInventoryTpsServiceId() != null)
                    {
                        illSiteBean.setErfServiceInventoryTpsServiceId(illSite.getErfServiceInventoryTpsServiceId());
                    }
                    List<OrderProductComponentBean> orderProductComponentBeans = getSortedComponents(
                            constructOrderProductComponent(illSite.getId(), illSite.getOrderProductSolution()
                                    .getOrderToLeProductFamily().getMstProductFamily().getName()));
                    illSiteBean.setOrderProductComponentBeans(orderProductComponentBeans);
                    sites.add(illSiteBean);
                }
            }
        }
        return sites;
    }
    /**
     * @link http://www.tatacommunications.com/
     * @throws TclCommonException
     */
    public List<OrderIllSite> getIllsitesBasenOnVersion(OrderProductSolution productSolution) {
        List<OrderIllSite> illsites = null;
        illsites = orderIllSitesRepository.findByOrderProductSolutionAndStatus(productSolution, (byte) 1);
        return illsites;

    }
    /**
     * constructSlaDetails
     *
     * @param illSite
     */
    private List<OrderSlaBean> constructSlaDetails(OrderIllSite illSite) {

        List<OrderSlaBean> orderSlas = new ArrayList<>();
        if (illSite.getOrderIllSiteSlas() != null) {

            illSite.getOrderIllSiteSlas().forEach(siteSla -> {
                OrderSlaBean sla = new OrderSlaBean();
                sla.setSlaEndDate(siteSla.getSlaEndDate());
                sla.setSlaStartDate(siteSla.getSlaStartDate());
                sla.setSlaValue(Utils.convertEval(siteSla.getSlaValue()));
                if (siteSla.getSlaMaster() != null) {
                    SlaMaster slaMaster = siteSla.getSlaMaster();
                    SlaMasterBean master = new SlaMasterBean();
                    master.setId(slaMaster.getId());
                    master.setSlaDurationInDays(slaMaster.getSlaDurationInDays());
                    master.setSlaName(slaMaster.getSlaName());
                    sla.setSlaMaster(master);
                }

                orderSlas.add(sla);
            });
        }

        return orderSlas;

    }
    /**
     * @link http://www.tatacommunications.com/ constructOrderProductComponent
     * @param id,version
     */
    private List<OrderProductComponentBean> constructOrderProductComponent(Integer id, String productFamilyName) {
        List<OrderProductComponentBean> orderProductComponentDtos = new ArrayList<>();
        List<OrderProductComponent> productComponents = getComponentBasenOnVersion(id, productFamilyName);

        if (productComponents != null) {

            for (OrderProductComponent orderProductComponent : productComponents) {
                OrderProductComponentBean orderProductComponentBean = new OrderProductComponentBean();
                orderProductComponentBean.setId(orderProductComponent.getId());
                orderProductComponentBean.setReferenceId(orderProductComponent.getReferenceId());
                if (orderProductComponent.getMstProductComponent() != null) {
                    orderProductComponentBean
                            .setDescription(orderProductComponent.getMstProductComponent().getDescription());
                    orderProductComponentBean.setName(orderProductComponent.getMstProductComponent().getName());
                }
                orderProductComponentBean.setType(orderProductComponent.getType());
                orderProductComponentBean.setPrice(constructComponentPriceDto(orderProductComponent));
                List<OrderProductComponentsAttributeValueBean> attributeValueBeans = getSortedAttributeComponents(
                        constructAttribute(orderProductComponent.getOrderProductComponentsAttributeValues()));
                orderProductComponentBean.setOrderProductComponentsAttributeValues(attributeValueBeans);
                orderProductComponentDtos.add(orderProductComponentBean);
            }

        }
        return orderProductComponentDtos;

    }
    /**
     * @link http://www.tatacommunications.com/ getSortedIllSiteDtos
     * @param illSites,version
     * @return List<QuoteIllSiteBean>
     */
    private List<OrderProductComponentBean> getSortedComponents(List<OrderProductComponentBean> orderComponentBeans) {
        if (orderComponentBeans != null) {
            orderComponentBeans.sort(Comparator.comparingInt(OrderProductComponentBean::getId));

        }

        return orderComponentBeans;
    }
    /**
     * @link http://www.tatacommunications.com/
     * @throws TclCommonException
     */
    private List<OrderProductComponent> getComponentBasenOnVersion(Integer siteId, String productFamilyName) {
        List<OrderProductComponent> components = null;
        components = orderProductComponentRepository.findByReferenceIdAndMstProductFamily_Name(siteId,
                productFamilyName);
        return components;

    }
    /**
     * @link http://www.tatacommunications.com/
     * @param orderProductComponent
     */
    private QuotePriceBean constructComponentPriceDto(OrderProductComponent orderProductComponent) {
        QuotePriceBean priceDto = null;
        if (orderProductComponent != null && orderProductComponent.getMstProductComponent() != null) {
            OrderPrice price = orderPriceRepository.findByReferenceIdAndReferenceName(
                    String.valueOf(orderProductComponent.getId()), QuoteConstants.COMPONENTS.toString());
            if (price != null) {
                priceDto = new QuotePriceBean(price);
            }
        }
        return priceDto;

    }
    /**
     * @link http://www.tatacommunications.com/ getSortedIllSiteDtos
     * @param illSites,version
     * @return List<QuoteIllSiteBean>
     */
    private List<OrderProductComponentsAttributeValueBean> getSortedAttributeComponents(
            List<OrderProductComponentsAttributeValueBean> attributeBeans) {
        if (attributeBeans != null) {
            attributeBeans.sort(Comparator.comparingInt(OrderProductComponentsAttributeValueBean::getId));

        }

        return attributeBeans;
    }
    /**
     * @link http://www.tatacommunications.com
     * @param orderProductComponentsAttributeValues
     * @return
     */
    private List<OrderProductComponentsAttributeValueBean> constructAttribute(
            Set<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues) {
        List<OrderProductComponentsAttributeValueBean> orderProductComponentsAttributeValueBean = new ArrayList<>();
        if (orderProductComponentsAttributeValues != null) {
            for (OrderProductComponentsAttributeValue attributeValue : orderProductComponentsAttributeValues) {
                OrderProductComponentsAttributeValueBean qtAttributeValue = new OrderProductComponentsAttributeValueBean(
                        attributeValue);
                ProductAttributeMaster productAttributeMaster = attributeValue.getProductAttributeMaster();
                if (productAttributeMaster != null) {
                    qtAttributeValue.setDescription(productAttributeMaster.getDescription());
                    qtAttributeValue.setName(productAttributeMaster.getName());
                }

                qtAttributeValue.setPrice(constructAttributePriceDto(attributeValue));
                orderProductComponentsAttributeValueBean.add(qtAttributeValue);
            }
        }

        return orderProductComponentsAttributeValueBean;
    }
    /**
     * @link http://www.tatacommunications.com/
     * @constructAttributePriceDto
     */
    private QuotePriceBean constructAttributePriceDto(OrderProductComponentsAttributeValue attributeValue) {
        QuotePriceBean priceDto = null;
        if (attributeValue != null && attributeValue.getProductAttributeMaster() != null) {
            OrderPrice attrPrice = orderPriceRepository.findByReferenceIdAndReferenceName(
                    String.valueOf(attributeValue.getId()), QuoteConstants.ATTRIBUTES.toString());
            if (attrPrice != null) {
                priceDto = new QuotePriceBean(attrPrice);
            }
        }
        return priceDto;

    }
    /**
     *
     * getUserId-This method get the user details if present or persist the user and
     * get the entity
     *
     * @param userData
     * @return User
     */
    public User getUserId(String username) {
        User user = null;
        if (username != null && !username.isEmpty()) {
            user = userRepository.findByUsernameAndStatus(username, 1);
        }
        return user;
    }
    /**
     * validateUpdateRequest - validates the UpdateRequest object
     *
     * @param request
     */
    public void validateUpdateRequest(UpdateRequest request) throws TclCommonException {
        if (request == null) {
            throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_ERROR);

        }

    }
    /**
     *
     * @link http://www.tatacommunications.com/ getMstProperties used to get Mst
     *       Properties
     * @param id
     * @param localITContactId
     * @throws TclCommonException
     */
    public MstProductComponent getMstProperties(User user) throws TclCommonException {
        if (Objects.isNull(user)) {
            throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
        }
        MstProductComponent mstProductComponent = null;
        List<MstProductComponent> mstProductComponents = mstProductComponentRepository
                .findByNameAndStatus(IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties(), (byte) 1);
        if (mstProductComponents != null && !mstProductComponents.isEmpty()) {
            mstProductComponent = mstProductComponents.get(0);

        }
        if (mstProductComponent == null) {
            mstProductComponent = new MstProductComponent();
            mstProductComponent.setCreatedBy(user.getUsername());
            mstProductComponent.setCreatedTime(new Date());
            mstProductComponent.setName(IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties());
            mstProductComponent.setDescription(IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties());
            mstProductComponent.setStatus((byte) 1);
            mstProductComponentRepository.save(mstProductComponent);
        }
        return mstProductComponent;

    }
    /**
     * upateSitePropertiesAttribute - updates attributes under site properties
     *
     * @param productAttributeMasters
     * @param attributeDetail
     * @param orderProductComponent
     * @param username
     * @throws TclCommonException
     */
    public void upateSitePropertiesAttribute(List<ProductAttributeMaster> productAttributeMasters,
                                             AttributeDetail attributeDetail, OrderProductComponent orderProductComponent) throws TclCommonException {

        if (Objects.isNull(orderProductComponent) || Objects.isNull(productAttributeMasters)
                || Objects.isNull(attributeDetail)) {
            throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
        }

        List<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues = orderProductComponentsAttributeValueRepository
                .findByOrderProductComponentAndProductAttributeMaster(orderProductComponent,
                        productAttributeMasters.get(0));
        if (orderProductComponentsAttributeValues != null && !orderProductComponentsAttributeValues.isEmpty()) {
            for (OrderProductComponentsAttributeValue orderProductComponentsAttributeValue : orderProductComponentsAttributeValues) {
                orderProductComponentsAttributeValue.setDisplayValue(attributeDetail.getName());
                orderProductComponentsAttributeValue.setAttributeValues(attributeDetail.getValue());
                orderProductComponentsAttributeValueRepository.save(orderProductComponentsAttributeValue);
                if((productAttributeMasters.get(0).getName().equalsIgnoreCase("GSTNO_A")) || (productAttributeMasters.get(0).getName().equalsIgnoreCase("GSTNO_Z"))) {
                    LOGGER.info("Before updating GST address");
                    updateGstAddress(attributeDetail, orderProductComponent);
                }
            }
        } else {

            orderProductComponent.setOrderProductComponentsAttributeValues(
                    createAttributes(productAttributeMasters.get(0), orderProductComponent, attributeDetail));

        }

    }

    private void updateGstAddress(AttributeDetail attributeDetail, OrderProductComponent orderProductComponent)
    {
        List<OrderProductComponentsAttributeValue> gstAddrComps = orderProductComponentsAttributeValueRepository
                .findByOrderProductComponentAndProductAttributeMaster_Name(orderProductComponent,
                        "GST_ADDRESS");
        for (OrderProductComponentsAttributeValue gstAddrComp : gstAddrComps) {
            if(gstAddrComp.getIsAdditionalParam().equals(CommonConstants.Y)) {
                String attrV=gstAddrComp.getAttributeValues();
                Optional<AdditionalServiceParams> additionalServiceParams=additionalServiceParamRepository.findById(Integer.valueOf(attrV));
                if(additionalServiceParams.isPresent()) {
                    additionalServiceParams.get().setValue(getGstAddress(attributeDetail.getValue()));
                    additionalServiceParams.get().setUpdatedBy(Utils.getSource());
                    additionalServiceParams.get().setUpdatedTime(new Date());
                    additionalServiceParamRepository.save(additionalServiceParams.get());
                    LOGGER.info("Updated AdditionalServiceParams for GST address");
                }
            }
        }
        if(gstAddrComps.isEmpty()) {
            LOGGER.info("Before creating GST address");
            createGstAddress(attributeDetail, orderProductComponent);
        }
    }

    /**
     * getPropertiesMaster - retrieves an attribute from attribute master based on
     * name
     *
     * @param name
     * @param attributeDetail
     * @return
     */
    private ProductAttributeMaster getPropertiesMaster(String name, AttributeDetail attributeDetail) {
        ProductAttributeMaster productAttributeMaster = null;

        List<ProductAttributeMaster> productAttributeMasters = productAttributeMasterRepository
                .findByNameAndStatus(attributeDetail.getName(), (byte) 1);
        if (productAttributeMasters != null && !productAttributeMasters.isEmpty()) {
            productAttributeMaster = productAttributeMasters.get(0);
        }

        if (productAttributeMaster == null) {
            productAttributeMaster = new ProductAttributeMaster();
            productAttributeMaster.setCreatedBy(name);
            productAttributeMaster.setCreatedTime(new Date());
            productAttributeMaster.setDescription(attributeDetail.getName());
            productAttributeMaster.setName(attributeDetail.getName());
            productAttributeMaster.setStatus((byte) 1);
            productAttributeMasterRepository.save(productAttributeMaster);
        }
        return productAttributeMaster;

    }
    /**
     * createAttributes - creates attributes for a component
     *
     * @param attributeMaster
     * @param orderProductComponent
     * @param attributeDetail
     */
    private Set<OrderProductComponentsAttributeValue> createAttributes(ProductAttributeMaster attributeMaster,
                                                                       OrderProductComponent orderProductComponent, AttributeDetail attributeDetail) {

        Set<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues = new HashSet<>();

        OrderProductComponentsAttributeValue orderProductComponentsAttributeValue = new OrderProductComponentsAttributeValue();
        orderProductComponentsAttributeValue.setAttributeValues(attributeDetail.getValue());
        orderProductComponentsAttributeValue.setDisplayValue(attributeDetail.getValue());
        orderProductComponentsAttributeValue.setOrderProductComponent(orderProductComponent);
        orderProductComponentsAttributeValue.setProductAttributeMaster(attributeMaster);
        orderProductComponentsAttributeValueRepository.save(orderProductComponentsAttributeValue);
        if (attributeMaster.getName().toLowerCase().contains("GSTNO".toLowerCase())) {
			orderProductComponentsAttributeValues.add(createGstAddress(attributeDetail, orderProductComponent));
		}
        orderProductComponentsAttributeValues.add(orderProductComponentsAttributeValue);

        return orderProductComponentsAttributeValues;

    }
    /**
     * createSitePropertiesAttribute - creates site properties attribute
     *
     * @param orderProductComponent
     * @param attributeDetail
     * @param username
     */
    private void createSitePropertiesAttribute(OrderProductComponent orderProductComponent,
                                               AttributeDetail attributeDetail, String username) {

        ProductAttributeMaster attributeMaster = getPropertiesMaster(username, attributeDetail);
        orderProductComponent.setOrderProductComponentsAttributeValues(
                createAttributes(attributeMaster, orderProductComponent, attributeDetail));

    }
    /**
     * updateIllSiteProperties - udpates site properties
     *
     * @param orderProductComponents
     * @param request
     * @param username
     * @throws TclCommonException
     */
    public void updateIllSiteProperties(List<OrderProductComponent> orderProductComponents, UpdateRequest request,
                                        String username) throws TclCommonException {
        validateUpdateRequest(request);
        if (Objects.isNull(orderProductComponents) || Objects.isNull(username) || StringUtils.isEmpty(username)) {
            throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
        }
        for (OrderProductComponent orderProductComponent : orderProductComponents) {

            if (request.getAttributeDetails() != null) {
                for (AttributeDetail attributeDetail : request.getAttributeDetails()) {

                    List<ProductAttributeMaster> productAttributeMasters = productAttributeMasterRepository
                            .findByNameAndStatus(attributeDetail.getName(), (byte) 1);
                    if (productAttributeMasters != null && !productAttributeMasters.isEmpty()) {
                        upateSitePropertiesAttribute(productAttributeMasters, attributeDetail, orderProductComponent);

                    } else {

                        createSitePropertiesAttribute(orderProductComponent, attributeDetail, username);

                    }

                }
            }

        }

    }
    /**
     * createIllSiteAttribute - creates site attributes
     *
     * @param mstProductComponent
     * @param mstProductFamily
     * @param orderIllSite
     * @param request
     * @param username
     */
    private void createCrossConnectSiteAttribute(MstProductComponent mstProductComponent, MstProductFamily mstProductFamily,
                                        OrderIllSite orderIllSite, UpdateRequest request, String username) {
        OrderProductComponent orderProductComponent = new OrderProductComponent();
        orderProductComponent.setMstProductComponent(mstProductComponent);
        orderProductComponent.setReferenceId(orderIllSite.getId());
        orderProductComponent.setMstProductFamily(mstProductFamily);
        orderProductComponent.setReferenceName(QuoteConstants.ILLSITES.toString());
        orderProductComponentRepository.save(orderProductComponent);

        if (request.getAttributeDetails() != null) {
            for (AttributeDetail attributeDetail : request.getAttributeDetails()) {
                createSitePropertiesAttribute(orderProductComponent, attributeDetail, username);

            }

        }
    }
    /**
     *
     * @link http://www.tatacommunications.com/ constructIllSitePropeties - creates/
     *       updates site properties
     *
     * @param mstProductComponent
     * @param orderIllSite
     * @param username
     * @param localITContactId
     * @param mstProductFamily
     * @throws TclCommonException
     */
    private void constructCrossConnectSitePropeties(MstProductComponent mstProductComponent, OrderIllSite orderIllSite,
                                           String username, UpdateRequest request, MstProductFamily mstProductFamily)
            throws TclCommonException {
        List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
                .findByReferenceIdAndMstProductComponent(orderIllSite.getId(), mstProductComponent);
        if (orderProductComponents != null && !orderProductComponents.isEmpty()) {
            updateIllSiteProperties(orderProductComponents, request, username);
        } else {
            createCrossConnectSiteAttribute(mstProductComponent, mstProductFamily, orderIllSite, request, username);
        }

    }
    /**
     *
     * @link
     * @param orderIllSite
     * @param localITContactId
     * @param mstProductFamily
     * @throws TclCommonException
     */
    private void saveCrossConnectSiteProperties(OrderIllSite orderIllSite, UpdateRequest request, User user,
                                       MstProductFamily mstProductFamily) throws TclCommonException {
        MstProductComponent mstProductComponent = getMstProperties(user);
        constructCrossConnectSitePropeties(mstProductComponent, orderIllSite, user.getUsername(), request, mstProductFamily);

    }

    /**
     * updateSiteProperties - updates site properties
     *
     * @param request
     * @return
     */
    @Transactional
    public OrderIllSiteBean updateOrderCrossConnectSiteProperties(UpdateRequest request) throws TclCommonException {
        OrderIllSiteBean orderIllSiteBean = new OrderIllSiteBean();
        try {
            validateUpdateRequest(request);
            Optional<OrderIllSite> orderIllSite = orderIllSitesRepository.findById(request.getSiteId());
            if (!orderIllSite.isPresent()) {
                throw new TclCommonException(ExceptionConstants.ORDER_ILLSITE_VALIDATION_ERROR,
                        ResponseResource.R_CODE_ERROR);
            }
            User user = getUserId(Utils.getSource());
            MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(request.getFamilyName(),
                    (byte) 1);

            if (mstProductFamily == null) {
                throw new TclCommonException(ExceptionConstants.MST_PRODUCT_EMPTY, ResponseResource.R_CODE_ERROR);

            }
            saveCrossConnectSiteProperties(orderIllSite.get(), request, user, mstProductFamily);
        }

        catch (Exception e) {
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
        }

        return orderIllSiteBean;

    }
    /**
     * @author
     * @link http://www.tatacommunications.com/ updateOrderSites
     *
     * @param request
     * @return
     */
    public Boolean updateCrossConnectOrderSites(UpdateRequest request) throws TclCommonException {
        Boolean response = false;
        try {


            if (request.getSiteId() > 0) {
                OrderIllSite orderIllSiteEntity = orderIllSitesRepository.findByIdAndStatus(request.getSiteId(),
                        (byte) 1);

                if (orderIllSiteEntity != null) {
                    orderIllSiteEntity.setRequestorDate(request.getRequestorDate());
                    orderIllSitesRepository.save(orderIllSiteEntity);
                    response=true;
                }
            }

        } catch (Exception e) {
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
        }

        return response;
    }

    /**
     * Get Local IT Contact And Demarcation ReferenceId
     * @param siteId
     * @return
     */
    public Integer getLocalITContactAndDemarcationReferenceId(Integer siteId) {
        List<OrderProductComponent> components = orderProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndReferenceName(siteId,
                IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties(), QuoteConstants.ILLSITES.toString());
        if (!CollectionUtils.isEmpty(components)) {
            OrderProductComponent orderProductComponentVal = components.stream()
                    .filter(orderProductComponent ->
                            orderProductComponent.getMstProductComponent().getName()
                                    .equalsIgnoreCase(IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties()))
                    .findFirst().get();
            if (Objects.nonNull(orderProductComponentVal)) {
                OrderProductComponentsAttributeValue crossConnectLocalDemarcationId = orderProductComponentVal.getOrderProductComponentsAttributeValues().
                        stream()
                        .filter(orderProductComponentsAttributeValue -> orderProductComponentsAttributeValue.getProductAttributeMaster().getName()
                                .equalsIgnoreCase(CROSS_CONNECT_LOCAL_DEMARCATION_ID)).findFirst().get();
                if (Objects.nonNull(crossConnectLocalDemarcationId.getAttributeValues())) {
                    return Integer.valueOf(crossConnectLocalDemarcationId.getAttributeValues());
                }
            }
        }

        return 0;
    }
    
    /**
	 * createGstAddress
	 * @param attributeDetail
	 * @param orderProductComponent
	 */
	private OrderProductComponentsAttributeValue createGstAddress(AttributeDetail attributeDetail,
			OrderProductComponent orderProductComponent) {
		String address = getGstAddress(attributeDetail.getValue());
		AdditionalServiceParams additionalServiceParam = new AdditionalServiceParams();
		additionalServiceParam.setValue(address);
		additionalServiceParam.setCreatedBy(Utils.getSource());
		additionalServiceParam.setCreatedTime(new Date());
		additionalServiceParam.setIsActive(CommonConstants.Y);
		additionalServiceParam.setAttribute("GST_ADDRESS");
		additionalServiceParamRepository.save(additionalServiceParam);
		List<ProductAttributeMaster> gstAttributeMasters = productAttributeMasterRepository
				.findByNameAndStatus("GST_ADDRESS", (byte) 1);
		if (gstAttributeMasters != null && !gstAttributeMasters.isEmpty()) {
			OrderProductComponentsAttributeValue gstComponentsAttributeValue = new OrderProductComponentsAttributeValue();
			gstComponentsAttributeValue.setAttributeValues(additionalServiceParam.getId()+"");
			gstComponentsAttributeValue.setDisplayValue(additionalServiceParam.getId()+"");
			gstComponentsAttributeValue.setOrderProductComponent(orderProductComponent);
			gstComponentsAttributeValue.setIsAdditionalParam(CommonConstants.Y);
			gstComponentsAttributeValue.setProductAttributeMaster(gstAttributeMasters.get(0));
			orderProductComponentsAttributeValueRepository.save(gstComponentsAttributeValue);
			return gstComponentsAttributeValue;
		}
		return null;
	}
	
	/**
	 * saveGstAddress
	 */
	private String getGstAddress(String gstIn) {
		String gstAddress = null;
		try {
			GstAddressBean gstAddressBean = new GstAddressBean();
			gstInService.getGstAddress(gstIn, gstAddressBean);
			gstAddress = Utils.convertObjectToJson(gstAddressBean);
		} catch (Exception e) {
			LOGGER.error("Error in getting gst address cross connect", e);
		}
		return gstAddress;
	}
}
