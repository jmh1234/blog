FROM java:openjdk-8u111-alpine

RUN mkdir /app

WORKDIR /app

COPY target/blog-0.0.1.jar /app

EXPOSE 8080

CMD ["java","-jar","blog-0.0.1.jar"]
