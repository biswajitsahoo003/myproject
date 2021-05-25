package com.tcl.dias.servicefulfillmentutils.beans;

public class ExperienceSurveyBean extends TaskDetailsBaseBean
    {
        private String serviceDeliveryExperience;
        private String ownershipCommitment;
        private String portalUserFriendly;
        private String proactiveAndTransparent;
        private String improvementOnEnhancement;

        public String getServiceDeliveryExperience() {
            return serviceDeliveryExperience;
        }

        public void setServiceDeliveryExperience(String serviceDeliveryExperience) {
            this.serviceDeliveryExperience = serviceDeliveryExperience;
        }

        public String getOwnershipCommitment() {
            return ownershipCommitment;
        }

        public void setOwnershipCommitment(String ownershipCommitment) {
            this.ownershipCommitment = ownershipCommitment;
        }

        public String getPortalUserFriendly() {
            return portalUserFriendly;
        }

        public void setPortalUserFriendly(String portalUserFriendly) {
            this.portalUserFriendly = portalUserFriendly;
        }

        public String getProactiveAndTransparent() {
            return proactiveAndTransparent;
        }

        public void setProactiveAndTransparent(String proactiveandTransparent) {
            this.proactiveAndTransparent = proactiveandTransparent;
        }

        public String getImprovementOnEnhancement() {
            return improvementOnEnhancement;
        }

        public void setImprovementOnEnhancement(String improvementOnEnhancement) {
            this.improvementOnEnhancement = improvementOnEnhancement;
        }

        @Override
        public String toString() {
            return "ExperienceSurveyBean{" +
                    "serviceDeliveryExperience='" + serviceDeliveryExperience + '\'' +
                    ", ownershipCommitment='" + ownershipCommitment + '\'' +
                    ", portalUserFriendly='" + portalUserFriendly + '\'' +
                    ", proactiveandTransparent='" + proactiveAndTransparent + '\'' +
                    ", improvementOnEnhancement='" + improvementOnEnhancement + '\'' +
                    '}';
        }
    }
