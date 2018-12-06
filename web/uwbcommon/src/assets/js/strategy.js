function level (val) {
  switch (val) {
    case 0:
      return '提示'
    case 1:
      return '普通'
    case 2:
      return '严重'
    default:
      break
  }
}
function behavior (val) {
  switch (val) {
    case 0:
      return '禁止进入'
    case 1:
      return '禁止离开'
    default:
      break
  }
}
export {level, behavior}
