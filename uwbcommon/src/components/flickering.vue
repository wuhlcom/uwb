<template>
  <div ref="box" class="draw-flickering">
    <canvas @click="clickAlert($event)" id="flickering" :width="canvasWidth" :height="canvasHeight"></canvas>
  </div>
</template>
<style lang="scss" scoped>
.draw-flickering {
  width: 100%;
  height: 100%;
}
</style>
<script>
export default {
  name: 'draw-flickering',
  props:{
    point: {type: Array},
    scale: {type: Number}
  },
  data () {
    return {
      radius: 5,
      personObj: {},
      radiusArr: [
        
      ]
    }
  },
  computed: {
    canvasHeight () {
      return this.$store.state.canvasSize.height
    },
    canvasWidth () {
      return this.$store.state.canvasSize.width
    }
  },
  methods: {
    // 闪烁动画
    animation () {
      let cvs = document.getElementById('flickering')
      let ctx = cvs.getContext('2d')
      ctx.clearRect(0, 0, cvs.width, cvs.height)
      this.radius += 0.5 // 每一帧半径增加0.5
      //半径radius大于15时，重置为0
      if (this.radius > 15) {
        this.radius = 5
      }
      this.point.forEach(e => {
        if (e.namePosition.type === 1000) {
          ctx.beginPath()
          ctx.arc(e.namePosition.x, e.namePosition.y, this.radius, 0, Math.PI * 2);
          ctx.closePath()
          ctx.lineWidth = 5 // 线条宽度
          ctx.strokeStyle = `rgba(250,0,0,0.5)` // 颜色
          ctx.stroke()
        }
      })
      window.requestAnimationFrame(this.animation)
    },
    // 点击事件
    clickAlert (evnet) {
      let cvs = document.getElementById('flickering')
      // 获取鼠标点击位置的X轴和Y轴的值，这里需要除以canvas缩放的比例值，不然在canvas缩放时获取不到准确的值
      let x = (event.pageX - cvs.getBoundingClientRect().left) / this.scale
      let y = (event.pageY - cvs.getBoundingClientRect().top) / this.scale
      // 用鼠标点击的位置的值与所有位置点进行对比
      for (let i = this.point.length - 1; i > -1; i--) {
        let c = this.point[i];
        if ((x - c.namePosition.x) * (x- c.namePosition.x) + (y - c.namePosition.y) * (y - c.namePosition.y) <= 20) {
          this.personObj = c
          this.open()
        }
      }  
    },
    // 人员信息弹窗
    open() {
        const h = this.$createElement;
        this.$message({
          showClose: true,
          duration: 5000,
          message: h('i', { style: 'color: teal; width: 500px'},
          `名字：${this.personObj.personName}，
          职位：${this.personObj.positionName}，
          级别：${this.personObj.levelName}，
          心率：${this.personObj.heartRate}，
          电量：${this.personObj.power}`)
        });
      }
  },
  mounted () {
    this.animation()
  }
}
</script>
