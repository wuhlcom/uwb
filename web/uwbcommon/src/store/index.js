import Vue from 'vue'
import Vuex from 'vuex'
import configurationNav from './module/configuration-nav'
import playback from './module/playback'
import permission from './module/permission'
import allPerson from './module/allPerson'
import mapProportion from './module/mapProportion'
import canvasSize from './module/canvasSize'

Vue.use(Vuex)

// 报警数模块
const bell = {
  state: { count: 0 },
  mutations: {
    increment (state) {
      state.count++
    },
    reduce (state) {
      state.count--
    },
    init (state, num) {
      state.count = num
    }
  },
}

export default new Vuex.Store({
  modules: {
    nav: configurationNav,
    playback: playback,
    bell: bell,
    permission: permission,
    allPerson: allPerson,
    mapProportion: mapProportion,
    canvasSize: canvasSize
  },
  state: {
    navShow: true,
  }
})
