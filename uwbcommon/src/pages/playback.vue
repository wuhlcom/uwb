<template>
  <div class="playback">
    <personnel-selection
      @show="navShows"
      :pitchOn.sync="getDataObj.personCodes"
      >
    </personnel-selection>
    <div class="map">
      <div class="show-selection">
      <selection
        @msg="nameShows"
        class="selection"
        msg="显示名字">
      </selection>
      <selection
        @msg="trackShows"
        class="selection"
        msg="显示轨迹">
        </selection>
      <!-- <selection class="selection" msg="显示状态"></selection> -->
      <selection 
        class="selection"
        @msg="areaShows"
        msg="显示围栏">
      </selection>
      <div class="time">
        <span class="title">开始时间</span>
        <el-date-picker
          v-model="startTime"
          type="datetime"
          :picker-options="pickerBeginDateBefore"
          placeholder="选择日期时间">
        </el-date-picker>
        <span class="title">结束时间</span>
        <el-date-picker
          v-model="endTime"
          type="datetime"
          :picker-options="pickerBeginDateAfter"
          placeholder="选择日期时间">
        </el-date-picker>
      </div>
    </div> 
      <div ref="viewBox" class="viewBox">
        <div
          ref="map"
          data-panning="false"
          :style="imgSize"
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
          <area-show
            :rectangle='ratioRectangle'
            :polygon="ratioPolygon"
            class="canvas"
            v-if="areaShow">
          </area-show>
          <draw-playback
            v-if="drawShow"
            class="canvas"
            ref="draw"
            :position="personPosition"
            :startTime="drawStartTime"
            :endTime="drawEndTime"
            :proportion="proportion"
            :nameShow="nameShow"
            :trackShow="trackShow"
            :multiple="multiple"
            :personArr="getDataObj.personCodes"
            :canvasWidth="canvasWidth"
            :canvasHeight="canvasHeight">
            </draw-playback>
        </div>
      </div>
      <div class="player">
        <time-line class="time-line"></time-line>
        <div class="but">
          <i @click="getPositionData" v-if="!start" class="iconfont icon-weibiaoti518"></i>
          <i @click="stop" v-if="start" class="iconfont icon-weibiaoti519"></i>
          <i @click="speed" class="iconfont icon-ai19"></i>
          <i @click="empty" class="iconfont icon-ai08"></i>
          <span class="rate">x{{multiple}}</span>
        </div>
      </div>
    </div>
  </div>
