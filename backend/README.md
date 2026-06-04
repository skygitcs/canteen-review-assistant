# THU Canteen Backend

这是“清华食堂点评助手”的后端服务，负责用户登录注册、食堂和菜品数据、评价、收藏、补充菜品信息、公告和管理审核等功能。

## 1. 技术栈

- Java 17
- Spring Boot 3.3
- Spring Security + JWT
- MyBatis-Plus
- MySQL 8
- springdoc-openapi / Swagger UI

## 2. 需要安装什么

后端本地运行需要：

| 软件 | 用途 | 是否必须 |
| --- | --- | --- |
| JDK 17 | 运行 Java 后端 | 必须 |
| Maven | 下载依赖、启动 Spring Boot | 必须 |
| MySQL 8 | 保存用户、食堂、菜品、评价等数据 | 必须 |
| SQL 工具 | 执行建表和种子数据脚本 | 推荐 |

SQL 工具可以使用 MySQL 自带命令行，也可以使用 MySQL Workbench、DataGrip、Navicat。

## 3. Windows 安装教程

### 3.1 安装 JDK 17

如果已经能看到 Java 17，可以跳过。

检查命令：

```powershell
java -version
```

期望看到类似：

```text
openjdk version "17.x.x"
```

如果没有安装，可以安装 Temurin JDK 17：

```text
https://adoptium.net/temurin/releases/?version=17
```

安装后如果命令行仍然找不到 Java，需要检查环境变量：

- `JAVA_HOME` 指向 JDK 安装目录，例如 `C:\Program Files\Eclipse Adoptium\jdk-17...`
- `Path` 中包含 `%JAVA_HOME%\bin`

### 3.2 安装 Maven

检查命令：

```powershell
mvn -version
```

如果提示找不到 `mvn`，需要安装 Maven。

下载地址：

```text
https://maven.apache.org/download.cgi
```

选择 `Binary zip archive`，例如：

```text
apache-maven-3.9.x-bin.zip
```

解压到一个固定位置，例如：

```text
C:\dev\apache-maven-3.9.x
```

配置环境变量：

- 新增 `MAVEN_HOME`：`C:\dev\apache-maven-3.9.x`
- 在 `Path` 中新增：`%MAVEN_HOME%\bin`

重新打开 PowerShell，再执行：

```powershell
mvn -version
```

能看到 Maven 版本就说明安装成功。

### 3.3 安装 MySQL 8

下载地址：

```text
https://dev.mysql.com/downloads/installer/
```

Windows 建议安装：

```text
MySQL Installer for Windows
```

安装时建议选择：

- MySQL Server 8.x
- MySQL Workbench
- MySQL Shell 可选

安装过程中会设置 `root` 用户密码。请记住这个密码，后面要写进 `application.yml`。

检查 MySQL 是否能用：

```powershell
mysql --version
```

如果命令行找不到 `mysql`，可以直接用 MySQL Workbench 执行 SQL，不一定非要配置命令行。

### 3.4 确认 MySQL 服务已启动

可以在 Windows 服务里找：

```text
MySQL80
```

确保它是“正在运行”。

也可以用命令登录测试：

```powershell
mysql -u root -p
```

输入安装时设置的 root 密码。能进入 MySQL 提示符就说明可用。

## 4. 本地运行后端

### 4.1 修改数据库配置

打开：

```text
src/main/resources/application.yml
```

默认配置是：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/thu_canteen?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: root
    password: root
