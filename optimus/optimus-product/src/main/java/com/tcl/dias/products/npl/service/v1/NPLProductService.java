package com.tcl.dias.products.npl.service.v1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.products.dto.DataCenterBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.productcatelog.entity.entities.NplSlaView;
import com.tcl.dias.productcatelog.entity.entities.ServiceAreaMatrixDataCenter;
import com.tcl.dias.productcatelog.entity.entities.ServiceAreaMatrixNPL;
import com.tcl.dias.productcatelog.entity.repository.DataCenterRepository;
import com.tcl.dias.productcatelog.entity.repository.NplSlaViewRepository;
import com.tcl.dias.productcatelog.entity.repository.ProductCompAssnRepository;
import com.tcl.dias.productcatelog.entity.repository.ProductFamilyRepository;
import com.tcl.dias.productcatelog.entity.repository.ProductRepository;
import com.tcl.dias.productcatelog.entity.repository.ServiceAreaMatrixNPLRepository;
import com.tcl.dias.products.constants.DataCenterType;
import com.tcl.dias.products.constants.ExceptionConstants;
import com.tcl.dias.products.dto.SLADto;
import com.tcl.dias.products.dto.ServiceAreaMatrixNPLDto;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * Service class for NPL product related operations
 * 
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Service
@Transactional
public class NPLProductService {

	@Autowired
	NplSlaViewRepository nplSlaViewRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	ProductFamilyRepository productFamilyRepository;

	@Autowired
	ProductCompAssnRepository productCompAssnRepository;

	@Autowired
	ServiceAreaMatrixNPLRepository serviceAreaMatrixNPLRepository;

	@Autowired
	DataCenterRepository dataCenterRepository;

	public static final Logger LOGGER = LoggerFactory.getLogger(NPLProductService.class);

