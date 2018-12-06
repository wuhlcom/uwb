-- MySQL dump 10.13  Distrib 5.5.60, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: uwb
-- ------------------------------------------------------
-- Server version	5.5.60-0ubuntu0.14.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `uwb_absence`
--

DROP TABLE IF EXISTS `uwb_absence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `uwb_absence` (
  `id` int(20) unsigned NOT NULL AUTO_INCREMENT,
  `type` tinyint(8) DEFAULT NULL COMMENT '缺勤类型',
  `name` varchar(32) DEFAULT NULL COMMENT '名称',
  `remark` varchar(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='缺勤分类';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uwb_apis`
--

DROP TABLE IF EXISTS `uwb_apis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `uwb_apis` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `addr` varchar(64) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='url权限';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uwb_configs`
--

DROP TABLE IF EXISTS `uwb_configs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `uwb_configs` (
  `id` int(20) unsigned NOT NULL AUTO_INCREMENT,
  `start_time` int(12) DEFAULT NULL COMMENT '开始时间',
  `end_time` int(12) DEFAULT NULL COMMENT '结束时间',
  `prisoner_code` varchar(32) DEFAULT NULL COMMENT '囚徒ID',
  `absence_type` tinyint(2) DEFAULT NULL COMMENT '分配的原因',
  `available` tinyint(1) DEFAULT NULL COMMENT '0 不可用 1 可用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='囚徒时间位置配置';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uwb_coordinates`
--

DROP TABLE IF EXISTS `uwb_coordinates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=46389 DEFAULT CHARSET=utf8 COMMENT='历史坐标信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uwb_departments`
--

DROP TABLE IF EXISTS `uwb_departments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8 COMMENT='人员组织结构';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uwb_fences`
--

DROP TABLE IF EXISTS `uwb_fences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='围栏信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uwb_files`
--

DROP TABLE IF EXISTS `uwb_files`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='文件服务';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uwb_levels`
--

DROP TABLE IF EXISTS `uwb_levels`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `uwb_levels` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `level_name` varchar(16) DEFAULT NULL COMMENT '职称分类',
  `level_code` varchar(16) DEFAULT NULL,
  `parent_code` varchar(16) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `isdel` tinyint(2) DEFAULT '1' COMMENT '0 禁用 1使用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='职称分级';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uwb_modes`
--

DROP TABLE IF EXISTS `uwb_modes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `uwb_modes` (
  `id` int(20) DEFAULT NULL,
  `name` varchar(32) DEFAULT NULL COMMENT '手环模式名',
  `type` tinyint(2) DEFAULT NULL COMMENT '模式类型'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='手环工作模式';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uwb_permissions`
--

DROP TABLE IF EXISTS `uwb_permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uwb_persons`
--

DROP TABLE IF EXISTS `uwb_persons`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='定位人员信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uwb_positions`
--

DROP TABLE IF EXISTS `uwb_positions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `uwb_positions` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `position_name` varchar(32) DEFAULT NULL,
  `position_code` varchar(32) DEFAULT NULL,
  `isdel` tinyint(2) DEFAULT '1' COMMENT '0 不使用 1使用',
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='岗位分类';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uwb_role_permission`
--

DROP TABLE IF EXISTS `uwb_role_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `uwb_role_permission` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL,
  `permission_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='角色权限';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uwb_roles`
--

DROP TABLE IF EXISTS `uwb_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uwb_status`
--

DROP TABLE IF EXISTS `uwb_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=53055 DEFAULT CHARSET=utf8 COMMENT='手环状态信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uwb_strategies`
--

DROP TABLE IF EXISTS `uwb_strategies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='围栏策略';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uwb_user_role`
--

DROP TABLE IF EXISTS `uwb_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `uwb_user_role` (
  `id` int(20) NOT NULL DEFAULT '0',
  `user_id` int(20) NOT NULL,
  `role_id` int(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色用户关系';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uwb_users`
--

DROP TABLE IF EXISTS `uwb_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uwb_warnings`
--

DROP TABLE IF EXISTS `uwb_warnings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=5993 DEFAULT CHARSET=utf8 COMMENT='历史报警';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uwb_wristbands`
--

DROP TABLE IF EXISTS `uwb_wristbands`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-06-15 14:29:09
