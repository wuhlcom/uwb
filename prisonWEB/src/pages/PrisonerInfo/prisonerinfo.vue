<template>
  <div class="priinfo">
    <alert
      v-if="alertShow"
      @hide="tohide">
    </alert>
    <warning-alert
      v-show="warningShow"
      @warning="warning"
      class="warning">
    </warning-alert>
    <v-header 
      :actShow="true" 
      :praShow="true"
      :bellShow="true"
      @out="tohide" 
      class="header">
    </v-header>
    <personal-alarm-popover
      v-if="list"
      @listalert="listshow">
    </personal-alarm-popover>
    <div class="title">
      <div>
        二监区三分监区>
        <router-link to="/panorama">
          首页
        </router-link>
        >
        <router-link :to="{name: 'cell', query: {num: info.areaCode, id: id}}">{{ info.areaName }}</router-link>
        >{{ info.name }}
      </div>
      <v-time class="floors-time"></v-time>
    </div>
    <div class="main">
      <div class="flex-one">
        <div class="photograph">
          <div class="pgp-img">
            <img src="./head.png">
          </div>
        </div>
        <div class="info">
          <div class="info-head">
            <div class="head-title vertical">个人信息</div>
            <div>更多></div>
          </div>
          <div class="content">
            <div class="content-info headings">
              <div class="info-one">囚号</div>
              <div class="info-one">姓名</div>
              <div class="info-one">籍贯</div>
              <div class="info-one">年龄</div>
              <div class="info-one">犯罪类型</div>
              <div class="info-one">刑期</div>
            </div>
            <div class="content-info">
              <div class="info-one">{{ info.code }}</div>
              <div class="info-one">{{ info.name }}</div>
              <div class="info-one">{{ info.nativePlace }}</div>
              <div class="info-one">{{ info.age }}</div>
              <div class="info-one">{{ info.condemnation }}</div>
              <div class="info-one">{{ info.imprisonment }}</div>
            </div>
          </div>
        </div>
      </div>
      <div class="flex-one alarm-list">
        <div class="list-head">
          <div class="head-title vertical">报警信息</div>
          <div
            @click="list=true"
            style="cursor:pointer">
            更多>
          </div>
        </div>
        <div class="list">
          <div class="list-title">
            <sequencing-list 
              :sequencinglist="sequencinglist"
            ></sequencing-list>
          </div>
          <div v-for="item in warnings" :key="item.id" class="list-content">
            <span class="flex-one">{{ item.level }}</span>
            <span class="flex-two">{{ item.msg }}</span>
            <span class="flex-two">{{ item.position }}</span>
            <span class="flex-four">{{ item.timestamp }}</span>
            <span class="flex-four">{{ item.finishTime }}</span>
            <span class="flex-two">{{ item.state }}</span>
          </div>
        </div>
      </div>
    </div>
    <div class="prisoner-view">
      <div class="cell-position">
        <div class="position-title vertical">{{ positionName }}</div>
        <div v-if="nowifi" class="nowifi">
          <div class="nowifi-img">
            <img src="./nowifi.png">
            <div>信号消失</div>
          </div>
          
        </div>
        <div 
          class="cell-img" 
          ref="size">
          <img 
            v-if="imgl"
            src="./cell.png"
            class="img">
          <img
            v-if="!imgl"
            src="./cell1.png"
            class="img">
          <position 
            v-if="canvasShow"  
            :prisonerPosition="prisonerPosition"
            :sizeHig="sizeHig" 
            :sizeWih="sizeWih" 
            class="canvas">
          </position>
        </div>  
      </div>
      <div class="cell-position">
        <div class="position-title vertical">视频</div>
        <div class="videos">
          <img src="./videos.png">
        </div>  
      </div>
    </div>
  </div>
