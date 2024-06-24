FROM maven:3.9.7-eclipse-temurin-17 as resolve-dependencies

RUN --mount=type=bind,source=pom.xml,target=pom.xml \
    --mount=type=cache,target=/root/.m2 \
    mvn dependency:go-offline

FROM resolve-dependencies as build

WORKDIR /usr/local/build

COPY ./src src/
RUN --mount=type=bind,source=pom.xml,target=pom.xml \
    --mount=type=cache,target=/root/.m2 \
    mvn package -DskipTests \
    && mv target/*-executable.jar bot.jar

FROM eclipse-temurin:17-jre-jammy AS execution

ARG UID=5005
ARG USERNAME=service-user

WORKDIR /opt/tg-bots/compliment-bot

COPY --from=build /usr/local/build/bot.jar .

RUN adduser \
    --disabled-password \
    --gecos "" \
    --home "/nonexistent" \
    --shell "/sbin/nologin" \
    --no-create-home \
    --uid "${UID}" \
    $USERNAME
USER $USERNAME

EXPOSE 80

ENTRYPOINT [ "java", "-jar", "bot.jar" ]
