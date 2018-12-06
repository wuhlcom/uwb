<template>
  <div class="cell">
    <div class="cell-status flex-one" >
      <status 
        :cellNum="cellNum">
      </status>
    </div>
    <div class="cell-position flex-one">
      <div class="title vertical">实时轨迹图</div>
      <div class="cell-img" ref="size">
        <img v-if="imgl" src="./cell.png">
        <img v-if="!imgl" src="./cell1.png">
          <position 
          v-if="canvasShow"  
          :prisonerPosition="prisonersPosition"
          :sizeHig="sizeHig" 
          :sizeWih="sizeWih" 
          class="canvas"></position>
      </div>  
    </div>
  </div>
</template>
<style lang="scss" scoped>
  .cell{
    display: flex;
    margin: 10px;
    overflow: hidden;
    font-size: 0;
    .flex-one{
      flex: 1;
      height: 100%;
      background: #fff;
      margin: 10px 10px 0 10px;
    }
    .cell-position{
      position: relative;
      display: flex;
      justify-content: center;
      align-items: center;
      .title{
        position: absolute;
        top: 20px;
        left: 20px;
        font-size: 14px;
      }
      .cell-img{
        display: block;
        position: relative;
        width: 95%;
        img{
          width: 100%;
          height: 100%;
        }
      }
      .canvas{
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        box-sizing: border-box;
      }
    }
  }
</style>
<script>
import Status from 'com/status/status'
import Position from 'com/position/position'
import {accDiv, accMul, accSub} from '@/assets/js/parseFloat'
import { PRISON_API } from '../../api/api'
export default {
  name: 'cell',
  components: {
    Status,
    Position,
  },
  data () {
    return {
      imgl: true,
      positionInfo: [],
      cellNum: this.$route.query.id,
      prisonerstatus: [],
      widthPpt: 0,
      heightPpt: 0,
      canvasShow: false,
      count: 0,
      viewSize: document.body.clientWidth,
    }
  },
  computed: {
    sizeHig () {
      return this.$store.state.sizeHig
    },
    sizeWih () {
      return this.$store.state.sizeWih
    },
    prisonersPosition () {
      return this.$store.getters.prisonersPosition
    }  
  },
  methods: {
    // 获取canvas尺寸
    getSize () {
      this.$store.state.sizeHig = this.$refs.size.offsetHeight
      this.$store.state.sizeWih = this.$refs.size.clientWidth
      this.$store.state.width = accDiv(this.$store.state.sizeWih, 4.385)
      this.$store.state.height = accDiv(this.$store.state.sizeHig, 2.675)
    }
  },
  created () {
    this.$http.Post(PRISON_API.prisoners, {areaCode: this.$route.query.num}).then(res => {
      
    }).catch(rej => {
      let data = rej.data
      data.prisoners.forEach(e => {
        e.areaCode = data.areaCode
      })
      this.$store.state.prisoners = data.prisoners
      let num = data.areaName.slice(2)
      if (num%2 !== 0) {
        this.imgl = false
      }
    })
  },
  mounted () {
    /*  
    因为本页面的canvas大小的依赖于背景图
    所以要在img加载完成之后再渲染canvas部分
   */
    let imgs = document.querySelectorAll('img')
      Array.from(imgs).forEach((item)=>{
        let img = new Image()
        img.onload = ()=>{
          this.count++
        }
        img.src=item.getAttribute('src')
    })
  },
  updated () {
   window.onresize = () => {
      return (() => {
        this.viewSize = document.body.clientWidth
      })()
    }
  },
  watch: {
    // img加载完成之后再渲染canvas
    count (val) {
      if(val === 3){
        this.getSize()
        this.canvasShow = true
      }
    },
    viewSize () {
      this.getSize()
    },
  }
}
</script>
