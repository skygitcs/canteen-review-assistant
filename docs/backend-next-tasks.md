# 后端接手待办清单

本文档用于后端接手和验收前冲刺。当前目标不是做完整商业后端，而是保证移动应用大作业可以在手机上跑通核心流程，并且有数据库、接口文档、测试记录作为验收支撑。

## 1. 当前后端已经具备

- Spring Boot 后端工程
- MySQL 数据库建表脚本
- 演示数据脚本
- JWT 注册登录
- 食堂、窗口、菜品、标签、评价、收藏、公告等核心表
- 食堂列表和详情接口
- 菜品列表、详情、推荐、标签接口
- 写评价接口
- 收藏接口
- 补充菜品信息接口
- 管理员审核补充信息接口
- Swagger / OpenAPI 文档
- 健康检查接口

## 2. 现在可以不运行先做的事

这些任务不依赖本地 MySQL/Maven，可以先由后端同学继续补：

| 优先级 | 任务 | 说明 |
| --- | --- | --- |
| 高 | 完善 README | 安装、启动、真机联调、常见问题要让同学能照着跑 |
| 高 | 完善接口文档 | 前端先按 Markdown 和 Swagger 接口开发 |
| 高 | 检查 seed 数据 | 确保食堂、窗口、菜品、标签、公告、评论够展示 |
| 高 | 增加健康检查接口 | 用于确认后端是否在线 |
| 中 | 增加标签列表接口 | 前端筛选和补充信息弹窗可复用 |
| 中 | 梳理验收测试用例 | 登录、列表、详情、评价、收藏、补充信息 |
| 低 | 图片上传设计 | 先写方案，实际 multipart 可以后置 |

## 3. 装好环境后必须验证

按顺序做，不要跳。

### 3.1 基础环境

```powershell
java -version
mvn -version
mysql --version
```

期望：

- Java 是 17
- Maven 能输出版本
- MySQL 8 可用

### 3.2 初始化数据库

执行：

```sql
SOURCE E:/canteen-review/canteen-review-assistant/backend/src/main/resources/db/schema.sql;
SOURCE E:/canteen-review/canteen-review-assistant/backend/src/main/resources/db/seed.sql;
```

然后检查：

```sql
USE thu_canteen;
SELECT COUNT(*) FROM canteens;
SELECT COUNT(*) FROM dishes;
SELECT COUNT(*) FROM reviews;
```

期望：

- `canteens` 至少 7 条
- `dishes` 至少 20 条
- `reviews` 有演示评论

### 3.3 启动后端

```powershell
cd E:\canteen-review\canteen-review-assistant\backend
mvn spring-boot:run
```

打开：

```text
http://localhost:8080/api/health
http://localhost:8080/swagger-ui.html
```

期望：

- 健康检查返回 `status: UP`
- Swagger 页面能打开

### 3.4 Swagger 主流程测试

按这个顺序测：

1. `POST /api/auth/login`
2. `GET /api/canteens`
3. `GET /api/dishes`
4. `GET /api/dishes/{id}`
5. `POST /api/dishes/{dishId}/reviews`
6. `POST /api/users/me/favorites`
7. `GET /api/users/me/favorites`
8. `POST /api/dishes/submissions`

演示账号：

```text
demo / password
reviewer / password
admin / password
```

需要登录的接口要加：

```text
Authorization: Bearer token内容
```

### 3.5 自动化冒烟测试

后端启动后，也可以直接运行脚本：

```powershell
powershell -ExecutionPolicy Bypass -File backend/scripts/smoke-test.ps1
```

脚本通过时会输出：

```text
Smoke test passed.
```

这可以作为验收前的测试记录材料。脚本会创建临时用户，并写入一条评价、一条收藏和一条补充菜品信息。

## 4. 验收前最重要的后端功能

这些是评分表里最容易体现“后端和数据库确实工作了”的部分。

### 4.1 必须完成

- 注册登录能用
- App 可以从后端加载食堂列表
- App 可以从后端加载菜品详情
- App 可以提交五星评价
- App 可以收藏菜品
- App 可以查看我的收藏
- App 可以提交补充菜品信息
- 数据库里能看到新增评价、收藏、补充信息

### 4.2 建议完成

- 首页公告从后端获取
- 标签筛选从后端获取标签列表
- 管理员通过 Swagger 审核补充菜品信息
- 后端 README 和接口文档完整
- 写一份测试记录

### 4.3 可以后置

- 真实图片上传
- 分页
- 复杂推荐算法
- 管理员 Android 页面
- 校园卡消费记录真实接入
- 地图/热力图完整展示

## 5. 前后端分工建议

### 后端负责人

- 保证 Spring Boot 可以启动
- 保证 MySQL 脚本可导入
- 保证 Swagger 主流程可测
- 修接口 bug
- 维护接口文档和 README

### 前端负责人

- 封装 Base URL 和 token
- 接登录/注册
- 接食堂和菜品接口
- 接评价和收藏接口
- 保留 mock 数据兜底

### 数据负责人，可以由后端兼任

- 补真实食堂和菜品
- 补标签
- 补评论
- 检查演示数据是否足够自然

## 6. 当前分支建议

建议保持：

```text
dev
├── feat-backend-api
└── feat-android-api-integration
```

后端改动先进 `feat-backend-api`，能启动并通过 Swagger 主流程后，再合进 `dev`。

前端同学如果只想跑后端，可以单独 clone 或使用 `git worktree` 拉一份后端目录，不要在自己的 Android 分支里反复切分支。

## 7. 今日最小目标

今天只要做到下面这些，就算后端推进有效：

1. 安装 Maven 和 MySQL。
2. 导入 `schema.sql` 和 `seed.sql`。
3. 启动 Spring Boot。
4. 打开 Swagger。
5. 登录 `demo / password`。
6. 用 Swagger 写一条评价。
7. 数据库里能查到这条评价。

这条链路跑通以后，前端就可以放心接真实接口。
