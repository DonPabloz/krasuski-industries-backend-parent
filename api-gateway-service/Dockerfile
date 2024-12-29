ARG JDK_LIB
FROM ${JDK_LIB}
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8124

ENTRYPOINT ["java","-jar","/app.jar"]