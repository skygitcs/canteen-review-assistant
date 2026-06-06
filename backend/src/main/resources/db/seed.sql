SET NAMES utf8mb4;
USE thu_canteen;

-- 开发演示数据：重新导入前清空旧 seed 数据，避免旧 demo 菜品和标签残留。
SET FOREIGN_KEY_CHECKS = 0;
DELETE FROM review_votes;
ALTER TABLE review_votes AUTO_INCREMENT = 1;
DELETE FROM favorites;
ALTER TABLE favorites AUTO_INCREMENT = 1;
DELETE FROM reviews;
ALTER TABLE reviews AUTO_INCREMENT = 1;
DELETE FROM dish_tags;
ALTER TABLE dish_tags AUTO_INCREMENT = 1;
DELETE FROM dish_submissions;
ALTER TABLE dish_submissions AUTO_INCREMENT = 1;
DELETE FROM dishes;
ALTER TABLE dishes AUTO_INCREMENT = 1;
DELETE FROM canteen_windows;
ALTER TABLE canteen_windows AUTO_INCREMENT = 1;
DELETE FROM crowd_reports;
ALTER TABLE crowd_reports AUTO_INCREMENT = 1;
DELETE FROM announcements;
ALTER TABLE announcements AUTO_INCREMENT = 1;
DELETE FROM consumption_records;
ALTER TABLE consumption_records AUTO_INCREMENT = 1;
DELETE FROM canteens;
ALTER TABLE canteens AUTO_INCREMENT = 1;
DELETE FROM users;
ALTER TABLE users AUTO_INCREMENT = 1;
SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO users (id, username, password_hash, nickname, avatar_url, role, taste_preference, campus_card_authorized, status) VALUES
(1, 'demo', '$2a$10$o6Js5uUIk.IlN5.vWnG7xuDgWYm77MtFeWT3x/Zha/20bN6THD99q', '演示用户', NULL, 'USER', '清淡,实惠', 0, 1),
(2, 'reviewer', '$2a$10$o6Js5uUIk.IlN5.vWnG7xuDgWYm77MtFeWT3x/Zha/20bN6THD99q', '紫荆常客', NULL, 'USER', '辣味,下饭', 0, 1),
(3, 'admin', '$2a$10$o6Js5uUIk.IlN5.vWnG7xuDgWYm77MtFeWT3x/Zha/20bN6THD99q', '管理员', NULL, 'ADMIN', NULL, 0, 1);

INSERT INTO canteens (id, name, cover_url, address, open_hours, pay_methods, on_campus, latitude, longitude, status) VALUES
(1, '紫荆', NULL, '紫荆学生公寓区', '06:30-22:00', '校园卡,微信,支付宝', 1, 40.0090500, 116.3339000, 1),
(2, '桃李', NULL, '清华大学学生区', '06:30-21:00', '校园卡,微信,支付宝', 1, 40.0041100, 116.3268700, 1),
(3, '清芬', NULL, '清华大学中部教学区附近', '07:00-20:30', '校园卡,微信,支付宝', 1, 40.0032500, 116.3292800, 1),
(4, '玉树', NULL, '清华大学西北生活区', '07:00-20:30', '校园卡,微信', 1, 40.0101200, 116.3243100, 1),
(5, '听涛', NULL, '清华大学东北区', '07:00-20:30', '校园卡,微信', 1, 40.0066600, 116.3315200, 1),
(6, '观畴', NULL, '清华大学南部生活区', '07:00-21:00', '校园卡,微信,支付宝', 1, 39.9997600, 116.3261800, 1),
(7, '丁香', NULL, '丁香园学生公寓附近', '06:30-22:00', '校园卡,微信,支付宝', 1, 40.0078800, 116.3362400, 1);

