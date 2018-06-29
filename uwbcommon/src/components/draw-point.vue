<template>
  <div ref="box" class="draw-point">
    <canvas id="point" :width="canvasWidth" :height="canvasHeight"></canvas>
  </div>
</template>
<style lang="scss" scoped>
.draw-point {
  width: 100%;
  height: 100%;
}
</style>
<script>
export default {
  name: 'draw-point',
  props: {
    allPerson: {
      type: Array
    },
    a:{
      type: Number
    },
    statusShow: {
      type: Boolean
    },
    nameShow: {
      type: Boolean,
      default: false
    },
  },
  data () {
    return {
      width: 0,
      height: 0,
      viewSize: document.body.clientWidth, // 初始化视图窗口大小，这个很重要！！！
      radius: 5,
      lucency: 1,
    }
  },
  computed: {
    canvasHeight () {
      return this.$store.state.canvasSize.height
    },
    canvasWidth () {
      return this.$store.state.canvasSize.width
    },
    proportionWith () {
      return this.$store.state.mapProportion.width
    },
    proportionHeight () {
      return this.$store.state.mapProportion.height
    },
  },
  methods: {
    animation (arr) {
      let cvs = document.getElementById('point')
      let ctx = cvs.getContext('2d')
      let darw = () => {
        ctx.clearRect(0, 0, cvs.width, cvs.height)
        // 画点
        // ctx.beginPath()
        arr.forEach(e => {
          if (e.show){
            let lucency = 0
            e.point.forEach(el => {
              if (el) {
                ctx.beginPath()
                ctx.arc(el.x * this.proportionWith, el.y * this.proportionHeight, 5, 0, Math.PI * 2)
                if (e.point.length === 1){
                  lucency = 1
                } else {
                  lucency+=(1/e.point.length)
                }
                if(el.type == 2 || !this.statusShow) {
                  ctx.fillStyle = 'rgba(0,255,0,'+lucency+')'
                  ctx.fill()
                  ctx.strokeStyle = 'rgba(0,255,0,'+lucency+')'
                  ctx.stroke()
                } else if (this.statusShow) {
                  ctx.fillStyle = 'rgba(255,0,0,'+lucency+')'
                  ctx.fill()
                  ctx.strokeStyle = 'rgba(255,0,0,'+lucency+')'
                  ctx.stroke()
                }
                ctx.closePath()
              }
            })
            if (this.nameShow) {
              ctx.beginPath()
              ctx.font = '12px serif'
              ctx.textAlign = 'center'
              ctx.textBaseline = 'center'
              if (e.namePosition.type == 3) {
                ctx.fillStyle = 'rgba(255,0,0,1)'
              } else {
                ctx.fillStyle = 'rgba(0, 0, 0, 1)'
              }
              let y = 0
              if (e.namePosition.y-10 < 5) {
                y = e.namePosition.y + 20
              } else {
                y = e.namePosition.y - 10
              }
              ctx.fillText(e.personName, e.namePosition.x, y)
              ctx.closePath()
            }
          }
        })
      }
      darw()
    }
  },
  mounted () {
    // window.requestAnimationFrame(this.animation)
    // 当浏览器窗口大小发生改变是对 viewSize 进行重新赋值
    window.onresize = () => {
      return (() => {
        this.viewSize = document.body.clientWidth
      })()
    }
    this.animation(this.allPerson)
  },
  updated () {
  },
  watch: {
    // 监听 viewSize，当该属性值发生变化时canvas大小也会改变
    viewSize () {
      this.getSize()
    },
    a () {
      this.animation(this.allPerson)
    }
  }
}
</script>