</template>
<style lang="scss" scoped>
  .playback{
    display: flex;
    height: 78%;
    margin-top: 50px;
    .show-selection{
      position: absolute;
      top: 20px;
      left: 50px;
      height: 54px;
      padding: 22px 25px;
      font-size: 14px;
      color: #fff;
      .selection {
        margin-right: 36px;
      }
      i{
        margin: 0 5px;
        vertical-align: middle;
        font-size: 24px;
      }
      .icon-ai08{
        font-size: 20px;
      }
    }
    .map {
      position: relative;
      flex: 1;
      display: flex;
      justify-content: center;
      align-items: center;
      text-align: center;
      vertical-align: middle;
      margin: 0 40px;
      padding: 110px;
      background: url('../assets/img/map-bk.png');
      background-size: 100% 100%;
      .viewBox{
        width: 100%;
        height: 100%;
        display: flex;
        justify-content: center;
        align-items: center;
        overflow: hidden;
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
  }
  .time{
    display: inline-block;
    .title{
      margin-right: 10px;
    }
  }
  @media screen and (max-height: 820px) {
    .playback {
      margin-top: 20px;
    }
  }
   @media screen and (max-width: 1693px) {
    .player{
      margin-top: 20px;
    }
    .map{
      padding: 100px;
    }
  }
  @media screen and (max-width: 1529px) {
    .time{
      margin-top: 20px;
      .map{
        padding: 150px;
      }
    }
  }
  .el-date-editor{
    .el-input{
      width: 180px !important;
    }
  }
  .el-input{
    margin-right: 15px !important;
  }
  .iconfont:active{
    color: #0f0;
  }
  .iconfont{
    cursor: pointer;
  }
  .player{
    position: absolute !important;
    bottom: 55px;
    display: inline-block;
    width: 600px;
    user-select: none;
    .but{
      position: relative;
      margin-top: 10px; 
      color: #fff;
      .iconfont{
        font-size: 30px;
      }
      .rate{
        position: absolute;
        bottom: -15px;
        right: 290px;
        width: 30px;
        text-align: center;
      }
    }
  }
</style>
<script>
import DrawPlayback from '@/components/draw-playback'
import selection from "@/components/selection";
import AreaShow from "@/components/area-show";
import PersonnelSelection from "@/components/personnel-selection";
import TimeLine from '@/components/time-line'
import { accDiv, accSub, accAdd } from "@/assets/js/parseFloat";
import { mapState } from 'vuex'
export default {
  name: "real-time",
  components: {
    DrawPlayback,
    selection,
    AreaShow,
    PersonnelSelection,
    TimeLine
  },
  data() {
    return {
      drawShow: false,
      startTime: '',
      endTime: '',
      playShow: false,
      drawStartTime: 0,
      drawEndTime: 0,
      multiple: 1,
      navShow: 0, // 侧栏是否显示
      canvasWidth: 0, // canvas的宽
      canvasHeight: 0, // canvas的高
      viewSize: document.body.clientWidth, // 初始化视图窗口大小，这个很重要！！！
      actualSize: {
        // 地图的实际大小
        width: 0,
        height: 0
      },
      proportion: {
        // 地图比例
        width: 0,
        height: 0
      },
      canvasShow: false, // cnvas是否显示
      allPerson: [], // 所有人员信息
      area: {}, // 区域划分
      ratioRectangle: [], // 计算比例后的区域
      rectangle: [],
      polygon: [],
      ratioPolygon: [],
      areaShow: false,
      trackShow: false,
      nameShow: false,
      statusShow: true,
      personPosition: [],
      imgUrl: "",
      imgSize: {
        width: "",
        height: ""
      },
      getDataObj: {
        personCodes: [],
        startTime: '',
        endTime: '',
      },
      // 开始时间必须小于等于结束时间，或者小于等于当天
      pickerBeginDateBefore:{
        disabledDate: (time) => {
          if (this.endTime !== "") {
              return time.getTime() > Date.now() || time.getTime() > this.endTime;
          } else {
            return time.getTime() > Date.now();
          }
        }
      },
      // 结束时间必须小于等于当天，大于开始时间
      pickerBeginDateAfter:{
        disabledDate: (time) => {
          return time.getTime() < this.startTime-1000*60*60*24 || time.getTime() > Date.now();
        }
      }
    }
  },
  computed: {
    ...mapState({
      start: state => state.playback.play
    })
  },
  methods: {
    navShows(msg) {
      setTimeout(() => {
        this.ratioCalculation()
        this.setCanvasSize()
      }, 0)
    },
    speed () {
      if (this.multiple<16) {
        this.multiple *= 2
      } else {
        this.multiple = 1
      }
    },
    stop () {
      this.$refs.draw.stop()
      this.$store.commit('setPlay', false)
    },
    empty () {
      this.multiple = 1
      this.drawShow = false
      this.$store.commit('setPlay', false)
      this.$store.commit('reset')
      this.$refs.draw.stop()
      this.startTime = ''
      this.endTime = ''
    },
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
    areaShows(msg) {
      this.areaShow = msg;
    },
    trackShows(msg) {
      this.trackShow = msg;
    },
    nameShows(msg) {
      this.nameShow = msg;
    },
    navShows(msg) {
      this.navShow += msg;
    },
    statusShows(msg) {
      this.statusShow = msg;
    },
    // 设置canvas的宽高，宽高等于地图的大小
    setCanvasSize() {
      this.canvasWidth = 0
      this.canvasHeight = 0
      this.canvasWidth = this.$refs.map.clientWidth;
      this.canvasHeight = this.$refs.map.clientHeight;
      this.$store.commit('setCanvasWith', this.$refs.map.clientWidth)
      this.$store.commit('setCanvasHeight', this.$refs.map.clientHeight)
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
    getPositionData() {
      if (this.personPosition.length) {
        // this.$store.commit('setPlayTimeLength', 0)
        this.$refs.draw.animation()
        this.$store.commit('setPlay', true)
      } else {
        if (this.startTime === '' || this.endTime === '') {
          this.$message({
            type: "error",
            message: "请选择时间范围!"
          });
        } else if (this.startTime === null || this.endTime === null) {
          this.$message({
            type: "error",
            message: "请选择时间范围!"
          })
        } else if (this.endTime <= this.startTime) {
          this.$message({
            type: "error",
            message: "结束时间必须大于起始时间!"
          })
        } else if (this.startTime >= Date.now()) {
          this.$message({
            type: "error",
            message: "请输入正确的起始时间!"
          })
        } else if (!this.getDataObj.personCodes.length) {
          this.$message({
            type: "error",
            message: "请选择人员!"
          })
        } else {
          this.getDataObj.startTime = Date.parse(this.startTime) / 1000;
          this.getDataObj.endTime = Date.parse(this.endTime) / 1000;
          this.$http
          .Post(this.$url.coordinates, this.getDataObj)
          .then(res => {})
          .catch(rej => {
            if (rej.data.errcode === 10001) {
              this.personPosition = rej.data.coordinates
              this.personPosition.forEach(e => {
                e.point=[]
                e.namePosition={}
              })
              // 设置开始时间和结束时间
              this.$store.commit('setStartTime', rej.data.startTime)
              this.$store.commit('setEndTime', rej.data.endTime)
              this.$store.commit('setPlay', true)
              this.drawShow = true
            } else {
              this.$message({
                type: "error",
                message: rej.data.msg
              })
            }
          })
        }
      }  
    },
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
      this.$http.Post(this.$url.groupFences, {policyType: 0}).catch(rej => {
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
        this.setCanvasSize()
        this.canvasShow = true
      }, 1000)
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
      );
      // 对后端传过来的区域划分对象进行深度拷贝
      let arr = JSON.stringify(this.rectangle);
      this.ratioRectangle = JSON.parse(arr);
      this.ratioRectangle.forEach(e => {
        e.points.forEach(el => {
          el.x *= this.proportion.width;
          el.y *= this.proportion.height;
        });
        /*
        * 区域名称显示的位置
        * X轴等于：区域的起始点的X轴加上最大的X轴减去最小的X轴除以2的值，Y轴计算同理
        */
        e.textx = accAdd(
          accDiv(accSub(e.points[0].x, e.points[1].x), 2),
          e.points[0].x
        );
        e.texty = accAdd(
          accDiv(accSub(e.points[0].y, e.points[2].y), 2),
          e.points[0].y
        );
      })
      this.ratioPolygon = JSON.parse(JSON.stringify(this.polygon))
      this.ratioPolygon.forEach(e => {
        e.points.forEach(el => {
          el.x = el.x * this.proportion.width;
          el.y = el.y * this.proportion.width;
        })
      })
    }
  },
  created () {
    this.getData()
  },
  mounted() {
  },
  updated() {
    // 当浏览器窗口大小发生改变是对 viewSize 进行重新赋值
    window.onresize = () => {
      return (() => {
        this.viewSize = document.body.clientWidth;
      })();
    };
  },
  watch: {
    // 监听 viewSize，当该属性值发生变化时canvas大小也会改变
    viewSize() {
      this.ratioCalculation();
      this.setCanvasSize();
    },
    /* 
      * 监听左侧导航栏是否显示，当侧栏不显示时，图片展示区域会放大，这是需要改变canvas的大小
      */
    navShow() {
      // 如果不使用setTimeout的话，当图片变化时canvas的大小赋值会延迟
      setTimeout(() => {
        this.ratioCalculation();
        this.setCanvasSize();
      }, 0);
    },
    startTime () {
      this.personPosition=[]
      this.$store.commit('setPlayTimeLength', 0)
    },
    endTime () {
      this.personPosition=[]
      this.$store.commit('setPlayTimeLength', 0)
    },
  }
};
</script>
