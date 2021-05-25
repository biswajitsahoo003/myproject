package com.tcl.dias.products.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.productcatelog.entity.entities.AttributeGroupAttrAssoc;
import com.tcl.dias.productcatelog.entity.entities.AttributeGroupMaster;
import com.tcl.dias.productcatelog.entity.entities.AttributeMaster;
import com.tcl.dias.productcatelog.entity.entities.AttributeValue;
import com.tcl.dias.productcatelog.entity.entities.AttributeValueGroupAssoc;
import com.tcl.dias.productcatelog.entity.entities.AttributeValuesAlternate;
import com.tcl.dias.productcatelog.entity.entities.BomMaster;
import com.tcl.dias.productcatelog.entity.entities.BomPhysicalResourceAssoc;
import com.tcl.dias.productcatelog.entity.entities.CloudProviderAttribute;
import com.tcl.dias.productcatelog.entity.entities.Component;
import com.tcl.dias.productcatelog.entity.entities.ComponentAttributeGroupAssoc;
import com.tcl.dias.productcatelog.entity.entities.CpeBomGvpnView;
import com.tcl.dias.productcatelog.entity.entities.CpeBomView;
import com.tcl.dias.productcatelog.entity.entities.GscCountrySpecificDocumentList;
import com.tcl.dias.productcatelog.entity.entities.GvpnSlaCosView;
import com.tcl.dias.productcatelog.entity.entities.GvpnSlaView;
import com.tcl.dias.productcatelog.entity.entities.IasPriceBook;
import com.tcl.dias.productcatelog.entity.entities.IasSLAView;
import com.tcl.dias.productcatelog.entity.entities.IzoPcSlaView;
import com.tcl.dias.productcatelog.entity.entities.NplSlaView;
import com.tcl.dias.productcatelog.entity.entities.PhysicalResource;
import com.tcl.dias.productcatelog.entity.entities.Product;
import com.tcl.dias.productcatelog.entity.entities.ProductCatalog;
import com.tcl.dias.productcatelog.entity.entities.ProductComponentAssoc;
import com.tcl.dias.productcatelog.entity.entities.ProductDataCentreAssoc;
import com.tcl.dias.productcatelog.entity.entities.ProductServiceAreaMatrixIAS;
import com.tcl.dias.productcatelog.entity.entities.ProductSlaCosSpec;
import com.tcl.dias.productcatelog.entity.entities.Provider;
import com.tcl.dias.productcatelog.entity.entities.ServiceAreaMatrixDataCenter;
import com.tcl.dias.productcatelog.entity.entities.ServiceAreaMatrixGSCLNSView;
import com.tcl.dias.productcatelog.entity.entities.ServiceAreaMatrixGVPN;
import com.tcl.dias.productcatelog.entity.entities.ServiceAreaMatrixIAS;
import com.tcl.dias.productcatelog.entity.entities.ServiceAreaMatrixNPL;
import com.tcl.dias.productcatelog.entity.entities.SlaMetricMaster;
import com.tcl.dias.productcatelog.entity.entities.VwSdwanProductOffering;
import com.tcl.dias.products.constants.IzosdwanConstants;
import com.tcl.dias.products.dto.AttributeMasterDto;
import com.tcl.dias.products.dto.AttributeValueDto;
import com.tcl.dias.products.dto.ComponentDto;
import com.tcl.dias.products.dto.CpeBomDto;
import com.tcl.dias.products.dto.GvpnSlaRequestDto;
import com.tcl.dias.products.dto.ProductAttributeDto;
import com.tcl.dias.products.dto.ProductDto;
import com.tcl.dias.products.dto.ProductFamilyDto;
import com.tcl.dias.products.dto.ProductProfileDto;
import com.tcl.dias.products.dto.ProfileComponentDto;
import com.tcl.dias.products.dto.ProfileComponentListDto;
import com.tcl.dias.products.dto.ResourceDto;
import com.tcl.dias.products.dto.SLADto;
import com.tcl.dias.products.dto.ServiceAreaMatrixNPLDto;
import com.tcl.dias.products.gsc.beans.GscProductLocationDetailBean;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains object creation methods used in test cases.
 * 
 *
 * @author Dinahar Vivekanandan
 * 
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Service
public class ObjectCreator {

	Date date = new Date();
	private Product product = new Product();
	private ServiceAreaMatrixGVPN serviceAreaMatrixGVPN = new ServiceAreaMatrixGVPN();
	private Component component = new Component();
	private ProductCatalog productFamily = new ProductCatalog();
	private ProductComponentAssoc productComponentAssoc = new ProductComponentAssoc();
	private String serviceDetailsPath = "template/ILL.html";
	private ProductServiceAreaMatrixIAS serviceMatrixIAS = new ProductServiceAreaMatrixIAS();
	private SLADto slaDto = new SLADto();
	GvpnSlaView gvpnSlaView = new GvpnSlaView();
	List<GvpnSlaView> gvpnSlaViewList = new ArrayList<GvpnSlaView>();
	public static final String CITY_NAME = "Chennai";
	

	public CloudProviderAttribute getCloudProviderAttribute() {
		CloudProviderAttribute attribute = new CloudProviderAttribute();
		attribute.setAttributeName("name");
		attribute.setAttributeValue("value");
		attribute.setCloudProviderName("provider");
		attribute.setUom("uom");
		return attribute;
		
	}
	
	public List<ProductDataCentreAssoc> getDataCenterList(){
		List<ProductDataCentreAssoc> dataCenters = new ArrayList<>();
		dataCenters.add(new ProductDataCentreAssoc());
		return dataCenters;
	}
	
	public List<IzoPcSlaView> getIzoPcSlaViewList(){
		List<IzoPcSlaView> slas=  new ArrayList<>();
		slas.add(new IzoPcSlaView());
		return slas;
	}
	
	public List<CloudProviderAttribute> getCloudProviderAttributeList() {
		List<CloudProviderAttribute> list = new ArrayList<>();
		list.add(getCloudProviderAttribute());
		return list;
		
	}
	public ProductSlaCosSpec createProductSlaCosSpec() {
		ProductSlaCosSpec productSlaCosSpec = new ProductSlaCosSpec();
		productSlaCosSpec.setCos1Value("cos1 value");
		productSlaCosSpec.setCos2Value("cos2 value");
		productSlaCosSpec.setCos3Value("cos3 value");
		productSlaCosSpec.setCos4Value("cos4 value");
		productSlaCosSpec.setCos5Value("cos5 value");
		productSlaCosSpec.setCos6Value("cos6 value");
		return productSlaCosSpec;
	}
	
