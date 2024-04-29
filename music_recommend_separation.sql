/*
 Navicat Premium Data Transfer

 Source Server         : loacl
 Source Server Type    : MySQL
 Source Server Version : 80100
 Source Host           : localhost:3306
 Source Schema         : music_recommend_separation

 Target Server Type    : MySQL
 Target Server Version : 80100
 File Encoding         : 65001

 Date: 29/04/2024 21:25:43
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin_info
-- ----------------------------
DROP TABLE IF EXISTS `admin_info`;
CREATE TABLE `admin_info`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `admin_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '管理员名',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邮箱',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `createtime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `role` tinyint NOT NULL COMMENT '角色类型(0:超管 1:普管)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_name_password`(`admin_name`, `password`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '后台管理员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin_info
-- ----------------------------
INSERT INTO `admin_info` VALUES (1, 'admin_llq', '1276602269@qq.com', '$2a$10$fesQCCxOr/uffXACHDKc2uAM6GZcPzLI475YWrcvrG7wwa6Z48Hea', '2024-04-15 23:39:02', 0);

-- ----------------------------
-- Table structure for music_info
-- ----------------------------
DROP TABLE IF EXISTS `music_info`;
CREATE TABLE `music_info`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `music_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '音乐名称',
  `author` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '音乐作者',
  `play_duration` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '播放时长',
  `play_num` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '播放量',
  `intro` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_name`(`music_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '音乐信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of music_info
-- ----------------------------
INSERT INTO `music_info` VALUES (1, '鼓楼', '赵雷', '04:41', 15, NULL);
INSERT INTO `music_info` VALUES (2, '玛丽', '赵雷', '04:42', 4, NULL);
INSERT INTO `music_info` VALUES (3, '我记得', '赵雷', '05:29', 1, NULL);
INSERT INTO `music_info` VALUES (4, '成都', '赵雷', '05:28', 13, NULL);
INSERT INTO `music_info` VALUES (5, '晴る', 'ヨルシカ', '04:30', 11, NULL);
INSERT INTO `music_info` VALUES (6, '花人局', 'ヨルシカ ', '05:33', 6, NULL);
INSERT INTO `music_info` VALUES (7, '春泥棒', 'ヨルシカ ', '04:50', 22, NULL);
INSERT INTO `music_info` VALUES (8, 'Five Hundred Miles', 'Justin Timberlake; Carey Mulligan; Stark Sands', '01:00', 8, NULL);
INSERT INTO `music_info` VALUES (9, 'The Nights', 'Acivii', '02:54', 6, NULL);
INSERT INTO `music_info` VALUES (10, 'One Last Kiss', '宇多田ヒカル', '01:00', 9, NULL);
INSERT INTO `music_info` VALUES (11, '盛夏的果实', '莫文蔚', '04:11', 21, NULL);
INSERT INTO `music_info` VALUES (12, '飘洋过海来看你', '周深', '03:02', 13, NULL);
INSERT INTO `music_info` VALUES (14, '心墙', '林俊杰', '03:54', 0, NULL);
INSERT INTO `music_info` VALUES (15, 'Monsters', 'Katie Sky', '04:14', 1, NULL);
INSERT INTO `music_info` VALUES (16, '1-800-273-8255', 'Tiffany Alvord', '04:45', 2, NULL);
INSERT INTO `music_info` VALUES (17, 'BRETHLESS', '小林未郁', '04:06', 0, NULL);
INSERT INTO `music_info` VALUES (18, '一程山路', '毛不易', '04:21', 0, NULL);
INSERT INTO `music_info` VALUES (19, '盗将行', '花粥&马雨阳', '03:27', 0, NULL);
INSERT INTO `music_info` VALUES (20, '易燃易爆炸', '陈粒', '04:10', 1, NULL);
INSERT INTO `music_info` VALUES (21, '水星记', '郭顶', '04:04', 0, NULL);
INSERT INTO `music_info` VALUES (22, '我们终会拥有美好的未来', 'C.S.B.Q', '04:42', 0, NULL);
INSERT INTO `music_info` VALUES (23, '像风一样自由', '许巍', '04:01', 0, NULL);
INSERT INTO `music_info` VALUES (24, '紫荆花盛开', '陈之', '04:47', 1, NULL);
INSERT INTO `music_info` VALUES (25, 'Sincerely', '唐沢美帆', '04:55', 0, NULL);
INSERT INTO `music_info` VALUES (26, '阿修羅ちゃん', 'Ado', '03:35', 0, NULL);
INSERT INTO `music_info` VALUES (27, '残響散歌', 'Aimer', '04:48', 1, NULL);
INSERT INTO `music_info` VALUES (28, 'One Call Away', 'Charlie Puth', '03:55', 0, NULL);
INSERT INTO `music_info` VALUES (29, 'Stronger', 'Kelly Clarkson', '03:49', 1, NULL);
INSERT INTO `music_info` VALUES (30, 'Butterfly', '姚智鑫', '03:35', 0, NULL);

-- ----------------------------
-- Table structure for music_type
-- ----------------------------
DROP TABLE IF EXISTS `music_type`;
CREATE TABLE `music_type`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `music_id` int UNSIGNED NOT NULL COMMENT '音乐ID',
  `tag_id` int UNSIGNED NOT NULL COMMENT '标签ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_music_tag_id`(`music_id`, `tag_id`) USING BTREE,
  INDEX `idx_tag_id`(`tag_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 80 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of music_type
-- ----------------------------
INSERT INTO `music_type` VALUES (21, 1, 1);
INSERT INTO `music_type` VALUES (22, 1, 4);
INSERT INTO `music_type` VALUES (23, 2, 1);
INSERT INTO `music_type` VALUES (24, 2, 4);
INSERT INTO `music_type` VALUES (25, 3, 1);
INSERT INTO `music_type` VALUES (26, 3, 4);
INSERT INTO `music_type` VALUES (27, 4, 1);
INSERT INTO `music_type` VALUES (28, 4, 4);
INSERT INTO `music_type` VALUES (29, 5, 2);
INSERT INTO `music_type` VALUES (30, 5, 6);
INSERT INTO `music_type` VALUES (31, 6, 2);
INSERT INTO `music_type` VALUES (32, 6, 6);
INSERT INTO `music_type` VALUES (33, 7, 2);
INSERT INTO `music_type` VALUES (34, 7, 6);
INSERT INTO `music_type` VALUES (35, 8, 3);
INSERT INTO `music_type` VALUES (36, 8, 4);
INSERT INTO `music_type` VALUES (37, 9, 3);
INSERT INTO `music_type` VALUES (38, 9, 6);
INSERT INTO `music_type` VALUES (39, 10, 2);
INSERT INTO `music_type` VALUES (40, 10, 6);
INSERT INTO `music_type` VALUES (42, 11, 1);
INSERT INTO `music_type` VALUES (44, 11, 5);
INSERT INTO `music_type` VALUES (43, 12, 1);
INSERT INTO `music_type` VALUES (45, 12, 5);
INSERT INTO `music_type` VALUES (47, 14, 1);
INSERT INTO `music_type` VALUES (48, 14, 5);
INSERT INTO `music_type` VALUES (49, 15, 3);
INSERT INTO `music_type` VALUES (50, 15, 5);
INSERT INTO `music_type` VALUES (51, 16, 3);
INSERT INTO `music_type` VALUES (52, 16, 5);
INSERT INTO `music_type` VALUES (53, 17, 2);
INSERT INTO `music_type` VALUES (54, 17, 6);
INSERT INTO `music_type` VALUES (55, 18, 1);
INSERT INTO `music_type` VALUES (56, 18, 5);
INSERT INTO `music_type` VALUES (57, 19, 1);
INSERT INTO `music_type` VALUES (58, 19, 4);
INSERT INTO `music_type` VALUES (59, 20, 1);
INSERT INTO `music_type` VALUES (60, 20, 4);
INSERT INTO `music_type` VALUES (61, 21, 1);
INSERT INTO `music_type` VALUES (62, 21, 4);
INSERT INTO `music_type` VALUES (63, 22, 1);
INSERT INTO `music_type` VALUES (64, 22, 6);
INSERT INTO `music_type` VALUES (65, 23, 1);
INSERT INTO `music_type` VALUES (66, 23, 6);
INSERT INTO `music_type` VALUES (67, 24, 1);
INSERT INTO `music_type` VALUES (68, 24, 5);
INSERT INTO `music_type` VALUES (69, 25, 2);
INSERT INTO `music_type` VALUES (70, 25, 5);
INSERT INTO `music_type` VALUES (71, 26, 2);
INSERT INTO `music_type` VALUES (72, 26, 6);
INSERT INTO `music_type` VALUES (73, 27, 2);
INSERT INTO `music_type` VALUES (74, 27, 5);
INSERT INTO `music_type` VALUES (75, 28, 3);
INSERT INTO `music_type` VALUES (76, 28, 5);
INSERT INTO `music_type` VALUES (77, 29, 3);
INSERT INTO `music_type` VALUES (78, 29, 6);
INSERT INTO `music_type` VALUES (79, 30, 3);
INSERT INTO `music_type` VALUES (80, 30, 5);

-- ----------------------------
-- Table structure for persistent_logins
-- ----------------------------
DROP TABLE IF EXISTS `persistent_logins`;
CREATE TABLE `persistent_logins`  (
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `series` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `token` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `last_used` timestamp NOT NULL,
  PRIMARY KEY (`series`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of persistent_logins
-- ----------------------------

-- ----------------------------
-- Table structure for tag_info
-- ----------------------------
DROP TABLE IF EXISTS `tag_info`;
CREATE TABLE `tag_info`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tag_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标签名称',
  `createtime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `intro` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `tag_category` tinyint NOT NULL COMMENT '标签类别\r\n1：语言；2：风格',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_name`(`tag_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tag_info
-- ----------------------------
INSERT INTO `tag_info` VALUES (1, '华语', '2024-04-08 13:30:56', '华语歌曲', 1);
INSERT INTO `tag_info` VALUES (2, '日语', '2024-04-08 13:31:07', '日语歌曲', 1);
INSERT INTO `tag_info` VALUES (3, '欧美', '2024-04-08 13:31:19', '欧美歌曲', 1);
INSERT INTO `tag_info` VALUES (4, '民谣', '2024-04-08 13:35:44', NULL, 2);
INSERT INTO `tag_info` VALUES (5, '流行', '2024-04-08 13:35:44', NULL, 2);
INSERT INTO `tag_info` VALUES (6, '摇滚', '2024-04-08 13:33:08', NULL, 2);

-- ----------------------------
-- Table structure for user_collect
-- ----------------------------
DROP TABLE IF EXISTS `user_collect`;
CREATE TABLE `user_collect`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int UNSIGNED NOT NULL COMMENT '用户ID',
  `music_id` int UNSIGNED NOT NULL COMMENT '音乐ID',
  `createtime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_music_id`(`user_id`, `music_id`) USING BTREE,
  INDEX `idx_music_id`(`music_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_collect
-- ----------------------------
INSERT INTO `user_collect` VALUES (6, 1, 1, '2024-04-12 11:28:37');
INSERT INTO `user_collect` VALUES (7, 1, 7, '2024-04-12 21:38:31');
INSERT INTO `user_collect` VALUES (10, 2, 5, '2024-04-13 16:39:38');
INSERT INTO `user_collect` VALUES (11, 2, 1, '2024-04-13 16:39:39');
INSERT INTO `user_collect` VALUES (12, 1, 14, '2024-04-18 17:40:17');
INSERT INTO `user_collect` VALUES (13, 1, 11, '2024-04-18 17:40:33');
INSERT INTO `user_collect` VALUES (14, 1, 10, '2024-04-18 17:40:40');
INSERT INTO `user_collect` VALUES (16, 3, 11, '2024-04-26 09:44:28');

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邮箱',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `intro` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_name_password`(`user_name`, `password`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_info
-- ----------------------------
INSERT INTO `user_info` VALUES (1, 'jackLi', '1276602269@qq.com', '$2a$10$cu3McbJ7xise///a5rhh3e6e8d1BMk7BK3v99HK36cKTmLYKRXLC6', NULL);
INSERT INTO `user_info` VALUES (2, 'jackLi2', '3506973937@qq.com', '$2a$10$piYVyATLOIvYpiLnhjCktea4AxLRj7eK0D8agC1lEJ9weHUxKfwdy', NULL);
INSERT INTO `user_info` VALUES (3, 'xhqiang', '2054014267@qq.com', '$2a$10$QgHyUOuxEtmvPyn/9VNLZe0XTygCFVi2MPrxqd/Yyi3Utf2vYC6w2', NULL);

-- ----------------------------
-- Table structure for user_likedtag
-- ----------------------------
DROP TABLE IF EXISTS `user_likedtag`;
CREATE TABLE `user_likedtag`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int UNSIGNED NOT NULL COMMENT '用户ID',
  `tag_id` int UNSIGNED NOT NULL COMMENT '标签ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_tag_id`(`user_id`, `tag_id`) USING BTREE,
  INDEX `idx_tag_id`(`tag_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_likedtag
-- ----------------------------
INSERT INTO `user_likedtag` VALUES (8, 1, 2);
INSERT INTO `user_likedtag` VALUES (9, 1, 5);
INSERT INTO `user_likedtag` VALUES (10, 1, 6);
INSERT INTO `user_likedtag` VALUES (11, 2, 2);
INSERT INTO `user_likedtag` VALUES (12, 2, 6);

-- ----------------------------
-- Table structure for user_play
-- ----------------------------
DROP TABLE IF EXISTS `user_play`;
CREATE TABLE `user_play`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int UNSIGNED NOT NULL COMMENT '用户ID',
  `music_id` int UNSIGNED NOT NULL COMMENT '音乐ID',
  `play_num` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '播放次数',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_music_id`(`user_id`, `music_id`) USING BTREE,
  INDEX `idx_music_id`(`music_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_play
-- ----------------------------
INSERT INTO `user_play` VALUES (15, 1, 2, 1);
INSERT INTO `user_play` VALUES (16, 1, 15, 1);
INSERT INTO `user_play` VALUES (17, 1, 16, 2);
INSERT INTO `user_play` VALUES (18, 1, 24, 1);
INSERT INTO `user_play` VALUES (19, 1, 27, 1);
INSERT INTO `user_play` VALUES (20, 1, 20, 1);
INSERT INTO `user_play` VALUES (21, 1, 29, 1);
INSERT INTO `user_play` VALUES (23, 3, 11, 1);

SET FOREIGN_KEY_CHECKS = 1;
