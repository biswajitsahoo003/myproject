## Export proxy
export http_proxy="http://10.133.12.181:80"
export https_proxy="http://10.133.12.181:80"

#Env variables
DB_UNAME=optimus
DB_PWD=OPTimus12\#\$
MQ_HOST=10.133.208.115
MQ_PORT=31548
MQ_UNAME=web
MQ_PWD=tata
AUTH_URL=http://10.133.208.115/auth/
REDIS_HOST=10.133.208.115
REDIS_PORT=31549
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
R_PRICING=http://10.133.208.121/new/pricing/api

#GVPN-NPL CHANGES
R_GVPN_PRICING=http://10.133.208.121/new/gvpn/pricing/api
R_GVPN_IND_PRICING=http://10.133.208.121/new/gvpn/ind-int/pricing/api
R_NPL_PRICING=http://10.133.208.121/new/npl/pricing/api
R_NPL_ONNET_POP=http://10.133.208.121/new/npl/pop/api

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
BUILD_VERSION=Hydrogen1.0
APP_HOST=http://optimus-uat.tatacommunications.com
APP_ENV=DEV
CUSTOMER_SUPPORT_EMAIL=idriveoptimus@gmail.com
#Web hook env
USER_NAME=optimus_docusign_admin
USER_PASSWORD=UIOSDnkmasdmasdTYIojmnlmn3457918230

## clone & mvn build application
/docker/script/maven/apache-maven-3.5.2/bin/mvn -version
cd /docker/script/workspace/k8s
sudo git clone -b Hydrogen http://root:Tata#123@10.133.208.133/gitlab/root/optimus.git
sudo chmod -R og+rwx optimus
cd optimus/
/docker/script/maven/apache-maven-3.5.2/bin/mvn clean install

## copy files to /opt/...
sudo cp /docker/script/workspace/k8s/optimus/optimus-product/target/optimus-product-1.0.jar /opt/product
sudo cp /docker/script/workspace/k8s/optimus/optimus-auth/target/optimus-auth-1.0.jar /opt/auth
sudo cp /docker/script/workspace/k8s/optimus/optimus-webhook/target/optimus-webhook-1.0.jar /opt/webhook
sudo cp /docker/script/workspace/k8s/optimus/optimus-notification/target/optimus-notification-1.0.jar /opt/notification
sudo cp /docker/script/workspace/k8s/optimus/optimus-location/target/optimus-location-1.0.jar /opt/location
sudo cp /docker/script/workspace/k8s/optimus/optimus-customer/target/optimus-customer-1.0.jar /opt/customer
sudo cp /docker/script/workspace/k8s/optimus/optimus-oms/target/optimus-oms-1.0.jar /opt/oms
sudo cp /docker/script/workspace/k8s/optimus/optimus-service/target/optimus-service-1.0.jar /opt/service
sudo cp /docker/script/workspace/k8s/optimus/optimus-wfe/target/optimus-wfe-1.0.jar /opt/wfe

## Build docker images
sudo docker build -t optimus-product /opt/product/
sudo docker tag optimus-product:latest 10.133.208.115:32550/optimus-product:latest
sudo docker push 10.133.208.115:32550/optimus-product:latest

sudo docker build -t optimus-auth /opt/auth/
sudo docker tag optimus-auth:latest 10.133.208.115:32550/optimus-auth:latest
sudo docker push 10.133.208.115:32550/optimus-auth:latest

sudo docker build -t optimus-webhook /opt/webhook/
sudo docker tag optimus-webhook:latest 10.133.208.115:32550/optimus-webhook:latest
sudo docker push 10.133.208.115:32550/optimus-webhook:latest

sudo docker build -t optimus-notification /opt/notification/
sudo docker tag optimus-notification:latest 10.133.208.115:32550/optimus-notification:latest
sudo docker push 10.133.208.115:32550/optimus-notification:latest

sudo docker build -t optimus-location /opt/location/
sudo docker tag optimus-location:latest 10.133.208.115:32550/optimus-location:latest
sudo docker push 10.133.208.115:32550/optimus-location:latest

sudo docker build -t optimus-customer /opt/customer/
sudo docker tag optimus-customer:latest 10.133.208.115:32550/optimus-customer:latest
sudo docker push 10.133.208.115:32550/optimus-customer:latest

