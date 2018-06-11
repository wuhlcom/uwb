<template>
  <div ref="box" class="draw-grids">
    <canvas id="grids" :width="canvasWidth" :height="canvasHeight"></canvas>
  </div>
</template>
<style lang="scss" scoped>
  .draw-grids {
    width: 100%;
    height: 100%;
  }
</style>
<script>
export default {
  name: 'grids',
  data () {
      return {
        viewSize: document.body.clientWidth      // 初始化视图窗口大小，这个很重要！！！
      }
    },
    computed: {
      proportionWith () {
        return this.$store.state.mapProportion.width
      },
      proportionHeight () {
        return this.$store.state.mapProportion.height
      },
      canvasHeight () {
        return this.$store.state.canvasSize.height
      },
      canvasWidth () {
        return this.$store.state.canvasSize.width
      }
    },
    methods: {
       drawGrid(){  
        let cvs = document.getElementById('grids')
        let context = cvs.getContext('2d')
          context.save();  
          context.lineWidth = 1;  
          context.strokeStyle = '#666';  
          for(var i = 0; i < context.canvas.height; i += this.proportionHeight){  
            context.beginPath();  
            context.moveTo(0, i);  
            context.lineTo(context.canvas.width, i);  
            context.stroke();  
          }  
          for(var i = 0; i < context.canvas.width; i += this.proportionWith){  
            context.beginPath();  
            context.moveTo(i, 0);  
            context.lineTo(i, context.canvas.height);  
            context.stroke();  
          }  
          context.restore();  
      }  
    },
    mounted () {
      this.drawGrid()
      // 当浏览器窗口大小发生改变是对 viewSize 进行重新赋值
      window.onresize = () => {
        return (() => {
          this.viewSize = document.body.clientWidth
        })()
      }
    },
    updated () {
      this.drawGrid()
    },
    watch: {
      // 监听 viewSize，当该属性值发生变化时canvas大小也会改变
      viewSize () {
        this.getSize()
        this.drawGrid()
      }
    }
}
</script>
