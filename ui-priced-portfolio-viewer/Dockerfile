FROM scholzj/centos-java-base:latest

ARG version=latest
ENV VERSION ${version}

COPY ./scripts/ /bin

ADD target/ui-priced-portfolio-viewer.jar /

CMD ["/bin/run.sh", "/ui-priced-portfolio-viewer.jar"]