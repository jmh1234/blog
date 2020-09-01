FROM java:openjdk-8u111-alpine

RUN mkdir /app

WORKDIR /app

COPY target/spring-aop-redis-mysql-0.0.1.jar /app

EXPOSE 8080

CMD ["java","-jar","spring-aop-redis-mysql-0.0.1.jar"]
