<template>
  <div class="draw-polygon">
    <canvas id="polygon" :width="canvasWidth" :height="canvasHeight">
    </canvas>
  </div>
</template>
<style lang="scss" scoped>
.draw-polygon {
  width: 100%;
  height: 100%;
}
</style>
<script>
export default {
  name: "draw-polygon",
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
      arr: [],
      isColse: false,
      isOrigin: false
    };
  },
  methods: {
    goBackCoord() {
      let arr = JSON.parse(JSON.stringify(this.arr));
      arr.forEach(e => {
        (e.x = Number((e.x / this.widthRatio).toFixed(2))),
          (e.y = Number((e.y / this.heightRatio).toFixed(2)));
      });
      this.$emit("coord", arr);
    },
    crossMul(v1, v2) {
      return v1.x * v2.y - v1.y * v2.x;
    },
    checkCross(p1, p2, p3, p4) {
      var v1 = { x: p1.x - p3.x, y: p1.y - p3.y },
        v2 = { x: p2.x - p3.x, y: p2.y - p3.y },
        v3 = { x: p4.x - p3.x, y: p4.y - p3.y },
        v = this.crossMul(v1, v3) * this.crossMul(v2, v3);
      v1 = { x: p3.x - p1.x, y: p3.y - p1.y };
      v2 = { x: p4.x - p1.x, y: p4.y - p1.y };
      v3 = { x: p2.x - p1.x, y: p2.y - p1.y };
      return v <= 0 && this.crossMul(v1, v3) * this.crossMul(v2, v3) <= 0
        ? true
        : false;
    },
    polygon() {
      let canvas = document.getElementById("polygon");

      let positionX = canvas.getBoundingClientRect().left;
      let positionY = canvas.getBoundingClientRect().top;

      if (canvas.getContext) {
        var ctx = canvas.getContext("2d");

        function draw(array, close) {
          ctx.clearRect(0, 0, canvas.width, canvas.height);
          if (!array.length) return;
          ctx.beginPath();
          ctx.lineWidth = 1;
          ctx.strokeStyle = "#f00";
          ctx.moveTo(array[0].x, array[0].y);
          if (array.length > 0) {
            for (let i = 1; i < array.length; i++) {
              ctx.lineTo(array[i].x, array[i].y);
            }
          }
          if (close) {
            ctx.closePath();
          }
          ctx.stroke();
          array.forEach(e => {
            ctx.beginPath();
            ctx.arc(e.x, e.y, 3, 0, Math.PI * 2, true);
            ctx.fillStyle = "#f00";
            ctx.fill();
          });
        }

        canvas.addEventListener("click", e => {
          if (this.isColse) return;
          let x = e.clientX - positionX;
          let y = e.clientY - positionY;
          // 判断点击位置是否是起点，
          // 是起点的话就进行闭合操作
          // 不是的话就将位置点推入arr数组
          if (!this.isOrigin) {
            this.arr.push({ x: x, y: y });
            if (this.arr.length > 3) {
              let length = this.arr.length;
              let isCheckCross;
              for (let i = 0; i < this.arr.length - 3; i++) {
                isCheckCross = this.checkCross(
                  this.arr[i],
                  this.arr[i + 1],
                  this.arr[length - 2],
                  this.arr[length - 1]
                );
                if (isCheckCross) {
                  this.arr.pop();
                  draw(this.arr, false);
                  ctx.font = "12px serif";
                  ctx.textAlign = "center";
                  ctx.textBaseline = "center";
                  ctx.fillStyle = "#f00";
                  ctx.fillText("不能相交", x, y);
                } else {
                  draw(this.arr, false);
                }
              }
            } else {
              draw(this.arr, false);
            }
          } else {
            this.isColse = true;
            draw(this.arr, true);
          }
        });

        canvas.addEventListener("mousemove", e => {
          if (this.arr.length < 1) return;

          let x = e.clientX - positionX;
          let y = e.clientY - positionY;
          // 判断鼠标位置是否在起点附近
          // 是的话就显示标识框
          if (
            (x - this.arr[0].x) * (x - this.arr[0].x) +
              (y - this.arr[0].y) * (y - this.arr[0].y) <=
            50
          ) {
            this.isOrigin = true;
            ctx.strokeRect(this.arr[0].x - 6, this.arr[0].y - 6, 12, 12);
          } else {
            this.isOrigin = false;
            if (this.isColse) {
              draw(this.arr, true);
            } else {
              draw(this.arr, false);
            }
          }
        });

        // 这一步是为了阻止右击时系统默认的弹出框
        canvas.addEventListener("contextmenu", e => {
          e.preventDefault();
        });

        canvas.addEventListener("mouseup", oEvent => {
          if (!oEvent) oEvent = window.event;
          if (oEvent.button == 2 && this.arr.length > 0) {
            // 判断是否已经进行了闭合操作
            // 是的话就取消闭合操作
            // 不是的话就删除arr数组中的最后一个位置点
            if (this.isColse) {
              this.isColse = false;
              draw(this.arr, false);
            } else {
              this.arr.pop();
              draw(this.arr, false);
            }
          }
        });
      }
    }
  },
  mounted() {
    this.polygon();
  },
  watch: {
    arr() {
      this.goBackCoord();
    }
  }
};
</script>
