// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUi from 'element-ui'
import store from './store/index'

Vue.config.productionTip = false
Vue.prototype.router = router

Vue.use(ElementUi)
// 引入CSS样式
import './assets/css/index.css'
import './assets/font/iconfont.css' 

// 添加axios为Vue原型属性
import axios from './axios/index'
Vue.prototype.$http = axios
/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  template: '<App/>',
  components: { App }
})
import 'element-ui/lib/theme-chalk/index.css'
