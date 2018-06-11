<template>
  <div class="floors">
    <alert v-if="alertShow" @hide="tohide"></alert>
    <crewalert v-if="crewShow" @close="crew"></crewalert>
    <warning-alert v-show="warningShow" @warning="warning" class="warning"></warning-alert>
    <v-header 
    :actShow="true" 
    :praShow="true" 
    :bellShow="true"
    @out="tohide"
    class="header"
    ></v-header>
    <div class="title">
      <div>
        一监区 >
        <router-link to="/panorama">
        首页
        </router-link>
        > 监仓{{cellNum}}
      </div>
      <v-time class="floors-time"></v-time>
    </div>
    <div class="main">
      <div class="flex-one">
        <div class="box-title vertical">人数统计</div>
        <div @click="callTheRoll" class="roll-call">电子点名</div>
        <statistics 
        @close="crew" 
        :code="this.$route.query.num" 
        :cellNum="cellNum"
        class="statistics"
        ></statistics>
      </div>
      <router-link :to="{name: 'absencelist', query:{num: this.$route.query.num, id: cellNum}}">
        <div class="flex-one">
          <div class="box-title vertical">缺勤人员统计</div>
          <absenteeism class="abse"></absenteeism>
        </div>
      </router-link>
      <router-link class="flex-one" :to="{name: 'warning', query: {num: this.$route.query.num, id: cellNum}}">
        <div class="flex-one">
          <div class="box-title vertical">今日报警统计</div>
          <pie
          :code="this.$route.query.num" 
          :cellNum="cellNum"
          class="pie-box"></pie>  
        </div>
      </router-link>                 
    </div>
   <router-view class="view"></router-view> 
  </div>
</template>
<style lang="scss" scoped>
  .floors{
    position: relative;
    background: #f1f3f6;
    height: 100%;
    overflow: auto;
    a{
      color: #2d2f33;
    }
    .header{
      display: flex;
      align-items: center;
      height: 6.3%;
      min-height: 40px;
    } 
    .title{
      display: flex;
      align-items: center;
      position: relative;
      padding-left: 20px; 
      width: 100%;
      height: 4.6%;
      font-size: 14px;
      color: #2d2f33;
      .floors-time{
        position: absolute;
        right: 20px;
        font-size: 14px;
        color: #409eff;
      }
    }
    .main{
      display: flex;
      width: 100%; 
      height: 32.6%;
      padding: 0 11px;
      .flex-one{
        display: flex;
        align-items: center;
        justify-content: center;
        position: relative;
        flex: 1;
        margin: 0 10px;
        // padding: 20px;
        height: 100%;
        background: #fff;
        .box-title{
          position: absolute;
          top: 20px;
          left: 20px;
          font-size: 14px;
        }
        .roll-call{
          position: absolute;
          right: 20px;
          top: 9px;
          width: 90px;
          height: 34px;
          line-height: 34px;
          text-align: center;
          background: #409eff;
          font-size: 14px;
          color: #fff;
          cursor:pointer;
        }
        .roll-call:hover{
          background: #1D8CE0;
        }
        .statistics{
          width: 90%;
          height: 55%;
        }
        .abse{
          display: flex;
          align-items: center;
          justify-content: center;
          width: 90%;
          height: 80%;
        }
        .pie-box{
           margin-top: 30px;
          width: 90%;
          height: 80%;
        }
      }
    }
    .warning{
      position: absolute;
      right: 0;
      bottom: 0;
      z-index: 100;
      box-shadow: -3px -3px 10px 1px #888;
    }
    .view{
      height: 52.2%;
    }  
  }
</style>
<script>
import VHeader from 'com/header/header'
import Alert from 'com/alert/alert'
import VTime from 'com/time/time'
import Statistics from 'com/statistics/statistics'
import Absenteeism from 'com/absenteeism/absenteeism';
import Pie from 'com/pie/pie'
import Crewalert from 'com/crewalert/crewalert'
import WarningAlert from 'com/warningalert/warningalert'
import { PRISON_API, WEB_SOCKET_PAI } from '../../api/api'

export default {
  name: 'floors',  
  components: {
    VHeader,
    VTime,
    Statistics,
    Absenteeism,
    Alert,
    Pie,
    Crewalert,
    WarningAlert,
  },
  data () {
    return {
      alertShow: false,
      crewShow: false,
      warningShow: false,
      cellNum: this.$route.query.id,
    }
  },
  computed: {

  },
  methods: {
    // 退出窗口弹出
    tohide (msg) {
      this.alertShow = msg
    },
    // 电子点名窗口弹出或关闭
    crew (msg) {
      this.crewShow = msg
    },
    // 报警弹窗
    warning (msg) {
      this.warningShow = msg
    },
    callTheRoll () {
      this.crewShow = true
    },
    websocket () {
      let ws = new WebSocket(WEB_SOCKET_PAI)
      ws.onopen = () => {
        ws.send('{"type":2,"dataType":0,"areaCode":"'+ this.$route.query.num+'"}')
      }
      ws.onmessage = evt => {
        let data = JSON.parse(evt.data).result
        // console.log(data.prisoners[6].flag, 'flag')
        // console.log(data.prisoners[6].msg, 'msg')
        this.$store.state.warningPie = data.warnings
        this.$store.state.personnelInof = data.checkingIn
        this.$store.state.abStatistics = data.abStatistics
        this.$store.state.prisoners = data.prisoners
        this.$store.state.cross = data.cross
      }
      ws.onclose = () => {
        console.log('duankai')
      }
      // 组件销毁时调用，中断websocket链接
      this.over = () => {
        ws.close()
        this.$store.state.warningPie = {}
        this.$store.state.personnelInof = {}
        this.$store.state.abStatistics = {}
        this.$store.state.prisoners = []
        this.$store.state.cross = []
      }
    }
  },
  created () {
    this.$http.Post(PRISON_API.cellinfo, {areaCode: this.$route.query.num}).then(res => {

    }).catch(rej => {
      let data = rej.data.result
      this.$store.state.warningPie = data.warnings
      this.$store.state.personnelInof = data.checkingIn
      this.$store.state.abStatistics = data.abStatistics
      this.websocket()
    })
  },
  mounted () {
  },
  beforeDestroy () {
    this.over()
  }
}
</script>
