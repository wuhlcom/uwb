<template>
  <div class="areas" ref="content">
    <div v-if="!addArea" class="head">
      <div class="search">
        <el-input placeholder="请输入名称" v-model="getDataObj.fenceName" @keyup.enter.native="getData" class="input-with-select">
          <el-button slot="append" icon="el-icon-search" @click="getData"></el-button>
        </el-input>
      </div>
      <div class="add-dle">
        <button @click="deleteFence">批量删除</button>
        <button @click="add">+增加区域</button>
      </div>
    </div>
    <div v-show="!addArea" ref="teble" class="teble">
      <el-table ref="multipleTable" :data="tableData" :style="{width: tebleWidth + 'px'}" @selection-change="handleSelectionChange">
        <el-table-column
          type="selection"
          width="55">
        </el-table-column>
        </el-table-column>
        <el-table-column type="index" label="序号" width="180">
        </el-table-column>
        <el-table-column prop="fenceName" label="名称">
        </el-table-column>
        <el-table-column prop="typeName" label="类型">
        </el-table-column>
        <el-table-column prop="remark" label="描述" show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <span @click="handleEdit(scope.row)">
              <i class="iconfont icon-bianji"></i>
            </span>
            <span @click="handleDelete(scope.row.fenceCode)">
              <i class="iconfont icon-lajitong"></i>
            </span>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <add-area @closes="addAreaClose" class="add-area" v-if="addArea" ref="addArea">
    </add-area>
    <modify-fence :modifyData="modifyData" v-if="modifyFence" @closemodify="modifyFenceClose"></modify-fence>
    <el-pagination
      v-show="!addArea"
      class="pagination"
      :page-size="getDataObj.listRows"
      background
      layout="total, prev, pager, next"
      @current-change="handleCurrentChange"
      :total="total">
    </el-pagination>
  </div>
</template>
<style lang="scss" scoped>
$color: #59b3ff;
.areas {
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
}
.icon-bianji {
  margin-right:58px;
}
</style>
<script>
import AddArea from "../../components/add-area";
import ModifyFence from '../../components/modify-fence'
export default {
  name: "areas",
  components: {
    AddArea,
    ModifyFence
  },
  data() {
    return {
      tableData: [],
      search: "",
      addArea: false,
      tebleWidth: 0,
      delArr: [],
      total: 0,
      modifyFence: false,
      modifyData: {},
      getDataObj: {
        page: 1,
        fenceName: '',
        listRows: 15
      }
    };
  },
  computed: {
    navShow() {
      return this.$store.state.nav.navShow;
    }
  },
  methods: {
    addAreaClose(msg) {
      this.addArea = msg;
      this.getData()
    },
    add() {
      this.$store.state.nav.navShow = false;
      this.addArea = true;
    },
    modify () {
      this.modifyFence = true
    },
    modifyFenceClose (msg) {
      this.modifyFence = msg
      this.getData()
    },
    handleEdit(row) {
      this.modifyData = row
      this.modify()
    },
    handleDelete(val) {
      this.delArr=[]
      this.delArr.push(val);
      this.deleteFence();
    },
    handleSelectionChange (val) {
      if (val.length) {
        this.delArr = []
        val.forEach(e => {
          this.delArr.push(e.fenceCode)
        })
      } else {
        this.delArr = []
      }
    },
    handleCurrentChange(val) {
      this.getDataObj.page = val;
      this.getData();
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
        .Post(this.$url.fences, this.getDataObj)
        .then(res => {})
        .catch(rej => {
          this.tableData = rej.data.result
          this.total = rej.data.totalRows;
          this.tableData.forEach(e => {
            if (e.type === 0) {
              e.typeName = '矩形区'
            }
          })
        });
    },
    deleteFence () {
      if (!this.delArr.length) {
        this.$message({
          type: "error",
          message: "请选择至少一区域!"
        });
      } else {
        this.$confirm("此操作将永久删除该项, 是否继续?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            this.$http.Post(this.$url.fenceDel,{
              fenceCodes: this.delArr
            }).then(res => {

            }).catch(rej => {
              this.delArr=[]
              if (rej.data.errcode === 10001) {
                this.getData()
                this.$message({
                  type: "success",
                  message: "删除成功!"
                })
              } else {
                this.$message({
                  type: "error",
                  message: rej.data.msg
                })
              }
            })
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
    getListRows () {
      let height = this.$refs.content.clientHeight - 150
      this.getDataObj.listRows =Math.floor(height/45)
      this.getData()
    },
    toggleSelection(rows) {
      if (rows) {
        rows.forEach(row => {
          this.$refs.multipleTable.toggleRowSelection(row);
        });
      } else {
        this.$refs.multipleTable.clearSelection();
      }
    },
    hint(msg) {
      this.$notify.error({
        title: "错误",
        message: msg,
        position: "bottom-right",
        duration: 2000
      });
    },
  },
  watch: {
    navShow() {
      this.getTebleWidth();
      if (this.addArea) {
        setTimeout(() => {
          this.$refs.addArea.setCanvasSize()
        }, 500);
      }
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
      this.getListRows()
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
  }
};
</script>
