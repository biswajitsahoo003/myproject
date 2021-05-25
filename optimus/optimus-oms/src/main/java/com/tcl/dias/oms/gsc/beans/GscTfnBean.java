package com.tcl.dias.oms.gsc.beans;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.oms.entity.entities.OrderGscTfn;
import com.tcl.dias.oms.gsc.tiger.beans.NumberDetails;
import com.tcl.dias.oms.gsc.util.GscConstants;

/**
 * Bean class for Toll Free Numbers
 *
 * @author Ramasubramanian Sankar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(value = Include.NON_NULL)
public class GscTfnBean {
	private String number;
	private Integer id;
	private Byte ported;
	private String country;
	private String city;
	private Boolean isNumberSelected;
	private String npa;

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Byte getPorted() {
		return ported;
	}

	public void setPorted(Byte ported) {
		this.ported = ported;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getNpa() {
		return npa;
	}

	public void setNpa(String npa) {
		this.npa = npa;
	}

	public GscTfnBean() {
		// TODO Auto-generated constructor stub
	}

	public Boolean getNumberSelected() {
		return isNumberSelected;
	}

	public void setNumberSelected(Boolean numberSelected) {
		isNumberSelected = numberSelected;
	}

	public static GscTfnBean fromNumberDetails(NumberDetails numberDetails) {
		GscTfnBean bean = new GscTfnBean();
		bean.setNumber(numberDetails.getE164());
		bean.setPorted(GscConstants.STATUS_INACTIVE);
		bean.setCountry(numberDetails.getOriginCountry());
		bean.setCity(numberDetails.getOriginCity());
		return bean;
	}

	public static GscTfnBean fromOrderGscTfn(OrderGscTfn orderGscTfn) {
		GscTfnBean bean = new GscTfnBean();
		bean.setId(orderGscTfn.getId());
		bean.setNumber(orderGscTfn.getTfnNumber());
		bean.setPorted(orderGscTfn.getIsPorted());
		String countryStr = Optional.ofNullable(orderGscTfn.getCountryCode()).orElse("");
		String[] fragments = countryStr.split(":");
		if (!StringUtils.isEmpty(fragments[0])) {
			bean.setCountry(fragments[0]);
		}
		if (fragments.length > 1) {
			bean.setCity(fragments[1]);
		}
		return bean;
	}

	public OrderGscTfn toOrderGscTfn() {
		OrderGscTfn orderGscTfn = new OrderGscTfn();
		orderGscTfn.setTfnNumber(number);
		orderGscTfn.setIsPorted(ported);
		orderGscTfn.setId(id);
		return orderGscTfn;
	}

	public static GscTfnBean mapToGscTfnBean(GscTfnBean gscTfnBean) {
		GscTfnBean tfnBean = new GscTfnBean();
		tfnBean.setId(gscTfnBean.getId());
		tfnBean.setNumber(gscTfnBean.getNumber());
		tfnBean.setNpa(gscTfnBean.getNpa());
		if (gscTfnBean.getPorted().equals((byte) 0))
			tfnBean.setNumberSelected(true);
		return tfnBean;
	}

	@Override
	public String toString() {
		return "GscTfnBean{" +
				"number='" + number + '\'' +
				", id=" + id +
				", ported=" + ported +
				", country='" + country + '\'' +
				", city='" + city + '\'' +
				", isNumberSelected=" + isNumberSelected +
				", npa='" + npa + '\'' +
				'}';
	}
}
