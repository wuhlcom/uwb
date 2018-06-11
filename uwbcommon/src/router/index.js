import Vue from 'vue'
import Router from 'vue-router'

import login from '@/pages/login'
import Home from '@/pages/index'
import RealTime from '@/pages/real-time'
import playback from '@/pages/playback'
import attendance from '@/pages/attendance'
import alarm from '@/pages/alarm'
import configuration from '@/pages/configuration'
import PersonnelManagement from '@/pages/config-children/personnel-management'
import organizational from '@/pages/config-children/organizational'
import tactics from '@/pages/config-children/tactics'
import equipment from '@/pages/config-children/equipment'
import area from '@/pages/config-children/area'
import MapArrangement from '@/pages/config-children/map-arrangement'
import edition from '@/pages/config-children/edition'
import upgrade from '@/pages/config-children/upgrade'
import twice from '@/pages/config-children/twice'
import error from '@/pages/404'

Vue.use(Router)
export const router =  new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      name: 'login',
      component: login
    },
    {
      path: '/404',
      name: '404',
      component: error
    },
    {
      path: '/index',
      name: 'home',
      redirect: '/index/realtime',
      component: Home,
      meta: {
        requireAuth: true,
      },
      children: 
      [
        {
          // 实时轨迹
          path: '/index/realtime',
          name: 'realtime',
          component: RealTime
        },
        {
          // 轨迹回放
          path: '/index/playback',
          name: 'playback',
          component: playback
        },
        {
          // 电子考勤
          path: '/index/attendance',
          name: 'attendance',
          component: attendance
        },
        {
          // 告警管理
          path: '/index/alarm',
          name: 'alarm',
          component: alarm
        },
        {
          // 系统配置
          path: '/index/configuration',
          name: 'configuration',
          component: configuration,
          redirect: '/index/configuration/PersonnelManagement',
          children: [
            {
              // 人员管理
              path: '/index/configuration/PersonnelManagement',
              name: 'PersonnelManagement',
              component: PersonnelManagement
            },
            {
              path: '/index/configuration/organizational',
              name: 'organizational',
              component: organizational
            },
            {
              // 策略管理
              path: '/index/configuration/tactics',
              name: 'tactics',
              component: tactics
            },
            {
              // 设备管理
              path: '/index/configuration/equipment',
              name: 'equipment',
              component: equipment
            },
            {
              // 区域管理
              path: '/index/configuration/area',
              name: 'area',
              component: area
            },
            {
              // 地图管理
              path: '/index/configuration/map-arrangement',
              name: 'maparrangement',
              component: MapArrangement
            },
            {
              path: '/index/configuration/edition',
              name: 'edition',
              component: edition
            },
            {
              // 升级管理
              path: '/index/configuration/upgrade',
              name: 'upgrade',
              component: upgrade
            },
            {
              // 二次开发
              path: '/index/configuration/twice',
              name: 'twice',
              component: twice
            }
          ]
        }
      ]
    }
  ]
})

router.beforeEach((to, from, next) => {
  let menus = JSON.parse(sessionStorage.getItem('menus'))
  // 如果跳转到的页面是404或登录页，那么就直接进入
  if (to.name === 'login' || to.name === '404') {
    next()
  } else {
    let name
    // 如果跳转的页面是configuration的子路由页面，那么就算是configuration
    if (to.path.slice('/').indexOf('configuration') > -1) {
      name = 'configuration'
    } else {
      name = to.name
    }
    // 如果没有权限列表那就需要重新登录
    if (!menus) {
      next({ path: '/' })
      // 如果跳转页面在权限列表中则可以跳转，不然跳转到404页面
    } else if (menus.indexOf(name) > -1) {
      next()
    } else {
      next({ path: '/404' })
    }
  } 
})
