# 项目当前状态与下一步计划

本文档用于交接当前项目状态，方便后续继续开发和联调。

## 项目结构

主要目录：

- `android/`：Android 原生前端，Java + XML + Material 组件。
- `backend/`：Spring Boot 后端，包含 Controller、Service、Mapper、Entity、DTO、安全配置和数据库脚本。
- `docs/`：项目说明和阶段进度文档。

早期存在 `frontend/` Web 原型，但当前主要开发目标是 `android/`。

## Android 前端当前状态

已实现的主要页面：

- 启动页：登录/注册页，当前为静态演示，不校验账号，不保存用户数据。
- 首页：公告条、今日推荐食堂、推荐菜品。
- 美食广场：展示所有食堂，支持搜索和 tag 筛选。
- 食堂详情：展示食堂信息、楼层筛选、窗口筛选、搜索、菜品列表。
- 菜品详情：展示菜品信息、tag、价格、描述、评论列表。
- 我的页面：用户信息、最近动态、收藏菜品、留言入口。

已实现的交互：

- 底部导航：只有图标，包含首页、美食广场、我的。
- 详情页顶部返回栏。
- 收藏菜品可点击进入菜品详情。
- 点击个人页头像/昵称区域，会弹出“切换账号 / 退出登录”，进入静态登录页。
- 补充菜品信息弹窗：支持输入菜品名、说明、tag，并有图片上传入口的静态演示。
- 写评价弹窗：支持五颗星评分、文字评价、tag 输入、图片上传入口的静态演示。
- 评价提交后会在当前菜品详情页即时插入一条本地评价。

当前限制：

- 前端仍主要使用 `MockRepository` 静态数据。
- 登录/注册不连接后端。
- 收藏、评价、补充菜品信息没有持久化。
- 图片目前用空占位，不使用随机风景图。
- 地图/热力图代码保留，但美食广场页面里先隐藏。

## 后端当前状态

后端不是空项目，已有较完整骨架：

- Spring Boot 3
- Spring Security + JWT 相关类
- MyBatis-Plus
- MySQL schema
- Controller / Service / Mapper / Entity / DTO 基本结构

已有 Controller 覆盖：

- `AuthController`
  - `POST /api/auth/register`
  - `POST /api/auth/login`

- `CanteenController`
  - `GET /api/canteens`
  - `GET /api/canteens/{id}`
  - `POST /api/canteens/{id}/crowd`
  - `GET /api/canteens/heatmap`

- `DishController`
  - `GET /api/dishes`
  - `GET /api/dishes/{id}`
  - `GET /api/dishes/recommendations`
  - `POST /api/dishes/submissions`

- `ReviewController`
  - `POST /api/dishes/{dishId}/reviews`
  - `POST /api/reviews/{reviewId}/vote`

数据库 schema 已包含：

- `users`
- `canteens`
- `canteen_windows`
- `dishes`
- `dish_tags`
- `reviews`
- `review_votes`
- `favorites`
- `crowd_reports`
- `announcements`
- `dish_submissions`
- `consumption_records`

当前风险：

- `seed.sql` 里的中文数据已经乱码，需要重写。
- 还没确认后端在本地 MySQL 上是否能完整启动。
- 还没确认所有接口是否都能正常返回数据。
- 图片上传/图片静态访问还没有形成稳定方案。

## 数据库统一方案

推荐方案：每个人本地各跑一个 MySQL，但统一 SQL 文件。

统一文件：

- `backend/src/main/resources/db/schema.sql`
- `backend/src/main/resources/db/seed.sql`

做法：

- 每个人本地创建同名数据库：`thu_canteen`
- schema 改动必须提交 SQL。
- seed 数据统一从仓库导入。
- 不建议多人共用一个远程数据库作为开发库，容易互相改坏数据。

后续建议：

- 把 `application.yml` 中的数据库账号密码改成环境变量，避免所有人必须使用 `root/root`。
- 可以增加 `application-local.yml.example`，让每个人复制成本地配置。

## 菜品和图片数据准备方案

不要靠 App 手动录入几十个菜，建议用表格批量准备。

推荐准备一个 CSV 或 Excel，字段类似：

