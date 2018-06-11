<template>
  <div ref="box" class="draw-playback">
    <canvas id="point" :width="canvasWidth" :height="canvasHeight"></canvas>
  </div>
</template>
<style lang="scss" scoped>
.draw-playback {
  width: 100%;
  height: 100%;
}
</style>
<script>
import { mapState, mapGetters } from 'vuex'
export default {
  name: 'draw-playback',
  props: {
    canvasHeight: {type: Number},
    canvasWidth: {type: Number},
    position: {type: Array},
    statusShow: {type: Boolean},
    proportion: {type: Object},
    nameShow: {type: Boolean},
    trackShow: {type: Boolean},
    multiple: {tyoe: Number},
    personArr: {type: Array}
  },
  data () {
    return {
      width: 0,
      height: 0,
      viewSize: document.body.clientWidth, // 初始化视图窗口大小，这个很重要！！！
      radius: 5,
      lucency: 1,
      preson: []
    }
  },
  computed: {
    ...mapState({
      startTime: state => state.playback.startTime,
      endTime: state => state.playback.endTime,
      playTime: state => state.playback.playTime,
      play: state => state.playback.play,
      isEmpty: state => state.playback.isEmpty
    })
  },
  methods: {
    darw (arr) {
      let cvs = document.getElementById('point')
      let ctx = cvs.getContext('2d')
      ctx.clearRect(0, 0, cvs.width, cvs.height)
      // 画点
      arr.forEach(e => {
        if (e.show) {
          let lucency = 0
          e.point.forEach(el => {
            ctx.beginPath()
            ctx.arc(el.x, el.y, 5, 0, Math.PI * 2)
            if (e.point.length === 1){
              lucency = 1
            } else {
              lucency+=(1/e.point.length)
            }
            if(el.type == 2) {
              ctx.fillStyle = 'rgba(0,255,0,'+lucency+')'
              ctx.fill()
              ctx.strokeStyle = 'rgba(0,255,0,'+lucency+')'
              ctx.stroke()
            } else {
              ctx.fillStyle = 'rgba(255,0,0,'+lucency+')'
              ctx.fill()
              ctx.strokeStyle = 'rgba(255,0,0,'+lucency+')'
              ctx.stroke()
            }
            ctx.closePath()
          })
          if (this.nameShow) {
            ctx.beginPath()
            ctx.font = '12px serif'
            ctx.textAlign = 'center'
            ctx.textBaseline = 'center'
            if (e.type !== 2) {
              ctx.fillStyle = 'rgba(255,0,0,1)'
            } else {
              ctx.fillStyle = 'rgba(0, 0, 0, 1)'
            }
            let y = 0
            if (e.namePosition.y-10 < 5) {
              y = e.namePosition.y + 20
            } else {
              y = e.namePosition.y - 10
            }
            ctx.fillText(e.personName, e.namePosition.x, y)
            ctx.closePath()
          }
        }
      })
    },
    animation () {
      this.interval = setInterval(() => {
        this.$store.commit('addPlayTimeLength')
        this.$store.commit('setPlayTime')
        if (this.isEmpty) {
          let cvs = document.getElementById('point')
          let ctx = cvs.getContext('2d')
          ctx.clearRect(0, 0, cvs.width, cvs.height)
          this.preson.forEach(el => {
            el.point = []
          })
        }
        this.$store.commit('setEmpty')
        let arr = []
        this.position.forEach((e, i) => {
          if (e.timestamp == Math.floor(this.playTime)) {
            let pointObj = {
              x: e.posX*this.proportion.width,
              y: e.posY*this.proportion.height,
              type: e.type
            }
            this.preson.forEach(el => {
              if (el.personCode === e.personCode) {
                el.personName = e.personName
                el.type = e.type
                if (this.trackShow) {
                  if (el.point.length < 60) {
                    el.point.push(pointObj)
                  } else {
                    el.point.shift()
                    el.point.push(pointObj)
                  }
                } else {
                  el.point = []
                  el.point.push(pointObj)
                }
                el.namePosition = pointObj
              }
            })
            this.darw(this.preson)
          }
        })
      }, 1000 / this.multiple)
    },
    stop () {
      clearInterval(this.interval)
    }
  },
  created () {
    this.personArr.forEach(e => {
      let obj = {
        personCode: e,
        point: [],
        show: true,
        namePosition: {},
        personName: '',
        type: 0
      }
      this.preson.push(obj)
    })
  },
  mounted () {
    this.animation()
    // 当浏览器窗口大小发生改变是对 viewSize 进行重新赋值
    window.onresize = () => {
      return (() => {
        this.viewSize = document.body.clientWidth
      })()
    }
  },
  beforeDestroy () {
    clearInterval(this.interval)
  },
  watch: {
    // 监听 viewSize，当该属性值发生变化时canvas大小也会改变
    viewSize () {
      this.getSize()
    },
    multiple () {
      clearInterval(this.interval)
      this.animation()
    },
    // 当回放时间大于结束时间时，动画停止，回放时间初始化，清空轨迹点
    playTime (val) {
      if (val>this.endTime) {
        this.$store.commit('setPlay', false)
        clearInterval(this.interval)
        this.preson.forEach(el => {
           el.point = []    
        })
      }
    },
    // 当动画执行时，勾选的人员出现变动，就只显示勾选人员的位置点
    personArr (val) {
      this.preson.forEach(e => {
        e.show = false
        val.forEach(el => {
          if (el === e.personCode) {
            e.show = true
          }
        })
      })
    },
  }
}
</script>
