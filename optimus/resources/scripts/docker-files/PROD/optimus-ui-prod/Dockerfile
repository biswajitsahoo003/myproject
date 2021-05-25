FROM solsson/httpd-openidc
#COPY httpd.conf_prod /usr/local/apache2/conf/httpd.conf
COPY httpd.conf_uat /usr/local/apache2/conf/httpd.conf
COPY ./public-html/ /usr/local/apache2/htdocs/
RUN chmod -R go+rx /usr/local/apache2/htdocs/optimus
