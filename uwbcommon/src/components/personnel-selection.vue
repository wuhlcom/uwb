<template>
  <div class="nav-box">
    <div
      @click="navShows"
      v-show="!navShow"
      class="unfold">
      <i class="iconfont icon-zhankai"></i>
    </div>
    <div class="nav" v-show="navShow">
        <div class="nav-head">
        <div @click="navShows" class="shrink">
          <i class="iconfont icon-zhankai"></i>
        </div>
      </div>
      <div class="nav-main">
        <tree
          v-if="treeShow&&department.length&&person.length"
          :department="department"
          :person="person"
          @getPerson="selectPersonnel"
        ></tree>
      </div>
      <div class="nav-bottom"></div>
    </div>
  </div>
</template>
<style lang="scss" scoped>
 @import '../assets/css/nav.scss';
  .select{
    position: absolute;
  }
  .checkAll{
    margin: 26px 0 0 31px;
    font-size: 13px;
    color: #59b3ff;
    cursor: pointer;
  }
  .nav-title {
    width: 167px;
    height: 47px;
    margin: 20px auto;
    text-align: center;
    line-height: 47px;
    color: #fff;
    font-size: 14px;
    background: url('../assets/img/unselected-nav.png');
    user-select: none;
    cursor: pointer;
  }
  .title-opt {
    background: url('../assets/img/opt-nav.png')
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
      background: rgba(0, 60, 140, .8);
    }
  }
</style>
<script>
  import tree from '../components/tree/tree'
  export default {
    name: 'left',
    components: {tree},
    props: {
      groupPeople: {
        type: Array
      },
      unfold: {
        type: Array
      },
      pitchOn: {
        type: Array
      }
    },
    data () {
      return {
        checkedCities: [],
        checkAll: false,
        navShow: true,
        treeShow: false,
        department: [],
        person: [],
      }
    },
    methods: {
      selectPersonnel (msg) {
        this.checkedCities = msg
      },
      // 导航展开与缩放
      navShows () {
        this.navShow ? this.navShow = false : this.navShow = true
        this.$emit('show', 1)
      },
      getData () {
        this.$http.Get(this.$url.departments).then(res => {
        }).catch(rej => {
          this.department = rej.data.result
        })
        this.$http.Get(this.$url.personName).then(res => {
          }).catch(rej => {
          this.person = rej.data.result
          this.$store.commit('setAllPersonArr', this.person)
          this.treeShow = true
        })
      }
    },
    watch: {
      checkedCities (val) {
        this.$emit('update:pitchOn', val)
      }
    },
    created () {
      this.getData()
    },
    mounted () {

    }
  }
</script>
