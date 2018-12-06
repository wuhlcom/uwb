<template>
  <div class="statistics">
    <div class="sttt-content">
      <router-link 
      :to="{name: 'cell', query:{num: code, id: cellNum}}" 
      class="flex-one">
        <div>
          <i class="iconfont icon-ren"></i>
          <div class="text">总人数</div>
          <div class="num">{{ total || '0' }}</div>
        </div>
      </router-link>
      <router-link 
      :to="{name: 'cell', query:{num: code, id: cellNum}}" 
      class="flex-one">
          <div>
            <i class="iconfont icon-zaixian"></i>
            <div class="text">实到人数</div>
            <div class="num">{{ attendances || '0'  }}</div>
        </div>
      </router-link>
      <router-link 
      :to="{name: 'absencelist', query:{num: code, id: cellNum}}"
      class="flex-one offline">
      <div>
        <i class="iconfont icon-lixian"></i>
        <div class="text">缺勤人数</div>
        <div class="num">{{ absence || '0' }}</div>
      </div>
      </router-link>
    </div>
  </div>
</template>
<style lang="scss" scoped>
  .statistics{
    height: 100%;
    .sttt-content{
      height: 100%;
      display: flex;
      .flex-one{
        display: flex;
        flex: 1;
        align-items: center;
        justify-content: center;
        margin: 0 16px;
        padding: 6% 0;
        height: 120%;
        text-align: center;
        background: #f6f7fb;
        color: #409eff;
        i{
          display: block;
          font-size: 40px;
          margin-bottom: 10%;
        }
        .text{
          font-size: 16px;
          margin-bottom: 12px;
        }
        .num{
          font-size: 22px;
        }
      }
      .flex-one:hover{
        box-shadow: 0 0 6px 2px #f0f1f5;
        // background: #f0f1f5;
      }
      .offline{
        color: #878d99;
      }
    }  
  }
</style>
<script>
export default {
  name: 'statistics',
  props: {
    code: {
      type: String,
    },
    cellNum: {
      type: String,
    } 
  },
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
  }
}
</script>

