<template>
  <div class="alarm-window">
    <div class="title">告警</div>
    <div
      @click="close" 
      class="close">
      <i class="iconfont icon-quxiao"></i>
    </div>
    <div class="content">
      <div>
        <i class="level" :class="level"></i>
        <span class="info">{{personName}}{{msg}}</span>
        <span class="time">{{timestamp}}</span>
      </div>
    </div>
    <div class="cut">
      <i class="out iconfont icon-weibiaoti518" @click="out"></i>
      <span>{{num+1}}/10</span>
      <i class="iconfont icon-weibiaoti518" @click="go"></i>
    </div>
  </div>
</template>
<style lang="scss" scoped>
  @import '../assets/css/common.scss';
  .alarm-window{
    width: 450px;
    height: 239px;
    background: url('../assets/img/alarm.png');
    background-size: 100% 100%;
    .title{
      width: 171px;
      height: 34px;
      margin: 10px auto;
      text-align: center;
      line-height: 34px;
      font-size: 16px;
      color: #fff;
      cursor: pointer;
    }
    .close{
      position: absolute;
      top: 30px;
      right: 5px;
      cursor: pointer;
      .iconfont{
        font-size: 30px;
        color: #36a2fe;
      }
    }
    .content{
      display: flex;
      align-items: center;
      width: 100%;
      height: 180px;
      div{
        display: flex;
        align-items: center;
        width: 100%;
        height: 30px;
        padding: 0 26px;
        line-height: 30px;
        font-size: 13px;
        color: #fff;
        cursor: pointer;
        .level{
          margin-right: 18px;
        }
        .severity{
          @include point(10px,$severity);
        }
        .general{
          @include point(10px,$general);
        }
        .hint{
          @include point(10px,$hint);
        }
        .info{
          flex: 1;
          vertical-align: middle;
        }
        .time{
          text-align: right;
        }
      }
    }
    .cut{
      position: absolute;
      left: 50%;
      bottom: 20px;
      display: flex;
      justify-content: space-between;
      width: 100px;
      margin-left: -40px;
      color: #fff;
      i{
        display: inline-block;
        font-size: 22px;
        vertical-align: bottom;
        cursor: pointer;
      }
      .out{
        transform: rotate(180deg)
      }
      span{
        display: inline-block;
        position: absolute;
        top: 4px;
        left: 26px;
        width: 40px;
        text-align: center;
      }
    }
  }
</style>
<script>
export default {
  name: 'alarm-window',
  props: {data: Array},
  data () {
    return {
      personName: '',
      msg: '',
      level: '',
      timestamp: '',
      num: this.data.length-1,
    }
  },
  methods: {
    close () {
      this.$emit('windowShow', false)
    },
    turn (num) {
      this.personName = this.data[num].personName
      this.msg = this.data[num].msg
      this.level = this.data[num].level
      this.timestamp = this.data[num].timestamp
    },
    go () {
      if (this.num < this.data.length-1) {
        this.num++  
        this.turn(this.num)
      }
    },
    out () {
      if (this.num > 0) {
        this.num--
        this.turn(this.num)
      }
    }
  },
  created () {
    this.turn(this.num)
  },
  watch: {
    data () {
      this.turn(this.data.length-1)
      this.num = this.data.length-1
    }
  }
}
</script>
