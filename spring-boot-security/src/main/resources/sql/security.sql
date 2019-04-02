/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1_3306
Source Server Version : 50555
Source Host           : 127.0.0.1:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50555
File Encoding         : 65001

Date: 2018-03-25 14:34:02
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for security_authority
-- ----------------------------
DROP TABLE IF EXISTS `security_authority`;
CREATE TABLE `security_authority` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `method` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of security_authority
-- ----------------------------
INSERT INTO `security_authority` VALUES ('1', '主页', '/index', null, '1');
INSERT INTO `security_authority` VALUES ('2', '更多', '/more', null, '1');
INSERT INTO `security_authority` VALUES ('3', '后台', '/admin', null, '1');

-- ----------------------------
-- Table structure for security_role
-- ----------------------------
DROP TABLE IF EXISTS `security_role`;
CREATE TABLE `security_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of security_role
-- ----------------------------
INSERT INTO `security_role` VALUES ('1', '管理员');
INSERT INTO `security_role` VALUES ('2', '会员');
INSERT INTO `security_role` VALUES ('3', '普通用户');

-- ----------------------------
-- Table structure for security_role_auth
-- ----------------------------
DROP TABLE IF EXISTS `security_role_auth`;
CREATE TABLE `security_role_auth` (
  `role_id` int(11) NOT NULL,
  `auth_id` int(11) NOT NULL,
  PRIMARY KEY (`role_id`,`auth_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of security_role_auth
-- ----------------------------
INSERT INTO `security_role_auth` VALUES ('1', '1');
INSERT INTO `security_role_auth` VALUES ('1', '2');
INSERT INTO `security_role_auth` VALUES ('1', '3');
INSERT INTO `security_role_auth` VALUES ('2', '1');
INSERT INTO `security_role_auth` VALUES ('2', '2');
INSERT INTO `security_role_auth` VALUES ('3', '1');

-- ----------------------------
-- Table structure for security_user
-- ----------------------------
DROP TABLE IF EXISTS `security_user`;
CREATE TABLE `security_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`,`username`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of security_user
-- ----------------------------
INSERT INTO `security_user` VALUES ('1', 'admin', '大佬', 'e601d554448b13da22a64e9b1979b47e');
INSERT INTO `security_user` VALUES ('2', 'braska', '可爱又迷人', 'e601d554448b13da22a64e9b1979b47e');
INSERT INTO `security_user` VALUES ('3', 'syher', '开局一把枪', 'e601d554448b13da22a64e9b1979b47e');

-- ----------------------------
-- Table structure for security_user_role
-- ----------------------------
DROP TABLE IF EXISTS `security_user_role`;
CREATE TABLE `security_user_role` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of security_user_role
-- ----------------------------
INSERT INTO `security_user_role` VALUES ('1', '1');
INSERT INTO `security_user_role` VALUES ('2', '2');
INSERT INTO `security_user_role` VALUES ('3', '3');
