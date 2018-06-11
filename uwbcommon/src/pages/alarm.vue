<template>
  <div class="alarm" ref="content">
    <div class="head">
      <div class="flex">
        <div class="title">今日告警</div>
        <pie :val="day" :setId='pie1' v-if="chartShow"></pie>
      </div>  
      <div class="flex">
        <div class="title">月度告警</div>
        <pie :val="month" :setId='pie2' v-if="chartShow"></pie>
      </div>
      <div class="flex">
        <div class="title">告警人员</div>
        <histogram :val="top"  v-if="chartShow"></histogram>
      </div>
    </div>
    <div class="teble">
      <div class="teble-head">
        <div class="serial">序号</div>
        <div class="name">姓名</div>
        <div class="number">工号</div>
        <div class="affiliation">所属部门</div>
        <div class="level">
          <el-dropdown @command="levelCommand">
            <span class="el-dropdown-link">
              级别<i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="">查看全部</el-dropdown-item>
              <el-dropdown-item command="2">严重</el-dropdown-item>
              <el-dropdown-item command="1">普通</el-dropdown-item>
              <el-dropdown-item command="0">提示</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
        <div class="type">
          <el-dropdown @command="typeCommand">
            <span class="el-dropdown-link">
              类型<i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="">查看全部</el-dropdown-item>
              <el-dropdown-item command="0">围栏报警</el-dropdown-item>
              <el-dropdown-item command="1">心率报警</el-dropdown-item>
              <el-dropdown-item command="3">电量报警</el-dropdown-item>
              <el-dropdown-item command="4">求救报警</el-dropdown-item>
              <el-dropdown-item command="5">腕带报警</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
        <div class="area">
          <el-dropdown @command="areaCommand">
            <span class="el-dropdown-link">
              区域<i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item v-for="(item, index) in area" :key="index" :command="item.fenceName">{{item.fenceName}}</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
        <div class="start-time">开始时间</div>
        <div class="end-time">结束时间</div>
        <div class="status">
          <el-dropdown @command="statusCommand">
            <span class="el-dropdown-link">
              状态<i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="">查看全部</el-dropdown-item>
              <el-dropdown-item command="0">未处理</el-dropdown-item>
              <el-dropdown-item command="1">已处理</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>
      <div class="teble-content">
        <div class="row" v-for="(item, index) in listData">
          <div class="serial">{{index+1}}</div>
          <div class="name">{{item.personName}}</div>
          <div class="number">{{item.personCode}}</div>
          <div class="affiliation">{{item.departmentName}}</div>
          <div class="level">
            {{level(item.level)}}
          </div>
          <div class="type">
            <span>{{type(item.type)}}</span>
          </div>
          <div class="area">
            {{item.fenceName}}
          </div>
          <div class="start-time">
            {{formattingTime(item.timestamp)}}
          </div>
          <div class="end-time">{{formattingTime(item.finishTime)}}</div>
          <div class="status">
            <div v-if="item.status===1">
              <el-dropdown @command="handleCommand">
                <span class="el-dropdown-link">
                  已处理<i class="el-icon-arrow-down el-icon--right"></i>
                </span>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item :command="{id: item.id, status: 1}">已处理</el-dropdown-item>
                  <el-dropdown-item :command="{id: item.id, status: 0}">未处理</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </div>
            <div v-if="item.status===0">
              <el-dropdown @command="handleCommand">
                <span class="el-dropdown-link">
                  未处理<i class="el-icon-arrow-down el-icon--right"></i>
                </span>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item :command="{id: item.id, status: 1}">已处理</el-dropdown-item>
                  <el-dropdown-item :command="{id: item.id, status: 0}">未处理</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </div>
          </div>
          </div>
        </div>
      <el-pagination
        background
        @current-change="handleCurrentChange"
        :page-size="ListObj.listRows"
        layout="total, prev, pager, next"
        :total="total"
        class="pagination">
      </el-pagination>
    </div>
  </div>
</template>
<style lang="scss" scoped>
  .alarm{
    height: 78%;
    margin-top: 50px;
    padding: 0 56px;
    .head{
      display: flex;
      width: 100%;
      height: 273px;
      padding: 0 -15px;
      .flex{
        position: relative;
        flex: 1;
        background: rgba(0, 83, 193, 0.5);
        margin: 0 15px;
        .title{
          position: absolute;
          top: 21px;
          left: 21px;
          font-size: 14px;
          color: #fff;
        }
      }
    }
    .teble{
      margin-top: 27px;
      padding: 0 15px;
      .teble-head,
      .row{
        display: flex;
        height: 58px;
        line-height: 58px;
        border-bottom: 1px solid rgba(0, 144, 236, 0.6);
        font-size: 14px;
        background-color: rgba(0, 102, 204, 0.5);
        color: #59b3ff;
        >div{
          padding-left: 10px;
        }
        .serial{
          width: 50px;
        }
        .name,
        .number,
        .status,
        .area,
        .level
        {
          width: 120px;
        }
        .affiliation,
        .type,
        {
          flex: 1;
        }
        .start-time,
        .end-time{
          flex: 2;
        }
      }
      .row{
        height: 45px;
        line-height: 45px;
        background-color: rgba(0, 0, 0, 0);
        &:hover{
          background: rgba(0, 153, 255, 0.15);
          color: #fff;
        }
      }
      .teble-content{
        border-bottom: 1px solid #ccc;
      }
    }
  }
  @media screen and (max-height: 820px) {
    .alarm{
      margin-top: 20px;
    }
  }
  .el-dropdown-link{
    cursor: pointer;
  }
