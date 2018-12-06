// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import { router } from './router'
import store from './store/index'
import axios from './api/axios'
import ElementUi from 'element-ui'
import {URL} from '../src/api/url'
import 'element-ui/lib/theme-chalk/index.css'
import './assets/font/iconfont.css'
import './components/orgChart/org.css'
import './components/tree/tree.css'

Vue.use(ElementUi)
Vue.config.productionTip = false
Vue.prototype.router = router
Vue.prototype.$http = axios
Vue.prototype.$url = URL
/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  template: '<App/>',
  components: { App }
})
