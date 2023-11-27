# work3
## 技术栈
使用了 mybatis + maven + mysql 进行了订单系统的构建
## 项目结构介绍
### mysql层面
1. 编辑了俩张基础表，分别是goods与order表，对应着货物与订单
2. 编辑了俩张额外表，分别是ordergoods（用于处理多对多问题）与dropgoods（用于处理含有废弃货物订单问题）
### java层面
1.在pojo层中建立了四个对象类
- Order
- Goods
- OrderGoods
- DropGood
2.在dao层中建立了四个对象类对应的mapper接口
3.utils
- 建立了myException包用于放置自定义异常类
- 建立了各个对象对应的工具类，将CRUD等操作进一步简化和优化（应该是由别的层完成，但是我不太清楚具体是哪一个，所以放到了工具类）
- 建立了MybatisUtils用于创建Sqlsession
4.resources
- 放置了各个mapper接口对应的xml文件
- 放置了mybatis-config.xml配置文件
- 放置了db.properties文件，其中包含了连接数据库所需的数据，用于mybatis连接数据库
5.test
放置了俩个测试类，一个是GoodsTest,一个是OrderTest，用于进行各种操作的测试
## 项目功能
1.俩个表常规的CRUD操作
2.实现了分页查询，排序的功能
3.解决了含有废弃货物订单无法查出的问题
## 项目存有问题
1.表的数量众多，感觉三大范式未必的完美满足，但是已经是我能想到解决各种问题的最佳手段了
2.对于dao层中的增删改查操作的进一步优化，感觉不应该仍在Utils类中，但是目前学习的知识也不知道这一步操作应该在哪完成，所以姑且扔在Utils类中
## 项目如何启动
1.已经将环境与测试数据都放置在了test中，同时也进行了异常的捕捉，方便直观观赏
