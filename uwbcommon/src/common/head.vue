<template>
  <div class="head">
    <alarm-window
      v-if="alertShow"
      @windowShow="close"
      :data='arr'
      class="alert">
    </alarm-window>
    <div class="head-left">
      <router-link :to="{name: 'home'}">PoleStar™位置服务平台</router-link>
    </div>
    <div class="head-centre">
      <div class="head-nav">
        <router-link
          v-for="(item, index) in permissionsList"
          :key="index"
          :to="{name: item.path}">
          {{item.name}}
        </router-link>
      </div>
    </div>
    <div class="head-right">
      <el-badge :value="mail" class="item">
        <i class="iconfont icon-youjian"></i>
      </el-badge>
      <el-badge :value="bell" class="item">
        <i class="iconfont icon-tongzhi"></i>
      </el-badge>
      <!-- <span class="time item">2018年1月25日 15:20:20</span> -->
      <div class="user">
        <i @click="show()" class="iconfont icon-quanyonghu"></i>
      </div>
      <transition name="account">
        <div
          v-if="accountShow"
          class="account">
          <div class="triangle-top"></div>
          <ul>
            <li class="alertinfo">
              <i class="iconfont icon-quanyonghu"></i>
              <router-link to="">{{username}}</router-link>
            </li>
            <li @click="logout" class="alertinfo">
              <i class="iconfont icon-tuichu"></i>
              <span>退出登录</span>
            </li>
          </ul>
        </div>
      </transition>
    </div>
    <v-audio :num="voice"></v-audio>
  </div>
</template>
<style lang="scss" scoped>
  @import '../assets/css/common';
  .head{
    position: relative;
    display: flex;
    width: 100%;
    height: 89px;
    .head-left{
      flex: 2;
      height: 307px;
      min-width: 334px;
      text-align: center;
      line-height: 89px;
      background: url('../assets/img/111_01.png');
      background-size: 100% 100%;
      background-repeat: no-repeat;
      a{
        font-size: $web-name-fz;
        color: #fff;
      }
    }
    .head-centre{
      flex: 5;
      display: flex;
      justify-content: left;
      align-items: center;
      height: 70px;
      padding-left: 73px;
      background: url('../assets/img/head-center.png');
      background-size: 100% 100%;
      .head-nav{
        min-width: 834px;
        a{
          display: inline-block;
          width: 147px;
          height: 49px;
          padding-left: 50px;
          margin-right: 16px;
          line-height: 35px;
          font-size: $head-nav-fz;
          color: $head-nav-crlor;
          background: url('../assets/img/link.png') no-repeat;
        }
        .router-link-active{
          line-height: 37px;
          color: #fff;
          background: url('../assets/img/linking.png') no-repeat;
        }
      }
    }
    .head-right{
      position: relative;
      display: flex;
      flex: 1;
      min-width: 148px;
      height: 69px;
      padding-top: 20px;
      padding-right: 36px;
      background: url('../assets/img/head-right.png') right;
      text-align: right;
      i{
        font-size: 24px;
        color: $head-nav-crlor;
        cursor: pointer;
      }
      .time{
        color: #fff;
      }
      .user{
        flex: 1;

        vertical-align: middle;
      }
    }
    .item{
      flex: 1;
    }
    @media screen and (max-width: 1500px) {
      .item{
        margin-right: 20px;
      }
      .head-centre{
         padding-left: 20px;
          .head-nav{
          a{
            margin-right: 6px;
          }
        }
      }
    }
    .alert{
      position: fixed;
      bottom: 6%;
      right: 0;
      z-index: 1000;
    }
  }
  .account{
      position: absolute;
      top: 60px;
      left: 70%;
      z-index: 1000;
      margin-left: -40px;
      width: 100px;
      text-align: center;
      border: 1px solid rgba(0, 102, 204, 0.5);
      font-size: 14px;
      box-shadow: 0 2px 6px rgba(0, 102, 204, 0.5);
      background: rgba(0, 102, 204, 0.5);
      .alertinfo{
        height: 34px;
        line-height: 34px;
        font-size: 12px;
        cursor: pointer;
        a,
        span{
          display: inline-block;
          width: 48px;
          font-size: 12px;
          color: #59b3ff;
        }
        i{
          font-size: 16px;
          color: #59b3ff;
        }
      }
      .alertinfo:hover{
        background: #edf4f5;
      }
      .triangle-top{
        position: absolute;
        top: -8px;
        right: 30px;
        width: 0;
        height: 0;
        border-left: 8px solid transparent;
        border-right: 8px solid transparent;
        border-bottom: 8px solid rgba(0, 102, 204, 0.5);
      }
    }
  .account-enter-active{
    transition: all .5s ease;
  }
  .account-leave-active{
    transition: all .5s ease;
  }
  .account-enter, .account-leave-to{
    transform: translateX(-20px);
    opacity: 0;
  }
