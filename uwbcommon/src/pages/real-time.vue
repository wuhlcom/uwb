<template>
  <div class="real-time">
    <personnel-selection
      @show="navShows"
      :pitchOn.sync="pitchOn">
    </personnel-selection>
    <div class="map">
      <div class="show-selection">
        <selection @msg="nameShowFn" class="selection" msg="显示名字">
        </selection>
        <selection @msg="trackShowFn" class="selection" msg="显示轨迹">
        </selection>
        <!-- <selection @msg="statusShowFn" class="selection" msg="显示状态"></selection> -->
        <selection @msg="areaShowFn" class="selection" msg="显示围栏">
        </selection>
        <selection @msg="gridsShowFn" class="selection" msg="显示标尺">
        </selection>
      </div>
      <div ref="viewBox" class="viewBox">
        <div
          ref="map"
          data-panning="false"
          @mousedown="onPanStart($event)"
          @mousemove="onPanning($event)"
          @mouseup="onPanEnd($event)"
          @wheel="onWheeling($event)"
          @touchstart="onTouchStart($event)"
          @touchmove="onTouchMove($event)"
          @touchend="onTouchEnd($event)"
          id="mapbox"
          class="map-box">
          <img :src="imgUrl">
          <draw-grids
            v-if="isGridsShow"
            class="canvas">
          </draw-grids>
          <area-show
            :rectangle='ratioRectangle'
            :polygon="ratioPolygon"
            class="canvas"
            v-if="isAreaShow">
          </area-show>
          <draw-point
            class="canvas"
            :statusShow="isStatusShow"
            :allPerson="allPerson"
            :a="a"
            :nameShow="isNameShow">
          </draw-point>
          <flickering
            class="canvas"
            :scale="scale"
            :point="allPerson">
          </flickering>
        </div>
        <div v-if="isGridsShow" class="ide">
          标尺：
          <i class="square"></i>
          1m x 1m
        </div>
      </div>  
    </div>
  </div>
