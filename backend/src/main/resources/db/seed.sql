SET NAMES utf8mb4;
USE thu_canteen;

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
(1,'demo','$2a$10$o6Js5uUIk.IlN5.vWnG7xuDgWYm77MtFeWT3x/Zha/20bN6THD99q','演示用户',NULL,'USER','清淡,实惠',0,1),(2,'reviewer','$2a$10$o6Js5uUIk.IlN5.vWnG7xuDgWYm77MtFeWT3x/Zha/20bN6THD99q','紫荆常客',NULL,'USER','辣味,下饭',0,1),(3,'admin','$2a$10$o6Js5uUIk.IlN5.vWnG7xuDgWYm77MtFeWT3x/Zha/20bN6THD99q','管理员',NULL,'ADMIN',NULL,0,1);

INSERT INTO canteens (id,name,cover_url,address,open_hours,pay_methods,on_campus,latitude,longitude,status) VALUES
(1,'紫荆','/uploads/canteens/zijing.jpg','紫荆学生公寓区','06:30-22:00','校园卡,微信,支付宝',1,NULL,NULL,1),
(2,'桃李','/uploads/canteens/taoli.jpg','清华大学学生区','06:30-22:00','校园卡,微信,支付宝',1,NULL,NULL,1),
(3,'清芬','/uploads/canteens/qingfen.jpg','清华大学中部教学区附近','06:30-22:00','校园卡,微信,支付宝',1,NULL,NULL,1),
(4,'玉树','/uploads/canteens/yushu.jpg','清华大学西北生活区','06:30-22:00','校园卡,微信,支付宝',1,NULL,NULL,1),
(5,'听涛','/uploads/canteens/tintao.jpg','清华大学东北区','06:30-22:00','校园卡,微信,支付宝',1,NULL,NULL,1),
(6,'观畴','/uploads/canteens/guanchou.jpg','清华大学南部生活区','06:30-22:00','校园卡,微信,支付宝',1,NULL,NULL,1),
(7,'丁香','/uploads/canteens/dingxiang.jpg','丁香园学生公寓附近','06:30-22:00','校园卡,微信,支付宝',1,NULL,NULL,1);

INSERT INTO canteen_windows (id,canteen_id,floor_no,name,open_hours,status) VALUES
(1,2,1,'麻辣烫','按食堂实际开放时间为准',1),
(2,2,1,'西式面包','按食堂实际开放时间为准',1),
(3,2,1,'煎饼手抓饼','按食堂实际开放时间为准',1),
(4,2,1,'烤烙','按食堂实际开放时间为准',1),
(5,2,1,'米粉','按食堂实际开放时间为准',1),
(6,2,1,'冷饮热饮','按食堂实际开放时间为准',1),
(7,3,1,'小吃','按食堂实际开放时间为准',1),
(8,3,1,'烤盘饭','按食堂实际开放时间为准',1),
(9,3,1,'麻辣烫','按食堂实际开放时间为准',1),
(10,3,1,'土豆粉','按食堂实际开放时间为准',1),
(11,3,1,'西北风味','按食堂实际开放时间为准',1),
(12,3,2,'精品菜','按食堂实际开放时间为准',1),
(13,3,2,'普通伙食','按食堂实际开放时间为准',1),
(14,5,1,'水果','按食堂实际开放时间为准',1),
(15,5,1,'西北风味','按食堂实际开放时间为准',1),
(16,5,1,'饺子','按食堂实际开放时间为准',1),
(17,1,2,'涮羊肉','按食堂实际开放时间为准',1),
(18,1,1,'中式套餐','按食堂实际开放时间为准',1),
(19,1,2,'花样主食','按食堂实际开放时间为准',1),
(20,1,2,'冷饮热饮','按食堂实际开放时间为准',1),
(21,1,3,'东北风味','按食堂实际开放时间为准',1);

