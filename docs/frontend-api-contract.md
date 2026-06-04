# 前后端联调接口约定

本文档给前端同学开发使用，先按当前后端代码已经存在的接口整理。后端服务默认运行在 `8080` 端口。

## 1. 基础约定

### 1.1 Base URL

本机调试：

```text
http://localhost:8080
```

Android 真机调试时不能使用 `localhost`，需要使用后端电脑的局域网 IP：

```text
http://后端电脑IP:8080
```

例如：

```text
http://192.168.1.23:8080
```

### 1.2 统一返回格式

所有接口统一返回：

```json
{
  "code": 0,
  "message": "ok",
  "data": {}
}
```

前端判断方式：

- `code == 0`：请求成功，使用 `data`
- `code != 0`：请求失败，展示 `message`

### 1.3 登录 token

注册和登录成功后，后端会返回 `token`。

前端需要保存 token，之后访问需要登录的接口时，在请求头加入：

```text
Authorization: Bearer 登录返回的token
```

不需要登录也能访问：

- 健康检查
- 获取食堂列表
- 获取食堂详情
- 获取菜品列表
- 获取菜品详情
- 获取推荐菜品
- 获取菜品标签
- 获取公告

需要登录才能访问：

- 获取个人资料
- 修改个人资料
- 收藏/取消收藏
- 获取我的收藏
- 写评价
- 给评价点赞/踩
- 补充菜品信息
- 上报拥挤度

### 1.4 演示账号

导入后端 `seed.sql` 后，可以使用以下账号登录。密码均为：

```text
password
```

| 用户名 | 角色 | 说明 |
| --- | --- | --- |
| demo | USER | 普通演示用户 |
| reviewer | USER | 带有部分评价和收藏数据 |
| admin | ADMIN | 管理员演示用户 |

### 1.5 JSON 请求头

除特别说明外，请求体都使用 JSON：

```text
Content-Type: application/json
```

目前先不做真正的图片上传，图片字段统一传 `imageUrl` 字符串。没有图片时可以传 `null` 或空字符串。

### 1.6 常见错误返回

后端统一使用业务 `code` 表示错误。即使未登录或无权限，也会返回 JSON：

```json
{
  "code": 401,
  "message": "login required",
  "data": null
}
```

```json
{
  "code": 403,
  "message": "permission denied",
  "data": null
}
```

## 2. 健康检查

### 2.1 检查后端是否在线

```http
GET /api/health
```

返回 `data`：

```json
{
  "status": "UP",
  "time": "2026-06-04 18:30:00"
}
```

前端页面对应：

- 联调时手动检查 Base URL 是否写对
- App 启动时可以临时用来判断后端是否可访问

正式页面不一定要展示这个接口。

## 3. 登录与注册

### 3.1 注册

```http
POST /api/auth/register
```

请求体：

```json
{
  "username": "student001",
  "password": "123456",
  "nickname": "清华吃饭人"
}
```

字段说明：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| username | string | 是 | 用户名，3-32 位 |
| password | string | 是 | 密码，6-64 位 |
| nickname | string | 是 | 昵称，最多 32 位 |

返回 `data`：

```json
{
  "token": "jwt-token",
  "user": {
    "id": 1,
    "username": "student001",
    "nickname": "清华吃饭人",
    "avatarUrl": null,
    "role": "USER",
    "tastePreference": null,
    "campusCardAuthorized": false
  }
}
```

### 3.2 登录

```http
POST /api/auth/login
```

请求体：

```json
{
  "username": "student001",
  "password": "123456"
}
```

返回格式同注册。

前端页面对应：

- 登录页
- 注册页
- 个人页切换账号

## 4. 公告

### 4.1 获取当前公告

```http
GET /api/announcements
```

返回 `data`：

```json
[
  {
    "id": 1,
    "title": "今日公告",
    "content": "紫荆食堂晚餐新增窗口，欢迎体验。",
    "startsAt": "2026-06-01T00:00:00",
    "endsAt": "2026-06-30T23:59:59",
    "status": 1,
    "createdAt": "2026-06-01T10:00:00"
  }
]
```

前端页面对应：

- 首页顶部滚动公告栏

## 5. 食堂

### 5.1 获取食堂列表

```http
GET /api/canteens
```

可选查询参数：

