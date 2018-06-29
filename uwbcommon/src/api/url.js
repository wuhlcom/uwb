export const URL = {
  personName: '/uwb/resources/person/simple', // 人员姓名
  organization: '/uwb/resources/department/tree', // 人员结构
  addOrg: '/uwb/resources/department/add', // 添加部门
  delOrg: '/uwb/resources/department/delete', // 删除部门
  modifyOrg: '/uwb/resources/department/update', // 修改部门
  topPersons: '/uwb/resources/person/topPersons', // 顶级部门人员信息
  addPerson: '/uwb/resources/person/add', // 添加人员
  modifyPerson: '/uwb/resources/person/update', // 修改人员信息
  delPerson: '/uwb/resources/person/delete', // 删除人员
  level: '/uwb/resources/level/info', // 职位和级别
  fences: '/uwb/resources/fence/fences', // 获取所有围栏列表
  fenceAdd: '/uwb/resources/fence/add', // 新增围栏
  fenceDel: '/uwb/resources/fence/delete', // 删除围栏
  modifyFence: '/uwb/resources/fence/update', // 修改围栏信息
  strategyList: '/uwb/resources/strategy/strategies', // 策略列表
  strategyAdd: '/uwb/resources/strategy/add', // 添加策略
  strategyDel: '/uwb/resources/strategy/delete', // 删除策略
  strategyModify: '/uwb/resources/strategy/update', // 修改策略
  strategySwitch: '/uwb/resources/strategy/switch', // 策略开关
  allImg: '/uwb/fastdfs/fdfs/files',  // 图片列表
  changeImg: '/uwb/fastdfs/fdfs/update', // 修改图片信息
  fdfsSwitch: '/uwb/fastdfs/fdfs/switch', // 地图启用开关
  personnelList: '/uwb/resources/person/persons', // 人员信息列表
  delMap: '/uwb/fastdfs/fdfs/delete', // 删除地图文件
  upImg: '/uwb/fastdfs/fdfs/upload', // 图片上传
  departments: '/uwb/resources/department/departments', // 部门信息
  warnings: '/uwb/resources/warning/warnings', // 告警管理
  statistics: '/uwb/resources/warning/statistics', // 告警分类统计
  coordinates: '/uwb/resources/coordinate/coordinates', // 轨迹查询
  warningUpdate: '/uwb/resources/warning/update', // 告警处理
  version: '/uwb/producer/upgrade/version', // 版本查询
  upgrade: '/uwb/producer/upgrade/update', // 版本升级
  login: '/uwb/resources/user/login', // 登录
  logout: '/uwb/resources/user/logout', // 退出
  bell: '/uwb/resources/warning/amount', // 未处理告警数
  groupFences: '/uwb/resources/fence/groupFences'
}
export const WEB_SOCKET_URL = 'ws://192.168.10.196:10003/coordinate/coors'
export const WS_ALARM_URL = 'ws://192.168.10.196:10004/alarm/alarms'
