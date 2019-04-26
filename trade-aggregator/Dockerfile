FROM scholzj/centos-java-base:latest

ARG version=latest
ENV VERSION ${version}

COPY ./scripts/ /bin

ADD target/trade-aggregator.jar /

CMD ["/bin/run.sh", "/trade-aggregator.jar"]