<template>
  <div class="sequencinglist">
    <span 
    v-for="item in sequencinglist"
    :key="item.id"
    @click="sequencingClass($event)" 
    :class="[item.flex, item.line]">
      <span class="text">{{ item.text }}</span>
      <div @click="sequencingData(item.msg)" v-if="item.sequencingShow" class="sequencing" :class="item.elect">
        <i class="iconfont icon-arrLeft-fill up"></i>
        <i class="iconfont icon-arrLeft-fill1 down"></i>
      </div>
    </span>
  </div>
</template>
<style lang="scss" scoped>
  .sequencinglist{
    display: flex;
    width: 100%;
    height: 40px;
    line-height: 40px;
    border-top: 1px solid #e5e9f2;
    background: #eff2f7;
    font-size: 14px;
    color: #2d2f33;
    .flex-one{
      flex: 1;
    }
    .flex-two{
      flex: 2;
    }
    .flex-three{
      flex: 3;
    }
    .flex-four{
      flex: 4;
    }
    .flex-penta{
      flex: 5;
    }
    .flex-one,
    .flex-two,
    .flex-three,
    .flex-four,
    .flex-penta{
      padding-left: 10px;
      .text{
        float: left;
      }
      .sequencing{
        position: relative;
        display: inline-block;
        height: 100%;
        margin-left: 5px; 
        color: #b4bccf;
        .up{
          position: absolute;
          top: -2px;
        }
        .down{
          position: absolute;
          top: 5px;
        }
      }
      .elect-up{
        .up{
          color: #4e90ff;
        }
      }
      .elect-down{
        .down{
          color: #4e90ff;
        }
      }
    }
    .left-line{
      border-left: 1px solid #ddd;
    }
  }
</style>
<script>
export default {
  name: 'sequencinglist',
  data () {
    return {
    }
  },
  props: {
    sequencinglist: {
      type: Array,
    }
  },
  methods: {
    // 排序更换样式
    sequencingClass (event) {
      let e = event.target
      if (e.nodeName == "I") {
        let seq = document.getElementsByClassName('sequencing')
        for (let i=0; i<seq.length; i++) {
          if (seq[i] !== e.parentNode) {
            seq[i].setAttribute('class', 'sequencing')
          }
        }
        let parcls = e.parentNode.getAttribute('class')
        if (parcls == 'sequencing'){
          e.parentNode.setAttribute('class', 'sequencing elect-up')
          this.$emit('sequencingClass', 'asc')
        } else if (parcls == 'sequencing elect-up') {
          e.parentNode.setAttribute('class', 'sequencing elect-down')
          this.$emit('sequencingClass', 'desc')
        } else if (parcls == 'sequencing elect-down') {
          e.parentNode.setAttribute('class', 'sequencing elect-up')
          this.$emit('sequencingClass', 'asc')
        }
      }
    },
    sequencingData (a) {
      this.$emit('sequencingContent', a)
    }
  },
  mounted () {
    this.sequencinglist.forEach(e => {
      switch (e.flex) {
      case 1:
        e.flex = 'flex-one'
        break;
      case 2:
        e.flex = 'flex-two'
        break;
      case 3:
        e.flex = 'flex-three'
        break;
      case 4:
        e.flex = 'flex-four'
        break;
      case 5:
        e.flex = 'flex-penta'
        break;
      default:
        this.flex = 'flex-one'
        break;
      }
      switch (e.elect) {
        case 'up':
          e.elect = 'elect-up'
          break;
        case 'down':
          e.elect = 'elect-down'
      }
      if (e.line == true) {
        e.line = 'left-line'
      }
    })
  }
}
</script>
