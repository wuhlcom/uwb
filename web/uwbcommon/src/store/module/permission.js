// import { routerMap } from '@/router/index'

const isPermissions = value => {
  return value.isPermissions
}

// function hasPermission(menus, route) {
//   if (route.menu) {
//     /*
//     * 如果这个路由有menu属性,就需要判断用户是否拥有此menu权限
//     */
//     return menus.indexOf(route.menu) > -1;
//   } else {
//     return true
//   }
// }

// function filterRouter(ruterMap, menus) {
//   const accessedRouters = ruterMap.filter(route => {
//     //filter,js语法里数组的过滤筛选方法
//     if (hasPermission(menus, route)) {
//       if (route.children && route.children.length) {
//         //如果这个路由下面还有下一级的话,就递归调用
//         route.children = filterRouter(route.children, menus)
//         //如果过滤一圈后,没有子元素了,这个父级菜单就也不显示了
//         return (route.children && route.children.length)
//       }
//       return true
//     }
//     return false
//   })
//   return accessedRouters
// }

const permission = {
  state: {
    permissionsList: [],
    routers: []
  },
  getters: {
    // addRouters: state => state.routers
  },
  mutations: {
    newPermissionsList (state, arr) {
      let spread = [...arr]
      state.permissionsList = spread.filter(isPermissions)
    },
  }
}
export default permission
