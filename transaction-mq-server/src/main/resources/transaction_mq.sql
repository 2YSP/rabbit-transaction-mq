/*
SQLyog Ultimate v11.33 (64 bit)
MySQL - 5.7.28-log : Database - transaction_mq
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`transaction_mq` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `transaction_mq`;

/*Table structure for table `transaction_message` */

DROP TABLE IF EXISTS `transaction_message`;

CREATE TABLE `transaction_message` (
  `id` bigint(20) NOT NULL,
  `current_retry_times` int(11) NOT NULL DEFAULT '0' COMMENT '当前重试次数',
  `max_retry_times` int(11) NOT NULL DEFAULT '0',
  `queue_name` varchar(200) NOT NULL DEFAULT '',
  `exchange_name` varchar(200) NOT NULL DEFAULT '',
  `exchange_type` varchar(50) NOT NULL DEFAULT '',
  `routing_key` varchar(32) NOT NULL DEFAULT '',
  `business_module` varchar(32) NOT NULL DEFAULT '',
  `business_key` varchar(32) NOT NULL DEFAULT '',
  `message_status` int(11) DEFAULT NULL COMMENT '1=成功0=待处理-1=失败',
  `init_backoff` bigint(11) NOT NULL DEFAULT '0',
  `backoff_factor` int(11) NOT NULL DEFAULT '0',
  `is_final` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否为最终状态：1=是2=不是',
  `next_schedule_time` datetime DEFAULT NULL COMMENT '下次重试时间',
  `edit_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Table structure for table `transaction_message_content` */

DROP TABLE IF EXISTS `transaction_message_content`;

CREATE TABLE `transaction_message_content` (
  `id` bigint(20) NOT NULL,
  `message_id` bigint(20) NOT NULL DEFAULT '0',
  `content` varchar(500) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