| 参数 | 类型 | 说明 |
| --- | --- | --- |
| keyword | string | 按食堂名/地址搜索 |
| onCampus | boolean | 是否校内 |
| sort | string | 排序方式，建议先支持 `rating`、`crowd` |

示例：

```http
GET /api/canteens?keyword=紫荆&onCampus=true&sort=rating
```

返回 `data`：

```json
[
  {
    "id": 1,
    "name": "紫荆",
    "coverUrl": null,
    "address": "紫荆学生公寓附近",
    "openHours": "07:00-20:00",
    "payMethods": "校园卡,微信,支付宝",
    "onCampus": true,
    "latitude": 40.000000,
    "longitude": 116.000000,
    "avgRating": 4.5,
    "dishCount": 18,
    "crowdLevel": 3.2
  }
]
```

前端页面对应：

- 美食广场
- 首页推荐食堂

### 5.2 获取食堂详情

```http
GET /api/canteens/{id}
```

可选查询参数：

| 参数 | 类型 | 说明 |
| --- | --- | --- |
| floorNo | number | 楼层筛选 |
| tag | string | 标签筛选 |
| sort | string | 菜品排序 |

示例：

```http
GET /api/canteens/1?floorNo=2&tag=辣味&sort=rating
```

返回 `data`：

```json
{
  "base": {
    "id": 1,
    "name": "紫荆",
    "coverUrl": null,
    "address": "紫荆学生公寓附近",
    "openHours": "07:00-20:00",
    "payMethods": "校园卡,微信,支付宝",
    "onCampus": true,
    "latitude": 40.000000,
    "longitude": 116.000000,
    "avgRating": 4.5,
    "dishCount": 18,
    "crowdLevel": 3.2
  },
  "windows": [
    {
      "id": 1,
      "canteenId": 1,
      "floorNo": 1,
      "name": "家常菜窗口",
      "openHours": "10:30-13:30,16:30-19:30",
      "status": 1,
      "createdAt": "2026-06-01T10:00:00",
      "updatedAt": "2026-06-01T10:00:00"
    }
  ],
  "dishes": [
    {
      "id": 1,
      "canteenId": 1,
      "canteenName": "紫荆",
      "windowId": 1,
      "windowName": "家常菜窗口",
      "floorNo": 1,
      "name": "番茄炒蛋",
      "imageUrl": null,
      "price": 8.00,
      "description": "酸甜口味，适合配米饭。",
      "spiceLevel": 0,
      "tags": ["清淡", "家常"],
      "avgRating": 4.6,
      "reviewCount": 12,
      "favoriteCount": 30
    }
  ]
}
```

前端页面对应：

- 食堂详情页
- 左侧楼层栏使用 `windows.floorNo`
- 顶部搜索/筛选调用该接口或本地筛选都可以

### 5.3 上报拥挤度

```http
POST /api/canteens/{id}/crowd
```

需要登录。

请求体：

```json
{
  "level": 3
}
```

字段说明：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| level | number | 是 | 1-5，数字越大越拥挤 |

返回：

```json
{
  "code": 0,
  "message": "ok",
  "data": null
}
```

前端页面对应：

- 食堂详情页的拥挤度反馈，可以验收前再接

## 6. 菜品

### 6.1 获取菜品列表

```http
GET /api/dishes
```

可选查询参数：

| 参数 | 类型 | 说明 |
| --- | --- | --- |
| keyword | string | 搜索菜名/描述 |
| canteenId | number | 指定食堂 |
| floorNo | number | 指定楼层 |
| tag | string | 指定标签 |
| sort | string | 排序方式，建议先支持 `rating`、`favorite`、`price` |

示例：

```http
GET /api/dishes?canteenId=1&keyword=牛肉&tag=辣味&sort=rating
```

返回 `data`：

```json
[
  {
    "id": 2,
    "canteenId": 1,
    "canteenName": "紫荆",
    "windowId": 1,
    "windowName": "家常菜窗口",
    "floorNo": 1,
    "name": "水煮牛肉",
    "imageUrl": null,
    "price": 16.00,
    "description": "偏辣，分量较足。",
    "spiceLevel": 4,
    "tags": ["辣味", "荤菜", "下饭"],
    "avgRating": 4.7,
    "reviewCount": 20,
    "favoriteCount": 45
  }
]
```

前端页面对应：

