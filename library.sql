/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50720
Source Host           : localhost:3306
Source Database       : library

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2020-10-30 14:27:59
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for book
-- ----------------------------
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book` (
  `id` varchar(255) NOT NULL,
  `book_name` varchar(64) NOT NULL,
  `author` varchar(100) NOT NULL,
  `price` double(32,0) DEFAULT NULL,
  `publishing_house` varchar(100) NOT NULL,
  `amount` int(64) DEFAULT NULL,
  `create_time` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of book
-- ----------------------------
INSERT INTO `book` VALUES ('2a30646', '测试之道', '东郭', '98', '春光出版社', '98', '2020-10-30 11:17:16');
INSERT INTO `book` VALUES ('4765c04', '碳烤咖啡', '朱劲松', '78', '清华大学出版社', '90', '2020-10-30 08:40:32');
INSERT INTO `book` VALUES ('4f2b6ac', '杀死一只知更鸟', '威廉詹姆斯', '98', '清华大学出版社', '99', '2020-10-29 10:17:27');
INSERT INTO `book` VALUES ('5e8365e', '老管家', '曾晓', '88', '人民大学出版社', '34', '2020-10-30 08:53:33');
INSERT INTO `book` VALUES ('689dfc2', '水浒传', '施耐庵', '89', '译林出版社', '89', '2020-10-30 14:00:09');
INSERT INTO `book` VALUES ('7a9ee33', '算法(第四版)', '罗伯特', '136', '人民邮电出版社', '800', '2020-10-29 10:42:48');
INSERT INTO `book` VALUES ('94af34c', '活着', '余华', '45', '作家出版社', '400', '2020-10-09 09:42:51');
INSERT INTO `book` VALUES ('ca428df', 'Java并发编程实战', 'Brian Goetz', '69', '机械工业出版社', '1300', '2020-10-29 10:41:52');
INSERT INTO `book` VALUES ('d3ba8a1', '卫星定位原理与方法', '袁正午', '45', '北京大学出版社', '500', '2020-10-30 09:43:19');
INSERT INTO `book` VALUES ('dc5855a', '三国演义', '罗贯中', '32', '北京大学出版社', '343', '2020-10-02 20:46:28');
INSERT INTO `book` VALUES ('e46beb6', '小妇人', '大先生', '88', '重庆邮电大学出版社', '75', '2020-10-30 10:10:10');
INSERT INTO `book` VALUES ('f09411b', 'Java编程实战', '何玮康', '136', '江苏出版社', '89', '2020-10-30 10:17:08');