	public ComponentDto createComponentDto() {
		ComponentDto componentDto = new ComponentDto();
		componentDto.setDescription("desc");
		componentDto.setId(1);
		componentDto.setIsActive("Y");
		componentDto.setName("name");
		componentDto.setParentId(2);
		return componentDto;
	}

	public ServiceAreaMatrixGVPN createTierDetailForGvpn(String popTierCd) {
		ServiceAreaMatrixGVPN tierDetails = new ServiceAreaMatrixGVPN();
		tierDetails.setId(1);
		tierDetails.setLocationId("1");
		tierDetails.setPopTierCd(popTierCd);
		tierDetails.setCityNm("chennai");
		tierDetails.setCountry_dtl("india");
		tierDetails.setDualPop("dualPop");
		tierDetails.setFloorDetails("1st floor");
		tierDetails.setIsActive("Y");
		tierDetails.setIsDualPopInd("Y");
		tierDetails.setLocationDetails("JTP");
		tierDetails.setParentPoPDetails("pune");
		tierDetails.setPopType("pop");
		tierDetails.setRegionNm("south");
		tierDetails.setSiteCode("DFGTY678B");
		tierDetails.setStateNm("Tamilnadu");
		return tierDetails;
	}
	
	
	
	
	public GvpnSlaView getGvpnSlaView() {
		GvpnSlaView gvpnSlaView=new GvpnSlaView();
		gvpnSlaView.setAccessTopology("Routine");
		gvpnSlaView.setPdtCatalogId(1);
		gvpnSlaView.setPdtName("sla");
		gvpnSlaView.setRemarksTxt("remark");
		gvpnSlaView.setSlaIdNo(2);
		gvpnSlaView.setSlaName("Up Time");
		gvpnSlaView.setTier1Value("Tier 1");
		gvpnSlaView.setTier1Value("Tier 2");
		gvpnSlaView.setTier1Value("Tier 3");
		return gvpnSlaView;
	}

	public List<GvpnSlaView> getGvpnSlaViewList() {
		gvpnSlaViewList.add(getGvpnSlaView());
		return gvpnSlaViewList;
	}

	public GvpnSlaRequestDto createGvpnSlaRequest() {
		GvpnSlaRequestDto gvpnSlaRequestDto = new GvpnSlaRequestDto();
		gvpnSlaRequestDto.setSltVariant("Standard");
		gvpnSlaRequestDto.setAccessTopology("Routine");
		gvpnSlaRequestDto.setPopRegionSourceId("1");
		gvpnSlaRequestDto.setPopRegionDestId("2");
		gvpnSlaRequestDto.setNoOfPortsId(905);
		gvpnSlaRequestDto.setNoOfCpe(1);
		gvpnSlaRequestDto.setNoOfCpeId(911);
		gvpnSlaRequestDto.setLocalLoopType("Single");
		gvpnSlaRequestDto.setLocalLoopTypeId(908);
		gvpnSlaRequestDto.setCosValue(1);
		gvpnSlaRequestDto.setCosScheme("6 Cos");
		return gvpnSlaRequestDto;
	}

	public GvpnSlaRequestDto createGvpnSlaRequestForException() {
		GvpnSlaRequestDto gvpnSlaRequestDto = new GvpnSlaRequestDto();
		gvpnSlaRequestDto.setPopRegionSourceId("1");
		gvpnSlaRequestDto.setPopRegionDestId("2");
		gvpnSlaRequestDto.setNoOfPortsId(905);
		gvpnSlaRequestDto.setNoOfCpe(1);
		gvpnSlaRequestDto.setNoOfCpeId(911);
		gvpnSlaRequestDto.setLocalLoopType("Single");
		gvpnSlaRequestDto.setLocalLoopTypeId(908);
		gvpnSlaRequestDto.setCosValue(1);
		gvpnSlaRequestDto.setCosScheme("6 Cos");
		return gvpnSlaRequestDto;

	}

	public SLADto createSLADto() {
		slaDto.setFactor("factor");
		slaDto.setValue("value");
		return slaDto;
	}

	public List<Integer> createFactorGroupList(Integer serviceVariantId) {
		List<Integer> factorGroupList = new ArrayList<Integer>();
		factorGroupList.add(serviceVariantId);
		return factorGroupList;
	}

	public List<IasSLAView> createSLAViewList(Integer serviceVariantId, Integer slaId) {
		IasSLAView slaView = new IasSLAView();
		List<IasSLAView> slaViewList = new ArrayList<>();
		slaView.setFactorName("factorName");
		slaView.setFactorValue("factorValue");
		// slaView.setFactorGroupId(serviceVariantId);
		// slaView.setProductOfferingId(1);
		// slaView.setServiceVarientId(serviceVariantId);
		slaView.setFactorValueId(1);
		slaView.setSlaId(slaId);
		slaViewList.add(slaView);
		return slaViewList;
	}
	
	public NplSlaView createNplSlaView() {
		NplSlaView nplSlaView = new NplSlaView();
		nplSlaView.setSlaId(4);
		nplSlaView.setDefaultValue("99.5");
		return nplSlaView;
	}
	
	
	public List<NplSlaView> createEmptyNplSlaViewList() {
		List<NplSlaView> slaViewList = new ArrayList<>();
		return slaViewList;
	}

	public ProductServiceAreaMatrixIAS createServiceMatrixIAS() {
		serviceMatrixIAS.setTierId(1);
		serviceMatrixIAS.setId(1);
		return serviceMatrixIAS;
	}
	

	

	public List<ProductProfileDto> createProductProfileDtoList()
	{
		List<ProductProfileDto> productProfiles=new ArrayList<ProductProfileDto>();
		ProductProfileDto productProfileDto=new ProductProfileDto();
		productProfileDto.setBackup(true);
		productProfileDto.setProfileId(1);
		productProfileDto.setDescription("description");
		productProfileDto.setImage("image");
		productProfileDto.setProfileName("ProfileName");
		productProfileDto.setShowMore(false);
		productProfiles.add(productProfileDto);
		return productProfiles;
	}

