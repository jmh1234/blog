FROM java:openjdk-8u111-alpine

RUN curl -fsSLO https://get.docker.com/builds/Linux/x86_64/docker-17.04.0-ce.tgz \
  && tar xzvf docker-17.04.0-ce.tgz \
  && mv docker/docker /usr/local/bin \
  && rm -r docker docker-17.04.0-ce.tgz

RUN mkdir /app

WORKDIR /app

COPY target/blog-0.0.1.jar /app

EXPOSE 8080

CMD ["java","-jar","blog-0.0.1.jar"]
