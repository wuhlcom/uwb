<template>
  <div class="absencelist">
    <div class="title vertical">缺勤人员列表</div>
    <div class="head">
      <sequencing-list
        :sequencinglist="sequencinglist"
        @sequencingClass="getorder"
        @sequencingContent="listContent">
      </sequencing-list>
    </div>
    <div v-if="datum" v-for="item in listInfo" :key="item.id" class="content">
      <span class="flex-one">{{item.code}}</span>
      <span class="flex-one">
        <router-link :to="{name: 'prisonerinfo', query: {num: item.code}}">
          {{ item.name }}
        </router-link>
      </span>
      <span class="flex-one">{{item.msg}}</span>
      <span class="flex-one">{{item.position}}</span>
      <span class="flex-one">{{item.timestamp}}</span>
      <span class="flex-one">{{item.finishTime}}</span>
    </div>
    <div v-if="datum" class="pagination">
      <el-pagination
        @current-change="handleCurrentChange"
        background 
        :page-size="this.listRows"
        layout="total, prev, pager, next, jumper"
        :total="total"
      ></el-pagination>
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
  .absencelist{
    margin: 20px; 
    padding: 20px;
    background: #fff;
    .title{
      font-size: 14px;
      color: #2d2f33;
      margin-bottom: 20px;
    }
    .head,.content{
      display: flex;
      height: 40px;
      line-height: 40px;
      border-top: 1px solid #e5e9f2;
      background: #eff2f7;
      font-size: 13px;
      color: #2d2f33;
      .flex-one{
        flex: 1;
        padding-left: 10px;
        font-size: 13px;
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
    .content{
      font-size: 12px;
      color: #676a6c;
      background: #fff;
    }
    .pagination{
      margin-top: 30px;
      text-align: center;
    }
    .nodatum{
      display: flex;
      align-items: center;
      justify-content: center;
      width: 100%;
      height: 80%;
      .nodatum-img{
        width: 8%;
        text-align: center;
        img{
          width: 100%;
          margin-bottom: 10px;
        }
      }
    }
  }
</style>
<script>
import SequencingList from "com/sequencinglist/sequencinglist"
import { formattingTime, buling } from '@/assets/js/formattingTime'
import { PRISON_API } from '../../api/api'

export default {
  name: 'absencelist',
  components: {
    SequencingList
  },
  data () {
    return {
      datum: false,
      listInfo: [],
      total: 0,
      currentPage: 1,
      order: 'desc',
      sort: 'timestamp',
      listRows: '',
      sequencinglist: [
        {
          text: "囚号",
          flex: 1,
        },
        {
          text: "姓名",
          flex: 1,
          line: true,
        },
        {
          text: "缺勤原因",
          flex: 1,
          line: true,
        },
        {
          text: "所属区域",
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
        }
      ]
    }
  },
  methods: {
    handleCurrentChange(val) {
      this.$http.Post(PRISON_API.absence, {areaCode: this.$route.query.num, "listRows": this.listRows, "page": val}).then(res => {

      }).catch(rej => {
        if (rej.data.errcode === 10001) {
          this.listInfo = rej.data.result.absence
          this.total = rej.data.totalRows
          if (this.listInfo.length) {
            this.datum = true
            this.listInfo.forEach(e => {
              e.timestamp = this.formattingTime(e.timestamp)
              e.finishTime = this.formattingTime(e.finishTime)
            })
          }
        }
      })
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
    getData (sort,val) {
      let v = val || this.current
      let o = sort || this.sort
      this.$http.Post('/prison/area/absence',{"areaCode": this.$route.query.num,"listRows": this.listRows, "sort": this.sort, "order": this.order, "page": v}).then(res => {
      }).catch(rej => {
        if (rej.data.errcode === 10001) {
          this.listInfo = rej.data.result.absence
          this.total = rej.data.totalRows
          if (this.listInfo.length) {
            this.datum = true
            this.listInfo.forEach(e => {
              e.timestamp = formattingTime(e.timestamp)
              e.finishTime = formattingTime(e.finishTime)
            })
          }
        }      
      })
    }
  },
  created () {
  },
  mounted () {
    // 计算列表显示区域高度，确定一页显示条数
    this.listRows = Math.floor((document.getElementsByClassName('absencelist')[0].clientHeight - 24 - 62) / 40) -2
    this.getData()
  },
}
</script>