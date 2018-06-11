1 将脚本拷贝到/etc/init.d目录
2 vim文件，:set ff?查看当前脚本编码如果为dos就修改为unix,修改方法:set ff=unix,然后保存
3 update-rc.d 脚本名 default 启动时顺序编号 关闭时顺序编号，注意脚本的启动顺序,有的脚本启动要依赖上一个脚本，如kafka，依赖zookeeper
