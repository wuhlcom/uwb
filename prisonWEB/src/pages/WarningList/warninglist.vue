<template>
  <div class="warninglist">
    <handlingstatus v-if="statusshow" @statusalert="staalert" @num="setnum" :handlinginfo="handlinginfo"></handlingstatus>
    <warning-alert v-show="warningShow" @warning="warning" class="warning">
    </warning-alert>
    <alert v-if="alertShow" @hide="tohide"></alert>
    <v-header :actShow="true" :praShow="true" :bellShow="true" @out="tohide" class="header"></v-header>
    <div class="title">
      <div>
        一监区 > 报警列表
      </div>
    </div>
    <div class="main">
      <div class="main-content">
        <div class="distinguish">
          <span class="select-title">区域</span>
          <el-select style="width: 140px;" v-model="area" @change="selectData('building',area, 1)" class="sel-box" placeholder="请选择">
            <el-option v-for="item in areaOptions" :key="item.domainUniqueCode" :label="item.name" :value="item.domainUniqueCode">
            </el-option>
          </el-select>
          <span class="select-title">栋数</span>
          <el-select style="width: 140px;" v-model="building" @change="selectData('floor', building, 2)" :disabled="buildingok" class="sel-box" placeholder="请选择">
            <el-option v-for="item in buildingOptions" :key="item.value" :label="item.name" :value="item.buildingUniqueCode">
            </el-option>
          </el-select>
          <span class="select-title">层数</span>
          <el-select style="width: 140px;" v-model="tier" @change="selectData('area', tier, 3)" :disabled="tierok" placeholder="请选择">
            <el-option v-for="item in tierOptions" :key="item.value" :label="item.name" :value="item.floorUniqueCode">
            </el-option>
          </el-select>
          <span class="select-title">监仓</span>
          <el-select style="width: 140px;" v-model="room" :disabled="roomok" placeholder="请选择">
            <el-option v-for="item in roomOptions" :key="item.value" :label="item.name" :value="item.areaCode">
            </el-option>
          </el-select>
          <span class="select-title">时间</span>
          <el-date-picker v-model="time" type="datetimerange" :picker-options="pickerOptions2" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" align="right">
          </el-date-picker>
          <div class="enquiries" @click="getData">
            查询</div>
        </div>
        <div class="list-content">
          <sequencing-list :sequencinglist="sequencinglist" @sequencingClass="getorder" @sequencingContent="listContent">
          </sequencing-list>
        </div>
        <div v-for="(item, index) in listInfo" :key="item.id" class="content">
          <span class="flex-one">{{ item.level }}</span>
          <span class="flex-one">{{ item.msg }}</span>
          <span class="flex-one">
            <router-link :to="{name: 'prisonerinfo', query: {num: item.code}}">
              {{ item.name }}
            </router-link>
          </span>
          <span class="flex-one">{{ item.code }}</span>
          <span class="flex-one">{{ item.position }}</span>
          <span class="flex-one">{{ item.timestamp }}</span>
          <span class="flex-one">{{ item.finishTime }}</span>
          <span class="flex-one" :data-index="index" style="cursor: pointer;" @click="handling($event)">{{ item.state }}</span>
          <span class="flex-one"></span>
        </div>
        <div class="pagination">
          <el-pagination @current-change="handleCurrentChange" background layout="total, prev, pager, next, jumper" :page-size="this.listRows" :total="pages">
          </el-pagination>
        </div>
      </div>
    </div>
  </div>