```

如果你的 MySQL 密码不是 `root`，把 `password` 改成自己的密码。

### 4.2 初始化数据库

进入 MySQL 后执行：

```sql
SOURCE E:/canteen-review/canteen-review-assistant/backend/src/main/resources/db/schema.sql;
SOURCE E:/canteen-review/canteen-review-assistant/backend/src/main/resources/db/seed.sql;
```

如果项目路径不同，把 `SOURCE` 后面的路径改成自己电脑上的实际路径。

`schema.sql` 会创建数据库和表，`seed.sql` 会导入演示数据。

### 4.3 启动服务

在项目后端目录执行：

```powershell
cd E:\canteen-review\canteen-review-assistant\backend
mvn spring-boot:run
```

启动成功后访问：

```text
http://localhost:8080/swagger-ui.html
```

机器可读的 OpenAPI 文档地址：

```text
http://localhost:8080/v3/api-docs
```

## 5. Android 真机联调

Android 真机不能访问后端电脑上的 `localhost`。

后端电脑和手机需要在同一个网络下。后端电脑执行：

```powershell
ipconfig
```

找到局域网 IPv4 地址，例如：

```text
192.168.1.23
```

Android 端 Base URL 写成：

```text
http://192.168.1.23:8080/
```

如果手机无法访问，优先检查：

- 手机和电脑是否在同一个 Wi-Fi 或热点下
- Windows 防火墙是否允许 8080 端口
- 后端是否启动成功
- Android 端 Base URL 是否写成电脑局域网 IP，而不是 `localhost`

## 6. 演示账号

导入 `seed.sql` 后有三个演示账号，密码均为：

```text
password
```

| 用户名 | 角色 | 说明 |
| --- | --- | --- |
| demo | USER | 普通演示用户 |
| reviewer | USER | 带有部分评价和收藏数据的用户 |
| admin | ADMIN | 管理员演示用户 |

也可以通过注册接口创建新用户：

```http
POST /api/auth/register
```

## 7. 核心接口

给前端联调用的接口文档在：

```text
../docs/frontend-api-contract.md
```

后端启动后可以使用 Swagger 页面测试接口：

```text
http://localhost:8080/swagger-ui.html
```

核心接口包括：

- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/announcements`
- `GET /api/canteens`
- `GET /api/canteens/{id}`
- `GET /api/dishes`
- `GET /api/dishes/{id}`
- `GET /api/dishes/recommendations`
- `POST /api/dishes/{dishId}/reviews`
- `GET /api/users/me/favorites`
- `POST /api/users/me/favorites`
- `DELETE /api/users/me/favorites/{dishId}`
- `POST /api/dishes/submissions`

## 8. 统一响应格式

成功：

```json
{
  "code": 0,
  "message": "ok",
  "data": {}
}
```

失败：

```json
{
  "code": 400,
  "message": "错误说明",
  "data": null
}
```

前端应优先根据 `code` 判断业务是否成功。

## 9. 鉴权方式

登录或注册成功后，返回：

```json
{
  "token": "...",
  "user": {}
}
```

访问需要登录的接口时，请求头加：

```text
Authorization: Bearer token内容
```

## 10. 当前实现范围

已经实现：

- 用户注册和登录
- JWT 登录态
- 食堂列表和详情
- 菜品列表、详情和推荐
- 菜品标签、评分、收藏数统计
- 菜品评价
- 评价点赞、踩和取消
- 用户收藏
- 用户补充菜品信息
- 管理员审核补充信息
- 公告
- 拥挤度上报

暂时简化：

- 图片上传暂时只使用 `imageUrl` 字符串，不做真实 multipart 上传。
- 列表接口暂时不分页，演示数据量较小。
- 管理员功能主要通过 Swagger 演示，Android 端可以不做完整后台页面。

## 11. 常见问题

### 11.1 `mvn` 找不到

说明 Maven 没安装好，或者 `Path` 没配置 `%MAVEN_HOME%\bin`。

### 11.2 后端启动时报 MySQL 连接失败

优先检查：

- MySQL 服务是否启动
- `application.yml` 里的用户名和密码是否正确
- 是否执行过 `schema.sql`
- 数据库名是否是 `thu_canteen`

### 11.3 Swagger 能打开，但接口没有数据

通常是没有执行 `seed.sql`，或者执行到了另一个数据库。

### 11.4 Android 真机访问不了

优先检查：

- Base URL 是否写成 `http://电脑局域网IP:8080/`
- 手机和电脑是否在同一网络
- Windows 防火墙是否拦截 8080 端口