	public Set<ProductAttributeDto> createProductAttributeDtoSet()
	{
		Set<ProductAttributeDto> productAttributes=new HashSet<ProductAttributeDto>();
		productAttributes.add(new ProductAttributeDto(createAttributeMaster(),new HashSet(createAttributeValueList()), createComponent()));
		return productAttributes;
	}
	public AttributeValueDto createAttributeValueDto() {
		AttributeValueDto attributeValueDto = new AttributeValueDto();
		attributeValueDto.setAttrId(1);
		attributeValueDto.setIsActive("Y");
		attributeValueDto.setReasonTxt("Reason");
		attributeValueDto.setAttrValue("value");
		return attributeValueDto;
	}

	public AttributeMasterDto createAttributeMasterDto() {
		AttributeMasterDto attrMasterDto = new AttributeMasterDto();
		attrMasterDto.setId(1);
		attrMasterDto.setIsActive("Y");
		attrMasterDto.setIsChargeable("Y");
		attrMasterDto.setDescription("desc");
		attrMasterDto.setName("name");
		return attrMasterDto;

	}

	public ProductComponentAssoc createProductComponentAssoc() {
		productComponentAssoc.setId(1);
		productComponentAssoc.setIsActive("Y");
		productComponentAssoc.setIsAdvanced("Y");
		productComponentAssoc.setIsBackup("Y");
		productComponentAssoc.setComponent(createComponent());
		productComponentAssoc.setProduct(createProduct());
		return productComponentAssoc;
	}
	
	public ProductComponentAssoc createProductComponentAssoc1() {
		productComponentAssoc.setId(1);
		productComponentAssoc.setIsActive("Y");
		productComponentAssoc.setIsAdvanced("Y");
		productComponentAssoc.setIsBackup("Y");
		productComponentAssoc.setComponent(createComponent1());
		productComponentAssoc.setProduct(createProduct());
		return productComponentAssoc;
	}
	

	public Component createComponent() {
		component.setId(1);
		component.setDescription("desc");
		component.setName("name");
		component.setIsActive("Y");
		component.setComponentAttributeGroupAssocs(createComponentAttributeGroupAssoc());
		return component;
	}
	
	public Component createComponent1() {
		component.setId(1);
		component.setDescription("desc");
		component.setName("name");
		component.setIsActive("N");
		return component;
	}

	public Product createProduct() {
		product.setParentId(3);
		product.setIsActive("Y");
		product.setIsAddonService("Y");
		product.setIsTemplate("Y");
		product.setDescription("desc");
		product.setName("name");
		product.setId(1);
		product.setUrl("url");
		return product;
	}

	public Optional<Product> createOptionalProduct() {
		
		product.setParentId(3);
		product.setIsActive("Y");
		product.setIsAddonService("Y");
		product.setIsTemplate("Y");
		product.setDescription("desc");
		product.setName("name");
		product.setId(1);
		product.setUrl("url");
		product.setProductComponentAssocs(createProductComponentAssocList());
		Optional<Product> optionalProd=Optional.of(product);
		return optionalProd;
	}
	
	public Product createInActiveProduct() {
		product.setIsActive("N");
		product.setIsAddonService("Y");
		product.setIsTemplate("Y");
		product.setDescription("desc");
		product.setName("name");
		product.setId(2);
		product.setUrl("url");

		return product;
	}

	public ProductCatalog createProductFamily() {
		productFamily.setEffectiveFrom(date);
		productFamily.setEffectiveTo(date);
		productFamily.setIsActive("Y");
		productFamily.setDescription("desc");
		productFamily.setName("name");
		productFamily.setUrl("url");
		productFamily.setId(1);
		productFamily.setServiceDetails("template/ILL.html");
		return productFamily;
	}

	public List<IasPriceBook> createIasPriceBookList() {
		IasPriceBook iasPriceBook = new IasPriceBook("1", "10.0", "10.0", "10.0", "10.0", "10.0");
		List<IasPriceBook> iasPriceBookList = new ArrayList<IasPriceBook>();
		iasPriceBookList.add(iasPriceBook);
		return iasPriceBookList;
	}

	public ProductFamilyDto createProductFamilyDto() {
		return new ProductFamilyDto(createProductFamily());
	}

	public List<ProductFamilyDto> createProductFamilyDtoList() {
		List<ProductFamilyDto> mockProductFamilyDtoList = new ArrayList<ProductFamilyDto>();
		mockProductFamilyDtoList.add(createProductFamilyDto());
		return mockProductFamilyDtoList;
	}

	public ProductDto createProductDto() {
		return new ProductDto(createProduct());
	}

	public ProductDto createInActiveProductDto() {
		return new ProductDto(createInActiveProduct());
	}

	public List<ProductDto> createProductDtoList() {
		List<ProductDto> mockProductDtoList = new ArrayList<ProductDto>();
		mockProductDtoList.add(createProductDto());
		return mockProductDtoList;
	}

	public List<ProductDto> createMixedProductDtoList() {
		List<ProductDto> mockProductDtoList = new ArrayList<ProductDto>();
		mockProductDtoList.add(createProductDto());
		mockProductDtoList.add(createInActiveProductDto());
		return mockProductDtoList;
	}

	public List<Product> createProductList() {
		List<Product> mockProductList = new ArrayList<Product>();
		mockProductList.add(createProduct());
		return mockProductList;
	}

	public List<ProductCatalog> createProductFamilyList() {
		List<ProductCatalog> mockProductFamilyList = new ArrayList<ProductCatalog>();
		mockProductFamilyList.add(createProductFamily());
		return mockProductFamilyList;
	}

	public List<ProductComponentAssoc> createProductComponentAssocList() {
		List<ProductComponentAssoc> mockProdCompAssocnList = new ArrayList<ProductComponentAssoc>();
		mockProdCompAssocnList.add(createProductComponentAssoc());
		return mockProdCompAssocnList;
	}

	public List<ProductComponentAssoc> createProductComponentAssocListWithIsActiveN() {
		List<ProductComponentAssoc> mockProdCompAssocnList = new ArrayList<ProductComponentAssoc>();
		mockProdCompAssocnList.add(createProductComponentAssoc1());
		
		return mockProdCompAssocnList;
	}
	
