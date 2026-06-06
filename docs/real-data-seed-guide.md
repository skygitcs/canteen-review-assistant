# 真实食堂数据入库指南

本文档用于把共享表格中的真实食堂、窗口、菜品、标签、图片和简单评价整理进 `backend/src/main/resources/db/seed.sql`。

## 1. 表格到数据库的对应关系

| 表格字段 | 数据库位置 | 说明 |
| --- | --- | --- |
| 食堂名 | `canteens.name` | 紫荆、桃李、清芬、玉树、听涛、观畴、丁香 |
| 楼层 | `canteen_windows.floor_no` | 只填数字 |
| 窗口名 | `canteen_windows.name` | 同一食堂同一楼层下尽量不要重名 |
| 菜品名称 | `dishes.name` | 同一窗口内尽量不要重复 |
| 价格 | `dishes.price` | 可以为空，入库时用 `0` 或实际价格 |
| 菜品图片 | `dishes.image_url` | 推荐 `/uploads/dishes/xxx.jpg` |
| tag | `dish_tags.tag` | 多个标签拆成多行插入 |
| 简单评价 | `reviews.content` | 可作为 seed 中的演示评论 |
| 推荐评分 | `reviews.rating` | 1-5 |

## 2. 图片记录方式

当前后端支持展示已有静态图片，不做真实 multipart 上传。

图片文件放在：

```text
backend/uploads/dishes/
```

文件名建议使用拼音或英文，不要用中文：

```text
zijing_fanqie_chaodan.jpg
taoli_niurou_lamian.jpg
dingxiang_hongdou_shuangpinai.jpg
```

`seed.sql` 中 `image_url` 填：

```text
/uploads/dishes/zijing_fanqie_chaodan.jpg
```

Android 实际访问时会拼成：

```text
http://后端IP:8080/uploads/dishes/zijing_fanqie_chaodan.jpg
```

没有图片时填 `NULL`。

## 3. seed 编写顺序

必须按外键依赖顺序写：

1. `users`
2. `canteens`
3. `canteen_windows`
4. `dishes`
5. `dish_tags`
6. `reviews`
7. `favorites`
8. `announcements`

不要先插入菜品标签或评论，因为它们依赖菜品 ID。

## 4. 推荐 ID 规则

为了方便前端和测试复现，seed 里建议显式写 ID。

例如：

```sql
INSERT INTO canteens (id, name, cover_url, address, open_hours, pay_methods, on_campus, latitude, longitude, status) VALUES
(1, '紫荆', NULL, '紫荆学生公寓区', '06:30-22:00', '校园卡,微信,支付宝', 1, 40.0090500, 116.3339000, 1)
ON DUPLICATE KEY UPDATE name = VALUES(name);
```

窗口和菜品也同理，保持 ID 稳定。

## 5. 标签规范

优先使用这些标准标签：

```text
辣味
微辣
清淡
大块肉
荤菜
素菜
下饭
面食
粉面
早餐
甜口
酸甜
高蛋白
轻食
汤类
饮品
甜品
快餐
家常
实惠
```

表格里出现别名时，入库前统一：

| 表格写法 | 入库标签 |
| --- | --- |
| 辣、麻辣、很辣 | 辣味 |
| 不辣、淡口 | 清淡 |
| 肉多、肉量足 | 大块肉 |
| 拌饭、米饭搭子 | 下饭 |
| 面、拉面、拌面 | 面食 |

## 6. 导入后验证

每次改完 `seed.sql` 后，建议删库重导：

```sql
DROP DATABASE IF EXISTS thu_canteen;
SOURCE E:/canteen-review/canteen-review-assistant/backend/src/main/resources/db/schema.sql;
SOURCE E:/canteen-review/canteen-review-assistant/backend/src/main/resources/db/seed.sql;
```

注意进入 MySQL 时要带 UTF-8：

```powershell
& "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" --default-character-set=utf8mb4 -u root -p
```

检查数量：

```sql
USE thu_canteen;
SELECT COUNT(*) FROM canteens;
SELECT COUNT(*) FROM canteen_windows;
SELECT COUNT(*) FROM dishes;
SELECT COUNT(*) FROM dish_tags;
SELECT COUNT(*) FROM reviews;
```

然后启动后端，跑：

```powershell
powershell -ExecutionPolicy Bypass -File backend/scripts/smoke-test.ps1
```

通过后再让前端看页面展示效果。

## 7. 管理员审核相关

用户补充菜品会进入：

```text
dish_submissions.audit_status = PENDING
```

管理员通过接口审核：

```http
GET /api/admin/submissions?status=PENDING
POST /api/admin/submissions/{id}/approve
POST /api/admin/submissions/{id}/reject
```

评论审核/隐藏：

```http
GET /api/admin/reviews?status=APPROVED
POST /api/admin/reviews/{id}/approve
POST /api/admin/reviews/{id}/reject
```

评论被 reject 后不会在菜品详情页展示。