</template>
<style lang="scss" scoped>
  .priinfo {
    background: #f1f3f6;
    height: 100%;
    overflow: auto;
    a{
      color: #2d2f33;
    }
    .header{
      display: flex;
      align-items: center;
      height: 6.3%;
      min-height: 40px;
    } 
    .title{
      display: flex;
      justify-content:space-between;
      align-items: center;
      position: relative;
      padding-left: 20px; 
      width: 100%;
      height: 4.6%;
      font-size: 14px;
      color: #2d2f33;
      .floors-time{
        padding: 0 20px;
        font-size: 14px;
        color: #409eff;
      }
    }
    .main{
      display: flex;
      padding: 0 10px;
      width: 100%;
      height: 27.3%;
      .flex-one{
        display: flex;
        flex: 1;
        margin: 10px;
      }
      .photograph{
        display: flex;
        flex: 1;
        align-items: center;
        justify-content: center;
        margin-right: 20px;
        background: #fff;
        overflow: hidden;
        .pgp-img{
          width: 80%;
          img{
            width: 100%;
          }             
        }

      }
      .info{
        position: relative;
        display: flex;
        align-items: center;
        flex: 2;
        background: #fff;
        .info-head{
          position: absolute;
          top: 0;
          display: flex;
          align-items: center;
          justify-content:space-between;
          padding: 0 20px;
          width: 100%;
          height: 18.8%;
          font-size: 14px;
          border-bottom: 1px solid #ebebeb;
        }
        .content{
          width: 100%;
          .content-info{
            display: flex;
            font-size: 14px;
            .info-one{
              flex: 1;
              height: 36px;
              line-height: 36px;
              text-align: center;
            }
          }
          .headings{
            color: #2d2f33;
          }
        }        
      }
      .alarm-list{
        position: relative;
        display: flex;
        align-items: center;
        justify-content: center;
        background: #fff;
        .list-head{
          position: absolute;
          top: 0;
          display: flex;
          align-items: center;  
          justify-content: space-between;
          padding: 0 20px;
          width: 100%;
          height: 18.8%;
          font-size: 14px;
        }
      }
      .list{
        padding: 0 10px;
        width: 100%;
        height: 62.7%;
        font-size: 13px;
        .list-title{
          display: flex;
          height: 40px;
          line-height: 40px;
          border-top: 1px solid #e5e9f2;
          background: #eff2f7;
          font-size: 14px;
          color: #2d2f33;
          .flex-two{
            flex: 2;
            padding-left: 10px;
          }
          .flex-one{
            flex: 1;
          }
          .flex-four{
            flex: 4;
            padding-left: 10px;
          }
            .text{
              float: left;
            }
            .sequencing{
              position: relative;
              display: inline-block;
              height: 100%;
              margin-left: 5px; 
              color: #b4bccf;
              .up{
                position: absolute;
                top: -2px;
              }
              .down{
                position: absolute;
                top: 5px;
              }
            }
            .elect-up{
              .up{
                color: #4e90ff;
              }
            }
            .elect-down{
              .down{
                color: #4e90ff;
              }
            }
          }
          .left-line{
            border-left: 1px solid #ddd;
          }
        }
        .list-content{
          display: flex;
          font-size: 13px;
          .flex-one{
            flex: 1;
            height: 30px;
            line-height: 30px;
            margin: 0 0 0 5px;
          }
          .flex-two{
            flex: 2;
            height: 30px;
            line-height: 30px;
            padding-left: 10px;
          }
          .flex-four{
            flex: 4;
            height: 30px;
            line-height: 30px;
            padding-left: 10px;
          }
        }
    }
    .prisoner-view{
      display: flex;
      padding: 10px;
      width: 100%;
      height: 61%;
      .cell-position{
        position: relative;
        display: flex;
        flex: 1;
        align-items: center;
        justify-content: center;
        margin: 0 10px;
        background: #fff;
        .position-title{
          position: absolute;
          top: 20px;
          left: 20px;
          font-size: 14px;
        }
        .nowifi{
          position: absolute;
          top: 0;
          bottom: 0;
          left: 0;
          width: 100%;
          z-index: 60;
          display: flex;
          align-items: center;
          justify-content: center;
          background: #fff;
          .nowifi-img{
            display: flex;
            flex-direction: column;
            align-items: center;
          }
        }
        .cell-img{
          position: relative;
          width: 90%;
          img{
            width: 100%;
          }
          .canvas{
            position: absolute;
            top: 0;
            left: 0;
          }
        }
        .videos{
          width: 50%;
          img{
            width: 100%;
          }
        }
      }
    }
    .warning{
      position: absolute;
      right: 0;
      bottom: 0;
      z-index: 100;
      box-shadow: -3px -3px 10px 1px #888;
    }
  }