	public ObjectCreator() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<SLADto> createSlaDtoList() {
		List<SLADto> slaList = new ArrayList<SLADto>();

		SLADto rtd = new SLADto();
		rtd.setFactor("Round Trip Delay");
		rtd.setValue("<= 315");

		SLADto upTime = new SLADto();
		upTime.setFactor("Network up-time");
		upTime.setValue("99.95%");

		SLADto pktDrop = new SLADto();
		pktDrop.setFactor("Packet drop");
		pktDrop.setValue("<=315");

		slaList.add(rtd);
		slaList.add(upTime);
		slaList.add(pktDrop);

		return slaList;
	}

	public List<Map<Object, Object>> createListOfMapForSla() {
		List<Map<Object, Object>> slaList = new ArrayList<Map<Object, Object>>();
		slaList.add(createMapForSla());
		return slaList;

	}

	public Map<Object, Object> createMapForSla() {

		HashMap<Object, Object> slaMap = new HashMap<Object, Object>();

		slaMap.put("slaId", 1);
		slaMap.put("sla", "Round Trip Delay (ms)");
		slaMap.put("factorMasterId", 29);
		slaMap.put("factorMasterName", "Tier of Supplier PoP");
		slaMap.put("factorValueId", 53);
		slaMap.put("factorValue", "Tier2");
		slaMap.put("pdtOfferingId", 1);
		slaMap.put("pdtOffering", "Single Internet Access");
		slaMap.put("slaValueId", 2);
		slaMap.put("slaValue", "≤ 325 ms");
		slaMap.put("slaVerientId", 1);
		slaMap.put("serviceVarient", "Standard IAS");
		slaMap.put("slaFactorGroup", "RTD IAS Standard Tier-2");
		return slaMap;
	}

	public List<Map<Object, Object>> createProductSlaDtoListForException() throws TclCommonException {
		List<Map<Object, Object>> productSlaDtoList = new ArrayList<Map<Object, Object>>();
		return productSlaDtoList;
	}

	public List<String> createCountryList() {
		List<String> countryList = new ArrayList<String>();
		countryList.add("India");
		countryList.add("USA");
		return countryList;
	}
	
	public List<String> createCityList() {
		List<String> cityList = new ArrayList<String>();
		cityList.add("Delhi");
		cityList.add("Pune");
		return cityList;
	}

	public String getServiceDetailsPath() {
		return serviceDetailsPath;
	}

	public ResourceDto createResourceDto() {
		ResourceDto resource = new ResourceDto();
		resource.setId(1);
		return resource;
	}

	public List<ResourceDto> createResourceDtoSet() {
		List<ResourceDto> resourceSet = new ArrayList<ResourceDto>();
		resourceSet.add(createResourceDto());
		return resourceSet;
	}

	public CpeBomDto createCpeBomDto() {
		CpeBomDto cpeBom = new CpeBomDto();
		cpeBom.setId(1);
		cpeBom.setBomName("bomName");
		cpeBom.setResources(createResourceDtoSet());
		return cpeBom;
	}

	public CpeBomView createCpeBomView() {
		CpeBomView cpeBom = new CpeBomView();
		cpeBom.setBomId(1);
		cpeBom.setBomName("bomName");
		return cpeBom;
	}

	public Set<CpeBomDto> createCpeBomDtoSet() {
		Set<CpeBomDto> cpeBomDtoSet = new HashSet<CpeBomDto>();

		cpeBomDtoSet.add(createCpeBomDto());
		return cpeBomDtoSet;
	}

	public List<CpeBomView> createCpeBomViewList() {
		List<CpeBomView> cpeBomList = new ArrayList<CpeBomView>();
		cpeBomList.add(createCpeBomView());
		return cpeBomList;
	}

	public BomMaster createBomMaster() {
		BomMaster bomMaster = new BomMaster();
		bomMaster.setId(1);
		bomMaster.setBomName("bomName");
		bomMaster.setBomPhysicalResourceAssocList(createBomResourceAssList());
		return bomMaster;
	}

	public PhysicalResource createPhysicalResource() {
		PhysicalResource resource = new PhysicalResource();
		resource.setId(1);
		resource.setHsnCode("hsnCode");
		resource.setListPrice(new Double(200));
		resource.setListPriceCurrencyId(1);
		resource.setLongDesc("long desc");
		resource.setProductCode("prod code");
		resource.setProviderId(1);
		return resource;
	}

	public BomPhysicalResourceAssoc createBomResourceAss() {
		BomPhysicalResourceAssoc assoc = new BomPhysicalResourceAssoc();
		assoc.setId(1);
		assoc.setPhysicalResource(createPhysicalResource());
		return assoc;
	}

	public List<BomPhysicalResourceAssoc> createBomResourceAssList() {
		List<BomPhysicalResourceAssoc> assocList = new ArrayList<BomPhysicalResourceAssoc>();
		assocList.add(createBomResourceAss());
		return assocList;
	}

	public List<Integer> createIntegerList() {
		List<Integer> intList = new ArrayList<Integer>();
		intList.add(1);
		return intList;
	}

	public List<NplSlaView> createNplSlaViewList() {
		List<NplSlaView> slaList = new ArrayList<>();
		NplSlaView npl1 = new NplSlaView();
		npl1.setSlaId(1);
		npl1.setAccessTopology("path protection 3");
		npl1.setDefaultValue("99.5");
		npl1.setProductCatalogId(5);
		npl1.setProductName("NPL");
		npl1.setRemarksText("sample");
		npl1.setServiceVarient("standard");
		npl1.setSlaMetricId(3);
		npl1.setSlaName("Service Availability %");

		NplSlaView npl2 = new NplSlaView();
		npl2.setSlaId(1);
		npl2.setAccessTopology("path protection 3");
		npl2.setDefaultValue("99.5");
		npl2.setProductCatalogId(5);
		npl2.setProductName("NPL");
		npl2.setRemarksText("sample");
		npl2.setServiceVarient("Premium");
		npl2.setSlaMetricId(3);
		npl2.setSlaName("Service Availability %");
		slaList.add(npl1);
		slaList.add(npl2);
		return slaList;
	}
	
public List<NplSlaView> createNplSlaViewList1() {
		
		List<NplSlaView> slaList = new ArrayList<>();
		
		NplSlaView npl1 = new NplSlaView();
		npl1.setSlaId(1);
		npl1.setAccessTopology("path protection 3");
		npl1.setDefaultValue("98.5");
		npl1.setProductCatalogId(5);
		npl1.setProductName("NPL");
		npl1.setRemarksText("sample");
		npl1.setServiceVarient("standard");
		npl1.setSlaMetricId(3);
		npl1.setSlaName("Service Availability %");

		NplSlaView npl2 = new NplSlaView();
		npl2.setSlaId(1);
		npl2.setAccessTopology("path protection 3");
		npl2.setDefaultValue("99.5");
		npl2.setProductCatalogId(5);
		npl2.setProductName("NPL");
		npl2.setRemarksText("sample");
		npl2.setServiceVarient("Premium");
		npl2.setSlaMetricId(3);
		npl2.setSlaName("Service Availability %");
		slaList.add(npl1);
		slaList.add(npl2);
		return slaList;
	}

