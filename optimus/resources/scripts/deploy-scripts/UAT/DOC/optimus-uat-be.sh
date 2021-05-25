#set -X
## Export proxy
export http_proxy="http://10.133.12.181:80"
export https_proxy="http://10.133.12.181:80"

## clone & mvn build application
/docker/script/maven/apache-maven-3.5.2/bin/mvn -version
cd /docker/script/workspace/
sudo git clone -b Hydrogen http://root:Tata#123@10.133.208.133/gitlab/root/optimus.git
sudo chmod -R og+rwx optimus
cd optimus/
/docker/script/maven/apache-maven-3.5.2/bin/mvn clean install clean install

#Env variables
DB_UNAME=optimus
DB_PWD=OPTimus12\#\$
MQ_HOST=10.133.208.135
MQ_PORT=5672
MQ_UNAME=web
MQ_PWD=tata
AUTH_URL=https://optimus-uat.tatacommunications.com/auth/
REDIS_HOST=10.133.208.136
REDIS_PORT=6379
REDIS_UNAME=
REDIS_PWD=
LDAP_URL=ldap://10.133.208.116:389
LDAP_BASE=dc=TCL
LDAP_UNAME=cn=Manager,dc=TCL
LDAP_PWD=AzXXb5lqpw1YgiHK5t
KEYCLOAK_PROXY=
KEYCLOAK_UNAME=admin
KEYCLOAK_PWD=admin
## ML F&P ##
R_ONNET_WIRELINE=http://10.133.208.121/new/onnet-wireline/api
R_ONNET_WIRELESS=http://10.133.208.121/new/onnet-wireless/api
R_OFFNET_WIRELESS=http://10.133.208.121/new/offnet-wireless/api
R_NPL_ONNET_POP=http://10.133.208.121/new/npl/pop/api
R_PRICING=http://10.133.208.121/new/pricing/api
R_GVPN_PRICING=http://10.133.208.121/new/gvpn/pricing/api
R_GVPN_IND_PRICING=http://10.133.208.121/new/gvpn/ind-int/pricing/api
R_NPL_PRICING=http://10.133.208.120:2500/new/npl/pricing/api
## SFDC ##
SFDC_UNAME=huzefa.tarwala@tatacommunications.com.tcluat
SFDC_PWD=7ujm\&UJMKmmcdUX7JprK1kwkoyOm8LSXx
SFDC_GRANT_TYPE=password
SFDC_AUTH=Basic Og==
SFDC_CLIENT=3MVG9rKhT8ocoxGkPdSEUBFzU_VZC0HhknudUIGBqUgoTG6mlBwbhnihq74XgyvhW5.fEOubuJg1t6LVDW04X
SFDC_SECRET=4585614469580292015
SFDC_AUTH_URL=https://test.salesforce.com/services/oauth2/token
SFDC_GEN_HOST=https://cs80.salesforce.com/services/apexrest/
SFDC_OPT_CREATE_PATH=GSC_REST_V9_2/OpportunityService/
SFDC_PRD_CREATE_REQ_URL=GSC_REST_V9_2/Products_Services_Service/
SFDC_PRD_DELETE_URL=DeleteProductService
SFDC_STAGE_UPDATE_URL=GSC_REST_V9_2/OpportunityService/
SFDC_SITE_UPDATE_URL=CreateSiteLocation
## SMTP ##
SMTP_HOST=p44relay
SMTP_UNAME=
SMTP_PWD=
SMTP_PORT=25
## Internet Proxy ##
SYSTEM_PROXY_HOST=10.133.12.181
SYSTEM_PROXY_PORT=80
BUILD_VERSION=Hydrogen:v1.2
APP_HOST=http://optimus-uat.tatacommunications.com
APP_ENV=UAT
CUSTOMER_SUPPORT_EMAIL=idriveoptimus@gmail.com
#Web hook env
USER_NAME=optimus_docusign_admin
USER_PASSWORD=UIOSDnkmasdmasdTYIojmnlmn3457918230