- 美食广场的全校菜品搜索
- 首页推荐菜品
- 食堂详情页菜品列表

### 6.2 获取菜品详情

```http
GET /api/dishes/{id}
```

返回 `data`：

```json
{
  "base": {
    "id": 2,
    "canteenId": 1,
    "canteenName": "紫荆",
    "windowId": 1,
    "windowName": "家常菜窗口",
    "floorNo": 1,
    "name": "水煮牛肉",
    "imageUrl": null,
    "price": 16.00,
    "description": "偏辣，分量较足。",
    "spiceLevel": 4,
    "tags": ["辣味", "荤菜", "下饭"],
    "avgRating": 4.7,
    "reviewCount": 20,
    "favoriteCount": 45
  },
  "reviews": [
    {
      "id": 1,
      "dishId": 2,
      "userId": 1,
      "nickname": "清华吃饭人",
      "rating": 5,
      "content": "味道不错，但中午排队比较久。",
      "imageUrl": null,
      "upVotes": 3,
      "downVotes": 0,
      "createdAt": "2026-06-01T12:30:00"
    }
  ]
}
```

前端页面对应：

- 菜品详情页
- 评价列表

### 6.3 获取推荐菜品

```http
GET /api/dishes/recommendations
```

可选查询参数：

| 参数 | 类型 | 说明 |
| --- | --- | --- |
| limit | number | 返回数量 |

示例：

```http
GET /api/dishes/recommendations?limit=6
```

返回 `data`：同菜品列表。

前端页面对应：

- 首页推荐菜品

### 6.4 获取菜品标签

```http
GET /api/dishes/tags
```

返回 `data`：

```json
[
  "清淡",
  "辣味",
  "家常",
  "下饭",
  "早餐"
]
```

前端页面对应：

- 美食广场标签筛选
- 补充菜品信息弹窗里的标签候选

### 6.5 补充菜品信息

```http
POST /api/dishes/submissions
```

需要登录。

请求体：

```json
{
  "canteenId": 1,
  "windowId": 1,
  "name": "红烧排骨",
  "imageUrl": null,
  "price": 18.00,
  "description": "偏甜口，肉量还可以。",
  "spiceLevel": 0,
  "tags": ["荤菜", "家常", "下饭"]
}
```

字段说明：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| canteenId | number | 是 | 食堂 ID |
| windowId | number | 是 | 窗口 ID |
| name | string | 是 | 菜名，2-50 位 |
| imageUrl | string | 否 | 图片地址，暂时可为空 |
| price | number | 否 | 价格 |
| description | string | 否 | 描述，最多 500 字 |
| spiceLevel | number | 否 | 辣度，0-5 |
| tags | string[] | 否 | 标签列表 |

前端页面对应：

- 菜品详情页或食堂详情页的“补充菜品信息”弹窗

验收前建议：

- 先让前端选择食堂、窗口、输入菜名、价格、描述、标签。
- 图片先只保留 UI，不一定真的上传。

## 7. 评价

### 7.1 写评价

```http
POST /api/dishes/{dishId}/reviews
```

需要登录。

请求体：

```json
{
  "rating": 5,
  "content": "五星推荐，晚饭时间稍微有点排队。",
  "imageUrl": null
}
```

字段说明：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| rating | number | 是 | 1-5，只使用整颗星 |
| content | string | 是 | 评价内容，2-500 字 |
| imageUrl | string | 否 | 图片地址，暂时可为空 |

返回 `data`：

```json
{
  "id": 10,
  "dishId": 2,
  "userId": 1,
  "nickname": "清华吃饭人",
  "rating": 5,
  "content": "五星推荐，晚饭时间稍微有点排队。",
  "imageUrl": null,
  "upVotes": 0,
  "downVotes": 0,
  "createdAt": "2026-06-04T18:20:00"
}
```

前端页面对应：

- 菜品详情页写评价弹窗
- 五星评分只传整数 `1` 到 `5`

### 7.2 给评价点赞/踩

```http
POST /api/reviews/{reviewId}/vote
```

需要登录。

请求体：

```json
{
  "vote": 1
}
```

字段说明：

| vote | 含义 |
| --- | --- |
| 1 | 点赞 |
| 0 | 取消 |
| -1 | 踩 |

返回 `data`：更新后的评价对象。

前端页面对应：

- 菜品详情页评价列表，可以作为后续功能

