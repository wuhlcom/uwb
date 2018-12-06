<template>
  <div class="error-404">
    <div class="top">
      <div class="text">
        <h1>404</h1>
        <p>您所访问的页面不存在</p>
        <router-link :to="{name: 'realtime'}">返回首页</router-link>
      </div>
    </div>
  </div>
</template>
<style lang="scss" scoped>
  .error-404{
    position: relative;
    height: 100%;
    background: url('../assets/img/404.jpg');
    .top{
      display: flex;
      justify-content: center;
      align-items: center;
      height: 100%;
      background: rgba(0, 0, 0, 0.2);
      .text{
        width: 400px;
        text-align: center;
        color: #fff;
        h1{
          font-size: 120px;
        }
        p{
          margin: 20px 0;
          font-family: serif;
        }
        a{
          display: inline-block;
          padding-bottom: 5px; 
          border-bottom: 1px solid #fff;
          font-size: 16px;
          font-family: serif;
          color: #fff;
        }
      }
    }
  }
</style>
<script>
export default {
  name: 'error-404'
}
</script>
