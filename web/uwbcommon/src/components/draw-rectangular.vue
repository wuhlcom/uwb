<template>
  <div class="draw-rectangular">
    <canvas id="canvas" :width="canvasWidth" :height="canvasHeight"></canvas>
  </div>
</template>
<style lang="scss" scoped>
.draw-rectangular {
  width: 100%;
  height: 100%;
  // border: 1px solid #f00;
}
</style>
<script>
export default {
  name: "draw-rectangular",
  props: {
    canvasHeight: {
      type: Number
    },
    canvasWidth: {
      type: Number
    },
    heightRatio: {
      type: Number
    },
    widthRatio: {
      type: Number
    }
  },
  data() {
    return {
      viewSize: document.body.clientWidth, // 初始化视图窗口大小，这个很重要！！！
      coord: [{ x: 0, y: 0 }, { x: 0, y: 0 }, { x: 0, y: 0 }, { x: 0, y: 0 }],
      color: "#f00",
      lineWidth: "1"
    };
  },
  methods: {
    goBackCoord() {
      this.$emit("coord", this.coord)
    },
    DrawRectangular() {
      let cvs = document.getElementById("canvas");
      let ctx = cvs.getContext("2d");
      /** 
       * startX: 起始位置的X轴坐标
       * startY: 起始位置的Y轴坐标
       * width: 矩形宽度
       * height: 矩形高度
       * i: 判断鼠标点击是否是第一次点击的标记，0是未点击，1是已点击
       */
      let startX,
        startY,
        width,
        height,
        i = 0;
      /* 
       * 获取canvas当前位置
       * 获取top值时外部父元素要设置样式font-size：0，不然会出现误差！！！！！
       */
      let x = cvs.getBoundingClientRect().left;
      let y = cvs.getBoundingClientRect().top;
      // 画矩形
      let draw = () => {
        ctx.clearRect(0, 0, cvs.width, cvs.height);
        ctx.beginPath();
        ctx.lineWidth = this.lineWidth;
        ctx.strokeStyle = this.color;
        ctx.rect(startX, startY, width, height);
        ctx.stroke();
        ctx.closePath();
        this.coord[0].x = Number((startX / this.widthRatio).toFixed('2'))+0.02;
        this.coord[3].x = Number((startX / this.widthRatio).toFixed('2'))+0.02;
        this.coord[1].x = Number(((startX + width) / this.widthRatio).toFixed('2'))+0.02;
        this.coord[2].x = Number(((startX + width) / this.widthRatio).toFixed('2'))+0.02;
        this.coord[0].y = Number((startY / this.heightRatio).toFixed('2'))+0.02;
        this.coord[1].y = Number((startY / this.heightRatio).toFixed('2'))+0.02;
        this.coord[2].y = Number(((startY + height) / this.heightRatio).toFixed('2'))+0.02;
        this.coord[3].y = Number(((startY + height) / this.heightRatio).toFixed('2'))+0.02;
        this.goBackCoord();
      };
      /* 
       * 获取鼠标第一次点击时的位置，并将该点作为起始点
       * 点击后标记i变为1，为1时开启绘画
       * 鼠标左键弹起后为0，为0时结束绘画
      */
      cvs.onmousedown = e => {
        startX = e.clientX - x;
        startY = e.clientY - y;
        // i === 0 ? (i = 1) : (i = 0);
        i = 1
      };
      // 获取鼠标移动时的位置点，并计算此时矩形的宽高
      cvs.onmousemove = e => {
        if (i === 1) {
          width = e.clientX - x - startX;
          height = e.clientY - y - startY;
          draw();
        }
      };
      cvs.onmouseup = e => {
        i = 0
      }
    }
  },
  mounted() {
    this.DrawRectangular();
    // 当浏览器窗口大小发生改变是对 viewSize 进行重新赋值
    window.onresize = () => {
      return (() => {
        this.viewSize = document.body.clientWidth;
      })();
    };
  },
  updated() {
    
  },
  watch: {
    // 监听 viewSize，当该属性值发生变化时canvas大小也会改变
    viewSize() {
      this.getSize();
    }
  }
};
</script>