## 8. 用户与收藏

### 8.1 获取当前用户

```http
GET /api/users/me
```

需要登录。

返回 `data`：

```json
{
  "id": 1,
  "username": "student001",
  "nickname": "清华吃饭人",
  "avatarUrl": null,
  "role": "USER",
  "tastePreference": "清淡",
  "campusCardAuthorized": false
}
```

前端页面对应：

- 个人页
- 启动后判断登录状态

### 8.2 修改个人资料

```http
PUT /api/users/me
```

需要登录。

请求体：

```json
{
  "nickname": "紫荆常客",
  "avatarUrl": null,
  "tastePreference": "清淡,少辣",
  "campusCardAuthorized": false
}
```

返回 `data`：更新后的用户资料。

### 8.3 获取我的收藏

```http
GET /api/users/me/favorites
```

需要登录。

返回 `data`：菜品列表，字段同 `DishCard`。

前端页面对应：

- 个人页收藏列表
- 收藏里的菜品点击进入菜品详情页

### 8.4 添加收藏

```http
POST /api/users/me/favorites
```

需要登录。

请求体：

```json
{
  "dishId": 2
}
```

返回：

```json
{
  "code": 0,
  "message": "ok",
  "data": null
}
```

### 8.5 取消收藏

```http
DELETE /api/users/me/favorites/{dishId}
```

需要登录。

返回同添加收藏。

前端页面对应：

- 菜品详情页收藏按钮
- 个人页收藏列表

## 9. 管理员接口

管理员接口前端主 App 可以先不接，验收时可以用 Swagger 展示。

需要管理员账号，并且 token 对应用户角色为 `ADMIN`。

### 9.1 获取待审核补充信息

```http
GET /api/admin/submissions
```

### 9.2 通过补充信息

```http
POST /api/admin/submissions/{id}/approve
```

请求体：

```json
{
  "reason": "信息完整，审核通过"
}
```

### 9.3 拒绝补充信息

```http
POST /api/admin/submissions/{id}/reject
```

请求体：

```json
{
  "reason": "图片或菜品信息不清楚"
}
```

### 9.4 发布公告

```http
POST /api/admin/announcements
```

请求体：

```json
{
  "title": "今日公告",
  "content": "丁香食堂晚餐新增特色窗口。"
}
```

## 10. 前端页面接口对应表

| 前端页面 | 需要调用的接口 |
| --- | --- |
| 联调检查 | `GET /api/health` |
| 登录页 | `POST /api/auth/login` |
| 注册页 | `POST /api/auth/register` |
| 首页公告 | `GET /api/announcements` |
| 首页推荐菜品 | `GET /api/dishes/recommendations?limit=6` |
| 首页推荐食堂 | `GET /api/canteens?sort=rating` |
| 美食广场 | `GET /api/canteens`、`GET /api/dishes`、`GET /api/dishes/tags` |
| 食堂详情 | `GET /api/canteens/{id}` |
| 菜品详情 | `GET /api/dishes/{id}` |
| 写评价 | `POST /api/dishes/{dishId}/reviews` |
| 补充菜品信息 | `POST /api/dishes/submissions` |
| 个人页资料 | `GET /api/users/me` |
| 我的收藏 | `GET /api/users/me/favorites` |
| 收藏/取消收藏 | `POST /api/users/me/favorites`、`DELETE /api/users/me/favorites/{dishId}` |

## 11. 前端优先接入顺序

建议前端按这个顺序接，不容易乱：

1. `GET /api/health`，确认 Base URL 可用。
2. 登录/注册，保存 token。
3. 首页公告、推荐菜品、推荐食堂。
4. 美食广场食堂列表、菜品列表、标签列表。
5. 食堂详情页。
6. 菜品详情页和评价列表。
7. 写评价。
8. 收藏和我的收藏。
9. 补充菜品信息。

## 12. 联调注意事项

- Android 真机访问后端时，Base URL 必须是后端电脑局域网 IP，不是 `localhost`。
- 请求失败时先看 `code` 和 `message`，再看后端控制台日志。
- 需要登录的接口如果返回 401，优先检查是否加了 `Authorization: Bearer token`。
- 图片上传暂时不作为核心联调项，先传 `imageUrl: null`。
- 前端可以保留静态 mock 数据作为兜底，但验收主流程应优先展示真实接口数据。
