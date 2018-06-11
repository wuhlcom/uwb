<template>
  <div class="panorama">
    <alert v-if="alertShow" @hide="tohide"></alert>
    <warning-alert v-show="warningShow" @warning="warning" class="warning"></warning-alert>
    <crewalert v-if="crewShow" @cl113se="crew"></crewalert>
    <v-header :actShow="true" :praShow="false" :bellShow="true" @out="tohide" class="header"></v-header>
    <div class="title">
      <div>一监区>首页</div>
      <v-time class="fl113ors-time"></v-time>
    </div>
    <div class="view">
      <div class="color-scheme">
        <ul>
          <li class="scheme">
            <i class="orange"></i>监仓403</li>
          <li class="scheme">
            <i class="green"></i>监仓302</li>
          <li class="scheme">
            <i class="purple"></i>监仓303</li>
          <li class="scheme">
            <i class="blue"></i>监仓304</li>
          <li class="scheme">
            <i class="yellow"></i>监仓205</li>
        </ul>
      </div>
      <div class="view-img">
        <img ref="size" src="./jiancang5.png">
        <div v-if="canvasShow" class="dian" @click="goCell($event)">
          <div class="cell403 cell" :style="{width:w+'px', height:h+'px', top:t6+'px', left:l113+'px'}">
            <canvas id="cell403" @load="ratio" :width="w" :height="h" :data-ins="cellCode[0]" :data-id="cellId[0]">
            </canvas>
            <div>监仓{{cellId[0]}}</div>
          </div>
          <div class="cell302 cell" :style="{width:w+'px', height:h+'px', top:t256+'px', left:l5+'px'}">
            <canvas id="cell302" :width="w" :height="h" :data-ins="cellCode[2]" :data-id="cellId[2]">
            </canvas>
            <div>监仓{{cellId[2]}}</div>
          </div>
          <div class="cell303 cell" :style="{width:w+'px', height:h+'px', top:t256+'px', left:l113+'px'}">
            <canvas id="cell303" :width="w" :height="h" :data-ins="cellCode[3]" :data-id="cellId[3]">
            </canvas>
            <div>监仓{{cellId[3]}}</div>
          </div>
          <div class="cell304 cell" :style="{width:w+'px', height:h+'px', top:t256+'px', left:l220+'px'}">
            <canvas id="cell304" :width="w" :height="h" :data-ins="cellCode[4]" :data-id="cellId[4]">
            </canvas>
            <div>监仓{{cellId[4]}}</div>
          </div>
          <div class="cell205 cell" :style="{width:w+'px', height:h+'px', top:t506+'px', left:l326+'px'}">
            <canvas id="cell205" :width="w" :height="h" :data-ins="cellCode[1]" :data-id="cellId[1]">
            </canvas>
            <div>监仓{{cellId[1]}}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<style lang="scss" scoped>
.panorama {
  background: #f1f3f6;
  height: 100%;
  overflow: auto;
  .header {
    display: flex;
    align-items: center;
    height: 6.3%;
    min-height: 40px;
  }
  .title {
    display: flex;
    position: relative;
    padding-left: 20px;
    width: 100%;
    height: 4.6%;
    align-items: center;
    font-size: 14px;
    color: #000;
    font-weight: 900;
    .fl113ors-time {
      position: absolute;
      right: 20px;
      font-size: 14px;
      color: #409eff;
    }
  }
  .view {
    position: absolute;
    top: 11%;
    right: 20px;
    bottom: 20px;
    left: 20px;
    background: #fff;
    min-width: 1340px;
    min-height: 532px;
    .view-img {
      position: relative;
      width: 21%;
      height: auto;
      margin: 20px auto;
      img {
        width: 100%;
      }
      .dian {
        position: absolute;
        top: 0;
        display: flex;
        flex-direction: column;
        width: 100%;
        height: 100%;
        .cell {
          position: absolute;
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 13px;
          canvas {
            position: absolute;
            top: 0;
            left: 0;
          }
        }
      }
    }
    .color-scheme {
      position: absolute;
      top: 10px;
      right: 10px;
      .scheme {
        margin: 16px;
        font-size: 13px;
        i {
          display: inline-block;
          width: 10px;
          height: 10px;
          margin-right: 10px;
          border-radius: 50%;
        }
        .orange {
          background: #fb1e1e;
        }
        .green {
          background: #67c23a;
        }
        .purple {
          background: #d814c4;
        }
        .blue {
          background: #0943d7;
        }
        .yellow {
          background: #efb400;
        }
      }
    }
  }
  .warning {
    position: absolute;
    right: 0;
    bottom: 0;
    z-index: 100;
    box-shadow: -3px -3px 10px 1px #888;
  }
}
</style>
<script>
import VHeader from "com/header/header";
import Alert from "com/alert/alert";
import VTime from "com/time/time";
import Statistics from "com/statistics/statistics";
import Absenteeism from "com/absenteeism/absenteeism";
import Pie from "com/pie/pie";
import Crewalert from "com/crewalert/crewalert";
import WarningAlert from "com/warningalert/warningalert";
import { accDiv, accMul } from "@/assets/js/parseFloat";
import { PRISON_API, WEB_SOCKET_PAI } from "../../api/api";

