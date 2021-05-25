package com.tcl.dias.oms.partner.thirdpartysystem.dnb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DNB Partner Organization
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Organization {

    private String primaryName;
    private List<String> websiteAddress;

    public Integer getDuns() {
        return duns;
    }

    public void setDuns(Integer duns) {
        this.duns = duns;
    }

    private Integer duns;
    private List<RegistrationNumbers> registrationNumbers;
    private MailingAddress mailingAddress;
    private PrimaryAddress primaryAddress;

    public Organization() {
    }

    ;

    public PrimaryAddress getPrimaryAddress() {
        return primaryAddress;
    }

    public void setPrimaryAddress(PrimaryAddress primaryAddress) {
        this.primaryAddress = primaryAddress;
    }

    public List<RegistrationNumbers> getRegistrationNumbers() {
        return registrationNumbers;
    }

    public void setRegistrationNumbers(List<RegistrationNumbers> registrationNumbers) {
        this.registrationNumbers = registrationNumbers;
    }

    public MailingAddress getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(MailingAddress mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public List<String> getWebsiteAddress() {
        return websiteAddress;
    }

    public void setWebsiteAddress(List<String> websiteAddress) {
        this.websiteAddress = websiteAddress;
    }

    public String getPrimaryName() {
        return primaryName;
    }

    public void setPrimaryName(String primaryName) {
        this.primaryName = primaryName;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class PrimaryAddress {
        private AddressCountry addressCountry;
        private AddressLocality addressLocality;
        private StreetAddress streetAddress;

        public PrimaryAddress() {
        }

        ;

        public AddressCountry getAddressCountry() {
            return addressCountry;
        }

        public void setAddressCountry(AddressCountry addressCountry) {
            this.addressCountry = addressCountry;
        }

        public AddressLocality getAddressLocality() {
            return addressLocality;
        }

        public void setAddressLocality(AddressLocality addressLocality) {
            this.addressLocality = addressLocality;
        }

        public StreetAddress getStreetAddress() {
            return streetAddress;
        }

        public void setStreetAddress(StreetAddress streetAddress) {
            this.streetAddress = streetAddress;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public class AddressCountry {
            @JsonProperty("name")
            public String getCountryName() {
                return countryName;
            }

            @JsonProperty("name")
            public void setCountryName(String countryName) {
                this.countryName = countryName;
            }

            @JsonProperty("name")
            private String countryName;

            @JsonIgnore
            private Map<String, Object> additionalProperties = new HashMap<>();
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public class AddressLocality {
            @JsonProperty("name")
            public String getCityName() {
                return cityName;
            }

            @JsonProperty("name")
            public void setCityName(String cityName) {
                this.cityName = cityName;
            }

            @JsonProperty("name")
            private String cityName;

            @JsonIgnore
            private Map<String, Object> additionalProperties = new HashMap<>();
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public class StreetAddress {
            public String getLine1() {
                return line1;
            }

            public void setLine1(String line1) {
                this.line1 = line1;
            }

            private String line1;

            @JsonIgnore
            private Map<String, Object> additionalProperties = new HashMap<>();
        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class MailingAddress {
        public MailingAddress() {
        }

        ;

        public String getPostalCode() {
            return postalCode;
        }

        public void setPostalCode(String postalCode) {
            this.postalCode = postalCode;
        }

        private String postalCode;
    }

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }


}
