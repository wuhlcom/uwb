/*
Navicat MySQL Data Transfer

Source Server         : 192.168.10.232
Source Server Version : 50560
Source Host           : 192.168.10.232:3306
Source Database       : uwb

Target Server Type    : MYSQL
Target Server Version : 50560
File Encoding         : 65001

Date: 2018-10-19 11:21:30
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='zuul路由表';

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
INSERT INTO `gateway_api_define` VALUES ('client1', '/api/client1/**', 'client1', null, '0', '1', '0', 'client1');
INSERT INTO `gateway_api_define` VALUES ('dust', '/env/dust/**', 'env-dust', null, '0', '1', '0', 'env-dust');
INSERT INTO `gateway_api_define` VALUES ('dust2', '/env/dust2/**', 'env-dust2', null, '0', '1', '0', 'env-dust2');
INSERT INTO `gateway_api_define` VALUES ('engine', '/env/engine/**', 'env-engine', null, '0', '1', '0', 'env-engine');
INSERT INTO `gateway_api_define` VALUES ('env', '/test/**', 'env-air', null, '0', '1', '0', 'env-air');
INSERT INTO `gateway_api_define` VALUES ('env-device', '/env/device/**', 'env-dust2', null, '0', '1', '0', 'env-dust2');
INSERT INTO `gateway_api_define` VALUES ('env-station', '/env/station/**', 'env-dust2', null, '0', '1', '0', 'env-dust2');
INSERT INTO `gateway_api_define` VALUES ('permission', '/permission/**', 'permission', null, '0', '1', '0', 'permission');
INSERT INTO `gateway_api_define` VALUES ('rvcbase', '/base/**', 'rvcbase', null, '0', '1', '0', 'rvcbase');
INSERT INTO `gateway_api_define` VALUES ('rvcboda', '/charge/**', 'rvcboda', null, '0', '1', '0', 'rvcboda');
INSERT INTO `gateway_api_define` VALUES ('rvcdevice', '/device/**', 'rvcdevice', null, '0', '1', '0', 'rvcdevice');
INSERT INTO `gateway_api_define` VALUES ('rvcgis', '/gis/**', 'rvcgis', null, '0', '1', '0', 'rvcgis');
INSERT INTO `gateway_api_define` VALUES ('rvcsyslog', '/systemlog/**', 'rvcsyslog', null, '0', '1', '0', 'rvcsyslog');
INSERT INTO `gateway_api_define` VALUES ('rvcunresolved', '/unresolved/**', 'rvcunresolved', null, '0', '1', '0', 'rvcunresolved');
INSERT INTO `gateway_api_define` VALUES ('user', '/user/**', 'security-authentication', null, '0', '1', '0', 'security-authentication');
INSERT INTO `gateway_api_define` VALUES ('uwb-fastdfs', '/uwb/fastdfs/**', 'uwb-fastdfs', null, '0', '1', '0', 'uwb-fastdfs');
INSERT INTO `gateway_api_define` VALUES ('uwb-producer', '/uwb/producer/**', 'uwb-producer', null, '0', '1', '0', 'uwb-producer');
INSERT INTO `gateway_api_define` VALUES ('uwb-resources', '/uwb/resources/**', 'uwb-resources', null, '0', '1', '0', 'uwb-resources');
INSERT INTO `gateway_api_define` VALUES ('video', '/video/**', 'application-video', null, '0', '1', '0', 'application-video');
INSERT INTO `gateway_api_define` VALUES ('weather', '/env/weather/**', 'env-dust2', null, '0', '1', '0', 'env-dust2');

-- ----------------------------
-- Table structure for `uwb_absence`
-- ----------------------------
DROP TABLE IF EXISTS `uwb_absence`;
CREATE TABLE `uwb_absence` (
  `id` int(20) unsigned NOT NULL AUTO_INCREMENT,
  `type` tinyint(8) DEFAULT NULL COMMENT '缺勤类型',
  `name` varchar(32) DEFAULT NULL COMMENT '名称',
  `remark` varchar(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='缺勤分类';

-- ----------------------------
-- Records of uwb_absence
-- ----------------------------

-- ----------------------------
-- Table structure for `uwb_api_gateway`
-- ----------------------------
DROP TABLE IF EXISTS `uwb_api_gateway`;
CREATE TABLE `uwb_api_gateway` (
  `id` int(50) NOT NULL,
  `path` varchar(255) NOT NULL COMMENT '接口地址 eg: ''/api/***''',
  `service_id` varchar(50) DEFAULT NULL COMMENT '服务id eg:resources',
  `url` varchar(255) DEFAULT NULL COMMENT 'ip地址和端口或域名',
  `retryable` tinyint(1) DEFAULT NULL COMMENT '是否重试',
  `enabled` tinyint(1) NOT NULL COMMENT '是否使用此路由',
  `strip_prefix` int(11) DEFAULT NULL COMMENT '是否加路由前缀',
  `api_name` varchar(255) DEFAULT NULL COMMENT '接口名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of uwb_api_gateway
-- ----------------------------
INSERT INTO `uwb_api_gateway` VALUES ('0', '/uwb/resources', 'resources', 'http://localhost:11003', '0', '1', '1', null);

-- ----------------------------
-- Table structure for `uwb_apis`
-- ----------------------------
DROP TABLE IF EXISTS `uwb_apis`;
CREATE TABLE `uwb_apis` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `addr` varchar(64) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='url权限';

-- ----------------------------
-- Records of uwb_apis
-- ----------------------------

-- ----------------------------
-- Table structure for `uwb_configs`
-- ----------------------------
DROP TABLE IF EXISTS `uwb_configs`;
CREATE TABLE `uwb_configs` (
  `id` int(20) unsigned NOT NULL AUTO_INCREMENT,
  `start_time` int(12) DEFAULT NULL COMMENT '开始时间',
  `end_time` int(12) DEFAULT NULL COMMENT '结束时间',
  `prisoner_code` varchar(32) DEFAULT NULL COMMENT '囚徒ID',
  `absence_type` tinyint(2) DEFAULT NULL COMMENT '分配的原因',
  `available` tinyint(1) DEFAULT NULL COMMENT '0 不可用 1 可用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='囚徒时间位置配置';

-- ----------------------------
-- Records of uwb_configs
-- ----------------------------

-- ----------------------------
-- Table structure for `uwb_coordinates`
-- ----------------------------
DROP TABLE IF EXISTS `uwb_coordinates`;
CREATE TABLE `uwb_coordinates` (
  `id` int(20) unsigned NOT NULL AUTO_INCREMENT,
  `tag_id` int(11) NOT NULL COMMENT '手环编号',
  `person_name` varchar(64) NOT NULL COMMENT '人名',
  `person_code` varchar(64) DEFAULT NULL COMMENT '工号',
  `strategy_name` varchar(64) DEFAULT NULL COMMENT '策略名',
  `strategy_code` varchar(64) DEFAULT NULL COMMENT '策略编号',
  `department_name` varchar(64) DEFAULT NULL COMMENT '部门名',
  `department_code` varchar(64) DEFAULT NULL COMMENT '部门编号',
  `fence_name` varchar(64) DEFAULT NULL COMMENT '围栏名',
  `fence_code` varchar(64) DEFAULT NULL COMMENT '围栏编号',
  `pos_x` decimal(12,8) DEFAULT NULL COMMENT 'x坐标',
  `pos_y` decimal(12,8) DEFAULT NULL COMMENT 'y坐标',
  `pos_z` decimal(12,8) DEFAULT NULL COMMENT 'z坐标',
  `type` tinyint(2) DEFAULT NULL COMMENT '消息类别  2 正常; 3 围栏 4 心率; 5电量;6 SOS;7 腕带;',
  `timestamp` int(12) DEFAULT NULL COMMENT '坐标产生时间',
  `level` varchar(2) DEFAULT NULL COMMENT '报警级别 2 严重 1 普通 0 提示',
  `msg` varchar(255) DEFAULT NULL COMMENT '报警消息',
  `op` tinyint(2) DEFAULT NULL,
  `power` int(11) DEFAULT NULL COMMENT '电量',
  `heart_rate` int(11) DEFAULT NULL COMMENT '心率',
  PRIMARY KEY (`id`),
  KEY `codeAndtimestamp` (`person_code`,`timestamp`) USING BTREE,
  KEY `personCode` (`person_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=398078 DEFAULT CHARSET=utf8 COMMENT='历史坐标信息';

-- ----------------------------
-- Records of uwb_coordinates
-- ----------------------------
INSERT INTO `uwb_coordinates` VALUES ('19', '4', '4号员工', '004', '禁止进入区域1', '0c026347-5d6e-453c-b43a-4b7dd2f34db0', 'UWB', '7ca0a6a6-802b-4949-a5ed-c6801f921775', '禁止进入', '4c8738c0-93e3-4c1a-934e-087fed047437', '15.21880700', '11.48951900', '2.56559800', '2', '1539856590', '0', null, null, '255', '255');
-- ----------------------------
-- Table structure for `uwb_departments`
-- ----------------------------
DROP TABLE IF EXISTS `uwb_departments`;
CREATE TABLE `uwb_departments` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `department_name` varchar(128) NOT NULL COMMENT '部门名称',
  `department_code` varchar(64) DEFAULT NULL COMMENT '部门编号,顶级部门编号为100,顶级部门的父部门编号为空',
  `remark` varchar(255) DEFAULT NULL COMMENT '说明',
  `parent_code` varchar(64) DEFAULT NULL COMMENT '上一级部门',
  `leader_code` varchar(64) DEFAULT NULL COMMENT '领导编号',
  `order_num` int(64) DEFAULT NULL COMMENT '排序编号',
  `isdel` tinyint(2) NOT NULL DEFAULT '1' COMMENT '0 删除 1 保留',
  `created_at` int(12) DEFAULT NULL,
  `pid` int(11) DEFAULT NULL COMMENT '父Id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8 COMMENT='人员组织结构';

-- ----------------------------
-- Records of uwb_departments
-- ----------------------------
INSERT INTO `uwb_departments` VALUES ('1', '知路科技', '100', null, '', null, null, '1', null, null);
INSERT INTO `uwb_departments` VALUES ('59', '测试', '6436e134-0331-4341-94d9-1bcec24fb013', '', '100', null, null, '0', '1528266497', null);
INSERT INTO `uwb_departments` VALUES ('60', '123', '982fa1d3-230a-428f-b691-11b7398b1069', '', '100', null, null, '0', '1528450168', null);
INSERT INTO `uwb_departments` VALUES ('61', '人事部', '3ef35982-76d7-4245-9e3d-1fe3a421cea5', '', '100', null, null, '0', '1528872432', null);
INSERT INTO `uwb_departments` VALUES ('62', '研发部', '9f42802b-c054-4c78-bfad-695fc973e965', '', '100', null, null, '1', '1528872444', null);
INSERT INTO `uwb_departments` VALUES ('63', '财务部', '408c56c0-595a-48f8-9e6f-66ef34a6db64', '', '100', null, null, '1', '1528872455', null);
INSERT INTO `uwb_departments` VALUES ('64', 'UWB', '7ca0a6a6-802b-4949-a5ed-c6801f921775', '', '9f42802b-c054-4c78-bfad-695fc973e965', null, null, '1', '1528872467', null);
INSERT INTO `uwb_departments` VALUES ('65', '测试部', '23160d5d-656d-46e7-beed-1d9d237997a2', '', '9f42802b-c054-4c78-bfad-695fc973e965', null, null, '1', '1528872494', null);
INSERT INTO `uwb_departments` VALUES ('66', '会计组', '9d7717ec-7e71-4029-a362-9bf2359e0616', '', '408c56c0-595a-48f8-9e6f-66ef34a6db64', null, null, '1', '1530607668', null);
INSERT INTO `uwb_departments` VALUES ('67', '出纳组', 'b20276c0-8112-4723-8866-6ed504f0d280', '', '408c56c0-595a-48f8-9e6f-66ef34a6db64', null, null, '1', '1530607679', null);
INSERT INTO `uwb_departments` VALUES ('68', '行政部', '6d225b52-b76b-476b-b74e-8f53a017a454', '', '3ef35982-76d7-4245-9e3d-1fe3a421cea5', null, null, '0', '1530607701', null);
INSERT INTO `uwb_departments` VALUES ('69', 'HR', '811660a3-95e3-4649-bc54-743ef4247a9a', '', '3ef35982-76d7-4245-9e3d-1fe3a421cea5', null, null, '0', '1530607708', null);
INSERT INTO `uwb_departments` VALUES ('70', '人事部', 'f973a921-e39a-4c5d-aac2-e0dd60f12cf7', '', '100', null, null, '0', '1530618201', null);
INSERT INTO `uwb_departments` VALUES ('71', '4545', '059bf6c5-1300-4b1e-b0ee-b11d27b43e77', '4545', 'b20276c0-8112-4723-8866-6ed504f0d280', null, null, '0', '1531387905', null);
INSERT INTO `uwb_departments` VALUES ('72', '45454', '7811ba7b-a4bc-4d48-bf4c-0eb8685e4ae3', '56565', '408c56c0-595a-48f8-9e6f-66ef34a6db64', null, null, '0', '1531387959', null);
INSERT INTO `uwb_departments` VALUES ('73', '5656', '9173b4d4-5d3c-47d5-9762-9883d9d520f9', '565656', '7811ba7b-a4bc-4d48-bf4c-0eb8685e4ae3', null, null, '0', '1531387966', null);

-- ----------------------------
-- Table structure for `uwb_engine`
-- ----------------------------
DROP TABLE IF EXISTS `uwb_engine`;
CREATE TABLE `uwb_engine` (
  `sn` varchar(64) CHARACTER SET utf8 NOT NULL,
  `type` tinyint(2) DEFAULT NULL,
  `version` varchar(12) CHARACTER SET utf8 DEFAULT NULL,
  `name` varchar(64) CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`sn`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='定位引擎';

-- ----------------------------
-- Records of uwb_engine
-- ----------------------------

-- ----------------------------
-- Table structure for `uwb_fences`
-- ----------------------------
DROP TABLE IF EXISTS `uwb_fences`;
CREATE TABLE `uwb_fences` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `fence_name` varchar(32) DEFAULT NULL COMMENT '围栏名',
  `points` varchar(500) DEFAULT NULL COMMENT '坐标组',
  `fence_code` varchar(64) DEFAULT NULL COMMENT '编号',
  `isdel` tinyint(2) DEFAULT '1' COMMENT '0 无效 1有效',
  `remark` varchar(255) DEFAULT NULL COMMENT '描述',
  `type` tinyint(2) DEFAULT NULL COMMENT '0 多边形 1 圆形  2 椭圆 3 矩形',
  `parent_code` varchar(64) DEFAULT NULL,
  `parents` varchar(255) DEFAULT NULL,
  `created_at` int(12) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COMMENT='围栏信息';

-- ----------------------------
-- Records of uwb_fences
-- ----------------------------
INSERT INTO `uwb_fences` VALUES ('1', 'a', '[{\"x\":10.1,\"y\":0.13},{\"x\":15,\"y\":0.13},{\"x\":15,\"y\":8.95},{\"x\":10.1,\"y\":8.95}]', 'b073ef65-7349-4eee-9205-f336c686d129', '0', 'a', '3', null, null, '1528339123');
-- ----------------------------
-- Table structure for `uwb_files`
-- ----------------------------
DROP TABLE IF EXISTS `uwb_files`;
CREATE TABLE `uwb_files` (
  `id` int(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `file_name` varchar(255) DEFAULT '' COMMENT '本地文件名',
  `status` tinyint(2) DEFAULT '0' COMMENT '0 不使用 1 使用',
  `file_path` varchar(255) DEFAULT NULL COMMENT '本地文件保存路径',
  `created_at` int(12) DEFAULT NULL,
  `isdel` tinyint(2) NOT NULL DEFAULT '1' COMMENT '0 删除 1存在',
  `group_name` varchar(255) DEFAULT NULL COMMENT 'fdfs group',
  `remote_path` varchar(255) DEFAULT NULL COMMENT 'group name + remote name',
  `remote_name` varchar(255) DEFAULT NULL COMMENT 'fdfs 文件名',
  `user_code` varchar(64) DEFAULT NULL COMMENT '文件上传者code',
  `length` decimal(10,4) DEFAULT NULL,
  `width` decimal(10,4) DEFAULT NULL,
  `pic_length` int(10) DEFAULT NULL COMMENT '图片长度',
  `pic_width` int(10) DEFAULT NULL COMMENT '图片宽度',
  `remark` varchar(255) DEFAULT NULL COMMENT '描述',
  `type` tinyint(2) DEFAULT NULL COMMENT '0 图片 1文件或数据',
  `size` int(12) DEFAULT NULL COMMENT '文件大小',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8 COMMENT='文件服务';

-- ----------------------------
-- Records of uwb_files
-- ----------------------------
INSERT INTO `uwb_files` VALUES ('1', 'hk', '微信图片_20180604202259.jpg', '0', null, '1528270079', '0', 'group1', 'group1/M00/00/00/fwAAAVsXjP-APpyUAACjClfr7kk189.jpg', 'M00/00/00/fwAAAVsXjP-APpyUAACjClfr7kk189.jpg', null, '15.0000', '30.0000', '745', '800', null, '0', '41738');
INSERT INTO `uwb_files` VALUES ('2', 'hospital', '平面图.png', '0', null, '1528339073', '0', 'group1', 'group1/M00/00/00/fwAAAVsYmoGAcEH2AAcEZQbKoNA671.png', 'M00/00/00/fwAAAVsYmoGAcEH2AAcEZQbKoNA671.png', null, '15.0000', '30.0000', '3123', '3353', null, '0', '459877');
INSERT INTO `uwb_files` VALUES ('3', 'office', '办公平面图.jpg', '0', null, '1528339814', '0', 'group1', 'group1/M00/00/00/fwAAAVsYnWaANq7kAI09K9XykYs799.jpg', 'M00/00/00/fwAAAVsYnWaANq7kAI09K9XykYs799.jpg', null, '54.4250', '11.9600', '10000', '2213', null, '0', '9256235');
INSERT INTO `uwb_files` VALUES ('4', '1231', '办公平面图.jpg', '0', null, '1528798417', '0', 'group1', 'group1/M00/00/00/fwAAAVsfnNGAE20vAI09K9XykYs493.jpg', 'M00/00/00/fwAAAVsfnNGAE20vAI09K9XykYs493.jpg', null, '54.0000', '11.0000', '10000', '2213', null, '0', '9256235');
INSERT INTO `uwb_files` VALUES ('5', '123', '多围栏策略不生效未复现.png', '0', null, '1529034177', '0', 'group1', 'group1/M00/00/00/fwAAAVsjNcGAO2VrAATXi9lo1h0021.png', 'M00/00/00/fwAAAVsjNcGAO2VrAATXi9lo1h0021.png', null, '123.0000', '123.0000', '910', '375', null, '0', '317323');
INSERT INTO `uwb_files` VALUES ('6', '2323111', '办公平面图.jpg', '0', null, '1529037156', '0', 'group1', 'group1/M00/00/00/fwAAAVsjQWSANxfsAI09K9XykYs024.jpg', 'M00/00/00/fwAAAVsjQWSANxfsAI09K9XykYs024.jpg', null, '22.0000', '22.0000', '10000', '2213', null, '0', '9256235');
INSERT INTO `uwb_files` VALUES ('7', '11', '333.jpg', '0', null, '1529042387', '0', 'group1', 'group1/M00/00/00/fwAAAVsjVdOAJj9FAADbhocFS-I379.jpg', 'M00/00/00/fwAAAVsjVdOAJj9FAADbhocFS-I379.jpg', null, '11.0000', '11.0000', '1024', '640', null, '0', '56198');
INSERT INTO `uwb_files` VALUES ('8', 'show1', 'show.png', '0', null, '1529045399', '0', 'group1', 'group1/M00/00/00/fwAAAVsjYZeATn2kAAcO5meiHOo590.png', 'M00/00/00/fwAAAVsjYZeATn2kAAcO5meiHOo590.png', null, '10.0000', '10.0000', '1000', '2133', null, '0', '462566');
INSERT INTO `uwb_files` VALUES ('9', 'bin1', 'uwb180608Data.bin', '0', null, '1529045456', '0', 'group1', 'group1/M00/00/00/fwAAAVsjYdCAH7RcAAGBH4r8-zo414.bin', 'M00/00/00/fwAAAVsjYdCAH7RcAAGBH4r8-zo414.bin', null, null, null, null, null, null, '1', '98591');
INSERT INTO `uwb_files` VALUES ('10', 'bin2', 'uwb180608Data.bin', '0', null, '1529045643', '0', 'group1', 'group1/M00/00/00/fwAAAVsjYouAGaWyAAGBH4r8-zo980.bin', 'M00/00/00/fwAAAVsjYouAGaWyAAGBH4r8-zo980.bin', null, null, null, null, null, null, '1', '98591');
INSERT INTO `uwb_files` VALUES ('11', '123213', 'show.png', '0', null, '1529047929', '0', 'group1', 'group1/M00/00/00/fwAAAVsja3mAB2hlAAcO5meiHOo344.png', 'M00/00/00/fwAAAVsja3mAB2hlAAcO5meiHOo344.png', null, '1.0000', '1.0000', '2211', '1361', null, '0', '462566');
INSERT INTO `uwb_files` VALUES ('12', '1', 'E:\\工作管理\\项目\\UWB\\定位平台\\uwb180608Data.bin', '0', null, '1529056306', '0', 'group1', 'group1/M00/00/00/fwAAAVsjjDKAUgffAAGBH4r8-zo211.bin', 'M00/00/00/fwAAAVsjjDKAUgffAAGBH4r8-zo211.bin', null, null, null, null, null, null, '1', '98591');
INSERT INTO `uwb_files` VALUES ('13', '1111', 'uwb180608Data.bin', '0', null, '1529056649', '0', 'group1', 'group1/M00/00/00/fwAAAVsjjYmASDT7AAGBH4r8-zo174.bin', 'M00/00/00/fwAAAVsjjYmASDT7AAGBH4r8-zo174.bin', null, null, null, null, null, null, '1', '98591');
INSERT INTO `uwb_files` VALUES ('14', '2313', '多围栏策略不生效未复现.png', '0', null, '1530153690', '0', 'group1', 'group1/M00/00/00/fwAAAVs0StqAX-ygAATXi9lo1h0288.png', 'M00/00/00/fwAAAVs0StqAX-ygAATXi9lo1h0288.png', null, '12312.0000', '123.0000', '910', '375', null, '0', '317323');
INSERT INTO `uwb_files` VALUES ('15', 'suidao', 'suidao.jpg', '0', null, '1530583215', '0', 'group1', 'group1/M00/00/00/Cmgw3Fs62K-ATA2uAAi7CI7anno091.jpg', 'M00/00/00/Cmgw3Fs62K-ATA2uAAi7CI7anno091.jpg', null, '10.0000', '10.0000', '1000', '2133', null, '0', '572168');
INSERT INTO `uwb_files` VALUES ('16', 'suidao', 'suidao.jpg', '0', null, '1530583440', '0', 'group1', 'group1/M00/00/00/Cmgw3Fs62ZCAW-E8AAi7CI7anno225.jpg', 'M00/00/00/Cmgw3Fs62ZCAW-E8AAi7CI7anno225.jpg', null, '10.0000', '10.0000', '1000', '2133', null, '0', '572168');
INSERT INTO `uwb_files` VALUES ('17', 'suidao', 'suidao.jpg', '0', null, '1530585908', '0', 'group1', 'group1/M00/00/00/Cmgw3Fs64zSAcAhWAAi7CI7anno296.jpg', 'M00/00/00/Cmgw3Fs64zSAcAhWAAi7CI7anno296.jpg', null, '10.0000', '10.0000', '1000', '2133', null, '0', '572168');
INSERT INTO `uwb_files` VALUES ('18', 'UWB_ANC_APP_180625.bin', 'UWB_ANC_APP_180625.bin', '0', null, '1530600123', '1', 'group1', 'group1/M00/00/00/wKgK6Fs7GruABjDwAAFhIGghK0c427.bin', 'M00/00/00/wKgK6Fs7GruABjDwAAFhIGghK0c427.bin', null, null, null, null, null, null, '1', '90400');
INSERT INTO `uwb_files` VALUES ('19', 'UWB_ANC_APP_180703', 'UWB_ANC_APP_180625.bin', '0', null, '1530602247', '1', 'group1', 'group1/M00/00/00/wKgK6Fs7IweAML7EAAFhIGghK0c526.bin', 'M00/00/00/wKgK6Fs7IweAML7EAAFhIGghK0c526.bin', null, null, null, null, null, null, '1', '90400');
INSERT INTO `uwb_files` VALUES ('20', '乔丹', 'IMG_0628.JPG', '0', null, '1530608026', '0', 'group1', 'group1/M00/00/00/wKgK6Fs7OZqAEYsrAAF1c66VVf0759.JPG', 'M00/00/00/wKgK6Fs7OZqAEYsrAAF1c66VVf0759.JPG', null, '5.0000', '5.0000', '640', '640', null, '0', '95603');
INSERT INTO `uwb_files` VALUES ('21', 'suidao', '办公平面图.jpg', '0', null, '1530851549', '0', 'group1', 'group1/M00/00/00/Cmgw3Fs-8N2ANYSBAI09K9XykYs162.jpg', 'M00/00/00/Cmgw3Fs-8N2ANYSBAI09K9XykYs162.jpg', null, '55.0000', '102.0000', '1000', '2133', null, '0', '9256235');
INSERT INTO `uwb_files` VALUES ('22', 'suidao', '办公平面图.jpg', '0', null, '1530851594', '0', 'group1', 'group1/M00/00/00/Cmgw3Fs-8QqAX3-wAI09K9XykYs091.jpg', 'M00/00/00/Cmgw3Fs-8QqAX3-wAI09K9XykYs091.jpg', null, '55.0000', '102.0000', '1000', '2133', null, '0', '9256235');
INSERT INTO `uwb_files` VALUES ('23', 'suidao2', '办公平面图.jpg', '0', null, '1530851686', '0', 'group1', 'group1/M00/00/00/Cmgw3Fs-8WaAeItXAI09K9XykYs127.jpg', 'M00/00/00/Cmgw3Fs-8WaAeItXAI09K9XykYs127.jpg', null, '55.0000', '102.0000', '1000', '2133', null, '0', '9256235');
INSERT INTO `uwb_files` VALUES ('24', 'suidao2', '办公平面图.jpg', '0', null, '1530857882', '0', 'group1', 'group1/M00/00/00/Cmgw3Fs_CZqAZaOpAI09K9XykYs940.jpg', 'M00/00/00/Cmgw3Fs_CZqAZaOpAI09K9XykYs940.jpg', null, '55.0000', '102.0000', '1000', '2133', null, '0', '9256235');
INSERT INTO `uwb_files` VALUES ('25', 'suidao33333', '办公平面图.jpg', '0', null, '1530858001', '0', 'group1', 'group1/M00/00/00/Cmgw3Fs_ChGAO0kwAI09K9XykYs753.jpg', 'M00/00/00/Cmgw3Fs_ChGAO0kwAI09K9XykYs753.jpg', null, '55.0000', '102.0000', '1000', '2133', null, '0', '9256235');
INSERT INTO `uwb_files` VALUES ('26', 'office', 'office 54.425_11.96.png', '1', null, '1530865836', '1', 'group1', 'group1/M00/00/00/wKgK6Fs_KKyAb2DNAAKAbXfgw-w440.png', 'M00/00/00/wKgK6Fs_KKyAb2DNAAKAbXfgw-w440.png', null, '54.4250', '11.9600', '1500', '332', null, '0', '163949');
INSERT INTO `uwb_files` VALUES ('27', 'suidao9', '隧道9-100X40.jpg', '0', null, '1531815376', '1', 'group1', 'group1/M00/00/00/wKgK6FtNpdCAHHzlAAi7CI7anno982.jpg', 'M00/00/00/wKgK6FtNpdCAHHzlAAi7CI7anno982.jpg', null, '100.0000', '40.0000', '2891', '1051', null, '0', '572168');
INSERT INTO `uwb_files` VALUES ('28', '深圳展会8', 'zhanhui_8x8.png', '0', null, '1532573976', '1', 'group1', 'group1/M00/00/00/wKgK6FtZORiAFKQrAAe0VyR0l0o505.png', 'M00/00/00/wKgK6FtZORiAFKQrAAe0VyR0l0o505.png', null, '8.0000', '8.0000', '1600', '1600', null, '0', '504919');
INSERT INTO `uwb_files` VALUES ('29', '深圳展会6', 'zhanhui_6x6.png', '0', null, '1532574070', '1', 'group1', 'group1/M00/00/00/wKgK6FtZOXaAYkWlAA-RlM_lcRI566.png', 'M00/00/00/wKgK6FtZOXaAYkWlAA-RlM_lcRI566.png', null, '6.0000', '6.0000', '2501', '2501', null, '0', '1020308');
INSERT INTO `uwb_files` VALUES ('30', 'o3', '办公平面图54.425X11.96.png', '0', null, '1532574124', '0', 'group1', 'group1/M00/00/00/wKgK6FtZOayAYHYbAAKAbXfgw-w160.png', 'M00/00/00/wKgK6FtZOayAYHYbAAKAbXfgw-w160.png', null, '54.4250', '11.9600', '1500', '332', null, '0', '163949');
INSERT INTO `uwb_files` VALUES ('31', 'zhan8', 'zhanhui_8x8_200.png', '0', null, '1532950056', '1', 'group1', 'group1/M00/00/00/wKgK6Fte9iiAbW1qAAe0VyR0l0o105.png', 'M00/00/00/wKgK6Fte9iiAbW1qAAe0VyR0l0o105.png', null, '8.0000', '8.0000', '1600', '1600', null, '0', '504919');

-- ----------------------------
-- Table structure for `uwb_levels`
-- ----------------------------
DROP TABLE IF EXISTS `uwb_levels`;
CREATE TABLE `uwb_levels` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `level_name` varchar(16) DEFAULT NULL COMMENT '职称分类',
  `level_code` varchar(16) DEFAULT NULL,
  `parent_code` varchar(16) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `isdel` tinyint(2) DEFAULT '1' COMMENT '0 禁用 1使用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='职称分级';

-- ----------------------------
-- Records of uwb_levels
-- ----------------------------
INSERT INTO `uwb_levels` VALUES ('1', '工程师', '100', '0', null, '1');
INSERT INTO `uwb_levels` VALUES ('2', '初级工程师', '10001', '100', null, '1');
INSERT INTO `uwb_levels` VALUES ('3', '中级工程师', '10002', '100', null, '1');
INSERT INTO `uwb_levels` VALUES ('4', '高级工程师', '10003', '100', null, '1');
INSERT INTO `uwb_levels` VALUES ('5', '项目经理', '101', '0', null, '1');
INSERT INTO `uwb_levels` VALUES ('6', '初级项目经理', '10101', '101', null, '1');
INSERT INTO `uwb_levels` VALUES ('7', '高级项目经理', '10102', '101', null, '1');
INSERT INTO `uwb_levels` VALUES ('8', '中级项目经理', '10103', '101', null, '1');

-- ----------------------------
-- Table structure for `uwb_modes`
-- ----------------------------
DROP TABLE IF EXISTS `uwb_modes`;
CREATE TABLE `uwb_modes` (
  `id` int(20) DEFAULT NULL,
  `name` varchar(32) DEFAULT NULL COMMENT '手环模式名',
  `type` tinyint(2) DEFAULT NULL COMMENT '模式类型'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='手环工作模式';

-- ----------------------------
-- Records of uwb_modes
-- ----------------------------

-- ----------------------------
-- Table structure for `uwb_permissions`
-- ----------------------------
DROP TABLE IF EXISTS `uwb_permissions`;
CREATE TABLE `uwb_permissions` (
  `id` int(11) NOT NULL DEFAULT '0' COMMENT '自定id,主要供前端展示权限列表分类排序使用.',
  `menu_code` varchar(255) DEFAULT '' COMMENT '归属菜单,前端判断并展示菜单使用,',
  `menu_name` varchar(255) DEFAULT '' COMMENT '菜单的中文释义',
  `permission_code` varchar(255) DEFAULT '' COMMENT '权限的代码/通配符,对应代码中@RequiresPermissions 的value',
  `permission_name` varchar(255) DEFAULT '' COMMENT '本权限的中文释义',
  `required_permission` tinyint(1) DEFAULT '2' COMMENT '是否本菜单必选权限, 1.必选 2非必选 通常是"列表"权限是必选',
  `type` varchar(16) DEFAULT NULL COMMENT 'menu button',
  `url` varchar(32) DEFAULT NULL COMMENT '接口地址',
  `parent_id` varchar(11) DEFAULT NULL,
  `isdel` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='后台权限表 菜单';

-- ----------------------------
-- Records of uwb_permissions
-- ----------------------------
INSERT INTO `uwb_permissions` VALUES ('1', 'configuration', '系统配置', 'article:list', '列表', '1', null, null, '0', '1');
INSERT INTO `uwb_permissions` VALUES ('2', 'realtime', '实时监控', 'user:list', '列表', '1', null, null, '0', '1');
INSERT INTO `uwb_permissions` VALUES ('3', 'alarm', '告警管理', 'user:update', '修改', '2', null, null, '0', '1');
INSERT INTO `uwb_permissions` VALUES ('4', 'playback', '轨迹回放', 'role:list', '列表', '1', null, null, '0', '1');
INSERT INTO `uwb_permissions` VALUES ('5', 'role', '角色权限', 'role:add', '新增', '2', null, null, '0', '1');
INSERT INTO `uwb_permissions` VALUES ('7', 'map', '地图管理', 'role:delete', '删除', '2', null, null, '9', '1');
INSERT INTO `uwb_permissions` VALUES ('10', 'attendance', '自动考勤', 'article:list', '列表', '1', null, null, '0', '1');

-- ----------------------------
-- Table structure for `uwb_persons`
-- ----------------------------
DROP TABLE IF EXISTS `uwb_persons`;
CREATE TABLE `uwb_persons` (
  `id` int(20) unsigned NOT NULL AUTO_INCREMENT,
  `person_name` varchar(32) NOT NULL COMMENT '姓名',
  `person_code` varchar(32) NOT NULL COMMENT '编号',
  `idcard` varchar(20) DEFAULT NULL COMMENT '身份证号码',
  `birth` date DEFAULT NULL COMMENT '出生日期',
  `sex` tinyint(1) DEFAULT '1' COMMENT '性别 1 男 0女',
  `remark` varchar(255) DEFAULT NULL COMMENT '描述',
  `created_at` int(12) DEFAULT NULL COMMENT '添加时间',
  `native_place` varchar(255) DEFAULT NULL COMMENT '籍贯',
  `isdel` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1存在  0 不存在',
  `telephone` varchar(12) DEFAULT NULL COMMENT '手机号码',
  `email` varchar(32) DEFAULT NULL COMMENT '邮箱',
  `portrait` varchar(32) DEFAULT NULL COMMENT '头像url',
  `department_code` varchar(64) NOT NULL COMMENT '部门编号',
  `tag_id` int(64) DEFAULT NULL COMMENT '手环tag_id',
  `position_code` varchar(64) DEFAULT NULL COMMENT '职务',
  `level_code` varchar(64) DEFAULT NULL COMMENT '级别',
  PRIMARY KEY (`id`),
  UNIQUE KEY `pr_index` (`id`,`person_name`,`person_code`) USING BTREE,
  KEY `code_isdel` (`person_code`,`isdel`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COMMENT='定位人员信息';

-- ----------------------------
-- Records of uwb_persons
-- ----------------------------
INSERT INTO `uwb_persons` VALUES ('1', '3', '3', null, null, '1', '', null, null, '0', '', '', '', '7ca0a6a6-802b-4949-a5ed-c6801f921775', '3', '', '10001');
INSERT INTO `uwb_persons` VALUES ('2', '李四', '002', null, null, '1', '', '1528450146', null, '0', '', '', '', '100', null, '', null);
INSERT INTO `uwb_persons` VALUES ('3', '3号', '003', null, null, '1', '', null, null, '0', '', '', '', '7ca0a6a6-802b-4949-a5ed-c6801f921775', '3', '102', '10101');
INSERT INTO `uwb_persons` VALUES ('4', '42', '42', null, null, '1', '', null, null, '0', '', '', '', '7ca0a6a6-802b-4949-a5ed-c6801f921775', '42', '', null);
INSERT INTO `uwb_persons` VALUES ('5', '42', '42', null, null, '1', '', null, null, '0', '', '', '', '23160d5d-656d-46e7-beed-1d9d237997a2', '42', '104', '10003');
INSERT INTO `uwb_persons` VALUES ('6', '63', '63', null, null, '1', '', null, null, '0', '', '', '', '7ca0a6a6-802b-4949-a5ed-c6801f921775', '63', '', null);
INSERT INTO `uwb_persons` VALUES ('7', '25', '25', null, null, '1', '', '1530772921', null, '0', '', '', '', '7ca0a6a6-802b-4949-a5ed-c6801f921775', '25', '', null);
INSERT INTO `uwb_persons` VALUES ('8', '34', '34', null, null, '1', '', '1530772934', null, '0', '', '', '', '7ca0a6a6-802b-4949-a5ed-c6801f921775', '34', '', null);
INSERT INTO `uwb_persons` VALUES ('9', '33', '33', null, null, '1', '', '1531382164', null, '0', '', '', '', '100', '33', '', null);
INSERT INTO `uwb_persons` VALUES ('10', '0号员工', '000', null, null, '1', '', null, null, '1', '', '', '', '7ca0a6a6-802b-4949-a5ed-c6801f921775', '0', '103', '10101');
INSERT INTO `uwb_persons` VALUES ('11', '1号员工', '001', null, null, '1', '', '1532591119', null, '1', '', '', '', '7ca0a6a6-802b-4949-a5ed-c6801f921775', '1', '101', '10102');
INSERT INTO `uwb_persons` VALUES ('12', '2号员工', '002', null, null, '1', '', '1532591150', null, '1', '', '', '', '9d7717ec-7e71-4029-a362-9bf2359e0616', '2', '102', '10002');
INSERT INTO `uwb_persons` VALUES ('13', '3号员工', '003', null, null, '1', '', '1532591200', null, '1', '', '', '', '23160d5d-656d-46e7-beed-1d9d237997a2', '3', '104', '10003');
INSERT INTO `uwb_persons` VALUES ('14', '4号员工', '004', null, null, '1', '', null, null, '1', '', '', '', '7ca0a6a6-802b-4949-a5ed-c6801f921775', '4', '100', '10001');
INSERT INTO `uwb_persons` VALUES ('15', '33号员工', '033', null, null, '1', '', '1532591272', null, '1', '', '', '', '7ca0a6a6-802b-4949-a5ed-c6801f921775', '33', '100', '10003');
INSERT INTO `uwb_persons` VALUES ('16', '10号员工', '10', null, null, '1', '', null, null, '1', '', '', '', '23160d5d-656d-46e7-beed-1d9d237997a2', '10', '101', '10001');

-- ----------------------------
-- Table structure for `uwb_positions`
-- ----------------------------
DROP TABLE IF EXISTS `uwb_positions`;
CREATE TABLE `uwb_positions` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `position_name` varchar(32) DEFAULT NULL,
  `position_code` varchar(32) DEFAULT NULL,
  `isdel` tinyint(2) DEFAULT '1' COMMENT '0 不使用 1使用',
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='岗位分类';

-- ----------------------------
-- Records of uwb_positions
-- ----------------------------
INSERT INTO `uwb_positions` VALUES ('1', 'CEO', '100', '1', null);
INSERT INTO `uwb_positions` VALUES ('2', 'CFO', '101', '1', null);
INSERT INTO `uwb_positions` VALUES ('3', 'COO', '102', '1', null);
INSERT INTO `uwb_positions` VALUES ('4', '市场经理', '103', '1', null);
INSERT INTO `uwb_positions` VALUES ('5', '项目经理', '104', '1', null);
INSERT INTO `uwb_positions` VALUES ('6', '产品经理', '105', '1', null);

-- ----------------------------
-- Table structure for `uwb_role_permission`
-- ----------------------------
DROP TABLE IF EXISTS `uwb_role_permission`;
CREATE TABLE `uwb_role_permission` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL,
  `permission_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='角色权限';

-- ----------------------------
-- Records of uwb_role_permission
-- ----------------------------
INSERT INTO `uwb_role_permission` VALUES ('1', '1', '1');
INSERT INTO `uwb_role_permission` VALUES ('2', '1', '5');
INSERT INTO `uwb_role_permission` VALUES ('3', '1', '2');
INSERT INTO `uwb_role_permission` VALUES ('4', '1', '3');
INSERT INTO `uwb_role_permission` VALUES ('5', '1', '4');
INSERT INTO `uwb_role_permission` VALUES ('6', '1', '6');
INSERT INTO `uwb_role_permission` VALUES ('7', '1', '7');
INSERT INTO `uwb_role_permission` VALUES ('8', '1', '8');
INSERT INTO `uwb_role_permission` VALUES ('9', '1', '9');
INSERT INTO `uwb_role_permission` VALUES ('10', '1', '10');

-- ----------------------------
-- Table structure for `uwb_roles`
-- ----------------------------
DROP TABLE IF EXISTS `uwb_roles`;
CREATE TABLE `uwb_roles` (
  `id` int(20) unsigned NOT NULL AUTO_INCREMENT,
  `role_name` varchar(32) NOT NULL COMMENT '角色名',
  `role_level` tinyint(1) DEFAULT NULL COMMENT '角色级别',
  `remark` varchar(255) DEFAULT NULL COMMENT '角色描述',
  `role_code` varchar(32) DEFAULT NULL,
  `available` tinyint(1) DEFAULT '1',
  `isdel` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='角色信息';

-- ----------------------------
-- Records of uwb_roles
-- ----------------------------
INSERT INTO `uwb_roles` VALUES ('1', '管理员', null, '管理员', null, '1', '1');

-- ----------------------------
-- Table structure for `uwb_station`
-- ----------------------------
DROP TABLE IF EXISTS `uwb_station`;
CREATE TABLE `uwb_station` (
  `sn` varchar(64) NOT NULL,
  `name` varchar(32) DEFAULT NULL,
  `id` int(11) DEFAULT NULL,
  `version` varchar(12) DEFAULT NULL,
  `type` tinyint(2) DEFAULT NULL,
  PRIMARY KEY (`sn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of uwb_station
-- ----------------------------

-- ----------------------------
-- Table structure for `uwb_status`
-- ----------------------------
DROP TABLE IF EXISTS `uwb_status`;
CREATE TABLE `uwb_status` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `tag_id` int(11) NOT NULL COMMENT '手环ID',
  `move` int(11) DEFAULT NULL,
  `power` int(11) DEFAULT NULL COMMENT '电量',
  `wristlet` int(11) DEFAULT NULL COMMENT '腕带',
  `heart` int(11) DEFAULT NULL COMMENT '心率',
  `sos` int(11) DEFAULT NULL COMMENT '求救',
  `type` int(11) DEFAULT NULL COMMENT '消息类型',
  `person_name` varchar(64) DEFAULT NULL COMMENT '人名',
  `timestamp` int(11) DEFAULT NULL COMMENT '时间',
  PRIMARY KEY (`id`),
  KEY `tagId` (`tag_id`,`timestamp`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=135374 DEFAULT CHARSET=utf8 COMMENT='手环状态信息';

-- ----------------------------
-- Records of uwb_status
-- ----------------------------
INSERT INTO `uwb_status` VALUES ('1', '0', '0', '255', '0', '255', '0', '7', '0号员工', '1539865174');
-- ----------------------------
-- Table structure for `uwb_strategies`
-- ----------------------------
DROP TABLE IF EXISTS `uwb_strategies`;
CREATE TABLE `uwb_strategies` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `strategy_name` varchar(128) NOT NULL COMMENT '策略名',
  `strategy_code` varchar(64) NOT NULL COMMENT '策略编号',
  `fence_code` varchar(64) NOT NULL COMMENT '围栏编号',
  `remark` varchar(255) DEFAULT NULL COMMENT '策略备注',
  `user_type` tinyint(2) NOT NULL COMMENT '策略类型 0 个人策略 1 组策略',
  `available` tinyint(2) NOT NULL DEFAULT '1' COMMENT ' 0 不启用 1 启用',
  `start_time` time DEFAULT NULL COMMENT '策略开始时间',
  `finish_time` time DEFAULT NULL COMMENT '策略结束时间',
  `isdel` tinyint(2) NOT NULL DEFAULT '1' COMMENT '0 删除 1 保留',
  `created_at` int(12) DEFAULT NULL,
  `strategy_user_id` varchar(64) NOT NULL COMMENT '用户或用户组id',
  `level` tinyint(2) DEFAULT '0' COMMENT '0 提示 1 普通 2 严重',
  `time_type` tinyint(2) NOT NULL DEFAULT '1' COMMENT '0 日 1 周 2 月 3 季 4 年',
  `time_value` varchar(255) NOT NULL COMMENT '0 周日 1 周一 2 周二 3 周三 4 周四 5 周五 6 周六',
  `forbidden` tinyint(2) DEFAULT '0' COMMENT '0 禁止进入，1 禁止离开',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COMMENT='围栏策略';

-- ----------------------------
-- Records of uwb_strategies
-- ----------------------------
INSERT INTO `uwb_strategies` VALUES ('1', 'a', 'ff540683-f601-4a36-ae09-6f7475db671d', 'b073ef65-7349-4eee-9205-f336c686d129', null, '1', '0', '00:00:00', '23:59:59', '0', '1528339153', '100', '0', '1', '1,2,3,4,5,6', '0');
INSERT INTO `uwb_strategies` VALUES ('2', '12', 'e1b73403-6ee7-4aab-bc13-5e4fded083d3', '0ae98854-73f7-49c8-a249-590366206c7e', null, '1', '0', '00:00:00', '23:59:59', '0', '1528427738', '100', '0', '1', '1,2,3,4,5,6,0', '0');
INSERT INTO `uwb_strategies` VALUES ('3', '34', '7d6b5697-31dc-4b61-9f01-ebfe31289860', '9d34f98c-e6b5-48f9-b04b-dd759fe62913', null, '1', '0', '00:00:00', '23:59:59', '0', '1528450037', '100', '0', '1', '1,2,3,4,5,6,0', '0');
INSERT INTO `uwb_strategies` VALUES ('4', '禁入活动区', 'c8446a3c-8927-4226-83fe-0ef147eefa68', '2365b743-5a44-4f48-ba21-3ca263221926', '', '1', '0', '00:00:00', '23:59:59', '0', '1528708097', '100', '0', '1', '1,2,4,5,6,0', '0');
INSERT INTO `uwb_strategies` VALUES ('5', 'test', 'ffdb4abd-4cc6-40e3-8dfb-63bdf5a89f20', '4ea55f5b-2cf9-4add-855a-901fe571f7ae', '', '1', '0', '00:00:00', '17:35:47', '0', '1528874135', '23160d5d-656d-46e7-beed-1d9d237997a2', '0', '1', '1,2,6,0,5,4,3', '1');
INSERT INTO `uwb_strategies` VALUES ('6', '111', 'f592f79d-364a-4b35-b42c-f4cb7fc4752b', '4ea55f5b-2cf9-4add-855a-901fe571f7ae', '', '1', '0', '00:00:00', '15:19:11', '0', '1528874327', '23160d5d-656d-46e7-beed-1d9d237997a2', '0', '1', '1,2,4,5,6,0,3', '0');
INSERT INTO `uwb_strategies` VALUES ('7', '不能进', '59fd56ae-646d-4a60-b19a-be81934a398f', '15224577-7293-4c84-815b-8baffd32b182', '', '1', '0', '00:00:00', '23:59:59', '0', '1529581933', '100', '0', '1', '1,2,3,4,5,6,0', '0');
INSERT INTO `uwb_strategies` VALUES ('8', 'CTO策略', '6643505e-d7ed-4bcb-ac2c-8b9cdd9dc40c', '93e91318-6cc9-4f36-8a3b-68cff6737c7c', '', '1', '0', '00:00:00', '23:42:59', '0', '1530003083', '100', '0', '1', '1,3,4,5,6,2', '1');
INSERT INTO `uwb_strategies` VALUES ('9', '测试区策略', '00e5f59b-d347-48dd-98bd-02c58c5a41a6', '1776c4e8-2fdc-4d6c-b221-18a194324052', '', '1', '0', '09:00:00', '23:59:59', '0', '1530003113', '23160d5d-656d-46e7-beed-1d9d237997a2', '1', '1', '3,4,5,1,2', '0');
INSERT INTO `uwb_strategies` VALUES ('10', '测试策略', '986d7045-b30e-4230-8b5b-f01cc40b33d4', 'dfadd7aa-e894-4515-8154-77f96136a9bf', '', '1', '0', '00:00:00', '23:59:59', '0', '1530611744', '408c56c0-595a-48f8-9e6f-66ef34a6db64', '0', '1', '1,2,3,4,5,6,0', '0');
INSERT INTO `uwb_strategies` VALUES ('11', '测试人员', '7e4c0d85-6c49-4e5c-8d5c-4d9853faee14', '87a39da8-ca2f-4aeb-be75-5eeb8fef3e4b', '', '1', '0', '14:22:00', '14:24:59', '0', '1530684717', '9f42802b-c054-4c78-bfad-695fc973e965', '1', '1', '1,2,4,5,6,0,3', '0');
INSERT INTO `uwb_strategies` VALUES ('12', '禁止进入', 'aea0c89f-9698-4498-9d0f-0267b003bf49', 'defd6c51-dba8-4c29-a3e3-98a6ef48bb82', '', '1', '0', '00:00:00', '23:59:59', '0', '1531383213', '7ca0a6a6-802b-4949-a5ed-c6801f921775', '0', '1', '1,2,3,4,5,6,0', '0');
INSERT INTO `uwb_strategies` VALUES ('13', '禁止出去', 'ebaf20fc-8947-4074-a3f0-dcceb44c7a68', '63e8032f-aa25-42dd-9cc4-dff315de7715', null, '1', '0', '00:00:00', '23:59:59', '0', '1532596457', '7ca0a6a6-802b-4949-a5ed-c6801f921775', '0', '1', '1,2,3,4,5,6,0', '1');
INSERT INTO `uwb_strategies` VALUES ('14', '禁止出去', '6b240b59-d6bb-44db-9c58-85cfa42f31e1', '9ce30f5f-210a-4d88-b5c7-468a61e6c3a0', null, '1', '0', '00:00:00', '23:59:59', '0', '1532601135', '7ca0a6a6-802b-4949-a5ed-c6801f921775', '0', '1', '1,2,3,4,5,6,0', '1');
INSERT INTO `uwb_strategies` VALUES ('15', 'test', 'dded2be1-1429-483a-a8ba-d043d8ffd4fa', 'bae44967-c705-4e5e-a18d-414b3c60de78', null, '1', '0', '00:00:00', '23:59:59', '0', '1532949499', '100', '2', '1', '1,2,3,4,5,6,0', '0');
INSERT INTO `uwb_strategies` VALUES ('16', '测试策略', 'fdbd21f3-9440-46d3-86fb-d431b575c8df', 'a374252b-7c53-44a9-ba2c-2915083e4c4b', null, '1', '0', '00:00:00', '23:59:59', '1', '1533716351', '100', '0', '1', '1,2,3,4,5,6,0', '0');
INSERT INTO `uwb_strategies` VALUES ('17', '禁止进入区域1', '0c026347-5d6e-453c-b43a-4b7dd2f34db0', '4c8738c0-93e3-4c1a-934e-087fed047437', '', '1', '1', '00:00:00', '23:59:59', '1', '1537345703', '100', '0', '1', '1,2,3,4,5,6,0', '0');

-- ----------------------------
-- Table structure for `uwb_terminal`
-- ----------------------------
DROP TABLE IF EXISTS `uwb_terminal`;
CREATE TABLE `uwb_terminal` (
  `sn` varchar(64) NOT NULL,
  `id` int(11) NOT NULL,
  `version` varchar(12) DEFAULT NULL,
  `type` tinyint(2) DEFAULT NULL,
  `name` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`sn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='定位终端';

-- ----------------------------
-- Records of uwb_terminal
-- ----------------------------

-- ----------------------------
-- Table structure for `uwb_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `uwb_user_role`;
CREATE TABLE `uwb_user_role` (
  `id` int(20) NOT NULL DEFAULT '0',
  `user_id` int(20) NOT NULL,
  `role_id` int(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色用户关系';

-- ----------------------------
-- Records of uwb_user_role
-- ----------------------------
INSERT INTO `uwb_user_role` VALUES ('0', '1', '1');
INSERT INTO `uwb_user_role` VALUES ('1', '2', '1');
INSERT INTO `uwb_user_role` VALUES ('2', '13', '1');

-- ----------------------------
-- Table structure for `uwb_users`
-- ----------------------------
DROP TABLE IF EXISTS `uwb_users`;
CREATE TABLE `uwb_users` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(64) NOT NULL COMMENT '登录账户',
  `password` varchar(128) NOT NULL COMMENT '密码',
  `nickname` varchar(32) DEFAULT NULL COMMENT '昵称或姓名',
  `isdel` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1 存在 0 删除',
  `parent_user` int(20) DEFAULT NULL COMMENT ' 父用户ID',
  `code` varchar(32) DEFAULT NULL COMMENT '编号',
  `idcard` varchar(20) DEFAULT NULL COMMENT '身份证号码',
  `sex` tinyint(1) NOT NULL DEFAULT '1' COMMENT '性别  0 女 1 男',
  `birth` date DEFAULT NULL COMMENT '出生日期',
  `position` varchar(64) DEFAULT NULL COMMENT '职务',
  `created_at` int(11) DEFAULT NULL COMMENT '用户添加日期',
  `available` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1 启用 0 禁用',
  `telephone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `address` varchar(300) DEFAULT NULL COMMENT '联系地址',
  `email` varchar(128) DEFAULT NULL COMMENT '邮箱',
  `remark` varchar(255) DEFAULT NULL COMMENT '描述',
  `salt` varchar(64) DEFAULT NULL,
  `role_id` int(12) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='管理员信息';

-- ----------------------------
-- Records of uwb_users
-- ----------------------------
INSERT INTO `uwb_users` VALUES ('1', 'admin', '2bcae2166568ed85ce2fa296fea096e4', null, '1', null, null, null, '1', null, null, '1524197613', '1', null, null, null, null, null, null);
INSERT INTO `uwb_users` VALUES ('2', 'hongliang', '4e397d0adae6d9d17c25892d0477e5ba', null, '1', null, null, null, '1', null, null, '1524192076', '1', null, null, null, null, null, null);

-- ----------------------------
-- Table structure for `uwb_warnings`
-- ----------------------------
DROP TABLE IF EXISTS `uwb_warnings`;
CREATE TABLE `uwb_warnings` (
  `id` int(20) unsigned NOT NULL AUTO_INCREMENT,
  `tag_id` int(11) NOT NULL COMMENT '手环编号',
  `person_name` varchar(64) NOT NULL COMMENT '人名',
  `person_code` varchar(64) DEFAULT NULL COMMENT '工号',
  `department_name` varchar(64) DEFAULT NULL COMMENT '部门名',
  `department_code` varchar(64) DEFAULT NULL COMMENT '部门编号',
  `timestamp` int(12) DEFAULT NULL COMMENT '坐标产生时间',
  `strategy_name` varchar(64) DEFAULT NULL COMMENT '策略名',
  `strategy_code` varchar(64) DEFAULT NULL COMMENT '策略编号',
  `fence_name` varchar(64) DEFAULT NULL COMMENT '围栏名',
  `fence_code` varchar(64) DEFAULT NULL COMMENT '围栏编号',
  `msg` varchar(255) DEFAULT NULL COMMENT '报警消息',
  `pos_x` decimal(12,8) DEFAULT NULL COMMENT 'x坐标',
  `pos_y` decimal(12,8) DEFAULT NULL COMMENT 'y坐标',
  `pos_z` decimal(12,8) DEFAULT NULL COMMENT 'z坐标',
  `type` tinyint(2) DEFAULT NULL COMMENT '报警类型  0 围栏 1 心率 3 电量 4 求救 5 腕带',
  `level` varchar(2) DEFAULT NULL COMMENT '0 提示 1 普通 2 严重',
  `op` tinyint(2) DEFAULT NULL COMMENT '0 取消报警 1 报警',
  `status` tinyint(2) DEFAULT '0' COMMENT '0 未处理 1处理',
  `remark` varchar(64) DEFAULT NULL COMMENT '备注',
  `power` int(12) DEFAULT NULL COMMENT '电量',
  `heart` int(12) DEFAULT NULL COMMENT '心率',
  `wristlet` int(11) DEFAULT NULL COMMENT '腕带拆除状态',
  `sos` int(11) DEFAULT NULL COMMENT '求信号状态',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_tag` (`id`,`tag_id`) USING BTREE,
  UNIQUE KEY `id_time` (`id`,`timestamp`) USING BTREE,
  KEY `person` (`person_name`,`person_code`,`op`) USING BTREE,
  KEY `level_op_time` (`timestamp`,`level`,`op`) USING BTREE,
  KEY `op` (`id`,`timestamp`,`fence_name`,`msg`,`type`,`level`,`op`)
) ENGINE=InnoDB AUTO_INCREMENT=5502 DEFAULT CHARSET=utf8 COMMENT='历史报警';

-- ----------------------------
-- Records of uwb_warnings
-- ----------------------------
INSERT INTO `uwb_warnings` VALUES ('1', '3', '3', '3', 'UWB', '7ca0a6a6-802b-4949-a5ed-c6801f921775', '1525672722', null, '6643505e-d7ed-4bcb-ac2c-8b9cdd9dc40c', 'cto', '93e91318-6cc9-4f36-8a3b-68cff6737c7c', '非法离开区域:cto', '2.00000000', '1.00000000', '5.00000000', '0', '0', '1', '0', null, null, null, null, null);

-- ----------------------------
-- Table structure for `uwb_wristbands`
-- ----------------------------
DROP TABLE IF EXISTS `uwb_wristbands`;
CREATE TABLE `uwb_wristbands` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `code` varchar(32) NOT NULL COMMENT '手环编码',
  `type` tinyint(2) DEFAULT NULL COMMENT '手环类型',
  `name` varchar(32) DEFAULT NULL COMMENT '手环名字',
  `remark` varchar(255) DEFAULT NULL COMMENT '描述',
  `mode` tinyint(2) DEFAULT NULL COMMENT '1  睡眠  2 工作 3 学习 4其他',
  `tag_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='手环信息';

-- ----------------------------
-- Records of uwb_wristbands
-- ----------------------------
INSERT INTO `uwb_wristbands` VALUES ('4', '', '0', '定位手环', '测试手环', '1', '0');
INSERT INTO `uwb_wristbands` VALUES ('5', '', '0', '定位手环', '测试手环', '1', '1');
INSERT INTO `uwb_wristbands` VALUES ('6', '', '0', '定位手环', '测试手环', '1', '2');
INSERT INTO `uwb_wristbands` VALUES ('7', '', '0', '定位手环', '测试手环', '1', '3');
INSERT INTO `uwb_wristbands` VALUES ('8', '', '0', '定位手环', '测试手环', '1', '4');
