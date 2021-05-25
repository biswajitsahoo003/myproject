package com.tcl.dias.sfdc.component;
import com.tcl.dias.common.sfdc.bean.PartnerEntityRequestBean;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.sfdc.bean.*;
import com.tcl.dias.sfdc.constants.SfdcConstants;
import com.tcl.dias.sfdc.mapper.ISfdcMapper;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
/**
 *
 * This file contains the SfdcPartnerEntityMapper.java class.
 *
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component
public class SfdcPartnerEntityMapper implements ISfdcMapper {
    @Override
    public BaseBean transfortToSfdcRequest(String inputRequest) throws TclCommonException {
        SfdcPartnerEntityRequest sfdcPartnerEntityBean = null;

        if (StringUtils.isNotBlank(inputRequest)) {
            PartnerEntityRequestBean partnerEntityBean = (PartnerEntityRequestBean) Utils.convertJsonToObject(inputRequest,
                    PartnerEntityRequestBean.class);
            sfdcPartnerEntityBean = constructPartnerEntityRequestBean(partnerEntityBean);
        }

        return sfdcPartnerEntityBean;
    }

    private SfdcPartnerEntityRequest constructPartnerEntityRequestBean(PartnerEntityRequestBean partnerEntityRequestBean){
        SfdcPartnerEntityRequest sfdcPartnerEntityRequest=new SfdcPartnerEntityRequest();
        SfdcPartnerEntityAccountBean sfdcPartnerEntityAccountBean=new SfdcPartnerEntityAccountBean();
        SfdcPartnerEntityBean sfdcPartnerEntityBean=new SfdcPartnerEntityBean();

        sfdcPartnerEntityRequest.setSfdcPartnerEntityAccountBean(sfdcPartnerEntityAccountBean);
        sfdcPartnerEntityRequest.setReferenceId(partnerEntityRequestBean.getReferenceId());
        //setting Mandatory fields for entity creation
        sfdcPartnerEntityAccountBean.setSfdcAccountId(partnerEntityRequestBean.getSfdcAccountId());
        sfdcPartnerEntityAccountBean.setRegisteredAddressCity(partnerEntityRequestBean.getRegisteredAddressCity());
        sfdcPartnerEntityAccountBean.setLegalEntityOwnerName(partnerEntityRequestBean.getLegalEntityOwnerName());
        sfdcPartnerEntityAccountBean.sethQContact(partnerEntityRequestBean.getHQContact());
        sfdcPartnerEntityAccountBean.setServiceManagerName(partnerEntityRequestBean.getServiceManagerName());
        sfdcPartnerEntityAccountBean.setCustomerContact(partnerEntityRequestBean.getCustomerContact());
        sfdcPartnerEntityAccountBean.setSfdcPartnerEntityBean(sfdcPartnerEntityBean);
         /** Set the 'OPTIMUS' as value in migrationSourceSystem property to by pass the
          * customerContact Id for create new entity*/
        sfdcPartnerEntityBean.setMigrationSourceSystem(SfdcConstants.OPTIMUS.toString());
        //GP-418 VAT Number for End Customer Creation
        sfdcPartnerEntityBean.setIsPartnerAccount(SfdcConstants.PARTNER_END_CUSTOMER.toString());
        sfdcPartnerEntityBean.setName(partnerEntityRequestBean.getCustomerName());
        sfdcPartnerEntityBean.setIndustry(partnerEntityRequestBean.getIndustry());
        sfdcPartnerEntityBean.setIndustrySubType(partnerEntityRequestBean.getIndustrySubType());
        sfdcPartnerEntityBean.setWebsiteaddress(partnerEntityRequestBean.getCustomerWebsite());
        sfdcPartnerEntityBean.setRegistrationNumber(partnerEntityRequestBean.getRegistrationNumber());
        sfdcPartnerEntityBean.setPORequired(partnerEntityRequestBean.getpORequired());
        sfdcPartnerEntityBean.setRegisteredAddressCountry(partnerEntityRequestBean.getCountry());
        sfdcPartnerEntityBean.setRegisteredAddressZipPostalCode(partnerEntityRequestBean.getRegisteredAddressZipPostalCode());
        sfdcPartnerEntityBean.setRegisteredAddressStateProvince(partnerEntityRequestBean.getRegisteredAddressStateProvince());
        sfdcPartnerEntityBean.setRegisteredAddressStreet(partnerEntityRequestBean.getRegisteredAddressStreet());
        sfdcPartnerEntityBean.setContactPerson(partnerEntityRequestBean.getContactPerson());
        sfdcPartnerEntityBean.setSubIndustry(partnerEntityRequestBean.getSubIndustry());
        sfdcPartnerEntityBean.setTypeOfBusiness(partnerEntityRequestBean.getTypeOfBusiness());

        //setting other fields value for entity creation
        sfdcPartnerEntityBean.setAliasbasedloginrequired(partnerEntityRequestBean.getAliasbasedloginrequired());
        sfdcPartnerEntityBean.setAnyrenewal(partnerEntityRequestBean.getAnyrenewal());
        sfdcPartnerEntityBean.setAnySeniorSecutive(partnerEntityRequestBean.getAnySeniorSecutive());
        sfdcPartnerEntityBean.setApprovedCreditLimit(partnerEntityRequestBean.getApprovedCreditLimit());
        sfdcPartnerEntityBean.setAveragepricepremium(partnerEntityRequestBean.getAveragepricepremium());
        sfdcPartnerEntityBean.setAliasbasedloginrequired(partnerEntityRequestBean.getAliasbasedloginrequired());
        sfdcPartnerEntityBean.setBlacklisted(partnerEntityRequestBean.getBlacklisted());
        sfdcPartnerEntityBean.setBlacklistingDate(partnerEntityRequestBean.getBlacklistingDate());
        sfdcPartnerEntityBean.setBlacklistingRemarks(partnerEntityRequestBean.getBlacklistingRemovalRemarks());
        sfdcPartnerEntityBean.setBlacklistingRemovalDate(partnerEntityRequestBean.getBlacklistingRemovalDate());
        sfdcPartnerEntityBean.setBlacklistingRemovedTillDate(partnerEntityRequestBean.getBlacklistingRemovedTillDate());
        sfdcPartnerEntityBean.setBldgName(partnerEntityRequestBean.getBldgName());
        sfdcPartnerEntityBean.setBlacklistingRemovalRemarks(partnerEntityRequestBean.getBlacklistingRemovalRemarks());
        sfdcPartnerEntityBean.setCHMDefaultValue(partnerEntityRequestBean.getCHMDefaultValue());
        sfdcPartnerEntityBean.setCINNumber(partnerEntityRequestBean.getcINNumber());
        sfdcPartnerEntityBean.setCompetitionwillingtoinvest(partnerEntityRequestBean.getCompetitionwillingtoinvest());
        sfdcPartnerEntityBean.setCreditCheckPassed(partnerEntityRequestBean.getCreditCheckPassed());
        sfdcPartnerEntityBean.setCreditCheckRemarks(partnerEntityRequestBean.getCreditCheckRemarks());
        sfdcPartnerEntityBean.setCreditRating(partnerEntityRequestBean.getCreditRating());
        sfdcPartnerEntityBean.setCustomersCurrentExposure(partnerEntityRequestBean.getCustomersCurrentExposure());
        sfdcPartnerEntityBean.setGTCMSAdocumentssignedbycustomer1(partnerEntityRequestBean.getGTCMSAdocumentssignedbycustomer1());
        sfdcPartnerEntityBean.setDateLastreviewed(partnerEntityRequestBean.getDateLastreviewed());
        sfdcPartnerEntityBean.setDepositAmount(partnerEntityRequestBean.getDepositAmount());
        sfdcPartnerEntityBean.setDocumentType(partnerEntityRequestBean.getDocumentType());
        sfdcPartnerEntityBean.setDunningScore(partnerEntityRequestBean.getDunningScore());
        sfdcPartnerEntityBean.setFlatBldgNumber(partnerEntityRequestBean.getFlatBldgNumber());
        sfdcPartnerEntityBean.setImprovedservicevariant(partnerEntityRequestBean.getImprovedservicevariant());
        sfdcPartnerEntityBean.setIntId(partnerEntityRequestBean.getIntId());
        sfdcPartnerEntityBean.setITshare(partnerEntityRequestBean.getITshare());
        sfdcPartnerEntityBean.setLandmark(partnerEntityRequestBean.getLandmark());
        sfdcPartnerEntityBean.setLicense(partnerEntityRequestBean.getLicense());
        sfdcPartnerEntityBean.setLicenseNo(partnerEntityRequestBean.getLicenseNo());
        sfdcPartnerEntityBean.setLicenseType(partnerEntityRequestBean.getLicenseType());
        sfdcPartnerEntityBean.setLocation(partnerEntityRequestBean.getLocation());
        sfdcPartnerEntityBean.setLosttoCompetition(partnerEntityRequestBean.getLosttoCompetition());
        sfdcPartnerEntityBean.setREQLastupdatedon(partnerEntityRequestBean.getREQLastupdatedon());
        sfdcPartnerEntityBean.setNewBusinessScore(partnerEntityRequestBean.getNewBusinessScore());
        sfdcPartnerEntityBean.setOneTimeUndertakingfromcustomer(partnerEntityRequestBean.getOneTimeUndertakingfromcustomer());
        sfdcPartnerEntityBean.setOwnerInactive(partnerEntityRequestBean.getOwnerInactive());
        sfdcPartnerEntityBean.setPrimarySAPCODE(partnerEntityRequestBean.getPrimarySAPCODE());
        sfdcPartnerEntityBean.setPrimarySECSCODE(partnerEntityRequestBean.getPrimarySECSCODE());
        sfdcPartnerEntityBean.setrRMCategory(partnerEntityRequestBean.getRRMCategory());
        sfdcPartnerEntityBean.setRenewalScore(partnerEntityRequestBean.getRenewalScore());
        sfdcPartnerEntityBean.setRenewalScoreSR(partnerEntityRequestBean.getRenewalScoreSR());
        sfdcPartnerEntityBean.setSAPCodeGenerated(partnerEntityRequestBean.getSAPCodeGenerated());
        sfdcPartnerEntityBean.setSAPCodeUsage(partnerEntityRequestBean.getSAPCodeUsage());
        sfdcPartnerEntityBean.setSegment(partnerEntityRequestBean.getSegment());
        sfdcPartnerEntityBean.setServicesProducts(partnerEntityRequestBean.getServicesProducts());
        sfdcPartnerEntityBean.setSuretyRequired(partnerEntityRequestBean.getSuretyRequired());
        sfdcPartnerEntityBean.setTempCSATScore(partnerEntityRequestBean.getTempCSATScore());
        sfdcPartnerEntityBean.setTempCSATScoreType(partnerEntityRequestBean.getTempCSATScoreType());
        sfdcPartnerEntityBean.setTempTransactionalscore(partnerEntityRequestBean.getTempTransactionalscore());
        sfdcPartnerEntityBean.setUpforrenewal(partnerEntityRequestBean.getUpforrenewal());
        sfdcPartnerEntityBean.setVettingStatus(partnerEntityRequestBean.getVettingStatus());
        sfdcPartnerEntityBean.setX2ndSAPCODE(partnerEntityRequestBean.getX2ndSAPCODE());
        sfdcPartnerEntityBean.setX2ndSECSCODE(partnerEntityRequestBean.getX2ndSECSCODE());
        sfdcPartnerEntityBean.setX3rdSAPCODE(partnerEntityRequestBean.getX3rdSAPCODE());
        sfdcPartnerEntityBean.setX3rdSECSCODE(partnerEntityRequestBean.getX3rdSECSCODE());

       return  sfdcPartnerEntityRequest;
    }
}