	public List<NplSlaView> createNplSlaViewListEmpty() {
		List<NplSlaView> slaList = new ArrayList<>();
		return slaList;
	}

	public AttributeValueDto createAttrValDto(Integer attrId,String attrVal) {
		AttributeValueDto attrValDto = new AttributeValueDto();
		attrValDto.setAttrId(attrId);
		attrValDto.setAttrValue(attrVal);
		return attrValDto;
	}
	
	public List<AttributeValueDto> createAttrValDtoList() {
		List<AttributeValueDto> cpeAttrList = new ArrayList<AttributeValueDto>();
		cpeAttrList.add(createAttrValDto(1056,"45 Mbps"));
		cpeAttrList.add(createAttrValDto(1062,"4 Gbps"));
		return cpeAttrList;
	}

	public Set<AttributeValueDto> createAttrValDtoSet() {
		Set<AttributeValueDto> cpeAttrSet = new HashSet<AttributeValueDto>();
		cpeAttrSet.add(createAttrValDto(1056,"45 Mbps"));
		cpeAttrSet.add(createAttrValDto(1062,"4 Gbps"));
		return cpeAttrSet;
	}
	public AttributeMaster createAttributeMaster() {
		AttributeMaster attrMaster=new AttributeMaster(); 
		attrMaster.setId(280);
		return attrMaster;
	}
	public AttributeGroupAttrAssoc createAttributeGroupAttrAssoc () {
		AttributeGroupAttrAssoc attrGrpAttrMaster=new AttributeGroupAttrAssoc(); 
		AttributeGroupMaster attrGroupMaster=new AttributeGroupMaster();
		attrGroupMaster.setId(58);
		attrGrpAttrMaster.setAttributeValueGroupAssoc(createAttributeValueGroupAssocList());
		attrGrpAttrMaster.setAttributeGroupMaster(attrGroupMaster);;
		return attrGrpAttrMaster;
	}
	public List<AttributeValueGroupAssoc> createAttributeValueGroupAssocList () {
		List<AttributeValueGroupAssoc> attrValGroupAssocList=new ArrayList<AttributeValueGroupAssoc>();
		AttributeValueGroupAssoc attrValGroupAssoc=new AttributeValueGroupAssoc();
		AttributeValuesAlternate attributeValuesAlternate=new AttributeValuesAlternate();
		attributeValuesAlternate.setAttrAlternateValue("value");
		attrValGroupAssoc.setId(1);
		AttributeValue attrValOne=new AttributeValue(); 
		attrValOne.setId(1054);
		attrValOne.setIsActive("N");
		attrValOne.setAttributeValuesAlternate(attributeValuesAlternate);
		attrValGroupAssoc.setAttributeValue(attrValOne);
		attrValGroupAssocList.add(attrValGroupAssoc);
		return attrValGroupAssocList;
	}
	public List<AttributeValue> createAttributeValueList () {
		List<AttributeValue> attrValList=new ArrayList<AttributeValue>();
		AttributeValue attrValOne=new AttributeValue();
		AttributeValue attrValtwo=new AttributeValue(); 
		AttributeValuesAlternate attributeValuesAlternate=new AttributeValuesAlternate();
		attributeValuesAlternate.setAttrAlternateValue("value");
		attrValOne.setId(1054);
		attrValOne.setAttrValues("45 Mbps");
		attrValOne.setIsActive("A");
		attrValOne.setAttributeValuesAlternate(attributeValuesAlternate);
		attrValtwo.setId(1055);
		attrValtwo.setAttrValues("4 Gbps");
		attrValtwo.setIsActive("A");
		attrValtwo.setAttributeValuesAlternate(attributeValuesAlternate);
		attrValList.add(attrValOne);
		attrValList.add(attrValtwo);
		return attrValList;
	}
	


	public List<IasSLAView> getProductSlaBean() {
		List<IasSLAView> slaViewList = new ArrayList<>();
		IasSLAView iasSlaView = new IasSLAView();
		iasSlaView.setDefaultValue("0");
		iasSlaView.setFactorName("Access type");
		iasSlaView.setFactorNameId(45);
		iasSlaView.setFactorValueId(56);
		iasSlaView.setFactorValue("OnNet");
		iasSlaView.setProductCatalogId(34);
		iasSlaView.setProductName("IAS");
		iasSlaView.setRemarksText("for LocalLoopType On-Net");
		iasSlaView.setSlaId(1);
		iasSlaView.setSlaIdNo(116);
		iasSlaView.setSlaMetricId(2);
		iasSlaView.setSlaName("Supplier Monthly Average Network uptime %");
		iasSlaView.setSltVariant("Varient");
		iasSlaView.setSubGroupId(2);
		iasSlaView.setTier1Value("99");
		iasSlaView.setTier2Value("99.09");
		iasSlaView.setTier3Value("99.90");
		slaViewList.add(iasSlaView);
		return slaViewList;
	}
	
	public ProfileComponentListDto createProfileComponentListDto()
	{
		ProfileComponentListDto componentList=new ProfileComponentListDto();
		
		ProfileComponentDto component1=new ProfileComponentDto();
		component1.setId(1);
		component1.setName("Private Lines");
		component1.setDisplay(false);
		component1.setDescription("Private Lines");
		Set<Integer> attributes=new HashSet<Integer>();
		attributes.add(304);
		attributes.add(305);
		attributes.add(306);
		component1.setAttributes(attributes);
		component1.setActive(true);
		
		ProfileComponentDto component2=new ProfileComponentDto();
		component2.setId(1);
		component2.setName("Private Lines");
		component2.setDisplay(false);
		component2.setDescription("Private Lines");
		Set<Integer> attributes1=new HashSet<Integer>();
		attributes1.add(304);
		attributes1.add(305);
		attributes1.add(306);
		component2.setAttributes(attributes);
		component2.setActive(true);
		
		Set<ProfileComponentDto> components=new HashSet<ProfileComponentDto>();
		components.add(component1);
		components.add(component2);
		componentList.setProfileComponents(components);
		return componentList;
	}
	
