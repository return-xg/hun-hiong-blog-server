# hun-hiong-blog-server

个人技术博客系统后端服务

## 技术栈

- **框架**: Spring Boot 3.5.x
- **语言**: Java 21
- **安全**: Spring Security + JWT
- **ORM**: MyBatis-Plus 3.5.9
- **数据库**: PostgreSQL
- **缓存**: Redis
- **API文档**: SpringDoc OpenAPI (Swagger UI)
- **工具类**: Hutool 5.8.27
- **构建工具**: Maven

## 功能特性

- 用户认证与授权（JWT）
- 文章管理
- 分类管理
- 标签管理
- 评论系统
- Redis缓存支持

## 环境要求

- JDK 21+
- Maven 3.8+
- PostgreSQL 14+
- Redis 6+

## 快速开始

### 1. 克隆仓库

```bash
git clone https://github.com/return-xg/hun-hiong-blog-server.git
cd hun-hiong-blog-server
```

### 2. 数据库配置

创建 PostgreSQL 数据库：

```sql
CREATE DATABASE hun_hiong_blog;
```

### 3. 设置环境变量

```bash
# Linux/Mac
export DB_USERNAME="your_db_username"
export DB_PASSWORD="your_db_password"
export JWT_SECRET="your-strong-secret-key-at-least-32-characters"

# Windows PowerShell
$env:DB_USERNAME="your_db_username"
$env:DB_PASSWORD="your_db_password"
$env:JWT_SECRET="your-strong-secret-key-at-least-32-characters"
```

### 4. 运行项目

```bash
mvn spring-boot:run
```

或打包运行：

```bash
mvn clean package
java -jar target/hun-hiong-blog-server.jar
```

## 配置说明

支持通过环境变量或配置文件进行配置：

| 环境变量 | 默认值 | 说明 |
| :--- | :--- | :--- |
| `SERVER_PORT` | 8080 | 服务端口 |
| `DB_HOST` | localhost | 数据库主机 |
| `DB_PORT` | 5432 | 数据库端口 |
| `DB_NAME` | hun_hiong_blog | 数据库名称 |
| `DB_USERNAME` | `<your-db-username>` | 数据库用户名 |
| `DB_PASSWORD` | `<your-db-password>` | 数据库密码 |
| `REDIS_HOST` | localhost | Redis主机 |
| `REDIS_PORT` | 6379 | Redis端口 |
| `JWT_SECRET` | `<your-jwt-secret>` | JWT密钥（务必设置，≥32字符） |
| `SPRING_PROFILES_ACTIVE` | dev | 激活的配置文件 |

## API 文档

启动项目后访问 Swagger UI：

- **Swagger UI**: http://localhost:8080/swagger-ui.html

## 项目结构

```
src/main/java/com/hunhiong/blog/
├── BlogApplication.java          # 启动类
├── ai/                           # AI相关模块
├── common/                       # 通用模块
│   ├── constants/                # 常量定义
│   ├── enums/                    # 枚举类
│   ├── exception/                # 异常处理
│   └── result/                   # 返回结果封装
├── config/                       # 配置类
├── controller/                   # 控制器
├── converter/                    # 数据转换
├── dto/                          # 数据传输对象
├── entity/                       # 实体类
├── mapper/                       # 数据访问层
├── security/                     # 安全模块
├── service/                      # 业务逻辑层
├── task/                         # 定时任务
├── utils/                        # 工具类
└── vo/                           # 视图对象
```

## License

MIT License