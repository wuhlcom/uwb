<template>
  <div class="tactics" ref="content">
    <div v-if="!addTactics" class="head">
      <div class="search">
        <el-input
          placeholder="请输入策略名"
          v-model="getDataObj.strategyName"
          @keyup.enter.native="getData"
          class="input-with-select">
          <el-button
            slot="append"
            icon="el-icon-search"
            @click="getData">
          </el-button>
        </el-input>
      </div>
      <div class="add-dle">
        <button @click="delTactics">批量删除</button>
        <button @click="add">+新增策略</button>
      </div>
    </div>
    <div v-show="!addTactics" ref="teble" class="teble">
      <el-table :data="tableData" @selection-change="handleSelectionChange" :style="{width: tebleWidth + 'px'}" ref="multipleTable">
        <el-table-column type="selection" width="55">
        </el-table-column>
        <el-table-column type="index" label="序号">
        </el-table-column>
        <el-table-column prop="strategyName" label="策略名">
        </el-table-column>
        <el-table-column prop="fenceName" label="区域名">
        </el-table-column>
        <el-table-column prop="behavior" label="行为">
        </el-table-column>
        <el-table-column prop="strategyUser" label="关联用户">
        </el-table-column>
        <el-table-column prop="time" label="生效时间">
        </el-table-column>
        <el-table-column prop="levelName" label="告警级别">
        </el-table-column>
        <el-table-column label="操作" width="230">
          <template slot-scope="scope">
            <el-switch
            v-model="select[scope.$index].value"
            active-color="#13ce66"
            @change="open(scope.$index, scope.row)"
            active-text="启用"
            inactive-text="关闭"
            inactive-color="#ff4949">
            </el-switch>
            <span @click="handleEdit(scope.row)">
              <i class="iconfont icon-bianji"></i>
            </span>
            <span @click="handleDelete(scope.row)">
              <i class="iconfont icon-lajitong"></i>
            </span>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <add-tactics @closes="addClose" v-if="addTactics">
    </add-tactics>
    <modify-tactics @closeModify="modifyClose" :data="modifyData" v-if="modifyTactics"></modify-tactics>
    <el-pagination
      v-show="!addTactics"
      background
      @current-change="handleCurrentChange"
      :page-size="getDataObj.listRows"
      layout="total, prev, pager, next"
      :total="total"
      class="pagination">
    </el-pagination>
  </div>
