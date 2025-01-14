# 기본 이미지를 지정합니다. (OpenJDK 17 버전 사용)
FROM openjdk:17-jdk-slim

# JAR 파일을 컨테이너의 /app 폴더로 복사
COPY build/libs/Seoul-0.0.1-SNAPSHOT.jar /app/Souel-app.jar

# 컨테이너가 시작될 때 실행할 명령
CMD ["java", "-jar", "/app/my-app.jar"]