INSERT INTO canteen_windows (id, canteen_id, floor_no, name, open_hours, status) VALUES
(1, 2, 1, '麻辣烫', '按食堂实际开放时间为准', 1),
(2, 2, 1, '西式面包', '按食堂实际开放时间为准', 1),
(3, 2, 1, '煎饼手抓饼', '按食堂实际开放时间为准', 1),
(4, 2, 1, '烤烙', '按食堂实际开放时间为准', 1),
(5, 2, 1, '米粉', '按食堂实际开放时间为准', 1),
(6, 2, 1, '冷饮热饮', '按食堂实际开放时间为准', 1),
(7, 3, 1, '小吃', '按食堂实际开放时间为准', 1),
(8, 3, 1, '烤盘饭', '按食堂实际开放时间为准', 1),
(9, 3, 1, '麻辣烫', '按食堂实际开放时间为准', 1),
(10, 3, 1, '土豆粉', '按食堂实际开放时间为准', 1),
(11, 3, 1, '西北风味', '按食堂实际开放时间为准', 1),
(12, 3, 2, '精品菜', '按食堂实际开放时间为准', 1),
(13, 3, 2, '普通伙食', '按食堂实际开放时间为准', 1),
(14, 5, 1, '水果', '按食堂实际开放时间为准', 1),
(15, 5, 1, '西北风味', '按食堂实际开放时间为准', 1),
(16, 5, 1, '饺子', '按食堂实际开放时间为准', 1),
(17, 1, 2, '涮羊肉', '按食堂实际开放时间为准', 1),
(18, 1, 1, '中式套餐', '按食堂实际开放时间为准', 1),
(19, 1, 2, '花样主食', '按食堂实际开放时间为准', 1),
(20, 1, 2, '冷饮热饮', '按食堂实际开放时间为准', 1),
(21, 1, 3, '东北风味', '按食堂实际开放时间为准', 1);

