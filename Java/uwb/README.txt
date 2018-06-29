定位平台后台服务变更记录
1 修改了redis的序列化方式使用 jackson2json序列化,将reids相关的操作抽象到接口中
2 微服务合并，将10个服务部分合并，合并后变成了7个服务
   zl_alarm_save zl_coordinate合并到zl_storage
   zl_permission合并到zl_resources
   zl_alarm,zl_coordinate合并到zl_websocket
3 加入了mia平台的部署时的kakfa和zookeeper相关的配置，jar，依赖包
4 服务端口列表 
11000
11001
11002
11003
11004
11005
11006