	public UserInformation getUserInformation() {
		UserInformation userInformation = new UserInformation();
		userInformation.setUserId("admin");
		userInformation.setCustomers(getCustomerList());
		userInformation.setRole(Arrays.asList("OPTIMUS_MU"));
		return userInformation;

	}
	
	public List<CustomerDetail> getCustomerList()
	{
		List<CustomerDetail>  list=new ArrayList<>();
		list.add(createCustomerDetails());
		return list;
		
		
		
	}
	
	public CustomerDetail createCustomerDetails()
	{
		CustomerDetail cd=new CustomerDetail();
		cd.setCustomerAcId("test");
		cd.setCustomerEmailId("test@gmail.com");

		cd.setCustomerId(1);
		cd.setCustomerLeId(1);
		cd.setErfCustomerId(1);
		cd.setStatus((byte)1);
		return cd;	
		
	}
	
	public List<ComponentAttributeGroupAssoc> createComponentAttributeGroupAssoc()
	{
		List<ComponentAttributeGroupAssoc> componentAttrGroupAssoc=new ArrayList<ComponentAttributeGroupAssoc>();
		ComponentAttributeGroupAssoc attrGroupAssoc=new ComponentAttributeGroupAssoc();
		attrGroupAssoc.setId(10);
		attrGroupAssoc.setIsActive("Y");
		Component component=new Component();
		component.setId(23);
		component.setName("XXXX");
		attrGroupAssoc.setComponent(component);
		AttributeGroupMaster attrGroupMaster=new AttributeGroupMaster();
		attrGroupMaster.setId(12);
		attrGroupMaster.setName("YYY");
		attrGroupAssoc.setAttributeGroupMaster(attrGroupMaster);
		attrGroupAssoc.setComponent(component);
		ComponentAttributeGroupAssoc attrGroupAssoc1=new ComponentAttributeGroupAssoc();
		attrGroupAssoc1.setId(10);
		attrGroupAssoc1.setIsActive("Y");
		Component component1=new Component();
		component.setId(23);
		component.setName("XXXX");
		attrGroupAssoc1.setComponent(component1);
		AttributeGroupMaster attrGroupMaster1=new AttributeGroupMaster();
		attrGroupMaster1.setId(12);
		attrGroupMaster1.setName("YYY");
		attrGroupMaster1.setAttributeGroupAssocList(createAttributeGroupAttrAssocList());
		/*attrGroupMaster1.setAttributeValueGroupAssoc(createAttributeValueGroupAssocList());*/
		attrGroupAssoc1.setAttributeGroupMaster(attrGroupMaster1);
		attrGroupAssoc1.setComponent(component1);
		componentAttrGroupAssoc.add(attrGroupAssoc1);
		
		return componentAttrGroupAssoc;
		
	}
	
	public List<AttributeGroupAttrAssoc> createAttributeGroupAttrAssocList()
	{
		List<AttributeGroupAttrAssoc> attrGroupAttrAssocList=new ArrayList<AttributeGroupAttrAssoc>();
		
		AttributeGroupAttrAssoc attrGroupAttrAssoc1=new AttributeGroupAttrAssoc();
		attrGroupAttrAssoc1.setId(23);
		attrGroupAttrAssoc1.setAttributeGroupMaster(new AttributeGroupMaster());
		attrGroupAttrAssoc1.setAttributeMaster(new AttributeMaster());
		attrGroupAttrAssoc1.setIsActive("Y");
		attrGroupAttrAssoc1.setAttributeValueGroupAssoc(createAttributeValueGroupAssocList());
		
		AttributeGroupAttrAssoc attrGroupAttrAssoc2=new AttributeGroupAttrAssoc();
		attrGroupAttrAssoc2.setId(23);
		attrGroupAttrAssoc2.setAttributeGroupMaster(new AttributeGroupMaster());
		attrGroupAttrAssoc2.setAttributeMaster(new AttributeMaster());
		attrGroupAttrAssoc2.setIsActive("Y");
		attrGroupAttrAssoc2.setAttributeValueGroupAssoc(createAttributeValueGroupAssocList());
		attrGroupAttrAssocList.add(attrGroupAttrAssoc1);
		attrGroupAttrAssocList.add(attrGroupAttrAssoc2);
		return attrGroupAttrAssocList;
	}

	/**
	 * Populate ServiceAreaMatrixNPLDto List
	 *
	 * @return
	 */
	public List<ServiceAreaMatrixNPLDto> getServiceAreaMatrixNPLDtos() {
		ServiceAreaMatrixNPLDto serviceAreaMatrixNPLDto = new ServiceAreaMatrixNPLDto();
		serviceAreaMatrixNPLDto.setTownsDtl("Chennai");
		List<ServiceAreaMatrixNPLDto> list = new ArrayList<>();
		list.add(serviceAreaMatrixNPLDto);
		return list;
	}
	public ServiceAreaMatrixNPL getServiceAreaMatrixNPL() {
	ServiceAreaMatrixNPL serviceAreaMatrixNPL = new ServiceAreaMatrixNPL();
	serviceAreaMatrixNPL.setId(1);
	serviceAreaMatrixNPL.setIsActive("active");
	serviceAreaMatrixNPL.setLatitudeNbr("87.89");
	serviceAreaMatrixNPL.setLongitudeNbr("67.89");
	serviceAreaMatrixNPL.setPopAddressTxt("jtp");
	serviceAreaMatrixNPL.setPopLocationId("23");
	serviceAreaMatrixNPL.setRegionDtl("south");
	serviceAreaMatrixNPL.setTownsDtl("Chennai");
	return serviceAreaMatrixNPL;	
	}
	
	/**
	 * Populate ServiceAreaMatrixNPL List
	 *
	 * @return
	 */
	public List<ServiceAreaMatrixNPL> getServiceAreaMatrixNPLs() {
		List<ServiceAreaMatrixNPL> list = new ArrayList<>();
		list.add(getServiceAreaMatrixNPL());
		return list;
	}
	
