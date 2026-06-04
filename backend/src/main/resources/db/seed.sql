USE thu_canteen;

INSERT INTO users (id, username, password_hash, nickname, avatar_url, role, taste_preference, campus_card_authorized, status) VALUES
(1, 'demo', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjZAgcfl7p92ldGxad68LJZdL17lhWy', '清华吃饭人', NULL, 'USER', '清淡,少辣', 0, 1),
(2, 'reviewer', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjZAgcfl7p92ldGxad68LJZdL17lhWy', '紫荆常客', NULL, 'USER', '辣味,下饭', 0, 1),
(3, 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjZAgcfl7p92ldGxad68LJZdL17lhWy', '管理员', NULL, 'ADMIN', NULL, 0, 1)
ON DUPLICATE KEY UPDATE
    nickname = VALUES(nickname),
    role = VALUES(role),
    taste_preference = VALUES(taste_preference),
    campus_card_authorized = VALUES(campus_card_authorized),
    status = VALUES(status);

INSERT INTO canteens (id, name, cover_url, address, open_hours, pay_methods, on_campus, latitude, longitude, status) VALUES
(1, '紫荆', NULL, '紫荆学生公寓区', '06:30-22:00', '校园卡,微信,支付宝', 1, 40.0090500, 116.3339000, 1),
(2, '桃李', NULL, '清华大学学生区', '06:30-21:00', '校园卡,微信,支付宝', 1, 40.0041100, 116.3268700, 1),
(3, '清芬', NULL, '清华大学中部教学区附近', '07:00-20:30', '校园卡,微信,支付宝', 1, 40.0032500, 116.3292800, 1),
(4, '玉树', NULL, '清华大学西北生活区', '07:00-20:30', '校园卡,微信', 1, 40.0101200, 116.3243100, 1),
(5, '听涛', NULL, '清华大学东北区', '07:00-20:30', '校园卡,微信', 1, 40.0066600, 116.3315200, 1),
(6, '观畴', NULL, '清华大学南部生活区', '07:00-21:00', '校园卡,微信,支付宝', 1, 39.9997600, 116.3261800, 1),
(7, '丁香', NULL, '丁香园学生公寓附近', '06:30-22:00', '校园卡,微信,支付宝', 1, 40.0078800, 116.3362400, 1)
ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    cover_url = VALUES(cover_url),
    address = VALUES(address),
    open_hours = VALUES(open_hours),
    pay_methods = VALUES(pay_methods),
    on_campus = VALUES(on_campus),
    latitude = VALUES(latitude),
    longitude = VALUES(longitude),
    status = VALUES(status);

INSERT INTO canteen_windows (id, canteen_id, floor_no, name, open_hours, status) VALUES
(1, 1, 1, '家常菜窗口', '10:30-13:30,16:30-19:30', 1),
(2, 1, 1, '风味小炒', '10:30-13:30,16:30-20:00', 1),
(3, 1, 2, '轻食面点', '07:00-20:30', 1),
(4, 2, 1, '经典套餐', '10:30-13:30,16:30-19:30', 1),
(5, 2, 2, '面食窗口', '10:30-20:30', 1),
(6, 2, 2, '清淡蒸菜', '10:30-13:30,16:30-19:30', 1),
(7, 3, 1, '热炒窗口', '10:30-13:30,16:30-19:30', 1),
(8, 3, 2, '西式简餐', '10:30-20:00', 1),
(9, 4, 1, '盖饭窗口', '10:30-13:30,16:30-19:30', 1),
(10, 4, 2, '汤粉窗口', '10:30-20:30', 1),
(11, 5, 1, '早餐粥点', '06:30-10:00', 1),
(12, 5, 1, '下饭小炒', '10:30-13:30,16:30-19:30', 1),
(13, 6, 1, '荤素套餐', '10:30-13:30,16:30-19:30', 1),
(14, 6, 2, '特色粉面', '10:30-20:30', 1),
(15, 7, 1, '快餐窗口', '10:30-13:30,16:30-19:30', 1),
(16, 7, 2, '甜品饮品', '11:00-20:30', 1)
ON DUPLICATE KEY UPDATE
    canteen_id = VALUES(canteen_id),
    floor_no = VALUES(floor_no),
    name = VALUES(name),
    open_hours = VALUES(open_hours),
    status = VALUES(status);

INSERT INTO dishes (id, canteen_id, window_id, name, image_url, price, description, spice_level, status) VALUES
(1, 1, 1, '番茄炒蛋', NULL, 8.00, '酸甜口味，适合配米饭。', 0, 'APPROVED'),
(2, 1, 2, '水煮牛肉', NULL, 16.00, '偏辣，分量较足，适合想吃重口味的时候。', 4, 'APPROVED'),
(3, 1, 3, '鸡胸肉沙拉', NULL, 16.00, '蔬菜量足，口味清爽。', 0, 'APPROVED'),
(4, 1, 2, '宫保鸡丁', NULL, 12.00, '甜辣口，花生很香。', 2, 'APPROVED'),
(5, 2, 4, '红烧排骨', NULL, 18.00, '偏甜口，肉量稳定。', 0, 'APPROVED'),
(6, 2, 5, '牛肉拉面', NULL, 14.00, '汤底清爽，出餐速度快。', 1, 'APPROVED'),
(7, 2, 6, '清蒸鱼块', NULL, 17.00, '口味清淡，适合不想吃油腻的时候。', 0, 'APPROVED'),
(8, 2, 4, '土豆炖牛腩', NULL, 15.00, '软烂入味，适合晚餐。', 1, 'APPROVED'),
(9, 3, 7, '麻婆豆腐', NULL, 8.00, '微麻微辣，性价比高。', 3, 'APPROVED'),
(10, 3, 7, '鱼香肉丝', NULL, 11.00, '酸甜辣都有，比较下饭。', 2, 'APPROVED'),
(11, 3, 8, '黑椒鸡排饭', NULL, 18.00, '黑椒味明显，肉量足。', 1, 'APPROVED'),
(12, 4, 9, '咖喱鸡块饭', NULL, 13.00, '咖喱味浓，土豆软糯。', 1, 'APPROVED'),
(13, 4, 9, '香菇滑鸡饭', NULL, 14.00, '口味温和，汤汁拌饭不错。', 0, 'APPROVED'),
(14, 4, 10, '酸辣粉', NULL, 10.00, '酸辣开胃，粉比较劲道。', 4, 'APPROVED'),
(15, 5, 11, '皮蛋瘦肉粥', NULL, 6.00, '早餐热粥，口味清淡。', 0, 'APPROVED'),
(16, 5, 11, '鲜肉包子', NULL, 3.00, '早餐常规选择，出餐快。', 0, 'APPROVED'),
(17, 5, 12, '辣子鸡丁', NULL, 15.00, '比较辣，适合重口味。', 5, 'APPROVED'),
(18, 6, 13, '照烧鸡腿饭', NULL, 16.00, '甜咸口，鸡腿肉比较嫩。', 0, 'APPROVED'),
(19, 6, 13, '蒜蓉西兰花', NULL, 7.00, '清淡蔬菜，适合搭配主菜。', 0, 'APPROVED'),
(20, 6, 14, '重庆小面', NULL, 9.00, '麻辣味明显，适合午饭。', 4, 'APPROVED'),
(21, 7, 15, '糖醋里脊', NULL, 14.00, '酸甜口，比较受欢迎。', 0, 'APPROVED'),
(22, 7, 15, '香辣鸡腿堡', NULL, 13.00, '辣味适中，适合赶时间。', 3, 'APPROVED'),
(23, 7, 16, '红豆双皮奶', NULL, 8.00, '甜度适中，饭后小甜品。', 0, 'APPROVED'),
(24, 7, 16, '柠檬冰茶', NULL, 6.00, '清爽解腻，适合夏天。', 0, 'APPROVED')
ON DUPLICATE KEY UPDATE
    canteen_id = VALUES(canteen_id),
    window_id = VALUES(window_id),
    name = VALUES(name),
    image_url = VALUES(image_url),
    price = VALUES(price),
    description = VALUES(description),
    spice_level = VALUES(spice_level),
    status = VALUES(status);

INSERT INTO dish_tags (dish_id, tag) VALUES
(1, '清淡'), (1, '家常'), (1, '下饭'),
(2, '辣味'), (2, '荤菜'), (2, '下饭'),
(3, '清淡'), (3, '轻食'), (3, '高蛋白'),
(4, '甜辣'), (4, '荤菜'), (4, '下饭'),
(5, '荤菜'), (5, '家常'), (5, '下饭'),
(6, '面食'), (6, '热汤'), (6, '荤菜'),
(7, '清淡'), (7, '鱼类'), (7, '高蛋白'),
(8, '家常'), (8, '荤菜'), (8, '下饭'),
(9, '辣味'), (9, '豆制品'), (9, '下饭'),
(10, '酸甜'), (10, '辣味'), (10, '下饭'),
(11, '西式'), (11, '高蛋白'), (11, '荤菜'),
(12, '咖喱'), (12, '盖饭'), (12, '下饭'),
(13, '清淡'), (13, '盖饭'), (13, '荤菜'),
(14, '酸辣'), (14, '粉面'), (14, '辣味'),
(15, '早餐'), (15, '清淡'), (15, '热粥'),
(16, '早餐'), (16, '面点'),
(17, '辣味'), (17, '荤菜'), (17, '下饭'),
(18, '盖饭'), (18, '荤菜'), (18, '甜咸'),
(19, '清淡'), (19, '素菜'), (19, '健康'),
(20, '辣味'), (20, '面食'), (20, '麻辣'),
(21, '酸甜'), (21, '荤菜'), (21, '家常'),
(22, '辣味'), (22, '快餐'), (22, '荤菜'),
(23, '甜品'), (23, '清凉'),
(24, '饮品'), (24, '清爽')
ON DUPLICATE KEY UPDATE tag = VALUES(tag);

INSERT INTO reviews (id, dish_id, user_id, rating, content, image_url, status) VALUES
(1, 1, 1, 5, '番茄味很足，拌饭刚好。', NULL, 'APPROVED'),
(2, 2, 1, 4, '味道不错，但中午排队比较久。', NULL, 'APPROVED'),
(3, 2, 2, 5, '辣度很合适，肉量也可以。', NULL, 'APPROVED'),
(4, 5, 1, 4, '排骨偏甜，晚饭吃比较满足。', NULL, 'APPROVED'),
(5, 6, 2, 4, '出餐快，汤底清爽。', NULL, 'APPROVED'),
(6, 9, 1, 5, '便宜又下饭，常点。', NULL, 'APPROVED'),
(7, 14, 2, 4, '酸辣味比较重，适合换口味。', NULL, 'APPROVED'),
(8, 15, 1, 4, '早上喝一碗很舒服。', NULL, 'APPROVED'),
(9, 17, 2, 5, '确实很辣，很香。', NULL, 'APPROVED'),
(10, 21, 1, 4, '酸甜口稳定，适合不知道吃什么的时候。', NULL, 'APPROVED')
ON DUPLICATE KEY UPDATE
    rating = VALUES(rating),
    content = VALUES(content),
    image_url = VALUES(image_url),
    status = VALUES(status);

INSERT INTO favorites (user_id, dish_id) VALUES
(1, 1),
(1, 2),
(1, 9),
(1, 21),
(2, 2),
(2, 17),
(2, 20)
ON DUPLICATE KEY UPDATE dish_id = VALUES(dish_id);

INSERT INTO crowd_reports (id, canteen_id, user_id, level, created_at) VALUES
(1, 1, 1, 3, NOW() - INTERVAL 10 MINUTE),
(2, 1, 2, 4, NOW() - INTERVAL 20 MINUTE),
(3, 2, 1, 2, NOW() - INTERVAL 15 MINUTE),
(4, 3, 2, 3, NOW() - INTERVAL 25 MINUTE),
(5, 7, 1, 4, NOW() - INTERVAL 5 MINUTE)
ON DUPLICATE KEY UPDATE
    canteen_id = VALUES(canteen_id),
    user_id = VALUES(user_id),
    level = VALUES(level),
    created_at = VALUES(created_at);

INSERT INTO announcements (id, title, content, starts_at, ends_at, status) VALUES
(1, '紫荆食堂上新', '紫荆二楼轻食面点窗口新增鸡胸肉沙拉，欢迎体验。', NOW() - INTERVAL 1 DAY, NOW() + INTERVAL 30 DAY, 1),
(2, '桃李晚餐时间调整', '考试周期间桃李食堂晚餐供应延长至 20:30。', NOW() - INTERVAL 1 DAY, NOW() + INTERVAL 14 DAY, 1),
(3, '丁香甜品窗口开放', '丁香二楼甜品饮品窗口本周试运行，部分饮品有优惠。', NOW() - INTERVAL 1 DAY, NOW() + INTERVAL 14 DAY, 1)
ON DUPLICATE KEY UPDATE
    title = VALUES(title),
    content = VALUES(content),
    starts_at = VALUES(starts_at),
    ends_at = VALUES(ends_at),
    status = VALUES(status);

INSERT INTO consumption_records (id, user_id, canteen_id, amount, consumed_at) VALUES
(1, 1, 1, 16.00, NOW() - INTERVAL 2 DAY),
(2, 1, 2, 14.00, NOW() - INTERVAL 1 DAY),
(3, 1, 7, 22.00, NOW() - INTERVAL 4 HOUR),
(4, 2, 1, 18.00, NOW() - INTERVAL 3 DAY),
(5, 2, 5, 15.00, NOW() - INTERVAL 1 DAY)
ON DUPLICATE KEY UPDATE
    user_id = VALUES(user_id),
    canteen_id = VALUES(canteen_id),
    amount = VALUES(amount),
    consumed_at = VALUES(consumed_at);
