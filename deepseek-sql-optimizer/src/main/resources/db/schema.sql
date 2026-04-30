-- 创建数据库
CREATE DATABASE IF NOT EXISTS `demo-sql` DEFAULT CHARACTER SET utf8mb4;

USE `demo-sql`;

-- 品类表
CREATE TABLE `category` (
                            `id` INT NOT NULL AUTO_INCREMENT,
                            `name` VARCHAR(50) NOT NULL COMMENT '品类名称',
                            `parent_id` INT DEFAULT 0 COMMENT '父品类ID',
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 订单表
CREATE TABLE `orders` (
                          `id` BIGINT NOT NULL AUTO_INCREMENT,
                          `order_no` VARCHAR(32) NOT NULL COMMENT '订单号',
                          `category_id` INT NOT NULL COMMENT '品类ID',
                          `amount` DECIMAL(10,2) NOT NULL COMMENT '订单金额',
                          `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0-正常 1-退货',
                          `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          PRIMARY KEY (`id`),
                          KEY `idx_category` (`category_id`),
                          KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 品类数据
INSERT INTO `category` (`name`) VALUES ('电子产品'), ('服装'), ('家电'), ('图书');

-- 订单数据（过去30天随机生成，这里给一些示例）
INSERT INTO `orders` (`order_no`, `category_id`, `amount`, `status`, `create_time`) VALUES
                                                                                        ('O001', 1, 299.00, 0, DATE_SUB(NOW(), INTERVAL 5 DAY)),
                                                                                        ('O002', 1, 459.00, 0, DATE_SUB(NOW(), INTERVAL 12 DAY)),
                                                                                        ('O003', 2, 129.00, 1, DATE_SUB(NOW(), INTERVAL 3 DAY)), -- 退货
                                                                                        ('O004', 2, 399.00, 0, DATE_SUB(NOW(), INTERVAL 20 DAY)),
                                                                                        ('O005', 3, 1999.00, 0, DATE_SUB(NOW(), INTERVAL 8 DAY)),
                                                                                        ('O006', 3, 799.00, 0, DATE_SUB(NOW(), INTERVAL 15 DAY)),
                                                                                        ('O007', 4, 55.00, 0, DATE_SUB(NOW(), INTERVAL 2 DAY)),
                                                                                        ('O008', 4, 89.00, 0, DATE_SUB(NOW(), INTERVAL 25 DAY)),
                                                                                        ('O009', 1, 1299.00, 0, DATE_SUB(NOW(), INTERVAL 1 DAY)),
                                                                                        ('O010', 2, 259.00, 0, DATE_SUB(NOW(), INTERVAL 18 DAY));