```text
canteen_name,window_name,floor_no,dish_name,price,tags,description,image_file
紫荆食堂,川湘热菜,1,麻婆豆腐,12,辣味/下饭/经典,辣度较高，适合配饭,mapo_tofu.jpg
桃李食堂,盖饭窗口,1,番茄滑蛋饭,13,清淡/米饭/家常,油盐较轻,tomato_egg.jpg
```

图片建议：

- 图片先放在统一目录，例如 `backend/uploads/dishes/`。
- 图片文件名用英文或拼音，不用中文。
- 数据库里存相对路径或 URL，例如 `/uploads/dishes/mapo_tofu.jpg`。
- 先实现静态图片访问，再考虑真正上传。

短期目标：

- 7 个食堂。
- 每个食堂 2-4 个窗口。
- 每个食堂 5-8 个菜。
- 每个热门菜 2-3 条评论。

## 后端下一步任务

优先级从高到低：

1. 确认后端能启动。
   - 本地 MySQL 创建 `thu_canteen`。
   - 执行 `schema.sql`。
   - 启动 Spring Boot。

2. 重写 `seed.sql`。
   - 修复乱码。
   - 加入真实或半真实食堂、窗口、菜品、tag、评论。

3. 验证核心接口。
   - 注册
   - 登录
   - 食堂列表
   - 食堂详情
   - 菜品列表
   - 菜品详情
   - 写评价

4. 做图片静态访问。
   - 先支持 `/uploads/dishes/xxx.jpg` 能被访问。
   - 图片上传接口可以稍后做。

5. 确认 JWT 和鉴权流程。
   - 登录返回 token。
   - 需要登录的接口能识别 token。
   - 普通用户能评价、收藏、提交补充菜品信息。

6. 再补管理员审核。
   - 查看补充菜品提交。
   - 审核通过后写入 `dishes` 和 `dish_tags`。
   - 驳回时记录原因。

## 前端下一步任务

优先级从高到低：

1. 搭 Retrofit / OkHttp 基础层。
   - `ApiClient`
   - `ApiService`
   - DTO 类
   - token 保存工具

2. 先接只读接口。
   - `GET /api/canteens`
   - `GET /api/canteens/{id}`
   - `GET /api/dishes`
   - `GET /api/dishes/{id}`

3. 保留静态数据兜底。
   - API 请求失败时仍显示 `MockRepository` 数据。
   - 这样演示时不容易崩。

4. 登录页改成真实请求。
   - `POST /api/auth/login`
   - `POST /api/auth/register`
   - 成功后保存 token 并进入主界面。

5. 写评价接后端。
   - `POST /api/dishes/{dishId}/reviews`
   - 成功后刷新评论列表或本地插入。

6. 补充菜品信息接后端。
   - `POST /api/dishes/submissions`
   - 先提交文字、tag、价格、窗口信息。
   - 图片可先用 URL 字段，后续再上传。

## 推荐联调顺序

先跑通最小闭环：

1. 后端启动。
2. 数据库导入真实 seed。
3. 前端登录成功并保存 token。
4. 前端请求真实食堂列表。
5. 点击食堂进入详情，展示真实菜品。
6. 点击菜品进入详情，展示真实评论。
7. 写评价，后端落库。
8. 刷新菜品详情，能看到刚写的评价。

这个闭环完成后，再做收藏、补充菜品、管理员审核和图片上传。

## 如果后端同学暂时没空，前端同学可以先做什么

可以做，且风险较低：

- 重写 `seed.sql`，修复乱码。
- 整理 `sample-dishes.csv` 或 Excel 数据表。
- 准备菜品图片文件夹和命名规则。
- 搭 Android Retrofit 基础层。
- 写 DTO 类和 API service。
- 用 Swagger 或 Postman 测已有接口。

暂时不建议前端同学深改：

- Spring Security 复杂逻辑。
- JWT 核心实现。
- 管理员审核复杂业务。
- 大规模重构 Service 层。

原因是这些地方容易和后端同学之后的实现冲突。

## 当前最重要的判断

静态 UI 已经够多了。接下来最加分的是前后端真实联调。

最优先目标：

```text
登录 -> 食堂列表 -> 菜品详情 -> 写评价 -> 数据库能看到评价
```

只要这条链路跑通，项目就从“静态原型”进入“真实应用雏形”。
