// 时间格式化
function formattingTime (t) {
  let nowTime = ''
  if (t !== 0) {
    let time = new Date(t * 1000)
    let y = time.getFullYear()
    let m = buling(time.getMonth() + 1)
    let d = buling(time.getDate())
    let h = buling(time.getHours())
    let mi = buling(time.getMinutes())
    let s = buling(time.getSeconds())
    nowTime = y + '-' + m + '-' + d + ' ' + h + ':' + mi + ':' + s
  } else {
    nowTime = ''
  }
  return nowTime
}
// 补零
function buling (val) {
  if (val < 10) {
    val = '0' + val
  }
  return val
}
export { formattingTime, buling }
