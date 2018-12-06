<template>
  <div class="warningalert">
    <div class="box-h">
      <span class="title">报警提示</span>
      <i @click="close()" class="iconfont icon-X"></i>
    </div>
    <div class="box-c">
      <i class="iconfont icon-icon"></i>
      <router-link to="/warninglist">
        <span @click="winReload">{{info.areaName+info.name+info.msg+'告警'}}</span>
      </router-link>
    </div>
  </div>
</template>
<style lang="scss" scoped>
  .warningalert{
    position: relative;
    display: flex;
    align-items: center;
    width: 310px;
    height: 197px;
    background: #fff;
    .box-h{
      position: absolute;
      top: 0;
      padding: 20px;
      width: 100%;
      height: 15px;
      line-height: 15px;
      font-size: 16px;
      i{
        position: absolute;
        right: 20px;
        color: #8a909c;
        cursor:pointer;
        font-size: 13px;
      }
    }
    .box-c{
      padding: 20px;
      width: 100%;
      height: 15px;
      line-height: 15px;
      font-size: 13px;
      text-align: center;
      span{
        display: inline-block;
        padding: 5px;
        cursor:pointer;
      }
      i{
        font-size: 24px;
        vertical-align: middle;
        color: #eb9305;
      }
    }
    .box-c span:hover{
      border-bottom: 1px solid #999;
    }
  }
</style>
<script>
import { WEB_SOCKET_PAI } from '../../api/api'
export default {
  name: 'warningalert',
  data () {
    return {
      checked: 0,
      info: {}
    }
  },
  methods: {
    winReload () {
      if (this.$route.path == '/warninglist') {
        window.location.reload()
      }
    },
    // 关闭弹出层
    close () {
      this.$emit('warning', false)
    },
    warningAlertShow () {
      this.$emit('warning', true)
    },
    websocket () {
      let ws = new WebSocket(WEB_SOCKET_PAI)
      ws.onopen = () => {
        ws.send('{"type":3,"dataType":4}')
      }
      ws.onmessage = evt => {
        let data = JSON.parse(evt.data).result
        this.info = data
        this.checked = 1
        this.$store.state.warningAmount++
        this.warningAlertShow()
      }
      ws.onclose = () => {
      }
      this.over = () => {
        ws.close()
      }
    },
  },
  mounted () {
    this.websocket()
  },
  beforeDestroy () {
    this.over()
  },
  watch: {
    checked (msg) {
      if(msg == 1) {
        setTimeout(() => {
          this.close()
          this.checked = 0
        }, 5000);
      }
    }
  }
}
</script>