</template>
<style lang="scss" scoped>
.warninglist {
  position: relative;
  background: #f1f3f6;
  height: 100%;
  overflow: auto;
  a {
    color: #2d2f33;
  }
  .header {
    display: flex;
    align-items: center;
    height: 6.3%;
    min-height: 40px;
  }
  .title {
    display: flex;
    align-items: center;
    position: relative;
    padding-left: 20px;
    width: 100%;
    height: 4.6%;
    font-size: 14px;
    color: #2d2f33;
  }
  .main {
    width: 100%;
    height: 88%;
    padding: 0 20px;
    .main-content {
      padding: 20px;
      width: 100%;
      height: 100%;
      background: #fff;
      .distinguish {
        margin-bottom: 20px;
        font-size: 12px;
        .select-title {
          font-size: 14px;
          margin: 0 5px;
          vertical-align: middle;
        }
        .enquiries {
          display: inline-block;
          height: 40px;
          width: 80px;
          background: #409eff;
          vertical-align: top;
          margin-left: 10px;
          text-align: center;
          line-height: 40px;
          font-size: 16px;
          border-radius: 5px;
          color: #fff;
          cursor: pointer;
        }
      }
    }
  }
  .content {
    display: flex;
    font-size: 13px;
    color: #676a6c;
    background: #fff;
    height: 40px;
    border-top: 1px solid #ddd;
    .flex-one {
      flex: 1;
      line-height: 40px;
      padding-left: 10px;
    }
  }
  .pagination {
    margin-top: 10px;
    text-align: center;
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
import WarningAlert from "com/warningalert/warningalert";
import Alert from "com/alert/alert";
import VHeader from "com/header/header";
import SequencingList from "com/sequencinglist/sequencinglist";
import Handlingstatus from "com/handlingstatus/handlingstatus";
import { formattingTime, buling } from "@/assets/js/formattingTime";
import { PRISON_API } from "../../api/api";

export default {
  name: "warninglist",
  components: {
    SequencingList,
    VHeader,
    Alert,
    Handlingstatus,
    WarningAlert
  },
  data() {
    return {
      warningShow: false,
      handlinginfo: {},
      statusshow: false,
      sequencinglist: [
        {
          text: "级别",
          flex: 1,
          msg: "level"
        },
        {
          text: "类型",
          flex: 1,
          line: true,
          msg: "msg"
        },
        {
          text: "姓名",
          flex: 1,
          line: true,
          msg: "name"
        },
        {
          text: "囚号",
          flex: 1,
          line: true,
          msg: "code"
        },
        {
          text: "报警区域",
          flex: 1,
          line: true,
          msg: "position"
        },
        {
          text: "起始时间",
          flex: 1,
          sequencingShow: true,
          elect: "down",
          line: true,
          msg: "timestamp"
        },
        {
          text: "结束时间",
          flex: 1,
          line: true,
          msg: "finishTime"
        },
        {
          text: "状态",
          flex: 1,
          line: true,
          msg: "state"
        },
        {
          text: "备注",
          flex: 1,
          line: true
        }
      ],
      area: "",
      building: "",
      tier: "",
      room: "",
      areaOptions: [],
      buildingOptions: [],
      tierOptions: [],
      roomOptions: [],
      buildingok: true,
      tierok: true,
      roomok: true,
      alertShow: false,
      listRows: 1,
      listInfo: [],
      time: null,
      pages: 0,
      pickerOptions2: {
        disabledDate(time) {
          return time.getTime() > Date.now();
        },
        shortcuts: [
          {
            text: "最近一周",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit("pick", [start, end]);
            }
          },
          {
            text: "最近一个月",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit("pick", [start, end]);
            }
          },
          {
            text: "最近三个月",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
              picker.$emit("pick", [start, end]);
            }
          }
        ]
      },
      startTime: "",
      endTime: "",
      sort: "",
      order: "",
      current: ""
    };
  },
  methods: {
    warning(msg) {
      this.warningShow = msg;
    },
    setnum() {
      this.getData();
    },
    handling(event) {
      let e = event.target;
      let i = e.getAttribute("data-index");
      this.handlinginfo = this.listInfo[i];
      this.statusshow = true;
    },
    staalert(msg) {
      this.statusshow = msg;
    },
    // 排序
    getorder(msg) {
      this.order = msg;
      this.getData();
    },
    // 更新列表内容
    listContent(msg) {
      this.sort = msg;
    },
    // 级别
    level(val) {
      switch (val) {
        case "01":
          return "严重";
          break;
        case "02":
          return "普通";
          break;
        case "03":
          return "提示";
          break;
      }
    },
    // 状态
    state(val) {
      switch (val) {
        case 0:
          return "未处理";
          break;
        case 1:
          return "已处理";
          break;
      }
    },
    // 翻页
    handleCurrentChange(val) {
      this.current = val;
      this.getData();
    },
    // 退出窗口弹出
    tohide(msg) {
      this.alertShow = msg;
    },
    getOptions() {
      // 获取下拉列表数据
      this.$http
        .Post("/prison/domain/queryCode", {})
        .then(res => {})
        .catch(rej => {
          this.areaOptions = rej.data.result;
        });
    },
    getData() {
      // 获取列表信息
      this.$http
        .Post("/prison/warning/areas", {
          listRows: this.listRows,
          domainUniqueCode: this.area,
          buildingUniqueCode: this.building,
          floorUniqueCode: this.tier,
          areaCode: this.room,
          startTime: this.startTime,
          endTime: this.endTime,
          sort: this.sort,
          order: this.order,
          page: this.current
        })
        .then(res => {})
        .catch(rej => {
          this.num = 0;
          this.listInfo = rej.data.result;
          if (rej.data.errcode === 10001) {
            this.listInfo = rej.data.result;
            this.pages = rej.data.totalRows;
            this.listInfo.forEach(e => {
              e.state = this.state(e.state);
              e.level = this.level(e.level);
              if (e.timestamp) {
                e.timestamp = formattingTime(e.timestamp);
              }
              if (e.finishTime) {
                e.finishTime = formattingTime(e.finishTime);
              }
            });
          }
        });
    },
    selectData(url, msg, num) {
      this.$http
        .Post("/prison/" + url + "/queryCode", { codeValue: msg })
        .then(res => {})
        .catch(rej => {
          if (num == 1) {
            this.buildingOptions = rej.data.result;
          }
          if (num == 2) {
            this.tierOptions = rej.data.result;
          }
          if (num == 3) {
            this.roomOptions = rej.data.result;
          }
        });
    },
    // 退出窗口弹出
    tohide(msg) {
      this.alertShow = msg;
    }
  },
  created() {
    this.$store.state.warningAmount = 0;
    this.time = [new Date(new Date().setHours(0, 0, 0, 0)), new Date()];
  },
  mounted() {
    this.listRows =
      Math.floor(
        (document.getElementsByClassName("main")[0].clientHeight - 110) / 40
      ) - 2;
    this.startTime = Date.parse(this.time[0]) / 1000;
    this.endTime = Date.parse(this.time[1]) / 1000;
    this.getOptions();
    this.getData();
  },
  watch: {
    area(val) {
      this.building = "";
      this.tier = "";
      this.room = "";
      if (val != "") {
        this.buildingok = false;
      }
    },
    building(val) {
      this.tier = "";
      this.room = "";
      if (val != "") {
        this.tierok = false;
      } else {
        this.tierok = true;
      }
    },
    tier(val) {
      this.room = "";
      if (val != "") {
        this.roomok = false;
      } else {
        this.roomok = true;
      }
    },
    time(val) {
      if (val == null) {
        this.startTime = "";
        this.endTime = "";
      }
      this.startTime = Date.parse(val[0]) / 1000;
      this.endTime = Date.parse(val[1]) / 1000;
    }
  }
};
</script>
