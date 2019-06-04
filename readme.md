# 项目介绍

基于Netty框架,提供对MQTT、WebSocket等协议的服务端与客户端的封装 (易伸缩、易扩展)  
 
* MQTT服务器 (核心实现、可扩展消息存储、可集群、可处理消息)  
* MQTT客户端  (核心实现、可扩展消息存储)
* WebSocket服务器  (核心实现、可处理消息)
* WebSocket客户端  (核心实现)

# 项目结构
```
ext-opensource-netty
  ├── ext.opensource.redis  -- redis公用
  ├── netty-client          -- MQTT客户端 、WebSocket客户端等核心实现
  ├── netty-client-example  -- MQTT客户端 、Websocket客户端等扩展用例
  ├── netyy-common          -- 公共类及其它
  ├── netty-server          -- MQTT服务器 、Websocket服务器等核心实现
  ├── netty-server-example  -- MQTT服务器 、Websocket服务器扩展用例
```

# 开发规范
  * IDE: Eclipse + Lombok + JDK1.8 + Maven   
  * 应用技术: netty、 spring boot、  redis、 ignite、 kafka.
  * 代码质量: Spotbugs
  * 代码规范: 阿里代码插件
  
# 开发配置
  *  源码格式要求,导入standard目录下的代码格式文件至IDE中.  
     eclipse添加自定义用户名: 在eclipse.ini 中 -vmargs下面  
         添加一行  -Duser.name=您的名字
  * 代码简化插件: Lombok   
    * 1. 将maven本地包对应的lombok.jar 复制到 eclipse.ini 所在的文件夹目录下    
    * 2. 打开 eclipse.ini ，在最后面插入以下两行并保存.  
           -Xbootclasspath/a:lombok.jar   
           -javaagent:lombok.jar   
    * 3.重启 eclipse 

# Socket通信 
  * 数据粘包
  * 数据拆包
  * 数据验证
  * 数据安全
  * 数据序列化
  * 鉴权
  * 心跳
  * 重连
  
# Todo List
  * MQTT监控
  * MQTT管理
  * 订阅通配符支持
  * MQTT WebSocket支持


# 参考资料
  * Netty实战 (诺曼-毛瑞尔)
  * Netty权威指南
  * mqtt协议(英文): <http://docs.oasis-open.org/mqtt/mqtt/v3.1.1/os/mqtt-v3.1.1-os.html>
  * mqtt协议(中文): <https://mcxiaoke.gitbooks.io/mqtt-cn/content/>
  * mqtt压测: <https://github.com/emqx/mqtt-jmeter>
  * mqtt流程: <https://github.com/emqx/emqx-docs-cn/blob/master/source/mqtt.rst>
  * mqtt官网: <http://mqtt.org/>
  * paho开源: <https://www.eclipse.org/paho>
  * iot-mqtt-server: <https://gitee.com/recallcode/iot-mqtt-server>  
  * iot-push: <https://github.com/1ssqq1lxr/iot_push>   
  

link:  
   weixin: chinaxjc208
