# Netty 聊天室-ssChat

## 功能

项目实现了：

- 聊天数据本地化存储
- 双人在线文本、表情、图片消息发送
- 心跳检测机制
- 断线重连机制
- 聊天对象登陆状态获取
- 自定义通信协议
- 数据库线程池异步数据持久化
  
## 技术模块

- 项目客户端使用java Swing构建页面,sqlite进行数据持久化
- 服务端使用mysql,redis进行数据持久化
- 使用slf4j+logback记录日志
- 使用mybatis简化操作
- 使用gjson解析json数据
- 使用netty处理并发异步请求
  
## 项目地址

- 客户端：<https://github.com/869621380/ssChat-Netty-Client.git>
- 服务端：<https://github.com/869621380/ssChat-Netty-Server.git>
