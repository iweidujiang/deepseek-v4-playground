# DeepSeek V4 Playground

<div align="center">

**基于 Spring AI 的 DeepSeek V4 大模型实战项目集**

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/java-21-orange.svg)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/spring%20boot-3.5.14-green.svg)](https://spring.io/projects/spring-boot)
[![Spring AI](https://img.shields.io/badge/spring%20ai-1.0.5-brightgreen.svg)](https://spring.io/projects/spring-ai)

📖 **作者：苏渡苇** | 
🔗 [GitHub主页](https://github.com/iweidujiang) | 
📝 [CSDN主页](https://blog.csdn.net/iweidujiang) | 
💬 公众号：**苏渡苇**

</div>

---

## 📋 项目简介

本项目是一个基于 **DeepSeek V4** 大模型的实战项目集合，通过三个独立的模块分别展示了 DeepSeek V4 在以下三个核心领域的应用能力：

1. **智能代码分析** - 自动化项目理解与架构分析
2. **AI 编程助手** - 智能代码生成、审查与单元测试
3. **SQL 优化引擎** - 自然语言转 SQL 与智能优化

所有模块均基于 **Spring AI** 框架开发，采用 **Spring Boot 3.x** + **Java 21** 技术栈，充分利用 DeepSeek V4 的强大能力，为企业提供实用的 AI 增强解决方案。

---

## 🚀 核心特性

### ✨ 统一技术栈
- **后端框架**: Spring Boot 3.5.14
- **AI 集成**: Spring AI 1.0.5 (OpenAI 兼容接口)
- **JDK 版本**: Java 21
- **构建工具**: Maven
- **依赖管理**: Lombok, Jackson, Druid 等

### 🎯 DeepSeek V4 三大能力展示

| 模块 | 能力维度 | 核心功能 | 应用场景 |
|------|---------|---------|---------|
| deepseek-analyzer | 代码理解与分析 | 项目结构解析、架构评估、代码质量报告 | 技术审计、遗留系统分析 |
| deepseek-coding-assistant | 代码生成与优化 | 智能编码、代码审查、单元测试生成 | 日常开发、Code Review |
| deepseek-sql-optimizer | SQL 生成与优化 | NL2SQL、执行计划分析、性能优化建议 | 数据库开发、性能调优 |

---

## 📦 模块详解

### 1️⃣ deepseek-analyzer - 智能项目分析器

**能力定位**: 利用 DeepSeek V4 的代码理解能力，自动化分析项目结构与代码质量。

#### 核心功能
- 🔍 **项目扫描**: 递归扫描指定目录，收集源代码文件
- 📊 **架构分析**: 识别项目技术栈、依赖关系、模块划分
- 📈 **质量报告**: 生成代码复杂度、规范遵循度、潜在问题报告
- 🎨 **双模式支持**: 
  - `quick` 模式: 快速概览分析
  - `deep` 模式: 深度详细分析

#### 技术亮点
- 使用 `jtokkit` 进行 Token 计数与成本控制
- 支持多模型配置（主模型 + 降级模型）
- 内置重试机制与错误处理

#### API 示例
```bash
POST http://localhost:8080/api/analyze
Content-Type: application/json

{
  "projectPath": "/path/to/your/project",
  "mode": "deep"
}
```

#### 配置文件
```yaml
spring:
  ai:
    openai:
      api-key: ${DEEPSEEK_API_KEY}
      base-url: https://api.deepseek.com
      chat:
        options:
          model: deepseek-v4-pro
          temperature: 0.1
          max-tokens: 8192

deepseek:
  models:
    primary: deepseek-v4-pro
    fallback: deepseek-v4-flash
  retry:
    max-attempts: 2
```

---

### 2️⃣ deepseek-coding-assistant - AI 编程助手

**能力定位**: 基于 DeepSeek V4 的代码生成能力，打造企业级智能编程助手。

#### 核心功能
- 💻 **智能代码生成**: 根据需求描述自动生成高质量代码
- 🔎 **代码审查**: 自动检测代码缺陷、安全漏洞、性能问题
- 🧪 **单元测试生成**: 为现有代码自动生成完整的单元测试
- 🌐 **多语言支持**: Java, Python, JavaScript, Go 等主流语言
- ⚡ **限流与缓存**: 基于 Redis 的 API 限流与结果缓存

#### 技术亮点
- 支持多种编程框架（Spring Boot, Django, React 等）
- 内置 System Prompt 工程，确保代码规范性
- Docker 容器化部署支持
- 完善的监控指标（Actuator + Micrometer）

#### API 示例

**代码生成**
```bash
POST http://localhost:8080/api/code/generate
Content-Type: application/json

{
  "language": "java",
  "framework": "spring-boot",
  "requirement": "创建一个用户注册接口，包含参数校验和异常处理",
  "needExplanation": true
}
```

**代码审查**
```bash
POST http://localhost:8080/api/code/review
Content-Type: application/json

{
  "code": "public void process(List<String> items) { ... }",
  "language": "java"
}
```

**单元测试生成**
```bash
POST http://localhost:8080/api/test/generate
Content-Type: application/json

{
  "code": "public class UserService { ... }",
  "language": "java",
  "testFramework": "junit5"
}
```

#### 配置文件
```yaml
spring:
  ai:
    openai:
      api-key: ${DEEPSEEK_API_KEY}
      base-url: https://api.deepseek.com
      chat:
        options:
          model: deepseek-v4-pro
          temperature: 0.2
          max-tokens: 8192
  
  data:
    redis:
      host: localhost
      port: 6379
      password: devRed1s

coding-assistant:
  api:
    rate-limit: 30
    timeout: 60
  cache:
    ttl: 3600
```

#### Docker 部署
```bash
# 构建镜像
docker build -t deepseek-coding-assistant:latest .

# 运行容器
docker run -d \
  -p 8080:8080 \
  -e DEEPSEEK_API_KEY=your_api_key \
  -e SPRING_PROFILES_ACTIVE=docker \
  deepseek-coding-assistant:latest
```

---

### 3️⃣ deepseek-sql-optimizer - SQL 优化引擎

**能力定位**: 结合 DeepSeek V4 的逻辑推理能力，实现自然语言到 SQL 的智能转换与优化。

#### 核心功能
- 🗣️ **NL2SQL**: 自然语言查询转换为可执行 SQL 语句
- 🔧 **智能优化**: 分析 SQL 执行计划，提供优化建议
- 📊 **元数据感知**: 自动读取数据库表结构，生成准确 SQL
- 🎯 **多方言支持**: 当前支持 MySQL，可扩展 PostgreSQL、Oracle
- 📝 **索引建议**: 基于查询模式推荐索引创建

#### 技术亮点
- 集成 MyBatis Plus 与 Druid 连接池
- 支持数据库元数据自动发现
- 内置 SQL 语法校验与格式化
- 可选优化模式（生成 + 优化两步流程）

#### API 示例
```bash
POST http://localhost:8080/api/sql/generate
Content-Type: application/json

{
  "naturalLanguage": "查询最近30天内订单金额超过1000的用户，按金额降序排列",
  "dialect": "mysql",
  "model": "deepseek-v4-pro",
  "enableOptimize": true
}
```

#### 响应示例
```json
{
  "originalSql": "SELECT * FROM users WHERE ...",
  "optimizedSql": "SELECT u.* FROM users u INNER JOIN ...",
  "explanation": "优化说明：使用索引覆盖、避免全表扫描...",
  "executionPlan": "EXPLAIN 结果分析",
  "indexSuggestions": ["建议在 orders.user_id 上创建索引"],
  "error": null
}
```

#### 配置文件
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/demo-sql?useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: 111111
    driver-class-name: com.mysql.cj.jdbc.Driver
    type: com.alibaba.druid.pool.DruidDataSource
  
  ai:
    openai:
      api-key: ${DEEPSEEK_API_KEY}
      base-url: https://api.deepseek.com
      chat:
        options:
          model: deepseek-v4-pro
          temperature: 0.2
          max-tokens: 4096

deepseek:
  sql:
    dialect: mysql
  retry:
    max-attempts: 2
```

---

## 🛠️ 快速开始

### 前置要求
- JDK 21+
- Maven 3.8+
- DeepSeek API Key ([申请地址](https://platform.deepseek.com))
- MySQL 8.0+
- Redis 7.0+

### 环境配置

1. **获取 DeepSeek API Key**
   ```bash
   # 访问 https://platform.deepseek.com 注册并获取 API Key
   export DEEPSEEK_API_KEY=sk-your-api-key-here
   ```

2. **克隆项目**
   ```bash
   git clone https://github.com/iweidujiang/deepseek-v4-playground.git
   cd deepseek-v4-playground
   ```

### 模块启动

#### 启动 deepseek-analyzer
```bash
cd deepseek-analyzer
mvn clean package
java -jar target/deepseek-analyzer-1.0.0.jar

# 访问 Web 界面
open http://localhost:8080
```

#### 启动 deepseek-coding-assistant
```bash
# 启动 Redis (可选，用于限流和缓存)
docker run -d -p 6379:6379 redis:7 --requirepass devRed1s

cd deepseek-coding-assistant
mvn clean package
java -jar target/deepseek-coding-assistant-1.0.0.jar

# 访问 Web 界面
open http://localhost:8080
```

#### 启动 deepseek-sql-optimizer
```bash
# 初始化数据库
mysql -u root -p < deepseek-sql-optimizer/src/main/resources/db/schema.sql

cd deepseek-sql-optimizer
mvn clean package
java -jar target/deepseek-sql-optimizer-1.0.0.jar

# 访问 Web 界面
open http://localhost:8080
```
Tips: 可以为每个项目设置单独的端口，如：8081、8082、8083，以免端口冲突。

---

## 🏗️ 项目结构

```
deepseek-v4-playground/
├── deepseek-analyzer/              # 智能项目分析器
│   ├── src/main/java/
│   │   └── io/github/iweidujiang/dsv4/analyzer/
│   │       ├── config/             # 配置类
│   │       ├── controller/         # REST API
│   │       ├── dto/                # 数据传输对象
│   │       ├── entity/             # 实体类
│   │       └── service/            # 业务逻辑
│   ├── src/main/resources/
│   │   ├── static/index.html       # Web 界面
│   │   └── application.yml         # 配置文件
│   └── pom.xml
│
├── deepseek-coding-assistant/      # AI 编程助手
│   ├── src/main/java/
│   │   └── io/github/iweidujiang/dsv4/
│   │       ├── commom/             # 通用组件
│   │       ├── config/             # 配置类
│   │       ├── controller/         # REST API
│   │       ├── dto/                # 数据传输对象
│   │       ├── function/           # 工具调用
│   │       └── service/            # 业务逻辑
│   ├── src/main/resources/
│   │   ├── static/index.html       # Web 界面
│   │   ├── application.yaml        # 配置文件
│   │   └── application-docker.yml  # Docker 配置
│   ├── Dockerfile                  # Docker 镜像
│   ├── docker-compose.yml          # Docker Compose
│   └── pom.xml
│
├── deepseek-sql-optimizer/         # SQL 优化引擎
│   ├── src/main/java/
│   │   └── io/github/iweidujiang/dsv4/sqloptimizer/
│   │       ├── config/             # 配置类
│   │       ├── controller/         # REST API
│   │       ├── dto/                # 数据传输对象
│   │       ├── service/            # 业务逻辑
│   │       └── util/               # 工具类
│   ├── src/main/resources/
│   │   ├── db/schema.sql           # 数据库脚本
│   │   ├── static/index.html       # Web 界面
│   │   └── application.yml         # 配置文件
│   └── pom.xml
│
├── .gitignore
└── LICENSE
```

---

## 📊 技术架构图

<img width="620" height="992" alt="局部截取_20260430_151955" src="https://github.com/user-attachments/assets/4753314e-63b7-49eb-935a-9cd0f73793a6" />


---

## 🔧 高级配置

### 环境变量说明

| 变量名 | 说明 | 默认值 | 必填 |
|--------|------|--------|------|
| `DEEPSEEK_API_KEY` | DeepSeek API 密钥 | - | ✅ |
| `DEEPSEEK_MODEL` | 默认模型 | `deepseek-v4-pro` | ❌ |
| `DEEPSEEK_MODEL_PRIMARY` | 主模型 | `deepseek-v4-pro` | ❌ |
| `DEEPSEEK_MODEL_FALLBACK` | 降级模型 | `deepseek-v4-flash` | ❌ |
| `SPRING_PROFILES_ACTIVE` | Spring Profile | `default` | ❌ |

### 模型选择建议

| 场景 | 推荐模型 | 原因 |
|------|---------|------|
| 复杂代码分析 | `deepseek-v4-pro` | 更强的推理能力 |
| 快速代码生成 | `deepseek-v4-flash` | 更快的响应速度 |
| SQL 优化 | `deepseek-v4-pro` | 需要深度逻辑分析 |
| 批量处理 | `deepseek-v4-flash` | 成本更低 |

### 性能调优

**Temperature 设置**
- 代码生成: `0.1-0.3` (确定性高)
- 创意任务: `0.5-0.7` (多样性强)
- SQL 优化: `0.2-0.4` (平衡准确性与创新)

**Max Tokens 设置**
- 简单任务: `2048-4096`
- 复杂分析: `8192`
- 超长上下文: 根据实际需求调整

---

## 🧪 测试与验证


### API 测试
每个模块都提供了 Web 界面 (`http://localhost:8080`)，可以直接在浏览器中测试功能。

也可以使用 curl 或 Postman 进行 API 测试：
```bash
# 示例：测试代码生成
curl -X POST http://localhost:8080/api/code/generate \
  -H "Content-Type: application/json" \
  -d '{
    "language": "java",
    "framework": "spring-boot",
    "requirement": "创建一个 RESTful API 接口",
    "needExplanation": false
  }'
```

---

## 📈 监控与日志

### Actuator 端点 (coding-assistant)
```bash
# 健康检查
curl http://localhost:8080/actuator/health

# 指标监控
curl http://localhost:8080/actuator/metrics

# 应用信息
curl http://localhost:8080/actuator/info
```


---

## 🤝 贡献指南

欢迎提交 Issue 和 Pull Request！

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

---

## 📄 许可证

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件

---

## 🙏 致谢

- [DeepSeek](https://deepseek.com) - 提供强大的大模型能力
- [Spring AI](https://spring.io/projects/spring-ai) - 简化 AI 集成
- [Spring Boot](https://spring.io/projects/spring-boot) - 优秀的后端框架

---

## 📮 联系方式

**作者**: 苏渡苇

- 🌐 **GitHub**: [https://github.com/iweidujiang](https://github.com/iweidujiang)
- 📝 **CSDN**: [https://blog.csdn.net/iweidujiang](https://blog.csdn.net/weixin_42364968)
- 💬 **微信公众号**: 苏渡苇

如有问题或建议，欢迎通过以下方式联系：
- 提交 GitHub Issue
- CSDN 私信
- 关注公众号留言

---

<div align="center">

**⭐ 如果这个项目对您有帮助，请给个 Star！**

Made with ❤️ by 苏渡苇

</div>
