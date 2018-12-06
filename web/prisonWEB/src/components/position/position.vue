<template>
  <div class="position">
    <canvas id="canvas" :width="sizeWih" :height="sizeHig"></canvas>
  </div>
</template>
<style lang="scss" scoped>
  canvas{
    box-sizing: border-box;
  }
</style>
<script>
export default {
  name:'position',
  data () {
    return {
    }
  },
  props: {
    sizeHig: {
      type: Number
    },
    sizeWih: {
      type: Number
    },
    prisonerPosition: {
      type: Array
    }
  },
  methods: {
    drawCanvas (a) {
      let cvs = document.querySelector('#canvas')
      let ctx = cvs.getContext('2d')
      ctx.clearRect(0, 0, cvs.width, cvs.height)
      a.forEach(e => {
        // 获取当前文本的宽度，可自适应的设置文字背景
        let textWidth = ctx.measureText((e.name)).width
        let color = ''
        switch (e.status) {
          case 1:
            color = '#fa5555'
          break;
          case 2:
            color = '#ff8c00'
          break;
          case 3:
            color = '#f5c124'
          break;
          case 4:
            color = '#409eff'
          break;
        }
        ctx.beginPath()
        ctx.fillStyle = color
        ctx.arc(e.x, e.y, 5, 0 * Math.PI, 2 * Math.PI)
        if (e.nameshow == true) {
          let x,y = 0
          if (e.y <= 30) {
            y = e.y + 10
          } else{
            y = e.y - 27
          }
          if (e.x - textWidth <= 0) {
            x = textWidth / 2;
          } else if (e.x + textWidth >= cvs.width) {
            x = e.x - textWidth;
          } else {
            x = e.x - textWidth / 2;
          }
          ctx.fillRect(x, y, textWidth, 15)
        }  
        ctx.fill()
        ctx.closePath()
      })
      a.forEach(e => {
        let textWidth = ctx.measureText((e.name)).width
        if (e.nameshow == true) {
          let y, x = 0
          if (e.y <= 30) {
            y = e.y + 22
          } else{
            y = e.y - 15
          }
          if (e.x - textWidth <= 0) {
            x = textWidth;
          } else if (e.x + textWidth >= cvs.width) {
            x = e.x - textWidth;
          } else {
            x = e.x;
          }
          ctx.font = '12px serif'
          ctx.fillStyle = '#fff'
          ctx.textAlign="center"
          ctx.fillText(e.name, x, y)
        } 
      })
    }
  },
  mounted () {
  },
  watch: {
    sizeWih (val) {
      this.drawCanvas(this.prisonerPosition)
    },
    prisonerPosition (val) {
      this.drawCanvas(val)
    }
  }
}
</script>
