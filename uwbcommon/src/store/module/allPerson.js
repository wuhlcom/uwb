const allPerson = {
  state: {
    allPersonArr: [],
  },
  getters: {
    newAllpersonArr (state) {
      let arr = [...state.allPersonArr]
      arr.forEach(el => {
        el.nameShow = false
        el.point = []
        el.namePosition = {}
        el.show = true
      })
      return arr
    }  
  },
  mutations: {
    setAllPersonArr (state, arr) {
      state.allPersonArr = arr
    }
  }
}
export default allPerson
