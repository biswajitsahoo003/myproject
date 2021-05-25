## Export proxy
export http_proxy="http://10.133.12.181:80"
export https_proxy="http://10.133.12.181:80"

## clone & mvn build application
cd /docker/script/workspace/k8s
sudo git clone -b Hydrogen http://root:Tata#123@10.133.208.133/gitlab/root/optimus-ui.git
sudo chmod -R og+rw optimus-ui
sudo chown -R pmurugan:pmurugan optimus-ui
cd optimus-ui/
sudo npm config set strict-ssl false
sudo npm config set proxy http://10.133.12.181:80
sudo npm config set https-proxy http://10.133.12.181:80
sudo npm install --unsafe-perm -loglevel silent
#sudo ng build --base-href="./"
sudo ng build --prod --base-href="/optimus/"
sudo chown -R pmurugan:pmurugan /docker/script/workspace/k8s/optimus-ui
sudo chmod -R go+rx /docker/script/workspace/k8s/optimus-ui/dist/assets/images


## copy files to /opt/...
sudo cp -r /docker/script/workspace/k8s/optimus-ui/dist/. /opt/optimus-ui-prod/public-html/optimus

unset http_proxy
unset https_proxy

## Build docker images
sudo docker build  /opt/optimus-ui-prod/ -t optimus-ui
sudo docker tag optimus-ui:latest 10.132.12.239:32550/optimus-ui:latest
sudo docker push 10.132.12.239:32550/optimus-ui:latest

#Env variables
HTTP_PROXY=121.244.253.5:8080
HTTPS_PROXY=121.244.253.5:8080

## Remove containers
kubectl --kubeconfig=/docker/script/workspace/prod-kubeconfig -n optimus delete deployment optimus-ui
sleep 30

## Run Containers
kubectl --kubeconfig=/docker/script/workspace/prod-kubeconfig -n optimus run optimus-ui --image=10.132.12.239:32550/optimus-ui:latest --port=80 --replicas=1 --env="http_proxy="$HTTP_PROXY  --env="https_proxy="$HTTPS_PROXY

## Clean workspace application directory
cd ..
rm -rf optimus-ui
