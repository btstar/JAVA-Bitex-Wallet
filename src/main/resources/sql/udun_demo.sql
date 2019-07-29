/*
SQLyog Ultimate v12.08 (64 bit)
MySQL - 5.7.23 : Database - udun_demo
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`udun_demo` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `udun_demo`;

/*Table structure for table `udun_coin` */

DROP TABLE IF EXISTS `udun_coin`;

CREATE TABLE `udun_coin` (
  `coin_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '币种id',
  `coin_type` varchar(255) DEFAULT NULL COMMENT '币种类型',
  `main_coin_type` varchar(255) DEFAULT NULL COMMENT '主币种类型',
  `coin_name` varchar(255) DEFAULT NULL COMMENT '币种名称',
  `logo` varchar(255) DEFAULT NULL COMMENT 'logo地址',
  `full_name` varchar(255) DEFAULT NULL COMMENT '名字全称',
  `symbol` varchar(255) DEFAULT NULL COMMENT '单位',
  `decimal` int(11) DEFAULT NULL COMMENT '精度',
  `master_address` varchar(255) DEFAULT NULL COMMENT '主地址',
  `min_withdraw_amount` decimal(18,8) DEFAULT NULL COMMENT '最小提币金额',
  `max_withdraw_amount` decimal(18,8) DEFAULT NULL COMMENT '最大提币金额',
  `min_deposit_amount` decimal(18,8) DEFAULT NULL COMMENT '最小充币金额',
  `fee_amount` decimal(18,8) DEFAULT NULL COMMENT '基础费用',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`coin_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

/*Table structure for table `udun_trade_record` */

DROP TABLE IF EXISTS `udun_trade_record`;

CREATE TABLE `udun_trade_record` (
  `trade_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '交易id',
  `username` varchar(255) DEFAULT NULL COMMENT '用户名',
  `coin_type` varchar(255) DEFAULT NULL COMMENT '币种类型',
  `main_coin_type` varchar(255) DEFAULT NULL COMMENT '主币种类型',
  `fee` decimal(18,8) DEFAULT NULL COMMENT '交易费用',
  `trade_type` int(11) DEFAULT NULL COMMENT '交易类型 1提币 2充币',
  `txid` varchar(255) DEFAULT NULL COMMENT 'txid',
  `business_id` varchar(255) DEFAULT NULL COMMENT '业务id号，只有提币有',
  `trade_amount` decimal(18,8) DEFAULT NULL COMMENT '交易金额',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`trade_id`)
) ENGINE=InnoDB AUTO_INCREMENT=76 DEFAULT CHARSET=utf8;

/*Table structure for table `udun_user` */

DROP TABLE IF EXISTS `udun_user`;

CREATE TABLE `udun_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(255) DEFAULT NULL COMMENT '用户名称',
  `password` varchar(255) DEFAULT NULL COMMENT '用户密码',
  `transaction_password` varchar(255) DEFAULT NULL COMMENT '交易密码',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

/*Table structure for table `udun_user_balance` */

DROP TABLE IF EXISTS `udun_user_balance`;

CREATE TABLE `udun_user_balance` (
  `balance_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '余额id',
  `username` varchar(255) NOT NULL COMMENT '用户名',
  `coin_type` varchar(255) NOT NULL COMMENT '币种类型',
  `main_coin_type` varchar(255) DEFAULT NULL COMMENT '主币种公钥',
  `address` varchar(255) DEFAULT NULL COMMENT '地址',
  `balance` decimal(18,8) DEFAULT NULL COMMENT '余额',
  `frozen_balance` decimal(18,8) DEFAULT NULL COMMENT '冻结资金',
  `status` int(11) DEFAULT NULL COMMENT '1 显示， 0 不显示',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`balance_id`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8;

/*Table structure for table `udun_withdraw_audit` */

DROP TABLE IF EXISTS `udun_withdraw_audit`;

CREATE TABLE `udun_withdraw_audit` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id号',
  `username` varchar(255) DEFAULT NULL COMMENT '用户名称',
  `coin_type` varchar(255) DEFAULT NULL COMMENT '币种类型',
  `main_coin_type` varchar(255) DEFAULT NULL COMMENT '主币种类型',
  `address` varchar(255) DEFAULT NULL COMMENT '到账地址',
  `withdraw_amount` decimal(18,8) DEFAULT NULL COMMENT '提币金额',
  `status` int(11) DEFAULT NULL COMMENT '状态: 0 待审核  1： 待支付 2：待链上确认 3：完成 4：取消 5：失败 6：审核被拒绝 7：支付拒绝',
  `fee_amount` decimal(18,8) DEFAULT NULL COMMENT '资金',
  `note` varchar(255) DEFAULT NULL COMMENT '备注',
  `business_id` varchar(255) DEFAULT NULL COMMENT '业务id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `business_id` (`business_id`)
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
