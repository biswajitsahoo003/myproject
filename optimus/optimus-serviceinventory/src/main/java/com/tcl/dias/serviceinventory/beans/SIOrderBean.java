package com.tcl.dias.serviceinventory.beans;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.serviceinventory.entity.entities.SIOrder;
import com.tcl.dias.serviceinventory.entity.entities.SIServiceDetail;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Bean class to hold SI order details
 *
 * @author Dinahar V
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SIOrderBean {

	private Integer id;
	private Integer erfCustomerId;
	private Set<SIServiceDetailBean> serviceDetails;

	public SIOrderBean() {

	}

	public SIOrderBean(SIOrder entity) {
		if (Objects.nonNull(entity)) {
			this.setId(entity.getId());
			this.setErfCustomerId(Integer.valueOf(entity.getErfCustCustomerId()));
			if (Objects.nonNull(entity.getSiServiceDetails())) {
				List<SIServiceDetail> serviceDetailList = entity
						.getSiServiceDetails().stream().filter(siServiceDetail -> siServiceDetail
								.getErfPrdCatalogProductName().equalsIgnoreCase(CommonConstants.GVPN))
						.collect(Collectors.toList());
				this.setServiceDetails(
						serviceDetailList.stream().map(SIServiceDetailBean::new).collect(Collectors.toSet()));
			}

		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getErfCustomerId() {
		return erfCustomerId;
	}

	public void setErfCustomerId(Integer erfCustomerId) {
		this.erfCustomerId = erfCustomerId;
	}

	public Set<SIServiceDetailBean> getServiceDetails() {
		return serviceDetails;
	}

	public void setServiceDetails(Set<SIServiceDetailBean> serviceDetails) {
		this.serviceDetails = serviceDetails;
	}

}
