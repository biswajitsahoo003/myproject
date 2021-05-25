package com.tcl.dias.sfdc.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * This file contains the SfdcPartnerEntityBean.java class.
 * used to pass the actual partner entity property value to SFDC
 *
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"Name", "Alias_based_login_required__c", "Approved_Credit_Limit__c", "Any_renewal__c",
        "Average_price_premium__c", "Blacklisted__c", "Improved_service_variant__c", "Blacklisting_Date__c", "Blacklisting_Remarks__c",
        "Blacklisting_Removal_Date__c", "Blacklisting_Removal_Remarks__c", "Blacklisting_Removed_Till_Date__c", "Bldg_Name__c", "CHM_Default_Value__c",
        "CIN_Number__c", "Credit_Check_Passed__c", "Credit_Check_Remarks__c", "Credit_Rating__c", "Dunning_Score__c",
        "Customer_s_Current_Exposure__c", "Industry_Sub_Type__c", "Date_Last_reviewed__c", "Deposit_Amount__c",
        "Document_Type__c", "Flat_Bldg_Number__c","GT_C_MSA_documents_signed_by_customer1__c",
        "Industry__c", "Int_Id__c", "Any_Senior_Secutive__c", "Competition_willing_to_invest__c", "Landmark__c",
        "License__c", "License_No__c", "License_Type__c", "Location__c", "New_Business_Score__c",
        "ServicesProducts__c", "One_Time_Undertaking_from_customer__c", "IT_share__c", "Owner_Inactive__c", "PO_Required__c",
        "Primary_SAP_CODE__c", "Primary_SECS_CODE__c", "Up_for_renewal__c", "Temp_CSAT_Score__c",
        "Temp_CSAT_Score_Type__c", "Temp_Transactional_score__c","Registered_Address_Country__c",
        "Registered_Address_State_Province__c", "Registered_Address_Street__c", "Registered_Address_Zip_Postal_Code__c", "Registration_Number__c", "Renewal_Score__c",
        "RenewalScoreSR__c", "REQLast_updated_on__c", "RRM_Category__c", "SAP_Code_Generated__c",
        "SAP_Code_Usage__c", "Segment__c","Lost_to_Competition__c",
        "Sub_Industry__c", "Surety_Required__c", "Termination_Score__c", "Vetting_Status__c",
        "Website_address__c", "X2nd_SAP_CODE__c","X2nd_SECS_CODE__c","X3rd_SAP_CODE__c", "X3rd_SECS_CODE__c"})
public class SfdcPartnerEntityBean extends BaseBean{