## copy files to /opt/...
sudo cp /docker/script/workspace/optimus/optimus-product/target/optimus-product-1.0.jar /opt/product
sudo cp /docker/script/workspace/optimus/optimus-auth/target/optimus-auth-1.0.jar /opt/auth
sudo cp /docker/script/workspace/optimus/optimus-webhook/target/optimus-webhook-1.0.jar /opt/webhook
sudo cp /docker/script/workspace/optimus/optimus-notification/target/optimus-notification-1.0.jar /opt/notification
sudo cp /docker/script/workspace/optimus/optimus-location/target/optimus-location-1.0.jar /opt/location
sudo cp /docker/script/workspace/optimus/optimus-customer/target/optimus-customer-1.0.jar /opt/customer
sudo cp /docker/script/workspace/optimus/optimus-oms/target/optimus-oms-1.0.jar /opt/oms
sudo cp /docker/script/workspace/optimus/optimus-service/target/optimus-service-1.0.jar /opt/service
sudo cp /docker/script/workspace/optimus/optimus-wfe/target/optimus-wfe-1.0.jar /opt/wfe

## Build docker images
sudo docker build -t optimus-product /opt/product/
sudo docker build -t optimus-auth /opt/auth/
sudo docker build -t optimus-webhook /opt/webhook/
sudo docker build -t optimus-notification /opt/notification/
sudo docker build -t optimus-location /opt/location/
sudo docker build -t optimus-customer /opt/customer/
sudo docker build -t optimus-oms /opt/oms/
sudo docker build -t optimus-service /opt/service/
sudo docker build -t optimus-wfe /opt/wfe/

## Remove containers
sudo docker kill optimus-product
sudo docker rm optimus-product
sudo docker kill optimus-auth
sudo docker rm optimus-auth
sudo docker kill optimus-webhook
sudo docker rm optimus-webhook
sudo docker kill optimus-notification
sudo docker rm optimus-notification
sudo docker kill optimus-oms
sudo docker rm optimus-oms
sudo docker kill optimus-location
sudo docker rm optimus-location
sudo docker kill optimus-customer
sudo docker rm optimus-customer
sudo docker kill optimus-service
sudo docker rm optimus-service
sudo docker kill optimus-wfe
sudo docker rm optimus-wfe