INSERT INTO dishes (id,canteen_id,window_id,name,image_url,price,description,spice_level,status) VALUES
(1,2,1,'麻辣烫','/uploads/dishes/taoli_malatang.jpg',0.00,'可选麻辣和番茄锅底，同窗口还有冒烤鸭价格按称重计价。',0,'APPROVED'),
(2,2,2,'面包','/uploads/dishes/taoli_bread.jpg',3.50,'有红果/葡萄/酵母/黄油/椰蓉/红豆/维生素多种口味',0,'APPROVED'),
(3,2,3,'绿豆面煎饼','/uploads/dishes/taoli_jianbing.jpg',4.00,'有火腿肠、辣条、培根、鸡柳、海带、生菜等配菜可加',0,'APPROVED'),
(4,2,4,'羊肉烧麦','/uploads/dishes/taoli_shaomai.jpg',2.90,'比较大个的羊肉烧麦',0,'APPROVED'),
(5,2,4,'小米粥','/uploads/dishes/taoli_xiaomizhou.jpg',0.40,'来自烤烙窗口。',0,'APPROVED'),
(6,2,5,'鸡腿肉热拌粉','/uploads/dishes/taoli_jituiroubanfen.jpg',11.00,'来自米粉窗口。',0,'APPROVED'),
(7,2,4,'鸡蛋煎饼','/uploads/dishes/taoli_jidanjianbing.jpg',4.00,'来自烤烙窗口。',0,'APPROVED'),
(8,2,3,'手抓饼','/uploads/dishes/taoli_shouzhuabing.jpg',3.50,'来自煎饼手抓饼窗口。',0,'APPROVED'),
(9,2,6,'可乐','/uploads/dishes/taoli_kele.jpg',1.50,'一杯可乐',0,'APPROVED'),
(10,3,7,'山葵炸鸡','/uploads/dishes/qingfen_zhaji.jpg',0.00,'来自小吃窗口。价格按称重计价。',0,'APPROVED'),
(11,3,7,'三明治','/uploads/dishes/qingfen_sanmingzhi.jpg',2.00,'火腿鸡蛋三明治',0,'APPROVED'),
(12,3,8,'烤盘饭','/uploads/dishes/qingfen_kaopanfan.jpg',0.00,'来自烤盘饭窗口。价格按称重计价。',0,'APPROVED'),
(13,3,9,'清芬麻辣烫','/uploads/dishes/qingfen_malatang.jpg',0.00,'可选加麻酱和辣椒价格按称重计价。',0,'APPROVED'),
(14,3,10,'酸菜鱼砂锅面','/uploads/dishes/qingfen_shaoguomian.jpg',16.00,'来自土豆粉窗口。',0,'APPROVED'),
(15,3,11,'酸汤水饺','/uploads/dishes/qingfen_suantangshuijiao.jpg',13.00,'来自西北风味窗口。',0,'APPROVED'),
(16,3,12,'麻辣草鱼','/uploads/dishes/qingfen_malacaoyu.jpg',6.00,'来自精品菜窗口。',0,'APPROVED'),
(17,3,12,'双椒牛肉','/uploads/dishes/qingfen_shuangjiaoniurou.jpg',7.00,'来自精品菜窗口。',0,'APPROVED'),
(18,3,12,'山楂话梅猪蹄','/uploads/dishes/qingfen_shanzhahuameipaigu.jpg',7.00,'来自精品菜窗口。',0,'APPROVED'),
(19,3,13,'肉末豆腐','/uploads/dishes/qingfen_roumodoufu.jpg',3.00,'来自普通伙食窗口。',0,'APPROVED'),
(20,5,14,'西瓜','/uploads/dishes/tintao_xigua.jpg',0.00,'来自水果窗口。价格按称重计价。',0,'APPROVED'),
(21,5,15,'牛肉三丁面','/uploads/dishes/tintao_niurousanding.jpg',9.50,'来自西北风味窗口。',0,'APPROVED'),
(22,5,15,'肉夹馍','/uploads/dishes/tintao_roujiamo.jpg',3.00,'来自西北风味窗口。',0,'APPROVED'),
(23,5,15,'羊杂汤','/uploads/dishes/tintao_yangzatang.jpg',10.50,'来自西北风味窗口。',0,'APPROVED'),
(24,5,16,'牛肉胡萝卜饺子','/uploads/dishes/tintao_shuijiao.jpg',0.00,'来自饺子窗口。价格按称重计价。',0,'APPROVED'),
(25,1,17,'涮羊肉','/uploads/dishes/zijing_shuaiyangrou.jpg',0.00,'来自涮羊肉窗口。价格按称重计价。',0,'APPROVED'),
(26,1,18,'猪排饭','/uploads/dishes/zijing_zhupaifan.jpg',9.50,'来自中式套餐窗口。',0,'APPROVED'),
(27,1,19,'猪肉酸菜馅饼','/uploads/dishes/zijing_suancaizhuroubing.jpg',2.00,'来自花样主食窗口。',0,'APPROVED'),
(28,1,20,'西瓜','/uploads/dishes/zijing_xigua.jpg',0.00,'来自冷饮热饮窗口。价格按称重计价。',0,'APPROVED'),
(29,1,21,'酸菜白肉','/uploads/dishes/zijing_suancaibairou.jpg',3.00,'来自东北风味窗口。',0,'APPROVED');

