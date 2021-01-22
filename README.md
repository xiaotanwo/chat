# chat 即时聊天系统
## springboot + vue + mysql + websocket

## 前端项目：https://github.com/xiaotanwo/chat_front

``` 建表语句

# 用户表，添加用户昵称的唯一索引
CREATE TABLE `t_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID，自增',
  `name` varchar(32) NOT NULL UNIQUE COMMENT '用户昵称，唯一约束',
  `password` varchar(32) NOT NULL COMMENT 'MD5(MD5(pass明文+固定salt)+salt)',
  `salt` varchar(10) NOT NULL COMMENT '随机盐值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

# 好友列表
CREATE TABLE `t_friend` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID，自增',
  `name` varchar(32) NOT NULL COMMENT '用户',
  `friend` varchar(32) NOT NULL COMMENT '用户好友',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
# 添加索引
CREATE INDEX indexName ON t_friend(name(32))

# 好友申请列表
CREATE TABLE `t_friend_apply` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID，自增',
  `name` varchar(32) NOT NULL COMMENT '用户',
  `apply_name` varchar(32) NOT NULL COMMENT '申请的用户好友',
  `state` char(1) DEFAULT NULL COMMENT '状态，null未处理，0接受，1拒绝',
  `msg` varchar(50) DEFAULT NULL COMMENT '申请好友的信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
# 添加索引
CREATE INDEX indexName ON t_friend_apply(name(32))
CREATE INDEX indexApplyName ON t_friend_apply(apply_name(32))

# 群列表
CREATE TABLE `t_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID，自增',
  `name` varchar(32) NOT NULL UNIQUE COMMENT '群聊名称，唯一约束',
  `password` varchar(32) NOT NULL COMMENT 'MD5(pass明文+固定salt)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

# 群聊用户表
CREATE TABLE `t_group_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID，自增',
  `group_name` varchar(32) NOT NULL COMMENT '群聊名称',
  `user_name` varchar(32) NOT NULL COMMENT '用户',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
# 添加索引
CREATE INDEX indexGroupName ON t_group_user(group_name(32))
CREATE INDEX indexUserName ON t_group_user(user_name(32))

```