</template>
<style lang="scss" scoped>
$color: #59b3ff;
.tactics {
  .head {
    display: flex;
    justify-content: space-between;
    height: 26px;
    margin-bottom: 19px;
    .search {
      width: 265px;
    }
    .add-dle {
      button {
        height: 26px;
        margin-left: 19px;
        padding: 0 20px;
        border: none;
        font-size: 14px;
        color: $color;
        background: #003585;
      }
    }
  }
  .teble {
    color: $color;
  }
  .add-area {
    width: 100%;
    height: 100%;
    margin: 0 auto;
  }
}
.iconfont {
  font-size: 24px;
  vertical-align: middle;
  margin-right: 10px;
}
</style>
<script>
import AddTactics from "../../components/add-tactics";
import ModifyTactics from '../../components/modify-tactics'
import { formattingTime } from "../../assets/js/formattingTime";
import {level,behavior} from "../../assets/js/strategy";
export default {
  name: "tactics-arrangement",
  components: {
    AddTactics,
    ModifyTactics
  },
  data() {
    return {
      tableData: [],
      search: "",
      addTactics: false,
      modifyTactics: false,
      tebleWidth: 0,
      getDataObj: {
        page: 1,
        listRows: 0,
        strategyName: ''
      },
      total: 0,
      select: [],
      delArr: [],
      modifyData: {}
    };
  },
  computed: {
    navShow() {
      return this.$store.state.nav.navShow;
    }
  },
  methods: {
    addClose(msg) {
      this.addTactics = msg;
      this.getData()
    },
    modifyClose(msg) {
      this.modifyTactics = msg
      this.getData()
    },
    add() {
      this.addTactics = true;
    },
    getTebleWidth() {
      setTimeout(() => {
        let windowWidth = document.body.clientWidth;
        // 根据左侧导航栏是否显示来判断是否需要减去310px的导航栏宽度
        if (this.$store.state.nav.navShow) {
          this.tebleWidth = document.body.clientWidth - 310 - 42 - 71;
        } else if (!this.$store.state.nav.navShow) {
          this.tebleWidth = document.body.clientWidth - 42 - 71;
        }
      }, 0);
    },
    getData() {
      this.$http
        .Post(this.$url.strategyList, this.getDataObj)
        .then(res => {})
        .catch(rej => {
          let data = rej.data;
          if (data.errcode === 10001) {
            this.tableData = data.result;
            this.total = data.totalRows;
            this.select=[]
            this.tableData.forEach(e => {
              e.levelName = level(e.level);
              e.behavior = behavior(e.forbidden)
              e.time = e.startTime+'至'+e.finishTime
              if (e.available=== 1) {
                this.select.push({ value: true });
              } else if (e.available=== 0) {
                this.select.push({ value: false });
              }
            });
          } else {
            this.$message({
              type: "error",
              message: data.msg
            });
          }
        });
    },
    open(index, row) {
      let num = this.select[index].value ? 1 : 0;
      this.$http
        .Post(this.$url.strategySwitch, {
          strategyCode: row.strategyCode,
          available: num
        })
        .then(res => {})
        .catch(rej => {
          this.getData();
        });
    },
    getListRows() {
      let height = this.$refs.content.clientHeight - 150;
      this.getDataObj.listRows = Math.floor(height / 45);
      this.getData();
    },
    handleEdit(row) {
      this.modifyData = row
      this.modifyTactics = true
    },
    handleDelete(val) {
      this.delArr.push(val.strategyCode)
      this.delTactics()
    },
    delTactics() {
      if (!this.delArr.length) {
        this.$message({
          type: "error",
          message: "请选择至少一策略!"
        });
      } else {
        this.$confirm("此操作将永久删除该项, 是否继续?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            this.$http
              .Post(this.$url.strategyDel, { strategyCodes: this.delArr })
              .then(res => {})
              .catch(rej => {
                this.delArr = [];
                if (rej.data.errcode === 10001) {
                  this.getData();
                  this.$message({
                    type: "success",
                    message: "删除成功!"
                  });
                } else {
                  this.$message({
                    type: "error",
                    message: rej.data.msg
                  });
                }
              });
          })
          .catch(() => {
            this.toggleSelection()
            this.delArr = [];
            this.$message({
              type: "info",
              message: "已取消删除"
            });
          });
      }
    },
    handleSelectionChange(val) {   
      if (val.length) {
        this.delArr = []
        val.forEach(e => {
          this.delArr.push(e.strategyCode);
        });
      } else {
        this.delArr = [];
      }
    },
    handleCurrentChange(val) {
      this.getDataObj.page = val;
      this.getData();
    },
    hint(msg) {
      this.$notify.error({
        title: "错误",
        message: msg,
        position: "bottom-right",
        duration: 2000
      });
    },
    toggleSelection(rows) {
      if (rows) {
        rows.forEach(row => {
          this.$refs.multipleTable.toggleRowSelection(row);
        });
      } else {
        this.$refs.multipleTable.clearSelection();
      }
    }
  },
  watch: {
    navShow() {
      this.getTebleWidth();
    }
  },
  mounted() {
    this.$nextTick(() => {
      /* 
      * 窗口宽度大于1366时调用getTebleWidth方法
      * 小于1366时将窗口的值固定在1366
      */
      if (document.body.clientWidth > 1366) {
        this.getTebleWidth();
      } else {
        // 根据左侧导航栏是否显示来判断是否需要减去310px的导航栏宽度
        if (this.$store.state.nav.navShow) {
          this.tebleWidth = 1366 - 310 - 42 - 71;
        } else {
          this.tebleWidth = 1366 - 42 - 71;
        }
      }
      this.getListRows();
    });
    window.onresize = () => {
      if (document.body.clientWidth > 1366) {
        this.getTebleWidth();
      } else {
        if (this.$store.state.nav.navShow) {
          this.tebleWidth = 1366 - 310 - 42 - 71;
        } else {
          this.tebleWidth = 1366 - 42 - 71;
        }
      }
    };
  },
  updated() {}
};
</script>
