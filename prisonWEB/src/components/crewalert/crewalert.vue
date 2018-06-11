<template>
  <div class="crewalert">
    <div class="alert-box">
      <div class="box-h">
        <span class="title">人员分布统计</span>
        <i @click="close()" class="iconfont icon-X"></i>
      </div>
      <div class="head">
        <div>总人数{{ total }}人</div>
        <div>实到人数{{ attendances }}人</div>
        <div>缺勤人数{{ absence }}人</div>
      </div>
      <div class="content">
        <div>监仓</div>
        <div>{{ attendances }}人</div>
      </div>
       <div class="content">
        <div>阅览室</div>
        <div>0人</div>
      </div>
       <div class="content">
        <div>活动室</div>
        <div>0人</div>
      </div>
       <div class="content">
        <div>洗澡间</div>
        <div>0人</div>
      </div>
       <div class="content">
        <div>谈话室</div>
        <div>0人</div>
      </div>
       <div class="content">
        <div>走廊</div>
        <div>0人</div>
      </div>
      <div class="but">
        <div @click="close()" class="close">取消</div>
        <div @click="close()" class="ok">确认</div>
      </div>
    </div>
  </div>
</template>
<style lang="scss" scoped>
  .crewalert{
    position: absolute;
    bottom: 0;
    top: 0;
    left: 0;
    width: 100%;
    z-index: 1000;
    background: rgba(0, 0, 0, .4);
    font-size: 0;
    .alert-box{
      position: absolute;
      top: 50%;
      left: 50%;
      margin-top: -213px;
      margin-left: -190px;
      padding: 20px;
      width: 380px;
      height: 426px;
      background: #fff;
      .box-h{
        width: 100%;
        height: 15px;
        line-height: 15px;
        font-size: 16px;
        i{
          position: absolute;
          right: 20px;
          color: #8a909c;
          cursor:pointer;
        }
      }
      .head{
        display: flex;
        margin: 19px -2px 0;
        height: 50px;
        font-size: 13px;
        color: #2d2f33;
        div{
          display: inline-block;
          width: 111px;
          height: 50px;
          line-height: 50px;
          text-align: center;
          background: #f6f7fb;
          margin: 0 2px;
        }
      }
      .content{
        display: flex;
        justify-content:space-between;
        margin-top: 4px;
        padding: 0 20px;
        height: 37px;
        line-height: 37px;
        font-size: 13px;
        background: #f6f7fb;
      }
      .but{
        position: absolute;
        bottom: 21px;
        right: 20px;
        font-size: 13px;
        .close{
          display: inline-block;
          margin-right: 10px;
          width: 60px;
          height: 30px;
          line-height: 30px;
          text-align: center;         
          border: 1px solid #ddd;
          border-radius: 5px;
          color: #919398;
          cursor:pointer;
        }
        .ok{
          display: inline-block;
          width: 60px;
          height: 30px;
          line-height: 30px;
          text-align: center;
          border-radius: 5px;
          background: #409eff;
          color: #fff;
          cursor:pointer;
        }
      }
    }
  }
</style>
<script>
  export default {
    name: 'crewalert',
    computed: {
      // 缺勤人数
      absence () {
        return this.$store.state.personnelInof.absence
      },
      // 出席人数
      attendances () {
        return this.$store.state.personnelInof.attendances
      },
      // 总人数
      total () {
        return this.$store.state.personnelInof.total
      }
    },
    methods: {
      // 关闭弹出层
      close () {
        this.$emit('close', false)
      },
      // 
      ok () {
        this.$router.push({path:'/'})
      }
    }
  }
</script>