export default {
  name: "panorama",
  components: {
    VHeader,
    VTime,
    Statistics,
    Absenteeism,
    Alert,
    Pie,
    Crewalert,
    WarningAlert
  },
  data() {
    return {
      routerChange: false,
      warningShow: false,
      alertShow: false,
      crewShow: false,
      l113: 0,
      l5: 0,
      l220: 0,
      l326: 0,
      t6: 0,
      t256: 0,
      t506: 0,
      h: 0,
      w: 0,
      widthPpt: 0,
      heightPpt: 0,
      cellCode: [],
      potInfo: [[], [], [], [], []],
      canvasShow: false,
      count: 0,
      cellId: [],
      i: 0
    };
  },
  methods: {
    warning(msg) {
      this.warningShow = msg;
    },
    // 退出窗口弹出
    tohide(msg) {
      this.alertShow = msg;
    },
    // 电子点名窗口弹出或关闭
    crew(msg) {
      this.crewShow = msg;
    },
    // 各个监仓的大小和定位
    ratio() {
      let w = this.$refs.size.clientWidth;
      let h = this.$refs.size.clientHeight;
      this.w = accMul(w, 104 / 435);
      this.h = accMul(h, 240 / 752);
      this.l113 = accMul(w, 113 / 435);
      this.l5 = accMul(w, 5 / 435);
      this.l220 = accMul(w, 220 / 435);
      this.l326 = accMul(w, 326 / 435);
      this.t6 = accMul(h, 6 / 752);
      this.t256 = accMul(h, 256 / 752);
      this.t506 = accMul(h, 506 / 752);
      this.widthPpt = accDiv(this.w, 2.675);
      this.heightPpt = accDiv(this.h, 4.385);
    },
    // 跳转页面
    goCell(e) {
      let event = e.target;
      if (event.nodeName === "CANVAS") {
        let num = event.getAttribute("data-ins");
        let id = event.getAttribute("data-id");
        this.$router.push({ name: "cell", query: { num: num, id: id } });
      }
    },
    // 位置点
    drawCanvas(id, arr) {
      let cvs = document.getElementById(id);
      // 获取当前canvas的位置
      let x = cvs.getBoundingClientRect().left;
      let y = cvs.getBoundingClientRect().top;
      let ctx = cvs.getContext("2d");
      // 图形绘制
      function draw(a) {
        ctx.clearRect(0, 0, cvs.width, cvs.height);
        a.forEach(e => {
          // 根据犯人所在监仓显示不同颜色，此处为圆点的颜色
          let color = "";
          switch (e.areaCode) {
            case "0101041000":
              color = "#fb1e1e";
              break;
            case "0101031100":
              color = "#0943d7";
              break;
            case "0101030900":
              color = "#67c23a";
              break;
            case "0101031000":
              color = "#d709c1";
              break;
            case "0101021200":
              color = "#efb400";
              break;
          }
          // 获取当前文本的宽度，可自适应的设置文字背景
          let textWidth = ctx.measureText(e.name).width;
          ctx.beginPath();
          ctx.fillStyle = color;
          ctx.arc(e.x, e.y, 5, 0 * Math.PI, 2 * Math.PI);
          ctx.fill();
          ctx.closePath();
          if (e.isHover) {
            let x = 0,
              y = 0,
              tx = 0,
              ty = 0; 
            ctx.beginPath();
            ctx.fillStyle = color;
            if (e.x - textWidth <= 0) {
              x = 10;
              tx = textWidth / 2 + 12;
            } else if (e.x + textWidth >= cvs.width) {
              x = e.x - textWidth;
              tx = e.x - textWidth / 2;
            } else {
              x = e.x - textWidth / 2 - 1;
              tx = e.x;
            }
            if (e.y - 20 <= 0) {
              y = e.y + 11;
              ty = e.y + 20;
            } else if (e.y + 20 >= cvs.height) {
              y = e.y - 21;
              ty = e.y - 10;
            } else {
              y = e.y - 21;
              ty = e.y - 10;
            }
            ctx.fillRect(x, y, textWidth + 3, 15);
            ctx.fill();
            ctx.closePath();
            ctx.beginPath();
            ctx.font = "12px serif";
            ctx.fillStyle = "#fff";
            ctx.textAlign = "center";
            ctx.fillText(e.name, tx, ty);
            ctx.closePath();
          }
        });
      }
      draw(arr);
      // hover显示名字
      cvs.onmousemove = e => {
        let hasHover = false;
        for (let i = arr.length - 1; i > -1; i--) {
          let c = arr[i];
          if (
            (e.clientX - x - c.x) * (e.clientX - x - c.x) +
              (e.clientY - y - c.y) * (e.clientY - y - c.y) <=
              5 * 5 &&
            !hasHover
          ) {
            c.isHover = true;
            hasHover = true;
            draw(arr);
          } else {
            if (c.isHover) {
              c.isHover = false;
              draw(arr);
            }
          }
        }
      };
    },
    // post请求获取数据，并提取监仓编号
    getCellInfo() {
      this.$http
        .Post(PRISON_API.panorama, {})
        .then(res => {})
        .catch(rej => {
          if (rej.data.errcode == 10001) {
            let data = rej.data.result;
            this.$store.state.warningAmount = data.warningAmount;
            data.areas.forEach(e => {
              this.cellCode.push(e.areaCode);
              this.cellId.push(e.areaName.slice(2));
            });
          }
          this.websocket();
        });
    },
    // websocket实时获取犯人位置信息
    websocket() {
      let ws = new WebSocket(WEB_SOCKET_PAI);
      ws.onopen = () => {
        // Web Socket 已连接上，使用 send() 方法发送数据
        ws.send('{"type": 1, "dataType": 2}');
        console.log("已连接");
      };
      ws.onmessage = evt => {
        let msg = JSON.parse(evt.data).result;
        msg.forEach(e => {
          e.prisoners.forEach(evt => {
            evt.isHover = false;
            evt.x = accMul(evt.posX, this.widthPpt);
            evt.y = accMul(evt.posY, this.heightPpt);
          });
          switch (e.areaCode) {
            case "0101041000":
              this.potInfo[0] = e.prisoners;
              break;
            case "0101021200":
              this.potInfo[4] = e.prisoners;
              break;
            case "0101030900":
              this.potInfo[1] = e.prisoners;
              break;
            case "0101031000":
              this.potInfo[2] = e.prisoners;
              break;
            case "0101031100":
              this.potInfo[3] = e.prisoners;
              break;
            default:
              break;
          }   
        });
        this.i++;
      };
      ws.onclose = function() {
        // 关闭 websocket
      };
      // 路由跳转时结束websocket链接
      this.$router.afterEach(function() {
        ws.close();
      });
    }
  },
  mounted() {
    /*  
    因为本页面的canvas大小的依赖于背景图
    所以要在img加载完成之后再渲染canvas部分
   */
    let imgs = document.querySelectorAll("img");
    Array.from(imgs).forEach(item => {
      let img = new Image();
      img.onload = () => {
        this.count++;
      };
      img.src = item.getAttribute("src");
    });
    // 自适应缩放
    window.onresize = () => {
      this.ratio();
      this.drawCanvas("cell403", this.potInfo[0]);
      this.drawCanvas("cell302", this.potInfo[1]);
      this.drawCanvas("cell303", this.potInfo[2]);
      this.drawCanvas("cell304", this.potInfo[3]);
      this.drawCanvas("cell205", this.potInfo[4]);
    };
  },
  watch: {
    // 位置更新重新渲染所有的画布
    i(val) {
      this.drawCanvas("cell403", this.potInfo[0]);
      this.drawCanvas("cell302", this.potInfo[1]);
      this.drawCanvas("cell303", this.potInfo[2]);
      this.drawCanvas("cell304", this.potInfo[3]);
      this.drawCanvas("cell205", this.potInfo[4]);
    },
    // img加载完成之后再渲染canvas
    count(val) {
      if (val == 1) {
        this.ratio();
        this.canvasShow = true;
        this.getCellInfo();
      }
    }
  }
};
</script>
