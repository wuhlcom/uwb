import Vue from 'vue'
import Router from 'vue-router'
import GateWay from '@/components/GateWay'
import Login from '@/components/Login'

Vue.use(Router)
const router = new Router({
  // mode: 'history',
  routes: [
    {
      path: '/',
      name: 'login',
      component: Login
    },
    {
      path: '/gateway',
      name: 'gateway',
      component: GateWay
    },
  ]
})

// 全局路由守卫
// to: Route: 即将要进入的目标 路由对象
// from: Route: 当前导航正要离开的路由
// next: Function: 一定要调用该方法来 resolve 这个钩子。执行效果依赖 next 方法的调用参数。
router.beforeEach((to, from, next) => {
  const nextRoute = ['gateway'];
  let isLogin = sessionStorage.getItem('isLogin')
  // let isLogin = sessionStorage;  // 是否登录
  // 未登录状态；当路由到nextRoute指定页时，跳转至login
  if (nextRoute.indexOf(to.name) >= 0) {
    if (!isLogin) {
      router.push({ name: 'login' })
    }
  }
  // 已登录状态；当路由到login时，跳转至home 
  if (to.name === 'login') {
    if (isLogin) {
      router.push({ name: 'gateway' });
    }
  }
  next();
});

export default router;
