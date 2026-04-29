# 第一阶段：构建
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app

# 复制依赖定义文件，利用 Docker 缓存
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 复制源代码并构建
COPY src ./src
RUN mvn clean package -DskipTests -B

# 第二阶段：运行
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# 创建非 root 用户运行应用（安全）
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# 复制 JAR 包并重命名为 app.jar
COPY --from=builder /app/target/*.jar app.jar

# 暴露端口
EXPOSE 8008

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# 启动命令（使用环境变量自动配置）
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]