	/**
	 * SLA Values for NPL product
	 * 
	 * @author Thamizhselvi Perumal
	 * @param serviceVarient Value of service variant
	 * @param accessTopology Value of access topology
	 * @return List<SLADto> Returns the list of slaDto objects
	 * @throws TclCommonException
	 */
	public List<SLADto> getSlaValue(String serviceVarient, String accessTopology) throws TclCommonException {

		List<SLADto> slaDtoList = new ArrayList<>();
		try {
			if (Objects.isNull(serviceVarient) || Objects.isNull(accessTopology))
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			/*
			 * Get NPL slaValue based on serviceVarient and accessTopology
			 */
			List<NplSlaView> nplSlaViewList = nplSlaViewRepository.findByServiceVarientAndAccessTopology(serviceVarient,
					accessTopology);
			if (nplSlaViewList != null && !nplSlaViewList.isEmpty())
				slaDtoList = getSLADtoList(nplSlaViewList);
			else
				throw new TclCommonException(ExceptionConstants.NPL_PRODUCT_SLA_EMPTY,
						ResponseResource.R_CODE_NOT_FOUND);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);

		}
		return slaDtoList;
	}

	/**
	 * Method to convert nplSlaViewList into slaDtoList
	 * 
	 * @param nplSlaViewList List of nplSlaView entities
	 * @return List<SLADto> Returns the list of slaDto objects
	 */
	public List<SLADto> getSLADtoList(List<NplSlaView> nplSlaViewList) {
		return nplSlaViewList.stream().map(SLADto::new).collect(Collectors.toList());
	}

	/**
	 * Method to minimal uptime value for NPL
	 * 
	 * @author Dinahar Vivekanandan
	 * @return String - Returns the minimal value as String
	 * @throws TclCommonException
	 */
	public String getMinimalUptime() throws TclCommonException {
		List<NplSlaView> nplSlaViewList = nplSlaViewRepository.findAll();
		if (nplSlaViewList.isEmpty())
			throw new TclCommonException(ExceptionConstants.NPL_PRODUCT_SLA_EMPTY, ResponseResource.R_CODE_NOT_FOUND);

		nplSlaViewList.sort(Comparator.comparingDouble(NplSlaView::getDefaultValueInDouble));
		return nplSlaViewList.get(0).getDefaultValue();
	}

	/**
	 * To Get NPL Pop Location Details
	 *
	 * @param cityName
	 * @return List<ServiceAreaMatrixNPLDto>
	 * @throws TclCommonException
	 */
	public List<ServiceAreaMatrixNPLDto> getNplPopLocationDetails(final String cityName) throws TclCommonException {
		final List<ServiceAreaMatrixNPL> serviceAreaMatrixNPLs;
		final List<ServiceAreaMatrixNPLDto> serviceAreaMatrixNPLDtos;

		if (StringUtils.isEmpty(cityName)) {
			serviceAreaMatrixNPLs = serviceAreaMatrixNPLRepository.findAll();
		} else {
			serviceAreaMatrixNPLs = serviceAreaMatrixNPLRepository.findByTownsDtl(cityName);
		}

		if (serviceAreaMatrixNPLs.isEmpty()) {
			throw new TclCommonException(ExceptionConstants.NPL_PRODUCT_POP_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}

		serviceAreaMatrixNPLDtos = serviceAreaMatrixNPLs.stream()
				.map(serviceAreaMatrixNPL -> convertServiceAreaMatrixNPL(serviceAreaMatrixNPL))
				.collect(Collectors.toList());
		return serviceAreaMatrixNPLDtos;
	}

	/**
	 * Convert Entity to DTO
	 *
	 * @param serviceAreaMatrixNPL
	 * @return
	 */
	private ServiceAreaMatrixNPLDto convertServiceAreaMatrixNPL(final ServiceAreaMatrixNPL serviceAreaMatrixNPL) {
		return new ServiceAreaMatrixNPLDto(serviceAreaMatrixNPL.getId(), serviceAreaMatrixNPL.getPopLocationId(),
				serviceAreaMatrixNPL.getTownsDtl(), serviceAreaMatrixNPL.getRegionDtl(),
				serviceAreaMatrixNPL.getPopAddressTxt(), serviceAreaMatrixNPL.getLongitudeNbr(),
				serviceAreaMatrixNPL.getLatitudeNbr());
	}

	/**
	 * getServiceAreaMatrixDc - Method to fetch data center details
	 * @return List of DataCenterBeans
	 * @throws TclCommonException
	 */
	public List<DataCenterBean> getServiceAreaMatrixDc(String cityName) throws TclCommonException {
		List<DataCenterBean> dataCenterBeans;
		List<ServiceAreaMatrixDataCenter> serviceAreaMatrixDcList;

		if(Objects.nonNull(cityName) && !cityName.isEmpty()){
			serviceAreaMatrixDcList=dataCenterRepository.findByDcTypeAndTownsDtlAndIsActive(DataCenterType.INDIA_DATA_CENTER.getDcType(), cityName,"Y");
		}else {
			serviceAreaMatrixDcList = dataCenterRepository.findByDcTypeAndIsActive(DataCenterType.INDIA_DATA_CENTER.getDcType(), "Y");
		}
		LOGGER.info("Request query for DC: {} "+cityName);
		if (serviceAreaMatrixDcList.isEmpty()) {
			throw new TclCommonException(ExceptionConstants.NPL_PRODUCT_DC_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}

		dataCenterBeans = serviceAreaMatrixDcList.stream()
				.map(DataCenterBean::new)
				.collect(Collectors.toList());

		return dataCenterBeans;
	}
	public List<String> getCitiesForDataCenter(){
		List<String> cities=new ArrayList<>();
		cities.add("All");
		cities=dataCenterRepository.findDistinctCitiesForDC();
		if(Objects.nonNull(cities) && !cities.isEmpty())
			cities=cities.stream().sorted().collect(Collectors.toList());
		return cities;
	}
	
}
