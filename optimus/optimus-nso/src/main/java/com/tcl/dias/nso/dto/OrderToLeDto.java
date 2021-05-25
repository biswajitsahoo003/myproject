package com.tcl.dias.nso.dto;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.oms.entity.entities.OrderToLe;

/**
 * Data transfer object for OrderToLe Entity class
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@JsonInclude(Include.NON_NULL)
public class OrderToLeDto {

	@NotNull(message = Constants.ID_NULL)
	private Integer id;

	@NotNull(message = Constants.FOREIGN_KEY_NULL)
	private Integer orderId;

	@JsonProperty("attributes")
	private Set<OrderToLeAttributeValueDto> orderToLeAttributeValueDtos;

	@JsonProperty("sites")
	private Set<OrderIllsitesDto> orderIllsitesDto;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public OrderToLeDto(OrderToLe orderToLe) {
		if (orderToLe != null) {
			this.setId(orderToLe.getId());
			this.setOrderId(orderToLe.getOrder().getId());

			if (orderToLe.getOrdersLeAttributeValues() != null && !orderToLe.getOrdersLeAttributeValues().isEmpty())
				this.setOrderToLeAttributeValueDtos(orderToLe.getOrdersLeAttributeValues().stream()
						.map(OrderToLeAttributeValueDto::new).collect(Collectors.toSet()));

			if (orderToLe.getOrderToLeProductFamilies() != null && !orderToLe.getOrderToLeProductFamilies().isEmpty())
				this.setOrderIllsitesDto(orderToLe.getOrderToLeProductFamilies().stream()
						.flatMap(orderToLeProductFamily -> orderToLeProductFamily.getOrderProductSolutions().stream())
						.flatMap(orderProductSolution -> orderProductSolution.getOrderIllSites().stream())
						.map(OrderIllsitesDto::new).collect(Collectors.toSet()));

		}

	}

	public Set<OrderToLeAttributeValueDto> getOrderToLeAttributeValueDtos() {
		return orderToLeAttributeValueDtos;
	}

	public void setOrderToLeAttributeValueDtos(Set<OrderToLeAttributeValueDto> orderToLeAttributeValueDtos) {
		this.orderToLeAttributeValueDtos = orderToLeAttributeValueDtos;
	}

	@Override
	public String toString() {
		return "OrderToLeDto [id=" + id + ", orderId=" + orderId + ", orderToLeAttributeValueDtos=" + orderToLeAttributeValueDtos + ", orderIllsitesDto="
				+ orderIllsitesDto + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((orderId == null) ? 0 : orderId.hashCode());
		result = prime * result + ((orderIllsitesDto == null) ? 0 : orderIllsitesDto.hashCode());
		result = prime * result + ((orderToLeAttributeValueDtos == null) ? 0 : orderToLeAttributeValueDtos.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderToLeDto other = (OrderToLeDto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (orderId == null) {
			if (other.orderId != null)
				return false;
		} else if (!orderId.equals(other.orderId))
			return false;
		if (orderIllsitesDto == null) {
			if (other.orderIllsitesDto != null)
				return false;
		} else if (!orderIllsitesDto.equals(other.orderIllsitesDto))
			return false;
		if (orderToLeAttributeValueDtos == null) {
			if (other.orderToLeAttributeValueDtos != null)
				return false;
		} else if (!orderToLeAttributeValueDtos.equals(other.orderToLeAttributeValueDtos))
			return false;
		return true;
	}

	public Set<OrderIllsitesDto> getOrderIllsitesDto() {
		return orderIllsitesDto;
	}

	public void setOrderIllsitesDto(Set<OrderIllsitesDto> orderIllsitesDto) {
		this.orderIllsitesDto = orderIllsitesDto;
	}

}
