<template>
  <div class="absencelist">
    <handlingstatus v-if="statusshow" @statusalert="staalert" @num="setnum" :handlinginfo="handlinginfo"></handlingstatus>
    <div class="title vertical">报警信息列表</div>
    <div class="head">
      <sequencing-list
        :sequencinglist="sequencinglist"
        @sequencingClass="getorder"
        @sequencingContent="listContent">
      </sequencing-list>
    </div>
    <div v-if="datum" v-for="(item, index) in warningsInfo" :key="item.id" class="content">
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
      <span
            class="flex-one"
            :data-index="index"
            style="cursor: pointer;"
            @click="handling($event)"
          >{{ item.state }}</span>
      <span class="flex-one"></span>
    </div>
    <div v-if="datum" class="pagination">
      <el-pagination @current-change="handleCurrentChange" :page-size="this.listRows" background layout="total, prev, pager, next, jumper" :total="pages">
      </el-pagination>
    </div>
    <div v-if="!datum" class="nodatum">
      <div class="nodatum-img">
        <img src="./nodatum.png">
        <div>无数据</div>
      </div>
    </div>
  </div>
</template>
<style lang="scss" scoped>
.absencelist {
  margin: 20px;
  padding: 20px;
  background: #fff;
  .title {
    font-size: 14px;
    color: #2d2f33;
    margin-bottom: 20px;
  }
  .head,
  .content {
    display: flex;
    height: 40px;
    line-height: 40px;
    border-top: 1px solid #e5e9f2;
    background: #eff2f7;
    font-size: 12px;
    color: #2d2f33;
    .flex-one {
      flex: 1;
      padding-left: 10px;
      .text {
        float: left;
      }
      .sequencing {
        position: relative;
        display: inline-block;
        height: 100%;
        margin-left: 5px;
        color: #b4bccf;
        .up {
          position: absolute;
          top: -2px;
        }
        .down {
          position: absolute;
          top: 5px;
        }
      }
      .elect-up {
        .up {
          color: #4e90ff;
        }
      }
      .elect-down {
        .down {
          color: #4e90ff;
        }
      }
    }
    .left-line {
      border-left: 1px solid #ddd;
    }
  }
  .content {
    font-size: 13px;
    color: #676a6c;
    background: #fff;
  }
  .nodatum {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
    height: 80%;
    .nodatum-img {
      width: 8%;
      text-align: center;
      img {
        width: 100%;
        margin-bottom: 10px;
      }
    }
  }
  .pagination {
    margin-top: 30px;
    text-align: center;
  }
}
</style>
<script>
import Handlingstatus from 'com/handlingstatus/handlingstatus'
import SequencingList from "com/sequencinglist/sequencinglist"
import { formattingTime, buling } from '@/assets/js/formattingTime'
import { PRISON_API } from '../../api/api'

export default {
  name: "absencelist",
  components: {
    Handlingstatus,
    SequencingList
  },
  data() {
    return {
      listRows: 1,
      datum: false,
      pages: 0,
      warningsInfo: [],
      order: "desc",
      sort: "timestamp",
      current: 1,
      handlinginfo: {},
      statusshow: false,
      sequencinglist: [
        {
          text: "级别",
          flex: 1,
        },
        {
          text: "类型",
          flex: 1,
          line: true,
        },
        {
          text: "姓名",
          flex: 1,
          line: true,
        },
        {
          text: "囚号",
          flex: 1,
          line: true,
        },
        {
          text: "报警区域",
          flex: 1,
          line: true,
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
        },
        {
          text: "状态",
          flex: 1,
          line: true,
        },
        {
          text: "备注",
          flex: 1,
          line: true
        }
      ],
    };
  },
  methods: {
    setnum () {
      this.getData()
    },
    handling (event) {
      let e = event.target
      let i = e.getAttribute('data-index')
      this.handlinginfo = this.warningsInfo[i]
      this.statusshow=true
    },
    staalert (msg) {
      this.statusshow = msg
    },
    // 状态
    level(val) {
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
      this.getData(val);
    },
    // 排序更新数据
    getorder(msg) {
      this.order = msg;
      this.getData();
    },
    // 更新列表内容
    listContent(msg) {
      this.sort = msg;
    },
    // 数据请求
    getData(sort, val) {
      let v = val || this.current;
      let o = sort || this.sort;
      this.$http
        .Post(PRISON_API.alarm, {
          areaCode: this.$route.query.num,
          listRows: this.listRows,
          sort: this.sort,
          order: this.order,
          page: v
        })
        .then(res => {})
        .catch(rej => {
          if (rej.data.errcode === 10001) {
            this.warningsInfo = rej.data.result.warnings;
            this.pages = rej.data.totalRows;
            if (this.warningsInfo.length) {
              this.datum = true;
              this.warningsInfo.forEach(e => {
                e.state = this.state(e.state);
                e.level = this.level(e.level);
                e.timestamp = formattingTime(e.timestamp);
                e.finishTime = formattingTime(e.finishTime);
              });
            }
          }
        });
    }
  },
  mounted() {
    this.listRows =Math.floor((document.getElementsByClassName("absencelist")[0].clientHeight -24 -62) /40) - 2;
    this.getData();
  }
};
</script>