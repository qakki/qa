# 项目记录  
  
## 使用到的技术  
springboot + velocity + redis + rabbitmq + mybatis + mysql + tomcat + nginx + git + solr8 + ik-analyzer8 + qiniuCloud  + pyspider
## 实现的功能  
1. 用户登陆，注册功能  
* 用户名合法性及存在性检验  
* 用户密码强度检验  
* 密码salt+md5加密  
* 未登录权限控制，通过interceptor拦截器实现  
* 各种登录权限控制，msg私密拦截  
* 已登陆用户加cookie，数据库备份token  
* 退出用户token失效  
2. 问答功能  
* 添加问题入口    
* 图片转存，采用七牛云存储  
3. 评论和站内信   
* 分页，使用了PageHealper，前端未增加  
* 发送站内信，接口实现  
4. 点赞点踩  
* 使用JedisPool管理Jedis对象  
* 使用redis-list数据结构来存储点赞信息  
* 点赞后，异步发送通知告知新闻添加人  
5. 邮件发送  
* 邮件工具类，前端未配置   
6. 前端模板引擎   
* springboot2.0以上版本默认采用thymeleaf，thymeleaf极其难用，freemaker更好使  
*  springboot采用的1.4.7 兼容velocity  
7. 持久层配置  
* Mysql部署在腾讯云虚拟机上  
* 持久层采用mybatis，mapper采用class  
* mybatis-generate plugins初始化model类和dao  
* 数据库和表建立适用Navicat可视化创建  
8. 云服务器部署  
* 部署在腾讯云服务器Tomcat+Nginx转发  
* ssl证书  https加密  
9. 敏感词过滤  
* 数据结构采用字典树trietree，即每个节点表示一个char  
* 双指针敏感词过滤  
10. 关注功能  
* follow和followee两个redis sorted set结构 score时间 obj是userid   
* 关注和取消关注功能    
11. crawl爬虫  
* pyspider爬虫框架   
* py+mysql实现入库  
12. solr检索     
* ik-analyzer中文分词     
* solr+mysql数据导入，索引建立    
* solrj springboot+solr实现检索  
12. 异步消息队列     
* rabbitmq中间件做消息队列  
* 实现一些事件的异步处理    
