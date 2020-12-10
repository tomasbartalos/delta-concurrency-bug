FROM docker.io/bitnami/spark:3-debian-10

USER root

COPY resources/log4j.properties /opt/bitnami/spark/conf/
COPY resources/*.jar /usr/spark-app/
COPY resources/run.sh /usr/spark-scripts/

ENTRYPOINT ["/usr/spark-scripts/run.sh"]