## Run Containers
sudo docker run -e DB_URL='jdbc:mysql://10.133.208.140:3306/oms_uat?zeroDateTimeBehavior=convertToNull&autoReconnect=true&failOverReadOnly=false&maxReconnects=10&useSSL=false' -e CAS_HOST='optimus-uat.tatacommunications.com' -e APP_HOST='optimus-uat.tatacommunications.com' -e DB_UNAME=$DB_UNAME -e DB_PWD=$DB_PWD -e MQ_HOST=$MQ_HOST -e MQ_PORT=$MQ_PORT -e MQ_UNAME=$MQ_UNAME -e MQ_PWD=$MQ_PWD -e REDIS_HOST=$REDIS_HOST -e REDIS_PORT=$REDIS_PORT -e REDIS_UNAME=$REDIS_UNAME -e REDIS_PWD=$REDIS_PWD -e AUTH_URL=$AUTH_URL -e LDAP_URL=$LDAP_URL -e LDAP_BASE=$LDAP_BASE -e LDAP_UNAME=$LDAP_UNAME -e LDAP_PWD=$LDAP_PWD -e KEYCLOAK_PROXY=$KEYCLOAK_PROXY -e SYSTEM_PROXY_HOST=$SYSTEM_PROXY_HOST -e SYSTEM_PROXY_PORT=$SYSTEM_PROXY_PORT -e KEYCLOAK_UNAME=$KEYCLOAK_UNAME -e KEYCLOAK_PWD=$KEYCLOAK_PWD -d --name optimus-auth -p 6060:6060 -t optimus-auth
sudo docker run -e DB_URL='jdbc:mysql://10.133.208.140:3306/product_catalog_uat?zeroDateTimeBehavior=convertToNull&autoReconnect=true&failOverReadOnly=false&maxReconnects=10&useSSL=false' -e DB_UNAME=$DB_UNAME -e DB_PWD=$DB_PWD -e MQ_HOST=$MQ_HOST -e MQ_PORT=$MQ_PORT -e MQ_UNAME=$MQ_UNAME -e MQ_PWD=$MQ_PWD -e REDIS_HOST=$REDIS_HOST -e REDIS_PORT=$REDIS_PORT -e REDIS_UNAME=$REDIS_UNAME -e REDIS_PWD=$REDIS_PWD -e SYSTEM_PROXY_HOST=$SYSTEM_PROXY_HOST -e SYSTEM_PROXY_PORT=$SYSTEM_PROXY_PORT -e KEYCLOAK_PROXY=$KEYCLOAK_PROXY -e AUTH_URL=$AUTH_URL -d --name optimus-product -p 9091:7070 -t optimus-product
sudo docker run -e MQ_HOST=$MQ_HOST -e MQ_PORT=$MQ_PORT -e MQ_UNAME=$MQ_UNAME -e MQ_PWD=$MQ_PWD -e USER_NAME=$USER_NAME -e USER_PASSWORD=$USER_PASSWORD -d --name optimus-webhook -p 4242:4242 -t optimus-webhook
sudo docker run -e DB_UNAME=$DB_UNAME -e DB_PWD=$DB_PWD -e MQ_HOST=$MQ_HOST -e MQ_PORT=$MQ_PORT -e MQ_UNAME=$MQ_UNAME -e MQ_PWD=$MQ_PWD -e REDIS_HOST=$REDIS_HOST -e REDIS_PORT=$REDIS_PORT -e REDIS_UNAME=$REDIS_UNAME -e REDIS_PWD=$REDIS_PWD -e SMTP_HOST=$SMTP_HOST -e SMTP_UNAME=$SMTP_UNAME -e SMTP_PWD=$SMTP_PWD -e SMTP_PORT=$SMTP_PORT -e SYSTEM_PROXY_HOST=$SYSTEM_PROXY_HOST -e SYSTEM_PROXY_PORT=$SYSTEM_PROXY_PORT -e KEYCLOAK_PROXY=$KEYCLOAK_PROXY -e APP_ENV=$APP_ENV -e AUTH_URL=$AUTH_URL  -d --name optimus-notification -v /docker/oms/:/opt/optimus-files/ -p 8090:8090 -t optimus-notification
sudo docker run -e DB_URL='jdbc:mysql://10.133.208.140:3306/location_uat?zeroDateTimeBehavior=convertToNull&autoReconnect=true&failOverReadOnly=false&maxReconnects=10&useSSL=false' -e DB_UNAME=$DB_UNAME -e DB_PWD=$DB_PWD -e MQ_HOST=$MQ_HOST -e MQ_PORT=$MQ_PORT -e MQ_UNAME=$MQ_UNAME -e MQ_PWD=$MQ_PWD -e REDIS_HOST=$REDIS_HOST -e REDIS_PORT=$REDIS_PORT -e REDIS_UNAME=$REDIS_UNAME -e REDIS_PWD=$REDIS_PWD -e SYSTEM_PROXY_HOST=$SYSTEM_PROXY_HOST -e SYSTEM_PROXY_PORT=$SYSTEM_PROXY_PORT -e KEYCLOAK_PROXY=$KEYCLOAK_PROXY -e AUTH_URL=$AUTH_URL  -d --name optimus-location -p 7071:7071 -t optimus-location
sudo docker run -v "/docker/customer/optimus-files:/opt/optimus-files" -e DB_URL='jdbc:mysql://10.133.208.140:3306/customer_uat?zeroDateTimeBehavior=convertToNull&autoReconnect=true&failOverReadOnly=false&maxReconnects=10&useSSL=false' -e DB_UNAME=$DB_UNAME -e DB_PWD=$DB_PWD -e MQ_HOST=$MQ_HOST -e MQ_PORT=$MQ_PORT -e MQ_UNAME=$MQ_UNAME -e MQ_PWD=$MQ_PWD -e REDIS_HOST=$REDIS_HOST -e REDIS_PORT=$REDIS_PORT -e REDIS_UNAME=$REDIS_UNAME -e REDIS_PWD=$REDIS_PWD -e SYSTEM_PROXY_HOST=$SYSTEM_PROXY_HOST -e SYSTEM_PROXY_PORT=$SYSTEM_PROXY_PORT -e KEYCLOAK_PROXY=$KEYCLOAK_PROXY -e AUTH_URL=$AUTH_URL -d --name optimus-customer -p 7073:7078 -t optimus-customer
sudo docker run -e DB_URL='jdbc:mysql://10.133.208.140:3306/oms_uat?zeroDateTimeBehavior=convertToNull&autoReconnect=true&failOverReadOnly=false&maxReconnects=10&useSSL=false' -e DB_UNAME=$DB_UNAME -e DB_PWD=$DB_PWD -e MQ_HOST=$MQ_HOST -e MQ_PORT=$MQ_PORT -e MQ_UNAME=$MQ_UNAME -e MQ_PWD=$MQ_PWD -e REDIS_HOST=$REDIS_HOST -e REDIS_PORT=$REDIS_PORT -e REDIS_UNAME=$REDIS_UNAME -e REDIS_PWD=$REDIS_PWD -e R_PRICING=$R_PRICING -e SYSTEM_PROXY_HOST=$SYSTEM_PROXY_HOST -e SYSTEM_PROXY_PORT=$SYSTEM_PROXY_PORT -e APP_HOST=$APP_HOST -e APP_ENV=$APP_ENV -e CUSTOMER_SUPPORT_EMAIL=$CUSTOMER_SUPPORT_EMAIL -e KEYCLOAK_PROXY=$KEYCLOAK_PROXY -e AUTH_URL=$AUTH_URL -e R_GVPN_PRICING=$R_GVPN_PRICING -e R_GVPN_IND_PRICING=$R_GVPN_IND_PRICING -e R_NPL_PRICING=$R_NPL_PRICING -d --name optimus-oms -v /docker/oms/:/opt/optimus-files/ -p 7072:7072 -t optimus-oms
sudo docker run -e DB_UNAME=$DB_UNAME -e DB_PWD=$DB_PWD -e MQ_HOST=$MQ_HOST -e MQ_PORT=$MQ_PORT -e MQ_UNAME=$MQ_UNAME -e MQ_PWD=$MQ_PWD -e REDIS_HOST=$REDIS_HOST -e REDIS_PORT=$REDIS_PORT -e REDIS_UNAME=$REDIS_UNAME -e REDIS_PWD=$REDIS_PWD -e APP_HOST='optimus-uat.tatacommunications.com' -e SFDC_UNAME=$SFDC_UNAME -e SFDC_PWD=$SFDC_PWD -e SFDC_GRANT_TYPE=$SFDC_GRANT_TYPE -e SFDC_AUTH=$SFDC_AUTH -e SFDC_CLIENT=$SFDC_CLIENT -e SFDC_SECRET=$SFDC_SECRET -e SFDC_AUTH_URL=$SFDC_AUTH_URL -e SFDC_GEN_HOST=$SFDC_GEN_HOST -e SFDC_OPT_CREATE_PATH=$SFDC_OPT_CREATE_PATH -e SFDC_PRD_CREATE_REQ_URL=$SFDC_PRD_CREATE_REQ_URL -e SFDC_PRD_DELETE_URL=$SFDC_PRD_DELETE_URL -e SFDC_STAGE_UPDATE_URL=$SFDC_STAGE_UPDATE_URL -e SFDC_SITE_UPDATE_URL=$SFDC_SITE_UPDATE_URL -e SYSTEM_PROXY_HOST=$SYSTEM_PROXY_HOST -e SYSTEM_PROXY_PORT=$SYSTEM_PROXY_PORT  -e BUILD_VERSION=$BUILD_VERSION -e KEYCLOAK_PROXY=$KEYCLOAK_PROXY -e AUTH_URL=$AUTH_URL -d --name optimus-service -p 7074:7074 -t optimus-service
sudo docker run -e DB_URL='jdbc:mysql://10.133.208.140:3306/camunda?zeroDateTimeBehavior=convertToNull&autoReconnect=true&failOverReadOnly=false&maxReconnects=10&useSSL=false' -e DB_UNAME=$DB_UNAME -e DB_PWD=$DB_PWD -e MQ_HOST=$MQ_HOST -e MQ_PORT=$MQ_PORT -e MQ_UNAME=$MQ_UNAME -e MQ_PWD=$MQ_PWD -e REDIS_HOST=$REDIS_HOST -e REDIS_PORT=$REDIS_PORT -e REDIS_UNAME=$REDIS_UNAME -e REDIS_PWD=$REDIS_PWD -e R_ONNET_WIRELINE=$R_ONNET_WIRELINE -e R_ONNET_WIRELESS=$R_ONNET_WIRELESS -e R_OFFNET_WIRELESS=$R_OFFNET_WIRELESS -e SYSTEM_PROXY_HOST=$SYSTEM_PROXY_HOST -e SYSTEM_PROXY_PORT=$SYSTEM_PROXY_PORT -e KEYCLOAK_PROXY=$KEYCLOAK_PROXY -e AUTH_URL=$AUTH_URL -e R_NPL_ONNET_POP=$R_NPL_ONNET_POP -d --name optimus-wfe -p 7019:7019 -t optimus-wfe

## Clean workspace application directory
cd ..
rm -rf optimus

