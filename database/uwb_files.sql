/*
Navicat MySQL Data Transfer

Source Server         : 192.168.10.232
Source Server Version : 50560
Source Host           : 192.168.10.232:3306
Source Database       : uwb

Target Server Type    : MYSQL
Target Server Version : 50560
File Encoding         : 65001

Date: 2018-10-24 20:52:07
*/

SET FOREIGN_KEY_CHECKS=0;

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
