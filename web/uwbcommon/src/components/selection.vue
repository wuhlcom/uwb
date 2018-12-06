<template>
  <div class="selection" 
    @click="select()">
    <i 
      :class="opt ? 'icon-fangxingxuanzhong': 'icon-fangxingweixuanzhong'"
      class="iconfont">
    </i>
    <span>{{msg}}</span>
  </div>
</template>
<style lang="scss" scoped>
  .selection{
    display: inline-block;
    cursor: pointer;
    user-select: none;
    span,
    i{
      vertical-align: middle;
    }
  }
</style>
<script>
export default {
  name: 'selection',
  props: {
    msg: {
      type: String
    },
  },
  data () {
    return {
      opt: false
    }
  },
  methods: {
    select () {
      this.opt ? this.opt = false : this.opt = true
      this.$emit('msg', this.opt)
    }
  }
}
</script>