sudo docker build -t optimus-oms /opt/oms/
sudo docker tag optimus-oms:latest 10.133.208.115:32550/optimus-oms:latest
sudo docker push 10.133.208.115:32550/optimus-oms:latest

sudo docker build -t optimus-service /opt/service/
sudo docker tag optimus-service:latest 10.133.208.115:32550/optimus-service:latest
sudo docker push 10.133.208.115:32550/optimus-service:latest

sudo docker build -t optimus-wfe /opt/wfe/
sudo docker tag optimus-wfe:latest 10.133.208.115:32550/optimus-wfe:latest
sudo docker push 10.133.208.115:32550/optimus-wfe:latest

unset http_proxy
unset https_proxy

## Remove containers
kubectl -n optimus delete deployment optimus-product
kubectl -n optimus delete deployment optimus-auth
kubectl -n optimus delete deployment optimus-webhook
#kubectl -n optimus delete deployment optimus-notification
#kubectl -n optimus delete deployment optimus-oms
kubectl -n optimus delete deployment optimus-location
#kubectl -n optimus delete deployment optimus-customer
kubectl -n optimus delete deployment optimus-service
kubectl -n optimus delete deployment optimus-wfe
sleep 30


## Run Containers  ==> add "MQ_PORT" && "REDIS_PORT"
kubectl -n optimus run optimus-auth --image=10.133.208.115:32550/optimus-auth:latest --port=6060 --replicas=1 --env="DB_URL=jdbc:mysql://10.133.208.134:3306/oms?zeroDateTimeBehavior=convertToNull&autoReconnect=true&failOverReadOnly=false&maxReconnects=10&useSSL=false" --env="DB_UNAME="$DB_UNAME --env="DB_PWD="$DB_PWD --env="MQ_HOST="$MQ_HOST --env="MQ_PORT="$MQ_PORT --env="MQ_UNAME="$MQ_UNAME --env="MQ_PWD="$MQ_PWD --env="REDIS_HOST="$REDIS_HOST --env="REDIS_PORT="$REDIS_PORT --env="REDIS_UNAME="$REDIS_UNAME --env="REDIS_PWD="$REDIS_PWD --env="CAS_HOST=10.133.208.115" --env="APP_HOST=10.133.208.115" --env="AUTH_URL="$AUTH_URL --env="LDAP_URL="$LDAP_URL --env="LDAP_BASE="$LDAP_BASE  --env="LDAP_UNAME="$LDAP_UNAME  --env="LDAP_PWD="$LDAP_PWD --env="KEYCLOAK_PROXY="$KEYCLOAK_PROXY --env="SYSTEM_PROXY_HOST="$SYSTEM_PROXY_HOST --env="SYSTEM_PROXY_PORT="$SYSTEM_PROXY_PORT --env="KEYCLOAK_UNAME="$KEYCLOAK_UNAME --env="KEYCLOAK_PWD="$KEYCLOAK_PWD

kubectl -n optimus run optimus-product --image=10.133.208.115:32550/optimus-product:latest --port=7070 --replicas=1 --env="MQ_PWD=tata" --env="DB_URL=jdbc:mysql://10.133.208.134:3306/product_catalog?zeroDateTimeBehavior=convertToNull&autoReconnect=true&failOverReadOnly=false&maxReconnects=10&useSSL=false" --env="DB_UNAME="$DB_UNAME --env="DB_PWD="$DB_PWD --env="MQ_HOST="$MQ_HOST --env="MQ_PORT="$MQ_PORT --env="MQ_UNAME="$MQ_UNAME --env="MQ_PWD="$MQ_PWD --env="REDIS_HOST="$REDIS_HOST --env="REDIS_PORT="$REDIS_PORT --env="REDIS_UNAME="$REDIS_UNAME --env="REDIS_PWD="$REDIS_PWD --env="APP_HOST=10.133.208.115" --env="SYSTEM_PROXY_HOST="$SYSTEM_PROXY_HOST --env="SYSTEM_PROXY_PORT="$SYSTEM_PROXY_PORT --env="KEYCLOAK_PROXY="$KEYCLOAK_PROXY --env="AUTH_URL="$AUTH_URL

kubectl -n optimus run optimus-webhook --image=10.133.208.115:32550/optimus-webhook:latest --port=4042 --replicas=1 --env="MQ_HOST="$MQ_HOST --env="MQ_PORT="$MQ_PORT --env="MQ_UNAME="$MQ_UNAME --env="MQ_PWD="$MQ_PWD --env="USER_NAME="$USER_NAME --env="USER_PASSWORD="$USER_PASSWORD

