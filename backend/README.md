# THU Canteen Backend

这是“清华食堂点评助手”的后端工程，对应 A 同学职责：服务器架构、数据库建模、API 接口实现、数据准确性、安全性与接口文档。

## 技术栈

- Java 17
- Spring Boot 3.3
- Spring Security + JWT
- MyBatis-Plus
- MySQL 8.x
- springdoc-openapi / Swagger UI

## 文件结构

```text
backend/
├── pom.xml                         # Maven 依赖与构建配置
├── README.md                       # 后端启动与交付说明
├── docs/
│   ├── API.md                      # Android 端对接接口文档
│   └── backend-design.md           # 后端设计说明、ER 图、核心逻辑
└── src/main/
    ├── java/edu/thu/canteen/
    │   ├── CanteenApplication.java # Spring Boot 启动入口
    │   ├── common/                 # 统一响应、业务异常、全局异常处理
    │   ├── config/                 # Spring Security 与 CORS 配置
    │   ├── controller/             # REST API 控制器
    │   ├── domain/
    │   │   ├── entity/             # 数据库实体：用户、食堂、菜品、评论等
    │   │   └── mapper/             # MyBatis-Plus Mapper
    │   ├── dto/                    # 请求体与响应视图对象
    │   ├── security/               # JWT 生成解析、鉴权过滤器、当前用户工具
    │   └── service/                # 业务逻辑：统计、推荐、审核、收藏等
    └── resources/
        ├── application.yml         # 服务端口、数据库、JWT 配置
        └── db/
            ├── schema.sql          # MySQL 建表脚本
            └── seed.sql            # 示例食堂、窗口、菜品、公告数据
```

## 启动步骤

1. 创建并初始化数据库：

```sql
source src/main/resources/db/schema.sql;
source src/main/resources/db/seed.sql;
```

2. 修改 `src/main/resources/application.yml` 中的 MySQL 用户名和密码。

3. 启动服务：

```bash
mvn spring-boot:run
```

4. 访问接口文档：

```text
http://localhost:8080/swagger-ui.html
```

## 管理员账号

先通过 `/api/auth/register` 注册一个普通用户，然后在 MySQL 中执行：

```sql
UPDATE users SET role = 'ADMIN' WHERE username = '你的用户名';
```

之后重新登录获取新 token，即可调用 `/api/admin/**`。

## 统一响应格式

```json
{
  "code": 0,
  "message": "ok",
  "data": {}
}
```

业务错误也返回 HTTP 200，`code` 使用 `400/401/403/404/409/500`，方便 Android 端统一处理弹窗和页面状态。

## 已覆盖评分点

- 基础数据：食堂、窗口、菜品、标签、用户、评论、收藏、公告、消费记录。
- 数据统计：评分均值、评论数、收藏数、实时拥挤度、全体/个人热力图、推荐排序。
- 内容审核：用户上传菜品进入 `PENDING`，管理员审核后生成正式菜品或驳回。
- 安全：BCrypt 密码哈希、JWT 登录态、管理员接口 RBAC 权限控制。
- 鲁棒性：参数校验、重复注册、重复评价、重复收藏、非法食堂/窗口关系、未登录访问等都有统一处理。
