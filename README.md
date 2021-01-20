# chat 即时聊天系统
## springboot + vue + mysql + websocket

## 前端项目：https://github.com/xiaotanwo/chat_front

``` 建表语句

# 用户表
CREATE TABLE `t_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID，自增',
  `name` varchar(32) NOT NULL COMMENT '用户昵称',
  `password` varchar(32) DEFAULT NULL COMMENT 'MD5(MD5(pass明文+固定salt)+salt)',
  `salt` varchar(10) DEFAULT NULL COMMENT '随机盐值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

# 添加索引
CREATE UNIQUE INDEX indexName ON t_user(name(32))

```