	public List<ServiceAreaMatrixGSCLNSView> createServiceAreaMatrixGSCLNSView()
	{
		List<ServiceAreaMatrixGSCLNSView> serviceAreaMatrixGSCLNSList=new ArrayList<ServiceAreaMatrixGSCLNSView>();
		
		ServiceAreaMatrixGSCLNSView s1 = new ServiceAreaMatrixGSCLNSView();
		s1.setProductName("gsc");
		s1.setCityCode("11");
		s1.setIso3CountryCode("22");
		s1.setIsoCountryName("india");
		s1.setCityName("chennai");
		s1.setPortabilityText("text");
		s1.setCommentsText("text");
		s1.setNumberSimultaneousCallsPerNumber("number");
		
		ServiceAreaMatrixGSCLNSView s2 = new ServiceAreaMatrixGSCLNSView();
		s2.setProductName("gsc");
		s2.setCityCode("13");
		s2.setIso3CountryCode("22");
		s2.setIsoCountryName("india");
		s2.setCityName("pune");
		s2.setPortabilityText("text");
		s2.setCommentsText("text");
		s2.setNumberSimultaneousCallsPerNumber("number");
		
		serviceAreaMatrixGSCLNSList.add(s1);
		serviceAreaMatrixGSCLNSList.add(s2);
		
		return serviceAreaMatrixGSCLNSList;
	}
	
	public GscProductLocationDetailBean getProductLocationDetailBean() {
		GscProductLocationDetailBean gscProductLocationDetailBean = new GscProductLocationDetailBean();
		gscProductLocationDetailBean.setName("chennai");
		gscProductLocationDetailBean.setCode("11");
		
		return gscProductLocationDetailBean;	
		}
		
		/**
		 * Populate GSCProductLocationDetailBean List
		 *
		 * @return
		 */
		public List<GscProductLocationDetailBean> getProductLocationDetailBeans() {
			List<GscProductLocationDetailBean> list = new ArrayList<>();
			list.add(getProductLocationDetailBean());
			return list;
		}
	

	
	public ServiceAreaMatrixDataCenter getServiceAreaMatrixDataCenter(){
		ServiceAreaMatrixDataCenter serviceAreaMatrixDataCenter=new ServiceAreaMatrixDataCenter();
		serviceAreaMatrixDataCenter.setId(1);
		serviceAreaMatrixDataCenter.setDcType("dc");
		serviceAreaMatrixDataCenter.setTownsDtl("Chennai");
		serviceAreaMatrixDataCenter.setRegionDtl("south");
		serviceAreaMatrixDataCenter.setAddressTxt("JTP");
		serviceAreaMatrixDataCenter.setLongitudeNbr("68.98078");
		serviceAreaMatrixDataCenter.setLatitudeNbr("89,0976");
		serviceAreaMatrixDataCenter.setLocationId("45");
		serviceAreaMatrixDataCenter.setPincodeNbr("6000089");
		serviceAreaMatrixDataCenter.setLocalityDtl("Nandambakam");
		serviceAreaMatrixDataCenter.setStateDtl("Tamilnadu");
		return serviceAreaMatrixDataCenter;
	}
	public List<ServiceAreaMatrixDataCenter> getServiceAreaMatrixDataCenterList(){
		List<ServiceAreaMatrixDataCenter> serviceAreaMatrixDataCenterList=new ArrayList<>();
		serviceAreaMatrixDataCenterList.add(getServiceAreaMatrixDataCenter());
		return serviceAreaMatrixDataCenterList;
	}
	public ServiceAreaMatrixIAS getServiceAreaMatrixIAS(Integer tierCdNrb) {
		ServiceAreaMatrixIAS serviceAreaMatrixIAS=new ServiceAreaMatrixIAS();
		serviceAreaMatrixIAS.setCityDtl("chennai");
		serviceAreaMatrixIAS.setId(1);
		serviceAreaMatrixIAS.setIsActiveInd("Y");
		serviceAreaMatrixIAS.setNetworkLocationId("23");
		serviceAreaMatrixIAS.setPopAddressDtl("JTP");
		serviceAreaMatrixIAS.setPopName("TCL pop");
		serviceAreaMatrixIAS.setPopSts("popsts");
		serviceAreaMatrixIAS.setStateDtl("tamilnadu");
		serviceAreaMatrixIAS.setTierCdNrb(tierCdNrb);
		return serviceAreaMatrixIAS;
	}
	public ProductSlaCosSpec getProductSlaCosSpec() {
		ProductSlaCosSpec productSlaCosSpec=new ProductSlaCosSpec();
		productSlaCosSpec.setCosSchemaNm("6 Cos");
		productSlaCosSpec.setId(1);
		productSlaCosSpec.setPdtCatalogId(2);
		productSlaCosSpec.setSlaMetricMaster(new SlaMetricMaster());
		productSlaCosSpec.setCos1Value(">= 99.99%");
		productSlaCosSpec.setCos2Value(">= 99.99%");
		productSlaCosSpec.setCos3Value(">= 99.99%");
		productSlaCosSpec.setCos4Value(">= 99.99%");
		productSlaCosSpec.setCos5Value(">= 99.99%");
		productSlaCosSpec.setCos6Value(">= 99.99%");
		return productSlaCosSpec;
	}
	public GvpnSlaCosView getGvpnSlaCosView() {
		GvpnSlaCosView gvpnSlaCosView=new GvpnSlaCosView();
		gvpnSlaCosView.setCosSchemaName("6 Cos");
		gvpnSlaCosView.setPdtCatalogId(1);
		gvpnSlaCosView.setPdtName("GVPN");
		gvpnSlaCosView.setPopTierCd("1");
		gvpnSlaCosView.setSlaId(3);
		gvpnSlaCosView.setSlaIdNo(4);
		gvpnSlaCosView.setSlaName("Packet Delivery Ratio Service Level Target %");
		gvpnSlaCosView.setUomCd("ms");
		gvpnSlaCosView.setCos1Value(">= 99.99%");
		gvpnSlaCosView.setCos2Value(">= 99.99%");
		gvpnSlaCosView.setCos3Value(">= 99.99%");
		gvpnSlaCosView.setCos4Value(">= 99.99%");
		gvpnSlaCosView.setCos5Value(">= 99.99%");
		gvpnSlaCosView.setCos6Value(">= 99.99%");
		return gvpnSlaCosView;
	}
	public CpeBomGvpnView getCpeBomGvpnView() {
		CpeBomGvpnView cpeBomGvpnView=new CpeBomGvpnView();
		cpeBomGvpnView.setBomName("C867VAE-K9");
		cpeBomGvpnView.setMaxBwInMbps(45D);
		return cpeBomGvpnView;
	}
	public List<CpeBomGvpnView> getCpeBomGvpnViewList() {
		List<CpeBomGvpnView> cpeBomGvpnViewList=new ArrayList<>();
		cpeBomGvpnViewList.add(getCpeBomGvpnView());
		return cpeBomGvpnViewList;
	}
	public CpeBomDto getCpeBomDto() {
		CpeBomDto cpeBomDto=new CpeBomDto();
		List<ResourceDto> resourceDtoList=new ArrayList<>();
		resourceDtoList.add(new ResourceDto());
		cpeBomDto.setResources(resourceDtoList);
		return cpeBomDto;
	}

