<template>
  <div class="header">
    <div @click="goPanorama()" class="logo">
      <div class="img">
         <img src="./logo.png">
      </div>
      <h4>深圳智慧监狱管理系统</h4>
    </div>
    <div class="head-info">
      <router-link to="/warninglist">
        <div @click="winReload" v-if="bellShow" class="bell">
          <i class="iconfont icon-lingdang"></i>
          <div class="point">{{warningAmount}}</div>
        </div>
      </router-link>
      <div @click="goPanorama()" v-if="praShow" class="gopra">
        <div>全景图</div>
      </div> 
      <div v-if="actShow" @click="out()" class="account">
        <div>
          <i class="quit">
            <img src="./quit.png">
          </i>
          <span class="number">{{zhanghao}}</span>
        </div>
      </div>
    </div>
  </div>
</template>
<style lang="scss" scoped>
  .header{
    position: relative;
    display: flex;
    justify-content: space-between;
    padding-left: 20px;
    background: #409eff;
    color: #fff;
    a{
      color: #fff;
    }
    .logo{
      display: flex;
      align-items: center;
      cursor: pointer;
      .img{
        margin: 3px 0 0 0;
      }
      h4{
        font-size: 24px;
      }
    }
    .head-info{
      display: flex;
      align-items: center;
      height: 100%;
      .gopra{
        display: flex;
        align-items: center;
        justify-content: center;
        right: 125px;
        height: 100%;
        width: 88px;
        font-size: 14px;
        cursor:pointer;
      }
      .gopra:hover{
        background: #1D8CE0;
      }
      .bell{
        position: relative;
        width: 20px;
        height: 20px;
        margin-right: 10px;
        line-height: 20px;
        text-align: center;
        cursor:pointer;
        .point{
          position: absolute;
          top: -5px;
          right: -5px;
          width: 15px;
          height: 15px;
          line-height: 15px;
          border-radius: 50%;
          font-size: 12px;
          background: #fa5555;
          color: #fff;
        }
      }
      .bell:hover{
        font-size: 10px;
      }
      .account{
        display: flex;
        align-items: center;
        justify-content: center;
        width: 110px;
        height: 100%;
        cursor:pointer;
        i{
          margin-right: 2px;
          font-size: 16px;
          vertical-align: middle;
        }
        .number{
          vertical-align: top; 
        }
      }
      .account:hover{
        background: #1D8CE0;
      }
    }
  }
</style>
<script>
import {getCookie} from '../../assets/js/cookie'
export default {
  name: 'header',
  data () {
    return {
      zhanghao: ''
    }
  },
  computed: {
    warningAmount () {
      if (this.$store.state.warningAmount>99) {
        return 99
      } else {
        return this.$store.state.warningAmount
      }
    }
  },
  props: {
    actShow: {
      type: Boolean,
    },
    praShow: {
      type: Boolean,
    },
    bellShow: {
      type: Boolean,
    }
  },
  methods: {
    winReload () {
      if (this.$route.path == '/warninglist') {
        window.location.reload()
      }
    },
    // 弹出退出框
    out () {
      this.$emit('out', true)
    },
    goPanorama () {
      this.$router.push({path:'/panorama'})
    }
  },
  mounted () {
    this.zhanghao = sessionStorage.getItem('zhanghao')
  }
}
</script>
