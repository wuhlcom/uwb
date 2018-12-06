import { accDiv, accAdd, accSub } from '../../assets/js/parseFloat'
const mapProportion = {
  state: {
    width: 0,
    height: 0,
  },
  mutations: {
    setWidth(state, payload) {
      state.width = accDiv(payload.clientWidth, payload.actualWidth)
    },
    setHeight(state, payload) {
      state.height = accDiv(payload.clientHeight, payload.actualHeight)
    }
  }
}
export default mapProportion