</style>
<script>
  import WarningAlert from 'com/warningalert/warningalert'
  import VHeader from 'com/header/header'
  import Alert from 'com/alert/alert'
  import VTime from 'com/time/time'
  import Position from 'com/position/position'
  import SequencingList from 'com/sequencinglist/sequencinglist'
  import PersonalAlarmPopover from 'com/personalalarmpopover/personalalarmpopover'
  import { accDiv, accMul, accSub } from '@/assets/js/parseFloat'
  import { formattingTime, buling } from '@/assets/js/formattingTime'
  import { PRISON_API, WEB_SOCKET_PAI } from '../../api/api'
  export default {
    name: 'prisonerinfo',
    components: {
      VHeader,
      VTime,
      Alert,
      Position,
      SequencingList,
      PersonalAlarmPopover,
      WarningAlert,
    },
    data() {
      return {
        id: this.$route.query.id,
        cellnum: '',
        nowifi: true,
        warningShow: false,
        list: false,
        alertShow: false,
        sizeHig: 0,
        sizeWih: 0,
        imgl: true,
        info: {},
        widthPpt: 0,
        heightPpt: 0,
        warnings: [],
        count: 0,
        prisonerPosition: [],
        canvasShow: false,
        positionName: '',
        sequencinglist: [
          {
            text: '级别',
            flex: 1,
            sequencingShow: false,
          },
          {
            text: '类型',
            flex: 2,
            sequencingShow: false,
            line: true,
          },
          {
            text: '报警区域',
            flex: 2,
            sequencingShow: false,
            line: true,
          },
          {
            text: '起始时间',
            flex: 4,
            sequencingShow: false,
            line: true,
          },
          {
            text: '结束时间',
            flex: 4,
            sequencingShow: false,
            line: true,
          },
          {
            text: '状态',
            flex: 2,
            sequencingShow: false,
            line: true,
          },
        ],
      }
    },
    methods: {
      warning (msg) {
        this.warningShow = msg
      },
      listshow (msg) {
        this.list = msg
      },
      // 退出窗口弹出
      tohide(msg) {
        this.alertShow = msg
      },
      getData () {
        let num = this.$route.query.num
        this.$http.Post(PRISON_API.prisonerinfo, '{"code":"'+ num +'", "limit": 3}').then(res => {
          // console.log(res)
        }).catch(rej => {
          this.info = rej.data.result.info
          this.warnings = rej.data.result.warnings
          this.warnings.forEach(e => {
            e.timestamp = formattingTime(e.timestamp)
            e.finishTime = formattingTime(e.finishTime)
            e.state = this.state(e.state)
            e.level = this.level(e.level)
          })
          this.websocket()
        })
      },
      // 排序更新数据
      sequencingData (sort) {
        this.sort = sort
        this.getData(sort)
      },
       // 获取canvas尺寸
      getSize () {
        this.sizeHig = this.$refs.size.clientHeight
        this.sizeWih = this.$refs.size.clientWidth
        this.widthPpt = accDiv(this.sizeWih, 4.385)
        this.heightPpt = accDiv(this.sizeHig, 2.675)
      },
      // 信息处理
      sta (val) {
        let obj = {}
        obj.tagId = val.tagId
        obj.code = val.code
        obj.name = val.name
        obj.nameshow = true
        obj.posCode = val.posCode
        obj.areaCode = val.areaCode
        obj.x = Number(accSub(accMul(val.posY, this.widthPpt), this.sizeWih))
        obj.y = accMul(val.posX, this.heightPpt)
        if (val.flag === 6) {
          obj.status = 1
        } else {
          obj.status = val.flag
        }
        return obj
      },
      // 状态
      state (val) {
        switch (val) {
          case 0:
            return '未处理'
            break;
          case 1:
            return '已处理'
            break;
        }    
      },
      // 级别
      level (val) {
        switch (val) {
          case '01':
            return "严重";
            break;
          case '02':
            return "普通";
            break;
          case '03':
            return "提示";
            break;
        }
      },
      websocket () {
        let ws = new WebSocket('ws://192.168.10.9:8802/websocket/prison')
        ws.onopen = () => {
          ws.send('{"type":3,"dataType":0,"tagId": '+ this.info.tagId +'}')
        }
        ws.onmessage = evt => {
          let datainfo = JSON.parse(evt.data)
          if (this.cellnum != datainfo.result.position.slice(2, 5)) {
            this.prisonerPosition = []
          }
          if (datainfo.result.flag === 5 || datainfo.result.flag === 7) {
            this.nowifi = true
          } else {
            this.nowifi = false
          }
          this.positionName = datainfo.result.position
          this.cellnum = this.positionName.slice(2, 5)
          if (this.cellnum%2 !== 0) {
            this.imgl = false
          } else {
            this.imgl = true
          }
          let data = this.sta(JSON.parse(evt.data).result)
          if (this.prisonerPosition.length < 10) {
            this.prisonerPosition.push(data)
          } else {
            this.prisonerPosition.shift()
            this.prisonerPosition.push(data)
          }
          if (this.prisonerPosition.length > 1) {
            for (let i=0; i<this.prisonerPosition.length-1; i++) {
              this.prisonerPosition[i].nameshow = false
            }
          }
        }
        // 路由跳转时结束websocket链接
        this.$router.afterEach(function () {
          ws.close()
        })
      },
    },
    mounted () {
      this.getData()
       // 页面大小改变时设置高宽
      window.onresize = () => {
        this.getSize()
        this.websocket()
      }
      /*  
      因为本页面的canvas大小的依赖于背景图
      所以要在img加载完成之后再渲染canvas部分
    */
      let imgs = document.querySelectorAll('.img')
        Array.from(imgs).forEach((item)=>{
          let img = new Image()
          img.onload = ()=>{
            this.count++
          }
          img.src=item.getAttribute('src')
      })
    },
    watch: {
      // img加载完成之后再渲染canvas
      count (val) {
        if(val == 1){
          this.getSize()
          this.canvasShow = true
        }
      }
    },
  }
</script>
