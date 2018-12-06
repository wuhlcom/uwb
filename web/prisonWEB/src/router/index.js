import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/pages/Login/login'
import Cell from '@/pages/Cell/cell'
import Panorama from '@/pages/Panorama/panorama'
import Floors from '@/pages/Floors/floors'
import PrisonerInfo from '@/pages/PrisonerInfo/prisonerinfo'
import AbsenceList from '@/pages/AbsenceList/absencelist'
import Warning from '@/pages/Warning/warning'
import WarningList from '@/pages/WarningList/warninglist'
Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      redirect: '/login'
    },
    {
      path: '/login',
      name: 'login',
      component: Login,
    },
    {
      path: '/floors',
      name: 'Floors',
      component: Floors,
      children: [
        {
          path: '/floors/cell',
          name: 'cell',
          component: Cell,
        },
        {
          path: '/floors/absencelist',
          name: 'absencelist',
          component: AbsenceList,
        },
        { 
          path: '/floors/warning',
          name: 'warning',
          component: Warning,
        }
      ]
    },
    {
      path: '/panorama',
      name: 'panorama',
      component: Panorama,
    },
    {
      path: '/prisonerinfo',
      name: 'prisonerinfo',
      component: PrisonerInfo,
    },
    {
      path: '/warninglist',
      name: 'warninglist',
      component: WarningList
    }
  ]
})
