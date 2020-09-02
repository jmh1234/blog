-- 用户表
CREATE TABLE user
(
  id         BIGINT             AUTO_INCREMENT PRIMARY KEY,
  username   VARCHAR(100) UNIQUE
  COMMENT '用户名',
  password   VARCHAR(100) COMMENT '密码',
  avatar     VARCHAR(100) COMMENT '头像',
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP NOT NULL DEFAULT NOW()
)
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

-- 博客表
CREATE TABLE blog
(
  id          BIGINT             AUTO_INCREMENT PRIMARY KEY,
  title       VARCHAR(100) COMMENT '博客标题',
  description VARCHAR(1000) COMMENT '博客内容简要描述',
  content  VARCHAR(10000) COMMENT '博客内容',
  user_id     BIGINT COMMENT '博客用户ID',
  created_at  TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at  TIMESTAMP NOT NULL DEFAULT NOW()
)
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

-- 商品表
CREATE TABLE goods
(
  id   BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) COMMENT '商品名'
)
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

-- 订单表
CREATE TABLE `order`
(
  id       BIGINT AUTO_INCREMENT PRIMARY KEY,
  goods_id BIGINT COMMENT '商品id',
  user_id  BIGINT COMMENT '用户id',
  price    INT COMMENT '价格',
  quantity INT COMMENT '数量'
)
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

/*INSERT INTO user (id, username, password, avatar, CREATED_AT, UPDATED_AT)
VALUES (1, '张三', '123456', 'https://blog-server.hunger-valley.com/avatar/69.jpg', NOW(), NOW()),
  (2, '李四', '123456', 'https://blog-server.hunger-valley.com/avatar/69.jpg', NOW(), NOW()),
  (3, '王五', '123456', 'https://blog-server.hunger-valley.com/avatar/69.jpg', NOW(), NOW()),
  (4, '赵六', '123456', 'https://blog-server.hunger-valley.com/avatar/69.jpg', NOW(), NOW());*/

INSERT INTO goods (id, name)
VALUES (100, '苹果'),
  (200, '香蕉'),
  (300, '桃子'),
  (400, '葡萄'),
  (500, '西瓜');

INSERT INTO `order` (id, goods_id, user_id, price, quantity)
VALUES (1, 100, 1, 100, 2),
  (2, 100, 4, 100, 10),
  (3, 200, 3, 200, 11),
  (4, 300, 1, 300, 9),
  (5, 200, 2, 200, 3),
  (6, 400, 1, 400, 1),
  (7, 500, 4, 500, 2),
  (8, 200, 2, 200, 6),
  (9, 100, 2, 100, 6),
  (10, 400, 3, 400, 4),
  (11, 500, 4, 500, 5),
  (12, 300, 1, 300, 20),
  (13, 200, 3, 200, 2),
  (14, 400, 2, 400, 7),
  (15, 200, 4, 200, 8),
  (16, 300, 1, 300, 20),
  (17, 100, 4, 100, 13),
  (18, 400, 2, 400, 8)
