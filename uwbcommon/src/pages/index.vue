<template>
  <div class="index">
    <v-head></v-head>
    <router-view class="view"></router-view>
    <footer></footer>
  </div>
</template>
<style lang="scss" scoped>
  .index{
    position: relative;
    width: 100%;
    min-width: 1336px;
    height: 100%;
    min-height: 720px;
    background: url('../assets/img/background.png');
    .view{
      position: relative;
      z-index: 999;
      overflow: auto;
    }
    footer{
      position: absolute;
      bottom: 0;
      width: 100%;
      min-width: 1336px;
      height: 260px;
      background: url('../assets/img/footer.png');
      background-size: 100% 100%;
    }
  }
</style>
<script>
import VHead from '../common/head'
export default {
  name: 'index',
  components: {
    VHead
  }
}
</script>
