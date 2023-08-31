/*
SQLyog Ultimate v11.24 (32 bit)
MySQL - 8.0.27 : Database - icepip_demo0
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`icepip_demo0` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

/*Table structure for table `t_order0` */

CREATE TABLE `t_order0` (
  `order_id` bigint NOT NULL COMMENT '订单号（主键）',
  `order_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单名称',
  `order_status` int DEFAULT NULL COMMENT '订单状态',
  `user_id` bigint NOT NULL COMMENT '用户id',
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

/*Data for the table `t_order0` */

/*Table structure for table `t_order1` */

CREATE TABLE `t_order1` (
  `order_id` bigint NOT NULL COMMENT '`icepip_demo1`订单号（主键）',
  `order_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单名称',
  `order_status` int DEFAULT NULL COMMENT '订单状态',
  `user_id` bigint NOT NULL COMMENT '用户id',
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

/*Data for the table `t_order1` */

insert  into `t_order1`(`order_id`,`order_name`,`order_status`,`user_id`) values (1,'订单1',1,2),(3,'Order Name 3',3,30),(5,'Order Name 5',2,50),(7,'Order Name 7',1,70),(9,'Order Name 9',3,90),(11,'Order Name 11',2,110),(13,'Order Name 13',1,130),(15,'Order Name 15',3,150),(17,'Order Name 17',2,170),(19,'Order Name 19',1,190),(1697180308914176001,'订单1',1,2);

/*Table structure for table `t_vehicle` */

CREATE TABLE `t_vehicle` (
  `id` bigint NOT NULL,
  `vehicle_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `t_vehicle` */

insert  into `t_vehicle`(`id`,`vehicle_no`) values (1,'陕A12323'),(1697189607405125634,'陕A12323');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
