FROM java:8
# Monitoring
RUN curl -x  10.133.12.181:80 -L -O https://artifacts.elastic.co/downloads/beats/filebeat/filebeat-6.2.4-amd64.deb
RUN dpkg -i filebeat-6.2.4-amd64.deb
RUN rm filebeat-6.2.4-amd64.deb

COPY filebeat.yml /etc/filebeat/filebeat.yml

VOLUME /tmp
COPY optimus-notification-1.0.jar optimus-notification.jar
RUN bash -c 'touch /optimus-notification.jar'
ENTRYPOINT command service filebeat start;java -Djava.security.egd=file:/dev/./urandom -jar /optimus-notification.jar
#ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/optimus-notification.jar"]
