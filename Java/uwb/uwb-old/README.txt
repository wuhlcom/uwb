定位平台后台服务变更记录
1 修改了redis的序列化方式使用 jackson2json序列化,将reids相关的操作抽象到接口中
2 微服务合并，将10个服务部分合并，合并后变成了7个服务
   zl_alarm_save zl_coordinate合并到zl_storage
   zl_permission合并到zl_resources
   zl_alarm,zl_coordinate合并到zl_websocket
3 加入了mia平台的部署时的kakfa和zookeeper相关的配置jar，依赖包

4 服务端口列表 

fastdfs 11001
resources 11003

producer 11002 9922--要对外网暴露此端口
websocket 11006

center 11000
status_save 11004
storage 11005

eureka 5555,5556
gateway 11007

其中fastdfs producer要根据环境修改配置文件中的IP地址

5 创建后台运行屏幕
screen -S uwb_test 创建screen 
screen -R uwb_test 重新登录screen
ctrl+a+d 关掉screen

6 环境相关依赖
  nginx:1.12.2
  fastdfs:trackerd storaged
  kafka:
  zookeeper
  java:jdk或jre 1.8
  mysql
  redis
  



