/*
Navicat MySQL Data Transfer

Source Server         : test
Source Server Version : 50616
Source Database       : win_testng

Target Server Type    : MYSQL
Target Server Version : 50616
File Encoding         : 65001

Date: 2019-07-25 11:53:04
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for admin_resource
-- ----------------------------
CREATE TABLE `admin_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `name` varchar(64) NOT NULL COMMENT '资源名称',
  `code` varchar(64) DEFAULT '' COMMENT '编码',
  `global_code` varchar(1024) DEFAULT '' COMMENT '全局编码, 格式: 系统code.菜单code.thisCode',
  `value` varchar(100) DEFAULT NULL COMMENT '资源路径',
  `ajax_urls` varchar(1000) DEFAULT NULL COMMENT '后端接口地址，支持多个，中间用,隔开',
  `description` varchar(255) DEFAULT NULL COMMENT '资源介绍',
  `icon` varchar(32) DEFAULT NULL COMMENT '资源图标',
  `pid` bigint(20) DEFAULT '0' COMMENT '父级资源id',
  `seq` bigint(20) NOT NULL DEFAULT '0' COMMENT '排序',
  `status` tinyint(2) NOT NULL DEFAULT '1' COMMENT '状态，0:无效 , 1:有效',
  `resource_type` tinyint(2) NOT NULL DEFAULT '0' COMMENT '资源类别, 0:无特别作用，1:菜单，2:其他',
  `belong_roles` varchar(255) DEFAULT NULL COMMENT '属于哪些角色拥有，中间用,隔开， NULL不限制',
  `deleted` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`id`),
  KEY `idx_value` (`value`),
  KEY `idx_pid_code` (`pid`,`code`) USING BTREE,
  KEY `idx_seq` (`seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资源';

-- ----------------------------
-- Table structure for admin_role
-- ----------------------------
CREATE TABLE `admin_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `name` varchar(64) NOT NULL COMMENT '角色名',
  `description` varchar(255) DEFAULT NULL COMMENT '简介',
  `seq` bigint(20) NOT NULL DEFAULT '0' COMMENT '排序号',
  `status` tinyint(2) NOT NULL DEFAULT '1' COMMENT '状态，0:无效 , 1:有效',
  `deleted` bit(1) NOT NULL DEFAULT b'0',
  `dtype` varchar(255) DEFAULT '' COMMENT 'entity间有继承关系时，hibernate需要的type来区分',
  PRIMARY KEY (`id`),
  KEY `idx_seq` (`seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色';

-- ----------------------------
-- Table structure for admin_role_resource
-- ----------------------------
CREATE TABLE `admin_role_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  `resource_id` bigint(20) NOT NULL COMMENT '资源id',
  PRIMARY KEY (`id`),
  KEY `idx_role_resource_ids` (`role_id`,`resource_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=107 DEFAULT CHARSET=utf8 COMMENT='角色资源';

-- ----------------------------
-- Table structure for admin_sys_log
-- ----------------------------
CREATE TABLE `admin_sys_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `user_name` varchar(255) DEFAULT NULL COMMENT '登陆名',
  `role_name` varchar(255) DEFAULT NULL COMMENT '角色名',
  `opt_content` text COMMENT '内容',
  `client_ip` varchar(255) DEFAULT NULL COMMENT '客户端ip',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='系统日志';

-- ----------------------------
-- Table structure for admin_user
-- ----------------------------
CREATE TABLE `admin_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `user_name` varchar(64) NOT NULL COMMENT '登陆名',
  `name` varchar(64) DEFAULT NULL COMMENT '用户名',
  `mobile` varchar(20) DEFAULT NULL COMMENT '手机号',
  `status` tinyint(2) NOT NULL DEFAULT '1' COMMENT '状态，0:无效 , 1:有效',
  `super_admin_flag` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否是超级管理员',
  `deleted` bit(1) NOT NULL DEFAULT b'0',
  `dtype` varchar(255) DEFAULT '' COMMENT 'entity间有继承关系时，hibernate需要的type来区分',
  PRIMARY KEY (`id`),
  KEY `idx_mobile` (`mobile`),
  KEY `idx_user_name` (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户';

-- ----------------------------
-- Table structure for admin_user_role
-- ----------------------------
CREATE TABLE `admin_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  PRIMARY KEY (`id`),
  KEY `idx_user_role_ids` (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色';

-- ----------------------------
-- Table structure for sys_settings
-- ----------------------------
CREATE TABLE `sys_settings` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `key` varchar(100) DEFAULT NULL COMMENT '名称',
  `value` varchar(4096) DEFAULT NULL COMMENT '值',
  `description` varchar(4096) DEFAULT NULL COMMENT '描述',
  `deleted` bit(1) DEFAULT b'0' COMMENT '是否删除， 1:删除， 0:有效',
  PRIMARY KEY (`id`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_key` (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';

-- ----------------------------
-- Table structure for action_template
-- ----------------------------
CREATE TABLE `action_template` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator_uid` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `name` varchar(100) DEFAULT NULL COMMENT '接口名称',
  `description` varchar(4096) DEFAULT NULL COMMENT '描述',
  `params` varchar(4096) DEFAULT NULL,
  `base_params` text COMMENT '基础数据配置，json格式',
  `actions` text COMMENT '内容，json格式',
  `result` tinytext,
  `concurrent_cache_support` bit(1) DEFAULT b'0' COMMENT '是否支持并发缓存， true：支持， false:不支持',
  `deleted` bit(1) DEFAULT b'0' COMMENT '是否删除， 1:删除， 0:有效',
  PRIMARY KEY (`id`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for global_params
-- ----------------------------
CREATE TABLE `global_params` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `key` varchar(100) DEFAULT NULL COMMENT '名称',
  `value` varchar(4096) DEFAULT NULL COMMENT '值',
  `description` varchar(4096) DEFAULT NULL COMMENT '描述',
  `deleted` bit(1) DEFAULT b'0' COMMENT '是否删除， 1:删除， 0:有效',
  PRIMARY KEY (`id`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_key` (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for module
-- ----------------------------
CREATE TABLE `module` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `project_id` bigint(20) DEFAULT NULL COMMENT '所属项目',
  `description` varchar(4096) DEFAULT NULL COMMENT '描述',
  `deleted` bit(1) DEFAULT b'0' COMMENT '是否删除， 1:删除， 0:有效',
  PRIMARY KEY (`id`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_name` (`name`),
  KEY `idx_project_id` (`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for project
-- ----------------------------
CREATE TABLE `project` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `integration_flag` bit(1) DEFAULT b'0' COMMENT '集成状态，true：是 ; false：否',
  `description` varchar(4096) DEFAULT NULL COMMENT '描述',
  `mail_address` varchar(3000) DEFAULT NULL COMMENT '配置项目邮件发送地址',
  `target_count` bigint(20) DEFAULT '0' COMMENT '目标数量',
  `finish_count` bigint(20) DEFAULT '0' COMMENT '完成数量',
  `test_owner_ids` varchar(100) DEFAULT NULL COMMENT '测试负责人ID，多个用，隔开',
  `dev_owner_ids` varchar(100) DEFAULT NULL COMMENT '开发负责人ID，多个用,隔开',
  `quality_score` decimal(4,1) DEFAULT NULL COMMENT '质量分',
  `git_address` varchar(200) DEFAULT NULL COMMENT 'git仓库地址',
  `sync_api_data_flag` bit(1) DEFAULT b'0' COMMENT '同步api数据',
  `deleted` bit(1) DEFAULT b'0' COMMENT '是否删除， 1:删除， 0:有效',
  PRIMARY KEY (`id`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for resource_config
-- ----------------------------
CREATE TABLE `resource_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `description` varchar(4096) DEFAULT NULL COMMENT '描述',
  `resource_type` enum('mysql','mongo','redis','mq') DEFAULT 'mysql' COMMENT '数据库类型',
  `settings` text COMMENT '资源配置数据',
  `deleted` bit(1) DEFAULT b'0' COMMENT '是否删除， 1:删除， 0:有效',
  PRIMARY KEY (`id`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for test_cases
-- ----------------------------
CREATE TABLE `test_cases` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator_uid` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `owner_uid` bigint(20) DEFAULT NULL COMMENT '责任人ID',
  `name` varchar(100) DEFAULT NULL COMMENT '接口名称',
  `description` varchar(4096) DEFAULT NULL COMMENT '描述',
  `env` enum('prod','test') DEFAULT 'test' COMMENT '环境',
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目ID',
  `module_id` bigint(20) DEFAULT NULL COMMENT '模块',
  `ci_flag` bit(1) DEFAULT b'0' COMMENT '上线状态，true:已上线，false:未上线',
  `base_params` text COMMENT '基础数据配置，json格式',
  `pre_actions` text COMMENT '前置动作',
  `data_preparation_config` text COMMENT '用例数据配置',
  `main_actions` text COMMENT '主流程动作',
  `post_actions` text COMMENT '后置动作',
  `last_run_state` tinyint(2) DEFAULT NULL COMMENT '上次运行状态',
  `quality_score` decimal(4,1) DEFAULT NULL COMMENT '质量分',
  `quality_score_origin` decimal(8,4) DEFAULT NULL COMMENT '原始质量分',
  `deleted` bit(1) DEFAULT b'0' COMMENT '是否删除， 1:删除， 0:有效',
  PRIMARY KEY (`id`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_name` (`name`),
  KEY `idx_project_id` (`project_id`),
  KEY `idx_module_id` (`module_id`),
  KEY `idx_ci_flag` (`ci_flag`),
  KEY `idx_owner_uid` (`owner_uid`),
  KEY `idx_last_run_state` (`last_run_state`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for test_report
-- ----------------------------
CREATE TABLE `test_report` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `executor_uid` bigint(20) DEFAULT '0' COMMENT '执行人UID',
  `report_uuid` varchar(50) DEFAULT NULL COMMENT '报告唯一UID',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `duration` bigint(20) DEFAULT NULL COMMENT '花费的时间(单位毫秒)',
  `total_count` bigint(20) DEFAULT NULL COMMENT '总数',
  `success_count` bigint(20) DEFAULT NULL COMMENT '成功数',
  `fail_count` bigint(20) DEFAULT NULL COMMENT '失败数',
  `trigger_mode` tinyint(2) DEFAULT NULL COMMENT '触发方式',
  `run_state` tinyint(2) DEFAULT NULL COMMENT '运行状态',
  `fail_reason_id` tinyint(5) DEFAULT NULL COMMENT '失败原因，见ReportFailReason',
  `fix_flag` bit(1) DEFAULT b'0' COMMENT '修复状态，0：未修复 1：已修复',
  `deleted` bit(1) DEFAULT b'0' COMMENT '是否删除，1:删除， 0:有效',
  PRIMARY KEY (`id`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_executor_uid` (`executor_uid`),
  KEY `idx_report_uuid` (`report_uuid`),
  KEY `idx_start_time` (`start_time`),
  KEY `idx_trigger_mode` (`trigger_mode`),
  KEY `idx_run_state` (`run_state`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for test_report_details
-- ----------------------------
CREATE TABLE `test_report_details` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `report_uuid` varchar(50) DEFAULT NULL COMMENT '报告唯一UID',
  `test_cases_id` bigint(20) DEFAULT NULL COMMENT '测试用例ID',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `duration` bigint(20) DEFAULT NULL COMMENT '花费的时间(单位毫秒)',
  `total_count` bigint(20) DEFAULT NULL COMMENT '总数',
  `success_count` bigint(20) DEFAULT NULL COMMENT '成功数',
  `fail_count` bigint(20) DEFAULT NULL COMMENT '失败数',
  `run_state` tinyint(2) DEFAULT NULL COMMENT '运行状态',
  `details` longtext CHARACTER SET utf16le COMMENT '详细日志',
  `exceptions` longtext,
  `deleted` bit(1) DEFAULT b'0' COMMENT '是否删除， 1:删除， 0:有效',
  PRIMARY KEY (`id`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_report_uuid` (`report_uuid`),
  KEY `idx_test_cases_id` (`test_cases_id`),
  KEY `idx_run_state` (`run_state`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for api
-- ----------------------------
CREATE TABLE `api` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `api_url` varchar(100) NOT NULL COMMENT '地址',
  `api_title` varchar(150) DEFAULT NULL COMMENT '标题',
  `project_id` bigint(20) NOT NULL COMMENT '项目ID',
  `target_flag` bit(1) DEFAULT b'0' COMMENT '目标标识，true：是 ; false：否',
  `finish_flag` bit(1) DEFAULT b'0' COMMENT '是否已完成，true：已完成 ; false：未完成',
  `check_key` varchar(100) DEFAULT NULL COMMENT '重复检查值，用于同步api数据',
  `deleted` bit(1) DEFAULT b'0' COMMENT '是否删除， 1:删除， 0:有效',
  PRIMARY KEY (`id`),
  KEY `idx_project_id` (`project_id`),
  KEY `idx_api_url` (`api_url`),
  KEY `idx_api_title` (`api_title`),
  KEY `idx_check_key` (`check_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for project_quality_report_history
-- ----------------------------
CREATE TABLE `project_quality_report_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `date_range` varchar(100) DEFAULT NULL COMMENT '日期范围',
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目ID',
  `test_owner_ids` varchar(100) DEFAULT NULL COMMENT '测试负责人ID，多个用，隔开',
  `dev_owner_ids` varchar(100) DEFAULT NULL COMMENT '开发负责人ID，多个用,隔开',
  `run_total_count` bigint(20) DEFAULT '0' COMMENT '自动执行次数',
  `run_success_count` bigint(20) DEFAULT '0' COMMENT '自动执行成功次数',
  `run_fail_info` text COMMENT '失败原因及修复情况',
  `api_target_count` bigint(20) DEFAULT '0' COMMENT '接口目标数',
  `api_finish_count` bigint(20) DEFAULT '0' COMMENT '接口完成数据',
  `quality_score` decimal(4,1) DEFAULT NULL COMMENT '项目质量分',
  `online_test_cases_count` bigint(20) DEFAULT '0' COMMENT '上线测试任务数',
  `online_test_cases_success_count` bigint(20) DEFAULT '0' COMMENT '上线测试任务成功数',
  `deleted` bit(1) DEFAULT b'0' COMMENT '是否删除， 1:删除， 0:有效',
  PRIMARY KEY (`id`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_date_range` (`date_range`),
  KEY `idx_project_id` (`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目质量报告(历史)';