</style>
<script>
import Pie from '../components/pie'
import Histogram from '../components/histogram'
export default {
  name: 'alarm',
  components: {
    Pie,
    Histogram
  },
  data () {
    return {
      pie1: 'pie1',
      pie2: 'pie2',
      pie3: 'pie3',
      chartShow: false,
      listData: [],
      ListObj: {
        page: 1,
        listRows: 0,
        level: '',
        type: '',
        fenceName: '',
        status: '',
      },
      area: [],
      total: 0,
      month: {
        severity: 0,
        general: 0,
        hint: 0
      },
      day: {
        severity: 0,
        general: 0,
        hint: 0
      },
      top: {
        name: [],
        severity: [],
        general: [],
        hint: []
      }
    }
  },
  methods: {
    /* 级别，类型，区域筛选不做多重筛选，所以选择一项时，其他两项清空 */
    levelCommand (command) {
      this.ListObj.level = command
      this.ListObj.type = ''
      this.ListObj.fenceName = ''
      this.ListObj.status = ''
      setTimeout(() => {
        this.getListData()
      }, 0);
    },
    typeCommand (command) {
      this.ListObj.type = command
      this.ListObj.level = ''
      this.ListObj.fenceName = ''
      this.ListObj.status = ''
      setTimeout(() => {
        this.getListData()
      }, 0);
    },
    areaCommand (command) {
      this.ListObj.level = ''
      this.ListObj.type = ''
      this.ListObj.status = ''
      if (command === '查看全部') {
        this.ListObj.fenceName = ''
      } else {
        this.ListObj.fenceName = command
      }
      setTimeout(() => {
        this.getListData()
      }, 0);
    },
    statusCommand (command) {
      this.ListObj.level = ''
      this.ListObj.type = ''
      this.ListObj.fenceName = ''
      if (command === '查看全部') {
        this.ListObj.status = ''
      } else {
        this.ListObj.status = command
      }
      setTimeout(() => {
        this.getListData()
      }, 0);
    },
    // 翻页
    handleCurrentChange(val) {
      this.ListObj.page = val
      this.getListData()
    },
    handleCommand(command) {
      this.$http.Post(this.$url.warningUpdate, command)
      .then(res => {})
      .catch(rej => {
        if (rej.data.errcode === 10001) {
          this.getListRows()
          if (command.status === 1) {
            this.$store.commit('reduce')
          } else {
            this.$store.commit('increment')
          }
          
        }
      })
    },
    level (val) {
      switch (val) {
        case 0:
          return '提示'
        case 1:
          return '普通'
        case 2:
          return '严重'
        default:
          break
      }
    },
    type (val) {
      switch (val) {
        case 0:
          return '围栏报警'
        case 1:
          return '心率报警'
        case 3:
          return '电量报警'
        case 4:
          return '求救报警'
        case 5:
          return '腕带报警'
        default:
          break
      }
    },
    formattingTime (t) {
      function buling (val) {
        if (val < 10) {
          val = '0' + val
        }
        return val
      }
      let nowTime = ''
      if (t !== 0) {
        let time = new Date(t * 1000)
        let y = time.getFullYear()
        let m = buling(time.getMonth() + 1)
        let d = buling(time.getDate())
        let h = buling(time.getHours())
        let mi = buling(time.getMinutes())
        let s = buling(time.getSeconds())
        nowTime = y + '-' + m + '-' + d + ' ' + h + ':' + mi + ':' + s
      } else {
        nowTime = ''
      }
      return nowTime
    },
    getListData () {
      this.$http
      .Post(this.$url.warnings, this.ListObj)
      .then(res => {

      })
      .catch(rej => {
        this.listData = rej.data.result
        this.total = rej.data.totalRows
      })
    },
    getListRows() {
      let height = this.$refs.content.clientHeight - 410;
      this.ListObj.listRows = Math.floor(height / 45);
      this.getListData();
    },
    getStatistics () {
      this.$http
      .Post(this.$url.statistics, {"limit":10})
      .then(res => {

      })
      .catch(rej => {
        let data = rej.data
        this.month.hint = data.month.tip
        this.month.general = data.month.common
        this.month.severity = data.month.urgency
        this.day.hint = data.day.tip
        this.day.general = data.day.common
        this.day.severity = data.day.urgency
        data.top.forEach(e => {
          this.top.name.push(e.personName)
          this.top.hint.push(e.tip)
          this.top.general.push(e.common)
          this.top.severity.push(e.urgency)
        })
        this.chartShow = true
      })
    }
  },
  created () {
    this.getStatistics()
    this.$http.Post(this.$url.fences, {})
    .then(res => {})
    .catch(rej => {
      this.area = rej.data.result
      this.area.unshift({fenceName: '查看全部'})
    })
  },
  mounted () {
    this.$nextTick(() => {
      this.getListRows()
    })
  },
}
</script>
