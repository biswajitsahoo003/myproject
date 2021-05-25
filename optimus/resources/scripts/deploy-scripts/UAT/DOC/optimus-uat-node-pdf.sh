## Export proxy
export http_proxy="http://10.133.12.181:80"
export https_proxy="http://10.133.12.181:80"

## clone & mvn build application
cd /docker/script/workspace/
sudo git clone -b dev-pdfgenerator http://root:Tata#123@10.133.208.133/gitlab/root/optimus-pdfgenerator.git
sudo chmod -R og+rw optimus-pdfgenerator
sudo chown -R pmurugan:pmurugan optimus-pdfgenerator
cd optimus-pdfgenerator/
sudo npm config set strict-ssl false
sudo npm config set proxy http://10.133.12.181:80
sudo npm config set https-proxy http://10.133.12.181:80
sudo npm install --unsafe-perm -loglevel silent

sudo chown -R pmurugan:pmurugan /docker/script/workspace/optimus-pdfgenerator/


sudo cp -r /docker/script/workspace/optimus-pdfgenerator/. /opt/nodejs_pdf/

## Build docker images
sudo docker build /opt/nodejs_pdf/ -t node_pdf_app

## Remove containers
sudo docker kill node_pdf_app
sudo docker rm node_pdf_app

## Run Containers
sudo docker run -d --name node_pdf_app -p 8000:8000 -t node_pdf_app

## Clean workspace application directory
cd ..
rm -rf optimus-pdfgenerator/
