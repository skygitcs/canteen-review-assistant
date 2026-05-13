# API 接口文档

基础地址：`http://localhost:8080`

需要登录的接口添加请求头：

```text
Authorization: Bearer <token>
```

## 认证

### 注册

`POST /api/auth/register`

```json
{
  "username": "alice",
  "password": "123456",
  "nickname": "爱吃桃李园"
}
```

### 登录

`POST /api/auth/login`

```json
{
  "username": "alice",
  "password": "123456"
}
```

返回：

```json
{
  "code": 0,
  "message": "ok",
  "data": {
    "token": "...",
    "user": {
      "id": 1,
      "username": "alice",
      "nickname": "爱吃桃李园",
      "role": "USER"
    }
  }
}
```

## 食堂

### 食堂列表

`GET /api/canteens?keyword=桃李&onCampus=true&sort=rating`

`sort` 可选：`rating`、`crowd`。

### 食堂详情

`GET /api/canteens/{id}?floorNo=1&tag=辣&sort=rating`

返回食堂基础信息、窗口列表、菜品卡片列表。

### 上报拥挤度

`POST /api/canteens/{id}/crowd`

```json
{
  "level": 3
}
```

### 热力图

`GET /api/canteens/heatmap?scope=global`

`scope` 可选：`global`、`mine`。`mine` 需要登录，用于个人校园卡热力图。

## 菜品

### 菜品搜索

`GET /api/dishes?keyword=麻婆豆腐&canteenId=1&floorNo=1&tag=川味&sort=rating`

`sort` 可选：`rating`、`favorite`、`price`。

### 菜品详情

`GET /api/dishes/{id}`

返回图片、价格、标签、评分、收藏数、评论区。

### 今日推荐

`GET /api/dishes/recommendations?limit=10`

### 上传新菜品

`POST /api/dishes/submissions`

```json
{
  "canteenId": 1,
  "windowId": 1,
  "name": "番茄牛腩",
  "imageUrl": "https://example.com/images/tomato-beef.jpg",
  "price": 18.00,
  "description": "酸甜口，适合晚餐。",
  "spiceLevel": 0,
  "tags": ["下饭", "不辣"]
}
```

## 评论与点赞

### 发布评论

`POST /api/dishes/{dishId}/reviews`

```json
{
  "rating": 5,
  "content": "分量足，味道稳定。",
  "imageUrl": "https://example.com/review.jpg"
}
```

### 点赞/点踩评论

`POST /api/reviews/{reviewId}/vote`

```json
{
  "vote": 1
}
```

`vote = 1` 表示点赞，`vote = -1` 表示点踩。

## 用户中心

### 当前用户

`GET /api/users/me`

### 修改资料

`PUT /api/users/me`

```json
{
  "nickname": "今天想吃辣",
  "avatarUrl": "https://example.com/avatar.png",
  "tastePreference": "川味,面食",
  "campusCardAuthorized": true
}
```

### 收藏

`GET /api/users/me/favorites`

`POST /api/users/me/favorites`

```json
{
  "dishId": 1
}
```

`DELETE /api/users/me/favorites/{dishId}`

## 公告

`GET /api/announcements`

## 管理员

### 待审核上传

`GET /api/admin/submissions`

### 审核通过

`POST /api/admin/submissions/{id}/approve`

```json
{
  "reason": "信息完整，图片合规"
}
```

### 驳回

`POST /api/admin/submissions/{id}/reject`

```json
{
  "reason": "图片不清晰"
}
```

### 发布公告

`POST /api/admin/announcements`

```json
{
  "title": "美食节活动",
  "content": "本周五桃李园推出限时套餐。"
}
```
