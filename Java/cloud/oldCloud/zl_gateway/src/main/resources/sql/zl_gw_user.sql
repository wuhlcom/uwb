/*
Navicat MySQL Data Transfer

Source Server         : 192.168.10.232
Source Server Version : 50560
Source Host           : 192.168.10.232:3306
Source Database       : MIA_Gw_db

Target Server Type    : MYSQL
Target Server Version : 50560
File Encoding         : 65001

Date: 2018-08-14 17:51:47
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `zl_gw_user`
-- ----------------------------
DROP TABLE IF EXISTS `zl_gw_user`;
CREATE TABLE `zl_gw_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(64) NOT NULL COMMENT '用户名',
  `password` varchar(32) NOT NULL,
  `locked` tinyint(4) NOT NULL DEFAULT '0' COMMENT '1 被锁 0 解锁 被锁账户在规定时间内不能使用',
  `expired` tinyint(4) DEFAULT '0' COMMENT '1 过期 0 未过期 是否过期',
  `created_at` int(11) DEFAULT NULL COMMENT '创建时间',
  `updated_at` int(11) DEFAULT NULL COMMENT '更新时间',
  `parent_id` int(11) DEFAULT NULL COMMENT '账户的创建者的Id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='MIA网关管理用户表';

-- ----------------------------
-- Records of zl_gw_user
-- ----------------------------
INSERT INTO `zl_gw_user` VALUES ('1', 'zhilutec', '5fc42359079fea4d22d295413c030073', '0', '0', '1534240198', '1534240254', null);
INSERT INTO `zl_gw_user` VALUES ('2', 'zhilutec1', '35e67c4aa73d8510084758da8871dff5', '0', '0', '1534240284', '1534240284', null);
