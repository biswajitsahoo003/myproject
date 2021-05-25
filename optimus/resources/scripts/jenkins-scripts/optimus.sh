echo "optimus-shell"
if [ "$(ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker inspect -f '{{.State.Running}}' optimus-product")" ];then
echo "optimus-product is running";

#ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker kill optimus-product"
ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker rm -f optimus-product"
echo "Killed..";
fi

if [ "$(ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker inspect -f '{{.State.Running}}' optimus-auth")" ];then
echo "optimus-auth is running...";

#ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker kill optimus-auth"
ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker rm -f optimus-auth"
echo "Killed..";
fi

if [ "$(ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker inspect -f '{{.State.Running}}' optimus-userservice")" ];then
echo "optimus-userservice is running...";

#ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker kill optimus-userservice"
ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker rm -f optimus-userservice"
echo "Killed..";
fi


if [ "$(ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker inspect -f '{{.State.Running}}' optimus-notification")" ];then
echo "optimus-notification is running...";

#ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker kill optimus-notification"
ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker rm -f optimus-notification"
echo "Killed..";
fi


if [ "$(ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker inspect -f '{{.State.Running}}' optimus-oms")" ];then
echo "optimus-oms is running...";

#ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker kill optimus-oms"
ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker rm -f optimus-oms"
echo "Killed..";
fi


if [ "$(ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker inspect -f '{{.State.Running}}'  optimus-location")" ];then
echo " optimus-location is running...";

#ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker kill  optimus-location"
ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker rm -f optimus-location"
echo "Killed..";
fi

if [ "$(ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker inspect -f '{{.State.Running}}' optimus-customer")" ];then
echo "optimus-customer is running";

#ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker kill optimus-customer"
ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker rm -f optimus-customer"
echo "Killed..";
fi

if [ "$(ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker inspect -f '{{.State.Running}}' optimus-service")" ];then
echo "optimus-service is running";

#ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker kill optimus-service"
ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker rm -f optimus-service"
echo "Killed..";
fi

if [ "$(ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker inspect -f '{{.State.Running}}' optimus-wfe")" ];then
echo "optimus-wfe is running";

#ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker kill optimus-wfe"
ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker rm -f optimus-wfe"
echo "Killed..";
fi

#if [ "$(ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker inspect -f '{{.State.Running}}' optimus-soap")" ];then
#echo "optimus-soap is running";

#ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker kill optimus-soap"
#ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker rm optimus-soap"
#echo "Killed..";
#fi


#ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker kill optimus-product"
#ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker rm optimus-product"
#ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker kill optimus-auth"
#ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker rm optimus-auth"
#ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker kill optimus-userservice"
#ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker rm optimus-userservice"
#ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker kill optimus-notification"
#ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker rm optimus-notification"
#ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker kill optimus-quote"
#ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker rm optimus-quote"
#ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker kill optimus-location"
#ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker rm optimus-location"
#ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker kill optimus-customer"
#ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker rm optimus-customer"

ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker cp 32f2ae7d7dd0:var/jenkins_home/workspace/Dev.optimus-product/optimus-auth/target/optimus-auth-1.0.jar /opt/auth"
ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker cp 32f2ae7d7dd0:var/jenkins_home/workspace/Dev.optimus-product/optimus-product/target/optimus-product-1.0.jar /opt/product"
ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker cp 32f2ae7d7dd0:var/jenkins_home/workspace/Dev.optimus-product/optimus-userservice/target/optimus-userservice-1.0.jar /opt/userservice"
ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker cp 32f2ae7d7dd0:var/jenkins_home/workspace/Dev.optimus-product/optimus-notification/target/optimus-notification-1.0.jar /opt/notification"
ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker cp 32f2ae7d7dd0:var/jenkins_home/workspace/Dev.optimus-product/optimus-location/target/optimus-location-1.0.jar /opt/location"
ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker cp 32f2ae7d7dd0:var/jenkins_home/workspace/Dev.optimus-product/optimus-customer/target/optimus-customer-1.0.jar /opt/customer"
ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker cp 32f2ae7d7dd0:var/jenkins_home/workspace/Dev.optimus-product/optimus-oms/target/optimus-oms-1.0.jar /opt/oms"
ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker cp 32f2ae7d7dd0:var/jenkins_home/workspace/Dev.optimus-product/optimus-service/target/optimus-service-1.0.jar /opt/service"
ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker cp 32f2ae7d7dd0:var/jenkins_home/workspace/Dev.optimus-product/optimus-wfe/target/optimus-wfe-1.0.jar /opt/wfe"
#ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker cp #32f2ae7d7dd0:var/jenkins_home/workspace/Dev.optimus-soap/optimus-soap/target/optimus-soap-1.0.jar /opt/soap"

ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker build -t optimus-product /opt/product/"
ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker build -t optimus-auth /opt/auth/"
ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker build -t optimus-userservice /opt/userservice/"
ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker build -t optimus-notification /opt/notification/"
ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker build -t optimus-location /opt/location/"
ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker build -t optimus-customer /opt/customer/"
ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker build -t optimus-oms /opt/oms/"
ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker build -t optimus-service /opt/service/"
ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker build -t optimus-wfe /opt/wfe/"
#ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker build -t optimus-soap /opt/soap/"


ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker run -e val1='am from docker Dev.env' -e MQ_HOST='10.133.208.135' -e MQ_PWD='tata' -e DB_HOST='jdbc:mysql://10.133.208.140:3306/oms_uat?zeroDateTimeBehavior=convertToNull&autoReconnect=true&failOverReadOnly=false&maxReconnects=10&useSSL=false' -e REDIS_HOST='10.133.208.136' -e CAS_HOST='optimus-uat.tatacommunications.com' -e APP_HOST='optimus-uat.tatacommunications.com' -d --name optimus-auth -p 6060:6060 -t optimus-auth"

ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker run -e val1='am from docker Dev.env' -e MQ_HOST='10.133.208.135' -e MQ_PWD='tata' -e DB_HOST='jdbc:mysql://10.133.208.140:3306/product_catalog?zeroDateTimeBehavior=convertToNull&autoReconnect=true&failOverReadOnly=false&maxReconnects=10&useSSL=false' -e REDIS_HOST='10.133.208.136' -d --name optimus-product -p 9091:7070 -t optimus-product"

ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker run -e val1='am from docker Dev.env' -e MQ_HOST='10.133.208.135' -e MQ_PWD='tata' -e REDIS_HOST='10.133.208.136' -e APP_HOST='optimus-uat.tatacommunications.com' -d --name optimus-userservice -p 8085:8085 -t optimus-userservice"

ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker run -e val1='am from docker Dev.env' -e MQ_HOST='10.133.208.135' -e MQ_PWD='tata' -e  REDIS_HOST='10.133.208.136' -d --name optimus-notification -p 8090:8090 -t optimus-notification"

ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker run -e val1='am from docker Dev.env' -e MQ_HOST='10.133.208.135' -e MQ_PWD='tata'  -e DB_HOST='jdbc:mysql://10.133.208.140:3306/location_uat?zeroDateTimeBehavior=convertToNull&autoReconnect=true&failOverReadOnly=false&maxReconnects=10&useSSL=false' -e REDIS_HOST='10.133.208.136' -d --name optimus-location -p 7071:7071 -t optimus-location"


ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker run -e val1='am from docker Dev.env' -v "/docker/customer/optimus-files:/opt/optimus-files" -e MQ_HOST='10.133.208.135' -e MQ_PWD='tata' -e DB_HOST='jdbc:mysql://10.133.208.140:3306/customer_uat?zeroDateTimeBehavior=convertToNull&autoReconnect=true&failOverReadOnly=false&maxReconnects=10&useSSL=false' -e REDIS_HOST='10.133.208.136' -d --name optimus-customer -p 7073:7078 -t optimus-customer"

ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker run -e val1='am from docker Dev.env' -e MQ_HOST='10.133.208.135' -e MQ_PWD='tata'  -e DB_HOST='jdbc:mysql://10.133.208.140:3306/oms_uat?zeroDateTimeBehavior=convertToNull&autoReconnect=true&failOverReadOnly=false&maxReconnects=10&useSSL=false' -e REDIS_HOST='10.133.208.136' -d --name optimus-oms -p 7072:7072 -t optimus-oms"

ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker run -e val1='am from docker Dev.env' -e MQ_HOST='10.133.208.135' -e MQ_PWD='tata' -e REDIS_HOST='10.133.208.136' -e APP_HOST='optimus-uat.tatacommunications.com' -d --name optimus-service -p 7074:7074 -t optimus-service"

ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker run -e MQ_PWD='tata' -d --name optimus-wfe -p 7019:7019 -t optimus-wfe"
#ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker run -d --name optimus-soap -p 7075:7075 -t optimus-soap"

#ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker tag optimus-product:latest 10.133.208.127:5000/optimus-product"
#ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker push 10.133.208.127:5000/optimus-product"
#ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker tag optimus-auth:latest 10.133.208.127:5000/optimus-auth"
#ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker push 10.133.208.127:5000/optimus-auth"
rm -rf /var/jenkins_home/jobs/Dev.Optimus-UI/modules/*