</template>
<style lang="scss" scoped>
.real-time {
  display: flex;
  height: 78%;
  margin-top: 50px;
  .map {
    position: relative;
    flex: 1;
    vertical-align: middle;
    margin: 0 40px;
    padding: 70px;
    background: url("../assets/img/map-bk.png");
    background-size: 100% 100%;
    .viewBox{
      width: 100%;
      height: 100%;
      display: flex;
      justify-content: center;
      align-items: center;
      overflow: hidden;
    }
    .show-selection {
      position: absolute;
      top: 20px;
      left: 50px;
      width: 582px;
      height: 54px;
      padding: 22px 25px;
      font-size: 14px;
      // background: rgba(0, 76, 173, 0.9);
      color: #fff;
      z-index: 100;
      .selection {
        margin-right: 36px;
      }
    }
    .map-box {
      position: relative;
      user-select:none;
      // overflow: hidden;
      img {
        width: 100%;
        // height: 100%;
      }
      .canvas {
        position: absolute;
        top: 0;
      }
    }
  }
}
@media screen and (max-height: 820px) {
  .real-time {
    margin-top: 20px;
  }
}
.ide{
  position: absolute;
  bottom: 50px;
  right: 70px;
  height: 20px;
  line-height: 20px;
  color: #fff;
  .square{
    display: inline-block;
    width: 15px;
    height: 15px;
    border: 1px solid #fff;
    vertical-align: middle;
  }
}
</style>
<script>
import DrawPoint from "@/components/draw-point";
import selection from "@/components/selection";
import AreaShow from "@/components/area-show";
import DrawGrids from '@/components/draw-grids'
import PersonnelSelection from "@/components/personnel-selection";
import flickering from '@/components/flickering'
import { WEB_SOCKET_URL } from "../../static/js/websocket-url";
import { accDiv, accSub, accAdd } from "@/assets/js/parseFloat";
export default {
  name: "real-time",
  components: {
    DrawPoint,
    selection,
    AreaShow,
    PersonnelSelection,
    DrawGrids,
    flickering
  },
  data() {
    return {
      navShow: 0, // 侧栏是否显示
      canvasWidth: 0, // canvas的宽
      canvasHeight: 0, // canvas的高
      viewSize: document.body.clientWidth, // 初始化视图窗口大小，这个很重要！！！
      // 地图的实际大小
      actualSize: {
        width: 0,
        height: 0
      },
      // 地图比例
      proportion: {
        width: 0,
        height: 0
      },
      rectangle: [], // 区域划分
      ratioRectangle: [], // 计算比例后的区域
      polygon: [],
      ratioPolygon: [],
      isAreaShow: false, // 区域显示
      isTrackShow: false, // 轨迹显示
      isNameShow: false, // 名字显示
      isGridsShow: false,  // 标尺显示
      isStatusShow: true, // 状态显示
      pitchOn: [],
      a: 0, // 当websockt接收到数据时a加一，位置显示的子组件监听到a变化时对canvas进行重绘
      imgUrl: "",
      imgSize: {
        width: "",
        height: ""
      },
      scale: 1,
    };
  },
  computed: {
    allPerson () {
      return this.$store.getters.newAllpersonArr
    }
  },
  methods: {
    // 拖拽开始
    onPanStart(event) {
      let chart = event.currentTarget;
      chart.dataset.panning = true;
      let lastX = 0,
        lastY = 0,
        lastTf = window.getComputedStyle(chart).transform;
      if (lastTf !== "none") {
        let temp = lastTf.split(",");
        if (!lastTf.includes("3d")) {
          lastX = Number.parseInt(temp[4], 10);
          lastY = Number.parseInt(temp[5], 10);
        } else {
          lastX = Number.parseInt(temp[12], 10);
          lastY = Number.parseInt(temp[13], 10);
        }
      }
      let startX = 0,
        startY = 0;

      if (!event.targetTouches) {
        startX = event.pageX - lastX;
        startY = event.pageY - lastY;
      } else if (event.targetTouches.length === 1) {
        startX = event.targetTouches[0].pageX - lastX;
        startY = event.targetTouches[0].pageY - lastY;
      } else if (event.targetTouches.length > 1) {
        return;
      }
      chart.dataset.panStart = JSON.stringify({
        startX: startX,
        startY: startY
      });
    },
    // 正在拖拽
    onPanning(event) {
      let chart = event.currentTarget;
      if (chart.dataset.panning === "false") {
        return;
      }
      let newX = 0,
        newY = 0,
        panStart = JSON.parse(chart.dataset.panStart),
        startX = panStart.startX,
        startY = panStart.startY;
      if (!event.targetTouches) {
        newX = event.pageX - startX;
        newY = event.pageY - startY;
      } else if (event.targetTouches.length === 1) {
        newX = event.targetTouches[0].pageX - startX;
        newY = event.targetTouches[0].pageY - startY;
      } else if (event.targetTouches.length > 1) {
        return;
      }
      let lastTf = window.getComputedStyle(chart).transform;
      if (lastTf === "none") {
        if (!lastTf.includes("3d")) {
          chart.style.transform =
            "matrix(1, 0, 0, 1, " + newX + ", " + newY + ")";
        } else {
          chart.style.transform =
            "matrix3d(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, " +
            newX +
            ", " +
            newY +
            ", 0, 1)";
        }
      } else {
        let matrix = lastTf.split(",");

        if (!lastTf.includes("3d")) {
          matrix[4] = newX;
          matrix[5] = newY + ")";
        } else {
          matrix[12] = newX;
          matrix[13] = newY;
        }
        chart.style.transform = matrix.join(",");
      }
    },
    // 拖拽结束
    onPanEnd(event) {
      let chart = document.getElementById("mapbox");
      if (chart.dataset.panning === "true") {
        chart.dataset.panning = false
        chart.style.cursor = "default"
      }
    },
    // 设置图形缩放
    setChartScale(chart, newScale) {
      let lastTf = window.getComputedStyle(chart).transform;

      if (lastTf === "none") {
        chart.style.transform = "scale(" + newScale + "," + newScale + ")";
      } else {
        let matrix = lastTf.split(",");
        if (!lastTf.includes("3d")) {
          matrix[0] = "matrix(" + newScale;
          matrix[3] = newScale;
          chart.style.transform =
            lastTf + " scale(" + newScale + "," + newScale + ")";
        } else {
          chart.style.transform =
          lastTf + " scale3d(" + newScale + "," + newScale + ", 1)";
        }
      }
      chart.dataset.scale = newScale;
      this.scale *= newScale
    },
    onWheeling(event) {
      event.preventDefault();
      let newScale = event.deltaY > 0 ? 0.8 : 1.2;
      this.setChartScale(document.getElementById("mapbox"), newScale);
    },
    getPinchDist(event) {
      return Math.sqrt(
        (event.touches[0].clientX - event.touches[1].clientX) *
          (event.touches[0].clientX - event.touches[1].clientX) +
          (event.touches[0].clientY - event.touches[1].clientY) *
            (event.touches[0].clientY - event.touches[1].clientY)
      );
    },
    onTouchStart(event) {
      let chart = document.getElementById("mapbox");
      if (event.touches && event.touches.length === 2) {
        let dist = this.getPinchDist(event);
        chart.dataset.pinching = true;
        chart.dataset.pinchDistStart = dist;
      }
    },
    onTouchMove(event) {
      let chart = document.getElementById("mapbox");
      if (chart.dataset.pinching) {
        let dist = this.getPinchDist(event);
        chart.dataset.pinchDistEnd = dist;
      }
    },
    onTouchEnd(event) {
      let chart = document.getElementById("mapbox");
      if (chart.dataset.pinching) {
        chart.dataset.pinching = false;
        let diff = chart.dataset.pinchDistEnd - chart.dataset.pinchDistStart;
        if (diff > 0) {
          this.setChartScale(chart, 1);
        } else if (diff < 0) {
          this.setChartScale(chart, -1);
        }
      }
    },
    areaShowFn(msg) {
      this.isAreaShow = msg
    },
    /**
     * 轨迹显示
     * 当选择为不显示时，只保留最后一个位置点
     * @param {Boolean} msg
     */
    trackShowFn(msg) {
      this.isTrackShow = msg
      if (!msg) {
        this.allPerson.forEach(e => {
          if (e.point.length) {
            let i = e.point.pop()
            e.point = []
            e.point.push(i)
          }
        })
      }
      this.a++
    },
    gridsShowFn(msg) {
      this.isGridsShow = msg
    },
    nameShowFn(msg) {
      this.isNameShow = msg;
      this.a++
    },
    navShows(msg) {
      this.navShow += msg
    },
    statusShowFn(msg) {
      this.isStatusShow = msg
    },
    // 设置canvas的宽高，宽高等于地图的大小
    setCanvasSize() {
      this.$store.commit('setCanvasWith', this.$refs.map.clientWidth)
      this.$store.commit('setCanvasHeight', this.$refs.map.clientHeight)
      this.canvasWidth = this.$refs.map.clientWidth;
      this.canvasHeight = this.$refs.map.clientHeight;
    },
    // 设置图片大小
    setImgSize(width, height) {
      let boxProportion = (
        this.$refs.viewBox.clientWidth / this.$refs.viewBox.clientHeight
      ).toFixed("1");
      let imgProportion = (width / height).toFixed("1");
      if (imgProportion >= boxProportion) {
        this.imgSize.width = this.$refs.viewBox.clientWidth+'px';
        this.imgSize.height = height*(this.$refs.viewBox.clientWidth/width)+'px';
      } else {
        this.imgSize.height = this.$refs.viewBox.clientHeight+'px';
        this.imgSize.width = width*(this.$refs.viewBox.clientHeight/height)+'px'
      }
    },
    // 获取数据
    getData() {
      this.$http.Post(this.$url.allImg, {status: 1}).catch(rej => {
        this.imgUrl = rej.data.result[0].path
        this.actualSize.width = rej.data.result[0].length
        this.actualSize.height = rej.data.result[0].width
        this.setImgSize(
          rej.data.result[0].picLength,
          rej.data.result[0].picWidth
        )
      })
      this.$http.Post(this.$url.groupFences, {policyType: 1}).catch(rej => {
        rej.data.result.forEach(e => {
          if (e.rectangle) {
            this.rectangle = e.rectangle
          } else if (e.polygon) {
            this.polygon = e.polygon
          }
        })
        this.rectangle.forEach(e => {
          e.points = JSON.parse(e.points)
        })
        this.polygon.forEach(e => {
          e.points = JSON.parse(e.points)
        })
      })
      setTimeout(() => {
        this.ratioCalculation()
        this.webSowcket()
        this.setCanvasSize()
        this.$store.commit('setWidth', {
          actualWidth: this.actualSize.width,
          clientWidth: this.$refs.map.clientWidth
        })
        this.$store.commit('setHeight', {
          actualHeight: this.actualSize.height,
          clientHeight: this.$refs.map.clientHeight
        })
      }, 1000)
    },
    getWebSocketURL() {
      if (window.location.port === '9999') {
        return 'ws://192.168.10.232:80/coordinate/coors';
      }
      return ((window.location.protocol === 'https:') ? 'wss://' : 'ws://') + window.location.host + '/uwb/websocket/wsmsg'
    },
    webSowcket() {
      let ws = new WebSocket(this.getWebSocketURL())
      ws.onopen = () => {
        ws.send('{"type":1}')
      };
      ws.onmessage = msg => {
        let data = JSON.parse(msg.data).result
        data.forEach(e => {
          this.allPerson.forEach(el => {
            if (e.tagId == el.tagId) {
              let point = {
                x: e.posX,
                y: e.posY,
                type: e.type
              };
              el.positionName = e.positionName
              el.levelName = e.levelName
              // 心率值为255时显示 ‘--’
              if(e.heartRate === 255) {
                el.heartRate = '--'
              } else {
                el.heartRate = e.heartRate
              }
              // 电量值为255时显示 ‘--’
              if (e.power === 255) {
                el.power = '--'
              } else {
                el.power = e.power
              }
              /* 
               * 轨迹点最多保存60个，当等于60个时删除第一个并增加一个点
               * isTrackShow用来判断是否显示轨迹
               */
              if (this.isTrackShow) {
                if (el.point.length < 60) {
                  el.point.push(point)
                } else {
                  el.point.shift()
                  el.point.push(point)
                }
              } else {
                el.point.splice(0, 60)
                el.point.push(point)
              }
              // 名字的显示位置
              el.namePosition = {
                x: e.posX * this.proportion.width,
                y: e.posY * this.proportion.height,
                type: e.type
              }
            }   
          })
        })
        this.a++
      }
      // 路由跳转时结束websocket链接
      this.$router.afterEach(function() {
        ws.close()
      })
    },
    // 比例计算
    ratioCalculation() {
      // 图片的长宽除以地图的实际长宽获取比例
      this.proportion.width = accDiv(
        this.$refs.map.clientWidth,
        this.actualSize.width
      );
      this.proportion.height = accDiv(
        this.$refs.map.clientHeight,
        this.actualSize.height
      )
      // 对后端传过来的区域划分对象进行深拷贝
      let arr = JSON.stringify(this.rectangle)
      this.ratioRectangle = JSON.parse(arr)
      this.ratioRectangle.forEach(e => {
        e.points.forEach(el => {
          el.x = el.x * this.proportion.width;
          el.y = el.y * this.proportion.height;
        })
        /*
         * 区域名称显示的位置
         * X轴等于：区域的起始点的X轴加上最大的X轴减去最小的X轴除以2的值，Y轴计算同理
         */
        e.textx = accAdd(
          accDiv(accSub(e.points[0].x, e.points[1].x), 2),
          e.points[0].x
        )
        e.texty = accAdd(
          accDiv(accSub(e.points[0].y, e.points[2].y), 2),
          e.points[0].y
        )
      })
      this.ratioPolygon = JSON.parse(JSON.stringify(this.polygon))
      this.ratioPolygon.forEach(e => {
        e.points.forEach(el => {
          el.x = el.x * this.proportion.width;
          el.y = el.y * this.proportion.width;
        })
      })
    },
  },
  mounted() {
    this.getData()
  },
  updated() {
    // 当浏览器窗口大小发生改变是对 viewSize 进行重新赋值
    window.onresize = () => {
      return (() => {
        this.viewSize = document.body.clientWidth;
      })()
    }
  },
  watch: {
    // 监听 viewSize，当该属性值发生变化时canvas大小也会改变
    viewSize() {
      this.ratioCalculation()
      this.setCanvasSize()
    },
    /* 
     * 监听左侧导航栏是否显示，当侧栏不显示时，图片展示区域会放大，这是需要改变canvas的大小
     */
    navShow() {
      // 如果不使用setTimeout的话，当图片变化时canvas的大小赋值会不同步
      setTimeout(() => {
        this.ratioCalculation()
        this.setCanvasSize()
      }, 0)
    },
    pitchOn (val) {
      this.a++
      this.allPerson.forEach(e => {
        e.show = false
      })
      val.forEach(e => {
        this.allPerson.forEach(el => {
          if (e === el.personCode) {
            el.show = true
          }
        })
      })
    }, 
  }
}
</script>