INSERT INTO dish_tags (dish_id,tag) VALUES
(1,'面食'),
(1,'热汤'),
(1,'荤菜'),
(1,'辣味'),
(1,'酸甜'),
(1,'称重计价'),
(2,'甜口'),
(3,'早餐'),
(3,'实惠'),
(4,'荤菜'),
(4,'早餐'),
(4,'高蛋白'),
(5,'早餐'),
(5,'清淡'),
(5,'实惠'),
(5,'家常'),
(6,'荤菜'),
(6,'粉面'),
(7,'早餐'),
(8,'早餐'),
(8,'实惠'),
(9,'饮品'),
(9,'快餐'),
(9,'实惠'),
(10,'荤菜'),
(10,'快餐'),
(10,'实惠'),
(10,'称重计价'),
(11,'快餐'),
(11,'实惠'),
(12,'荤菜'),
(12,'大块肉'),
(12,'高蛋白'),
(12,'称重计价'),
(13,'面食'),
(13,'热汤'),
(13,'荤菜'),
(13,'辣味'),
(13,'称重计价'),
(14,'面食'),
(14,'热汤'),
(14,'荤菜'),
(14,'微辣'),
(14,'粉面'),
(15,'热汤'),
(15,'荤菜'),
(15,'微辣'),
(16,'大块肉'),
(16,'热汤'),
(16,'荤菜'),
(16,'辣味'),
(17,'荤菜'),
(17,'微辣'),
(18,'荤菜'),
(18,'大块肉'),
(18,'酸甜'),
(19,'微辣'),
(19,'下饭'),
(19,'家常'),
(20,'水果'),
(20,'甜品'),
(20,'称重计价'),
(21,'荤菜'),
(21,'粉面'),
(22,'荤菜'),
(22,'面食'),
(23,'荤菜'),
(23,'粉面'),
(24,'荤菜'),
(24,'实惠'),
(24,'称重计价'),
(25,'荤菜'),
(25,'实惠'),
(25,'热汤'),
(25,'微辣'),
(25,'称重计价'),
(26,'荤菜'),
(26,'下饭'),
(26,'大块肉'),
(26,'实惠'),
(27,'荤菜'),
(27,'面食'),
(27,'实惠'),
(28,'水果'),
(28,'甜品'),
(28,'称重计价'),
(29,'荤菜'),
(29,'实惠');

