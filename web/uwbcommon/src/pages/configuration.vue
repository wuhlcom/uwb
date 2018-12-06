<template>
  <div class="configuration">
    <div @click="navShows" v-show="!navShow" class="unfold">
      <i class="iconfont icon-zhankai"></i>
    </div>
    <div v-show="navShow" class="nav">
      <div class="nav-head">
        <div @click="navShows" class="shrink">
          <i class="iconfont icon-zhankai"></i>
        </div>
      </div>
      <div class="nav-main">
        <div v-for="(item, index) in navList" :key="index">
          <router-link
            @click.native="handleOpen(index)"
            :to='{name: item.url}' class="nav-title"
            :class="navList[index].show ? 'title-opt': ''">
            {{item.title}}
          </router-link>
          <div v-show="navList[index].show">
            <router-link
            v-for="(items, index) in item.option"
            :key="index" :to='{name: items.url}'
            class="nav-content">
            {{items.name}}
          </router-link>
          </div>
        </div>
      </div>
      <div class="nav-bottom"></div>
    </div>
    <router-view class="content"></router-view>
  </div>
</template>
<style lang="scss" scoped>
@import "../assets/css/nav.scss";
.configuration {
  display: flex;
  height: 78%;
  margin-top: 50px;
  .nav-title {
    display: block;
    width: 167px;
    height: 47px;
    margin: 20px auto;
    text-align: center;
    line-height: 47px;
    color: #fff;
    font-size: 14px;
    background: url("../assets/img/unselected-nav.png");
    user-select: none;
    cursor: pointer;
  }
  .title-opt {
    background: url("../assets/img/opt-nav.png");
  }
  .router-link-active {
    color: #fff;
  }
  .content {
    flex: 1;
    padding-left: 42px;
    padding-right: 71px;
    color: #59b3ff;
  }
}
@media screen and (max-height: 820px) {
  .configuration {
    margin-top: 20px;
  }
}
.nav-content {
  display: block;
  width: 166px;
  height: 32px;
  margin: 10px auto;
  text-align: center;
  font-size: 13px;
  line-height: 32px;
  color: #68acff;
  user-select: none;
  cursor: pointer;
  &:hover {
    background: rgba(0, 60, 140, 0.8);
  }
}
</style>
<script>
export default {
  name: "configuration",
  components: {},
  data() {
    return {
      /* 
      * 左侧导航栏数据
      * title - 标题
      * show - 判断是否展开的值
      * url - 页面跳转的路由名
      * option - 二级菜单
      *   |-- name - 二级菜单标题
      *   |-- url - 页面跳转的路由名
      */
      navList: [
        {
          title: "人员管理",
          show: false,
          url: "PersonnelManagement",
          option: [
            {
              name: '人员管理',
              url: 'PersonnelManagement'
            },
            {
              name: '组织管理',
              url: 'organizational'
            }
          ]
        },              
        {
          title: "策略管理",
          show: false,
          url: 'tactics',
          option: [
            {
              name: '围栏策略',
              url: 'tactics'
            }
          ]
        },
        {
          title: "设备管理",
          show: false,
          url: "upgrade",
          option: [
            {
              name: "设备升级",
              url: "upgrade",
            }
          ]
        },
        {
          title: "区域管理",
          show: false,
          url: "area",
          option: []
        },
        {
          title: "文件管理",
          show: false,
          url: "maparrangement",
          option: [
            {
              name: "地图管理",
              url: "maparrangement"
            },
            {
              name: "版本管理",
              url: "edition"
            }
          ]
        },
        {
          title: "二次开发",
          show: false,
          url: "twice",
          option: []
        }
      ]
    };
  },
  computed: {
    navShow() {
      return this.$store.state.nav.navShow;
    }
  },
  methods: {
    navShows() {
      this.$store.state.nav.navShow
        ? (this.$store.state.nav.navShow = false)
        : (this.$store.state.nav.navShow = true)
    },
    // 判断导航栏是否展开
    handleOpen(key) {
      if (this.navList[key].show) {
        this.navList[key].show = false
      } else {
        this.navList.forEach(e => {
          e.show = false
        })
        this.navList[key].show = true
      }
    }
  },
  created() {
    this.$store.state.nav.navShow = true
  }
}
</script>