## OPTIMUS-NOTIFICATION ##
#kubectl -n optimus run optimus-notification --image=10.133.208.115:32550/optimus-notification:latest --port=8090 --replicas=1 --env="DB_UNAME="$DB_UNAME --env="DB_PWD="$DB_PWD --env="MQ_HOST="$MQ_HOST --env="MQ_PORT="$MQ_PORT --env="MQ_UNAME="$MQ_UNAME --env="MQ_PWD="$MQ_PWD --env="REDIS_HOST="$REDIS_HOST --env="REDIS_PORT="$REDIS_PORT --env="REDIS_UNAME="$REDIS_UNAME --env="REDIS_PWD="$REDIS_PWD --env="SMTP_HOST="$SMTP_HOST --env="SMTP_UNAME="$SMTP_UNAME --env="SMTP_PWD="$SMTP_PWD --env="SMTP_PORT="$SMTP_PORT --env="SYSTEM_PROXY_HOST="$SYSTEM_PROXY_HOST --env="SYSTEM_PROXY_PORT="$SYSTEM_PROXY_PORT --env="KEYCLOAK_PROXY="$KEYCLOAK_PROXY --env="AUTH_URL="$AUTH_URL --env="APP_ENV="$APP_ENV

kubectl -n optimus set image deploy/optimus-notification optimus-notification="10.133.208.115:32550/optimus-notification:latest"

kubectl -n optimus set env deployment/optimus-notification UPDATED="$(date)" DB_UNAME=$DB_UNAME DB_PWD=$DB_PWD MQ_HOST=$MQ_HOST MQ_PORT=$MQ_PORT MQ_UNAME=$MQ_UNAME MQ_PWD=$MQ_PWD REDIS_HOST=$REDIS_HOST REDIS_PORT=$REDIS_PORT REDIS_UNAME=$REDIS_UNAME REDIS_PWD=$REDIS_PWD SMTP_HOST=$SMTP_HOST SMTP_UNAME=$SMTP_UNAME SMTP_PWD=$SMTP_PWD SMTP_PORT=$SMTP_PORT SYSTEM_PROXY_HOST=$SYSTEM_PROXY_HOST SYSTEM_PROXY_PORT=$SYSTEM_PROXY_PORT KEYCLOAK_PROXY=$KEYCLOAK_PROXY AUTH_URL=$AUTH_URL APP_ENV=$APP_ENV
## OPTIMUS-NOTIFICATION ##

kubectl -n optimus run optimus-location --image=10.133.208.115:32550/optimus-location:latest --port=7071 --replicas=1 --env="DB_URL=jdbc:mysql://10.133.208.134:3306/location?zeroDateTimeBehavior=convertToNull&autoReconnect=true&failOverReadOnly=false&maxReconnects=10&useSSL=false" --env="DB_UNAME="$DB_UNAME --env="DB_PWD="$DB_PWD --env="MQ_HOST="$MQ_HOST --env="MQ_PORT="$MQ_PORT --env="MQ_UNAME="$MQ_UNAME --env="MQ_PWD="$MQ_PWD --env="REDIS_HOST="$REDIS_HOST --env="REDIS_PORT="$REDIS_PORT --env="REDIS_UNAME="$REDIS_UNAME --env="REDIS_PWD="$REDIS_PWD --env="SYSTEM_PROXY_HOST="$SYSTEM_PROXY_HOST --env="SYSTEM_PROXY_PORT="$SYSTEM_PROXY_PORT --env="KEYCLOAK_PROXY="$KEYCLOAK_PROXY --env="AUTH_URL="$AUTH_URL


## OPTIMUS-CUSTOMER ##

