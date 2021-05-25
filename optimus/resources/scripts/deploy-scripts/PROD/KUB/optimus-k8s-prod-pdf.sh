## Export proxy
export http_proxy="http://10.133.12.181:80"
export https_proxy="http://10.133.12.181:80"

## clone & mvn build application
cd /docker/script/workspace/k8s
sudo git clone -b dev-pdfgenerator http://root:Tata#123@10.133.208.133/gitlab/root/optimus-pdfgenerator.git
sudo chmod -R og+rw optimus-pdfgenerator
sudo chown -R pmurugan:pmurugan optimus-pdfgenerator
cd optimus-pdfgenerator/
sudo chown -R pmurugan:pmurugan /docker/script/workspace/k8s/optimus-pdfgenerator
sudo chmod -R go+rx /docker/script/workspace/k8s/optimus-pdfgenerator


## copy files to /opt/...
sudo cp -r /docker/script/workspace/k8s/optimus-pdfgenerator/. /opt/pdf/

unset http_proxy
unset https_proxy

## Build docker images
sudo docker build  /opt/pdf/ -t optimus-pdf
sudo docker tag optimus-pdf:latest 10.132.12.239:32550/optimus-pdf:latest
sudo docker push 10.132.12.239:32550/optimus-pdf:latest

#Env variables
HTTP_PROXY=http://121.244.253.5:8080
HTTPS_PROXY=http://121.244.253.5:8080
#HTTP_PROXY=http://121.244.254.154:80
#HTTPS_PROXY=http://121.244.254.154:80

## Remove containers
kubectl --kubeconfig=/docker/script/workspace/prod-kubeconfig -n optimus delete deployment optimus-pdf
sleep 30

## Run Containers
kubectl --kubeconfig=/docker/script/workspace/prod-kubeconfig -n optimus run optimus-pdf --image=10.132.12.239:32550/optimus-pdf:latest --port=8000 --replicas=1 --env="http_proxy="$HTTP_PROXY  --env="https_proxy="$HTTPS_PROXY

## Clean workspace application directory
cd ..
rm -rf optimus-pdfgenerator
