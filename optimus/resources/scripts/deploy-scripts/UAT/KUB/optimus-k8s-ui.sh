## Export proxy
#export http_proxy="http://10.133.19.36:80"
#export https_proxy="http://10.133.19.36:80"

## clone & mvn build application
cd /docker/script/workspace/k8s
sudo git clone -b Hydrogen http://root:Tata#123@10.133.208.133/gitlab/root/optimus-ui.git
sudo chmod -R og+rw optimus-ui
sudo chown -R pmurugan:pmurugan optimus-ui
cd optimus-ui/
#cp -a ../node_modules .
sudo npm config set strict-ssl false
#sudo npm config set proxy http://10.133.19.36:80
#sudo npm config set https-proxy http://10.133.19.36:80
env|grep proxy
sudo npm install --unsafe-perm 
#sudo ng build --base-href="./"
sudo ng build  --base-href="/optimus/"
sudo chown -R pmurugan:pmurugan /docker/script/workspace/k8s/optimus-ui
sudo chmod -R go+rx /docker/script/workspace/k8s/optimus-ui/dist/assets/images


## copy files to /opt/...
sudo cp -r /docker/script/workspace/k8s/optimus-ui/dist/. /opt/optimus-ui/public-html/optimus

unset http_proxy
unset https_proxy

## Build docker images
sudo docker build /opt/optimus-ui/ -t optimus-ui
sudo docker tag optimus-ui:latest 10.133.208.115:32550/optimus-ui:latest
sudo docker push 10.133.208.115:32550/optimus-ui:latest

## Remove containers
kubectl -n optimus delete deployment optimus-ui
sleep 30

## Run Containers
kubectl -n optimus run optimus-ui --image=10.133.208.115:32550/optimus-ui:latest --port=80 --replicas=1

## Clean workspace application directory
cd ..
rm -rf optimus-ui
