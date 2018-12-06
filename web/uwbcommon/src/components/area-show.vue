<template>
  <div class="draw-area">
    <canvas id="area" :width="canvasWidth" :height="canvasHeight"></canvas>
  </div>
</template>
<style lang="scss" scoped>
  .draw-area {
    width: 100%;
    height: 100%;
  }
</style>
<script>
  export default {
    name: 'area-show',
    props: {
      rectangle: {
        type: Array
      },
      polygon: {
        type: Array
      }
    },
    data () {
      return {
        viewSize: document.body.clientWidth      // 初始化视图窗口大小，这个很重要！！！
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
      draw (rectangle,polygon) {
        var canvas = document.getElementById('area')
        if (canvas.getContext) {
          var ctx = canvas.getContext('2d')
          if (rectangle) {
            rectangle.forEach(e => {
              ctx.beginPath()
              ctx.moveTo(e.points[0].x, e.points[0].y)
              e.points.forEach((el, i) => {
                if (i > 0) {
                  ctx.lineTo(el.x, el.y)
                }
              })
              ctx.lineTo(e.points[0].x, e.points[0].y)
              ctx.fillStyle = 'rgba(255, 255, 185, .5)'
              ctx.fill()
              ctx.strokeStyle = '#f00'
              ctx.stroke()
              ctx.closePath()
              ctx.beginPath()
              ctx.font = '12px serif'
              ctx.textAlign = 'center'
              ctx.textBaseline = 'center'
              ctx.fillStyle = 'rgba(0, 0, 0, 1)'
              ctx.fillText(e.fenceName, e.textx, e.texty)
              ctx.closePath()
            })
          }
          if (polygon) {
            polygon.forEach(e => {
              ctx.beginPath()
              ctx.moveTo(e.points[0].x, e.points[0].y)
              e.points.forEach((el, i) => {
                if (i > 0) {
                  ctx.lineTo(el.x, el.y)
                }
              })
              ctx.lineTo(e.points[0].x, e.points[0].y)
              ctx.fillStyle = 'rgba(255, 255, 185, .5)'
              ctx.fill()
              ctx.strokeStyle = '#f00'
              ctx.stroke()
              ctx.closePath()
              ctx.beginPath()
              ctx.font = '12px serif'
              ctx.textAlign = 'center'
              ctx.textBaseline = 'center'
              ctx.fillStyle = 'rgba(0, 0, 0, 1)'
              ctx.fillText(e.fenceName, e.points[0].x, e.points[0].y)
              ctx.closePath()
            })
          }
        }
      }
    },
    mounted () {
      this.draw(this.rectangle, this.polygon)
      // 当浏览器窗口大小发生改变是对 viewSize 进行重新赋值
      window.onresize = () => {
        return (() => {
          this.viewSize = document.body.clientWidth
        })()
      }
    },
    updated () {
      console.log(this.polygon);
      
      this.draw(this.rectangle, this.polygon)
    },
    watch: {
      // 监听 viewSize，当该属性值发生变化时canvas大小也会改变
      viewSize () {
        this.getSize()
        this.draw(this.rectangle, this.polygon)
      }
    }
  }
</script>