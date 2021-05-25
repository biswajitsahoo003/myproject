
package com.tcl.dias.servicefulfillmentutils.beans.gsc;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "country",
    "pincode",
    "addressLineTwo",
    "quantity",
    "city",
    "countryCode",
    "addressLineOne",
    "locality",
    "source",
    "state"
})
public class AddressDetail {

    @JsonProperty("country")
    private String country;
    @JsonProperty("pincode")
    private String pincode;
    @JsonProperty("addressLineTwo")
    private String addressLineTwo;
    @JsonProperty("quantity")
    private String quantity;
    @JsonProperty("city")
    private String city;
    @JsonProperty("countryCode")
    private String countryCode;
    @JsonProperty("addressLineOne")
    private String addressLineOne;
    @JsonProperty("locality")
    private String locality;
    @JsonProperty("source")
    private String source;
    @JsonProperty("state")
    private String state;

    @JsonProperty("country")
    public String getCountry() {
        return country;
    }

    @JsonProperty("state")
    public String getState() {
		return state;
	}

    @JsonProperty("state")
	public void setState(String state) {
		this.state = state;
	}

	@JsonProperty("country")
    public void setCountry(String country) {
        this.country = country;
    }

    @JsonProperty("pincode")
    public String getPincode() {
        return pincode;
    }

    @JsonProperty("pincode")
    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    @JsonProperty("addressLineTwo")
    public String getAddressLineTwo() {
        return addressLineTwo;
    }

    @JsonProperty("addressLineTwo")
    public void setAddressLineTwo(String addressLineTwo) {
        this.addressLineTwo = addressLineTwo;
    }

    @JsonProperty("quantity")
    public String getQuantity() {
        return quantity;
    }

    @JsonProperty("quantity")
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @JsonProperty("city")
    public String getCity() {
        return city;
    }

    @JsonProperty("city")
    public void setCity(String city) {
        this.city = city;
    }

    @JsonProperty("countryCode")
    public String getCountryCode() {
        return countryCode;
    }

    @JsonProperty("countryCode")
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @JsonProperty("addressLineOne")
    public String getAddressLineOne() {
        return addressLineOne;
    }

    @JsonProperty("addressLineOne")
    public void setAddressLineOne(String addressLineOne) {
        this.addressLineOne = addressLineOne;
    }

    @JsonProperty("locality")
    public String getLocality() {
        return locality;
    }

    @JsonProperty("locality")
    public void setLocality(String locality) {
        this.locality = locality;
    }

    @JsonProperty("source")
    public String getSource() {
        return source;
    }

    @JsonProperty("source")
    public void setSource(String source) {
        this.source = source;
    }
    
    @Override
	public String toString() {
		return "AddressDetail [country=" + country + ", pincode=" + pincode + ", addressLineTwo="
				+ addressLineTwo + ", quantity=" + quantity + ", city=" + city + ", countryCode=" + countryCode + ", addressLineOne="
				+ addressLineOne + ", locality=" + locality + ", source=" + source +  ", state=" + state + "]";
	}

}