INSERT INTO dishes (id, canteen_id, window_id, name, image_url, price, description, spice_level, status) VALUES
(1, 2, 1, '麻辣烫', '/uploads/dishes/taoli_malatang.jpg', 0.00, '可选麻辣和番茄锅底，同窗口还有冒烤鸭价格按称重计量。', 4, 'APPROVED'),
(2, 2, 2, '面包', '/uploads/dishes/taoli_bread.jpg', 3.50, '有红果/葡萄/酵母/黄油/椰蓉/红豆/维生素多种口味表格记录价格区间为3.5~6元。', 0, 'APPROVED'),
(3, 2, 3, '绿豆面煎饼', '/uploads/dishes/taoli_jianbing.jpg', 4.00, '有火腿肠、辣条、培根、鸡柳、海带、生菜等配菜可加表格记录价格区间为4~12元。', 0, 'APPROVED'),
(4, 2, 4, '羊肉烧麦', '/uploads/dishes/taoli_shaomai.jpg', 2.90, '比较大个的羊肉烧麦', 0, 'APPROVED'),
(5, 2, 4, '小米粥', '/uploads/dishes/taoli_xiaomizhou.jpg', 0.40, '来自烤烙窗口，适合想吃早餐，清淡，实惠，家常类菜品的时候。', 0, 'APPROVED'),
(6, 2, 5, '鸡腿肉热拌粉', '/uploads/dishes/taoli_jituiroubanfen.jpg', 11.00, '来自米粉窗口，适合想吃荤菜，粉面类菜品的时候。', 0, 'APPROVED'),
(7, 2, 4, '鸡蛋煎饼', '/uploads/dishes/taoli_jidanjianbing.jpg', 4.00, '来自烤烙窗口，适合想吃早餐类菜品的时候。', 0, 'APPROVED'),
(8, 2, 3, '手抓饼', '/uploads/dishes/taoli_shouzhuabing.jpg', 3.50, '来自煎饼手抓饼窗口，适合想吃早餐类菜品的时候。表格记录价格区间为3.5~12元。', 0, 'APPROVED'),
(9, 2, 6, '可乐', '/uploads/dishes/taoli_kele.jpg', 1.50, '一杯可乐', 0, 'APPROVED'),
(10, 3, 7, '山葵炸鸡', '/uploads/dishes/qingfen_zhaji.jpg', 0.00, '来自小吃窗口，适合想吃荤菜，快餐，实惠类菜品的时候。价格按称重计量。', 0, 'APPROVED'),
(11, 3, 7, '三明治', '/uploads/dishes/qingfen_sanmingzhi.jpg', 2.00, '火腿鸡蛋三明治', 0, 'APPROVED'),
(12, 3, 8, '烤盘饭', '/uploads/dishes/qingfen_kaopanfan.jpg', 0.00, '来自烤盘饭窗口，适合想吃荤菜，大块肉，高蛋白类菜品的时候。价格按称重计量。', 0, 'APPROVED'),
(13, 3, 9, '清芬麻辣烫', '/uploads/dishes/qingfen_malatang.jpg', 0.00, '可选加麻酱和辣椒价格按称重计量。', 4, 'APPROVED'),
(14, 3, 10, '酸菜鱼砂锅面', '/uploads/dishes/qingfen_shaoguomian.jpg', 16.00, '来自土豆粉窗口，适合想吃面食类菜品的时候。', 0, 'APPROVED'),
(15, 3, 11, '酸汤水饺', '/uploads/dishes/qingfen_suantangshuijiao.jpg', 13.00, '来自西北风味窗口，适合想吃热汤类菜品的时候。', 2, 'APPROVED'),
(16, 3, 12, '麻辣草鱼', '/uploads/dishes/qingfen_malacaoyu.jpg', 6.00, '来自精品菜窗口，适合想吃大块肉，热汤类菜品的时候。表格记录价格为6（半份）。', 4, 'APPROVED'),
(17, 3, 12, '双椒牛肉', '/uploads/dishes/qingfen_shuangjiaoniurou.jpg', 7.00, '来自精品菜窗口，适合想吃荤菜类菜品的时候。表格记录价格为7（半份）。', 2, 'APPROVED'),
(18, 3, 12, '山楂话梅猪蹄', '/uploads/dishes/qingfen_shanzhahuameipaigu.jpg', 7.00, '来自精品菜窗口，适合想吃荤菜，大块肉，酸甜类菜品的时候。表格记录价格为7（半份）。', 0, 'APPROVED'),
(19, 3, 13, '肉末豆腐', '/uploads/dishes/qingfen_roumodoufu.jpg', 3.00, '来自普通伙食窗口，适合想吃微辣，下饭，家常类菜品的时候。表格记录价格为3（半份）。', 0, 'APPROVED'),
(20, 5, 14, '西瓜', '/uploads/dishes/tintao_xigua.jpg', 0.00, '来自水果窗口，适合想吃水果，甜品类菜品的时候。价格按称重计量。', 0, 'APPROVED'),
(21, 5, 15, '牛肉三丁面', '/uploads/dishes/tintao_niurousanding.jpg', 9.50, '来自西北风味窗口，适合想吃荤菜，粉面类菜品的时候。', 0, 'APPROVED'),
(22, 5, 15, '肉夹馍', '/uploads/dishes/tintao_roujiamo.jpg', 3.00, '来自西北风味窗口，适合想吃荤菜，面食类菜品的时候。', 0, 'APPROVED'),
(23, 5, 15, '羊杂汤', '/uploads/dishes/tintao_yangzatang.jpg', 10.50, '来自西北风味窗口，适合想吃荤菜，粉面类菜品的时候。', 0, 'APPROVED'),
(24, 5, 16, '牛肉胡萝卜饺子', '/uploads/dishes/tintao_shuijiao.jpg', 0.00, '来自饺子窗口，适合想吃荤菜，实惠类菜品的时候。价格按称重计量。', 0, 'APPROVED'),
(25, 1, 17, '涮羊肉', '/uploads/dishes/zijing_shuaiyangrou.jpg', 0.00, '来自涮羊肉窗口，适合想吃荤菜，实惠，热汤，微辣类菜品的时候。价格按称重计量。', 0, 'APPROVED'),
(26, 1, 18, '猪排饭', '/uploads/dishes/zijing_zhupaifan.jpg', 9.50, '来自中式套餐窗口，适合想吃荤菜，下饭，大块肉，实惠类菜品的时候。表格记录价格区间为9.5~12.8元。', 0, 'APPROVED'),
(27, 1, 19, '猪肉酸菜馅饼', '/uploads/dishes/zijing_suancaizhuroubing.jpg', 2.00, '来自花样主食窗口，适合想吃荤菜，面食，实惠类菜品的时候。', 0, 'APPROVED'),
(28, 1, 20, '西瓜', '/uploads/dishes/zijing_xigua.jpg', 0.00, '来自冷饮热饮窗口，适合想吃水果，甜品类菜品的时候。价格按称重计量。', 0, 'APPROVED'),
(29, 1, 21, '酸菜白肉', '/uploads/dishes/zijing_suancaibairou.jpg', 3.00, '来自东北风味窗口，适合想吃荤菜，实惠类菜品的时候。表格记录价格为3（半份）。', 0, 'APPROVED');

