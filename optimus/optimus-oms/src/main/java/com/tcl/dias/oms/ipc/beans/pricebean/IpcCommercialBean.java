package com.tcl.dias.oms.ipc.beans.pricebean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "productComponents" })
public class IpcCommercialBean {

	@JsonProperty("productComponents")
	List<Component> productComponents;

	public List<Component> getProductComponents() {
		return productComponents;
	}

	public void setProductComponents(List<Component> productComponents) {
		this.productComponents = productComponents;
	}
}
