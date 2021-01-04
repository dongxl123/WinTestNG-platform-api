# WinTestNG-platform-api
> 基于WinTestNG开发的自动化测试平台接口项目，参考项目:WinTestNG

## 特色
- 适用于HTTP接口测试
- 测试数据维护在DB
- 支持Redis、Mongo、Mysql等操作
- 支持测试模板设置，可抽象通用功能
- 全局变量设置
- 支持断言
- 使用FreeMarker作为模板引擎
- 生成测试报告数据存储在DB
- 涉及测试概念:测试用例、数据驱动、断言、测试报告、接口测试、自动化测试、测试模板
- 整合WinSecurity权限管理项目
- 整合单点登录

## 使用方法
- 执行sql语句*src/main/resources/sql/WinTestNGPlatform.sql*
- 修改*application.yml*
- 执行*com.winbaoxian.testng.platform.Application*的**main**方法, 启动项目


## 文档
- 产品设计文档: *prd/start.html*
- 后端接口文档
    - 安装apidoc: `npm i apidoc`
    - 执行gradle命令:`gradle apidocs`
    - 查看文档: *buid/apidocs/index.html*