INSERT INTO dish_tags (dish_id, tag) VALUES
(1, '面食'),
(1, '热汤'),
(1, '荤菜'),
(1, '辣味'),
(1, '酸甜'),
(1, '称重计量'),
(2, '甜口'),
(2, '主食'),
(2, '早餐'),
(2, '甜品'),
(2, '实惠'),
(3, '早餐'),
(3, '实惠'),
(3, '主食'),
(3, '面食'),
(4, '荤菜，早餐，高蛋白'),
(4, '主食'),
(4, '高蛋白'),
(4, '实惠'),
(5, '早餐，清淡，实惠，家常'),
(5, '实惠'),
(6, '荤菜，粉面'),
(6, '主食'),
(6, '高蛋白'),
(7, '早餐'),
(7, '主食'),
(7, '高蛋白'),
(7, '面食'),
(7, '实惠'),
(8, '早餐'),
(8, '实惠'),
(8, '主食'),
(8, '面食'),
(9, '饮品，快餐，实惠'),
(9, '清爽'),
(9, '实惠'),
(10, '荤菜，快餐，实惠'),
(10, '高蛋白'),
(10, '称重计量'),
(11, '快餐，实惠'),
(11, '主食'),
(11, '实惠'),
(12, '荤菜，大块肉，高蛋白'),
(12, '称重计量'),
(13, '面食'),
(13, '热汤'),
(13, '荤菜'),
(13, '辣味'),
(13, '称重计量'),
(14, '面食'),
(14, '热汤'),
(14, '荤菜'),
(14, '微辣，粉面'),
(14, '主食'),
(14, '高蛋白'),
(15, '热汤'),
(15, '荤菜'),
(15, '微辣'),
(16, '大块肉，热汤'),
(16, '荤菜'),
(16, '辣味'),
(16, '高蛋白'),
(17, '荤菜'),
(17, '微辣'),
(17, '高蛋白'),
(18, '荤菜，大块肉，酸甜'),
(18, '高蛋白'),
(19, '微辣，下饭，家常'),
(19, '高蛋白'),
(19, '实惠'),
(20, '水果，甜品'),
(20, '清爽'),
(20, '称重计量'),
(21, '荤菜，粉面'),
(21, '主食'),
(21, '高蛋白'),
(22, '荤菜，面食'),
(22, '主食'),
(22, '高蛋白'),
(22, '实惠'),
(23, '荤菜，粉面'),
(23, '热汤'),
(24, '荤菜，实惠'),
(24, '高蛋白'),
(24, '称重计量'),
(25, '荤菜，实惠，热汤，微辣'),
(25, '高蛋白'),
(25, '称重计量'),
(26, '荤菜，下饭，大块肉，实惠'),
(26, '高蛋白'),
(27, '荤菜，面食，实惠'),
(27, '主食'),
(27, '高蛋白'),
(27, '实惠'),
(28, '水果，甜品'),
(28, '清爽'),
(28, '称重计量'),
(29, '荤菜，实惠'),
(29, '高蛋白'),
(29, '实惠');