INSERT INTO reviews (id,dish_id,user_id,rating,content,image_url,status) VALUES
(1,1,1,5,'麻辣烫出餐速度还可以，口味属于比较保险的选择。',NULL,'APPROVED'),
(2,2,2,4,'面包分量和价格都比较适合学生日常吃。',NULL,'APPROVED'),
(3,3,1,4,'绿豆面煎饼口味特点比较明显，喜欢这一类的人可以试试。',NULL,'APPROVED'),
(4,4,2,5,'羊肉烧麦味道比较稳定，适合日常不知道吃什么的时候选。',NULL,'APPROVED'),
(5,5,1,5,'小米粥出餐速度还可以，口味属于比较保险的选择。',NULL,'APPROVED'),
(6,6,2,4,'鸡腿肉热拌粉分量和价格都比较适合学生日常吃。',NULL,'APPROVED'),
(7,7,1,4,'鸡蛋煎饼口味特点比较明显，喜欢这一类的人可以试试。',NULL,'APPROVED'),
(8,8,2,5,'手抓饼味道比较稳定，适合日常不知道吃什么的时候选。',NULL,'APPROVED'),
(9,9,1,5,'可乐出餐速度还可以，口味属于比较保险的选择。',NULL,'APPROVED'),
(10,10,2,4,'山葵炸鸡分量和价格都比较适合学生日常吃。',NULL,'APPROVED'),
(11,11,1,4,'三明治口味特点比较明显，喜欢这一类的人可以试试。',NULL,'APPROVED'),
(12,12,2,5,'烤盘饭味道比较稳定，适合日常不知道吃什么的时候选。',NULL,'APPROVED'),
(13,13,1,5,'清芬麻辣烫出餐速度还可以，口味属于比较保险的选择。',NULL,'APPROVED'),
(14,14,2,4,'酸菜鱼砂锅面分量和价格都比较适合学生日常吃。',NULL,'APPROVED'),
(15,15,1,4,'酸汤水饺口味特点比较明显，喜欢这一类的人可以试试。',NULL,'APPROVED'),
(16,16,2,5,'麻辣草鱼味道比较稳定，适合日常不知道吃什么的时候选。',NULL,'APPROVED'),
(17,17,1,5,'双椒牛肉出餐速度还可以，口味属于比较保险的选择。',NULL,'APPROVED'),
(18,18,2,4,'山楂话梅猪蹄分量和价格都比较适合学生日常吃。',NULL,'APPROVED'),
(19,19,1,4,'肉末豆腐口味特点比较明显，喜欢这一类的人可以试试。',NULL,'APPROVED'),
(20,20,2,5,'西瓜味道比较稳定，适合日常不知道吃什么的时候选。',NULL,'APPROVED'),
(21,21,1,5,'牛肉三丁面出餐速度还可以，口味属于比较保险的选择。',NULL,'APPROVED'),
(22,22,2,4,'肉夹馍分量和价格都比较适合学生日常吃。',NULL,'APPROVED'),
(23,23,1,4,'羊杂汤口味特点比较明显，喜欢这一类的人可以试试。',NULL,'APPROVED'),
(24,24,2,5,'牛肉胡萝卜饺子味道比较稳定，适合日常不知道吃什么的时候选。',NULL,'APPROVED'),
(25,25,1,5,'涮羊肉出餐速度还可以，口味属于比较保险的选择。',NULL,'APPROVED'),
(26,26,2,4,'猪排饭分量和价格都比较适合学生日常吃。',NULL,'APPROVED'),
(27,27,1,4,'猪肉酸菜馅饼口味特点比较明显，喜欢这一类的人可以试试。',NULL,'APPROVED'),
(28,28,2,5,'西瓜味道比较稳定，适合日常不知道吃什么的时候选。',NULL,'APPROVED'),
(29,29,1,5,'酸菜白肉出餐速度还可以，口味属于比较保险的选择。',NULL,'APPROVED');

INSERT INTO announcements (id,title,content,starts_at,ends_at,status) VALUES
(1,'真实菜品数据已更新','真实菜品、窗口和食堂照片已更新。',NOW()-INTERVAL 1 DAY,NOW()+INTERVAL 30 DAY,1);
