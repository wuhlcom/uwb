<template>
  <div class="add-area">
    <div @click="close" class="close">x</div>
    <div class="head">
      <div class="title">新增区域</div>
      <div class="new-shape">
        <el-select
          v-model="shape"
          placeholder="请选择新建区域形状"
          class="med-box">
            <el-option
              v-for="item in shapeSelection"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
        </el-select>
      </div>
      <div class="area-name must">
        区域名称：
        <input v-model="fenceName" type="text">
        <span v-if="vacancy">请输入区域名称</span>
      </div>
      <div class="describe">
        区域描述：
        <input v-model="remark" type="text">
      </div>
      <div class="position" v-if="shape===3">
        <div class="origin">
          起始坐标：
          x<input v-model="coord[0].x" type="number" disabled min="0">
          y<input v-model="coord[0].y" type="number" disabled min="0">
        </div>
        <div class="end">
          对角坐标：
          x<input v-model="coord[2].x" type="text" disabled>
          y<input v-model="coord[2].y" type="text" disabled>
        </div>
        <span v-if="area">请选择区域</span>
      </div>
    </div>
    <div class="img-view">
      <div class="box" ref="viewBox">
        <div :style="imgSize" ref="map" class="img-box">
        <img :src="imgUrl">
        <area-show
          :rectangle='ratioRectangle'
          :polygon="ratioPolygon"
          v-if="ratioRectangle.length||ratioPolygon.length"
          class="canvas">
        </area-show>
        <draw-rectangular
          v-if="shape===3"
          @coord="getCoord"
          :widthRatio="widthRatio"
          :heightRatio="heightRatio"
          class="canvas"
          :canvasWidth="canvasWidth"
          :canvasHeight="canvasHeight"
          >
        </draw-rectangular>
        <draw-polygon
          v-if="shape===0"
           @coord="getCoord"
          :widthRatio="widthRatio"
          :heightRatio="heightRatio"
          class="canvas"
          :canvasWidth="canvasWidth"
          :canvasHeight="canvasHeight"
          >
        </draw-polygon>
      </div>
      </div>
    </div>
    <div class="but">
      <button @click="addFence" class="up">确定</button>
      <button @click="close">取消</button>
    </div>
  </div>
</template>
<style lang="scss" scoped>
  $color: #59b3ff;
  .add-area{
    position: relative;
    padding-top: 20px; 
    background: url('../assets/img/map-bk.png');
    background-size: 100% 100%;
    font-size: 0;
    .close{
      position: absolute;
      top: 15px;
      right: 50px;
      width: 18px;
      height: 18px;
      font-size: 28px;
      color: $color;
      user-select:none;
      cursor: pointer;
    }
    .head{
      line-height: 30px;
      padding-left: 40px;
      .title{
        margin-bottom: 20px;
        float: left;
        color: $color;
        font-size: 14px;
      }
      .new-shape{
        margin-bottom: 20px;
        height: 30px;
        margin-left: 45px;
        float: left;
        .shape{
          height: 26px;
          border: none;
          color: $color;
          background: #0053c1;
          margin-right: 19px;
        }
        .select{
          background-color: #0090ec;
          color: #fff;
        }
      }
      .area-name{
        position: relative;
        margin-bottom: 20px;
        float: left;
        margin: 0 38px 20px 16px;
        font-size: 13px;
        color: $color;
        input{
          width: 136px;
        }
        span{
          position: absolute;
          bottom: -25px;
          right: 42px;
          color: #f00;
        }
      }
      .describe{
        margin-bottom: 20px;
        float: left;
        margin-right: 49px;
        font-size: 13px;
        color: $color;
        input{
          width: 258px;
        }
      }
      .position{
        position: relative;
        display: flex;
        width: 550px;
        // margin-right: 200px;
        font-size: 13px;
        color: $color;
        .origin{
          margin-right: 34px;
        }
        input{
          width: 49px;
          margin: 0 5px;
          padding: 0;
          font-size: 12px;
          text-align: center;
        }
        span{
          position: absolute;
          bottom: -25px;
          left: 200px;
          color: #f00;
        }
      }
    }
    input{
      height: 30px;
      border: 1px solid #0061d0;
      background: #00418c;
      color: #fff;
      padding-left: 10px; 
    }
    .but {
      position: absolute;
      bottom: 50px;
      left: 50%;
      width: 190px;
      margin-left: -90px;
      button {
        width: 70px;
        height: 31px;
        margin-right: 20px;
        border: 1px solid #016e9c;
        color: $color;
        background: #014c9b;
      }
      .up {
        color: #fff;
        background: #0090ec;
        border: 1px solid #0090ec;
      }
    }
  }
  @media screen and (max-width: 1680px) {
    .position{
      display: block;
    }
  }
  .img-view{
    position: absolute;
    top: 100px;
    bottom: 0;
    width: 100%;
    padding: 40px 40px 100px 40px;
    .box{
      display: flex;
      justify-content: center;
      align-items: center;
      width: 100%;
      height: 100%;
    }
    .img-box{
      position: relative;
      img{
        width: 100%;
      }
      .canvas{
        position: absolute;
        top: 0;
      }
    }
  }