INSERT INTO reviews (id, dish_id, user_id, rating, content, image_url, status) VALUES
(1, 1, 1, 5, '麻辣烫整体表现稳定，面食特点比较明显，适合作为桃李的一个备选。', NULL, 'APPROVED'),
(2, 2, 2, 4, '面包整体表现稳定，甜口特点比较明显，适合作为桃李的一个备选。', NULL, 'APPROVED'),
(3, 3, 1, 4, '绿豆面煎饼整体表现稳定，早餐特点比较明显，适合作为桃李的一个备选。', NULL, 'APPROVED'),
(4, 4, 2, 5, '羊肉烧麦整体表现稳定，荤菜，早餐，高蛋白特点比较明显，适合作为桃李的一个备选。', NULL, 'APPROVED'),
(5, 5, 1, 5, '小米粥整体表现稳定，早餐，清淡，实惠，家常特点比较明显，适合作为桃李的一个备选。', NULL, 'APPROVED'),
(6, 6, 2, 4, '鸡腿肉热拌粉整体表现稳定，荤菜，粉面特点比较明显，适合作为桃李的一个备选。', NULL, 'APPROVED'),
(7, 7, 1, 4, '鸡蛋煎饼整体表现稳定，早餐特点比较明显，适合作为桃李的一个备选。', NULL, 'APPROVED'),
(8, 8, 2, 5, '手抓饼整体表现稳定，早餐特点比较明显，适合作为桃李的一个备选。', NULL, 'APPROVED'),
(9, 9, 1, 5, '可乐整体表现稳定，饮品，快餐，实惠特点比较明显，适合作为桃李的一个备选。', NULL, 'APPROVED'),
(10, 10, 2, 4, '山葵炸鸡整体表现稳定，荤菜，快餐，实惠特点比较明显，适合作为清芬的一个备选。', NULL, 'APPROVED'),
(11, 11, 1, 4, '三明治整体表现稳定，快餐，实惠特点比较明显，适合作为清芬的一个备选。', NULL, 'APPROVED'),
(12, 12, 2, 5, '烤盘饭整体表现稳定，荤菜，大块肉，高蛋白特点比较明显，适合作为清芬的一个备选。', NULL, 'APPROVED'),
(13, 13, 1, 5, '清芬麻辣烫整体表现稳定，面食特点比较明显，适合作为清芬的一个备选。', NULL, 'APPROVED'),
(14, 14, 2, 4, '酸菜鱼砂锅面整体表现稳定，面食特点比较明显，适合作为清芬的一个备选。', NULL, 'APPROVED'),
(15, 15, 1, 4, '酸汤水饺整体表现稳定，热汤特点比较明显，适合作为清芬的一个备选。', NULL, 'APPROVED'),
(16, 16, 2, 5, '麻辣草鱼整体表现稳定，大块肉，热汤特点比较明显，适合作为清芬的一个备选。', NULL, 'APPROVED'),
(17, 17, 1, 5, '双椒牛肉整体表现稳定，荤菜特点比较明显，适合作为清芬的一个备选。', NULL, 'APPROVED'),
(18, 18, 2, 4, '山楂话梅猪蹄整体表现稳定，荤菜，大块肉，酸甜特点比较明显，适合作为清芬的一个备选。', NULL, 'APPROVED'),
(19, 19, 1, 4, '肉末豆腐整体表现稳定，微辣，下饭，家常特点比较明显，适合作为清芬的一个备选。', NULL, 'APPROVED'),
(20, 20, 2, 5, '西瓜整体表现稳定，水果，甜品特点比较明显，适合作为听涛的一个备选。', NULL, 'APPROVED'),
(21, 21, 1, 5, '牛肉三丁面整体表现稳定，荤菜，粉面特点比较明显，适合作为听涛的一个备选。', NULL, 'APPROVED'),
(22, 22, 2, 4, '肉夹馍整体表现稳定，荤菜，面食特点比较明显，适合作为听涛的一个备选。', NULL, 'APPROVED'),
(23, 23, 1, 4, '羊杂汤整体表现稳定，荤菜，粉面特点比较明显，适合作为听涛的一个备选。', NULL, 'APPROVED'),
(24, 24, 2, 5, '牛肉胡萝卜饺子整体表现稳定，荤菜，实惠特点比较明显，适合作为听涛的一个备选。', NULL, 'APPROVED'),
(25, 25, 1, 5, '涮羊肉整体表现稳定，荤菜，实惠，热汤，微辣特点比较明显，适合作为紫荆的一个备选。', NULL, 'APPROVED'),
(26, 26, 2, 4, '猪排饭整体表现稳定，荤菜，下饭，大块肉，实惠特点比较明显，适合作为紫荆的一个备选。', NULL, 'APPROVED'),
(27, 27, 1, 4, '猪肉酸菜馅饼整体表现稳定，荤菜，面食，实惠特点比较明显，适合作为紫荆的一个备选。', NULL, 'APPROVED'),
(28, 28, 2, 5, '西瓜整体表现稳定，水果，甜品特点比较明显，适合作为紫荆的一个备选。', NULL, 'APPROVED'),
(29, 29, 1, 5, '酸菜白肉整体表现稳定，荤菜，实惠特点比较明显，适合作为紫荆的一个备选。', NULL, 'APPROVED');

INSERT INTO favorites (user_id, dish_id) VALUES
(1, 1),
(1, 3),
(1, 10),
(1, 16),
(1, 26),
(2, 13),
(2, 18),
(2, 21),
(2, 25);

INSERT INTO crowd_reports (id, canteen_id, user_id, level, created_at) VALUES
(1, 1, 1, 3, NOW() - INTERVAL 7 MINUTE),
(2, 2, 2, 2, NOW() - INTERVAL 14 MINUTE),
(3, 3, 1, 3, NOW() - INTERVAL 21 MINUTE),
(4, 5, 2, 2, NOW() - INTERVAL 28 MINUTE),
(5, 1, 2, 4, NOW() - INTERVAL 35 MINUTE);

INSERT INTO announcements (id, title, content, starts_at, ends_at, status) VALUES
(1, '真实菜品数据已更新', '紫荆、桃李、清芬、听涛已录入一批真实菜品，可按窗口和标签筛选。', NOW() - INTERVAL 1 DAY, NOW() + INTERVAL 30 DAY, 1),
(2, '欢迎补充菜品信息', '如果发现窗口或菜品缺失，可以在详情页提交补充信息，等待管理员审核。', NOW() - INTERVAL 1 DAY, NOW() + INTERVAL 30 DAY, 1);

INSERT INTO consumption_records (id, user_id, canteen_id, amount, consumed_at) VALUES
(1, 1, 2, 12.50, NOW() - INTERVAL 2 DAY),
(2, 1, 3, 16.00, NOW() - INTERVAL 1 DAY),
(3, 2, 1, 9.50, NOW() - INTERVAL 1 DAY),
(4, 2, 5, 10.50, NOW() - INTERVAL 3 DAY);
