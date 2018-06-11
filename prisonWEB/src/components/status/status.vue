<template>
  <div class="status">
    <div class="title vertical">监仓{{cellNum}}</div>
    <div class="status-info">
      <span v-for="item in prisonerstatus" :key="item.id" :class="[status(item.flag)]">
        <router-link :to="{name: 'prisonerinfo', query: {num: item.code,id: id}}">
          <i class="iconfont icon-ren"></i>
          <div class="name">{{item.name}}</div>
          <div class="labels">{{item.msg}}</div>
        </router-link>
      </span>
    </div>
  </div>
</template>
<style lang="scss" scoped>
  .status{
    position: relative;
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
    .title{
      position: absolute;
      top: 20px;
      left: 20px;
      font-size: 14px;
    }
    .status-info{
      display: flex;
      flex-wrap: wrap;
      width: 90%;
      span{
        display: inline-block;
        margin: 10px 0; 
        width: 16%;
        text-align: center;
        i{
          font-size: 35px;
        }
        .name{
          margin-bottom: 3px ;
          font-size: 13px;
        }
        .labels{
          display: inline-block;
          min-width: 50px;
          height: 24px;
          line-height: 24px;
          font-size: 13px;
          color: #fff;
          border-radius: 2px;
        }
      }
      .online{
        i{
          color: #409eff;
        }
        .name{
          color: #409eff;
        }
        .labels{
          background: #409eff;
        }
      }
      .severity{
        i{
          color: #fa5555;
        }
        .name{
          color: #fa5555;
        }
        .labels{
          background: #fa5555;
        }
      }
      .general{
        i{
          color: #ff8c00;
        }
        .name{
          color: #ff8c00;
        }
        .labels{
          background: #ff8c00;
        }
      }
      .hint{
        i{
          color: #f5c124;
        }
        .name{
          color: #f5c124;
        }
        .labels{
          background: #f5c124;
        }
      }
      .offline{
        i{
          color: #878d99;
        }
        .name{
          color: #878d99;
        }
        .labels{
          background: #878d99;
        }
      }
    }
  }
</style>
<script>
export default {
  name: 'status',
  data () {
    return {
      id: this.$route.query.id
    }
  },
  computed: {
    prisonerstatus () {
      return this.$store.state.prisoners
    }
  },
  props: {
    // prisonerstatus: {
    //   type:Array,
    // },
    cellNum: {
      type: String,
    }
  },
  methods: {
    status(status) {
      switch (status) {
        case 1:
          // 严重
          return 'severity'
        break;
        case 2:
          // 普通
          return 'general'
        break;
        case 3:
          // 提示
          return 'hint'
        case 4:
          // 在线
          return 'online'
        break;
        case 5:
          // 离线
          return 'offline'
        break;
        case 7:
          return 'severity'
        break;
         case 6:
          return 'severity'
        break;
      }
    }  
  },
  watch: {
  }
}
</script>
