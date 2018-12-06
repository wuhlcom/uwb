const PRISON_API = {
  // 登录
  login: '/permission/user/login',
  // 退出
  quit: '/permission/user/logout',
  // 全景图
  panorama: '/prison/area/areas/query',
  // 监仓信息
  cellinfo: '/prison/area/query',
  // 缺勤列表
  absence: '/prison/area/absence',
  // 报警列表
  alarm: '/prison/area/warning',
  // 电子点名
  callname: '/prison/area/summary',
  // 个人信息
  prisonerinfo: '/prison/prisoner/info',
  // 个人报警列表
  prisonerwarnings: '/prison/prisoner/warnings',
  // 分监区监仓信息
  areainfo: '/prison/area/areas/query',
  // 分监区报警列表
  areawarning: '/prison/warning/areas',
  // 报警处理
  warningdispose: 'prison/warning/manage',
  // 监仓囚犯列表
  prisoners: '/prison/area/prisoners'
}
const WEB_SOCKET_PAI = 'ws://192.168.10.9:8802/websocket/prison'
export { PRISON_API, WEB_SOCKET_PAI } 