</style>
<script>
import DrawRectangular from '../components/draw-rectangular'
import AreaShow from '../components/area-show'
import DrawPolygon from '../components/draw-polygon'
import { accDiv, accSub, accAdd } from "@/assets/js/parseFloat";
export default {
  name: 'add-area',
  components: {
    DrawRectangular,
    AreaShow,
    DrawPolygon
  },
  data () {
    return {
      area: false,
      vacancy: false,
      rectangle: [],
      ratioRectangle: [],
      polygon: [],
      ratioPolygon: [],
      shape: null,
      shapeSelection: [
        {
          label: '多边形',
          value: 0
        },
        // {
        //   label: '圆形',
        //   value: 1
        // },
        // {
        //   label: '椭圆',
        //   value: 2,
        // },
        {
          label: '矩形',
          value: 3
        }
      ],
      imgUrl: '',
      imgSize: {
        width: '',
        height: ''
      },
      viewSize: document.body.clientWidth, // 初始化视图窗口大小，这个很重要！！！
      fenceName: '',
      remark: '',
      canvasWidth: 0,
      canvasHeight: 0,
      mapWidth: 0,
      mapHeight: 0,
      coord: [
        {x: '', y: ''},
        {x: '', y: ''},
        {x: '', y: ''},
        {x: '', y: ''},
      ],
      widthRatio: 0,
      heightRatio: 0,
    }
  },
  methods: {
    close () {
      this.$store.state.nav.navShow = true
      this.$emit('closes', false)
    },
    getCoord (msg) {
      this.coord = msg
    },
    // 设置canvas的宽高，宽高等于地图的大小
    setCanvasSize () {
      this.canvasWidth = this.$refs.map.clientWidth
      this.canvasHeight = this.$refs.map.clientHeight
      this.widthRatio = this.canvasWidth / this.mapWidth
      this.heightRatio = this.canvasHeight / this.mapHeight
      this.$store.commit('setCanvasWith', this.$refs.map.clientWidth)
      this.$store.commit('setCanvasHeight', this.$refs.map.clientHeight)
       // 对后端传过来的区域划分对象进行深拷贝
      let arr = JSON.stringify(this.rectangle)
      let i = 0
      this.ratioRectangle = JSON.parse(arr)
      this.ratioRectangle.forEach(e => {
        e.points.forEach(el => {
          el.x = el.x * this.widthRatio;
          el.y = el.y * this.heightRatio;
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
          el.x = el.x * this.widthRatio;
          el.y = el.y * this.heightRatio;
        })
      })
    },
    // 设置图片大小
    setImgSize (width, height) {
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
    getImg () {
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
      this.$http.Post(this.$url.allImg, {"status":1})
      .then(res => {})
      .catch(rej => {
        let data = rej.data.result[0]
        this.imgUrl = data.path
        this.mapWidth = data.length
        this.mapHeight = data.width
        this.setImgSize(data.picLength, data.picWidth)
        setTimeout(() => {
          this.setCanvasSize()
        }, 1000)
      })
    },
    addFence () {
      if (this.fenceName === '') {
        this.vacancy = true
      } else if (this.coord[0].x === '') {
        this.area = true
      } else  {
        this.$http.Post(this.$url.fenceAdd, {
          fenceName: this.fenceName,
          remark: this.remark,
          type: this.shape,
          points: this.coord
        }).then(res => {
        }).catch(rej => {
         if (rej.data.errcode === 10001) {
            this.close()
          } else {
            this.$message({
                type: "error",
                message: rej.data.msg
              });
          }
        })
      }
    }
  },
  mounted () {
    this.getImg()
    window.onresize = () => {
      return (() => {
        this.viewSize = document.body.clientWidth;
      })();
    };
  },
  watch: {
    // 监听 viewSize，当该属性值发生变化时canvas大小也会改变
    viewSize() {
      this.setCanvasSize();
    },
    fenceName (val) {
      if (val !=='') {
        this.vacancy = false
      }
    },
    coord () {
      this.area = false
    },
    shape () {
      this.coord = [
        {x: '', y: ''},
        {x: '', y: ''},
        {x: '', y: ''},
        {x: '', y: ''},
      ]
    }
  }
}
</script>