    @JsonProperty("Name")
    private String name;
    @JsonProperty("Alias_based_login_required__c")
    private String  aliasbasedloginrequired;
    @JsonProperty("Approved_Credit_Limit__c")
    private String  approvedCreditLimit;
    @JsonProperty("Any_renewal__c")
    private String anyrenewal;
    @JsonProperty("Average_price_premium__c")
    private String averagepricepremium;
    @JsonProperty("Blacklisted__c")
    private String blacklisted;
    @JsonProperty("Improved_service_variant__c")
    private String improvedservicevariant;
    @JsonProperty("Blacklisting_Date__c")
    private String blacklistingDate;
    @JsonProperty("Blacklisting_Remarks__c")
    private String blacklistingRemarks;
    @JsonProperty("Blacklisting_Removal_Date__c")
    private String blacklistingRemovalDate;
    @JsonProperty("Blacklisting_Removal_Remarks__c")
    private String blacklistingRemovalRemarks;
    @JsonProperty("Blacklisting_Removed_Till_Date__c")
    private String blacklistingRemovedTillDate;
    @JsonProperty("Bldg_Name__c")
    private String bldgName;
    @JsonProperty("CHM_Default_Value__c")
    private String cHMDefaultValue;
    @JsonProperty("CIN_Number__c")
    private String cINNumber;
    @JsonProperty("Credit_Check_Passed__c")
    private String creditCheckPassed;
    @JsonProperty("Credit_Check_Remarks__c")
    private String creditCheckRemarks;
    @JsonProperty("Credit_Rating__c")
    private String creditRating;
    @JsonProperty("Dunning_Score__c")
    private String dunningScore;
    @JsonProperty("Customer_s_Current_Exposure__c")
    private String customersCurrentExposure;
    @JsonProperty("Industry_Sub_Type__c")
    private String  industrySubType;
    @JsonProperty("Date_Last_reviewed__c")
    private String dateLastreviewed;
    @JsonProperty("Deposit_Amount__c")
    private String depositAmount;
    @JsonProperty("Document_Type__c")
    private String documentType;
    @JsonProperty("Flat_Bldg_Number__c")
    private String  flatBldgNumber;
    @JsonProperty("GT_C_MSA_documents_signed_by_customer1__c")
    private String gTCMSAdocumentssignedbycustomer1;
    @JsonProperty("Industry__c")
    private String  industry;
    @JsonProperty("Int_Id__c")
    private String intId;
    @JsonProperty("Any_Senior_Secutive__c")
    private String anySeniorSecutive;
    @JsonProperty("Competition_willing_to_invest__c")
    private String competitionwillingtoinvest;
    @JsonProperty("Landmark__c")
    private String landmark;
    @JsonProperty("License__c")
    private String license;
    @JsonProperty("License_No__c")
    private String licenseNo;
    @JsonProperty("License_Type__c")
    private String licenseType;
    @JsonProperty("Location__c")
    private String  location;
    @JsonProperty("New_Business_Score__c")
    private String newBusinessScore;
    @JsonProperty("ServicesProducts__c")
    private String servicesProducts;
    @JsonProperty("One_Time_Undertaking_from_customer__c")
    private String oneTimeUndertakingfromcustomer;
    @JsonProperty("IT_share__c")
    private String  ITshare;
    @JsonProperty("Owner_Inactive__c")
    private String ownerInactive;
    @JsonProperty("PO_Required__c")
    private String pORequired;
    @JsonProperty("Primary_SAP_CODE__c")
    private String  primarySAPCODE;
    @JsonProperty("Primary_SECS_CODE__c")
    private String primarySECSCODE;
    @JsonProperty("Up_for_renewal__c")
    private String  upforrenewal;
    @JsonProperty("Temp_CSAT_Score__c")
    private String tempCSATScore;
    @JsonProperty("Temp_CSAT_Score_Type__c")
    private String  tempCSATScoreType;
    @JsonProperty("Temp_Transactional_score__c")
    private String tempTransactionalscore;
    @JsonProperty("Registered_Address_Country__c")
    private String  registeredAddressCountry;
    @JsonProperty("Registered_Address_State_Province__c")
    private String registeredAddressStateProvince;
    @JsonProperty("Registered_Address_Street__c")
    private String registeredAddressStreet;
    @JsonProperty("Registered_Address_Zip_Postal_Code__c")
    private String registeredAddressZipPostalCode;
    @JsonProperty("Registration_Number__c")
    private String  registrationNumber;
    @JsonProperty("Renewal_Score__c")
    private String renewalScore;
    @JsonProperty("RenewalScoreSR__c")
    private String renewalScoreSR;
    @JsonProperty("REQLast_updated_on__c")
    private String rEQLastupdatedon;
    @JsonProperty("RRM_Category__c")
    private String rRMCategory;
    @JsonProperty("SAP_Code_Generated__c")
    private String sAPCodeGenerated;
    @JsonProperty("SAP_Code_Usage__c")
    private String sAPCodeUsage;
    @JsonProperty("Segment__c")
    private String  segment;
    @JsonProperty("Lost_to_Competition__c")
    private String  losttoCompetition;
    @JsonProperty("Sub_Industry__c")
    private String subIndustry;
    @JsonProperty("Surety_Required__c")
    private String  suretyRequired;
    @JsonProperty("Termination_Score__c")
    private String terminationScore;
    @JsonProperty("Vetting_Status__c")
    private String  vettingStatus;
    @JsonProperty("Website_address__c")
    private String  websiteaddress;
    @JsonProperty("X2nd_SAP_CODE__c")
    private String  x2ndSAPCODE;
    @JsonProperty("X2nd_SECS_CODE__c")
    private String  x2ndSECSCODE;
    @JsonProperty("X3rd_SAP_CODE__c")
    private String x3rdSAPCODE;
    @JsonProperty("X3rd_SECS_CODE__c")
    private String x3rdSECSCODE;
    @JsonProperty("Contact_person__c")
    private String contactPerson;
    @JsonProperty("Type_Of_Business__c")
    private String typeOfBusiness;
    /**This property is specific property to by pass
    * customerContactwhich is manadatory field while create new Entity*/
    @JsonProperty("Migration_Source_System__c")
    private String migrationSourceSystem;
    //GP-418 VAT Number for End Customer Creation
    @JsonProperty("is_Partner_account__c")
    private String isPartnerAccount;

