package com.tcl.dias.products.service.v1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.tcl.dias.productcatelog.entity.entities.*;
import com.tcl.dias.productcatelog.entity.repository.*;
import com.tcl.dias.products.constants.ProductSpecificConstant;
import com.tcl.dias.products.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.products.constants.DataCenterType;
import com.tcl.dias.products.constants.ExceptionConstants;
import com.tcl.dias.products.constants.ProductId;
import com.tcl.dias.common.beans.BomInventoryCatalogAssocResponse;
import com.tcl.dias.common.beans.DataCenterBean;
import com.tcl.dias.common.beans.GvpnInternationalCpeDto;
import com.tcl.dias.common.beans.ProductInformationBean;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.springframework.util.CollectionUtils;

/**
 * Service class for product related operations
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class ProductsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductsService.class);
	public static final String DSP_MODULE = "DSP module";
	public static final String CUBE_LICENSES = "Cube Licenses";
	public static final String CUBE_T_STD = "CUBE-T-STD";
	public static final String CON_ECMU_CUBETSTD = "CON-ECMU-CUBETSTD";
	public static final String MFT_CARD = "MFT Card";
	public static final String INDIA = "INDIA";
	public static final String CPE_POWER_CORD = "CPE Power Cord";
	public static final String ADDITIONAL_CPE_INFORMATION = "Additional CPE Information";
	public static final String IP_PBX_CUBE = "IP PBX Cube";
	public static final String REDUNDANT = "Redundant";
	public static final String CUBE_T_RED = "CUBE-T-RED";
	public static final String CON_ECMU_CUBETRDE = "CON-ECMU-CUBETRDE";
	public static final String L_CUBE = "L_CUBE";
	public static final String CON_ECMU_LCUBE001 = "CON-ECMU-LCUBE001";
	public static final String SFP = "SFP";
	public static final String CPE_PORT = "CPE Port";
	public static final List<String> customProductCategories = Arrays.asList(SFP,CPE_PORT,CPE_POWER_CORD);
	public static final String NIM_BLANK = "NIM-BLANK";

	@Autowired
	ProductRepository productRepository;

	@Autowired
	ProductFamilyRepository productFamilyRepository;

	@Autowired
	CpeBomViewRepository cpeBomViewRepository;

	@Autowired
	CpeBomGvpnViewRepository cpeBomGvpnViewRepository;

	@Autowired
	BomMasterRepository bomMasterRepository;
	
	@Autowired
	AttributeMasterRepository attributeMasterRepository;
	
	@Autowired
	AttributeGroupAssocRepository attributeGroupAssocRepository;
	
	@Autowired
	AttributeValueGroupAssocRepository attributeValueGroupAssocRepository;
	@Autowired
	AttributeValueRepository attributeValueRepository;
	
	@Autowired
	ProductCompAssnRepository productCompAssnRepository;
	
	@Autowired
	ComponentAttrGroupAssocRepository componentAttrGroupAssocRepository;
	
	
	@Autowired
	AttributeGroupAttrAssocRepository attrGroupAttrAssocRepository;
	
	@Autowired
	NplSlaViewRepository nplSlaViewRepository;
	
	@Autowired
	DataCenterRepository dataCenterRepository;
	
	@Autowired
	CpeBomGvpnDetailIntlRepository cpeBomGvpnDetailIntlRepository;

	@Autowired
	CpeBomGscDetailIntlRepository cpeBomGscDetailIntlRepository;

	@Autowired
	CpeBomGscAdditionalAttributesRepository cpeBomGscAdditionalAttributesRepository;

	@Autowired
	CpeBomGscDetailRepository cpeBomGscDetailRepository;
	
	@Autowired
	BomInventoryCatalogAssocRepository bomInventoryCatalogAssocRepository;

	@Autowired
	VwCpeBomPowerCableGscRepository vwCpeBomPowerCableGscRepository;



	@Autowired
	BomPhysicalResourceAssocRepository bomPhysicalResourceAssocRepository;


	/**
	 * Method to get pricing details
	 * 
	 * @author Dinahar Vivekanandan
	 * @return ResponseResource<List<IasPriceBook>>
	 * @throws TclCommonException
	 */
	public List<ProductDto> getByProductFamilyId(Integer prodFamilyId) throws TclCommonException {
		List<ProductDto> productDtos = null;
		try {
			if (Objects.isNull(prodFamilyId))
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			List<Product> products = productRepository.findByProductFamily_IdAndIsActive(prodFamilyId, "Y");
			LOGGER.debug("porduct size : {}", products.size());
			if (products.isEmpty())
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_NOT_FOUND);
			productDtos = products.stream().map(ProductDto::new).collect(Collectors.toList());
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);
		}
		return productDtos;
	}

	/**
	 * Method to fetch a profile based on profile id
	 * 
	 * @author Dinahar Vivekanandan
	 * @param productOfferingId
	 * @return ProductDto
	 * @throws TclCommonException
	 */
	public ProductDto getByProductOfferingId(Integer productOfferingId) throws TclCommonException {
		ProductDto productDto = null;
		try {
			if (Objects.isNull(productOfferingId))
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			Optional<Product> optionalProduct = productRepository.findByIdAndIsActiveIsNullOrIdAndIsActive(productOfferingId,productOfferingId, "Y");
			if (optionalProduct.isPresent())
				productDto = new ProductDto(optionalProduct.get());
			else
				throw new TclCommonException(ExceptionConstants.PROFILE_NOT_FOUND, ResponseResource.R_CODE_NOT_FOUND);

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);
		}
		return productDto;
	}

	/**
	 * Method to fetch all the family details
	 * 
	 * @author Dinahar Vivekanandan
	 * @return List<ProductFamilyDto>
	 * @throws TclCommonException
	 */
	public List<ProductFamilyDto> getAllProductFamily() throws TclCommonException {
		List<ProductFamilyDto> productDtos = null;
		try {
			List<ProductCatalog> prodFamilyList = productFamilyRepository.findAll();
			productDtos = prodFamilyList.stream().map(ProductFamilyDto::new).collect(Collectors.toList());
			if (prodFamilyList.isEmpty()) {
				throw new TclCommonException(ExceptionConstants.PRODUCT_FAMILY_EMPTY,
						ResponseResource.R_CODE_NOT_FOUND);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return productDtos;
	}

	/**
	 * 
	 * Method to fetch all the product service details for a given familyId
	 * 
	 * @author Vinod
	 * @param prodFamilyId
	 * @return String
	 * @throws TclCommonException
	 */

	public String getServiceDetails(Integer prodFamilyId) throws TclCommonException {
		Optional<ProductCatalog> serviceDetails = null;
		try {
			if (Objects.isNull(prodFamilyId))
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			serviceDetails = productFamilyRepository.findById(prodFamilyId);

			if (!serviceDetails.isPresent()) {
				throw new TclCommonException(ExceptionConstants.PRODUCT_SERVICE_DETAILS_EMPTY,
						ResponseResource.R_CODE_NOT_FOUND);
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);
		}
		return serviceDetails.get().getServiceDetails();
	}

	public List<String> getProductLocations(String productName) throws TclCommonException {
		List<String> prodLocations = null;
		if (productName == null) {
			throw new TclCommonException(ExceptionConstants.PRODUCT_LOCATIONS_EMPTY, ResponseResource.R_CODE_ERROR);
		}
		try {
			prodLocations = productFamilyRepository.getProductLocations(productName);
			if (productName.equalsIgnoreCase(CommonConstants.GVPN)) {
				prodLocations = prodLocations.stream()
						.filter(location -> location.equalsIgnoreCase(ProductSpecificConstant.INDIA)
								|| location.equalsIgnoreCase(ProductSpecificConstant.USA))
						.collect(Collectors.toList());
			}
			if(productName.equalsIgnoreCase(CommonConstants.IPC_DESC)) {
				prodLocations = prodLocations.stream()
						.filter(location -> location.equalsIgnoreCase(ProductSpecificConstant.INDIA))
						.collect(Collectors.toList());
			}
			} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return prodLocations;
	}

	/**
	 * getCpeBom
	 * 
	 * Method to retrieve CPE BOM details
	 * 
	 * @author Dinahar Vivekanandan
	 * 
	 * @param productOfferingId
	 * @param bandwidth
	 * @param portInterfaceId
	 * @param routingProtocolId
	 * @return List<CpeBomDto>
	 * @throws TclCommonException
	 */
	public Set<CpeBomDto> getCpeBom(Integer bandwidth, String portInterface, String routingProtocol)
			throws TclCommonException {
		List<CpeBomView> bomDetailsView = null;
		Set<CpeBomDto> bomDetails = null;
		if (bandwidth == null || portInterface == null || routingProtocol == null)
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
		try {
			bomDetailsView =cpeBomViewRepository.findByBomTypeAndMaxBandwidthGreaterThanEqual("CPE", bandwidth); 
			if (bomDetailsView == null || bomDetailsView.isEmpty())
				throw new TclCommonException(ExceptionConstants.PRODUCT_LOCATIONS_EMPTY,
						ResponseResource.R_CODE_NOT_FOUND);
			else {
				bomDetails = bomDetailsView.stream().map(CpeBomDto::new).collect(Collectors.toSet());
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return bomDetails;

	}

	/**
	 * getCpeBomDetails
	 * 
	 * Method to retrieve CPE BOM details
	 * 
	 * @author Dinahar Vivekanandan
	 *
	 * @param List<Integer>
	 *            cpeList
	 * @return ResponseResource<List<CpeBomDto>>
	 * @throws TclCommonException
	 */
	public Set<CpeBomDto> getCpeBomDetails(List<String> cpeBomIdList) throws TclCommonException {
		Set<CpeBomDto> bomDetails = null;
		if (cpeBomIdList == null || cpeBomIdList.isEmpty())
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
		try {

			/*
			 * bomDetails = cpeBomIdList.stream() .map(cpeBomId -> new
			 * CpeBomDto(bomMasterRepository.findByBomName(cpeBomId)))
			 * .collect(Collectors.toSet());
			 */


			bomDetails = new HashSet<>();
			for (String cpe : cpeBomIdList) {

				CpeBomDto cpeBomDto = new CpeBomDto();
				BomMaster bomMaster = bomMasterRepository.findByBomName(cpe);
				if (bomMaster != null) {
					cpeBomDto.setId(bomMaster.getId());
					cpeBomDto.setBomName(bomMaster.getBomName());
					cpeBomDto.setUniCode(bomMaster.getBomName());
					List<BomPhysicalResourceAssoc> physicalAssoc = bomPhysicalResourceAssocRepository
							.getPhysicalResourcesMapped(bomMaster.getBomName());
					List<ResourceDto> resourceDtos = new ArrayList<>();
					for (BomPhysicalResourceAssoc bomPhysicalResourceAssoc : physicalAssoc) {
						LOGGER.info("Incoming resource {}", bomPhysicalResourceAssoc.getRelation());
						ResourceDto resourceDto = new ResourceDto(bomPhysicalResourceAssoc.getPhysicalResource());
						LOGGER.info("Resource constructed {}", resourceDto);
						resourceDtos.add(resourceDto);
					}
					cpeBomDto.setResources(resourceDtos);
				}
				bomDetails.add(cpeBomDto);

			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return bomDetails;
	}
	
	/**
	 * getAttributeValue
	 * 
	 * Method to retrieve CPE BOM details
	 * 
	 * @author Biswajit Sahoo
	 *
	 * @param String attributeCd
	 * 
	 * @return Set<AttributeValueDto>
	 * @throws TclCommonException
	 */
	public List<AttributeValueDto> getAttributeValue(String attributeCd) throws TclCommonException {
		Integer attrMasterId = null;
		List<AttributeValueDto> attrValues = null;

		if (attributeCd == null || attributeCd.isEmpty())
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
		try {
			Optional<AttributeMaster> attributeMaster = attributeMasterRepository
					.findByCdAndIsActiveIsNullOrCdAndIsActive(attributeCd, attributeCd, "Y");

			if (attributeMaster.isPresent()) {
				attrMasterId = attributeMaster.get().getId();
				Optional<AttributeGroupAttrAssoc> attributeGroupAttrAssoc = attributeGroupAssocRepository
						.findByAttributeMaster_IdAndIsActiveIsNullOrAttributeMaster_IdAndIsActive(attrMasterId,
								attrMasterId, "Y");
				if (attributeGroupAttrAssoc.isPresent()) {
					List<AttributeValueGroupAssoc> attributeValueGroupAssocList = attributeGroupAttrAssoc.get().getAttributeValueGroupAssoc();
					if (attributeValueGroupAssocList != null && !attributeValueGroupAssocList.isEmpty()) {
						List<Integer> attrGrpIdList = attributeValueGroupAssocList.stream()
								.map(ele -> ele.getAttributeValue().getId()).collect(Collectors.toList());
						List<AttributeValue> attributeValues = attributeValueRepository
								.findByIdInAndIsActiveIsNullOrIdInAndIsActive(attrGrpIdList, attrGrpIdList, "Y");
						attrValues =attributeValues.stream().map(AttributeValueDto::new).collect(Collectors.toList());
						if (attributeCd.equalsIgnoreCase("NPLBandwidth")) {
						attrValues =getSortedAttributeValueDtos(attrValues);
						}
					} else {
						throw new TclCommonException(ExceptionConstants.ATTRIBUTE_VALUE_GROUP_NOT_AVAILABLE, ResponseResource.R_CODE_ERROR);
					}
				} else {
					throw new TclCommonException(ExceptionConstants.ATTRIBUTE_GROUP_NOT_AVAILABLE, ResponseResource.R_CODE_ERROR);
				}
			} else {
				throw new TclCommonException(ExceptionConstants.ATTRIBUTE_MASTER_EMPTY, ResponseResource.R_CODE_ERROR);
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return attrValues;
	}


	private List<AttributeValueDto> getSortedAttributeValueDtos(List<AttributeValueDto> attrDtos) {
		if (attrDtos != null && !attrDtos.isEmpty()) {
			attrDtos.sort((AttributeValueDto attr1,AttributeValueDto attr2)->
			{
				Double attr1Val = Double.parseDouble(attr1.getAttrValue().split(" ")[0]);
				Double attr2Val = Double.parseDouble(attr2.getAttrValue().split(" ")[0]);
				String attr1Unit = attr1.getAttrValue().split(" ")[1];
				String attr2Unit = attr2.getAttrValue().split(" ")[1];
				if (attr1Unit.equals("Gbps"))
					attr1Val = attr1Val*1024;
				if (attr2Unit.equals("Gbps"))
					attr2Val = attr2Val*1024;
				return (int) Math.round(attr1Val - attr2Val);
			});
		}
	
		return attrDtos;
	}
	

    /* * getAttributesForProduct - Method to get attributes list for a particular product
     * @author Dinahar Vivekanandan
     * @param productId
     * @return
     * @throws TclCommonException
     */
     public Set<ProductAttributeDto> getAttributesForProduct(Integer productId) throws TclCommonException {
            Product product = null;
            Set<Component> components = new HashSet<>();
            Set<AttributeGroupMaster> attrGrps = new HashSet<>();
            Set<AttributeMaster> attributes = new HashSet<>();
            Set<AttributeValue> attrValues = new HashSet<>();
            Set<List<AttributeValueGroupAssoc>> attrValueGroups= new HashSet<>();
            Set<ProductAttributeDto> productAttrDtos = new HashSet<>();


                   try {
                         if (productId == null)
                         throw new TclCommonException(ExceptionConstants.INVALID_PRODUCT_ID, ResponseResource.R_CODE_ERROR);
            
                         Optional<Product> optProduct = productRepository.findById(productId);
                                if (optProduct.isPresent())
                                       product  = optProduct.get();
                                else 
                                       throw new TclCommonException(ExceptionConstants.INVALID_PRODUCT_ID, ResponseResource.R_CODE_ERROR);


                                components=  product.getProductComponentAssocs().stream()
                                              .filter(prdCompAs -> prdCompAs.getIsActive()==null || prdCompAs.getIsActive().equals("Y"))
                                              .map(ProductComponentAssoc::getComponent).collect(Collectors.toSet());
                                
                                components.forEach(component->{
                                       attrGrps.addAll(component.getComponentAttributeGroupAssocs().stream()
                                                     .filter(cmpAttGrp -> cmpAttGrp.getIsActive()==null || cmpAttGrp.getIsActive().equals("Y"))
                                                     .map(ComponentAttributeGroupAssoc::getAttributeGroupMaster).collect(Collectors.toSet()));
                                       attrGrps.forEach(attGrp -> {
                                              attributes.addAll(attGrp.getAttributeGroupAssocList().stream()
                                                            .filter(grpAttrAs -> grpAttrAs.getIsActive()==null || grpAttrAs.getIsActive().equals("Y"))
                                                            .map(AttributeGroupAttrAssoc::getAttributeMaster).collect(Collectors.toSet()));
                                       
                                              attrValueGroups.addAll(attGrp.getAttributeGroupAssocList().stream()
                                                            .filter(grpAttrAs -> grpAttrAs.getIsActive()==null || grpAttrAs.getIsActive().equals("Y"))
                                                            .map(AttributeGroupAttrAssoc::getAttributeValueGroupAssoc).collect(Collectors.toSet()));
                                              
                                              
                                              attrValueGroups.forEach(attrValGroup->{
                                                     attrValues.addAll(attrValGroup.stream().map(AttributeValueGroupAssoc::getAttributeValue)
                                                     .filter(attributeValue->attributeValue.getIsActive()==null || attributeValue.getIsActive().equals("Y"))
                                                     .collect(Collectors.toSet()));
                                                     
                                              attributes.forEach(attr ->{
                                                     productAttrDtos.add(new ProductAttributeDto(attr,attrValues,component));
                                                     });
                                                     attrValues.clear();
                                              });
                                              attributes.clear();
                                              
                                       });
                                       attrGrps.clear();


                                });
                                components.clear();
                   
                         
                   } catch (TclCommonException e) {
                         LOGGER.info(e.getMessage());
                         throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
                   }

                   return productAttrDtos;

     }


	public List<ProductProfileDto> getProfileList(Integer productFamilyId) throws TclCommonException {
		String defaultValue =null;
		//Below Map is used for Network up time
		Map<String, String> networkUpTime = new LinkedHashMap<>();
		networkUpTime.put("label", "Network up time");

		List<ProductProfileDto> productProfileList = new ArrayList<>();
		if (productFamilyId == null )
			throw new TclCommonException(ExceptionConstants.INVALID_PRODUCT_ID, ResponseResource.R_CODE_ERROR);
		try {
			
			List<Product> productList = productRepository.findByProductFamily_IdAndIsActive(productFamilyId, "Y");
			
			if(productList!=null && !productList.isEmpty())
			{
				for (Product product : productList) {
					
					//switch (productFamilyId) 
					switch (ProductId.valueOf(productFamilyId)){
					case NPL:
						// Since NPL has single profile switch based on profile id isn't introduced
						List<NplSlaView> nplSlaViewList = nplSlaViewRepository.findByServiceVarientAndAccessTopology("Standard", "2 path protection");
						defaultValue = nplSlaViewList.get(0).getDefaultValue();
						break;
					case GVPN:
						//code for GVPN - May require another internal switch based on profile id to populate network uptime based on profile
						break;
						
					}
					
					networkUpTime.put("value", defaultValue);
					
					ProductProfileDto productProfile = new ProductProfileDto();
					List<ProductComponentAssoc> productComponentAssocList = productCompAssnRepository
							.findByProduct_IdAndIsActiveIsNullOrProduct_IdAndIsActive(product.getId(),product.getId(), "Y");
					List<Integer> componentIds = new ArrayList<>();
					for (ProductComponentAssoc productComponentAssoc : productComponentAssocList) {
						componentIds.add(productComponentAssoc.getComponent().getId());
						if (productComponentAssoc.getIsBackup()!=null && productComponentAssoc.getIsBackup().equalsIgnoreCase("Y")) {
							productProfile.setBackup(true);
						}
					}
					productProfile.setProfileId(product.getId());
					productProfile.setProfileName(product.getName());
					productProfile.setDescription(product.getDescription());
					productProfile.setComponentIds(componentIds);
					productProfile.setImage(product.getUrl());
					productProfile.setNetworkInTime(networkUpTime);
					productProfileList.add(productProfile);
				}
			}
			else
				throw new TclCommonException(ExceptionConstants.INVALID_PRODUCT_ID, ResponseResource.R_CODE_ERROR);
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return productProfileList;

	}
	
	
	/**
	 * Method to fetch a components based on profile id
	 * 
	 * @author Thamizhselvi Perumal
	 * @param productOfferingId
	 * @return ProductDto
	 * @throws TclCommonException
	 */
	public ProfileComponentListDto getComponentListByProductOfferingId(Integer productOfferingId) throws TclCommonException {
		ProfileComponentListDto componentsDto = new ProfileComponentListDto();
		try {
			if (Objects.isNull(productOfferingId))
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			Optional<Product> optionalProduct = productRepository.findByIdAndIsActiveIsNullOrIdAndIsActive(productOfferingId, productOfferingId,"Y");
			
			if (optionalProduct.isPresent())
			{
				Product product=optionalProduct.get();
				Set<ProfileComponentDto> profileComponentSet= new HashSet<>();
				List<ProductComponentAssoc> productComponentAssocList = productCompAssnRepository.findByProduct_IdAndIsActiveIsNullOrProduct_IdAndIsActive(product.getId(), product.getId(),"Y");
						
				for (ProductComponentAssoc productComponentAssoc : productComponentAssocList) {
					Set<Integer> attributeIds=getAttributeIds(productComponentAssoc);
					ProfileComponentDto profileComponent=new ProfileComponentDto();
					profileComponent.setId(productComponentAssoc.getComponent().getId());
					profileComponent.setDescription(productComponentAssoc.getComponent().getDescription());
					profileComponent.setName(productComponentAssoc.getComponent().getName());
					if((productComponentAssoc.getComponent().getIsActive().equals("Y"))||(productComponentAssoc.getComponent().getIsActive()==null))
						profileComponent.setActive(true);
					else
					{
						profileComponent.setActive(false);
					}
					profileComponent.setDisplay(false);
					profileComponent.setAttributes(attributeIds);
					profileComponentSet.add(profileComponent);
					
				}
				componentsDto.setProfileComponents(profileComponentSet);
			}
			else
				throw new TclCommonException(ExceptionConstants.PROFILE_NOT_FOUND, ResponseResource.R_CODE_NOT_FOUND);

		} catch (Exception e) {
			LOGGER.info("Exception occurred:{}",e.getMessage());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);
		}
		return componentsDto;
	}
	
	/**
	 * Method to get attribute Ids based on component object
	 * 
	 * @author Thamizhselvi Perumal
	 * @param ProductComponentAssoc productComponentAssoc
	 * @return Set<Integer>
	 */

	public Set<Integer> getAttributeIds(ProductComponentAssoc productComponentAssoc)
	{
		Set<Integer> attributeIds = new HashSet<>();
		List<ComponentAttributeGroupAssoc> componentAttrGroupAssocList = componentAttrGroupAssocRepository.findByComponent_IdAndIsActiveIsNullOrComponent_IdAndIsActive(productComponentAssoc.getComponent().getId(),productComponentAssoc.getComponent().getId(),"Y");					
		for (ComponentAttributeGroupAssoc componentAttributeGroupAssoc : componentAttrGroupAssocList) {
			List<AttributeGroupAttrAssoc> attributeGroupAttrAssocList = attrGroupAttrAssocRepository.findByAttributeGroupMaster_IdAndIsActiveIsNullOrAttributeGroupMaster_IdAndIsActive(componentAttributeGroupAssoc.getAttributeGroupMaster().getId(),componentAttributeGroupAssoc.getAttributeGroupMaster().getId(),"Y");
			for (AttributeGroupAttrAssoc attributeGroupAttrAssoc : attributeGroupAttrAssocList) {
				attributeIds.add(attributeGroupAttrAssoc.getAttributeMaster().getId());
			}
		}
		return attributeIds;
	}
	/**
	 * Method to get data center
	 * 
	 * @author Biswajit
	 * @param dataCenterIds
	 * @return List<DataCenterDto>
	 */
	public List<DataCenterBean> getDataCenter(String[] dataCenterIds) {
		List<DataCenterBean> dataCenterBeanList = new ArrayList<>();
		List<Integer> dataCenterId = Arrays.asList(dataCenterIds).stream().map(Integer::parseInt)
				.collect(Collectors.toList());
		List<ServiceAreaMatrixDataCenter> dataCenters = dataCenterRepository.findByDcTypeAndIsActiveAndIdIn(DataCenterType.INDIA_DATA_CENTER.getDcType(),"Y",dataCenterId);
		for (ServiceAreaMatrixDataCenter serviceAreaMatrixDc : dataCenters) {
			DataCenterBean dataCenterBean = new DataCenterBean();
			dataCenterBean.setId(serviceAreaMatrixDc.getId());
			dataCenterBean.setDcType(serviceAreaMatrixDc.getDcType());
			dataCenterBean.setLocationId(serviceAreaMatrixDc.getLocationId());
			dataCenterBean.setLatitudeNbr(serviceAreaMatrixDc.getLatitudeNbr());
			dataCenterBean.setLongitudeNbr(serviceAreaMatrixDc.getLongitudeNbr());
			dataCenterBean.setRegionDtl(serviceAreaMatrixDc.getRegionDtl());
			dataCenterBean.setAddressTxt(serviceAreaMatrixDc.getAddressTxt());
			dataCenterBean.setTownsDtl(serviceAreaMatrixDc.getTownsDtl());
			dataCenterBeanList.add(dataCenterBean);
		}

		return dataCenterBeanList;
	}

	/**
	 * Get Product Specific Locations
	 *
	 * @param productName
	 * @param serviceName
	 * @param country
	 * @return {@link List<String>}
	 * @throws TclCommonException
	 */
	public List<String> getProductLocationsForService(String productName, String serviceName, String country) throws TclCommonException {
		Objects.requireNonNull(productName, "Product Name cannot be null");

		if (!ProductSpecificConstant.GSIP.equalsIgnoreCase(productName)) {
			throw new TclCommonException(ExceptionConstants.PRODUCT_FAMILY_EMPTY, ResponseResource.R_CODE_ERROR);
		}

		if(serviceName.equalsIgnoreCase(ProductSpecificConstant.DOMESTIC_VOICE)) {
			return productFamilyRepository.getProductLocationsForGivenCountry(productName, country);
		} else if(serviceName.equalsIgnoreCase(ProductSpecificConstant.GLOBAL_OUTBOUND) && ProductSpecificConstant.INDIA.equalsIgnoreCase(country)) {
			return productFamilyRepository.getProductLocationsAndExcludeGivenCountry(productName, country);
		}

		return productFamilyRepository.getProductLocations(productName);
	}
	
	
	public List<ProductInformationBean> getAllProductDetails() throws TclCommonException{
		List<ProductInformationBean> productInformationBeans =  new ArrayList<>();
		List<ProductFamilyDto> productFamilyDtos = getAllProductFamily();
		if(productFamilyDtos!=null) {
			productFamilyDtos.stream().forEach(productFamily->{
				ProductInformationBean productInformationBean = new ProductInformationBean();
				productInformationBean.setProductName(productFamily.getName());
				productInformationBean.setProductId(productFamily.getId());
				productInformationBean.setProductDesc(productFamily.getDescription());
				productInformationBean.setIsMacdEnabledFlag(productFamily.getIsMacdEnabledFlag());
				productInformationBean.setProductShortName(productFamily.getProductShortName());
				productInformationBeans.add(productInformationBean);
				
			});
		}
		return productInformationBeans;
	}
	
	/**
	 * getCpeBomDetails
	 * 
	 * Method to retrieve  international CPE BOM details
	 *
	 *
	 * @param List<Integer>
	 *            cpeList
	 * @return ResponseResource<List<CpeBomDto>>
	 * @throws TclCommonException
	 */
	public Set<ResourceDto> getCpeInternationalBomDetails(List<GvpnInternationalCpeDto> cpeDtoInternational) throws TclCommonException {
		Set<ResourceDto> bomDetails = new HashSet<ResourceDto>();
		List<CpeBomGvpnIntlDetailView> cpeIntbom=new ArrayList<CpeBomGvpnIntlDetailView>();
		if (cpeDtoInternational == null )
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
		try {
			for(GvpnInternationalCpeDto cpeDto:cpeDtoInternational) {
				LOGGER.info("Parameters for cpe bom details are, country : {}, bomname : {}, port interface : {}, routing protocal : {}", cpeDto.getCountry(), cpeDto.getBom_name(), cpeDto.getPort_interface(),  cpeDto.getRouting_protocol());
				cpeIntbom = cpeBomGvpnDetailIntlRepository.findByCountryCodeAndBomNameAndPortInterfaceAndRoutingProtocolAndHandoffOrHandoff(cpeDto.getCountry(), cpeDto.getBom_name(),
							cpeDto.getPort_interface(), cpeDto.getRouting_protocol(),cpeDto.getHandOff());
				if(!cpeIntbom.isEmpty()) {
					for(CpeBomGvpnIntlDetailView view:cpeIntbom) {
						ResourceDto res=new ResourceDto();
						res.setBomName(view.getBomName());
						res.setProductCode(view.getProductCode());
						res.setLongDesc(view.getLongDesc());
						res.setListPrice(view.getListPrice());
						res.setProductCategory(view.getProductCategory());
						bomDetails.add(res);
						
					  }
					}

			}
			
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return bomDetails;
	}

	/**
	 * Get cpe bom details for gsc gvpn
	 *
	 * @param cpeBoms
	 * @return
	 * @throws TclCommonException
	 */
	public Set<ResourceDto> getCpeBomDetailsForGscGvpn(List<GvpnInternationalCpeDto> cpeBoms) throws TclCommonException {
		Set<ResourceDto> bomDetails = new HashSet<>();
		try {
			cpeBoms.stream().forEach(cpeDto -> {
				getGscBomDetails(bomDetails, cpeDto);
				getCubeLicensesForGscBom(bomDetails, cpeDto);
				getPvdmCardDetailsForGscGvpn(bomDetails, cpeDto);
				getMftCardDetailsForGscGvpnBom(bomDetails, cpeDto);
			});
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return bomDetails;
	}

	/**
	 * Get Cpe Intl bom details for gsc gvpn
	 *
	 * @param cpeDtoInternational
	 * @return
	 * @throws TclCommonException
	 */
	public Set<ResourceDto> getCpeInternationalBomDetailsForGscGvpn(List<GvpnInternationalCpeDto> cpeDtoInternational) throws TclCommonException {
		Set<ResourceDto> bomDetails = new HashSet<>();
		try {
			cpeDtoInternational.stream().forEach(cpeDto -> {
				getGscIntlBomDetails(bomDetails, cpeDto);
				getCubeLicensesForGscBom(bomDetails, cpeDto);
				getPvdmCardDetailsForGscGvpn(bomDetails, cpeDto);
				getMftCardDetailsForGscGvpnBom(bomDetails, cpeDto);
			});
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return bomDetails;
	}

	/**
	 * Get mft card details for gsc gvpn
	 *  @param bomDetails
	 * @param cpeDto
	 * @param country
	 */
	private void getMftCardDetailsForGscGvpnBom(Set<ResourceDto> bomDetails, GvpnInternationalCpeDto cpeDto) {
		if (!CollectionUtils.isEmpty(cpeDto.getMftCardsAndQuantitiesMap())) {
			cpeDto.getMftCardsAndQuantitiesMap().entrySet().stream().forEach(mftCard -> {
				LOGGER.info("MFT card type is {} and quantity are {} ", mftCard.getKey(), mftCard.getValue());
				CpeBomGscBomAttributesView mftCardModule = cpeBomGscAdditionalAttributesRepository.findByCountryNameAndProductCategoryAndProductCode(cpeDto.getCountry(), MFT_CARD, mftCard.getKey());
				if (Objects.nonNull(mftCardModule)) {
					LOGGER.info("Mftcard product code  is {} ", mftCardModule.getProductCode());
					ResourceDto res = new ResourceDto();
					res.setBomName(cpeDto.getBom_name());
					res.setProductCode(mftCard.getKey());
					res.setLongDesc(mftCardModule.getLongDesc());
					res.setListPrice(mftCardModule.getListPrice());
					res.setProductCategory(mftCardModule.getProductCategory());
					res.setQuantity(mftCard.getValue());
					bomDetails.add(res);
				}
			});
		}
	}

	/**
	 * List of mft cards available
	 *
	 * @return
	 */
	private static Map<String, String> getMFTCardName() {
		Map<String, String> mftCards = new HashMap<>();
		mftCards.put("1", "NIM-1MFT-T1/E1");
		mftCards.put("2", "NIM-2MFT-T1/E1");
		mftCards.put("4", "NIM-4MFT-T1/E1");
		mftCards.put("8", "NIM-8MFT-T1/E1");
		return mftCards;
	}

	/**
	 * Get pvdm card details for gsc gvpn
	 *  @param bomDetails
	 * @param cpeDto
	 * @param country
	 */
	private void getPvdmCardDetailsForGscGvpn(Set<ResourceDto> bomDetails, GvpnInternationalCpeDto cpeDto) {
		if (!CollectionUtils.isEmpty(cpeDto.getPvdmCardsAndQuantitiesMap())) {
			cpeDto.getPvdmCardsAndQuantitiesMap().entrySet().stream().forEach(pvdmCard -> {
				CpeBomGscBomAttributesView dspModule = cpeBomGscAdditionalAttributesRepository.findByCountryNameAndProductCategoryAndProductCode(cpeDto.getCountry(), DSP_MODULE, pvdmCard.getKey());
				if (Objects.nonNull(dspModule)) {
					ResourceDto res = new ResourceDto();
					res.setBomName(cpeDto.getBom_name());
					res.setProductCode(pvdmCard.getKey());
					res.setLongDesc(dspModule.getLongDesc());
					res.setListPrice(dspModule.getListPrice());
					res.setProductCategory(dspModule.getProductCategory());
					res.setQuantity(pvdmCard.getValue());
					bomDetails.add(res);
				}

			});
		}
	}

	/**
	 * Get cube licenses for gsc bom
	 *  @param bomDetails
	 * @param cpeDto
	 * @param country
	 */
	private void getCubeLicensesForGscBom(Set<ResourceDto> bomDetails, GvpnInternationalCpeDto cpeDto) {
		if (IP_PBX_CUBE.equalsIgnoreCase(cpeDto.getTypeOfCpe())) {
			List<CpeBomGscBomAttributesView> additionalAttributes;
			List<CpeBomGscBomAttributesView> allAttributes = cpeBomGscAdditionalAttributesRepository.findByCountryNameAndProductCategory(cpeDto.getCountry(), CUBE_LICENSES);
			if (!CollectionUtils.isEmpty(allAttributes)) {
				if(REDUNDANT.equalsIgnoreCase(cpeDto.getAccessTopology())){
					additionalAttributes = allAttributes.stream().filter(cpeBomGscBomAttributesView -> !(cpeBomGscBomAttributesView.getProductCode().equalsIgnoreCase(CON_ECMU_CUBETSTD)
							|| cpeBomGscBomAttributesView.getProductCode().equalsIgnoreCase(CUBE_T_STD))).collect(Collectors.toList());
				}
				else {
					additionalAttributes = allAttributes.stream().filter(cpeBomGscBomAttributesView -> !(cpeBomGscBomAttributesView.getProductCode().equalsIgnoreCase(CON_ECMU_CUBETRDE)
							|| cpeBomGscBomAttributesView.getProductCode().equalsIgnoreCase(CUBE_T_RED))).collect(Collectors.toList());

				}
				constructCubeLicenseForGscBom(bomDetails, cpeDto, additionalAttributes);
			}
		}
	}

	/**
	 * construct cube license for bom
	 *
	 * @param bomDetails
	 * @param cpeDto
	 * @param additionalAttributes
	 */
	private void constructCubeLicenseForGscBom(Set<ResourceDto> bomDetails, GvpnInternationalCpeDto cpeDto, List<CpeBomGscBomAttributesView> additionalAttributes) {
		additionalAttributes.stream().forEach(view -> {
			ResourceDto res = new ResourceDto();
			res.setBomName(cpeDto.getBom_name());
			res.setProductCode(view.getProductCode());
			res.setLongDesc(view.getLongDesc());
			res.setListPrice(view.getListPrice());
			res.setProductCategory(view.getProductCategory());
			if (view.getProductCode().equalsIgnoreCase(L_CUBE) || view.getProductCode().equalsIgnoreCase(CON_ECMU_LCUBE001)) {
				res.setQuantity(view.getQuantity());
			} else {
				res.setQuantity(Integer.valueOf(cpeDto.getCubeLicenses()));
			}
			bomDetails.add(res);
		});
	}

	/**
	 * Get gsc intl bom details
	 *
	 * @param bomDetails
	 * @param cpeDto
	 */
	private void getGscIntlBomDetails(Set<ResourceDto> bomDetails, GvpnInternationalCpeDto cpeDto) {
		List<CpeBomGscIntlDetailView> allcpeIntbom;
		LOGGER.info("Parameters for cpe bom details for gsc gvpn are, country : {}, bomname : {}, port interface : {}, routing protocal : {}", cpeDto.getCountry(), cpeDto.getBom_name(), cpeDto.getPort_interface(), cpeDto.getRouting_protocol());
		allcpeIntbom = cpeBomGscDetailIntlRepository.findByCountryCodeAndBomNameAndPortInterfaceAndRoutingProtocolAndHandoffOrHandoff(cpeDto.getCountry(), cpeDto.getBom_name(),
				cpeDto.getPort_interface(), cpeDto.getRouting_protocol(), cpeDto.getHandOff());
		if(Objects.nonNull(cpeDto.getPvdmAndMftQuantities())) {
			LOGGER.info("Checking for NIM's with blank for TDM type");
			Optional<String> anyNimWithQuantityZero = checkIfNimModulesAreBlank(cpeDto);
			if (!anyNimWithQuantityZero.isPresent()) {
				allcpeIntbom = allcpeIntbom.stream().filter(cpeBomGscIntlDetailView -> !cpeBomGscIntlDetailView.getProductCode().equalsIgnoreCase(NIM_BLANK)).collect(Collectors.toList());

			}
		}
		constructGscIntlCpeBomDetails(bomDetails, allcpeIntbom);
		setPowerCableInGscCpeBomDetails(bomDetails, cpeDto);
		setAdditionalInformationInGscCpeBomDetails(bomDetails, cpeDto);
		setSfpModuleInGscIntlCpeBomDetails(bomDetails, cpeDto);
		setNimModuleInGscIntlCpeBomDetails(bomDetails, cpeDto);
	}

	/**
	 * set nim module in cpe bom for intl
	 *
	 * @param bomDetails
	 * @param cpeDto
	 */
	private void setNimModuleInGscIntlCpeBomDetails(Set<ResourceDto> bomDetails, GvpnInternationalCpeDto cpeDto) {
		if(Objects.nonNull(cpeDto.getNimModule())){
			LOGGER.info("Nim Modules is {}", cpeDto.getNimModule());
			List<String> nimModulesProductCodes = Arrays.asList(cpeDto.getNimModule().split(","));
			List<CpeBomGscIntlDetailView> byProductCodes = cpeBomGscDetailIntlRepository.findByProductCodeIn(nimModulesProductCodes);
			if(!CollectionUtils.isEmpty(byProductCodes)){
				byProductCodes.stream().forEach(cpeBomGscDetailView -> {
					ResourceDto resourceDto = new ResourceDto();
					resourceDto.setBomName(cpeBomGscDetailView.getBomName());
					resourceDto.setLongDesc(cpeBomGscDetailView.getLongDesc());
					resourceDto.setProductCategory(cpeBomGscDetailView.getProductCategory());
					resourceDto.setProductCode(cpeBomGscDetailView.getProductCode());
					bomDetails.add(resourceDto);
				});
			}
		}
	}

	/**
	 * set sfp module in cpe bom for intl
	 *
	 * @param bomDetails
	 * @param cpeDto
	 */
	private void setSfpModuleInGscIntlCpeBomDetails(Set<ResourceDto> bomDetails, GvpnInternationalCpeDto cpeDto) {
		if(Objects.nonNull(cpeDto.getSfpModule())){
			LOGGER.info("SFP Modules is {}", cpeDto.getSfpModule());
			List<String> sfpModulesProductCodes = Arrays.asList(cpeDto.getSfpModule().split(","));
			List<CpeBomGscIntlDetailView> byProductCodes = cpeBomGscDetailIntlRepository.findByProductCodeIn(sfpModulesProductCodes);
			if(!CollectionUtils.isEmpty(byProductCodes)){
				byProductCodes.stream().forEach(cpeBomGscDetailView -> {
					ResourceDto resourceDto = new ResourceDto();
					resourceDto.setBomName(cpeBomGscDetailView.getBomName());
					resourceDto.setLongDesc(cpeBomGscDetailView.getLongDesc());
					resourceDto.setProductCategory(cpeBomGscDetailView.getProductCategory());
					resourceDto.setProductCode(cpeBomGscDetailView.getProductCode());
					bomDetails.add(resourceDto);
				});
			}
		}
	}

	/**
	 * set nim module in cpe bom
	 *
	 * @param bomDetails
	 * @param cpeDto
	 */
	private void setNimModuleInGscCpeBomDetails(Set<ResourceDto> bomDetails, GvpnInternationalCpeDto cpeDto) {
		if(Objects.nonNull(cpeDto.getNimModule())){
			LOGGER.info("Nim Modules is {}", cpeDto.getNimModule());
			List<String> nimModulesProductCodes = Arrays.asList(cpeDto.getNimModule().split(","));
			List<CpeBomGscDetailView> byProductCodes = cpeBomGscDetailRepository.findByProductCodeIn(nimModulesProductCodes);
			if(!CollectionUtils.isEmpty(byProductCodes)){
				byProductCodes.stream().forEach(cpeBomGscDetailView -> {
					ResourceDto resourceDto = new ResourceDto();
					resourceDto.setBomName(cpeBomGscDetailView.getBomName());
					resourceDto.setLongDesc(cpeBomGscDetailView.getLongDesc());
					resourceDto.setProductCategory(cpeBomGscDetailView.getProductCategory());
					resourceDto.setProductCode(cpeBomGscDetailView.getProductCode());
					bomDetails.add(resourceDto);
				});
			}
		}
	}

	/**
	 * set sfp module in cpe bom
	 *
	 * @param bomDetails
	 * @param cpeDto
	 */
	private void setSfpModuleInGscCpeBomDetails(Set<ResourceDto> bomDetails, GvpnInternationalCpeDto cpeDto) {
		if(Objects.nonNull(cpeDto.getSfpModule())){
			LOGGER.info("SFP Modules is {}", cpeDto.getSfpModule());
			List<String> sfpModulesProductCodes = Arrays.asList(cpeDto.getSfpModule().split(","));
			List<CpeBomGscDetailView> byProductCodes = cpeBomGscDetailRepository.findByProductCodeIn(sfpModulesProductCodes);
			if(!CollectionUtils.isEmpty(byProductCodes)){
				byProductCodes.stream().forEach(cpeBomGscDetailView -> {
					ResourceDto resourceDto = new ResourceDto();
					resourceDto.setBomName(cpeBomGscDetailView.getBomName());
					resourceDto.setLongDesc(cpeBomGscDetailView.getLongDesc());
					resourceDto.setProductCategory(cpeBomGscDetailView.getProductCategory());
					resourceDto.setProductCode(cpeBomGscDetailView.getProductCode());
					bomDetails.add(resourceDto);
				});
			}
		}
	}

	/**
	 * Include user entered additional informations in cpe bom details
	 *
	 * @param bomDetails
	 * @param cpeDto
	 */
	private void setAdditionalInformationInGscCpeBomDetails(Set<ResourceDto> bomDetails, GvpnInternationalCpeDto cpeDto) {
		if (Objects.nonNull(cpeDto.getAdditionalCpeInformation())) {
			ResourceDto resourceDto = new ResourceDto();
			resourceDto.setBomName(cpeDto.getBom_name());
			resourceDto.setLongDesc(cpeDto.getAdditionalCpeInformation());
			resourceDto.setProductCategory(ADDITIONAL_CPE_INFORMATION);
			bomDetails.add(resourceDto);
		}
	}

	/**
	 *  Include user selected power cable information in cpe bom details
	 *
	 * @param bomDetails
	 * @param cpeDto
	 */
	private void setPowerCableInGscCpeBomDetails(Set<ResourceDto> bomDetails, GvpnInternationalCpeDto cpeDto) {
		if(Objects.nonNull(cpeDto.getPowerCable())){
			LOGGER.info("power cable info is {}", cpeDto.getPowerCable());
			List<VwCpeBomPowerCableGsc> byProductCodes = vwCpeBomPowerCableGscRepository.findByProductCode(cpeDto.getPowerCable());
			if(!CollectionUtils.isEmpty(byProductCodes)){
				ResourceDto resourceDto = new ResourceDto();
				resourceDto.setBomName(cpeDto.getBom_name());
				resourceDto.setProductCode(cpeDto.getPowerCable());
				resourceDto.setLongDesc(byProductCodes.stream().findFirst().get().getProductDescription());
				resourceDto.setProductCategory(CPE_POWER_CORD);
				bomDetails.add(resourceDto);
			}
		}
	}

	/**
	 * construct basic bom details
	 *
	 * @param bomDetails
	 * @param allcpeIntbom
	 */
	private void constructGscIntlCpeBomDetails(Set<ResourceDto> bomDetails, List<CpeBomGscIntlDetailView> allcpeIntbom) {
		if (!CollectionUtils.isEmpty(allcpeIntbom)) {
			LOGGER.info("allCpeIntBom size {} ", allcpeIntbom.size());
			allcpeIntbom.stream().filter(cpeBomGscDetailView -> !customProductCategories.contains(cpeBomGscDetailView.getProductCategory())).forEach(view -> {
				ResourceDto res = new ResourceDto();
				res.setBomName(view.getBomName());
				res.setProductCode(view.getProductCode());
				res.setLongDesc(view.getLongDesc());
				res.setListPrice(view.getListPrice());
				res.setProductCategory(view.getProductCategory());
				res.setQuantity(view.getQuantity());
				bomDetails.add(res);
			});
		}
	}

	/**
	 * Get gsc intl bom details
	 *
	 * @param bomDetails
	 * @param cpeDto
	 */
	private void getGscBomDetails(Set<ResourceDto> bomDetails, GvpnInternationalCpeDto cpeDto) {
		List<CpeBomGscDetailView> cpeIntbom;
		LOGGER.info("Parameters for cpe bom details for gsc gvpn are, country : {}, bomname : {}, port interface : {}, routing protocal : {}", cpeDto.getCountry(), cpeDto.getBom_name(), cpeDto.getPort_interface(), cpeDto.getRouting_protocol());
		cpeIntbom = cpeBomGscDetailRepository.findByBomNameAndPortInterfaceAndRoutingProtocol(cpeDto.getBom_name(), cpeDto.getPort_interface(), cpeDto.getRouting_protocol());
		if(Objects.nonNull(cpeDto.getPvdmAndMftQuantities())) {
			LOGGER.info("Checking for NIM's with blank for TDM type");
			Optional<String> anyNimWithQuantityZero = checkIfNimModulesAreBlank(cpeDto);
			if (!anyNimWithQuantityZero.isPresent()) {
				cpeIntbom = cpeIntbom.stream().filter(cpeBomGscIntlDetailView -> !cpeBomGscIntlDetailView.getProductCode().equalsIgnoreCase(NIM_BLANK)).collect(Collectors.toList());

			}
		}
		constructGscCpeBomDetails(bomDetails, cpeIntbom);
		setPowerCableInGscCpeBomDetails(bomDetails, cpeDto);
		setAdditionalInformationInGscCpeBomDetails(bomDetails, cpeDto);
		setSfpModuleInGscCpeBomDetails(bomDetails, cpeDto);
		setNimModuleInGscCpeBomDetails(bomDetails, cpeDto);
	}

	/**
	 * Check if nim modules are blank
	 *
	 * @param cpeDto
	 * @return
	 */
	private Optional<String> checkIfNimModulesAreBlank(GvpnInternationalCpeDto cpeDto) {
		List<String> individualNimModules = Arrays.asList(cpeDto.getPvdmAndMftQuantities().split(","));
		LOGGER.info("No of Individual NIM modules {}", individualNimModules.size());
		return individualNimModules.stream().filter(s -> {
			String[] splitByTypeAndQuantity = s.split(":");
			LOGGER.info("Type and quantity of nim are {}, {}", splitByTypeAndQuantity[0], splitByTypeAndQuantity[1]);
			if (Integer.valueOf(splitByTypeAndQuantity[1]) == 0) {
				LOGGER.info("Found a NIM module with blank");
				return true;
			}
			return false;
		}).findFirst();
	}

	/**
	 * Method to construct gsc cpe bom details
	 *
	 * @param bomDetails
	 * @param cpeIntbom
	 */
	private void constructGscCpeBomDetails(Set<ResourceDto> bomDetails, List<CpeBomGscDetailView> cpeBoms) {
		if (!CollectionUtils.isEmpty(cpeBoms)) {
			cpeBoms.stream().filter(cpeBomGscDetailView -> !customProductCategories.contains(cpeBomGscDetailView.getProductCategory())).forEach(view -> {
				ResourceDto res = new ResourceDto();
				res.setBomName(view.getBomName());
				res.setProductCode(view.getProductCode());
				res.setLongDesc(view.getLongDesc());
				res.setListPrice(view.getListPrice());
				res.setProductCategory(view.getProductCategory());
				res.setQuantity(view.getQuantity());
				bomDetails.add(res);
			});
		}
	}


	public Set<CpeBandwidthBean> getAllCpeValuesIrrespectiveOfBw(){
		Set<CpeBandwidthBean> cpeBwObj = cpeBomViewRepository.findAll()
				.stream()
				.map(cpeBomView -> {
					CpeBandwidthBean CpeBandwidthBean = new CpeBandwidthBean();
					CpeBandwidthBean.setCpeName(cpeBomView.getBomName());
					CpeBandwidthBean.setMaxBandwidth(cpeBomView.getMaxBandwidth());
					return CpeBandwidthBean;
				})
				.collect(Collectors.toSet());
		return cpeBwObj;
	}

	public Set<CpeBandwidthBean> getAllCpeValuesIrrespectiveOfBwForGvpn(){
		Set<CpeBandwidthBean> cpeBwObjGvpn = cpeBomGvpnViewRepository.findAll()
				.stream()
				.map(cpeBomView -> {
					CpeBandwidthBean cpeBandwidthBean = new CpeBandwidthBean();
					cpeBandwidthBean.setCpeName(cpeBomView.getBomName());
					cpeBandwidthBean.setMaxBandwidth(cpeBomView.getMaxBw());
					return cpeBandwidthBean;
				})
				.collect(Collectors.toSet());
		return cpeBwObjGvpn;
	}

	public Set<CpeBomDto> getCpeBomDetailsForNtwProducts(List<String> list) throws TclCommonException {
		Set<CpeBomDto> bomDetails = null;
		if (list == null || list.isEmpty())
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
		try {
			bomDetails = list.stream()
					.map(cpeBomId -> new CpeBomDto(bomMasterRepository.findByBomNameAndIsApplicalbeNtwProduct(cpeBomId)))
					.collect(Collectors.toSet());
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return bomDetails;
	}

	
	public BomInventoryCatalogAssocResponse getEquivalentProductCatalogBomNameForInventoryCpe(String oldCpeBom) throws TclCommonException {
		BomInventoryCatalogAssocResponse response = new BomInventoryCatalogAssocResponse();
		List<String> productCatalogCpeBomList = new ArrayList<>();
		if(oldCpeBom == null)
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
		
		try {
			LOGGER.info("input old cpe bom {}", oldCpeBom);
			List<BomInventoryCatalogAssoc> bomList = bomInventoryCatalogAssocRepository.findByInventoryBomName(oldCpeBom);
			if(bomList != null && !bomList.isEmpty()) {
				bomList.stream().forEach(cpeBom -> {
					productCatalogCpeBomList.add(cpeBom.getProductCatalogBomName());
				});
			}
			LOGGER.info("Returned list of prod catalog cpe boms productCatalogCpeBomList {}", productCatalogCpeBomList);
			response.setProductCatalogCpeBoms(productCatalogCpeBomList);
		}
		catch(Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return response;
	}
	
	/**
	 * Method to get product details by id
	 * @param productId
	 * @return
	 */
	public ProductInformationBean findProductDetailsById(Integer productId) {
		ProductInformationBean productInformationBean = new ProductInformationBean();
		try {
			LOGGER.info("Entering findProductDetailsById to fetch data for Id {} ",productId);
			Optional<ProductCatalog> prodFamilyList = productFamilyRepository.findById(productId);
			if(prodFamilyList.isPresent()) {
				productInformationBean.setProductName(prodFamilyList.get().getName());
				productInformationBean.setProductId(prodFamilyList.get().getId());
			}
			
		} catch(Exception e) {
			LOGGER.warn("error in getting product details by id", e);
		}
		return productInformationBean;
		 
	}
	
	/**
	 * Method to get product details by id
	 * @param productId
	 * @return
	 */
	public Boolean verifyCpeByName(String cpeName) {
		Boolean CpeAvailable = false;
		try {
			LOGGER.info("Entering verifyCpeByName to verify CPE {} ",cpeName);
			BomMaster bomMaster = bomMasterRepository.findByBomName(cpeName);
			List<BomInventoryCatalogAssoc> bomInvCatalog = bomInventoryCatalogAssocRepository.findByInventoryBomName(cpeName);
			
			if(Objects.nonNull(bomMaster) && bomInvCatalog!=null && !bomInvCatalog.isEmpty()) {
				CpeAvailable =  true;
			}
			
		} catch(Exception e) {
			LOGGER.error("error in verifyCpeByName {}", e);
		}
		return CpeAvailable;
	}
}
