# Add nca node
FROM eclipse-temurin:17-jre-alpine AS ncanode
WORKDIR /app
EXPOSE 14579
RUN sudo apt install docker.io
RUN sudo systemctl enable docker
RUN docker login --username kazakuper@mail.ru --password adialtair1250
RUN docker volume create ncanode_cache
RUN docker pull malikzh/ncanode
RUN docker run -p 14579:14579 -v ncanode_cache:/app/cache -d malikzh/ncanode

FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY . /app/
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar /app/app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","app.jar"]

RUN mkdir files


VOLUME ["/app/cache"]