#Kafka Message center
> 消息处理中心,保证最终展现给用户的消息同源
  即websocket 消息入库，消息查询等要一致
* Kafka Consumer 订阅消息
* 将订阅的消息进行处理
* 将处理后的消息再发送给Kafka Server
