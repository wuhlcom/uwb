//docker fastdfs服务地址
//由于zuul网关无法处理websocket请求，所以要直接指定websocket spring微服务地址
//spring cloud gateway可以处理websocket请求，后续网关更新后可直接使用网关地址
//export const WEBSOCKET_URL = 'ws://123.207.39.248:11006/uwb/websocket/wsmsg'

//export const WEBSOCKET_URL = ''
export const WEBSOCKET_URL = 'ws://192.168.10.232/uwb/websocket/wsmsg'

