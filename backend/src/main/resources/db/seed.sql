USE thu_canteen;

INSERT INTO canteens (id, name, cover_url, address, open_hours, pay_methods, on_campus, latitude, longitude) VALUES
(1, '桃李园', 'https://example.com/images/taoli.jpg', '清华大学学生区', '06:30-21:00', '校园卡,微信,支付宝', 1, 40.0041100, 116.3268700),
(2, '紫荆园', 'https://example.com/images/zijing.jpg', '紫荆学生公寓区', '06:30-22:00', '校园卡,微信,支付宝', 1, 40.0090500, 116.3339000),
(3, '听涛园', 'https://example.com/images/tingtao.jpg', '清华大学东北区', '07:00-20:30', '校园卡,微信', 1, 40.0066600, 116.3315200)
ON DUPLICATE KEY UPDATE name = VALUES(name);

INSERT INTO canteen_windows (id, canteen_id, floor_no, name, open_hours) VALUES
(1, 1, 1, '家常菜窗口', '10:30-13:30,16:30-19:30'),
(2, 1, 2, '面食窗口', '10:30-20:30'),
(3, 2, 1, '风味小炒', '10:30-13:30,16:30-20:00'),
(4, 2, 2, '轻食窗口', '10:30-20:00'),
(5, 3, 1, '早餐与粥粉', '07:00-10:00,16:30-19:30')
ON DUPLICATE KEY UPDATE name = VALUES(name);

INSERT INTO dishes (id, canteen_id, window_id, name, image_url, price, description, spice_level, status) VALUES
(1, 1, 1, '麻婆豆腐', 'https://example.com/images/mapo.jpg', 8.00, '微麻微辣，下饭稳定。', 3, 'APPROVED'),
(2, 1, 2, '牛肉拉面', 'https://example.com/images/noodle.jpg', 14.00, '汤底清爽，出餐速度快。', 1, 'APPROVED'),
(3, 2, 3, '宫保鸡丁', 'https://example.com/images/kungpao.jpg', 12.00, '甜辣口，花生很香。', 2, 'APPROVED'),
(4, 2, 4, '鸡胸沙拉', 'https://example.com/images/salad.jpg', 16.00, '适合轻食和健身餐。', 0, 'APPROVED'),
(5, 3, 5, '皮蛋瘦肉粥', 'https://example.com/images/congee.jpg', 6.00, '早餐热粥，口味清淡。', 0, 'APPROVED')
ON DUPLICATE KEY UPDATE name = VALUES(name);

INSERT INTO dish_tags (dish_id, tag) VALUES
(1, '川味'), (1, '下饭'), (1, '辣'),
(2, '面食'), (2, '热汤'),
(3, '小炒'), (3, '甜辣'),
(4, '轻食'), (4, '高蛋白'),
(5, '早餐'), (5, '清淡')
ON DUPLICATE KEY UPDATE tag = VALUES(tag);

INSERT INTO announcements (id, title, content, starts_at, ends_at, status) VALUES
(1, '紫荆园新品试吃', '本周二楼轻食窗口上新鸡胸沙拉套餐。', NOW() - INTERVAL 1 DAY, NOW() + INTERVAL 30 DAY, 1),
(2, '桃李园营业时间调整', '考试周晚餐延长至 20:30。', NOW() - INTERVAL 1 DAY, NOW() + INTERVAL 14 DAY, 1)
ON DUPLICATE KEY UPDATE title = VALUES(title);
