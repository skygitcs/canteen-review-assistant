# Canteen Review Assistant

清华食堂点评助手，课程“移动应用软件开发”大作业项目仓库。

当前仓库已包含后端服务工程与 Android 客户端原型：

```text
canteen-review-assistant/
├── README.md
├── .gitignore
├── backend/                 # Spring Boot 后端服务
└── android/                 # Android 客户端（Java + XML）
```

## 快速启动流程

本项目本地运行需要先启动 MySQL 和后端，再运行 Android 客户端。

### 1. 准备环境

需要安装：

- JDK 17
- Maven
- MySQL 8
- Android Studio
- Android SDK Platform-Tools（用于真机调试时执行 `adb`）

### 2. 初始化数据库

进入 MySQL 时建议带上 `utf8mb4`，否则导入中文种子数据可能乱码：

```powershell
mysql --default-character-set=utf8mb4 -u root -p
```

如果系统找不到 `mysql`，可以使用完整路径：

```powershell
& "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" --default-character-set=utf8mb4 -u root -p
```

进入 MySQL 后执行：

```sql
DROP DATABASE IF EXISTS thu_canteen;
SOURCE E:/canteen-review/canteen-review-assistant/backend/src/main/resources/db/schema.sql;
SOURCE E:/canteen-review/canteen-review-assistant/backend/src/main/resources/db/seed.sql;
```

如果项目不在 `E:/canteen-review/canteen-review-assistant`，请把 `SOURCE` 路径改成自己的实际路径。

### 3. 启动后端

```powershell
cd E:\canteen-review\canteen-review-assistant\backend
$env:DB_PASSWORD="你的MySQL密码"
mvn spring-boot:run
```

启动后检查：

```text
http://localhost:8080/swagger-ui.html
```

看到 Swagger 页面说明后端已经启动。

### 4. 运行 Android 客户端

用 Android Studio 打开：

```text
canteen-review-assistant/android
```

等待 Gradle 同步完成后，选择模拟器或真机运行。

当前 Android 端后端地址使用：

```text
http://127.0.0.1:8080/
```

真机测试前需要执行端口转发：

```powershell
adb devices
adb reverse tcp:8080 tcp:8080
```

如果 PowerShell 找不到 `adb`，可以使用 SDK 的完整路径，例如：

```powershell
& "E:\AndroidSDK\platform-tools\adb.exe" devices
& "E:\AndroidSDK\platform-tools\adb.exe" reverse tcp:8080 tcp:8080
```

模拟器通常也可以配合 `adb reverse` 使用。如果不使用 `adb reverse`，模拟器访问电脑后端常用地址是 `http://10.0.2.2:8080/`，需要在 `NetworkClient.BASE_URL` 中切换。

### 5. 演示账号

导入 `seed.sql` 后可使用以下账号，密码均为 `password`：

| 用户名 | 角色 | 说明 |
| --- | --- | --- |
| demo | USER | 普通演示用户 |
| reviewer | USER | 带有评价和收藏数据的普通用户 |
| admin | ADMIN | 管理员演示用户 |

## Android 客户端

Android 客户端基于 Java 17 + 原生 XML + Material 组件实现移动端原型界面，
可直接用 Android Studio 打开 `android/` 目录运行。

快速说明：

1. 用 Android Studio 打开 `canteen-review-assistant/android`。
2. 等待 Gradle 同步完成。
3. 选择模拟器或真机，点击 Run 运行。

详细说明见 `android/README.md`。

## 后端

后端负责服务器架构、数据库建模、API 接口实现、数据准确性、安全性与接口文档。

进入后端目录查看完整说明：

```bash
cd backend
```

主要文档：

- [后端启动说明](backend/README.md)
- [API 接口文档](backend/docs/API.md)
- [后端设计说明](backend/docs/backend-design.md)

## 技术栈

- 后端：Java 17、Spring Boot 3、Spring Security + JWT、MyBatis-Plus、MySQL
- Android 端：Java 17、原生 XML 布局、Material Design、Glide、RecyclerView

## 推荐仓库结构

```text
canteen-review-assistant/
├── android/                 # Android 客户端
├── backend/                 # 后端服务
├── docs/                    # 小组汇总文档、报告、截图等，后续可添加
├── README.md
└── .gitignore
```

## 大作业交付文档

- [实现说明文档](docs/implementation-report.md)
- [用户手册](docs/user-manual.md)
- [测试报告](docs/test-report.md)
- [评分项对照表](docs/scorecard-alignment.md)
- [最终进度报告](docs/final-progress-report.md)