##---------- attach gluster fs path -v "/docker/customer/optimus-files:/opt/optimus-files" ------------
#kubectl -n optimus run optimus-customer --image=10.133.208.115:32550/optimus-customer:latest --port=7078 --replicas=1 --env="DB_URL=jdbc:mysql://10.133.208.134:3306/customer?zeroDateTimeBehavior=convertToNull&autoReconnect=true&failOverReadOnly=false&maxReconnects=10&useSSL=false" --env="DB_UNAME="$DB_UNAME --env="DB_PWD="$DB_PWD --env="MQ_HOST="$MQ_HOST --env="MQ_PORT="$MQ_PORT --env="MQ_UNAME="$MQ_UNAME --env="MQ_PWD="$MQ_PWD --env="REDIS_HOST="$REDIS_HOST --env="REDIS_PORT="$REDIS_PORT --env="REDIS_UNAME="$REDIS_UNAME --env="REDIS_PWD="$REDIS_PWD --env="SYSTEM_PROXY_HOST="$SYSTEM_PROXY_HOST --env="SYSTEM_PROXY_PORT="$SYSTEM_PROXY_PORT --env="KEYCLOAK_PROXY="$KEYCLOAK_PROXY --env="AUTH_URL="$AUTH_URL

kubectl -n optimus set image deploy/optimus-customer optimus-customer="10.133.208.115:32550/optimus-customer:latest"

kubectl -n optimus set env deployment/optimus-customer UPDATED="$(date)" DB_URL="jdbc:mysql://10.133.208.134:3306/customer?zeroDateTimeBehavior=convertToNull&autoReconnect=true&failOverReadOnly=false&maxReconnects=10&useSSL=false" DB_UNAME=$DB_UNAME DB_PWD=$DB_PWD MQ_HOST=$MQ_HOST MQ_PORT=$MQ_PORT MQ_UNAME=$MQ_UNAME MQ_PWD=$MQ_PWD REDIS_HOST=$REDIS_HOST REDIS_PORT=$REDIS_PORT REDIS_UNAME=$REDIS_UNAME REDIS_PWD=$REDIS_PWD SYSTEM_PROXY_HOST=$SYSTEM_PROXY_HOST SYSTEM_PROXY_PORT=$SYSTEM_PROXY_PORT KEYCLOAK_PROXY=$KEYCLOAK_PROXY AUTH_URL=$AUTH_URL

## OPTIMUS-CUSTOMER ##

## OPTIMUS-OMS ##
#kubectl -n optimus run optimus-oms --image=10.133.208.115:32550/optimus-oms:latest --port=7072 --replicas=1 --env="DB_URL=jdbc:mysql://10.133.208.134:3306/oms?zeroDateTimeBehavior=convertToNull&autoReconnect=true&failOverReadOnly=false&maxReconnects=10&useSSL=false" --env="DB_UNAME="$DB_UNAME --env="DB_PWD="$DB_PWD --env="MQ_HOST="$MQ_HOST --env="MQ_PORT="$MQ_PORT --env="MQ_UNAME="$MQ_UNAME --env="MQ_PWD="$MQ_PWD --env="REDIS_HOST="$REDIS_HOST --env="REDIS_PORT="$REDIS_PORT --env="REDIS_UNAME="$REDIS_UNAME --env="REDIS_PWD="$REDIS_PWD --env="R_PRICING="$R_PRICING --env="SYSTEM_PROXY_HOST="$SYSTEM_PROXY_HOST --env="SYSTEM_PROXY_PORT="$SYSTEM_PROXY_PORT --env="APP_HOST="$APP_HOST --env="APP_ENV="$APP_ENV --env="CUSTOMER_SUPPORT_EMAIL="$CUSTOMER_SUPPORT_EMAIL --env="KEYCLOAK_PROXY="$KEYCLOAK_PROXY --env="AUTH_URL="$AUTH_URL --env="R_GVPN_PRICING="$R_GVPN_PRICING --env="R_GVPN_IND_PRICING="$R_GVPN_IND_PRICING --env="R_NPL_PRICING="$R_NPL_PRICING

kubectl -n optimus set image deploy/optimus-oms optimus-oms="10.133.208.115:32550/optimus-oms:latest"

