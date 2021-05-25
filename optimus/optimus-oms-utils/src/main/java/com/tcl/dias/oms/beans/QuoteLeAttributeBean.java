package com.tcl.dias.oms.beans;
/**
* This file contains the QuoteLeAttributeBean.java class.
* 
 *
* @author NAVEEN GUNASEKARAN
* @link http://www.tatacommunications.com/
* @copyright 2018 Tata Communications Limited
*/
public class QuoteLeAttributeBean {
       private Integer quoteLegalEntityId;
       private String isFeasibilityCheckDone;
       private String isPricingCheckDone;

       public String getIsFeasibilityCheckDone() {
              return isFeasibilityCheckDone;
       }

       public String getIsPricingCheckDone() {
              return isPricingCheckDone;
       }

       public void setIsFeasibilityCheckDone(String isFeasibilityCheckDone) {
              this.isFeasibilityCheckDone = isFeasibilityCheckDone;
       }

       public void setIsPricingCheckDone(String isPricingCheckDone) {
              this.isPricingCheckDone = isPricingCheckDone;
       }

       public Integer getQuoteLegalEntityId() {
              return quoteLegalEntityId;
       }

       public void setQuoteLegalEntityId(Integer quoteLegalEntityId) {
              this.quoteLegalEntityId = quoteLegalEntityId;
       }
}
