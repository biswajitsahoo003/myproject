## Export proxy
#export http_proxy="http://10.133.12.181:80"
#export https_proxy="http://10.133.12.181:80"

## clone & mvn build application
cd /docker/script/workspace/

sudo git clone -b Hydrogen http://root:Tata#123@10.133.208.133/gitlab/root/optimus-ui.git

#sudo git clone -b Kishore.Murugan2 http://root:Tata#123@10.133.208.133/gitlab/root/optimus-ui.git

sudo chmod -R og+rw optimus-ui
sudo chown -R pmurugan:pmurugan optimus-ui
cd optimus-ui/
sudo npm config set strict-ssl false
#sudo npm config set proxy http://10.133.12.181:80
#sudo npm config set https-proxy http://10.133.12.181:80
#cp -a ../node_modules .
sudo npm install --unsafe-perm --verbose
#sudo npm install --unsafe-perm -loglevel silent

sudo ng build --prod --base-href="/optimus/"
sudo chown -R pmurugan:pmurugan /docker/script/workspace/optimus-ui
sudo chmod -R go+rx /docker/script/workspace/optimus-ui/dist/assets/images
sudo chmod -R go+rx /docker/script/workspace/optimus-ui/dist


## copy files to /opt/...
sudo cp -r /docker/script/workspace/optimus-ui/dist/. /opt/optimus-ui/public-html/optimus

## Build docker images
sudo docker build /opt/optimus-ui/ -t optimus-ui

## Remove containers
sudo docker kill optimus-ui
sudo docker rm optimus-ui

## Run Containers
sudo docker run -d --name optimus-ui -p 8095:80 -t optimus-ui

## Clean workspace application directory
cd ..
rm -rf optimus-ui