	public List<String> getCloudProviderName() {
		
		List<String> providerName=new ArrayList<>();
		providerName.add("Oracle fast Connet");
		
		return providerName;
	}

	public List<ProductDataCentreAssoc> getDataCenterDetails() {
		List<ProductDataCentreAssoc>  productDataCentreAssocs=new ArrayList<>();
		ProductDataCentreAssoc productDataCentreAssoc=new ProductDataCentreAssoc();
		productDataCentreAssoc.setCloudProviderName("Oracle fast Connet");
		productDataCentreAssoc.setDataCenterAddress("Laarderhoogtweg 57  1101 EB Amsterdam, The Netherlands");
		productDataCentreAssoc.setDataCenterCity("Amsterdam");
		productDataCentreAssoc.setRemarks("yes");
		productDataCentreAssoc.setProviderId(1);
		productDataCentreAssoc.setProductName("IZOPC");
		productDataCentreAssoc.setProductCatalogId(1);
		productDataCentreAssoc.setInterfaceName("Interface");
		productDataCentreAssoc.setDataCentrSiteCode("AUVX");
		productDataCentreAssoc.setDataCentreDec("Equinix AMS 2");
		productDataCentreAssoc.setDataCenterLatitude("4.942308");
		productDataCentreAssoc.setDataCenterLongitude("4.942308");
		productDataCentreAssocs.add(productDataCentreAssoc);
		return productDataCentreAssocs;
	} 
	public Optional<GscCountrySpecificDocumentList> getDocumentUID() {
		GscCountrySpecificDocumentList gscCountrySpecificDocumentList=new GscCountrySpecificDocumentList();
		gscCountrySpecificDocumentList.setUID("ABC1234");
		return Optional.of(gscCountrySpecificDocumentList);
	}
	
	
	public Optional <ServiceAreaMatrixGVPN> createServiceAreaMatrixGVPN() {
		serviceAreaMatrixGVPN.setLocationId("1");
		serviceAreaMatrixGVPN.setRegionNm("namdambakkam");
		serviceAreaMatrixGVPN.setStateNm("tamilnadu");
		serviceAreaMatrixGVPN.setCityNm("chennai");
		serviceAreaMatrixGVPN.setLocationDetails("locationdetails");
		serviceAreaMatrixGVPN.setFloorDetails("floorDetails");
		serviceAreaMatrixGVPN.setParentPoPDetails("parentPoPDetails");
		serviceAreaMatrixGVPN.setPopType("pop");
		serviceAreaMatrixGVPN.setDualPop("dual");
		serviceAreaMatrixGVPN.setPopTierCd("poptiercd");
		serviceAreaMatrixGVPN.setPopSts("popsts");
		serviceAreaMatrixGVPN.setIsDualPopInd("y");
		serviceAreaMatrixGVPN.setCountry_dtl("india");
		serviceAreaMatrixGVPN.setSiteCode("101");
		Optional<ServiceAreaMatrixGVPN> serviceAreaMatrixGVPN1 = Optional.of(serviceAreaMatrixGVPN);
		return serviceAreaMatrixGVPN1;
	}
	
	public List<String> createProductLocationList(){
		List<String> productLocation = new ArrayList<>();
		productLocation.add("chennai");
		productLocation.add("pune");
		return productLocation;
	}
	
	public Provider getCloudProviderDetail() throws TclCommonException {
		Provider attribute = new Provider();
		attribute.setAlternateContactNo("999887654");
		attribute.setContactNo("887789876");
		attribute.setEmailId("tcl@gmail.com");
		attribute.setDescription("desc");
		attribute.setName("mb");
		attribute.setAliasName("alias");
		//attribute.setProviderType(providerType);
		return attribute;	
	}
	
	/**
	 * 
	 * Get the vendor list for IZOSDWAN
	 * @return
	 */
	public List<String> getTheVendors(){
		List<String> vendorList = new ArrayList<>();
		vendorList.add(IzosdwanConstants.VERSA_VENDOR_CODE);
		return vendorList;
	}
	
	/**
	 * 
	 * Get the profiles for IZOSDWAN
	 * @return
	 */
	public List<String> getProfiles(){
		List<String> profiles = new ArrayList<>();
		profiles.add("Basic");
		return profiles;
	}
	
	/**
	 * 
	 * Get the product offerings list for IZOSDWAN
	 * @return
	 */
	public List<VwSdwanProductOffering> getProductOfferingsList(){
		List<VwSdwanProductOffering> vwSdwanProductOfferings = new ArrayList<>();
		VwSdwanProductOffering vwSdwanProductOffering = new VwSdwanProductOffering();
		vwSdwanProductOffering.setAddonCd("Test");
		vwSdwanProductOffering.setAddonDescription("Desc1 \r\n Desc2");
		vwSdwanProductOffering.setAddonName("Test");
		vwSdwanProductOffering.setId(1);
		vwSdwanProductOffering.setMrc("10");
		vwSdwanProductOffering.setNrc("10");
		vwSdwanProductOffering.setProfileCd("Basic");
		vwSdwanProductOffering.setProfileDescription("Desc1 \r\n Desc2");
		vwSdwanProductOffering.setProfileName("Basic");
		vwSdwanProductOffering.setVendorCd(IzosdwanConstants.VERSA_VENDOR_CODE);
		vwSdwanProductOfferings.add(vwSdwanProductOffering);
		return vwSdwanProductOfferings;
	}

	
}
