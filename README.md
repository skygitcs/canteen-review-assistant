# Canteen Review Assistant

清华食堂点评助手，课程“移动应用软件开发”大作业项目仓库。

代码仓库地址：[skygitcs/canteen-review-assistant](https://github.com/skygitcs/canteen-review-assistant/)

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

进入 MySQL 时需要带上 `utf8mb4`，否则导入中文种子数据可能乱码：

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

当前 Android 端默认面向模拟器运行，后端地址使用：

```text
http://10.0.2.2:8080/
```

`10.0.2.2` 是 Android 模拟器访问电脑本机服务的地址。后端启动在电脑的 `localhost:8080` 后，模拟器可以通过这个地址访问。

使用真机测试时，将 `NetworkClient.BASE_URL` 改成：

```text
http://127.0.0.1:8080/
```

然后在真机测试前执行端口转发：

```powershell
adb devices
adb reverse tcp:8080 tcp:8080
```

如果 PowerShell 找不到 `adb`，可以使用 SDK 的完整路径，例如：

```powershell
& "E:\AndroidSDK\platform-tools\adb.exe" devices
& "E:\AndroidSDK\platform-tools\adb.exe" reverse tcp:8080 tcp:8080
```

也可以把真机和电脑连接到同一网络后，将 `BASE_URL` 改成电脑的局域网 IP，例如 `http://192.168.1.23:8080/`。

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

## 项目结构

```text
canteen-review-assistant/
├── android/                 # Android 客户端
├── backend/                 # 后端服务
├── docs/                    # 项目说明、用户手册、测试文档和进度文档
├── README.md
└── .gitignore
```

## 大作业交付文档

本次大作业提交材料包含以下部分：

```text
大作业提交材料/
├── canteen-review-assistant/     # 本项目代码、数据库脚本和项目文档
├── 自评表/                        # 小组自评表、评分项说明等
└── 演示视频/                      # App 演示视频文件
```


本项目仓库内的主要文档如下：

- [说明文档](docs/说明文档.md)：项目背景、功能说明、架构设计和实现概述。
- [用户手册](docs/用户手册.md)：面向用户和验收演示的使用说明。
- [测试文档](docs/测试文档.md)：测试环境、测试用例、测试结果和问题记录。
- [主进度文档](docs/主进度文档.md)：项目整体进度和阶段性工作记录。
- [前端进度文档](docs/前端进度文档.md)：Android 前端实现过程和界面功能说明。
- [后端进度文档](docs/后端进度文档.md)：后端开发过程、接口、数据库和联调记录。
- [前后端接口约定](docs/frontend-api-contract.md)：Android 与后端联调用的接口字段和请求格式。
- [真实数据导入说明](docs/real-data-seed-guide.md)：真实食堂、菜品、图片和 seed 数据导入说明。
- [真实菜品数据收集表](docs/canteen-data-collection.xlsx)：小组整理菜品数据时使用的表格。
- [原型构思草图](docs/原型构思草图.pdf)：项目早期界面结构和功能构思草图。
