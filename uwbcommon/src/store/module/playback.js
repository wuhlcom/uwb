import { accDiv, accMul, accSub, accAdd} from '../../assets/js/parseFloat'

const state = {
  nameShow: false,
  trackShow: false,
  startTime: 0,
  endTime: 0,
  play: false,
  stop: true,
  speed: 1,
  emty: true,
  playTimeLength: -1,
  playTime: 0,
  timeLineLength: 0,
  isEmpty: false,
}
const getters = {
  // 时间格式化
  formattingTime(state) {
    function buling(val) {
      if (val < 10) {
        val = '0' + val
      }
      return val
    }
    let nowTime = ''
    if (state.playTime !== 0) {
      let time = new Date(state.playTime * 1000)
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
}
const mutations = {
  // 播放时间每秒加一，进度条长度加等于进度条总长度除以播放总时长
  addPlayTimeLength (state) {
    if (state.playTime <= state.endTime) {
      state.playTimeLength++
      state.timeLineLength += accDiv(586, accSub(state.startTime, state.endTime))
    } else {
      state.playTimeLength = -1
      state.timeLineLength = 0
    }
  },
  reset (state) {
    state.playTimeLength = -1
    state.timeLineLength = 0
  },
  /* 
   * 进度条等于拖拽的长度
   * 播放时间等于拖拽长度乘以播放总时长除以进度条总长度
   * 播放的毫秒时间等于开始时间加播放时间
  */ 
  setPlayTimeLength (state, num) {
    state.timeLineLength = num
    state.playTimeLength = accMul(num, accDiv(accSub(state.startTime, state.endTime), 586))
    state.playTime = accAdd(state.startTime, state.playTimeLength)
    state.isEmpty = true
  },
  // 播放的毫秒时间等于开始时间加播放时间
  setPlayTime (state) {
    state.playTime = accAdd(state.startTime, state.playTimeLength)
  },
  // 设置开始时间
  setStartTime (state, num) {
    state.startTime = num
  },
  // 设置结束时间
  setEndTime (state, num) {
    state.endTime = num
  },
  setPlay (state, boolean) {
    state.play = boolean
  },
  setEmpty (state) {
    state.isEmpty = false
  }
}
export default {
  state,
  getters,
  mutations,
}