    @JsonProperty("Migration_Source_System__c")
    public String getMigrationSourceSystem() {
        return migrationSourceSystem;
    }
    @JsonProperty("Migration_Source_System__c")
    public void setMigrationSourceSystem(String migrationSourceSystem) {
        this.migrationSourceSystem = migrationSourceSystem;
    }
    @JsonProperty("Type_Of_Business__c")
    public String getTypeOfBusiness() {
        return typeOfBusiness;
    }
    @JsonProperty("Type_Of_Business__c")
    public void setTypeOfBusiness(String typeOfBusiness) {
        this.typeOfBusiness = typeOfBusiness;
    }
    @JsonProperty("Contact_person__c")
    public String getContactPerson() {
        return contactPerson;
    }
    @JsonProperty("Contact_person__c")
    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }
    @JsonProperty("Name")
    public String getName() {
        return name;
    }
    @JsonProperty("Name")
    public void setName(String name) {
        this.name = name;
    }
    @JsonProperty("Alias_based_login_required__c")
    public String getAliasbasedloginrequired() {
        return aliasbasedloginrequired;
    }
    @JsonProperty("Alias_based_login_required__c")
    public void setAliasbasedloginrequired(String aliasbasedloginrequired) {
        this.aliasbasedloginrequired = aliasbasedloginrequired;
    }
    @JsonProperty("Approved_Credit_Limit__c")
    public String getApprovedCreditLimit() {
        return approvedCreditLimit;
    }
    @JsonProperty("Approved_Credit_Limit__c")
    public void setApprovedCreditLimit(String approvedCreditLimit) {
        this.approvedCreditLimit = approvedCreditLimit;
    }
    @JsonProperty("Any_renewal__c")
    public String getAnyrenewal() {
        return anyrenewal;
    }
    @JsonProperty("Any_renewal__c")
    public void setAnyrenewal(String anyrenewal) {
        this.anyrenewal = anyrenewal;
    }
    @JsonProperty("Average_price_premium__c")
    public String getAveragepricepremium() {
        return averagepricepremium;
    }
    @JsonProperty("Average_price_premium__c")
    public void setAveragepricepremium(String averagepricepremium) {
        this.averagepricepremium = averagepricepremium;
    }
    @JsonProperty("Blacklisted__c")
    public String getBlacklisted() {
        return blacklisted;
    }
    @JsonProperty("Blacklisted__c")
    public void setBlacklisted(String blacklisted) {
        this.blacklisted = blacklisted;
    }
    @JsonProperty("Improved_service_variant__c")
    public String getImprovedservicevariant() {
        return improvedservicevariant;
    }
    @JsonProperty("Improved_service_variant__c")
    public void setImprovedservicevariant(String improvedservicevariant) {
        this.improvedservicevariant = improvedservicevariant;
    }
    @JsonProperty("Blacklisting_Date__c")
    public String getBlacklistingDate() {
        return blacklistingDate;
    }
    @JsonProperty("Blacklisting_Date__c")
    public void setBlacklistingDate(String blacklistingDate) {
        this.blacklistingDate = blacklistingDate;
    }
    @JsonProperty("Blacklisting_Remarks__c")
    public String getBlacklistingRemarks() {
        return blacklistingRemarks;
    }
    @JsonProperty("Blacklisting_Remarks__c")
    public void setBlacklistingRemarks(String blacklistingRemarks) {
        this.blacklistingRemarks = blacklistingRemarks;
    }
    @JsonProperty("Blacklisting_Removal_Date__c")
    public String getBlacklistingRemovalDate() {
        return blacklistingRemovalDate;
    }
    @JsonProperty("Blacklisting_Removal_Date__c")
    public void setBlacklistingRemovalDate(String blacklistingRemovalDate) {
        this.blacklistingRemovalDate = blacklistingRemovalDate;
    }
    @JsonProperty("Blacklisting_Removal_Remarks__c")
    public String getBlacklistingRemovalRemarks() {
        return blacklistingRemovalRemarks;
    }
    @JsonProperty("Blacklisting_Removal_Remarks__c")
    public void setBlacklistingRemovalRemarks(String blacklistingRemovalRemarks) {
        this.blacklistingRemovalRemarks = blacklistingRemovalRemarks;
    }
    @JsonProperty("Blacklisting_Removed_Till_Date__c")
    public String getBlacklistingRemovedTillDate() {
        return blacklistingRemovedTillDate;
    }
    @JsonProperty("Blacklisting_Removed_Till_Date__c")
    public void setBlacklistingRemovedTillDate(String blacklistingRemovedTillDate) {
        this.blacklistingRemovedTillDate = blacklistingRemovedTillDate;
    }
    @JsonProperty("Bldg_Name__c")
    public String getBldgName() {
        return bldgName;
    }
    @JsonProperty("Bldg_Name__c")
    public void setBldgName(String bldgName) {
        this.bldgName = bldgName;
    }
    @JsonProperty("CHM_Default_Value__c")
    public String getCHMDefaultValue() {
        return cHMDefaultValue;
    }
    @JsonProperty("CHM_Default_Value__c")
    public void setCHMDefaultValue(String cHMDefaultValue) {
        this.cHMDefaultValue = cHMDefaultValue;
    }
    @JsonProperty("CIN_Number__c")
    public String getCINNumber() {
        return cINNumber;
    }
    @JsonProperty("CIN_Number__c")
    public void setCINNumber(String cINNumber) {
        this.cINNumber = cINNumber;
    }
    @JsonProperty("Credit_Check_Passed__c")
    public String getCreditCheckPassed() {
        return creditCheckPassed;
    }
    @JsonProperty("Credit_Check_Passed__c")
    public void setCreditCheckPassed(String creditCheckPassed) {
        this.creditCheckPassed = creditCheckPassed;
    }
    @JsonProperty("Credit_Check_Remarks__c")
    public String getCreditCheckRemarks() {
        return creditCheckRemarks;
    }
    @JsonProperty("Credit_Check_Remarks__c")
    public void setCreditCheckRemarks(String creditCheckRemarks) {
        this.creditCheckRemarks = creditCheckRemarks;
    }
    @JsonProperty("Credit_Rating__c")
    public String getCreditRating() {
        return creditRating;
    }
    @JsonProperty("Credit_Rating__c")
    public void setCreditRating(String creditRating) {
        this.creditRating = creditRating;
    }
    @JsonProperty("Dunning_Score__c")
    public String getDunningScore() {
        return dunningScore;
    }
    @JsonProperty("Dunning_Score__c")
    public void setDunningScore(String dunningScore) {
        this.dunningScore = dunningScore;
    }
    @JsonProperty("Customer_s_Current_Exposure__c")
    public String getCustomersCurrentExposure() {
        return customersCurrentExposure;
    }
    @JsonProperty("Customer_s_Current_Exposure__c")
    public void setCustomersCurrentExposure(String customersCurrentExposure) {
        this.customersCurrentExposure = customersCurrentExposure;
    }
    @JsonProperty("Industry_Sub_Type__c")
    public String getIndustrySubType() {
        return industrySubType;
    }
    @JsonProperty("Industry_Sub_Type__c")
    public void setIndustrySubType(String industrySubType) {
        this.industrySubType = industrySubType;
    }
    @JsonProperty("Date_Last_reviewed__c")
    public String getDateLastreviewed() {
        return dateLastreviewed;
    }
    @JsonProperty("Date_Last_reviewed__c")
    public void setDateLastreviewed(String dateLastreviewed) {
        this.dateLastreviewed = dateLastreviewed;
    }
    @JsonProperty("Deposit_Amount__c")
    public String getDepositAmount() {
        return depositAmount;
    }
    @JsonProperty("Deposit_Amount__c")
    public void setDepositAmount(String depositAmount) {
        this.depositAmount = depositAmount;
    }
    @JsonProperty("Document_Type__c")
    public String getDocumentType() {
        return documentType;
    }
    @JsonProperty("Document_Type__c")
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }
    @JsonProperty("Flat_Bldg_Number__c")
    public String getFlatBldgNumber() {
        return flatBldgNumber;
    }
    @JsonProperty("Flat_Bldg_Number__c")
    public void setFlatBldgNumber(String flatBldgNumber) {
        this.flatBldgNumber = flatBldgNumber;
    }
    @JsonProperty("GT_C_MSA_documents_signed_by_customer1__c")
    public String getGTCMSAdocumentssignedbycustomer1() {
        return gTCMSAdocumentssignedbycustomer1;
    }
    @JsonProperty("GT_C_MSA_documents_signed_by_customer1__c")
    public void setGTCMSAdocumentssignedbycustomer1(String gTCMSAdocumentssignedbycustomer1) {
        this.gTCMSAdocumentssignedbycustomer1 = gTCMSAdocumentssignedbycustomer1;
    }
    @JsonProperty("Industry__c")
    public String getIndustry() {
        return industry;
    }
    @JsonProperty("Industry__c")
    public void setIndustry(String industry) {
        this.industry = industry;
    }
    @JsonProperty("Int_Id__c")
    public String getIntId() {
        return intId;
    }
    @JsonProperty("Int_Id__c")
    public void setIntId(String intId) {
        this.intId = intId;
    }
    @JsonProperty("Any_Senior_Secutive__c")
    public String getAnySeniorSecutive() {
        return anySeniorSecutive;
    }
    @JsonProperty("Any_Senior_Secutive__c")
    public void setAnySeniorSecutive(String anySeniorSecutive) {
        this.anySeniorSecutive = anySeniorSecutive;
    }
    @JsonProperty("Competition_willing_to_invest__c")
    public String getCompetitionwillingtoinvest() {
        return competitionwillingtoinvest;
    }
    @JsonProperty("Competition_willing_to_invest__c")
    public void setCompetitionwillingtoinvest(String competitionwillingtoinvest) {
        this.competitionwillingtoinvest = competitionwillingtoinvest;
    }
    @JsonProperty("Landmark__c")
    public String getLandmark() {
        return landmark;
    }
    @JsonProperty("Landmark__c")
    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }
    @JsonProperty("License__c")
    public String getLicense() {
        return license;
    }
    @JsonProperty("License__c")
    public void setLicense(String license) {
        this.license = license;
    }
    @JsonProperty("License_No__c")
    public String getLicenseNo() {
        return licenseNo;
    }
    @JsonProperty("License_No__c")
    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }
    @JsonProperty("License_Type__c")
    public String getLicenseType() {
        return licenseType;
    }
    @JsonProperty("License_Type__c")
    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }
    @JsonProperty("Location__c")
    public String getLocation() {
        return location;
    }
    @JsonProperty("Location__c")
    public void setLocation(String location) {
        this.location = location;
    }
    @JsonProperty("New_Business_Score__c")
    public String getNewBusinessScore() {
        return newBusinessScore;
    }
    @JsonProperty("New_Business_Score__c")
    public void setNewBusinessScore(String newBusinessScore) {
        this.newBusinessScore = newBusinessScore;
    }
    @JsonProperty("ServicesProducts__c")
    public String getServicesProducts() {
        return servicesProducts;
    }
    @JsonProperty("ServicesProducts__c")
    public void setServicesProducts(String servicesProducts) {
        this.servicesProducts = servicesProducts;
    }
    @JsonProperty("One_Time_Undertaking_from_customer__c")
    public String getOneTimeUndertakingfromcustomer() {
        return oneTimeUndertakingfromcustomer;
    }
    @JsonProperty("One_Time_Undertaking_from_customer__c")
    public void setOneTimeUndertakingfromcustomer(String oneTimeUndertakingfromcustomer) {
        this.oneTimeUndertakingfromcustomer = oneTimeUndertakingfromcustomer;
    }
    @JsonProperty("IT_share__c")
    public String getITshare() {
        return ITshare;
    }
    @JsonProperty("IT_share__c")
    public void setITshare(String ITshare) {
        this.ITshare = ITshare;
    }
    @JsonProperty("Owner_Inactive__c")
    public String getOwnerInactive() {
        return ownerInactive;
    }
    @JsonProperty("Owner_Inactive__c")
    public void setOwnerInactive(String ownerInactive) {
        this.ownerInactive = ownerInactive;
    }
    @JsonProperty("PO_Required__c")
    public String getPORequired() {
        return pORequired;
    }
    @JsonProperty("PO_Required__c")
    public void setPORequired(String pORequired) {
        this.pORequired = pORequired;
    }
    @JsonProperty("Primary_SAP_CODE__c")
    public String getPrimarySAPCODE() {
        return primarySAPCODE;
    }
    @JsonProperty("Primary_SAP_CODE__c")
    public void setPrimarySAPCODE(String primarySAPCODE) {
        this.primarySAPCODE = primarySAPCODE;
    }
    @JsonProperty("Primary_SECS_CODE__c")
    public String getPrimarySECSCODE() {
        return primarySECSCODE;
    }
    @JsonProperty("Primary_SECS_CODE__c")
    public void setPrimarySECSCODE(String primarySECSCODE) {
        this.primarySECSCODE = primarySECSCODE;
    }
    @JsonProperty("Up_for_renewal__c")
    public String getUpforrenewal() {
        return upforrenewal;
    }
    @JsonProperty("Up_for_renewal__c")
    public void setUpforrenewal(String upforrenewal) {
        this.upforrenewal = upforrenewal;
    }
    @JsonProperty("Temp_CSAT_Score__c")
    public String getTempCSATScore() {
        return tempCSATScore;
    }
    @JsonProperty("Temp_CSAT_Score__c")
    public void setTempCSATScore(String tempCSATScore) {
        this.tempCSATScore = tempCSATScore;
    }
    @JsonProperty("Temp_CSAT_Score_Type__c")
    public String getTempCSATScoreType() {
        return tempCSATScoreType;
    }
    @JsonProperty("Temp_CSAT_Score_Type__c")
    public void setTempCSATScoreType(String tempCSATScoreType) {
        this.tempCSATScoreType = tempCSATScoreType;
    }
    @JsonProperty("Temp_Transactional_score__c")
    public String getTempTransactionalscore() {
        return tempTransactionalscore;
    }
    @JsonProperty("Temp_Transactional_score__c")
    public void setTempTransactionalscore(String tempTransactionalscore) {
        this.tempTransactionalscore = tempTransactionalscore;
    }
    @JsonProperty("Registered_Address_Country__c")
    public String getRegisteredAddressCountry() {
        return registeredAddressCountry;
    }
    @JsonProperty("Registered_Address_Country__c")
    public void setRegisteredAddressCountry(String registeredAddressCountry) {
        this.registeredAddressCountry = registeredAddressCountry;
    }
    @JsonProperty("Registered_Address_State_Province__c")
    public String getRegisteredAddressStateProvince() {
        return registeredAddressStateProvince;
    }
    @JsonProperty("Registered_Address_State_Province__c")
    public void setRegisteredAddressStateProvince(String registeredAddressStateProvince) {
        this.registeredAddressStateProvince = registeredAddressStateProvince;
    }
    @JsonProperty("Registered_Address_Street__c")
    public String getRegisteredAddressStreet() {
        return registeredAddressStreet;
    }
    @JsonProperty("Registered_Address_Street__c")
    public void setRegisteredAddressStreet(String registeredAddressStreet) {
        this.registeredAddressStreet = registeredAddressStreet;
    }
    @JsonProperty("Registered_Address_Zip_Postal_Code__c")
    public String getRegisteredAddressZipPostalCode() {
        return registeredAddressZipPostalCode;
    }
    @JsonProperty("Registered_Address_Zip_Postal_Code__c")
    public void setRegisteredAddressZipPostalCode(String registeredAddressZipPostalCode) {
        this.registeredAddressZipPostalCode = registeredAddressZipPostalCode;
    }
    @JsonProperty("Registration_Number__c")
    public String getRegistrationNumber() {
        return registrationNumber;
    }
    @JsonProperty("Registration_Number__c")
    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }
    @JsonProperty("Renewal_Score__c")
    public String getRenewalScore() {
        return renewalScore;
    }
    @JsonProperty("Renewal_Score__c")
    public void setRenewalScore(String renewalScore) {
        this.renewalScore = renewalScore;
    }
    @JsonProperty("RenewalScoreSR__c")
    public String getRenewalScoreSR() {
        return renewalScoreSR;
    }
    @JsonProperty("RenewalScoreSR__c")
    public void setRenewalScoreSR(String renewalScoreSR) {
        this.renewalScoreSR = renewalScoreSR;
    }
    @JsonProperty("REQLast_updated_on__c")
    public String getREQLastupdatedon() {
        return rEQLastupdatedon;
    }
    @JsonProperty("REQLast_updated_on__c")
    public void setREQLastupdatedon(String rEQLastupdatedon) {
        this.rEQLastupdatedon = rEQLastupdatedon;
    }
    @JsonProperty("RRM_Category__c")
    public String getrRMCategory() {
        return rRMCategory;
    }
    @JsonProperty("RRM_Category__c")
    public void setrRMCategory(String rRMCategory) {
        this.rRMCategory = rRMCategory;
    }
    @JsonProperty("SAP_Code_Generated__c")
    public String getSAPCodeGenerated() {
        return sAPCodeGenerated;
    }
    @JsonProperty("SAP_Code_Generated__c")
    public void setSAPCodeGenerated(String sAPCodeGenerated) {
        this.sAPCodeGenerated = sAPCodeGenerated;
    }
    @JsonProperty("SAP_Code_Usage__c")
    public String getSAPCodeUsage() {
        return sAPCodeUsage;
    }
    @JsonProperty("SAP_Code_Usage__c")
    public void setSAPCodeUsage(String sAPCodeUsage) {
        this.sAPCodeUsage = sAPCodeUsage;
    }
    @JsonProperty("Segment__c")
    public String getSegment() {
        return segment;
    }
    @JsonProperty("Segment__c")
    public void setSegment(String segment) {
        this.segment = segment;
    }
    @JsonProperty("Lost_to_Competition__c")
    public String getLosttoCompetition() {
        return losttoCompetition;
    }
    @JsonProperty("Lost_to_Competition__c")
    public void setLosttoCompetition(String losttoCompetition) {
        this.losttoCompetition = losttoCompetition;
    }
    @JsonProperty("Sub_Industry__c")
    public String getSubIndustry() {
        return subIndustry;
    }
    @JsonProperty("Sub_Industry__c")
    public void setSubIndustry(String subIndustry) {
        this.subIndustry = subIndustry;
    }
    @JsonProperty("Surety_Required__c")
    public String getSuretyRequired() {
        return suretyRequired;
    }
    @JsonProperty("Surety_Required__c")
    public void setSuretyRequired(String suretyRequired) {
        this.suretyRequired = suretyRequired;
    }
    @JsonProperty("Termination_Score__c")
    public String getTerminationScore() {
        return terminationScore;
    }
    @JsonProperty("Termination_Score__c")
    public void setTerminationScore(String terminationScore) {
        this.terminationScore = terminationScore;
    }
    @JsonProperty("Vetting_Status__c")
    public String getVettingStatus() {
        return vettingStatus;
    }
    @JsonProperty("Vetting_Status__c")
    public void setVettingStatus(String vettingStatus) {
        this.vettingStatus = vettingStatus;
    }
    @JsonProperty("Website_address__c")
    public String getWebsiteaddress() {
        return websiteaddress;
    }
    @JsonProperty("Website_address__c")
    public void setWebsiteaddress(String websiteaddress) {
        this.websiteaddress = websiteaddress;
    }
    @JsonProperty("X2nd_SAP_CODE__c")
    public String getX2ndSAPCODE() {
        return x2ndSAPCODE;
    }
    @JsonProperty("X2nd_SAP_CODE__c")
    public void setX2ndSAPCODE(String x2ndSAPCODE) {
        this.x2ndSAPCODE = x2ndSAPCODE;
    }
    @JsonProperty("X2nd_SECS_CODE__c")
    public String getX2ndSECSCODE() {
        return x2ndSECSCODE;
    }
    @JsonProperty("X2nd_SECS_CODE__c")
    public void setX2ndSECSCODE(String x2ndSECSCODE) {
        this.x2ndSECSCODE = x2ndSECSCODE;
    }
    @JsonProperty("X3rd_SAP_CODE__c")
    public String getX3rdSAPCODE() {
        return x3rdSAPCODE;
    }
    @JsonProperty("X3rd_SAP_CODE__c")
    public void setX3rdSAPCODE(String x3rdSAPCODE) {
        this.x3rdSAPCODE = x3rdSAPCODE;
    }
    @JsonProperty("X3rd_SECS_CODE__c")
    public String getX3rdSECSCODE() {
        return x3rdSECSCODE;
    }
    @JsonProperty("X3rd_SECS_CODE__c")
    public void setX3rdSECSCODE(String x3rdSECSCODE) {
        this.x3rdSECSCODE = x3rdSECSCODE;
    }
    @JsonProperty("is_Partner_account__c")
    public String getIsPartnerAccount() {
        return isPartnerAccount;
    }
    @JsonProperty("is_Partner_account__c")
    public void setIsPartnerAccount(String isPartnerAccount) {
        this.isPartnerAccount = isPartnerAccount;
    }
}