kubectl -n optimus set env deployment/optimus-oms UPDATED="$(date)" DB_URL="jdbc:mysql://10.133.208.134:3306/oms?zeroDateTimeBehavior=convertToNull&autoReconnect=true&failOverReadOnly=false&maxReconnects=10&useSSL=false" DB_UNAME=$DB_UNAME DB_PWD=$DB_PWD MQ_HOST=$MQ_HOST MQ_PORT=$MQ_PORT MQ_UNAME=$MQ_UNAME MQ_PWD=$MQ_PWD REDIS_HOST=$REDIS_HOST REDIS_PORT=$REDIS_PORT REDIS_UNAME=$REDIS_UNAME REDIS_PWD=$REDIS_PWD R_PRICING=$R_PRICING SYSTEM_PROXY_HOST=$SYSTEM_PROXY_HOST SYSTEM_PROXY_PORT=$SYSTEM_PROXY_PORT APP_HOST=$APP_HOST APP_ENV=$APP_ENV CUSTOMER_SUPPORT_EMAIL=$CUSTOMER_SUPPORT_EMAIL KEYCLOAK_PROXY=$KEYCLOAK_PROXY AUTH_URL=$AUTH_URL R_GVPN_PRICING=$R_GVPN_PRICING R_GVPN_IND_PRICING=$R_GVPN_IND_PRICING R_NPL_PRICING=$R_NPL_PRICING
## OPTIMUS-OMS ##

kubectl -n optimus run optimus-service --image=10.133.208.115:32550/optimus-service:latest --port=7074 --replicas=1 --env="DB_UNAME="$DB_UNAME --env="DB_PWD="$DB_PWD --env="MQ_HOST="$MQ_HOST --env="MQ_PORT="$MQ_PORT --env="MQ_UNAME="$MQ_UNAME --env="MQ_PWD="$MQ_PWD --env="REDIS_HOST="$REDIS_HOST --env="REDIS_PORT="$REDIS_PORT --env="REDIS_UNAME="$REDIS_UNAME --env="REDIS_PWD="$REDIS_PWD --env="APP_HOST=10.133.208.115" --env="SFDC_UNAME="$SFDC_UNAME --env="SFDC_PWD="$SFDC_PWD --env="SFDC_GRANT_TYPE="$SFDC_GRANT_TYPE --env="SFDC_AUTH="$SFDC_AUTH --env="SFDC_CLIENT="$SFDC_CLIENT --env="SFDC_SECRET="$SFDC_SECRET --env="SFDC_AUTH_URL="$SFDC_AUTH_URL --env="SFDC_GEN_HOST="$SFDC_GEN_HOST --env="SFDC_OPT_CREATE_PATH="$SFDC_OPT_CREATE_PATH --env="SFDC_PRD_CREATE_REQ_URL="$SFDC_PRD_CREATE_REQ_URL --env="SFDC_PRD_DELETE_URL="$SFDC_PRD_DELETE_URL --env="SFDC_STAGE_UPDATE_URL="$SFDC_STAGE_UPDATE_URL --env="SFDC_SITE_UPDATE_URL="$SFDC_SITE_UPDATE_URL --env="SYSTEM_PROXY_HOST="$SYSTEM_PROXY_HOST --env="SYSTEM_PROXY_PORT="$SYSTEM_PROXY_PORT --env="BUILD_VERSION="$BUILD_VERSION  --env="KEYCLOAK_PROXY="$KEYCLOAK_PROXY --env="AUTH_URL="$AUTH_URL

kubectl -n optimus run optimus-wfe --image=10.133.208.115:32550/optimus-wfe:latest --port=7019 --replicas=1 --env="DB_URL=jdbc:mysql://10.133.208.134:3306/camunda?zeroDateTimeBehavior=convertToNull&autoReconnect=true&failOverReadOnly=false&maxReconnects=10&useSSL=false" --env="DB_UNAME="$DB_UNAME --env="DB_PWD="$DB_PWD --env="MQ_HOST="$MQ_HOST --env="MQ_PORT="$MQ_PORT --env="MQ_UNAME="$MQ_UNAME --env="MQ_PWD="$MQ_PWD --env="REDIS_HOST="$REDIS_HOST --env="REDIS_PORT="$REDIS_PORT --env="REDIS_UNAME="$REDIS_UNAME --env="REDIS_PWD="$REDIS_PWD --env="R_ONNET_WIRELINE="$R_ONNET_WIRELINE --env="R_ONNET_WIRELESS="$R_ONNET_WIRELESS --env="R_OFFNET_WIRELESS="$R_OFFNET_WIRELESS --env="SYSTEM_PROXY_HOST="$SYSTEM_PROXY_HOST --env="SYSTEM_PROXY_PORT="$SYSTEM_PROXY_PORT --env="KEYCLOAK_PROXY="$KEYCLOAK_PROXY --env="AUTH_URL="$AUTH_URL  --env="R_NPL_ONNET_POP="$R_NPL_ONNET_POP


## Clean workspace application directory
cd ..
rm -rf optimus
