FROM openjdk:8
EXPOSE 8084
ADD target/project-one.jar project-one.jar
ENTRYPOINT [ "java" , "-jar" , "/project-one.jar"]