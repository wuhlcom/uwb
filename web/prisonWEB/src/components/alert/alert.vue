<template>
  <div class="alert">
    <div class="alert-box">
      <div class="box-h">
        <span class="title">提示</span>
        <i @click="close()" class="iconfont icon-X"></i>
      </div>
      <div class="box-c">
        <i class="iconfont icon-icon"></i>
        <span>确定退出吗？</span>
      </div>
      <div class="box-but">
        <span @click="close()" class="cancel">取消</span>
        <span @click="goLogin()" class="determine">确定</span>
      </div>
    </div>
  </div>
</template>
<style lang="scss" scoped>
  .alert{
    position: absolute;
    bottom: 0;
    top: 0;
    left: 0;
    width: 100%;
    z-index: 1000;
    background: rgba(0, 0, 0, .4);
    .alert-box{
      position: absolute;
      top: 50%;
      left: 50%;
      margin-top: -100px;
      margin-left: -200px;
      width: 400px;
      height: 200px;
      background: #fff;
      .box-h{
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
        }
      }
      .box-c{
        margin-top: 35px;
        padding-left: 50px;
        height: 45px;
        line-height: 45px;
        i{
          font-size: 50px;
          color: #eb9e05;
          padding-right: 20px; 
        }
        span{
          font-size: 14px;
          vertical-align: top;
          color: #2d2f33;
        }
      }
      .box-but{
        position: absolute;
        bottom: 21px;
        right: 20px;
        font-size: 13px;
        .cancel{
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
        .determine{
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
    name: 'alert',
    methods: {
      // 关闭弹出层
      close () {
        this.$emit('hide', false)
      },
      // 退出登录
      goLogin () {
        let tk = sessionStorage.getItem('token')
        this.$http.Post('/permission/user/logout', {"token": tk})
        this.$router.push({path:'/login'})
      }
    }
  }
</script>
