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
