const canvasSize = {
  state: {
    width: 0,
    height: 0
  },
  mutations: {
    setCanvasWith (state, width) {
      state.width = width
    },
    setCanvasHeight (state, height) {
      state.height = height
    }
  }
}
export default canvasSize
