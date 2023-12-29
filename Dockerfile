# Stage 1: Build Angular app
FROM node:14 AS angular-builder

WORKDIR /app

COPY ./organizerClient/package.json .
COPY ./organizerClient/package-lock.json .

RUN npm ci

COPY ./organizerClient .

RUN npm run build -- --prod

# Stage 2: Build Spring Boot API with Angular app
FROM adoptopenjdk/openjdk11:alpine as java-angular-app

WORKDIR /app

COPY ./OrganizerBackend/mvnw .
COPY ./OrganizerBackend/.mvn/wrapper ./.mvn/wrapper
COPY ./OrganizerBackend/pom.xml .

RUN ./mvnw dependency:go-offline

COPY ./OrganizerBackend .
COPY --from=angular-builder /app/dist/prod ./target/classes/static

RUN ./mvnw package -f ./pom.xml -DskipTests

# Stage 3: Final application image
FROM adoptopenjdk/openjdk11:alpine

WORKDIR /app

COPY ./OrganizerBackend/resources/ffmpeg ./resources/ffmpeg
COPY ./OrganizerBackend/resources/ffprobe ./resources/ffprobe

COPY --from=java-angular-app /app/target/OrganizerBackend*.jar ./OrganizerBackend.jar
EXPOSE 8080

CMD ["java", "-jar", "OrganizerBackend.jar"]