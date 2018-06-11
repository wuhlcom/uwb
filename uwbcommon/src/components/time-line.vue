<template>
  <div ref="line" class="time-line" @click="clickPosition($event)">
    <div ref="pro" class="progress" :style="{width: timeLineLength + 'px'}"></div>
    <div
      @mousedown="moveDown($event)"
      ref="handle"
      class="handle"
      :style="{left: timeLineLength + 'px'}">
      <div v-if="play" class="timeshow">{{formattingTime}}
        <div class="triangle-top"></div>
      </div>
    </div>
  </div>
</template>
<style lang="scss" scoped>
  .time-line{
    position: relative;
    width: 600px;
    height: 6px;
    margin: 0 auto;
    background: #e5e9ef;
    cursor: pointer;
    .progress{
      position: absolute;
      top: 50%;
      height: 6px;
      margin-top: -3px;
      background-color: #0f0;
    }
    .handle{
      position: absolute;
      top: 50%;
      width: 14px;
      height: 14px;
      // margin-left: -7px;
      margin-top: -7px;
      cursor: pointer;
      border-radius: 7px;
      background: #0f0;
      .timeshow{
        position: absolute;
        top: -30px;
        left: -75px;
        width: 150px;
        height: 20px;
        line-height: 20px;
        text-align: center;
        background: rgba(0, 102, 204, 0.5);
        font-size: 12px;
        color: #fff;
        .triangle-top{
          position: absolute;
          bottom: -8px;
          right: 64px;
          width: 0;
          height: 0;
          border-left: 4px solid transparent;
          border-right: 4px solid transparent;
          border-top: 8px solid rgba(0, 102, 204, 0.5);
        }
      }
    }
  }
</style>
<script>
import { mapState, mapGetters } from 'vuex'
export default {
  name: 'time-line',
  data () {
    return {
      progressLength: 0,
      handlePosition: 0,
      onmouse: false,
      disX: 0,
      moveX: 0,
      max: 0,
    }
  },
  computed:{
    ...mapState({
      timeLineLength: state => state.playback.timeLineLength,
      play: state => state.playback.play
    }),
    ...mapGetters([
      'formattingTime'
    ])
  },
  methods: {
    moveDown (event) {
      if (!this.play) return
      this.onmouse = true
		  let l = this.$refs.handle.offsetLeft; 
		  let x = event.clientX ;  
		  this.disX = x-l;  //点击滑块内区域，即鼠标点击点距离元素左边缘的距离
		  this.max = this.$refs.line.offsetWidth - this.$refs.handle.offsetWidth;  //移动最大距离	
    },
    moveMove(event){
      if (!this.onmouse) {   //若没有mousedown滑块，则mousemove不产生距离
        return;
      }
      this.moveX = event.clientX;
      let moveL = Math.min(this.max,Math.max(0,this.moveX - this.disX));
      this.$store.commit('setPlayTimeLength', moveL)
    },
    moveUp(event){
      this.onmouse = false; 
    },
    clickPosition (event) {
      if (!this.play) return
      let distance = event.clientX - this.$refs.line.getBoundingClientRect().left
      this.$store.commit('setPlayTimeLength', distance)
    },
    movePosition (event) {
      let distance = event.clientX - this.$refs.line.getBoundingClientRect().left
    }
  },
  mounted () {
    document.addEventListener('mousemove', this.moveMove, false)
    document.addEventListener('mouseup', this.moveUp, false)
  }
}
</script>
