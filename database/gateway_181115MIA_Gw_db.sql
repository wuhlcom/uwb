/*
Navicat MySQL Data Transfer

Source Server         : 123.207.39.248:13306
Source Server Version : 50560
Source Host           : 123.207.39.248:13306
Source Database       : MIA_Gw_db

Target Server Type    : MYSQL
Target Server Version : 50560
File Encoding         : 65001

Date: 2018-11-15 22:30:54
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `gateway_api_define`
-- ----------------------------
DROP TABLE IF EXISTS `gateway_api_define`;
CREATE TABLE `gateway_api_define` (
  `id` varchar(50) NOT NULL,
  `path` varchar(255) NOT NULL,
  `service_id` varchar(50) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `retryable` tinyint(1) DEFAULT NULL,
  `enabled` tinyint(1) NOT NULL,
  `strip_prefix` int(11) DEFAULT NULL,
  `api_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gateway_api_define
-- ----------------------------
INSERT INTO `gateway_api_define` VALUES ('1', '/summarize/**', 'env-air', null, '0', '1', '0', 'env-air');
INSERT INTO `gateway_api_define` VALUES ('2', '/logman/**', 'env-air', '', '0', '1', '0', 'env-air');
INSERT INTO `gateway_api_define` VALUES ('3', '/alarmman/**', 'env-air', '', '0', '1', '0', 'env-air');
INSERT INTO `gateway_api_define` VALUES ('4', '/entry/**', 'env-air', '', '0', '1', '0', 'env-air');
INSERT INTO `gateway_api_define` VALUES ('5', '/eplogin/**', 'security-authentication', null, '0', '1', '0', 'security-authentication');
INSERT INTO `gateway_api_define` VALUES ('6', '/menu/**', 'env-air', null, '0', '1', '0', 'env-air');
INSERT INTO `gateway_api_define` VALUES ('atmosphere', '/env/atmosphere/**', 'env-dust2', null, '0', '1', '0', 'env-dust2');
INSERT INTO `gateway_api_define` VALUES ('bucket', '/charging-bucket/**', 'bucket-service', null, '0', '1', '0', 'bucket-service');
INSERT INTO `gateway_api_define` VALUES ('client1', '/api/client1/**', 'client1', null, '0', '1', '0', 'client1');
INSERT INTO `gateway_api_define` VALUES ('data-admin', '/mia-iot/api/env/data/**', 'data-admin', null, '0', '1', '0', 'data-admin');
INSERT INTO `gateway_api_define` VALUES ('dust', '/env/dust/**', 'env-dust', null, '0', '1', '0', 'env-dust');
INSERT INTO `gateway_api_define` VALUES ('dust2', '/env/dust2/**', 'env-dust2', null, '0', '1', '0', 'env-dust2');
INSERT INTO `gateway_api_define` VALUES ('engine', '/env/engine/**', 'env-engine', null, '0', '1', '0', 'env-engine');
INSERT INTO `gateway_api_define` VALUES ('env', '/test/**', 'env-air', null, '0', '1', '0', 'env-air');
INSERT INTO `gateway_api_define` VALUES ('env-device', '/env/device/**', 'env-dust2', null, '0', '1', '0', 'env-dust2');
INSERT INTO `gateway_api_define` VALUES ('env-obd', '/mia-iot/api/env/obd/**', 'env-obd', null, '0', '1', '0', 'env-obd');
INSERT INTO `gateway_api_define` VALUES ('env-om', '/mia-iot/api/env/om/**', 'env-om', null, '0', '1', '0', 'env-om');
INSERT INTO `gateway_api_define` VALUES ('env-station', '/env/station/**', 'env-dust2', null, '0', '1', '0', 'env-dust2');
INSERT INTO `gateway_api_define` VALUES ('parking-service', '/mia-iot/api/Parking-Service/**', 'parking-service', null, '0', '1', '0', 'parking-service');
INSERT INTO `gateway_api_define` VALUES ('permission', '/permission/**', 'permission', null, '0', '1', '0', 'permission');
INSERT INTO `gateway_api_define` VALUES ('rvcbase', '/base/**', 'rvcbase', null, '0', '1', '0', 'rvcbase');
INSERT INTO `gateway_api_define` VALUES ('rvcboda', '/charge/**', 'rvcboda', null, '0', '1', '0', 'rvcboda');
INSERT INTO `gateway_api_define` VALUES ('rvcdevice', '/device/**', 'rvcdevice', null, '0', '1', '0', 'rvcdevice');
INSERT INTO `gateway_api_define` VALUES ('rvcgis', '/gis/**', 'rvcgis', null, '0', '1', '0', 'rvcgis');
INSERT INTO `gateway_api_define` VALUES ('rvcsyslog', '/systemlog/**', 'rvcsyslog', null, '0', '1', '0', 'rvcsyslog');
INSERT INTO `gateway_api_define` VALUES ('rvcunresolved', '/unresolved/**', 'rvcunresolved', null, '0', '1', '0', 'rvcunresolved');
INSERT INTO `gateway_api_define` VALUES ('user', '/user/**', 'security-authentication', null, '0', '1', '0', 'security-authentication');
INSERT INTO `gateway_api_define` VALUES ('uwb-access', '/uwb/access/**', 'uwb-access', null, '0', '1', '0', 'uwb-access');
INSERT INTO `gateway_api_define` VALUES ('uwb-fastdfs', '/uwb/fastdfs/**', 'uwb-fastdfs', null, '0', '1', '0', 'uwb-fastdfs');
INSERT INTO `gateway_api_define` VALUES ('uwb-resources', '/uwb/resources/**', 'uwb-resources', null, '0', '1', '0', 'uwb-resources');
INSERT INTO `gateway_api_define` VALUES ('video', '/video/**', 'application-video', null, '0', '1', '0', 'application-video');
INSERT INTO `gateway_api_define` VALUES ('weather', '/env/weather/**', 'env-dust2', null, '0', '1', '0', 'env-dust2');

-- ----------------------------
-- Table structure for `user_tbl`
-- ----------------------------
DROP TABLE IF EXISTS `user_tbl`;
CREATE TABLE `user_tbl` (
  `id` int(11) NOT NULL,
  `username` varchar(20) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `enabled` varchar(5) DEFAULT '0' COMMENT '是否被禁用',
  `credential` varchar(5) DEFAULT '0' COMMENT '凭证是否过期',
  `locked` varchar(5) DEFAULT '0' COMMENT '是否被锁',
  `expired` varchar(5) DEFAULT '0' COMMENT '是否过期',
  `createTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_tbl
-- ----------------------------
INSERT INTO `user_tbl` VALUES ('1', 'admin', '82236891c269b75638bd254e7739327c', 'false', 'false', 'false', 'false', '2017-05-17 14:32:13');

-- ----------------------------
-- Table structure for `zl_env_access`
-- ----------------------------
DROP TABLE IF EXISTS `zl_env_access`;
CREATE TABLE `zl_env_access` (
  `user_id` varchar(11) NOT NULL,
  `group_id` int(11) NOT NULL,
  `system_id` int(11) NOT NULL,
  UNIQUE KEY `pkey` (`user_id`,`group_id`,`system_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of zl_env_access
-- ----------------------------
INSERT INTO `zl_env_access` VALUES ('1', '1', '1');

-- ----------------------------
-- Table structure for `zl_env_group`
-- ----------------------------
DROP TABLE IF EXISTS `zl_env_group`;
CREATE TABLE `zl_env_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) DEFAULT NULL,
  `menus` varchar(4000) DEFAULT NULL COMMENT '菜单id',
  `pid` int(11) DEFAULT NULL,
  `remark` varchar(100) DEFAULT NULL,
  `status` tinyint(3) DEFAULT '1',
  `systemid` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of zl_env_group
-- ----------------------------
INSERT INTO `zl_env_group` VALUES ('1', '系统管理员', '100,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17', '0', null, '1', '1');
INSERT INTO `zl_env_group` VALUES ('2', '超级管理员', '18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40', '0', null, '1', '3');

-- ----------------------------
-- Table structure for `zl_env_url`
-- ----------------------------
DROP TABLE IF EXISTS `zl_env_url`;
CREATE TABLE `zl_env_url` (
  `id` int(11) NOT NULL DEFAULT '0',
  `name` varchar(32) DEFAULT NULL,
  `url` varchar(64) DEFAULT NULL,
  `menuid` int(11) DEFAULT NULL,
  `menuname` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of zl_env_url
-- ----------------------------
INSERT INTO `zl_env_url` VALUES ('1', 'mainpoll_ratio', '/env/atmosphere/mainpoll_ratio', '4', '态势分析');
INSERT INTO `zl_env_url` VALUES ('2', 'analysisAqi', '/env/atmosphere/analysisAqi', '4', '态势分析');
INSERT INTO `zl_env_url` VALUES ('3', 'pollRatio', '/env/atmosphere/pollRatio', '4', '态势分析');
INSERT INTO `zl_env_url` VALUES ('4', 'analysisAqi', '/env/atmosphere/analysisAqi', '4', '态势分析');
INSERT INTO `zl_env_url` VALUES ('5', 'trend', '/env/atmosphere/trend', '4', '态势分析');
INSERT INTO `zl_env_url` VALUES ('6', 'ranking1', '/env/atmosphere/ranking1', '4', '态势分析');
INSERT INTO `zl_env_url` VALUES ('7', 'ranking2', '/env/atmosphere/ranking2', '4', '态势分析');
INSERT INTO `zl_env_url` VALUES ('8', 'ranking3', '/env/atmosphere/ranking3', '4', '态势分析');
INSERT INTO `zl_env_url` VALUES ('9', 'compareMainpoll', '/env/atmosphere/compareMainpoll', '5', '质量对比');
INSERT INTO `zl_env_url` VALUES ('10', 'compareAqi', '/env/atmosphere/compareAqi', '5', '质量对比');
INSERT INTO `zl_env_url` VALUES ('11', 'comparePollRatio', '/env/atmosphere/comparePollRatio', '5', '质量对比');
INSERT INTO `zl_env_url` VALUES ('12', 'cityoptimalstation', '/summarize/cityoptimalstation', '13', '环境概览');
INSERT INTO `zl_env_url` VALUES ('13', 'cityworststation', '/summarize/cityworststation', '13', '环境概览');
INSERT INTO `zl_env_url` VALUES ('14', 'distrealtimedata', '/summarize/distrealtimedata', '13', '环境概览');
INSERT INTO `zl_env_url` VALUES ('15', 'cityrealtimedata', '/summarize/cityrealtimedata', '13', '环境概览');
INSERT INTO `zl_env_url` VALUES ('16', 'loglist', '/summarize/loglist', '13', '环境概览');
INSERT INTO `zl_env_url` VALUES ('17', 'alarmlist', '/summarize/alarmlist', '13', '环境概览');
INSERT INTO `zl_env_url` VALUES ('18', 'cityrealtimeaqi', '/summarize/cityrealtimeaqi', '13', '环境概览');
INSERT INTO `zl_env_url` VALUES ('19', 'distfocusarea', '/summarize/distfocusarea', '13', '环境概览');
INSERT INTO `zl_env_url` VALUES ('20', 'alarmlist', '/alarmman/alarmlist', '15', '告警管理');
INSERT INTO `zl_env_url` VALUES ('21', 'loglist', '/logman/loglist', '16', '日志管理');
INSERT INTO `zl_env_url` VALUES ('100', 'getroute', '/menu/getroute', '1', '获取菜单');

-- ----------------------------
-- Table structure for `zl_gw_user`
-- ----------------------------
DROP TABLE IF EXISTS `zl_gw_user`;
CREATE TABLE `zl_gw_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(64) NOT NULL COMMENT '用户名',
  `password` varchar(32) NOT NULL COMMENT '密码',
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

-- ----------------------------
-- Table structure for `zl_mobile_code`
-- ----------------------------
DROP TABLE IF EXISTS `zl_mobile_code`;
CREATE TABLE `zl_mobile_code` (
  `mobile` varchar(20) NOT NULL,
  `code` varchar(12) NOT NULL,
  `create_time` bigint(11) NOT NULL,
  PRIMARY KEY (`mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='手机验证码表';

-- ----------------------------
-- Records of zl_mobile_code
-- ----------------------------
INSERT INTO `zl_mobile_code` VALUES ('', '124151', '1534926400622');
INSERT INTO `zl_mobile_code` VALUES ('15899879109', '158138', '1524464868197');
INSERT INTO `zl_mobile_code` VALUES ('15899879208', '126083', '1532671951355');
INSERT INTO `zl_mobile_code` VALUES ('15899879209', '193115', '1529480578627');
INSERT INTO `zl_mobile_code` VALUES ('18588409658', '187436', '1524454241564');
INSERT INTO `zl_mobile_code` VALUES ('18671646096', '172626', '1524454270681');

-- ----------------------------
-- Table structure for `zl_system`
-- ----------------------------
DROP TABLE IF EXISTS `zl_system`;
CREATE TABLE `zl_system` (
  `alias` varchar(64) NOT NULL,
  `id` varchar(32) NOT NULL COMMENT '系统id',
  `name` varchar(32) NOT NULL,
  `entryurl` varchar(64) NOT NULL COMMENT '系统入口地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of zl_system
-- ----------------------------
INSERT INTO `zl_system` VALUES ('factory', '1', '污染源管理系统', '');
INSERT INTO `zl_system` VALUES ('env', '2', '空气质量监测及预报预警系统', 'https://air.thingspark.io');
INSERT INTO `zl_system` VALUES ('dust', '3', '扬尘噪声一体化系统', 'https://dust.thingspark.io');

-- ----------------------------
-- Table structure for `zl_user`
-- ----------------------------
DROP TABLE IF EXISTS `zl_user`;
CREATE TABLE `zl_user` (
  `role` tinyint(5) NOT NULL,
  `id` varchar(50) NOT NULL COMMENT '用户id',
  `username` varchar(20) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `enabled` varchar(5) DEFAULT '0' COMMENT '是否被禁用',
  `credential` varchar(5) DEFAULT '0' COMMENT '凭证是否过期',
  `locked` varchar(5) DEFAULT '0' COMMENT '是否被锁',
  `expired` varchar(5) DEFAULT '0' COMMENT '是否过期',
  `createtime` datetime DEFAULT NULL,
  `parent_id` varchar(50) DEFAULT NULL COMMENT '创建账号的id',
  `email` varchar(125) DEFAULT NULL,
  `phoneno` varchar(50) DEFAULT NULL,
  `nickname` varchar(100) NOT NULL DEFAULT '昵称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of zl_user
-- ----------------------------
INSERT INTO `zl_user` VALUES ('4', '04431e72-a8ef-49d9-9a81-b1fe8bdf807f', 'admin003', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-01-23 14:06:45', '1', '18675568899@163.com', '15899879003', '南山陈伟鸿');
INSERT INTO `zl_user` VALUES ('1', '1', 'zhilu', '6d946270f1389a8da047ec5a63dfa6d1', '0', '0', '0', '0', '2017-05-17 14:32:13', null, '', '15899879109', '知路科技');
INSERT INTO `zl_user` VALUES ('3', '15846252-2d44-4665-90c7-9e6590f566d8', 'test111', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-05-11 10:56:48', '1', '', '18662991234', '测试全国');
INSERT INTO `zl_user` VALUES ('3', '1639c16b-069f-4bee-b7af-1cfd8d490bdf', 'admin002', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-01-23 14:05:42', '1', '18676710556@163.com', '15899879002', '宝安张志刚');
INSERT INTO `zl_user` VALUES ('0', '16c4f9ca-8024-42f3-9aa8-ed96ad348395', 'xunjianyuan', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-01-26 09:05:13', '999', '', '15899879103', '巡检员');
INSERT INTO `zl_user` VALUES ('0', '18439706-2e20-4f72-afea-a21424141cf3', 'test1', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-01-24 20:39:22', '1', '', '18667334567', 'test');
INSERT INTO `zl_user` VALUES ('2', '2', 'sy001', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-01-04 10:56:02', '1', '15899879103@163.com', '15899879103', '沈阳');
INSERT INTO `zl_user` VALUES ('0', '23b8ee8b-0d20-4e17-857b-e1685a28f579', '沈阳009', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-03-16 09:41:31', '2', '', '18667889067', '沈阳A');
INSERT INTO `zl_user` VALUES ('4', '28ea81f5-bd8e-4f19-af57-57639e04cb00', 'admin004', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-01-23 14:07:36', '1', '15810567755@163.com', '15899879004', '罗湖李振海');
INSERT INTO `zl_user` VALUES ('3', '2e98694a-a42c-427a-953b-3e5b734e9c4a', 'whxjy', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-10-23 09:45:14', '5f4b77ae-90cc-458c-9343-c8f198f72db5', '', '15965236554', '巡检员');
INSERT INTO `zl_user` VALUES ('3', '34f3c580-c7a0-4650-bf10-4be583cc9b1e', 'admin113', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-05-07 14:45:34', '1', '', '15899879018', '南山姚顺奇');
INSERT INTO `zl_user` VALUES ('4', '365b3f94-315c-47cc-9430-623a3192fecf', 'admin203', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-05-07 17:16:38', '1', '', '15899879028', '龙华陈真');
INSERT INTO `zl_user` VALUES ('3', '3b5359d9-78cb-4ba3-b600-1b10c61b6b5b', 'admin117', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-05-07 14:48:53', '1', '', '15899879022', '宝安徐鑫');
INSERT INTO `zl_user` VALUES ('6', '3b5c1b1f-a00e-4f2c-a875-43fef8da4d4e', 'admin007', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-01-23 14:10:00', '1', '18625631526@163.com', '15899879005', '冉晓旭');
INSERT INTO `zl_user` VALUES ('4', '3be42bf3-5cdd-4f69-a2af-c929d496d365', 'admin206', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-05-07 18:56:57', '1', '', '15899879031', '桃园雾炮车');
INSERT INTO `zl_user` VALUES ('0', '3da3a2f3-23e2-4d40-afa6-1b86f3f3bc37', 'jianceche1', 'E10ADC3949BA59ABBE56E057F20F883E', '0', '0', '0', '0', '2018-01-22 17:49:19', '1', '', '18676710556', '陈小小1');
INSERT INTO `zl_user` VALUES ('0', '3fe204c7-6799-4c48-9ab8-9101e75c025b', 'test3', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-01-25 11:05:49', '1', '', '18667334567', 'testab');
INSERT INTO `zl_user` VALUES ('4', '46d1814d-f10f-4c71-8c40-692172a8d587', 'admin205', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-05-07 18:51:19', '1', '', '15899879030', '西乡雾炮车');
INSERT INTO `zl_user` VALUES ('0', '4d567c98-4584-4ff6-a00a-db7bcf34f78e', '沈阳009', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-03-16 09:45:44', '2', '', '18667889067', '沈阳A');
INSERT INTO `zl_user` VALUES ('3', '52a8310a-d27a-4623-8681-4981119eb8a2', 'admin115', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-05-07 14:47:26', '1', '', '15899879020', '宝安杨畅');
INSERT INTO `zl_user` VALUES ('0', '5c87d42f-9c6f-41b4-8eee-750965052db3', 'sy005', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-03-16 09:23:57', '2', '', '15899879109', '卢本山');
INSERT INTO `zl_user` VALUES ('3', '5e18c6b0-dd49-4ae6-ae8b-b791ed7f41da', 'sy002', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-03-10 15:41:04', '2', '13019370146@163.com', '15899879104', '巡检员');
INSERT INTO `zl_user` VALUES ('2', '5f4b77ae-90cc-458c-9343-c8f198f72db5', 'weihai', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-10-23 09:31:46', '1', '', '18899856781', '威海管理员');
INSERT INTO `zl_user` VALUES ('4', '66e3a31f-9b31-4958-b218-a1f7f09e6724', '15899879209', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-05-08 10:43:18', '1', '', '15899879209', '雾炮员高');
INSERT INTO `zl_user` VALUES ('6', '69b53894-2e82-43a9-bae8-4dda6fd8b490', '运维员01', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-05-03 18:38:47', 'cfb78209-bc63-43c1-b82f-9dc52c8125e2', '', '15899879010', '运维员01');
INSERT INTO `zl_user` VALUES ('3', '6b46db3a-5a12-4330-9746-3d0fa6d86bbd', 'admin006', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-05-07 14:29:20', '1', '', '15899879012', '福田李魁');
INSERT INTO `zl_user` VALUES ('0', '6c5d541b-e182-434d-a8ac-7c160f6a183f', '沈阳009', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-03-16 09:46:53', '2', '', '18667889067', '沈阳A');
INSERT INTO `zl_user` VALUES ('3', '6e8623fb-6949-453a-affe-773d3f1e92d2', 'admin114', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-05-07 14:46:09', '1', '', '15899879019', '南山佟文');
INSERT INTO `zl_user` VALUES ('0', '70d0cbe2-b3ac-48b4-8d56-3465758f90c2', '沈阳009', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-03-16 09:42:14', '2', '', '18667889067', '沈阳A');
INSERT INTO `zl_user` VALUES ('3', '77d1a479-7a41-4aa3-b993-0b1efe627642', '15899879208', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-05-09 14:46:00', '1', '', '15899879208', '高进');
INSERT INTO `zl_user` VALUES ('4', '8cc1df21-7330-44f3-a8d7-4ccf7acba32e', 'admin201', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-05-07 17:07:12', '1', '', '15899879026', '宝安李华');
INSERT INTO `zl_user` VALUES ('3', '9168f177-9d3c-4eaf-acf3-9dc09a335cb5', 'admin118', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-05-07 14:50:01', '1', '', '15899879023', '罗湖陈福');
INSERT INTO `zl_user` VALUES ('2', '932fbbbc-e970-4c39-83f3-88ca0ea09b0e', 'laiyige', '52d66ccb03c1fff4cbcf1943f8ffa222', '0', '0', '0', '0', '2018-04-27 16:08:39', '1', '', '18899856789', 'laiyige');
INSERT INTO `zl_user` VALUES ('0', '96b355ea-9f00-4f58-9554-82102ec374c4', 'yunwei', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-01-26 09:06:07', '999', '', '15899879103', '运维员');
INSERT INTO `zl_user` VALUES ('0', '9edb9909-36de-489b-a997-2660ee42bfc1', 'jianceche2', 'E10ADC3949BA59ABBE56E057F20F883E', '0', '0', '0', '0', '2018-01-22 18:28:51', '1', '', '18676710556', '陈小小2');
INSERT INTO `zl_user` VALUES ('0', 'a2762ee7-fe9c-4619-a10f-4eba6c153316', 'sy005', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-03-16 09:28:20', '2', '', '15899879109', '卢本山');
INSERT INTO `zl_user` VALUES ('3', 'a6ca0827-8727-44ed-873f-e4b6491460b7', 'admin100', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-05-07 14:31:42', '1', '', '15899879015', '龙华李杰');
INSERT INTO `zl_user` VALUES ('0', 'ab76fadf-b657-4fa1-8ac1-c194f837bf21', 'sy005', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-03-16 09:23:46', '2', '', '15899879109', '卢本山');
INSERT INTO `zl_user` VALUES ('3', 'adaa5e95-4616-4ca0-8007-70e5c0d09b69', 'admin008', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-05-07 14:30:13', '1', '', '15899879013', '龙岗刘洪');
INSERT INTO `zl_user` VALUES ('4', 'b151c5d7-5921-415d-8c71-d6e37421347a', 'admin202', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-05-07 17:07:51', '1', '', '15899879027', '坪山付健');
INSERT INTO `zl_user` VALUES ('6', 'b772eaa5-2261-4ba4-ad1f-20a5dbcd4428', 'sy003', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-03-10 15:42:03', '2', '13019370146@163.com', '15899879105', '运维员');
INSERT INTO `zl_user` VALUES ('3', 'bc312708-26be-4fa5-b1d0-2e541d34f313', 'test111', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-05-11 10:51:28', '1', '', '18662991234', '测试全国');
INSERT INTO `zl_user` VALUES ('3', 'c1a45894-8b3a-4be9-9c07-6e83241c1162', 'admin112', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-05-07 14:44:47', '1', '', '15899879017', '南山王庆发');
INSERT INTO `zl_user` VALUES ('3', 'c9e9b1f2-0317-4adb-9b5c-15f21006f309', 'admin001', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-01-23 14:04:45', '1', '15899879001@163.com', '15899879001', '南山徐茂生');
INSERT INTO `zl_user` VALUES ('2', 'cfb78209-bc63-43c1-b82f-9dc52c8125e2', 'yingong', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-05-03 18:35:18', '1', '', '18675595259', '尹工');
INSERT INTO `zl_user` VALUES ('4', 'd182e667-0239-43b5-b37f-eb6e9e86c29e', 'admin204', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-05-07 18:50:33', '1', '', '15899879029', '福田雾炮车');
INSERT INTO `zl_user` VALUES ('3', 'd35d385d-d15f-4fd2-b899-942bbd2ebf8e', 'admin119', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-05-07 14:55:21', '1', '', '15899879024', '南山李晓');
INSERT INTO `zl_user` VALUES ('0', 'dd8ce740-884f-4ba9-8657-cc4d1e45dc39', '沈阳009', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-03-16 09:42:26', '2', '', '18667889067', '沈阳A');
INSERT INTO `zl_user` VALUES ('3', 'e15b6fdd-528a-4c2d-ada4-be379489ae8d', 'admin005', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-05-07 14:28:46', '1', '', '15899879011', '罗湖齐伟红');
INSERT INTO `zl_user` VALUES ('3', 'e2b4ed80-0993-4193-883f-43cae37962a6', 'admin116', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-05-07 14:47:58', '1', '', '15899879021', '南山杨');
INSERT INTO `zl_user` VALUES ('3', 'e6c5597d-b7e9-44b6-bfca-7b1da0139ea6', 'admin207', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-07-12 14:14:02', '1', '', '15899879210', '南山李');
INSERT INTO `zl_user` VALUES ('3', 'ec1b0a86-c7e2-4ed5-9bed-e9a9102b665a', 'admin111', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-05-07 14:32:26', '1', '', '15899879016', '坪山王小伟');
INSERT INTO `zl_user` VALUES ('6', 'efb193d5-9064-4125-836a-29b394536024', 'whywy', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-10-23 09:45:45', '5f4b77ae-90cc-458c-9343-c8f198f72db5', '', '16532654512', '运维员');
INSERT INTO `zl_user` VALUES ('3', 'f238dcfb-755a-46f3-92fd-e1011d64d584', 'admin200', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-05-07 14:55:51', '1', '', '15899879025', '南山熊军');
INSERT INTO `zl_user` VALUES ('0', 'fd19955a-b002-403e-94e3-233e47f74317', 'test1', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '0', '0', '2018-01-24 20:38:53', '1', '4578878@qq.com', '1234566', '123wuge123');

-- ----------------------------
-- Table structure for `zl_user_system`
-- ----------------------------
DROP TABLE IF EXISTS `zl_user_system`;
CREATE TABLE `zl_user_system` (
  `userid` varchar(50) NOT NULL,
  `systemid` varchar(50) NOT NULL,
  `groupid` int(11) NOT NULL,
  KEY `userid` (`userid`,`systemid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of zl_user_system
-- ----------------------------
INSERT INTO `zl_user_system` VALUES ('1', '2', '0');
INSERT INTO `zl_user_system` VALUES ('1', '3', '0');
INSERT INTO `zl_user_system` VALUES ('2', '3', '0');
INSERT INTO `zl_user_system` VALUES ('3', '3', '0');
INSERT INTO `zl_user_system` VALUES ('3da3a2f3-23e2-4d40-afa6-1b86f3f3bc37', '3', '0');
INSERT INTO `zl_user_system` VALUES ('9edb9909-36de-489b-a997-2660ee42bfc1', '3', '0');
INSERT INTO `zl_user_system` VALUES ('ec63b4fe-ba2a-4330-94ca-6653c7c99d00', '3', '0');
INSERT INTO `zl_user_system` VALUES ('63027357-8337-4d00-a89b-652179297f8e', '3', '0');
INSERT INTO `zl_user_system` VALUES ('c9e9b1f2-0317-4adb-9b5c-15f21006f309', '3', '0');
INSERT INTO `zl_user_system` VALUES ('1639c16b-069f-4bee-b7af-1cfd8d490bdf', '3', '0');
INSERT INTO `zl_user_system` VALUES ('04431e72-a8ef-49d9-9a81-b1fe8bdf807f', '3', '0');
INSERT INTO `zl_user_system` VALUES ('28ea81f5-bd8e-4f19-af57-57639e04cb00', '3', '0');
INSERT INTO `zl_user_system` VALUES ('93f468f0-dba8-4f15-90c2-92bf45b4c0f0', '3', '0');
INSERT INTO `zl_user_system` VALUES ('583e5ac7-fe70-4ffb-af32-129f5d018e70', '3', '0');
INSERT INTO `zl_user_system` VALUES ('3b5c1b1f-a00e-4f2c-a875-43fef8da4d4e', '3', '0');
INSERT INTO `zl_user_system` VALUES ('f1632a8a-4f64-4d53-a699-4ec3454c0d6f', '3', '0');
INSERT INTO `zl_user_system` VALUES ('73a3cc37-4ce1-46ab-833f-d7262e78a6ff', '3', '0');
INSERT INTO `zl_user_system` VALUES ('2d7755c4-7d7c-4b42-b4f1-076f90bb92da', '3', '0');
INSERT INTO `zl_user_system` VALUES ('fd19955a-b002-403e-94e3-233e47f74317', '3', '0');
INSERT INTO `zl_user_system` VALUES ('18439706-2e20-4f72-afea-a21424141cf3', '3', '0');
INSERT INTO `zl_user_system` VALUES ('cec47d26-3d0e-4f13-864e-ae7103eab5ce', '3', '0');
INSERT INTO `zl_user_system` VALUES ('8e4bd27e-a55c-402a-aac2-09602cedf5cf', '3', '0');
INSERT INTO `zl_user_system` VALUES ('3fe204c7-6799-4c48-9ab8-9101e75c025b', '3', '0');
INSERT INTO `zl_user_system` VALUES ('65727e3f-137d-4f7d-b0c8-891c88e21c59', '3', '0');
INSERT INTO `zl_user_system` VALUES ('16c4f9ca-8024-42f3-9aa8-ed96ad348395', '3', '0');
INSERT INTO `zl_user_system` VALUES ('96b355ea-9f00-4f58-9554-82102ec374c4', '3', '0');
INSERT INTO `zl_user_system` VALUES ('b479bbd0-1d8f-40c6-b615-e72670eb5756', '3', '0');
INSERT INTO `zl_user_system` VALUES ('df2a55bd-2665-4357-8f1e-9a3ff56108c9', '3', '0');
INSERT INTO `zl_user_system` VALUES ('0105b773-23b8-4d2f-b726-ce44ce2a4268', '3', '0');
INSERT INTO `zl_user_system` VALUES ('65ce6593-5062-4b62-9ba8-ebe627ea7404', '3', '0');
INSERT INTO `zl_user_system` VALUES ('919575c3-823a-4faa-afb3-a06a63a37ce5', '3', '0');
INSERT INTO `zl_user_system` VALUES ('9a37367c-f7b9-4f8b-9c79-6301b483bd29', '3', '0');
INSERT INTO `zl_user_system` VALUES ('8adbeb72-2481-4421-9e6e-00570a22e76f', '3', '0');
INSERT INTO `zl_user_system` VALUES ('95109125-7e93-48e1-8db3-158490bf8231', '3', '0');
INSERT INTO `zl_user_system` VALUES ('a8e4c746-ee03-40b0-94c8-76b06cbadbcb', '3', '0');
INSERT INTO `zl_user_system` VALUES ('30dd2843-4287-43ec-8a70-8d1c6de62d33', '3', '0');
INSERT INTO `zl_user_system` VALUES ('19779cce-dde1-422e-9662-926605c6d300', '3', '0');
INSERT INTO `zl_user_system` VALUES ('5e18c6b0-dd49-4ae6-ae8b-b791ed7f41da', '3', '0');
INSERT INTO `zl_user_system` VALUES ('b772eaa5-2261-4ba4-ad1f-20a5dbcd4428', '3', '0');
INSERT INTO `zl_user_system` VALUES ('e6ee98e4-c511-499c-9c7a-08f2f50ed5fd', '3', '0');
INSERT INTO `zl_user_system` VALUES ('957ae586-fbf5-4057-824a-9e3d3891fc53', '3', '0');
INSERT INTO `zl_user_system` VALUES ('ab76fadf-b657-4fa1-8ac1-c194f837bf21', '3', '0');
INSERT INTO `zl_user_system` VALUES ('5c87d42f-9c6f-41b4-8eee-750965052db3', '3', '0');
INSERT INTO `zl_user_system` VALUES ('a2762ee7-fe9c-4619-a10f-4eba6c153316', '3', '0');
INSERT INTO `zl_user_system` VALUES ('23b8ee8b-0d20-4e17-857b-e1685a28f579', '3', '0');
INSERT INTO `zl_user_system` VALUES ('70d0cbe2-b3ac-48b4-8d56-3465758f90c2', '3', '0');
INSERT INTO `zl_user_system` VALUES ('dd8ce740-884f-4ba9-8657-cc4d1e45dc39', '3', '0');
INSERT INTO `zl_user_system` VALUES ('4d567c98-4584-4ff6-a00a-db7bcf34f78e', '3', '0');
INSERT INTO `zl_user_system` VALUES ('6c5d541b-e182-434d-a8ac-7c160f6a183f', '3', '0');
INSERT INTO `zl_user_system` VALUES ('bb7ffc8f-fa35-4d39-8a47-51935dbf26d0', '3', '0');
INSERT INTO `zl_user_system` VALUES ('e168d8cb-935e-4cfb-9dbc-4c9694695797', '3', '0');
INSERT INTO `zl_user_system` VALUES ('df2ef4a1-9bd2-4d6e-8f17-0980dbbe2ff0', '3', '0');
INSERT INTO `zl_user_system` VALUES ('17f84676-a970-44c1-b611-6a3346a8e0f7', '3', '0');
INSERT INTO `zl_user_system` VALUES ('3426eb55-6fe7-4119-a713-4a257f9fcf13', '3', '0');
INSERT INTO `zl_user_system` VALUES ('68a5fc64-e9de-4330-aeac-cb7d2150e4d2', '3', '0');
INSERT INTO `zl_user_system` VALUES ('0f589f91-705d-40fc-ba87-ebeb1b70533a', '3', '0');
INSERT INTO `zl_user_system` VALUES ('1dd7f4bd-f0cb-4602-9357-c75569f55571', '3', '0');
INSERT INTO `zl_user_system` VALUES ('33e66ea2-b544-4031-9bc0-9c289f3c08bb', '3', '0');
INSERT INTO `zl_user_system` VALUES ('53f95efd-4cb1-4b6a-9e57-8c70391487f8', '3', '0');
INSERT INTO `zl_user_system` VALUES ('b2503474-3c59-433c-8456-3cd493542ee0', '3', '0');
INSERT INTO `zl_user_system` VALUES ('2e90421b-a12d-4c24-82e7-02b3706042a3', '3', '0');
INSERT INTO `zl_user_system` VALUES ('37952242-f2cf-4c02-beac-7b91e745f070', '3', '0');
INSERT INTO `zl_user_system` VALUES ('40ec78fa-4b17-410c-baeb-08dce43d1696', '3', '0');
INSERT INTO `zl_user_system` VALUES ('4dd532b6-9e80-4f02-9bb8-ad422cbc86c9', '3', '0');
INSERT INTO `zl_user_system` VALUES ('4667f57c-63d8-4e07-b530-b5d94c0a86af', '3', '0');
INSERT INTO `zl_user_system` VALUES ('2bf1baa0-34b6-4d60-9ca4-ddda6de1aba7', '3', '0');
INSERT INTO `zl_user_system` VALUES ('6aff2732-d94a-43f0-ab28-f742405303f7', '3', '0');
INSERT INTO `zl_user_system` VALUES ('cc7e805d-4917-4fa3-bf22-e7e5da9a6a55', '3', '0');
INSERT INTO `zl_user_system` VALUES ('39e93a1c-d689-4928-bcf9-ff43c38aa033', '3', '0');
INSERT INTO `zl_user_system` VALUES ('d68724ce-78b2-4487-845a-5f85ab140e6d', '3', '0');
INSERT INTO `zl_user_system` VALUES ('ae02199f-8437-43d5-b89b-4024f8c911a9', '3', '0');
INSERT INTO `zl_user_system` VALUES ('85697abc-ec9c-4235-81fd-329b571fbf2a', '3', '0');
INSERT INTO `zl_user_system` VALUES ('57bb1db9-d14a-4674-af32-b9a9b2e90893', '3', '0');
INSERT INTO `zl_user_system` VALUES ('2d2f3273-f784-4181-bbf3-7a85c028e9fa', '3', '0');
INSERT INTO `zl_user_system` VALUES ('25f75eaf-1526-4f42-9271-309ba91e97bb', '3', '0');
INSERT INTO `zl_user_system` VALUES ('28701a97-7234-45b2-8b58-f8fa203f3dfe', '3', '0');
INSERT INTO `zl_user_system` VALUES ('06469618-eac2-4570-acb6-9d14fda0d15f', '3', '0');
INSERT INTO `zl_user_system` VALUES ('ef6cce54-b305-462b-b8e3-a8ddcbcece23', '3', '0');
INSERT INTO `zl_user_system` VALUES ('cf51d345-d4d4-420c-aa8c-d3de653c97e2', '3', '0');
INSERT INTO `zl_user_system` VALUES ('bba07881-7bd9-41e2-b38e-332f2c8d28de', '3', '0');
INSERT INTO `zl_user_system` VALUES ('c716381c-a9ab-4e97-a115-a75561a78b70', '3', '0');
INSERT INTO `zl_user_system` VALUES ('b0a6e865-06e4-4e87-8db8-44b918463513', '3', '0');
INSERT INTO `zl_user_system` VALUES ('932fbbbc-e970-4c39-83f3-88ca0ea09b0e', '3', '0');
INSERT INTO `zl_user_system` VALUES ('cfb78209-bc63-43c1-b82f-9dc52c8125e2', '3', '0');
INSERT INTO `zl_user_system` VALUES ('69b53894-2e82-43a9-bae8-4dda6fd8b490', '3', '0');
INSERT INTO `zl_user_system` VALUES ('35ea3a0d-3d8a-40bb-ad71-1c746dfb3674', '3', '0');
INSERT INTO `zl_user_system` VALUES ('e15b6fdd-528a-4c2d-ada4-be379489ae8d', '3', '0');
INSERT INTO `zl_user_system` VALUES ('6b46db3a-5a12-4330-9746-3d0fa6d86bbd', '3', '0');
INSERT INTO `zl_user_system` VALUES ('adaa5e95-4616-4ca0-8007-70e5c0d09b69', '3', '0');
INSERT INTO `zl_user_system` VALUES ('acd49323-328a-4135-b14e-400ded8c5756', '3', '0');
INSERT INTO `zl_user_system` VALUES ('a6ca0827-8727-44ed-873f-e4b6491460b7', '3', '0');
INSERT INTO `zl_user_system` VALUES ('ec1b0a86-c7e2-4ed5-9bed-e9a9102b665a', '3', '0');
INSERT INTO `zl_user_system` VALUES ('c1a45894-8b3a-4be9-9c07-6e83241c1162', '3', '0');
INSERT INTO `zl_user_system` VALUES ('34f3c580-c7a0-4650-bf10-4be583cc9b1e', '3', '0');
INSERT INTO `zl_user_system` VALUES ('6e8623fb-6949-453a-affe-773d3f1e92d2', '3', '0');
INSERT INTO `zl_user_system` VALUES ('52a8310a-d27a-4623-8681-4981119eb8a2', '3', '0');
INSERT INTO `zl_user_system` VALUES ('e2b4ed80-0993-4193-883f-43cae37962a6', '3', '0');
INSERT INTO `zl_user_system` VALUES ('3b5359d9-78cb-4ba3-b600-1b10c61b6b5b', '3', '0');
INSERT INTO `zl_user_system` VALUES ('9168f177-9d3c-4eaf-acf3-9dc09a335cb5', '3', '0');
INSERT INTO `zl_user_system` VALUES ('d35d385d-d15f-4fd2-b899-942bbd2ebf8e', '3', '0');
INSERT INTO `zl_user_system` VALUES ('f238dcfb-755a-46f3-92fd-e1011d64d584', '3', '0');
INSERT INTO `zl_user_system` VALUES ('8cc1df21-7330-44f3-a8d7-4ccf7acba32e', '3', '0');
INSERT INTO `zl_user_system` VALUES ('b151c5d7-5921-415d-8c71-d6e37421347a', '3', '0');
INSERT INTO `zl_user_system` VALUES ('c063dd0f-5767-48df-872f-501f2c11aec9', '3', '0');
INSERT INTO `zl_user_system` VALUES ('365b3f94-315c-47cc-9430-623a3192fecf', '3', '0');
INSERT INTO `zl_user_system` VALUES ('d182e667-0239-43b5-b37f-eb6e9e86c29e', '3', '0');
INSERT INTO `zl_user_system` VALUES ('46d1814d-f10f-4c71-8c40-692172a8d587', '3', '0');
INSERT INTO `zl_user_system` VALUES ('3be42bf3-5cdd-4f69-a2af-c929d496d365', '3', '0');
INSERT INTO `zl_user_system` VALUES ('eb6a4550-524c-46e4-94d4-52ff57e433ef', '3', '0');
INSERT INTO `zl_user_system` VALUES ('911a9c5b-e5fe-4a58-9552-49e78cb9b6f4', '3', '0');
INSERT INTO `zl_user_system` VALUES ('0484b68b-c77f-45a6-a30d-1e77549cf253', '3', '0');
INSERT INTO `zl_user_system` VALUES ('66e3a31f-9b31-4958-b218-a1f7f09e6724', '3', '0');
INSERT INTO `zl_user_system` VALUES ('4bf23aee-29b1-4e2d-9f5c-af3a6ed9799b', '3', '0');
INSERT INTO `zl_user_system` VALUES ('77d1a479-7a41-4aa3-b993-0b1efe627642', '3', '0');
INSERT INTO `zl_user_system` VALUES ('dcb495f1-a45b-413b-8529-3521a8cd8ec5', '3', '0');
INSERT INTO `zl_user_system` VALUES ('016b2b06-00dd-49bf-8e6f-9bf0276fd2d0', '3', '0');
INSERT INTO `zl_user_system` VALUES ('43431894-6b41-4ace-ae2c-a97f5fbc81e6', '3', '0');
INSERT INTO `zl_user_system` VALUES ('8398fecd-8a01-4f98-8ffe-4ad35774599f', '3', '0');
INSERT INTO `zl_user_system` VALUES ('599ecb6e-4bb1-4f5a-8ed1-9d78df9dadb7', '3', '0');
INSERT INTO `zl_user_system` VALUES ('6fb33cf7-a2d8-4c61-8ad6-6356c2f73aba', '3', '0');
INSERT INTO `zl_user_system` VALUES ('102248a3-77f2-40ed-a6be-c6a523fd6891', '3', '0');
INSERT INTO `zl_user_system` VALUES ('c946d90d-3049-4d94-b850-e4c5bfb7675c', '3', '0');
INSERT INTO `zl_user_system` VALUES ('c1827c9b-9e63-4e2a-9ed1-35f2aec6fe83', '3', '0');
INSERT INTO `zl_user_system` VALUES ('6cdb9aba-e129-4146-bff2-fdeddbb8d4fd', '3', '0');
INSERT INTO `zl_user_system` VALUES ('15f7663b-26cd-4896-b420-5ab0f10ca166', '3', '0');
INSERT INTO `zl_user_system` VALUES ('6b508855-f85d-43cf-ae9e-315a39176a0c', '3', '0');
INSERT INTO `zl_user_system` VALUES ('e9d54068-55b5-4683-929a-81521d2302d7', '3', '0');
INSERT INTO `zl_user_system` VALUES ('11b21e9f-87a3-4c84-bc62-455b8a7a2a11', '3', '0');
INSERT INTO `zl_user_system` VALUES ('c99b47bf-42c7-45cd-8db8-b6a2e33a2714', '3', '0');
INSERT INTO `zl_user_system` VALUES ('bc312708-26be-4fa5-b1d0-2e541d34f313', '3', '0');
INSERT INTO `zl_user_system` VALUES ('15846252-2d44-4665-90c7-9e6590f566d8', '3', '0');
INSERT INTO `zl_user_system` VALUES ('05254d95-fa45-4b47-a369-3d3de84735e0', '3', '0');
INSERT INTO `zl_user_system` VALUES ('bd64b582-1ad5-49ff-bca4-2d138e47bc60', '3', '0');
INSERT INTO `zl_user_system` VALUES ('75818d5c-a73c-47fc-ae64-aa35deca637d', '3', '0');
INSERT INTO `zl_user_system` VALUES ('3ca5df37-d815-432c-a82a-4228f611034c', '3', '0');
INSERT INTO `zl_user_system` VALUES ('90cc945c-fadb-40eb-8321-ccad7707b951', '3', '0');
INSERT INTO `zl_user_system` VALUES ('9a0873a0-73f0-47a4-ab6f-51df243ad116', '3', '0');
INSERT INTO `zl_user_system` VALUES ('6b354bac-a24a-4eec-93cf-778e7b86f230', '3', '0');
INSERT INTO `zl_user_system` VALUES ('bda5b99d-1a55-4411-9e7a-e80b0fa12c67', '3', '0');
INSERT INTO `zl_user_system` VALUES ('24f2bfd2-7b85-4caf-ab63-81bd07204ae8', '3', '0');
INSERT INTO `zl_user_system` VALUES ('b4863315-7ca7-45fb-b3f8-38571ceae4e8', '3', '0');
INSERT INTO `zl_user_system` VALUES ('47a86906-1e7b-4183-bda0-449158ce6ff4', '3', '0');
INSERT INTO `zl_user_system` VALUES ('421cdaf5-b770-429a-839e-9317de463f6b', '3', '0');
INSERT INTO `zl_user_system` VALUES ('159367ae-f91d-4a68-9230-c3c4037f6c48', '3', '0');
INSERT INTO `zl_user_system` VALUES ('af9dce31-fb04-4ce3-9d81-7c674a4663ae', '3', '0');
INSERT INTO `zl_user_system` VALUES ('3541bf99-b395-40b3-a4a9-c6acf0fc3609', '3', '0');
INSERT INTO `zl_user_system` VALUES ('d7fa0a0f-8fcf-4988-ba2d-bd59425f0302', '3', '0');
INSERT INTO `zl_user_system` VALUES ('a3d90e93-105b-45fc-973a-98ee2e3ef9b2', '3', '0');
INSERT INTO `zl_user_system` VALUES ('36266bee-7134-469a-9345-8a9899056236', '3', '0');
INSERT INTO `zl_user_system` VALUES ('1eb4b860-caba-4f41-9e6e-c4b7ec019b6a', '3', '0');
INSERT INTO `zl_user_system` VALUES ('fbad658c-5ee1-483a-9013-f2a2f03d8b14', '3', '0');
INSERT INTO `zl_user_system` VALUES ('9fc3e826-02db-48cd-9190-7f2780603017', '3', '0');
INSERT INTO `zl_user_system` VALUES ('0a1912c5-1c66-4167-9be0-42fab32f6a23', '3', '0');
INSERT INTO `zl_user_system` VALUES ('6ec13f18-60a9-40ec-b5cd-58b889cefacc', '3', '0');
INSERT INTO `zl_user_system` VALUES ('360b5d0e-eb74-4bb6-a1d6-09b1f4e11465', '3', '0');
INSERT INTO `zl_user_system` VALUES ('a10c2894-b939-4b02-96cd-3401fb5f1e43', '3', '0');
INSERT INTO `zl_user_system` VALUES ('5edeb895-4b63-4603-a72b-31059ab5045d', '3', '0');
INSERT INTO `zl_user_system` VALUES ('06fbd117-76fe-4190-a666-cdfbc7801114', '3', '0');
INSERT INTO `zl_user_system` VALUES ('bfde3b76-8017-4b95-b717-8b322fb22866', '3', '0');
INSERT INTO `zl_user_system` VALUES ('fecdc97f-e1ad-426d-b6d2-629151c178ac', '3', '0');
INSERT INTO `zl_user_system` VALUES ('479d5661-f26b-4b4f-a29a-86ff160328f4', '3', '0');
INSERT INTO `zl_user_system` VALUES ('ad208011-1b90-4f8b-ae37-74553bb7f929', '3', '0');
INSERT INTO `zl_user_system` VALUES ('27ef9d25-472f-4004-9926-6e280372f957', '3', '0');
INSERT INTO `zl_user_system` VALUES ('87c7b94f-7ab5-492c-a1b1-32238eb08ed5', '3', '0');
INSERT INTO `zl_user_system` VALUES ('12d1a54f-ecff-4e83-b658-3b9b0bce0897', '3', '0');
INSERT INTO `zl_user_system` VALUES ('9766b925-8793-4469-b268-5f99c00a45fd', '3', '0');
INSERT INTO `zl_user_system` VALUES ('35a6c96e-bb8c-48ce-aa69-7201bbc2a9fa', '3', '0');
INSERT INTO `zl_user_system` VALUES ('9a90c6de-3fa3-41c1-b28a-3de2d19b7116', '3', '0');
INSERT INTO `zl_user_system` VALUES ('a3c7aca0-82d2-48e5-8af3-e78bb5b7fd34', '3', '0');
INSERT INTO `zl_user_system` VALUES ('ecee06ed-e478-4e79-8322-49aec7a48284', '3', '0');
INSERT INTO `zl_user_system` VALUES ('18fbc614-923d-460a-b5e5-0eecbf7c9db0', '3', '0');
INSERT INTO `zl_user_system` VALUES ('5a9748a2-1596-4e55-b420-08b314fd46f5', '3', '0');
INSERT INTO `zl_user_system` VALUES ('bb15f165-816e-42c5-81b8-a09c632ae482', '3', '0');
INSERT INTO `zl_user_system` VALUES ('dc054674-45d8-4675-bd36-a7893e251546', '3', '0');
INSERT INTO `zl_user_system` VALUES ('0bd123d1-68dc-4bcb-8d15-821afe1d56b0', '3', '0');
INSERT INTO `zl_user_system` VALUES ('50c906d7-bb95-4111-88a7-6bbfb82038d5', '3', '0');
INSERT INTO `zl_user_system` VALUES ('381414e0-6574-44de-9e0e-9bda6244d516', '3', '0');
INSERT INTO `zl_user_system` VALUES ('547cf6f1-4355-4b29-9916-c886601c8c82', '3', '0');
INSERT INTO `zl_user_system` VALUES ('e6c5597d-b7e9-44b6-bfca-7b1da0139ea6', '3', '0');
INSERT INTO `zl_user_system` VALUES ('7f779411-10b1-45f6-85e1-0faffda0ebf2', '3', '0');
INSERT INTO `zl_user_system` VALUES ('fa9b2088-03fd-44ba-9bcd-b0f9c3668d00', '3', '0');
INSERT INTO `zl_user_system` VALUES ('e2888fc3-139a-479d-b460-d0fd379beea9', '3', '0');
INSERT INTO `zl_user_system` VALUES ('93a26c68-a9b5-405d-9d6f-2987169fedd0', '3', '0');
INSERT INTO `zl_user_system` VALUES ('40cf7267-cc97-4f56-87c9-9eee6e71a28b', '3', '0');
INSERT INTO `zl_user_system` VALUES ('4bcd3301-ecad-4c39-9b43-3fdafd3197c6', '3', '0');
INSERT INTO `zl_user_system` VALUES ('d65f6e01-1227-46a9-b278-a708b5f3f61b', '3', '0');
INSERT INTO `zl_user_system` VALUES ('5f4b77ae-90cc-458c-9343-c8f198f72db5', '3', '0');
INSERT INTO `zl_user_system` VALUES ('2e98694a-a42c-427a-953b-3e5b734e9c4a', '3', '0');
INSERT INTO `zl_user_system` VALUES ('efb193d5-9064-4125-836a-29b394536024', '3', '0');
