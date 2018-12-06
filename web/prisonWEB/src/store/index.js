import Vue from 'vue'
import Vuex from 'vuex'
import { accMul, accSub } from '@/assets/js/parseFloat'
Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    // 报警数量
    warningAmount: 0,
    // 监仓人数统计
    personnelInof: {},
    // 监仓号
    cellCode: '',
    // 监仓编号
    areaCode: '',
    // 监仓页饼状图数据
    warningPie: {},
    // 缺勤人员统计
    abStatistics: {},
    // 监仓人员信息
    prisoners: [],
    // 监仓canvas比例
    width: 0,
    height: 0,
    // 监仓canvas大小
    sizeHig: 0,
    sizeWih: 0,
    // 其他监仓串仓人员
    cross: []
  },
  getters: {
    prisonersPosition: state => {
      let arr = []
      let arr1 = state.prisoners.concat(state.cross)
      arr1.forEach(e => {
        // if (e.flag) {
        //   console.log('mmp')
        // } else {
        //   console.log('ppx')
        // }
        if (e.flag < 5) {
          let obj = {}
          obj.name = e.name
          obj.nameshow = true
          obj.status = e.flag
          obj.x = Number(accSub(accMul(e.posY, state.width), state.sizeWih))
          obj.y = accMul(e.posX, state.height)
          arr.push(obj)
        }
      })
      return arr
    }
  }
})
