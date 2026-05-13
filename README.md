# Canteen Review Assistant

清华食堂点评助手，课程“移动应用软件开发”大作业项目仓库。

当前仓库已包含后端服务工程，后续 Android 客户端可以放在同一仓库下，例如：

```text
canteen-review-assistant/
├── README.md
├── .gitignore
└── backend/                 # Spring Boot 后端服务
```

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
- Android 端规划：Java、原生 XML 布局、Retrofit2、OkHttp3、Glide、Room

## 推荐仓库结构

```text
canteen-review-assistant/
├── android/                 # Android 客户端，后续由前端同学放入
├── backend/                 # 后端服务
├── docs/                    # 小组汇总文档、报告、截图等，后续可添加
├── README.md
└── .gitignore
```

当前只提交已有的 `backend/` 即可，后续其他成员再按模块补充。