</style>
<script>
import AlarmWindow from '@/components/alarm-window'
import VAudio from '@/components/v-audio'
import { WS_ALARM_URL } from "../../static/js/websocket-url";
import { formattingTime } from '../assets/js/formattingTime'
import { mapState } from 'vuex'
export default {
  name: 'v-head',
  components: {
    AlarmWindow,
    VAudio
  },
  data () {
    return {
      accountShow: false,
      alertShow: false,
      username: '',
      token: '',
      voice: 0,
      mail: '',
      dataArr: [],
      arr: [],
      obj1: {level: 'severity', msg: '进入非指定区域', personName: '黄然伟', timestamp:formattingTime(new Date().getTime()/1000)},
      obj2: {level: 'severity', msg: 'SOS求救报警', personName: '黄然伟', timestamp:formattingTime(new Date().getTime()/1000)},
      obj3: {level: 'severity', msg: '跌倒紧急报警', personName: '黄然伟', timestamp:formattingTime(new Date().getTime()/1000)},
      time: 0,
      pathList: [
        {path: 'realtime', name: '实时监控', isPermissions: false},
        {path: 'playback', name: '轨迹回放', isPermissions: false},
        {path: 'attendance', name: '自动考勤', isPermissions: false},
        {path: 'alarm', name: '告警管理', isPermissions: false},
        {path: 'configuration', name: '系统管理', isPermissions: false},
      ]
    }
  },
  computed: {
    bell () {
      return this.$store.state.bell.count
    },
    ...mapState({
      permissionsList: state => state.permission.permissionsList
    })
  },
  methods: {
    close (msg) {
      this.alertShow = msg
      this.dataArr = []
      this.arr = []
    },
    level (msg) {
      switch (msg) {
        case 2:
          return 'severity'
        case 1:
          return 'general'
        case 0:
          return 'hint' 
        default:
          break;
      }
    },
    getWebSocketURL() {
      if (window.location.port === '9999') {
        return 'ws://192.168.10.232:80/uwb/websocket/wsmsg';
      }
      return ((window.location.protocol === 'https:') ? 'wss://' : 'ws://') + window.location.host + '/uwb/websocket/wsmsg'
    },
    webSowcket() {
      let ws = new WebSocket(this.getWebSocketURL())
      ws.onopen = () => {
        ws.send('{"type":2}')
      };
      ws.onmessage = msg => {
        let data = JSON.parse(msg.data).result
        data.forEach(e => {
          e.personName = e.personName
          e.level = this.level(e.level)
          e.timestamp = formattingTime(e.timestamp)
        })
        this.dataArr = this.dataArr.concat(data)
        this.alertShow = true
        this.$store.commit('increment')
      }
    },
    show () {
      event.stopPropagation()
      if (this.accountShow == true) {
        this.accountShow = false
      } else{
        this.accountShow = true
      }
    },
    logout () {
      this.$confirm('您确定退出吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.Post(this.$url.logout,{token: this.token})
        .then(res => {})
        .catch(rej => {
          this.$router.push({path: '/'})
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消退出'
        })        
      })
    }
  },
  created () {
    this.webSowcket()
    document.addEventListener('click', (e) => {
      if (this.accountShow == true) {
        this.accountShow = false
      }
    })
    this.$http.Get(this.$url.bell)
    .then(res => {})
    .catch(rej => {
      this.$store.commit('init', rej.data.result.amount)
    })
    /*-------- 香港测试假数据 ----------*/
    // setTimeout(() => {
    //   this.arr.push(this.obj1)
    //   this.alertShow = true
    // }, 1000*42);
    // setTimeout(() => {
    //   this.arr.push(this.obj2)
    //   this.alertShow = true
    //   this.voice++
    // }, 1000*87);
    // setTimeout(() => {
    //   this.arr.push(this.obj3)
    //   this.alertShow = true
    //   this.voice++
    // }, 1000*103);
  },
  mounted () {
    this.username = sessionStorage.getItem('username')
    this.token = sessionStorage.getItem('token')
    let menus = JSON.parse(sessionStorage.getItem('menus'))
    menus.forEach(e => {
      this.pathList.forEach(el => {
        if (e === el.path) {
          el.isPermissions = true
        }
      })
    })
    this.$store.commit('newPermissionsList', this.pathList)
  },
  watch: {
    // 告警弹窗最多显示最近10条信息
    dataArr (val) {
      if (this.dataArr.length > 10) {
        this.arr = this.dataArr.slice(-10)
      } else {
        this.arr = this.dataArr
      }
    },
  }
}
</script>
